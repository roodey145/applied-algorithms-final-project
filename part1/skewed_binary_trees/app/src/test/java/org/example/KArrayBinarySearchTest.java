package org.example;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;

public class KArrayBinarySearchTest {

    private Integer referencePred(List<Integer> sortedList, int x) {
        // Reference: linear scan from the right
        for (int i = sortedList.size() - 1; i >= 0; i--) {
            if (sortedList.get(i) <= x) {
                return sortedList.get(i);
            }
        }
        return null;
    }

    @Test
    public void testPredSmallOutboundriesElement(){
        int[] array = new int[] {1, 3, 4, 5, 10, 15};
        KArrayBinarySearch sortedArray = new KArrayBinarySearch(array, 0.9f, 2);

        sortedArray.printTree();

        assertEquals((Integer)4, sortedArray.pred(5));
        System.out.println(sortedArray.pred(-2));
        assertNull(sortedArray.pred(-2));
        // assertEquals(null, sortedArray.pred(-10));
        // assertEquals(null, sortedArray.pred(-1000));
        // assertEquals(null, sortedArray.pred(-114));
        // assertEquals(null, sortedArray.pred(-1214214));
    }

    @Test
    public void testSingleElement() {
        int[] arr = {5};
        KArrayBinarySearch tree = new KArrayBinarySearch(arr, 0.3f, 2);

        assertEquals((Integer)5, tree.pred(5));
        assertEquals((Integer)5, tree.pred(10));
        assertNull(tree.pred(2));
    }

    @Test
    public void testSimpleFixedValues() {
        int[] arr = {1, 5, 10, 20};
        KArrayBinarySearch tree = new KArrayBinarySearch(arr, 0.5f, 2);

        assertNull(tree.pred(0));
        assertEquals((Integer)1, tree.pred(1));
        assertEquals((Integer)10, tree.pred(11));
        assertEquals((Integer)20, tree.pred(25));
    }

    @Test
    public void testPredAtExactMatches() {
        int[] arr = {2, 4, 6, 8};
        KArrayBinarySearch tree = new KArrayBinarySearch(arr, 0.7f, 2);

        assertEquals((Integer)2, tree.pred(2));
        assertEquals((Integer)2, tree.pred(4));
        assertEquals((Integer)6, tree.pred(8));
    }

    // @Test
    // public void testAllQueriesRange() {
    //     int[] arr = {1, 3, 5, 7, 9};
    //     KArrayBinarySearch tree = new KArrayBinarySearch(arr, 0.2f, 2);

    //     for (int x = -5; x <= 15; x++) {
    //         Integer expected = referencePred(Arrays.asList(arr), x);
    //         assertEquals(expected, tree.pred(x));
    //     }
    // }

    // @Test
    // public void testRandomizedCases() {
    //     Random rand = new Random(42);

    //     for (int size = 1; size < 50; size++) {

    //         // generate random input set
    //         TreeSet<Integer> set = new TreeSet<>();
    //         while (set.size() < size) set.add(rand.nextInt(200));  // adding elements to the TreeSet

    //         int[] arr = set.toArray(new int[0]);  // passing the set elements to an array

    //         float alpha = rand.nextFloat();  // rand for alpha
    //         KArrayBinarySearch tree = new KArrayBinarySearch(arr, alpha);   // building tree

    //         List<Integer> sortedList = Arrays.asList(arr);  // passing this to a list to test it with the referencePred function

    //         // test all possible values in range
    //         for (int x = -50; x <= 250; x++) {    // from -50 to -1 it must always return null
    //             assertEquals(referencePred(sortedList, x), tree.pred(x));
    //         }
    //     }
    // }
}

