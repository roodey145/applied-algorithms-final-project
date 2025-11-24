package org.example;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;

public class SortedArrayBinnarySearchTest {
    // private SortedArrayBinnarySearch<Integer> sortedArray;


    @Test
    public void testSorting() {
        int seed = 5;
        int size = 1_000_000;
        Random rand = new Random(seed);
        Integer[] array = new Integer[size];

        for(int i = 0; i < array.length; i++) {
            array[i] = rand.nextInt();
        }

        SortedArrayBinarySearch<Integer> sortedArray = new SortedArrayBinarySearch<Integer>(array, 0.2f);

        array = sortedArray.getArray(new Integer[array.length]);

        for(int i = 0; i < array.length - 1; i++) {
            assertTrue("The array is sorted wrongly! " + array[i] + " should be less than " + array[i + 1], array[i] <= array[i + 1]);
        }
    }


    @Test
    public void testPredMiddleNonExistingElement(){
        Integer[] array = new Integer[] {-1, 3, 4, 5, 10, 15};
        SortedArrayBinarySearch<Integer> sortedArray = new SortedArrayBinarySearch<Integer>(array, 0.2f);

        assertEquals(-1l, (long)sortedArray.pred(2));
        assertEquals(-1l, (long)sortedArray.pred(1));
        assertEquals(5l, (long)sortedArray.pred(6));
        assertEquals(5l, (long)sortedArray.pred(7));
        assertEquals(5l, (long)sortedArray.pred(9));
        assertEquals(10l, (long)sortedArray.pred(14));
    }

    @Test
    public void testPredMiddleExistingElement(){
        Integer[] array = new Integer[] {-1, 3, 4, 5, 10, 15};
        SortedArrayBinarySearch<Integer> sortedArray = new SortedArrayBinarySearch<Integer>(array, 0.2f);

        assertEquals(-1l, (long)sortedArray.pred(3));
        assertEquals(3l, (long)sortedArray.pred(4));
        assertEquals(4l, (long)sortedArray.pred(5));
        assertEquals(5l, (long)sortedArray.pred(10));
        assertEquals(10l, (long)sortedArray.pred(15));
    }

    @Test
    public void testPredSmallOutboundriesElement(){
        Integer[] array = new Integer[] {-1, 3, 4, 5, 10, 15};
        SortedArrayBinarySearch<Integer> sortedArray = new SortedArrayBinarySearch<Integer>(array, 0.2f);

        assertEquals(null, sortedArray.pred(-1));
        assertEquals(null, sortedArray.pred(-2));
        assertEquals(null, sortedArray.pred(-10));
        assertEquals(null, sortedArray.pred(-1000));
        assertEquals(null, sortedArray.pred(-114));
        assertEquals(null, sortedArray.pred(-1214214));
    }


    @Test
    public void testPredLargeOutboundriesElement(){
        Integer[] array = new Integer[] {-1, 3, 4, 5, 10, 15};
        SortedArrayBinarySearch<Integer> sortedArray = new SortedArrayBinarySearch<Integer>(array, 0.2f);

        assertEquals(15l, (long)sortedArray.pred(16));
        assertEquals(15l, (long)sortedArray.pred(60));
        assertEquals(15l, (long)sortedArray.pred(615));
        assertEquals(15l, (long)sortedArray.pred(61523));
        assertEquals(15l, (long)sortedArray.pred(61523234));
    }
}
