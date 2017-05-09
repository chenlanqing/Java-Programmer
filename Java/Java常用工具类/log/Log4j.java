1. 概述
　　1.1. 背景　　
　　在应用程序中添加日志记录总的来说基于三个目的：监视代码中变量的变化情况，周期性的记录到文件中供其他应用进行统计分析工作；跟踪代码运行时轨迹，作为日后审计的依据；担当集成开发环境中的调试器的作用，向文件或控制台打印代码的调试信息。最普通的做法就是在代码中嵌入许多的打印语句，这些打印语句可以输出到控制台或文件中，比较好的做法就是构造一个日志操作类来封装此类操作，而不是让一系列的打印语句充斥了代码的主体。
　　
　　1.2. Log4j简介　　
　　在强调可重用组件开发的今天，除了自己从头到尾开发一个可重用的日志操作类外，Apache为我们提供了一个强有力的日志操作包-Log4j。　　
　　Log4j是Apache的一个开放源代码项目，通过使用Log4j，我们可以控制日志信息输送的目的地是控制台、文件、GUI组件、甚至是套接口服务器、NT的事件记录器、UNIX Syslog守护进程等；我们也可以控制每一条日志的输出格式；通过定义每一条日志信息的级别，我们能够更加细致地控制日志的生成过程。最令人感兴趣的就是，这些可以通过一个配置文件来灵活地进行配置，而不需要修改应用的代码。　
　　此外，通过Log4j其他语言接口，您可以在C、C+ +、.Net、PL/SQL程序中使用Log4j，其语法和用法与在Java程序中一样，使得多语言分布式系统得到一个统一一致的日志组件模块。而且，通过使用各种第三方扩展，您可以很方便地将Log4j集成到J2EE、JINI甚至是SNMP应用中。
　　
　　本文介绍的Log4j版本是 1.2.3。作者试图通过一个简单的客户/服务器Java程序例子对比使用与不使用Log4j 1.2.3的差别，并详细讲解了在实践中最常使用Log4j的方法和步骤。在强调可重用组件开发的今天，相信Log4j将会给广大的设计开发人员带来方便。加入到Log4j的队伍来吧！
　　
2. 一个简单的例子
　　我们先来看一个简单的例子，它是一个用Java实现的客户/服务器网络程序。刚开始我们不使用Log4j，而是使用了一系列的打印语句，然后我们将使用Log4j来实现它的日志功能。这样，大家就可以清楚地比较出前后两个代码的差别。
　　
　　2.1. 不使用Log4j
　　
　　2.1.1. 客户程序
　　package log4j ;
　　
　　import java.io.* ;
　　import java.net.* ;
　　
　　/**
　　 *
　　 * <p> Client Without Log4j </p>
　　 * <p> Description: a sample with log4j</p>
　　 * @version 1.0
　　 */
　　public class ClientWithoutLog4j {
　　
　　　　/**
　　　　 *
　　　　 * @param args
　　　　 */
　　　　public static void main ( String args [] ) {
　　
　　　　　　String welcome = null;
　　　　　　String response = null;
　　　　　　BufferedReader reader = null;
　　　　　　PrintWriter writer = null;
　　　　　　InputStream in = null;
　　　　　　OutputStream out = null;
　　　　　　Socket client = null;
　　
　　　　　　try {
　　　　　　　　client = new Socket ( "localhost", 8001 ) ;
　　　　　　　　System.out.println ( "info: Client socket: " + client ) ;
　　　　　　　　in = client.getInputStream () ;
　　　　　　　　out = client.getOutputStream () ;
　　　　　　} catch ( IOException e ) {
　　　　　　　　System.out.println ( "error: IOException : " + e ) ;
　　　　　　　　System.exit ( 0 ) ;
　　　　　　}
　　
　　　　　　try{
　　　　　　　　reader = new BufferedReader( new InputStreamReader ( in ) ) ;
　　　　　　　　writer = new PrintWriter ( new OutputStreamWriter ( out ), true ) ;
　　
　　　　　　　　welcome = reader.readLine () ;
　　　　　　　　System.out.println ( "debug: Server says: '" + welcome + "'" ) ;
　　
　　　　　　　　System.out.println ( "debug: HELLO" ) ;
　　　　　　　　writer.println ( "HELLO" ) ;
　　　　　　　　response = reader.readLine () ;
　　　　　　　　System.out.println ( "debug: Server responds: '" + response + "'") ;
　　
　　　　　　　　System.out.println ( "debug: HELP" ) ;
　　　　　　　　writer.println ( "HELP" ) ;
　　　　　　　　response = reader.readLine () ;
　　　　　　　　System.out.println ( "debug: Server responds: '" + response + "'" ) ;
　　
　　　　　　　　System.out.println ( "debug: QUIT" ) ;
　　　　　　　　writer.println ( "QUIT" ) ;
　　　　　　} catch ( IOException e ) {
　　　　　　　　System.out.println ( "warn: IOException in client.in.readln()" ) ;
　　　　　　　　System.out.println ( e ) ;
　　　　　　}
　　　　　　try{
　　　　　　　　Thread.sleep ( 2000 ) ;
　　　　　　} catch ( Exception ignored ) {}
　　　　}
　　}
　　
　　2.1.2. 服务器程序
　　package log4j ;
　　
　　import java.util.* ;
　　import java.io.* ;
　　import java.net.* ;
　　
　　/**
　　 *
　　 * <p> Server Without Log4j </p>
　　 * <p> Description: a sample with log4j</p>
　　 * @version 1.0
　　 */
　　public class ServerWithoutLog4j {
　　
　　　　final static int SERVER_PORT = 8001 ; // this server's port
　　
　　　　/**
　　　　 *
　　　　 * @param args
　　　　 */
　　　　public static void main ( String args [] ) {
　　　　　　String clientRequest = null;
　　　　　　BufferedReader reader = null;
　　　　　　PrintWriter writer = null;
　　　　　　ServerSocket server = null;
　　　　　　Socket socket = null;
　　　　　　InputStream in = null;
　　　　　　OutputStream out = null;
　　
　　　　　　try {
　　　　　　　　server = new ServerSocket ( SERVER_PORT ) ;
　　　　　　　　System.out.println ( "info: ServerSocket before accept: " + server ) ;
　　　　　　　　System.out.println ( "info: Java server without log4j, on-line!" ) ;
　　
　　　　　　　　// wait for client's connection
　　　　　　　　socket = server.accept () ;
　　　　　　　　System.out.println ( "info: ServerSocket after accept: " + server )　;
　　
　　　　　　　　in = socket.getInputStream () ;
　　　　　　　　out = socket.getOutputStream () ;
　　
　　　　　　} catch ( IOException e ) {
　　　　　　　　System.out.println( "error: Server constructor IOException: " + e ) ;
　　　　　　　　System.exit ( 0 ) ;
　　　　　　}
　　　　　　reader = new BufferedReader ( new InputStreamReader ( in ) ) ;
　　　　　　writer = new PrintWriter ( new OutputStreamWriter ( out ) , true ) ;
　　
　　　　　　// send welcome string to client
　　　　　　writer.println ( "Java server without log4j, " + new Date () ) ;
　　
　　　　　　while ( true ) {
　　　　　　　　try {
　　　　　　　　　　// read from client
　　　　　　　　　　clientRequest = reader.readLine () ;
　　　　　　　　　　System.out.println ( "debug: Client says: " + clientRequest ) ;
　　　　　　　　　　if ( clientRequest.startsWith ( "HELP" ) ) {
　　　　　　　　　　　　System.out.println ( "debug: OK!" ) ;
　　　　　　　　　　　　writer.println ( "Vocabulary: HELP QUIT" ) ;
　　　　　　　　　　}
　　　　　　　　　　else {
　　　　　　　　　　　　if ( clientRequest.startsWith ( "QUIT" ) ) {
　　　　　　　　　　　　　　System.out.println ( "debug: OK!" ) ;
　　　　　　　　　　　　　　System.exit ( 0 ) ;
　　　　　　　　　　　　}
　　　　　　　　　　　　else{
　　　　　　　　　　　　　　System.out.println ( "warn: Command '" + 
　　 clientRequest + "' not understood." ) ;
　　　　　　　　　　　　　　writer.println ( "Command '" + clientRequest 
　　 + "' not understood." ) ;
　　　　　　　　　　　　}
　　　　　　　　　　}
　　　　　　　　} catch ( IOException e ) {
　　　　　　　　　　System.out.println ( "error: IOException in Server " + e ) ;
　　　　　　　　　　System.exit ( 0 ) ;
　　　　　　　　}
　　　　　　}
　　　　}
　　}
　　
　　2.2. 迁移到Log4j
　　
　　2.2.1. 客户程序
　　
　　package log4j ;
　　
　　import java.io.* ;
　　import java.net.* ;
　　
　　// add for log4j: import some package
　　import org.apache.log4j.PropertyConfigurator ;
　　import org.apache.log4j.Logger ;
　　import org.apache.log4j.Level ;
　　
　　/**
　　 *
　　 * <p> Client With Log4j </p>
　　 * <p> Description: a sample with log4j</p>
　　 * @version 1.0
　　 */
　　public class ClientWithLog4j {
　　
　　　　/*
　　　　add for log4j: class Logger is the central class in the log4j package.
　　　　we can do most logging operations by Logger except configuration.
　　　　getLogger(...): retrieve a logger by name, if not then create for it.
　　　　*/
　　　　static Logger logger = Logger.getLogger 
　　 ( ClientWithLog4j.class.getName () ) ;
　　
　　　　/**
　　　　 *
　　　　 * @param args : configuration file name
　　　　 */
　　　　public static void main ( String args [] ) {
　　
　　　　　　String welcome = null ;
　　　　　　String response = null ;
　　　　　　BufferedReader reader = null ;
　　　　　　PrintWriter writer = null ;
　　　　　　InputStream in = null ;
　　　　　　OutputStream out = null ;
　　　　　　Socket client = null ;
　　
　　　　　　/*
　　　　　　add for log4j: class BasicConfigurator can quickly configure the package.
　　　　　　print the information to console.
　　　　　　*/
　　　　　　PropertyConfigurator.configure ( "ClientWithLog4j.properties" ) ;
　　
　　　　　　// add for log4j: set the level
　　//　　　　logger.setLevel ( ( Level ) Level.DEBUG ) ;
　　
　　　　　　try{
　　　　　　　　client = new Socket( "localhost" , 8001 ) ;
　　
　　　　　　　　// add for log4j: log a message with the info level
　　　　　　　　logger.info ( "Client socket: " + client ) ;
　　
　　　　　　　　in = client.getInputStream () ;
　　　　　　　　out = client.getOutputStream () ;
　　　　　　} catch ( IOException e ) {
　　
　　　　　　　　// add for log4j: log a message with the error level
　　　　　　　　logger.error ( "IOException : " + e ) ;
　　
　　　　　　　　System.exit ( 0 ) ;
　　　　　　}
　　
　　　　　　try{
　　　　　　　　reader = new BufferedReader ( new InputStreamReader ( in ) ) ;
　　　　　　　　writer = new PrintWriter ( new OutputStreamWriter ( out ), true ) ;
　　
　　　　　　　　welcome = reader.readLine () ;
　　
　　　　　　　　// add for log4j: log a message with the debug level
　　　　　　　　logger.debug ( "Server says: '" + welcome + "'" ) ;
　　
　　　　　　　　// add for log4j: log a message with the debug level
　　　　　　　　logger.debug ( "HELLO" ) ;
　　
　　　　　　　　writer.println ( "HELLO" ) ;
　　　　　　　　response = reader.readLine () ;
　　
　　　　　　　　// add for log4j: log a message with the debug level
　　　　　　　　logger.debug ( "Server responds: '" + response + "'" ) ;
　　
　　　　　　　　// add for log4j: log a message with the debug level
　　　　　　　　logger.debug ( "HELP" ) ;
　　
　　　　　　　　writer.println ( "HELP" ) ;
　　　　　　　　response = reader.readLine () ;
　　
　　　　　　　　// add for log4j: log a message with the debug level
　　　　　　　　logger.debug ( "Server responds: '" + response + "'") ;
　　
　　　　　　　　// add for log4j: log a message with the debug level
　　　　　　　　logger.debug ( "QUIT" ) ;
　　
　　　　　　　　writer.println ( "QUIT" ) ;
　　　　　　} catch ( IOException e ) {
　　
　　　　　　　　// add for log4j: log a message with the warn level
　　　　　　　　logger.warn ( "IOException in client.in.readln()" ) ;
　　
　　　　　　　　System.out.println ( e ) ;
　　　　　　}
　　　　　　try {
　　　　　　　　Thread.sleep ( 2000 ) ;
　　　　　　} catch ( Exception ignored ) {}
　　　　}
　　}
　　
　　2.2.2. 服务器程序
　　
　　package log4j;
　　
　　import java.util.* ;
　　import java.io.* ;
　　import java.net.* ;
　　
　　// add for log4j: import some package
　　import org.apache.log4j.PropertyConfigurator ;
　　import org.apache.log4j.Logger ;
　　import org.apache.log4j.Level ;
　　
　　/**
　　 *
　　 * <p> Server With Log4j </p>
　　 * <p> Description: a sample with log4j</p>
　　 * @version 1.0
　　 */
　　public class ServerWithLog4j {
　　
　　　　final static int SERVER_PORT = 8001 ; // this server's port
　　
　　　　/*
　　　　add for log4j: class Logger is the central class in the log4j package.
　　　　we can do most logging operations by Logger except configuration.
　　　　getLogger(...): retrieve a logger by name, if not then create for it.
　　　　*/
　　　　static Logger logger = Logger.getLogger 
　　 ( ServerWithLog4j.class.getName () ) ;
　　
　　　　/**
　　　　 *
　　　　 * @param args
　　　　 */
　　　　public static void main ( String args[]) {
　　　　　　String clientRequest = null ;
　　　　　　BufferedReader reader = null ;
　　　　　　PrintWriter writer = null ;
　　　　　　ServerSocket server = null ;
　　　　　　Socket socket = null ;
　　
　　　　　　InputStream in = null ;
　　　　　　OutputStream out = null ;
　　
　　　　　　/*
　　　　　　add for log4j: class BasicConfigurator can quickly configure the package.
　　　　　　print the information to console.
　　　　　　*/
　　　　　　PropertyConfigurator.configure ( "ServerWithLog4j.properties" ) ;
　　
　　　　　　// add for log4j: set the level
　　//　　　　logger.setLevel ( ( Level ) Level.DEBUG ) ;
　　
　　　　　　try{
　　　　　　　　server = new ServerSocket ( SERVER_PORT ) ;
　　
　　　　　　　　// add for log4j: log a message with the info level
　　　　　　　　logger.info ( "ServerSocket before accept: " + server ) ;
　　
　　　　　　　　// add for log4j: log a message with the info level
　　　　　　　　logger.info ( "Java server with log4j, on-line!" ) ;
　　
　　　　　　　　// wait for client's connection
　　　　　　　　socket = server.accept() ;
　　
　　　　　　　　// add for log4j: log a message with the info level
　　　　　　　　logger.info ( "ServerSocket after accept: " + server ) ;
　　
　　　　　　　　in = socket.getInputStream() ;
　　　　　　　　out = socket.getOutputStream() ;
　　
　　　　　　} catch ( IOException e ) {
　　
　　　　　　　　// add for log4j: log a message with the error level
　　　　　　　　logger.error ( "Server constructor IOException: " + e ) ;
　　　　　　　　System.exit ( 0 ) ;
　　　　　　}
　　　　　　reader = new BufferedReader ( new InputStreamReader ( in ) ) ;
　　　　　　writer = new PrintWriter ( new OutputStreamWriter ( out ), true ) ;
　　
　　　　　　// send welcome string to client
　　　　　　writer.println ( "Java server with log4j, " + new Date () ) ;
　　
　　　　　　while ( true ) {
　　　　　　　　try {
　　　　　　　　　　// read from client
　　　　　　　　　　clientRequest = reader.readLine () ;
　　
　　　　　　　　　　// add for log4j: log a message with the debug level
　　　　　　　　　　logger.debug ( "Client says: " + clientRequest ) ;
　　
　　　　　　　　　　if ( clientRequest.startsWith ( "HELP" ) ) {
　　
　　　　　　　　　　　　// add for log4j: log a message with the debug level
　　　　　　　　　　　　logger.debug ( "OK!" ) ;
　　
　　　　　　　　　　　　writer.println ( "Vocabulary: HELP QUIT" ) ;
　　　　　　　　　　}
　　　　　　　　　　else {
　　　　　　　　　　　　if ( clientRequest.startsWith ( "QUIT" ) ) {
　　
　　　　　　　　　　　　　　// add for log4j: log a message with the debug level
　　　　　　　　　　　　　　logger.debug ( "OK!" ) ;
　　
　　　　　　　　　　　　　　System.exit ( 0 ) ;
　　　　　　　　　　　　}
　　　　　　　　　　　　else {
　　
　　　　　　　　　　　　　　// add for log4j: log a message with the warn level
　　　　　　　　　　　　　　logger.warn ( "Command '" 
　　 + clientRequest + "' not understood." ) ;
　　
　　　　　　　　　　　　　　writer.println ( "Command '"
　　 + clientRequest + "' not understood." ) ;
　　　　　　　　　　　　}
　　　　　　　　　　}
　　　　　　　　} catch ( IOException e ) {
　　
　　　　　　　　　　// add for log4j: log a message with the error level
　　　　　　　　　　logger.error( "IOException in Server " + e ) ;
　　
　　　　　　　　　　System.exit ( 0 ) ;
　　　　　　　　}
　　　　　　}
　　　　}
　　}
　　
　　2.2.3. 配置文件
　　
　　2.2.3.1. 客户程序配置文件
　　
　　log4j.rootLogger=INFO, A1
　　
　　log4j.appender.A1=org.apache.log4j.ConsoleAppender
　　
　　log4j.appender.A1.layout=org.apache.log4j.PatternLayout
　　
　　log4j.appender.A1.layout.ConversionPattern=%-4r %-5p [%t] %37c %3x - %m%n
　　
　　2.2.3.2. 服务器程序配置文件
　　
　　log4j.rootLogger=INFO, A1
　　
　　log4j.appender.A1=org.apache.log4j.ConsoleAppender
　　
　　log4j.appender.A1.layout=org.apache.log4j.PatternLayout
　　
　　log4j.appender.A1.layout.ConversionPattern=%-4r %-5p [%t] %37c %3x - %m%n
　　
　　2.3. 比较
　　
　　比较这两个应用可以看出，采用Log4j进行日志操作的整个过程相当简单明了，与直接使用System.out.println语句进行日志信息输出的方式相比，基本上没有增加代码量，同时能够清楚地理解每一条日志信息的重要程度。通过控制配置文件，我们还可以灵活地修改日志信息的格式，输出目的地等等方面，而单纯依靠System.out.println语句，显然需要做更多的工作。

>>>> 3 . Log4j日志管理系统简单使用说明 <<<<

    通常，我们都提供一个名为 log4j.properties的文件，在第一次调用到Log4J时，Log4J会在类路径（../web-inf/class/当然也可以放到其它任何目录，只要该目录被包含到类路径中即可）中定位这个文件，并读入这个文件完成的配置。这个配置文件告诉Log4J以什么样的格式、把什么样的信息、输出到什么地方。
　　Log4j有三个主要的组件：Loggers(记录器)，Appenders (输出源)和Layouts(布局)，这里可简单理解为日志类别，日志要输出的地方和日志以何种形式输出。综合使用这三个组件可以轻松的记录信息的类型和级别，并可以在运行时控制日志输出的样式和位置。下面对三个组件分别进行说明：
　　
　　1、 Loggers
　　Loggers组件在此系统中被分为五个级别：DEBUG、INFO、WARN、ERROR和FATAL。这五个级别是有顺序的，DEBUG < INFO < WARN < ERROR < FATAL，分别用来指定这条日志信息的重要程度,明白这一点很重要，这里Log4j有一个规则：假设Loggers级别为P，如果在Loggers中发生了一个级别Q比P高，则可以启动，否则屏蔽掉。
假设你定义的级别是info，那么error和warn的日志可以显示而比他低的debug信息就不显示了。
 　
　　Java程序举例来说：
　　
　　//建立Logger的一个实例，命名为“com.foo”
　　　Logger　logger = Logger.getLogger("com.foo"); //"com.foo"是实例进行命名，也可以任意
　　//设置logger的级别。通常不在程序中设置logger的级别。一般在配置文件中设置。
　　logger.setLevel(Level.INFO);
　　Logger barlogger = Logger.getLogger("com.foo.Bar");
　　//下面这个请求可用，因为WARN >= INFO
　　logger.warn("Low fuel level.");
　　//下面这个请求不可用，因为DEBUG < INFO
　　logger.debug("Starting search for nearest gas station.");
　　//命名为“com.foo.bar”的实例barlogger会继承实例“com.foo”的级别。因此，下面这个请求可用，因为INFO >= INFO
　　barlogger.info("Located nearest gas station.");
　　//下面这个请求不可用，因为DEBUG < INFO
　　barlogger.debug("Exiting gas station search");
　　这里“是否可用”的意思是能否输出Logger信息。
　　　　在对Logger实例进行命名时，没有限制，可以取任意自己感兴趣的名字。一般情况下建议以类的所在位置来命名Logger实例，这是目前来讲比较有效的Logger命名方式。这样可以使得每个类建立自己的日志信息，便于管理。比如：
　　
　　static Logger logger = Logger.getLogger(ClientWithLog4j.class.getName());
　　
　　2、Appenders
　　禁用与使用日志请求只是Log4j其中的一个小小的地方，Log4j日志系统允许把日志输出到不同的地方，如控制台（Console）、文件（Files）、根据天数或者文件大小产生新的文件、以流的形式发送到其它地方等等。
　　
　　其语法表示为：
　　
　　org.apache.log4j.ConsoleAppender（控制台）
　　org.apache.log4j.FileAppender（文件）
　　org.apache.log4j.DailyRollingFileAppender（每天产生一个日志文件）
    org.apache.log4j.RollingFileAppender（文件大小到达指定尺寸的时候产生一个新的文件）
　　org.apache.log4j.WriterAppender（将日志信息以流格式发送到任意指定的地方）
　　
　　配置时使用方式为：
　　log4j.appender.appenderName = fully.qualified.name.of.appender.class
　　log4j.appender.appenderName.option1 = value1
　　…
    log4j.appender.appenderName.option = valueN
　　这样就为日志的输出提供了相当大的便利。
　　
　　3、Layouts
　　有时用户希望根据自己的喜好格式化自己的日志输出。Log4j可以在Appenders的后面附加Layouts来完成这个功能。Layouts提供了四种日志输出样式，如根据HTML样式、自由指定样式、包含日志级别与信息的样式和包含日志时间、线程、类别等信息的样式等等。
　　
　　其语法表示为：
　　
　　org.apache.log4j.HTMLLayout（以HTML表格形式布局），
　　org.apache.log4j.PatternLayout（可以灵活地指定布局模式），
　　org.apache.log4j.SimpleLayout（包含日志信息的级别和信息字符串），
　　org.apache.log4j.TTCCLayout（包含日志产生的时间、线程、类别等等信息）
　　
　　配置时使用方式为：
　　
　　log4j.appender.appenderName.layout =fully.qualified.name.of.layout.class
　　log4j.appender.appenderName.layout.option1 = value1
　　…
　　log4j.appender.appenderName.layout.option = valueN
>>>>  4 . Log4j的配置 <<<<　　
　　
　　以上是从原理方面说明Log4j的使用方法，在具体Java编程使用Log4j可以参照以下示例：
　　
　　1、 建立Logger实例：
　　语法表示：public static Logger getLogger( String name)
　　实际使用：static Logger logger = Logger.getLogger(ServerWithLog4j.class.getName ()) ;
　　
　　2、 读取配置文件：
　　获得了Logger的实例之后，接下来将配置Log4j使用环境：
　　语法表示：
　　BasicConfigurator.configure()：自动快速地使用缺省Log4j环境。
　　PropertyConfigurator.configure(String configFilename)：读取使用Java的特性文件编写的配置文件。
　　DOMConfigurator.configure(String filename)：读取XML形式的配置文件。
　　实际使用：
    PropertyConfigurator.configure("ServerWithLog4j.properties");
　　
　　3、 插入日志信息
　　完成了以上连个步骤以后，下面就可以按日志的不同级别插入到你要记录日志的任何地方了。
　　语法表示：
　　Logger.debug(Object message);//调试信息
　　Logger.info(Object message);//一般信息
　　Logger.warn(Object message);//警告信息
　　Logger.error(Object message);//错误信息
　　Logger.fatal(Object message);//致命错误信息
　　实际使用：logger.info("ServerSocket before accept: " + server);
　　
　>>>> 5. 配置过程 <<<<

　在实际编程时，要使Log4j真正在系统中运行事先还要对配置文件进行定义。定义步骤就是对Logger、Appender及Layout的分别使用。
    Log4j支持两种配置文件格式，一种是XML格式的文件，一种是java properties（key=value）【Java特性文件（键=值）】。下面我们介绍使用Java特性文件做为配置文件的方法
   具体如下：
　　
　　1、配置根Logger，其语法为：
　　log4j.rootLogger = [ level ] , appenderName1, appenderName2, …
         level : 是日志记录的优先级，分为OFF、FATAL、ERROR、WARN、INFO、DEBUG、ALL或者您定义的级别。Log4j建议只使用四个级别，优先级从高到低分别是ERROR、WARN、INFO、DEBUG。通过在这里定义的级别，您可以控制到应用程序中相应级别的日志信息的开关。比如在这里定义了INFO级别，则应用程序中所有DEBUG级别的日志信息将不被打印出来。
　　     appenderName:就是指定日志信息输出到哪个地方。您可以同时指定多个输出目的地。
   例如：log4j.rootLogger＝info,A1,B2,C3
　　
　　2、配置日志信息输出目的地，其语法为：
　　log4j.appender.appenderName = fully.qualified.name.of.appender.class  //
　　  "fully.qualified.name.of.appender.class" 可以指定下面五个目的地中的一个：
          1.org.apache.log4j.ConsoleAppender（控制台）
          2.org.apache.log4j.FileAppender（文件）
          3.org.apache.log4j.DailyRollingFileAppender（每天产生一个日志文件）
          4.org.apache.log4j.RollingFileAppender（文件大小到达指定尺寸的时候产生一个新的文件）
          5.org.apache.log4j.WriterAppender（将日志信息以流格式发送到任意指定的地方）
             1.ConsoleAppender选项
                    Threshold=WARN:指定日志消息的输出最低层次。
                    ImmediateFlush=true:默认值是true,意谓着所有的消息都会被立即输出。
                    Target=System.err：默认情况下是：System.out,指定输出控制台
              2.FileAppender 选项
                    Threshold=WARN:指定日志消息的输出最低层次。
                    ImmediateFlush=true:默认值是true,意谓着所有的消息都会被立即输出。
                    File=mylog.txt:指定消息输出到mylog.txt文件。
                    Append=false:默认值是true,即将消息增加到指定文件中，false指将消息覆盖指定的文件内容。
            3.DailyRollingFileAppender 选项
                    Threshold=WARN:指定日志消息的输出最低层次。
                    ImmediateFlush=true:默认值是true,意谓着所有的消息都会被立即输出。
                    File=mylog.txt:指定消息输出到mylog.txt文件。
                    Append=false:默认值是true,即将消息增加到指定文件中，false指将消息覆盖指定的文件内容。
                    DatePattern='.'yyyy-ww:每周滚动一次文件，即每周产生一个新的文件。当然也可以指定按月、周、天、时和分。即对应的格式如下：
                    1)'.'yyyy-MM: 每月
                    2)'.'yyyy-ww: 每周 
                    3)'.'yyyy-MM-dd: 每天
                    4)'.'yyyy-MM-dd-a: 每天两次
                    5)'.'yyyy-MM-dd-HH: 每小时
                    6)'.'yyyy-MM-dd-HH-mm: 每分钟
            4.RollingFileAppender 选项
                    Threshold=WARN:指定日志消息的输出最低层次。
                    ImmediateFlush=true:默认值是true,意谓着所有的消息都会被立即输出。
                    File=mylog.txt:指定消息输出到mylog.txt文件。
                    Append=false:默认值是true,即将消息增加到指定文件中，false指将消息覆盖指定的文件内容。
                    MaxFileSize=100KB: 后缀可以是KB, MB 或者是 GB. 在日志文件到达该大小时，将会自动滚动，即将原来的内容移到mylog.log.1文件。
                    MaxBackupIndex=2:指定可以产生的滚动文件的最大数。
实际应用：
　　log4j.appender.A1=org.apache.log4j.ConsoleAppender //这里指定了日志输出的第一个位置A1是控制台ConsoleAppender
　　
　　3、配置日志信息的格式，其语法为：
　　A. log4j.appender.appenderName.layout = fully.qualified.name.of.layout.class
              "fully.qualified.name.of.layout.class" 可以指定下面4个格式中的一个：
               1.org.apache.log4j.HTMLLayout（以HTML表格形式布局），
　　       2.org.apache.log4j.PatternLayout（可以灵活地指定布局模式），
　　       3.org.apache.log4j.SimpleLayout（包含日志信息的级别和信息字符串），
　　       4.org.apache.log4j.TTCCLayout（包含日志产生的时间、线程、类别等等信息）
                   1.HTMLLayout 选项
                      LocationInfo=true:默认值是false,输出java文件名称和行号
                      Title=my app file: 默认值是 Log4J Log Messages.
                   2.PatternLayout 选项
                      ConversionPattern=%m%n :指定怎样格式化指定的消息。
                   3.XMLLayout  选项
                      LocationInfo=true:默认值是false,输出java文件和行号
   实际应用：
   　　log4j.appender.A1.layout=org.apache.log4j.PatternLayout
       B. log4j.appender.A1.layout.ConversionPattern=%-4r %-5p %d{yyyy-MM-dd HH:mm:ssS} %c %m%n
           这里需要说明的就是日志信息格式中几个符号所代表的含义：
　　         －X号: X信息输出时左对齐；
                   %p: 输出日志信息优先级，即DEBUG，INFO，WARN，ERROR，FATAL,
                   %d: 输出日志时间点的日期或时间，默认格式为ISO8601，也可以在其后指定格式，比如：%d{yyy MMM dd HH:mm:ss,SSS}，输出类似：2002年10月18日 22：10：28，921
                   %r: 输出自应用启动到输出该log信息耗费的毫秒数
                   %c: 输出日志信息所属的类目，通常就是所在类的全名
                   %t: 输出产生该日志事件的线程名
                   %l: 输出日志事件的发生位置，相当于%C.%M(%F:%L)的组合,包括类目名、发生的线程，以及在代码中的行数。举例：Testlog4.main(TestLog4.java:10)
                   %x: 输出和当前线程相关联的NDC(嵌套诊断环境),尤其用到像java servlets这样的多客户多线程的应用中。
                   %%: 输出一个"%"字符
                   %F: 输出日志消息产生时所在的文件名称
                   %L: 输出代码中的行号
                   %m: 输出代码中指定的消息,产生的日志具体信息
                   %n: 输出一个回车换行符，Windows平台为"\r\n"，Unix平台为"\n"输出日志信息换行
            可以在%与模式字符之间加上修饰符来控制其最小宽度、最大宽度、和文本的对齐方式。如：
                     1)%20c：指定输出category的名称，最小的宽度是20，如果category的名称小于20的话，默认的情况下右对齐。
                     2)%-20c:指定输出category的名称，最小的宽度是20，如果category的名称小于20的话，"-"号指定左对齐。
                     3)%.30c:指定输出category的名称，最大的宽度是30，如果category的名称大于30的话，就会将左边多出的字符截掉，但小于30的话也不会有空格。
                     4)%20.30c:如果category的名称小于20就补空格，并且右对齐，如果其名称长于30字符，就从左边交远销出的字符截掉。
　　这里上面三个步骤是对前面Log4j组件说明的一个简化；下面给出一个具体配置例子，在程序中可以参照执行：
　　log4j.rootLogger=INFO,A1，B2
　　log4j.appender.A1=org.apache.log4j.ConsoleAppender
　　log4j.appender.A1.layout=org.apache.log4j.PatternLayout
　　log4j.appender.A1.layout.ConversionPattern=%-4r %-5p %d{yyyy-MM-dd HH:mm:ssS} %c %m%n
 　　根据上面的日志格式，某一个程序的输出结果如下：
　　0　　INFO　2003-06-13 13:23:46968 ClientWithLog4j Client socket: Socket[addr=localhost/127.0.0.1,port=8002,localport=2014]
         16　 DEBUG 2003-06-13 13:23:46984 ClientWithLog4j Server says: 'Java server with log4j, Fri Jun 13 13:23:46 CST 2003'
　　16　 DEBUG 2003-06-13 13:23:46984 ClientWithLog4j GOOD
　　16　 DEBUG 2003-06-13 13:23:46984 ClientWithLog4j Server responds: 'Command 'HELLO' not understood.'
　　16　 DEBUG 2003-06-13 13:23:46984 ClientWithLog4j HELP
　　16　 DEBUG 2003-06-13 13:23:46984 ClientWithLog4j Server responds: 'Vocabulary: HELP QUIT'
　　16　 DEBUG 2003-06-13 13:23:46984 ClientWithLog4j QUIT
 
      4. # 当输出信息于回滚文件时
          log4j.appender.ROLLING_FILE=org.apache.log4j.RollingFileAppender   //指定以文件的方式输出日志
           log4j.appender.ROLLING_FILE.Threshold=ERROR 
           log4j.appender.ROLLING_FILE.File=rolling.log  //文件位置,也可以用变量${java.home}、rolling.log
           log4j.appender.ROLLING_FILE.Append=true 
           log4j.appender.ROLLING_FILE.MaxFileSize=10KB  //文件最大尺寸
           log4j.appender.ROLLING_FILE.MaxBackupIndex=1  //备份数
           log4j.appender.ROLLING_FILE.layout=org.apache.log4j.PatternLayout 
           log4j.appender.ROLLING_FILE.layout.ConversionPattern=[framework] %d - %c -%-4r [%t] %-5p %c %x - %m%n  　　
××××××××××××××××××××××××××××××××××××××××××××××××
 
>>>> 6. Log4j比较全面的配置 <<<<
 LOG4J的配置之简单使它遍及于越来越多的应用中了：Log4J配置文件实现了输出到控制台、文件、回滚文件、发送日志邮件、输出到数据库日志表、自定义标签等全套功能。择其一二使用就够用了，
 log4j.rootLogger=DEBUG,CONSOLE,A1,im 
 log4j.addivity.org.apache=true
 # 应用于控制台
 log4j.appender.CONSOLE=org.apache.log4j.ConsoleAppender 
 log4j.appender.Threshold=DEBUG 
 log4j.appender.CONSOLE.Target=System.out 
 log4j.appender.CONSOLE.layout=org.apache.log4j.PatternLayout 
 log4j.appender.CONSOLE.layout.ConversionPattern=[framework] %d - %c -%-4r [%t] %-5p %c %x - %m%n 
 #log4j.appender.CONSOLE.layout.ConversionPattern=[start]%d{DATE}[DATE]%n%p[PRIORITY]%n%x[NDC]%n%t[THREAD] n%c[CATEGORY]%n%m[MESSAGE]%n%n
 #应用于文件
 log4j.appender.FILE=org.apache.log4j.FileAppender 
 log4j.appender.FILE.File=file.log 
 log4j.appender.FILE.Append=false 
 log4j.appender.FILE.layout=org.apache.log4j.PatternLayout 
 log4j.appender.FILE.layout.ConversionPattern=[framework] %d - %c -%-4r [%t] %-5p %c %x - %m%n 
 # Use this layout for LogFactor 5 analysis
 # 应用于文件回滚
 log4j.appender.ROLLING_FILE=org.apache.log4j.RollingFileAppender 
 log4j.appender.ROLLING_FILE.Threshold=ERROR 
 log4j.appender.ROLLING_FILE.File=rolling.log  //文件位置,也可以用变量${java.home}、rolling.log
 log4j.appender.ROLLING_FILE.Append=true       //true:添加  false:覆盖
 log4j.appender.ROLLING_FILE.MaxFileSize=10KB   //文件最大尺寸
 log4j.appender.ROLLING_FILE.MaxBackupIndex=1  //备份数
 log4j.appender.ROLLING_FILE.layout=org.apache.log4j.PatternLayout 
 log4j.appender.ROLLING_FILE.layout.ConversionPattern=[framework] %d - %c -%-4r [%t] %-5p %c %x - %m%n

 #应用于socket 
 log4j.appender.SOCKET=org.apache.log4j.RollingFileAppender 
 log4j.appender.SOCKET.RemoteHost=localhost 
 log4j.appender.SOCKET.Port=5001 
 log4j.appender.SOCKET.LocationInfo=true 
 # Set up for Log Facter 5 
 log4j.appender.SOCKET.layout=org.apache.log4j.PatternLayout 
 log4j.appender.SOCET.layout.ConversionPattern=[start]%d{DATE}[DATE]%n%p[PRIORITY]%n%x[NDC]%n%t[THREAD]%n%c[CATEGORY]%n%m[MESSAGE]%n%n

 # Log Factor 5 Appender 
 log4j.appender.LF5_APPENDER=org.apache.log4j.lf5.LF5Appender 
 log4j.appender.LF5_APPENDER.MaxNumberOfRecords=2000
 # 发送日志给邮件
 log4j.appender.MAIL=org.apache.log4j.net.SMTPAppender 
 log4j.appender.MAIL.Threshold=FATAL 
 log4j.appender.MAIL.BufferSize=10 
 www.wuset.com">log4j.appender.MAIL.From=web@www.wuset.com 
 log4j.appender.MAIL.SMTPHost=www.wusetu.com 
 log4j.appender.MAIL.Subject=Log4J Message 
 www.wusetu.com">log4j.appender.MAIL.To=web@www.wusetu.com 
 log4j.appender.MAIL.layout=org.apache.log4j.PatternLayout 
 log4j.appender.MAIL.layout.ConversionPattern=[framework] %d - %c -%-4r [%t] %-5p %c %x - %m%n
 # 用于数据库 
 log4j.appender.DATABASE=org.apache.log4j.jdbc.JDBCAppender 
 log4j.appender.DATABASE.URL=jdbc:mysql://localhost:3306/test 
 log4j.appender.DATABASE.driver=com.mysql.jdbc.Driver 
 log4j.appender.DATABASE.user=root 
 log4j.appender.DATABASE.password= 
 log4j.appender.DATABASE.sql=INSERT INTO LOG4J (Message) VALUES ('[framework] %d - %c -%-4r [%t] %-5p %c %x - %m%n') 
 log4j.appender.DATABASE.layout=org.apache.log4j.PatternLayout 
 log4j.appender.DATABASE.layout.ConversionPattern=[framework] %d - %c -%-4r [%t] %-5p %c %x - %m%n

 log4j.appender.A1=org.apache.log4j.DailyRollingFileAppender 
 log4j.appender.A1.File=SampleMessages.log4j 
 log4j.appender.A1.DatePattern=yyyyMMdd-HH'.log4j' 
 log4j.appender.A1.layout=org.apache.log4j.xml.XMLLayout
 #自定义Appender
 log4j.appender.im = net.cybercorlin.util.logger.appender.IMAppender
 log4j.appender.im.host = mail.cybercorlin.net 
 log4j.appender.im.username = username 
 log4j.appender.im.password = password 
 log4j.appender.im.recipient = corlin@cybercorlin.net
 log4j.appender.im.layout=org.apache.log4j.PatternLayout 
 log4j.appender.im.layout.ConversionPattern =[framework] %d - %c -%-4r [%t] %-5p %c %x - %m%n

在实际编程时，要使Log4j真正在系统中运行事先还要对配置文件进行定义。定义步骤就是对Logger、Appender及Layout的分别使用。Log4j支持两种配置文件格式，一种是XML格式的文件，一种是java properties（key=value）【Java特性文件（键=值）】。（这里只说明properties文件） 

1、配置根Logger 

        其语法为： 
        log4j.rootLogger = [ level ] , appenderName1, appenderName2, … 
        level : 是日志记录的优先级，分为OFF、FATAL、ERROR、WARN、INFO、DEBUG、ALL或者您定义的级别。Log4j建议只使用四个级别，优先级从高到低分别是ERROR、WARN、INFO、DEBUG。通过在这里定义的级别，您可以控制到应用程序中相应级别的日志信息的开关。比如在这里定 义了INFO级别，则应用程序中所有DEBUG级别的日志信息将不被打印出来。appenderName:就是指定日志信息输出到哪个地方。您可以同时指定多个输出目的地。 
       例如：log4j.rootLogger＝info,A1,B2,C3 

2、配置日志信息输出目的地 

        其语法为： 
        log4j.appender.appenderName = fully.qualified.name.of.appender.class    // 
        "fully.qualified.name.of.appender.class" 可以指定下面五个目的地中的一个： 

            1.org.apache.log4j.ConsoleAppender（控制台） 
            2.org.apache.log4j.FileAppender（文件） 
            3.org.apache.log4j.DailyRollingFileAppender（每天产生一个日志文件） 
            4.org.apache.log4j.RollingFileAppender（文件大小到达指定尺寸的时候产生一个新的文件） 
            5.org.apache.log4j.WriterAppender（将日志信息以流格式发送到任意指定的地方） 

               1.ConsoleAppender选项 
                      Threshold=WARN:指定日志消息的输出最低层次。 
                      ImmediateFlush=true:默认值是true,意谓着所有的消息都会被立即输出。 
                      Target=System.err：默认情况下是：System.out,指定输出控制台 
                2.FileAppender 选项 
                      Threshold=WARN:指定日志消息的输出最低层次。 
                      ImmediateFlush=true:默认值是true,意谓着所有的消息都会被立即输出。 
                      File=mylog.txt:指定消息输出到mylog.txt文件。 
                      Append=false:默认值是true,即将消息增加到指定文件中，false指将消息覆盖指定的文件内容。 
              3.DailyRollingFileAppender 选项 
                      Threshold=WARN:指定日志消息的输出最低层次。 
                      ImmediateFlush=true:默认值是true,意谓着所有的消息都会被立即输出。 
                      File=mylog.txt:指定消息输出到mylog.txt文件。 
                      Append=false:默认值是true,即将消息增加到指定文件中，false指将消息覆盖指定的文件内容。 
                      DatePattern=''.''yyyy-ww:每周滚动一次文件，即每周产生一个新的文件。当然也可以指定按月、周、天、时和分。即对应的格式如下： 
                      1)''.''yyyy-MM: 每月 
                      2)''.''yyyy-ww: 每周  
                      3)''.''yyyy-MM-dd: 每天 
                      4)''.''yyyy-MM-dd-a: 每天两次 
                      5)''.''yyyy-MM-dd-HH: 每小时 
                      6)''.''yyyy-MM-dd-HH-mm: 每分钟 
              4.RollingFileAppender 选项 
                      Threshold=WARN:指定日志消息的输出最低层次。 
                      ImmediateFlush=true:默认值是true,意谓着所有的消息都会被立即输出。 
                      File=mylog.txt:指定消息输出到mylog.txt文件。 
                      Append=false:默认值是true,即将消息增加到指定文件中，false指将消息覆盖指定的文件内容。 
                      MaxFileSize=100KB: 后缀可以是KB, MB 或者是 GB. 在日志文件到达该大小时，将会自动滚动，即将原来的内容移到mylog.log.1文件。 
                      MaxBackupIndex=2:指定可以产生的滚动文件的最大数。 

3、配置日志信息的格式 

          其语法为： 
　　1). log4j.appender.appenderName.layout = fully.qualified.name.of.layout.class 
                "fully.qualified.name.of.layout.class" 可以指定下面4个格式中的一个： 
                 1.org.apache.log4j.HTMLLayout（以HTML表格形式布局）， 
　　         2.org.apache.log4j.PatternLayout（可以灵活地指定布局模式）， 
　　         3.org.apache.log4j.SimpleLayout（包含日志信息的级别和信息字符串）， 
　　         4.org.apache.log4j.TTCCLayout（包含日志产生的时间、线程、类别等等信息） 
                     1.HTMLLayout 选项 
                        LocationInfo=true:默认值是false,输出java文件名称和行号 
                        Title=my app file: 默认值是 Log4J Log Messages. 
                     2.PatternLayout 选项 
                        ConversionPattern=%m%n :指定怎样格式化指定的消息。 
                     3.XMLLayout    选项 
                        LocationInfo=true:默认值是false,输出java文件和行号 
          2). log4j.appender.A1.layout.ConversionPattern=%-4r %-5p %d{yyyy-MM-dd HH:mm:ssS} %c %m%n 
             这里需要说明的就是日志信息格式中几个符号所代表的含义： 
　　           －X号: X信息输出时左对齐； 
                     %p: 输出日志信息优先级，即DEBUG，INFO，WARN，ERROR，FATAL, 
                     %d: 输出日志时间点的日期或时间，默认格式为ISO8601，也可以在其后指定格式，比如：%d{yyy MMM dd HH:mm:ss,SSS}，输出类似：2002年10月18日 22：10：28，921 
                     %r: 输出自应用启动到输出该log信息耗费的毫秒数 
                     %c: 输出日志信息所属的类目，通常就是所在类的全名 
                     %t: 输出产生该日志事件的线程名 
                     %l: 输出日志事件的发生位置，相当于%C.%M(%F:%L)的组合,包括类目名、发生的线程，以及在代码中的行数。举例：Testlog4.main(TestLog4.java:10) 
                     %x: 输出和当前线程相关联的NDC(嵌套诊断环境),尤其用到像java servlets这样的多客户多线程的应用中。 
                     %%: 输出一个"%"字符 
                     %F: 输出日志消息产生时所在的文件名称 
                     %L: 输出代码中的行号 
                     %m: 输出代码中指定的消息,产生的日志具体信息 
                     %n: 输出一个回车换行符，Windows平台为" 
"，Unix平台为"
"输出日志信息换行 
              可以在%与模式字符之间加上修饰符来控制其最小宽度、最大宽度、和文本的对齐方式。如： 
                       1)%20c：指定输出category的名称，最小的宽度是20，如果category的名称小于20的话，默认的情况下右对齐。 
                       2)%-20c:指定输出category的名称，最小的宽度是20，如果category的名称小于20的话，"-"号指定左对齐。 
                       3)%.30c:指定输出category的名称，最大的宽度是30，如果category的名称大于30的话，就会将左边多出的字符截掉，但小于30的话也不会有空格。 
                       4)%20.30c:如果category的名称小于20就补空格，并且右对齐，如果其名称长于30字符，就从左边交远销出的字符截掉 

比较详细的例子 

log4j.rootLogger=INFO,consoleAppender,logfile,MAIL
log4j.addivity.org.apache=true

#ConsoleAppender，控制台输出
#FileAppender，文件日志输出
#SMTPAppender，发邮件输出日志
#SocketAppender，Socket日志
#NTEventLogAppender，Window NT日志
#SyslogAppender，
#JMSAppender，
#AsyncAppender，
#NullAppender

#文件输出：RollingFileAppender
#log4j.rootLogger = INFO,logfile
log4j.appender.logfile = org.apache.log4j.RollingFileAppender
log4j.appender.logfile.Threshold = INFO   
# 输出以上的INFO信息 
log4j.appender.logfile.File = INFO_log.html     
#保存log文件路径 
log4j.appender.logfile.Append = true    
# 默认为true，添加到末尾，false在每次启动时进行覆盖 
log4j.appender.logfile.MaxFileSize = 1MB 
# 一个log文件的大小，超过这个大小就又会生成1个日志 # KB ，MB，GB 
log4j.appender.logfile.MaxBackupIndex = 3   
# 最多保存3个文件备份 
log4j.appender.logfile.layout = org.apache.log4j.HTMLLayout 
# 输出文件的格式 
log4j.appender.logfile.layout.LocationInfo = true 
#是否显示类名和行数
log4j.appender.logfile.layout.Title =title:\u63d0\u9192\u60a8\uff1a\u7cfb\u7edf\u53d1\u751f\u4e86\u4e25\u91cd\u9519\u8bef 
#html页面的 < title > 
############################## SampleLayout ####################################
# log4j.appender.logfile.layout = org.apache.log4j.SampleLayout
############################## PatternLayout ###################################
# log4j.appender.logfile.layout = org.apache.log4j.PatternLayout
# log4j.appender.logfile.layout.ConversionPattern =% d % p [ % c] -   % m % n % d
############################## XMLLayout #######################################
# log4j.appender.logfile.layout = org.apache.log4j.XMLLayout
# log4j.appender.logfile.layout.LocationInfo = true #是否显示类名和行数
############################## TTCCLayout ######################################
# log4j.appender.logfile.layout = org.apache.log4j.TTCCLayout
# log4j.appender.logfile.layout.DateFormat = ISO8601
#NULL, RELATIVE, ABSOLUTE, DATE or ISO8601.
# log4j.appender.logfile.layout.TimeZoneID = GMT - 8 : 00 
# log4j.appender.logfile.layout.CategoryPrefixing = false ##默认为true 打印类别名
# log4j.appender.logfile.layout.ContextPrinting = false ##默认为true 打印上下文信息
# log4j.appender.logfile.layout.ThreadPrinting = false ##默认为true 打印线程名
# 打印信息如下：
#2007 - 09 - 13   14 : 45 : 39 , 765 [http - 8080 - 1 ] ERROR com.poxool.test.test - error成功关闭链接
###############################################################################
#每天文件的输出：DailyRollingFileAppender
#log4j.rootLogger = INFO,errorlogfile
log4j.appender.errorlogfile = org.apache.log4j.DailyRollingFileAppender
log4j.appender.errorlogfile.Threshold = ERROR
log4j.appender.errorlogfile.File = ../logs/ERROR_log
log4j.appender.errorlogfile.Append = true 
#默认为true，添加到末尾，false在每次启动时进行覆盖
log4j.appender.errorlogfile.ImmediateFlush = true   
#直接输出，不进行缓存
# ' . ' yyyy - MM: 每个月更新一个log日志
# ' . ' yyyy - ww: 每个星期更新一个log日志
# ' . ' yyyy - MM - dd: 每天更新一个log日志
# ' . ' yyyy - MM - dd - a: 每天的午夜和正午更新一个log日志
# ' . ' yyyy - MM - dd - HH: 每小时更新一个log日志
# ' . ' yyyy - MM - dd - HH - mm: 每分钟更新一个log日志
log4j.appender.errorlogfile.DatePattern = ' . ' yyyy - MM - dd ' .log ' 
#文件名称的格式
log4j.appender.errorlogfile.layout = org.apache.log4j.PatternLayout
log4j.appender.errorlogfile.layout.ConversionPattern =%d %p [ %c] -   %m %n %d

#控制台输出：
#log4j.rootLogger = INFO,consoleAppender
log4j.appender.consoleAppender = org.apache.log4j.ConsoleAppender
log4j.appender.consoleAppender.Threshold = ERROR
log4j.appender.consoleAppender.layout = org.apache.log4j.PatternLayout
log4j.appender.consoleAppender.layout.ConversionPattern =%d %-5p %m %n
log4j.appender.consoleAppender.ImmediateFlush = true

# 直接输出，不进行缓存 
log4j.appender.consoleAppender.Target = System.err 
# 默认是System.out方式输出 

#发送邮件：SMTPAppender
#log4j.rootLogger = INFO,MAIL
log4j.appender.MAIL = org.apache.log4j.net.SMTPAppender
log4j.appender.MAIL.Threshold = INFO
log4j.appender.MAIL.BufferSize = 10
log4j.appender.MAIL.From = yourmail@gmail.com
log4j.appender.MAIL.SMTPHost = smtp.gmail.com
log4j.appender.MAIL.Subject = Log4J Message
log4j.appender.MAIL.To = yourmail@gmail.com
log4j.appender.MAIL.layout = org.apache.log4j.PatternLayout
log4j.appender.MAIL.layout.ConversionPattern =%d - %c -%-4r [%t] %-5p %c %x - %m %n

#数据库：JDBCAppender
log4j.appender.DATABASE = org.apache.log4j.jdbc.JDBCAppender
log4j.appender.DATABASE.URL = jdbc:oracle:thin:@ 210.51 . 173.94 : 1521 :YDB
log4j.appender.DATABASE.driver = oracle.jdbc.driver.OracleDriver
log4j.appender.DATABASE.user = ydbuser
log4j.appender.DATABASE.password = ydbuser
log4j.appender.DATABASE.sql = INSERT INTO A1 (TITLE3) VALUES ( ' %d - %c %-5p %c %x - %m%n ' )
log4j.appender.DATABASE.layout = org.apache.log4j.PatternLayout
log4j.appender.DATABASE.layout.ConversionPattern =% d -   % c -%- 4r [ % t] %- 5p % c % x -   % m % n
#数据库的链接会有问题，可以重写org.apache.log4j.jdbc.JDBCAppender的getConnection() 使用数据库链接池去得链接，可以避免insert一条就链接一次数据库







log4j.properties 使用
	一.参数意义说明
	输出级别的种类
	ERROR、WARN、INFO、DEBUG
	ERROR 为严重错误 主要是程序的错误
	WARN 为一般警告，比如session丢失
	INFO 为一般要显示的信息，比如登录登出
	DEBUG 为程序的调试信息
	配置日志信息输出目的地
	log4j.appender.appenderName = fully.qualified.name.of.appender.class
	1.org.apache.log4j.ConsoleAppender（控制台）
	2.org.apache.log4j.FileAppender（文件）
	3.org.apache.log4j.DailyRollingFileAppender（每天产生一个日志文件）
	4.org.apache.log4j.RollingFileAppender（文件大小到达指定尺寸的时候产生一个新的文件）
	5.org.apache.log4j.WriterAppender（将日志信息以流格式发送到任意指定的地方）
	配置日志信息的格式
	log4j.appender.appenderName.layout = fully.qualified.name.of.layout.class
	1.org.apache.log4j.HTMLLayout（以HTML表格形式布局），
	2.org.apache.log4j.PatternLayout（可以灵活地指定布局模式），
	3.org.apache.log4j.SimpleLayout（包含日志信息的级别和信息字符串），
	4.org.apache.log4j.TTCCLayout（包含日志产生的时间、线程、类别等等信息）
	控制台选项
	Threshold=DEBUG:指定日志消息的输出最低层次。
	ImmediateFlush=true:默认值是true,意谓着所有的消息都会被立即输出。
	Target=System.err：默认情况下是：System.out,指定输出控制台
	FileAppender 选项
	Threshold=DEBUF:指定日志消息的输出最低层次。
	ImmediateFlush=true:默认值是true,意谓着所有的消息都会被立即输出。
	File=mylog.txt:指定消息输出到mylog.txt文件。
	Append=false:默认值是true,即将消息增加到指定文件中，false指将消息覆盖指定的文件内容。
	RollingFileAppender 选项
	Threshold=DEBUG:指定日志消息的输出最低层次。
	ImmediateFlush=true:默认值是true,意谓着所有的消息都会被立即输出。
	File=mylog.txt:指定消息输出到mylog.txt文件。
	Append=false:默认值是true,即将消息增加到指定文件中，false指将消息覆盖指定的文件内容。
	MaxFileSize=100KB: 后缀可以是KB, MB 或者是 GB. 在日志文件到达该大小时，将会自动滚动，即将原来的内容移到mylog.log.1文件。
	MaxBackupIndex=2:指定可以产生的滚动文件的最大数。
	log4j.appender.A1.layout.ConversionPattern=%-4r %-5p %d{yyyy-MM-dd HH:mm:ssS} %c %m%n
	日志信息格式中几个符号所代表的含义：
	 -X号: X信息输出时左对齐；
	 %p: 输出日志信息优先级，即DEBUG，INFO，WARN，ERROR，FATAL,
	 %d: 输出日志时间点的日期或时间，默认格式为ISO8601，也可以在其后指定格式，比如：%d{yyy MMM dd HH:mm:ss,SSS}，输出类似：2002年10月18日 22：10：28，921
	 %r: 输出自应用启动到输出该log信息耗费的毫秒数
	 %c: 输出日志信息所属的类目，通常就是所在类的全名
	 %t: 输出产生该日志事件的线程名
	 %l: 输出日志事件的发生位置，相当于%C.%M(%F:%L)的组合,包括类目名、发生的线程，以及在代码中的行数。举例：Testlog4.main (TestLog4.java:10)
	 %x: 输出和当前线程相关联的NDC(嵌套诊断环境),尤其用到像java servlets这样的多客户多线程的应用中。
	 %%: 输出一个"%"字符
	 %F: 输出日志消息产生时所在的文件名称
	 %L: 输出代码中的行号
	 %m: 输出代码中指定的消息,产生的日志具体信息
	 %n: 输出一个回车换行符，Windows平台为"\r\n"，Unix平台为"\n"输出日志信息换行
	 可以在%与模式字符之间加上修饰符来控制其最小宽度、最大宽度、和文本的对齐方式。如：
	 1)%20c：指定输出category的名称，最小的宽度是20，如果category的名称小于20的话，默认的情况下右对齐。
	 2)%-20c:指定输出category的名称，最小的宽度是20，如果category的名称小于20的话，"-"号指定左对齐。
	 3)%.30c:指定输出category的名称，最大的宽度是30，如果category的名称大于30的话，就会将左边多出的字符截掉，但小于30的话也不会有空格。
	 4)%20.30c:如果category的名称小于20就补空格，并且右对齐，如果其名称长于30字符，就从左边较远输出的字符截掉。
	二.文件配置Sample1
	log4j.rootLogger=DEBUG,A1,R
	#log4j.rootLogger=INFO,A1,R
	# ConsoleAppender 输出
	log4j.appender.A1=org.apache.log4j.ConsoleAppender
	log4j.appender.A1.layout=org.apache.log4j.PatternLayout
	log4j.appender.A1.layout.ConversionPattern=%-d{yyyy-MM-dd HH:mm:ss,SSS} [%c]-[%p] %m%n
	# File 输出 一天一个文件,输出路径可以定制,一般在根路径下
	log4j.appender.R=org.apache.log4j.DailyRollingFileAppender
	log4j.appender.R.File=blog_log.txt
	log4j.appender.R.MaxFileSize=500KB
	log4j.appender.R.MaxBackupIndex=10
	log4j.appender.R.layout=org.apache.log4j.PatternLayout
	log4j.appender.R.layout.ConversionPattern=%d{yyyy-MM-dd HH:mm:ss,SSS} [%t] [%c] [%p] - %m%n
	文件配置Sample2
	下面给出的Log4J配置文件实现了输出到控制台，文件，回滚文件，发送日志邮件，输出到数据库日志表，自定义标签等全套功能。
	log4j.rootLogger=DEBUG,CONSOLE,A1,im 
	#DEBUG,CONSOLE,FILE,ROLLING_FILE,MAIL,DATABASE
	log4j.addivity.org.apache=true
	################### 
	# Console Appender 
	################### 
	log4j.appender.CONSOLE=org.apache.log4j.ConsoleAppender 
	log4j.appender.Threshold=DEBUG 
	log4j.appender.CONSOLE.Target=System.out 
	log4j.appender.CONSOLE.layout=org.apache.log4j.PatternLayout 
	log4j.appender.CONSOLE.layout.ConversionPattern=[framework] %d - %c -%-4r [%t] %-5p %c %x - %m%n 
	#log4j.appender.CONSOLE.layout.ConversionPattern=[start]%d{DATE}[DATE]%n%p[PRIORITY]%n%x[NDC]%n%t[THREAD] n%c[CATEGORY]%n%m[MESSAGE]%n%n
	##################### 
	# File Appender 
	##################### 
	log4j.appender.FILE=org.apache.log4j.FileAppender 
	log4j.appender.FILE.File=file.log 
	log4j.appender.FILE.Append=false 
	log4j.appender.FILE.layout=org.apache.log4j.PatternLayout 
	log4j.appender.FILE.layout.ConversionPattern=[framework] %d - %c -%-4r [%t] %-5p %c %x - %m%n 
	# Use this layout for LogFactor 5 analysis
	######################## 
	# Rolling File 
	######################## 
	log4j.appender.ROLLING_FILE=org.apache.log4j.RollingFileAppender 
	log4j.appender.ROLLING_FILE.Threshold=ERROR 
	log4j.appender.ROLLING_FILE.File=rolling.log 
	log4j.appender.ROLLING_FILE.Append=true 
	log4j.appender.ROLLING_FILE.MaxFileSize=10KB 
	log4j.appender.ROLLING_FILE.MaxBackupIndex=1 
	log4j.appender.ROLLING_FILE.layout=org.apache.log4j.PatternLayout 
	log4j.appender.ROLLING_FILE.layout.ConversionPattern=[framework] %d - %c -%-4r [%t] %-5p %c %x - %m%n
	#################### 
	# Socket Appender 
	#################### 
	log4j.appender.SOCKET=org.apache.log4j.RollingFileAppender 
	log4j.appender.SOCKET.RemoteHost=localhost 
	log4j.appender.SOCKET.Port=5001 
	log4j.appender.SOCKET.LocationInfo=true 
	# Set up for Log Facter 5 
	log4j.appender.SOCKET.layout=org.apache.log4j.PatternLayout 
	log4j.appender.SOCET.layout.ConversionPattern=[start]%d{DATE}[DATE]%n%p[PRIORITY]%n%x[NDC]%n%t[THREAD]%n%c[CATEGORY]%n%m[MESSAGE]%n%n
	######################## 
	# Log Factor 5 Appender 
	######################## 
	log4j.appender.LF5_APPENDER=org.apache.log4j.lf5.LF5Appender 
	log4j.appender.LF5_APPENDER.MaxNumberOfRecords=2000
	######################## 
	# SMTP Appender 
	####################### 
	log4j.appender.MAIL=org.apache.log4j.net.SMTPAppender 
	log4j.appender.MAIL.Threshold=FATAL 
	log4j.appender.MAIL.BufferSize=10 
	log4j.appender.MAIL.From=chenyl@yeqiangwei.com 
	log4j.appender.MAIL.SMTPHost=mail.hollycrm.com 
	log4j.appender.MAIL.Subject=Log4J Message 
	log4j.appender.MAIL.To=chenyl@yeqiangwei.com 
	log4j.appender.MAIL.layout=org.apache.log4j.PatternLayout 
	log4j.appender.MAIL.layout.ConversionPattern=[framework] %d - %c -%-4r [%t] %-5p %c %x - %m%n
	######################## 
	# JDBC Appender 
	####################### 
	log4j.appender.DATABASE=org.apache.log4j.jdbc.JDBCAppender 
	log4j.appender.DATABASE.URL=jdbc:mysql://localhost:3306/test 
	log4j.appender.DATABASE.driver=com.mysql.jdbc.Driver 
	log4j.appender.DATABASE.user=root 
	log4j.appender.DATABASE.password= 
	log4j.appender.DATABASE.sql=INSERT INTO LOG4J (Message) VALUES ('[framework] %d - %c -%-4r [%t] %-5p %c %x - %m%n') 
	log4j.appender.DATABASE.layout=org.apache.log4j.PatternLayout 
	log4j.appender.DATABASE.layout.ConversionPattern=[framework] %d - %c -%-4r [%t] %-5p %c %x - %m%n
	log4j.appender.A1=org.apache.log4j.DailyRollingFileAppender 
	log4j.appender.A1.File=SampleMessages.log4j 
	log4j.appender.A1.DatePattern=yyyyMMdd-HH'.log4j' 
	log4j.appender.A1.layout=org.apache.log4j.xml.XMLLayout
	################### 
	#自定义Appender 
	################### 
	log4j.appender.im = net.cybercorlin.util.logger.appender.IMAppender
	log4j.appender.im.host = mail.cybercorlin.net 
	log4j.appender.im.username = username 
	log4j.appender.im.password = password 
	log4j.appender.im.recipient = corlin@yeqiangwei.com 
	log4j.appender.im.layout=org.apache.log4j.PatternLayout 
	log4j.appender.im.layout.ConversionPattern =[framework] %d - %c -%-4r [%t] %-5p %c %x - %m%n
	三.高级使用
	实验目的：
	 1.把FATAL级错误写入2000NT日志
	 2. WARN，ERROR，FATAL级错误发送email通知管理员
	 3.其他级别的错误直接在后台输出
	实验步骤：
	 输出到2000NT日志
	 1.把Log4j压缩包里的NTEventLogAppender.dll拷到WINNT\SYSTEM32目录下
	 2.写配置文件log4j.properties
	# 在2000系统日志输出
	 log4j.logger.NTlog=FATAL, A8
	 # APPENDER A8
	 log4j.appender.A8=org.apache.log4j.nt.NTEventLogAppender
	 log4j.appender.A8.Source=JavaTest
	 log4j.appender.A8.layout=org.apache.log4j.PatternLayout
	 log4j.appender.A8.layout.ConversionPattern=%-4r %-5p [%t] %37c %3x - %m%n
	3.调用代码：
	 Logger logger2 = Logger.getLogger("NTlog"); //要和配置文件中设置的名字相同
	 logger2.debug("debug!!!");
	 logger2.info("info!!!");
	 logger2.warn("warn!!!");
	 logger2.error("error!!!");
	 //只有这个错误才会写入2000日志
	 logger2.fatal("fatal!!!");
	发送email通知管理员：
	 1. 首先下载JavaMail和JAF, 
	  http://java.sun.com/j2ee/ja/javamail/index.html 
	  http://java.sun.com/beans/glasgow/jaf.html 
	 在项目中引用mail.jar和activation.jar。
	 2. 写配置文件
	 # 将日志发送到email
	 log4j.logger.MailLog=WARN,A5
	 #  APPENDER A5
	 log4j.appender.A5=org.apache.log4j.net.SMTPAppender
	 log4j.appender.A5.BufferSize=5
	 log4j.appender.A5.To=chunjie@yeqiangwei.com 
	 log4j.appender.A5.From=error@yeqiangwei.com 
	 log4j.appender.A5.Subject=ErrorLog
	 log4j.appender.A5.SMTPHost=smtp.263.net
	 log4j.appender.A5.layout=org.apache.log4j.PatternLayout
	 log4j.appender.A5.layout.ConversionPattern=%-4r %-5p [%t] %37c %3x - %m%n
	 3.调用代码：
	 //把日志发送到mail
	 Logger logger3 = Logger.getLogger("MailLog");
	 logger3.warn("warn!!!");
	 logger3.error("error!!!");
	 logger3.fatal("fatal!!!");
	在后台输出所有类别的错误：
	 1. 写配置文件
	 # 在后台输出
	 log4j.logger.console=DEBUG, A1
	 # APPENDER A1
	 log4j.appender.A1=org.apache.log4j.ConsoleAppender
	 log4j.appender.A1.layout=org.apache.log4j.PatternLayout
	 log4j.appender.A1.layout.ConversionPattern=%-4r %-5p [%t] %37c %3x - %m%n
	 2．调用代码
	 Logger logger1 = Logger.getLogger("console");
	 logger1.debug("debug!!!");
	 logger1.info("info!!!");
	 logger1.warn("warn!!!");
	 logger1.error("error!!!");
	 logger1.fatal("fatal!!!");
	--------------------------------------------------------------------
	 全部配置文件：log4j.properties
	 # 在后台输出
	 log4j.logger.console=DEBUG, A1
	 # APPENDER A1
	 log4j.appender.A1=org.apache.log4j.ConsoleAppender
	 log4j.appender.A1.layout=org.apache.log4j.PatternLayout
	 log4j.appender.A1.layout.ConversionPattern=%-4r %-5p [%t] %37c %3x - %m%n
	# 在2000系统日志输出
	 log4j.logger.NTlog=FATAL, A8
	 # APPENDER A8
	 log4j.appender.A8=org.apache.log4j.nt.NTEventLogAppender
	 log4j.appender.A8.Source=JavaTest
	 log4j.appender.A8.layout=org.apache.log4j.PatternLayout
	 log4j.appender.A8.layout.ConversionPattern=%-4r %-5p [%t] %37c %3x - %m%n
	# 将日志发送到email
	 log4j.logger.MailLog=WARN,A5
	 #  APPENDER A5
	 log4j.appender.A5=org.apache.log4j.net.SMTPAppender
	 log4j.appender.A5.BufferSize=5
	 log4j.appender.A5.To=chunjie@yeqiangwei.com 
	 log4j.appender.A5.From=error@yeqiangwei.com 
	 log4j.appender.A5.Subject=ErrorLog
	 log4j.appender.A5.SMTPHost=smtp.263.net
	 log4j.appender.A5.layout=org.apache.log4j.PatternLayout
	 log4j.appender.A5.layout.ConversionPattern=%-4r %-5p [%t] %37c %3x - %m%n
	全部代码：Log4jTest.java
	  
	/* 
	  * 创建日期 2003-11-13 
	  */ 
	 package edu.bcu.Bean; 
	 import org.apache.log4j.*; 
	 //import org.apache.log4j.nt.*; 
	 //import org.apache.log4j.net.*; 
	 /** 
	  * @author yanxu 
	  */ 
	 public class Log4jTest 
	 { 
	  public static void main(String args[]) 
	  { 
	   PropertyConfigurator.configure("log4j.properties"); 
	   //在后台输出 
	   Logger logger1 = Logger.getLogger("console"); 
	   logger1.debug("debug!!!"); 
	   logger1.info("info!!!"); 
	   logger1.warn("warn!!!"); 
	   logger1.error("error!!!"); 
	   logger1.fatal("fatal!!!");
	//在NT系统日志输出 
	   Logger logger2 = Logger.getLogger("NTlog"); 
	   //NTEventLogAppender nla = new NTEventLogAppender(); 
	   logger2.debug("debug!!!"); 
	   logger2.info("info!!!"); 
	   logger2.warn("warn!!!"); 
	   logger2.error("error!!!"); 
	   //只有这个错误才会写入2000日志 
	   logger2.fatal("fatal!!!");
	//把日志发送到mail 
	   Logger logger3 = Logger.getLogger("MailLog"); 
	   //SMTPAppender sa = new SMTPAppender(); 
	   logger3.warn("warn!!!"); 
	   logger3.error("error!!!"); 
	   logger3.fatal("fatal!!!"); 
	  } 
	 }












