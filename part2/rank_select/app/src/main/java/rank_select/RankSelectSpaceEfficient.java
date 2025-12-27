package rank_select;

public class RankSelectSpaceEfficient implements RankSelectInterface {
    
    private final int[] bits;
    private final int k;
    private final int n;
    private final int[] words;
    private final int[] Rs;           // Rs[b] = number of 1s BEFORE block b
    private final int numWords;       // = n / 32
    private final int numBlocks;      // number of superblocks

    public RankSelectSpaceEfficient(int[] bits, int k  ) {
        this.bits = bits;
        n = bits.length;
        this.k = k;
        numWords = n / 32;
        words = new int[numWords];

        for (int i = 0; i < n; i++) {
            if (bits[i] != 0) {           // treat any non-zero as 1
                int wordIndex = i >>> 5;  // i / 32
                int bitIndex  = i & 31;   // i % 32
                words[wordIndex] |= (1 << bitIndex);  // put a one in the bitIndex position from right to left of the 32 bit int
            }
        }


        this.numBlocks = (numWords + k - 1) / k; // ceil(numWords / k)
        this.Rs = new int[numBlocks];

        int onesSoFar = 0;
        int block = 0;

        for (int i = 0; i < numWords; i++) {
            // At the start of each superblock, record how many 1s we've seen BEFORE it.
            if (i % k == 0) {   // this makes to jump to the next block
                Rs[block] = onesSoFar;
                block++;
            }
            
            onesSoFar += Integer.bitCount(words[i]);
        }
    }

        public int rank (int index) {
             if (index < 0 || index >= n) {
            throw new IndexOutOfBoundsException(
                "Index " + index + " out of bounds for n = " + n
            );
        }

            int wordIndex = index >>> 5;  // index / 32
            int bitIndex  = index & 31;   // index % 32

            int block = wordIndex / k;

            // Start with all 1s BEFORE this block
            int ones = Rs[block];

            // To count ones after the ones from the last block before that one we need top look
            int firstWordInBlock = block * k;   // in what numWord to start 
            for (int i = firstWordInBlock; i < wordIndex; i++) {
                ones += Integer.bitCount(words[i]);
            }

            // Now count bits 0..bitIndex in the current word
            int mask;
            if (bitIndex == 31) {
                mask = -1; // all 32 bits set, all 1s, (32 ones)
            } else {
                mask = (1 << (bitIndex + 1)) - 1;
                // 1 << (3+1) = 1 << 4 = 00010000
                // subtract 1:            00001111
            } 
            int partialWord = words[wordIndex] & mask;   // only keep the matching ones between the words[index] and the mask
            ones += Integer.bitCount(partialWord);

            return ones;
        }

        // Binary search on Rs to find the block containing the r-th 1.
        // Rs[b] = number of 1s BEFORE block b.
        // We want the largest b with Rs[b] < r.
        private int findBlockForRank(int r) {
            int left = 0;
            int right = numBlocks - 1;
            int best = 0;

            while (left <= right) {
                int mid = (left + right) / 2;
                if (Rs[mid] < r) {
                    best = mid;      // this block is still before the r-th 1
                    left = mid + 1;  // try a later block
                } else {
                    right = mid - 1; // r-th 1 is before or in this block, go left
                }
            }

            return best;
        }


    

        @Override
        public int select(int r) {
            if (r <= 0) {
                return -1;
            }

            int totalOnes = rank(n - 1);  // biggest rank
            if (r > totalOnes) {
                return -1; // r-th 1 doesn't exist
            }

            // Step 1: find block
            int block = findBlockForRank(r);

            int onesSoFar = Rs[block];
            int wordIndex = block * k;

            // Step 2: scan words until we find the word containing the r-th 1
            while (wordIndex < numWords) {
                int word = words[wordIndex];
                int bitCount = Integer.bitCount(word);

                if (onesSoFar + bitCount >= r) {
                    // The r-th 1 is in this word
                    break;   // we may have gone beyond the desired rank, so we go to step 3 to get the exact index
                } else {
                    onesSoFar += bitCount;
                    wordIndex++;
                }
            }

            // Step 3: scan bits inside this word
            int word = words[wordIndex];
            int remaining = r - onesSoFar; // which 1 inside this word
/*             Example:
            If we want r = 10, and there are onesSoFar = 7 ones before this word:
            Then the r-th 1 is the (10 - 7) = 3rd 1 inside this word. So we would need the third 1 inside the word*/

            for (int bit = 0; bit < 32; bit++) {
                if (((word >>> bit) & 1) != 0) {   // checks if the inspected bit is not equal to 0 (shift it to the end and compare with 1)
                    remaining--;
                    if (remaining == 0) {
                        return (wordIndex << 5) + bit; // wordIndex * 32 + bit (this gives you the exact index)
                    }
                }
            }

            // Should not happen
            return -1;
        }

        public static void main(String[] args) {
            int[] bits = new int[64];
            bits[0] = 1;
            bits[2] = 1;
            bits[3] = 1;
            bits[6] = 1;

            int k = 2; // superblock = 32 * 2 = 64 bits (just 1 block here)

            RankSelectSpaceEfficient rs = new RankSelectSpaceEfficient(bits, k);

            System.out.println("Rank:");
            System.out.println("rank(0) = " + rs.rank(0)); // 1
            System.out.println("rank(2) = " + rs.rank(2)); // 2
            System.out.println("rank(3) = " + rs.rank(3)); // 3
            System.out.println("rank(6) = " + rs.rank(6)); // 4

            System.out.println("\nSelect:");
            System.out.println("select(1) = " + rs.select(1)); // 0
            System.out.println("select(2) = " + rs.select(2)); // 2
            System.out.println("select(3) = " + rs.select(3)); // 3
            System.out.println("select(4) = " + rs.select(4)); // 6
            System.out.println("select(5) = " + rs.select(5)); // -1
    }





    
}
