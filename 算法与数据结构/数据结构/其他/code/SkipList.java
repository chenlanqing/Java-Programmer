@SuppressWarnings("unchecked")
public class SkipList<E extends Comparable<E>> {

    private static final float SKIP_LIST_P = 0.5f;
    private static final int MAX_LEVEL = 0x10;

    private int levelCount = 1;

    private int size;

    private final Node<E> head = new Node<>(0, null);

    public int getSize() {
        return size;
    }

    public boolean contains(E e) {
        return find(e) != null;
    }

    public Node<E> find(E e) {
        Node<E> p = head;
        // 先按层级，从顶层到最底层：即上 ——> 下
        for (int i = levelCount - 1; i >= 0; i--) {
            // 每一层：从左到右找数据
            while (p.forwards[i] != null && p.forwards[i].data.compareTo(e) < 0) {
                p = p.forwards[i];
            }
        }
        if (p.forwards[0] != null && p.forwards[0].data.compareTo(e) == 0) {
            return p.forwards[0];
        }
        return null;
    }

    public void insert(E e, int level) {
        if (contains(e)) {
            return;
        }
        if (level == 0) {
            level = randomLevel();
        }
        Node<E> newNode = new Node<>(level, e);
        // update 数组记录在每一层中新节点插入位置的前一个节点，初始值设置为头结点
        Node<E>[] update = new Node[level];
        for (int i = 0; i < level; i++) {
            update[i] = head;
        }
        // 从跳表的顶层开始遍历，找到每一层中最后一个小于新节点值的节点，记录到 update 数组中
        Node<E> p = head;
        for (int i = level - 1; i >= 0; i--) {
            while (p.forwards[i] != null && p.forwards[i].data.compareTo(e) < 0) {
                p = p.forwards[i];
            }
            update[i] = p;// 这里update[i]表示当前层节点的前一节点，因为要找到前一节点，才好插入数据
        }
        // 将每一层节点和后面节点关联
        for (int i = 0; i < level; i++) {
            // 记录当前层节点后面节点指针
            newNode.forwards[i] = update[i].forwards[i];
             // 前一个节点指向当前节点
            update[i].forwards[i] = newNode;
        }
        size++;
        if (level > levelCount) {
            levelCount = level;
        }
    }

    public void insert(E e) {
        int level = randomLevel();
        insert(e, level);
    }

    public void delete(E e) {
        size--;
        Node<E> p = head;
        Node<E>[] updates = new Node[levelCount];
        // 先按层级，从顶层到最底层：即上 ——> 下
        for (int i = levelCount - 1; i >= 0; i--) {
            // 每一层：从左到右找数据
            while (p.forwards[i] != null && p.forwards[i].data.compareTo(e) < 0) {
                p = p.forwards[i];
            }
            updates[i] = p;
        }
        // 找到待删除的节点
        if (p.forwards[0] != null && p.forwards[0].data.compareTo(e) == 0) {
            for (int i = levelCount - 1; i >= 0; i--) {
                // 在查找到待删除节点之后，需要遍历每一层，从 update 数组中找到待删除节点的前一个节点，并更新前一个节点的 forwards 指针，将其指向待删除节点的后一个节点
                if (updates[i].forwards[i] != null && updates[i].forwards[i].data.compareTo(e) == 0) {
                    updates[i].forwards[i] = updates[i].forwards[i].forwards[i];
                }
            }
        }
        // 如果删除节点的层数大于跳表的最大层数，需要先将待删除节点的层数调整为跳表的最大层数
        // 删除节点之后，如果存在一些节点在删除节点之后成为了“孤儿节点”（即没有任何节点指向它），我们需要将这些孤儿节点从跳表中删除，以保持跳表的空间效率
        while (levelCount > 1 && head.forwards[levelCount] == null) {
            levelCount--;
        }
    }

    public void printAll() {
        Node<E> p = head;
        while (p.forwards[0] != null) {
            System.out.println(p.forwards[0]);
            p = p.forwards[0];
        }
        System.out.println();
    }

    /**
     * 理论来讲，一级索引中元素个数应该占原始数据的 50%，二级索引中元素个数占 25%，三级索引12.5% ，一直到最顶层。
     * 因为这里每一层的晋升概率是 50%。对于每一个新插入的节点，都需要调用 randomLevel 生成一个合理的层数。
     * 该 randomLevel 方法会随机生成 1~MAX_LEVEL 之间的数，且 ：
     * 50%的概率返回 1
     * 25%的概率返回 2
     * 12.5%的概率返回 3 ...
     */
    private int randomLevel() {
        int level = 1;

        while (Math.random() < SKIP_LIST_P && level < MAX_LEVEL)
            level += 1;
        return level;
    }

    public static class Node<E> {
        int maxLevel;
        E data;
        private final Node<E>[] forwards = new Node[MAX_LEVEL];

        public Node(int maxLevel, E data) {
            this.maxLevel = maxLevel;
            this.data = data;
        }

        @Override
        public String toString() {
            return "{ data: " + data +
                    "; maxLevel: " + maxLevel +
                    " }";
        }
    }
}
