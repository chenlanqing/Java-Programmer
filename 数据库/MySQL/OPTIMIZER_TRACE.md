*本文基于MySQL 8.0编写，理论支持MySQL 5.6及更高版本。*

`OPTIMIZER_TRACE`是MySQL 5.6引入的一项跟踪功能，它可以跟踪优化器做出的各种决策（比如访问表的方法、各种开销计算、各种转换等），并将跟踪结果记录到 `INFORMATION_SCHEMA.OPTIMIZER_TRACE` 表中。此功能默认关闭，开启后，可分析如下语句：`SELECT、INSERT、REPLACE、UPDATE、DELETE、EXPLAIN、SET、DECLARE、CASE、IF、RETURN、CALL`；

## 1、OPTIMIZER_TRACE 参数

- [参考文档](https://dev.mysql.com/doc/internals/en/system-variables-controlling-trace.html)

- `optimizer_trace`
    - optimizer_trace总开关，默认值：`enabled=off,one_line=off`
    - enabled：是否开启`optimizer_trace`；on表示开启，off表示关闭。
    - one_line：是否开启单行存储。on表示开启；off表示关闭，将会用标准的JSON格式化存储。设置成on将会有良好的格式，设置成off可节省一些空间；

- `optimizer_trace_features`：控制optimizer_trace跟踪的内容，默认值：`greedy_search=on,range_optimizer=on,dynamic_range=on,repeated_subselect=on`，表示开启所有跟踪项；[详细参考](https://dev.mysql.com/doc/internals/en/optimizer-features-to-trace.html)
    - `greedy_search`：是否跟踪贪心搜索，有关[贪心算法](https://blog.csdn.net/weixin_42813521/article/details/105563103)
        - range_optimizer：是否跟踪范围优化器
    - `dynamic_range`：是否跟踪动态范围优化
        - `repeated_subselect`：是否跟踪子查询，如果设置成off，只跟踪第一条Item_subselect的执行；
- `optimizer_trace_limit`：控制optimizer_trace展示多少条结果，默认1
- `optimizer_trace_max_mem_size`：optimizer_trace堆栈信息允许的最大内存，默认1048576
- `optimizer_trace_offset`：第一个要展示的optimizer trace的偏移量，默认-1。
- `end_markers_in_json`：如果JSON结构很大，则很难将右括号和左括号配对。为了帮助读者阅读，可将其设置成on，这样会在右括号附近加上注释，默认off


以上参数可用SET语句操作，例如，用如下命令即可打开OPTIMIZER TRACE
```sql
SET OPTIMIZER_TRACE="enabled=on",END_MARKERS_IN_JSON=on;
```
也可用SET GLOBAL全局开启。但即使全局开启OPTIMIZER_TRACE，每个Session也只能跟踪它自己执行的语句：
```sql
SET GLOBAL OPTIMIZER_TRACE="enabled=on",END_MARKERS_IN_JSON=on;
```
optimizer_trace_limit和optimizer_trace_offset这两个参数经常配合使用，例如：
```sql
SET optimizer_trace_offset=<OFFSET>, optimizer_trace_limit=<LIMIT>
```
默认情况下，由于`optimizer_trace_offset=-1，optimizer_trace_limit=1`，记录最近的一条SQL语句，展示时，每次展示1条数据；如果改成 SET optimizer_trace_offset=-2, optimizer_trace_limit=1 ，则会记录倒数第二条SQL语句；

## 2、OPTIMIZER_TRACE使用

开启OPTIMIZER_TRACE功能，并设置要展示的数据条目数：
```
SET OPTIMIZER_TRACE="enabled=on",END_MARKERS_IN_JSON=on;
SET optimizer_trace_offset=-30, optimizer_trace_limit=30;
```

发送你想要分析的SQL语句，例如：
```sql
select * from salarieswhere from_date = '1986-06-26' and to_date = '1987-06-26';
```

使用如下语句分析，即可获得类似如下的结果：
```json
mysql> SELECT * FROM INFORMATION_SCHEMA.OPTIMIZER_TRACE limit 30 \G;
*************************** 1. row ***************************
    QUERY: select * from salarieswhere from_date = '1986-06-26' and to_date = '1987-06-26';
    TRACE: {
  "steps": [
    {
      "join_preparation": {
        "select#": 1,
        "steps": [
          {
            "expanded_query": "/* select#1 */ select `salaries`.`emp_no` AS `emp_no`,`salaries`.`salary` AS `salary`,`salaries`.`from_date` AS `from_date`,`salaries`.`to_date` AS `to_date` from `salaries` where ((`salaries`.`from_date` = '1986-06-26') and (`salaries`.`to_date` = '1987-06-26'))"
          }
        ] /* steps */
      } /* join_preparation */
    },
    {
      "join_optimization": {
        "select#": 1,
        "steps": [
          {
            "condition_processing": {
              "condition": "WHERE",
              "original_condition": "((`salaries`.`from_date` = '1986-06-26') and (`salaries`.`to_date` = '1987-06-26'))",
              "steps": [
                {
                  "transformation": "equality_propagation",
                  "resulting_condition": "(multiple equal('1986-06-26', `salaries`.`from_date`) and multiple equal('1987-06-26', `salaries`.`to_date`))"
                },
                {
                  "transformation": "constant_propagation",
                  "resulting_condition": "(multiple equal('1986-06-26', `salaries`.`from_date`) and multiple equal('1987-06-26', `salaries`.`to_date`))"
                },
                {
                  "transformation": "trivial_condition_removal",
                  "resulting_condition": "(multiple equal(DATE'1986-06-26', `salaries`.`from_date`) and multiple equal(DATE'1987-06-26', `salaries`.`to_date`))"
                }
              ] /* steps */
            } /* condition_processing */
          },
          {
            "substitute_generated_columns": {
            } /* substitute_generated_columns */
          },
          {
            "table_dependencies": [
              {
                "table": "`salaries`",
                "row_may_be_null": false,
                "map_bit": 0,
                "depends_on_map_bits": [
                ] /* depends_on_map_bits */
              }
            ] /* table_dependencies */
          },
          {
            "ref_optimizer_key_uses": [
              {
                "table": "`salaries`",
                "field": "from_date",
                "equals": "DATE'1986-06-26'",
                "null_rejecting": false
              },
              {
                "table": "`salaries`",
                "field": "to_date",
                "equals": "DATE'1987-06-26'",
                "null_rejecting": false
              }
            ] /* ref_optimizer_key_uses */
          },
          {
            "rows_estimation": [
              {
                "table": "`salaries`",
                "range_analysis": {
                  "table_scan": {
                    "rows": 2838216,
                    "cost": 286799
                  } /* table_scan */,
                  "potential_range_indexes": [
                    {
                      "index": "PRIMARY",
                      "usable": false,
                      "cause": "not_applicable"
                    },
                    {
                      "index": "salaries_from_date_to_date_index",
                      "usable": true,
                      "key_parts": [
                        "from_date",
                        "to_date",
                        "emp_no"
                      ] /* key_parts */
                    }
                  ] /* potential_range_indexes */,
                  "setup_range_conditions": [
                  ] /* setup_range_conditions */,
                  "group_index_range": {
                    "chosen": false,
                    "cause": "not_group_by_or_distinct"
                  } /* group_index_range */,
                  "skip_scan_range": {
                    "potential_skip_scan_indexes": [
                      {
                        "index": "salaries_from_date_to_date_index",
                        "usable": false,
                        "cause": "query_references_nonkey_column"
                      }
                    ] /* potential_skip_scan_indexes */
                  } /* skip_scan_range */,
                  "analyzing_range_alternatives": {
                    "range_scan_alternatives": [
                      {
                        "index": "salaries_from_date_to_date_index",
                        "ranges": [
                          "0xda840f <= from_date <= 0xda840f AND 0xda860f <= to_date <= 0xda860f"
                        ] /* ranges */,
                        "index_dives_for_eq_ranges": true,
                        "rowid_ordered": true,
                        "using_mrr": false,
                        "index_only": false,
                        "rows": 86,
                        "cost": 50.909,
                        "chosen": true
                      }
                    ] /* range_scan_alternatives */,
                    "analyzing_roworder_intersect": {
                      "usable": false,
                      "cause": "too_few_roworder_scans"
                    } /* analyzing_roworder_intersect */
                  } /* analyzing_range_alternatives */,
                  "chosen_range_access_summary": {
                    "range_access_plan": {
                      "type": "range_scan",
                      "index": "salaries_from_date_to_date_index",
                      "rows": 86,
                      "ranges": [
                        "0xda840f <= from_date <= 0xda840f AND 0xda860f <= to_date <= 0xda860f"
                      ] /* ranges */
                    } /* range_access_plan */,
                    "rows_for_plan": 86,
                    "cost_for_plan": 50.909,
                    "chosen": true
                  } /* chosen_range_access_summary */
                } /* range_analysis */
              }
            ] /* rows_estimation */
          },
          {
            "considered_execution_plans": [
              {
                "plan_prefix": [
                ] /* plan_prefix */,
                "table": "`salaries`",
                "best_access_path": {
                  "considered_access_paths": [
                    {
                      "access_type": "ref",
                      "index": "salaries_from_date_to_date_index",
                      "rows": 86,
                      "cost": 50.412,
                      "chosen": true
                    },
                    {
                      "access_type": "range",
                      "range_details": {
                        "used_index": "salaries_from_date_to_date_index"
                      } /* range_details */,
                      "chosen": false,
                      "cause": "heuristic_index_cheaper"
                    }
                  ] /* considered_access_paths */
                } /* best_access_path */,
                "condition_filtering_pct": 100,
                "rows_for_plan": 86,
                "cost_for_plan": 50.412,
                "chosen": true
              }
            ] /* considered_execution_plans */
          },
          {
            "attaching_conditions_to_tables": {
              "original_condition": "((`salaries`.`to_date` = DATE'1987-06-26') and (`salaries`.`from_date` = DATE'1986-06-26'))",
              "attached_conditions_computation": [
              ] /* attached_conditions_computation */,
              "attached_conditions_summary": [
                {
                  "table": "`salaries`",
                  "attached": "((`salaries`.`to_date` = DATE'1987-06-26') and (`salaries`.`from_date` = DATE'1986-06-26'))"
                }
              ] /* attached_conditions_summary */
            } /* attaching_conditions_to_tables */
          },
          {
            "finalizing_table_conditions": [
              {
                "table": "`salaries`",
                "original_table_condition": "((`salaries`.`to_date` = DATE'1987-06-26') and (`salaries`.`from_date` = DATE'1986-06-26'))",
                "final_table_condition   ": null
              }
            ] /* finalizing_table_conditions */
          },
          {
            "refine_plan": [
              {
                "table": "`salaries`"
              }
            ] /* refine_plan */
          }
        ] /* steps */
      } /* join_optimization */
    },
    {
      "join_execution": {
        "select#": 1,
        "steps": [
        ] /* steps */
      } /* join_execution */
    }
  ] /* steps */
}
MISSING_BYTES_BEYOND_MAX_MEM_SIZE: 0
          INSUFFICIENT_PRIVILEGES: 0
1 row in set (0.00 sec)
```

分析完成，关闭OPTIMIZER_TRACE
```sql
SET optimizer_trace="enabled=off";
```

## 3、OPTIMIZER_TRACE结果分析

- [OPTIMIZER_TRACE Result](https://dev.mysql.com/doc/refman/8.0/en/optimizer-trace-table.html)

OPTIMIZER_TRACE有四个字段：
- QUERY：查询语句
- TRACE：QUERY字段对应语句的跟踪信息
- MISSING_BYTES_BEYOND_MAX_MEM_SIZE：跟踪信息过长时，被截断的跟踪信息的字节数。
- INSUFFICIENT_PRIVILEGES：执行跟踪语句的用户是否有查看对象的权限。当不具有权限时，该列信息为1且TRACE字段为空，一般在调用带有SQL SECURITY DEFINER的视图或者是存储过程的情况下，会出现此问题

最核心的是 TREAC 字段的内容；

### 3.1、join_preparation

join_preparation段落展示了准备阶段的执行过程。
```json
{
  "join_preparation": {
    "select#": 1,
    "steps": [
      {
        -- 对比下原始语句，可以知道，这一步做了个格式化。
        "expanded_query": "/* select#1 */ select `salaries`.`emp_no` AS `emp_no`,`salaries`.`salary` AS `salary`,`salaries`.`from_date` AS `from_date`,`salaries`.`to_date` AS `to_date` from `salaries` where ((`salaries`.`from_date` = '1986-06-26') and (`salaries`.`to_date` = '1987-06-26'))"
      }
    ]
    /* steps */
  }
  /* join_preparation */
}
```

### 3.2、join_optimization

join_optimization展示了优化阶段的执行过程，是分析OPTIMIZER TRACE的重点

#### 3.2.1、condition_processing

该段用来做条件处理，主要对WHERE条件进行优化处理
```json
{
    "condition_processing": {
        "condition": "WHERE", // 优化对象类型。WHERE条件句或者是HAVING条件句
        "original_condition": "((`salaries`.`from_date` = '1986-06-26') and (`salaries`.`to_date` = '1987-06-26'))", // 优化前的原始语句
        "steps": [// 主要包括三步，分别是quality_propagation（等值条件句转换），constant_propagation（常量条件句转换），trivial_condition_removal（无效条件移除的转换）
        {
            "transformation": "equality_propagation",
            "resulting_condition": "(multiple equal('1986-06-26', `salaries`.`from_date`) and multiple equal('1987-06-26', `salaries`.`to_date`))"
        },
        {
            "transformation": "constant_propagation",
            "resulting_condition": "(multiple equal('1986-06-26', `salaries`.`from_date`) and multiple equal('1987-06-26', `salaries`.`to_date`))"
        },
        {
            "transformation": "trivial_condition_removal",
            "resulting_condition": "(multiple equal(DATE'1986-06-26', `salaries`.`from_date`) and multiple equal(DATE'1987-06-26', `salaries`.`to_date`))"
        }
        ] /* steps */
    } /* condition_processing */
}
```

#### 3.2、substitute_generated_columns

```json
{
    "substitute_generated_columns": {
    } /* substitute_generated_columns */
}
```

#### 3.3、table_dependencies

分析表之间的依赖关系
```json
{
  "table_dependencies": [
    {
      "table": "`salaries`", // 涉及的表名，如果有别名，也会展示出来
      "row_may_be_null": false, // 行是否可能为NULL，这里是指JOIN操作之后，这张表里的数据是不是可能为NULL。如果语句中使用了LEFT JOIN，则后一张表的row_may_be_null会显示为true
      "map_bit": 0, // 表的映射编号，从0开始递增
      "depends_on_map_bits": [ // 依赖的映射表。主要是当使用STRAIGHT_JOIN强行控制连接顺序或者LEFT JOIN/RIGHT JOIN有顺序差别时，会在depends_on_map_bits中展示前置表的map_bit值
      ] /* depends_on_map_bits */
    }
  ] /* table_dependencies */
}
```

#### 3.4、ref_optimizer_key_uses

列出所有可用的ref类型的索引。如果使用了组合索引的多个部分（例如本例，用到了index(from_date, to_date) 的多列索引），则会在ref_optimizer_key_uses下列出多个元素，每个元素中会列出ref使用的索引及对应值
```json
{
  "ref_optimizer_key_uses": [
    {
      "table": "`salaries`",
      "field": "from_date",
      "equals": "DATE'1986-06-26'",
      "null_rejecting": false
    },
    {
      "table": "`salaries`",
      "field": "to_date",
      "equals": "DATE'1987-06-26'",
      "null_rejecting": false
    }
  ] /* ref_optimizer_key_uses */
}
```

#### 3.5、rows_estimation

用于估算需要扫描的记录数
```json
{
  "rows_estimation": [
    {
      "table": "`salaries`", // 表名
      "range_analysis": {
        "table_scan": { // 如果全表扫描的话，需要扫描多少行（row，2838216），以及需要的代价（cost，286799）
          "rows": 2838216,
          "cost": 286799
        } /* table_scan */,
        "potential_range_indexes": [ // 列出表中所有的索引并分析其是否可用。如果不可用的话，会列出不可用的原因是什么；如果可用会列出索引中可用的字段；
          {
            "index": "PRIMARY",
            "usable": false,
            "cause": "not_applicable"
          },
          {
            "index": "salaries_from_date_to_date_index",
            "usable": true,
            "key_parts": [
              "from_date",
              "to_date",
              "emp_no"
            ] /* key_parts */
          }
        ] /* potential_range_indexes */,
        "setup_range_conditions": [// 如果有可下推的条件，则带条件考虑范围查询
        ] /* setup_range_conditions */,
        "group_index_range": {// 当使用了GROUP BY或DISTINCT时，是否有合适的索引可用。当未使用GROUP BY或DISTINCT时，会显示chosen=false, cause=not_group_by_or_distinct；如使用了GROUP BY或DISTINCT，但是多表查询时，会显示chosen=false，cause =not_single_table。其他情况下会尝试分析可用的索引（potential_group_range_indexes）并计算对应的扫描行数及其所需代价
          "chosen": false,
          "cause": "not_group_by_or_distinct"
        } /* group_index_range */,
        "skip_scan_range": { // 是否使用了skip scan
          "potential_skip_scan_indexes": [
            {
              "index": "salaries_from_date_to_date_index",
              "usable": false,
              "cause": "query_references_nonkey_column"
            }
          ] /* potential_skip_scan_indexes */
        } /* skip_scan_range */,
        "analyzing_range_alternatives": {// 分析各个索引的使用成本
          "range_scan_alternatives": [// range扫描分析
            {
              "index": "salaries_from_date_to_date_index", // 索引名
              "ranges": [// range扫描的条件范围
                "0xda840f <= from_date <= 0xda840f AND 0xda860f <= to_date <= 0xda860f"
              ] /* ranges */,
              "index_dives_for_eq_ranges": true, // 是否使用了index dive，该值会被参数eq_range_index_dive_limit变量值影响。
              "rowid_ordered": true, // 该range扫描的结果集是否根据PK值进行排序
              "using_mrr": false, // 是否使用了mrr
              "index_only": false, // 表示是否使用了覆盖索引
              "rows": 86, // 扫描的行数
              "cost": 50.909, // 索引的使用成本
              "chosen": true // 表示是否使用了该索引
            }
          ] /* range_scan_alternatives */,
          "analyzing_roworder_intersect": { // 分析是否使用了索引合并（index merge），如果未使用，会在cause中展示原因；如果使用了索引合并，会在该部分展示索引合并的代价。
            "usable": false,
            "cause": "too_few_roworder_scans"
          } /* analyzing_roworder_intersect */
        } /* analyzing_range_alternatives */,
        "chosen_range_access_summary": {// 在前一个步骤中分析了各类索引使用的方法及代价，得出了一定的中间结果之后，在summary阶段汇总前一阶段的中间结果确认最后的方案
          "range_access_plan": { // range扫描最终选择的执行计划
            "type": "range_scan", // 展示执行计划的type，如果使用了索引合并，则会显示index_roworder_intersect
            "index": "salaries_from_date_to_date_index", // 索引名
            "rows": 86, // 扫描的行数
            "ranges": [ // range扫描的条件范围
              "0xda840f <= from_date <= 0xda840f AND 0xda860f <= to_date <= 0xda860f"
            ] /* ranges */
          } /* range_access_plan */,
          "rows_for_plan": 86, // 该执行计划的扫描行数
          "cost_for_plan": 50.909, // 该执行计划的执行代价
          "chosen": true // 是否选择该执行计划
        } /* chosen_range_access_summary */
      } /* range_analysis */
    }
  ] /* rows_estimation */
}
```

#### 3.4、considered_execution_plans

负责对比各可行计划的开销，并选择相对最优的执行计划。
```json
{
  "considered_execution_plans": [
    {
      "plan_prefix": [ // 当前计划的前置执行计划。
      ] /* plan_prefix */,
      "table": "`salaries`", // 涉及的表名，如果有别名，也会展示出来
      "best_access_path": { // 通过对比considered_access_paths，选择一个最优的访问路径
        "considered_access_paths": [ // 当前考虑的访问路径
          {
            "access_type": "ref", // 使用索引的方式，可参考explain中的type字段
            "index": "salaries_from_date_to_date_index", // 索引
            "rows": 86, // 行数
            "cost": 50.412, // 开销
            "chosen": true // 是否选用这种执行路径
          },
          {
            "access_type": "range",
            "range_details": {
              "used_index": "salaries_from_date_to_date_index"
            } /* range_details */,
            "chosen": false,
            "cause": "heuristic_index_cheaper"
          }
        ] /* considered_access_paths */
      } /* best_access_path */,
      "condition_filtering_pct": 100, // 类似于explain的filtered列，是一个估算值
      "rows_for_plan": 86, // 执行计划最终的扫描行数，由considered_access_paths.rows X condition_filtering_pct计算获得。
      "cost_for_plan": 50.412, // 执行计划的代价，由considered_access_paths.cost相加获得
      "chosen": true // 是否选择了该执行计划
    }
  ] /* considered_execution_plans */
}
```

#### 3.5、attaching_conditions_to_tables

基于considered_execution_plans中选择的执行计划，改造原有where条件，并针对表增加适当的附加条件，以便于单表数据的筛选
```json
{
  "attaching_conditions_to_tables": {
    "original_condition": "((`salaries`.`to_date` = DATE'1987-06-26') and (`salaries`.`from_date` = DATE'1986-06-26'))",// 原始的条件语句
    "attached_conditions_computation": [ // 使用启发式算法计算已使用的索引，如果已使用的索引的访问类型是ref，则计算用range能否使用组合索引中更多的列，如果可以，则用range的方式替换ref
    ] /* attached_conditions_computation */,
    "attached_conditions_summary": [ // 附加之后的情况汇总
      {
        "table": "`salaries`",
        "attached": "((`salaries`.`to_date` = DATE'1987-06-26') and (`salaries`.`from_date` = DATE'1986-06-26'))"// 附加的条件或原语句中能直接下推给单表筛选的条件
      }
    ] /* attached_conditions_summary */
  } /* attaching_conditions_to_tables */
}
```

#### 3.6、finalizing_table_conditions

最终的、经过优化后的表条件
```json
{
  "finalizing_table_conditions": [
    {
      "table": "`salaries`",
      "original_table_condition": "((`salaries`.`to_date` = DATE'1987-06-26') and (`salaries`.`from_date` = DATE'1986-06-26'))",
      "final_table_condition   ": null
    }
  ] /* finalizing_table_conditions */
}
```

#### 3.7、refine_plan

改善执行计划：
```json
table：表名及别名
```