package com.chotoxautinh.model;

public class Block {
	private String character;
	private boolean space;
	private boolean faceUp;

	public Block(char character) {
		if (character == ' ') {
			space = true;
			faceUp = true;
		} else {
			this.character = String.valueOf(character).toUpperCase();
			faceUp = false;
		}
	}

	public String getCharacter() {
		return character;
	}

	public void setCharacter(String character) {
		this.character = character;
	}

	public boolean isSpace() {
		return space;
	}

	public void setSpace(boolean space) {
		this.space = space;
	}

	public boolean isFaceUp() {
		return faceUp;
	}

	public void setFaceUp(boolean faceUp) {
		this.faceUp = faceUp;
	}

}
