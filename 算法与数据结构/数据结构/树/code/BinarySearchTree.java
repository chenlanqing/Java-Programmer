import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

/**
 * 二分搜索树
 */
public class BinarySearchTree<E extends Comparable<E>> {

    private class Node {

        public E e;
        public Node left, right;

        public Node(E e) {
            this.e = e;
            left = null;
            right = null;
        }
    }

    private Node root;
    private int size;

    public BinarySearchTree() {
        this.root = null;
        this.size = 0;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void add(E e) {
        root = add(root, e);
    }
    /**
     * 递归算法，返回插入新节点后二分搜索树的根
     */
    private Node add(Node node, E e) {
        if (node == null) {
            size++;
            return new Node(e);
        }
        if (e.compareTo(node.e) < 0) {
            node.left = add(node.left, e);
        } else if (e.compareTo(node.e) > 0) {
            node.right = add(node.right, e);
        }
        return node;
    }

    /*
    // 下面写法太囊肿了
    public void add(Node node, E e) {
        if (e.equals(node.e)) {
            return;
        }
        // 左子树是否为空
        if (e.compareTo(node.e) < 0 && node.left == null) {
            node.left = new Node(e);
            size++;
            return;
        }
        // 右子树是否为空
        if (e.compareTo(node.e) > 0 && node.right == null) {
            node.right = new Node(e);
            size++;
            return;
        }
        if (e.compareTo(node.e) < 0) {
            add(node.left, e);
        } else {
            add(node.right, e);
        }
    }
     */
    public boolean contains(E e) {
        return contains(root, e);
    }

    private boolean contains(Node node, E e) {
        if (node == null) {
            return false;
        }
        if (e.compareTo(node.e) == 0) {
            return true;
        } else if (e.compareTo(node.e) < 0) {
            return contains(node.left, e);
        } else {
            return contains(node.right, e);
        }
    }

    /**
     * 前序遍历
     */
    public void preOrder() {
        preOrder(root);
    }

    private void preOrder(Node root) {
        if (root == null) {
            return;
        }
        System.out.print(root.e + "\t");
        preOrder(root.left);
        preOrder(root.right);
    }

    /**
     * 使用栈来实现非递归前序遍历
     */
    public void preOrderNotRecursive() {
        if (root == null) {
            return;
        }
        Stack<Node> stack = new Stack<>();
        stack.push(root);

        while (!stack.isEmpty()) {
            final Node node = stack.pop();
            System.out.print(node.e + "\t");
            if (node.right != null) {
                stack.push(node.right);
            }
            if (node.left != null) {
                stack.push(node.left);
            }
        }
    }

    /**
     * 中序遍历
     */
    public void inOrder() {
        inOrder(root);
    }

    private void inOrder(Node root) {
        if (root == null) {
            return;
        }
        inOrder(root.left);
        System.out.print(root.e + "\t");
        inOrder(root.right);
    }

    /**
     * 非递归实现
     */
    public void inOrderNotRecursive() {
        if (root == null) {
            return;
        }
        Stack<Node> stack = new Stack<>();
        Node cur = root;

        while (cur != null || !stack.isEmpty()) {
            while (cur != null) {
                stack.push(cur);
                cur = cur.left;
            }
            cur = stack.pop();
            System.out.print(cur.e + "\t");
            cur = cur.right;
        }
    }

    /**
     * 后序遍历
     */
    public void postOrder() {
        postOrder(root);
    }

    private void postOrder(Node root) {
        if (root == null) {
            return;
        }
        postOrder(root.left);
        postOrder(root.right);
        System.out.print(root.e + "\t");
    }

    public void postOrderNotRecursive() {
        if (root == null) {
            return;
        }
        Stack<Node> stack = new Stack<>();
        Node cur = root;
        Node last = null;
        while (cur != null || !stack.isEmpty()) {
            while (cur != null) {
                stack.push(cur);
                cur = cur.left;
            }
            cur = stack.peek();
            if (cur.right == null || cur.right == last) {
                System.out.print(cur.e + "\t");
                stack.pop();
                // 记录上一个访问的节点
                // 用于判断“访问根节点之前，右子树是否已访问过”
                last = cur;
                cur = null;
            } else {
                cur = cur.right;
            }
        }
    }

    public void levelOrder() {
        if (root == null) {
            return;
        }
        Queue<Node> queue = new LinkedList<>();
        queue.add(root);

        while (!queue.isEmpty()) {
            Node node = queue.poll();
            System.out.print(node.e + "\t");
            if (node.left != null) {
                queue.add(node.left);
            }
            if (node.right != null) {
                queue.add(node.right);
            }
        }
    }

    /**
     * 找出二分搜索树的最大值
     */
    public E max() {
        if (size == 0) {
            throw new IllegalStateException("tree empty");
        }
        return max(root).e;
    }

    private Node max(Node node) {
        if (node.right == null) {
            return node;
        }
        return max(node.right);
    }

    public E removeMax() {
        E res = max();
        root = removeMax(root);
        return res;
    }

    private Node removeMax(Node node) {
        if (node.right == null) {
            Node leftNode = node.left;
            node.left = null;
            size--;
            return leftNode;
        }
        node.right = removeMax(node.right);
        return node;
    }

    /**
     * 找出二分搜索树的最小值
     */
    public E min() {
        if (size == 0) {
            throw new IllegalStateException("tree empty");
        }
        return min(root).e;
    }

    private Node min(Node node) {
        if (node.left == null) {
            return node;
        }
        return min(node.left);
    }

    public E removeMin() {
        E res = min();
        root = removeMin(root);
        return res;
    }

    private Node removeMin(Node node) {
        if (node.left == null) {
            Node rightNode = node.right;
            node.right = null;
            size--;
            return rightNode;
        }
        node.left = removeMin(node.left);
        return node;
    }

    /**
     * 删除指定的元素
     */
    public void remove(E e) {
        root = remove(root, e);
    }

    private Node remove(Node node, E e) {
        if (node == null) {
            return null;
        }
        if (e.compareTo(node.e) < 0) {
            node.left = remove(node.left, e);
            return node;
        } else if (e.compareTo(node.e) > 0) {
            node.right = remove(node.right, e);
            return node;
        } else {
            // 待删除节点左子树为空的情况
            if (node.left == null) {
                Node rightNode = node.right;
                node.right = null;
                size--;
                return rightNode;
            }
            // 待删除节点右子树为空的情况
            if (node.right == null) {
                Node leftNode = node.left;
                node.left = null;
                size--;
                return leftNode;
            }
            // node节点的左右子树都不为空
            // 找到比待删除节点大的最小节点, 即待删除节点右子树的最小节点
            // 用这个节点顶替待删除节点的位置
            Node successor = min(node.right);
            successor.right = removeMin(node.right);
            successor.left = node.left;
            node.left = node.right = null;
            // 或者还有一种写法，找到比待删除节点小的最大节点，即待删除节点左子树的最大节点，用这个节点顶替待删除节点的位置
            /*
            Node successor = max(node.left);
            successor.left = removeMax(node.left);
            successor.right = node.right;
            node.left = node.right = null;
            */
            return successor;
        }
    }

    public int heightOf() {
        return heightOf(root);
    }

    private int heightOf(Node node) {
        if (node == null) {
            return 0;
        }
        return Math.max(
                heightOf(node.left),
                heightOf(node.right)
        ) + 1;
    }

    public void print() {
        int h = heightOf(root);
        int W = 2 * (int) Math.pow(2, h);
        var lines = new StringBuilder[h * 2];
        for (int i = 0; i < h * 2; i++) {
            lines[i] = new StringBuilder(String.format("%" + W + "s", ""));
        }

        printNode(lines, W, root, 0, 0);
        for (var line : lines) {
            System.out.println(line.toString());
        }

    }

    private void printNode(StringBuilder[] lines, int W, Node node, int h, int base) {
        var nums = Math.pow(2, h);
        var pos = base + (int) (W / (nums * 2));

        var str = node.e.toString();
        for (int i = 0; i < str.length(); i++) {
            lines[h * 2].setCharAt(pos + i, str.charAt(i));
        }

        if (node.left != null) {
            lines[h * 2 + 1].setCharAt(pos - 1, '/');
            printNode(lines, W, node.left, h + 1, base);
        }

        if (node.right != null) {
            lines[h * 2 + 1].setCharAt(pos + str.length() + 1, '\\');
            printNode(lines, W, node.right, h + 1, pos);
        }
    }
}
