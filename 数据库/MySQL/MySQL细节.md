<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
**Table of Contents**  *generated with [DocToc](https://github.com/thlorenz/doctoc)*

- [1.MySQL自增主键问题:](#1mysql%E8%87%AA%E5%A2%9E%E4%B8%BB%E9%94%AE%E9%97%AE%E9%A2%98)
- [2.](#2)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

### 1.MySQL自增主键问题:
    * 一张表里有自增主键,当自增到 17后,删除了低15,16,17三条记录,再把mysql重启,在插入一条记录,该记录的ID是18还是15?
	1.1.AUTO_INCREMENT 列在 InnoDB 里如何工作:
		(1).如果为一个表指定 AUTO_INCREMENT 列,在数据词典里的InnoDB表句柄包含一个名为自动增长计数器的计数器,被用在为该列赋新值.
			自动增长计数器仅被存储在主内存中,而不是存在磁盘上.
		(2).InnoDB使用下列算法来为包含一个名为ai_col的AUTO_INCREMENT列的表T初始化自动增长计数器:
			服务器启动之后,当一个用户对表T做插入之时,InnoDB执行等价如下语句的动作:
			SELECT MAX(ai_col) FROM T FOR UPDATE;
	1.2.如果 mysql 服务重启, 因为 自动增长计数器仅被存储在主内存中,所以每次重启mysql都会重置.
		解决方法:
			(1).先不重启mysql,继续插入表一行记录,这行记录的id为 18,
			(2).重启mysql,插入表一行记录,这行记录的id为 19,

### 2.




