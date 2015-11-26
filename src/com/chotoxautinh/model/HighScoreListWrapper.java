package com.chotoxautinh.model;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "highScoreList")
public class HighScoreListWrapper {
	private List<HighScore> highScoreList;

	@XmlElement(name = "highscore")
	public List<HighScore> getHighScoreList() {
		return highScoreList;
	}

	public void setHighScoreList(List<HighScore> highScoreList) {
		this.highScoreList = highScoreList;
	}
}
