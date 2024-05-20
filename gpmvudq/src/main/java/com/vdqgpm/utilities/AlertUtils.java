package com.vdqgpm.utilities;

import java.util.Optional;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

public class AlertUtils {

	public static void showConfirmationAlert(String title, String content, AlertActionHandler handler) {
		Platform.runLater(() -> {
			Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
			alert.setTitle(title);
			alert.setHeaderText(null);
			alert.setContentText(content);

			ButtonType buttonTypeOk = new ButtonType("OK", ButtonData.OK_DONE);
			ButtonType buttonTypeCancel = new ButtonType("Hủy", ButtonData.CANCEL_CLOSE);
			alert.getButtonTypes().setAll(buttonTypeOk, buttonTypeCancel);

			Optional<ButtonType> result = alert.showAndWait();
			if (result.isPresent()) {
				if (result.get() == buttonTypeOk) {
					handler.onSuccess();
				} else if (result.get() == buttonTypeCancel) {
					handler.onFailure();
				}
			}
		});
	}

}
