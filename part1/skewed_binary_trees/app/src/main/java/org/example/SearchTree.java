package org.example;

import java.util.Arrays;
import java.util.LinkedList;

public class SearchTree implements SkewedBinarySearchTree {
    private int[] tree;
    private final float alpha;
    private Node root;

    public SearchTree(int[] array, float alpha) {
        this.tree = Arrays.copyOf(array, array.length);
        Arrays.sort(tree);
        this.alpha = alpha;
        this.root = buildSkewedTree(0, this.tree.length);

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

    // Simple method to ensure that the constructed binary tree contains the same
    // elements
    public int[] nodeToSortedArray() {
        LinkedList<Node> queue = new LinkedList<>();
        int[] arr = new int[this.tree.length];
        int i = 0;
        queue.add(this.root);
        while (!queue.isEmpty()) {
            Node curr = queue.pollFirst();
            arr[i++] = curr.key;
            if (curr.left != null) {
                queue.add(curr.left);
            }
            if (curr.right != null) {
                queue.add(curr.right);
            }
        }
        Arrays.sort(arr);
        assert (Arrays.equals(this.tree, arr));
        return arr;

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
        Node node = new Node(this.tree[root_index]);
        node.left = buildSkewedTree(start, root_index);
        node.right = buildSkewedTree(root_index + 1, end);
        return node;
    }
}
