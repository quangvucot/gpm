package com.vdqgpm.models;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class Profile {

	private String id;
	private String name;
	private String raw_proxy;
	private String browser_type;
	private String browser_version;
	private String group_id;
	private String profile_path;
	private String note;
	private String created_at;
	private boolean isRunning;
	private BooleanProperty selected;

	public Profile() {
		this.isRunning = false;
		this.selected = new SimpleBooleanProperty(false);
	}

	public Profile(String id, String name, String raw_proxy, String browser_type, String browser_version) {
		this.id = id;
		this.name = name;
		this.raw_proxy = raw_proxy;
		this.browser_type = browser_type;
		this.browser_version = browser_version;
		this.isRunning = false;
		this.selected = new SimpleBooleanProperty(false);
	}

	public Profile(String id, String name, String raw_proxy, String browser_type, String browser_version,
			String group_id, String profile_path, String note, String created_at) {
		this.id = id;
		this.name = name;
		this.raw_proxy = raw_proxy;
		this.browser_type = browser_type;
		this.browser_version = browser_version;
		this.group_id = group_id;
		this.profile_path = profile_path;
		this.note = note;
		this.created_at = created_at;
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

	public String getRaw_proxy() {
		return raw_proxy;
	}

	public void setRaw_proxy(String raw_proxy) {
		this.raw_proxy = raw_proxy;
	}

	public String getBrowser_type() {
		return browser_type;
	}

	public void setBrowser_type(String browser_type) {
		this.browser_type = browser_type;
	}

	public String getBrowser_version() {
		return browser_version;
	}

	public void setBrowser_version(String browser_version) {
		this.browser_version = browser_version;
	}

	public String getGroup_id() {
		return group_id;
	}

	public void setGroup_id(String group_id) {
		this.group_id = group_id;
	}

	public String getProfile_path() {
		return profile_path;
	}

	public void setProfile_path(String profile_path) {
		this.profile_path = profile_path;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getCreated_at() {
		return created_at;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}

	public BooleanProperty getSelected() {
		return selected;
	}

	public void setSelected(BooleanProperty selected) {
		this.selected = selected;
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
