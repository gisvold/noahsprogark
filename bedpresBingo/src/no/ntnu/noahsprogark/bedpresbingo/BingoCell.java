package no.ntnu.noahsprogark.bedpresbingo;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class BingoCell {
	private String word;
	private boolean selected;
	private Rect bounds;
	private Paint p = new Paint(Paint.SUBPIXEL_TEXT_FLAG
			| Paint.ANTI_ALIAS_FLAG);
	private int dx, dy;

	public BingoCell(String word, Rect bounds, float textSize) {
		this.word = word;
		this.bounds = bounds;
		p.setTextSize(textSize);
		p.setColor(Color.BLACK);
		
		dx = (int) p.measureText(word) / 2;
		dy = (int) (-p.ascent() + p.descent()) / 2;
	}
	
	protected void draw(Canvas c) {
		c.drawText(word, bounds.centerX() - dx, bounds.centerY() + dy, p);
	}

	public boolean isSelected() {
		return selected;
	}

	public String getWord() {
		return word;
	}
}
