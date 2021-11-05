package a04;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stopwatch;

public class PuzzleChecker {

public static void main(String[] args) {

		// header
		StdOut.printf("%-25s %7s %8s\n", "filename", "moves", "time");
		StdOut.println("------------------------------------------");

		// for each command-line argument
		for (String filename : args) {
			// read in the board specified in the filename
			In in = new In(filename);
			int n = in.readInt();
			int[][] blocks = new int[n][n];
			for (int i = 0; i < n; i++)
				for (int j = 0; j < n; j++)
					blocks[i][j] = in.readInt();
			Board initial = new Board(blocks);

			// check if puzzle is solvable; if so, solve it print out number of moves
			if (initial.isSolvable()) {
				Stopwatch timer = new Stopwatch();
				Solver solver = new Solver(initial);
				int moves = solver.moves();
				double time = timer.elapsedTime();
				StdOut.printf("%-25s %7d %8.2f\n", filename, moves, time);
			}

			// if not, print that it is unsolvable
			else {
				StdOut.printf("%-25s   unsolvable\n", filename);
			}
		}
	}
}
