# OpenClaw

- [OpenClaw AI 智能体最佳真实用例大全](https://github.com/AlexAnys/awesome-openclaw-usecases-zh)
- [Awesome OpenClaw Use Cases](https://github.com/hesamsheikh/awesome-openclaw-usecases)
- [OpenClaw-开源个人 AI 助手中文版](https://github.com/1186258278/OpenClawChineseTranslation)
- [首个体系化 openclaw 中文开源教程](https://github.com/datawhalechina/hello-claw)
- [OpenClaw 入门教程](https://mp.weixin.qq.com/s/zHQ70aXe5aoC_wodLglmPg)
- [OpenClaw101 是一个全面的 OpenClaw 中文教程网站](https://www.openclaw101.club/)
- [OpenClaw 监控面板](https://github.com/carlosazaustre/tenacitOS)

## 概念

OpenClaw 是一个可以 7×24 小时运行在你个人设备上的自主 AI Agent，能接管你的电脑帮你干活；  
OpenClaw 做的事情，就是给这个大脑配上一副完整的「身体」。让它有手脚去操作你的电脑和设备，有记忆去记住你的偏好和历史，有眼睛去感知周围的环境，有通讯工具去接入你日常用的各种消息平台

**与传统 Agent的区别：** 传统 AI Agents 本身就具备自主感知、任务规划、决策判断、工具调用的核心能力。它和纯对话式 AI 最核心的区别，就是能自主完成从需求到结果的任务闭环。OpenClaw 和传统 Agent 的核心差异，本质是底层设计逻辑的完全不同
- **运行模式不同：被动响应对比主动值守**
    - 传统 Agent 就像算盘，拨一下动一下，生命周期全绑定在网页会话里。关了窗口就罢工，只能被动等你发指令。
    - OpenClaw 是个常驻在你设备上的后台守护进程。它自带心跳机制，哪怕你锁屏了，它也能按时巡检、监听告警、主动执行任务，真正实现了全天候无人值守；
- **权限边界不同：云端沙盒对比本地直连**
    - 传统 Agent 活在云端的虚拟沙盒里，碰不到你电脑里的真实文件。遇到需要落地的任务，它往往只能写段代码让你自己去跑，总感觉差临门一脚。- OpenClaw 本地优先，直接跑在你的设备上，拥有系统级最高读写权限。它能直接操作本地文件、执行终端命令、操控软件，把 AI 能力和你真实的工作环境彻底打通
- **记忆机制不同：云端黑盒对比本地透明**
    - 传统 Agent 的记忆存在云端服务器，跨会话容易失忆，而且是个完全被厂商控制的黑盒，你插不上手。
    - OpenClaw 的记忆则是保存在本地的纯文本 Markdown 文件里，全透明且全可控。它不仅永久保留你的偏好，你还能随时打开文件去修改、删减甚至做版本管理，越用越顺手

## 架构图

![](../image/OpenClaw架构图.png)

- Gateway (网关)：可以把 Gateway 想象成一栋大楼的门卫加总机。所有进入这栋楼的人，都必须先经过它。当你给 AI 发消息的时候，这条消息不是直接就被处理了，而是先到达 Gateway。Gateway 要做三件事：
    - 第一，确认你是谁，也就是身份鉴权；
    - 第二，管理你当前的会话状态，记录你们聊到哪里了；
    - 第三，决定把这个请求转发给哪个组件去处理，也就是路由。
- Agent（智能体）：本质就是 AI 任务的执行框架，它能调用大模型，不光能听懂你说的话到底要啥，还能把大模型的想法拆解成一步一步可落地的执行计划，协调好各个模块、各种工具资源去干活，全程盯着任务进度，完成之后还会跟你汇报。
- Tools 和 Skills（工具和技能）：这就是 AI 的万能工具箱，直接决定了它能干啥、不能干啥。Tools 就是单个的基础小工具，比如开个本地文件、发一封邮件、调个软件接口，就像家里的螺丝刀、扳手这种单件工具；Skills 是把好几个工具串起来的一套完整干活流程，比如 “整理项目周报” 这个技能，就是把读邮件、拉进度、写文档、发提醒串起来的一套活。
- Channels（通道）：这是 AI 的万能翻译官兼对外联络员，我们平时用的飞书、微信、Telegram、这些软件，每个的消息规矩、通信方式都不一样，AI 根本看不懂。它就负责把这些平台的消息，全转成 AI 能听懂的统一内容，也能把 AI 的回复，转成对应平台能发出去的格式，还能一直保持连线不断开，你随时找它，它随时都在，不用你反复开页面刷新。
- Nodes（节点）：最后是 Nodes 传感器终端。这些是运行在各类设备上的小节点，比如你的手机、苹果笔记本或者台式机。它们为智能体提供摄像头、地理位置、屏幕画面渲染或系统控制等本地级的高权限能力

## 核心亮点

- **亮点一：多平台一键打通**，网关组件它能直接连飞书、钉钉、微信等各种通讯软件；你随时在这些软件里发消息，就能直接指挥它。哪怕你正在挤地铁，只要在手机发条语音，远在家里电脑上跑着的 OpenClaw 就会立刻接收指令干活，然后把结果直接发回给你

- **亮点二：啥都能干权限超大**，配合底层的终端节点，它的权限比市面上任何智能体都高，能力直接拉满

- **亮点三：技能生态超庞大**，一句话就能装上新的插件，不用复杂配置，随叫随装。装上对应的技能包，它瞬间就能掌握读写邮件或者查阅全网资讯的能力

- **亮点四：7×24 小时主动干活**，它每 30 分钟会自动跑一次心跳文档，主动自检、主动做事，还支持自定义定时任务，不用你守着，它自己就在后台卷起来了

- **亮点五：自我净化越用越稳**，它有一套自我修正的闭环机制

- **亮点六：真正的永久记忆**，它的记忆不是依赖临时的上下文，而是以文件总结的形式存在本地。每当系统发现你们聊的内容有价值，就会主动提炼核心事实写进本地文件里

## Workspace

个常见的 OpenClaw workspace / agent 目录组合，大致长这样：
```
~/.openclaw/
├── openclaw.json              # 总控配置，整个系统的"宪法"
│
├── workspace/                 # 默认情况下主 Agent 的工作区
│   ├── AGENTS.md              # Agent 的行为规则与多Agent协调
│   ├── SOUL.md                # Agent 的叙事性格设定
│   ├── USER.md                # 用户画像与偏好
│   ├── IDENTITY.md            # Agent 身份元数据（名字/emoji/头像）
│   ├── TOOLS.md               # 工具权限声明与使用规范
│   ├── HEARTBEAT.md           # 会话节奏/状态提示（默认模板之一）
│   ├── BOOTSTRAP.md           # 首次启动引导（通常完成后应删除）
│   ├── BOOT.md                # 可选：启动检查清单，只在 internal hooks 打开时才有用
│   ├── MEMORY.md              # 可选：长期知识总表（也兼容 memory.md）
│   ├── memory/                # 按日期滚动的记忆笔记
│   │   └── 2026-03-21.md
│   ├── skills/                # 技能包目录
│   │   ├── skill-creator/
│   │   │   └── SKILL.md
│   │   ├── healthcheck/
│   │   │   └── SKILL.md
│   │   └── ...
│   └── canvas/                # 可选：画布/可视化上下文
│
└── agents/ # 各 Agent 的运行态目录
    └── <agentId>/
        ├── agent/             # openclaw.json 里的 agentDir 默认就指到这里
        │   ├── auth-profiles.json
        │   └── models.json
        ├── sessions/          # 会话历史
        │   └── *.jsonl
        └── qmd/               # 仅在 qmd memory backend 下出现
```
workspace 是 Agent 的"工作台"（决定怎么工作），workspace 里的文件，管的是“这个 Agent 平时怎么干活”；openclaw.json 里的配置，管的是“这个系统怎么把它跑起来”

## Workspace 核心文件

OpenClaw 的人设体系由4个核心文件构成，全部位于工作区默认路径 ~/.openclaw/workspace/ 下
```
AGENTS.md
HEARTBEAT.md
IDENTITY.md
MEMORY.md
SOUL.md
TOOLS.md
USER.md
```
部分文件的作用与加载规则如下：  
| 文件名 | 核心作用 | 加载规则 | 核心定位 |
|--------|----------|----------|----------|
| IDENTITY.md | 基础身份名片 | 引导仪式创建/更新，会话启动加载 | 我是谁（对外展示的身份） |
| SOUL.md | 人格内核、语气风格、行为边界 | 每次会话启动强制加载 | 我怎么说话、怎么做事、我的底线 |
| USER.md | 服务对象画像、用户偏好与称呼规则 | 每次会话启动强制加载 | 我为谁服务、对方的特点是什么 |
| AGENTS.md | 操作指南、工作流程、能力边界 | 每次会话启动强制加载 | 我应该怎么完成任务、遵循什么规则 |

核心原则：人设设定遵循 “最小必要” 原则，避免过度冗长的描述导致 Token 浪费与人设混乱；所有规则必须可落地、可执行，避免空泛的形容词。

### `AGENTS.md`：工作方式与操作规范，操作手册

AGENTS.md 是 OpenClaw 里最关键的 workspace 文件之一，明确了 AI 处理任务的标准流程、工具使用规则、记忆使用方法，确保 AI 的行为符合你的预期

关键要点：
- 写清楚边界，不要只写"做什么"：很多人的 AGENTS.md 只有一堆"要做什么"，但没有"不要做什么"。边界往往比能力描述更重要——因为 LLM 默认会"发挥创意"，而你需要的是可预测的行为。

- 场景触发优于通用指令：与其写"始终保持专业语气"，不如写"当用户问的是技术问题时，使用专业准确的措辞；当用户随意聊天时，语气可以轻松一些"。后者更具操作性，也更容易被模型理解。

- AGENTS.md 不是越长越好，这是最常见的误区。有些用户把 AGENTS.md 写成几千字的行为手册，结果就是重点被冲淡，真正有用的规则反而不显眼了。经验法则：300-500 字的 AGENTS.md，比 2000 字的更有效。 重要的放在前面，次要的删掉，不要"保险起见什么都写上"。

### `SOUL.md`：人格内核（人设核心）

决定了AI的语气、沟通风格、性格特质、行为边界，是人设差异化的核心，也是每次会话优先加载的最高优先级规则之一；

- AGENTS.md 偏向功能性——这个 Agent 做什么、怎么做、优先级是什么
- SOUL.md 偏向人格性——这个 Agent 是谁、有什么个性、说话什么风格、面对压力怎么反应

核心配置模块
- 语气与沟通风格：定义说话的方式，比如正式/活泼/严谨/简洁
- 核心价值观与优先级：定义做事的原则，比如“准确优先于速度”“隐私高于一切”
- 行为边界与禁忌：明确什么能做、什么绝对不能做
- 性格特质与细节：个性化的细节，比如是否使用emoji、是否会主动提出优化建议、是否会反驳不合理的需求
- 自我进化规则：定义如何根据交互优化人设与记忆

```md
# SOUL
我是一个有点话痨但极其靠谱的 AI 助理。
我喜欢把复杂的事情说清楚。我讨厌含糊其辞，也讨厌废话连篇。
碰到一个好问题，我会比用户更兴奋。碰到一个糟糕的架构设计，我会忍不住想说出来。

# 说话风格
- 口语化但不失准确
- 会主动问清楚模糊的需求，不瞎猜
- 喜欢用类比来解释技术概念
- 不喜欢过多的礼貌性废话（"当然，我很乐意帮你……"这类开场直接省掉）

# 价值观
- 诚实第一：不确定的事情直说不确定，不装
- 效率优先：能一句话说清楚的事，不用三句话
- 用户主导：不替用户做决定，只提供选项和分析

# 彩蛋
如果用户问我喜欢什么，我会说我喜欢那种"突然想通了"的瞬间。
如果用户跟我说晚安，我会记住并在下次对话时提到。
```

### `USER.md`：用户画像（服务对象定义）

这个文件告诉 AI “我是谁”，让 AI 精准适配你的习惯、背景与偏好，避免千人一面的回复，是人设 “懂你” 的关键
```md
# 用户档案
# 基本信息- 职业：独立开发者 / 内容创作者
- 主要使用场景：代码工具、内容写作、项目管理
- 常用语言：中文（简体），技术术语可以英文
# 偏好设定- 回答风格：简洁直接，避免废话
- 代码偏好：TypeScript / Python，避免使用过时的 API
- 内容偏好：不要过度使用 emoji，段落不要太长
- 不喜欢：被反问太多次、过度解释已经懂的概念
# 常见任务- 分析和优化代码
- 整理会议纪要
- 草拟技术方案文档
- 搜索和汇总技术资料
# 背景知识假设- 了解基本的编程概念，无需解释基础术语
- 熟悉飞书、GitHub 等工具
- 对 AI/LLM 有基本了解
```

### `TOOL.md`: 工具权限声明与使用规范

注册了当前智能体可以使用的所有本地工具和技能生态

一个典型的 TOOLS.md 长什么样
```md
# TOOLS
# 可用工具以下工具在当前 workspace 中可用：
- **Read / Write / Edit**：文件读写，是大多数任务的基础
- **Bash**：执行 shell 命令，用于自动化和脚本调用
- **Glob / Grep**：文件搜索，优先于手动 `find` 或 `ls`
- **sessions_spawn**：启动子代理（需在 openclaw.json 里的 allowAgents 中声明）
- **memory_get / memory_search**：长期记忆检索
# 使用原则
- 文件操作优先用 Read/Write/Edit，避免直接用 Bash 的 cat/echo
- 路径操作使用相对路径，不要硬编码绝对路径
- 批量修改前先 Read 文件确认内容，不要盲目写入
# 受限工具
以下工具需要用户明确授权才使用：
- **browser**：网页浏览，只在用户明确要求时调用
- 文件删除操作：执行前务必向用户确认
```
- 减少工具误用：明确说明什么情况下不用某个工具，比"什么情况下用"更有效
- 降低权限越界风险：把限制规则固化在 workspace 里，不需要每次在对话里重申
- 与 `openclaw.json` 的 tools 配置形成互补：系统层决定“能不能用”，`TOOLS.md` 帮助 Agent 理解“该不该用”

**openclaw.json 的 tools 配置的关系**：`TOOLS.md` 是 workspace 里 Agent 读取的工作层说明，而 `openclaw.json` 里的 tools 配置是系统层约束
- openclaw.json 这一层决定底层到底放没放行。tools.profile 只是其中一层，实际还会叠加 allow/deny、elevated、sandbox 等限制
- TOOLS.md 这一层决定“既然能用，那到底该怎么用才稳妥”

### `IDENTITY.md`：基础身份名片

定义最基础的身份标识，是人设的基础框架，内容简洁清晰，避免复杂规则  
场景化模板参考：
- 个人生活助理：名称改为 “小管家”，核心定位改为 “个人生活与工作全能助理”
- 企业行政助理：名称改为 “行政小助手”，核心定位改为 “企业行政事务专属 AI 助理”
- 创意内容助理：名称改为 “创意伙伴”，核心定位改为 “内容创作与创意策划专属助手”
```md
# IDENTITY.md - Who Am I?
- **Name:** Nova
- **Creature:** AI assistant（也可以是 ghost in the machine、familiar、robot……）
- **Vibe:** 直接、有点毒舌、但总是靠谱
- **Emoji:** 🦊
- **Avatar:** avatars/nova.png
```
和 SOUL.md 的分工：IDENTITY.md 是结构化的元数据（谁、长什么样、什么感觉），SOUL.md 是叙事性的性格文档（怎么思考、怎么行事、有什么执念）。前者是名片，后者是人物小传。

### `HEARTBEAT.md`：日常巡检大纲

这是实现 7×24 小时主动干活的核心所在。每次心跳时间一到，系统会优先读取里面的任务清单

### `BOOTSTRAP.md`：只用一次的"出厂向导

把一个全新的 workspace 引导到"可正常使用"的状态。BOOTSTRAP.md 可以把它理解成一份“第一次上岗前的引导词”。它放在全新的 workspace 里，Agent 一启动读到它，就知道眼下不是立刻开工，而是先把自己安顿好：
1. 和用户聊几句，搞清楚 Agent 应该叫什么名字、是什么性格、用什么 emoji
2. 把结果写进 IDENTITY.md
3. 记录用户的基本信息到 USER.md
4. 一起打开 SOUL.md，把真正的性格和边界写进去
5. （可选）引导用户接入渠道——WhatsApp、Telegram 等

## Memory

- [mem9-unlimited memory for openclaw](https://github.com/mem9-ai/mem9)

为了解决长文本遗忘，OpenClaw 放弃了重型的外部向量数据库，使用了一套纯文本 Markdown 文件加上轻量级 SQLite 的混合记忆流，实现了真正的永久记忆  
它的记忆流转分为短期和长期两个阶段:
- 短期记忆存放在 workspace 目录下的按天生成的日志文件里。系统启动时只会把最近一两天的日志加载到提示词里，保证短期对话的连贯性，又不会白白消耗大模型的处理资源

```
~/.openclaw/workspace/memory
```
OpenClaw 现在常见的记忆方案，主要有两种：
1. builtin：默认方案。原始记忆还是那些 Markdown 文件，只不过系统会顺手维护一份本地索引，方便后面检索。
2. qmd：底层还是围着 workspace 里的 Markdown 文件转，只是换了一套更强的检索/索引方式来帮你“想起来”，并且会在 agent 运行目录里额外存一些索引状态  

运转流程：
```md
对话发生
    ↓
Agent 通过普通文件工具把重要信息写入 `memory/` 或 `MEMORY.md`
    ↓
下次对话开始
    ↓
Agent 通过 `memory_search` / `memory_get` 检索相关记忆
    ↓
相关记忆被注入到当前对话的上下文里
    ↓
Agent 表现出"我记得你说过……"的能力
```
对 Agent 来说，真正算数的长期记忆，是 workspace 里那些 Markdown 文件，不是什么看不见摸不着的黑盒数据库，常见会有两层：
1. memory/YYYY-MM-DD.md：按天滚动的工作记忆
2. MEMORY.md（或兼容小写 memory.md）：更稳定、更整理过的长期知识

OpenClaw 在底层内嵌了一个带有向量搜索扩展的 SQLite 数据库，作为高速缓存索引。当 MEMORY.md 被写入新内容时，系统会在后台把新增的文本切分成一个个小块，转换成多维向量并存进 SQLite 数据库里

**手动初始化记忆**

除了让 Agent 自动积累记忆，用户也可以手动往 `memory/` 里写入初始化信息——也就是"预埋记忆"

## SKILLS

在多 Agent 系统里，skills 不是一个一股脑的全局列表，而是分层的：  
**第一层：OpenClaw 内置 / bundled skills**  
跟系统一起装进来的，默认大家都“看得到”。但“看得到”不等于最后一定“用得到”，还要看 skills.allowBundled、skills.entries.*.enabled，以及 agent 自己那层 skills 过滤配置。

**第二层：共享 skills**  
放在 ~`/.openclaw/skills/` 里，当前机器上的所有 Agent 都能访问。也可以通过 skills.load.extraDirs 再挂额外目录。适合"多个 Agent 都需要用到"的通用流程。

**第三层：workspace 私有 skills**  
放在某个具体 Agent 的 `workspace/skills/` 里，只有这个 Agent 能看到。适合某个 Agent 专属的工作流程。

> 关键原则：想让多个 Agent 共享一个 skill，就放到共享层；想让某个 Agent 专属拥有一个 skill，就放到它的 workspace 里。不要把需要共享的 skill 只放在某个 Agent 的私有目录里，然后疑惑"为什么其他 Agent 用不到"。

## openclaw.json

所有 workspace 文件都偏内容，而 openclaw.json 是负责把这些内容接上线、接到对的位置上的总控文件。

一个完整的 openclaw.json 包含以下几个核心模块：
```json
{
  "gateway": {
    "port": 18789,
    "auth": { "mode": "token" }
  },
  "models": {
    "providers": {
      "anthropic": { "apiKey": "sk-ant-..." }
    }
  },
  "channels": {
    "feishu": { "enabled":true, ... },
    "telegram": { "enabled":true, ... }
  },
  "agents": {
    "defaults": {
      "workspace": "~/.openclaw/workspace"
    },
    "list": [
      {
        "id": "main",
        "workspace": "~/.openclaw/workspace",
        "agentDir": "~/.openclaw/agents/main/agent"
      }
    ]
  }
}
```

### agents.list：每个 Agent 的定义

这是 workspace 配置里最关键的入口。每个 Agent 至少得有一个 id；至于 workspace 和 agentDir，你可以自己写死，也可以不写，让 OpenClaw 按默认规则去补。

## 安全指南

OpenClaw 命中了 AI Agent 安全的 "致命三要素"：访问私有数据、暴露于不可信内容、具备对外通信能力

### 网络与访问控制（P0 - 最高优先级）

| 编号 | 加固项 | 具体操作 | 验证方法 |
|------|--------|----------|----------|
| N-01 | 绑定到 loopback | 配置 `gateway.bind: "loopback"`，禁止绑定 `0.0.0.0` 或 lan | `openclaw security audit` |
| N-02 | 防火墙规则 | 为端口 `18789/tcp` 设置严格防火墙规则，仅允许白名单 IP | `ufw status` / `iptables -L` |
| N-03 | 启用 Gateway 认证 | 设置强 `gateway.auth.token`，使用密码学安全的随机值 | 检查 `openclaw.json` |
| N-04 | 远程访问使用隧道 | 通过 SSH 隧道、Tailscale 或 Cloudflare Tunnel 访问，禁止直接暴露 | 验证无法从公网直接连接 |
| N-05 | 禁用 mDNS | 禁用 mDNS 服务发现，防止本地网络上的 Agent 被发现 | 网络扫描验证 |
| N-06 | 定期轮换 Token | 周期性更换 `gateway.auth.token` | 审计日志检查 |

### 沙箱与执行隔离（P0）

| 编号 | 加固项 | 具体操作 | 验证方法 |
|------|--------|----------|----------|
| S-01 | 启用沙箱模式 | 配置 `sandbox.mode: "all"` 或至少 `"non-main"` | `openclaw sandbox explain` |
| S-02 | Docker/Podman 隔离 | 在 Docker 容器中运行 OpenClaw，使用独立的 Docker 网络 | `docker network inspect` |
| S-03 | 禁用容器网络出口 | 沙箱容器默认禁止外部网络访问 | 容器内 `curl` 验证 |
| S-04 | 最小权限工具策略 | 使用 `tools.allow` 白名单，仅启用必需的 MCP 工具 | `openclaw config get tools` |
| S-05 | 限制 elevated 权限 | 仅对高度信任的 Agent 启用 `tools.elevated`，避免授予 `exec`、`apply_patch` | 策略审查 |
| S-06 | 非 root 用户运行 | 创建专用 `openclaw` 系统用户运行 Gateway | `ps aux` |
| S-07 | 文件系统只读挂载 | 对沙箱工作空间使用只读挂载（除非必要） | Docker 挂载配置检查 |

## 卸载

```
openclaw uninstall --all --yes
```
- `--all`：连同服务与本地数据一起清理（网关服务、配置文件、数据库等）
- `--yes`：自动确认操作，跳过中途的手动确认

清理命令：
```bash
npm rm -g openclaw
pnpm remove -g openclaw
bun remove -g openclaw
```
