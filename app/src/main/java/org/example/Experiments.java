package org.example;

import java.util.ArrayList;
import java.util.List;

public class Experiments {
    public static void main(String[] args) {
        // List<Integer> list = new ArrayList<>();
        // list.add(1);
        Integer[] array = new Integer[]{5, 2, 10, 4, 7};
        SortedArrayBinarySearch<Integer> binarySearch = new SortedArrayBinarySearch<>(array, 0.2f);
        binarySearch.printList();
        System.out.println("Pred: " + binarySearch.pred(-5));
    }
}
