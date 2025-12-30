package rank_select;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Random;

public class RankSelectSpaceEfficientTest {
    private final int SEED = 30;
    // Testing the helpers

    /**
     * This test is sufficent for the mask helper method as
     * it tests for all the possibile scenarios. I.e. an
     * an exhusion test.
     */
    @Test
    public void mask_test() {
        int nrOfBitsInInt = 32;
        String ithBitMask = "";
        // populate the chars with 1s, representing nonmasking for each bit
        for (int i = 0; i < nrOfBitsInInt; i++) {
            ithBitMask += "1";
            assertEquals(
                    "The index(" + i + ") should have the mask(" + ithBitMask + ")",
                    ithBitMask,
                    Integer.toBinaryString(RankSelectSpaceEfficient.mask(i)));
        }
    }

    @Test
    public void findBlockForRank_test_allones() {
        // You can increase the number as long as it is a multiple of 2.
        int size = 64 * 64;
        int margin = 10;
        int[] bits = new int[size];
        int wordSize = 32;
        int blockSize;
        int wordIndex;

        // Populate the array
        for (int i = 0; i < size; i++) {
            bits[i] = 1;
        }

        for (int k = 1; k <= size; k++) {
            RankSelectSpaceEfficient rs = new RankSelectSpaceEfficient(bits, k);
            blockSize = k;
            for (int rank = -margin; rank < size + margin; rank++) {
                if (rank <= 0) {
                    // Any number less or equal to first index
                    // should return the first block i.e. 0
                    assertEquals(0, rs.findBlockForRank(rank));
                } else if (rank >= size - 1) {
                    // any number larger or equal to the size of the array
                    // should return the last block i.e. nrBlocks - 1
                    assertEquals(rs.getNumBlocks() - 1, rs.findBlockForRank(rank));
                } else {
                    // Any rank should return (rank - 1) / wordSize / (nrBlocks/n) - 1
                    // rank - 1 to get bit index ranging from 0-31
                    // (rank - 1) / wordSize => the index of the word which the required bit lays in
                    // nrBlcoks/n => number of words in each block
                    // wordIndex / blockSize => the index of the block the word lays in
                    wordIndex = (rank - 1) / wordSize;
                    assertEquals(wordIndex / blockSize, rs.findBlockForRank(rank));
                }
            }
        }
    }

    @Test
    public void findBlockForRank_test_random() {
        // You can increase the number as long as it is a multiple of 2.
        int size = 64 * 64;
        int margin = 10;
        int[] bits = new int[size];
        int wordSize = 32;
        int blockSize;
        int wordIndex;
        Random random = new Random(SEED);
        int maxRank = 0;
        List<Integer> ranksIndex = new ArrayList<Integer>();

        // Populate the array
        for (int i = 0; i < size; i++) {
            bits[i] = random.nextInt(0, 2); // 0 or 1
            maxRank += bits[i];
            if (bits[i] == 1) {
                // New rank
                ranksIndex.add(i); // Add the index of the new maxRank to the list
            }
        }

        for (int k = 1; k <= size; k++) {
            RankSelectSpaceEfficient rs = new RankSelectSpaceEfficient(bits, k);
            blockSize = k;
            for (int rank = -margin; rank < size + margin; rank++) {
                if (rank <= 0) {
                    // Any number less or equal to first index
                    // should return the first block i.e. 0
                    assertEquals(0, rs.findBlockForRank(rank));
                } else if (rank > maxRank) {
                    // any number larger than maxSize
                    // should return the last block i.e. nrBlocks - 1
                    assertEquals(rs.getNumBlocks() - 1, rs.findBlockForRank(rank));
                } else {
                    // Get the index where that i exists
                    int rankIndex = ranksIndex.get(rank - 1); // the ith rank is rank - 1
                    wordIndex = rankIndex / wordSize; // the index of the subblock the rank lays in
                    assertEquals(wordIndex / blockSize, rs.findBlockForRank(rank));
                }
            }
        }
    }

    @Test
    public void testRankAndSelect64BitsSingleBlock() {
        // 64 bits → 2 words, 1 block if k = 2
        int[] bits = new int[64];

        // Set some 1s in the first word (indices 0..31)
        bits[0] = 1; // 1st one
        bits[2] = 1; // 2nd
        bits[3] = 1; // 3rd
        bits[6] = 1; // 4th

        // And some 1s in the second word (indices 32..63)
        bits[32] = 1; // 5th
        bits[40] = 1; // 6th

        int k = 2; // block = 2 words = 64 bits
        RankSelectSpaceEfficient rs = new RankSelectSpaceEfficient(bits, k);

        // --- Rank checks ---
        assertEquals(1, rs.rank(0)); // index 0 → [1]
        assertEquals(2, rs.rank(2)); // indices [0,2]
        assertEquals(3, rs.rank(3)); // [0,2,3]
        assertEquals(4, rs.rank(6)); // [0,2,3,6]
        assertEquals(5, rs.rank(32)); // new 1 at 32
        assertEquals(6, rs.rank(40)); // new 1 at 40
        assertEquals(6, rs.rank(63)); // total 6 ones in whole array

        // --- Select checks ---
        assertEquals(0, rs.select(1)); // 1st 1
        assertEquals(2, rs.select(2)); // 2nd 1
        assertEquals(3, rs.select(3)); // 3rd 1
        assertEquals(6, rs.select(4)); // 4th 1
        assertEquals(32, rs.select(5)); // 5th 1
        assertEquals(40, rs.select(6)); // 6th 1

        // Out of range
        assertEquals(-1, rs.select(0));
        assertEquals(-1, rs.select(7)); // only 6 ones exist
    }

    @Test
    public void testRankAndSelect128BitsMultipleBlocks() {
        // 128 bits → 4 words
        int[] bits = new int[128];

        // Put some 1s spread across words to hit multiple blocks
        // Word 0 (bits 0..31)
        bits[1] = 1; // 1st
        bits[5] = 1; // 2nd

        // Word 1 (bits 32..63)
        bits[35] = 1; // 3rd
        bits[50] = 1; // 4th

        // Word 2 (bits 64..95)
        bits[64] = 1; // 5th
        bits[70] = 1; // 6th

        // Word 3 (bits 96..127)
        bits[100] = 1; // 7th

        int k = 2; // 2 words per block → 4 words = 2 blocks
        RankSelectSpaceEfficient rs = new RankSelectSpaceEfficient(bits, k);

        // --- Rank checks around boundaries ---
        assertEquals(0, rs.rank(0)); // bit 0 is 0
        assertEquals(1, rs.rank(1)); // 1 at index 1
        assertEquals(2, rs.rank(5)); // 1s at 1,5
        assertEquals(3, rs.rank(35)); // + 1 at 35
        assertEquals(4, rs.rank(50)); // + 1 at 50
        assertEquals(5, rs.rank(64)); // + 1 at 64
        assertEquals(6, rs.rank(70)); // + 1 at 70
        assertEquals(7, rs.rank(100)); // + 1 at 100
        assertEquals(7, rs.rank(127)); // total 7 ones

        // --- Select checks ---
        assertEquals(1, rs.select(1)); // 1st 1
        assertEquals(5, rs.select(2)); // 2nd
        assertEquals(35, rs.select(3)); // 3rd
        assertEquals(50, rs.select(4)); // 4th
        assertEquals(64, rs.select(5)); // 5th
        assertEquals(70, rs.select(6)); // 6th
        assertEquals(100, rs.select(7)); // 7th
        assertEquals(-1, rs.select(8)); // no 8th 1
    }

    @Test
    public void testAllZeros64Bits() {
        int[] bits = new int[64]; // all 0
        RankSelectSpaceEfficient rs = new RankSelectSpaceEfficient(bits, 2);

        assertEquals(0, rs.rank(0));
        assertEquals(0, rs.rank(63));

        assertEquals(-1, rs.select(1));
        assertEquals(-1, rs.select(5));
    }

    @Test
    public void select_test_random() {
        // You can increase the number as long as it is a multiple of 2.
        int size = 64 * 64;
        int margin = 1000;
        int[] bits = new int[size];
        Random random = new Random(SEED);
        int maxRank = 0;
        List<Integer> ranksIndex = new ArrayList<Integer>();

        // Populate the array
        for (int i = 0; i < size; i++) {
            bits[i] = random.nextInt(0, 2); // 0 or 1
            maxRank += bits[i];
            if (bits[i] == 1) {
                // New rank
                ranksIndex.add(i); // Add the index of the new maxRank to the list
            }
        }

        for (int k = 1; k <= size; k++) {
            RankSelectSpaceEfficient rs = new RankSelectSpaceEfficient(bits, k);
            for (int rank = -margin; rank < size + margin; rank++) {
                if (rank <= 0) {
                    // Any number less or equal to first index
                    // should return the first block i.e. 0
                    assertEquals(-1, rs.select(rank));
                } else if (rank > maxRank) {
                    // any number larger than maxSize
                    // should return the last block i.e. nrBlocks - 1
                    assertEquals(-1, rs.select(rank));
                } else {
                    // Get the index where that i exists
                    int rankIndex = ranksIndex.get(rank - 1); // the ith rank is rank - 1
                    assertEquals(rankIndex, rs.select(rank));
                }
            }
        }
    }

    @Test
    public void rank_test_random() {
        // You can increase the number as long as it is a multiple of 2.
        int size = 64 * 64;
        int margin = 1000;
        int[] bits = new int[size];
        Random random = new Random(SEED);
        int maxRank = 0;
        int[] ranks = new int[size];

        // Populate the array
        for (int i = 0; i < size; i++) {
            bits[i] = random.nextInt(0, 2); // 0 or 1
            maxRank += bits[i];
            ranks[i] = maxRank; // Store the rank at the index
        }

        for (int k = 1; k <= size; k++) {
            RankSelectSpaceEfficient rs = new RankSelectSpaceEfficient(bits, k);
            for (int i = -margin; i < size + margin; i++) {
                if (i < 0 || i >= size) {
                    // Any number outside the boundaries
                    // should throw an error
                    try {
                        assertEquals(-1, rs.rank(i)); // Should throw an error
                    } catch (Exception e) {
                        assertTrue(true);
                    }
                } else {
                    // Get the index where that i exists
                    int rankIndex = ranks[i]; // the ith rank is rank - 1
                    assertEquals(rankIndex, rs.rank(i));
                }
            }
        }
    }
}
