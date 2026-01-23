# 一、LLM

## 1、大模型岗位能力要求

### 1.1、产品经理

- 大模型基础知识了解：什么是大模型 / 基本概念如训练 微调 学习等 -->方便和工程师沟通
- 大模型能力的边界了解: 有哪些能力 / 可以做什么 / 有什么缺点 --> 方便结合公司业务找到切入点
- 大模型基本使用（prompt）： 更体会大模型的能力和测试
- 业务数据从哪里来？：评估功能是否有数据来支持实现以及最终验收评估


### 1.2、大模型应用开发工程师

- 大模型基础知识：大模型技术架构（transfromer,embedding）/ 哪些可用的大模型
- 如何调用大模型: gpu知识 / 复杂prompt技术 / python / pytorch / transformers
- 大模型能力评估：是否需要进行微调
- 大模型应用开发中间件: langchain / llamaindex / rag / agent / embedding / 向量数据库等
- 大模型技术选型: 根据业务选择什么大模型 / 中间件选型
- 大模型AI应用开发流程: 开发 / 迭代 / 评估
- 功能容错能力分析：是否允许出现错误 / 如何后处理

### 1.3、大模型推理部署工程师

- 推理框架： vLLM、Tensorrt-LLM、DeepSpeed 和Text Generation Inference
- 掌握推理优化技术：模型压缩/量化技术、解码方法、底层优化与分布式并行推理
- GPU优化知识： cuda / 并行计算优化 / 访存优化 / 低比特计算
- 主流大模型架构: 包含哪些算子 / 各部分耗时分析和优化
- 模型推理性能测试：并发性能，资源利用率，吞吐

### 1.4、大模型算法工程师

- 推理熟悉大模型的训练和微调全过程：数据准备，清理，预训练、指令微调、强化学习，分布式训练，训练策略，模型评测
- 参数高效模型训练技术：LoRA / Prompt Tuning / Prefix Tuning / P-Tuning
- 大模型训练框架：Pytorch、Tensorflow、 Megatron、Deepspeed
- 负责跟踪、探索业界前沿的大模型训练及优化方案
- 大模型评测

## 2、怎么在AI项目中引入deepsearch功能

## 3、如何从零开始训练一个模型

数据准备 → 预训练 → 后训练（或称对齐）

https://mp.weixin.qq.com/s/O4KQPtYExtLcBbBximlffQ

## 4、LLM能否自己做规划

https://mp.weixin.qq.com/s/_WTjjSCKssTd20lBoGzycQ

主要是考察：对 Agent 框架底层机制的理解深度

### 4.1、现状

在目前主流的 Agent 系统里，不论是 ReAct、BabyAGI 还是 AutoGPT，核心的 “Planning（规划）” 都是写在 Prompt 或代码结构里的
```
You are an AI assistant. To complete tasks, always think step by step, consider tools you have, and reason before acting.
Use this format:
Think
Decide
Act
Observe
```
这其实就是在告诉模型如何“装作”会规划。模型的每一步行动、观察、反思，都是在模板引导下按部就班地产生。 它并不是在“主动思考”，而是在“填空题”

现在的规划，不是 LLM 自己悟出来的，是我们写给它的

### 4.2、LLM 能不能自己做规划

能，但是不靠谱，真正的“Agent 规划”，要求的是：
- 能动态调整计划；
- 能看环境反馈再决定下一步；
- 能持续修正目标。

这些，目前 LLM 靠自己还做不到。

所以我们才会看到各种框架都在帮它“补脑”：
- ReAct：让它“想一步，做一步”；
- MRKL：帮它“选工具”；
- BabyAGI：帮它“维护任务列表”；
- AutoGPT：帮它“循环执行命令”。

它们的本质都一样，让 LLM 看起来像在思考，其实是被程序框架“拎着走”

# 二、RAG

- [35 道高频 RAG 面试题总结](https://articles.zsxq.com/id_h3529hb345rr.html)

## 用的向量模型是哪个？为什么选它？有没有做过评估对比？

## Chunk 粒度你怎么切的？固定大小？内容语义？有没有做 sliding window？

## 向量库召回效果好不好？有没有加 rerank？有没有 query rewrite？

## Prompt 是怎么构造的？知识内容和用户问题怎么融合？有没有模板设计？

## 向量库查询失败怎么办？有没有兜底？

## 系统怎么部署的？支持并发吗？是否支持热更新？

```
                              用户问题
                                 ↓
                          Query Rewrite（可选）
                                 ↓
                ┌─────────────向量召回───────────────┐
                ↓                                   ↓
           多文档语义切分                       Embedding 模型
                ↓                                   ↓
           Chunk 压缩/筛选                  向量入库（Faiss/Qdrant）
                ↓                                   ↓
               TopK 检索结果 ←────────────← Query Embedding
                ↓
         Rerank（可选）/ 过滤低质量段落
                ↓
        拼接 Prompt（格式/长度控制等）
                ↓
        发送给 LLM（ChatGPT/DeepSeek）
                ↓
            返回最终答案
```

## 面试两点句式：

系统描述型
- 构建基于大语言模型的知识增强问答系统，支持文档解析、向量召回、Prompt 构造、答案生成全链路流程。
- 项目采用 Faiss + text-embedding-3 构建向量库，实现毫秒级语义检索；结合 Prompt 工程提升上下文注入效果。

技术挑战型
- 针对知识重复、文本冗余问题，优化 Chunk 生成逻辑，引入 sliding window + 去重策略，有效提升知识利用率。
- 使用 query rewrite 技术处理用户自然语言问题，提升召回准确率 12%。

工程能力型
- 使用 FastAPI + Streamlit 构建前后端分离系统，支持多轮问答、token 成本统计与错误日志追踪。
- 项目支持私有化部署，封装为一键运行的 Docker 镜像，支持企业内网部署。

效果指标型
- 系统上线后在内部知识问答场景中覆盖率达 92%，用户满意度超 87%，平均响应时间小于 1.2 秒。
- 在真实评测集上与 baseline（无 query rewrite + 无 rerank）相比，召回 Top-5 命中率提升 15%。


# 三、MCP

# 四、Agent

多智能体协作

## 1、在多智能体系统中，如何解决智能体间的冲突和竞争？

标准答案：
- 优先级队列机制
- 共识算法（如PBFT）
- 资源锁定策略
- 仲裁者模式

## 2、如何量化评估智能体系统的性能？

评估维度：
- 响应时间（RT）
- 准确率（Accuracy）
- 用户满意度（CSAT）
- 系统稳定性（SLA）

加分回答：建立A/B测试框架，实时监控关键指标，定期优化模型参数

## 3、如何设计智能体的状态持久化方案？

- Redis缓存策略
- 数据库事务管理
- 状态快照机制
- 故障恢复策略

# 五、Transforms

## 1、请描述一下 Transformer 的核心结构

https://mp.weixin.qq.com/s/B2q6lVtr_2DR6uHkdqp7EA

“Transformer 在做什么？” → “结构是怎样的？” → “为什么现在都用 Decoder-only？”

# AI设计

## 1、什么是 AI-Native 应用（AI原生应用）？如何设计一个 AI原生应用？

