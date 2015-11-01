package com.chotoxautinh;

import java.io.IOException;

import com.chotoxautinh.controller.WheelController;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Wheel extends Application {

	private AnchorPane rootLayout;

	@Override
	public void start(Stage primaryStage) {
		try {
			primaryStage.setTitle("Wheel of fortune");

			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("view/WheelLayout.fxml"));
			rootLayout = (AnchorPane) loader.load();

			// Show the scene containing the root layout.
			Scene scene = new Scene(rootLayout);
			primaryStage.setScene(scene);

			// Give the controller access to the main app.
			WheelController controller = loader.getController();
			controller.setMainApp(this);

			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		launch(args);
	}
}
