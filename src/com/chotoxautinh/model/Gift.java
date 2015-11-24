package com.chotoxautinh.model;

public class Gift {
	private boolean opened;
	private int bonusPoint;

	public int getBonusPoint() {
		return bonusPoint;
	}

	public void setBonusPoint(int bonusPoint) {
		this.bonusPoint = bonusPoint;
	}

	public boolean isOpened() {
		return opened;
	}

	public void setOpened(boolean opened) {
		this.opened = opened;
	}

	public Gift(int bonusPoint) {
		this.bonusPoint = bonusPoint;
		this.opened = false;
	}
}
