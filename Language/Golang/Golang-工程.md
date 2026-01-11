# 模块

- [Go Modules Reference](https://go.dev/ref/mod)

## 编写模块

Go Module 本质上是基于 VCS（版本控制系统），当在下载依赖时，实际上执行的是 VCS 命令，比如git，所以如果想要分享编写的库，只需要做到以下三点：
1. 源代码仓库可公开访问，且 VCS 属于以下的其中之一
2. 是一个符合规范的 go mod 项目
3. 符合语义化版本规范



