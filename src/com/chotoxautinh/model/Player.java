package com.chotoxautinh.model;

public class Player {
	private String name;
	private long point;
	private long tempPoint;
	private int left;
	private int tempLeft;
	private boolean winner;
	private int index;

	public Player() {
	}
	
	public Player(int index) {
		this.index = index;
		name = "Player " + (index + 1);
		point = 0;
		left = 3;
		tempLeft = 0;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getPoint() {
		return point;
	}

	public void setPoint(long point) {
		this.point = point;
	}

	public int getLeft() {
		return left;
	}

	public void setLeft(int left) {
		this.left = left;
	}

	public long getTempPoint() {
		return tempPoint;
	}

	public void setTempPoint(long tempPoint) {
		this.tempPoint = tempPoint;
	}

	public int getIndex() {
		return index;
	}

	public boolean isWinner() {
		return winner;
	}

	public void setWinner(boolean winner) {
		this.winner = winner;
	}

	public int getTempLeft() {
		return tempLeft;
	}

	public void setTempLeft(int tempLeft) {
		this.tempLeft = tempLeft;
	}

}
