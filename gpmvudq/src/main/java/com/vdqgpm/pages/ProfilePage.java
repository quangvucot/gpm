package com.vdqgpm.pages;

import java.awt.TextField;

import com.vdqgpm.models.Profile;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;

public class ProfilePage {
	@FXML
	private VBox root;

	@FXML
	private TextField urlField;

	@FXML
	private TableView<Profile> profileTable;

	@FXML
	private TableColumn<Profile, Boolean> selectColumn;

	@FXML
	private TableColumn<Profile, String> nameColumn;

	@FXML
	private TableColumn<Profile, String> statusColumn;

	@FXML
	private TableColumn<Profile, String> proxyColumn;

	@FXML
	private TableColumn<Profile, String> lastRunColumn;

	@FXML
	private TableColumn<Profile, Void> actionColumn;

	@FXML
	private TextField nameField;

	@FXML
	private TextField statusField;

	@FXML
	private TextField proxyField;

	@FXML
	private TextField lastRunField;

	@FXML
	private TextArea logArea;

	@FXML
	private Button startButton;

	@FXML
	private Button stopButton;

	@FXML
	private Button openProfilesButton;

	@FXML
	private Button goButton;

	// Getters để truy cập các phần tử giao diện người dùng
	public VBox getRoot() {
		return root;
	}

	public TextField getUrlField() {
		return urlField;
	}

	public TableView<Profile> getProfileTable() {
		return profileTable;
	}

	public TableColumn<Profile, Boolean> getSelectColumn() {
		return selectColumn;
	}

	public TableColumn<Profile, String> getNameColumn() {
		return nameColumn;
	}

	public TableColumn<Profile, String> getStatusColumn() {
		return statusColumn;
	}

	public TableColumn<Profile, String> getProxyColumn() {
		return proxyColumn;
	}

	public TableColumn<Profile, String> getLastRunColumn() {
		return lastRunColumn;
	}

	public TableColumn<Profile, Void> getActionColumn() {
		return actionColumn;
	}

	public TextField getNameField() {
		return nameField;
	}

	public TextField getStatusField() {
		return statusField;
	}

	public TextField getProxyField() {
		return proxyField;
	}

	public TextField getLastRunField() {
		return lastRunField;
	}

	public TextArea getLogArea() {
		return logArea;
	}

	public Button getStartButton() {
		return startButton;
	}

	public Button getStopButton() {
		return stopButton;
	}

	public Button getOpenProfilesButton() {
		return openProfilesButton;
	}

	public Button getGoButton() {
		return goButton;
	}
}
