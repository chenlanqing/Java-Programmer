
# 1、使用jstack分析cpu消耗过高的问题
当linux出现cpu被java程序消耗过高时，可以使用如下步骤来进行问题排查

- （1）top查找出哪个进程消耗的cpu高

- （2）top中shift+h查找出哪个线程消耗的cpu高 

    先输入top -p [pid]，比如21125进程，然后再按shift+h。这里意思为只查看21125的进程，并且显示线程

- （3）.jstack查找这个线程的信息 

    jstack [进程]|grep -A 10

    ``` 
    "http-8081-11" daemon prio=10 tid=0x00002aab049a1800 nid=0x52f1 in Object.wait() [0x0000000042c75000] 
   java.lang.Thread.State: WAITING (on object monitor)  
     at java.lang.Object.wait(Native Method)  
     at java.lang.Object.wait(Object.java:485)  
     at org.apache.tomcat.util.net.JIoEndpoint$Worker.await(JIoEndpoint.java:416)  
    ```