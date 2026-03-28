
## Transformers

参考：[Transformers](./Transformers.md)

# Tokenizer

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

## 概念

Tokenizer（分词器）可以将原始文本（raw text）转换为模型能够理解的数字序列，在模型输入和输出的两个主要阶段中发挥重要作用

### 模型输入（编码 Encode）阶段

1、**分词（Tokenize）**

将文本拆分为词元（Token），常见的分词方式包括字级、词级、子词级（如 BPE、WordPiece）、空格分词等。
```
输入: "你好"
分词: ["你", "好"]
```
2、**映射（Mapping）**

将每个词元映射为词汇表中的唯一 ID，生成的数字序列即为模型的输入。
```
分词: ["你", "好"]
映射: [1001, 1002]
```

### 模型输出（解码 Decode）阶段

1、**反映射（De-mapping）**

模型输出的数字序列通过词汇表映射回对应的词元，二者是一一对应的关系。
```
输出: [1001, 1002]
反映射: ["你", "好"]
```
2、**文本重组**

将解码后的词元以某种规则重新拼接为完整文本。
```
反映射: ["你", "好"]
重组: "你好"
```

## Tokenizer 基础属性

### 查看词汇表大小

```python
# 获取词汇表大小
vocab_size = tokenizer.vocab_size
print(vocab_size) # 50257
```

### 查看词汇表

```python
print("Vocabulary:", tokenizer.vocab)
# 输出
{'ĠNaturally': 30413,
 'Ġinteractive': 14333,
 'ĠPlays': 38116,
 'hemer': 39557,
 ...}
```

### 查看特定 Token 和 ID 的映射关系

```python
# 查看特定 Token 的 ID
token_id = tokenizer.convert_tokens_to_ids('world')
print(token_id) # 6894

# 查看特定 ID 对应的 Token
token = tokenizer.convert_ids_to_tokens(995)
print(token) # Ġworld
```
> 说明：这里的 `Ġ` 代表一个空格字符：
```py
print(tokenizer.tokenize(' ')) # `['Ġ']`
```

### 查看特殊标记（Special Tokens）

一些分词器需要使用特殊标记来表示句子的开始、结束、填充等。
```python
# 查看所有特殊标记
special_tokens = tokenizer.all_special_tokens
print("All Special Tokens:", special_tokens) # ['<|endoftext|>']

# 查看特殊标记对应的 ID
special_token_ids = tokenizer.all_special_ids
print("Special Token IDs:", special_token_ids) # [50256]
```
GPT-2 只有一个特殊标记 `<|endoftext|>`，用于表示文本的结束

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
- 词汇表覆盖范围有限，无法处理未登录词 (Out-of-Vocabulary Words-不在词汇表中的词): Word-based Tokenizer需要一个固定的词汇表。如果遇到词汇表中没有的新词或拼写错误的词，它们将无法处理。这导致Tokenizer无法处理这些未登录词，影响其泛化能力；

### 基于字符（Char-Based）

对英文来说是将文本按照字母级别分割成token。这样的好处是
- 词汇量要小得多；
- OOV要少得多，因为每个单词都可以从字符构建。

对中文来说是将文本按照字级别分割成token

这种方法也不是完美的。基于字符而不是单词，从直觉上讲意义不大：在英文中每个字母本身并没有多大意义，单词才有意义。然而在中文中，每个字比拉丁语言中的字母包含更多的信息。

另外，我们的模型最终会处理大量的token：使用基于单词（word）的标记器(tokenizer)，单词只会是单个标记，但当转换为字母/字（character）时，它很容易变成 10 个或更多的标记(token)。

### 基于子词（Sub-Word-Based）

- [21. BPE vs WordPiece：理解 Tokenizer 的工作原理与子词分割方法.md](https://github.com/Hoper-J/AI-Guide-and-Demos-zh_CN/blob/master/Guide/21.%20BPE%20vs%20WordPiece：理解%20Tokenizer%20的工作原理与子词分割方法.md)

基于子词（subword）的 tokenization 算法依赖于这样一个原则：常用词不应被分解为更小的子词，但罕见词应被分解为有意义的子词；

Sub-Word 分词方式使模型相对合理的词汇量（不会太多也不会太少），同时能够学习有意义的与上下文无关的表示形式（另外，该分词方式通过将模型分解成已知的子词，使模型能够处理以前从未见过的词（oov问题得到了很大程度上的缓解）

#### BPE-Byte Pair Encoding

Byte Pair Encoding (BPE) 是一种源于数据压缩领域的子词分割技术，BPE的核心思想：是通过迭代地合并出现频率最高的字节对，逐步构建子词单元，从而实现文本的分段，[该方法最早由Philip Gage在1994年提出](https://arxiv.org/pdf/1209.1045)，用于文件压缩，随后被Sennrich等人在2015年引入到机器翻译中3，以应对词汇表过大和稀疏性问题；它被许多 Transformer 模型使用，包括 GPT、GPT-2、RoBERTa、BART 和 DeBERTa

BPE 每次的迭代目标是找到频率最高的相邻字符对，定义 Score 以与 WordPiece 作对比：
$$
\text{Score}_{\text{BPE}}(x, y) = \text{freq}(x, y)
$$
其中, $\text{freq}(x, y)$ 表示字符对 $(x, y)$ 在语料库中的出现频次。

##### [BPE的训练步骤](https://apxml.com/zh/courses/how-to-build-a-large-language-model/chapter-5-tokenization-large-vocabularies/byte-pair-encoding-bpe)

1、初始化
- 设定目标最终词汇表大小 $V_{target}$。
- 将训练语料库中的每个词表示为字符序列，并添加一个特殊的词尾符号（通常是 `</w>` 或 `</w>`）以区分词 (tokenization) 边界。例如，“lower”变成 `l`、`o`、`w`、`e`、`r`、`</w>`。
- 初始词汇表包含语料库中所有独有的字符。  

2、迭代
重复以下步骤，直至达到目标词汇表大小 $V_{target}$ 或已执行预设数量的合并操作：
- **统计词对**：在语料库当前表示中，找出所有相邻符号对（字符或已合并的子词 (subword)），并统计它们的频率。
- **找出最频繁词对**：选择出现频率最高的词对 $(s_1, s_2)$。
- **合并**：创建一个新符号 $s_{12}$ 来表示合并后的词对。将 $s_{12}$ 添加到词汇表。
- **更新语料库表示**：将语料库表示中所有相邻词对 $(s_1, s_2)$ 的出现替换为新符号 $s_{12}$。记录此合并操作（例如：“将 $s_1$ 和 $s_2$ 合并为 $s_{12}$”）

**示例：** 假设有一个小语料库，词频如下：`{'low': 5, 'lower': 2, 'newest': 6, 'widest': 3}`

步骤 0: 初始化
- 将词表示为字符序列 + `</w>`：
  - `low`: `l o w </w>` (出现 5 次)
  - `lower`: `l o w e r </w>` (出现 2 次)
  - `newest`: `n e w e s t </w>` (出现 6 次)
  - `widest`: `w i d e s t </w>` (出现 3 次)
- 初始词汇表 (vocabulary): `{l, o, w, </w>, e, r, n, s, t, i, d}`，大小为 11
- 目标词汇表大小: 14

步骤 1: 统计词对并合并
- 统计整个语料库中的相邻词对（考虑频率）：
  - `(l, o)`: \(5 + 2 = 7\)
  - `(o, w)`: \(5 + 2 = 7\)
  - `(w, </w>)`: \(5\)
  - `(w, e)`: \(2 + 6 = 8\)
  - `(e, r)`: \(2\)
  - `(r, </w>)`: \(2\)
  - `(n, e)`: \(6\)
  - `(e, w)`: \(6\)
  - `(e, s)`: \(6 + 3 = 9\) <- 最频繁
  - `(s, t)`: \(6 + 3 = 9\) <- 频率相同（我们选择 `(e, s)`）
  - `(t, </w>)`: \(6 + 3 = 9\) <- 频率相同
  - `(w, i)`: \(3\)
  - `(i, d)`: \(3\)
  - `(d, e)`: \(3\)
- 将 `(e, s)` 合并成新符号 `es`。词汇表：`{'t', 'o', 'w', 'r', '</w>', 'es', 'n', 'l', 'd', 's', 'i', 'e'}`，大小为 12；
- 更新语料库：
  - `l o w </w>` (5)
  - `l o w e r </w>` (2)
  - `n e w es t </w>` (6)
  - `w i d es t </w>` (3)
- 记录合并: `e + s -> es`

步骤 2: 统计词对并合并
- 在更新后的语料库中重新计算词对频率：
  - `(l, o)`: 7
  - `(o, w)`: 7
  - `(w, </w>)`: 5
  - `(w, e)`: \(2 + 6 = 8\)
  - `(e, r)`: 2
  - `(r, </w>)`: 2
  - `(n, e)`: 6
  - `(e, w)`: 6
  - `(w, es)`: 6
  - `(es, t)`: \(6 + 3 = 9\) <- 最频繁（或与 `(t, </w>)` 频率相同）
  - `(t, </w>)`: \(6 + 3 = 9\)
  - `(w, i)`: 3
  - `(i, d)`: 3
  - `(d, es)`: 3
- 将 `(es, t)` 合并成新符号 `est`。
- 词汇表为：`{'t', 'o', 'w', 'est', 'r', '</w>', 'es', 'n', 'l', 'd', 's', 'i', 'e'}`，大小为：13
- 更新语料库：
  - `l o w </w>` (5)
  - `l o w e r </w>` (2)
  - `n e w est </w>` (6)
  - `w i d est </w>` (3)
- 记录合并: `es + t -> est`

步骤 3: 统计词对并合并
- 重新计算频率：
  - ...
  - `(est, </w>)`: \(6 + 3 = 9\) <- 最频繁（如果 `est` 在第 2 步中未被选中，则与 `(t, </w>)` 频率相同）
  - ...
- 将 `(est, </w>)` 合并成 `est</w>`。
- 词汇表为：`{'t', 'o', 'w', 'est', 'est</w>', 'r', '</w>', 'es', 'n', 'l', 'd', 's', 'i', 'e'}`，大小为 14
- 更新语料库：
  - `l o w </w>` (5)
  - `l o w e r </w>` (2)
  - `n e w est</w>` (6)
  - `w i d est</w>` (3)
- 记录合并: `est + </w> -> est</w>`

此过程持续进行。如果我们执行更多合并，像 `(l, o)`、`(o, w)`、`(w, e)` 这样的词对也可能被合并，潜在地形成 `low` 或 `we`。最终词汇表将包含单个字符和常见的多个字符子词 (subword)，例如 `es`、`est`、`est</w>` 等，这些都是根据它们在训练数据中的频率形成的。

##### 基本代码实现

[BPE_Implement](./code/bpe_algorithm.py)

#### WordPiece

WordPiece 是一种子词分割算法，最初用于处理日语和韩语的语音搜索，后来在 Google 的神经机器翻译系统中得到应用；与BPE 一样，WordPiece 也是从包含模型使用的特殊 tokens 和初始字母表的小词汇表开始的。由于它是通过添加前缀（如 BERT 中的 ## ）来识别子词的，每个词最初都会通过在词内部所有字符前添加该前缀进行分割

与 BPE 不同，WordPiece 的 Score 由字符对频次与其组成部分频次的比值决定，定义 Score：
$$
\text{Score}_{\text{WordPiece}}(x, y) = \frac{\text{freq}(xy)}{\text{freq}(x) \times \text{freq}(y)}
$$

其中, $\text{freq}(x)$, $\text{freq}(y)$ 和 $\text{freq}(xy)$ 分别表示符号 $x$, $y$ 和它们合并后的符号 $xy$ 的频次。

##### 训练步骤

1. **初始化词汇表 $V$**：
   - 与 BPE 相同, $V$ 包含语料库中的所有唯一字符，但处理方式略有不同：对于每个单词，除了首个字符外，其他字符前都加上 `##` 前缀。
2. **统计字符对的频次及 Score**：
   - 对于每个可能的字符对 $(x, y)$，计算 $\text{freq}(x)$, $\text{freq}(y)$, $\text{freq}(xy)$，并计算 Score。
3. **找到 Score 最高的字符对并合并**：
   - 选择 Score 最高的字符对 $(x, y)$，将其合并为新符号 $xy$，注意：
     - 如果第二个符号以 `##` 开头，合并时去掉 `##` 前缀再进行连接。
     - 新符号是否以 `##` 开头，取决于第一个符号是否以 `##` 开头。
4. **更新词汇表并重复步骤 2 到 4**：
   - 将新符号添加到词汇表 $V = V \cup \{xy\}$。
   - 更新语料库中的单词表示，重复统计和合并过程，直到满足停止条件。

示例：使用与 BPE 示例相同的语料库

**步骤 1：初始化词汇表**

- **将单词拆分为字符序列**：
  ```plaintext
  ('l', '##o', '##w'), 5                       # "low"
  ('l', '##o', '##w', '##e', '##r'), 2         # "lower"
  ('n', '##e', '##w', '##e', '##s', '##t'), 6  # "newest"
  ('w', '##i', '##d', '##e', '##s', '##t'), 3  # "widest"
  ```
- **词汇表 $V$**：
  ```plaintext
  {'l', '##o', '##w', '##e', '##r', 'n', '##s', '##t', 'w', '##i', '##d'}
  ```

**步骤 2：统计字符和字符对的频次，计算 Score**
可以设计一个函数完成这个步骤（直接运行查看输出）：
```python
from collections import defaultdict
def count_char_pairs_wordpiece(word_freq):
    """
    计算字符对的频次和单个字符的频次。
    参数：
        word_freq: List of tuples, 每个元组包含单词（列表形式）和其频次
    返回：
        两个字典，分别为字符对频次和单个字符频次
    """
    pair_freq = defaultdict(int)
    char_freq = defaultdict(int)
    for word, freq in word_freq:
        for i in range(len(word)):
            char_freq[word[i]] += freq
            if i < len(word) - 1:
                pair = (word[i], word[i + 1])
                pair_freq[pair] += freq
    return pair_freq, char_freq

def compute_wordpiece_score(freq_xy, freq_x, freq_y):
    """
    根据 WordPiece 的定义计算 Score。
    参数：
        freq_xy: 符号对的频次
        freq_x: 符号 x 的频次
        freq_y: 符号 y 的频次
    返回：
        计算得到的 Score
    """
    if freq_x == 0 or freq_y == 0:
        return 0
    return freq_xy / (freq_x * freq_y)

# 示例词汇表和单词频次
word_freq = [
    (['l', '##o', '##w'], 5),
    (['l', '##o', '##w', '##e', '##r'], 2),
    (['n', '##e', '##w', '##e', '##s', '##t'], 6),
    (['w', '##i', '##d', '##e', '##s', '##t'], 3)
]
# 统计字符对频次和单个字符频次
pair_freq, char_freq = count_char_pairs_wordpiece(word_freq)
# 计算每对字符的 Score
scores = {}
for pair in pair_freq:
    freq_xy = pair_freq[pair]
    freq_x = char_freq[pair[0]]
    freq_y = char_freq[pair[1]]
    score = compute_wordpiece_score(freq_xy, freq_x, freq_y)
    scores[pair] = score

# 输出结果
print("字符对频次统计结果:")
for pair, freq in pair_freq.items():
    print(f"{pair}: {freq}")

print("\n单个字符频次统计结果:")
for char, freq in char_freq.items():
    print(f"{char}: {freq}")

print("\n字符对 Score 计算结果:")
for pair, score in scores.items():
    print(f"{pair}: {score:.4f}")
```

**输出**：
```python
字符对频次统计结果:
('l', '##o'): 7
('##o', '##w'): 7
('##w', '##e'): 8
('##e', '##r'): 2
('n', '##e'): 6
('##e', '##w'): 6
('##e', '##s'): 9
('##s', '##t'): 9
('w', '##i'): 3
('##i', '##d'): 3
('##d', '##e'): 3

单个字符频次统计结果:
l: 7
##o: 7
##w: 13
##e: 17
##r: 2
n: 6
##s: 9
##t: 9
w: 3
##i: 3
##d: 3

字符对 Score 计算结果:
('l', '##o'): 0.1429
('##o', '##w'): 0.0769
('##w', '##e'): 0.0362
('##e', '##r'): 0.0588
('n', '##e'): 0.0588
('##e', '##w'): 0.0271
('##e', '##s'): 0.0588
('##s', '##t'): 0.1111
('w', '##i'): 0.3333
('##i', '##d'): 0.3333
('##d', '##e'): 0.0588
```
- **选择频次最高的字符对**：
  -  `('w', '##i')` 和 `('##i', '##d')`，Score 都为 0.3333。可以任选其一进行合并，假设选择排序第一的： `("w", "##i")`。
- **合并 `('w', '##i')` 为新符号 `wi`**
  - 注意：合并时，若第二个符号以 `##` 开头，合并后的新符号为第一个符号加上第二个符号去掉 `##` 前缀的部分。
- **记录合并操作：**
  ```plaintext
  Merge 1: ('w', '##i') -> 'wi'
  ```

**步骤 4：更新词汇表并重复**
- **更新词汇表 $V$**：
  ```plaintext
  {'l', '##o', '##w', '##e', '##r', 'n', '##s', '##t', 'w', '##i', '##d', 'wi'}
  ```
- **更新单词序列**：
  ```plaintext
  ('l', '##o', '##w'), 5                       # "low"
  ('l', '##o', '##w', '##e', '##r'), 2         # "lower"
  ('n', '##e', '##w', '##e', '##s', '##t'), 6  # "newest"
  ('wi', '##d', '##e', '##s', '##t'), 3        # "widest"
  ```
- **重复步骤 2 到 4，直到达到预定的词汇表大小**。

##### 基本代码实现

[Word Piece](./code/word_piece_algorithm.py)

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