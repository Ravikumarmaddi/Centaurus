package org.centaurus.exceptions;

/**
 * 
 * @author Vladislav Socolov
 */
public class CentaurusException extends RuntimeException {

	private static final long serialVersionUID = 9133227266151069045L;

	public CentaurusException() {
		super();
	}

	public CentaurusException(String message) {
		super(message);
	}

	public CentaurusException(Throwable cause) {
		super(cause);
	}

	public CentaurusException(String message, Throwable cause) {
		super(message, cause);
	}
}
