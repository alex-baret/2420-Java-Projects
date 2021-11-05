/*
 * Alex Baret
 * A05 P2 KdTreeST
 * 7/27/21
 */
package a05;

import java.util.Comparator;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.RedBlackBST;
import edu.princeton.cs.algs4.Stack;

/**
 * A mutable data type KdTreeST.java that uses a 2d-tree to implement a symbol
 * table with Point2D. A 2d-tree is a generalization of a BST to two-dimensional
 * keys.
 * 
 * @author alex
 *
 * @param <Value>
 */
public class KdTreeST<Value> { // point2D implements comparable already

	private static final boolean HORIZONTAL = true;
	private static final boolean VERTICAL = false;

	private Node root;
	private Stack<Point2D> points = new Stack<Point2D>();
	private Stack<Point2D> matches = new Stack<Point2D>();
	private Point2D champion;

	/**
	 * KDTree helper Node Data Type.
	 * 
	 * @author alex
	 *
	 */
	private class Node {
		private Point2D p; // the point(x,y) also the (key of ST)
		private Value value; // the ST maps the point to this value
		private RectHV rect; // the axis-aligned rectangle corresponding to this node
		private Node lb; // the left/bottom subtree
		private Node rt; // the right/top subtree
		private boolean orientation; // horizontal/vertical orientation of this node's partition
		private int size;

		/**
		 * Node Data Type Constructor. Creates a new node.
		 * 
		 * @param p           Point2D input parameter initializes the instance variable
		 *                    p.
		 * @param value       Value input parameter initializes the instance variable
		 *                    value.
		 * @param size        int input parameter initializes the instance variable
		 *                    size.
		 * @param orientation boolean input parameter initializes the instance variable
		 *                    orientation.
		 */
		public Node(Point2D p, Value value, int size, boolean orientation) {
			this.p = p;
			this.value = value;
			this.size = size;
			this.orientation = orientation;
		}

	}

	/**
	 * Constructs an empty symbol table of points
	 */
	public KdTreeST() {

	}

	/**
	 * Determines whether the ST is empty. Returns true if the ST is empty, or False
	 * otherwise.
	 * 
	 * @return Boolean True if the KDTreeST is empty, and false otherwise.
	 */
	public boolean isEmpty() {
		return this.root == null;
	}

	/**
	 * Returns the size of the ST. No need to store subtree size, number of points
	 * is from the ST.
	 * 
	 * @return int number of points in the KDTreeST
	 */
	public int size() {
		return size(root);
	}

	/**
	 * Helper method for size. Returns an int containing the number of subtrees for
	 * this node.
	 * 
	 * @param x Node input parameter starts the size calculation. Returns an int
	 *          containing the number of subtrees for this node.
	 * @return int number of subtrees
	 */
	private int size(Node x) {
		if (x == null) {
			return 0;
		} else {
			return x.size;
		}
	}

	/**
	 * Flips the Node's partitioning direction orientation. Returns the opposite
	 * orientation in the form of a boolean.
	 * 
	 * @param orientation boolean True or False, which ultimately represents
	 *                    Horizontal and Vertical respectively.
	 * @return boolean True if the input orientation is False, and False if the
	 *         input orientation was True.
	 */
	private boolean reverseOrient(boolean orientation) {
		if (orientation == true) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Helper method for put(). Determines the orientation of the Node's partition.
	 * Depending on the Node's partition, compares the Point2D p with the Node x's
	 * Point2D's x or y coordinate. Returns a negative, positive, or zero for
	 * greater than, less than, or equal respectively.
	 * 
	 * @param p Point2D to compare with the other input paramater's Point2D.
	 * @param x Node to compare with the other input paramater's Point2D.
	 * @return int positive, negative, or equal, depending on
	 */
	private int customCompare(Point2D p, Node x) {
		if (x.orientation == true) { // orientation is TRUE = HORIZONTAL, compare y values
			Comparator<Point2D> yCompare = Point2D.Y_ORDER;
			int yResult = yCompare.compare(p, x.p);
			// check for same value
			if (yResult == 0) {
				return 1; // place on right side if the same
			} else {
				return yResult;
			}
		} else { // orientation is FALSE = VERTICAL, compare x values
			Comparator<Point2D> xCompare = Point2D.X_ORDER;
			int xResult = xCompare.compare(p, x.p);
			if (xResult == 0) {
				return 1; // place on right side if the same
			} else {
				return xResult;
			}
		}
	}

	/**
	 * Inserts a new element into the ST.
	 * 
	 * @param p   Point2D to be added.
	 * @param val Value to be associated with the Point2D p in the ST.
	 */
	public void put(Point2D p, Value val) { // associate the value val with point p
		if (p == null) {
			throw new NullPointerException("first argument to put() is null");
		} else {
			this.root = put(p, val, this.root, true);
		}
	}

	/**
	 * Put() helper method, recursively adds a new Node to the ST. Generates
	 * rectangle for the Node. Returns a node which ultimately passes to the public
	 * put() method.
	 * 
	 * @param point       Point2D to be added to the ST.
	 * @param val         Value to be associated with the Point2D p in the ST.
	 * @param h           Node, root to start the recursive insert.
	 * @param orientation boolean to pass onto customCompare() method for accurate x
	 *                    or y comparison.
	 * @return Node to be inserted into the ST.
	 */
	private Node put(Point2D point, Value val, Node h, boolean orientation) { // helper method for put
		if (h == null) {
			Node node = new Node(point, val, 1, reverseOrient(orientation));
			node.rect = new RectHV(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY,
					Double.POSITIVE_INFINITY);
			points.push(node.p);

			return node;
		}
		// call customCompare(new Point2D point, Node x)
		// use that value to place items in the tree
		if (point.equals(h.p)) { // if already in the ST
			h.value = val;

			return h;
		} else if (customCompare(point, h) < 0) { // if x or y leads to left-side placement
			// this is to the left
			// get the orientation of node h

			// replace max value for that respective x or y coordinate for the rect
			h.lb = put(point, val, h.lb, reverseOrient(orientation));
			Boolean orientationH = h.orientation;
			// determine the orientation
			double parentXMin = h.rect.xmin();
			double parentXMax = h.rect.xmax();
			double parentYMax = h.rect.ymax();
			double parentYMin = h.rect.ymin();
			if (orientationH == true) { // if h was true, that means horizontal, therefore it's relating Y values
				// get the y value of h's point
				double yMax = h.p.y();
				// set the ymax of h.lb to yMax
				h.lb.rect = new RectHV(parentXMin, parentYMin, parentXMax, yMax);
			} else { // if h was false, that means vertical, therefore it's relating X values
						// get the y value of h's point
				double xMax = h.p.x();
				// set the ymax of h.lb to yMax
				h.lb.rect = new RectHV(parentXMin, parentYMin, xMax, parentYMax);
			}
			h.size = size(h.rt) + size(h.lb) + 1;
			return h;
		} else { // if x or y leads to right side placement

			h.rt = put(point, val, h.rt, reverseOrient(orientation));
			Boolean orientationH = h.orientation;
			double parentXMin = h.rect.xmin();
			double parentXMax = h.rect.xmax();
			double parentYMax = h.rect.ymax();
			double parentYMin = h.rect.ymin();
			if (orientationH == true) { // if h was true, that means horizontal, therefore it's relating Y values
				// get the y value of h's point
				double yMin = h.p.y();
				// set the ymax of h.lb to yMax
				h.rt.rect = new RectHV(parentXMin, yMin, parentXMax, parentYMax);

			} else { // if h was false, that means vertical, therefore it's relating X values
						// get the y value of h's point
				double xMin = h.p.x();
				// set the ymax of h.lb to yMax
				h.rt.rect = new RectHV(xMin, parentYMin, parentXMax, parentYMax);
			}
			h.size = size(h.rt) + size(h.lb) + 1;
			return h;
		}
	}

	/**
	 * Returns the value associated with point p
	 * 
	 * @param p Point2D in which the value is sought.
	 * @return Value of the Point2D passed in as the input parameter.
	 */
	public Value get(Point2D p) {
		return get(p, root, true);
	}

	/**
	 * Helper method for public get() method. Recursively searches the KDTreeST for
	 * the desired point and then returns the value.
	 * 
	 * @param p           Point2D in which the value is sought.
	 * @param h           Node, root to start the search.
	 * @param orientation boolean to keep track of compare orientations of vertical
	 *                    or horizontal.
	 * @return Value of the Point2D or null if it does not exist in the ST.
	 */
	private Value get(Point2D p, Node h, boolean orientation) { // helper method for get
		if (h == null) {
			return null;
		}
		int cmp = p.compareTo(h.p); // generally comparing
		if (cmp == 0) { // ok for exact x and y matches
			return h.value;
		} else if (customCompare(p, h) < 0) { // if x or y leads to left-side placement) {
			return get(p, h.lb, reverseOrient(orientation));
		} else {
			return get(p, h.rt, reverseOrient(orientation));
		}
	}

	/**
	 * Determines whether the input parameter Point2D p is in the ST. Returns True
	 * if it does, False otherwise.
	 * 
	 * @param p Point2D to be determined whether it exists or not.
	 * @return Boolean True if the Point2D p is in the ST and False otherwise.
	 */
	public boolean contains(Point2D p) { // does the symbol table contain point p?
		return get(p) != null;
	}

	/**
	 * Returns all Point2D points in the ST.
	 * 
	 * @return Stack<Point2D> in the ST.
	 */
	public Iterable<Point2D> points() {
		return this.points;

	}

	/**
	 * Finds all points contained in a given query rectangle by starting at the root
	 * and recursively search for points in both subtrees using the following
	 * pruning rule: if the query rectangle does not intersect the rectangle
	 * corresponding to a node, there is no need to explore that node (or its
	 * subtrees).
	 * 
	 * @param rect RectHV to find the points within it.
	 * @return Stack<Point2D> points inside the given rectangle.
	 */
	public Iterable<Point2D> range(RectHV rect) {
		if (rect == null) {
			throw new NullPointerException("Arguments cannot be null.");
		} else {
			this.matches = new Stack<Point2D>();
			if (root == null) {
				return matches;
			} else {
				range(root, rect);
				return matches;
			}
		}
	}

	/**
	 * Helper method for public range() method. Recursively searches for points in
	 * both subtrees using the pruning rule.
	 * 
	 * @param h    Node, root to start the search.
	 * @param rect RectHV that the points are to be contained within.
	 * @return Stack<Point2D> points contained in the rectangle.
	 */
	private Iterable<Point2D> range(Node h, RectHV rect) {
		if (h == null) {
			return matches;
		} else {
			if (!h.rect.intersects(rect)) { // if the root's rectangle doesn't intersect, stop the search
				return matches;
			} else { // else search
				if (rect.contains(h.p)) {
					matches.push(h.p);
				}
				range(h.lb, rect);
				range(h.rt, rect);
				return matches;
			}
		}
	}

	/**
	 * Finds the closest point to the given query Point2D p. Returns the Point2D
	 * closest to the given query point p.
	 * 
	 * @param p Point2D query point.
	 * @return Point2D closest point in the ST to the Point2D p.
	 */
	public Point2D nearest(Point2D p) { // a nearest neighbor to point p; null if the symbol table is empty

		if (root == null) {
			return null;
		} else {
			double x = this.root.p.x();
			double y = this.root.p.y();
			this.champion = new Point2D(x, y);
			nearest(root, p);
			return this.champion;
		}

	}

	/**
	 * Helper method for public nearest() method. Returns Point2D p closest to the
	 * query point.
	 * 
	 * @param h Node, root to start the search.
	 * @param p Point2D, query point to find the closest point to.
	 * @return Point2D closest to the query point p.
	 */
	private Point2D nearest(Node h, Point2D p) {
		if (h == null) {
			return this.champion;
		} else {
			// check the rectangle of node h, and see if it contains a closer point than
			// current champion
			if (h.rect.distanceSquaredTo(p) < this.champion.distanceSquaredTo(p)) {
				// if it can, check the distance of node h's point to p
				if (h.p.distanceSquaredTo(p) < this.champion.distanceSquaredTo(p)) {
					this.champion = h.p; // update if closer, else proceed
				}
				// check the left subtree by calling nearest with the subtree's node and p
				nearest(h.lb, p);
				// check the right subtree by calling nearest with the subtree's node and p
				nearest(h.rt, p);
				return this.champion;
			} else { // if the rectangle of node h cannot contain a point closer to the current
				return this.champion;
			}

		}
	}

	public static void main(String[] args) {
		// === Testing empty constructor and empty method ===//
		KdTreeST<Integer> tree = new KdTreeST<>();
		System.out.println(tree.isEmpty());

		// === Testing put method === //
		Point2D[] points = { new Point2D(2, 3), new Point2D(1, 5), new Point2D(4, 2), new Point2D(4, 5),
				new Point2D(3, 3), new Point2D(4, 4) };
		int i = 0;
		for (Point2D point : points) {
			i += 1;
			tree.put(point, i);
		}

		// === Testing get method === //
		Point2D exisitingPoint1 = new Point2D(2, 3);
		Point2D exisitingPoint2 = new Point2D(4, 4);
		System.out.println(tree.get(exisitingPoint1));
		System.out.println(tree.get(exisitingPoint2));

		Point2D nullPoint = new Point2D(10.0, 0.0);
		System.out.println(tree.get(nullPoint));

		// === Testing points() method === //
		for (Point2D point : points) {
			System.out.println(point.toString());
		}

		// === Testing range() method === //
		tree.range(new RectHV(1.3, 2.4, 3.6, 3.6));
		for (Point2D point : tree.matches) {
			System.out.println(point.toString());
		}

		tree.range(new RectHV(1, 1, 2, 2));
		for (Point2D point : tree.matches) {
			System.out.println(point.toString());
		}

	}

}
