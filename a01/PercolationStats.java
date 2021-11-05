/*CS2420-A02-Percolation-Stats
 * Alex Baret
 * 6/7/21
 */

package a01;

import edu.princeton.cs.algs4.*;

/**
 * Implements Monte Carlo simulation for Percolation. Calculates STDDEV, Mean,
 * and 95% Low and Max for experiments.
 * 
 * @author alex
 *
 */
public class PercolationStats {
	private int gridSize; // grid size
	private int numExperiments; // number of experiments
	private double[] experimentalData; // array to store percolation thresholds from

	/**
	 * Constructs a new Percolation Stats experiment. Runs T experiments for N sized
	 * grid. An experiment consists of opening sites one at a time until the system
	 * percolates.
	 * 
	 * @param N is an int representing the size of the grid
	 * @param T is an int representing the number of times the experiment is to be
	 *          run.
	 */
	public PercolationStats(int N, int T) {
		if (N <= 0 || T <= 0) {
			throw new IllegalArgumentException("Index is out of bounds");
		} else {
			this.gridSize = N;
			this.numExperiments = T;
			this.experimentalData = new double[T]; // T is number of experiments
			for (int experiment = 0; experiment < numExperiments; experiment++) { // iterate the experiment
				Percolation percolation = new Percolation(gridSize);
				// creating experiment numbers to be stored for each iteration
				double openSites = 0;
				double totalSites = N * N;
				while (!percolation.percolates()) { // while it doesn't percolate
					// generate random site
					int row = (StdRandom.uniform(gridSize));
					int col = (StdRandom.uniform(gridSize));
					// check if it's not already open
					if (!percolation.isOpen(row, col)) {
						percolation.open(row, col);
						openSites++;
					}
				}
				// once it percolates move to calculate percolation threshold
				double percolationThreshold = openSites / totalSites;
				// store percolation threshold in array
				experimentalData[experiment] = percolationThreshold;
			}
		}
	}

	/**
	 * Calculates the mean of the experiments and returns a double.
	 * 
	 * @return double containing the mean of the experiment.
	 */
	public double mean() {
		// return the mean of the array
		return StdStats.mean(this.experimentalData);

	}

	/**
	 * Calculates the Standard Deviation of the experiment
	 * 
	 * @return double containing the STDDEV
	 */
	public double stddev() {
		// return the stddev of the array
		return StdStats.stddev(this.experimentalData);

	}

	/**
	 * Calculates the low end of the 95% confidence interval
	 * 
	 * @return double containing the low end of the 95% confidence interval.
	 */
	public double confidenceLow() {
		// return the low confidence interval of the array
		double squareExperiments = Math.sqrt(this.numExperiments);
		return (this.mean() - ((1.96 * this.stddev()) / squareExperiments));

	}

	/**
	 * Calculates the high end of the 95% confidence interval
	 * 
	 * @return double containing the high end of the 95% confidence interval.
	 */
	public double confidenceHigh() {
		// return the high confidence interval of the array
		double squareExperiments = Math.sqrt(this.numExperiments);
		return (this.mean() + ((1.96 * this.stddev()) / squareExperiments));

	}

	public static void main(String[] args) {

		// === Tests === //
		PercolationStats test1 = new PercolationStats(200, 100);
		System.out.println(test1.mean());
		System.out.println(test1.stddev());
		System.out.println(test1.confidenceLow());
		System.out.println(test1.confidenceHigh());

	}

}