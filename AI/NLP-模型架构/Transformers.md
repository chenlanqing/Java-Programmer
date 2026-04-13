# 前置知识

## 神经网络

神经网络（Neural Network）本质是一个参数化函数，用于学习输入到输出之间的映射关系

**整体结构**：
```text
输入层 → 隐藏层（多层）→ 输出层
```
* 输入层：接收特征
* 隐藏层：进行特征变换（核心计算）
* 输出层：给出预测结果

**基本计算单元（神经元）**：每个神经元本质是：

```text
y = 激活函数(加权求和)
```
展开：
```text
y = f(w1x1 + w2x2 + ... + wn*xn + b)
```

### 组成部分

1. 权重（Weights）
* 表示输入的重要性
* 可训练参数
2. 偏置（Bias）
* 调整整体输出位置
* 提供灵活性
3. 激活函数（Activation Function）
* 引入非线性能力
* 常见：
  * ReLU
  * Sigmoid
  * Tanh

### 两种典型结构

#### 前馈神经网络（Feedforward Neural Network, FNN）

特点：
* 信息单向流动
* 无记忆

结构：
```text
输入 → 隐藏层 → 输出
```
适用：
* 表格数据
* 图像分类（基础）

#### 循环神经网络（Recurrent Neural Network, RNN）

**核心原理与公式:**

其核心是，RNN单元在每个时间步执行相同的计算，但其内部状态根据输入序列而变化。如果有一个输入序列 $x = (x_1, x_2, ..., x_T)$，RNN在时间步 $t$ 更新其隐藏状态 $h_t$，使用当前输入 $x_t$ 和前一个隐藏状态 $h_{t-1}$。这种更新的常见公式是：

$$h_t = \tanh(W_{hh}h_{t-1} + W_{xh}x_t + b_h)$$

- $h_t$ 是时间步 $t$ 的新隐藏状态。
- $h_{t-1}$ 是前一个时间步的隐藏状态（$h_0$ 通常初始化为零）。
- $x_t$ 是时间步 $t$ 的输入向量（vector）。
- $W_{hh}$ 和 $W_{xh}$ 分别是隐藏到隐藏连接和输入到隐藏连接的权重（weight）矩阵。这些权重在所有时间步中是共享的，这是RNN学习序列模式的根本。
- $b_h$ 是一个偏置（bias）向量。
- $\tanh$ 是一种常见的激活函数（activation function）（双曲正切），引入非线性。

RNN 还可以选择在每个时间步生成输出 $y_t$，通常根据隐藏状态计算：

$$y_t = W_{hy}h_t + b_y$$

这里 $W_{hy}$ 是隐藏到输出的权重矩阵，$b_y$ 是输出偏置。是否在每一步都需要输出取决于具体的任务（例如，在每一步预测下一个词，或对整个序列进行分类）。

## 梯度下降

本质：**让模型一点点变聪明的方法**

可以理解为：
> 在“错误的山坡”上往最低点走

**往“下降最快”的方向走一点点**

关键直觉
* 梯度 = 当前最陡的方向
* 学习率 = 步子大小

## 反向传播

本质：**告诉模型“哪里错了”**

核心作用
* 计算每个参数的“责任大小”
* 给梯度下降提供方向

## 激活函数

本质：**让模型具备“非线性能力”**

如果没有激活函数：
- 整个神经网络 = 一个线性函数
- 再多层也没用（等价于一层）

激活函数 = **“开关 / 弯曲器”**
* 决定哪些信息通过
* 让模型可以拟合复杂关系

### Sigmoid函数

输出范围：0 ~ 1

像一个“概率压缩器”：
* 很小 → 接近 0
* 很大 → 接近 1

常用于：
* 二分类问题（概率）

缺点：
* 容易“饱和”（梯度消失）
* 深层网络效果差

### ReLU 函数

规则很简单：
```text
小于0 → 变成0
大于0 → 原样输出
```
优点
* 计算简单
* 收敛快
* 解决梯度消失（部分）

缺点
* 可能“神经元死亡”（一直输出0）

### Softmax 函数

本质：**把一组数变成“概率分布”**

所有值：
* 都 ≥ 0
* 总和 = 1

使用场景：
* 多分类问题（比如词预测）
* Transformer 输出层核心组件

### Tanh 函数

输出范围：-1 ~ 1

- 比 Sigmoid 更适合深层网络
- 仍然会梯度消失

### Silu 函数

现代模型（如 Transformer）常用

## LayerNorm-层归一化

本质：**让每一层的输出“稳定”**

训练神经网络时：
* 每一层输出分布会变化
* 导致训练不稳定

LayerNorm 做的事：
```text
把数据拉回“标准范围”
```
* 对**每一个样本内部**做归一化
* 不依赖 batch（适合 Transformer）

## RMSNorm

**LayerNorm 的简化版本**

LayerNorm：
* 减均值 + 除方差

RMSNorm：
* 不减均值
* 只做“尺度归一化”

## 总结

这些组件在 Transformer 里的作用：
```text
输入
 ↓
线性变换
 ↓
激活函数（增强表达）
 ↓
归一化（LayerNorm / RMSNorm）
 ↓
反向传播 + 梯度下降（训练）
```

| 概念        | 在 Transformer 中的位置 | 作用     |
| --------- | ------------------ | ------ |
| 梯度下降      | 训练阶段               | 更新所有参数 |
| 反向传播      | 训练阶段               | 计算梯度   |
| 激活函数      | FFN 中              | 提升表达能力 |
| Sigmoid   | 激活函数的一种            | 早期常用   |
| ReLU      | 激活函数               | 简单高效   |
| Tanh      | 激活函数               | 对称性好   |
| SiLU      | ⭐FFN常用             | 更平滑、更强 |
| Softmax   | Attention / 输出层    | 变成概率分布 |
| LayerNorm | 每个子层后              | 稳定训练   |
| RMSNorm   | 替代 LayerNorm       | 更高效    |


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