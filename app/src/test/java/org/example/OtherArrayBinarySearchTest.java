package org.example;

import static org.junit.Assert.*;

import org.junit.Test;

public class OtherArrayBinarySearchTest {
    @Test
    public void testPredSmallOutboundriesElement(){
        Integer[] array = new Integer[] {-1, 3, 4, 5, 10, 15};
        OtherArrayBinarySearch sortedArray = new OtherArrayBinarySearch(array, 0.2f);

        sortedArray.printTree();

        // assertEquals(null, sortedArray.pred(-1));
        // assertEquals(null, sortedArray.pred(-2));
        // assertEquals(null, sortedArray.pred(-10));
        // assertEquals(null, sortedArray.pred(-1000));
        // assertEquals(null, sortedArray.pred(-114));
        // assertEquals(null, sortedArray.pred(-1214214));
    }
}
