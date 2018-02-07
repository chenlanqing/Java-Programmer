 * JDK6 TreeMap 的源码解析:http://www.importnew.com/17605.html
 * TreeMap的工作原理: http://www.importnew.com/19074.html

# 一.签名:
    public class TreeMap<K,V> extends AbstractMap<K,V> implements NavigableMap<K,V>, Cloneable, java.io.Serializable{}
    1.相比 HashMap,TreeMap 多实现了一个接口 NavigableMap,这个决定了 TreeMap 与 HashMap 的不同:
        HashMap 的key是无序的, TreeMap 的key是有序的
    2.接口 NavigableMap 其继承了接口 SortedMap,从字面上理解,说明该Map是有序的,所谓的顺序是指:
        由 Comparable 接口提供的keys的自然序,或者也可以在创建 SortedMap 实例时指定一个 Comparator 来决定;
        (1).插入 SortedMap 中的key的类类都必须继承 Comparable 类(或者指定一个 Comparator),这样才能确定如何比较两个key,
            否则在插入时会报 ClassCastException 异常;因此 SortedMap 中key的顺序性应该与equals方法保持一致,
            也就是说k1.compareTo(k2)或comparator.compare(k1, k2)为true时,k1.equals(k2)也应该为true
        (2).NavigableMap 是JDK1.6新增的,在SortedMap的基础上，增加了一些"导航方法"(navigation methods)来返回与搜索目标最近的元素
            lowerEntry :返回所有比给定 Map.Entry 小的元素
            floorEntry :返回所有比给定 Map.Entry 小或相等的元素
            ceilingEntry:返回所有比给定 Map.Entry 大或相等的元素
            higherEntry:返回所有比给定 Map.Entry 大的元素
# 二.设计理念:
    1.TreeMap 是用红黑树作为基础实现的,红黑树是一种二叉搜索树
    2.二叉搜索树:左子树的值小于根节点,右子树的值大于根节点
        二叉搜索树的优势在于每进行一次判断就是能将问题的规模减少一半,所以如果二叉搜索树是平衡的话,
        查找元素的时间复杂度为log(n),也就是树的高度
    3.红黑树:
        3.1.红黑树通过下面五条规则,保证了树的平衡:
            (1).树的节点只有红与黑两种颜色
            (2).根节点为黑色的
            (3).叶子节点为黑色的
            (4).红色节点的字节点必定是黑色的
            (5).从任意一节点出发，到其后继的叶子节点的路径中，黑色节点的数目相同
        3.2
    4.TreeMap 与 HashMap:
        TreeMap 的key是有序的，增删改查操作的时间复杂度为O(log(n))，为了保证红黑树平衡，在必要时会进行旋转
        HashMap 的key是无序的，增删改查操作的时间复杂度为O(1)，为了做到动态扩容，在必要时会进行resize
# 三.属性:
	// 比较器
    private final Comparator<? super K> comparator;
    // 红黑树根节点
    private transient Entry<K,V> root = null;
    // 集合元素数量
    private transient int size = 0;
    // "fail-fast"集合修改记录
    private transient int modCount = 0;

# 四.构造方法:
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
	 * 构造一个指定map的TreeMap，同样比较器comparator为空，使用key的自然顺序排序
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
# 五.方法:
    1.public V put(K key, V value);
        1.1.红黑树的添加原理:
            将一个节点添加到红黑树,通常需要经过以下步骤:
            (1).将红黑树当成一颗二叉查找树,将节点插入
            (2).将新插入的节点设置为红色
            (3).通过旋转和着色,使它恢复平衡,重新变成一颗符合规则的红黑树






















