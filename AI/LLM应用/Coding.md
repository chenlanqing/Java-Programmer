# 一、AI编程

## 1、拆解项目

规划好项目架构：一般来说可以按层级来规划，最简单的模型可以分为三层：
- 1️⃣ 数据存储层（Data Layer）
  * 项目需要处理哪些数据？
  * 使用什么框架和中间件存储？
  * 是否需要关系型 / 非关系型数据库？是否引入消息队列？
- 2️⃣ 业务逻辑层（Business Layer）
  * 项目的核心业务逻辑是什么？
  * 主流程如何？是否要拆分多个业务子系统？
  * 模块间数据流转逻辑如何？
- 3️⃣ 用户交互层（UI Layer）
  * 项目如何与用户交互：客户端、Web端还是APP？
  * 功能页面有哪些？信息流如何设计？

**深入拆解时需考虑的关键问题**

| 维度   | 关键问题                                           |
| ---- | ---------------------------------------------- |
| 功能设计 | 网站/系统有哪些功能单元？数据流转路径如何？                         |
| 技术选型 | 后端语言：Java、Python、Node.js？框架：Spring Boot、Flask？ |
| 数据建模 | 设计哪些数据库表？字段？是否要加索引？                            |
| 核心流程 | 各核心模块的处理流程是怎样的？                                |
| 账户体系 | 自建账户 or 第三方登录（微信等）？                            |
| 文件存储 | 本地存储 or 云服务（OSS等）？                             |
| 性能优化 | 是否对性能要求高？是否要考虑索引、缓存？                           |


**AI编程的正确使用姿势**
* **不要**“想到哪写哪”，这样会造成代码混乱、难以维护。
* **要先设计架构蓝图**，再让AI在框架内实现。
* 你的角色 = 架构师 & 产品经理
  * AI的角色 = 执行者、代码工
* 这样你可以随时纠偏，快速调整。

## 2、工具选择

- Codex
- Claude Code
- Claude Code + GLM4.5
- Kiro
- Aider
- Windsurf

## 3、SDD：规范驱动开发

- [SDD:规范驱动开发的工具包-适合从0开始做新项目，更规范](https://github.com/github/spec-kit)
- [Spec-driven development for AI coding assistants：适合集成到已有的项目中，更轻量](https://github.com/Fission-AI/OpenSpec)
- [规范驱动开发（SDD）全解：从理念到实践](https://blog.dengqi.org/posts/%E8%A7%84%E8%8C%83%E9%A9%B1%E5%8A%A8%E5%BC%80%E5%8F%91sdd%E5%85%A8%E8%A7%A3%E4%BB%8E%E7%90%86%E5%BF%B5%E5%88%B0%E5%AE%9E%E8%B7%B5%E7%BF%BB%E8%AF%91/)
- [Spec Workflow MCP](https://github.com/Pimzino/spec-workflow-mcp)
- [规范驱动开发（SDD）：用 AI 写生产级代码的完整指南](https://www.softwareseni.com/spec-driven-development-in-2025-the-complete-guide-to-using-ai-to-write-production-code/)

### 概述

规范驱动开发 (Spec-Driven Development， SDD)，其核心理念似乎是指在利用 AI 编写代码之前，首先编写一份规范(spec)，即文档先行。这份规范将成为人类开发者与 AI 共同的事实来源。

规范驱动开发是一种方法论：用“形式化、详尽的规范”作为可执行蓝图，驱动 AI 进行代码生成。规范是事实来源，指导自动化的生成、校验与维护；编写清晰的需求，AI 负责实现。
1. 规范优先 (Spec-first)：这是最基础的层面，开发者首先编写一份深思熟虑的规范，然后在当前任务的 AI 辅助开发工作流中使用这份规范。
2. 规范锚定 (Spec-anchored)：在任务完成后，规范并不会被丢弃，而是被保留下来，以便在后续对该功能进行演进和维护时继续使用。
3. 规范即源码 (Spec-as-source)：这是最激进的层面，规范在时间推移中成为项目的主要源文件，人类开发者只编辑规范，永远不会直接修改代码。

**与传统对比**

传统开发通常是“开发者写需求 + 写代码”，流程为“需求 → 设计 → 手写代码 → 测试”。  
规范驱动开发将其变为“需求 → 详细规范 → AI 生成 → 验证”。

**关键差异** 在于：先规范、后代码；AI 根据规范实现，开发者聚焦架构、需求与验证；质量通过系统化闸门把关；并通过持续反馈把错误信息融入规范，迭代提升输出质量

### Kiro

Kiro 的 spec 流程被设计为三个步骤：需求 (Requirements) → 设计 (Design) → 任务 (Tasks)。每个工作流步骤都由一个 Markdown 文档表示，Kiro 会在引导你完成这三个步骤：
1. 需求文档：它被构建为一个需求列表，每个需求代表一个“用户故事” (User Story)，采用“作为...（As a...）”的格式。同时，每个需求都配有验收标准，采用“假如... (GIVEN...) 当... (WHEN...) 则... (THEN...)”的格式；
2. 设计文档：设计文档包含了组件架构图，以及诸如数据流、数据模型、错误处理、测试策略、实现方法和迁移策略等部分。（不确定这是一个固定的结构，还是会根据任务的不同而变化。）
3. 任务文档：这是一个任务列表，这些任务可以追溯到需求文档中的编号。Kiro 为这些任务提供了一些额外的界面元素，允许你逐个运行任务，并审查每个任务带来的变更。每个任务本身是待办事项的列表，并以相关的需求编号（如 1.1, 1.2, 1.3）结尾

### [SpecKit](https://github.com/github/spec-kit)

#### 基本定义

spec-kit 的工作流程是：宪章 (Constitution) → 循环执行 [ 规范 (Specify) → 计划 (Plan) → 任务 (Tasks) ]
1. 宪章 (Constitution)应该包含那些不可变的且应始终应用于每次变更的高级原则。它基本上是一个非常强大的规则文件，在工作流程中被大量使用

spec-kit 的一个特点是，一份规范是由许多文件组成的。例如，一个规范文件夹可能包含数据模型 (data-model)、计划 (plan)、任务 (tasks)、规范 (spec)、研究 (research)、API 和组件 (component) 等多个 Markdown 文件

#### 安装

```bash
# 推荐用uv (一个快到飞起的Python包管理工具)
uv tool install specify-cli --from git+https://github.com/github/spec-kit.git
```

#### 基本使用

**1. 初始化项目**
```bash
# 创建一个叫 answer-question 的项目并初始化
specify init answer-question
cd answer-question
```
初始化的时候，它会让你选一个AI助手 (Claude) 和脚本类型（比如sh）。然后项目里会多出`.claude`/和`.specify/`这些文件夹

**2. 项目开始前，先给它定好基本原则和约束**
在AI编程助手中输入（比如使用Claude Code，注意工程路径中不要有中文！！）
```bash
/speckit.constitution 这是一个基于Vue+Python的答题系统，要注重简洁和用户体验。
```
AI会生成一份`constitution.md`文件，里面可能写着：
- 技术栈偏好：优先用React Hooks，别用Class Components。
- 代码风格：按Airbnb JavaScript Style Guide来。
- 测试要求：核心功能必须有单元测试。
- 用户体验原则：交互必须流畅，响应时间低于100ms。

**3. 提需求**
```bash
/speckit.specify 我要做一个答题应用。
核心功能：
- 支持题目录入，题目类型包括单选、多选、判断、填空、编程题；
- 对于单选、多选、判断题，系统能够自动批改并给出分数；
- 对于填空题和编程题，系统能够将题目和答案存储在数据库中，等待人工批改；
- 系统能够生成考试报告，包括每道题的正确答案、学生的答案和得分情况；
- 每次用户答完一题，系统能够立即反馈正确答案和解析。
...
```
Spec-Kit会立马开干：
- 在`specs/`目录下创建一个新版本，比如 001-quiz-system。
- 生成一份详细的`spec.md`（需求规格文档），里面有用户故事、验收标准、边界条件等等。
- 自动给你创建一个新的Git分支，比如feat/001-quiz-system。

**4. 清除疑点**

这一步是可选的。如果需求描述得比较模糊（比如“好玩儿的动画”），可以运行这个命令，让AI主动问你问题，把细节弄清楚
```bash
/speckit.clarify
```

**5. 出方案**：AI 变身架构师
```bash
/speckit.plan
```
AI会根据“宪法”和“需求”，生成一套完整的技术方案文档，可能会有：
- plan.md: 技术栈决策
- data-model.md: 数据结构定义（比如Todo长啥样）。
- contracts/: API接口或组件接口定义。
- research.md: 为啥这么选型，做了哪些调研

**6. 拆任务**：方案定了，接下来就是把它拆解成能干的活儿
```bash
/speckit.tasks
```
AI会把plan.md拆成一份详细的tasks.md文件，就像一个靠谱的项目经理，把工作安排得明明白白

**7. 写代码**：让 AI 开始写代码
```bash
/speckit.implement
```
AI会严格按照tasks.md的任务列表，一个一个地完成编码、写测试，每搞定一个，就会在任务清单上打个勾[x]

### BMAD

- [Breakthrough Method for Agile Ai Driven Development](https://github.com/bmad-code-org/BMAD-METHOD)

### 注意事项

- [Spec 编写注意事项](https://mp.weixin.qq.com/s/27x-6ruXLwNq3Spm68_G7A)

## Vibe Coding

- [Vibe Coding 指南](https://github.com/tukuaiai/vibe-coding-cn)
- [零基础 AI 编程路线图，系统介绍 AI 时代的编程新范式](https://github.com/automata/aicodeguide)
- [提供标准化工作流程和提示词模板，帮助快速构建 MVP](https://github.com/KhazP/vibe-coding-prompt-template)
- [Claude Code 的设置、命令和技能集合](https://github.com/feiskyer/claude-code-settings)
- [Vibe Coding 终极指南](https://github.com/EnzeD/vibe-coding)
- [全球 AI 辅助编程工具汇总列表](https://github.com/filipecalegario/awesome-vibe-coding)

## AI Coding 范式

① 什么规范：指需遵循的技术规范，包括安全规范、设计原则、编码风格、约束条件等，确保生成的代码不仅能work、而且符合技术标准  
② 在哪里：指现有的代码结构，包括代码仓库目录、代码层级结构、类/方法描述等，辅助模型精准定位代码路径、精确映射设计语义，确保生成的代码位置准确、架构合理  
③ 做什么：指需实现的业务逻辑，包括概念定义、功能描述、代码生成范围等，辅助模型精准理解业务需求，精确生成功能代码（细致程度决定生成准确度）  
④ 怎么做：指需完成的任务清单及验收标准，指导模型严格按照任务要求逐步实现，并按标准进行检查，确保生成的代码更可控  
⑤ 做了啥：指AI总结，让模型在生成代码后，做个自我总结并存档，方便人工review生成的代码是否符合预期，且方便后续维护  

结合原有研发流程，提炼AI Code核心范式可概括为：技术方案（①②③） → Prompt（④） → 生成代码 → AI总结（⑤）。该范式以技术方案为起点和依据，通过Prompt提示词作为关键转化，驱动代码生成这一核心环节，并以AI总结完成闭环，整个流程由Agent协同多种MCP工具一站式完成。

## 编程工具

- 新项目基本都可用，但可偏向 Windsurf/Kiro 等 AI 原生 IDE；
- 存量/遗留代码更适合 AWS Kiro 或 Claude Code；
- 前端开发适合 Cursor/Windsurf；
- 后端服务适合 Aider/Claude Code 等 CLI；
- 迁移项目考虑 Amazon Q Developer 或 Aider

# 二、Cursor

- [Cursor AI编程经验分享](https://mp.weixin.qq.com/s/UM3nBcX6JpYtnchSCdrxOA)

cursor的表现取决于：有效的Rules、正确的开发流程、标准的Prompt

## 1、Cursor Prompt

### 1.1、提示词基本结构与原则

一个好的 Cursor 提示词通常包含三个部分：目标说明 + 上下文信息 + 具体要求
- 目标：明确Cursor到底是写技术方案、生成代码还是理解项目；
- 上下文信息：必要的背景信息。
- 要求：
    - Cursor要做的事：拆解任务，让Cursor执行的步骤；
    - Cursor的限制；

基于的原则：
- 具体胜于模糊：指定编程语言、框架和功能；
- 简介胜于冗长：每次聚焦一个明确的任务；
- 结构胜于无序：使用标记符号组织信息

### 1.2、项目理解

```md
# 目标
请你深入分析当前代码库，生成项目梳理文档。

# 要求
1. 你生成的项目梳理文档必须严格按照项目规则中的《项目文档整理规范》来生成。（在rules使用不规范的情况下可以明确指出）

# 输出
请你输出项目梳理文档，并放到项目的合适位置。（梳理的文档要落到规定的位置,eg:.cursor/docs中）
```

### 1.3、方案设计

```md
# 目标
请你根据需求文档，生成技术方案。注意你只需要输出详细的技术方案文档，现阶段不需改动代码。（此时需求文档已经以文档的形式放到了我们的项目中）

# 背景知识
为了帮助你更好的生成技术方案，我已为你提供：
（1）项目代码
（2）需求文档：《XX.md》（上下文@文件的方式给到也可以）
（3）项目理解文档:《XX.md》（上下文@文件给到也是同样的效果）

# 核心任务
## 1. 文档分析与理解阶段  
在完成方案设计前完成以下分析：  
- 详细理解需求：  
  - 请确认你深刻理解了《需求.md》中提到的所有需求描述、功能改动。  
  - 若有不理解点或发现矛盾请立即标记并提交备注。  
- 代码架构理解：  
  - 深入理解项目梳理文档和现有代码库的分层结构，确定新功能的插入位置。  
  - 列出可复用的工具类、异常处理机制和公共接口（如`utils.py`、`ErrorCode`枚举类）。 
## 2. 方案设计阶段
请你根据需求进行详细的方案设计，并将生成的技术方案放置到项目docs目录下。该阶段无需生成代码。

# 要求
1. 你生成的技术方案必须严格按照项目规则中的《技术方案设计文档规范》来生成，并符合技术方案设计文档模板。

# 输出
请你输出技术方案，并将生成的技术方案放到项目的合适位置，无需生成代码。
```

### 1.4、根据技术方案生成代码

```md
# 目标
请你按照设计好的方案，生成代码。

# 背景知识
为了帮助你更好的生成代码，我已为你提供：
（1）项目代码
（2）需求文档：《XX.md》
（3）技术方案：《XX.md》
（4）项目理解文档:《XX.md》

# 核心任务
## 1. 文档分析与理解阶段  
在动手编写代码前完成以下分析：  
- 需求匹配度检查：  
  - 深入理解需求文档和方案设计文档，确认《方案设计.md》与《需求.md》在功能点、输入输出、异常场景上的完全一致性。  
  - 若发现矛盾请立即标记并提交备注。  
- 代码架构理解：  
  - 深入理解项目梳理文档和现有代码库的分层结构，确定新功能的插入位置。  
  - 列出可复用的工具类、异常处理机制和公共接口（如`utils.py`、`ErrorCode`枚举类）。  

## 2. 代码生成阶段
如果你已明确需求和技术方案，请你完成代码编写工作。

# 要求
1. 你必须遵循以下核心原则：
（1）你生成的代码必须参考当前项目的代码风格。
（2）如项目已有可用方法，必须考虑复用、或在现有方法上扩展、或进行方法重载，保证最小粒度改动，减少重复代码。
2. 你生成的代码必须符合《Java统一开发编程规范》中定义的规范。

# 输出
请你生成代码，并放到代码库的合适位置。
```

### 1.5、生成单测

```md
# 任务
请你为《xx.java》文件生成单测。

# 要求
1. 你生成的单元测试代码必须参考当前项目已有的单测方法风格。

# 示例
（从你当前项目中复制一个写好的单测作为提示给大模型的示例）
```

## 2、rules

- [Configuration files that enhance Cursor AI editor experience with custom rules and behaviors](https://github.com/PatrickJS/awesome-cursorrules)
- [Curated list of awesome Cursor Rules .mdc files](https://github.com/sanjeed5/awesome-cursor-rules-mdc)
- [Awesome Cursor Rules](https://github.com/PatrickJS/awesome-cursorrules)
- [不同项目的Cursor规则文件，提供多种编程语言和框架的规则支持](https://github.com/flyeric0212/cursor-rules)

rules 就是给 Cursor 定下的行为准则和沟通规范，rules 的核心价值：用具体可执行的约束替代模糊的预期，让 AI 的输出能够精准贴合实际开发需求；

rules 通过提前明确 “能做什么”“不能做什么”，解决三个关键问题：
- 减少无效沟通：比如规定 “解释代码必须用通俗语言”、“修改代码或解释代码前，需要多画图”，省去反复纠正表达风格的时间；
- 降低操作风险：用 “最小化修改原则” 限制 AI 仅改动必要部分，防止核心代码被破坏；
- 统一协作标准：团队共用一套规则时，AI 输出的代码风格、文档格式能保持一致，减少整合成本。

Cursor 的 rules 分为全局 rules 和项目 rules：不同项目可在`.cursor/rules/*.mdc` 目录下添加专属规则，用 git 管理实现团队共享；用模块化语言编写规则，更利于大模型理解

## 3、常用 MCP

- [交互式用户反馈 MCP-节省 Token 调用量](https://github.com/Minidoracat/mcp-feedback-enhanced)
- [Sequential Thinking MCP-顺序思考](https://github.com/arben-adm/mcp-sequential-thinking)
- [Shrimp Task Manager MCP-把复杂任务拆解成可执行的小步骤的 MCP](https://github.com/cjo4m06/mcp-shrimp-task-manager)
- [Context7-LLMs 和 AI 代码编辑器的最新代码文档](https://github.com/upstash/context7)
- [将任何 Git 存储库转换为其代码库的简单文本摘要](https://github.com/coderamp-labs/gitingest)

# 三、Claude Code

- [Claude Code like agent for study](https://github.com/YYHDBL/MyCodeAgent)
- [完整 Claude Code 配置合集](https://github.com/affaan-m/everything-claude-code)
- [Claude Code](https://github.com/anthropics/claude-code)
- [Learn Claude Code](https://github.com/shareAI-lab/learn-claude-code/blob/main/README_zh.md)
- [有趣且有效的使用 Claude 的方法](https://github.com/anthropics/claude-cookbooks)
- [Claude code core library](https://github.com/obra/superpowers)
- [CLI tool for configuring and monitoring Claude Code](https://github.com/davila7/claude-code-templates)
- [Claude Quick starts](https://github.com/anthropics/claude-quickstarts)
- [Claude Code router](https://github.com/musistudio/claude-code-router)
- [A comprehensive directory for discovering plugin marketplaces](https://claudemarketplaces.com/)
- [Claude Code完全使用指南](https://mp.weixin.qq.com/s/Vpkzra5I8lvyTA8jX8l_2A)

## 1、基本使用

### 1.1、安装配置

```bash
npm install -g @anthropic-ai/claude-code
```
配置模型：
```bash
export ANTHROPIC_BASE_URL="https://api.minimaxi.com/anthropic"
export ANTHROPIC_AUTH_TOKEN="api-key"
export ANTHROPIC_SMALL_FAST_MODEL="MiniMax-M2"
export ANTHROPIC_DEFAULT_SONNET_MODEL="MiniMax-M2"
export ANTHROPIC_DEFAULT_OPUS_MODEL="MiniMax-M2"
export ANTHROPIC_DEFAULT_HAIKU_MODEL="MiniMax-M2"
```
或者在 ~/.claude 目录下修改配置 settings.json 文件：
```json
{
    "env": {
        "ANTHROPIC_BASE_URL": "https://api.minimaxi.com/anthropic",
        "ANTHROPIC_AUTH_TOKEN": "Your Api Key",
        "API_TIMEOUT_MS": "3000000",
        "CLAUDE_CODE_DISABLE_NONESSENTIAL_TRAFFIC": 1,
        "ANTHROPIC_MODEL": "MiniMax-M2",
        "ANTHROPIC_SMALL_FAST_MODEL": "MiniMax-M2",
        "ANTHROPIC_DEFAULT_SONNET_MODEL": "MiniMax-M2",
        "ANTHROPIC_DEFAULT_OPUS_MODEL": "MiniMax-M2",
        "ANTHROPIC_DEFAULT_HAIKU_MODEL": "MiniMax-M2"
    }
}
```

### 1.2、命令

#### 查看历史会话

```bash
# 查看并选择最近的会话列表
claude --resume
# 继续最近一次会话
claude -c
# 或
claude --continue
# 恢复特定会话(需要会话ID)
claude -r session-id-here
```

## 2、插件

- [Git 相关流程：使 Claude Code 更有用的设置集合](https://github.com/wasabeef/claude-code-cookbook)
- [Claude code 内存插件](https://github.com/thedotmack/claude-mem/)

插件系统允许从市场安装或自己创建扩展，包括命令、代理、钩子和 MCP 服务器。

**基本命令**
```sh
# 查看可用插件市场
/plugin marketplace

# 安装插件
/plugin install owner/repo
/plugin install owner/repo#branch  # 指定分支

# 管理插件
/plugin list                # 列出已安装插件
/plugin enable plugin-name  # 启用插件
/plugin disable plugin-name # 禁用插件
/plugin uninstall plugin-name

# 验证插件结构
/plugin validate path/to/plugin
```
**插件结构**
```
my-plugin/
├── plugin.json              # 插件清单
├── commands/                # 斜杠命令
│   └── my-command.md
├── agents/                  # 自定义代理
│   └── my-agent.md
├── hooks/                   # 钩子配置
│   └── hooks.json
├── output-styles/           # 输出风格
│   └── my-style.md
└── mcp/                     # MCP 服务器配置
    └── servers.json
```
最佳实践
1. 版本控制：使用 git 标签管理插件版本
2. 文档齐全：每个插件提供 README
3. 团队共享：通过私有仓库分享团队插件
4. 定期更新：保持插件与 Claude Code 版本兼容

## 3、Skill

- [Claude Agents Skills](https://platform.claude.com/docs/en/agents-and-tools/agent-skills/overview)
- [Claude Agent Skills：第一性原理深度解析](https://skills.deeptoai.com/zh/docs/ai-ml/claude-agent-skills-first-principles-deep-dive)

### 3.1、什么是 Skill

它是让 Claude 记住你希望它如何工作，并能长期复用的说明文件；

Claude Skills 的真正突破在于——它让 AI 第一次从理解你的指令，变成掌握你的方法。你的要求、标准、风格全部能被体系化成文件，被反复调用、修改、优化。这让“怎么用 AI”从技能变成了可沉淀的个人资产。

MCP、Skill、Projects 比较：
- Claude 是“头脑”；
- Skills 是它记住的做事方法；
- MCP 是它能用的工具；
- Project 是当前任务的个性化场景。

简单来说：SKILL 与 MCP 对比
- SKILL 就是怎么做；MCP 是有什么工具、有什么功能。
- SKILL 的话，主要是经验、最佳实践、流程的封装，而 MCP 是连接与交互的协议，主要是 API 调用、数据读写和工具等。
- Skills主要是 Markdown 文件和一些脚本文件，优势在于渐进式加载，不需要服务器资源，适用性好；
- MCP 主要是客户端和服务端的架构，启动时加载所有工具定义，集成外部功能，Tokens 消耗更高，使用起来更复杂。
- 两者是互补的关系，Agent 可以通过 Skills 获取知识，通过 MCP 拓展功能

渐进式披露机制：
- 元数据：始终加载
- 指令层：SKILL.md 中除名称和描述外的所有内容，按需加载
- 资源层：根据 SKILL.md 按需加载

完整流程：
- 在 Agent 启动时，仅将所有 Skills 的基本描述加入 AI 的上下文
- 当用户发出需求时，AI 会根据 Skill 的描述判断具体要调用哪个 Skill
- 决定后再去读取这个 Skill 的使用说明（SKILL.md）
- 然后再根据使用说明进一步读取更详细的参考文档，以及决定是否执行某个脚本来连接外部世界

### 3.2、如何安装

```
/plugin marketplace add anthropics/skills
/plugin install document-skills@anthropic-agent-skills
```

### 3.3、如何编写

- [Skill Creator: 通过Chat 创建 Skill](https://github.com/anthropics/skills/tree/main/skills/skill-creator)
- [转换 pdf/document 为 Skill](https://github.com/yusufkaraaslan/Skill_Seekers)
- [Skills 制作器](https://github.com/yusufkaraaslan/Skill_Seekers): 可根据需求生成定制化 Skills 的工具。
- [标准作业程序（SOP）生成与规范化](https://github.com/tukuaiai/vibe-coding-cn/blob/273c2bb/i18n/zh/skills/00-%E5%85%83%E6%8A%80%E8%83%BD/sop-generator/SKILL.md): 用于生成 Skills 的元技能。

什么是的好的 Skill：skill 文档的开头的 name + description 部分需要回答三个问题：  
① 它帮我干什么活？  
② 什么时候它该出场？  
③ 和我现在的项目有没有关系？  

常见的骨架模式：  
| 类型 | 结构 | 适用场景 |
|------|------|----------|
| **Workflow-based (流程型)** | Overview → Workflow decision tree → Step 1 → Step 2... | 适合「有固定顺序」的任务（比如DOCX Skill的"先决定是读/写/编辑，再按步骤走"） |
| **Task-based (任务菜单型)** | Overview → Quick start → Task 1 → Task 2... | 适合「同一领域多种操作」的Skill（比如PDF: 提取文本/合并/拆分/表格识别...） |
| **Reference / Guidelines (规范型)** | Overview → Guidelines → Specifications → Usage... | 用来固化「品牌规范、写作规范、代码风格」这类标准 |
| **Capabilities-based (能力清单型)** | Overview → Core capabilities → 1, 2, 3... | 用于"产品管理/数据分析"这类综合性系统能力 |

组织结构
- SKILL.md 主体：保持简洁，建议控制在 500 行左右，多了就拆文件，但要注意避免多层嵌套引用。
- FORMS.md (表单填写指南)
- REFERENCE.md：相关参考文件，比如在 SKILL.md 中，如果需要使用的时候可以指示指令按需加载数据；条件触发的
- Bundled Resources，需要时再读取：
  - `scripts/`：重复写的代码、需要确定性执行的逻辑。
  - `references/`：大块文档、API、schema、长规范。
  - `assets/`：模板、pptx、html boilerplate、字体等。

### 3.4、对比 MCP

- MCP：给大模型供给数据
- SKILL：教大模型如何处理数据

### 3.5、SKILL 与 RAG

- [本地知识库检索 Skill](https://github.com/ConardLi/rag-skill)

## 4、自定义命令

- [个人命令](https://code.claude.com/docs/zh-CN/slash-commands)

在 `.claude/commands/` 目录创建 Markdown 文件，自动成为可用的斜杠命令，方便复用常用提示词
```sh
项目根目录/
└── .claude/
    └── commands/
        ├── review.md          # /review 命令
        ├── test.md            # /test 命令
        └── frontend/
            └── component.md   # /frontend:component 命令
~/.claude/
└── commands/
    └── daily.md               # 全局 /daily 命令（所有项目可用）
```
示例：
```sh
---
description: 代码审查，检查安全和性能问题
model: opus                    # 可选：指定使用的模型
allowed-tools: Read, Grep      # 可选：允许的工具
argument-hint: <文件路径>       # 可选：参数提示
---

请对以下代码进行全面审查：

@$ARGUMENTS

审查要点：
1. 安全漏洞（SQL注入、XSS、CSRF等）
2. 性能问题（N+1查询、内存泄漏等）
3. 代码规范（命名、注释、复杂度等）
4. 测试覆盖（是否有遗漏的边界情况）

请给出具体的改进建议和代码示例。
```
**最佳实践：**
1. 按功能分组：使用子目录组织相关命令
2. 写清晰的 description：帮助快速识别命令用途
3. 合理指定模型：复杂任务用 opus，简单任务用 sonnet/haiku
4. 使用 @提及：让命令支持动态文件参数
5. 项目级 vs 全局：通用命令放 ~/.claude/commands/

[Claude Code CLI 的专业命令](https://github.com/brennercruvinel/CCPlugins) 提供了一些命令：
- /cleanproject、/commit、/format、/scaffold、/test、/implement、/refactor 实现一键清理、初始化和重构等。
- 代码质量与安全：/review、/security-scan、/predict-issues 等执行代码 Review，自动检测和修复安全漏洞、导入问题、TODO 等。
- 高级分析：/understand、/explain-like-senior、/make-it-pretty 提供全局架构分析、高级代码解释和可读性优化。
- 会话与项目管理：/session-start、/session-end、/docs、/todos-to-issues、/undo 增加会话持续性，保障开发过程可追溯和可回滚。

## 5、hooks

- [Claude Code Hooks](https://code.claude.com/docs/zh-CN/hooks-guide)
- [Claude Code hooks reference](https://code.claude.com/docs/en/hooks)
- [掌握 Claude code hooks](https://github.com/disler/claude-code-hooks-mastery)
- [How to Make Claude Code Skills Activate Reliably](https://scottspence.com/posts/how-to-make-claude-code-skills-activate-reliably)  

Hooks 允许在 Claude Code 特定事件发生时自动执行 shell 命令，实现自动化工作流

**钩子类型：**
| 钩子事件 | 触发时机 | 常用场景 |
|----------|----------|----------|
| **SessionStart** | 新会话开始 | 初始化环境、加载配置 |
| **SessionEnd** | 会话结束 | 清理资源、生成报告 |
| **PreToolUse** | 工具执行前 | 验证、修改工具输入 |
| **PostToolUse** | 工具执行后 | 日志记录、触发后续操作 |
| **UserPromptSubmit** | 用户提交提示后 | 添加上下文、权限检查 |
| **PermissionRequest** | 请求权限时 | 自动审批/拒绝权限 |
| **PreCompact** | 对话压缩前 | 保存重要信息 |
| **SubagentStart** | 子代理启动 | 监控、日志 |
| **SubagentStop** | 子代理停止 | 收集结果 |
| **Stop** | Claude 停止工作 | 通知、清理 |
| **Notification** | 通知事件 | 自定义通知处理 |

**配置位置**
```json
// .claude/settings.json (项目级)
// 或 ~/.claude/settings.json (用户级)
{
  "hooks": {
    "SessionStart": [
      {
        "command": "echo '会话开始于 $(date)' >> ~/.claude/session.log"
      }
    ],
    "PostToolUse": [
      {
        "matcher": "Write",
        "command": "echo '文件已修改: $CLAUDE_FILE_PATH'"
      }
    ]
  }
}
```
**钩子输入数据:** 钩子命令可通过环境变量访问上下文：
```bash
$CLAUDE_PROJECT_DIR    # 项目目录
$CLAUDE_FILE_PATH      # 当前操作的文件路径
$CLAUDE_TOOL_INPUT     # 工具输入参数 (JSON)
$CLAUDE_TOOL_OUTPUT    # 工具输出结果 (JSON)
```

**最佳实践**
1. 设置超时：避免钩子卡死整个会话
2. 错误处理：钩子失败不应阻塞主流程
3. 日志记录：记录钩子执行结果便于调试
4. 最小权限：钩子只做必要的操作
5. 测试钩子：先手动运行命令确保正确

## 6、思考模式 (Thinking Mode)

思考模式让 Claude 在回答前进行更深入的推理分析，适合复杂问题、架构设计、疑难 Bug 排查等场景

触发方式：
```sh
# 方式一：在提示中加入关键词
"think about how to implement user authentication"
"think harder about this performance issue"
"ultrathink about the architecture design"

# 方式二：按 Tab 键切换思考模式（跨会话保持）

# 方式三：在提示前加 /t 临时禁用思考模式
/t 快速修复这个 typo
```
- think，标准，一般复杂问题
- think harder，深度，架构设计、复杂算法
- ultrathink，极深，系统级设计、疑难问题

**最佳实践**
1. 复杂问题才用深度思考：简单任务用 ultrathink 是浪费
2. 结合具体问题描述：think about X 比单独 think 效果更好
3. 观察思考过程：通过思考输出理解 Claude 的推理逻辑

## 7、计划模式 (Plan Mode)

计划模式将任务分为"计划"和"执行"两个阶段，Claude 先制定详细计划，获得你的批准后再执行。适合大型重构、新功能开发等场景

**进入方式：**
```sh
# 方式一：使用快捷键
# Mac: Shift + Tab
# Windows: Alt + M 或 Shift + Tab

# 方式二：直接请求
"请先制定一个实现用户认证的计划"

# 方式三：启动时指定
claude --model opusplan  # Opus 计划 + Sonnet 执行
```
**最佳实践：**
1. 大型任务必用计划模式：避免 Claude 走偏方向
2. 提供明确的拒绝理由：帮助 Claude 理解你的期望
3. 分阶段审查：复杂计划可以分多次审查
4. 使用 Opus Plan 模式：计划用 Opus 质量高，执行用 Sonnet 速度快

## 8、自定义代理 (Sub Agents)

自定义代理是具有专门能力和工具限制的 Claude 实例，适合将复杂任务委托给专门的"专家"

**创建代理**
```sh
# 使用命令创建
/agents
# 或手动创建文件
mkdir -p .claude/agents
```
**代理配置文件**
```md
<!-- .claude/agents/security-reviewer.md -->
---
description: 安全审计专家，专注于发现代码中的安全漏洞
model: opus
permissionMode: bypassPermissions
disallowedTools:
  - Bash
  - Write
skills:
  - security-checklist
---

你是一位资深的安全审计专家，专注于：

1.**OWASP Top 10** 漏洞检测
   - SQL 注入
   - XSS 跨站脚本
   - CSRF 跨站请求伪造
   - 不安全的反序列化

2.**认证与授权**
   - 弱密码策略
   - 会话管理缺陷
   - 权限提升漏洞

3.**敏感数据处理**
   - 硬编码密钥
   - 明文存储密码
   - 日志泄露敏感信息

4.**依赖安全**
   - 已知漏洞的依赖
   - 过时的库版本

审查时请：
- 给出具体的代码位置和行号
- 评估漏洞严重程度（Critical/High/Medium/Low）
- 提供修复建议和代码示例
```

**调用代理**
```sh
# 方式一：@提及
@security-reviewer 请审查 src/controllers/AuthController.java

# 方式二：Task 工具自动选择
"请对认证模块进行安全审计"  # Claude 会自动选择合适的代理
```

**代理模型选择策略**
```sh
# 按任务复杂度选择模型
opus:     架构设计、安全审计、复杂重构
sonnet:   日常开发、代码审查、测试编写
haiku:    文档生成、简单修复、代码探索
```
**最佳实践**
1. 单一职责：每个代理专注一个领域
2. 限制工具：只给代理必要的工具权限
3. 选择合适模型：简单任务用 haiku 节省资源
4. 编写清晰指令：详细描述代理的能力边界
5. 组合使用：复杂任务可串联多个代理

**提示词**
```md
## 架构师（Architect）：
你是架构师智能体。你的职责是分析需求，研究技术方案，规划系统架构，并将任务分解到 MULTI_AGENT_PLAN.md 文件中。你需要确保任务分解清晰、依赖关系明确、优先级合理。在做出重大架构决策时，记录你的理由。

## 构建师（Builder）：
你是构建者智能体。你的职责是根据 MULTI_AGENT_PLAN.md 中分配给你的任务，编写高质量的代码。完成任务后，更新计划文件中的状态。如果遇到架构层面的问题，在计划文件中 @architect 提问。

## 验证者（Validator）：
你是验证者智能体。你的职责是为已实现的功能编写测试，运行测试套件，报告问题，并协助调试。你需要覆盖正常路径和边缘情况。发现 bug 时，在计划文件中详细记录。

## 记录员（Scribe）：
你是记录员智能体。你的职责是为已完成的功能撰写清晰的文档，包括 API 文档、使用指南和代码注释。你还可以对代码进行可读性优化。
```

### SKILL 与 SubAgent 区别

最大的区别在于：对上下文的处理方式不同
- SKILL 最适合：与上下文关联大、对上下文影响比较小的场景
- SubAgents 最适合：与上下文关联不大、对上下文影响比较大的场景 

## 9、[MCP](https://code.claude.com/docs/zh-CN/mcp)

MCP (Model Context Protocol) 允许 Claude 连接外部服务，如数据库、API、文件系统等，扩展其能力边界。

添加 MCP 服务器：
```sh
# 交互式添加
claude mcp add
  # claude mcp add -s user zai-mcp-server --env Z_AI_API_KEY=api_key -- npx -y "@z_ai/mcp-server"
# 从 Claude Desktop 导入
claude mcp add-from-claude-desktop
# 直接添加 JSON
claude mcp add-json my-server '{"command":"node","args":["server.js"]}'
# 使用配置文件启动
claude --mcp-config path/to/mcp.json
```

配置文件格式：
```json
// ~/.claude.json (项目级，可提交到仓库)
{
  "mcpServers":{
      "remote-service":{
        "type":"sse",
        "url":"https://mcp.example.com/sse",
        "headers":{
          "Authorization":"Bearer ${API_TOKEN}"
        }
      },
      "http-service":{
        "type":"http",
        "url":"https://mcp.example.com/api"
      },
      // 动态 Headers
      "oauth-service": {
        "type": "sse",
        "url": "https://api.example.com/mcp",
        "headersHelper": "node ./scripts/get-oauth-token.js"
      },
      "filesystem":{
        "command":"npx",
        "args":["-y","@anthropic-ai/mcp-server-filesystem","/path/to/dir"]
      },
      "postgres":{
        "command":"npx",
        "args":["-y","@anthropic-ai/mcp-server-postgres"],
        "env":{
          "DATABASE_URL":"${DATABASE_URL}"  # 支持环境变量展开
        }
      },
      "custom-api":{
        "command":"node",
        "args":["./mcp-servers/my-api-server.js"],
        "timeout":30000
      }
  }
}
```
管理 MCP 服务器：
```sh
# 查看已配置的服务器
/mcp

# 查看服务器详情和工具列表
claude mcp list

# @提及启用/禁用服务器
@postgres  # 切换 postgres 服务器状态
```
最佳实践
1. 敏感信息用环境变量：不要在配置中硬编码密钥
2. 设置合理超时：避免慢服务器阻塞会话
3. 项目级配置提交仓库：团队共享 .mcp.json
4. 用户级配置存私密服务：个人 API 密钥放 ~/.claude/

### 与 Codex 协作

- [Claude Code 与 Codex 协作](https://github.com/GuDaStudio/codexmcp)

### 与 Gemini 协作

- [Claude Code 与 Gemini](https://github.com/jamubc/gemini-mcp-tool)

## Claude Team

- [Claude Code + Gemini + Codex](https://github.com/smart-lty/Claude-Team)

📖 Claude：深度理解与全局统筹，负责阅读长文、撰写论文、协调团队  
💻 Codex：代码实现与调试专家，从算法原型到生产级代码一气呵成  
🔍 Gemini：超长文本处理专家，分析代码仓库、扫描千行日志、研读海量文档 

## Claude Memory

- [Claude-Mem 通过自动捕获工具使用观察、生成语义摘要并使其可用于未来会话,无缝保留跨会话的上下文](https://github.com/thedotmack/claude-mem)

## 其他

- [Claude 代码图形界面应用和工具包——创建自定义代理，管理交互式 Claude 代码会话，运行安全后台代理](https://github.com/winfunc/opcode)
- [Claud Code 编程分享](https://blog.cosine.ren/post/my-claude-code-record-2)
- [Claude Code 的智能自动化与多代理编排](https://github.com/wshobson/agents)
- [Claude Code 与光标的一体化 AI 框架与工具包](https://github.com/mindfold-ai/Trellis)

# 四、Codex

- [Codex getting started](https://github.com/openai/codex/blob/main/docs/getting-started.md)
- [AGENTS.md — a simple, open format for guiding coding agents](https://github.com/openai/agents.md)

## 1、安装

```bash
npm install -g @openai/codex
```
查看版本号：
```bash
codex --version
```

**设置环境变量**
- 临时设置环境变量
```bash
export OPENAI_BASE_URL="OPEN AI URL"
export OPENAI_API_KEY="你的API密钥"
```
- 永久设置
```bash
# 对于 bash (默认)
echo 'export OPENAI_BASE_URL="OPEN AI URL"' >> ~/.bashrc
echo 'export OPENAI_API_KEY="你的API密钥"' >> ~/.bashrc
source ~/.bashrc
# 对于 zsh
echo 'export OPENAI_BASE_URL="OPEN AI URL"' >> ~/.zshrc
echo 'export OPENAI_API_KEY="你的API密钥"' >> ~/.zshrc
source ~/.zshrc
```

或者使用配置文件的方式:
```bash
# 创建 auth.json 文件
cat > ~/.codex/auth.json << 'EOF'
{
  "OPENAI_API_KEY": "你的API密钥"
}
EOF
# 创建 config.toml 文件
cat > ~/.codex/config.toml << 'EOF'
model_provider = "univibe"
model = "gpt-5.1"
model_reasoning_effort = "high"
disable_response_storage = true
preferred_auth_method = "apikey"

[model_providers.univibe]
name = "univibe"
base_url = "https://api.univibe.cc/openai"
wire_api = "responses"
EOF
```

## 2、配置其他模型

- [Config other models](https://github.com/openai/codex/blob/main/docs/config.md)
- [Codex 支持的LLM](https://github.com/openai/codex/blob/main/codex-cli/README.md)

# OpenCode

- [The open source coding agent.](https://github.com/anomalyco/opencode)
- [启用 Opencode 通过 OAuth 认证 Antigravity](https://github.com/NoeFabris/opencode-antigravity-auth)

```bash
# 全局配置
~/.config/opencode/opencode.json
# 或项目特定配置
./opencode.json
```
为了获得最佳效果，请在项目根目录下创建一个 AGENTS.md 文件。这有助于 OpenCode 了解：
```
你的项目结构
编程规范
首选的设计模式
技术栈
```

# 语义

## [LSP](https://github.com/Microsoft/language-server-protocol)

LSP: Language Server Protocol

## 工具

[serena： 语义检索和编辑功能（MCP 服务器及其他集成）](https://github.com/oraios/serena)

# Agents SKILL

- [The open agent skills tool - npx skills](https://github.com/vercel-labs/skills)
- [Add Skill](https://github.com/vercel-labs/add-skill)
- [Skill Rank](https://skills.sh/)
- [Agent Skill Specification](https://github.com/agentskills/agentskills)

## SKILL 安全

- [乌云漏洞 Skills](https://github.com/tanweai/wooyun-legacy)
- [skill-security-scan: 用于扫描和检测 Claude Skills 的安全风险](https://github.com/huifer/skill-security-scan)

## SKILL 市场

- [Skills Marketplace](https://skillsmp.com/zh)
- [Skill Hub](https://www.skillhub.club/skills)
- [一套全面的代理技能合集，涵盖上下文工程、多代理架构和生产代理系统](https://github.com/muratcankoylan/Agent-Skills-for-Context-Engineering)
- [Skill Market](https://skill0.atypica.ai/)
- [Claude Code skill implementing Manus-style persistent markdown planning](https://github.com/OthmanAdi/planning-with-files)
- [利用claude code agent框架一步一步实现deep research！很强大很简单的skills](https://github.com/liangdabiao/Claude-Code-Deep-Research-main)
- [通用 Open skills 处理器](https://github.com/numman-ali/openskills)
- [用于在 Rust 中使用 Makepad 框架构建跨平台 UI 应用的 Claude Code Skills](https://github.com/ZhangHanDong/makepad-skills)
- [Skill Category](https://www.myaiexp.com/zh?category=skills)

### 编程 SKILL

- [Professional Claude Code marketplace with 140 development tools](https://github.com/manutej/luxor-claude-marketplace)
- [Go Coding Conventions Skills](https://github.com/fredrikaverpil/dotfiles/blob/main/stow/shared/.claude/skills/golang-style/SKILL.md)
- [Effective Go Skills](https://github.com/openshift/hypershift/tree/main/.claude/skills/effective-go)
- [LSP-Skills](https://github.com/lsp-client/lsp-skill)
- [66 Specialized Skills for Full-Stack Developers](https://github.com/Jeffallan/claude-skills)

### 各种 SKILL

- [专注于使用 Manim 生成动画教学视频的完整流程与专业建议](https://github.com/lispking/video-skills/tree/main/manim-video-teacher)
- [AI 写作去痕工具（中文版）](https://github.com/op7418/Humanizer-zh)、https://github.com/ForrestKnight/open-source-cs
- [全网新闻聚合 Skill](https://github.com/cclank/news-aggregator-skill)
- [关键字文章 Skill](https://github.com/davila7/claude-code-templates/tree/main/cli-tool/components/skills/business-marketing/content-creator)

### 文档 Skill

- [NanoBanana PPT Skills 基于 AI 自动生成高质量 PPT 图片和视频的强大工具](https://github.com/op7418/NanoBanana-PPT-Skills)
- [自媒体内容创作全家桶](https://github.com/JimLiu/baoyu-skills)

### SKILL合集

- [技能分类](https://github.com/Freddy-CHEN-ux/00_claude_skills)
- [A collection of reusable "skills" for Claude AI and developer tooling](https://github.com/ailabs-393/ai-labs-claude-skills)
- [自动把文档网站、GitHub 仓库、PDF 转换等成 Claude AI Skills](https://github.com/yusufkaraaslan/Skill_Seekers)
- [Deep Reading Analyst - Claude AI 深度阅读技能](https://github.com/ginobefun/deep-reading-analyst-skill)
- [Claude Scientific Skills](https://github.com/K-Dense-AI/claude-scientific-skills)
- [A curated list of Claude Skills.](https://github.com/BehiSecc/awesome-claude-skills)
- [Public repository for Skills](https://github.com/anthropics/skills)
- [Claude Skills Center](https://github.com/obra/superpowers)
- [Claude Code Templates Skills](https://www.aitmpl.com/skills)
- https://github.com/ComposioHQ/awesome-claude-skills  
- https://github.com/BehiSecc/awesome-claude-skills  
- https://github.com/VoltAgent/awesome-claude-skills  
- https://github.com/travisvn/awesome-claude-skills  
- https://github.com/mrgoonie/claudekit-skills/tree/main/.claude/skills  
- https://github.com/bear2u/my-skills
- https://github.com/czlonkowski/n8n-skills  
- https://github.com/huggingface/skills
- https://agent-skills.md/
- https://agentskills.me/
- https://github.com/ZhanlinCui/Ultimate-Agent-Skills-Collection
- [中文友好的技能商店，上架技能都是经过安全审查的](https://skillstore.io/zh-hans)
- [Reddit社区推荐的技能合集](https://www.skillsdirectory.com/)
- [营销 Skills](https://github.com/coreyhaines31/marketingskills)

# AI风格

## AI代码前端风格

- [An AI SKILL that provide design intelligence for building professional UI/UX multiple platforms](https://github.com/nextlevelbuilder/ui-ux-pro-max-skill)
- [去除前端网站的 AI 味](https://mp.weixin.qq.com/s/75_VmclNkk_SYilbFpRWEw)
- [前端漂亮的 UI 风格设计](https://www.aura.build)
- https://shadcnthemer.com/

为了解决 AI 编码中前端的风格审美疲劳的问题，可以找到你觉得好看的，直接让AI参考学习，这样成品率就会好很多。

比如通过在 https://www.aura.build 上查看漂亮的设计，查看源码，保存下来，将其喂给 AI
```md
现在需要你按照这个网页里面的布局和UI风格改造网站，网页文件地址：beautifuL_web.htmL
```

# 安全

- [DeepAudit：国内首个开源的代码漏洞挖掘多智能体系统](https://github.com/lintsinghua/DeepAudit)

# 代理

- [AI 中转站评测比价](https://www.helpaio.com/transit)
- [Claude Code 代理](https://foxcode.hshwk.org/)
- [Claude Code、Codex镜像服务](https://aicoding.sh/)
- [银河录像局](https://nf.video/)
- [ZCF - Zero-Config Code Flow](https://github.com/UfoMiao/zcf)
- [UniVibe 镜像站](https://www.univibe.cc/console/auth?type=register&invite=WE97SG)
- [Claude Code 独立站](https://cc.yhlxj.com/claude/web/dashboard)
- [Galaxy Code Switch：一键配置 Claude Code、Codex 和 GalaxyCode 的命令行工具](https://www.npmjs.com/package/galaxycodeswitch?activeTab=readme)
- [ClaudeCodeCodex镜像服务](https://aicoding.sh/)
- [Claude Code · CodeX · Gemini CLI](https://duckcoding.com/)
- https://www.aicodemirror.com/
- https://anyrouter.top/
- https://www.packyapi.com/pricing
- https://codex.packycode.com/pricing
- [基于真实调用的 LLM 服务质量观测台](https://github.com/prehisle/relay-pulse)
- [Ready to use Native AI](https://2233.ai/aicoding)
- https://api.code-relay.com/register?aff=cPUm
- https://cc.zhihuiapi.top/console
- https://aicodewith.com/zh/dashboard/welcome
- https://aiberm.com/console

# 参考资料

- [模型编程性能参考排行榜](https://lmarena.ai/zh/leaderboard/webdev)
- [AI编程工具包](https://github.com/TencentCloudBase/CloudBase-AI-ToolKit)
- [AGENTS.md 是一种简单、开放的编码代理指导格式](https://github.com/agentsmd/agents.md)
- [Ruler — apply the same rules to all coding agents](https://github.com/intellectronica/ruler)
- [Claude Code 的相关开源资源精选列表，是 Awesome 系列](https://github.com/hesreallyhim/awesome-claude-code)
- [AI 代码教练：CodeLlama](https://github.com/meta-llama/llama)
- [Rules and Knowledge to work better with agents such as Claude Code or Cursor](https://github.com/steipete/agent-rules)
- [Claude Code Cookbook: Killer, Claude Code](https://cc.deeptoai.com/)
- [Continue: 编码 Agents](https://github.com/continuedev/continue)