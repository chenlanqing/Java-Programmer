<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
**目录**

- [一.类的定义(签名):](#%E4%B8%80%E7%B1%BB%E7%9A%84%E5%AE%9A%E4%B9%89%E7%AD%BE%E5%90%8D)
- [二.属性:](#%E4%BA%8C%E5%B1%9E%E6%80%A7)
- [三.构造方法:](#%E4%B8%89%E6%9E%84%E9%80%A0%E6%96%B9%E6%B3%95)
- [四.方法:](#%E5%9B%9B%E6%96%B9%E6%B3%95)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->


# 一、类的定义(签名)
```java
public class LinkedList<E> extends AbstractSequentialList<E> implements List<E>, Deque<E>, Cloneable, java.io.Serializable {

}
```
- `AbstractSequenceList`提供了 List 接口骨干性的实现以减少实现 List 接口的复杂度
- `Deque`接口定义了双端队列的操作

# 二、属性

## 1、JDK6

```java
private transient Entry<E> header = new Entry<E>(null, null, null);// 链表的头节点
private transient int size = 0; // 链表的长度
```
- `Entry<E> header`：链表的头结点：
- `private static class Entry<E>`：Entry 即是节点对象，该对象里定义了存储的元素，后驱节点，前驱节点，每个节点只指定字节的前驱节点和后驱节点

## 2、JDK7

```java
transient int size = 0; // 链表的长度
transient Node<E> first;
transient Node<E> last;
```
- Node<E> first 表示第一个节点,
- Node<E> last 表示最后一个节点
- private static class Node<E>，Node 即是节点对象，该对象里定义了存储的元素，后驱节点，前驱节点，每个节点只指定字节的前驱节点和后驱节点
        


# 参考资料

* [LinkedList源码分析](https://mp.weixin.qq.com/s/FcTVC7rcq1GXxXa5yySu9w)






————————

# 三.构造方法:
    1.不带参数的构造器
        (1).JDK6:该构造器就是将头结点的前驱和后驱节点都指向header
            public LinkedList() {
                header.next = header.previous = header;
            } 
        (2).JDK7:构造一个空链表:
            public LinkedList() {
            }
    2.带参数的构造器:传入一个集合
        public LinkedList(Collection<? extends E> c) {
            this();
            addAll(c);
        }
# 四.方法:
    1.头尾插入:前驱节点和后驱节点指针的移动
        public boolean add(E e)		链表尾部插入元素	时间复杂度 O(1)
        public void addFirst(E e)	链表头部插入元素	时间复杂度 O(1)
        public void addLast(E e)	链表尾部插入元素	时间复杂度 O(1)
        1.1.boolean add(E e):size++,modCount++;
            (1).JDK6:该方法调用方法 addBeafore(private Entry<E> addBefore(E e, Entry<E> entry)),
                通过 Entry 的构造方法创建e的节点newEntry,修改插入位置后newEntry的前一节点的next引用和后一节点的previous引用,
                使链表节点间的引用关系保持正确。之后修改和size大小和记录modCount，然后返回新插入的节点;
            (2).JDK7:调用了linkLast(void linkLast(E e))方法,
                ①.定义节点变量 l 指向 last属性;
                ②.新建节点 newNode = new Node<>(l, e, null); newNode的前驱节点为 l,后驱节点为 null;
                ③.将 newNode 赋值给 last,因为第一次添加数据时,属性last和first都为 null;
                ④.如果是第一次添加数据则直接将newNode 赋值给 first,否则就是l.next = newNodesize++,modCount++;
        1.2.addFirst(E e):
            (1).JDK6:调用addBeafore,传入的参数是 e和 header.next
                public void addFirst(E e) {
                    addBefore(e, header.next);
                }
            (2).JDK7:调用linkFirst
                public void addFirst(E e) {
                    linkFirst(e);
                }
                linkFirst的实现如下:
                    private void linkFirst(E e) {
                        final Node<E> f = first;
                        final Node<E> newNode = new Node<>(null, e, f);
                        first = newNode;
                        if (f == null)
                            last = newNode;
                        else
                            f.prev = newNode;
                        size++;
                        modCount++;
                    }
        1.3.addLast(E e):
            (1).JDK6:调用如下: addBefore(e, header);
            (2).JDK7:调用linkLast(e)
    2.删除节点:指针的移动
        public boolean remove(Object o)		删除指定元素					时间复杂度 O(N)
        public E remove()					删除头结点,调用removeFirst()	时间复杂度 O(1),调用
        public E removeFirst()				删除头部节点,并返回节点的值		时间复杂度 O(1)
        public E removeLast()				删除尾部节点,并返回节点的值		时间复杂度 O(1)
        public E remove(int index)			删除某个位置的节点,并返回 		时间复杂度 O(N)
        2.1.boolean remove(Object o)
            (1).JDK6:
                最终调用的是:remove(e);
                    private E remove(Entry<E> e) {
                        if (e == header)
                            throw new NoSuchElementException();
                        E result = e.element;
                        e.previous.next = e.next;
                        e.next.previous = e.previous;
                        e.next = e.previous = null;
                        e.element = null;
                        size--;
                        modCount++;
                        return result;
                    }
            (2).JDK7:
                最终调用:unlink(Node<E> x)
                    E unlink(Node<E> x) {
                        // assert x != null;
                        final E element = x.item;
                        final Node<E> next = x.next;
                        final Node<E> prev = x.prev;
                        if (prev == null) {
                            first = next;
                        } else {
                            prev.next = next;
                            x.prev = null;
                        }
                        if (next == null) {
                            last = prev;
                        } else {
                            next.prev = prev;
                            x.next = null;
                        }
                        x.item = null;
                        size--;
                        modCount++;
                        return element;
                    }
        2.2.removeFirst():
            (1).JDK6:调用 remove(header.next);
            (2).JDK7:
                判断 first 属性是否为 null,为 null 的话抛出异常(NoSuchElementException);
                调用 unlinkFirst(f);
                    private E unlinkFirst(Node<E> f) {
                        // assert f == first && f != null;
                        final E element = f.item;
                        final Node<E> next = f.next;
                        f.item = null;
                        f.next = null; // help GC
                        first = next;
                        if (next == null)
                            last = null;
                        else
                            next.prev = null;
                        size--;
                        modCount++;
                        return element;
                    }
        2.3.removeLast():
            (1).JDK6:调用 remove(header.previous);
            (2).JDK7:
                判断 last 属性是否为 null,为 null 的话抛出异常(NoSuchElementException);
                unlinkLast(l):
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
        2.4.remove(int index)
            (1).JDK6:remove(entry(index)); private E remove(Entry<E> e)			
                entry()方法:找到该节点元素
                    ①.判断是否越界
                    ②.判断当前索引的大小,如果是在链表的后半段,则从尾节点开始循环
                    ③.如果是在链表的前半段,则从头节点开始循环
                remove(Entry<E> e):
                    指针移动,删除指定的元素
            (2).JDK7:
                checkElementIndex(index);// 判断是否越界
                return unlink(node(index));
                node(index)
    3.查询和修改:

    4.获取链表中某个节点:index在左半部分时，从链表头部遍历，否则从尾部遍历
        4.1.JDK6:private Entry<E> entry(int index)方法:
                if (index < 0 || index >= size)
                    throw new IndexOutOfBoundsException("Index: "+index+", Size: "+size);
                Entry<E> e = header;
                if (index < (size >> 1)) {
                    for (int i = 0; i <= index; i++)
                        e = e.next;
                } else {
                    for (int i = size; i > index; i--)
                        e = e.previous;
                }
                return e;
        4.2.JDK7:Node<E> node(int index):判断越界在调用本方法前都要调用 checkElementIndex(index)方法
            if (index < (size >> 1)) {
                Node<E> x = first;
                for (int i = 0; i < index; i++)
                    x = x.next;
                return x;
            } else {
                Node<E> x = last;
                for (int i = size - 1; i > index; i--)
                    x = x.prev;
                return x;
            }