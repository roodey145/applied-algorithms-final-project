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

    @Test
    public void exhaustiveTest() {
        for (int n = 0; n < 100; n++) {
            int[] data = new int[n];
            for (int i = 0; i < n; i++)
                data[i] = i * 2;

            float[] alphas = { 0.1f, 0.5f, 0.9f };
            for (float alpha : alphas) {
                SearchTree st = new SearchTree(data, alpha);
                for (int x = -1; x <= (2 * n); x++) {
                    Integer expected = goldenModelPred(data, x);
                    Integer actual = st.pred(x);
                    Assert.assertEquals("Failed for size " + n + " alpha " + alpha + " query " + x,
                            expected, actual);
                }
            }
        }
    }

    private Integer goldenModelPred(int[] data, int x) {
    Integer pred = null;
    for (int v : data) {
        if (v <= x) {      
            pred = v;
        } else {
            break;         
        }
    }
    return pred;
}

}
