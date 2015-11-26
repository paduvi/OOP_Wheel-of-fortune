package com.chotoxautinh.util;

import com.chotoxautinh.model.HighScore;

import javafx.collections.ObservableList;

public class InsertionSortUtil {
	public static int getInsertionIndex(ObservableList<HighScore> highScoreList, int capacity, HighScore obj) {
		int i = -1;
		int currentSize = highScoreList.size();
		for (HighScore temp : highScoreList) {
			i++;
			if (obj.getPoint() > temp.getPoint()) {
				highScoreList.add(i, obj);
				break;
			}
		}
		if (highScoreList.size() == currentSize) {
			if (currentSize < capacity) {
				highScoreList.add(obj);
				i = currentSize;
			} else
				i = -1;
		}
		while (highScoreList.size() > capacity) {
			highScoreList.remove(capacity);
		}
		return i;
	}
}
