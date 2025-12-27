package org.example;

import java.util.Arrays;

public class SortedArrayBinarySearch implements SkewedBinarySearchTree {

    private int[] tree;
    private final float alpha;

    public SortedArrayBinarySearch(int[] array, float alpha) {
        this.tree = Arrays.copyOf(array, array.length);
        Arrays.sort(this.tree);

        this.alpha = alpha > 1 ? 1 : (alpha < 0 ? 0 : alpha);
    }

    public Integer pred(int x) {
        int index = findPredecessorIndex(x);
        return index < 0 ? null : this.tree[index];
    }

    private int findPredecessorIndex(int x) {
        int low = 0;
        int high = this.tree.length - 1;
        int predIndex = -1;

        while (low <= high) {
            int mid = low + (int) ((high - low) * alpha);
            if (tree[mid] == x) {
                // If its equal thats the best match return the index
                return mid;
            }
            if (this.tree[mid] < x) {
                // This could be a potential pred save it and look if there's a better one at a
                // greater index
                predIndex = mid;
                low = mid + 1;
            } else {
                // If its greater then look to the left
                high = mid - 1;
            }
        }
        // Wil return -1 if all array items are greater than x
        return predIndex;
    }

    // private int getIndex(T x, int low, int high, int prevIndex) {
    // // System.out.println("low: " + low + ", high: " + high + ", PrevIndex " +
    // // prevIndex);
    // /*
    // * if (low > high && x.compareTo(list.get(prevIndex)) > 0) {
    // * System.out.println("X is larger than " + list.get(prevIndex) + ", " +
    // * x.compareTo(list.get(prevIndex)));
    // * return prevIndex;
    // * } else
    // */if (low > high) {
    // int predIndex = -1; // high;
    // for (int i = prevIndex; i >= 0; i--) {
    // if (x.compareTo(list.get(i)) > 0) {
    // // We found the first element that is less than x
    // predIndex = i;
    // break;
    // }
    // }
    // return predIndex;
    // }

    // int index = low + (int) ((high - low) * alpha);
    // T selected = list.get(index);
    // // System.out.println("Selected: " + selected + " compared to: " + x + " is "
    // +
    // // selected.compareTo(x));
    // if (selected.compareTo(x) < 0)
    // return getIndex(x, index + 1, high, index);
    // else {
    // return getIndex(x, low, index - 1, index);
    // }
    // }

    public void printList() {
        for (int t : this.tree) {
            System.out.println(t);
        }
    }

    public int[] getArray() {
        return this.tree;
    }
}
