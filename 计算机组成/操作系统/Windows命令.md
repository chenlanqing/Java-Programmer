
sysdm.cpl 打开安全属性

查看window端口占用情况:

	netstat -ano : 列出所有端口的情况
	netstat -aon|findstr "49157" :  查看被占用端口对应的PID, 49157 为对应的端口
	tasklist|findstr "2720"  : 哪个进程或者程序占用了2720端口


# 1、批量获取某个目录下的所有文件名称

```
dir /a-d /b *.jpg>src.txt
echo attention, job has done
pause
```

# 2、删除windows上保存的凭据

打开 Windows 凭据管理器，请按下 Win+R 键，输入 `control /name Microsoft.CredentialManager`，然后按 Enter 键。

在凭据管理器中，您可以查看、编辑和删除存储的凭据。如果您想删除 Git 存储的凭据，可以在凭据管理器中找到 Git 的凭据并将其删除


# 参考资料

- [PowerToys](https://github.com/microsoft/PowerToys)

