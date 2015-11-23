package com.chotoxautinh;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class WheelSimulator extends Application {

	private GridPane rootLayout;

	@Override
	public void start(Stage primaryStage) {
		try {
			primaryStage.setTitle("Wheel of fortune");

			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/com/chotoxautinh/view/WheelLayout.fxml"));
			rootLayout = (GridPane) loader.load();

			// Show the scene containing the root layout.
			Scene scene = new Scene(rootLayout);
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);

			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		launch(args);
	}
}
