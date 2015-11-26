package com.chotoxautinh.util;

import java.util.LinkedList;
import java.util.List;

import com.chotoxautinh.model.Block;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class BlockDisplayUtil extends Application {
	private final static int IMAGE_WIDTH = 50;
	private final static int IMAGE_HEIGHT = 50;
	private final static int VGAP = 5;
	private final static int HGAP = 10;

	public static void paint(Canvas canvas, List<Block> blockList) {
		GraphicsContext gc = canvas.getGraphicsContext2D();
		gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
		double width = canvas.getWidth();
		double tempY = 0;
		double tempX = 0;
		List<List<Block>> lineList = lineList(width, wordList(blockList));
		for (List<Block> line : lineList) {
			tempX = (width - line.size() * (2 * VGAP + IMAGE_WIDTH)) / 2;
			for (Block chr : line) {
				if (!chr.isSpace()) {
					if (chr.isFaceUp() == true) {
						if (chr.getCharacter().equals(".")) {
							Image image = new Image("file:resources/images/block/dot.png", 0, 50, true, true);
							gc.drawImage(image, tempX, tempY);
						} else {
							Image image = new Image("file:resources/images/block/" + chr.getCharacter() + ".png", 0, 50,
									true, true);
							gc.drawImage(image, tempX, tempY);
						}
					} else {
						Image image = new Image("file:resources/images/block/hiden.png", 0, 50, true, true);
						gc.drawImage(image, tempX, tempY);
					}
				}
				tempX += (2 * VGAP + IMAGE_WIDTH);
			}
			tempY += IMAGE_HEIGHT + 2 * HGAP;
		}
	}

	private static List<Block> substring(List<Block> blockList, int start, int end) {
		return blockList.subList(start, end);
	}

	private static List<Block> substring(List<Block> blockList, int index) {
		return blockList.subList(index, blockList.size());
	}

	private static List<List<Block>> lineList(double width, List<List<Block>> wordList) {
		int tempX = 0;
		int capacity = (int) width / (2 * VGAP + IMAGE_WIDTH);

		List<List<Block>> lineList = new LinkedList<>();
		List<Block> builder = new LinkedList<>();

		for (List<Block> word : wordList) {
			if (builder.isEmpty()) {
				while (word.size() > capacity) {
					lineList.add(substring(word, 0, capacity));
					word = substring(word, capacity);
				}
				if (word.size() > capacity - 2) {
					lineList.add(word);
					tempX = 0;
				} else {
					builder.addAll(word);
					builder.add(new Block(' '));
					tempX = word.size() + 1;
				}
			} else {
				if (tempX + word.size() > capacity) {
					if (word.size() > capacity) {
						int leftSpace = capacity - tempX;
						builder.addAll(substring(word, 0, leftSpace));
						lineList.add(builder);
						builder = new LinkedList<>();
						word = substring(word, 0, leftSpace);
						while (word.size() > capacity) {
							lineList.add(substring(word, 0, capacity));
							word = substring(word, capacity);
						}
						if (word.size() > capacity - 2) {
							lineList.add(word);
							tempX = 0;
						} else {
							builder.addAll(word);
							builder.add(new Block(' '));
							tempX = word.size() + 1;
						}
					} else if (word.size() > capacity - 2) {
						lineList.add(builder);
						builder = new LinkedList<>();
						lineList.add(word);
						tempX = 0;
					} else {
						lineList.add(builder);
						builder = new LinkedList<>();
						builder.addAll(word);
						builder.add(new Block(' '));
						tempX = word.size() + 1;
					}
				} else if (tempX + word.size() > capacity - 2) {
					builder.addAll(word);
					lineList.add(builder);
					builder = new LinkedList<>();
				} else {
					builder.addAll(word);
					builder.add(new Block(' '));
					tempX += word.size() + 1;
				}
			}
		}
		if (!builder.isEmpty())
			lineList.add(builder);
		return lineList;
	}

	private static List<List<Block>> wordList(List<Block> blockList) {
		List<List<Block>> wordList = new LinkedList<>();
		List<Block> builder = new LinkedList<>();
		for (Block block : blockList) {
			if (block.isSpace()) {
				wordList.add(builder);
				builder = new LinkedList<>();
				continue;
			}
			builder.add(block);
		}
		if (!builder.isEmpty()) {
			wordList.add(builder);
		}
		return wordList;
	}

	public static double calculateHeight(double width, List<Block> blockList) {
		return lineList(width, wordList(blockList)).size() * (IMAGE_HEIGHT + 2 * HGAP);
	}

	@Override
	public void start(Stage theStage) {
		List<Block> blockList = new LinkedList<>();
		String key = "PADUVI CHOTOXAUTINH";
		for (int i = 0; i < key.length(); i++) {
			blockList.add(new Block(key.charAt(i)));
		}
		for (int i = 0; i < key.length(); i++) {
			blockList.get(i).setFaceUp(true);
		}
		Canvas canvas = new Canvas();
		canvas.setWidth(720);
		canvas.setHeight(BlockDisplayUtil.calculateHeight(canvas.getWidth(), blockList));
		paint(canvas, blockList);
		theStage.setTitle("Chán đời");
		theStage.getIcons()
				.add(new Image("file:resources/images/stuff/10613146_1557821241104184_883503913799230125_n.jpg"));

		Group root = new Group();
		Scene theScene = new Scene(root);
		theStage.setScene(theScene);

		root.getChildren().add(canvas);
		theStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
