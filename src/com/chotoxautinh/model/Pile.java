package com.chotoxautinh.model;

public enum Pile {
	ZERO("0 Điểm", 0), ONE_HUNDRED("100 Điểm", 1), TWO_HUNDRED("200 Điểm", 2), THREE_HUNDRED("300 Điểm",
			3), FOUR_HUNDRED("400 Điểm", 4), FIVE_HUNDRED("500 Điểm", 5), SIX_HUNDRED("600 Điểm",
					6), SEVEN_HUNDRED("700 Điểm", 7), EIGHT_HUNDRED("800 Điểm", 8), NINE_HUNDRED("900 Điểm",
							9), TWO_THOUSAND("2000 Điểm", 10), FREE_SPIN("Thêm lượt", 11), LOST_TURN("Mất lượt",
									12), BANKRUPT("Mất điểm", 13), SURPRISE("Phần thưởng", 14);

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
