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

## 3、SDD：规范驱动开发

- [SDD:规范驱动开发的工具包](https://github.com/github/spec-kit)
- [规范驱动开发（SDD）全解：从理念到实践](https://blog.dengqi.org/posts/%E8%A7%84%E8%8C%83%E9%A9%B1%E5%8A%A8%E5%BC%80%E5%8F%91sdd%E5%85%A8%E8%A7%A3%E4%BB%8E%E7%90%86%E5%BF%B5%E5%88%B0%E5%AE%9E%E8%B7%B5%E7%BF%BB%E8%AF%91/)

规范驱动开发 (Spec-Driven Development， SDD)，其核心理念似乎是指在利用 AI 编写代码之前，首先编写一份规范(spec)，即文档先行。这份规范将成为人类开发者与 AI 共同的事实来源。

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

- [使 Claude Code 更有用的设置集合](https://github.com/wasabeef/claude-code-cookbook)
- https://github.com/anthropics/claude-cookbooks
- [自动把文档网站、GitHub 仓库、PDF 转换等成 Claude AI Skills](https://github.com/yusufkaraaslan/Skill_Seekers)
- [Deep Reading Analyst - Claude AI 深度阅读技能](https://github.com/ginobefun/deep-reading-analyst-skill)
- [Claude Skills Marketplace](https://skillsmp.com/zh)
- [Claude Scientific Skills](https://github.com/K-Dense-AI/claude-scientific-skills)
- [Claude code core library](https://github.com/obra/superpowers)
- [Claude Agent Skills: A First Principles Deep Dive](https://leehanchung.github.io/blogs/2025/10/26/claude-skills-deep-dive/)
- [Claude Agent Skills：第一性原理深度解析](https://skills.deeptoai.com/zh/docs/ai-ml/claude-agent-skills-first-principles-deep-dive)

## 1、安装配置

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

# AI风格

## AI代码前端风格

- [前端漂亮的 UI 风格设计](https://www.aura.build)

为了解决 AI 编码中前端的风格审美疲劳的问题，可以找到你觉得好看的，直接让AI参考学习，这样成品率就会好很多。

比如通过在 https://www.aura.build 上查看漂亮的设计，查看源码，保存下来，将其喂给 AI
```md
现在需要你按照这个网页里面的布局和UI风格改造网站，网页文件地址：beautifuL_web.htmL
```

# 工具代理

- [Claude Code、Codex镜像服务](https://aicoding.sh/)
- [银河录像局](https://nf.video/)
- https://github.com/UfoMiao/zcf
- [UniVibe 镜像站](https://www.univibe.cc/console/auth?type=register&invite=WE97SG)
- [Claude Code 独立站](https://cc.yhlxj.com/claude/web/dashboard)
- [Galaxy Code Switch：一键配置 Claude Code、Codex 和 GalaxyCode 的命令行工具](https://www.npmjs.com/package/galaxycodeswitch?activeTab=readme)
- [ClaudeCodeCodex镜像服务](https://aicoding.sh/)
- [Claude Code · CodeX · Gemini CLI](https://duckcoding.com/)
- https://www.aicodemirror.com/
- https://anyrouter.top/
- https://www.packyapi.com/pricing
- https://codex.packycode.com/pricing

# 参考资料

- [Ruler — apply the same rules to all coding agents](https://github.com/intellectronica/ruler)
- [Claude Code 的相关开源资源精选列表，是 Awesome 系列](https://github.com/hesreallyhim/awesome-claude-code)
- [AI 代码教练：CodeLlama](https://github.com/meta-llama/llama)
- [Ready to use Native AI](https://2233.ai/aicoding)
- [Rules and Knowledge to work better with agents such as Claude Code or Cursor](https://github.com/steipete/agent-rules)
- [Killer, Claude Code](https://cc.deeptoai.com/)
- https://platform.minimaxi.com/docs/guides/text-ai-coding-tools
