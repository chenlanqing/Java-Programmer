<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
**目录**

- [一、查找命令](#%E4%B8%80%E6%9F%A5%E6%89%BE%E5%91%BD%E4%BB%A4)
- [二、系统监控](#%E4%BA%8C%E7%B3%BB%E7%BB%9F%E7%9B%91%E6%8E%A7)
  - [1、top](#1top)
- [三、其他](#%E4%B8%89%E5%85%B6%E4%BB%96)
  - [1、查看Linux发行版本](#1%E6%9F%A5%E7%9C%8Blinux%E5%8F%91%E8%A1%8C%E7%89%88%E6%9C%AC)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->



# 一、查找命令

# 二、系统监控

## 1、top

top命令是Linux下常用的CPU性能分析工具，能够实时显示系统中各个进程的资源占用状况，常用于服务端性能分析

### 1.1、格式

top [参数]

### 1.2、命令参数
- -b 批处理
- -c 显示完整的治命令
- -I 忽略失效过程
- -s 保密模式
- -S 累积模式
- -i<时间> 设置间隔时间
- -u<用户名> 指定用户名
- -p<进程号> 指定进程
- -n<次数> 循环显示的次数

### 1.3、实例
![image](https://github.com/chenlanqing/learningNote/blob/master/Java/线上问题排查/image/top.jpg)

- **说明**

    统计信息区：前五行是当前系统情况整体的统计信息区。下面我们看每一行信息的具体意义

    - 第一行，任务队列信息，同 uptime 命令的执行结果，具体参数说明情况如下：

        - 14:06:23 — 当前系统时间
        - up 70 days, 16:44 — 系统已经运行了70天16小时44分钟（在这期间系统没有重启过的吆！）
        - 2 users — 当前有2个用户登录系统
        - load average: 1.15, 1.42, 1.44 — load average后面的三个数分别是1分钟、5分钟、15分钟的负载情况。
        - load average数据是每隔5秒钟检查一次活跃的进程数，然后按特定算法计算出的数值。如果这个数除以逻辑CPU的数量，结果高于5的时候就表明系统在超负荷运转了

    - 第二行，Tasks — 任务（进程），具体信息说明如下：

        系统现在共有206个进程，其中处于运行中的有1个，205个在休眠（sleep），stoped状态的有0个，zombie状态（僵尸）的有0个。

    - 第三行，cpu状态信息，具体属性说明如下：

        - 5.9%us — 用户空间占用CPU的百分比。
        - 3.4% sy — 内核空间占用CPU的百分比。
        - 0.0% ni — 改变过优先级的进程占用CPU的百分比
        - 90.4% id — 空闲CPU百分比
        - 0.0% wa — IO等待占用CPU的百分比
        - 0.0% hi — 硬中断（Hardware IRQ）占用CPU的百分比
        - 0.2% si — 软中断（Software Interrupts）占用CPU的百分比

        *备注：在这里CPU的使用比率和windows概念不同，需要理解linux系统用户空间和内核空间的相关知识！*

    - 第四行,内存状态，具体信息如下：

        - 32949016k total — 物理内存总量（32GB）
        - 14411180k used — 使用中的内存总量（14GB）
        - 18537836k free — 空闲内存总量（18GB）
        - 169884k buffers — 缓存的内存量 （169M）   

    - 第五行，swap交换分区信息，具体信息说明如下：

        - 32764556k total — 交换区总量（32GB）
        - 0k used — 使用的交换区总量（0K）
        - 32764556k free — 空闲交换区总量（32GB）
        - 3612636k cached — 缓冲的交换区总量（3.6GB）

    - 第六行，空行。
    
    - 第七行以下：各进程（任务）的状态监控，项目列信息说明如下：
        - PID : 进程 id
        - USER : 进程所有者
        - PR : 进程优先级
        - NI : nice 值。负值表示高优先级，正值表示低优先级
        - VIRT : 进程使用的虚拟内存总量，单位 kb。VIRT=SWAP+RES
        - RES : 进程使用的、未被换出的物理内存大小，单位 kb。RES=CODE+DATA
        - SHR : 共享内存大小，单位 kb
        - S : 进程状态。D= 不可中断的睡眠状态 R= 运行 S= 睡眠 T= 跟踪 / 停止 Z= 僵尸进程
        - %CPU : 上次更新到现在的 CPU 时间占用百分比
        - %MEM : 进程使用的物理内存百分比
        - TIME+ : 进程使用的 CPU 时间总计，单位 1/100 秒
        - COMMAND : 进程名称




# 三、其他
## 1、查看Linux发行版本

cat /etc/redhat-release

