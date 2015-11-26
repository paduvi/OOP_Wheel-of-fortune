package com.chotoxautinh.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import com.chotoxautinh.App;
import com.chotoxautinh.model.Block;
import com.chotoxautinh.model.Gift;
import com.chotoxautinh.model.Pile;
import com.chotoxautinh.model.Player;
import com.chotoxautinh.model.Quiz;
import com.chotoxautinh.util.BlockDisplayUtil;
import com.chotoxautinh.util.StringUtil;

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
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class EndlessGameController extends GameController {

	private Player player = new Player();

	private final long DELAY = 1000 / 60;
	private long beforeTime;

	private List<Block> blockList = new ArrayList<>();
	private boolean charBtnReady = false;
	private List<Gift> prizes;

	private List<Quiz> quizData;

	public List<Gift> getPrizes() {
		return prizes;
	}

	@FXML
	private Accordion playerAccordion;

	@FXML
	private TitledPane wheelPane;

	@FXML
	private Canvas keyCanvas;

	@FXML
	private GridPane keyboard;

	@FXML
	private Button nextBtn;

	@FXML
	private TextArea mcSpeech;

	@FXML
	private Label questionLabel;

	private App mainApp;
	private MediaPlayer mediaPlayer;

	public void setMainApp(App mainApp) {
		this.mainApp = mainApp;
		mainApp.playSong("GameThemeSong");
		quizData = mainApp.getQuizData();
		java.util.Collections.shuffle(quizData);
	}

	private void playSong(String filePath) {
		String path = "resources/audio/" + filePath + ".mp3";
		Media media = new Media(new File(path).toURI().toString());
		if (mediaPlayer != null)
			mediaPlayer.stop();
		mediaPlayer = new MediaPlayer(media);
		mediaPlayer.play();
	}

	@FXML
	private void initialize() {

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

	public EndlessGameController() {
		prizes = new LinkedList<>();

	}

	private void drawTempInfo() {
		new AnimationTimer() {
			public void handle(long currentNanoTime) {
				long timeDiff, sleep;

				timeDiff = currentNanoTime - beforeTime;
				sleep = DELAY * 1000 - timeDiff;

				if (sleep < 0) {
					Canvas canvas = (Canvas) playerAccordion.getPanes().get(player.getIndex()).getContent();
					GraphicsContext gc = canvas.getGraphicsContext2D();
					gc.clearRect(0, 0, 270, 180);
					Image avatar = new Image("file:resources/images/stuff/" + player.getIndex() + ".png", 120, 0, true,
							true);
					gc.drawImage(avatar, 15, 15);

					gc.fillText("Điểm vòng này: " + player.getTempPoint(), 150, 30, 120);
					gc.fillText("Số lượt còn lại: " + (player.getLeft() + player.getTempLeft()), 150, 60, 120);

					beforeTime = currentNanoTime;
				}
			}
		}.start();
	}

	@Override
	public void handleWheelFinished() {
		Pile pile = wheelController.getWheel().getPile();
		mcSpeech.setText("Bạn đã quay vào ô " + pile.getLabel());

		switch (pile.getId()) {
		case 12:
			switchUser(false);
			break;
		case 13:
			switchUser(true);
			break;
		case 14:
			Timeline timeline = new Timeline(new KeyFrame(Duration.millis(500), ae -> {
				mainApp.showPrizeLayout(this);
				mcSpeech.setText("Xin mời tiếp tục quay");
				wheelController.setReady(true);
			}));
			timeline.play();
			break;
		default:
			charBtnReady = true;
			break;
		}
	}

	private void concludeEachRound() {
		noRound++;
		if (player.isWinner()) {
			player.setPoint(player.getPoint() + player.getTempPoint());
			System.out.println("ahihi");
		}
		player.setTempPoint(0);
		player.setWinner(false);
		playerAccordion.getPanes().get(player.getIndex())
				.setText(player.getName() + " - Tổng số điểm: " + player.getPoint());
	}

	private void resetQuiz() {
		prizes.clear();
		Random r = new Random();
		for (int i = 0; i < 3; i++) {
			prizes.add(new Gift((r.nextInt(15) + 1) * 100));
		}
		for (Node node : keyboard.getChildren()) {
			node.setDisable(false);
		}
		mcSpeech.setText("Chào mừng các bạn đến với vòng thi thứ " + (noRound + 1));
		wheelPane.setText("Chiếc nón kỳ diệu - Vòng thứ " + (noRound + 1));

		Timeline timeline = new Timeline(new KeyFrame(Duration.millis(4000), ae -> {
			Quiz quiz = quizData.get(noRound);
			questionLabel.setText(quiz.getQuestion());
			String key = quiz.getAnswer();
			blockList = StringUtil.toBlockList(key);
			keyCanvas.setHeight(BlockDisplayUtil.calculateHeight(keyCanvas.getWidth(), blockList));
			handleKeyCanvas();
			playerAccordion.setExpandedPane(playerAccordion.getPanes().get(player.getIndex()));
			mcSpeech.setText("Câu hỏi mà chúng tôi đặt ra là: " + quiz.getQuestion());
			wheelController.setReady(true);
		}));
		timeline.play();
	}

	@Override
	public void handleKeyCanvas() {
		BlockDisplayUtil.paint(keyCanvas, blockList);
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
					mcSpeech.setText("Rất tiếc! Không có chữ " + temp + " nào");
					handleWrongPredict();
				} else {
					mcSpeech.setText("Thật là tuyệt vời. Có " + number + " chữ " + temp + ". Xin mời tiếp tục!");
					handleTruePredict();
				}
				Timeline timeline = new Timeline(new KeyFrame(Duration.millis(20), ae -> {
					btn.setDisable(true);
				}));
				timeline.play();
			}
		}
	};

	private void switchUser(boolean lostPoint) {
		playSong("failure");
		if (lostPoint) {
			player.setTempPoint(0);
		}
		wheelController.setReady(true);
	}

	private void handleWrongPredict() {
		playSong("wrong");
		int temp = player.getLeft() - 1;
		player.setLeft(temp);
		if (temp > 0) {
			wheelController.setReady(true);
		} else {
			Timeline timeline = new Timeline(new KeyFrame(Duration.millis(2000), ae -> {
				mcSpeech.setText("Rất tiếc người chơi " + player.getName()
						+ " đã đoán sai 3 lần và không được tiếp tục quay trong vòng này");
			}));
			timeline.play();
			quizOver();
		}
	}

	private void handleTruePredict() {
		playSong("right");
		handleKeyCanvas();
		Pile pile = wheelController.getWheel().getPile();
		switch (pile.getId()) {
		case 10:
			player.setTempPoint(player.getTempPoint() + 2000);
			break;
		case 11:
			player.setLeft(player.getLeft() + 1);
			break;
		default:
			player.setTempPoint(player.getTempPoint() + pile.getId() * 100);
			break;
		}
		for (Block block : blockList) {
			if (block.isFaceUp() || block.isSpace())
				continue;

			wheelController.setReady(true);
			return;
		}
		mcSpeech.setText("Người chơi " + player.getName() + " đã chiến thắng ở vòng này");
		player.setWinner(true);
		quizOver();
	}

	private void quizOver() {
		concludeEachRound();
		for (Block block : blockList) {
			block.setFaceUp(true);
		}
		handleKeyCanvas();
		Timeline timeline = new Timeline(new KeyFrame(Duration.millis(2000), ae -> {
			if (player.getLeft() > 0) {
				mcSpeech.setText("Bấm vào nút 'Next Stage' để tiếp tục");
				nextBtn.setVisible(true);
			} else
				showHighScore();
		}));
		timeline.play();
	}

	public void showHighScore() {
		mainApp.showHighScore("endless-game", player);
	}

	@FXML
	private void handleNextStage() {
		resetQuiz();
		nextBtn.setVisible(false);
	}

	@Override
	public void handlePrize(Gift gift) {
		player.setTempPoint(player.getTempPoint() + gift.getBonusPoint());
	}

}
