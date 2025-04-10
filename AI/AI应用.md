# 一、Prompt

- [215+ ChatGPT Prompts & How to Write your Own](https://writesonic.com/blog/chatgpt-prompts)
- [awesome-chatgpt-prompts](https://github.com/f/awesome-chatgpt-prompts)，对应中文版：[awesome-chatgpt-prompts-zh](https://github.com/PlexPt/awesome-chatgpt-prompts-zh)
- [AI List Chat Prompt](https://www.itis.chat/)
- [Prompt guide](https://www.promptingguide.ai/)
- [掌握Prompt写作秘籍](https://mp.weixin.qq.com/s/r1_eUa0qm9UDOdAM8s5Kug)

## 1、什么是 Prompt

在大规模语言模型（LLM, Large Language Models）领域，Prompt 是一种结构化的输入序列，用于引导预训练语言模型生成预期的输出。它通常包括明确的任务要求、背景信息、格式规定以及示例，以充分利用模型的能力在特定任务中生成高质量的响应

## 2、Prompt的运行过程

**1.接收输入**
- 用户或系统向语言模型提供一个Prompt，这个Prompt包含任务指令、背景信息、示例以及格式说明。

## 3、如何精准生成Prompt

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

# 二、RAG

- [基于深度文档理解构建的开源 RAG（Retrieval-Augmented Generation）引擎](https://github.com/infiniflow/ragflow)
- [RAG变体](https://www.53ai.com/news/RAG/2025031889753.html)
- [tavily.ai 是一个专为人工智能代理（如大型语言模型，LLMs）和检索增强生成（RAG）应用优化的搜索引擎](https://tavily.com/)
- [RAG + Tool Use](https://cohere.com/llmu/from-rag-to-tool-use)

RAG 检索增强生成（Retrieval Augmented Generation），已经成为当前最火热的LLM应用方案。理解起来不难，就是通过自有垂域数据库检索相关信息，然后合并成为提示模板，给大模型生成漂亮的回答。

RAG（中文为检索增强生成） = 知识库 + 检索技术 + LLM 提示。

## 1、如何选择大模型

### 1.1、在RAG应用中需要大模型的能力

- 信息抽取能力：大模型需要从RAG检索出来的上下文中，抽取出和问题最有价值的信息
- 上下文的阅读理解能力：大模型需要从RAG检索出来的上下文和问题进行语义理解，生成合适的答案；
- 工具调用和function call能力：RAG应用中可能会调用外部工具和接口

### 1.2、如何挑选大模型

- 模型大小选择：优先选择最大的模型进行测试是否可行，验证业务可行性可行的情况下，收集数据可迁移到小模型
- 模型能力测试：构建业务的测试集，测试信息抽取能力、阅读理解能力和工具调用，function call能力
- 成本和设备：大模型的使用是有一定的成本，api调用成本和设备
- 企业数据安全： 调用本地模型和外部api，要评估数据的安全


# 四、Agent


- [coze](https://www.coze.cn/store/bot)
- [coze ai agent](https://juejin.cn/post/7330426020997382184)
- [Agent调研--19类Agent框架对比](https://mp.weixin.qq.com/s/rogMCoS1zDN0mAAC5EKhFQ)
- [A list of AI autonomous agents](https://github.com/e2b-dev/awesome-ai-agents)
- [复杂表格多Agent方案](https://mp.weixin.qq.com/s/lEbFZTPCdFPW-X22253ZPg)
- [Dify平台](https://dify.ai/zh)
- [快速开发AI Agent](https://github.com/huggingface/smolagents)
- [What Are Agentic Workflows? Patterns, Use Cases, Examples, and More](https://weaviate.io/blog/what-are-agentic-workflows)
- [Autogen的基本框架](https://limoncc.com/post/3271c9aecd8f7df1/)
- [MetaGPT智能体开发入门](MetaGPT智能体开发入门)

# 三、MCP

- [Model Context Protocol](https://modelcontextprotocol.io/introduction)
- [探索MCP](https://v2ex.com/t/1119962)
