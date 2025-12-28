package org.example;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.TreeSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;

public class KArrayBinarySearchTest {

    private Integer referencePred(List<Integer> sortedList, int x) {
        boolean equalFound = false;
        // Reference: linear scan from the right
        for (int i = sortedList.size() - 1; i >= 0; i--) {
            if (sortedList.get(i) < x) {
                return sortedList.get(i);
            } else if (sortedList.get(i) == x) {
                equalFound = true;
            }
        }
        return equalFound ? x : null;
    }

    @Test
    public void testPredSmallOutboundriesElement() {
        int[] array = new int[] { -1, 3, 4, 5, 10, 15 };
        KArrayBinarySearch sortedArray = new KArrayBinarySearch(array, 0.9f, 2);

        // sortedArray.printTree();

        assertEquals((Integer) 4, sortedArray.pred(5));
        System.out.println(sortedArray.pred(-2));
        assertNull(sortedArray.pred(-2));
    }

    @Test
    public void testSingleElement() {
        int[] arr = { 5 };
        KArrayBinarySearch tree = new KArrayBinarySearch(arr, 0.3f, 2);

        assertEquals((Integer) 5, tree.pred(5));
        assertEquals((Integer) 5, tree.pred(10));
        assertNull(tree.pred(2));
    }

    @Test
    public void testSimpleFixedValues() {
        int[] arr = { 1, 5, 10, 20 };
        KArrayBinarySearch tree = new KArrayBinarySearch(arr, 0.5f, 2);

        assertNull(tree.pred(0));
        assertEquals((Integer) 1, tree.pred(1));
        assertEquals((Integer) 10, tree.pred(11));
        assertEquals((Integer) 20, tree.pred(25));
    }

    @Test
    public void testPredAtExactMatches() {
        int[] arr = { 2, 4, 6, 8 };
        KArrayBinarySearch tree = new KArrayBinarySearch(arr, 0.7f, 2);

        assertEquals((Integer) 2, tree.pred(2));
        assertEquals((Integer) 2, tree.pred(4));
        assertEquals((Integer) 6, tree.pred(8));
    }

    @Test
    public void testLargeCase_1() {
        // Changed size for faster build
        // original size = 10_000_000
        int size = 10_000;
        int testMargin = 10;
        // Create an int array where the values -size to +size is added
        int[] arr = new int[size * 2 + 1];

        for (int i = -size; i <= size; i++) {
            arr[i + size] = i;
        }

        for (float alpha = 0.1f; alpha <= 0.9f; alpha += 0.1f) {
            // Alpha shpuld be added dynamically
            KArrayBinarySearch tree = new KArrayBinarySearch(arr, alpha, 2);

            for (int i = -(size + testMargin); i <= (size + testMargin); i++) {
                if (i < -size) { // i is less than the smallest number in the tree, i.e. null should be returned
                    assertNull(tree.pred(i));
                } else if (i == -size) { // This is the first element, i.e. -size should be returned
                    assertEquals((Integer) i, tree.pred(i));
                } else if (i > -size && i <= size) { // The i-1 exists in the tree, i.e. i-1 should be returned
                    assertEquals((Integer) (i - 1), tree.pred(i));
                } else { // i is larger than the largest element in the tree, i.e. size should be
                         // returned
                    assertEquals((Integer) size, tree.pred(i));
                }
            }
        }
    }

    @Test
    public void testRandomizedCases() {
        Random rand = new Random(42);
        // Changed setMaxSize for faster build
        // original setMaxSize = 10_000
        int setMaxSize = 1000;
        int extraMargin = 100;

        for (int size = 1; size < setMaxSize; size++) {

            // generate random input set
            TreeSet<Integer> set = new TreeSet<>();
            while (set.size() < size)
                set.add(rand.nextInt(0, Integer.MAX_VALUE)); // adding elements to the TreeSet

            Integer[] arr = set.toArray(Integer[]::new); // passing the set elements to an array
            int[] intArr = new int[arr.length];
            for (int i = 0; i < arr.length; i++) {
                intArr[i] = arr[i];
            }

            float alpha = rand.nextFloat(); // rand for alpha
            KArrayBinarySearch tree = new KArrayBinarySearch(intArr, alpha, 2); // building tree

            List<Integer> sortedList = Arrays.asList(arr); // passing this to a list to test it with the referencePred
                                                           // function

            // test all possible values in range
            for (int x = -50; x <= size + extraMargin; x++) { // from -50 to -1 it must always return null
                assertEquals(referencePred(sortedList, x), tree.pred(x));
            }
        }
    }
}
