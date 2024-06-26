package com.vdqgpm.utilities;

import java.util.Random;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public final class Utils {

	public static JsonObject readJson(String data) {
		JsonObject jsonObject = JsonParser.parseString(data).getAsJsonObject();
		return jsonObject;
	}

	public static void showAlert(String title, String content, AlertType type) {
		Alert alert = new Alert(type);
		alert.setTitle(title);
		alert.setHeaderText(null); // Bỏ qua header text nếu không cần thiết
		alert.setContentText(content);
		alert.showAndWait();
	}

	public static int randomNumber(int begin, int end) {
		Random random = new Random();
		int randomIntInRange = begin + random.nextInt(end - begin + 1);
		return randomIntInRange;
	}
}
