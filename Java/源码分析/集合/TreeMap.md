<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
**目录**

- [一、签名](#%E4%B8%80%E7%AD%BE%E5%90%8D)
- [二、设计理念](#%E4%BA%8C%E8%AE%BE%E8%AE%A1%E7%90%86%E5%BF%B5)
- [三、属性](#%E4%B8%89%E5%B1%9E%E6%80%A7)
- [四、构造方法](#%E5%9B%9B%E6%9E%84%E9%80%A0%E6%96%B9%E6%B3%95)
- [五、方法](#%E4%BA%94%E6%96%B9%E6%B3%95)
  - [1、put方法](#1put%E6%96%B9%E6%B3%95)
  - [2、get方法](#2get%E6%96%B9%E6%B3%95)
- [六、TreeSet](#%E5%85%ADtreeset)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->


# 一、签名

```java
public class TreeMap<K,V> extends AbstractMap<K,V> implements NavigableMap<K,V>, Cloneable, java.io.Serializable
```

- 相比 HashMap，TreeMap 多实现了一个接口 NavigableMap，这个决定了 TreeMap 与 HashMap 的不同：HashMap 的key是无序的, TreeMap 的key是有序的
- 接口 NavigableMap 其继承了接口 SortedMap，从字面上理解，说明该Map是有序的，所谓的顺序是指：由 Comparable 接口提供的keys的自然序，或者也可以在创建 SortedMap 实例时指定一个 Comparator 来决定；
    - 插入 SortedMap 中的key的类类都必须继承 Comparable 类(或者指定一个 Comparator)，这样才能确定如何比较两个key，否则在插入时会报 ClassCastException 异常；因此 SortedMap 中key的顺序性应该与equals方法保持一致，也就是说`k1.compareTo(k2)或comparator.compare(k1, k2)为true时，k1.equals(k2)也应该为true`
    - NavigableMap 是JDK1.6新增的，在SortedMap的基础上，增加了一些"导航方法"(navigation methods)来返回与搜索目标最近的元素
        - lowerEntry：返回所有比给定 Map.Entry 小的元素；
        - floorEntry：返回所有比给定 Map.Entry 小或相等的元素；
        - ceilingEntry：返回所有比给定 Map.Entry 大或相等的元素；
        - higherEntry：返回所有比给定 Map.Entry 大的元素；

# 二、设计理念

- TreeMap 是用红黑树作为基础实现的，红黑树是一种二叉搜索树

- 二叉搜索树：左子树的值小于根节点，右子树的值大于根节点

    二叉搜索树的优势在于每进行一次判断就是能将问题的规模减少一半，所以如果二叉搜索树是平衡的话，查找元素的时间复杂度为log(n)，也就是树的高度；

- 红黑树，通过下面五条规则，保证了树的平衡：
    - 树的节点只有红与黑两种颜色
    - 根节点为黑色的
    - 叶子节点为黑色的
    - 红色节点的字节点必定是黑色的
    -从任意一节点出发，到其后继的叶子节点的路径中，黑色节点的数目相同

- TreeMap 与 HashMap
    - TreeMap 的key是有序的，增删改查操作的时间复杂度为O(log(n))，为了保证红黑树平衡，在必要时会进行旋转
    - HashMap 的key是无序的，增删改查操作的时间复杂度为O(1)，为了做到动态扩容，在必要时会进行resize

# 三、属性

```java
// 比较器，如果其key没有实现Comparable接口，也可以指定对应的比较器
private final Comparator<? super K> comparator;
// 红黑树根节点
private transient Entry<K,V> root = null;
// 集合元素数量
private transient int size = 0;
// "fail-fast"集合修改记录
private transient int modCount = 0;
```

# 四、构造方法

```java
/*
* 默认构造方法，comparator为空，代表使用key的自然顺序来维持TreeMap的顺序，这里要求key必须实现Comparable接口
*/
public TreeMap() {
    comparator = null;
}
/**
* 用指定的比较器构造一个TreeMap
*/
public TreeMap(Comparator<? super K> comparator) {
    this.comparator = comparator;
}
/**
* 构造一个指定map的TreeMap，同样比较器comparator为空，使用key的自然顺序排序，同样的，其key必须实现Comparable接口
*/
public TreeMap(Map<? extends K, ? extends V> m) {
    comparator = null;
    putAll(m);
}
/**
* 构造一个指定SortedMap的TreeMap，根据SortedMap的比较器来来维持TreeMap的顺序
*/
public TreeMap(SortedMap<K, ? extends V> m) {
    comparator = m.comparator();
    try {
        buildFromSorted(m.size(), m.entrySet().iterator(), null, null);
    } catch (java.io.IOException cannotHappen) {
    } catch (ClassNotFoundException cannotHappen) {
    }
}
```

# 五、方法

## 1、put方法

## 2、get方法

# 六、TreeSet

是树实现的集合

## 1、底层结构

TreeSet是基于TreeMap来实现的，其构造方法都是去构造一个TreeMap：
```java
public TreeSet() {
    this(new TreeMap<E,Object>());
}
```

## 2、添加元素

往TreeSet里添加对象方法是基于TreeMap的put方法实现
```java
private static final Object PRESENT = new Object();
public boolean add(E e) {
    return m.put(e, PRESENT)==null;
}
```
其是以传入的值作为key，构造一个Object对象作为value，并且该Key也必须要是实现Comparable接口，或者在构造时有对应的比较器；

如下例子：
```java
public static void main(String[] args) {
    SortedSet set = new TreeSet();
    set.add("1");// 1
    set.add(1);// 2
    System.out.println(set);
}
```
上述代码中会在代码2中抛出异常：
```java
Exception in thread "main" java.lang.ClassCastException: java.lang.String cannot be cast to java.lang.Integer
	at java.lang.Integer.compareTo(Integer.java:52)
	at java.util.TreeMap.put(TreeMap.java:568)
	at java.util.TreeSet.add(TreeSet.java:255)
```
因为TreeMap要实现有序，必须需要比较，代码2操作的时候，使用Integer的compareTo方法比较的时候，无法将字符串转为Integer，所以会报异常

## 3、获取元素

## 4、迭代

```java
// NavigableSet 接口，定义了迭代的一些规范，和一些取值的特殊方法，TreeSet 实现了该方法，也就是说 TreeSet 本身已经定义了迭代的规范
public interface NavigableSet<E> extends SortedSet<E> {
    Iterator<E> iterator();
    E lower(E e);
}  
// m.navigableKeySet() 是 TreeMap 写了一个子类实现了 NavigableSet接口，实现了 TreeSet 定义的迭代规范
public Iterator<E> iterator() {
    return m.navigableKeySet().iterator();
}
```
TreeSet定义了接口的规范，TreeMap 负责去实现。

**为什么要如此实现呢？**

因为TreeSet要求能够从头开始迭代，或者要取第一个值，或者只取最后一个值，再加上TreeMap本身结构实现比较复杂，而TreeSet又不清楚TreeMap内部结构，所以为了避免TreeSet实现复杂的场景，不如由TreeSet定义好规范，有TreeMap去实现

## 5、常见使用场景

一般需要把元素进行排序的时候使用TreeSet，但是使用时需要注意实现Comparable接口













