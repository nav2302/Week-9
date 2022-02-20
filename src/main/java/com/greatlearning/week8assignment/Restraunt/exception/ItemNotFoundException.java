package com.greatlearning.week8assignment.Restraunt.exception;

public class ItemNotFoundException extends RuntimeException {
	/**
	 * @author nav
	 */

	private static final long serialVersionUID = 1L;
	private String message;

	public ItemNotFoundException(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
