/*CS2420 A02 P1
 * Alex Baret
 * 6/11/21
 */
package a02;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;

import edu.princeton.cs.algs4.*;

/**
 * Randomized queue that is similar to a stack or queue, except that the item
 * removed is chosen uniformly at random from items in the data structure.
 * Iterators are not mutually independant.
 * 
 * @author alex
 *
 * @param <Item>
 */
public class RandomizedQueue<Item> implements Iterable<Item> {

	// initial capacity of underlying resizing array
	private static final int INIT_CAPACITY = 8;

	private Item[] array; // array of items
	private int n; // number of elements on stack

	
	/**
	 * Constructs an empty randomized queue
	 */
	public RandomizedQueue() {
		this.array = (Item[]) new Object[INIT_CAPACITY];
		n = 0;
	}

	/**
	 * Determines whether the queue is empty by returning a boolean.
	 * 
	 * @return boolean true or false based off n
	 */
	public boolean isEmpty() { // is the queue empty?
		return n == 0;
	}

	/**
	 * Returns the size of the queue.
	 * 
	 * @return int is the number of items in the queue.
	 */
	public int size() { // return the number of items on the queue
		return n;

	}

	/**
	 * Adds an item to the queue.
	 * 
	 * @param item is an Item to be added to the queue.
	 */
	public void enqueue(Item item) { // add the item
		if (item == null) {
			throw new NullPointerException("Item passed in is null");
		} else {
			if (n == array.length) { // double size of array if necessary
				Item[] copy = (Item[]) new Object[2 * array.length];
				for (int i = 0; i < n; i++) {
					copy[i] = array[i];
				}
				array = copy;
				array[n++] = item;
			} else {
				array[n++] = item;
			}
		}

	}

	/**
	 * Removes an item by choosing it uniformly random from items in the data
	 * structure. Performs array-resizing when necessary.
	 * 
	 * @return
	 */
	public Item dequeue() { // delete and return a random item
		if (this.isEmpty()) {
			throw new NoSuchElementException("Stack underflow");
		} else {
			Random random = new Random();
			int randomSelection = random.nextInt(n);
			Item selection = this.array[randomSelection]; // this line was just added 12:48pm
			this.array[randomSelection] = null;
			this.n--;
			// edge case: last element of the array
			if (randomSelection == array.length - 1) {
				array[randomSelection] = null;
			} else { // not last element of the array
				for (int index = randomSelection + 1; index < array.length; index++) {
					Item toMove = array[index]; // takes item at index and stores in toMove
					int newIndex = index - 1; // sets new index
					array[newIndex] = toMove; // sets item at newIndex which is index + 1
				}
			}
			if (n > 0 && n == array.length / 4) {
				// textbook implementation
				Item[] copy = (Item[]) new Object[array.length / 2];
				for (int i = 0; i < n; i++) {
					copy[i] = array[i];
				}
				array = copy;
//				return array[randomSelection]; //just commented
				return selection; // this line was just added 12:48pm
			} else {
//				return array[randomSelection]; //just commented
				return selection; // this line was just added 12:48pm

			}
		}
	}

	/**
	 * Randomly selects an item from the data structure and returns it.
	 * 
	 * @return
	 */
	public Item sample() { // return (but do not delete) a random item
		if (this.isEmpty())
			throw new NoSuchElementException("Stack underflow");
		else {
			Random random = new Random();
			return array[random.nextInt(n)];

		}
	}

	/**
	 * Constructs an Iterator. Copies the original array and returns a new Iterator
	 * with the shuffled copy array.
	 */
	@Override
	public Iterator<Item> iterator() { // return an independent iterator over items in random order
		Item[] copyArray = array.clone();
		StdRandom.shuffle(copyArray);
		return new RandomIterator();
	}

	/**
	 * Iterates through the queue. Utilizes a shuffled copy of the original array.
	 * 
	 * @author alex
	 *
	 */
	private class RandomIterator implements Iterator<Item> {

		private int index = 0;

		@Override
		public boolean hasNext() {
			return index >= 0 && index < n;
		}

		@Override
		public Item next() {
			if (hasNext() == false) {
				throw new NoSuchElementException("No more items to return");
			} else {
				return array[index++];
			}
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException("not supported");
		}
	}

	public static void main(String[] args) { // unit testing
		RandomizedQueue list = new RandomizedQueue();
		System.out.println(list.isEmpty());
		list.enqueue("hello");
		System.out.println(list.isEmpty());
		list.enqueue(2);
		list.enqueue(3);
		list.enqueue(4);
		list.dequeue();

		for (Object s : list) {
			System.out.print("loop:" + s + " ");
			for (Object i : list) {
				System.out.print(i);
			}
		}

		System.out.println("\nsize: " + list.size());

	}
}
