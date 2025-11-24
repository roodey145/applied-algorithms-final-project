package org.example;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Test;

public class SearchTreeTest {

    @Test
    public void checkForExistingPred() {
        int[] a = { 1, 2, 3, 5, 6, 7, 12 };
        SearchTree st = new SearchTree(a, 0.5f);
        Assert.assertEquals(Integer.valueOf(7), st.Pred(8));
    }

    @Test
    public void checkForExistingPred1() {
        // Lecture example
        int[] a = { 1, 3, 8, 12, 37, 49, 50 };
        SearchTree st = new SearchTree(a, 0.5f);
        Assert.assertEquals(Integer.valueOf(8), st.Pred(11));
    }

    @Test
    public void checkForExistingPred2() {
        // Lecture example
        int[] a = { 1, 3, 8, 12, 37, 49, 50 };
        SearchTree st = new SearchTree(a, 0.5f);
        Assert.assertEquals(Integer.valueOf(12), st.Pred(15));
    }

    @Test
    public void noPredIfArrayLengthIsZero() {
        // Lecture example
        int[] a = {};
        SearchTree st = new SearchTree(a, 0.5f);
        Assert.assertNull(st.Pred(12));
        ;
    }
}
