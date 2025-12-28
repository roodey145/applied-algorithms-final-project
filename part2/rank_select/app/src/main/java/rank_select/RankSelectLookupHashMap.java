package rank_select;

import java.util.HashMap;
import java.util.Map;

/*
 * The assignment does not specify the type of the data that will be received
 * The values are zeros and ones, thus, we could reduce the required space
 * a little bit by using boolean array.
 * 
 * The following is a basic implementation that could be improved by utilizing
 * parallelism.
*/
public class RankSelectLookupHashMap implements RankSelectInterface {
    private final int[] rank;
    private final Map<Integer, Integer> rankIndexMap; // Allows for constant select, space complexity O(n)

    public RankSelectLookupHashMap(int[] data) {
        rank = new int[data.length];
        rankIndexMap = new HashMap<>();
        int prevRank = 0;
        for (int i = 0; i < data.length; i++) {
            if (data[i] > 0) {
                // New rank detected
                prevRank++; // Absorb the rank

                // Register the rank in the map
                rankIndexMap.put(prevRank, i); // Map rank to first index with that rank
            } else if(i == 0) {
                rankIndexMap.put(0, 0);
            }
            rank[i] = prevRank;
        }

    }

    @Override
    public int rank(int index) throws Exception {
        return rank[index]; // Can be used in parallel
    }

    @Override
    public int select(int rank){
        // Check if the required rank exist
        if (!rankIndexMap.containsKey(rank)) {
            return -1;
            // throw new IndexOutOfBoundsException(
            //         "The rank " + rank + " does not exists! ");
        }

        return rankIndexMap.get(rank);
    }
}
