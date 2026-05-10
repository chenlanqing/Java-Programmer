# AGENTS.md


## 1. 项目概述
  一段话：项目定位、技术栈（Spring Boot + React）、monorepo 结构


## 2. 快速命令
  构建、启动、格式化、质量检查命令速查表
  环境变量配置：~/.<project>_env 优先级说明


## 3. 后端架构
  包结构树（ASCII）+ 每个包的用途注释
  核心子系统简要说明
  → 详见 docs/architecture.md


## 4. 前端架构
  技术栈、路由方案、API 层约定、组件库规范
  → 详见 docs/design-docs/frontend-architecture.md


## 5. 关键约定
  - 异常统一用 BusinessException，禁止直接抛 RuntimeException
  - 响应体由框架统一包装，禁止手动构造
  - 分层架构禁止跨层依赖（make lint-arch 自动检查）
  - 代码风格：Spotless + Google Java Format
  - 安全：无状态 JWT
  → 每条规则附详细文档链接


## 6. 本地开发及验证流程
  「改 → 构建 → 启动 → 验证」完整闭环
  curl 验证模板、Token 获取、日志路径
  → 详见 docs/design-docs/api-verification.md


## 7. 质量检查
  make lint-arch / lint-format / format / build / test


## 8. 参考项目约定
  参考项目列表 + 优先级规则


## 9. 文档导航
  所有详细文档的索引表（architecture / design-docs / ref-*）