/*CS2420 A02 P2
 * Alex Baret
 * 6/20/21
 */

package a02;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Linked-list based data structure that allows insertion and removal at the
 * start and end of the list. Interable interface implemented for iterating
 * through the Deque.
 * 
 * @author alex
 *
 * @param <Item> generic type parameter
 */
public class Deque<Item> implements Iterable<Item> {

	private Node first = null;
	private Node last = null;
	private int n = 0;

	/**
	 * Node class. Allows creation of a node object with instance variables Item,
	 * next, and previous.
	 * 
	 * @author alex
	 *
	 */
	private class Node {
		Item item;
		Node next; // links to node in front
		Node previous; // links to node behind
	}

	/**
	 * Constructs an empty Deque
	 */
	public Deque() {
		this.first = new Node();
		this.last = new Node();
	}

	/**
	 * Determines whether the deque calling this method is empty.
	 * 
	 * @return boolean which returns true false based off whether the Deque is empty
	 *         or not empty.
	 */
	public boolean isEmpty() { // is the deque empty?
		return first.item == null && last.item == null;
	}

	/**
	 * Returns the number of items on the Deque.
	 * 
	 * @return int number of items in the Deque.
	 */
	public int size() {
		return n;
	}

	/**
	 * Adds an item to the front of the Deque.
	 * 
	 * @param item to be added to the front of the Deque. Must not be null.
	 */
	public void addFirst(Item item) {
		if (item == null) {
			throw new NullPointerException("Cannot add null item to Deque");
		} else {
			// needs to check if it's empty
			if (isEmpty()) {
				first.item = item;
				last = first;
				n++;
			} else {
				Node oldFirst = first;
				first = new Node();
				first.item = item;
				first.next = oldFirst;
				first.previous = null;
				oldFirst.previous = first;
				n++;
			}

		}
	}

	/**
	 * Adds an item to the end of the Deque.
	 * 
	 * @param item to be added to the end of the Deque. Must not be null.
	 */
	public void addLast(Item item) {
		if (item == null) {
			throw new NullPointerException("Cannot add null item to Deque");
		} else {
			if (isEmpty()) { // if it's empty, just set the last item to item from null
				last.item = item;
				first = last;
				n++;
			} else {
				Node oldLast = last;
				last = new Node();
				last.item = item;
				last.next = null;
				if (this.size() < 1) {
					first = last;
				} else {
					oldLast.next = last;
					last.previous = oldLast; // backlink
					last.next = null; // forward link new last
				}
				n++;
			}
		}
	}

	/**
	 * Removes the first item from the Deque and returns the item removed.
	 * 
	 * @return Item removed from the Deque.
	 */
	public Item removeFirst() {
		if (this.isEmpty()) {
			throw new NoSuchElementException("Deque is empty");
		} else {
			Item item = first.item; // preventing loitering, setting first item to null
			first = first.next; // setting first to first.next
			n--;
			return item;
		}

	}

	/**
	 * Removes the last item from the Deque and returns the item removed.
	 * 
	 * @return Item removed from the Deque.
	 */
	public Item removeLast() {
		if (this.isEmpty()) {
			throw new NoSuchElementException("Deque is empty");
		} else if (last.item == null) {
			return first.item;
		} else {
			Item item = last.item; // store oldLast to return
			last = last.previous; // set last to previous node
			n--;
			return item;
		}
	}

	/**
	 * Constructs a new Iterator Object.
	 */
	public Iterator<Item> iterator() {
		return new ListIterator();
	}

	/**
	 * Iterates through each item in the Deque.
	 * 
	 * @author alex
	 *
	 */
	private class ListIterator implements Iterator<Item> {
		private Node current = first;

		/**
		 * Determines whether there are more items to examine in the Deque.
		 */
		@Override
		public boolean hasNext() {
			return current != null;
		}

		/**
		 * Moves to the next item in the Deque.
		 */
		@Override
		public Item next() {
			if (isEmpty() == true || hasNext() == false) {
				throw new NoSuchElementException("Deque is empty");
			} else {
				Item item = current.item;
				current = current.next;
				return item;
			}
		}

		/**
		 * Not supported, throws UnsupportedOperationException.
		 */
		@Override
		public void remove() {
			throw new UnsupportedOperationException("Not supported");
		}

	}

	public static void main(String[] args) { // unit testing

		Deque<String> d1 = new Deque<String>();
		d1.addFirst("hi");
		d1.addLast("hello");
		d1.addLast("bye");
		System.out.println(d1.removeFirst());
		System.out.println(d1.size());
	}
}
