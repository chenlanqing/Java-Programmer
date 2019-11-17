<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
**目录**

- [一、类的定义](#%e4%b8%80%e7%b1%bb%e7%9a%84%e5%ae%9a%e4%b9%89)
- [二、属性](#%e4%ba%8c%e5%b1%9e%e6%80%a7)
  - [1、节点属性](#1%e8%8a%82%e7%82%b9%e5%b1%9e%e6%80%a7)
    - [1.1、JDK1.6](#11jdk16)
    - [1.2、JDK7与JDK8](#12jdk7%e4%b8%8ejdk8)
  - [2、节点类型](#2%e8%8a%82%e7%82%b9%e7%b1%bb%e5%9e%8b)
- [三、方法](#%e4%b8%89%e6%96%b9%e6%b3%95)
  - [1、追加（新增）节点](#1%e8%bf%bd%e5%8a%a0%e6%96%b0%e5%a2%9e%e8%8a%82%e7%82%b9)
    - [1.1、链表尾部追加：add和addLast](#11%e9%93%be%e8%a1%a8%e5%b0%be%e9%83%a8%e8%bf%bd%e5%8a%a0add%e5%92%8caddlast)
    - [1.2、链表头部添加：addFirst](#12%e9%93%be%e8%a1%a8%e5%a4%b4%e9%83%a8%e6%b7%bb%e5%8a%a0addfirst)
  - [2、删除节点](#2%e5%88%a0%e9%99%a4%e8%8a%82%e7%82%b9)
    - [2.1、从头部删除](#21%e4%bb%8e%e5%a4%b4%e9%83%a8%e5%88%a0%e9%99%a4)
    - [2.2、从尾部删除](#22%e4%bb%8e%e5%b0%be%e9%83%a8%e5%88%a0%e9%99%a4)
  - [3、节点查询](#3%e8%8a%82%e7%82%b9%e6%9f%a5%e8%af%a2)
  - [4、Deque接口的方法](#4deque%e6%8e%a5%e5%8f%a3%e7%9a%84%e6%96%b9%e6%b3%95)
  - [5、迭代](#5%e8%bf%ad%e4%bb%a3)
- [参考资料](#%e5%8f%82%e8%80%83%e8%b5%84%e6%96%99)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->


# 一、类的定义

```java
public class LinkedList<E> extends AbstractSequentialList<E> implements List<E>, Deque<E>, Cloneable, java.io.Serializable {

}
```
- `AbstractSequenceList` 提供了 List 接口骨干性的实现以减少实现 List 接口的复杂度
- `Deque` 接口定义了双端队列的操作，是一个双向链表；

LinkedList的相关概念
- Node节点有prev（前驱节点）、next（后置节点）属性；
- first是双向链表的头节点，其前一个节点为null；
- last是双向链表的尾节点，其后一个节点为null；
- 当链表中没有数据时，first和last是同一个节点，都指向null；
- 双向链表只要内存足够，没有大小限制

# 二、属性

## 1、节点属性

### 1.1、JDK1.6

```java
private transient Entry<E> header = new Entry<E>(null, null, null);// 链表的头节点
private transient int size = 0; // 链表的长度
```
- `Entry<E> header`：链表的头结点：
- `private static class Entry<E>`：Entry 即是节点对象，该对象里定义了存储的元素，后驱节点，前驱节点，每个节点只指定字节的前驱节点和后驱节点

### 1.2、JDK7与JDK8

```java
transient int size = 0; // 链表的长度
transient Node<E> first;
transient Node<E> last;
```
- Node<E> first 表示第一个节点,
- Node<E> last 表示最后一个节点
- private static class Node<E>，Node 即是节点对象，该对象里定义了存储的元素，后驱节点，前驱节点，每个节点只指定字节的前驱节点和后驱节点
        
## 2、节点类型

双向链表中的节点
```java
private static class Node<E> {
    E item;// 节点值
    Node<E> next;// 前置节点
    Node<E> prev;// 后置节点

    Node(Node<E> prev, E element, Node<E> next) {
        this.item = element;
        this.next = next;
        this.prev = prev;
    }
}
```

# 三、方法

## 1、追加（新增）节点

追加节点时，可以追加到链表头，也可以追加到链表尾部，add方法默认是从尾部开始追加的，addFirst是从头部开始追加的

方法 | 方法含义 | 时间复杂度
-----|--------|-------
public boolean add(E e)|链表尾部插入元素|时间复杂度 O(1)
public void addFirst(E e)|链表头部插入元素|时间复杂度 O(1)
public void addLast(E e)|链表尾部插入元素|时间复杂度 O(1)

头部和尾部追加节点，只是前者是移动头节点的prev指向，后者是移动尾节点的next指向

### 1.1、链表尾部追加：add和addLast

```java
public boolean add(E e) {
    linkLast(e);
    return true;
}
public void addLast(E e) {
    linkLast(e);
}
// 尾部追加节点
void linkLast(E e) {
    // 暂存尾部节点
    final Node<E> l = last;
    // 新建添加的元素为节点
    final Node<E> newNode = new Node<>(l, e, null);
    // 新建节点为尾部节点
    last = newNode;
    
    if (l == null)
        // 如果链表为空（l 是尾节点，尾节点为空，链表即空），头部和尾部是同一个节点，都是新建的节点
        first = newNode;
    else
        // 否则把前尾节点的下一个节点，指向当前尾节点。
        l.next = newNode;
    size++;
    modCount++;
}
```

### 1.2、链表头部添加：addFirst

```java
public void addFirst(E e) {
    linkFirst(e);
}
// 从头部追加
private void linkFirst(E e) {
    // 头节点赋值给临时变量
    final Node<E> f = first;
    // 新建节点，前一个节点指向null，e 是新建节点，f 是新建节点的下一个节点，目前值是头节点的值
    final Node<E> newNode = new Node<>(null, e, f);
    // 新建节点成为头节点
    first = newNode;
    // 头节点为空，就是链表为空，头尾节点是一个节点
    if (f == null)
        last = newNode;
    //上一个头节点的前一个节点指向当前节点
    else
        f.prev = newNode;
    size++;
    modCount++;
}
```

## 2、删除节点

方法  | 方法描述 | 时间负复杂度
------|---------|----------
public boolean remove(Object o)|删除指定元素|时间复杂度 O(N)
public E remove()|删除头结点,调用removeFirst()|时间复杂度 O(1),调用
public E removeFirst()|删除头部节点,并返回节点的值|时间复杂度 O(1)
public E removeLast()|删除尾部节点,并返回节点的值|时间复杂度 O(1)
public E remove(int index)|删除某个位置的节点,并返回|时间复杂度 O(N)

### 2.1、从头部删除

```java
//从头删除节点 f 是链表头节点
private E unlinkFirst(Node<E> f) {
    // 拿出头节点的值，作为方法的返回值
    final E element = f.item;
    // 拿出头节点的下一个节点
    final Node<E> next = f.next;
    // 帮助 GC 回收头节点
    f.item = null;
    f.next = null;
    // 头节点的下一个节点成为头节点
    first = next;
    //如果 next 为空，表明链表为空
    if (next == null)
        last = null;
    //链表不为空，头节点的前一个节点指向 null
    else
        next.prev = null;
    //修改链表大小和版本
    size--;
    modCount++;
    return element;
}
```

### 2.2、从尾部删除

```java
private E unlinkLast(Node<E> l) {
    // assert l == last && l != null;
    final E element = l.item;
    final Node<E> prev = l.prev;
    l.item = null;
    l.prev = null; // help GC
    last = prev;
    if (prev == null)
        first = null;
    else
        prev.next = null;
    size--;
    modCount++;
    return element;
}
```

链表结构针对节点新增、删除都非常简单，只是修改前后节点的指向；

## 3、节点查询

```java
// 根据链表索引位置查询节点
Node<E> node(int index) {
    // 如果 index 处于队列的前半部分，从头开始找，size >> 1 是 size 除以 2 的意思。
    if (index < (size >> 1)) {
        Node<E> x = first;
        // 直到 for 循环到 index 的前一个 node 停止
        for (int i = 0; i < index; i++)
            x = x.next;
        return x;
    } else {// 如果 index 处于队列的后半部分，从尾开始找
        Node<E> x = last;
        // 直到 for 循环到 index 的后一个 node 停止
        for (int i = size - 1; i > index; i--)
            x = x.prev;
        return x;
    }
}
```

LinkedList的查询并没有采用从头到尾的循环，而是采用简单二分法，看index是在链表的前半部分还是后半部分；

- 获取头部元素：getFirst；
- 获取尾部元素：getLast

## 4、Deque接口的方法

方法含义 | 返回异常 | 返回特殊值 | 底层实现
--------|--------|----------|---------
新增 | add(e) | offer(e) | 底层实现相同
删除 | remove()|poll(e) |链表为空时，remove会抛出异常，poll返回null
查找 | element() | peek() | 链表为空时，element会抛出异常，peek返回null

## 5、迭代

LinkedList需要实现双向的迭代访问，Iterator接口不行，其只支持从头到尾访问。Java新增了一个迭代接口：ListIterator，该接口提供了向前和向后的迭代方式：
- 从尾到头迭代：hasPrevious、previous、previousIndex
- 从头到尾迭代：hasNext、next、nextIndex

LinkedList里有个内部类：ListItr实现了ListIterator：
```java
 private class ListItr implements ListIterator<E> {
    private Node<E> lastReturned;//上一次执行 next() 或者 previos() 方法时的节点位置
    private Node<E> next;// 下一个节点
    private int nextIndex;// 下一个节点的位置
    private int expectedModCount = modCount;

    ListItr(int index) {
        // assert isPositionIndex(index);
        next = (index == size) ? null : node(index);
        nextIndex = index;
    }
}
```


# 参考资料

* [LinkedList源码分析](https://mp.weixin.qq.com/s/FcTVC7rcq1GXxXa5yySu9w)
