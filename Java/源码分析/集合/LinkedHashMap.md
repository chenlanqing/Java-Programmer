
## 1、概述

签名：
```java
public class LinkedHashMap<K,V> extends HashMap<K,V> implements Map<K,V>{

}
```
LinkedHashMap是HashMap的一个子类，它保留插入的顺序，如果需要输出的顺序和输入时的相同，那么就选用LinkedHashMap。

LinkedHashMap是Map接口的哈希表和链表实现，具有可预知的迭代顺序。此实现提供所有可选的映射操作，并允许使用null值和null键。此类不保证映射的顺序，特别是它不保证该顺序恒久不变。除了可以保迭代历顺序，这种结构还有一个好处 : 迭代LinkedHashMap时不需要像HashMap那样遍历整个table，而只需要直接遍历header指向的双向链表即可，也就是说LinkedHashMap的迭代时间就只跟entry的个数相关，而跟table的大小无关
   
LinkedHashMap实现与HashMap的不同之处在于，后者维护着一个运行于所有条目的双向链表。此链表定义了迭代顺序，该迭代顺序可以是插入顺序或者是访问顺序。注意，此实现不是同步的。如果多个线程同时访问链接的哈希映射，而其中至少一个线程从结构上修改了该映射，则它必须保持外部同步。

根据链表中元素的顺序可以分为：按插入顺序的链表，和按访问顺序(调用get方法)的链表。  

默认是按插入顺序排序，如果指定按访问顺序排序，那么调用get方法后，会将这次访问的元素移至链表尾部，不断访问可以形成按访问顺序排序的链表。可以重写removeEldestEntry方法返回true值指定插入元素时移除最老的元素；

对于LinkedHashMap而言，它继承于HashMap、底层使用哈希表与双向链表来保存所有元素。其基本操作与父类HashMap相似，它通过重写父类相关的方法，来实现自己的链表特性

## 2、LinkedHashMap链表结构

其在HashMap的基础上增加了如下属性：
```java
// 链表头
transient LinkedHashMap.Entry<K,V> head;
// 链表尾
transient LinkedHashMap.Entry<K,V> tail;
// 继承 Node，为数组的每个元素增加了 before 和 after 属性
static class Entry<K,V> extends HashMap.Node<K,V> {
    Entry<K,V> before, after;
    Entry(int hash, K key, V value, Node<K,V> next) {
        super(hash, key, value, next);
    }
}
// 控制两种访问模式的字段，默认false；true按照访问顺序，会把经常访问的 key 放到队尾；false按照插入顺序提供访问
final boolean accessOrder;
```

## 3、如何按照顺序新增

LinkeHashMap初始化时，默认 accessOrder 为false，是按照插入顺序提供访问
```java
public LinkedHashMap() {
    super();
    accessOrder = false;
}
```
插入方法是使用的是父类的put方法，不过重写了 put方法调用执行的 newNode\newTreeNode 和 afterNodeAccess 方法：
```java
// 新增节点，并追加到链表的尾部
Node<K,V> newNode(int hash, K key, V value, Node<K,V> e) {
    // 新增节点
    LinkedHashMap.Entry<K,V> p = new LinkedHashMap.Entry<K,V>(hash, key, value, e);
    // 追加到链表的尾部
    linkNodeLast(p);
    return p;
}
// link at the end of list
private void linkNodeLast(LinkedHashMap.Entry<K,V> p) {
    LinkedHashMap.Entry<K,V> last = tail;
    // 新增节点等于位节点
    tail = p;
    // last 为空，说明链表为空，首尾节点相等
    if (last == null)
        head = p;
    // 链表有数据，直接建立新增节点和上个尾节点之间的前后关系即可
    else {
        p.before = last;
        last.after = p;
    }
}
```
LinkedHashMap 通过新增头节点、尾节点，给每个节点增加 before、after 属性，每次新增时，都把节点追加到尾节点等手段，在新增的时候，就已经维护了按照插入顺序的链表结构了。

## 4、按照顺序访问

LinkedHashMap只提供单向访问，即按照插入的顺序从头到尾访问，不能像LinkedList那样可以双向访问；

访问主要是通过迭代器进行访问，迭代器初始化时，默认从头结点开始访问，在迭代过程中，不断访问当前节点的after节点即可；

Map 对 key、value 和 entity（节点） 都提供出了迭代的方法，假设我们需要迭代 entity，就可使用 `LinkedHashMap.entrySet().iterator()` 这种写法直接返回  LinkedHashIterator ，LinkedHashIterator 是迭代器，我们调用迭代器的 nextNode 方法就可以得到下一个节点，迭代器的源码如下：
```java
// 初始化时，默认从头节点开始访问
LinkedHashIterator() {
    // 头节点作为第一个访问的节点
    next = head;
    expectedModCount = modCount;
    current = null;
}

final LinkedHashMap.Entry<K,V> nextNode() {
    LinkedHashMap.Entry<K,V> e = next;
    if (modCount != expectedModCount)// 校验
        throw new ConcurrentModificationException();
    if (e == null)
        throw new NoSuchElementException();
    current = e;
    next = e.after; // 通过链表的 after 结构，找到下一个迭代的节点
    return e;
}
```

## 5、访问最少删除策略（LRU）

### 5.1、如何实现

重写 removeEldestEntry方法即可
```java
// 新建 LinkedHashMap
LinkedHashMap<Integer, Integer> map = new LinkedHashMap<Integer, Integer>(4,0.75f,true) {
    {
      put(10, 10);
      put(9, 9);
      put(20, 20);
      put(1, 1);
    }
    @Override
    // 覆写了删除策略的方法，我们设定当节点个数大于 3 时，就开始删除头节点
    protected boolean removeEldestEntry(Map.Entry<Integer, Integer> eldest) {
      return size() > 3;
    }
};
```
当我们调用 map.get(9) 方法时，元素 9 移动到队尾，调用 map.get(20) 方法时， 元素 20 被移动到队尾，这个体现了经常被访问的节点会被移动到队尾。

### 5.2、get时为什么元素会放到队尾

```java
public V get(Object key) {
    Node<K,V> e;
    // 调用 HashMap  get 方法
    if ((e = getNode(hash(key), key)) == null)
        return null;
    // 如果设置了 LRU 策略
    if (accessOrder)
    // 这个方法把当前 key 移动到队尾
        afterNodeAccess(e);
    return e.value;
}
```
从上述源码中，可以看到，如果 accessOrder 为true的话通过 afterNodeAccess 方法把当前访问节点移动到了队尾，其实不仅仅是 get 方法，执行 getOrDefault、compute、computeIfAbsent、computeIfPresent、merge 方法时，也会这么做，通过不断的把经常访问的节点移动到队尾，那么靠近队头的节点，自然就是很少被访问的元素了。

### 5.3、删除策略

在执行 put 方法时，发现队头元素被删除了，LinkedHashMap 本身是没有 put 方法实现的，调用的是 HashMap 的 put 方法，但 LinkedHashMap 实现了 put 方法中的调用 afterNodeInsertion 方法，这个方式实现了删除，我们看下源码：
```java
// 删除很少被访问的元素，被 HashMap 的 put 方法所调用
void afterNodeInsertion(boolean evict) { 
    // 得到元素头节点
    LinkedHashMap.Entry<K,V> first;
    // removeEldestEntry 来控制删除策略，如果队列不为空，并且删除策略允许删除的情况下，删除头节点
    if (evict && (first = head) != null && removeEldestEntry(first)) {
        K key = first.key;
        // removeNode 删除头节点
        removeNode(hash(key), key, null, false, true);
    }
}
```


 * http://www.cnblogs.com/children/archive/2012/10/02/2710624.html
 * http://www.tuicool.com/articles/IRBNB3v
 * LRU算法:http://woming66.iteye.com/blog/1284326
 * LRU算法:http://tomyz0223.iteye.com/blog/1035686
 