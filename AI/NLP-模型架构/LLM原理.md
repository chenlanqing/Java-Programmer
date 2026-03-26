
## Transformers

参考：[Transformers](./Transformers.md)

# Token

- [Tokenizers摘要](https://huggingface.co/docs/transformers/zh/tokenizer_summary)
- [Token 计算器](https://github.com/dqbd/tiktokenizer)-https://tiktokenizer.vercel.app/
- [OpenAI Token 计算](https://platform.openai.com/tokenizer)
- [Tiktoken is a fast BPE tokeniser for use with OpenAI's models](https://github.com/openai/tiktoken)
- [AI 模型 Token 计算器](https://github.com/soulteary/ai-token-calculator)
- [TOON 是一种为 LLM 输入而生的紧凑序列化格式，减少 Token](https://github.com/toon-format/toon)
- [大模型分词器（Tokenizer）总结](https://github.com/luhengshiwo/LLMForEverybody/blob/main/01-第一章-预训练/全网最全的大模型分词器（Tokenizer）总结.md)

计算机不像人类那样理解词语和句子。它们处理的是数字。因此，让机器处理语言的首要障碍是将文本转换为模型可以实际操作的数字形式。这个过程包含两个主要步骤：
- 将文本分解为易于处理的小块，称作分词 (tokenization)；
- 然后将这些分词表示为数字列表，称作嵌入 (embedding)；

Token 是使用 Tokenizer（翻译为分词器）分词后的结果，Tokenizer 是什么呢？Tokenizer 是将文本分割成token的工具。

## 分词方式

在大模型中，Tokenizer有三种常见的分词方式：基于字符（Char-Based）、基于单词（Word-Based）、基于子词（Sub-Word-Based）

### 基于单词（Word-Based）

就是将文本按照空格或者标点分割成单词

英文分词示例 1：
```bash
Let's do some NLP tasks.
# 按照空格分词后，得到的token是：
Let's, do, some, NLP, tasks.
# 按照标点分词后，得到的token是：
Let, ', s, do, some, NLP, tasks, .
```
中文分词示例
> 我们来做一个自然语言处理任务  

一个可能的分词结果是  
>我们，来，做，一个，自然语言处理，任务。

这种分词方式的优点是简单易懂，缺点是`无法处理未登录词（Out-of-Vocabulary，简称OOV）`；[jieba 分词](https://github.com/fxsjy/jieba)就是基于这种分词方式的；

**缺点：**
- 词汇表规模大: 由于不同语言和应用场景中的词汇量非常庞大，Word-based Tokenizer 需要一个非常大的词汇表。这不仅增加了存储和计算的开销，还会导致训练和推理效率下降。此外，在实际应用中，许多单词是低频词。如果词汇表中包含大量低频词，会导致稀疏数据问题。
- 难以处理形态变化灵活的单词: 许多语言具有丰富的形态变化 (如英语的复数形式、时态变化，法语的性别变化等) 。Word-based Tokenizer需要包含所有这些形态变化形式，导致词汇表进一步膨胀。此外，不同形式的同一词语被视为不同的词元，导致模型无法有效共享这些词语的语义信息。
- 词汇表覆盖范围有限，无法处理未登录词 (Out-of-Vocabulary Words): Word-based Tokenizer需要一个固定的词汇表。如果遇到词汇表中没有的新词或拼写错误的词，它们将无法处理。这导致Tokenizer无法处理这些未登录词，影响其泛化能力；

### 基于字符（Char-Based）

对英文来说是将文本按照字母级别分割成token。这样的好处是
- 词汇量要小得多；
- OOV要少得多，因为每个单词都可以从字符构建。

对中文来说是将文本按照字级别分割成token

这种方法也不是完美的。基于字符而不是单词，从直觉上讲意义不大：在英文中每个字母本身并没有多大意义，单词才有意义。然而在中文中，每个字比拉丁语言中的字母包含更多的信息。

另外，我们的模型最终会处理大量的token：使用基于单词（word）的标记器(tokenizer)，单词只会是单个标记，但当转换为字母/字（character）时，它很容易变成 10 个或更多的标记(token)。

### 基于子词（Sub-Word-Based）

基于子词（subword）的 tokenization 算法依赖于这样一个原则：常用词不应被分解为更小的子词，但罕见词应被分解为有意义的子词；

Sub-Word 分词方式使模型相对合理的词汇量（不会太多也不会太少），同时能够学习有意义的与上下文无关的表示形式（另外，该分词方式通过将模型分解成已知的子词，使模型能够处理以前从未见过的词（oov问题得到了很大程度上的缓解）

#### BPE-Byte Pair Encoding

Byte Pair Encoding (BPE) 是一种源于数据压缩领域的子词分割技术，BPE的核心思想：是通过迭代地合并出现频率最高的字节对，逐步构建子词单元，从而实现文本的分段，[该方法最早由Philip Gage在1994年提出](https://arxiv.org/pdf/1209.1045)，用于文件压缩，随后被Sennrich等人在2015年引入到机器翻译中3，以应对词汇表过大和稀疏性问题；它被许多 Transformer 模型使用，包括 GPT、GPT-2、RoBERTa、BART 和 DeBERTa



#### WordPiece

#### Unigram Language Model

## Token 计算方法

- [如何计算 Token](https://help.apiyi.com/ai-api-local-token-calculation-guide.html)

## Token 相关问题

> 问题1：token 为什么不能是单词？因为单次作为 token 有两个缺点：
- 数量太大了，语义有重叠，每个单词还有不同的时态，词表一旦变大，模型的训练难度就会加大；
- 构建完词表后，如果出现了一个新的词，就会超出词表的范围，就会被被标记为 unload；

而子词可以通过多个 token来拼接新的词；

> 问题2：token 为什么不能是字符？
- 虽然词表变少了，但是字母本身并没有很强的语意信息，增加了模型训练的难度；

# 参考资料

- [最全面的 LLM 架构技术解析](https://magazine.sebastianraschka.com/p/the-big-llm-architecture-comparison)
- [从零开始理解LLM内部原理](https://mp.weixin.qq.com/s/6zKOgq7oYmUVQcEshNFUAA)
- [从零开始理解LLM内部原理-英文版](https://towardsdatascience.com/understanding-llms-from-scratch-using-middle-school-math-e602d27ec876/)