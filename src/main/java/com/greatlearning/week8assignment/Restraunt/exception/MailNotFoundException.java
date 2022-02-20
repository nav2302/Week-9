package com.greatlearning.week8assignment.Restraunt.exception;

public class MailNotFoundException extends RuntimeException{
	/**
	 * @author nav
	 */

	private static final long serialVersionUID = 1L;
	private String message;

	public MailNotFoundException(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
