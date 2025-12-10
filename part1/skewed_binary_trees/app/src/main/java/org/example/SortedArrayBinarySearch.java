package org.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SortedArrayBinarySearch<T extends Comparable<T>> implements BinarySearchTree<T> {

    private final List<T> list;
    private final float alpha;

    public SortedArrayBinarySearch(T[] array, float alpha) {
        this.list = new ArrayList<>();
        Collections.addAll(this.list, array);
        Collections.sort(this.list);

        this.alpha = alpha > 1 ? 1 : (alpha < 0 ? 0 : alpha);
    }

    @Override
    public T pred(T x) {
        int index = getIndex(x, 0, list.size() - 1, -1);
        if (index < 0)
            return null;
        else
            return list.get(index);
    }

    private int getIndex(T x, int low, int high, int prevIndex) {
        // System.out.println("low: " + low + ", high: " + high + ", PrevIndex " +
        // prevIndex);
        /*
         * if (low > high && x.compareTo(list.get(prevIndex)) > 0) {
         * System.out.println("X is larger than " + list.get(prevIndex) + ", " +
         * x.compareTo(list.get(prevIndex)));
         * return prevIndex;
         * } else
         */if (low > high) {
            int predIndex = -1; // high;
            for (int i = prevIndex; i >= 0; i--) {
                if (x.compareTo(list.get(i)) > 0) {
                    // We found the first element that is less than x
                    predIndex = i;
                    break;
                }
            }
            return predIndex;
        }

        int index = low + (int) ((high - low) * alpha);
        T selected = list.get(index);
        // System.out.println("Selected: " + selected + " compared to: " + x + " is " +
        // selected.compareTo(x));
        if (selected.compareTo(x) < 0)
            return getIndex(x, index + 1, high, index);
        else {
            return getIndex(x, low, index - 1, index);
        }
    }

    public void printList() {
        for (T t : list) {
            System.out.println(t);
        }
    }

    public T[] getArray(T[] tArray) {
        return list.toArray(tArray);
    }
}
