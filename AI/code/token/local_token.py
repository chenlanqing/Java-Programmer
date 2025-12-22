# 安装依赖
# pip install tiktoken transformers

import tkinter as tk
from tkinter import ttk, scrolledtext
import tiktoken
import transformers
import threading
import os
import requests

class TokenCalculatorApp:
    def __init__(self, root):
        self.root = root
        root.title("大模型Token计算器")
        root.geometry("600x500")

        # 模型选择
        ttk.Label(root, text="选择模型:").pack(pady=5, anchor="w", padx=10)
        self.model_var = tk.StringVar(value="gpt-4")
        models = ["gpt-3.5-turbo", "gpt-4", "gpt-4o", "deepseek-chat", "claude-3-5-sonnet"]
        ttk.Combobox(root, textvariable=self.model_var, values=models, width=40).pack(pady=5, padx=10, anchor="w")

        # 输入文本
        ttk.Label(root, text="输入文本:").pack(pady=5, anchor="w", padx=10)
        self.text_input = scrolledtext.ScrolledText(root, height=10)
        self.text_input.pack(padx=10, pady=5, fill="both", expand=True)

        # 计算按钮
        ttk.Button(root, text="计算Token数量", command=self.calculate_tokens).pack(pady=10)

        # 结果显示
        ttk.Label(root, text="计算结果:").pack(pady=5, anchor="w", padx=10)
        self.result_var = tk.StringVar(value="")
        ttk.Label(root, textvariable=self.result_var, wraplength=580).pack(pady=5, padx=10, anchor="w")

        # 详细Token信息
        self.token_info = scrolledtext.ScrolledText(root, height=5)
        self.token_info.pack(padx=10, pady=5, fill="both")

        # 状态栏
        self.status_var = tk.StringVar(value="就绪")
        ttk.Label(root, textvariable=self.status_var, relief="sunken", anchor="w").pack(side="bottom", fill="x")

        # 预加载分词器
        self.tokenizers = {}
        threading.Thread(target=self.load_tokenizers).start()

    def load_tokenizers(self):
        self.status_var.set("正在加载分词器...")
        try:
            # 加载GPT分词器
            self.tokenizers["gpt"] = tiktoken.get_encoding("cl100k_base")

            # 尝试加载DeepSeek分词器（如果已下载）
            try:
                self.tokenizers["deepseek"] = transformers.AutoTokenizer.from_pretrained(
                    "deepseek-ai/deepseek-llm-7b-chat", trust_remote_code=True
                )
            except:
                pass

            self.status_var.set("分词器加载完成")
        except Exception as e:
            self.status_var.set(f"分词器加载失败: {str(e)}")

    def calculate_tokens(self):
        text = self.text_input.get("1.0", "end-1c")
        model = self.model_var.get()

        if not text.strip():
            self.result_var.set("请输入文本")
            return

        self.status_var.set(f"正在计算 {model} 的Token数量...")

        try:
            if model.startswith("gpt"):
                encoding = self.tokenizers.get("gpt") or tiktoken.encoding_for_model(model)
                tokens = encoding.encode(text)
                token_count = len(tokens)

                # 显示结果
                chinese_chars = sum(1 for char in text if '\u4e00' <= char <= '\u9fff')
                english_words = len([word for word in text.split() if all(c.isalpha() for c in word)])

                self.result_var.set(f"文本长度: {len(text)}字符 | 中文字符: {chinese_chars} | 英文单词: {english_words} | Token数量: {token_count}")
                self.token_info.delete("1.0", "end")
                self.token_info.insert("1.0", f"Token列表前20个: {tokens[:20]}...\n")
                self.token_info.insert("end", f"平均比率: 每个字符约 {round(token_count/len(text), 2)} 个Token")

            elif model.startswith("deepseek"):
                if "deepseek" in self.tokenizers:
                    tokenizer = self.tokenizers["deepseek"]
                    tokens = tokenizer.encode(text)
                    token_count = len(tokens)

                    # 显示结果
                    chinese_chars = sum(1 for char in text if '\u4e00' <= char <= '\u9fff')
                    english_words = len([word for word in text.split() if all(c.isalpha() for c in word)])

                    self.result_var.set(f"文本长度: {len(text)}字符 | 中文字符: {chinese_chars} | 英文单词: {english_words} | Token数量: {token_count}")
                    self.token_info.delete("1.0", "end")
                    self.token_info.insert("1.0", f"Token列表前20个: {tokens[:20]}...\n")
                    self.token_info.insert("end", f"平均比率: 每个字符约 {round(token_count/len(text), 2)} 个Token")
                else:
                    self.result_var.set("DeepSeek分词器未加载，使用估算方法")
                    # 使用GPT分词器估算
                    encoding = self.tokenizers.get("gpt") or tiktoken.get_encoding("cl100k_base")
                    tokens = encoding.encode(text)
                    token_count = len(tokens)

                    self.result_var.set(f"[估算结果] Token数量: 约 {token_count} 个 (使用GPT分词器估算)")

            elif model.startswith("claude"):
                # 使用GPT分词器估算Claude的Token数量
                encoding = self.tokenizers.get("gpt") or tiktoken.get_encoding("cl100k_base") 
                tokens = encoding.encode(text)
                token_count = len(tokens)

                self.result_var.set(f"[估算结果] Token数量: 约 {token_count} 个 (Claude使用GPT分词器估算)")
                self.token_info.delete("1.0", "end")
                self.token_info.insert("1.0", "注意: Claude模型的实际Token数可能与此估算结果略有差异\n")

            self.status_var.set("计算完成")

        except Exception as e:
            self.result_var.set(f"计算错误: {str(e)}")
            self.status_var.set("计算出错")

if __name__ == "__main__":
    root = tk.Tk()
    app = TokenCalculatorApp(root)
    root.mainloop()