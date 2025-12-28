package org.example;

import org.junit.Assert;
import org.junit.Test;

public class SearchTreeTest {

    @Test
    public void checkForExistingPred() {
        int[] a = { 1, 2, 3, 5, 6, 7, 12 };
        SearchTree st = new SearchTree(a, 0.5f);
        Assert.assertEquals(Integer.valueOf(7), st.pred(8));
    }

    @Test
    public void checkForExistingPred1() {
        // Lecture example
        int[] a = { 1, 3, 8, 12, 37, 49, 50 };
        SearchTree st = new SearchTree(a, 0.5f);
        Assert.assertEquals(Integer.valueOf(8), st.pred(11));
    }

    @Test
    public void checkForExistingPred2() {
        // Lecture example
        int[] a = { 1, 3, 8, 12, 37, 49, 50 };
        SearchTree st = new SearchTree(a, 0.5f);
        Assert.assertEquals(Integer.valueOf(12), st.pred(12));
        Assert.assertEquals(Integer.valueOf(12), st.pred(15));
    }

    @Test
    public void noPredIfArrayLengthIsZero() {
        // Lecture example
        int[] a = {};
        SearchTree st = new SearchTree(a, 0.5f);
        Assert.assertNull(st.pred(12));
        ;
    }

    @Test
    public void allElementsGreaterThanPred() {
        // Lecture example
        int[] a = { 2, 3, 4, 5, 6, 7 };
        SearchTree st = new SearchTree(a, 0.5f);
        Assert.assertNull(st.pred(1));
        ;
    }
}
