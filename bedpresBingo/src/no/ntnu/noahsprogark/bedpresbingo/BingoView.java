package no.ntnu.noahsprogark.bedpresbingo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.Display;
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
	private static String goldenWord = null;
	private static String currentBingoLeader = null;
	private static BingoType currentMaxBingo = BingoType.NONE;
	private static GameActivity ga = null;
	private String[] words;
	private OnCellTouchListener octl = null;

	/**
	 * An interface that needs to be implemented for anyone that wants to listen
	 * to BingoCell touch events.
	 */
	public interface OnCellTouchListener {
		/**
		 * Method that's called whenever a BingoCell generates a touch event
		 *
		 * @param c
		 *            The BingoCell that generated the event
		 */
		public void onTouch(BingoCell c);
	}

	/**
	 * Creates a BingoView using the given context. The class itself uses
	 * neither of these, but they are required by the superclass constructor.
	 *
	 * @param c
	 *            The context that created the view
	 */
	public BingoView(Context c) {
		this(c, null);
	}

	/**
	 * Creates a BingoView using the given context, attribute set. The class
	 * itself uses neither of these, but they are required by the superclass
	 * constructor.
	 *
	 * @param c
	 *            The context that created the view
	 * @param as
	 *            The attribute set for the view
	 */
	public BingoView(Context c, AttributeSet as) {
		this(c, as, 0);
	}

	/**
	 * Creates a BingoView using the given context, attribute set and definition
	 * style. The class itself uses neither of these, but they are required by
	 * the superclass constructor.
	 *
	 * @param c
	 *            The context that created the view
	 * @param as
	 *            The attribute set for the view
	 * @param defStyle
	 *            The definition style for the view.
	 */
	public BingoView(Context c, AttributeSet as, int defStyle) {
		super(c, as, defStyle);
		Resources r = getResources();
		CELL_HEIGHT = (int) r.getDimension(R.dimen.cell_heigh);
		CELL_WIDTH = (int) r.getDimension(R.dimen.cell_width);
		CELL_TEXT_SIZE = r.getDimension(R.dimen.cell_text_size);
		INSTANCE = this;
		words = new String[1];
		words[0] = "ERROR";
		board = new BingoCell[1][1];
	}

	/**
	 * Sets the list of words to be drawn as bingo cells
	 *
	 * @param words
	 */
	public void setWords(String[] words) {
		this.words = words;
	}

	/**
	 * Sets up the margins for the bingo board, so that it is centered no matter
	 * its size. Uses the devices screen size to accomplish this.
	 *
	 * @param d
	 *            The display the application is running on. Used to get the
	 *            actual screen size.
	 */
	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	public void setupMargins(Display d) {
		Point size = new Point();

		if (android.os.Build.VERSION.SDK_INT >= 13)
			d.getSize(size);
		else {
			size.x = d.getWidth();
			size.y = d.getHeight();
		}

		int boardWidth = board.length * CELL_WIDTH;
		int boardHeight = board.length * CELL_HEIGHT;
		CELL_MARGIN_LEFT = (size.x - boardWidth) / 2;
		CELL_MARGIN_TOP = (size.y - boardHeight) / 2;

		this.buildBoard();
	}

	/**
	 * Creates new BingoCells based on the words in the words field, and inserts
	 * them into the board array. Depends on setWords and setDim being called
	 * beforehand.
	 */
	private void buildBoard() {
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

	public void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		buildBoard();
		super.onLayout(changed, left, top, right, bottom);
	}

	/**
	 * Draws the bingo board by calling the draw method on each of the
	 * BingoCells
	 */
	protected void onDraw(Canvas c) {
		super.onDraw(c);
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				board[i][j].draw(c);
			}
		}
	}

	/**
	 * Creates the BingoCell array using the dimension given. The bingo board is
	 * always square.
	 *
	 * @param dim
	 *            The length and height of the bingo board.
	 */
	public void setDim(int dim) {
		board = new BingoCell[dim][dim];
	}

	/**
	 * Checks if the player has a bingo. If he has, it also finds out what type
	 * of bingo the player has. If the bingo is better than the current best
	 * bingo on the server, the server is updated with this new best bingo.
	 *
	 * @return The type of bingo the player has.
	 */
	public BingoType hasBingo() {
		int size = board.length;
		int numOfBingos = 0;
		int maxBingos = (size * 2) + 2;
		BingoType type;
		boolean hasGoldenBingo = false;

		for (int i = 0; i < board.length; i++) {
			int rowTrue = 0;
			int colTrue = 0;
			boolean goldenWordInRow = false;
			boolean goldenWordInCol = false;

			for (int j = 0; j < board[i].length; j++) {
				if (board[i][j].isSelected()) {
					rowTrue++;
					if (board[i][j].word.equals(goldenWord))
						goldenWordInRow = true;
				}
				if (board[j][i].isSelected()) {
					colTrue++;
					if (board[j][i].word.equals(goldenWord))
						goldenWordInCol = true;
				}
			}
			if (rowTrue == size) {
				numOfBingos++;
				if (goldenWordInRow)
					hasGoldenBingo = true;
			}
			if (colTrue == size) {
				numOfBingos++;
				if (goldenWordInCol)
					hasGoldenBingo = true;
			}
		}

		int diagTrue = 0;
		boolean goldenWordInDiag = false;
		for (int i = 0, j = 0; i < size && j < size; i++, j++) {
			if (board[i][j].isSelected()) {
				diagTrue++;
				if (board[i][j].word.equals(goldenWord)) {
					goldenWordInDiag = true;
				}
			}
		}
		if (diagTrue == size) {
			numOfBingos++;
			if (goldenWordInDiag)
				hasGoldenBingo = true;
		}

		diagTrue = 0;
		goldenWordInDiag = false;
		for (int i = 0, j = size - 1; i < size && j >= 0; i++, j--) {
			if (board[i][j].isSelected()) {
				diagTrue++;
				if (board[i][j].word.equals(goldenWord)) {
					goldenWordInDiag = true;
				}
			}
		}
		if (diagTrue == size) {
			numOfBingos++;
			if (goldenWordInDiag)
				hasGoldenBingo = true;
		}

		if (numOfBingos == 0)
			type = BingoType.NONE;
		else if (numOfBingos == maxBingos) {
			type = BingoType.MEGA;
			if (hasGoldenBingo)
				type = BingoType.GOLDEN_MEGA;
		} else if (numOfBingos == 1) {
			type = BingoType.SINGLE;
			if (hasGoldenBingo)
				type = BingoType.GOLDEN_SINGLE;
		} else if (numOfBingos == 2) {
			type = BingoType.DOUBLE;
			if (hasGoldenBingo)
				type = BingoType.GOLDEN_DOUBLE;
		} else {
			type = BingoType.TRIPLE;
			if (hasGoldenBingo) {
				type = BingoType.GOLDEN_TRIPLE;
			}
		}
		if (type.getValue() > currentMaxBingo.getValue())
			ga.updateBingo(type);
		return type;
	}

	/**
	 * Sets the {@link OnCellTouchListener}, which is called whenever a bingo
	 * cell is touched.
	 *
	 * @param octl
	 *            The {@link OnCellTouchListener} to use.
	 */
	public void setOnCellTouchListener(OnCellTouchListener octl) {
		this.octl = octl;
	}

	/**
	 * Checks if the point touch intersects with a BingoCell. If it does, the
	 * {@link OnCellTouchListener}'s onTouch method is called on the relevant
	 * BingoCells
	 *
	 * @param e
	 *            The {@link MotionEvent} received by the touch listener.
	 */
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

	/**
	 * Sets the golden word to use for this bingo board.
	 *
	 * @param word
	 *            The golden word for this bingo board.
	 */
	public void setGoldenWord(String word) {
		goldenWord = word;
	}

	/**
	 * Sets the GameActivity that is the parent of this BingoView. Used for some
	 * callback.
	 *
	 * @param a
	 *            The {@link GameActivity} that's the parent of the BingoView
	 */
	public void setParentActivity(GameActivity a) {
		ga = a;
	}

	/**
	 * @return The GoldenWord for this bingo board
	 */
	public static String getGoldenWord() {
		return goldenWord;
	}

	/**
	 * Checks if the current bingo leader has changed since the last check. If
	 * it has, and the new bingo leader is someone else apart from the player, a
	 * message showing the name of the opponent and what type of bingo he got is
	 * displayed.
	 *
	 * @param leader
	 *            The name of the current leader from the server
	 * @param point
	 *            The point value of the bingo of the current leader from the
	 *            server
	 */
	public static void updateLeader(String leader, int point) {
		BingoType bt = BingoType.forInt(point);
		if (bt.getValue() > currentMaxBingo.getValue()) {
			currentMaxBingo = bt;
			if (!leader.equals(GameActivity.pName)
					&& !GameActivity.pName.equals(currentBingoLeader)) {
				final String msg = leader + " fikk en " + bt.name() + " bingo!";
				ga.displayToast(msg);
			}
			currentBingoLeader = leader;
		}
	}

	/**
	 * Resets the bingo leader locally. Typically done when resuming the
	 * application from lock screen, so that they will immediately be notified
	 * of who is leading
	 */
	public void resetBingoLeader() {
		currentBingoLeader = "";
		currentMaxBingo = BingoType.NONE;
	}
}
