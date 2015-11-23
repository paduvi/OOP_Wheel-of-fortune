package com.chotoxautinh.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.transform.Rotate;

public class Wheel {
	private Image image;
	private double angle;
	private double centerX;
	private double centerY;
	private double speed;
	private double acceleration;
	private List<Pile> pileList = new ArrayList<>();
	private double width;
	private double height;

	public Wheel(double centerX, double centerY, double width, double height) {
		this.centerX = centerX;
		this.centerY = centerY;
		this.setWidth(width);
		this.setHeight(height);
		image = new Image("file:resources/images/stuff/non.png", width, height, false, true);
		setAngle(0);
		Collections.addAll(pileList, Pile.FOUR_HUNDRED, Pile.TWO_THOUSAND, Pile.THREE_HUNDRED, Pile.SEVEN_HUNDRED,
				Pile.LOST_TURN, Pile.SIX_HUNDRED, Pile.ZERO, Pile.FIVE_HUNDRED, Pile.ONE_HUNDRED, Pile.FREE_SPIN,
				Pile.TWO_HUNDRED, Pile.THREE_HUNDRED, Pile.SURPRISE, Pile.NINE_HUNDRED, Pile.EIGHT_HUNDRED,
				Pile.BANKRUPT, Pile.ONE_HUNDRED, Pile.TWO_HUNDRED, Pile.NINE_HUNDRED, Pile.THREE_HUNDRED);
	}

	public void drawRotatedImage(GraphicsContext gc) {
		gc.save(); // saves the current state on stack, including the current
					// transform
		rotate(gc, angle, centerX, centerY);
		gc.drawImage(image, centerX - image.getWidth() / 2, centerY - image.getHeight() / 2);
		gc.restore(); // back to original state (before rotation)
	}

	private void rotate(GraphicsContext gc, double angle, double px, double py) {
		Rotate r = new Rotate(angle, px, py);
		gc.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());
	}

	public double getAngle() {
		return angle;
	}

	public void setAngle(double angle) {
		this.angle = angle;
	}

	public double getCenterX() {
		return centerX;
	}

	public double getCenterY() {
		return centerY;
	}

	public Pile getPile() {
		int i = (int) getDegreeFromNumber(angle) * pileList.size() / 360;
		return pileList.get(i);
	}

	private int getDegreeFromNumber(double number) {
		int div = (int) number / 360;
		int mod = (int) number - 360 * div;
		if (mod < 0)
			mod += 360;
		return mod;
	}

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

	public double getAcceleration() {
		return acceleration;
	}

	public void setAcceleration(double acceleration) {
		this.acceleration = acceleration;
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

}
