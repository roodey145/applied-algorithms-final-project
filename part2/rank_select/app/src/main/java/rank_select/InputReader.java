package rank_select;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class InputReader {

    public record InputData(int[] bits, int[] rankQuery, int[] selectQuery, int totalOnes) {
    }

    static InputData readInput() throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            String[] line = br.readLine().strip().split(" ");
            int n = Integer.parseInt(line[0]); // bit length
            int queryLength = Integer.parseInt(line[1]);
            int totalOnes = Integer.parseInt(line[2]);
            int[] bits = new int[n];
            int[] rankQuery = new int[queryLength];
            int[] selectQuery = new int[queryLength];

            String bitString = br.readLine().strip();
            for (int i = 0; i < n; i++) {
                bits[i] = bitString.charAt(i) - '0';
            }

            String[] rankQueryString = br.readLine().strip().split(" ");
            for (int i = 0; i < queryLength; i++) {
                rankQuery[i] = Integer.parseInt(rankQueryString[i]);
            }

            String[] selectQueryString = br.readLine().strip().split(" ");
            for (int i = 0; i < queryLength; i++) {
                selectQuery[i] = Integer.parseInt(selectQueryString[i]);
            }

            return new InputData(bits, rankQuery, selectQuery, totalOnes);

        }

    }
}
