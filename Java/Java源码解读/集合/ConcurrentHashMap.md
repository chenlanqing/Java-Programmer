
基于如下来分析
- ConcurrentHashMap 的锁分段技术；
- ConcurrentHashMap 的读是否要加锁，为什么？
- ConcurrentHashMap 的迭代器是强一致性的迭代器还是弱一致性的迭代器；

# 一、ConcurrentHashMap概述

## 1、为什么会出现ConcurrentHashMap

HashMap是用得非常频繁的一个集合，但是由于它是非线程安全的，在多线程环境下，put操作是有可能产生死循环的，导致CPU利用率接近100%。

为了解决该问题，提供了`Hashtable`和`Collections.synchronizedMap(hashMap)`两种解决方案，但是这两种方案都是对读写加锁，独占式，一个线程在读时其他线程必须等待，吞吐量较低，性能较为低下

## 2、ConcurrentHashMap 不同版本演进

### 2.1、JDK6

- 1、ConcurrentHashMap 的锁分段技术:

    首先将数据分成一段一段的存储,然后给每一段数据配一把锁,当一个线程占用锁访问其中一个段数据的时候,其他段的数据也能被其他线程访问.
    采用分段锁的机制,实现并发的更新操作,底层采用"数组+链表+红黑树"的存储结构,其包含两个核心静态内部类 Segment 和 HashEntry.

- 2、ConcurrentHashMap 是由 Segment 数组结构和 HashEntry 数组结构组成

    Segment 是一种可重入锁 ReentrantLock，在 ConcurrentHashMap 里扮演锁的角色，HashEntry 则用于存储键值对数据.
    一个 ConcurrentHashMap 里包含一个 Segment 数组，Segment 的结构和 HashMap 类似,是一种数组和链表结构,
    一个 Segment 里包含一个 HashEntry 数组，每个 HashEntry 是一个链表结构的元素,每个 Segment 守护着一个 
    HashEntry 数组里的元素,当对 HashEntry 数组的数据进行修改时，必须首先获得它对应的 Segment 锁

    - 2.1.HashEntry:
        用来封装散列映射表中的键值对.在 HashEntry 类中,key、hash和next 域都被声明为 final 型,value 域被声明为 volatile 型;
    - 2.2.Segment:继承于 ReentrantLock 类
        - (1).table 是一个由 HashEntry 对象组成的数组。table 数组的每一个数组成员就是散列映射表的一个桶
        - (2).count 变量是一个计数器，它表示每个 Segment 对象管理的 table 数组(若干个 HashEntry 组成的链表)
            包含的 HashEntry 对象的个数;

- 3、ConcurrentHashMap 类:在默认并发级别会创建包含 16 个 Segment 对象的数组

    (1).每个 Segment 的成员对象 table 包含若干个散列表的桶。每个桶是由 HashEntry 链接起来的一个链表。
        如果键能均匀散列，每个 Segment 大约守护整个散列表中桶总数的 1/16

- 4、ConcurrentHashMap 的初始化

    (1).ConcurrentHashMap 初始化方法是通过 initialCapacity，loadFactor, concurrencyLevel几个参数来初始化segments数组，
        段偏移量segmentShift，段掩码segmentMask和每个segment里的HashEntry数组

- 5、ConcurrentHashMap 实现高并发

    (1).ConcurrentHashMap 基于散列表的特性(除了少数插入操作和删除操作外,绝大多数都是读取操作,而且读操作在大多
        数时候都是成功的)针对读操作做了大量的优化;通过 HashEntry 对象的不变性和用 volatile 型变量协调线程间的内存可见性,
        使得大多数时候,读操作不需要加锁就可以正确获得值;
        ConcurrentHashMap 是一个并发散列映射表的实现，它允许完全并发的读取,并且支持给定数量的并发更新

    (2).用分离锁实现多个线程间的更深层次的共享访问,用 HashEntery 对象的不变性来降低执行读操作的线程在遍历链表期间对加锁的需求
        通过对同一个 Volatile 变量的写 / 读访问，协调不同线程间读 / 写操作的内存可见性

    (3).ConcurrentHashMap的并发度跟 segment 的大小有关.

### 2.2、JDK1.7版本

ConcurrentHashMap 由一个个 Segment 组成，ConcurrentHashMap 是一个 Segment 数组，Segment 通过继承 ReentrantLock 来进行加锁，所以每次需要加锁的操作锁住的是一个 segment，这样只要保证每个 Segment 是线程安全的，也就实现了全局的线程安全；

ConcurrentHashMap初始化时，计算出Segment数组的大小ssize和每个Segment中HashEntry数组的大小cap，并初始化Segment数组的第一个元素；其中ssize大小为2的幂次方，默认为16，cap大小也是2的幂次方，最小值为2，最终结果根据根据初始化容量initialCapacity进行计算，计算过程如下


### 2.3、JDK8版本

1.8中放弃了Segment臃肿的设计，取而代之的是采用`Node + CAS + Synchronized`来保证并发安全进行实现；只有在执行第一次put方法时才会调用initTable()初始化Node数组；

底层依然采用"数组+链表+红黑树"的存储结构

##  3、分段锁形式如何保证size的一致性

LongAdder 是一种JVM利用空间换取更高的效率

# 二、JDK8的实现

```java
public class ConcurrentHashMap<K,V> extends AbstractMap<K,V> implements ConcurrentMap<K,V>, Serializable {}
```

## 1、基本变量

- **常量**
    ```java
    // 链表转红黑树阀值,> 8 链表转换为红黑树
    static final int TREEIFY_THRESHOLD = 8;
    //树转链表阀值，小于等于6（tranfer时，lc、hc=0两个计数器分别++记录原bin、新binTreeNode数量，<=UNTREEIFY_THRESHOLD 则untreeify(lo)）
    static final int UNTREEIFY_THRESHOLD = 6;
    // 2^15-1，help resize的最大线程数
    private static final int MAX_RESIZERS = (1 << (32 - RESIZE_STAMP_BITS)) - 1;
    // 32-16=16，sizeCtl中记录size大小的偏移量
    private static final int RESIZE_STAMP_SHIFT = 32 - RESIZE_STAMP_BITS;
    // 树根节点的hash值
    static final int TREEBIN   = -2;
    // ReservationNode的hash值
    static final int RESERVED  = -3;
    // 可用处理器数量
    static final int NCPU = Runtime.getRuntime().availableProcessors();
    ```
- **几个重要的变量**
    - table：用来存放Node节点的数据，默认为null，默认大小为16的数组，每次扩容时大小总是2的幂次方；
    - nextTable：扩容时新生成的数据，数组为table的两倍；
    - Node：节点，保存key-value的数据结构；
    - ForwardingNode：一个特殊的Node节点，hash值为-1，其中存储nextTable的引用。只有当table发生扩容时，ForwardingNode才会发挥作用，作为一个占位符放在table中表示节点为null或者已经被移动；
    - sizeCtl：控制标识符，用来控制table初始化和扩容操作：
        - 负数代表正在初始化或者扩容操作；
        - `-1`代表正在初始化；
        - `-N` 标识有`N-1`个线程正在进行扩容操作；
        - 正数或0标识hash表还没有被初始化，这个值表示初始化或者下一次进行扩容的大小

## 2、内部类

### 2.1、Node

### 2.2、TreeNode

### 2.3、TreeBin

# 参考资料:

* [ConcurrentHashMap高并发性的实现机制](http://www.importnew.com/16147.html)
* [ConcurrentHashMap面试必问](https://mp.weixin.qq.com/s/HUvHUBRqp4I4ShyUJr5xDw)
* [ConcurrentHashMap进阶之红黑树实现](https://mp.weixin.qq.com/s/8XLqCwWQimAIr__S_BfrHA)
* [ConcurrentHashMap进阶之扩容实现](https://mp.weixin.qq.com/s/8MCq-i0AMqaJRQIecJl2WA)
* [Jdk7与JDK8的区别](https://www.jianshu.com/p/e694f1e868ec)