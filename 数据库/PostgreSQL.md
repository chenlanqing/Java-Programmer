用于Gis + 地图场景


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

- [PostgreSQL](https://www.postgresql.org/)
- [PostgreSQL作为搜索引擎](https://xata.io/blog/postgres-full-text-search-engine)
