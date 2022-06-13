package com.algorithm.tree.segment;

/**
 * 使用数组实现线段树
 */
@SuppressWarnings("unchecked")
public class SegmentTree<E> {

    private final E[] data;
    private final E[] tree;
    private final Merger<E> merger;

    public SegmentTree(E[] arr, Merger<E> merger) {
        this.merger = merger;

        final int len = arr.length;
        data = (E[]) new Object[len];
        System.arraycopy(arr, 0, data, 0, len);
        tree = (E[]) new Object[4 * len];
        buildSegmentTree(0, 0, len - 1);
    }

    private void buildSegmentTree(int treeIndex, int l, int r) {
        if (l == r) {
            tree[treeIndex] = data[l];
            return;
        }
        int leftTree = leftChild(treeIndex);
        int rightTree = rightChild(treeIndex);

        int mid = l + (r - l) / 2;
        buildSegmentTree(leftTree, l, mid);
        buildSegmentTree(rightTree, mid + 1, r);

        tree[treeIndex] = merger.merge(tree[leftTree], tree[rightTree]);
    }

    public int size() {
        return data.length;
    }

    public E get(int index) {
        if (index < 0 || index >= data.length) {
            throw new IllegalArgumentException("Index is illegal.");
        }
        return data[index];
    }

    // 返回完全二叉树的数组表示中，一个索引所表示的元素的左孩子节点的索引
    private int leftChild(int index) {
        return 2 * index + 1;
    }

    // 返回完全二叉树的数组表示中，一个索引所表示的元素的右孩子节点的索引
    private int rightChild(int index) {
        return 2 * index + 2;
    }
    // 返回区间[queryL, queryR]的值
    public E query(int queryL, int queryR) {
        if (queryL < 0 || queryL >= size() || queryR < 0 || queryR >= size()) {
            throw new IllegalArgumentException("index illegal");
        }
        return query(0, 0, data.length - 1, queryL, queryR);
    }
    // 在以treeIndex为根的线段树中[l...r]的范围里，搜索区间[queryL...queryR]的值
    private E query(int treeIndex, int l, int r, int queryL, int queryR) {
        if (l == queryL && r == queryR) {
            return tree[treeIndex];
        }
        int mid = l + (r - l) / 2;
        int leftIndex = leftChild(treeIndex);
        int rightIndex = rightChild(treeIndex);

        if (queryL >= mid + 1) {
            return query(rightIndex, mid + 1, r, queryL, queryR);
        } else if (queryR <= mid) {
            return query(leftIndex, l, mid, queryL, queryR);
        }
        E leftResult = query(leftIndex, l, mid, queryL, mid);
        E rightResult = query(rightIndex, mid + 1, r, mid + 1, queryR);

        return merger.merge(leftResult, rightResult);
    }
    // 将index位置的值，更新为e
    public void set(int index, E e) {
        if (index < 0 || index >= data.length) {
            throw new IllegalArgumentException("Index is illegal.");
        }
        data[index] = e;
        set(0, 0, data.length - 1, index, e);
    }

    /**
     * 在以treeIndex为根的线段树中更新index的值为e
     */
    private void set(int treeIndex, int l, int r, int index, E e) {
        if (l == r) {
            tree[treeIndex] = e;
            return;
        }
        // treeIndex的节点分为[l...mid]和[mid+1...r]两部分
        int mid = l + (r - l) / 2;
        int leftIndex = leftChild(treeIndex);
        int rightIndex = rightChild(treeIndex);

        if (index >= mid + 1) {
            set(rightIndex, mid + 1, r, index, e);
        } else {
            set(leftIndex, l, mid, index, e);
        }
        tree[treeIndex] = merger.merge(tree[leftIndex], tree[rightIndex]);
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();
        res.append('[');
        for (int i = 0; i < tree.length; i++) {
            if (tree[i] != null) {
                res.append(tree[i]);
            } else {
                res.append("null");
            }
            if (i != tree.length - 1) {
                res.append(", ");
            }
        }
        res.append(']');
        return res.toString();
    }
    // 以何种逻辑构建线段树节点
    public interface Merger<E> {
        E merge(E a, E b);
    }
}
