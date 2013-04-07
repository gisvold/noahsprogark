package no.ntnu.noahsprogark.bedpresbingo;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

public class BingoView extends ImageView {
	private static int CELL_WIDTH;
	private static int CELL_HEIGHT;
	private static int CELL_MARGIN_TOP;
	private static int CELL_MARGIN_LEFT;
	private static float CELL_TEXT_SIZE;
	public static BingoView INSTANCE = null;

	private BingoCell[][] board;
	private String[] words;
	private OnCellTouchListener octl = null;

	public interface OnCellTouchListener {
		public void onTouch(BingoCell c);
	}

	public BingoView(Context c) {
		this(c, null);
	}

	public BingoView(Context c, AttributeSet as) {
		this(c, as, 0);
	}

	public BingoView(Context c, AttributeSet as, int defStyle) {
		super(c, as, defStyle);
		Resources r = getResources();
		CELL_HEIGHT = (int) r.getDimension(R.dimen.cell_heigh);
		CELL_MARGIN_LEFT = (int) r.getDimension(R.dimen.cell_margin_left);
		CELL_MARGIN_TOP = (int) r.getDimension(R.dimen.cell_margin_top);
		CELL_WIDTH = (int) r.getDimension(R.dimen.cell_width);
		CELL_TEXT_SIZE = r.getDimension(R.dimen.cell_text_size);
		INSTANCE = this;
		words = new String[1];
		words[0] = "ERROR";
		board = new BingoCell[1][1];
	}

	public void setWords(String[] words) {
		this.words = words;
	}

	public void buildBoard() {
		Rect bounds = new Rect(CELL_MARGIN_LEFT, CELL_MARGIN_TOP, CELL_WIDTH
				+ CELL_MARGIN_LEFT, CELL_HEIGHT + CELL_MARGIN_TOP);
		int k = 0;

		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				board[i][j] = new BingoCell(words[k++], new Rect(bounds),
						CELL_TEXT_SIZE);
				bounds.offset(CELL_WIDTH, 0);
			}
			bounds.offset(0, CELL_HEIGHT);
			bounds.left = CELL_MARGIN_LEFT;
			bounds.right = CELL_MARGIN_LEFT + CELL_WIDTH;
		}
	}

	@Override
	public void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		buildBoard();
		super.onLayout(changed, left, top, right, bottom);
	}

	@Override
	protected void onDraw(Canvas c) {
		super.onDraw(c);
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				board[i][j].draw(c);
			}
		}
	}

	public void setDim(int dim) {
		board = new BingoCell[dim][dim];
	}

	public BingoType hasBingo() {
		int size = board.length;
		int numOfBingos = 0;
		int maxBingos = (size * 2) + 2;
		BingoType type;

		for (int i = 0; i < board.length; i++) {
			int rowTrue = 0;
			int colTrue = 0;
			for (int j = 0; j < board[i].length; j++) {
				if (board[i][j].isSelected())
					rowTrue++;
				if (board[j][i].isSelected())
					colTrue++;
			}
			if (rowTrue == size)
				numOfBingos++;
			if (colTrue == size)
				numOfBingos++;
		}

		int diagTrue = 0;
		for (int i = 0, j = 0; i < size && j < size; i++, j++) {
			if (board[i][j].isSelected())
				diagTrue++;
		}
		if (diagTrue == size)
			numOfBingos++;

		diagTrue = 0;
		for (int i = 0, j = size - 1; i < size && j >= 0; i++, j--) {
			if (board[i][j].isSelected())
				diagTrue++;
		}
		if (diagTrue == size)
			numOfBingos++;
		if (numOfBingos == 0)
			type = BingoType.NONE;
		else if (numOfBingos == maxBingos)
			type = BingoType.MEGA;
		else if (numOfBingos == 1)
			type = BingoType.SINGLE;
		else if (numOfBingos == 2)
			type = BingoType.DOUBLE;
		else
			type = BingoType.TRIPLE;
		return type;
	}

	public void setOnCellTouchListener(OnCellTouchListener octl) {
		this.octl = octl;
	}

	public boolean onTouchEvent(MotionEvent e) {
		if (octl != null) {
			for (BingoCell[] bc : board) {
				for (BingoCell b : bc) {
					if (b.doesHit((int) e.getX(), (int) e.getY())) {
						octl.onTouch(b);
					}
				}
			}
		}
		return super.onTouchEvent(e);
	}
}
