*本文基于MySQL 8.0编写，理论支持MySQL 5.6及更高版本。*

`OPTIMIZER_TRACE`是MySQL 5.6引入的一项跟踪功能，它可以跟踪优化器做出的各种决策（比如访问表的方法、各种开销计算、各种转换等），并将跟踪结果记录到 `INFORMATION_SCHEMA.OPTIMIZER_TRACE` 表中。此功能默认关闭，开启后，可分析如下语句：`SELECT、INSERT、REPLACE、UPDATE、DELETE、EXPLAIN、SET、DECLARE、CASE、IF、RETURN、CALL`；

## 1、OPTIMIZER_TRACE 参数

- [参考文档](https://dev.mysql.com/doc/internals/en/system-variables-controlling-trace.html)

- optimizer_trace
    - optimizer_trace总开关，默认值：`enabled=off,one_line=off`
    - enabled：是否开启`optimizer_trace`；on表示开启，off表示关闭。
    - one_line：是否开启单行存储。on表示开启；off表示关闭，将会用标准的JSON格式化存储。设置成on将会有良好的格式，设置成off可节省一些空间；
    