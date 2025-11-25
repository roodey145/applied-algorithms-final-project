package org.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OtherArrayBinarySearch implements BinarySearchTree<Integer> {
    private final List<Integer> list;
    private final int[] tree;
    private final float alpha;
    private final int nodeClauseSize = 3;

    public OtherArrayBinarySearch(Integer[] array, float alpha) {
        this.list = new ArrayList<>();
        Collections.addAll(this.list, array);
        Collections.sort(this.list);

        this.alpha = alpha > 1 ? 1 : (alpha < 0 ? 0 : alpha);

        tree = new int[array.length * nodeClauseSize];
        constructTree(array, 0, array.length - 1, -1, false);
    }

    private int currentNodeIndex = 1;
    private void constructTree(Integer[] origin, int low, int high, int prevIndex, boolean left){
        if(low > high) return;
        int index = low + (int)((high - low) * alpha);
        // Order the index inside the tree
        tree[currentNodeIndex] = origin[index];
        tree[currentNodeIndex - 1] = -1; // Indicate a node with no left child
        tree[currentNodeIndex + 1] = -1; // Indicate a node with no right child
        // If this is the left side of the array, make sure to remember
        if(left) tree[prevIndex - 1] =  currentNodeIndex; // Add left child to previous node
        else if (prevIndex != -1) tree[prevIndex + 1] = currentNodeIndex; // Add right child to previous node
        int tempCurrentNodeIndex = currentNodeIndex; // Important
        currentNodeIndex += nodeClauseSize;

        // Hanlde left side first (DFS)
        constructTree(origin, low, index - 1, tempCurrentNodeIndex, true);
        constructTree(origin, index + 1, high, tempCurrentNodeIndex, false);
    }

    @Override
    public Integer pred(Integer x) {
        if(tree.length < nodeClauseSize) return -1; // Empty tree
        // int index = getIndex(x, 0, list.size() - 1, -1);
        // if(index < 0) return null;
        // else return list.get(index);
        int predIndex = -1;
        int index = 1;
        int cValue;
        
        do { 
            // Get current number
            cValue = tree[index];
            if (cValue == x) return cValue;   // added
            if(cValue < x) {
                // Potential predeccesor
                predIndex = index;
                // Go to the right side
                index = tree[index + 1];
            } else {
                // Value is smaller or equal to the current node value
                // Go to the left side of the node if it exists
                index = tree[index - 1];
            }
        } while (index != -1);

        return predIndex == -1 ? null : tree[predIndex];
    }

/*     private int getIndex(Integer x, int low, int high, int prevIndex) {
        System.out.println("low: " + low + ", high: " + high + ", PrevIndex " + prevIndex);
        if(low > high && x.compareTo(list.get(prevIndex)) > 0){
            System.out.println("X is larger than " + list.get(prevIndex) + ", " + x.compareTo(list.get(prevIndex)));
            return prevIndex;
        }
        else if(low > high){
            int predIndex = high;
            for(int i = predIndex; i >= 0; i--) {
                if(x.compareTo(list.get(prevIndex)) > 0) {
                    // We found the first element that is less than x
                    predIndex = i;
                    break;
                }
            }
            return predIndex; 
        }

        int index = low + (int)((high - low) * alpha);
        Integer selected = list.get(index);
        System.out.println("Selected: " + selected + " compared to: " + x + " is " + selected.compareTo(x));
        if(selected.compareTo(x) < 0) 
            return getIndex(x, index + 1, high, index);
        else {
            return getIndex(x, low, index - 1, index);
        }
    } */

    public void printList() {
        for(Integer t : list) {
            System.out.println(t);
        }
    }

    public void printTree() {
        int left;
        int right;
        for(int i = 1; i < tree.length; i += nodeClauseSize) {
            // Get the value of the left side
            if(tree[i-1] > -1) left = tree[tree[i-1]];
            else left = -1;
            
            // Get the value of the right side
            if(tree[i+1] > -1) right = tree[tree[i+1]];
            else right = -1;

            System.out.println(left/*tree[i-1]*/ + "|" + tree[i] + "|" + right /*tree[i+1]*/);
        }
    }

    public Integer[] getArray(Integer[] tArray) {
        return list.toArray(tArray);
    }
}
