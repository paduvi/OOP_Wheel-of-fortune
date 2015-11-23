package com.chotoxautinh.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import com.chotoxautinh.model.Block;
import com.chotoxautinh.model.Player;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;

public class MultiPlayerGameController extends GameController {

	private Queue<Player> playerQueue = new LinkedBlockingQueue<>(3);
	private List<Player> players = new ArrayList<>();

	private final long DELAY = 1000 / 60;
	private long beforeTime;

	private final int MAX_ROUND = 3;
	private List<Block> blockList = new ArrayList<>();
	private boolean charBtnReady = false;

	@FXML
	private Accordion playerAccordion;

	@FXML
	private TitledPane wheelPane;

	@FXML
	private Label keyLabel;

	@FXML
	private GridPane keyboard;

	@FXML
	private Button nextBtn;

	@FXML
	private TextArea mcSpeech;

	@FXML
	private Label questionLabel;

	@FXML
	private void initialize() {
		for (int i = 0; i < 3; i++) {
			players.add(new Player(i));
		}

		for (Node node : keyboard.getChildren()) {
			node.addEventHandler(ActionEvent.ACTION, buttonActionHandler);
		}

		FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/chotoxautinh/view/WheelLayout.fxml"));
		try {
			wheelPane.setContent((GridPane) loader.load());

			wheelController = loader.getController();
			wheelController.setGameController(this);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		resetQuiz();
		drawTempInfo();
	}

	private void drawTempInfo() {
		new AnimationTimer() {
			public void handle(long currentNanoTime) {
				long timeDiff, sleep;

				timeDiff = currentNanoTime - beforeTime;
				sleep = DELAY * 1000 - timeDiff;

				if (sleep < 0) {
					for (Player player : players) {
						Canvas canvas = (Canvas) playerAccordion.getPanes().get(player.getIndex()).getContent();
						GraphicsContext gc = canvas.getGraphicsContext2D();
						gc.clearRect(0, 0, 270, 180);
						Image avatar = new Image("file:resources/images/stuff/" + player.getIndex() + ".png", 120, 120,
								true, true);
						gc.drawImage(avatar, 15, 15);

						gc.fillText("Điểm vòng này: " + player.getTempPoint(), 150, 30, 120);
						gc.fillText("Số lượt còn lại: " + player.getLeft(), 150, 60, 120);
					}

					beforeTime = currentNanoTime;
				}
			}
		}.start();
	}

	@Override
	public void handleWheelFinished() {
		mcSpeech.setText("Ban đã quay vào ô " + wheelController.getWheel().getPile().getLabel());
		charBtnReady = true;
	}

	private void resetQuiz() {
		mcSpeech.setText("Chào mừng các bạn đến với vòng thi thứ " + (noRound + 1));
		if (noRound < MAX_ROUND + 1) {
			for (Player player : players) {
				if (player.isWinner())
					player.setPoint(player.getPoint() + player.getTempPoint());
				player.setTempPoint(0);
				player.setLeft(3);
				playerAccordion.getPanes().get(player.getIndex())
						.setText(player.getName() + " - Tổng số điểm: " + player.getPoint());
			}
		}
		if (noRound == MAX_ROUND) {
			long max = players.get(0).getPoint();
			for (Player player : players) {
				if (player.getPoint() > max) {
					max = player.getPoint();
				}
			}
			Iterator<Player> it = players.iterator();
			while (it.hasNext()) {
				if (it.next().getPoint() < max)
					it.remove();
			}
		} else if (noRound > MAX_ROUND) {
			Iterator<Player> it = players.iterator();
			while (it.hasNext()) {
				if (!it.next().isWinner())
					it.remove();
			}
		}
		for (Player player : players) {
			player.setWinner(false);
		}
		playerQueue.clear();
		for (int i = noRound; i < noRound + players.size(); i++) {
			playerQueue.add(players.get(i % players.size()));
		}

		Timeline timeline = new Timeline(new KeyFrame(Duration.millis(4000), ae -> {
			String question = "Ai là người đẹp trai nhất Vịnh Bắc Bộ?";
			questionLabel.setText(question);
			String key = "VIET DEP ZAI";
			blockList.clear();
			for (int i = 0; i < key.length(); i++) {
				blockList.add(new Block(key.charAt(i)));
			}
			handleKeyLabel();
			playerAccordion.setExpandedPane(playerAccordion.getPanes().get(playerQueue.peek().getIndex()));
			mcSpeech.setText("Câu hỏi mà chúng tôi đặt ra là: " + question);
			wheelController.setReady(true);
		}));
		timeline.play();
	}

	@Override
	public void handleKeyLabel() {
		StringBuilder builder = new StringBuilder();
		for (Block block : blockList) {
			if (block.isSpace()) {
				builder.append(" ");
			} else if (block.isFaceUp()) {
				builder.append(block.getCharacter());
			} else {
				builder.append("_");
			}
		}
		keyLabel.setText(builder.toString());
	}

	EventHandler<ActionEvent> buttonActionHandler = new EventHandler<ActionEvent>() {

		@Override
		public void handle(ActionEvent event) {
			if (charBtnReady) {
				charBtnReady = false;
				Button btn = (Button) event.getSource();
				String temp = btn.getText();
				int number = 0;
				for (Block block : blockList) {
					if (block.isFaceUp() || block.isSpace())
						continue;
					if (block.getCharacter().equalsIgnoreCase(temp)) {
						block.setFaceUp(true);
						number++;
					}
				}
				if (number == 0) {
					mcSpeech.setText("Rất tiếc! Không có chữ +" + temp + " nào");
					switchUser();
				} else {
					mcSpeech.setText("Thật là tuyệt vời. Có " + number + " chữ " + temp + ". Xin mời tiếp tục!");
					handleTruePredict();
					wheelController.setReady(true);
				}
			}
		}
	};

	private void switchUser() {
		Player current = playerQueue.poll();
		int temp = current.getLeft() - 1;
		current.setLeft(temp);
		if (temp > 0) {
			playerQueue.add(current);
		} else {
			Timeline timeline = new Timeline(new KeyFrame(Duration.millis(2000), ae -> {
				mcSpeech.setText("Rất tiếc người chơi " + current.getName()
						+ " đã đoán sai 3 lần và không được tiếp tục quay trong vòng này");
			}));
			timeline.play();
		}
		if (playerQueue.isEmpty()) {
			noRound++;
			quizOver();
		} else {
			Timeline timeline = new Timeline(new KeyFrame(Duration.millis(2000), ae -> {
				mcSpeech.setText("Quyền quay được chuyển sang cho người chơi " + playerQueue.peek().getName());
				playerAccordion.setExpandedPane(playerAccordion.getPanes().get(playerQueue.peek().getIndex()));
				wheelController.setReady(true);
			}));
			timeline.play();
		}
	}

	private void handleTruePredict() {
		handleKeyLabel();
		for (Block block : blockList) {
			if (block.isFaceUp() || block.isSpace())
				continue;
			wheelController.setReady(true);
			return;
		}
		noRound++;
		mcSpeech.setText("Người chơi " + playerQueue.peek().getName() + " đã chiến thắng ở vòng này");
		playerQueue.peek().setWinner(true);
		quizOver();
	}

	private void quizOver() {
		for (Block block : blockList) {
			block.setFaceUp(true);
		}
		handleKeyLabel();
		Timeline timeline = new Timeline(new KeyFrame(Duration.millis(2000), ae -> {
			if (players.size() > 1) {
				mcSpeech.setText("Bấm vào nút 'Next Stage' để tiếp tục");
				nextBtn.setVisible(true);
			} else
				showHighScore();
		}));
		timeline.play();
	}

	public void showHighScore() {
		System.out.println("Show High Score");
	}

	@FXML
	private void handleNextStage() {
		resetQuiz();
		nextBtn.setVisible(false);
	}

}
