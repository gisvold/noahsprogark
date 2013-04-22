package no.ntnu.noahsprogark.bedpresbingo;

public enum BingoType {
	NONE(0), SINGLE(1), GOLDEN_SINGLE(2), DOUBLE(3), GOLDEN_DOUBLE(4), TRIPLE(5), GOLDEN_TRIPLE(
			6), MEGA(7), GOLDEN_MEGA(8);

	private int value;

	private BingoType(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
}
