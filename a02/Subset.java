/*CS2420 A02 Subset
 * Alex Baret
 * 6/20/21
 */

package a02;

import edu.princeton.cs.algs4.*;

/**
 * Test client that utilizes RandomizedQueue.
 * 
 * @author alex
 *
 */
public class Subset {

	public static void main(String[] args) {

		int k = 3;
		String[] input = { "A", "B", "C", "D", "E", "F", "G", "H", "I" };
		RandomizedQueue<String> random = new RandomizedQueue<>();
		for (String letter : input) {
			random.enqueue(letter);
		}
		while (k > 0) {
			System.out.println(random.dequeue());
			k--;
		}

	}
}
