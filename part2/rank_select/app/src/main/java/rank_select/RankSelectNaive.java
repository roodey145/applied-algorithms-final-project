public class RankSelectNaive implements RankSelectInterface {
    private final int[] data;

    public RankSelectNaive(int[] data) {
        this.data = data.clone();
    }

    public int rank(int index) {
        if (index > data.length)
            throw new IndexOutOfBoundsException(
                    "The index " + index + " is out of bounds for array length " + data.length);
        int rank = 0;
        for (int i = 0; i < index; i++) {
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
