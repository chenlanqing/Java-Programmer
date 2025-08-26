# 一、命令操作

## 1、快捷操作

sysdm.cpl 打开安全属性

## 2、查看window端口占用情况

* [netstat命令](https://learn.microsoft.com/zh-cn/windows-server/administration/windows-commands/netstat)

常见操作：
- netstat -ano : 列出所有端口的情况
- netstat -aon|findstr "49157" :  查看被占用端口对应的PID, 49157 为对应的端口
- tasklist|findstr "2720"  : 哪个进程或者程序占用了2720端口
- 使用 PowerShell：`Get-NetTCPConnection | Where-Object { $_.LocalPort -eq 8080 }` ，显示本地端口为 8080 的所有 TCP 连接信息

## 3、批量获取某个目录下的所有文件名称

```
dir /a-d /b *.jpg>src.txt
echo attention, job has done
pause
```

## 4、删除windows上保存的凭据

打开 Windows 凭据管理器，请按下 Win+R 键，输入 `control /name Microsoft.CredentialManager`，然后按 Enter 键。

在凭据管理器中，您可以查看、编辑和删除存储的凭据。如果您想删除 Git 存储的凭据，可以在凭据管理器中找到 Git 的凭据并将其删除

# 二、Windows工具

- [Windows文本比较工具](https://winmerge.org/)
- [好用、优秀的 Windows 应用](https://github.com/stackia/best-windows-apps)
- [Utools](https://www.u.tools/)
- [剪切板历史工具](ditto 、EcoPaste 、贴贴)
- [Windows效率外挂:窗口管理、批量重命名、屏幕取色全有](https://github.com/microsoft/PowerToys)


# 参考资料

- [PowerToys](https://github.com/microsoft/PowerToys)

