package org.example;

import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class SortedArrayBinarySearchTest {
    // private SortedArrayBinnarySearch<Integer> sortedArray;

    @Test
    public void testSorting() {
        int seed = 5;
        int size = 1_000_000;
        Random rand = new Random(seed);
        int[] array = new int[size];

        for (int i = 0; i < array.length; i++) {
            array[i] = rand.nextInt();
        }

        SortedArrayBinarySearch sortedArray = new SortedArrayBinarySearch(array, 0.2f);

        array = sortedArray.getArray();

        for (int i = 0; i < array.length - 1; i++) {
            assertTrue("The array is sorted wrongly! " + array[i] + " should be less than " + array[i + 1],
                    array[i] <= array[i + 1]);
        }
    }

    @Test
    public void testPredMiddleNonExistingElement() {
        int[] array = new int[] { -1, 3, 4, 5, 10, 15 };
        SortedArrayBinarySearch sortedArray = new SortedArrayBinarySearch(array, 0.2f);

        assertEquals((Integer.valueOf(-1)), sortedArray.pred(2));
        assertEquals(Integer.valueOf(-1), sortedArray.pred(1));
        assertEquals((Integer) 5, sortedArray.pred(6));
        assertEquals((Integer) 5, sortedArray.pred(7));
        assertEquals((Integer) 5, sortedArray.pred(9));
        assertEquals((Integer) 10, sortedArray.pred(14));
    }

    @Test
    public void testPredMiddleExistingElement() {
        int[] array = new int[] { -1, 3, 4, 5, 10, 15 };
        SortedArrayBinarySearch sortedArray = new SortedArrayBinarySearch(array, 0.2f);

        assertEquals(Integer.valueOf(3), sortedArray.pred(3));
        assertEquals((Integer) 4, sortedArray.pred(4));
        assertEquals((Integer) 5, sortedArray.pred(5));
        assertEquals((Integer) 10, sortedArray.pred(10));
        assertEquals((Integer) 15, sortedArray.pred(15));
    }

    @Test
    public void testPredSmallOutboundriesElement() {
        int[] array = new int[] { -1, 3, 4, 5, 10, 15 };
        SortedArrayBinarySearch sortedArray = new SortedArrayBinarySearch(array, 0.2f);

        assertEquals(Integer.valueOf(-1), sortedArray.pred(-1));
        assertEquals(null, sortedArray.pred(-2));
        assertEquals(null, sortedArray.pred(-10));
        assertEquals(null, sortedArray.pred(-1000));
        assertEquals(null, sortedArray.pred(-114));
        assertEquals(null, sortedArray.pred(-1214214));
    }

    @Test
    public void testPredLargeOutboundriesElement() {
        int[] array = new int[] { -1, 3, 4, 5, 10, 15 };
        SortedArrayBinarySearch sortedArray = new SortedArrayBinarySearch(array, 0.2f);

        assertEquals((Integer) 15, sortedArray.pred(16));
        assertEquals((Integer) 15, sortedArray.pred(60));
        assertEquals((Integer) 15, sortedArray.pred(615));
        assertEquals((Integer) 15, sortedArray.pred(61523));
        assertEquals((Integer) 15, sortedArray.pred(61523234));
    }

    @Test
    public void exhaustiveTest() {
        // We test all sizes n < 100
        for (int n = 0; n < 100; n++) {
            int[] data = new int[n];

            // Fill with a simple, strictly increasing, even sequence: 0, 2, 4, ...
            for (int i = 0; i < n; i++) {
                data[i] = i * 2;
            }

            // Try multiple alpha values (even if the implementation ignores alpha,
            // this shows the tests do not depend on a single parameter choice).
            float[] alphas = { 0.1f, 0.5f, 0.9f };
            for (float alpha : alphas) {
                SortedArrayBinarySearch sab = new SortedArrayBinarySearch(data, alpha);

                // For each size n, we query a range that:
                //  - goes below the smallest element (x = -1),
                //  - hits all elements and gaps up to just above the largest (2*n).
                for (int x = -1; x <= 2 * n; x++) {
                    Integer expected = goldenModelPred(data, x);
                    Integer actual = sab.pred(x);

                    assertEquals("Failed for size " + n + " alpha " + alpha + " query " + x,
                            expected, actual);
                }
            }
        }
    }

    /**
     * Golden model for pred: returns the largest value in the (sorted) array
     * that is <= x, or null if no such value exists.
     */
    private Integer goldenModelPred(int[] data, int x) {
        Integer pred = null;
        for (int value : data) {
            if (value <= x) {
                pred = value;
            } else {
                // data is sorted ascending, so we can stop early
                break;
            }
        }
        return pred;
    }

}
