package org.centaurus.enums;

/**
 * 
 * @author Vladislav Socolov
 */
public enum Operator {	
	/**
	 * Matches all values that are equal to the value specified in the query.
	 */
	eq,
	/**
	 * Matches arrays that contain all elements specified in the query.
	 */
	all,
	/**
	 * Matches values that are greater than the value specified in the query.
	 */
	gt,
	/**
	 * Matches values that are equal to or greater than the value specified in the query.
	 */
	gte,
	/**
	 * Matches any of the values that exist in an array specified in the query.
	 */
	in,
	/**
	 * Matches vales that are less than the value specified in the query.
	 */
	lt,
	/**
	 * Matches values that are less than or equal to the value specified in the query.
	 */
	lte,
	/**
	 * Matches all values that are not equal to the value specified in the query.
	 */
	ne,
	/**
	 * Matches values that do not exist in an array specified to the query.
	 */
	nin,
	/**
	 * Matches all values that match expression.
	 */
	like
}
