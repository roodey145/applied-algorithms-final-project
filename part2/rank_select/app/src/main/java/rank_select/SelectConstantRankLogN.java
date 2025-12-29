package rank_select;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is similair to the normal RankSelectLookup class. 
 * The difference here is the time complexity of the methods. In
 * this class the select method is favoured and have a constant
 * time complexity and the rank has a O(log n) time complexity. 
 * Where as the other class has it the other way around. This
 * class might be more space efficent in practice since it only
 * needs the indeces of the ranks which can reduce the space
 * by many folds depending on the zeros to ones ratio. If the
 * ratio is  32 : 1 or more i.e. 65 : 1, 100 : 1, then using
 * this class is even better than using an improved data 
 * strecture where each integer represents 32 bits.
 */
public class SelectConstantRankLogN implements RankSelectInterface {
    private final int[] data;

    
    public SelectConstantRankLogN(int[] data) {
        // Go through all the element and save the ranks only
        List<Integer> ranksInfo = new ArrayList<>();

        if(data.length == 0 || data[0] == 0) {
            // The first item is zero, i.e. there is rank 0
            ranksInfo.add(0);
        } else {
            ranksInfo.add(-1); // Indicate that there is no rank 0
        }

        for (int i = 0; i < data.length; i++) {
            if(data[i] > 0) { // When every a new rank is found, register its index
                ranksInfo.add(i);
            }
        }

        this.data = new int[ranksInfo.size()];
        for(int i = 0; i < this.data.length; i++) {
            this.data[i] = ranksInfo.get(i); // Copy the data info to the array
        }
    }


    @Override
    public int rank(int index) throws Exception {
        // Not needed since it is just an extra concept.
        // We can easily do a while loop where we search for the 
        // predecessor index. Just keep track of the index of the
        // last seen element that is smaller than the index
        // Using a while loop avoid the recursion which, according
        // to the predecessor methods papir, is better.

        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'rank'");
    }


    @Override
    public int select(int rank) throws Exception {
        if(rank >= 0 && rank < data.length) return data[rank];
        else return -1; // Indicate the specified rank does not exists
    }


}
