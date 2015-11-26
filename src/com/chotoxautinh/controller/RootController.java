package com.chotoxautinh.controller;

import java.io.File;
import java.util.Optional;

import com.chotoxautinh.App;
import com.chotoxautinh.util.FileUtil;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.util.Pair;

public class RootController {

	private App mainApp;

	public void setMainApp(App mainApp) {
		this.mainApp = mainApp;
	}

	public RootController() {

	}

	@FXML
	private void quizListHandle() {
		Dialog<Pair<String, String>> dialog = new Dialog<>();
		dialog.setTitle("Login Dialog");
		dialog.setHeaderText("Login Dialog");

		// Set the icon (must be included in the project).
		dialog.setGraphic(new ImageView("file:resources/images/stuff/security.png"));

		// Set the button types.
		ButtonType loginButtonType = new ButtonType("Login", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

		// Create the username and password labels and fields.
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 150, 10, 10));

		TextField username = new TextField();
		username.setPromptText("Username");
		PasswordField password = new PasswordField();
		password.setPromptText("Password");

		grid.add(new Label("Username:"), 0, 0);
		grid.add(username, 1, 0);
		grid.add(new Label("Password:"), 0, 1);
		grid.add(password, 1, 1);

		boolean error = false;
		Label errorLabel = new Label("Wrong user or password!");
		errorLabel.setStyle("-fx-text-fill:red");
		errorLabel.setVisible(false);
		grid.add(errorLabel, 1, 2);

		// Enable/Disable login button depending on whether a username was
		// entered.
		Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
		loginButton.setDisable(true);

		// Do some validation (using the Java 8 lambda syntax).
		username.textProperty().addListener((observable, oldValue, newValue) -> {
			loginButton.setDisable(newValue.trim().isEmpty());
		});

		dialog.getDialogPane().setContent(grid);
		dialog.initOwner(mainApp.getPrimaryStage());

		// Request focus on the username field by default.
		Platform.runLater(() -> username.requestFocus());

		// Convert the result to a username-password-pair when the login button
		// is clicked.
		dialog.setResultConverter(dialogButton -> {
			if (dialogButton == loginButtonType) {
				return new Pair<>(username.getText(), password.getText());
			}
			return null;
		});

		do {
			Optional<Pair<String, String>> result = dialog.showAndWait();

			if (result.isPresent()) {
				String auth = FileUtil.readFile(new File("resources/user/admin.properties"));
				String adminUser = auth.split("\n")[0].trim();
				String adminPass = auth.split("\n")[1].trim();
				if (result.get().getKey().equals(adminUser) && result.get().getValue().equals(adminPass)) {
					error = false;
					mainApp.showQuizOverview();
				} else {
					errorLabel.setVisible(true);
					error = true;
				}
			} else {
				error = false;
			}
		} while (error);
	}

	@FXML
	private void mainMenuHandle() {
		if (((StackPane) mainApp.getRootLayout().getCenter()).getChildren().size() > 1) {
			mainApp.playSong("MainMenuThemeSong");
			while (((StackPane) mainApp.getRootLayout().getCenter()).getChildren().size() > 1)
				((StackPane) mainApp.getRootLayout().getCenter()).getChildren().remove(1);
		}
	}

	@FXML
	private void handleExit() {
		System.exit(0);
	}

	@FXML
	private void handleAbout() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("About");
		String header = "Bộ môn Lập trình hướng đối tượng - TS. Cao Tuấn Dũng"
				+ "\nMã đề DE11A: Chương trình Chiếc nón kì diệu";
		alert.setHeaderText(header);
		String content = "Danh sách thành viên Nhóm 7 - KSTN CNTT K58: \n" + "- Phan Đức Việt\n" + "- La Văn Quân\n"
				+ "- Lương Xuân Tiến";
		alert.setContentText(content);

		alert.showAndWait();
	}
	
	@FXML
	private void handleHighScoreView() {
		mainApp.showHighScoreView();
	}
	
	@FXML
	private void handleIntroduce(){
		mainApp.showIntro();
	}
}
