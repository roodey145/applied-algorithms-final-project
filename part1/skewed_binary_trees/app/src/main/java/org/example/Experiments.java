package org.example;

import java.util.ArrayList;
import java.util.List;

public class Experiments {
    public static void main(String[] args) {
        // List<Integer> list = new ArrayList<>();
        // list.add(1);

        Integer[] array = new Integer[] { -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15 };
        OtherArrayBinarySearch sortedArray = new OtherArrayBinarySearch(array, 0.7f);

        // sortedArray.printTree();

        System.out.println(sortedArray.pred(-1)); // null
        System.out.println(sortedArray.pred(0)); // -1
        System.out.println(sortedArray.pred(5)); // 4
        System.out.println(sortedArray.pred(10)); // 9
        System.out.println(sortedArray.pred(16)); // 15

        // Tested: Working
        // Integer[] array = new Integer[]{5, 2, 10, 4, 7};
        // SortedArrayBinarySearch<Integer> binarySearch = new
        // SortedArrayBinarySearch<>(array, 0.2f);
        // binarySearch.printList();
        // System.out.println("Pred: " + binarySearch.pred(-5));
    }
}
