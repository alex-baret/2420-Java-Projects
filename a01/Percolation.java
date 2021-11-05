/* CS2420-A01-Percolation-Part-1
 * Alex Baret
 * 6/7/21
 * This assignment was completed alone without a parter.
 */

package a01;

import edu.princeton.cs.algs4.*;

/**
 * This class determines whether a system percolates. Utilizes Weighted Union
 * Quick Find to map which sites are connected. Percolation occurs when a site
 * on the top row is connected to a site on the bottom row.
 * 
 * @author alex
 *
 */
public class Percolation extends WeightedQuickUnionUF {

	private boolean[][] grid;
	private int N;
	private final int VIRTUAL_TOP_SITE = 0;
	private int virtualBottom = N * N + 1;

	/**
	 * Generates a unique number that's one dimensional to correspond with each
	 * row/col.
	 * 
	 * @param x is an int representing the row.
	 * @param y is an int representing the col.
	 * @return is a unique int for the selected row and column.
	 */
	private int gridTo1D(int row, int col) {
		if (row == 0) {
			return col + 1;
		} else {
			return (row * this.N) + col + 1;
		}
	}

	/**
	 * Constructs a new grid with a size of N by N. All sites are initialized to
	 * being blocked.
	 * 
	 * @param N is an int representing the length and width of the grid.
	 */
	public Percolation(int N) {
		super((N * N) + 2);
		this.N = N;
		this.virtualBottom = this.N * this.N + 1;
		this.grid = new boolean[N][N];
		for (int rowIndex = 0; rowIndex < grid.length; rowIndex++) {
			for (int colIndex = 0; colIndex < grid.length; colIndex++) {
				this.grid[rowIndex][colIndex] = false;
			}
		}
		for (int colIndex = 0; colIndex < this.N; colIndex++) {
			int site = gridTo1D(N - 1, colIndex);
			this.union(virtualBottom, site);
		}
	}

	/**
	 * Checks whether the x and y coordinate is within bounds of the grid.
	 * 
	 * @param x is an int representing first element of the index.
	 * @param y is an int representing the second element index.
	 */
	private void checkIndex(int x, int y) {
		if (x > N - 1 || y > N - 1 || x < 0 || y < 0) {
			throw new IndexOutOfBoundsException("Index is out of bounds");
		}
	}

	/**
	 * Opens a site on the grid. Checks surrounding sites and unions open sites
	 * together.
	 * 
	 * @param i is an int representing the row.
	 * @param j is an int representing the col.
	 */
	public void open(int i, int j) { // open site (row i, column j) if it is not open already
		checkIndex(i, j); // validate indices it's received

		// if it's not open, open it
		if (this.isOpen(i, j) == false) {
			this.grid[i][j] = true;

			// check for top or bottom row to union with virtual sites
			if (i == 0) {
				union((gridTo1D(i, j)), VIRTUAL_TOP_SITE);
			}
			if (i == this.N - 1) {
				union((gridTo1D(i, j)), virtualBottom);
			}

			// check if surrounding blocks are open, if they are union them
			// top left corner
			if (i == 0 && j == 0) {
				if (this.isOpen(i, j + 1)) {
					union((gridTo1D(i, j)), (gridTo1D(i, j + 1)));
				}
				if (this.isOpen(i + 1, j)) {
					union((gridTo1D(i, j)), (gridTo1D(i + 1, j)));
				}
			}
			// top row, exclude the corners
			if (j == 0 && (i != 0 && i != this.N - 1)) {
				if (this.isOpen(i, j + 1)) {
					union((gridTo1D(i, j)), (gridTo1D(i, j + 1)));
				}
				if (this.isOpen(i - 1, j)) {
					union((gridTo1D(i, j)), (gridTo1D(i - 1, j)));
				}
				if (this.isOpen(i + 1, j)) {
					union((gridTo1D(i, j)), (gridTo1D(i + 1, j)));
				}
			}
			// top-right corner
			if (i == this.N - 1 && j == 0) {
				if (this.isOpen(i, j + 1)) {
					union((gridTo1D(i, j)), (gridTo1D(i, j + 1)));
				}
				if (this.isOpen(i - 1, j)) {
					union((gridTo1D(i, j)), (gridTo1D(i - 1, j)));
				}
			}
			// right column excluding corners
			if (i == this.N - 1 && (j != 0 && j != this.N - 1)) {
				if (this.isOpen(i, j - 1)) {
					union((gridTo1D(i, j)), (gridTo1D(i, j - 1)));
				}
				if (this.isOpen(i, j + 1)) {
					union((gridTo1D(i, j)), (gridTo1D(i, j + 1)));
				}
				if (this.isOpen(i - 1, j)) {
					union((gridTo1D(i, j)), (gridTo1D(i - 1, j)));
				}
			}
			// bottom-right corner
			if (i == this.N - 1 && j == this.N - 1) {
				if (this.isOpen(i, j - 1)) {
					union((gridTo1D(i, j)), (gridTo1D(i, j - 1)));
				}

				if (this.isOpen(i - 1, j)) {
					union((gridTo1D(i, j)), (gridTo1D(i - 1, j)));
				}

			}
			// bottom row excluding corners
			if (j == this.N - 1 && (i != 0 && i != this.N - 1)) {
				if (this.isOpen(i, j - 1)) {
					union((gridTo1D(i, j)), (gridTo1D(i, j - 1)));
				}
				if (this.isOpen(i - 1, j)) {
					union((gridTo1D(i, j)), (gridTo1D(i - 1, j)));
				}
				if (this.isOpen(i + 1, j)) {
					union((gridTo1D(i, j)), (gridTo1D(i + 1, j)));
				}
			}
			// bottom-left corner
			if (i == 0 && j == this.N - 1) {
				if (this.isOpen(i, j - 1)) {
					union((gridTo1D(i, j)), (gridTo1D(i, j - 1)));
				}
				if (this.isOpen(i + 1, j)) {
					union((gridTo1D(i, j)), (gridTo1D(i + 1, j)));
				}
			}
			// left column excluding corners
			if (i == 0 && (j != 0 && j != this.N - 1)) {
				if (this.isOpen(i, j - 1)) {
					union((gridTo1D(i, j)), (gridTo1D(i, j - 1)));
				}
				if (this.isOpen(i, j + 1)) {
					union((gridTo1D(i, j)), (gridTo1D(i, j + 1)));
				}
				if (this.isOpen(i + 1, j)) {
					union((gridTo1D(i, j)), (gridTo1D(i + 1, j)));
				}
			}
			// inner sites
			else if (!(i == 0 || i == this.N - 1 || j == 0 || j == this.N - 1)) {
				if (this.isOpen(i, j - 1)) {
					union((gridTo1D(i, j)), (gridTo1D(i, j - 1)));
				}
				if (this.isOpen(i, j + 1)) {
					union((gridTo1D(i, j)), (gridTo1D(i, j + 1)));
				}
				if (this.isOpen(i - 1, j)) {
					union((gridTo1D(i, j)), (gridTo1D(i - 1, j)));
				}
				if (this.isOpen(i + 1, j)) {
					union((gridTo1D(i, j)), (gridTo1D(i + 1, j)));
				}
			}
		} else { // if the called site is already open, check for unions
			if (i == 0 && j == 0) {
				if (this.isOpen(i, j + 1)) {
					union((gridTo1D(i, j)), (gridTo1D(i, j - 1))); // union top-left and site below
				}
				if (this.isOpen(i + 1, j)) {
					union((gridTo1D(i, j)), (gridTo1D(i + 1, j))); // union top-left and site to the right
				}
			}
			// top row exclude the corners
			if (j == 0 && (i != 0 && i != this.N - 1)) {
				if (this.isOpen(i, j + 1)) {
					union((gridTo1D(i, j)), (gridTo1D(i, j + 1)));
				}
				if (this.isOpen(i - 1, j)) {
					union((gridTo1D(i, j)), (gridTo1D(i - 1, j)));
				}
				if (this.isOpen(i + 1, j)) {
					union((gridTo1D(i, j)), (gridTo1D(i + 1, j)));
				}
			}
			// top-right corner
			if (i == this.N - 1 && j == 0) {
				if (this.isOpen(i, j + 1)) {
					union((gridTo1D(i, j)), (gridTo1D(i, j + 1)));
				}
				if (this.isOpen(i - 1, j)) {
					union((gridTo1D(i, j)), (gridTo1D(i - 1, j)));
				}
			}
			// right column exclude the corners
			if (i == this.N - 1 && (j != 0 && j != this.N - 1)) {
				if (this.isOpen(i, j - 1)) {
					union((gridTo1D(i, j)), (gridTo1D(i, j - 1)));
				}
				if (this.isOpen(i, j + 1)) {
					union((gridTo1D(i, j)), (gridTo1D(i, j + 1)));
				}
				if (this.isOpen(i - 1, j)) {
					union((gridTo1D(i, j)), (gridTo1D(i - 1, j)));
				}
			}
			// bottom-right corner
			if (i == this.N - 1 && j == this.N - 1) {
				if (this.isOpen(i, j - 1)) {
					union((gridTo1D(i, j)), (gridTo1D(i, j - 1)));
				}

				if (this.isOpen(i - 1, j)) {
					union((gridTo1D(i, j)), (gridTo1D(i - 1, j)));
				}

			}
			// bottom row excluding corners
			if (j == this.N - 1 && (i != 0 && i != this.N - 1)) {
				if (this.isOpen(i, j - 1)) {
					union((gridTo1D(i, j)), (gridTo1D(i, j - 1)));
				}
				if (this.isOpen(i - 1, j)) {
					union((gridTo1D(i, j)), (gridTo1D(i - 1, j)));
				}
				if (this.isOpen(i + 1, j)) {
					union((gridTo1D(i, j)), (gridTo1D(i + 1, j)));
				}
			}
			// bottom-left corner
			if (i == 0 && j == this.N - 1) {
				if (this.isOpen(i, j - 1)) {
					union((gridTo1D(i, j)), (gridTo1D(i, j - 1)));
				}
				if (this.isOpen(i + 1, j)) {
					union((gridTo1D(i, j)), (gridTo1D(i + 1, j)));
				}
			}
			// left column excluding corners
			if (i == 0 && (j != 0 && j != this.N - 1)) {
				if (this.isOpen(i, j - 1)) {
					union((gridTo1D(i, j)), (gridTo1D(i, j - 1)));
				}
				if (this.isOpen(i, j + 1)) {
					union((gridTo1D(i, j)), (gridTo1D(i, j + 1)));
				}
				if (this.isOpen(i + 1, j)) {
					union((gridTo1D(i, j)), (gridTo1D(i + 1, j)));
				}
			}
			// inner sites
			else if (!(i == 0 || i == this.N - 1 || j == 0 || j == this.N - 1)) {
				if (this.isOpen(i, j - 1)) {
					union((gridTo1D(i, j)), (gridTo1D(i, j - 1)));
				}
				if (this.isOpen(i, j + 1)) {
					union((gridTo1D(i, j)), (gridTo1D(i, j + 1)));
				}
				if (this.isOpen(i - 1, j)) {
					union((gridTo1D(i, j)), (gridTo1D(i - 1, j)));
				}
				if (this.isOpen(i + 1, j)) {
					union((gridTo1D(i, j)), (gridTo1D(i + 1, j)));
				}
			}
		}
	}

	/**
	 * Determines whether a site is open or not.
	 * 
	 * @param i is an int representing the row.
	 * @param j is an int representing the col.
	 * @return
	 */
	public boolean isOpen(int i, int j) {
		checkIndex(i, j);
		return this.grid[i][j];

	}

	/**
	 * Determines whether a site is full. A site is full when it has a connection to
	 * the top row.
	 * 
	 * @param i is an int representing the row.
	 * @param j is an int representing the col.
	 * @return
	 */
	public boolean isFull(int i, int j) {
		checkIndex(i, j);
		int site = gridTo1D(i, j);
		if (this.isOpen(i, j)) { // check for open status
			return find(VIRTUAL_TOP_SITE) == find(site);
		} else {
			return false;
		}
	}

	/**
	 * Determines whether the system percolates or not. The system percolates when
	 * the top row is connected to the bottom row through a set of sites in union
	 * with one-another.
	 * 
	 * @return is a boolean representing percolation or not. True means the system
	 *         percolates and False means it does not.
	 */
	public boolean percolates() {
		return find(this.VIRTUAL_TOP_SITE) == find(this.virtualBottom);
	}

}
