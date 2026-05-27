AI 时代，何为程序员的核心竞争力？

--

## 工程能力

从码农到工程师：永不失业的秘诀。

- 什么是码农？就是单纯写代码的人。当 Agent 可以生成代码的时候，码农的价值就会被替代。
- 什么是工程师？是能够设计并驾驭复杂系统的人。工程师的核心能力不在于写代码，而在于：
- 理解系统的复杂性
- 抽象和结构化思维
- 驾驭不确定性

## 懂业务成为护城河

如果你说“我不懂，Agent 什么都能做，我没有我的立足之地”，那是因为咱们没有懂到那个层次。如果咱们懂到那个层次，你就会发现在 Agent 时代，你可以设计并驾驭的东西太多了。

## 深度交互能力

## Agent 岗位

### Agent 工程化能力

#### 具备 Claude Code、Cursor、OpenClaw 等 AI 编程工具的重度使用经验

不光要会用 Claude Code 写代码，还得知道它内部是怎么拆解任务的、上下文是怎么管理的、工具调用的 schema 是怎么设计的。

能接入 LangChain、OpenAI Agents SDK 这些外部框架，还要能做 Agent 的容器服务、运行时环境、轨迹回放和调试分析。

Claude Code、Codex 这些 Coding 工具，你得能说清楚它们在哪些任务上强，怎么改 prompt、改上下文、改工具链

#### 其他

Docker、Kubernetes、CI/CD、Git 工作流、代码审查

### 模型机制理解

#### 理解 Context Window 和 Token 机制

- 上下文为什么会爆？
- 工具 schema 为什么占 token？
- 多轮对话怎么压缩？
- 长上下文对 Agent 可靠性有什么影响？

这些问题直接决定了 Agent 在长任务中的可靠性

### Tool Use、Function Calling 和 MCP

- 你能不能定义稳定的工具接口？
- 能不能设计参数 schema？
- 能不能处理工具返回的各种格式？
- 调用链怎么做观测？
- 失败了怎么重试？
- 权限怎么隔离？

### Planning、Multi-Agent 和长期记忆

典型场景包括个人助理、Deep Research、自动化工作流、多模态设备控制。

要求是能把一个模糊的用户需求拆成模型能力 gap、数据构造方案和评测指标

### 数据、评测和 RL 循环

### 总结

工程基础设施：Linux、网络、数据库、Docker、K8s、CI/CD

Tool Use、Function Calling、MCP、context、token、Memory、planning、权限，这些不是概念，每一个背后都对应实际的工程问题

### 如何匹配岗位

- 深度使用一个编程工具：比如 Claude Code 在处理超大项目时，上下文经常会被压缩，之前说过的需求它也会忘
- 读一个 Agent 框架的源码：LangChain、OpenAI Agents SDK、或者 Anthropic 的 Claude Agent SDK 都行。不需要全读，重点看它怎么做工具调用、怎么管理对话历史、怎么处理错误；
- 自己实现一个


