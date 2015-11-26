package com.chotoxautinh.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;

import com.chotoxautinh.App;
import com.chotoxautinh.model.Block;
import com.chotoxautinh.model.Gift;
import com.chotoxautinh.model.Pile;
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
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class MultiPlayerGameController extends GameController {

	private Queue<Player> playerQueue = new LinkedBlockingQueue<>(3);
	private List<Player> players = new ArrayList<>();

	private final long DELAY = 1000 / 60;
	private long beforeTime;

	private final int MAX_ROUND = 3;
	private List<Block> blockList = new ArrayList<>();
	private boolean charBtnReady = false;
	private List<Gift> prizes;

	public List<Gift> getPrizes() {
		return prizes;
	}

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

	private App mainApp;
	private MediaPlayer mediaPlayer;

	public void setMainApp(App mainApp) {
		this.mainApp = mainApp;
		mainApp.playSong("GameThemeSong");
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

	public MultiPlayerGameController() {
		prizes = new LinkedList<>();

		Random r = new Random();
		for (int i = 0; i < 3; i++) {
			prizes.add(new Gift((r.nextInt(15) + 1) * 100));
		}
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
						Image avatar = new Image("file:resources/images/stuff/" + player.getIndex() + ".png", 120, 0,
								true, true);
						gc.drawImage(avatar, 15, 15);

						gc.fillText("Điểm vòng này: " + player.getTempPoint(), 150, 30, 120);
						gc.fillText("Số lượt còn lại: " + (player.getLeft() + player.getTempLeft()), 150, 60, 120);
					}

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
	
	private void concludeEachRound(){
		noRound++;
		if (noRound < MAX_ROUND + 1) {
			for (Player player : players) {
				if (player.isWinner()) {
					player.setPoint(player.getPoint() + player.getTempPoint());
					System.out.println("ahihi");
				}
				player.setTempPoint(0);
				player.setWinner(false);
				player.setLeft(3);
				player.setTempLeft(0);
				playerAccordion.getPanes().get(player.getIndex())
						.setText(player.getName() + " - Tổng số điểm: " + player.getPoint());
			}
		}
	}

	private void resetQuiz() {
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
		playerQueue.clear();
		for (int i = noRound; i < noRound + players.size(); i++) {
			playerQueue.add(players.get(i % players.size()));
		}
		if (playerQueue.size() == 1) {
			mcSpeech.setText("Chúc mừng người chơi " + playerQueue.peek().getName() + " "
					+ "đã giành chiến thắng chung cuộc và được tham dự vào vòng đặc biệt của chương trình");
		} else if (noRound > 2) {
			mcSpeech.setText("Chúng ta sẽ đến với vòng Playoff để tìm ra người chiến thắng chung cuộc"
					+ " tiếp tục bước vào vòng đặc biệt của chương trình");
		} else {
			mcSpeech.setText("Chào mừng các bạn đến với vòng thi thứ " + (noRound + 1));
		}

		Timeline timeline = new Timeline(new KeyFrame(Duration.millis(4000), ae -> {
			String question = "Ai là người đẹp trai nhất Vịnh Bắc Bộ?";
			questionLabel.setText(question);
			String key = "PHAN DUC VIET";
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
					mcSpeech.setText("Rất tiếc! Không có chữ " + temp + " nào");
					handleWrongPredict();
				} else {
					mcSpeech.setText("Thật là tuyệt vời. Có " + number + " chữ " + temp + ". Xin mời tiếp tục!");
					handleTruePredict();
					wheelController.setReady(true);
				}
			}
		}
	};

	private void switchUser(boolean lostPoint) {
		playSong("failure");
		Player current = new Player();
		if (playerQueue.peek().getTempLeft() > 0) {
			current = playerQueue.peek();
			current.setTempLeft(current.getTempLeft() - 1);
		} else {
			current = playerQueue.poll();
			playerQueue.add(current);
		}
		if (lostPoint) {
			current.setTempPoint(0);
		}
		Timeline timeline = new Timeline(new KeyFrame(Duration.millis(2000), ae -> {
			mcSpeech.setText("Quyền được quay thuộc về người chơi " + playerQueue.peek().getName());
			playerAccordion.setExpandedPane(playerAccordion.getPanes().get(playerQueue.peek().getIndex()));
			wheelController.setReady(true);
		}));
		timeline.play();
	}

	private void handleWrongPredict() {
		playSong("wrong");
		if (playerQueue.peek().getTempLeft() > 0) {
			Player current = playerQueue.peek();
			current.setTempLeft(current.getTempLeft() - 1);
		} else {
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
		}
		if (playerQueue.isEmpty()) {
			quizOver();
		} else {
			Timeline timeline = new Timeline(new KeyFrame(Duration.millis(2000), ae -> {
				mcSpeech.setText("Quyền được quay thuộc về người chơi " + playerQueue.peek().getName());
				playerAccordion.setExpandedPane(playerAccordion.getPanes().get(playerQueue.peek().getIndex()));
				wheelController.setReady(true);
			}));
			timeline.play();
		}
	}

	private void handleTruePredict() {
		playSong("right");
		handleKeyLabel();
		Pile pile = wheelController.getWheel().getPile();
		switch (pile.getId()) {
		case 10:
			playerQueue.peek().setTempPoint(playerQueue.peek().getTempPoint() + 2000);
			break;
		case 11:
			playerQueue.peek().setTempLeft(playerQueue.peek().getTempLeft() + 1);
			break;
		default:
			playerQueue.peek().setTempPoint(playerQueue.peek().getTempPoint() + pile.getId() * 100);
			break;
		}
		for (Block block : blockList) {
			if (block.isFaceUp() || block.isSpace())
				continue;

			wheelController.setReady(true);
			return;
		}
		mcSpeech.setText("Người chơi " + playerQueue.peek().getName() + " đã chiến thắng ở vòng này");
		playerQueue.peek().setWinner(true);
		quizOver();
	}

	private void quizOver() {
		concludeEachRound();
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
		mainApp.showHighScore("multi-player", playerQueue.peek());
	}

	@FXML
	private void handleNextStage() {
		resetQuiz();
		nextBtn.setVisible(false);
	}

	@Override
	public void handlePrize(Gift gift) {
		playerQueue.peek().setTempPoint(playerQueue.peek().getTempPoint() + gift.getBonusPoint());
	}

}
