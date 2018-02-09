<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
**Table of Contents**  *generated with [DocToc](https://github.com/thlorenz/doctoc)*

- [1.sysdm.cpl 打开安全属性](#1sysdmcpl-%E6%89%93%E5%BC%80%E5%AE%89%E5%85%A8%E5%B1%9E%E6%80%A7)
- [2.查看window端口占用情况:](#2%E6%9F%A5%E7%9C%8Bwindow%E7%AB%AF%E5%8F%A3%E5%8D%A0%E7%94%A8%E6%83%85%E5%86%B5)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

##### 1.sysdm.cpl 打开安全属性

##### 2.查看window端口占用情况:
	netstat -ano : 列出所有端口的情况
	netstat -aon|findstr "49157" :  查看被占用端口对应的PID, 49157 为对应的端口
	tasklist|findstr "2720"  : 哪个进程或者程序占用了2720端口




