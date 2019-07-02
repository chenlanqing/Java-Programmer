
# 添加jmx连接
## 监控tomcat
需要修改catalina.sh，加入如下配置：
```
JAVA_OPTS="$JAVA_OPTS -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=9004 -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false -Djava.net.preferIP4Stack=true -Djava.rmi.server.hostname=127.0.0.1"
```
- -Dcom.sun.management.jmxremote=true 
- -Dcom.sun.management.jmxremote.port=8099（配置远程 connection 的端口号的） 
- -Dcom.sun.management.jmxremote.ssl=false(指定了 JMX 是否启用 ssl) 
- -Dcom.sun.management.jmxremote.authenticate=false（ 指定了JMX 是否启用鉴权（需要用户名，密码鉴权）） 
- -Djava.rmi.server.hostname=192.168.0.1（配置 server 的 IP）

## java应用
主要是jar包启动的，在jar启动的时添加跟tomcat一样的参数

# 参考资料

https://visualvm.github.io/documentation.html