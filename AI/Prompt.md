
- [215+ ChatGPT Prompts & How to Write your Own](https://writesonic.com/blog/chatgpt-prompts)
- [awesome-chatgpt-prompts](https://github.com/f/awesome-chatgpt-prompts)，对应中文版：[awesome-chatgpt-prompts-zh](https://github.com/PlexPt/awesome-chatgpt-prompts-zh)
- [AI List Chat Prompt](https://www.itis.chat/)
- [Prompt guide](https://www.promptingguide.ai/)
- [Prompt Engineering Guide](https://github.com/dair-ai/Prompt-Engineering-Guide)
- [Language of GPT-结构化提示词](https://github.com/langgptai/LangGPT)
- [Learn Prompt Engineering](https://learnprompting.org/docs/introduction)
- [Google-Prompting-Guide](https://services.google.com/fh/files/misc/gemini-for-google-workspace-prompting-guide-101.pdf)
- [Prompt engineering overview](https://docs.anthropic.com/en/docs/build-with-claude/prompt-engineering/overview)
- [Prompt 模式](https://github.com/phodal/prompt-patterns)
- [提示词教程](https://github.com/PandaBearLab/prompt-tutorial)

# 1、什么是 Prompt

在大规模语言模型（LLM, Large Language Models）领域，Prompt 是一种结构化的输入序列，用于引导预训练语言模型生成预期的输出。它通常包括明确的任务要求、背景信息、格式规定以及示例，以充分利用模型的能力在特定任务中生成高质量的响应

# 2、Prompt的运行过程

**1.接收输入**
- 用户或系统向语言模型提供一个Prompt，这个Prompt包含任务指令、背景信息、示例以及格式说明。

**2、文本处理与编码**
- 模型首先对输入的Prompt进行预处理，包括分词（tokenization）和编码（encoding）。
- 预处理过程将文本转换为一系列的词汇ID（token IDs），这些IDs会传递到Embedding层，进行进一步处理。

**3、模型计算**
- 编码后的文本输入到基于Transformer架构的神经网络中。Transformer架构包括多个层的自注意力机制（Self-Attention Mechanism）和前馈神经网络（Feed-Forward Neural Network）

**4、生成输出**
- 模型根据计算结果，从生成的概率分布中采样下一个词汇。每个生成的词汇会迭代地输入回模型，以生成后续词汇。这个过程持续进行，直到满足输出条件（例如达到预定句子长度或遇到特殊结束符）。这一过程称为解码（Decoding），可以使用贪心搜索（Greedy Search）、束搜索（Beam Search）或其他采样方法（如随机采样和核采样），以生成最优的文本输出

# 3、如何精准生成Prompt

**（1）明确的目标和任务**：具体而清晰地阐明你的预期结果和要求，包括明确PROMPT的整体目标和具体希望进行的任务，例如生成文本、回答问题、翻译语言或进行情感分析等

举个例子：
- 👎不够清晰:“描述一下气候变化。”
- 👍清晰明确：“请写一篇200字的文章，讨论气候变化对全球农业生产的影响，特别是对水资源管理和农作物产量的影响。”

准确表达希望获得的信息或结果：
- 👎不够准确：“告诉我关于机器学习的知识。”
- 👍准确表达：“请解释什么是机器学习，包括其基本概念和至少两种常用算法（如决策树和神经网络），并讨论它们各自的应用领域。”

**（2）上下文和背景信息**：上下文和背景信息可以帮助更好地理解如何创建高质量的提示，引导生成型人工智能模型产生准确、高效和有针对性的回应。它包含了充分的上下文和背景信息，具体的任务目标和预期输出，并明确了任何必要的细节、限制条件和目标读者或用户群体
- 角色和身份：明确任务请求者的角色（例如：学生、研究员、产品经理）
- 任务的具体目标：明确需要完成的任务或回答的问题，包含具体的细节和预期输出
- 相关历史和现状：提供与任务相关的背景历史，例如之前的研究、项目进度或市场状况，涉及当前的环境或条件，包括任何变化或影响因素；
- 特定要求和条件：明确任务的具体要求和条件（例如：字数限制、格式要求、时间限制），说明任何必须满足的约束条件
- 读者或受众：阐明回答的目标受众

**（3）详细的衡量标准或考评维度**：实现任务目标详细的衡量标准或考评维度，提供清晰、全面、高效的评估，确保任务目标的实现

**（4）明确的输入和输出格式**
- 输入格式是指模型接收的原始数据的结构和形式。明确的输入格式定义了数据应该如何组织和呈现，以确保模型能够正确解析和理解这些数据。
- 输出格式是指模型生成结果的预期结构和形式。明确的输出格式定义了模型应该如何组织和呈现生成的内容，以满足特定的需求或标准

总结：
- 明确目标和任务：让 AI 知道你想要啥，选对方向。
- 充分上下文和背景：信息充足，不多不少，AI 掌握事实不胡思乱想。
- 设定衡量标准或考评维度：希冀结果，AI 就像你当年的满分作业。
- 简洁直接：少啰嗦，AI 秒懂。
- 避免歧义：不理解的词汇休想扰乱 AI 的步伐。
- 分步骤和层次化指导：分清先后，步步为赢。
- 考虑多种可能性和边界条件：极端情况，让 AI 时刻清醒。
- 纠错机制：一键修正，防微杜渐。
- 语言和文化敏感性：跨文化交流，AI 落落大方。
- 数据隐私和安全性：合规隐私，放心传递。
- 约束设置：锁定目标，输出精准。

# 4、大模型设置

在提交系统提示词给到大模型时，可以配置一些参数来获取不同的提示结果

- **温度（Temperature）**：简单来说， temperature 越低，结果就越确定，提高温度会导致更多随机性，从而鼓励更多多样化或创造性的输出。本质上，是在增加其他可能词的权重；
- **Top P**：一种带有温度的采样技术，称为 nucleus sampling，可以控制模型的确定性，如果在寻找精确和事实性的答案，请保持这个值较低。如果寻找更多样化的响应，请增加到更高的值。如果使用 Top P，这意味着只有构成 top_p 概率质量的标记才会被考虑用于响应，因此低 top_p 值会选择最自信的响应。这意味着高 top_p 值将使模型能够查看更多可能的单词，包括不太可能的单词，从而导致更多样化的输出；一般建议是修改温度或 Top P，但不要同时修改两者。
- **Max Length**：可以通过调整 max length 来管理模型生成的 token 数量。指定最大长度有助于防止生成过长或不相关的回复，并控制成本
- **Stop Sequences**：stop sequence 是一个字符串，用于停止模型生成 token。指定停止序列是控制模型回复长度和结构的一种方式。例如，你可以通过添加"11"作为停止序列，告诉模型生成最多包含 10 项的列表。
- **Frequency Penalty（频率惩罚）**：frequency penalty 对下一个 token 应用惩罚，惩罚程度与该 token 在回复和提示中已出现的次数成正比。频率惩罚越高，某个词再次出现的可能性就越低。这个设置通过给出现次数较多的 token 施加更高的惩罚，减少了模型回复中词语的重复
- **Presence Penalty（）**：presence penalty 也会对重复的 token 施加惩罚，但与频率惩罚不同，所有重复的 token 的惩罚是相同的。出现两次的 token 和出现 10 次的 token 受到相同的惩罚。这个设置可以防止模型在其回应中过于频繁地重复短语。如果你希望模型生成多样化或富有创造性的文本，你可能需要使用更高的存在惩罚。或者，如果你需要模型保持专注，可以尝试使用较低的存在惩罚

通常建议只调整频率惩罚或存在惩罚，而不是两者都调整

# 5、基础介绍

## 5.1、Prompt格式

一个标准的格式是：
```
<Question>?
```
或者
```
<Instruction>
```
可以将其格式化为问答（QA）格式，这在许多问答数据集中是标准的，如下所示：
```
Q: <Question>?
A: 
```
在像上述那样提示时，也称为：`zero-shot prompting`，即你直接提示模型给出响应，而无需提供任何关于你想让它完成的任务的示例或演示。一些大型语言模型具有执行零样本提示的能力，但这取决于任务的复杂性和知识，以及模型被训练以擅长执行的任务。

还有另外一种叫：`few-shot prompting`，即提供少量样例，少量样本提示能够实现情境学习，即语言模型在少量示例的指导下学习任务的能力
```
<Question>?
<Answer>
<Question>?
<Answer>
<Question>?
<Answer>
<Question>?
```

## 5.3、Prompt 元素

提示包含以下任一要素：
- Instruction：希望模型执行的具体任务或指令
- 上下文：外部信息或额外上下文，可以引导模型产生更好的回复
- Input Data：希望为其找到回复的输入或问题
- Output Indicator：输出的类型或格式

## 5.4、Prompt 通用设计技巧

- 从简单开始：从简单的提示开始，并在追求更好结果的过程中不断添加更多元素和上下文。由于这个原因，在过程中迭代Prompt至关重要
- 指令：可以通过使用命令来指导模型实现你的目标，从而为各种简单任务设计有效的提示，例如"写"、"分类"、"总结"、"翻译"、"排序"等。建议你将指令放在提示的开头。另一个建议是使用一些清晰的分隔符（如"##"）来分隔指令和上下文。
    ```
    ## Instruction ##
    Translate the text below to Chinese:
    Text: "hello!"
    ```
- 具体性：对于模型需要执行的具体指令和任务，应尽可能详细。提示越具体、越详细，结果就越好；设计提示时，还应考虑提示的长度，因为提示的长度存在限制
- 避免不精确：通常来说，具体和直接更好。这里的类比非常类似于有效的沟通——越直接，信息传达得越有效
- 做还是不做？设计提示时，另一个常见的技巧是避免说不要做什么，而是要说应该做什么。这能鼓励更具体的要求，并专注于那些能引导模型给出良好响应的细节

# 6、Prompts 技巧

- [语言模型 Prompt 工程-最佳实践](https://open.bigmodel.cn/dev/guidelines/LanguageModels)

- Zero-Shot Prompting：零样本提示词
- Few-Shot Prompting：少量样本提示词
- Chain-of-Thought Prompting：思维链提示词
```
The odd numbers in this group add up to an even number: 4, 8, 9, 15, 12, 2, 1.
A: Adding all the odd numbers (9, 15, 1) gives 25. The answer is False.

The odd numbers in this group add up to an even number: 17,  9, 10, 12, 13, 4, 2.
A: Adding all the odd numbers (17, 9, 13) gives 39. The answer is False.

The odd numbers in this group add up to an even number: 15, 32, 5, 13, 82, 7, 1. 
A:
```
- [Meta Prompting](https://arxiv.org/pdf/2311.11482): 是一种高级提示技术，它关注任务和问题的结构和句法方面，而不是其具体内容细节。元提示的目标是构建一种更抽象、结构化的方式来与大型语言模型（LLMs）交互，强调信息的形式和模式，而不是传统的以内容为中心的方法。关键特征可以总结如下：
    - Structure-oriented（结构导向）: 优先考虑问题和解决方案的格式和模式，而非具体内容。
    - Syntax-focused: 使用语法作为预期响应或解决方案的指导模板。
- Self-Consistency: 自洽性，路是通过少样本思维链采样多条多样的推理路径，并利用生成结果来选择最一致的答案。这有助于提升思维链提示在涉及算术和常识推理任务上的性能

# 7、Prompt框架

## 7.1、通用框架

Prompt 通用框架=定角色+说背景+下任务+提要求

```markdown
# 框架要素

## 1. 【定角色】设定角色：你希望让对话 AI 扮演什么角色

在构建 Prompt 之前，首先明确你希望 AI 扮演的角色。是作为一个专家顾问？还是一个友好的助手？角色的定义将影响 AI 的语气、回答的风格、以及内容深度。

## 2. 【说背景】阐述背景：我是谁，我的目的是什么

接下来，向 AI 介绍你自己，阐述你遇到的问题或需求，以及你的目的。提供必要的背景信息或数据，帮助 AI 在正确的背景下工作。这将使 AI 更好地理解任务的上下文。

## 3. 【下任务】明确目标：你希望让 AI 具体执行什么任务

接下来，告诉 AI 你需要它具体执行和完成什么任务。这个定义要清晰明确，让 AI 知道它的目标和工作方向是什么。

## 4. 【提要求】定义结果：输出的具体内容是什么

指出你期望 AI 输出的具体内容是什么，你希望得到的答案形式或结果。是希望 AI 提供一个列表、一段描述，还是一个详细的解释？明确你的期望，AI 就能更好地满足你的需求。

## 5. 【补条件】限制条件：其他辅助的复用性强的提示词

设定 AI 在回答问题时需要遵守的规则或限制。这可能包括回答的长度、使用的语言风格，或是需要避免的话题。
```

## 7.2、RICE 框架

通过明确四大要素，让 AI 模型快速理解任务边界与目标。 全称：Role（角色）、Input（输入）、Context（上下文）、Expectation（期望输出）。
- Role（指定模型角色）
    - 作用：定义模型在任务中的身份（如 “你是一位资深数据分析师”“你是科普博主”），引导其使用对应领域的知识与表达风格。
    - 例子： “假设你是一名金融分析师，请分析...”
- Input（明确输入内容）
    - 作用：提供任务的具体素材（如问题、数据、文本片段），确保模型基于准确信息响应。
    - 例子： “输入数据：某公司 2024 年财报显示营收增长 15%... 请分析其盈利趋势。”
- Context（补充背景信息）
    - 作用：说明任务的应用场景、限制条件或前置知识，避免模型输出偏离实际需求。
    - 例子： “背景：该分析将用于投资者路演，需简洁呈现核心结论。”
- Expectation（清晰输出预期）
    - 作用：规定输出的形式（如表格、报告、代码）、详细程度或重点方向。
    - 例子： “期望输出：用 3 点总结投资建议，每点不超过 20 字。”

## 7.3、CRISPE 框架

通过多维度拆解任务，提升提示词的逻辑性与可控性。 全称：Clarify（澄清）、Role（角色）、Input（输入）、Structure（结构）、Prompt（指令）、Evaluate（评估）。
- Clarify（澄清需求）
    - 作用：先明确任务的核心目标，避免模糊表述导致模型误解。
    - 例子： “需解决的问题：如何用 Python 优化数据可视化效率？”
- Role（角色定义）与 RICE 一致：指定模型身份（如 “你是 Python 开发专家”）。
- Input（输入）细化要求：明确输入数据的格式、来源或处理方式（如 “基于附件中的 CSV 文件...”）。
- Structure（输出结构设计）
    - 作用：预设输出的框架（如 “分步骤说明 + 代码示例 + 注意事项”），让结果更规整。
    - 例子： “输出需包含：①原理分析 ②代码片段 ③性能对比表格”
- Prompt（核心指令）
    - 作用：用具体动作词引导模型（如 “生成”“优化”“诊断”），避免抽象指令。
    - 例子： “请编写一个函数，实现数据批量清洗，并注释关键步骤。”
- Evaluate（结果评估标准）
    - 作用：提前设定输出的验收条件（如 “准确率需≥95%”“代码无报错”）。
    - 例子： “确保生成的方案在实际场景中可落地，附可行性测试步骤。”

## 7.4、常用模板

### 7.4.1、模板1

```markdown
# 角色
你是一个{{xxx}},你擅长{{xxx}}, 请按照要求完成下面的任务

# 输入
（可选，如果用在某个专用场景可以描述输入的内容范围和格式，便于大模型更好的理解、解析）

# 背景
{{context}}

# 任务（目标）
{{task1}}
{{task2}}

# 规则
{{要求&约束列表1}}
{{要求&约束列表1}}
(
  可以提供少样本学校案例，比如：
  问：xxx
  答：不建议的回复 v1
  答：建议回复 v2
)

# 格式
{输出格式, 比如：使用 Json 格式输出，包含 xx,yy 字段}
```

### 7.4.2、模板2

```md
# Role:

# Profile:

## Background:

## Goals:

## Constrains:

## Skills:

## Workflows:

## Initialization:
```
或者
```md
# 角色:

# 简介:

## 背景:

## 目标:

## 限制:

## 能力:

## 流程:

# 初始化:
```
- Role（角色）：通常是让大模型扮演的角色，本质上是为了缩小词向量查找范围，让大模型在自己的词向量坐标中找到和此角色更贴近的词汇，角色一般用第二人称【你】：`## Role: 心理咨询专家`
- Profile（简介）：可以介绍一些基本信息
- Background（背景）：任务的背景信息，目的也是为了让大模型更好的“理解”我们的任务
- Goals（目标）：任务的总体目标
    ```md
    ## Goals:
    - 根据用户输入的信息，生成一篇小红书笔记
    ```
- Constrains（限制）：对大模型的输出内容做规范
    ```md
    ## Constrains:
    - 不能有语气助词
    - 必须要考虑中国人的文化背景
    ```
- Skills（能力）:对角色的进一步补充，我们给大模型指定角色的时候，大模型扮演这个的这个角色需要具备哪些能力才能更好的完成我们的任务，需要具备的能力都在这里指定
- Workflows（流程）：大模型的工作流程，大模型会根据这里指定的流程，一步一步来往下执行
- Initialization（初始化）：初始化内容，一般在这里设置大模型和用户打招呼的内容。

提示词流程：
```mermaid
flowchart LR
    id1(分析需求) --> id2(确定能力)
    id2(分析需求) --> id3(设计流程)
    id3(设计流程) --> id4(编写提示词)
    id4(编写提示词) --> id5(持续迭代)
```

# 8、Prompt 优化

- [Claude Optimizer：自动分析Prompt结构并提出改进建议（如添加XML标签、调整逻辑层级）](https://docs.anthropic.com/en/docs/build-with-claude/prompt-engineering/prompt-improver)
- [火山方舟-Prompt 调优](https://console.volcengine.com/ark/region:ark+cn-beijing/autope/workbench/new)
- [Prompt Pilot](https://promptpilot.volcengine.com/home)
- [提示词优化器](https://github.com/linshenkx/prompt-optimizer)

# 9、魔法词

在提示词领域，有一些类词被称为“魔法词”，使用后可以立竿见影的提升大模型的输出质量，让人直呼玄学的力量。通常是因为这些提示词会让大模型联想或使用到一些高质量的数据，所以才会产生较好的输出结果。常用到两种“魔法词”，分别是：
- Let's think step by step.
- Read the question again
    - Re-Reading重读：Re-Reading Improves Reasoning in Large Language Models（https://arxiv.org/pdf/2309.06275）

# 10、提示词评测

- [AI 提示工程工作台，用于使用强大的分析工具制作、测试和系统评估提示](https://github.com/insaaniManav/prompt-forge)

# Prompt 生成案例

- [GPT 提示词](https://github.com/linexjlin/GPTs)
- [AI 提示 模板](https://www.notion.com/zh-cn/templates/category/ai-prompts)
- [AI工具的提示词](https://github.com/chenlanqing/system-prompts-and-models-of-ai-tools)
- [Snack Prompt](https://snackprompt.com)
- [Free AI Prompt](https://flowgpt.com/)
- [AI提示词大全](https://prompthero.com/)
- [Stable Diffusion提示词](https://publicprompts.art/)
- [UI Prompt Generator](https://uiprompt.art/ui-prompt-generator)
- [自动提示词](https://github.com/AIDotNet/auto-prompt)
- [大模型解谜游戏 LLM Riddles: https://zhuanlan.zhihu.com/p/665418646](https://modelscope.cn/studios/LLMRiddles/LLMRiddles/summary)
- [Manus 提示词](https://gist.github.com/jlia0/db0a9695b3ca7609c9b1a08dcbf872c9)
- [gpt-5-coding-examples:GPT-5 一句话生成的各种小程序和应用](https://github.com/openai/gpt-5-coding-examples)