package org.centaurus.exceptions;

/**
 * 
 * @author Vladislav Socolov
 */
public class CentaurusMappingException extends CentaurusException {

	private static final long serialVersionUID = 6844876203460812417L;

	public CentaurusMappingException() {
		super();
	}

	public CentaurusMappingException(String message) {
		super(message);
	}

	public CentaurusMappingException(Throwable cause) {
		super(cause);
	}

	public CentaurusMappingException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
