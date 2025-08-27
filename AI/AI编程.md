# 一、Cursor

- [Cursor AI编程经验分享](https://mp.weixin.qq.com/s/UM3nBcX6JpYtnchSCdrxOA)

cursor的表现取决于：有效的Rules、正确的开发流程、标准的Prompt

## 1、rules

rules 就是给 Cursor 定下的行为准则和沟通规范，rules 的核心价值：用具体可执行的约束替代模糊的预期，让 AI 的输出能够精准贴合实际开发需求；

rules 通过提前明确 “能做什么”“不能做什么”，解决三个关键问题：
- 减少无效沟通：比如规定 “解释代码必须用通俗语言”、“修改代码或解释代码前，需要多画图”，省去反复纠正表达风格的时间；
- 降低操作风险：用 “最小化修改原则” 限制 AI 仅改动必要部分，防止核心代码被破坏；
- 统一协作标准：团队共用一套规则时，AI 输出的代码风格、文档格式能保持一致，减少整合成本。

Cursor 的 rules 分为全局 rules 和项目 rules：不同项目可在`.cursor/rules/*.mdc` 目录下添加专属规则，用 git 管理实现团队共享；用模块化语言编写规则，更利于大模型理解

## 2、常用 MCP

- [交互式用户反馈 MCP-节省 Token 调用量](https://github.com/Minidoracat/mcp-feedback-enhanced)
- [Sequential Thinking MCP-顺序思考](https://github.com/arben-adm/mcp-sequential-thinking)
- [Shrimp Task Manager MCP-把复杂任务拆解成可执行的小步骤的 MCP](https://github.com/cjo4m06/mcp-shrimp-task-manager)
- [Context7-LLMs 和 AI 代码编辑器的最新代码文档](https://github.com/upstash/context7)
- [将任何 Git 存储库转换为其代码库的简单文本摘要](https://github.com/coderamp-labs/gitingest)

# 参考资料

- [Claude Code 的相关开源资源精选列表，是 Awesome 系列](https://github.com/hesreallyhim/awesome-claude-code)