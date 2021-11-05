/*
 * Alex Baret
 * CSIS2420 A03 P1 Binary Search Deluxe
 * 6/24/21
 * 
 */

package a03;

import java.util.*;

/**
 * 
 * When binary searching a sorted array that contains more than one key equal to
 * the search key, allows for finding the index of either the first or the last
 * of such key.
 * 
 * @author alex
 *
 */
public class BinarySearchDeluxe {

	/**
	 * Return the index of the first key in a[] that equals the search key, or -1 if
	 * no such key.
	 * 
	 * @param <Key>      generic type of method
	 * @param a          array of keys to search through
	 * @param key        to be found
	 * @param comparator passed in for comparing keys in the array of keys
	 * @return first index of the key found in the array or -1
	 */
	public static <Key> int firstIndexOf(Key[] a, Key key, Comparator<Key> comparator) {

		
		if (a == null || key == null || comparator == null) {
			throw new java.lang.NullPointerException();
		}
		if (a.length == 0) {
			return -1;
		}
		Arrays.sort(a, comparator);
		int l = 0; // set the low to the start
		int r = a.length - 1; // set the high to the end
		while (l + 1 < r) { // while the low + 1 is less than the high (terminates once low + 1 == high, or
							// they're next to each other)
			int mid = l + (r - l) / 2; // set the mid to be the low - (high-low) / two
			if (comparator.compare(key, a[mid]) <= 0) { // compare the key with the value at mid, if key is less than or
														// equal to mid
				r = mid; // set the high to mid
			} else { // if the key is larger than mid, set the low to mid
				l = mid;
			}
		}
		// once it's gotten here the search is exhaustive and there are no more items to
		// go through, high and low are next to eachother
		if (comparator.compare(key, a[l]) == 0) {
			return l; // if the key is equal to low return that
		}
		if (comparator.compare(key, a[r]) == 0) {
			return r; // if the key is equal to high return that
		}
		return -1; // if the key isn't found return that
	}

	/**
	 * Return the index of the last key in a[] that equals the search key, or -1 if
	 * no such key.
	 * 
	 * @param <Key>      generic type of method
	 * @param a          array of keys to search through
	 * @param key        to be found
	 * @param comparator passed in for comparing keys in the array of keys
	 * @return last index of the key found in the array or -1
	 */
	public static <Key> int lastIndexOf(Key[] a, Key key, Comparator<Key> comparator) {

		Arrays.sort(a, comparator);
		if (a == null || key == null || comparator == null) {
			throw new java.lang.NullPointerException();
		}
		if (a.length == 0) {
			return -1;
		}
		int l = 0;
		int r = a.length - 1;
		while (l + 1 < r) {
			int mid = l + (r - l) / 2;
			if (comparator.compare(key, a[mid]) >= 0) {
				l = mid;
			}

			else {
				r = mid;
			}
		}
		if (comparator.compare(key, a[l]) == 0 && comparator.compare(key, a[r]) == 0) {
			return r;
		}
		if (comparator.compare(key, a[r]) != 0 && comparator.compare(key, a[l]) == 0) {
			return l;
		}
		return -1;
	}

	/**
	 * Converts an int array into an Integer Array to test the methods lastIndexOf
	 * and firstIndexOf
	 * 
	 * @param inputInts is the array of ints to be converted.
	 * @return an Integer[] containing the ints wrapped in Integer wrapper classes
	 */
	private static Integer[] toInteger(int[] inputInts) {
		int N = inputInts.length;
		Integer[] outputInts = new Integer[N];
		for (int i = 0; i < N; i++) {
			outputInts[i] = new Integer(inputInts[i]);
		}
		return outputInts;
	}

	/**
	 * Compares Integers, utilized for testing lastIndexOf and firstIndexOf methods
	 * 
	 * @author alex
	 *
	 */
	private static class IntComp implements Comparator<Integer> {
		/**
		 * Compares integers a and b. Returns either a positive number, negative number,
		 * or zero.
		 * 
		 * @param a int to be compared
		 * @param b int to be compared
		 * @return int either a positive int, negative int, or zero.
		 */
		public int compare(Integer a, Integer b) {
			return a.compareTo(b);
		}
	}

	public static void main(String[] args) {
		// ==== Test Client ==== //

		int[] someInts = { 10, 10, 20, 30, 30, 30, 40, 50, 60, 70, 70, 70 };
		Integer[] intObjects = toInteger(someInts);

		Comparator<Integer> intComp = new IntComp();
		Arrays.sort(intObjects, intComp);
		int first30 = BinarySearchDeluxe.lastIndexOf(intObjects, 70, intComp);
		System.out.println(first30);
	}
}
