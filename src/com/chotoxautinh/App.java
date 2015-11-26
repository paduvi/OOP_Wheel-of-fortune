package com.chotoxautinh;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import com.chotoxautinh.controller.EditHighScoreController;
import com.chotoxautinh.controller.EditQuizController;
import com.chotoxautinh.controller.GameController;
import com.chotoxautinh.controller.HighScoreOverviewController;
import com.chotoxautinh.controller.MainController;
import com.chotoxautinh.controller.PrizeController;
import com.chotoxautinh.controller.QuizOverviewController;
import com.chotoxautinh.controller.RootController;
import com.chotoxautinh.model.HighScore;
import com.chotoxautinh.model.Player;
import com.chotoxautinh.model.Quiz;
import com.chotoxautinh.util.XmlUtil;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
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
	private ObservableList<Quiz> quizData = FXCollections.observableArrayList();

	public App() {
		quizData = XmlUtil.loadQuizDataFromFile();
	}

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

	public Stage getPrimaryStage() {
		return primaryStage;
	}

	public BorderPane getRootLayout() {
		return rootLayout;
	}

	public void initRootLayout() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/com/chotoxautinh/view/RootLayout.fxml"));
			rootLayout = (BorderPane) loader.load();

			// Give the controller access to the main app
			RootController controller = loader.getController();
			controller.setMainApp(this);

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
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					dialogStage.showAndWait();
				}
			});

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void showHighScore(String filePath, Player player) {
		try {
			playSong("applause");
			// Load the fxml file and create a new stage for the popup.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/com/chotoxautinh/view/EditHighScoreLayout.fxml"));
			BorderPane page = (BorderPane) loader.load();
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Phần thưởng");
			dialogStage.getIcons()
					.add(new Image("file:resources/images/stuff/10613146_1557821241104184_883503913799230125_n.jpg"));
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);

			File folder = new File(getClass().getResource("/com/chotoxautinh/savedata/").toURI());
			ObservableList<HighScore> highScoreList = XmlUtil
					.loadHighScoreDataFromFile(new File(folder, filePath + ".xml"));

			// Set the persons into the controller.
			EditHighScoreController controller = loader.getController();
			controller.setDialogStage(dialogStage, highScoreList, player);

			// Show the dialog and wait until the user closes it
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					dialogStage.showAndWait();
					XmlUtil.saveHighScoreDataToFile(new File(folder, filePath + ".xml"), controller.getTableList());
					playSong("MainMenuThemeSong");
					((StackPane) rootLayout.getCenter()).getChildren().remove(1);
				}
			});

		} catch (IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}

	public void playSong(String filePath) {
		String path = "resources/audio/" + filePath + ".mp3";
		Media media = new Media(new File(path).toURI().toString());
		if (mediaPlayer != null)
			mediaPlayer.stop();
		mediaPlayer = new MediaPlayer(media);
		mediaPlayer.setAutoPlay(true);
	}

	public void showQuizOverview() {
		try {
			// Load the fxml file and set into the center of the main layout
			FXMLLoader loader = new FXMLLoader(
					getClass().getResource("/com/chotoxautinh/view/QuizOverviewLayout.fxml"));
			AnchorPane overviewPage = (AnchorPane) loader.load();
			((StackPane) rootLayout.getCenter()).getChildren().add(overviewPage);

			// Give the controller access to the main app
			QuizOverviewController controller = loader.getController();
			controller.setMainApp(this);

		} catch (IOException e) {
			// Exception gets thrown if the fxml file could not be loaded
			e.printStackTrace();
		}
	}

	public boolean showPersonEditDialog(Quiz quiz) {
		try {
			// Load the fxml file and create a new stage for the popup
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/chotoxautinh/view/EditQuizLayout.fxml"));
			AnchorPane page = (AnchorPane) loader.load();
			Stage dialogStage = new Stage();
			// Set the application icon
			dialogStage.getIcons()
					.add(new Image("file:resources/images/stuff/10613146_1557821241104184_883503913799230125_n.jpg"));
			dialogStage.setTitle("Edit Quiz");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);

			// Set the person into the controller
			EditQuizController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setPerson(quiz);

			// Show the dialog and wait until the user closes it
			dialogStage.showAndWait();

			return controller.isOkClicked();

		} catch (IOException e) {
			// Exception gets thrown if the fxml file could not be loaded
			e.printStackTrace();
			return false;
		}
	}

	public ObservableList<Quiz> getQuizData() {
		return quizData;
	}

	public void showHighScoreView() {
		try {
			// Load the fxml file and create a new stage for the popup.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/com/chotoxautinh/view/HighScoreOverviewLayout.fxml"));
			AnchorPane page = (AnchorPane) loader.load();
			Stage dialogStage = new Stage();
			dialogStage.setTitle("High Score");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);

			File folder = new File(getClass().getResource("/com/chotoxautinh/savedata/").toURI());
			Map<Integer, ObservableList<HighScore>> highScoreMap = new HashMap<>();
			highScoreMap.put(0, XmlUtil.loadHighScoreDataFromFile(new File(folder, "classic-game.xml")));
			highScoreMap.put(1, XmlUtil.loadHighScoreDataFromFile(new File(folder, "endless-game.xml")));
			highScoreMap.put(2, XmlUtil.loadHighScoreDataFromFile(new File(folder, "multi-player.xml")));

			// Set the persons into the controller.
			HighScoreOverviewController controller = loader.getController();
			controller.setDialogStage(dialogStage, highScoreMap);

			dialogStage.show();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}

	public void showIntro() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Intro");
		alert.setHeaderText("Giới thiệu về Game Chiếc Nón Kì Diệu");
		String content = "Chơi game Chiếc Nón Kì Diệu, bạn sẽ được thử tài với những câu hỏi"
				+ " hóc búa, thú vị nhưng mang đậm chất truyền thống của dân tộc ta. Bắt đầu trò chơi bằng"
				+ " việc quay chiếc nón để có thể biết được bạn được bao nhiêu điểm nếu trả lời đúng, hoặc bạn sẽ"
				+ " được thêm lượt, hoặc bị mất lượt,… Trên màn hình chính sẽ có một số ô chữ gồm nhiều chữ cái chưa"
				+ " được mở, cùng với bảng chữ cái để bạn có thể lựa chọn để đoán trong mỗi lượt. Nếu bạn giải ra ô"
				+ " chữ thì điểm số cao sẽ thuộc về bạn, còn nếu quá một số lượt nhất định, bạn sẽ thua và phải"
				+ " chơi lại từ đầu.\n\nLà một trò chơi rất bổ ích, bạn hãy mời gọi bạn bè có thêm được nhiều sự"
				+ " thú vị như bạn qua game Chiếc Nón"
				+ " Kì Diệu nhé. Sự ganh đua trong điểm số, cùng sẽ là động lực để bạn có thêm nhiều kiến"
				+ " thức bổ ích cho mình hơn đấy. Hãy thoả thích chơi trò chơi miễn phí này bạn nhé.";
		alert.setContentText(content);

		alert.showAndWait();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
