package org.centaurus.enums;

/**
 * 
 * @author Vladislav Socolov
 */
public enum ProjectionType {
	/**
	 * Include field in projection.
	 */
	INCLUDE(1),
	/**
	 * Exclude field from projection.
	 */
	EXCLUDE(0);
	
	private int value;
	
	ProjectionType(int value){
		this.value = value;
	}

	public int getValue() {
		return value;
	}
}
