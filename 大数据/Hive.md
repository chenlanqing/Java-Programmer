
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

Hive中null值时用    `\N` 表示
在mysql中 null 值时用 null 表示，在mysql同步数据到hive中时需要注意空值的处理


# Tez

Hive基础 Tez