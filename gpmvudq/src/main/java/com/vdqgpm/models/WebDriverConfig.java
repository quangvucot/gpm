package com.vdqgpm.models;

public class WebDriverConfig {

	private boolean success;
	private String profile_id;
	private String browser_location;
	private String remote_debugging_address;
	private String driver_path;

	// Getters và Setters
	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getProfile_id() {
		return profile_id;
	}

	public void setProfile_id(String profile_id) {
		this.profile_id = profile_id;
	}

	public String getBrowser_location() {
		return browser_location;
	}

	public void setBrowser_location(String browser_location) {
		this.browser_location = browser_location;
	}

	public String getRemote_debugging_address() {
		return remote_debugging_address;
	}

	public void setRemote_debugging_address(String remote_debugging_address) {
		this.remote_debugging_address = remote_debugging_address;
	}

	public String getDriver_path() {
		return driver_path;
	}

	public void setDriver_path(String driver_path) {
		this.driver_path = driver_path;
	}

}
