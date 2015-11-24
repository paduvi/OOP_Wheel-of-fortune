package com.chotoxautinh;

import java.io.File;
import java.io.IOException;

import com.chotoxautinh.controller.GameController;
import com.chotoxautinh.controller.MainController;
import com.chotoxautinh.controller.PrizeController;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class App extends Application {

	private Stage primaryStage;
	private BorderPane rootLayout;
	private MediaPlayer mediaPlayer;

	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Chiếc nón kì diệu");

		// Set the application icon
		this.primaryStage.getIcons().add(new Image("file:resources/images/stuff/logo.png"));
		this.primaryStage.setResizable(false);
		initRootLayout();
		showMainLayout();
	}

	public BorderPane getRootLayout() {
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
			((StackPane) rootLayout.getCenter()).getChildren().add(mainPage);

			// Give the controller access to the main app
			MainController controller = loader.getController();
			controller.setMainApp(this);

		} catch (IOException e) {
			// Exception gets thrown if the fxml file could not be loaded
			e.printStackTrace();
		}
	}

	public void showPrizeLayout(GameController gameController) {
		try {
			// Load the fxml file and create a new stage for the popup.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/com/chotoxautinh/view/PrizeLayout.fxml"));
			GridPane page = (GridPane) loader.load();
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Phần thưởng");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);

			// Set the persons into the controller.
			PrizeController controller = loader.getController();
			controller.setGameController(gameController);
			controller.setDialogStage(dialogStage);

			// Show the dialog and wait until the user closes it
			Platform.runLater(new Runnable(){
			    @Override
			    public void run() {
			    	dialogStage.showAndWait();              
			    }           
			});

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void playSong(String filePath){
		 String path = "resources/audio/"+filePath+".mp3";
		 Media media = new Media(new File(path).toURI().toString());
		 if(mediaPlayer != null)
			 mediaPlayer.stop();
		 mediaPlayer = new MediaPlayer(media);
		 mediaPlayer.setAutoPlay(true);
	}

	public static void main(String[] args) {
		launch(args);
	}
}
