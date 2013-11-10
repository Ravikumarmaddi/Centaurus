package org.centaurus.enums;

/**
 * 
 * @author Vladislav Socolov
 */
public enum Junction {
	/**
	 * Conjunction
	 */
	AND("&&"), 
	/**
	 * Disjunction
	 */
	OR("||");
	
	private String value;

	private Junction(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
	
}
