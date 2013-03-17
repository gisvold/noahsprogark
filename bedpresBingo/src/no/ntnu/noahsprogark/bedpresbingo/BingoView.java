package no.ntnu.noahsprogark.bedpresbingo;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ImageView;

public class BingoView extends ImageView {
	private static int CELL_WIDTH;
	private static int CELL_HEIGHT;
	private static int CELL_MARGIN_TOP;
	private static int CELL_MARGIN_LEFT;
	private static float CELL_TEXT_SIZE;

	private BingoCell[][] board;

	public BingoView(Context c) {
		this(c, null);
	}

	public BingoView(Context c, AttributeSet as) {
		this(c, as, 0);
	}

	public BingoView(Context c, AttributeSet as, int defStyle) {
		super(c, as, defStyle);
		Resources r = getResources();
		System.out.println("HEI!");
		CELL_HEIGHT = (int) r.getDimension(R.dimen.cell_heigh);
		CELL_MARGIN_LEFT = (int) r.getDimension(R.dimen.cell_margin_left);
		CELL_MARGIN_TOP = (int) r.getDimension(R.dimen.cell_margin_top);
		CELL_WIDTH = (int) r.getDimension(R.dimen.cell_width);
		CELL_TEXT_SIZE = r.getDimension(R.dimen.cell_text_size);
	}

	public void buildBoard(String[] words, int dim) {
		board = new BingoCell[dim][dim];
		Rect bounds = new Rect(CELL_MARGIN_LEFT, CELL_MARGIN_TOP, CELL_WIDTH
				+ CELL_MARGIN_LEFT, CELL_HEIGHT + CELL_MARGIN_TOP);
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				board[i][j] = new BingoCell(words[i + j], new Rect(bounds),
						CELL_TEXT_SIZE);
				bounds.offset(CELL_WIDTH, 0);
			}
			bounds.offset(0, CELL_HEIGHT);
			bounds.left = CELL_MARGIN_LEFT;
			bounds.right = CELL_MARGIN_LEFT + CELL_WIDTH;
		}
	}
}
