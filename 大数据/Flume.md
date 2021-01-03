项目经验之Flume组件
1）Source
（1）Taildir Source相比Exec Source、Spooling Directory Source的优势
TailDir Source：断点续传、多目录。Flume1.6以前需要自己自定义Source记录每次读取文件位置，实现断点续传。
Exec Source可以实时搜集数据，但是在Flume不运行或者Shell命令出错的情况下，数据将会丢失。
Spooling Directory Source监控目录，不支持断点续传。
（2）batchSize大小如何设置？
答：Event 1K左右时，500-1000合适（默认为100）
2）Channel
采用Kafka Channel，省去了Sink，提高了效率。KafkaChannel数据存储在Kafka里面，所以数据是存储在磁盘中。
注意在Flume1.7以前，Kafka Channel很少有人使用，因为发现parseAsFlumeEvent这个配置起不了作用。也就是无论parseAsFlumeEvent配置为true还是false，都会转为Flume Event。这样的话，造成的结果是，会始终都把Flume的headers中的信息混合着内容一起写入Kafka的消息中，这显然不是我所需要的，我只是需要把内容写入即可


## Flume往kafka生产消息

Kafka-channel配置
```
#定义Agent必需的组件名称，同时指定本配置文件的Agent名称为a1
a1.sources=r1
a1.channels=c1 c2

#定义Source组件相关配置
#使用Taildir Source
a1.sources.r1.type = TAILDIR
#配置Taildir Source，保存断点位置文件的目录
a1.sources.r1.positionFile = /opt/module/flume/test/log_position.json
#配置监控目录组
a1.sources.r1.filegroups = f1
#配置目录组下的目录，可配置多个目录
a1.sources.r1.filegroups.f1 = /tmp/logs/app.+

#配置Source发送数据的目标Channel
a1.sources.r1.channels = c1 c2

#拦截器
#配置拦截器名称
a1.sources.r1.interceptors =  i1 i2
#配置拦截器名称，需要写明全类名
a1.sources.r1.interceptors.i1.type = com.blue.fish.flume.interceptor.LogETLInterceptor$Builder
a1.sources.r1.interceptors.i2.type = com.blue.fish.flume.interceptor.LogTypeInterceptor$Builder

#配置Channel选择器
#配置选择器类型
a1.sources.r1.selector.type = multiplexing
#配置选择器识别header中的key
a1.sources.r1.selector.header = topic
#配置不同的header信息，发往不同的Channel
a1.sources.r1.selector.mapping.topic_start = c1
a1.sources.r1.selector.mapping.topic_event = c2

# configure channel配置Channel
#配置Channel类型为Kafka Channel
a1.channels.c1.type = org.apache.flume.channel.kafka.KafkaChannel
#配置Kafka集群节点服务器列表
a1.channels.c1.kafka.bootstrap.servers = bluefish:9092
#配置该Channel发往Kafka的Topic，该Topic需要在Kafka中提前创建
a1.channels.c1.kafka.topic = topic_start
#配置不将header信息解析为event内容
a1.channels.c1.parseAsFlumeEvent = false
#配置该Kafka Channel所属的消费者组名，为实现multiplexing类型的Channel选择器，应将2个Kafka Channel配置相同的消费者组
a1.channels.c1.kafka.consumer.group.id = flume-consumer

#配置同上
a1.channels.c2.type = org.apache.flume.channel.kafka.KafkaChannel
a1.channels.c2.kafka.bootstrap.servers = bluefish:9092
a1.channels.c2.kafka.topic = topic_event
a1.channels.c2.parseAsFlumeEvent = false
a1.channels.c2.kafka.consumer.group.id = flume-consumer
```

自定义kafka-channel拦截器：
- 定义类实现 `org.apache.flume.interceptor.Interceptor`
- 重写4个方法：
    - 初始化
    - 单event处理
    - 多event处理
    - 关闭
- 在实现类中定义静态内部类实现 `org.apache.flume.interceptor.Interceptor.Builder`
    ```java
    public static class Builder implements  Interceptor.Builder{
        @Override
        public Interceptor build() {
            return new LogTypeInterceptor();
        }
        @Override
        public void configure(Context context) {
        }
    }
    ```
- 打包上传到`flume/lib`
- 在配置文件中配关联拦截器，全类名$builder：`com.blue.fish.flume.interceptor.LogETLInterceptor$Builder`

启动Flume：`nohup bin/flume-ng agent --name a1 --conf-file conf/file-flume-kafka.conf &`

--name 跟 file-flume-kafka.conf 配置的agent一样

## Flume从Kafka消费数据

flume从kafka消费数据并保存到 hdfs
```conf
## Flume Agent组件声明
a1.sources=r1 r2
a1.channels=c1 c2
a1.sinks=k1 k2

## Source1属性配置
#配置Source类型为Kafka Source
a1.sources.r1.type = org.apache.flume.source.kafka.KafkaSource
#配置Kafka Source每次从Kafka Topic中拉取的event个数
a1.sources.r1.batchSize = 5000
#配置拉取数据批次间隔为2000毫秒
a1.sources.r1.batchDurationMillis = 2000
#配置Kafka集群地址
a1.sources.r1.kafka.bootstrap.servers = localhost:9092
#配置Source 对接Kafka主题
a1.sources.r1.kafka.topics=topic_start

## source2属性配置，与Source1配置类似，只是消费主题不同
a1.sources.r2.type = org.apache.flume.source.kafka.KafkaSource
a1.sources.r2.batchSize = 5000
a1.sources.r2.batchDurationMillis = 2000
a1.sources.r2.kafka.bootstrap.servers = localhost:9092
a1.sources.r2.kafka.topics=topic_event

## Channel1属性配置
#配置Channel类型为File Channel
a1.channels.c1.type = file
#配置存储File Channel传输数据的断点信息目录
a1.channels.c1.checkpointDir = /opt/module/flume/checkpoint/behavior1
#配置File Channel传输数据的存储位置
a1.channels.c1.dataDirs = /opt/module/flume/data/behavior1/
#配置File Channel的最大存储容量
a1.channels.c1.maxFileSize = 2146435071
#配置File Channel最多存储event的个数
a1.channels.c1.capacity = 1000000
#配置Channel满时put事务的超时时间
a1.channels.c1.keep-alive = 6

## Channel2属性配置同Channel1，注意需要配置不同的目录路径
a1.channels.c2.type = file
a1.channels.c2.checkpointDir = /opt/module/flume/checkpoint/behavior2
a1.channels.c2.dataDirs = /opt/module/flume/data/behavior2/
a1.channels.c2.maxFileSize = 2146435071
a1.channels.c2.capacity = 1000000
a1.channels.c2.keep-alive = 6

## Sink1属性配置
#配置Sink1类型为HDFS Sink
a1.sinks.k1.type = hdfs
#配置发到HDFS的存储路径
a1.sinks.k1.hdfs.path = /origin_data/gmall/log/topic_start/%Y-%m-%d
#配置HDFS落盘文件的文件名前缀
a1.sinks.k1.hdfs.filePrefix = logstart-

##Sink2 属性配置同Sink1
a1.sinks.k2.type = hdfs
a1.sinks.k2.hdfs.path = /origin_data/gmall/log/topic_event/%Y-%m-%d
a1.sinks.k2.hdfs.filePrefix = logevent-

## 避免产生大量小文件的相关属性配置
a1.sinks.k1.hdfs.rollInterval = 10
a1.sinks.k1.hdfs.rollSize = 134217728
a1.sinks.k1.hdfs.rollCount = 0

a1.sinks.k2.hdfs.rollInterval = 10
a1.sinks.k2.hdfs.rollSize = 134217728
a1.sinks.k2.hdfs.rollCount = 0

## 控制输出文件是压缩文件
a1.sinks.k1.hdfs.fileType = CompressedStream
a1.sinks.k2.hdfs.fileType = CompressedStream

a1.sinks.k1.hdfs.codeC = lzop
a1.sinks.k2.hdfs.codeC = lzop

## 拼装
a1.sources.r1.channels = c1
a1.sinks.k1.channel= c1

a1.sources.r2.channels = c2
a1.sinks.k2.channel= c2
```

nohup bin/flume-ng agent --name a1 --conf-file conf/kafka-flume-hdfs.conf --name a1 -Dflume.root.logger=INFO,LOGFILE >/opt/module/flume/log.txt   2>&1 &