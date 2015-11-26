package com.chotoxautinh.controller;

import com.chotoxautinh.model.Quiz;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EditQuizController {
	@FXML
	private TextArea questionField;
	@FXML
	private TextField answerField;

	private Stage dialogStage;
	private Quiz quiz;
	private boolean okClicked = false;

	@FXML
	private void initialize() {
	}

	/**
	 * Sets the stage of this dialog.
	 * 
	 * @param dialogStage
	 */
	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	/**
	 * Sets the person to be edited in the dialog.
	 * 
	 * @param person
	 */
	public void setPerson(Quiz quiz) {
		this.quiz = quiz;

		questionField.setText(quiz.getQuestion());
		answerField.setText(quiz.getAnswer());
	}

	/**
	 * Returns true if the user clicked OK, false otherwise.
	 * 
	 * @return
	 */
	public boolean isOkClicked() {
		return okClicked;
	}

	/**
	 * Called when the user clicks ok.
	 */
	@FXML
	private void handleOk() {
		if (isInputValid()) {
			quiz.setQuestion(questionField.getText());
			quiz.setAnswer(answerField.getText());

			okClicked = true;
			dialogStage.close();
		}
	}

	/**
	 * Called when the user clicks cancel.
	 */
	@FXML
	private void handleCancel() {
		dialogStage.close();
	}

	/**
	 * Validates the user input in the text fields.
	 * 
	 * @return true if the input is valid
	 */
	private boolean isInputValid() {
		String errorMessage = "";

		if (questionField.getText() == null || questionField.getText().length() == 0) {
			errorMessage += "No valid question!\n";
		}
		if (answerField.getText() == null || answerField.getText().length() == 0) {
			errorMessage += "No valid answer!\n";
		}

		if (errorMessage.length() == 0) {
			return true;
		} else {
			// Show the error message
			// Nothing selected
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Invalid Quiz");
			alert.setContentText(errorMessage);

			alert.showAndWait();
			return false;
		}
	}
}
