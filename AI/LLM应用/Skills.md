# SKILL 设计原则

## 原则 1：SKILL 专注于“模糊逻辑”， 只描述需要模型推理的部分

Skill 的上下文窗口极其昂贵，不应浪费在计算机本来就能完美执行的确定性任务上。

Skill 应专注于“模糊逻辑”，而将“确定性逻辑”外包
1. 逻辑分流标准：在编写 Skill 前，先问自己：“这个步骤有唯一的标准答案吗？”
- 是（确定性）：如数学计算、文件格式转换、正则匹配、API 调用。→ 写入脚本或 MCP 工具，Skill 只负责调用。
- 否（模糊性）：如情感分析、意图识别、内容润色、复杂决策。→ 写入 Skill 正文。
2. 代码优于文本：如果一个流程可以用 Python 脚本（如 pandas 处理数据）或 Shell 命令精确描述，绝不要用自然语言在 Skill 里教 AI 怎么做。
3. Skill 的角色：Skill 是“大脑”，负责判断“做什么”；脚本/MCP 是“手脚”，负责“怎么做”。

指南：
- 错误：在 Skill 里写“请逐行读取 CSV 文件，计算第三列的总和”。
- 正确：Skill 指示调用 calculate_csv_sum 工具，仅处理工具返回的异常或解释结果。

## 原则 2：渐进式披露-上下文窗口是稀缺公共资源，最小化上下文占用

上下文（Context Window）是所有 Skill 共享的“内存”。一次性加载所有信息会导致“上下文污染”，增加延迟和成本，甚至导致模型迷失重点
1. 三层加载架构：
- L1 钩子（常驻）：仅在系统提示词中保留 name 和 description。这是 AI 决定是否调用 Skill 的唯一依据，必须极短（<100 tokens）。
- L2 核心（触发加载）：当 AI 决定调用 Skill 时，加载 SKILL.md 正文。包含核心工作流、关键规则。限制在 500 行或 5k tokens 以内。
- L3 资源（按需引用）：对于庞大的 API 文档、复杂的 JSON Schema 或参考代码，不要直接贴在正文中。使用相对路径引用（如 See ./references/api_spec.md），仅在模型明确读取该文件时才消耗 Token。
2. 目录化设计：将 Skill 设计为“目录”而非“百科全书”，引导模型去查阅详情，而不是把详情直接喂给它。

指南：
- 错误：把 50 页的 API 文档全部塞进 Skill 的 body 里。
- 正确：Skill 正文只写“调用 API 时需遵循鉴权规则（见 auth.md），参数格式参考 schema.json”。

## 原则 3：明确触发描述-Description 必须同时讲清「做什么 + 何时用」

Description 是 Skill 的“门面”和“路由器”。    
Description 需要明确触发描述逻辑，如果描述不清，AI 要么在不需要时误触发（浪费资源），要么在需要时忽略它（能力失效）。

1. 双重包含公式：一个完美的 Description = 能力定义 + 触发场景/关键词。
- 能力定义：用第三人称、动名词开头（如 "Processes...", "Analyzes..."）。
- 触发场景：明确指出用户说什么话、或者遇到什么文件类型时应该激活此 Skill。
2. 关键词埋点：在描述中显式包含高频触发词。例如，如果 Skill 用于处理 Excel，描述中必须出现 "Excel", "Spreadsheet", ".xlsx", "表格分析" 等词汇，以便向量检索或关键词匹配。

指南：
- 错误：“Excel 助手”（太泛，AI 不知道何时用它）。
- 正确：“从 Excel 文件中提取销售数据，按地区聚合，并生成 Markdown 表格；当用户上传 .xlsx 文件或请求分析季度报表时触发。”

## 原则 4：指令的绝对性-命令式语气，正文用祈使句，不用建议/解释

Skill 是给 AI 的“操作手册”，不是“建议指南”。  
模糊的建议语气会增加模型的认知负荷，导致执行结果的不确定性（幻觉）。

1. 消除“我”和“你”：不要写“你应该检查错误”或“我建议你先搜索”。
2. 使用命令式动词：直接以动词开头。
- 扫描错误日志...
- 提取关键字段...
- 格式化为 JSON...
3. 示例驱动：对于复杂的格式要求，不要写长篇大论的解释。直接给出一个 Input -> Output 的少样本（Few-Shot）示例，模型模仿能力远强于理解抽象规则的能力。

指南：
- 错误：“你可以尝试用 JSON 格式输出，这样比较好解析。”
- 正确：“输出必须严格遵循 JSON 格式。示例：{'status': 'success'}。”

## 原则 5：清晰定义边界-明确边界，什么能做、什么不能做、失败怎么办

AI 倾向于“讨好”用户，即使面对无法完成或危险的任务也会尝试强行回答。Skill 必须充当“安全护栏”。

1. 否定约束：明确列出禁止事项。例如：“严禁处理金额超过 10,000 元的退款申请”、“不要修改原始文件，仅生成副本”。
2. 失败与降级策略：告诉 AI 当遇到死胡同时该做什么。
- 数据缺失：“如果找不到日期字段，直接返回错误代码 ERR_DATE_MISSING，不要编造日期。”
- 超出范围：“如果用户询问非技术支持问题，请礼貌拒绝并引导至人工客服。”
3. 安全阈值：对于高风险操作（如删除文件、发送邮件），Skill 必须包含“确认步骤”或要求用户显式授权。

指南：
- 错误：只写成功流程，忽略异常情况。
- 正确：“若 API 返回 404，重试一次；若仍失败，停止执行并报告‘资源未找到’。”

## 原则 6：工程化闭环-可测试、可评估、可复现

Skill 是代码，不是文章。它必须像软件代码一样，有明确的“通过/失败”标准，而不是靠“感觉”。
1. 定义成功指标：在 Skill 开发之初就定义好：
- 格式准确率：输出是否符合 JSON Schema？
- 逻辑正确性：计算结果是否精确？
- 触发召回率：在 100 个相关查询中，Skill 被激活了多少次？
2. 构建测试集：
- 正例：10 个典型的、应该触发 Skill 的用户输入。
- 反例：10 个相似的、但不应该触发 Skill 的输入（防止误触）。
- 边缘案例：空输入、乱码输入、超长输入。
3. A/B 测试：修改 Skill 后，必须重新跑一遍测试集，确保没有“修复一个 Bug 引入两个新 Bug”。

指南：
- 错误：写完 Skill 后，只在聊天窗口里手动试一次就发布。
- 正确：维护一个 tests/ 目录，包含自动化测试脚本，每次更新 Skill 自动验证。

## 原则 7：最小够用原则

模型已经具备了海量的通用知识。  
Skill 的价值在于提供“特有知识”和“特定约束”。冗余信息不仅浪费 Token，还会稀释核心指令的权重。

1. 剔除常识：
- 不写：“PDF 是一种便携式文档格式，由 Adobe 发明...”
- 写：“使用 pypdf 库提取文本，注意处理多栏排版的换行符。”
2. 聚焦差异：只写那些模型“不知道”或者“容易做错”的事情。
- 业务特有规则：公司内部的项目命名规范、特殊的 API 鉴权头、特定的回复话术风格。
3. 高信噪比：每一行文字都必须有存在的理由。如果删掉某句话不影响任务执行，那就删掉它。

指南：
- 错误：把维基百科的定义或通用的编程教程复制到 Skill 里。
- 正确：直接给出针对当前任务的、经过优化的、最简练的操作步骤。

# Agents SKILL

- [The open agent skills tool - npx skills](https://github.com/vercel-labs/skills)
- [Add Skill](https://github.com/vercel-labs/add-skill)
- [Skill Rank](https://skills.sh/)
- [Agent Skill Specification](https://github.com/agentskills/agentskills)

## 对比 Function Call 和 MCP

想象 Agent 是一个新入职的员工。**Function Call 就是"打电话的能力"**，这个员工学会了怎么拿起电话、拨号、跟对方沟通。这是最基础的能力，没有这个能力他就没法跟外部世界互动。

**MCP 就是"公司的通讯录和电话系统"**，它统一管理所有外部联系方式（供应商、合作伙伴、服务商），员工不需要自己记住每个人的电话号码和通话方式，直接查通讯录就行。新增一个联系人只要加到通讯录里，所有员工都能用。

**Skills 就是"岗位培训手册"**，它告诉员工"遇到客户投诉应该按什么流程处理""做报表应该用什么模板和方法""跟供应商谈判要注意哪些要点"。它教的是做事的方法和规范，而不是打电话的技术。

本质区别：
- 从解决的问题来看，Function Call 解决的是"LLM 怎么跟外部函数交互"这个最基础的问题。MCP 解决的是"怎么用统一标准管理大量工具"的集成问题。Skills 解决的是"Agent 怎么获得领域专业知识"的知识问题。
- 从运行位置来看，Function Call 的函数在你的应用程序中执行。MCP 的工具在外部的 MCP Server 中执行。Skills 完全在 Agent 的上下文窗口内生效，不涉及任何外部调用。
- 从技术本质来看，Function Call 是一种 API 协议，LLM 输出结构化的调用请求，应用程序执行后返回结果。MCP 是一种通信标准，定义了 Client 和 Server 之间如何发现和调用工具。Skills 是一种提示词扩展，用自然语言编写的行为指令，加载到 Agent 的上下文中。
- 从标准化程度来看，Function Call 在各 LLM 厂商之间格式不统一（OpenAI 和 Anthropic 的格式就不一样）。MCP 是统一的开放标准，跨厂商通用。Skills 目前还没有统一标准，各个 Agent 平台有自己的 Skill 格式。

# SKILL 蒸馏

- [Awesome Knowledge-Distillation. 分类整理的知识蒸馏paper(2014-2021)](https://github.com/FLHonker/Awesome-Knowledge-Distillation)
- [同事.skill](https://github.com/titanwings/colleague-skill)
- [人会离开 dot-skill 不会](https://titanwings.github.io/colleague-skill-site/)
- [女娲帮你蒸馏任何人的思维方式，让乔布斯、马斯克、芒格、费曼都给你打工](https://github.com/alchaincyf/nuwa-skill)

# SKILL 安全

- [乌云漏洞 Skills](https://github.com/tanweai/wooyun-legacy)
- [skill-security-scan: 用于扫描和检测 Claude Skills 的安全风险](https://github.com/huifer/skill-security-scan)

# SKILL 市场

- [SkillHub 是一个自托管平台，为团队提供私有的、受治理的智能体技能共享空间](https://github.com/iflytek/skillhub)
- [Skills Marketplace](https://skillsmp.com/zh)
- [Skill Hub](https://www.skillhub.club/skills)
- [一套全面的代理技能合集，涵盖上下文工程、多代理架构和生产代理系统](https://github.com/muratcankoylan/Agent-Skills-for-Context-Engineering)
- [Skill Market](https://skill0.atypica.ai/)
- [Claude Code skill implementing Manus-style persistent markdown planning](https://github.com/OthmanAdi/planning-with-files)
- [利用claude code agent框架一步一步实现deep research！很强大很简单的skills](https://github.com/liangdabiao/Claude-Code-Deep-Research-main)
- [通用 Open skills 处理器](https://github.com/numman-ali/openskills)
- [用于在 Rust 中使用 Makepad 框架构建跨平台 UI 应用的 Claude Code Skills](https://github.com/ZhangHanDong/makepad-skills)
- [Skill Category](https://www.myaiexp.com/zh?category=skills)
- https://agentskillsfinder.com/zh

## 编程 SKILL

- [Professional Claude Code marketplace with 140 development tools](https://github.com/manutej/luxor-claude-marketplace)
- [Go Coding Conventions Skills](https://github.com/fredrikaverpil/dotfiles/blob/main/stow/shared/.claude/skills/golang-style/SKILL.md)
- [Effective Go Skills](https://github.com/openshift/hypershift/tree/main/.claude/skills/effective-go)
- [LSP-Skills](https://github.com/lsp-client/lsp-skill)
- [66 Specialized Skills for Full-Stack Developers](https://github.com/Jeffallan/claude-skills)
- [给 Claude Code 装上完整联网能力的 skill：三层通道调度 + 浏览器 CDP + 并行分治](https://github.com/eze-is/web-access)

## 前端 SKILL

## 设计 SKILL

- [Web Design Engineer Skill](https://github.com/ConardLi/web-design-skill)

## 各种 SKILL

- [PUA Skills](https://github.com/tanweai/pua)
- [专注于使用 Manim 生成动画教学视频的完整流程与专业建议](https://github.com/lispking/video-skills/tree/main/manim-video-teacher)
- [AI 写作去痕工具（中文版）](https://github.com/op7418/Humanizer-zh)、https://github.com/ForrestKnight/open-source-cs
- [全网新闻聚合 Skill](https://github.com/cclank/news-aggregator-skill)
- [关键字文章 Skill](https://github.com/davila7/claude-code-templates/tree/main/cli-tool/components/skills/business-marketing/content-creator)
- [同事.skill](https://github.com/titanwings/colleague-skill)
- [基于 Claude 代码构建的 AI 驱动求职系统](https://github.com/santifer/career-ops)

## 资讯 SKILL

- [last30days-cn 是一个 AI Agent 技能（Skill），能够自动搜索中国互联网 8 大主流平台最近 30 天的内容，综合分析后生成有据可查的研究报告](https://github.com/ChiTing111/last30days-skill-cn.git)

## 文档 Skill

- [将任意 URL 转为干净的 Markdown，支持需要登录的页面](https://github.com/joeseesun/markdown-proxy)
- [NanoBanana PPT Skills 基于 AI 自动生成高质量 PPT 图片和视频的强大工具](https://github.com/op7418/NanoBanana-PPT-Skills)
- [自媒体内容创作全家桶](https://github.com/JimLiu/baoyu-skills)

## SKILL合集

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