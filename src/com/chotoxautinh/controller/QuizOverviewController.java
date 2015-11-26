package com.chotoxautinh.controller;

import java.io.File;
import java.net.URISyntaxException;

import com.chotoxautinh.App;
import com.chotoxautinh.model.Quiz;
import com.chotoxautinh.util.XmlUtil;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class QuizOverviewController {
	@FXML
	private TableView<Quiz> quizTable;
	@FXML
	private TableColumn<Quiz, String> questionColumn;
	@FXML
	private TableColumn<Quiz, String> answerColumn;

	@FXML
	private Label questionLabel;
	@FXML
	private Label answerLabel;

	public QuizOverviewController() {
	}

	@FXML
	private void initialize() {
		// Initialize the quiz table
		questionColumn.setCellValueFactory(cellData -> cellData.getValue().questionProperty());
		answerColumn.setCellValueFactory(cellData -> cellData.getValue().answerProperty());

		// clear quiz
		showQuizDetails(null);

		// Listen for selection changes
		quizTable.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> showQuizDetails(newValue));
	}

	// Reference to the main application
	private App mainApp;

	/**
	 * Is called by the main application to give a reference back to itself.
	 * 
	 * @param mainApp
	 */
	public void setMainApp(App mainApp) {
		this.mainApp = mainApp;

		// Add observable list data to the table
		quizTable.setItems(mainApp.getQuizData());
	}

	/**
	 * Fills all text fields to show details about the quiz. If the specified
	 * quiz is null, all text fields are cleared.
	 * 
	 * @param quiz
	 *            the quiz or null
	 */
	private void showQuizDetails(Quiz quiz) {
		if (quiz != null) {
			questionLabel.setText(quiz.getQuestion());
			answerLabel.setText(quiz.getAnswer());

			return;
		}
		questionLabel.setText("");
		answerLabel.setText("");
	}

	@FXML
	private void handleDeletePerson() {
		int selectedIndex = quizTable.getSelectionModel().getSelectedIndex();
		if (selectedIndex >= 0) {
			quizTable.getItems().remove(selectedIndex);
		} else {
			// Nothing selected
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("No Quiz Selected");
			alert.setContentText("Please select a quiz in the table.");

			alert.showAndWait();
		}
	}

	@FXML
	private void handleNewPerson() {
		Quiz tempPerson = new Quiz();
		boolean okClicked = mainApp.showPersonEditDialog(tempPerson);
		if (okClicked) {
			mainApp.getQuizData().add(tempPerson);
			quizTable.getSelectionModel().select(tempPerson);
		}
	}

	/**
	 * Called when the user clicks the edit button. Opens a dialog to edit
	 * details for the selected person.
	 */
	@FXML
	private void handleEditPerson() {
		Quiz selectedPerson = quizTable.getSelectionModel().getSelectedItem();
		if (selectedPerson != null) {
			boolean okClicked = mainApp.showPersonEditDialog(selectedPerson);
			if (okClicked) {
				refreshPersonTable();
				showQuizDetails(selectedPerson);
			}

		} else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("No Quiz Selected");
			alert.setContentText("Please select a quiz in the table.");

			alert.showAndWait();
		}
	}

	private void refreshPersonTable() {
		int selectedIndex = quizTable.getSelectionModel().getSelectedIndex();
		quizTable.setItems(null);
		quizTable.layout();
		quizTable.setItems(mainApp.getQuizData());

		quizTable.getSelectionModel().select(selectedIndex);
	}

	private void closeStage() {
		((StackPane) mainApp.getRootLayout().getCenter()).getChildren().remove(1);
	}

	@FXML
	private void handleSaveChanges() {
		try {
			File folder = new File(getClass().getResource("/com/chotoxautinh/savedata/").toURI());
			XmlUtil.saveQuizDataToFile(new File(folder, "quiz.xml"), mainApp.getQuizData());
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			closeStage();
		}
	}

	@FXML
	private void handleCancel() {
		closeStage();
	}

}
