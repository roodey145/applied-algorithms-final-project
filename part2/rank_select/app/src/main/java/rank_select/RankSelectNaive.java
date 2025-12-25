package rank_select;


/**
 * This data strecture assumes that the input array is shaped correctly.
 * If the input array has values that are not equal to 1 and 0, then any
 * number above 1 is considered a 1 and any number less than 0 is considered
 * as 0.
 */
public class RankSelectNaive implements RankSelectInterface {
    private final int[] data;
    
    public RankSelectNaive(int[] data) {
        this.data = data.clone();
    }

    public int rank(int index) {
        if (index >= data.length || index < 0)
            throw new IndexOutOfBoundsException(
                    "The index " + index + " is out of bounds for array length " + data.length);
        int rank = 0;
        for (int i = 0; i <= index; i++) {
            if (data[i] > 0) // Assuming the received data is indeed only zeros and ones
                rank++;
        }
        return rank;
    }

    public int select(int rank) {
        if (rank == 0)
            return -1;

        int curRank = 0;
        int index = 0;

        do {
            curRank = data[index++] > 0 ? curRank + 1 : curRank; // Increase if data[i] == 1
        } while (curRank < rank);

        return index;
    }
}
