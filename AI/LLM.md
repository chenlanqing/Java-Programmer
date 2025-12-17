# 一、大模型基础

- [OpenAI文档](https://platform.openai.com/docs/concepts)
- [大模型理论基础](https://datawhalechina.github.io/so-large-lm/#/)
- [大模型基础](https://github.com/ZJU-LLMs/Foundations-of-LLMs)
- [从零开始的大语言模型原理与实践教程](https://github.com/datawhalechina/happy-llm)
- [LLM基础、模型构建和应用部署](https://github.com/mlabonne/llm-course)
- [大模型相关技术原理以及实战经验](https://github.com/liguodongiot/llm-action)
- [从头开始构建的轻量级 vLLM 实现](https://github.com/GeeeekExplorer/nano-vllm)
- [论文：Foundation of Large Language Models](https://arxiv.org/pdf/2501.09223)

## 1、基本概念

### 1.1、什么是大模型

大语言模型是一种人工智能模型，通常使用深度学习技术，比如神经网络，来理解和生成人类语言。这些模型的“大”在于它们的参数数量非常多，可以达到数十亿甚至更多，这使得它们能够理解和生成高度复杂的语言模式；

可以**将大语言模型想象成一个巨大的预测机器，其训练过程主要基于“猜词”**：给定一段文本的开头，它的任务就是预测下一个词是什么。模型会根据大量的训练数据（例如在互联网上爬取的文本），试图理解词语和词组在语言中的用法和含义，以及它们如何组合形成意义。它会通过不断地学习和调整参数，使得自己的预测越来越准确；

LangChain 是一个全方位的、基于大语言模型这种预测能力的应用开发工具，它的灵活性和模块化特性使得处理语言模型变得极其简便。不论你在何时何地，都能利用它流畅地调用语言模型，并基于语言模型的“预测”或者说“推理”能力开发新的应用；

### 1.2、LLM应用场景

### 1.3、主流大模型

**国际知名**
- Meta: Llama系列
- OpenAI: GPT系列
- Google: Gemini
- Anthropic: Claude系列

**国产主流**
- 深度求索: DeepSeek
- 阿里: QWen系列
- 百度: 文心大模型
- 智谱清言: GLM系列


## 2、架构原理

### 2.1、token

- [Token 计算器](https://github.com/dqbd/tiktokenizer)
- [TOON 是一种为 LLM 输入而生的紧凑序列化格式，减少 Token](https://github.com/toon-format/toon)

LLM 把要预测的每一个字成为 token，现在 LLM 都是按照 token 来计费的，所有的 token 成为 vocab 词表；生成 token 的方式成为分词（Tokenizer）
- BPE 分词法

token 是介于单词和字母之间的一个子词，语料中出现最频繁的部分，可以减少 token 的数量；

> 问题1：token 为什么不能是单词？因为单次作为 token 有两个缺点：
- 数量太大了，语义有重叠，每个单词还有不同的时态，词表一旦变大，模型的训练难度就会加大；
- 构建完词表后，如果出现了一个新的词，就会超出词表的范围，就会被被标记为 unload；

而子词可以通过多个 token来拼接新的词；

> 问题1：token 为什么不能是字符？
- 虽然词表变少了，但是字母本身并没有很强的语意信息，增加了模型训练的难度；

### 2.2、大模型技术原理

主要技术架构是基于 [transformer](https://en.wikipedia.org/wiki/Transformer_(deep_learning_architecture))

大模型推理过程：加载分词算法 -> 加载模型参数 -> 推理生成答案

### 2.3、[控制LLM输出的随机性的参数](./AI应用.md#4大模型设置)

大语言模型预测下一个token时会先输出所有token的概率值，有不同的方法来控制选择哪一个token作为输出，主要以下4个参数
- `温度（Temperature）`: 起到平滑调整概率的作用，temperature=1时，原始概率保持不变，temperature<1时，原来概率大的会变得更大（概率集中效果），temperature>1时,概率值越平均
- `Top-K`: 模型输出是在概率在top-k的范围里随机选择一个，K值越大，选择范围越广，生成的文本越多样；K值越小，选择范围越窄，生成的文本越趋向于高概率的词。 k=1就是直接选择最高概率的token输出
- `Top-p`: 通过累积概率来限定范围，top-p=0.5表示随机采样的范围是概率在前50%的tokens， top-p选择的tokens数是动态的
- `max-tokens`: max-tokens参数指定了模型在停止生成之前可以生成的最大token（或词）数量

```py
import torch
import torch.nn.functional as F
import numpy as np
# numpy 与 torch 如果版本冲突，需要将 numpy 降级为：1.26.4
inputs = np.array([[2.0, 1.0, 0.1]])
def calculate(temperature):
    logits = torch.tensor(inputs / temperature)
    softmax_scores = F.softmax(logits, dim=1)
    print(f"temperature = {temperature} {softmax_scores.cpu().numpy()}")
if __name__ == "__main__":
    calculate(1.0)
    calculate(0.1)
    calculate(10)
```

## 3、Transformer 架构

- [How transformer architecture works](https://www.datacamp.com/tutorial/how-transformers-work)
- [transformer 模型详解](https://zhuanlan.zhihu.com/p/338817680)
- [深入理解Transformer技术原理](https://tech.dewu.com/article?id=109)
- [transformer 整体指南](https://luxiangdong.com/2023/09/10/trans/)

## 4、大模型调用

### 4.1、CPU 与 GPU

- [The difference between CPU and GPU](https://www.intel.com/content/www/us/en/products/docs/processors/cpu-vs-gpu.html)

**CPU（Central Processing Unit），即中央处理器**；是电脑、手机等众多电子产品的“心脏”。它主要负责执行程序指令、进行算术和逻辑运算以及控制和协调计算机各个部件。为了满足处理各种不同数据的强大通用性能，CPU 的内部结构设计非常复杂。CPU 由多个核心组成，每个核心又包含算术逻辑单元、控制单元和高速缓存等组件，并且可以独立地执行任务。至今为止，所有的 CPU 都遵循冯·诺依曼体系结构的基本工作流程：取指令，指令译码，执行指令，数据回写，然后再取下一个指令、译码、执行、回写，重复进行直到程序结束。通过这种工作流程，CPU 能够有效地执行程序，并控制整个系统的运行；

**GPU（Graphics Processing Unit），即图形处理器**，顾名思义，一种专门用来处理图形和图像计算的处理器。GPU 最初是为图形渲染和显示而设计的，用于加速计算机中图像的处理，例如在视频游戏、电影渲染、图形设计等方面。它只有少量的控制单元和缓存单元，绝大部分的空间用来堆放运算单元，主要负责完成许多计算密集型任务

CPU 和 GPU 之间存在显著差异，是因为它们各自针对不同的目标和需求来设计，具体体现在：
- CPU 需要有强大的通用性，以处理各种不同类型的数据，同时需要进行逻辑判断，包括大量的分支跳转和中断处理，这导致内部结构异常复杂。
- GPU 主要面向类型高度统一、相互无依赖的大规模数据，并在纯净的计算环境中执行，因此不需要处理复杂的逻辑操作。

这就导致了 CPU 和 GPU 呈现出非常不同的架构：

![](image/CPU-vs-GPU.png)

CPU 拥有较大的缓存单元以及复杂的逻辑控制单元，相比之下计算能力只是 CPU 很小的一部分。而 GPU 则拥有数量众多的计算单元和超长的流水线，但只有非常简单的逻辑控制以及较小的缓存单元。

**设计理念**：CPU 和 GPU 的设计理念也截然不同。
- 首先，**CPU 是基于低延迟（Low Latency）设计的**：
    - 强大的运算单元：CPU 拥有数量较少但是单个计算性能更强的运算单元，可以减少操作延时，更快地响应。
    - 大容量缓存：将一部分数据存储到高速缓存当中，使得高延迟的内存访问转换为低延迟的缓存访问。
    - 复杂的控制单元：分支预测（Branch Prediction）机制可以降低分支延时；数据转发（Data Forwarding）机制降低数据延时。
- **GPU 则是基于高通量（High Throughput）设计的**：
    - 精简的运算单元：GPU 拥有大量的运算单元，虽然单个单元的性能比不上 CPU，但可以支持非常多的线程（Thread）从而达到非常大的吞吐量。
    - 小容量缓存：与 CPU 不同，GPU 缓存的目的并不是用来存储后面需要访问的数据，而是为线程提供服务，如果有很多线程需要访问同一个相同的数据，缓存会合并这些访问，然后再去访问内存。
    - 简单的控制单元：GPU 的控制单元没有分支预测和数据转发机制。

> CPU 更擅长一次处理一项任务，而 GPU 则可以同时处理多项任务。

### 4.2、大模型文件

![](image/大模型文件.png)

### 4.3、通过 GPU 调用LLM

如何查看 GPU：`nvidia-smi`，看机器上的nvidia显卡
- 驱动
- cuda
- 显卡型号
- 显存
- 利用率

<details>
    <summary>命令：nvidia-smi 输出</summary>

```
Fri Oct  4 16:04:01 2024       
+-----------------------------------------------------------------------------+
| NVIDIA-SMI 450.57       Driver Version: 450.57       CUDA Version: 11.0     |
|-------------------------------+----------------------+----------------------+
| GPU  Name        Persistence-M| Bus-Id        Disp.A | Volatile Uncorr. ECC |
| Fan  Temp  Perf  Pwr:Usage/Cap|         Memory-Usage | GPU-Util  Compute M. |
|                               |                      |               MIG M. |
|===============================+======================+======================|
|   0  TITAN RTX           Off  | 00000000:04:00.0 Off |                  N/A |
| 40%   28C    P8     1W / 280W |  13896MiB / 24220MiB |      0%      Default |
|                               |                      |                  N/A |
+-------------------------------+----------------------+----------------------+
|   1  TITAN RTX           Off  | 00000000:08:00.0 Off |                  N/A |
| 41%   26C    P8    15W / 280W |  11586MiB / 24220MiB |      0%      Default |
|                               |                      |                  N/A |
+-------------------------------+----------------------+----------------------+
|   2  TITAN RTX           Off  | 00000000:0C:00.0 Off |                  N/A |
| 41%   25C    P8    12W / 280W |  23315MiB / 24220MiB |      0%      Default |
|                               |                      |                  N/A |
+-------------------------------+----------------------+----------------------+
|   3  TITAN RTX           Off  | 00000000:0F:00.0 Off |                  N/A |
| 41%   26C    P8    14W / 280W |  14865MiB / 24220MiB |      0%      Default |
|                               |                      |                  N/A |
+-------------------------------+----------------------+----------------------+
                                                                               
+-----------------------------------------------------------------------------+
| Processes:                                                                  |
|  GPU   GI   CI        PID   Type   Process name                  GPU Memory |
|        ID   ID                                                   Usage      |
|=============================================================================|
|    0   N/A  N/A      4784      C   ...a_v11/ollama_llama_server    11161MiB |
|    0   N/A  N/A      6508      C   ...onda3/envs/llm/bin/python     2025MiB |
|    0   N/A  N/A     36275      C   ...on-webui/venv/bin/python3      707MiB |
|    1   N/A  N/A      4784      C   ...a_v11/ollama_llama_server    11583MiB |
|    2   N/A  N/A      4784      C   ...a_v11/ollama_llama_server    11161MiB |
|    2   N/A  N/A     50936      C   ...onda3/envs/llm/bin/python    12151MiB |
|    3   N/A  N/A      4784      C   ...a_v11/ollama_llama_server    11647MiB |
|    3   N/A  N/A     36275      C   ...on-webui/venv/bin/python3     3215MiB |
+-----------------------------------------------------------------------------+
```

</details>

（1）设置使用的 GPU，不指定默认全部
```py
os.environ["CUDA_VISIBLE_DEVICES"] = '0'
```
（2）加载分词器 tokenizer
```py
tokenizer = AutoTokenizer.from_pretrained(model_path, trust_remote_code=True)
```
（3）加载模型
```py
model = AutoModel.from_pretrained(model_path, device_map='auto', trust_remote_code=True)
```
（4）推理
```py
model.chat(
    tokenizer=tokenizer,
    prompt='你好',
    history=[],
    temperature=0.1,
    top_p=0.8,
    top_k=20
)
```

### 4.4、本地部署 CPU 调用 LLM

![](image/大模型部署方式区别.png)

可以使用 ollama 工具
- 安装大模型
- openai 接口
```py
from openai import OpenAI
client =  OpenAI(
    base_url='http://localhost:11434/v1',
    api_key='qwen2:1.5b'
)
chat_completion = client.chat.completions.create(
    messages=[
        {'role': 'user', 'content': '你好'}
    ],
    model='qwen2:1.5b'
)
print(chat_completion.choices[0].message.content)
```

### 4.5、AI 厂商 API 调用大模型

```py
from openai import OpenAI
client =  OpenAI(
    base_url='https://api.deepseek.com/v1',
    api_key='<deepseek api key>'
)
chat_completion = client.chat.completions.create(
    messages=[
        {'role': 'system', 'content': '你是一个有用的小助手'},
        {'role': 'user', 'content': '你好'}
    ],
    model='qwen2:1.5b'
)
print(chat_completion.choices[0].message.content)
```

## 5、推理模型

- [LLM推理优化技术](https://mp.weixin.qq.com/s/KRUfF4r1_e3I32FzSSlypg)
- [深入理解 vLLM](https://www.aleksagordic.com/blog/vllm)
- [一文梳理主流大模型推理部署框架：vLLM、SGLang、TensorRT-LLM、ollama、XInference](https://mp.weixin.qq.com/s/Fsaz7PAUSiKizl_lw-KSeg)
- [LLM 推理/微调优化的灵活框架](https://github.com/kvcache-ai/ktransformers)
- [LMCache 是一个 LLM 服务引擎扩展，用于减少 TTFT 并提高吞吐量](https://github.com/LMCache/LMCache)

llama.cpp 是一个模型推理框架，采用纯 C/C++ 实现，无需依赖 PyTorch、TensorFlow 等重型框架，通过静态编译生成单一可执行文件，在资源受限环境中展现出独特优势。

**量化技术**：在深度神经网络模型的开发流程中，结构设计完成后，训练阶段的核心任务是通过大量数据调整模型的**权重参数**。这些权重通常以浮点数的形式存储，常见的精度包括 16 位（FP16）、32 位（FP32）和 64 位（FP64）。训练过程通常依赖 GPU 的强大算力来加速计算，但这也带来了较高的硬件需求。为了降低这些需求，量化技术应运而生。

**量化的原理**：概括来说就是通过降低权重参数的精度，减少模型对计算资源和存储空间的要求，从而使其能够在更多设备上运行

llama.cpp 的量化实现依赖于作者 Georgi Gerganov 开发的另一个库——ggml。ggml 是一个用 C/C++ 实现的机器学习库，专注于高效处理神经网络中的核心数据结构——**张量（tensor）**。

张量是多维数组的泛化形式，广泛用于 TensorFlow、PyTorch 等主流深度学习框架中。通过改用 C/C++ 实现，ggml 不仅支持更广泛的硬件平台，还显著提升了计算效率。这种高效的设计为 llama.cpp 的诞生提供了坚实的基础，使其能够在资源受限的环境中实现高性能的模型推理。

量化技术的核心在于权衡精度与效率。通过降低权重参数的精度，模型的计算量和存储需求大幅减少，但同时也可能引入一定的精度损失。因此，量化算法的设计需要在压缩率和模型性能之间找到最佳平衡点。

## 6、大模型本地部署

- [GPT4All：在任何设备上运行本地 LLM](https://github.com/nomic-ai/gpt4all)
- [Ollama-运行大模型框架](https://ollama.com/)
- [Xinference-模型服务变得简单](https://github.com/xorbitsai/inference)
- [huggingface-开源大模型](https://huggingface.co/)
- [ollama-基本使用](https://github.com/datawhalechina/handy-ollama)
- [Shimmy: The 5MB Alternative to Ollama](https://github.com/Michael-A-Kuykendall/shimmy)
- [开源大模型食用（部署）指南](https://github.com/datawhalechina/self-llm)
- [ModelScope是阿里巴巴推出的开源模型即服务（MaaS）平台](https://www.modelscope.cn/home)
- [huggingface: 机器学习（ML）和数据科学平台及社区](https://huggingface.co/)
- [大模型部署电脑配置要求](https://www.zhihu.com/question/628771017)
- [AutoDL部署 Deepseek](https://zhuanlan.zhihu.com/p/23213698282)
- [EXO-分布式 AI 集群](https://github.com/exo-explore/exo)
- [计算 LLM 推理所需的 GPU 内存](https://selfhostllm.org/)
- [llama.cpp](https://github.com/ggml-org/llama.cpp)
- [如何拥有一个无限制、可联网、带本地知识库的私人 DeepSeek](https://mp.weixin.qq.com/s/qeKrwJXz_QJE6eNwUOhjsA)

### 6.1、显卡需求和推荐模型

使用 int4 + QLora 训练（效果并不会比Full-finetuning差多少，lora rank设置较大且应用到所有层）8K 上下文时，显存16GB的显卡则可以训练 20B 以下模型，显存24GB的显卡则可以训练 32B（含）以下模型。

推荐微调 1-7B 模型。推荐性价比显卡：4090，计算能力和显存带宽都足够好，比L40等商业卡还好用。

### 6.2. 服务商

- [google：免费提供 T4 等显卡，显存16GB](https://colab.research.google.com/)
- [新用户赠送 100h GPU 时间（16G、24G显卡）](https://modelscope.cn/)
- [每日签到可以领取免费 GPU 时间，但是限制框架只能使用 Paddle](https://aistudio.baidu.com/)
- [注册送 50元，4090显卡 2元/小时](https://console.ebcloud.com/)
- [显卡型号众多（适合测试模型在不同显卡上的性能），4090 2-3元/小时](https://www.autodl.com/)
- https://www.suanlix.cn/
- https://www.runpod.io/

## 7、大模型评测

- [LMArena 是由 LMSYS 组织的大众盲测竞技场。用户输入问题，两个匿名模型回答，用户凭感觉选哪个好](https://lmarena.ai/leaderboard)
- [GPQA Diamond (Graduate-Level Google-Proof Q&A) 是一套由领域专家编写的、Google 搜不到答案的博士级难题](https://www.vals.ai/benchmarks/gpqa)

### 7.1、如何分辨大模型的优劣

模型大小对模型能力的影响：[涌现能力](https://arxiv.org/pdf/2206.07682)，什么是涌现能力？在较小的模型中不出现，而在较大的模型中出现的能力；
主要表现在：
- 突破规模的临界点后，表现大幅度提升；
- 对某些 prompt 策略对小模型失效，而对大模型起作用

涌现能力是指在AI模型规模达到特定阈值后突然显现出来的新的能力，这些能力在较小规模的模型中无法观察到或预测。当模型参数量、训练数据量或计算量超过某个临界点时，模型会突然展现出新的认知能力，如多步推理、代码理解、创意写作等。这种现象表明AI能力的发展并非线性增长，而是存在质的飞跃节点。涌现并非AI独有的现象，而是自然界普遍存在的规律。当某一物质达到一定规模后，会涌现出单一物质不具备的新能力，这一现象被称为涌现现象

### 7.2、大模型评测

- [评估大模型的指标、框架和数据集](https://github.com/openai/evals)
- [MMLU-Massive Multitask Language Understanding](https://arxiv.org/abs/2406.01574)
- [FlagEval-大模型评测平台](https://flageval.baai.ac.cn/#/home)
- [Opencompass-司南-大模型评测体系](https://opencompass.org.cn/home)

主要评测步骤：
- 维度：评测哪些能力；
- 数据：在什么数据上评测；
- 指标：如何判断评测结果好坏；

## 8、专业LLM

- [Kronos 是首个面向金融市场的解读 K 线图基础模型](https://github.com/shiyu-coder/Kronos)

## 9、LLM安全

- [专注于大型语言模型 （LLM） 的安全相关论文、文章和资源的精选列表](https://github.com/ydyjya/Awesome-LLM-Safety)

# 二、模型微调

- [论文：从基础到突破微调LLM的终极指南](https://arxiv.org/pdf/2408.13296v1)
- [The Comprehensive Guide to Fine-tuning LLM](https://medium.com/data-science-collective/comprehensive-guide-to-fine-tuning-llm-4a8fd4d0e0af)
- [LLM 推理/微调优化的灵活框架](https://github.com/kvcache-ai/ktransformers)
- [用于预训练和微调 AI 模型的深度学习框架](https://github.com/Lightning-AI/pytorch-lightning)
- [SWIFT（用于微调的可扩展轻量级基础设施）](https://github.com/modelscope/ms-swift)
- [Claude to Fine-Tune an Open Source LLM](https://huggingface.co/blog/hf-skills-training)

## 1、模型微调基础

### 1.1、模型微调的概念

微调是采用预训练模型并在特定领域数据集上进一步训练它的过程。Transformer 允许访问适合各种任务的大量预训练模型。微调这些模型是提高模型以更高的准确性执行特定任务（例如情感分析、问答或文档摘要）的能力的关键步骤

**长文本、RAG、微调**

| 对比维度 | 长文本处理 | 知识库 | 微调 |
|---------|-----------|--------|------|
| **核心目标** | 理解和生成长篇内容 | 提供背景知识，增强回答能力 | 优化模型在特定任务或领域的表现 |
| **优点** | 连贯性强，适合复杂任务 | 灵活性高，可随时更新 | 性能提升，定制化强 |
| **缺点** | 资源消耗大，上下文限制 | 依赖检索，实时性要求高 | 需要标注数据，硬件要求高 |
| **适用场景** | 写作助手、阅读理解 | 智能客服、问答系统 | 专业领域、特定任务、风格定制 |
| **额外数据** | 不需要，但可能需要优化上下文长度 | 需要知识库数据 | 需要特定领域的标注数据 |
| **重新训练** | 不需要，但可能需要优化模型 | 不需要，只需更新知识库 | 需要对模型进行进一步训练 |
| **技术实现** | 扩大上下文窗口 | 检索+生成（RAG） | 调整模型参数 |
| **数据依赖** | 无需额外数据 | 依赖结构化知识库 | 需要大量标注数据 |
| **实时性** | 静态（依赖输入内容） | 动态（知识库可随时更新） | 静态（训练后固定） |
| **资源消耗** | 高（长文本计算成本高） | 中（需维护检索系统） | 高（训练算力需求大） |
| **灵活性** | 中（适合单次长内容分析） | 高（可扩展多知识库） | 低（需重新训练适应变化） |

为什么要做模型微调？通用大模型在特定领域或任务表现可能不佳，微调可实现领域专业化、适配不同任务、纠偏能力，还能保障数据安全，且成本效率高于从头训练，故需模型微调。相比长文本、知识库，微调能深度内化专业知识与任务逻辑，无需依赖实时检索或长文本处理，定制化强，显著提升特定场景性能，且数据安全可控，成本效率更高

### 1.2、数据工程

- 数据采集与清洗
- 数据标注与增强
- 数据集划分

### 1.3、微调的核心流程

- 选择预训练模型
- 数据准备与清洗：选择高质量的数据集
- 微调技术要点：设置超参数、选择合适的训练方法
- 模型评估与验证：确保微调后模型的效果

### 1.4、微调框架的选择

- pyTorch框架
  - 张量的创建、索引、运算等操作
  - 搭建神经网络，定义模型结构、前向传播、反向传播的流程
  - 案例：基于 PyTorch 的模型构建与训练之手写数字识别
- HuggingFace Transformers工具
- unsloth框架
  - unsloth 的开箱可用与高度可定制化
- LLaMA-Factory框架
- DeepSpeed

**Unsloth 与 LLaMA Factory对比**
- Unsloth 适合:
    - **资源受限场景**: 由于其出色的内存优化能力和快速的微调速度，非常适合在资源受限的环境中使用，如个人开发者使用消费级 GPU 进行大模型微调，或者企业在低成本硬件上进行模型实验和开发。
    - **快速迭代场景**: 对于需要快速迭代模型的场景，如学术研究中的实验验证、企业的快速原型开发等，Unsloth 的高效微调能力可以帮助用户在更短的时间内获得微调后的模型，加快项目进度。
- LLaMA Factory 适合:
    - **通用场景**: 由于其支持多种模型和训练算法，适用于各种通用的大语言模型训练和微调场景，无论是文本生成、情感分析还是机器翻译等任务，都可以使用 LLaMA Factory 进行模型的定制化训练。
    - **企业级应用**: 提供了 API Server 和一站式 WebUI Board，方便企业进行模型的管理和部署。
    - **零代码**: 适合不会写代码或代码基础比较弱的同学快速上手进行微调。

## 2、大模型训练技术

- [MLX LM 是一个 Python 包，用于使用 MLX 在 Apple 芯片上生成文本和微调大型语言模型](https://github.com/ml-explore/mlx-lm)
- [QLoRA：量化 LLM 的高效微调](https://github.com/artidoro/qlora)
- [unsloth:模型微调框架](https://github.com/unslothai/unsloth)
- [LLama-factory:整合了主流的各种高效训练微调技术](https://github.com/hiyouga/LLaMA-Factory) 
- [MS-Swift:魔搭社区提供的大模型与多模态大模型微调部署框架](https://github.com/modelscope/ms-swift) 
- Axolotl，YAML 配置驱动，可复现性强注重工程化和实验对比的团队
- DeepSpeed，ZeRO 分布式训练，支持万亿参数拥有大规模集群的企业和顶尖研究机构
- [Minimind-2小时完全从0训练26M的小参数GPT](https://github.com/jingyaogong/minimind)
- [Colab 是一个基于云端的编程环境](https://colab.google/)
- [LMFlow : 一个可扩展、方便和高效的工具箱，用于微调大型机器学习模型。支持所有 Decoder 模型的微调](https://github.com/OptimalScale/LMFlow)
- [FastChat 是一个开放平台，用于训练、服务和评估基于大型语言模型的聊天机器人](https://github.com/lm-sys/FastChat)
- [PEFT:参数高效微调工具库。支持 LoRA、Prefix Tuning、P-Tuning、Prompt Tuning、AdaLoRA等方法。](https://github.com/huggingface/peft)
- [LLaMA-Adapter](https://github.com/OpenGVLab/LLaMA-Adapter)
- [BELLE: 基于 LLaMA 的中文模型及微调训练工具库。](https://github.com/LianjiaTech/BELLE)
- [Linly: 基于 LLaMA 使用中文增量预训练以及微调训练的中文模型。](https://github.com/CVI-SZU/Linly)
- [Chinese-LLaMA-Alpaca: 基于 LLaMA 使用中文增量预训练以及微调训练的中文模型](https://github.com/ymcui/Chinese-LLaMA-Alpaca)

### 2.1、分布式训练

- 数据并行与模型并行
- 梯度累积与同步
- DeepSeed分布式训练/Llama Factory/Xtuner

### 2.2、混合精度训练

- FP32与FP16混合使用
- 动态损失缩放

### 2.3、模型压缩与加速

- 剪枝技术
- 量化技术
- 知识蒸馏

## 3、微调技术与应用

### 3.1、微调策略

- 基于预训练模型的微调
- 基于特定数据集进行模型微调，包括数据准备、参数设置、训练过程
- 解决微调过程中过拟合、训练不收敛等常见问题的方法

### 3.2、轻量化微调技术详解

- Prompt Tuning、P-Tuning、Prefix Tuning
- LoRA、QLoRA

## 4、预训练模型

### 4.1、模型选择

在微调前，选择一个合适的基座模型非常重要

| 分类 | 标识 | 含义 | 示例 (模型名称) |
|------|------|------|----------------|
| **功能与任务类型** | -Base | 基础模型，未经过特定任务微调，提供原始能力（用于二次开发）。 | Qwen-14B-Base |
| | -Chat | 对话优化模型，支持交互式聊天、问答，针对对话生成任务微调。 | DeepSeek-LLM-7B-Chat |
| | -Instruct | 指令微调模型，擅长遵循具体任务指令（推理、生成、翻译等）。 | Qwen-3-0.6B-Instruct |
| | -Distill | 知识蒸馏模型，通过蒸馏技术压缩，模型更小、推理更高效。 | DeepSeek-R1-1.5B-Distill |
| | -Math | 专注数学推理任务，优化数值计算、公式解析、逻辑证明等能力。 | DeepSeek-Math-7B-Instruct |
| | -Coder | 针对代码生成、编程任务优化，支持代码补全、漏洞检测、算法实现等。 | DeepSeek-Coder-V2-16B |
| **多模态** | -VL | 视觉-语言多模态（Vision-Language），支持图文联合输入输出。 | Kimi-VL-A3B-Instruct |
| | -Video | 视频多模态模型，结合视频帧与文本进行交互。 | LLaVA-NEXT-Video-7B-Chat |
| | -Audio | 支持音频输入输出，涉及语音识别（ASR）或语音生成（TTS）。 | Qwen2-Audio-7B |
| **技术特性与优化** | -Int8/-Int4 | 权重量化为8位/4位，降低显存占用，提升推理速度（适合低资源设备）。 | Qwen2-VL-2B-Instruct-GPTQ-Int8 |
| | -AWQ/-GPTQ | 特定量化技术（自适应权重/GPTQ量化），优化低精度下的模型性能。 | Qwen2.5-VL-72B-Instruct-AWQ |
| | -MoE | 混合专家模型（Mixture of Experts），包含多个专用模块处理复杂任务。 | DeepSeek-MoE-16B-Chat |
| | -RL | 使用强化学习（Reinforcement Learning）优化，提升对话质量或任务响应。 | MiMo-7B-Instruct-RL |
| **版本与变体标识** | -v0.1/-v0.2 | 模型版本号，标识开发阶段（alpha/beta/正式版）。 | Mistral-7B-v0.1 |
| | -Pure | 纯净版模型，去除领域数据或保留原始能力，避免预训练偏差。 | Index-1.9B-Base |
| | -Character | 角色对话模型，专注角色扮演或特定人设（如虚拟助手、动漫角色）。 | Index-1.9B-Character-Chat |
| | -Long-Chat | 支持长上下文对话（通常>4k tokens），处理超长输入输出。 | Orion-14B-Long-Chat |
| **领域与应用标识** | -RAG | 检索增强生成模型，结合外部知识库检索与生成能力。 | Orion-14B-RAG-Chat |
| | -Chinese | 中文优化版本，支持中文分词、方言、拼音纠错等本土化能力。 | Llama-3-70B-Chinese-Chat |
| | -MT | 机器翻译专用模型，支持多语言翻译任务（如中英、英日互译）。 | BLOOMZ-7B1-mt |

## 5、LLM数据集

- [LLaMA Factory 微调教程：如何构建高质量数据集？](https://zhuanlan.zhihu.com/p/1916489160333714285)
- [easy-dataset：LLM数据集生成工具](https://github.com/ConardLi/easy-dataset)
- [LLMDataHub: Awesome Datasets for LLM Training](https://github.com/Zjh-819/LLMDataHub)
- [用于后期训练的数据集和工具的精选列表](https://github.com/mlabonne/llm-datasets)
- [论文：大语言模型训练数据](https://arxiv.org/pdf/2411.07715v1)
- [论文： Datasets for Large Language Models: A Comprehensive Survey](https://arxiv.org/pdf/2402.18041)
- [Toolkit for linearizing PDFs for LLM datasets/training](https://github.com/allenai/olmocr)

### 1、数据集格式

模型微调数据集无明确格式要求，一般在代码中抹除差异，将其转为格式化字符串数组。主流格式有 Alpaca 和 ShareGPT：
- Alpaca：结构简洁，适用于单轮任务、指令微调；
- ShareGPT：支持多轮对话与工具调用。

### 网页数据处理

[CCNet](https://arxiv.org/abs/1911.00359)、SlimPajama、MNBVC、RefineWeb

我已经看到了图片中的表格内容。以下是将图片内容转换为markdown格式的表格：

### 高质量数据集

#### 预训练数据集

| 高质量数据集（有中文）                                                                                  | 大小                            | 特点                          |
| -------------------------------------------------------------------------------------------- | ----------------------------- | --------------------------- |
| [IndustryCorpus2](https://huggingface.co/datasets/BAAI/IndustryCorpus2)                      | 1TB Chinese / 2.2TB English   | 进行行业分类（31个行业），并对数据质量评级。     |
| [Fineweb-Edu-Chinese-V2.1](https://huggingface.co/datasets/opencsg/Fineweb-Edu-Chinese-V2.1) | =1.5TBtokens                  | 有4.6B Tokens 高质量教育语料        |
| [m-a-p/Matrix](https://huggingface.co/datasets/m-a-p/Matrix)                                 | 4.69T tokens                  | 训练 MAP-Neo 模型的预训练数据集        |
| [Ultra-FineWeb](https://huggingface.co/datasets/openbmb/Ultra-FineWeb)                       | en 1T tokens / zh 120B tokens | 最新的，过滤的更好的数据集               |
| [opencsg/chinese-cosmopedia](https://huggingface.co/datasets/opencsg/chinese-cosmopedia)     | zh 60B tokens                 | 参考 CosMopedia 创建的中文合成预训练数据集 |

**数据处理工具**：
- https://github.com/huggingface/datatrove
- https://github.com/modelscope/data-juicer
- https://github.com/multimodal-art-projection/MAP-NEO/tree/main/Matrix
- https://github.com/OpenDCAI/DataFlow

#### SFT 数据集

| SFT 数据集                                                                                                                 | 大小   | 语言       | 特点                             |
| ----------------------------------------------------------------------------------------------------------------------- | ---- | -------- | ------------------------------ |
| [m-a-p/neo_sft_phase2](https://huggingface.co/datasets/m-a-p/neo_sft_phase2)                                            | 109k | 中英       | MAP-Neo SFT 阶段2 Chat 数据，质量不错。  |
| [OpenCoder-LLM/opc-sft-stage1](https://huggingface.co/datasets/OpenCoder-LLM/opc-sft-stage1)                            | 3.2M | 中英（中文较少） | 从多个数据集中过滤和合成而来，有通用指令，更关注代码类数据。 |
| [OpenCoder-LLM/opc-sft-stage2](https://huggingface.co/datasets/OpenCoder-LLM/opc-sft-stage2)                            | 436k | 英        | 高质量的代码类数据。                     |
| [BAAI/Infinity-Instruct](https://huggingface.co/datasets/BAAI/Infinity-Instruct)                                        | 7M   | 中英（中文较少） | 多个尺寸的指令和对话数据。                  |
| [hfl/ruozhiba_gpt4](https://huggingface.co/datasets/hfl/ruozhiba_gpt4)                                                  | 4.9k | 中        | 著名的弱智吧+GPT4回答，对模型的能力有提升。       |
| [Mxode/Chinese-Instruct](https://huggingface.co/datasets/Mxode/Chinese-Instruct)                                        | 485k | 中        | 从多个数据集中筛选的中文指令数据集，价值较高。        |
| [SmolLM Instruct Datasets](https://huggingface.co/collections/HuggingFaceTB/instruct-datasets-66c12756198f9d79f2a60550) | -    | 英        | 多个开源数据集。其中自我认知部分值得参考。          |
| [Magpie-Qwen2-Pro-200K-Chinese](https://huggingface.co/datasets/Magpie-Align/Magpie-Qwen2-Pro-200K-Chinese)             | 200k | 中        | 使用 MagPie 从 Qwen2-72B 中提取的指令集。 |
| [lenML/longwriter-6k-filtered](https://huggingface.co/datasets/lenML/longwriter-6k-filtered)                            | 666  | 英        | 长文本输出（写作）                      |
| [THUDM/LongAlign-10k](https://huggingface.co/datasets/THUDM/LongAlign-10k)                                              | 10k  | 中英       | 长文本输入                          |
| [opencsg/smoltalk-chinese](https://huggingface.co/datasets/opencsg/smoltalk-chinese)                                    | 700k | 中        | 参考 SmolTalk 数据集创建的中文数据集        |
- Yulan的[数据 Recipe](https://docs.google.com/spreadsheets/d/1YP8-loVUxgxo36UEpOwflR3GRHLieBnLlCy8g10g8RU/edit?gid=0#gid=0) 不错，详细说明了其数据来源，可参考。

#### 偏好数据集

| 偏好数据集                                                                                                          | 大小   | 语言  | 特点                                    |
| -------------------------------------------------------------------------------------------------------------- | ---- | --- | ------------------------------------- |
| [llamafactory/DPO-En-Zh-20k](https://huggingface.co/datasets/llamafactory/DPO-En-Zh-20k)                       | 20k  | 中英  | 多个来源整理，质量较高，中英各10k                    |
| [unalignment-toxic-dpo-v0.2-zh_cn](https://huggingface.co/datasets/tastypear/unalignment-toxic-dpo-v0.2-zh_cn) | 541  | 中   | 去除模型安全逻辑                              |
| [ultrafeedback_binarized](https://huggingface.co/datasets/HuggingFaceH4/ultrafeedback_binarized)               | 187k | 英   | 将 UltraFeedback改成二元偏好的数据集             |
| [opencsg/UltraFeedback-chinese](https://huggingface.co/datasets/opencsg/UltraFeedback-chinese)                 | 58k  | 中   | 多个中文资源库中收集了约58k条中文指令，使用DeepSeek V3 评分 |
 
#### 推理数据集

| 推理数据集                                                                                                                  | 类型  | 大小   | 语言  | 特点                            |
| ---------------------------------------------------------------------------------------------------------------------- | --- | ---- | --- | ----------------------------- |
| [m-a-p/COIG-Writer](https://huggingface.co/datasets/m-a-p/COIG-Writer)                                                 | SFT | 914  | 中   | 高质量中文创作与思考过程蒸馏数据集             |
| [INTELLECT-2-RL-Dataset](https://huggingface.co/datasets/PrimeIntellect/INTELLECT-2-RL-Dataset)                        | RL  | 285k | 英   | RL math/code 数据集带ground_truth |
| [open-thoughts/OpenThoughts3-1.2M](https://huggingface.co/datasets/open-thoughts/OpenThoughts3-1.2M)                      | SFT | 1M   | 英   | DeepSeek 蒸馏出的大量数据             |
| [Chinese-DeepSeek-R1-Distill-data-110k](https://huggingface.co/datasets/Congliu/Chinese-DeepSeek-R1-Distill-data-110k) | SFT | 110k | 中   | 中文的 DeepSeek 蒸馏推理数据集          |

#### 评测数据集

| 评测数据集                                                  | 类型            | 大小  | 语言  | 特点                        |
| ------------------------------------------------------ | ------------- | --- | --- | ------------------------- |
| LiveBench                                              | 综合（偏数学和代码）    |     | 英   | 定时更新的综合评测集，质量较高           |
| [AlignBench](https://github.com/THUDM/AlignBench) v1.1 | 多轮对话          | 683 | 中   | 中文对齐评测集，需要LLM作为裁判         |
| IFEval                                                 | 指令遵循          |     | 英   | 自动打分                      |
| 大海捞针                                                   | 长上下文          |     | 英   | 通过插入针的方法评测长上下文能力          |
| Arena-Hard                                             | 多轮对话          |     | 英   | Arena 中比较难的问题，需要 LLM 作为裁判 |
| BFCL v3                                                | 函数调用          |     | 英   | 比较全面的函数调用评测集              |
| MMLU-Redux                                             | 综合（世界知识）      |     | 英   | MMLU的增强版本                 |
| GPQA-Diamond                                           | 综合（世界知识、复杂推理） |     | 英   | GPQA 中比较难的问题（博士级别）        |
| AIME’24                                                | 数学推理          |     | 英   | 2024年的AIME数据集             |
| LiveCodeBench v5                                       | 代码生成          |     | 英   | 定时更新的代码生成评测集              |
| C-Eval                                                 | 综合（世界知识）      |     | 中   | 中文场景下的综合评测集，目前模型多过拟合。     |
| CMMLU                                                  | 综合（世界知识）      |     | 中   | 中文场景下的综合评测集，目前模型多过拟合。     |
| MATH-500                                               | 数学推理          |     | 英   | OpenAI 的MATH数据集           |
| AIME’25                                                | 数学推理          |     | 英   | 2025年的AIME数据              |
| RULER                                                  | 长上下文          |     | 英   | 评测模型上上下文的能力               |
 

#### 国外预训练语料库

| 语料库 | 大小 | 开源状态 | 开源协议 |
|--------|------|----------|----------|
| RedPajama-1T | 5TB | 开源 | Apache-2.0 |
| SlimPajama | 895 GB | 开源 | Apache 2.0 |
| RedPajama-V2 | 180TB | 开源 | Apache-2.0 |
| The Pile | 825.18 GB | 开源 | MIT |
| Refinedweb | 5T | 部分开源 | ODC-BY-1.0 |
| FineWeb | 44T | 开源 | ODC-BY-1.0 |
| TigerBot pretrain en\|zh | 51 GB\|55 GB | 部分开源 | Apache-2.0 |
| Dolma | 11.24 TB | 开源 | MR Agreement |
| C4 | 806.87 GB | 开源 | Apache-2.0 |
| MassiveText | 10.5 TB | 未开源 | - |
| ROOTS | 1.61 TB | 开源 | BLOOM Open-RAIL-M |
| OSCAR | 8.41 TB | 开源 | CC0 |
| OpenWebMath | 26 GB | 开源 | ODC-BY-1.0 |

#### 国内预训练语料库

| 语料库 | 大小 | 开源情况 | 开源协议 |
|--------|------|----------|----------|
| CLUECorpus2020 | 100GB | 开源 | MIT |
| WuDaoCorpora-Text | 5TB | 部分开源 | CC-BY-NC-ND-4.0 |
| CCI 1.0/2.0/3.0 | 100GB/500GB/1TB | 开源 | CC-BY-NC-ND-4.0 |
| WanJuanText-1.0 | 1.1 TB | 开源 | CC-BY-4.0 |
| MNBVC | 32.3TB | 开源 | MIT |
| ChineseWebText | 1.42TB | 开源 | - |
| SkyPile | 600GB | 开源 | Skywork Community License |

## 6、超参数

- 训练轮数（Epochs）：指遍历训练数据次数，少则不熟、多则过拟合；
- 学习率（Learning Rate）：决定参数调整幅度，大则进步快易走偏，小则稳定但慢；
- 批量大小（Batch Size）：是每次更新用的样本数，大则快而粗、小则慢而细。实际需调参验证，小白用默认即可

## 7、训练方法


## 指令微调

## RLHF：强化学习

- [RLHF的完整流程](https://mp.weixin.qq.com/s/wz2PXBl_pNcj8NWej14SMQ)

## 模型蒸馏

蒸馏，本质上也是微调的一种类型；传统微调是为了让大模型获取一些私域知识，比如股票、医疗等等，这是让大模型的知识面增加了，但没有改变大模型的能力。而蒸馏不一样，蒸馏不光教知识，还要教能力。所谓授之以鱼，不如授之以渔，蒸馏就是要让被训练的模型能够学会教师模型的能力。

蒸馏版本的参数量从 1.5B 到 70B 不等，比如以下几种变体：
```
DeepSeek-R1-Distill-Qwen-1.5B
DeepSeek-R1-Distill-Qwen-7B
DeepSeek-R1-Distill-Qwen-14B
DeepSeek-R1-Distill-Qwen-32B
DeepSeek-R1-Distill-Llama-8B
DeepSeek-R1-Distill-Llama-70B
```
- DeepSeek-R1 是主模型的名字；
- Distill 的中文含义是 “蒸馏”，代表这是一个蒸馏后的版本；
- 后面跟的名字是从哪个模型蒸馏来的版本；

例如 `DeepSeek-R1-Distill-Qwen-32B` 代表是基于阿里的开源大模型千问（Qwen）蒸馏而来；最后的参数量（如 671B、32B、1.5B）：表示模型中可训练参数的数量（“B” 代表 “Billion” ，即十亿。因此，671B、32B、1.5B 分别表示模型的参数量为 6710亿、320亿和15亿。），参数量越大，模型的表达能力和复杂度越高，但对硬件资源的需求也越高。

### 蒸馏的流程

- 首先，需要准备好一份传统的数据集；
- 将这些数据喂给满血版的 `DeepSeek-R1:671B` 模型，让 `DeepSeek-R1:671B` 为我们输出带有思考过程和结果的回答，这便是我们的教学数据

![](image/蒸馏的流程.png)

# 三、大模型应用开发

- [动手学大模型应用开发](https://github.com/datawhalechina/llm-universe)
- [面向开发者的大模型](https://github.com/datawhalechina/llm-cookbook)

LLM的接口通常都遵循或类似于 OpenAI 的规范，在与大型模型交互时，除了控制模型输出随机性的参数外，最核心的参数只有两个：messages 和 tools：
- messages-大模型是怎么实现记忆的？messages是一个对话数组，其中角色主要有：
    - system：代表当前对话的系统指令，一般放提示词
    - user：用户指令
    - assistant：LLM的回复
- tools-是一个数组，包含了一堆工具集合，核心为工具的作用描述，和工具需要的参数

# 四、向量-Embedding

- [向量数据量](../数据库/向量数据库.md)

## 什么是向量

embedding是将数据对象(如文本)映射到固定大小的连续一维数字数组(向量空间)的技术。向量空间通常具有几百到几千的维度,每个维度代表某个语义特征或属性

## [向量数据库](../数据库/向量数据库.md)

向量数据库，也称为矢量数据库或者向量搜索引擎，是一种专门用于存储和搜索向量形式的数据的数据库。

在众多的机器学习和人工智能应用中，尤其是自然语言处理和图像识别这类涉及大量非结构化数据的领域，将数据转化为高维度的向量是常见的处理方式。这些向量可能拥有数百甚至数千个维度，是对复杂的非结构化数据如文本、图像的一种数学表述，从而使这些数据能被机器理解和处理。然而，传统的关系型数据库在存储和查询如此高维度和复杂性的向量数据时，往往面临着效率和性能的问题。

因此，向量数据库被设计出来以解决这一问题，它具备高效存储和处理高维向量数据的能力，从而更好地支持涉及非结构化数据处理的人工智能应用

向量数据库有很多种，比如 Pinecone、Chroma 和 Qdrant，有些是收费的，有些则是开源的；

向量之间的比较通常基于向量的距离或者相似度。在高维空间中，常用的向量距离或相似度计算方法有欧氏距离和余弦相似度。
- **欧氏距离**：这是最直接的距离度量方式，就像在二维平面上测量两点之间的直线距离那样。在高维空间中，两个向量的欧氏距离就是各个对应维度差的平方和的平方根。
- **余弦相似度**：在很多情况下，更关心向量的方向而不是它的大小。例如在文本处理中，一个词的向量可能会因为文本长度的不同，而在大小上有很大的差距，但方向更能反映其语义。余弦相似度就是度量向量之间方向的相似性，它的值范围在 -1 到 1 之间，值越接近 1，表示两个向量的方向越相似。

那么到底什么时候选择欧式距离，什么时候选择余弦相似度呢？简单来说，关心数量等大小差异时用欧氏距离，关心文本等语义差异时用余弦相似度。
- 欧氏距离度量的是**绝对距离**，它能很好地反映出向量的绝对差异。当我们关心数据的绝对大小，例如在物品推荐系统中，用户的购买量可能反映他们的偏好强度，此时可以考虑使用欧氏距离。同样，在数据集中各个向量的大小相似，且数据分布大致均匀时，使用欧氏距离也比较适合。
- 余弦相似度度量的是**方向的相似性**，它更关心的是两个向量的角度差异，而不是它们的大小差异。在处理文本数据或者其他高维稀疏数据的时候，余弦相似度特别有用。比如在信息检索和文本分类等任务中，文本数据往往被表示为高维的词向量，词向量的方向更能反映其语义相似性，此时可以使用余弦相似度。

## Embedding模型

- [关于 embedding 模型](https://zhuanlan.zhihu.com/p/29949362142)

# 参考资料

- [ChatGPT 替代品，可以在您的电脑上 100% 离线运行](https://github.com/menloresearch/jan)
- [BCEmbedding: Bilingual and Crosslingual Embedding for RAG](https://github.com/netease-youdao/BCEmbedding)
- [LangExtract：轻量却强大的结构化信息提取神器](https://github.com/google/langextract)
- [Dolphin: Document Image Parsing via Heterogeneous Anchor Prompting](https://github.com/bytedance/Dolphin)
- [从命令行访问大型语言模型](https://github.com/simonw/llm)
- [医学大型语言模型实用指南](https://github.com/AI-in-Health/MedLLMsPracticalGuide)
- [大型语言模型的结构化输出](https://github.com/dottxt-ai/outlines)
- [LLM Council](https://github.com/karpathy/llm-council)