/**
 * 跳表中存储的是正整数，并且存储的是不重复的。
 * 代码来源：https://github.com/wangzheng0822/algo/blob/master/java/17_skiplist/SkipList.java
 */
public class IntSkipList {

    private static final float SKIP_LIST_P = 0.5f;
    private static final int MAX_LEVEL = 16;

    private int levelCount = 1;

    private final Node head = new Node();  // 带头链表

    private int size;

    public int getSize() {
        return size;
    }

    public boolean contains(int value) {
        return find(value) != null;
    }

    public Node find(int value) {
        Node p = head;
        for (int i = levelCount - 1; i >= 0; --i) {
            while (p.forwards[i] != null && p.forwards[i].data < value) {
                p = p.forwards[i];
            }
        }

        if (p.forwards[0] != null && p.forwards[0].data == value) {
            return p.forwards[0];
        } else {
            return null;
        }
    }

    public void insert(int value) {
        if (contains(value)) {
            throw new IllegalArgumentException("数据已经存在");
        }
        // 随机层数
        int level = randomLevel();
        // 构建新的节点
        Node newNode = new Node();
        newNode.data = value;
        newNode.maxLevel = level;
        // update 数组记录在每一层中新节点插入位置的前一个节点，初始值设置为头结点
        Node[] update = new Node[level];
        for (int i = 0; i < level; ++i) {
            update[i] = head;
        }

        // 从跳表的顶层开始遍历，找到每一层中最后一个小于新节点值的节点，记录到 update 数组中
        Node p = head;
        for (int i = level - 1; i >= 0; --i) {
            while (p.forwards[i] != null && p.forwards[i].data < value) {
                p = p.forwards[i];
            }
            update[i] = p; // 这里update[i]表示当前层节点的前一节点，因为要找到前一节点，才好插入数据
        }

        // 将每一层节点和后面节点关联
        for (int i = 0; i < level; ++i) {
            // 记录当前层节点后面节点指针
            newNode.forwards[i] = update[i].forwards[i];
            // 前一个节点指向当前节点
            update[i].forwards[i] = newNode;
        }
        size++;
        // update node height
        if (levelCount < level) {
            levelCount = level;
        }
    }

    public void delete(int value) {
        Node[] update = new Node[levelCount];
        Node p = head;
        // 删除节点之前，小于待删除的节点所在层的上级节点
        for (int i = levelCount - 1; i >= 0; --i) {
            while (p.forwards[i] != null && p.forwards[i].data < value) {
                p = p.forwards[i];
            }
            update[i] = p;
        }
        // 找到待删除的节点
        if (p.forwards[0] != null && p.forwards[0].data == value) {
            // 在查找到待删除节点之后，需要遍历每一层，从 update 数组中找到待删除节点的前一个节点，并更新前一个节点的 forwards 指针，将其指向待删除节点的后一个节点
            for (int i = levelCount - 1; i >= 0; --i) {
                if (update[i].forwards[i] != null && update[i].forwards[i].data == value) {
                    update[i].forwards[i] = update[i].forwards[i].forwards[i];
                }
            }
            size--;
        }
        // 如果删除节点的层数大于跳表的最大层数，需要先将待删除节点的层数调整为跳表的最大层数
        // 删除节点之后，如果存在一些节点在删除节点之后成为了“孤儿节点”（即没有任何节点指向它），我们需要将这些孤儿节点从跳表中删除，以保持跳表的空间效率
        while (levelCount > 1 && head.forwards[levelCount] == null) {
            levelCount--;
        }

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

    public void printAll() {
        Node p = head;
        while (p.forwards[0] != null) {
            System.out.print(p.forwards[0] + " ");
            p = p.forwards[0];
        }
        System.out.println();
    }

    public static class Node {
        private int data = -1;
        /**
         * 表示当前节点位置的下一个节点所有层的数据，从上层切换到下层，就是数组下标-1，
         * forwards[3]表示当前节点在第三层的下一个节点。
         */
        private final Node[] forwards = new Node[MAX_LEVEL];
        private int maxLevel = 0;

        @Override
        public String toString() {
            return "{ data: " +
                    data +
                    "; levels: " +
                    maxLevel +
                    " }";
        }
    }
}
