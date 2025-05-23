# 一、类的定义

LinkedList是一个基于链表实现的双端队列
```java
public class LinkedList<E> extends AbstractSequentialList<E> 
    implements List<E>, Deque<E>, Cloneable, java.io.Serializable {
}
```
LinkedList同时实现了List接口和Deque接口，也就是说它既可以看作一个顺序容器，又可以看作一个队列(Queue)，同时又可以看作一个栈(Stack)
- `AbstractSequentialList` 提供了 List 接口骨干性的实现以减少实现 List 接口的复杂度
- `Deque`：接口定义了双端队列的操作，是一个双向链表；

LinkedList的相关概念
- Node节点有prev（前驱节点）、next（后置节点）属性；
- first是双向链表的头节点，其前一个节点为null；
- last是双向链表的尾节点，其后一个节点为null；
- 当链表中没有数据时，first和last是同一个节点，都指向null；
- 双向链表只要内存足够，没有大小限制

> 关于栈或队列，现在的首选是ArrayDeque，它有着比LinkedList(当作栈或队列使用时)有着更好的性能

LinkedList的实现方式决定了所有跟下标相关的操作都是线性时间，而在首段或者末尾删除元素只需要常数时间；

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
    // 如果index = 0，只能从头到尾遍历，如果 0 < index <= size，即可以往头节点遍历，也可以往尾部遍历
    ListItr(int index) {
        // assert isPositionIndex(index);
        next = (index == size) ? null : node(index);
        nextIndex = index;
    }
}
```
另外如果需要从尾部往头部遍历，可以使用 DescendingIterator，调用方法：`list.descendingIterator()`，其实现就是调用上面的 ListItr 来实现的：
```java
/**
* @since 1.6
*/
public Iterator<E> descendingIterator() {
    return new DescendingIterator();
}
/**
* Adapter to provide descending iterators via ListItr.previous
*/
private class DescendingIterator implements Iterator<E> {
    private final ListItr itr = new ListItr(size());
    public boolean hasNext() {
        return itr.hasPrevious();
    }
    public E next() {
        return itr.previous();
    }
    public void remove() {
        itr.remove();
    }
}
```

# 三、ArrayDeque

## 1、签名

```java
public class ArrayDeque<E> extends AbstractCollection<E> implements Deque<E>, Cloneable, Serializable {
    // 数组存储的元素，数组是可以扩容的，一般是2的N次方，数组内元素不可以为NULL；
    transient Object[] elements; 
    // 头结点索引位置
    transient int head;
    // 尾节点索引位置
    transient int tail;
}
```
ArrayDeque 是用环形数组实现的双端队列，可以使用ArrayDeque来实现队列或者栈；内部维护头、尾两个指针，可以分别头、尾读取数据；

## 2、构造方法

ArrayDeque 有三个构造方法
```java
public ArrayDeque() {
    elements = new Object[16];
}
public ArrayDeque(int numElements) {
    allocateElements(numElements);
}
public ArrayDeque(Collection<? extends E> c) {
    allocateElements(c.size());
    addAll(c);
}
private void allocateElements(int numElements) {
    elements = new Object[calculateSize(numElements)];
}
private static int calculateSize(int numElements) {
    int initialCapacity = MIN_INITIAL_CAPACITY;
    // Find the best power of two to hold elements.
    // Tests "<=" because arrays aren't kept full.
    if (numElements >= initialCapacity) {
        initialCapacity = numElements;
        initialCapacity |= (initialCapacity >>>  1);
        initialCapacity |= (initialCapacity >>>  2);
        initialCapacity |= (initialCapacity >>>  4);
        initialCapacity |= (initialCapacity >>>  8);
        initialCapacity |= (initialCapacity >>> 16);
        initialCapacity++;

        if (initialCapacity < 0)   // Too many elements, must back off
            initialCapacity >>>= 1;// Good luck allocating 2 ^ 30 elements
    }
    return initialCapacity;
}
```
- 无参构造方法：默认会构造一个数组，数组容量是16；
- 带容量参数构造方法：会根据传入的容量，计算得出大于等于传入容量的2的幂的最小值，比如传入13，那么最终构造的数组容量是16，因为16是大于13的最小2的幂；
- 带集合参数构造方法：会根据传入的集合的容量，计算出对应新的ArrayDeque内的数组的容量；

## 3、方法

### 3.1、扩容

ArrayDeque内的数组扩容是按照原有容量的两倍扩充的，保持数组的长度为2的幂；
```java
private void doubleCapacity() {
    // 只有头尾指针相等才会进行扩容
    assert head == tail;
    int p = head;
    int n = elements.length;
    int r = n - p; // number of elements to the right of p
    int newCapacity = n << 1;
    if (newCapacity < 0)
        throw new IllegalStateException("Sorry, deque too big");
    Object[] a = new Object[newCapacity];
    System.arraycopy(elements, p, a, 0, r);
    System.arraycopy(elements, 0, a, r, p);
    elements = a;
    head = 0;
    tail = n;
}
```

### 3.2、size方法

基于环形数组的实现，那么元素个数的计算不能使用简单的 element.length 来计算；
```java
public int size() {
    return (tail - head) & (elements.length - 1);
}
```

# 四、Stack、ArrayDeque、LinkedList对比

## 1、底层数据存储方式

|            | 存储方式       |
| ---------- | -------------- |
| Stack      | 长度为10的数组 |
| ArrayDeque | 长度为16的数组 |
| LinkedList | 链表           |

## 2、方法参照表

| Stack   | ArrayDeque                | LinkedList                |
| ------- | ------------------------- | ------------------------- |
| push(e) | addFirst(e)/offerFirst(e) | addFirst(e)/offerFirst(e) |
| pop()   | removeFirst()/pollFirst() | removeFirst()/pollFirst() |
| peek()  | getFirst()/peekFirst()    | getFirst()/peekFirst()    |

## 3、线程安全

|            | 线程安全   |
| ---------- | ---------- |
| Stack      | 线程同步   |
| ArrayDeque | 线程不同步 |
| LinkedList | 线程不同步 |

## 4、使用场景

- 频繁的插入、删除操作：LinkedList
- 频繁的随机访问操作：ArrayDeque
- 未知的初始数据量：LinkedList


# 参考资料

* [LinkedList源码分析](https://mp.weixin.qq.com/s/FcTVC7rcq1GXxXa5yySu9w)
