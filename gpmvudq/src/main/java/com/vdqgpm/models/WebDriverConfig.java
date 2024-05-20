package com.vdqgpm.models;

public class WebDriverConfig {

	private String id_profile;
	private String driver_path;
	private String remote_address;

	public WebDriverConfig() {

	}

	public WebDriverConfig(String driver_path, String remote_address) {
		super();

		this.driver_path = driver_path;
		this.remote_address = remote_address;
	}

	public String getId_profile() {
		return id_profile;
	}

	public void setId_profile(String id_profile) {
		this.id_profile = id_profile;
	}

	public String getDriver_path() {
		return driver_path;
	}

	public void setDriver_path(String driver_path) {
		this.driver_path = driver_path;
	}

	public String getRemote_address() {
		return remote_address;
	}

	public void setRemote_address(String remote_address) {
		this.remote_address = remote_address;
	}

}
