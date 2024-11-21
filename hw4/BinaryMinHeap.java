package edu.iastate.coms3110.hw4;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

public class BinaryMinHeap<T> extends PurePriorityQueue<T> {
	private ArrayList<T> heap = new ArrayList<T>();
	private HashMap<T, Integer> location = new HashMap<T, Integer>();

	public BinaryMinHeap(Comparator<T> comp) {
		super(comp);
	}

	/**
	 * 
	 *
	 * @return The number of elements in the heap
	 */
	@Override
	public int size() {
		return heap.size();
	}

	/**
	 * Adds an element to the heap.
	 *
	 * @param item An element not in the heap that will be added to it.
	 */
	@Override
	public void add(T item) { //O(log n)
		if (!location.containsKey(item))
			heap.add(item);
		
		//Update location
		 int index = heap.size() - 1;
	        location.put(item, index);
	        heapifyUp(index);
	}

	/**
	 * 
	 *
	 * @return Returns the minimum element of the heap without removing it.
	 */
	@Override
	public T getMin() { //O(1)
		return heap.get(0);
	}

	/**
	 * Removes the minimum element from the heap and returns it.
	 *
	 * @return The minimum element that was in the heap when the method was invoked.
	 */
	@Override
	public T extractMin() { //O(log n)
		T min = heap.get(0);

		//Maintain heap
		location.remove(min);
		T last = heap.remove(heap.size() - 1);

		if (!heap.isEmpty()) {
			heap.set(0, last); //Set highest to heapifyDown thru every option
			location.put(last, 0); //heapifyDown updates location
			heapifyDown(0);
		}

		return min;
	}

	/**
	 * Anytime the key decreases for an element in the heap, this method must be
	 * invoked to restored the heap property. Here, key refers to the value
	 * determining the ordering of heap elements as used in the Comparator.
	 *
	 * @param item An item in the heap that has had its key decreased.
	 */
	@Override
	public void keyDecreased(T item) { //O(log n)
		int index = location.get(item);
		heapifyUp(index);
	}

	/**
	 * Moves the item at the given index up in the heap
	 * 
	 * @param index
	 */
	private void heapifyUp(int index) { //O(log n)
		while (index > 0) {
			int parentIndex = (index - 1) / 2;
			if (comp.compare(heap.get(index), heap.get(parentIndex)) >= 0) { // Max height
				break;
			}
			swap(index, parentIndex);
			index = parentIndex;
		}
	}
	
	/**
	 *  Moves the item at the given index down in the heap
	 *  
	 * @param index
	 */
	private void heapifyDown(int index) { //O(log n)
	    int size = heap.size();
	    while (true) { //Until break condition
	        int leftChild = 2 * index + 1;
	        int rightChild = 2 * index + 2;
	        int smallest = index;

	        if (leftChild < size && comp.compare(heap.get(leftChild), heap.get(smallest)) < 0) {
	            smallest = leftChild;
	        }
	        
	        if (rightChild < size && comp.compare(heap.get(rightChild), heap.get(smallest)) < 0) {
	            smallest = rightChild;
	        }
	        
	        if (smallest == index) {
//	            System.out.println("heapifyDown to " + index);
	            break;
	        }
	        swap(index, smallest);
	        index = smallest;
	    }
	}

	/**
	 * Swaps the two indexes in the heap
	 * 
	 * @param i
	 * @param j
	 */
	private void swap(int i, int j) { //O(1)
		T temp = heap.get(i);
		heap.set(i, heap.get(j));
		heap.set(j, temp);
		location.put(heap.get(i), i);
		location.put(heap.get(j), j);
	}

}
