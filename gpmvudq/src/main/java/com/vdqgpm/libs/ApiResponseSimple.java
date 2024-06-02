package com.vdqgpm.libs;

public class ApiResponseSimple {
	private boolean success;
	private String message;

	// Getters và Setters
	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
