红黑树-Red Black Tree
/**
 * 红黑树视频解析: http://www.csanimated.com/animation.php?t=Red-black_tree
 * 红黑树入门: http://blog.csdn.net/v_JULY_v/article/details/6105630
 * http://tech.meituan.com/redblack-tree.html
 * http://blog.csdn.net/u011240877/article/details/53329023
 * http://www.cnblogs.com/yangecnu/p/Introduce-Red-Black-Tree.html
 */
1.二叉搜索树(Binary Search Tree)的问题:
	BST 存在的主要问题是:树在插入的时候会导致树倾斜,不同的插入顺序会导致树的高度不一样,
	而树的高度直接的影响了树的查找效率.理想的高度是log(N),最坏的情况是所有的节点都在一条斜线上,这样的树的高度为N;
2.平衡二叉树:
	基于BST存在的问题,一种新的树--平衡二叉查找树(Balanced BST)产生了.平衡二叉树在插入和删除的时候,会通过旋转
	操作将高度保持在log(N).其中两款具有代表性的平衡树分别为 AVL树 和 红黑(R-B Tree)树.
	AVL 树由于实现比较复杂,而且插入和删除性能差，在实际环境下的应用不如红黑树.
	红黑树(RBTree)实际应用广泛.比如Linux内核中的完全公平调度器、高精度计时器、ext3文件系统等等,
	各种语言的函数库如Java的 TreeMap和 TreeSet,C++ STL 的 map、multimap、multiset等
	==> 值的一提的是:Java 8中 HashMap 的实现也因为用 RBTree 取代链表,性能有所提升
3.RBTree 的定义
	(1).任何一个节点都有颜色，黑色或者红色
	(2).根节点是黑色的
	(3).每个叶结点(叶结点即指树尾端NIL指针或NULL结点)都是黑的
		Java 实现的红黑树将使用 null 来代表空节点,因此遍历红黑树时将看不到黑色的叶子节点,反而看到每个叶子节点都是红色的
	(4).每个红色节点的两个子节点一定都是黑色
		从每个根到节点的路径上不会有两个连续的红色节点,但黑色节点是可以连续的
	(5).从任一节点到其子树中每个叶子节点的路径都包含相同数量的黑色节点
	RBTree 在理论上还是一棵BST树,但是它在对BST的插入和删除操作时会维持树的平衡,即保证树的高度在[logN,logN+1]
	(理论上,极端的情况下可以出现RBTree的高度达到2*logN,但实际上很难遇到)








