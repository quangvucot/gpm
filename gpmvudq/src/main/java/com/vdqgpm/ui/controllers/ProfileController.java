package com.vdqgpm.ui.controllers;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import com.vdqgpm.libs.ApiResponseSimple;
import com.vdqgpm.libs.GPMLoginApiV3;
import com.vdqgpm.models.ApiResponse;
import com.vdqgpm.models.Profile;
import com.vdqgpm.services.ExcelReader;
import com.vdqgpm.services.ProfileProcessor;
import com.vdqgpm.services.ProfileService;
import com.vdqgpm.services.ProfileTask;
import com.vdqgpm.services.ProfileUpdateListener;
import com.vdqgpm.utilities.AlertActionHandler;
import com.vdqgpm.utilities.AlertUtils;
import com.vdqgpm.utilities.Utils;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;

public class ProfileController implements ProfileUpdateListener {

	@FXML
	private TableView<Profile> profileTable;
	@FXML
	private TableColumn<Profile, String> nameColumn;
	@FXML
	private TableColumn<Profile, String> statusColumn;
	@FXML
	private TableColumn<Profile, String> proxyColumn;
	@FXML
	private TableColumn<Profile, String> lastRunColumn;

	@FXML
	private TextField urlField;
	@FXML
	private TextArea logArea;

	@FXML
	private TableColumn<Profile, Void> actionColumn;
	@FXML
	private TableColumn<Profile, Boolean> selectColumn;
	@FXML
	private TextField profileOpen;

	private final ProfileService profileService;

	@FXML
	private TextField widthField;
	@FXML
	private CheckBox isSendEmail;
	@FXML
	private TextField heightField;

	private Stage primaryStage;
	String url = "http://127.0.0.1:19995/";

	public void setPrimaryStage(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}

	public ProfileController() {
		this.profileService = ProfileService.getInstance();
	}

	@FXML
	public void initialize() {
		nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
		statusColumn.setCellValueFactory(new PropertyValueFactory<>("browser_type"));
		proxyColumn.setCellValueFactory(new PropertyValueFactory<>("raw_proxy"));
		lastRunColumn.setCellValueFactory(new PropertyValueFactory<>("profile_path"));

		selectColumn.setCellFactory(CheckBoxTableCell.forTableColumn(selectColumn));
		selectColumn.setCellValueFactory(cellData -> cellData.getValue().selectedProperty());
		actionColumn.setCellFactory(new Callback<TableColumn<Profile, Void>, TableCell<Profile, Void>>() {
			@Override
			public TableCell<Profile, Void> call(final TableColumn<Profile, Void> param) {
				return new TableCell<Profile, Void>() {
					private final Button btn = new Button();

					{
						btn.setOnAction(event -> {
							Profile profile = getTableView().getItems().get(getIndex());
							String url = urlField.getText();
							if (profile.isRunning()) {
								profileService.stopSingleProfile(url, profile);
							} else {
//								profileService.startProfile(profile);
							}
						});
					}

					@Override
					public void updateItem(Void item, boolean empty) {
						super.updateItem(item, empty);
						if (empty) {
							setGraphic(null);
						} else {
							Profile profile = getTableView().getItems().get(getIndex());
							if (profile.isRunning()) {
								btn.setText("Stop");
								btn.setStyle("-fx-background-color: red; -fx-text-fill: white;");
							} else {
								btn.setText("Start");
								btn.setStyle("-fx-background-color: green; -fx-text-fill: white;");
							}
							setGraphic(btn);
						}
					}
				};
			}
		});
		setSizeOfColumns();
		urlField.setText("http://127.0.0.1:19995/");
		url = urlField.getText().toString();
		profileTable.setRowFactory(tv -> {
			TableRow<Profile> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if (!row.isEmpty() && event.getClickCount() == 1) { // Chỉ xử lý khi click một lần
					Profile rowData = row.getItem();
					rowData.setSelected(!rowData.isSelected());
					profileTable.refresh();
				}
			});
			return row;
		});
		profileOpen.textProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue.matches("\\d*")) {
				profileOpen.setText(newValue.replaceAll("[^\\d]", ""));
			}
		});
	}

	private void setSizeOfColumns() {
		selectColumn.setPrefWidth(90);
		nameColumn.setPrefWidth(200);
		statusColumn.setPrefWidth(120);
		proxyColumn.setPrefWidth(200);
		lastRunColumn.setPrefWidth(150);
	}

	@FXML
	private void handleGo() {
		profileService.loadProfiles(url, profileTable);
	}

	@FXML
	private void handleStart() {
		profileService.startProfile(profileTable, url, this);
	}

	@FXML
	private void handleStop() {
		profileService.stopProfile(url, profileTable);
	}

	@FXML
	private void nurtureProfile() {
		int profileWillOpen = Integer.parseInt(profileOpen.getText());
		String width = widthField.getText().toString();
		String height = heightField.getText().toString();
		profileService.nurtureProfile(url, profileTable, isSendEmail.isSelected(), this, profileWillOpen, width,
				height);
	}

	@Override
	public void onProfileUpdated(Profile profile, String status) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProfileError(Profile profile, String error) {
		// TODO Auto-generated method stub

	}

}
