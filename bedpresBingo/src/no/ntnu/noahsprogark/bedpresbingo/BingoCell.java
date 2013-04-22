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

	public BingoCell(String word, Rect bounds, float textSize) {
		this.word = word;
		this.bounds = bounds;
		p.setTextSize(textSize);
		p.setColor(Color.WHITE);
	}

	protected void draw(Canvas c) {
		Paint frame = new Paint();
		if (selected) {
			frame.setColor(Color.GREEN);
			p.setColor(Color.BLACK);
			if (word.equals(BingoView.goldenWord)) {
				p.setFakeBoldText(true);
				p.setColor(Color.MAGENTA);
			}

		} else {
			if (word.equals(BingoView.goldenWord)) {
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
		tr.prepare(word, bounds.width() - 8, bounds.height() - 8);
		tr.draw(c, bounds.left + 4, bounds.top + 4);
	}

	public boolean isSelected() {
		return selected;
	}

	public void toggleSelected() {
		selected = !selected;
	}

	public String getWord() {
		return word;
	}

	public boolean doesHit(int x, int y) {
		return bounds.contains(x, y);
	}
}
