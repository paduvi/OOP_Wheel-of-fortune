package com.chotoxautinh.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.chotoxautinh.Wheel;

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
import javafx.scene.shape.Circle;
import javafx.scene.transform.Rotate;

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
	private double wheelAngle = 0;
	private double omega = 0;
	private double anpha = 0;
	private List<Pile> pileList = new ArrayList<Pile>();
	private Wheel mainApp;
	private Image wheel;
	private Image arrow;
	private GraphicsContext gc;
	private boolean ready;
	private Point2D origin;
	private Circle wheelShape;
	private int direction = NONE;
	private double startAngle;
	private long[] timeArray = new long[360];

	public void setMainApp(Wheel mainApp) {
		this.mainApp = mainApp;
	}

	@FXML
	private void initialize() {
		Collections.addAll(pileList, Pile.FOUR_HUNDRED, Pile.TWO_THOUSAND, Pile.THREE_HUNDRED, Pile.SEVEN_HUNDRED,
				Pile.LOST_TURN, Pile.SIX_HUNDRED, Pile.ZERO, Pile.FIVE_HUNDRED, Pile.ONE_HUNDRED, Pile.FREE_SPIN,
				Pile.TWO_HUNDRED, Pile.THREE_HUNDRED, Pile.SURPRISE, Pile.NINE_HUNDRED, Pile.EIGHT_HUNDRED,
				Pile.BANKRUPT, Pile.ONE_HUNDRED, Pile.TWO_HUNDRED, Pile.NINE_HUNDRED, Pile.THREE_HUNDRED);
		gc = canvas.getGraphicsContext2D();
		wheel = new Image("file:resources/images/non.png", 350, 0, true, true);
		wheelShape = new Circle(350 / 2, 20 + 350 / 2, 350 / 2);
		arrow = new Image("file:resources/images/arrow.png", 0, 25, true, true);
		canvas.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				canvas.setCursor(Cursor.OPEN_HAND);
			}
		});
		ready = true;
		beforeTime = System.nanoTime();
		new AnimationTimer() {
			public void handle(long currentNanoTime) {
				long timeDiff, sleep;

				timeDiff = currentNanoTime - beforeTime;
				sleep = DELAY * 1000 - timeDiff;

				if (sleep < 0) {

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
		gc.setFill(new Color(0.85, 0.85, 1.0, 1.0));
		gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
	}

	private void paint(GraphicsContext gc) {
		clearBackground(gc);

		drawRotatedImage(gc, wheel, wheelAngle, 0, 20);

		gc.setFill(Color.BLACK);
		gc.fillText("Omega: " + omega, 10, 400);
		int i = (int) getDegreeFromNumber(wheelAngle) * pileList.size() / 360;
		gc.fillText("Pile: " + pileList.get(i).getPile(), 10, 425);

		gc.drawImage(arrow, (canvas.getWidth() - arrow.getWidth()) / 2 - 1, 0);
	}

	private int getDegreeFromNumber(double number) {
		int div = (int) number / 360;
		int mod = (int) number - 360 * div;
		if (mod < 0)
			mod += 360;
		return mod;
	}

	private void rotateWheel() {

		if (omega <= 0) {
			powerBar.setProgress(0);
			omega = 0;
			anpha = 0;
			ready = true;
			return;
		}
		omega -= anpha;
		wheelAngle = wheelAngle + direction * omega;
	}

	private void rotate(GraphicsContext gc, double angle, double px, double py) {
		Rotate r = new Rotate(angle, px, py);
		gc.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());
	}

	/**
	 * Draws an image on a graphics context.
	 *
	 * The image is drawn at (tlpx, tlpy) rotated by angle pivoted around the
	 * point: (tlpx + image.getWidth() / 2, tlpy + image.getHeight() / 2)
	 *
	 * @param gc
	 *            the graphics context the image is to be drawn on.
	 * @param angle
	 *            the angle of rotation.
	 * @param tlpx
	 *            the top left x co-ordinate where the image will be plotted (in
	 *            canvas co-ordinates).
	 * @param tlpy
	 *            the top left y co-ordinate where the image will be plotted (in
	 *            canvas co-ordinates).
	 */
	private void drawRotatedImage(GraphicsContext gc, Image image, double angle, double tlpx, double tlpy) {
		gc.save(); // saves the current state on stack, including the current
					// transform
		rotate(gc, angle, tlpx + image.getWidth() / 2, tlpy + image.getHeight() / 2);
		gc.drawImage(image, tlpx, tlpy);
		gc.restore(); // back to original state (before rotation)
	}

	private void actionMouseDragged(MouseEvent me) {
		if (ready) {
			int originDirection = direction;
			if (origin == null)
				origin = new Point2D(me.getX(), me.getY());
			Point2D current = new Point2D(me.getX(), me.getY());

			Point2D originVector = origin.subtract(wheelShape.getCenterX(), wheelShape.getCenterY());
			double originAngle;
			if (origin.getY() < wheelShape.getCenterY()) {
				originAngle = -originVector.angle(1, 0);
			} else {
				originAngle = originVector.angle(1, 0);
			}
			Point2D currentVector = current.subtract(wheelShape.getCenterX(), wheelShape.getCenterY());
			double currentAngle;
			if (current.getY() < wheelShape.getCenterY()) {
				currentAngle = -currentVector.angle(1, 0);
			} else {
				currentAngle = currentVector.angle(1, 0);
			}
			double angleDif = currentAngle - originAngle;
			wheelAngle += angleDif;

			if (angleDif > 0)
				direction = CLOCKWISE;
			if (angleDif < 0)
				direction = REVERSE_CLOCKWISE;
			if (angleDif > 180)
				direction = REVERSE_CLOCKWISE;
			if (angleDif < -180)
				direction = CLOCKWISE;
			if (direction != originDirection)
				startAngle = Math.floor(wheelAngle);
			origin = current;
		}
	}

	private void actionMouseReleased() {
		if (ready) {
			long currentTime = System.nanoTime();
			int endAngle = (int) Math.floor(wheelAngle);
			if (Math.abs(endAngle - startAngle) < 30)
				return;
			if (endAngle > startAngle) {
				omega = (5 * DELAY * 1E6) / (currentTime - timeArray[getDegreeFromNumber(endAngle - 5)]);
			} else {
				omega = (5 * DELAY * 1E6) / (currentTime - timeArray[getDegreeFromNumber(endAngle + 5)]);
			}
			anpha = 5E-2;
			ready = false;
		}
	}

}

enum Pile {
	ZERO("0"), ONE_HUNDRED("100"), TWO_HUNDRED("200"), THREE_HUNDRED("300"), FOUR_HUNDRED("400"), FIVE_HUNDRED(
			"500"), SIX_HUNDRED("600"), SEVEN_HUNDRED("700"), EIGHT_HUNDRED("800"), NINE_HUNDRED("900"), TWO_THOUSAND(
					"2000"), FREE_SPIN("Thêm lượt"), LOST_TURN("Mất lượt"), BANKRUPT("Mât điểm"), SURPRISE(
							"Phần thưởng");

	private String value;

	Pile(String value) {
		this.value = value;
	}

	public String getPile() {
		return value;
	}
}
