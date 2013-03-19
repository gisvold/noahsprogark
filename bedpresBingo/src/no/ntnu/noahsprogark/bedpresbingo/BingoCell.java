package no.ntnu.noahsprogark.bedpresbingo;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;

public class BingoCell {
	private String word;
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
		frame.setColor(Color.GREEN);
		frame.setStyle(Style.STROKE);
		c.drawRect(bounds, frame);
		TextRect tr = new TextRect(p);
		tr.prepare(word, bounds.width(), bounds.height());
		tr.draw(c, bounds.left + 8, bounds.top + 8);
	}

	public boolean isSelected() {
		return selected;
	}

	public String getWord() {
		return word;
	}
}
