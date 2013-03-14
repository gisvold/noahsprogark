/*
 * Copyright (C) 2011 Chris Gao <chris@exina.net>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package no.blopp.app.views;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.MonthDisplayHelper;
import android.view.MotionEvent;
import android.widget.ImageView;
import no.blopp.app.conf.R;
import no.blopp.app.models.*;
import no.blopp.app.xmlfeed.PollenCast;

public class CalendarView extends ImageView
{
	private static int WEEK_TOP_MARGIN = 0;
	private static int WEEK_LEFT_MARGIN = 0;
	private static int CELL_WIDTH = 58;
	private static int CELL_HEIGH = 53;
	private static int CELL_MARGIN_TOP = 0;
	private static int CELL_MARGIN_LEFT = 0;
	private static float CELL_TEXT_SIZE;
	private static final int CHILD_ID = 6; //Replace this with the actual id of the current child
	private static final String TAG = "CalendarView";
	private Calendar mRightNow = null;
	private Drawable mWeekTitle = null;
	private Cell mToday = null;
	private Cell[][] mCells = new Cell[6][7];
	private OnCellTouchListener mOnCellTouchListener = null;

	MonthDisplayHelper monthDisplayHelper;
	Drawable mDecoration = null;
	private Context context;
	private LogModel logModel = new LogModel(CHILD_ID); 
	private PollenCast pollenFeed;

	public interface OnCellTouchListener
	{
		public void onTouch(Cell cell);
	}

	public CalendarView(Context context)
	{

		this(context, null);
		this.context = context;
	}

	public CalendarView(Context context, AttributeSet attrs)
	{
		this(context, attrs, 0);
	}

	public CalendarView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		mDecoration = context.getResources().getDrawable(
				R.drawable.typeb_calendar_today);
		initCalendarView();
	}

	private void initCalendarView()
	{

		mRightNow = Calendar.getInstance();
		// prepare static vars
		Resources res = getResources();
		WEEK_TOP_MARGIN = (int) res.getDimension(R.dimen.week_top_margin);
		WEEK_LEFT_MARGIN = (int) res.getDimension(R.dimen.week_left_margin);

		CELL_WIDTH = (int) res.getDimension(R.dimen.cell_width);
		CELL_HEIGH = (int) res.getDimension(R.dimen.cell_heigh);
		CELL_MARGIN_TOP = (int) res.getDimension(R.dimen.cell_margin_top);
		CELL_MARGIN_LEFT = (int) res.getDimension(R.dimen.cell_margin_left);

		CELL_TEXT_SIZE = res.getDimension(R.dimen.cell_text_size);
		// set background
		setImageResource(R.drawable.background);
		mWeekTitle = res.getDrawable(R.drawable.calendar_week);

		monthDisplayHelper = new MonthDisplayHelper(mRightNow.get(Calendar.YEAR),
				mRightNow.get(Calendar.MONTH));

	}

	private void initCells()
	{
		class _calendar
		{
			public int day;
			public boolean thisMonth;

			public _calendar(int d, boolean b)
			{
				day = d;
				thisMonth = b;
			}

			public _calendar(int d)
			{
				this(d, false);
			}
		};

		_calendar calendarArray[][] = new _calendar[6][7];

		for (int i = 0; i < calendarArray.length; i++)
		{
			int n[] = monthDisplayHelper.getDigitsForRow(i);
			for (int d = 0; d < n.length; d++)
			{
				if (monthDisplayHelper.isWithinCurrentMonth(i, d))
					calendarArray[i][d] = new _calendar(n[d], true);
				else
					calendarArray[i][d] = new _calendar(n[d]);

			}
		}
		

		Calendar today = Calendar.getInstance();
		int thisDay = 0;
		mToday = null;

		if (monthDisplayHelper.getYear() == today.get(Calendar.YEAR)
				&& monthDisplayHelper.getMonth() == today.get(Calendar.MONTH))
		{
			thisDay = today.get(Calendar.DAY_OF_MONTH);
		}

		// build cells

		Rect Bound = new Rect(CELL_MARGIN_LEFT, CELL_MARGIN_TOP, CELL_WIDTH
				+ CELL_MARGIN_LEFT, CELL_HEIGH + CELL_MARGIN_TOP);
		//Get the worst spread of the day, and make this the bottom color of a cell.
		int worstSpread = getWorstPollenFeed();
		
		//Initialize the logmodel with children id and date.
		logModel = new LogModel(CHILD_ID, monthDisplayHelper.getMonth()+1, monthDisplayHelper.getYear());

		for (int week = 0; week < mCells.length; week++)
		{
			for (int day = 0; day < mCells[week].length; day++)
			{
				if (calendarArray[week][day].thisMonth)
				{
					
					//Get current date of month
					int dayOfMonth = calendarArray[week][day].day;
					//Get the date as a string
					String date = getDate(dayOfMonth, monthDisplayHelper);
					//Get the healthzone of the given date. 
					HealthZone hz = logModel.getHealthZoneAtDay(date);
					
					if (hz.equals(HealthZone.GREEN_ZONE))
					{
						mCells[week][day] = new GreenCell(
								calendarArray[week][day].day, new Rect(Bound),
								CELL_TEXT_SIZE, worstSpread, 1);
					} else if (hz.equals(HealthZone.YELLOW_ZONE))
					{
						mCells[week][day] = new YellowCell(
								calendarArray[week][day].day, new Rect(Bound),
								CELL_TEXT_SIZE, worstSpread, 2);
					} else if (hz.equals(HealthZone.RED_ZONE))
					{
						mCells[week][day] = new RedCell(
								calendarArray[week][day].day, new Rect(Bound),
								CELL_TEXT_SIZE, worstSpread, 3);
					}
				}
				// Seperate betweeen current month and days of other months.
				else
				{
					mCells[week][day] = new BlackCell(
							calendarArray[week][day].day, new Rect(Bound),
							CELL_TEXT_SIZE, 1);

				}
				Bound.offset(CELL_WIDTH, 0); // move to next column

				// get today
				if (calendarArray[week][day].day == thisDay
						&& calendarArray[week][day].thisMonth)
				{
					mToday = mCells[week][day];
					mDecoration.setBounds(mToday.getBound());
				}
			}
			Bound.offset(0, CELL_HEIGH); // move to next row and first column
			Bound.left = CELL_MARGIN_LEFT;
			Bound.right = CELL_MARGIN_LEFT + CELL_WIDTH;
		}
	}

	@Override
	public void onLayout(boolean changed, int left, int top, int right,
			int bottom)
	{
		Rect re = getDrawable().getBounds();
		WEEK_LEFT_MARGIN = CELL_MARGIN_LEFT = (right - left - re.width()) / 2;
		
		mWeekTitle.setBounds(0, 0, 0, 0);
		initCells();
		super.onLayout(changed, left, top, right, bottom);
	}

	public void setTimeInMillis(long milliseconds)
	{
		mRightNow.setTimeInMillis(milliseconds);
		initCells();
		this.invalidate();
	}

	public int getYear()
	{
		return monthDisplayHelper.getYear();
	}

	public int getMonth()
	{
		return monthDisplayHelper.getMonth();
	}

	public void nextMonth()
	{
		monthDisplayHelper.nextMonth();
		initCells();
		invalidate();
	}

	public void previousMonth()
	{
		monthDisplayHelper.previousMonth();
		initCells();
		invalidate();
	}

	public boolean firstDay(int day)
	{
		return day == 1;
	}

	public boolean lastDay(int day)
	{
		return monthDisplayHelper.getNumberOfDaysInMonth() == day;
	}

	public void goToday()
	{
		Calendar cal = Calendar.getInstance();
		monthDisplayHelper = new MonthDisplayHelper(cal.get(Calendar.YEAR),
				cal.get(Calendar.MONTH));
		initCells();
		invalidate();
	}

	public Calendar getDate()
	{
		return mRightNow;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		if (mOnCellTouchListener != null)
		{
			for (Cell[] week : mCells)
			{
				for (Cell day : week)
				{
					if (day.hitTest((int) event.getX(), (int) event.getY()))
					{
						mOnCellTouchListener.onTouch(day);
					}
				}
			}
		}
		return super.onTouchEvent(event);
	}

	public void setOnCellTouchListener(OnCellTouchListener p)
	{
		mOnCellTouchListener = p;
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		// draw background
		super.onDraw(canvas);
		mWeekTitle.draw(canvas);

		// draw cells
		for (Cell[] week : mCells)
		{
			for (Cell day : week)
			{
				day.draw(canvas);
			}
		}

		// draw today
		if (mDecoration != null && mToday!= null)
		{
			mDecoration.draw(canvas);
		}
	}

	// Green zone
	public class GreenCell extends Cell
	{
		public GreenCell(int dayOfMon, Rect rect, float s, int worstSpread,
				int healthState)
		{
			super(dayOfMon, rect, s, worstSpread, healthState);
			painter.setColor(Color.GREEN);
		}
	}

	// Red zone
	private class RedCell extends Cell
	{
		public RedCell(int dayOfMon, Rect rect, float s, int worstSpread,
				int healthState)
		{
			super(dayOfMon, rect, s, worstSpread, healthState);
			painter.setColor(Color.RED);
		}
	}

	// Yellow zone
	private class YellowCell extends Cell
	{
		public YellowCell(int dayOfMon, Rect rect, float s, int worstSpread,
				int healthState)
		{
			super(dayOfMon, rect, s, worstSpread, healthState);
			painter.setColor(Color.rgb(255, 165, 0));
		}

	}

	// Used for coloring days after today.
	private class BlackCell extends Cell
	{
		public BlackCell(int dayOfMon, Rect rect, float s, int healthState)
		{
			super(dayOfMon, rect, s, -1, healthState);
			painter.setColor(Color.BLACK);
		}

	}

//	public String getMonthOfYear()
//	{
//		int mMonth = monthDisplayHelper.getMonth();
//		int year = monthDisplayHelper.getYear();
//		return DateAdapter.getMonthText(mMonth) + year;
//
//	}
	public MonthDisplayHelper getCalendarHelper()
	{
		return monthDisplayHelper;
	}

	/**
	 * calles pollencast.get to retrieve xml-feed.
	 * 
	 * @return Worst distribution of pollen at a given day
	 */
	private int getWorstPollenFeed()
	{

		pollenFeed = new PollenCast();
		pollenFeed.execute();
		try
		{
			pollenFeed.get();
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		} catch (ExecutionException e)
		{
			e.printStackTrace();
		}
		return getWorst(pollenFeed.getPollenStateAtDayModel()
				.getPollenStatesAtDay());
	}

	/**
	 * Traverses the ArrayList to find the maximum spread. Max must be between
	 * 0-4.
	 * @param states, as given above
	 * @return worst distribution
	 */
	private int getWorst(ArrayList<PollenState> states)
	{
		int max = -1;
		for (PollenState ps : states)
		{
			if (ps.getDistribution() > max)
			{
				max = ps.getDistribution();
			}
		}
		return max;
	}
	private String getDate(int dayOfMonth, MonthDisplayHelper helper)
	{
		String date = "" + (monthDisplayHelper.getYear()) + "-" + (monthDisplayHelper.getMonth() + 1) + "-" + (dayOfMonth<=9 ? "0"+dayOfMonth : dayOfMonth);
		return date;
	}
}
