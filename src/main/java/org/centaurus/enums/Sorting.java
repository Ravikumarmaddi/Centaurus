package org.centaurus.enums;

/**
 * 
 * @author Vladislav Socolov
 */
public enum Sorting {
	/**
	 * Sort in ascending order.
	 */
	asc(1),
	/**
	 * Sort in descending order.
	 */
	desc(-1);
	
	private int value;
	
	private Sorting(int value){
		this.value = value;
	}

	public int getValue() {
		return value;
	}
	
}
