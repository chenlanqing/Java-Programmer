# 概述

# Transformer 模型结构

## 核心思想

## 整体结构

完整的Transformer架构包含多个组成部分，但为了高层理解，我们可以将其简化为两个主要部分：
- 编码器（Encoder）： 这部分读取输入文本。它利用自注意力 (self-attention)机制 (attention mechanism)，同时处理（或者说，以一种考虑所有词的方式）所有输入词，并为每个词构建丰富的表示（嵌入 (embedding)），这些表示融入了整个输入序列的上下文 (context)。
- 解码器（Decoder）： 这部分一次生成一个词元 (token)作为输出文本。它也使用自注意力机制来考虑已生成的词语。更重要的是，它也关注编码器生成的上下文表示。这确保了输出与输入提示相关，并在生成更多文本时保持连贯性

## 编码器

### 自注意力机制

自注意力机制（Self-Attention）是一种让每个词根据上下文来动态调整它的自身表示（即向量）的机制


## 解码器


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