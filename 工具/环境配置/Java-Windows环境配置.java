1.【我的电脑】--> 【属性】-->【高级系统设置】 --> 【环境变量】;
一.Java环境配置:
2.在系统变量中添加:JAVA_HOME,变量值为JDK的安装目录
3.在变量:PATH 后面添加:%JAVA_HOME%\bin;%JAVA_HOME%\jre\bin;
4.查看是否有变量:CLASSPATH,如果没有,则添加该变量,其值为:
	%JAVA_HOME%\lib\dt.jar;%JAVA_HOME%\lib\tools.jar

二.Maven环境配置:
2.在系统变量中添加:MAVEN_HOME,变量值为maven的安装目录
3.在变量:PATH 后面添加:%MAVEN_HOME%\bin

三.Tomcat环境配置:
1，新建变量名：CATALINA_BASE,变量值为tomcat的安装目录
2，新建变量名：CATALINA_HOME,变量值为tomcat的安装目录
3，打开PATH,添加变量值：%CATALINA_HOME%\lib;%CATALINA_HOME%\bin

四.Eclipse配置Maven:
	preference--> Maven--->Installtion[添加当前Maven的配置路径]
	User setting --> 选择Maven安装目录下conf下的settings.xml文件







