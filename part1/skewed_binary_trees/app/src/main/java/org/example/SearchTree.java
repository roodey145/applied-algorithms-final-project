package org.example;

import java.util.Arrays;

public class SearchTree {
    private int[] a;
    private final float alpha;
    private Node root;

    public SearchTree(int[] array, float alpha) {
        this.a = Arrays.copyOf(array, array.length);
        Arrays.sort(a);
        this.alpha = alpha;
        this.root = buildSkewedTree(0, this.a.length);

    }

    private class Node {
        private int key;
        private Node left, right;

        public Node(int key) {
            this.key = key;
        }

        public String toString() {
            return String.valueOf(this.key);
        }
    }

    public Integer pred(int x) {
        Node temp = this.root;
        Integer pred = null;
        while (temp != null) {

            if (x == temp.key)
                return temp.key;

            if (x < temp.key) {
                temp = temp.left;
            } else {
                pred = temp.key;
                temp = temp.right;
            }
        }
        return pred;
    }

    private Node buildSkewedTree(int start, int end) {
        if (start >= end) {
            return null;
        }
        // The left subtree must contain alpha-n elements, by doing this recursively
        // We ensure each subtree upholds the same rule
        int n = end - start;
        int left_size = (int) (this.alpha * n);
        int root_index = start + left_size;
        Node node = new Node(this.a[root_index]);
        node.left = buildSkewedTree(start, root_index);
        node.right = buildSkewedTree(root_index + 1, end);
        return node;
    }
}
