/*
 * Alex Baret
 * CSIS2420 A03 P2 Autocomplete
 * 6/24/21
 * 
 */

package a03;

import java.util.Arrays;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

/**
 * Immutable data structure containing methods to return allMatches of a key and
 * number of matches of a key from an input text file. Utilizes Term class and
 * BinarySearchDeluxe.
 * 
 * @author alex
 *
 */
public class Autocomplete {

	private Term[] terms;
	private int numMatches;

	/**
	 * Constructs an Autocomplete Object. Initializes the data structure from the
	 * given array of terms. Sorts the input array lexicographically
	 * 
	 * @param terms Term[] to initialize instance variable
	 */
	public Autocomplete(Term[] terms) {
		if (terms == null) {
			throw new NullPointerException("Argument can't be null");
		} else {
			this.terms = terms;
			Arrays.sort(this.terms); // sort lexicographically
		}
	}

	/**
	 * Return all terms that start with the given prefix, in descending order of
	 * weight.
	 * 
	 * @param prefix String to find matches of from the input array.
	 * @return Term[] containing the matches of prefix in descending order of
	 *         weight, or an empty array if no matches are found
	 */
	public Term[] allMatches(String prefix) {
		if (prefix == null) {
			throw new NullPointerException("Argument can't be null");
		} else {
			// converting prefix to term
			Term temp = new Term(prefix, 1.0);

			int i = BinarySearchDeluxe.firstIndexOf(terms, temp, Term.byPrefixOrder(prefix.length()));
			int j = BinarySearchDeluxe.lastIndexOf(terms, temp, Term.byPrefixOrder(prefix.length()));
			if (i == -1 || j == -1) {
				return new Term[0];
			}
			Term[] matches = new Term[j - i + 1];
			// add the different entries from i to j in terms to matches

			int index = 0;
			while (i <= j) {
				Term holdingTerm = terms[i];
				matches[index] = holdingTerm;
				i++;
				index++;
				numMatches++;
			}
			Arrays.sort(matches, Term.byReverseWeightOrder());
			return matches;

		}
	}

	/**
	 * Returns the number of terms that start with the given prefix.
	 * 
	 * @param prefix String to find matches of from the input array.
	 * @return int numberOfMatches containing the number of matches found in the
	 *         Term[]
	 */
	public int numberOfMatches(String prefix) {
		if (prefix == null) {
			throw new NullPointerException("Argument can't be null");
		} else {
			// converting prefix to term
			Term temp = new Term(prefix, 1.0);

			int i = BinarySearchDeluxe.firstIndexOf(terms, temp, Term.byPrefixOrder(prefix.length()));
			int j = BinarySearchDeluxe.lastIndexOf(terms, temp, Term.byPrefixOrder(prefix.length()));
			if (j < 0 || i < 0) {
				return 0;
			} else {
				allMatches(prefix);
				return this.numMatches;
			}

		}
	}

	public static void main(String[] args) {

		// read in the terms from a file
		String filename = "./src/a03/pu-buildings.txt"; // sets filename string
		In in = new In(filename); // in filename
		int N = in.readInt(); // read the int
		Term[] terms = new Term[N];
		for (int i = 0; i < N; i++) {
			double weight = in.readDouble(); // read the next weight
			in.readChar(); // scan past the tab
			String query = in.readLine(); // read the next query
			terms[i] = new Term(query, weight); // construct the term
		}
		Autocomplete testOne = new Autocomplete(terms);
		for (Term term : testOne.terms) {
			StdOut.println(term);
		}

		System.out.println(Arrays.toString(testOne.allMatches("The")));
//		System.out.println(testOne.numberOfMatches("The"));
	}
}
