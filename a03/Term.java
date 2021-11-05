/*
 * Alex Baret
 * CSIS2420 A03 P1 Term
 * 6/24/21
 * 
 */
package a03;

import java.util.*;

/**
 * An immutable data type Term that represents an autocomplete term: a string
 * query and an associated real-valued weight.
 * 
 * @author alex
 *
 */
public class Term implements Comparable<Term> {

	private String query;
	private double weight;

	/**
	 * Constructor for Term object. Initializes a term with the given query string
	 * and weight.
	 * 
	 * @param query  input String
	 * @param weight double value of weight
	 */
	public Term(String query, double weight) {
		if (query == null) {
			throw new NullPointerException("Query cannot be null");
		} else if (weight < 0) {
			throw new IllegalArgumentException("Weight must be nonnegative");
		} else {
			this.weight = weight;
			this.query = query;
		}
	}

	/**
	 * Returns a new ReverseWeightOrder Comparator Object.
	 * 
	 * @return Comparator<Term> in reverse weight order
	 */
	public static Comparator<Term> byReverseWeightOrder() {
		return new ReverseWeightOrderComparator();

	}

	/**
	 * Compare the terms in descending order by weight.
	 * 
	 * @author alex
	 *
	 */
	private static class ReverseWeightOrderComparator implements Comparator<Term> {
		@Override
		public int compare(Term t1, Term t2) {
			Double weight1 = t1.weight * -1; // multiplied by negative one potentially reversing the order?
			Double weight2 = t2.weight * -1;

			return weight1.compareTo(weight2);
		}
	}

	/**
	 * Returns a new PrefixOrder Comparator Object.
	 * 
	 * @param r int number of characters to compare up to
	 * @return
	 */
	public static Comparator<Term> byPrefixOrder(int r) {
		if (r < 0) {
			throw new IllegalArgumentException("Integer r must be nonnegative");
		} else {
			return new PrefixOrderComparator(r);
		}
	}

	/**
	 * Compare the terms in lexicographic order but using only the first r
	 * characters of each query.
	 * 
	 * @author alex
	 *
	 */
	private static class PrefixOrderComparator implements Comparator<Term> {

		private int r;

		/**
		 * Constructs a new PrefixOrderComparator to utilize prefix designator int r
		 * 
		 * @param r
		 */
		public PrefixOrderComparator(int r) {
			this.r = r;
		}

		/**
		 * Compares two term's query's based off substrings (their prefixes). Returns a
		 * positive integer, negative integer, or 0.
		 *
		 * @param t1 Term to be compared
		 * @param t2 Term to be compared
		 */
		@Override
		public int compare(Term t1, Term t2) {
			// compare substrings from 0 - r
			// get strings
			String query1 = t1.query;
			String query2 = t2.query;
			// create substrings
			String subQuery1 = query1.substring(0, Math.min(query1.length(), this.r));
			String subQuery2 = query2.substring(0, Math.min(query2.length(), this.r));
			// compare substrings
			return subQuery1.compareTo(subQuery2);
		}

	}

	/**
	 * Returns a string representation of the term in the following format: the
	 * weight, followed by a tab, followed by the query.
	 * 
	 * @return String representation of the term in the following format: the
	 *         weight, followed by a tab, followed by the query.
	 */
	public String toString() {
		return this.weight + "	" + this.query;

	}

	/**
	 * Compares the terms in lexicographic order by query.
	 */
	@Override
	public int compareTo(Term that) {
		return this.query.compareTo(that.query);
	}

}
