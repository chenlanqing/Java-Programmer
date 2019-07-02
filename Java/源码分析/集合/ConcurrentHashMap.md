<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
**目录**

- [一、ConcurrentHashMap概述](#%E4%B8%80concurrenthashmap%E6%A6%82%E8%BF%B0)
  - [1、为什么会出现ConcurrentHashMap](#1%E4%B8%BA%E4%BB%80%E4%B9%88%E4%BC%9A%E5%87%BA%E7%8E%B0concurrenthashmap)
  - [2、ConcurrentHashMap 不同版本演进](#2concurrenthashmap-%E4%B8%8D%E5%90%8C%E7%89%88%E6%9C%AC%E6%BC%94%E8%BF%9B)
    - [2.1、JDK6](#21jdk6)
    - [2.2、JDK1.7版本](#22jdk17%E7%89%88%E6%9C%AC)
    - [2.3、JDK8版本](#23jdk8%E7%89%88%E6%9C%AC)
  - [3、分段锁形式如何保证size的一致性](#3%E5%88%86%E6%AE%B5%E9%94%81%E5%BD%A2%E5%BC%8F%E5%A6%82%E4%BD%95%E4%BF%9D%E8%AF%81size%E7%9A%84%E4%B8%80%E8%87%B4%E6%80%A7)
- [二、JDK8的实现](#%E4%BA%8Cjdk8%E7%9A%84%E5%AE%9E%E7%8E%B0)
  - [1、基本变量](#1%E5%9F%BA%E6%9C%AC%E5%8F%98%E9%87%8F)
  - [2、内部类](#2%E5%86%85%E9%83%A8%E7%B1%BB)
    - [2.1、Node](#21node)
    - [2.2、TreeNode](#22treenode)
    - [2.3、TreeBin](#23treebin)
  - [3、put方法](#3put%E6%96%B9%E6%B3%95)
  - [4、get方法](#4get%E6%96%B9%E6%B3%95)
  - [5、扩容](#5%E6%89%A9%E5%AE%B9)
  - [6、hash冲突解决](#6hash%E5%86%B2%E7%AA%81%E8%A7%A3%E5%86%B3)
- [面试](#%E9%9D%A2%E8%AF%95)
  - [1、size方法和mappingCount方法的异同，两者计算是否准确](#1size%E6%96%B9%E6%B3%95%E5%92%8Cmappingcount%E6%96%B9%E6%B3%95%E7%9A%84%E5%BC%82%E5%90%8C%E4%B8%A4%E8%80%85%E8%AE%A1%E7%AE%97%E6%98%AF%E5%90%A6%E5%87%86%E7%A1%AE)
  - [2、多线程环境下如何进行扩容](#2%E5%A4%9A%E7%BA%BF%E7%A8%8B%E7%8E%AF%E5%A2%83%E4%B8%8B%E5%A6%82%E4%BD%95%E8%BF%9B%E8%A1%8C%E6%89%A9%E5%AE%B9)
  - [3、HashMap、HashTable、ConcurrenHashMap区别](#3hashmaphashtableconcurrenhashmap%E5%8C%BA%E5%88%AB)
  - [4、](#4)
- [参考资料:](#%E5%8F%82%E8%80%83%E8%B5%84%E6%96%99)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->


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

在Jdk1.7中是采用Segment + HashEntry + ReentrantLock

ConcurrentHashMap 由一个个 Segment 组成，ConcurrentHashMap 是一个 Segment 数组，Segment 通过继承 ReentrantLock 来进行加锁，所以每次需要加锁的操作锁住的是一个 segment，这样只要保证每个 Segment 是线程安全的，也就实现了全局的线程安全；

ConcurrentHashMap初始化时，计算出Segment数组的大小ssize和每个Segment中HashEntry数组的大小cap，并初始化Segment数组的第一个元素；其中ssize大小为2的幂次方，默认为16，cap大小也是2的幂次方，最小值为2，最终结果根据根据初始化容量initialCapacity进行计算，计算过程如下


### 2.3、JDK8版本

1.8中放弃了Segment臃肿的设计，取而代之的是采用`Node + CAS + Synchronized`来保证并发安全进行实现；只有在执行第一次put方法时才会调用initTable()初始化Node数组；

底层依然采用"数组+链表+红黑树"的存储结构

- JDK1.8的实现降低锁的粒度，JDK1.7版本锁的粒度是基于Segment的，包含多个HashEntry，而JDK1.8锁的粒度就是HashEntry（首节点）；
- JDK1.8版本的数据结构变得更加简单，使得操作也更加清晰流畅，因为已经使用synchronized来进行同步，所以不需要分段锁的概念，也就不需要Segment这种数据结构了，由于粒度的降低，实现的复杂度也增加了
- JDK1.8使用红黑树来优化链表，基于长度很长的链表的遍历是一个很漫长的过程，而红黑树的遍历效率是很快的，代替一定阈值的链表，这样形成一个最佳拍档

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
    ```java
    transient volatile Node<K,V>[] table;
    private transient volatile Node<K,V>[] nextTable;
    private transient volatile long baseCount;
    private transient volatile int sizeCtl;
    ```
    - table：用来存放Node节点的数据，默认为null，默认大小为16的数组，每次扩容时大小总是2的幂次方；是volatile修饰的，为了使得Node数组在扩容的时候对其他线程具有可见性而加的volatile
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

## 3、put方法

## 4、get方法

- 首先计算hash值，定位到该table索引位置，如果是首节点符合就返回
- 如果遇到扩容的时候，会调用标志正在扩容节点ForwardingNode的find方法，查找该节点，匹配就返回
- 以上都不符合的话，就往下遍历节点，匹配就返回，否则最后就返回null
```java
//会发现源码中没有一处加了锁
public V get(Object key) {
    Node<K,V>[] tab; Node<K,V> e, p; int n, eh; K ek;
    int h = spread(key.hashCode()); //计算hash
    if ((tab = table) != null && (n = tab.length) > 0 &&
        (e = tabAt(tab, (n - 1) & h)) != null) {//读取首节点的Node元素
        if ((eh = e.hash) == h) { //如果该节点就是首节点就返回
            if ((ek = e.key) == key || (ek != null && key.equals(ek)))
                return e.val;
        }
        //hash值为负值表示正在扩容，这个时候查的是ForwardingNode的find方法来定位到nextTable来
        //eh=-1，说明该节点是一个ForwardingNode，正在迁移，此时调用ForwardingNode的find方法去nextTable里找。
        //eh=-2，说明该节点是一个TreeBin，此时调用TreeBin的find方法遍历红黑树，由于红黑树有可能正在旋转变色，所以find里会有读写锁。
        //eh>=0，说明该节点下挂的是一个链表，直接遍历该链表即可。
        else if (eh < 0)
            return (p = e.find(h, key)) != null ? p.val : null;
        while ((e = e.next) != null) {//既不是首节点也不是ForwardingNode，那就往下遍历
            if (e.hash == h &&
                ((ek = e.key) == key || (ek != null && key.equals(ek))))
                return e.val;
        }
    }
    return null;
}
```
**get没有加锁的话，ConcurrentHashMap是如何保证读到的数据不是脏数据的呢？**

get操作可以无锁是由于Node的元素val和指针next是用volatile修饰的，在多线程环境下线程A修改结点的val或者新增节点的时候是对线程B可见的。

既然volatile修饰数组对get操作没有效果那加在数组上的volatile的目的是：为了使得Node数组在扩容的时候对其他线程具有可见性而加的volatile

## 5、扩容

## 6、hash冲突解决

# 面试

## 1、size方法和mappingCount方法的异同，两者计算是否准确

## 2、多线程环境下如何进行扩容

## 3、HashMap、HashTable、ConcurrenHashMap区别

## 4、

# 参考资料:

* [ConcurrentHashMap高并发性的实现机制](http://www.importnew.com/16147.html)
* [ConcurrentHashMap面试必问](https://mp.weixin.qq.com/s/HUvHUBRqp4I4ShyUJr5xDw)
* [ConcurrentHashMap进阶之红黑树实现](https://mp.weixin.qq.com/s/8XLqCwWQimAIr__S_BfrHA)
* [ConcurrentHashMap进阶之扩容实现](https://mp.weixin.qq.com/s/8MCq-i0AMqaJRQIecJl2WA)
* [Jdk7与JDK8的区别](https://www.jianshu.com/p/e694f1e868ec)