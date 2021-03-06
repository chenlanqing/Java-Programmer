-- （1）创建ods层表表 ods_user_order 表，分区表
create external table if not exists ods_mall.ods_user_order(
   order_id             bigint,
   order_date           string,
   user_id              bigint,
   order_money          double,
   order_type           int,
   order_status         int,
   pay_id               bigint,
   update_time          string
)partitioned by(dt string) row format delimited fields terminated by '\t' location 'hdfs://bluefish:9000/data/ods/user_order/';
-- （2）将数据加载到hive中
alter table ods_mall.ods_user_order add if not exists partition(dt='20260301') location '20260301';
alter table ods_mall.ods_user_order add if not exists partition(dt='20260302') location '20260302';
alter table ods_mall.ods_user_order add if not exists partition(dt='20260303') location '20260303';

-- （3）创建dwd层表：dwd_user_order，是一张分区表
create external table if not exists dwd_mall.dwd_user_order(
   order_id             bigint,
   order_date           string,
   user_id              bigint,
   order_money          double,
   order_type           int,
   order_status         int,
   pay_id               bigint,
   update_time          string
)partitioned by(dt string) row format delimited fields terminated by '\t' location 'hdfs://bluefish:9000/data/dwd/user_order/';

-- （4）从ods层将数据处理到dwd层
hive (default)> insert overwrite table dwd_mall.dwd_user_order partition(dt='20260301')  select 
   order_id,
   order_date,
   user_id,
   order_money,
   order_type,
   order_status,
   pay_id,
   update_time
from ods_mall.ods_user_order
where dt = '20260301' and order_id is not null;
hive (default)> insert overwrite table dwd_mall.dwd_user_order partition(dt='20260302')  select 
   order_id,
   order_date,
   user_id,
   order_money,
   order_type,
   order_status,
   pay_id,
   update_time
from ods_mall.ods_user_order
where dt = '20260302' and order_id is not null;
hive (default)> insert overwrite table dwd_mall.dwd_user_order partition(dt='20260303')  select 
   order_id,
   order_date,
   user_id,
   order_money,
   order_type,
   order_status,
   pay_id,
   update_time
from ods_mall.ods_user_order
where dt = '20260303' and order_id is not null;

-- （5）创建拉链表，是全表数据
create external table if not exists dws_mall.dws_user_order_zip(
   order_id             bigint,
   order_date           string,
   user_id              bigint,
   order_money          double,
   order_type           int,
   order_status         int,
   pay_id               bigint,
   update_time          string,
   start_time          string,
   end_time          string
)row format delimited fields terminated by '\t' location 'hdfs://bluefish:9000/data/dws/user_order_zip/';

-- （6）向拉链表中初始好数据
insert overwrite table dws_mall.dws_user_order_zip
select * 
from
(
    -- 一般初始化时，这一部分是没有数据的，只有 union all 的后面语句才有数据，即当天的数据
   select 
      duoz.order_id,
      duoz.order_date,
      duoz.user_id,
      duoz.order_money,
      duoz.order_type,
      duoz.order_status,
      duoz.pay_id,
      duoz.update_time,
      duoz.start_time,
      case
          when duoz.end_time = '9999-12-31' and duo.order_id is not null then date_add('2026-03-01',-1)
          else duoz.end_time
      end as end_time
   from dws_mall.dws_user_order_zip as duoz
   left join 
   (
    select 
       order_id 
    from dwd_mall.dwd_user_order 
    where dt='20260301'
   )as duo
   on duoz.order_id = duo.order_id
union all
   select
      duo.order_id,
      duo.order_date,
      duo.user_id,
      duo.order_money,
      duo.order_type,
      duo.order_status,
      duo.pay_id,
      duo.update_time,
      '2026-03-01' as start_time,
      '9999-12-31' as end_time
   from dwd_mall.dwd_user_order as duo
   where duo.dt='20260301'
) as t;


insert overwrite table dws_mall.dws_user_order_zip
select * 
from
(
   select 
      duoz.order_id,
      duoz.order_date,
      duoz.user_id,
      duoz.order_money,
      duoz.order_type,
      duoz.order_status,
      duoz.pay_id,
      duoz.update_time,
      duoz.start_time,
      case
          when duoz.end_time = '9999-12-31' and duo.order_id is not null then date_add('2026-03-02',-1)
          else duoz.end_time
      end as end_time
   from dws_mall.dws_user_order_zip as duoz
   left join 
   (
    select 
       order_id 
    from dwd_mall.dwd_user_order 
    where dt='20260302'
   )as duo
   on duoz.order_id = duo.order_id
union all
   select
      duo.order_id,
      duo.order_date,
      duo.user_id,
      duo.order_money,
      duo.order_type,
      duo.order_status,
      duo.pay_id,
      duo.update_time,
      '2026-03-02' as start_time,
      '9999-12-31' as end_time
   from dwd_mall.dwd_user_order as duo
   where duo.dt='20260302'
) as t;