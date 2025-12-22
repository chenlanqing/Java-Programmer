# 安装必要的库
# pip install tiktoken

import tiktoken

def count_gpt_tokens(text, model="gpt-4"):
    """计算GPT系列模型的token数量"""
    try:
        encoding = tiktoken.encoding_for_model(model)
    except KeyError:
        encoding = tiktoken.get_encoding("cl100k_base")  # 默认使用GPT-4的编码

    tokens = encoding.encode(text)
    return len(tokens), tokens

# 测试
text = "你好，这是一个Token计算示例。Hello World!"
model = "gpt-4"  # 可选: gpt-3.5-turbo, gpt-4, gpt-4o
count, tokens = count_gpt_tokens(text, model)
print(f"模型: {model}")
print(f"文本: {text}")
print(f"Token数量: {count}")
print(f"Token列表: {tokens}")