# 安装必要的库
# pip install transformers

import transformers

# 方式1：从本地目录加载分词器
def count_tokens_from_local(text, tokenizer_dir="./"):
    tokenizer = transformers.AutoTokenizer.from_pretrained(
        tokenizer_dir, trust_remote_code=True
    )
    tokens = tokenizer.encode(text)
    return len(tokens), tokens

# 方式2：从Hugging Face下载官方分词器
def count_tokens_from_hf(text):
    tokenizer = transformers.AutoTokenizer.from_pretrained(
        "deepseek-ai/deepseek-llm-7b-chat", trust_remote_code=True
    )
    tokens = tokenizer.encode(text)
    return len(tokens), tokens

# 测试
text = "你好，这是一个Token计算示例。Hello World!"
count, tokens = count_tokens_from_hf(text)
print(f"文本: {text}")
print(f"Token数量: {count}")
print(f"Token列表: {tokens}")