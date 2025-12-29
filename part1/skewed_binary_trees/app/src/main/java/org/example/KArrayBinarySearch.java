package org.example;

import java.util.Arrays;

public class KArrayBinarySearch implements SkewedBinarySearchTree {
    // private final List<Integer> list;
    private final int[] sortedArray;
    private final int[] tree;
    private final float alpha;
    private final int nodeClauseSize = 3;
    private final int k;

    public KArrayBinarySearch(int[] array, float alpha, int k) {
        this.k = k;

        // To make sure the array will not be tempored with from the outside
        sortedArray = array.clone();
        Arrays.sort(sortedArray);

        this.alpha = alpha > 1 ? 1 : (alpha < 0 ? 0 : alpha);

        tree = new int[array.length * nodeClauseSize];
        handleAddingTreeNode(1, 0, array.length - 1);
    }

    private int getNextElementIndex(int low, int high) {
        return low + (int) ((high - low) * this.alpha);
    }

    // private int currentNodeIndex = 1;

    private int[] getLowerBound(int low, int high, int index) {
        return new int[] { low, index - 1 };
    }

    private int[] getUpperBound(int low, int high, int index) {
        return new int[] { index + 1, high };
    }

    private Integer[] getNodeClauseData(int low, int high) {
        Integer[] nodeClauseData = new Integer[] {
                null,
                getNextElementIndex(low, high),
                null,
        };

        // Calculate the index of the left node
        if (nodeClauseData[1] > low) { // This node has a left child
            int[] bound = getLowerBound(low, high, nodeClauseData[1]);
            nodeClauseData[0] = getNextElementIndex(bound[0], bound[1]);
        }

        // Calculate the index of the left node
        if (nodeClauseData[1] < high) { // The center node has right child
            int[] bound = getUpperBound(low, high, nodeClauseData[1]);
            nodeClauseData[2] = getNextElementIndex(bound[0], bound[1]);
        }

        return nodeClauseData;
    }

    private int addNodeClauseData(int[] sortedArray, int posIndex, Integer[] nodeClauseData, int shift) {
        // If there is a left node, then the next one should be shifted by extra 3
        if (nodeClauseData[0] != null)
            shift += 3;
        // Left Node Index
        tree[posIndex - 1] = nodeClauseData[0] == null ? -1 : shift; // nodeClauseData[0];

        // If there is a left node, then the next one should be shifted by extra 3
        if (nodeClauseData[2] != null)
            shift += 3;

        tree[posIndex] = sortedArray[nodeClauseData[1]];

        tree[posIndex + 1] = nodeClauseData[2] == null ? -1 : shift; // nodeClauseData[2];

        return shift;
    }

    private void addNodeClause(int[] sortedArray, int posIndex, int leftNodeIndex, int centerNodeIndex,
            int rightNodeIndex) {
        tree[posIndex] = sortedArray[centerNodeIndex];
        tree[posIndex - 1] = leftNodeIndex;
        tree[posIndex + 1] = rightNodeIndex;
    }

    private int handleAddingTreeNode(int index, int low, int high) {
        if (low > high)
            return index - nodeClauseSize;

        // The minimum k levels is 2
        Integer[] centerNodeClauseData = getNodeClauseData(low, high);
        int[] lowerBound = null;
        int[] upperBound = null;
        int shift = index; // This is to shift the left and right node of the current node

        // Add the data to the tree
        // index += addNodeClauseData(sortedArray, index, centerNodeClauseData);
        shift = addNodeClauseData(sortedArray, index, centerNodeClauseData, shift) + 3;


        // Check if the lowerBound is not null
        // if(lowerBound != null) { // Hanlde the side of the left node
        if (centerNodeClauseData[0] != null) {
            lowerBound = getLowerBound(low, high, centerNodeClauseData[1]);
            // upperBound = getLowerBound(centerNodeClauseData[0] + 1, high,
            // centerNodeClauseData[1]);
            int[] leftNodeLowerBound = getLowerBound(low, high, centerNodeClauseData[0]);
            int[] leftNodeUpperBound = getLowerBound(centerNodeClauseData[0] + 1, high, centerNodeClauseData[1]);
            // Contains 3 info. Left-left node index, left-node index, right-left node
            // index.
            Integer[] leftNodeClauseData = getNodeClauseData(lowerBound[0], lowerBound[1]);
            int leftNodePosIndex = index + 3;
            int leftNode = -1;
            int rightNode = -1;

            // Check if there is left node
            if (leftNodeClauseData[0] != null) {
                leftNode = shift;
                // There is a left node to the left node
                shift = handleAddingTreeNode(shift, leftNodeLowerBound[0], leftNodeLowerBound[1]);
            }

            // Check if there is a right node
            if (leftNodeClauseData[2] != null) {
                rightNode = shift;
                shift = handleAddingTreeNode(shift, leftNodeUpperBound[0], leftNodeUpperBound[1]);
            }

            // Add the left node
            addNodeClause(sortedArray, leftNodePosIndex, leftNode, centerNodeClauseData[0], rightNode);

        }

        if (centerNodeClauseData[2] != null) {
            upperBound = getUpperBound(low, high, centerNodeClauseData[1]);
            // upperBound = getLowerBound(centerNodeClauseData[0] + 1, high,
            // centerNodeClauseData[1]);
            int[] rightNodeLowerBound = getLowerBound(centerNodeClauseData[1] + 1, high, centerNodeClauseData[2]);
            int[] rightNodeUpperBound = getUpperBound(low, high, centerNodeClauseData[2]);
            // Contains 3 info. Left-left node index, left-node index, right-left node
            // index.
            Integer[] rightNodeClauseData = getNodeClauseData(upperBound[0], upperBound[1]);
            int rightNodePosIndex = index + nodeClauseSize * (centerNodeClauseData[0] != null ? 2 : 1);
            int leftNode = -1;
            int rightNode = -1;

            // Check if there is left node
            if (rightNodeClauseData[0] != null) {
                leftNode = shift;
                // There is a left node to the left node
                shift = handleAddingTreeNode(shift, rightNodeLowerBound[0], rightNodeLowerBound[1]);
            }

            // Check if there is a right node
            if (rightNodeClauseData[2] != null) {
                rightNode = shift;
                shift = handleAddingTreeNode(shift, rightNodeUpperBound[0], rightNodeUpperBound[1]);
            }

            // Add the left node
            addNodeClause(sortedArray, rightNodePosIndex, leftNode, centerNodeClauseData[2], rightNode);
        }

        return shift;
    }

    public Integer pred(int x) {
        if (tree.length < nodeClauseSize)
            return -1; // Empty tree

        int predIndex = -1;
        int index = 1;
        int cValue;

        do {
            // Get current number
            cValue = tree[index];

            if (cValue < x) {
                // Potential predeccesor
                predIndex = index;
                // Go to the right side
                index = tree[index + 1];
            } else if (cValue == x) {
                // Potential predecessor
                if (predIndex == -1) {
                    // If there is no value to the left side of the current one
                    // then register the current one as a potential predecessor
                    predIndex = index;
                }

                // Try to go to the left node
                if (tree[index - 1] != -1) {
                    index = tree[index - 1];
                } else {
                    // Go to the right node
                    index = tree[index + 1];
                }
            } else {
                // Value is smaller or equal to the current node value
                // Go to the left side of the node if it exists
                index = tree[index - 1];
            }
        } while (index != -1);

        return predIndex == -1 ? null : tree[predIndex];
    }

    /*
     * private int getIndex(Integer x, int low, int high, int prevIndex) {
     * System.out.println("low: " + low + ", high: " + high + ", PrevIndex " +
     * prevIndex);
     * if(low > high && x.compareTo(list.get(prevIndex)) > 0){
     * System.out.println("X is larger than " + list.get(prevIndex) + ", " +
     * x.compareTo(list.get(prevIndex)));
     * return prevIndex;
     * }
     * else if(low > high){
     * int predIndex = high;
     * for(int i = predIndex; i >= 0; i--) {
     * if(x.compareTo(list.get(prevIndex)) > 0) {
     * // We found the first element that is less than x
     * predIndex = i;
     * break;
     * }
     * }
     * return predIndex;
     * }
     * 
     * int index = low + (int)((high - low) * alpha);
     * Integer selected = list.get(index);
     * System.out.println("Selected: " + selected + " compared to: " + x + " is " +
     * selected.compareTo(x));
     * if(selected.compareTo(x) < 0)
     * return getIndex(x, index + 1, high, index);
     * else {
     * return getIndex(x, low, index - 1, index);
     * }
     * }
     */

    // public void printList() {
    // for(Integer t : list) {
    // System.out.println(t);
    // }
    // }

    // public void printTree() {
    //     int left;
    //     int right;
    //     for (int i = 1; i < tree.length; i += nodeClauseSize) {
    //         // Get the value of the left side
    //         if (tree[i - 1] > -1)
    //             left = tree[tree[i - 1]];
    //         else
    //             left = -1;

    //         // Get the value of the right side
    //         if (tree[i + 1] > -1)
    //             right = tree[tree[i + 1]];
    //         else
    //             right = -1;

    //         System.out.println(left/* tree[i-1] */ + "|" + tree[i] + "|" + right /* tree[i+1] */);
    //     }
    // }

    // public Integer[] getArray(Integer[] tArray) {
    // return list.toArray(tArray);
    // }

}
