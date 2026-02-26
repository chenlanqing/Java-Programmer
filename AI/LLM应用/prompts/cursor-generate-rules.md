# 任务

为我的项目生成 Cursor Rules 规则文件。
 
# 项目信息
- 技术栈：[列出你的技术栈，如 React, TypeScript, Tailwind CSS]
- 框架：[列出主要框架，如 Vite, Next.js, Spring Boot]
- 项目类型：[如 Web 应用、管理后台、REST API]
 
# 目录结构

[粘贴你的项目目录结构]
说明：通过 tree src -L 2 -I 'node_modules|dist' 获取目录结构
 
# 规则要求
1. 生成 global-rules.mdc：包含技术栈、目录结构、语言规范、导入约定
2. 针对主要功能模块生成专门的规则文件（如路由、UI、API、测试等）
3. 只写项目中真实存在且被反复强制使用的规范，不要写大模型常识
4. 每条规则必须具体、可执行，避免泛泛描述
5. 仅在必要时附上代码示例（3~10 行）
6. 使用合适的 globs 和 alwaysApply 配置
 
# 输出格式
为每个规则文件输出完整内容，使用 .mdc 格式，包含 frontmatter。