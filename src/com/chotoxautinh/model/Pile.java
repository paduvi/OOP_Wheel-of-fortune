package com.chotoxautinh.model;

public enum Pile {
	ZERO("0", 0), ONE_HUNDRED("100", 1), TWO_HUNDRED("200", 2), THREE_HUNDRED("300", 3), FOUR_HUNDRED("400",
			4), FIVE_HUNDRED("500", 5), SIX_HUNDRED("600", 6), SEVEN_HUNDRED("700", 7), EIGHT_HUNDRED("800",
					8), NINE_HUNDRED("900", 9), TWO_THOUSAND("2000", 10), FREE_SPIN("Thêm lượt",
							11), LOST_TURN("Mất lượt", 12), BANKRUPT("Mất điểm", 13), SURPRISE("Phần thưởng", 14);

	private String label;
	private int id;

	Pile(String label, int id) {
		this.id = id;
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

	public int getId() {
		return id;
	}
}
