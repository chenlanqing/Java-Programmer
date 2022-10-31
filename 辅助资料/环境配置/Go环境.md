# 1、vscode的go开发

vscode安装go插件时总会缺少非常多的插件，导致无法执行 `go install golang.org/x/tools/gopls`

问题原因应该是国内没办法访问golang.org

此时我们需要在GoPath路径下的 cmd 中输入：
```
go env -w GO111MODULE=on
go env -w GOPROXY=https://goproxy.cn,direct
```

修改代理到国内的go，然后在 cmd 重新获取即可成功：
```
go get -v golang.org/x/tools/gopls
```
使用结束后需关闭 GO111MODULE，否则运行任何代码都会提示缺少 main.go：
```
go env -w GO111MODULE=off
```
