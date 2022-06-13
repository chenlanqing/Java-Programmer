package com.algorithm.tree.balance;

import java.util.ArrayList;

/**
 * 平衡二分搜索树
 */
public class AVLTree<K extends Comparable<K>, V> {

    private class Node {

        public K key;
        public V value;
        public Node left, right;
        public int height;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
            left = null;
            right = null;
            this.height = 1;
        }
    }

    private Node root;
    private int size;

    public AVLTree() {
        root = null;
        size = 0;
    }

    public int getSize() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * 获得节点node的高度
     */
    private int getHeight(Node node) {
        if (node == null) {
            return 0;
        }
        return node.height;
    }

    /**
     * 获得节点node的平衡因子
     */
    private int getBalanceFactor(Node node) {
        if (node == null) {
            return 0;
        }
        return getHeight(node.left) - getHeight(node.right);
    }

    /**
     * 判断当前是否为二分搜索树：根据二分搜索特性，其中序遍历是顺序的
     */
    private boolean isBinarySearchTree(Node node) {
        if (node == null) {
            return true;
        }
        ArrayList<K> ret = new ArrayList<>();
        inOrder(node, ret);
        for (int i = 1; i < ret.size(); i++) {
            if (ret.get(i).compareTo(ret.get(i - 1)) < 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断一棵树是否为平衡二叉树：主要是判断每个节点的递归因子是否为：-1、1、0，即 Math.abs(平衡因子) > 1
     */
    private boolean isBalanceTree() {
        return isBalanceTree(root);
    }

    private boolean isBalanceTree(Node node) {
        if (node == null) {
            return true;
        }
        final int balanceFactor = getBalanceFactor(node);
        if (Math.abs(balanceFactor) > 1) {
            return false;
        }
        return isBalanceTree(node.left) && isBalanceTree(node.right);
    }

    private void inOrder(Node node, ArrayList<K> ret) {
        if (node == null) {
            return;
        }
        inOrder(node.left, ret);
        ret.add(node.key);
        inOrder(node.right, ret);
    }

    private Node getNode(Node node, K key) {
        if (node == null) {
            return null;
        }
        if (node.key.compareTo(key) > 0) {
            return getNode(node.left, key);
        } else if (node.key.compareTo(key) < 0) {
            return getNode(node.right, key);
        } else {
            return node;
        }
    }

    public boolean contains(K key) {
        return getNode(root, key) != null;
    }

    public V get(K key) {

        Node node = getNode(root, key);
        return node == null ? null : node.value;
    }

    public void set(K key, V newValue) {
        Node node = getNode(root, key);
        if (node == null) {
            throw new IllegalArgumentException(key + " doesn't exist!");
        }

        node.value = newValue;
    }


    // 向二分搜索树中添加新的元素(key, value)
    public void add(K key, V value) {
        root = add(root, key, value);
    }

    private Node add(Node node, K key, V value) {
        if (node == null) {
            size++;
            return new Node(key, value);
        }
        if (node.key.compareTo(key) > 0) {
            node.left = add(node.left, key, value);
        } else if (node.key.compareTo(key) < 0) {
            node.right = add(node.right, key, value);
        } else {
            node.value = value;
        }
        // 计算节点高度，取左右子树高度的最大值 + 1
        node.height = Math.max(getHeight(node.left), getHeight(node.right)) + 1;
//        // 计算节点的平衡因子
//        int balanceFactor = getBalanceFactor(node);
//
//        // 平衡维护：因为是递归，所以找到第一个 平衡因子绝对值大于1的节点，此时维护平衡，那么该节点的左右子树肯定是平衡二叉树的
//        // 1、当前节点平衡因子大于1，且其左子树平衡因子小于等于0
//        if (balanceFactor > 1 && getBalanceFactor(node.left) >= 0) {
//            return rightRotate(node);
//        }
//        // 2、当前节点平衡因子小于-1，且其右子树平衡因子小于等于0
//        if (balanceFactor < -1 && getBalanceFactor(node.right) <= 0) {
//            return leftRotate(node);
//        }
//        if (balanceFactor > 1 && getBalanceFactor(node.left) < 0) {
//            node.left = leftRotate(node.left);
//            return rightRotate(node);
//        }
//        if (balanceFactor < -1 && getBalanceFactor(node.right) > 0) {
//            node.right = rightRotate(node.right);
//            return leftRotate(node);
//        }
        return balanceTree(node);
    }

    // 对节点y进行向右旋转操作，返回旋转后新的根节点x
    //        y                              x
    //       / \                           /   \
    //      x   T4     向右旋转 (y)        z     y
    //     / \       - - - - - - - ->    / \   / \
    //    z   T3                       T1  T2 T3 T4
    //   / \
    // T1   T2
    private Node rightRotate(Node y) {
        Node x = y.left;
        Node T3 = x.right;

        // 向右旋转过程
        x.right = y;
        y.left = T3;

        // 更新height
        y.height = Math.max(getHeight(y.left), getHeight(y.right)) + 1;
        x.height = Math.max(getHeight(x.left), getHeight(x.right)) + 1;

        return x;
    }

    // 对节点y进行向左旋转操作，返回旋转后新的根节点x
    //    y                             x
    //  /  \                          /   \
    // T1   x      向左旋转 (y)       y     z
    //     / \   - - - - - - - ->   / \   / \
    //   T2  z                     T1 T2 T3 T4
    //      / \
    //     T3 T4
    private Node leftRotate(Node y) {
        Node x = y.right;
        Node T2 = x.left;

        // 向左旋转
        x.left = y;
        y.left = T2;

        // 更新height
        y.height = Math.max(getHeight(y.left), getHeight(y.right)) + 1;
        x.height = Math.max(getHeight(x.left), getHeight(x.right)) + 1;

        return x;
    }

    // 返回以node为根的二分搜索树的最小值所在的节点
    private Node minimum(Node node) {
        if (node.left == null) {
            return node;
        }
        return minimum(node.left);
    }

    // 从二分搜索树中删除键为key的节点
    public V remove(K key) {

        Node node = getNode(root, key);
        if (node != null) {
            root = remove(root, key);
            return node.value;
        }
        return null;
    }

    private Node remove(Node node, K key) {

        if (node == null) {
            return null;
        }

        Node retNode;
        if (key.compareTo(node.key) < 0) {
            node.left = remove(node.left, key);
            // return node;
            retNode = node;
        } else if (key.compareTo(node.key) > 0) {
            node.right = remove(node.right, key);
            // return node;
            retNode = node;
        } else {   // key.compareTo(node.key) == 0

            // 待删除节点左子树为空的情况
            if (node.left == null) {
                Node rightNode = node.right;
                node.right = null;
                size--;
                // return rightNode;
                retNode = rightNode;
            }

            // 待删除节点右子树为空的情况
            else if (node.right == null) {
                Node leftNode = node.left;
                node.left = null;
                size--;
                // return leftNode;
                retNode = leftNode;
            }

            // 待删除节点左右子树均不为空的情况
            else {
                // 找到比待删除节点大的最小节点, 即待删除节点右子树的最小节点
                // 用这个节点顶替待删除节点的位置
                Node successor = minimum(node.right);
                //successor.right = removeMin(node.right);
                successor.right = remove(node.right, successor.key);
                successor.left = node.left;

                node.left = node.right = null;

                // return successor;
                retNode = successor;
            }
        }

        if (retNode == null) {
            return null;
        }

        // 更新height
        retNode.height = 1 + Math.max(getHeight(retNode.left), getHeight(retNode.right));

//        // 计算平衡因子
//        int balanceFactor = getBalanceFactor(retNode);
//
//        // 平衡维护
//        // LL
//        if (balanceFactor > 1 && getBalanceFactor(retNode.left) >= 0)
//            return rightRotate(retNode);
//
//        // RR
//        if (balanceFactor < -1 && getBalanceFactor(retNode.right) <= 0)
//            return leftRotate(retNode);
//
//        // LR
//        if (balanceFactor > 1 && getBalanceFactor(retNode.left) < 0) {
//            retNode.left = leftRotate(retNode.left);
//            return rightRotate(retNode);
//        }
//
//        // RL
//        if (balanceFactor < -1 && getBalanceFactor(retNode.right) > 0) {
//            retNode.right = rightRotate(retNode.right);
//            return leftRotate(retNode);
//        }

        return balanceTree(node);
    }

    private Node balanceTree(Node node) {
        // 计算节点的平衡因子
        int balanceFactor = getBalanceFactor(node);

        // 平衡维护：因为是递归，所以找到第一个 平衡因子绝对值大于1的节点，此时维护平衡，那么该节点的左右子树肯定是平衡二叉树的
        // LL：当前节点平衡因子大于1，且其左子树平衡因子小于等于0
        if (balanceFactor > 1 && getBalanceFactor(node.left) >= 0) {
            return rightRotate(node);
        }
        // RR：当前节点平衡因子小于-1，且其右子树平衡因子小于等于0
        if (balanceFactor < -1 && getBalanceFactor(node.right) <= 0) {
            return leftRotate(node);
        }
        // LR
        if (balanceFactor > 1 && getBalanceFactor(node.left) < 0) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }
        // RL
        if (balanceFactor < -1 && getBalanceFactor(node.right) > 0) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }
        return node;
    }
}
