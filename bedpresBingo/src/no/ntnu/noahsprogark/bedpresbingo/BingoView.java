package no.ntnu.noahsprogark.bedpresbingo;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class BingoView extends ImageView {
	private BingoCell[][] board;
	
	public BingoView(Context c) {
		this(c, null);
	}
	
	public BingoView(Context c, AttributeSet as) {
		this(c, as, 0);
	}
	
	public BingoView(Context c, AttributeSet as, int defStyle) {
		super(c, as, defStyle);
	}

	public void buildBoard(String[] words, int dim) {
		board = new BingoCell[dim][dim];
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				board[i][j] = new BingoCell(words[i+j]);
			}
		}
	}
}
