package com.chotoxautinh.controller;

import java.util.List;

import com.chotoxautinh.model.Gift;

public abstract class GameController {
	public abstract void handleWheelFinished();

	public abstract void handleKeyLabel();
	
	public abstract void showHighScore();
	
	protected int noRound;
	
	protected WheelController wheelController;
	
	public abstract List<Gift> getPrizes();
	
	public abstract void handlePrize(Gift gift);
}
