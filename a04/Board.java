/*CSIS2420 A04 P1 Board
 * Alex Baret
 * 7/7/21
 */

package a04;

import edu.princeton.cs.algs4.Stack;

public class Board {

	private int[][] board;
	int[] flatBoard;
	int[] idealBoard;
	private int boardSize;

	/**
	 * Construct a board from an N-by-N array of blocks (where blocks[i][j] = block
	 * in row i, column j)
	 * 
	 * @param blocks
	 */
	public Board(int[][] blocks) {
		this.boardSize = blocks.length;
		this.board = blocks;

		// initializing new flatBoard
		this.flatBoard = new int[blocks.length * blocks.length];

		// transfer 2d array to 1d array
		int flatIndex = 0;
		this.flatBoard[this.flatBoard.length - 1] = 0;
		for (int row = 0; row < this.boardSize; row++) {
			for (int col = 0; col < this.boardSize; col++) {
				this.flatBoard[flatIndex] = this.board[row][col];
				flatIndex++;
			}
		}

		// creating ideal board
		this.idealBoard = new int[this.boardSize * this.boardSize];
		idealBoard[idealBoard.length - 1] = 0;
		for (int index = 1; index < this.boardSize * this.boardSize; index++) {
			idealBoard[index - 1] = index;
		}
	}

	/**
	 * Constructs a copy of a board.
	 * 
	 * @param original Board is the Board object to be copied.
	 */
	private Board(Board original) {

		// copying boardSize
		this.boardSize = original.boardSize;

		// copying blocks
		this.board = new int[original.boardSize][original.boardSize];
		for (int row = 0; row < original.boardSize; row++) {
			for (int col = 0; col < original.boardSize; col++) {
				int block = original.board[row][col];
				this.board[row][col] = block;
			}
		}

		// copying flatBoard
		// initializing new flatBoard
		this.flatBoard = new int[this.board.length * board.length];

		// transfer 2d array to 1d array
		int flatIndex = 0;
		this.flatBoard[this.flatBoard.length - 1] = 0;
		for (int row = 0; row < this.boardSize; row++) {
			for (int col = 0; col < this.boardSize; col++) {
				this.flatBoard[flatIndex] = this.board[row][col];
				flatIndex++;
			}
		}

		// creating ideal board
		this.idealBoard = new int[this.boardSize * this.boardSize];
		idealBoard[idealBoard.length - 1] = 0;
		for (int index = 1; index < this.boardSize * this.boardSize; index++) {
			idealBoard[index - 1] = index;
		}

	}

	/**
	 * Calls the copy Board constructor, returns a new Board copied from the input
	 * parameter
	 * 
	 * @param original
	 * @return a new Board with the same values as original but different references
	 */
	private static Board boardCopier(Board original) {
		return new Board(original);
	}

	/**
	 * Board size N
	 * 
	 * @return int boardSize
	 */
	public int size() {
		return this.boardSize;
	}

	/**
	 * Compares with ideal board and enumerates the number of differences between
	 * the two. Number of differences are the number of squares out of place
	 * compared to ideal array
	 * 
	 * @return int number of blocks out of place
	 */
	public int hamming() {
		// determine how many blocks are out of place (compare the ideal to actual) not
		// counting zero because that's the blank square
		int numOff = 0;
		for (int index = 0; index < this.idealBoard.length; index++) {
			if (this.flatBoard[index] != this.idealBoard[index] && this.flatBoard[index] != 0) {
				numOff++;
			}
		}
		return numOff;
	}

	/**
	 * Calculates sum of Manhattan distances between blocks and goal.
	 * 
	 * @return int sum of Manhattan distances between blocks and goal
	 */
	public int manhattan() {
		int manhattenTotal = 0;
		// collecting off sites in stack
		for (int index = 0; index < idealBoard.length; index++) {
			if (this.flatBoard[index] != this.idealBoard[index] && this.flatBoard[index] != 0) { // no match
				int offSite = this.flatBoard[index];
				// find the correct index of this item in idealBoard
				for (int trueIndex = 0; trueIndex < idealBoard.length; trueIndex++) {
					if (idealBoard[trueIndex] == offSite) {
						// subtract the idealBoard index from the flatBoard index
						int difference = trueIndex - index;
						difference = Math.abs(difference);
						// if the difference is greater than N, divide by N-1 and add the remainder,
						if (difference > this.boardSize) {
							int manhattenTemp = difference / this.boardSize;
							manhattenTemp += difference % this.boardSize;
							manhattenTotal += manhattenTemp;
						} else { // otherwise it's just the difference
							manhattenTotal += difference;
						}
					}
				}

			}
		}

		return manhattenTotal;
	}

	/**
	 * Utilizes hamming method to determine whether the calling object is the goal
	 * board or not. Returns Boolean true or false.
	 * 
	 * @return boolean true or false determining whether the calling object is the
	 *         goal board or not
	 */
	public boolean isGoal() {
		if (this.hamming() == 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @return int number of inversions on the board
	 */
	private int inversions() {
		int numInversions = 0;
		// store last item in the board
		for (int index = flatBoard.length - 1; index >= 0; index--) {
			for (int compared = index - 1; compared >= 0; compared--) {
				if (this.flatBoard[index] < this.flatBoard[compared] && this.flatBoard[index] != 0
						&& this.flatBoard[compared] != 0) {
					numInversions++;
				}
			}
		}
		// loop through the board and count how many indicies are smaller than it
		// increment item to be checked against until the index is the end of the array
		// if it's zero then don't count it and just continue the iteration

		return numInversions;

	}

	// is this board solvable?
	/**
	 * Determines whether a board is solvable or not. Utilizes private method
	 * inversions() to calculate number of inversions. Returns a boolean true/false.
	 * 
	 * @return boolean true/false whether the board is solvable or not.
	 */
	public boolean isSolvable() {
		// ====== ODD BOARD LOGIC ===== //
		// horizontal moves change the number of inversions by zero
		// vertical moves change the number of inversions by zero or two
		// goal board has zero inversions

		// odd board sizes, if we can only change the number of inversions by two at a
		// time, if the number of
		// initial inversions is odd, then it's not solvable, because the number of
		// inversions will never get to zero

		// general steps
		// get the number of inversions
		int inversions = this.inversions();

		// ====== ODD BOARD STEPS ===== //
		if (this.boardSize % 2 != 0) {
			if (inversions % 2 != 0) { // if it's odd then it can't be solved
				return false;
			} else { // if it's even or zero, or anything but odd it can be solved
				return true;
			}
		}
		// ====== EVEN BOARD STEPS ===== //
		else { // if it's not odd, then it's even
			int row = 0;
			// get the row of the zero
			for (int index = 0; index < this.flatBoard.length; index++) {
				if (this.flatBoard[index] == 0) {
					if (index < this.boardSize) {
						row = 0;
					} else {
						row = index / this.boardSize;
					}
				}
			}
			// calculate row + inversions
			int sum = inversions + row;
			// determine if it's even or odd
			if (sum % 2 == 0 || sum == 0) { // even
				return false;
			} else {
				return true;
			}
		}

		// ====== EVEN BOARD LOGIC ===== //
		// even board sizes
		// track number of inversions
		// --> will have to be derived
		// track row number that the blank element resides
		// add those and compute the sum
		// horizontal moves doesn't change inversions or row number
		// vertical moves
		// --> row number changes by 1 or -1
		// --> inversion number changes by -3,-1,1,or 3
		// if sum is even, board is not solvable

	}

	/**
	 * Determines board equality.
	 * 
	 * @return is a boolean whether the calling object equals the input parameter.
	 */
	public boolean equals(Object y) {

		// check reference, if it's the same return true
		if (this == y) {
			return true;
		}
		// if argument is null return true
		if (y == null) {
			return false;
		}
		// if objects are not from the same class return false
		if (this.getClass() != y.getClass()) {
			return false;
		}
		Board that = (Board) y; // casting the argument from Object to Board check
		// === instance variable(s) check === //
		if (this.size() != that.size()) {
			return false;
		}
		// board check
		for (int row = 0; row < this.boardSize; row++) {
			for (int col = 0; col < this.boardSize; col++) {
				if (this.board[row][col] != that.board[row][col]) {
					return false;
				}
			}
		}
		// flatBoard check
		for (int index = 0; index < this.boardSize; index++) {
			if (this.flatBoard[index] != that.flatBoard[index]) {
				return false;
			}
		}
		// boardSize check
		if (this.boardSize != that.boardSize) {
			return false;
		}
		// idealBoard check
		for (int index = 0; index < this.boardSize; index++) {
			if (this.idealBoard[index] != that.idealBoard[index]) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Rebuilds flatBoard for calling object.
	 * 
	 * @param blocks is the 2D blocks array for the
	 */
	private void rebuildFlat() {

		// transfer 2d array to 1d array with new values from board
		int flatIndex = 0;
		this.flatBoard[this.flatBoard.length - 1] = 0;
		for (int row = 0; row < this.boardSize; row++) {
			for (int col = 0; col < this.boardSize; col++) {
				this.flatBoard[flatIndex] = this.board[row][col];
				flatIndex++;
			}
		}
	}

	/**
	 * Determines where blank square location is, and creates and returns a board
	 * that contains a move of the blank square to all possible adjacent squares.
	 * Returns an Iterable<Board> of 'neighbors'.
	 * 
	 * @return Iterable<Board> of 'neighbors' which are all possible adjacent moves
	 *         for the blank square.
	 */
	public Iterable<Board> neighbors() {

		// creating a new stack of boards
		Stack<Board> neighbors = new Stack<Board>();
//		neighbors.push(this); // adding current board to stack
		// has as many as four neighbors (all boards with zero placed in an adjacent
		// square)
		// return an iterable of the board and it's neighbors

		// determine where the zero block is
		int zeroRow = 0;
		int zeroCol = 0;
		for (int row = 0; row < this.boardSize; row++) {
			for (int col = 0; col < this.boardSize; col++) {
				if (this.board[row][col] == 0) {
					zeroRow = row;
					zeroCol = col;
				}
			}
		}
		// ===== generate new boards around it ===== //

		// check for size 1 board
		if (this.boardSize < 2) {
			return neighbors;
		}

		// ******* top left ******* //
		if (zeroRow == 0 && zeroCol == 0) {

			// ~~~ create +row, = col board ~~~ //
			Board neighbor1 = boardCopier(this); // copy of original with same indices
			// switch item to the right and index 0 0
			int oldNonZero = neighbor1.board[zeroRow + 1][0]; // storing new blank position's old item
			neighbor1.board[zeroRow + 1][0] = 0; // setting new blank position to blank
			neighbor1.board[zeroRow][zeroCol] = oldNonZero; // setting old blank position to oldNonZero item
			// update flatBoard for first neighbor boardCopy
			neighbor1.rebuildFlat();
			// everything is updated, push to stack
			neighbors.push(neighbor1);

			// ~~~ create +col, = row board ~~~ //
			Board neighbor2 = boardCopier(this); // copy of original with same indices
			// switch item to the right and index 0 0
			int oldNonZero2 = neighbor2.board[0][1]; // storing new blank position's old item
			neighbor2.board[0][1] = 0; // setting new blank position to blank
			neighbor2.board[0][0] = oldNonZero2; // setting old blank position to oldNonZero item
			// update flatBoard for first neighbor boardCopy
			neighbor2.rebuildFlat();
			// everything is updated, push to stack
			neighbors.push(neighbor2);
			return neighbors;
		}

		// ******* top right ******* //
		if (zeroRow == 0 && zeroCol == this.boardSize - 1) {

			// ~~~ create +row, = col board ~~~ //
			Board neighbor2 = boardCopier(this); // copy of original with same indices
			// switch item to the right and index 0 0
			int oldNonZero2 = neighbor2.board[1][this.boardSize - 1]; // row 1, col n-1 (below top right)
			neighbor2.board[1][this.boardSize - 1] = 0; // setting new blank position to blank
			neighbor2.board[0][this.boardSize - 1] = oldNonZero2; // setting old blank position to oldNonZero item
			// update flatBoard for first neighbor boardCopy
			neighbor2.rebuildFlat();
			// everything is updated, push to stack
			neighbors.push(neighbor2);

			// ~~~ create -col, = row board ~~~ //
			Board neighbor1 = boardCopier(this); // copy of original with same indices
			// switch item with blank
			int oldNonZero = neighbor1.board[zeroRow][zeroCol - 1]; // storing new blank position's old item
			neighbor1.board[zeroRow][zeroCol - 1] = 0; // setting new blank position to blank
			neighbor1.board[zeroRow][zeroCol] = oldNonZero; // setting old blank position to oldNonZero item
			// update flatBoard for first neighbor boardCopy
			neighbor1.rebuildFlat();
			// everything is updated, push to stack
			neighbors.push(neighbor1);

			return neighbors;
		}

		// ******* bottom right ******* //
		if (zeroRow == this.boardSize - 1 && zeroCol == this.boardSize - 1) {

			// ~~~ create -col, = row board ~~~ //
			Board neighbor1 = boardCopier(this); // copy of original with same indices
			// switch item with blank
			int oldNonZero = neighbor1.board[this.boardSize - 1][this.boardSize - 2]; // storing new blank position's
																						// old item
			neighbor1.board[this.boardSize - 1][this.boardSize - 2] = 0; // setting new blank position to blank
			neighbor1.board[this.boardSize - 1][this.boardSize - 1] = oldNonZero; // setting old blank position to
																					// oldNonZero item
			// update flatBoard for first neighbor boardCopy
			neighbor1.rebuildFlat();
			// everything is updated, push to stack
			neighbors.push(neighbor1);

			// ~~~ create -row, = col board ~~~ //
			Board neighbor2 = boardCopier(this); // copy of original with same indices
			// switch item to the right and index 0 0
			int oldNonZero2 = neighbor2.board[this.boardSize - 2][this.boardSize - 1];
			neighbor2.board[this.boardSize - 2][this.boardSize - 1] = 0; // setting new blank position to blank
			neighbor2.board[this.boardSize - 1][this.boardSize - 1] = oldNonZero2; // setting old blank position to
																					// oldNonZero item
			// update flatBoard for first neighbor boardCopy
			neighbor2.rebuildFlat();
			// everything is updated, push to stack
			neighbors.push(neighbor2);
			return neighbors;
		}

		// ******* bottom left ******* //
		if (zeroRow == this.boardSize - 1 && zeroCol == 0) {

			// ~~~ create -row, = col board ~~~ //
			Board neighbor1 = boardCopier(this); // copy of original with same indices
			// switch item with blank
			int oldNonZero = neighbor1.board[this.boardSize - 2][0]; // storing new blank position's
																		// old item
			neighbor1.board[this.boardSize - 2][0] = 0; // setting new blank position to blank
			neighbor1.board[this.boardSize - 1][0] = oldNonZero; // setting old blank position to
																	// oldNonZero item
			// update flatBoard for first neighbor boardCopy
			neighbor1.rebuildFlat();
			// everything is updated, push to stack
			neighbors.push(neighbor1);

			// ~~~ create +col, = row board ~~~ //
			Board neighbor2 = boardCopier(this); // copy of original with same indices
			// switch item to the right and index 0 0
			int oldNonZero2 = neighbor2.board[this.boardSize - 1][1]; // row n-1, col 2
			neighbor2.board[this.boardSize - 1][1] = 0; // setting new blank position to blank
			neighbor2.board[this.boardSize - 1][0] = oldNonZero2; // setting old blank position to
																	// oldNonZero item
			// update flatBoard for first neighbor boardCopy
			neighbor2.rebuildFlat();
			// everything is updated, push to stack
			neighbors.push(neighbor2);
			return neighbors;
		}

		// ******* rightmost column ******* //

		if (zeroCol == this.boardSize - 1 && (zeroRow != 0 && zeroRow != this.boardSize - 1)) {

			// ~~~ create -col, = row board ~~~ //
			// this is to the left of zeroLocation
			Board neighbor2 = boardCopier(this); // copy of original with same indices
			int oldNonZero2 = neighbor2.board[zeroRow][zeroCol - 1]; // same row, one column left (-1)
			neighbor2.board[zeroRow][zeroCol - 1] = 0; // setting new blank position to blank
			neighbor2.board[zeroRow][zeroCol] = oldNonZero2; // setting old blank position to
																// oldNonZero item
			// update flatBoard for first neighbor boardCopy
			neighbor2.rebuildFlat();
			// everything is updated, push to stack
			neighbors.push(neighbor2);

			// ~~~ create -row, = col board ~~~ //
			Board neighbor1 = boardCopier(this); // copy of original with same indices
			// above zero location
			int oldNonZero = neighbor1.board[zeroRow - 1][zeroCol]; // storing item above
			neighbor1.board[zeroRow - 1][zeroCol] = 0; // setting item above to blank
			neighbor1.board[zeroRow][zeroCol] = oldNonZero; // setting original blank to oldNonZero
			// update flatBoard for first neighbor boardCopy
			neighbor1.rebuildFlat();
			// everything is updated, push to stack
			neighbors.push(neighbor1);

			// ~~~ create +row, = col board ~~~ //
			// this is below the location of blank
			Board neighbor3 = boardCopier(this); // copy of original with same indices
			int oldNonZero3 = neighbor3.board[zeroRow + 1][zeroCol]; // one row lower, same column
			neighbor3.board[zeroRow + 1][zeroCol] = 0; // setting new blank position to blank
			neighbor3.board[zeroRow][zeroCol] = oldNonZero3; // setting old blank position to
																// oldNonZero item
			// update flatBoard for first neighbor boardCopy
			neighbor3.rebuildFlat();
			// everything is updated, push to stack
			neighbors.push(neighbor3);

			return neighbors;
		}

		// ******* top row ******* //

		if (zeroRow == 0 && (zeroCol != 0 && zeroCol != this.boardSize - 1)) {

			// ~~~ create -col, = row board ~~~ //
			Board neighbor1 = boardCopier(this); // copy of original with same indices
			int oldNonZero = neighbor1.board[zeroRow][zeroCol - 1]; // storing item to the left
			neighbor1.board[zeroRow][zeroCol - 1] = 0; // setting item to left to blank
			neighbor1.board[zeroRow][zeroCol] = oldNonZero; // setting original blank to oldNonZero
			// update flatBoard for first neighbor boardCopy
			neighbor1.rebuildFlat();
			// everything is updated, push to stack
			neighbors.push(neighbor1);

			// ~~~ create +row, = col board ~~~ //
			// this is to the left of zeroLocation
			Board neighbor2 = boardCopier(this); // copy of original with same indices
			int oldNonZero2 = neighbor2.board[zeroRow + 1][zeroCol]; // same column, one row below
			neighbor2.board[zeroRow + 1][zeroCol] = 0; // setting new blank position to blank
			neighbor2.board[zeroRow][zeroCol] = oldNonZero2; // setting old blank position to
			neighbor2.rebuildFlat();
			neighbors.push(neighbor2);

			// ~~~ create +col, = row board ~~~ //
			// this is to the left of zeroLocation
			Board neighbor3 = boardCopier(this); // copy of original with same indices
			int oldNonZero3 = neighbor3.board[zeroRow][zeroCol + 1]; // storing item to the right
			neighbor3.board[zeroRow][zeroCol + 1] = 0; // setting new blank position to blank
			neighbor3.board[zeroRow][zeroCol] = oldNonZero3; // setting old blank position to
																// oldNonZero item
			// update flatBoard for first neighbor boardCopy
			neighbor3.rebuildFlat();
			// everything is updated, push to stack
			neighbors.push(neighbor3);
			return neighbors;
		}

		// ******* bottom row ******* //
		if (zeroRow == this.boardSize - 1 && (zeroCol != 0 && zeroCol != this.boardSize - 1)) {

			// ~~~ create -col, = row board ~~~ //
			Board neighbor1 = boardCopier(this); // copy of original with same indices
			int oldNonZero = neighbor1.board[zeroRow][zeroCol - 1]; // storing item to the left
			neighbor1.board[zeroRow][zeroCol - 1] = 0; // setting item to left to blank
			neighbor1.board[zeroRow][zeroCol] = oldNonZero; // setting original blank to oldNonZero
			// update flatBoard for first neighbor boardCopy
			neighbor1.rebuildFlat();
			// everything is updated, push to stack
			neighbors.push(neighbor1);

			// ~~~ create -row, = col board ~~~ //
			// this is to the left of zeroLocation
			Board neighbor2 = boardCopier(this); // copy of original with same indices
			int oldNonZero2 = neighbor2.board[zeroRow - 1][zeroCol]; // same column, one row below
			neighbor2.board[zeroRow - 1][zeroCol] = 0; // setting new blank position to blank
			neighbor2.board[zeroRow][zeroCol] = oldNonZero2; // setting old blank position to
			neighbor2.rebuildFlat();
			neighbors.push(neighbor2);

			// ~~~ create +col, = row board ~~~ //
			// this is to the left of zeroLocation
			Board neighbor3 = boardCopier(this); // copy of original with same indices
			int oldNonZero3 = neighbor3.board[zeroRow][zeroCol + 1]; // storing item to the right
			neighbor3.board[zeroRow][zeroCol + 1] = 0; // setting new blank position to blank
			neighbor3.board[zeroRow][zeroCol] = oldNonZero3; // setting old blank position to
																// oldNonZero item
			// update flatBoard for first neighbor boardCopy
			neighbor3.rebuildFlat();
			// everything is updated, push to stack
			neighbors.push(neighbor3);
			return neighbors;
		}

		// ******* leftmost column ******* //
		if (zeroCol == 0 && (zeroRow != 0 && zeroRow != this.boardSize - 1)) {

			// ~~~ create -row, = col board ~~~ //
			Board neighbor1 = boardCopier(this); // copy of original with same indices
			// switch item with blank
			int oldNonZero = neighbor1.board[zeroRow - 1][zeroCol]; // storing item above
			neighbor1.board[zeroRow - 1][zeroCol] = 0; // setting item above to blank
			neighbor1.board[zeroRow][zeroCol] = oldNonZero; // setting original blank to oldNonZero
			// update flatBoard for first neighbor boardCopy
			neighbor1.rebuildFlat();
			// everything is updated, push to stack
			neighbors.push(neighbor1);

			// ~~~ create +col, = row board ~~~ //
			// this is to the left of zeroLocation
			Board neighbor2 = boardCopier(this); // copy of original with same indices
			int oldNonZero2 = neighbor2.board[zeroRow][zeroCol + 1]; // same row, one column left (-1)
			neighbor2.board[zeroRow][zeroCol + 1] = 0; // setting new blank position to blank
			neighbor2.board[zeroRow][zeroCol] = oldNonZero2; // setting old blank position to
																// oldNonZero item
			// update flatBoard for first neighbor boardCopy
			neighbor2.rebuildFlat();
			// everything is updated, push to stack
			neighbors.push(neighbor2);

			// ~~~ create +row, = col board ~~~ //
			// this is to the left of zeroLocation
			Board neighbor3 = boardCopier(this); // copy of original with same indices
			int oldNonZero3 = neighbor3.board[zeroRow + 1][zeroCol]; // one row lower, same column
			neighbor3.board[zeroRow + 1][zeroCol] = 0; // setting new blank position to blank
			neighbor3.board[zeroRow][zeroCol] = oldNonZero3; // setting old blank position to
																// oldNonZero item
			// update flatBoard for first neighbor boardCopy
			neighbor3.rebuildFlat();
			// everything is updated, push to stack
			neighbors.push(neighbor3);
			return neighbors;
		}

		else {

			// ~~~ create -row, = col board ~~~ //
			Board neighbor1 = boardCopier(this); // copy of original with same indices
			// switch item with blank
			int oldNonZero = neighbor1.board[zeroRow - 1][zeroCol]; // storing item above
			neighbor1.board[zeroRow - 1][zeroCol] = 0; // setting item above to blank
			neighbor1.board[zeroRow][zeroCol] = oldNonZero; // setting original blank to oldNonZero
			// update flatBoard for first neighbor boardCopy
			neighbor1.rebuildFlat();
			// everything is updated, push to stack
			neighbors.push(neighbor1);

			// ~~~ create +row, = col board ~~~ //
			// this is to the left of zeroLocation
			Board neighbor3 = boardCopier(this); // copy of original with same indices
			int oldNonZero3 = neighbor3.board[zeroRow + 1][zeroCol]; // one row lower, same column
			neighbor3.board[zeroRow + 1][zeroCol] = 0; // setting new blank position to blank
			neighbor3.board[zeroRow][zeroCol] = oldNonZero3; // setting old blank position to
																// oldNonZero item
			// update flatBoard for first neighbor boardCopy
			neighbor3.rebuildFlat();
			// everything is updated, push to stack
			neighbors.push(neighbor3);

			// ~~~ create -col, = row board ~~~ //
			// this is to the left of zeroLocation
			Board neighbor4 = boardCopier(this); // copy of original with same indices
			int oldNonZero4 = neighbor4.board[zeroRow][zeroCol - 1]; // same row, one column left (-1)
			neighbor4.board[zeroRow][zeroCol - 1] = 0; // setting new blank position to blank
			neighbor4.board[zeroRow][zeroCol] = oldNonZero4; // setting old blank position to
																// oldNonZero item
			// update flatBoard for first neighbor boardCopy
			neighbor4.rebuildFlat();
			// everything is updated, push to stack
			neighbors.push(neighbor4);

			// ~~~ create +col, = row board ~~~ //
			// this is to the left of zeroLocation
			Board neighbor2 = boardCopier(this); // copy of original with same indices
			int oldNonZero2 = neighbor2.board[zeroRow][zeroCol + 1]; // same row, one column left (-1)
			neighbor2.board[zeroRow][zeroCol + 1] = 0; // setting new blank position to blank
			neighbor2.board[zeroRow][zeroCol] = oldNonZero2; // setting old blank position to
																// oldNonZero item
			// update flatBoard for first neighbor boardCopy
			neighbor2.rebuildFlat();
			// everything is updated, push to stack
			neighbors.push(neighbor2);

			return neighbors;
		}

	}

//	public String toString() {
//		String s = String.format("%2d " , this.boardSize);
//		for (int row = 0; row < this.boardSize; row++) {
//			s += "\n";
//			for (int col = 0; col < this.boardSize; col++) {
//				s+= String.format(" %2d" , this.board[row][col]);
//			}
//		}
//		s += "\n";
//		return s;
//	}

	// string representation of this board (in the output format specified below)
	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append(this.boardSize + "\n");
		int k = 0;
		for (int i = 0; i < this.boardSize; i++) {
			for (int j = 0; j < this.boardSize; j++) {
				s.append(String.format("%2d ", this.flatBoard[k]));
				k++;
			}
			s.append("\n");
		}
		return s.toString();
	}

	// unit tests (not graded)
	public static void main(String[] args) {
//
		// testing constructor
		System.out.println("This is the initial board: ");
		int N = 3;
		int[][] sizeN = new int[N][N]; // argument to be passed in
		int num = 1;
		sizeN[N - 1][N - 1] = 0;
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if (i == N - 1 && j == N - 1) {
					continue;
				} else {
					sizeN[i][j] = num;
					num++;
				}
			}
		}
		Board boardOne = new Board(sizeN);
//
//		// ====== testing size method ====== //
//		System.out.println("\nThis is the board size: " + boardOne.size());
//
//		// ====== testing hamming method ====== //
//		System.out.println("Hamming method returns: " + boardOne.hamming() + " and should be: 0");
//		int[][] arrTwo = { { 1, 2, 3 }, { 4, 5, 7 }, { 6, 8, 0 } };
//		Board boardTwo = new Board(arrTwo);
//		System.out.println("Hamming method returns: " + boardTwo.hamming() + " and should be: 2");
//
		/*
		 * testing isGoal (need to comment out second hamming test, otherwise it's using
		 * an edited board which will return false
		 */
		System.out.println(boardOne.isGoal());
//
//		// ====== testing manhattan distance ====== //
//		int[][] arrThree = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 0, 8 } };
//		Board boardThree = new Board(arrThree);
//		System.out.println("Manhatten method returns: " + boardThree.manhattan() + " and should be: 1");
//
//		int[][] arrFour = { { 1, 2, 3 }, { 0, 5, 4 }, { 7, 6, 8 } };
//		Board boardFour = new Board(arrFour);
//		System.out.println("Manhatten method returns: " + boardFour.manhattan() + " and should be: 5");
//		System.out.println("Manhatten method returns: " + boardOne.manhattan() + " and should be: 0");
//
//		// ====== testing inversions method ====== //
//		System.out.println("Inversions returns: " + boardFour.inversions() + " and should be: 2");
//
//		int[][] arrFive = { { 1, 3, 2 }, { 0, 5, 4 }, { 7, 8, 6 } };
//		Board boardFive = new Board(arrFive);
//		System.out.println("Inversions returns: " + boardFive.inversions() + " and should be: 4");
//
//		// ====== testing isSolvable ====== //
//
//		// odd board, even inversions
//		System.out.println("isSolvable: " + boardFive.isSolvable() + " and should be: true");
//
//		// odd board, odd inversions
//		int[][] arrSix = { { 1, 2, 3 }, { 0, 5, 4 }, { 7, 8, 6 } };
//		Board boardSix = new Board(arrSix);
//		System.out.println("isSolvable: " + boardSix.isSolvable() + " and should be: false");
//
//		// even board, inversions + row = odd
//		int[][] arrSeven = { { 1, 2, 3, 4 }, { 5, 0, 6, 8 }, { 9, 10, 7, 11 }, { 13, 14, 15, 12 } };
//		Board boardSeven = new Board(arrSeven);
//		System.out.println("isSolvable: " + boardSeven.isSolvable() + " and should be: true");
//
//		// even board, inversions + row = even
//		int[][] arrEight = { { 1, 3, 2, 4 }, { 5, 0, 6, 8 }, { 9, 10, 7, 11 }, { 13, 14, 15, 12 } };
//		Board boardEight = new Board(arrEight);
//		System.out.println("isSolvable: " + boardEight.isSolvable() + " and should be: false");
//
//		// ====== testing boardCopier ====== //
//
//		Board newBoard = boardCopier(boardEight);
//		System.out.println(newBoard.equals(boardEight));
//		System.out.println(newBoard.boardSize);
//		System.out.println(boardEight.boardSize);
//		System.out.println(newBoard.flatBoard);
//		for (int i = 0; i < newBoard.flatBoard.length; i++) {
//			System.out.println(i);
//		}
//		System.out.println(boardEight.flatBoard);
//		for (int i = 0; i < boardEight.flatBoard.length; i++) {
//			System.out.println(i);
//		}
//
//		// ====== testing neighbors top left ====== //
//		System.out.println("\nNeighbors Top Left Test:");
//
//		int[][] arrNeighbor = { { 0, 1, 2 }, { 3, 4, 5 }, { 6, 7, 8 } };
//		Board boardNeighbor = new Board(arrNeighbor);
//		Iterable<Board> neighbors = boardNeighbor.neighbors();
//		int n = 3;
//		for (Board neighbor : neighbors) {
//			System.out.println("This is board number " + n + ": " + Arrays.toString(neighbor.flatBoard));
//			n--;
//		}
//
//		// ====== testing neighbors top right ====== //
//		System.out.println("\nNeighbors Top Right Test:");
//		int[][] arrNeighbor2 = { { 1, 2, 0 }, { 3, 4, 5 }, { 6, 7, 8 } };
//		Board boardNeighbor2 = new Board(arrNeighbor2);
//		Iterable<Board> neighbors2 = boardNeighbor2.neighbors();
//		int n2 = 3;
//		for (Board neighbor2 : neighbors2) {
//			System.out.println("This is board number " + n2 + ": " + Arrays.toString(neighbor2.flatBoard));
//			n2--;
//		}
//
//		// ====== testing neighbors bottom right ====== //
//		System.out.println("\nNeighbors Bottom Right Test:");
//		int[][] arrNeighbor3 = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 0 } };
//		Board boardNeighbor3 = new Board(arrNeighbor3);
//		Iterable<Board> neighbors3 = boardNeighbor3.neighbors();
//		int n3 = 3;
//		for (Board neighbor3 : neighbors3) {
//			System.out.println("This is board number " + n3 + ": " + Arrays.toString(neighbor3.flatBoard));
//			n3--;
//		}
//
//		// ====== testing neighbors bottom left ====== //
//		System.out.println("\nNeighbors Bottom Left Test:");
//		int[][] arrNeighbor4 = { { 1, 2, 3 }, { 4, 5, 6 }, { 0, 7, 8 } };
//		Board boardNeighbor4 = new Board(arrNeighbor4);
//		Iterable<Board> neighbors4 = boardNeighbor4.neighbors();
//		int n4 = 3;
//		for (Board neighbor4 : neighbors4) {
//			System.out.println("This is board number " + n4 + ": " + Arrays.toString(neighbor4.flatBoard));
//			n4--;
//		}
//
//		// ====== testing neighbors rightmost side ====== //
//		System.out.println("\nNeighbors Right Side Test:");
//		int[][] arrNeighbor5 = { { 1, 2, 3 }, { 4, 5, 0 }, { 6, 7, 8 } };
//		Board boardNeighbor5 = new Board(arrNeighbor5);
//		Iterable<Board> neighbors5 = boardNeighbor5.neighbors();
//		int n5 = 4;
//		for (Board neighbor5 : neighbors5) {
//			System.out.println("This is board number " + n5 + ": " + Arrays.toString(neighbor5.flatBoard));
//			n5--;
//		}
//
//		// ====== testing neighbors top row ====== //
//		System.out.println("\nNeighbors Top Row Test:");
//		int[][] arrNeighbor6 = { { 1, 0, 2 }, { 3, 4, 5 }, { 6, 7, 8 } };
//		Board boardNeighbor6 = new Board(arrNeighbor6);
//		Iterable<Board> neighbors6 = boardNeighbor6.neighbors();
//		int n6 = 4;
//		for (Board neighbor6 : neighbors6) {
//			System.out.println("This is board number " + n6 + ": " + Arrays.toString(neighbor6.flatBoard));
//			n6--;
//		}
//
//		// ====== testing neighbors bottom row ====== //
//		System.out.println("\nNeighbors Bottom Row Test:");
//		int[][] arrNeighbor7 = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 0, 8 } };
//		Board boardNeighbor7 = new Board(arrNeighbor7);
//		Iterable<Board> neighbors7 = boardNeighbor7.neighbors();
//		int n7 = 4;
//		for (Board neighbor7 : neighbors7) {
//			System.out.println("This is board number " + n7 + ": " + Arrays.toString(neighbor7.flatBoard));
//			n7--;
//		}
//
//		// ====== testing neighbors leftmost column ====== //
//		System.out.println("\nNeighbors Left Column Test:");
//		int[][] arrNeighbor8 = { { 1, 2, 3 }, { 0, 4, 5 }, { 6, 7, 8 } };
//		Board boardNeighbor8 = new Board(arrNeighbor8);
//		Iterable<Board> neighbors8 = boardNeighbor8.neighbors();
//		int n8 = 4;
//		for (Board neighbor8 : neighbors8) {
//			System.out.println("This is board number " + n8 + ": " + Arrays.toString(neighbor8.flatBoard));
//			n8--;
//		}
//
		// ====== testing neighbors else ====== //
		System.out.println("\nNeighbors Middle Position Test:");
		int[][] arrNeighbor9 = { { 1, 2, 3, 4 }, { 5, 0, 6, 7 }, { 8, 9, 10, 11 }, { 12, 13, 14, 15 } };
		Board boardNeighbor9 = new Board(arrNeighbor9);
		Iterable<Board> neighbors9 = boardNeighbor9.neighbors();
		int n9 = 5;
		for (Board neighbor9 : neighbors9) {
			System.out.println("This is board number " + n9 + ": \n" + neighbor9.toString());
			n9--;
		}

//		// ====== testing neighbors 1x1 ====== //
//		System.out.println("\nNeighbors Middle Position Test:");
//		int[][] arrNeighbor10 = { { 4 } };
//		Board boardNeighbor10 = new Board(arrNeighbor10);
//		Iterable<Board> neighbors10 = boardNeighbor10.neighbors();
//		int n10 = 5;
//		for (Board neighbor10 : neighbors10) {
//			System.out.println("This is board number " + n10 + ": " + Arrays.toString(neighbor10.flatBoard));
//			n10--;
//		}

		// ====== testing toString ====== //
		System.out.println(boardNeighbor9.toString());

	}
}
