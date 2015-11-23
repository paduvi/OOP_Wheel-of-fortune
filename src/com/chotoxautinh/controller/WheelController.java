package com.chotoxautinh.controller;

import com.chotoxautinh.model.Wheel;

import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class WheelController {

	@FXML
	private Canvas canvas;

	@FXML
	private ProgressBar powerBar;

	private final int NONE = 0;
	private final int CLOCKWISE = 1;
	private final int REVERSE_CLOCKWISE = -1;

	private final long DELAY = 1000 / 60;
	private long beforeTime;
	
	private Wheel wheel;
	private Image arrow;
	private GraphicsContext gc;
	private boolean ready;
	private Point2D origin;
	private int direction = NONE;
	private double powerProgress;
	private long startTime;
	private boolean stop;

	private GameController gameController;

	public Wheel getWheel() {
		return wheel;
	}

	public void setReady(boolean ready) {
		this.ready = ready;
	}

	@FXML
	private void initialize() {
		gc = canvas.getGraphicsContext2D();
		wheel = new Wheel(10 + 240 / 2, 15 + 240 / 2, 240, 240);
		arrow = new Image("file:resources/images/stuff/arrow1.png", 0, 32, true, true);

		ready = true;
		beforeTime = System.nanoTime();
		new AnimationTimer() {
			public void handle(long currentNanoTime) {
				long timeDiff, sleep;

				timeDiff = currentNanoTime - beforeTime;
				sleep = DELAY * 1000 - timeDiff;

				if (sleep < 0) {
					if (!ready)
						canvas.setCursor(Cursor.DEFAULT);
					powerBar.setProgress(powerProgress);
					rotateWheel();
					paint(gc);

					beforeTime = currentNanoTime;
				}
			}
		}.start();

		canvas.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				actionMouseDragged(event);
			}
		});

		canvas.setOnMouseReleased(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				actionMouseReleased();
			}
		});
	}

	private void clearBackground(GraphicsContext gc) {
		gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
	}

	private void paint(GraphicsContext gc) {
		clearBackground(gc);

		gc.setFill(Color.YELLOWGREEN);
		gc.fillOval(0, 5, 20 + wheel.getWidth(), 20 + wheel.getHeight());

		wheel.drawRotatedImage(gc);

		gc.drawImage(arrow, wheel.getCenterX() - arrow.getWidth() / 2 - 2, 0);
	}

	private void rotateWheel() {

		powerBar.setProgress(powerProgress);
		if (wheel.getSpeed() <= 0 && ready == false) {
			wheel.setSpeed(0);
			wheel.setAcceleration(0);
			origin = null;
			powerProgress = 0;
			direction = NONE;
			if (!stop) {
				gameController.handleWheelFinished();
				stop = true;
			}
			// ready = true;
			return;
		}
		long currentTime = System.currentTimeMillis();
		wheel.setAcceleration(wheel.getAcceleration() - Math.exp((startTime - currentTime) / 4000.0) * 3E-4);
		wheel.setSpeed(wheel.getSpeed() + wheel.getAcceleration());
		wheel.setAngle(wheel.getAngle() - direction * wheel.getSpeed());
	}

	private void actionMouseDragged(MouseEvent me) {
		if (ready) {
			canvas.setCursor(Cursor.CLOSED_HAND);
			if (origin == null)
				origin = new Point2D(me.getX(), me.getY());
			Point2D current = new Point2D(me.getX(), me.getY());

			Point2D originVector = origin.subtract(wheel.getCenterX(), wheel.getCenterY());
			double originAngle;
			if (origin.getY() < wheel.getCenterY()) {
				originAngle = -originVector.angle(1, 0);
			} else {
				originAngle = originVector.angle(1, 0);
			}
			Point2D currentVector = current.subtract(wheel.getCenterX(), wheel.getCenterY());
			double currentAngle;
			if (current.getY() < wheel.getCenterY()) {
				currentAngle = -currentVector.angle(1, 0);
			} else {
				currentAngle = currentVector.angle(1, 0);
			}
			double angleDif = currentAngle - originAngle;
			if (angleDif > 180)
				angleDif -= 360;
			else if (angleDif < -180)
				angleDif += 360;
			wheel.setAngle(wheel.getAngle() + angleDif);

			int curDir = direction;
			if (angleDif > 0) {
				curDir = CLOCKWISE;
			}
			if (angleDif < 0) {
				curDir = REVERSE_CLOCKWISE;
			}

			if (direction == NONE) {
				direction = curDir;
				powerProgress = Math.abs(angleDif) / 180;
			} else {
				if (curDir == direction) {
					powerProgress += Math.abs(angleDif) / 180;
				} else {
					double value = powerProgress - Math.abs(angleDif) / 180;
					if (value > 0) {
						powerProgress = value;
					} else if (value == 0) {
						direction = NONE;
						powerProgress = 0;
					} else {
						direction = curDir;
						powerProgress = Math.abs(value);
					}
				}
			}
			origin = current;
		}
	}

	private void actionMouseReleased() {
		if (ready && direction != NONE) {
			if (powerProgress < 0.05) {
				powerProgress = 0;
				return;
			} else if (powerProgress > 1)
				powerProgress = 1;
			wheel.setSpeed(2 + 8 * powerProgress);
			wheel.setAcceleration(25E-3 + powerProgress * 15E-3);
			startTime = System.currentTimeMillis();
			ready = false;
			stop = false;
		}
	}

	public GameController getGameController() {
		return gameController;
	}

	public void setGameController(GameController gameController) {
		this.gameController = gameController;
	}

}
