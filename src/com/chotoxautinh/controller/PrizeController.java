package com.chotoxautinh.controller;

import com.chotoxautinh.model.Gift;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class PrizeController {

	@FXML
	private GridPane prizeBg;

	@FXML
	private Label messageLabel;

	@FXML
	private Button okBtn;

	private Stage dialogStage;

	private GameController gameController;

	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	public void setGameController(GameController gameController) {
		this.gameController = gameController;
		int count = 0;
		for (int i = 0; i < gameController.getPrizes().size(); i++) {
			AnchorPane anc = (AnchorPane) prizeBg.getChildren().get(i);
			Canvas canvas = (Canvas) anc.getChildren().get(0);
			GraphicsContext gc = canvas.getGraphicsContext2D();
			Button btn = (Button) anc.getChildren().get(1);
			Label label = (Label) anc.getChildren().get(2);
			Gift gift = gameController.getPrizes().get(i);

			if (!gift.isOpened()) {
				count++;
				Image img = new Image("file:resources/images/stuff/prize.png", 120, 0, true, true);
				gc.drawImage(img, 40, 40);
				btn.setOnAction(new ButtonHandle(i));
			} else {
				label.setText(gift.getBonusPoint() + " ĐIỂM");
				label.setVisible(true);
				btn.setVisible(false);
			}
		}
		if (count == 0) {
			messageLabel.setText("Không còn phần thưởng để mở");
			okBtn.setVisible(true);
		}
	}

	@FXML
	private void handleOk() {
		dialogStage.close();
	}

	private class ButtonHandle implements EventHandler<ActionEvent> {

		private int index;

		public ButtonHandle(int index) {
			this.index = index;
		}

		@Override
		public void handle(ActionEvent event) {
			for (int i = 0; i < prizeBg.getChildren().size(); i++) {
				AnchorPane anc = (AnchorPane) prizeBg.getChildren().get(i);
				Node btn = anc.getChildren().get(1);
				btn.setVisible(false);
			}
			AnchorPane anc = (AnchorPane) prizeBg.getChildren().get(index);
			Canvas canvas = (Canvas) anc.getChildren().get(0);
			Label label = (Label) anc.getChildren().get(2);
			Gift gift = gameController.getPrizes().get(index);
			gift.setOpened(true);
			GraphicsContext gc = canvas.getGraphicsContext2D();
			gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
			label.setText(gift.getBonusPoint() + " ĐIỂM");
			messageLabel.setText("Phần thưởng bạn nhận được là " + gift.getBonusPoint() + " điểm");
			gameController.handlePrize(gift);
			label.setVisible(true);
			okBtn.setVisible(true);
		}
	}

}
