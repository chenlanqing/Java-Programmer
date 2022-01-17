package com.algorithm.tree.trie;

import java.util.TreeMap;

/**
 * 字典树的实现
 *
 * @author QingFan
 * @version 1.0.0
 * @date 2022年01月17日 09:34
 */
public class Trie {

    private final Node root;
    private int size;

    public Trie() {
        this.root = new Node();
        this.size = 0;
    }

    public int getSize() {
        return size;
    }

    /**
     * 往字典树添加单词
     */
    public void add(String word) {
        Node cur = root;
        int len = word.length();
        for (int i = 0; i < len; i++) {
            char c = word.charAt(i);
            if (cur.next.get(c) == null) {
                cur.next.put(c, new Node());
            }
            cur = cur.next.get(c);
        }
        // 判断当前cur是否为单词，如果不是，则需要将cur的isWord设置为true，然后size+1
        if (!cur.isWord) {
            cur.isWord = true;
            size++;
        }
    }

    /**
     * 判断字典树是否包含某个字符串
     */
    public boolean contains(String word) {
        Node cur = root;
        int len = word.length();
        for (int i = 0; i < len; i++) {
            char c = word.charAt(i);
            if (cur.next.get(c) == null) {
                return false;
            }
            cur = cur.next.get(c);
        }
        return cur.isWord;
    }

    /**
     * 字典树前缀匹配：查看trie是否以 prefix 为前缀
     */
    public boolean isPrefix(String prefix) {
        Node cur = root;
        int len = prefix.length();
        for (int i = 0; i < len; i++) {
            char c = prefix.charAt(i);
            if (cur.next.get(c) == null) {
                return false;
            }
            cur = cur.next.get(c);
        }
        return true;
    }

    /**
     * 字典树模式匹配：比如包含"."，其表示可以匹配任意一个字符
     */
    public boolean match(String word) {
        return match(root, word, 0);
    }

    private boolean match(Node node, String word, int index) {
        if (index == word.length()) {
            return node.isWord;
        }
        char c = word.charAt(index);
        if (c == '.') {
            for (Character nextChar : node.next.keySet()) {
                if (match(node.next.get(nextChar), word, index + 1)) {
                    return true;
                }
            }
            return false;
        } else {
            if (node.next.get(c) == null) {
                return false;
            }
            return match(node.next.get(c), word, index + 1);
        }
    }

    private class Node {

        public boolean isWord;
        public TreeMap<Character, Node> next;

        public Node(boolean isWord) {
            next = new TreeMap<>();
            this.isWord = isWord;
        }

        public Node() {
            this(false);
        }
    }
}
