package com.vdqgpm.models;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class Profile {
	private String id;
	private String name;
	private String status;
	private String proxy;
	private String lastRun;
	private boolean isRunning;
	private BooleanProperty selected;

	public Profile() {

	}

	public Profile(String id, String name, String status, String proxy, String lastRun) {
		this.id = id;
		this.name = name;
		this.status = status;
		this.proxy = proxy;
		this.lastRun = lastRun;
		this.isRunning = false;
		this.selected = new SimpleBooleanProperty(false);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getProxy() {
		return proxy;
	}

	public void setProxy(String proxy) {
		this.proxy = proxy;
	}

	public String getLastRun() {
		return lastRun;
	}

	public void setLastRun(String lastRun) {
		this.lastRun = lastRun;
	}

	public boolean isRunning() {
		return isRunning;
	}

	public void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}

	public BooleanProperty selectedProperty() {
		return selected;
	}

	public boolean isSelected() {
		return selected.get();
	}

	public void setSelected(boolean selected) {
		this.selected.set(selected);
	}
}
