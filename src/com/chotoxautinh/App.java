package com.chotoxautinh;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class App extends Application {

	private Stage primaryStage;

	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Chiếc nón kì diệu");

		// Set the application icon
		this.primaryStage.getIcons()
				.add(new Image("file:resources/images/10613146_1557821241104184_883503913799230125_n.jpg"));
		this.primaryStage.setResizable(false);
		initRootLayout();
	}

	public void initRootLayout() {
		try {
			BorderPane root = (BorderPane) FXMLLoader.load(getClass().getResource("view/Main.fxml"));
			Scene scene = new Scene(root, 800, 625);
			scene.getStylesheets().add(getClass().getResource("view/style/style.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
