package com.chotoxautinh;

import java.io.IOException;

import com.chotoxautinh.controller.MainController;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class App extends Application {

	private Stage primaryStage;
	private BorderPane rootLayout;

	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Chiếc nón kì diệu");

		// Set the application icon
		this.primaryStage.getIcons()
				.add(new Image("file:resources/images/stuff/logo.png"));
		this.primaryStage.setResizable(false);
		initRootLayout();
		showMainLayout();
	}
	
	public BorderPane getRootLayout(){
		return rootLayout;
	}

	public void initRootLayout() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/com/chotoxautinh/view/RootLayout.fxml"));
			rootLayout = (BorderPane) loader.load();
			
			Scene scene = new Scene(rootLayout, 1000, 675);
			scene.getStylesheets().add(getClass().getResource("view/style/style.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void showMainLayout() {
		try {
			// Load the fxml file and set into the center of the main layout
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/chotoxautinh/view/MainLayout.fxml"));
			AnchorPane mainPage = (AnchorPane) loader.load();
			((StackPane)rootLayout.getCenter()).getChildren().add(mainPage);

			// Give the controller access to the main app
			MainController controller = loader.getController();
			controller.setMainApp(this);

		} catch (IOException e) {
			// Exception gets thrown if the fxml file could not be loaded
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
