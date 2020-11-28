
# 1、Spark概述

## 1.1、什么是Spark

是用于大规模数据处理的统一分析引擎

## 1.2、Spark发展

MapReduce的局限性：
- 代码繁琐；
- 只能够支持map和reduce方法；
- 执行效率低下；
- 不适合迭代多次、交互式、流式的处理；

# 2、Spark编译安装

[Spark编译安装](../辅助资料/环境配置/大数据环境.md#4Spark编译安装)

# 3、Spark基本使用

## 3.1、local模式

`spark-shell --master local[2]`

## 3.2、standlone

Standlone模式的架构和Hadoop HDFS/YARN很类似的

需要在配置文件`spark-env.sh`配置如下
```
SPARK_MASTER_HOST=hadoop001
SPARK_WORKER_CORES=2
SPARK_WORKER_MEMORY=2g
SPARK_WORKER_INSTANCES=1
```

启动standlone模式：
```
start-all.sh   会在 hadoop1机器上启动master进程，在slaves文件配置的所有hostname的机器上启动worker进程
```
进入shell界面：
```
spark-shell  --master spark://bluefish:7077
```

# 4、SparkSQL

## 4.1、SqlContext

```java
public static void main(String[] args) {
    String path = "file:///workspace/scala/spark-sql-demo/people.json";
    //1)创建相应的Context
    SparkConf conf = new SparkConf();
    //在测试或者生产中，AppName和Master我们是通过脚本进行指定
    conf.setAppName("SQLContextApp").setMaster("local[2]");
    SparkContext sc = new SparkContext(conf);
    SQLContext sqlContext = new SQLContext(sc);
    Dataset<Row> json = sqlContext.read().json(path);
    json.printSchema();
    json.show();
    sc.stop();
}
```
如果打包到服务器上运行：
```
spark-submit \
--name SQLContextApp \
--class com.blue.fish.spark.SQLContextApp \
--master local[2] \
/root/lib/spark-sql.jar \
/root/software/spark-2.4.7/examples/src/main/resources/people.json
```

## 4.2、HiveContext

```java
public static void main(String[] args) {
    //1)创建相应的Context
    SparkConf conf = new SparkConf();
    SparkContext sc = new SparkContext(conf);
    // hiveContext 是过期API
    HiveContext hc = new HiveContext(sc);
    hc.table("emp").show();
    sc.stop();
}
```
这个需要部署打包到服务器上运行：
```
spark-submit \
--name HiveContextApp \
--class com.blue.fish.spark.HiveContextApp \
--master local[2] \
--jars /root/spark-log/mysql-connector-java-5.1.28.jar \
/root/lib/spark-sql.jar
```
- `--class`：指定运行的类
- `--master`：指定spark的启动模式
- `--jars`：指定外部依赖的类，这里需要依赖mysql驱动，因为hive的元数据信息存储在mysql里的

## 4.3、SparkSession

在Spark2.0之后，主要使用SparkSession来处理了
```java
public static void main(String[] args) {
    String path = "file:///workspace/scala/spark-sql-demo/people.json";
    SparkSession session = SparkSession.builder().appName("JavaSparkSessionContext")
            .master("local[2]").getOrCreate();
    Dataset<Row> dataset = session.read().json(path);
    dataset.printSchema();
    dataset.show();
    session.stop();
}
```

## 4.4、spark-shell方式使用

spark-shell 要访问hive里面的表需要将hive-site.xml 拷贝到spark的conf目录下

进入spark-shell控制台访问hive的表
```
[root@bluefish lib]# spark-shell --master local[2] --jars /root/spark-log/mysql-connector-java-5.1.28.jar 
Spark context available as 'sc' (master = local[2], app id = local-1606538300252).
Spark session available as 'spark'.
Welcome to
      ____              __
     / __/__  ___ _____/ /__
    _\ \/ _ \/ _ `/ __/  '_/
   /___/ .__/\_,_/_/ /_/\_\   version 2.4.6
      /_/
Using Scala version 2.11.12 (Java HotSpot(TM) 64-Bit Server VM, Java 1.8.0_271)
Type in expressions to have them evaluated.
Type :help for more information.

scala> 
```
因为hive的元数据信息存储在mysql中，需要知道mysql的驱动包地址

比如查询表，执行查询等：
```
scala> spark.sql("show tables").show
20/11/28 12:39:28 WARN ObjectStore: Failed to get database global_temp, returning NoSuchObjectException
+--------+--------------+-----------+
|database|     tableName|isTemporary|
+--------+--------------+-----------+
| default|          dept|      false|
| default|           emp|      false|
| default|hive_wordcount|      false|
+--------+--------------+-----------+
scala> spark.sql("select * from dept").show
+---+------+----------+--------+
| id|deptno|     dname|     loc|
+---+------+----------+--------+
|  1|    11|KOMvRWiYwm|TGBPuCDj|
|  2|    12|nKNLpqLGXT|AWjcLvYD|
|  3|    13|WXZfDzOvMx|ZIrLDIFd|
+---+------+----------+--------+
only showing top 20 rows
```

## 4.5、thriftserver

thriftserver/beeline的使用
- 启动thriftserver: 默认端口是10000 ，可以修改：
    ```
    ./start-thriftserver.sh  \
    --master local[2] \
    --jars /root/spark-log/mysql-connector-java-5.1.28.jar  \
    ```
- 启动beeline：`beeline -u jdbc:hive2://localhost:10000 -n root`

修改thriftserver启动占用的默认端口号：
```
./start-thriftserver.sh  \
--master local[2] \
--jars /root/spark-log/mysql-connector-java-5.1.28.jar  \
--hiveconf hive.server2.thrift.port=14000
```
那么使用beeline访问时：`beeline -u jdbc:hive2://localhost:14000 -n root`

**thriftserver和普通的spark-shell/spark-sql有什么区别？**
- spark-shell、spark-sql都是一个spark  application；
- thriftserver， 不管你启动多少个客户端(beeline/code)，永远都是一个spark application；解决了一个数据共享的问题，多个客户端可以共享数据；

## 4.6、使用jdbc连接thriftserver

```java
public static void main(String[] args) throws Exception{
    Class.forName("org.apache.hive.jdbc.HiveDriver");
    Connection conn = DriverManager.getConnection("jdbc:hive2://localhost:10000", "root", "");
    PreparedStatement statement = conn.prepareStatement("select empno, ename, sal from emp limit 10");
    ResultSet rs = statement.executeQuery();
    while (rs.next()) {
        System.out.println("empno:" + rs.getInt("empno") +
                " , ename:" + rs.getString("ename") +
                " , sal:" + rs.getDouble("sal"));
    }
    rs.close();
    statement.close();
    conn.close();
}
```
**注意事项**：在使用jdbc开发时，一定要先启动thriftserver
```
Exception in thread "main" java.sql.SQLException:
Could not open client transport with JDBC Uri: jdbc:hive2://bluefish:14000:
java.net.ConnectException: Connection refused
```

# 5、DataFrame

## 5.1、概述

- DataSet：分布式的数据集；
- DataFrame：以列（列名、列的类型、列值）的形式构成的分布式数据集，按照列赋予不同的名称

以往RDD的方式：
- java/scala  ==> jvm
- python ==> python runtime

这样往往对耗时比较高，而DataFrame是：java/scala/python ==> Logic Plan

将所有提交的逻辑执行计划里面去，而不是在JVM或者Python的环境中执行

## 5.2、DataFrame 与 RDD

### 5.2.1、DataFrame使用

```java
public static void main(String[] args) {
    SparkSession session = SparkSession.builder().appName("JavaSparkSessionContext")
            .master("local[2]").getOrCreate();
    Dataset<Row> df = session.read().json("file:///workspace/scala/spark-sql-demo/people.json");
    df.printSchema();
    df.show();
    df.select("name").show();
    df.select(df.col("name"), df.col("age")).show();
    df.select(df.col("name"), df.col("age").plus(10).as("age2")).show();
    // select * from table where age > 35
    df.filter(df.col("age").gt(35)).show();
    df.groupBy(df.col("age")).count().show();
    session.stop();
}
```

- [DataFrame与RDD交互](http://spark.apache.org/docs/2.4.7/sql-getting-started.html#interoperating-with-rdds)

DataFrame和RDD互操作的两种方式：
- 反射：case class   前提：事先需要知道你的字段、字段类型    
- 编程：Row          如果第一种情况不能满足你的要求（事先不知道列）

**选型**：优先考虑第一种

### 5.2.2、反射操作方式：

```java
public static void infer(SparkSession session) {
    JavaRDD<Info> map = session.read()
            .textFile("file:///Users/bluefish/Documents/workspace/scala/spark-sql-demo/infos.txt")
            .toJavaRDD()
            .map(line -> {
                String[] lines = line.split(",");
                return new Info(Integer.parseInt(lines[0]), lines[1], Integer.parseInt(lines[2]));
            });
    Dataset<Row> df = session.createDataFrame(map, Info.class);
    df.printSchema();
    df.show();
    df.createOrReplaceTempView("infos");
    session.sql("select * from infos where age > 35").show();
    session.stop();
}
public static class Info {
    public int id;
    public String name;
    public int age;
    public Info(int id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }
}
```

### 5.2.3、编程

```java
public static void program(SparkSession session) {
    JavaRDD<String> rdd = session.read()
            .textFile("file:///Users/bluefish/Documents/workspace/scala/spark-sql-demo/infos.txt")
            .toJavaRDD();
    // 定义schema
    String schema = "id name age";

    List<StructField> fields = new ArrayList<>();
    for (String fieldName : schema.split(" ")) {
        StructField field = DataTypes.createStructField(fieldName, DataTypes.StringType, true);
        fields.add(field);
    }
    StructType structType = DataTypes.createStructType(fields);
    JavaRDD<Row> javaRDD = rdd.map((Function<String, Row>) record -> {
        String[] lines = record.split(",");
        return RowFactory.create(lines[0], lines[1], lines[2]);
    });

    Dataset<Row> df = session.createDataFrame(javaRDD, structType);
    df.printSchema();
    df.show();
    df.createOrReplaceTempView("infos");
    session.sql("select * from infos where age > 35").show();
    session.stop();
}
```

# 6、外部数据源



# 参考资料

- [深入Spark设计与原理分析](https://github.com/JerryLead/SparkInternals)
- [官方文档](http://spark.apache.org/docs/latest/index.html)