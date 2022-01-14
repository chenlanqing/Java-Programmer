package com.algorithm.tree.heap;

/**
 * 使用数组实现最大堆
 */
@SuppressWarnings("unchecked")
public class MaxHeap<E extends Comparable<E>> {

    private static final int DEFAULT_INITIAL_CAPACITY = 16;

    private Array<E> data;

    public MaxHeap() {
        this(DEFAULT_INITIAL_CAPACITY);
    }

    public MaxHeap(int capacity) {
        data = new Array<>(capacity);
    }

    /**
     * 将一个数组构建为二叉堆，heapify，需要确认最后一个非叶子节点：数组的最后一个值的父节点的索引就是最后一个非叶子节点
     */
    public MaxHeap(E[] arr) {
        // 一个数组构建为二叉堆
        data = new Array<>(arr);
        if(arr.length != 1){
            // 数组的最后一个值的父节点的索引就是最后一个非叶子节点
            for(int i = parent(arr.length - 1) ; i >= 0 ; i --)
                shiftDown(i);
        }
    }

    public int size() {
        return data.getSize();
    }

    public boolean isEmpty() {
        return data.isEmpty();
    }

    public void add(E e) {
        data.addLast(e);
        shiftUp(data.getSize() - 1);
    }

    public E max() {
        if (isEmpty()) {
            throw new IllegalArgumentException("heap is empty");
        }
        return data.get(0);
    }

    public E extractMax() {
        E e = max();
        // 取巧：将最后一个元素替换为根节点
        data.swap(0, data.getSize() - 1);
        // 将最后一个元素删掉
        data.remove(data.getSize() - 1);
        // 然后再下沉操作
        shiftDown(0);

        return e;
    }

    /**
     * 取出最大元素，并替换
     */
    public E replace(E e) {
        E res = max();
        data.set(e, 0);
        shiftDown(0);
        return res;
    }

    private void shiftDown(int index) {
        while (leftChild(index) < data.getSize()) {
            // 完全二叉树：如有有右子树，肯定有左子树；
            int j = leftChild(index);
            // 判断是否有右子树，且右子树的值大于左子树
            if (j + 1 < data.getSize() && data.get(j + 1).compareTo(data.get(j)) > 0) {
                j++;
            }
            // data[j] 是 leftChild 和 rightChild 中的最大值
            if (data.get(index).compareTo(data.get(j)) >= 0) {
                break;
            }
            data.swap(index, j);
            index = j;
        }
    }

    private void shiftUp(int index) {
        // 上浮，只需要判断当前节点与父子节点的关系：如果比父节点都大，则直接交换，因为父节点肯定是比左右子树的节点的都大的
        while (index > 0 && data.get(index).compareTo(data.get(parent(index))) > 0) {
            data.swap(parent(index), index);
            index = parent(index);
        }
    }

    /**
     * 返回完全二叉树的数组表示中，一个索引所表示的元素的父亲节点的索引
     */
    private int parent(int index) {
        if (index == 0) {
            throw new IllegalStateException("illegal index");
        }
        return (index - 1) / 2;
    }

    /**
     * 返回完全二叉树的数组表示中，一个索引所表示的元素的左孩子节点的索引
     */
    private int leftChild(int index) {
        return index * 2 + 1;
    }

    /**
     * 返回完全二叉树的数组表示中，一个索引所表示的元素的右孩子节点的索引
     */
    private int rightChild(int index) {
//        return index * 2 + 2;
        return leftChild(index) + 1;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for (int i = 0; i < data.getSize(); i++) {
            sb.append(data.get(i));
            if (i < data.getSize() - 1) {
                sb.append(",");
            }
        }
        sb.append("}");
        return sb.toString();
    }

    private static class Array<E> {

        private E[] data;
        private int size;

        public Array() {
            this(DEFAULT_INITIAL_CAPACITY);
        }

        public Array(int capacity) {
            data = (E[]) new Object[capacity];
            size = 0;
        }
        public Array(E[] arr){
            data = (E[])new Object[arr.length];
            System.arraycopy(arr, 0, data, 0, arr.length);
            size = arr.length;
        }

        int getSize() {
            return size;
        }

        boolean isEmpty() {
            return size == 0;
        }

        void add(E e, int index) {
            if (index < 0 || index > size) {
                throw new IllegalArgumentException("Add failed. Require index >= 0 and index <= size.");
            }
            if (size == data.length) {
                ensureCapacity(size << 1);
            }
            for (int i = size - 1; i >= index; i--) {
                data[i + 1] = data[i];
            }

            data[index] = e;
            size++;
        }

        void addLast(E e) {
            add(e, size);
        }

        void addFirst(E e) {
            add(e, 0);
        }

        E get(int index) {
            if (index < 0 || index > size) {
                throw new IllegalArgumentException("Add failed. Require index >= 0 and index <= size.");
            }
            return data[index];
        }

        void set(E e, int index) {
            if (index < 0 || index > size) {
                throw new IllegalArgumentException("Add failed. Require index >= 0 and index <= size.");
            }
            data[index] = e;
        }

        boolean contains(E e) {
            for (int i = 0; i < size; i++) {
                if (data[i].equals(e)) {
                    return true;
                }
            }
            return false;
        }

        E remove(int index) {
            if (index < 0 || index > size) {
                throw new IllegalArgumentException("Add failed. Require index >= 0 and index <= size.");
            }
            E e = data[index];
            for (int i = index + 1; i < size; i++) {
                data[i - 1] = data[i];
            }
            size--;
            data[size] = null;
            if (size <= data.length / 4 && data.length / 2 != 0) {
                ensureCapacity(data.length / 2);
            }
            return e;
        }

        void swap(int i, int j) {
            if (i < 0 || i > size || j < 0 || j > size) {
                throw new IllegalArgumentException("Out of bound");
            }
            E temp = data[i];
            data[i] = data[j];
            data[j] = temp;
        }

        void ensureCapacity(int newCapacity) {
            E[] newData = (E[]) new Object[newCapacity];
            if (size >= 0) {
                System.arraycopy(data, 0, newData, 0, size);
            }
            data = newData;
        }
    }
}
