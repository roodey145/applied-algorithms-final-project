package org.example;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.TreeSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;

public class OtherArrayBinarySearchTest {

    private Integer referencePred(List<Integer> sortedList, int x) {
        boolean equalFound = false;
        // Reference: linear scan from the right
        for (int i = sortedList.size() - 1; i >= 0; i--) {
            if (sortedList.get(i) < x) {
                return sortedList.get(i);
            } else if(sortedList.get(i) == x) {
                equalFound = true;
            }
        }
        return equalFound ? x : null;
    }

    @Test
    public void testPredSmallOutboundriesElement(){
        Integer[] array = new Integer[] {-1, 3, 4, 5, 10, 15};
        OtherArrayBinarySearch sortedArray = new OtherArrayBinarySearch(array, 0.2f);

        sortedArray.printTree();

        assertEquals(Integer.valueOf(-1), sortedArray.pred(3));
        assertEquals(null, sortedArray.pred(-2));
        // assertEquals(null, sortedArray.pred(-10));
        // assertEquals(null, sortedArray.pred(-1000));
        // assertEquals(null, sortedArray.pred(-114));
        // assertEquals(null, sortedArray.pred(-1214214));
    }

    @Test
    public void testSingleElement() {
        Integer[] arr = {5};
        OtherArrayBinarySearch tree = new OtherArrayBinarySearch(arr, 0.3f);

        assertEquals((Integer)5, tree.pred(5));
        assertEquals((Integer)5, tree.pred(10));
        assertNull(tree.pred(2));
    }

    @Test
    public void testSimpleFixedValues() {
        Integer[] arr = {1, 5, 10, 20};
        OtherArrayBinarySearch tree = new OtherArrayBinarySearch(arr, 0.5f);

        assertNull(tree.pred(0));
        assertEquals((Integer)1, tree.pred(1));
        assertEquals((Integer)10, tree.pred(11));
        assertEquals((Integer)20, tree.pred(25));
    }

    @Test
    public void testPredAtExactMatches() {
        Integer[] arr = {2, 4, 6, 8};
        OtherArrayBinarySearch tree = new OtherArrayBinarySearch(arr, 0.7f);

        assertEquals((Integer)2, tree.pred(2));
        assertEquals((Integer)2, tree.pred(4));
        assertEquals((Integer)6, tree.pred(8));
    }

    @Test
    public void testAllQueriesRange() {
        Integer[] arr = {1, 3, 5, 7, 9};
        OtherArrayBinarySearch tree = new OtherArrayBinarySearch(arr, 0.2f);

        for (int x = -5; x <= 15; x++) {
            Integer expected = referencePred(Arrays.asList(arr), x);
            assertEquals(expected, tree.pred(x));
        }
    }

    @Test
    public void testLargeCase_1(){
        int size = 10_000_000;
        int testMargin = 10;
        // Create an int array where the values -size to +size is added
        Integer[] arr = new Integer[size * 2 + 1];

        for(int i = -size; i <= size; i++) {
            arr[i + size] = i;   
        }

        for(float alpha = 0.1f; alpha <= 0.9f; alpha += 0.1f){
            // Alpha shpuld be added dynamically 
            OtherArrayBinarySearch tree = new OtherArrayBinarySearch(arr, alpha);

            for(int i = -(size + testMargin); i <= (size + testMargin); i++) {
                if(i < -size) { // i is less than the smallest number in the tree, i.e. null should be returned
                    assertNull(tree.pred(i));
                }
                else if(i == -size) { // This is the first element, i.e. -size should be returned
                    assertEquals((Integer)i, tree.pred(i)); 
                }
                else if(i > -size && i <= size) { // The i-1 exists in the tree, i.e. i-1 should be returned
                    assertEquals((Integer)(i-1), tree.pred(i)); 
                }else { // i is larger than the largest element in the tree, i.e. size should be returned
                    assertEquals((Integer)size, tree.pred(i));
                }
            }
        }
    }

    @Test
    public void testRandomizedCases() {
        Random rand = new Random(42);

        for (int size = 1; size < 50; size++) {

            // generate random input set
            TreeSet<Integer> set = new TreeSet<>();
            while (set.size() < size) set.add(rand.nextInt(200));  // adding elements to the TreeSet

            Integer[] arr = set.toArray(new Integer[0]);  // passing the set elements to an array

            float alpha = rand.nextFloat();  // rand for alpha
            OtherArrayBinarySearch tree = new OtherArrayBinarySearch(arr, alpha);   // building tree

            List<Integer> sortedList = Arrays.asList(arr);  // passing this to a list to test it with the referencePred function

            // test all possible values in range
            for (int x = -50; x <= 250; x++) {    // from -50 to -1 it must always return null
                assertEquals(referencePred(sortedList, x), tree.pred(x));
            }
        }
    }
}

