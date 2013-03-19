package no.ntnu.noahsprogark.bedpresbingo;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class TextRect {
	private static final int MAX_LINES = 3;
	private Paint p;
	private int[] starts = new int[MAX_LINES];
	private int[] stops = new int[MAX_LINES];
	private int lines = 0;
	private int textHeight = 0;
	private Rect bounds = new Rect();
	private String text;
	private boolean wasCut;

	public TextRect(Paint p) {
		this.p = p;
	}

	public int prepare(String t, int maxW, int maxH) {
		lines = 0;
		textHeight = 0;
		this.text = t;

		p.getTextBounds("i", 0, 1, bounds);

		int maxInLine = maxW / bounds.width();
		int l = (text == null) ? 0 : text.length();

		if (l > 0) {
			int lineH = -p.getFontMetricsInt().ascent
					+ p.getFontMetricsInt().descent;
			int start = 0;
			int stop = (maxInLine > l) ? l : maxInLine;

			while (true) {
				for (; start < l; start++) {
					char c = text.charAt(start);
					if (c != '\n' && c != '\r' && c != '\t' && c != ' ')
						break;
				}

				for (int i = stop + 1; stop < i && stop > start;) {
					i = stop;
					int lowest = text.indexOf("\n", start);
					p.getTextBounds(text, start, stop, bounds);

					if ((lowest >= start && lowest < stop)
							|| bounds.width() > maxW) {
						stop--;

						if (lowest < start || lowest > stop) {
							int blank = text.lastIndexOf(" ", stop);
							int hyphen = text.lastIndexOf("-", stop);

							if (blank > start
									&& (hyphen < start || blank > hyphen))
								lowest = blank;
							else if (hyphen > start)
								lowest = hyphen;
						}

						if (lowest >= start && lowest <= stop) {
							final char ch = text.charAt(stop);

							if (ch != '\n' && ch != ' ')
								++lowest;

							stop = lowest;
						}

						continue;
					}
					break;
				}
				if (start >= stop)
					break;

				int minus = 0;

				if (stop < l) {
					final char ch = text.charAt(stop - 1);

					if (ch == '\n' || ch == ' ')
						minus = 1;
				}

				if (textHeight + lineH > maxH) {
					wasCut = true;
					break;
				}

				starts[lines] = start;
				stops[lines] = stop - minus;

				if (++lines > MAX_LINES) {
					wasCut = true;
					break;
				}

				if (textHeight > 0)
					textHeight += p.getFontMetricsInt().leading;

				textHeight += lineH;

				if (stop >= l)
					break;

				start = stop;
				stop = l;

			}
		}
		return textHeight;
	}

	public void draw(Canvas canvas, int left, int top) {
		if (textHeight == 0)
			return;

		final int before = -p.getFontMetricsInt().ascent;
		final int after = p.getFontMetricsInt().descent
				+ p.getFontMetricsInt().leading;
		int y = top;

		lines--;
		for (int n = 0; n <= lines; ++n) {
			String t;

			y += before;

			if (wasCut && n == lines && stops[n] - starts[n] > 3)
				t = text.substring(starts[n], stops[n] - 3).concat("...");
			else
				t = text.substring(starts[n], stops[n]);

			canvas.drawText(t, left, y, p);

			y += after;
		}
	}

	public boolean wasCut() {
		return wasCut;
	}
}
