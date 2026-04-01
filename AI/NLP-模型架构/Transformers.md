# 前置知识

## 神经网络

## 梯度下降

## 反向传播

## 激活函数

### Sigmoid函数

### ReLU 函数

### Softmax 函数

### Tanh 函数

### Silu 函数

## LayerNorm

## RMSNorm

# Transformer 模型结构

## 核心思想

Transformer架构的核心，即注意力机制的原理：从文本的上下文中找到需要注意的关键信息，帮助模型理解每个字的正确含义。

## 整体结构

完整的Transformer架构包含多个组成部分，但为了高层理解，我们可以将其简化为两个主要部分：
- 编码器（Encoder）： 这部分读取输入文本。它利用自注意力 (self-attention)机制 (attention mechanism)，同时处理（或者说，以一种考虑所有词的方式）所有输入词，并为每个词构建丰富的表示（嵌入 (embedding)），这些表示融入了整个输入序列的上下文 (context)。
- 解码器（Decoder）： 这部分一次生成一个词元 (token)作为输出文本。它也使用自注意力机制来考虑已生成的词语。更重要的是，它也关注编码器生成的上下文表示。这确保了输出与输入提示相关，并在生成更多文本时保持连贯性

# 注意力机制

- [An Intuition for Attention](https://jaykmody.com/blog/attention-intuition/)

## 什么是注意力机制

注意力机制最先源于计算机视觉领域，其核心思想：当我们关注一张图片，我们往往无需看清楚全部内容而仅将注意力集中在重点部分即可。在自然语言处理领域，也可以通过将重点注意力集中在一个或几个 token，从而取得更高效高质的计算效果;

注意力机制“真正解决了什么问题”：解决“信息太多，该看谁”的问题

注意力机制有三个核心变量：Query（查询值）、Key（键值）和 Value（真值），具体而言，注意力机制的特点是通过计算 Query 与Key的相关性为真值加权求和，从而拟合序列中每个词同其他词的相关关系

## 注意力机制公式

$$
Attention(Q,K,V) = softmax(\frac{QK^T}{\sqrt{d_k}})V
$$

- $QK^T$ —算"谁和谁有关系"：Q 和 K 做点积。

**人话：** 让每个词和其他所有词"握手"，看看谁和谁关系好。  
例子：
```
"饿了" 和 "冰箱" 握手 → 关系强 → 分数高
"饿了" 和 "小明" 握手 → 关系弱 → 分数低
```
- $\frac{}{\sqrt{d_k}}$ —缩放，防止数值爆炸，点积可能很大，除一下压住。

**人话：** 就像考试打分，满分1000分太夸张，按比例缩到100分。

- softmax：变成百分比，把分数转成 0~1 的概率，所有加起来等于1。

**人话：** 把"关系好坏"转成"注意力分配比例"。
```
"饿了" → 0.6 (60% 关注)
"冰箱" → 0.3 (30% 关注)  
"小明" → 0.1 (10% 关注)
```
- $*V$ — 按比例混合内容

用上面的百分比，对 V 加权求和。
**人话：** 最终输出 = 60%"饿了"的内容 + 30%"冰箱"的内容 + 10%"小明"的内容。

**"用 Q 找 K，算匹配度 → 变成百分比 → 按这个比例混合 V"**

最简化 Python 代码实现：
```py
'''注意力计算函数'''
def attention(query, key, value, dropout=None):
    '''
    args:
    query: 查询值矩阵
    key: 键值矩阵
    value: 真值矩阵
    '''
    # 获取键向量的维度，键向量的维度和值向量的维度相同
    d_k = query.size(-1) 
    # 计算Q与K的内积并除以根号dk
    # transpose——相当于转置
    scores = torch.matmul(query, key.transpose(-2, -1)) / math.sqrt(d_k)
    # Softmax
    p_attn = scores.softmax(dim=-1)
    if dropout is not None:
        p_attn = dropout(p_attn)
        # 采样
     # 根据计算结果对value进行加权求和
    return torch.matmul(p_attn, value), p_attn
```

## Self-Attention

自注意力，即是计算本身序列中每个元素对其他元素的注意力分布，即在计算过程中，Q、K、V 都由同一个输入通过不同的参数矩阵计算得到。在 Encoder 中，Q、K、V 分别是输入对参数矩阵 $W_q、W_k、W_v$ 做积得到，从而拟合输入语句中每一个 token 对其他所有 token 的关系。

通过自注意力机制，我们可以找到一段文本中每一个 token 与其他所有 token 的相关关系大小，从而建模文本之间的依赖关系。​在代码中的实现，self-attention 机制其实是通过给 Q、K、V 的输入传入同一个参数实现的：
```py
# attention 为上文定义的注意力计算函数
attention(x, x, x)
```

# 编码器



# 解码器


# 参考资料

- [transformer](https://en.wikipedia.org/wiki/Transformer_(deep_learning_architecture))
- [Transformers](https://github.com/huggingface/transformers)
- [Transformers Lab](https://github.com/transformerlab)
- [How transformer architecture works](https://www.datacamp.com/tutorial/how-transformers-work)
- [transformer 模型详解](https://zhuanlan.zhihu.com/p/338817680)
- [深入理解Transformer技术原理](https://tech.dewu.com/article?id=109)
- [transformer 整体指南](https://luxiangdong.com/2023/09/10/trans/)
- [LLM底层秘密—Transformer原理解析](https://mp.weixin.qq.com/s/x2aixxjfGJvA_epMR9mu2Q)
- [Attention Is All You Need](https://arxiv.org/pdf/1706.03762)
- [Transformers 快速入门](https://transformers.run/)
- [The Illustrated Transformer](https://jalammar.github.io/illustrated-transformer/)