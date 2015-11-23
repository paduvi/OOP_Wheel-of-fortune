package com.chotoxautinh.controller;

public abstract class GameController {
	public abstract void handleWheelFinished();

	public abstract void handleKeyLabel();
	
	public abstract void showHighScore();
	
	protected int noRound;
	
	protected WheelController wheelController;
}
