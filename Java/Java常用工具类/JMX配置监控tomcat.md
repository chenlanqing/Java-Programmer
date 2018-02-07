```
1.在tomcat/bin下找到catalina.bat(catalina.sh)
set JMX_REMOTE_CONFIG=-Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=8999 -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false  
set CATALINA_OPTS=%CATALINA_OPTS% %JMX_REMOTE_CONFIG% 



2.
$JAVA_HOME/jre/lib/management下有jmxremote.access和jmxremote.password的模板文件，将两个文件复制到$CATALINA_BASE/conf目录下 
◆ 修改$CATALINA_BASE/conf/jmxremote.access 添加内容： 
     monitorRole readonly 
     controlRole readwrite 
◆ 修改$CATALINA_BASE/conf/jmxremote.password 添加内容： 
     monitorRole chenfeng 
     controlRole chenfeng 
注意:jmxremote.password这个文件的权限问题


3.获取监控数据前台展示:

(1).Map<String, Map<String, Object>> monitorData = new HashMap<String, Map<String, Object>>();
key => hostName + ":" + port
value => 对应监控数据
```

















