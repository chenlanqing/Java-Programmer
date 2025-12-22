# 安装必要的库
# pip install anthropic

from anthropic import Anthropic

def estimate_claude_tokens(text):
    """估算Claude模型的token数量"""
    client = Anthropic()
    # 注意：此方法需要API密钥，仅用于演示
    # 在实际使用中，您可以使用自己的API密钥
    token_count = client.count_tokens(text)
    return token_count

# 由于Claude官方没有提供独立的分词器库，
# 上述方法需要API密钥并会产生API调用
# 如果只是估算，可以使用tiktoken作为近似
def estimate_claude_tokens_approx(text):
    """使用tiktoken近似估算Claude模型的token数量"""
    import tiktoken
    encoding = tiktoken.get_encoding("cl100k_base")
    tokens = encoding.encode(text)
    return len(tokens)