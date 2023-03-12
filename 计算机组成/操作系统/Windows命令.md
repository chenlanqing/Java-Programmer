
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



