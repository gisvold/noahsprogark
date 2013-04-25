package no.ntnu.noahsprogark.bedpresbingo;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;

public class BingoCell {
	public String word;
	private boolean selected;
	private Rect bounds;
	private Paint p = new Paint(Paint.SUBPIXEL_TEXT_FLAG
			| Paint.ANTI_ALIAS_FLAG);

	/**
	 * Creates a BingoCell
	 *
	 * @param word
	 *            The word to draw in the bingo cell
	 * @param bounds
	 *            The size of the bingo cell
	 * @param textSize
	 *            The text size to draw the word with
	 */
	public BingoCell(String word, Rect bounds, float textSize) {
		this.word = word;
		this.bounds = bounds;
		p.setTextSize(textSize);
		p.setColor(Color.WHITE);
	}

	/**
	 * Draws the bingo cell on the provided canvas by using a helper class to
	 * wrap the text properly given the text and text size. Which paint to use
	 * is decided by whether or not it is a golden word and whether or not the
	 * cell is selected.
	 *
	 * @param c
	 *            The canvas to draw the bingo cell on.
	 */
	protected void draw(Canvas c) {
		Paint frame = new Paint();
		if (selected) {
			frame.setColor(Color.GREEN);
			p.setColor(Color.BLACK);
			if (word.equals(BingoView.getGoldenWord())) {
				p.setFakeBoldText(true);
				p.setColor(Color.MAGENTA);
			}

		} else {
			if (word.equals(BingoView.getGoldenWord())) {
				frame.setColor(Color.YELLOW);
				p.setColor(Color.BLACK);

			} else {
				frame.setColor(Color.WHITE);
				frame.setStyle(Style.STROKE);
				p.setColor(Color.WHITE);
			}
		}
		c.drawRect(bounds, frame);
		TextRect tr = new TextRect(p);
		// Magic numbers on next two calls are so that the text doesn't end up
		// touching the border
		tr.prepare(word, bounds.width() - 8, bounds.height() - 8);
		tr.draw(c, bounds.left + 4, bounds.top + 4);
	}

	/**
	 * @return whether or not the cell is selected.
	 */
	public boolean isSelected() {
		return selected;
	}

	/**
	 * Toggles the selection of this cell.
	 */
	public void toggleSelected() {
		selected = !selected;
	}

	/**
	 * @return The word represented by this bingo cell.
	 */
	public String getWord() {
		return word;
	}

	/**
	 * @param x
	 *            An x-coordinate in pixels
	 * @param y
	 *            An y-coordinate in pixels
	 * @return Whether or not the given point is inside the bounding box of this
	 *         bingo cell.
	 */
	public boolean doesHit(int x, int y) {
		return bounds.contains(x, y);
	}
}
