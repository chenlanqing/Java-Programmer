
# 1、介绍

# 2、安装

[](../辅助资料/环境配置/大数据环境.md#3Hive环境配置)

# 3、基本用法

创建表：
```
CREATE  TABLE table_name 
  [(col_name data_type [COMMENT col_comment])]
```

加载数据到表中：
```
LOAD DATA LOCAL INPATH 'filepath' INTO TABLE tablename

load data local inpath '/home/hadoop/data/hello.txt' into table hive_wordcount;
```