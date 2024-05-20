package com.vdqgpm;

import java.io.IOException;

import com.vdqgpm.libs.GPMLoginApiV3;
import com.vdqgpm.ui.controllers.ProfileController;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

	@Override
	public void start(Stage primaryStage) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/fxml/ProfileView.fxml"));

		Parent root = loader.load();
		Scene scene = new Scene(root);
		primaryStage.setTitle("Profile Management");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

}
