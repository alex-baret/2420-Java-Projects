/*CSIS2420 A04 P2 Solver
 * Alex Baret
 * 7/10/21
 */

package a04;

import java.util.*;
import java.util.Stack;

import edu.princeton.cs.algs4.*;

/**
 * Utilizes the Board.java class and A* algorithm to find a solution to a
 * 8-Puzzle.
 * 
 * @author alex
 *
 */
public class Solver {

	private MinPQ<SearchNode> gameTree; // this is the priority queue to be used throughout Solver
	private SearchNode root; // this is the "root" node (and further incremented node(s) until the goal board
								// is found
	private Stack<Board> boards; // this is the stack of boards to contain the path to the solution
	private int numMoves = 0;

	/**
	 * Finds a solution to the initial board (using the A* algorithm)
	 * 
	 * @param initial Board.
	 */
	public Solver(Board initial) {
		if (initial == null) {
			throw new NullPointerException("Board cannot be null");
		}
		if (!initial.isSolvable()) {
			throw new IllegalArgumentException("Board cannot be solved");
		} else {
			this.boards = new Stack<Board>();
			this.root = new SearchNode(null, initial); // create a root node
			NodeComparator compare = new NodeComparator(); // create a new comparator to allow the PQ to compare Nodes

			// insert into PQ, which will contain just the root
			gameTree = new MinPQ<SearchNode>(compare);
			gameTree.insert(root);
			boards.push(this.root.board);
			while (!root.board.isGoal()) {
				// if it's not the goal board, check the neighbor boards
				Iterable<Board> neighbors = root.board.neighbors(); // create an interable board set
				for (Board board : neighbors) { // iterate through the neighbor boards
					if (board.equals(root.board)) { // don't add a board that matches the root
						continue;
					} else if (boards.contains(board)) {
						continue;
					} else { // if the board doesn't match the root, add the board
						// create a node for each neighbor board, setting the previous node to the root
						SearchNode neighbor = new SearchNode(root, board);
						gameTree.insert(neighbor);
					}
				}
				// get the lowest priority node and set it to root
				this.root = gameTree.delMin();
				gameTree = new MinPQ<SearchNode>(compare);
				// add the "new root's" board to the stack of boards to the solution
				boards.push(this.root.board);
				numMoves++;
			}

		}

	}

	/**
	 * Comparator for comparing Search Nodes. Asseses which node has a better
	 * priority (lowest amount of moves to the goal board).
	 * 
	 * @author alex
	 *
	 */
	public class NodeComparator implements Comparator<SearchNode> {

		/**
		 * Compares two Search Nodes based off their priorities.
		 * 
		 * @param o1 SearchNode to be compared
		 * @param o2 SearchNode to be compared.
		 * @return int -1, 0, or 1, based off whether o1 is less than, equal, or greater
		 *         than the other respectively.
		 */
		@Override
		public int compare(SearchNode o1, SearchNode o2) {
			Integer priorityOne = o1.priority;
			Integer priorityTwo = o2.priority;
			if (priorityOne < priorityTwo) {
				return -1;
			}
			if (priorityOne > priorityTwo) {
				return 1;
			} else {
				return 0;
			}
		}

	}

	/**
	 * Elements of the Priority Queue. Maintains higher level information than
	 * Board.java to set priorities and compare the rating of boards.
	 * 
	 * @author alex
	 *
	 */
	public class SearchNode {

		private Board board; // board associated with this search node
		private int priority; // sum of hamming method plus numMoves it's taken to get to this board
		private SearchNode previous;// needs a previous pointer to parent node

		/**
		 * Search Node Constructor, creates a new Search Node.
		 * 
		 * @param previous     is the parent Search Node
		 * @param currentBoard is the board to be associated with this Search Node.
		 */
		public SearchNode(SearchNode previous, Board currentBoard) {

			this.board = currentBoard;
			int manhattan = this.board.manhattan();
			this.priority = manhattan;
		}

	}

	/**
	 * Min number of moves to solve initial board. Returns an int containing the
	 * number of moves.
	 * 
	 * @return int moves to solve initial board
	 */
	public int moves() {
		return this.numMoves;
	}

	/**
	 * Returns a sequence of boards for the shortest solution.
	 * 
	 * @return Iterable<Board> containing the boards from initial to goal.
	 */
	public Iterable<Board> solution() {
		return this.boards;

	}

	public static void main(String[] args) {

	}
}