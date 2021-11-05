/*
 * Alex Baret
 * A05 P1 PointST
 * 7/27/21
 */
package a05;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.RedBlackBST;
import edu.princeton.cs.algs4.Stack;

/**
 * A mutable data type that is a symbol table with Point2D. Implemented by using
 * a red-black BST
 * 
 * @author alex
 *
 * @param <Value>
 */
public class PointST<Value> {

	private RedBlackBST<Point2D, Value> tree = new RedBlackBST<Point2D, Value>();

	/**
	 * Constructs an empty symbol table of points
	 */
	public PointST() {
		this.tree = new RedBlackBST<Point2D, Value>();
	}

	/**
	 * Determines whether the ST is empty. Returns boolean True if empty, False
	 * otherwise.
	 * 
	 * @return boolean True if empty, False otherwise.
	 */
	public boolean isEmpty() {
		return this.tree.isEmpty();

	}

	/**
	 * Returns the number of key-value pairs in this symbol table.
	 * 
	 * @return the number of key-value pairs in this symbol table
	 */
	public int size() { // number of points
		return this.tree.size();

	}

	/**
	 * Inserts the specified key-value pair into the symbol table, overwriting the
	 * old value with the new value if the symbol table already contains the
	 * specified key. Deletes the specified key (and its associated value) from this
	 * symbol table if the specified value is {@code null}.
	 * 
	 * @param p   Point2D the key
	 * @param val the value
	 * @throws IllegalArgumentException if p is or val is null.
	 */
	public void put(Point2D p, Value val) { // associate the value val with point p
		if (p == null || val == null) {
			throw new NullPointerException("Arguments cannot be null.");
		} else {
			this.tree.put(p, val);
		}

	}

	/**
	 * Returns the value associated with the given key.
	 * 
	 * @param p the Point2D
	 * @return the value associated with the given key if the key is in the symbol
	 *         table and {@code null} if the key is not in the symbol table
	 * @throws IllegalArgumentException if {@code key} is {@code null}
	 */
	public Value get(Point2D p) { // value associated with point p
		if (p == null) {
			throw new NullPointerException("Arguments cannot be null.");
		} else {
			return this.tree.get(p);
		}
	}

	/**
	 * Does this symbol table contain the given key?
	 * 
	 * @param p the point2D
	 * @return True if this symbol table contains {@code key} and False otherwise
	 */
	public boolean contains(Point2D p) { // does the symbol table contain point p?
		if (p == null) {
			throw new NullPointerException("Arguments cannot be null.");
		} else {
			return this.tree.contains(p);
		}
	}

	/**
	 * Returns all points in the symbol table as an Iterable. To iterate over all of
	 * the points in the symbol table.
	 * 
	 * @return all keys in the symbol table as an Iterable.
	 */
	public Iterable<Point2D> points() { // all points in the symbol table
		// use an iterator
		return this.tree.keys();

	}

	/**
	 * * Finds all points contained in a given query rectangle.
	 * 
	 * @param rect RectHV to find the points within it.
	 * @return Stack<Point2D> points inside the given rectangle.
	 */
	public Iterable<Point2D> range(RectHV rect) { // all points that are inside the rectangle
		Stack<Point2D> points = new Stack<Point2D>();
		if (rect == null) {
			throw new NullPointerException("Arguments cannot be null.");
		} else {
			for (Point2D point : this.points()) {
				if (rect.contains(point)) {
					points.push(point);
				} else {
					continue;
				}
			}
		}
		return points;
	}

	/**
	 * Finds the closest point to the given query Point2D p. Returns the Point2D
	 * closest to the given query point p.
	 * 
	 * @param p Point2D query point.
	 * @return Point2D closest point in the ST to the Point2D p.
	 */
	public Point2D nearest(Point2D p) { // a nearest neighbor to point p; null if the symbol table is empty
		List<Point2D> points = new ArrayList<Point2D>();
		if (this.tree.isEmpty()) {
			return null;
		} else {
			// iterate through points in the tree
			for (Point2D point : points()) {
				// add the points to an iterable
				points.add(point);
			}
			// sort it with a distancetoorder comparator
			Collections.sort(points, p.distanceToOrder());
			// return the first
			return points.get(0);
		}

	}

	public static void main(String[] args) { // unit testing of the methods (not graded)
		// TODO Auto-generated method stub

		// === Testing empty constructor ===//
		PointST<String> first = new PointST<String>();
		System.out.println(first);

		// === Testing put method === //
		Point2D firstPoint = new Point2D(2.3, 5.4);
		first.put(firstPoint, "hi im first");

		Point2D secondPoint = new Point2D(4.3, 7.4);
		first.put(secondPoint, "hi im second");

		// === Testing get method === //
		System.out.println(first.get(firstPoint));
		System.out.println(first.get(secondPoint));

	}

}
