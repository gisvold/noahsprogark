package no.ntnu.noahsprogark.bedpresbingo;

public class BingoCell {
	private String word;
	private boolean selected;
	public BingoCell(String word) {
		this.word = word;
	}
	
	public boolean isSelected() {
		return selected;
	}
	
	public String getWord() {
		return word;
	}
}
