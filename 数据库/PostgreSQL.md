# 一、基本介绍

PostgreSQL 是一种先进的企业级开源关系型数据库系统。PostgreSQL 支持 SQL（关系型）和 JSON（非关系型）查询。

PostgreSQL 是许多网络应用程序以及移动和分析应用程序的主要数据库。

## 1、特点

- 用户自定义类型
- 表继承
- 先进的锁定机制
- 外键参照完整性
- 视图、规则、子查询
- 嵌套事务（保存点）
- 多版本并发控制（MVCC）
- 异步复制

适用场景：
- 用于Gis + 地图场景
- 普通事务数据库；


# 安装

## Docker安装

- [Install postgresql with docker](https://www.baeldung.com/ops/postgresql-docker-setup)

```bash
$ docker pull postgres
$ docker run -itd -e POSTGRES_USER=root -e POSTGRES_PASSWORD=root -p 5432:5432 --name postgresql postgres
# pgAdmin
$ docker pull dpage/pgadmin4:latest
$ docker run --name pgadmin -p 5051:80 -e "PGADMIN_DEFAULT_EMAIL=743633145@qq.com" -e "PGADMIN_DEFAULT_PASSWORD=root" -d dpage/pgadmin4
```

# 参考资料

- [PostgreSQL官方文档](https://www.postgresql.org/docs/current/tutorial.html)
- [Postgresql Tutorial](https://www.postgresqltutorial.com/)
- [PostgreSQL作为搜索引擎](https://xata.io/blog/postgres-full-text-search-engine)
