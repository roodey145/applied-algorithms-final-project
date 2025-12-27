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
public class RankSelectLookup implements RankSelectInterface {
    private final int[] rank;
    // private final Map<Integer, Integer> rankIndexMap; // Allows for constant select, space complexity O(n)

    public RankSelectLookup(boolean[] data) {
        rank = new int[data.length];
        int prevRank = 0;
        for (int i = 0; i < data.length; i++) {
            if (data[i]) {
                // New rank detected
                prevRank++; // Absorb the rank
            }
            rank[i] = prevRank;
        }

    }

    @Override
    public int rank(int index) throws Exception {
        return rank[index]; // Can be used in parallel
    }

    @Override
    public int select(int rank) throws Exception {
        return select(rank, 0, this.rank.length - 1);
    }

    

    private int select(int target, int low, int high) {
        if(low > high) return -1;
        int mid = low + (high - low) / 2;
        
        if(rank[mid] == target) {
            if((mid > low && rank[mid - 1] != target) || mid == low) {
                // Mid is the index of the first element that has this rank
                return mid;
            }
            return select(target, low, (mid - 1));
        } 
        else if (rank[mid] > target) {
            return select(target, low, (mid - 1));
        } else { // The rank of this element is less than the target
            return select(target, mid + 1, high);
        }
    }

    public static void main(String[] args) throws Exception {
        boolean[] ranksData = new boolean[] {
            false, // 0 index 1
            false, // 0 
            true, // 1 index 2
            false, // 1
            false, // 1
            true, // 2 index 5
            true, // 3 index 6
            true, // 4 index 7
            false, // 4 
            false, // 4
        };
        RankSelectLookup rs = new RankSelectLookup(ranksData);

        System.out.println("========RANKS========");
        System.out.println((rs.rank(0) == 0));
        System.out.println(rs.rank(1) == 0);
        System.out.println(rs.rank(2) == 1);
        System.out.println(rs.rank(3) == 1);
        System.out.println(rs.rank(4) == 1);
        System.out.println(rs.rank(5) == 2);
        System.out.println(rs.rank(6) == 3);
        System.out.println(rs.rank(7) == 4);
        System.out.println(rs.rank(8) == 4);
        System.out.println(rs.rank(9) == 4);
        System.out.println("=====================\n");

        System.out.println("========SELECT=======");
        System.out.println((rs.select(0) == 0));
        System.out.println((rs.select(1) == 2));
        System.out.println((rs.select(2) == 5));
        System.out.println((rs.select(3) == 6));
        System.out.println((rs.select(4) == 7));
        System.out.println("=====================\n");
    }
}
