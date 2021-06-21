# 一、Presto

- [Presto原理&调优&面试](https://mp.weixin.qq.com/s/8CWXY864_QSmwNZ6Xh5B3w)
- [Presto实现原理和美团的使用实践](https://tech.meituan.com/2014/06/16/presto.html)
- [Presto使用指南](https://www.imangodoc.com/46466.html)
- [Presto官方文档](https://trino.io/docs/current/overview.html)
- [Presto在腾讯的实践](https://mp.weixin.qq.com/s/d56Qco7LUM3NroFq9PcVXA)
- [Presto实践](https://mp.weixin.qq.com/s/mnElO5Sau1VXLnhICKNdyA)

## 1、简介

## 2、环境搭建

启动presto：  ./launcher start

启动hive以便 thrift连接

hive --service hiveserver2 &
hive --service metastore &

./presto --server localhost:7670 --catalog hive --schema default

通过presto查询表的分区数据：schema和catalog要放在引号外面
`select * from hive.dws."daojia_user_pickup_daily$partitions"`

# 二、ClickHouse

- [ClickHouse实现原理](https://mp.weixin.qq.com/s/aJ7vdaG8LpBpBB5fCN0ncQ)