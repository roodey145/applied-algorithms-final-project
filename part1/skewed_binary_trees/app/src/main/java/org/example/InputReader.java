package org.example;

import java.util.Scanner;

public class InputReader {

    private record InputData(int[] array, int[] queries) {
    }

    static InputData readInput() {
        try (Scanner sc = new Scanner(System.in)) {
            int n = sc.nextInt();
            int q = sc.nextInt();

            int[] arr = new int[n];
            int[] queries = new int[q];
            for (int i = 0; i < n; i++)
                arr[i] = sc.nextInt();
            for (int i = 0; i < q; i++)
                queries[i] = sc.nextInt();

            return new InputData(arr, queries);
        }
    }
}
