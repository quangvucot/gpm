package com.vdqgpm.ui.controllers;

import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.vdqgpm.libs.GPMLoginApiV3;
import com.vdqgpm.models.Profile;
import com.vdqgpm.models.WebDriverConfig;
import com.vdqgpm.pages.NurturePage;
import com.vdqgpm.pages.ProfilePage;
import com.vdqgpm.utilities.AlertActionHandler;
import com.vdqgpm.utilities.AlertUtils;
import com.vdqgpm.utilities.Utils;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

public class ProfileController {

	private ProfilePage profilePage;
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
	private TextField nameField;

	@FXML
	private TextField statusField;

	@FXML
	private TextField lastRunField;

	@FXML
	private TableColumn<Profile, Void> actionColumn;

	@FXML
	private TableColumn<Profile, Boolean> selectColumn;

	private GPMLoginApiV3 info = null;

	@FXML
	public void initialize() {

		selectColumn.setCellValueFactory(cellData -> cellData.getValue().selectedProperty());
		selectColumn.setCellFactory(CheckBoxTableCell.forTableColumn(selectColumn));

		CheckBox selectAllCheckbox = new CheckBox();
		selectColumn.setGraphic(selectAllCheckbox);

		selectAllCheckbox.setOnAction(event -> {
			boolean selected = selectAllCheckbox.isSelected();
			for (Profile profile : profileTable.getItems()) {
				profile.setSelected(selected);
			}
		});
		urlField.setText("http://127.0.0.1:19995/");

		profileTable.setRowFactory(tv -> {
			TableRow<Profile> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if (!row.isEmpty()) {
					Profile rowData = row.getItem();
					rowData.setSelected(!rowData.isSelected());
				}
			});
			return row;
		});
		nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
		statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
		proxyColumn.setCellValueFactory(new PropertyValueFactory<>("proxy"));
		lastRunColumn.setCellValueFactory(new PropertyValueFactory<>("lastRun"));
		setSizeOfColumns();
	}

	private void setSizeOfColumns() {
		selectColumn.setPrefWidth(90);
		nameColumn.setPrefWidth(200);
		statusColumn.setPrefWidth(120);
		proxyColumn.setPrefWidth(200);
		lastRunColumn.setPrefWidth(150);
//		urlField.setPrefWidth(500);
	}

	private Boolean fetchDataToTable(String api) {
		info = new GPMLoginApiV3(api);
		List<Profile> profiles = info.fetchProfiles();
		if (profiles != null && profiles.size() > 0) {
			profileTable.getItems().setAll(profiles);
			return true;
		}
		return false;
	}

	private void showProfileDetails(Profile profile) {
		nameField.setText(profile.getName());
		statusField.setText(profile.getStatus());
//	        proxyField.setText(profile.getProxy());
		lastRunField.setText(profile.getLastRun());
	}

	@FXML
	private void handleStart() {
		AlertUtils.showConfirmationAlert("Bắt đầu profile ", "Chạy các profile đã chọn?", new AlertActionHandler() {

			@Override
			public void onSuccess() {
				List<Profile> selectedProfiles = profileTable.getItems().stream().filter(Profile::isSelected)
						.collect(Collectors.toList());

				for (Profile profile : selectedProfiles) {
					WebDriverConfig driverConfig = info.startProfile(profile.getId());
					if (driverConfig != null) {
						setupActionColumn();
						profile.setRunning(true);
						logArea.appendText("Opening profile: " + profile.getName() + "\n");
						System.out.println("Mở Profile " + profile.getName() + "được mở thành công");
					} else {
						logArea.appendText("Opening profile: " + profile.getName() + "\n");
						System.out.println("Mở Profile " + profile.getName() + "không thành công");
					}

				}

			}

			@Override
			public void onFailure() {

			}
		});

	}

	@FXML
	private void handleStop() {

		AlertUtils.showConfirmationAlert("Dừng Profile", "Dừng tất cả profile đã chọn?", new AlertActionHandler() {

			@Override
			public void onSuccess() {
				List<Profile> selectedProfiles = profileTable.getItems().stream().filter(Profile::isSelected)
						.collect(Collectors.toList());
				Task<Void> task = new Task<>() {
					@Override
					protected Void call() throws Exception {
						for (Profile profile : selectedProfiles) {
							JsonObject jsonObject = Utils.readJson(info.stopProfile(profile.getId()));
							Boolean stopProfile = jsonObject.get("success").getAsBoolean();
							if (stopProfile == true) {
								setupActionColumn();
								profile.setRunning(false);
								logArea.appendText("Tắt profile: " + profile.getName() + "\n thành công");
							} else {
								logArea.appendText("Tắt profile: " + profile.getName() + "\n thất bại");

							}
						}
						return null;
					}
				};

			}

			@Override
			public void onFailure() {
				// TODO Auto-generated method stub

			}
		});

	}

	@FXML
	private void handleGo() {
		profileTable.getItems().clear();
		String url = urlField.getText().toString();
		String validateUrl = "https?:\\/\\/[^\\s/$.?#].[^\\s]*\\/?";
		Pattern pattern = Pattern.compile(validateUrl);
		Matcher matcher = pattern.matcher(url);
		if (!url.equals("") & matcher.find()) {
			if (url.charAt(url.length() - 1) == '/') {
				url = url.substring(0, url.length() - 1);
			}
			Boolean addProfileStatus = fetchDataToTable(url);
			if (addProfileStatus) {
				setupActionColumn();
				Utils.showAlert("Thông báo", "Thêm dữ liệu thành công", AlertType.CONFIRMATION);

			} else {
				Utils.showAlert("Thông báo", "Không tìm thấy dữ liệu", AlertType.INFORMATION);
			}
		} else {
			Utils.showAlert("Thông báo", "Kiểm tra lại URL của bạn", AlertType.WARNING);

		}

	}

	@FXML
	private void nurtureProfile() {

		AlertUtils.showConfirmationAlert("Nuôi Profile", "Nuôi tất cả Profile đã chọn?", new AlertActionHandler() {

			@Override
			public void onSuccess() {
				List<Profile> selectedProfiles = profileTable.getItems().stream().filter(Profile::isSelected)
						.collect(Collectors.toList());
				ExecutorService executorService = Executors.newFixedThreadPool(selectedProfiles.size());

				int delay = 0;
				for (Profile profile : selectedProfiles) {
					int finalDelay = delay;
					executorService.submit(() -> {
						try {

							// Thread.sleep(finalDelay * 1000); // Thêm trễ nếu cần
							WebDriverConfig driverConfig = info.startProfile(profile.getId());
							if (driverConfig != null) {
								// setupActionColumn();
								NurturePage nuturePage = new NurturePage();
								profile.setRunning(true);
								File file = new File(driverConfig.getDriver_path());
								nuturePage.setUp(driverConfig.getRemote_address(), file);
								System.out.println("Driver Remote " + driverConfig.getRemote_address());
								Platform.runLater(
										() -> logArea.appendText("Mở profile " + profile.getName() + " thành công\n"));

							} else {
								logArea.appendText("Opening profile: " + profile.getName() + "\n");
								Platform.runLater(() -> logArea
										.appendText("Mở profile " + profile.getName() + "không thành công\n"));
							}
						} catch (Exception e) {
							System.out.println("Lỗi khi thực thi trên profile: " + e.getMessage());
						} finally {
							info.stopProfile(profile.getId());
						}
					});
					// delay = (delay + 1) % 2; // Thay đổi khoảng trễ cho mỗi profile
				}
				executorService.shutdown();
			}

			@Override
			public void onFailure() {
				// TODO Auto-generated method stub

			}
		});

	}

	@FXML
	private void nurtureProfil2e() {

		AlertUtils.showConfirmationAlert("Nuôi Profile", "Nuôi tất cả Profile đã chọn?", new AlertActionHandler() {

			@Override
			public void onSuccess() {
				List<Profile> selectedProfiles = profileTable.getItems().stream().filter(Profile::isSelected)
						.collect(Collectors.toList());
				NurturePage nuturePage = new NurturePage();
				Task<Void> task = new Task<>() {
					@Override
					protected Void call() throws Exception {
						for (Profile profile : selectedProfiles) {
							WebDriverConfig driverConfig = info.startProfile(profile.getId());
							if (driverConfig != null) {
								setupActionColumn();
								File file = new File(driverConfig.getDriver_path());
								profile.setRunning(true);
								nuturePage.setUp(driverConfig.getRemote_address(), file);
								Platform.runLater(
										() -> logArea.appendText("Mở profile " + profile.getName() + " thành công\n"));
							} else {
								logArea.appendText("Opening profile: " + profile.getName() + "\n");
								Platform.runLater(() -> logArea
										.appendText("Mở profile " + profile.getName() + "không thành công\n"));
							}

						}
						return null;
					}
				};

			}

			@Override
			public void onFailure() {
				// TODO Auto-generated method stub

			}
		});

	}

	private void setupActionColumn() {
		actionColumn.setCellFactory(new Callback<TableColumn<Profile, Void>, TableCell<Profile, Void>>() {
			@Override
			public TableCell<Profile, Void> call(final TableColumn<Profile, Void> param) {
				final TableCell<Profile, Void> cell = new TableCell<Profile, Void>() {

					private final Button btn = new Button("Start");
					{
						btn.setOnAction((event) -> {
							Profile profile = getTableView().getItems().get(getIndex());
							if (profile.isRunning()) {
								if (info != null) {
									info.stopProfile(profile.getId());
									profile.setRunning(false);
									btn.setText("Start");
									btn.setStyle("-fx-background-color: green; -fx-text-fill: white;");
									logArea.appendText("Stopped: " + profile.getName() + "\n");
								}
							} else {
								if (info != null) {
									info.startProfile(profile.getId());
									profile.setRunning(true);
									btn.setText("Stop");
									btn.setStyle("-fx-background-color: red; -fx-text-fill: white;");
									logArea.appendText("Started: " + profile.getName() + "\n");
								}
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
				return cell;
			}
		});
	}

	private void addButtonToTable() {

		actionColumn.setCellFactory(new Callback<TableColumn<Profile, Void>, TableCell<Profile, Void>>() {
			@Override
			public TableCell<Profile, Void> call(final TableColumn<Profile, Void> param) {
				final TableCell<Profile, Void> cell = new TableCell<Profile, Void>() {

					private final Button btn = new Button("Start");
					{
						btn.setOnAction((event) -> {
							Profile profile = getTableView().getItems().get(getIndex());
							if (profile.isRunning()) {
								profile.setRunning(false);
								btn.setText("Start");
								logArea.appendText("Stopped: " + profile.getName() + "\n");
								btn.setStyle("-fx-background-color: green; -fx-text-fill: white;");

							} else {
								profile.setRunning(true);
								btn.setText("Stop");
								logArea.appendText("Started: " + profile.getName() + "\n");
								btn.setStyle("-fx-background-color: red; -fx-text-fill: white;");
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
				return cell;
			}
		});

	}

}
