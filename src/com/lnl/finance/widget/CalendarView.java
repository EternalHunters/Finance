package com.lnl.finance.widget;

import java.util.ArrayList;
import java.util.Calendar;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.MonthDisplayHelper;
import android.view.View;

import com.lnl.finance.R;
/**
 * 
 * @author yuanzhi.cjy
 *
 */
public class CalendarView extends View{
	public static final int DEFAULT_BOARD_SIZE = 100;
	private static float CELL_TEXT_SIZE;
	
	private int mCellWidth;
	private int mCellHeight;
	
	public static final int CURRENT_MOUNT = 0;
    public static final int NEXT_MOUNT = 1;
    public static final int PREVIOUS_MOUNT = -1;
	private static final String[] weekTitle = {"日","一","二","三","四","五","六"};
	
	private ArrayList<String> markedArr = null;
	
	private Calendar mRightNow = null;
	private Cell mToday = null;
	private Cell mTouchedCell = null;
	private Cell[][] mCells = new Cell[6][7];
	
	private OnMonthChangeListener monthChangeListener;
	MonthDisplayHelper mHelper;
	
	private Paint mBackgroundColor;
	private Paint mBackgroundColorToday;
	private Paint mBackgroundColorTouched;
	private Paint mBackgroundColorMarked;
	private Paint mWeekTitle;
	private Paint mLinePaint;
	private Paint mLinePaint2;
	private Context context;
	
	//发工资日
	private String payOffDay;
	private Bitmap badgeBitmap;
	
	public CalendarView(Context context) {
		this(context, null);
		this.context = context;
	}
	
	public CalendarView(Context context, AttributeSet attrs){
		super(context, attrs);
		this.context = context;
		initCalendarView();
	}
	
	private void initCalendarView() {
		mRightNow = Calendar.getInstance();
		mHelper = new MonthDisplayHelper(
					mRightNow.get(Calendar.YEAR),
					mRightNow.get(Calendar.MONTH),
					mRightNow.getFirstDayOfWeek()
				);
		
		mBackgroundColor = new Paint();
		mBackgroundColorToday = new Paint();
		mBackgroundColorTouched = new Paint();
		mBackgroundColorMarked = new Paint();
		mWeekTitle = new Paint(Paint.SUBPIXEL_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
		mLinePaint = new Paint();
		mLinePaint2 = new Paint();
		
		mBackgroundColor.setColor(Color.rgb(255, 255, 255));
		mBackgroundColorToday.setColor(Color.rgb(51, 181, 229));
		mBackgroundColorToday.setAlpha(255);
		mBackgroundColorTouched.setColor(Color.rgb(255, 187, 51));
		mBackgroundColorTouched.setAlpha(255);
		mBackgroundColorMarked.setColor(Color.rgb(255, 68, 68));
		mBackgroundColorMarked.setAlpha(255);
		mWeekTitle.setColor(Color.rgb(255, 255, 255));
		mLinePaint.setColor(Color.rgb(153, 204, 0));
		mLinePaint2.setColor(Color.rgb(153, 204, 0));
		badgeBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.calendar_payoff_tag);
		
	}
	
	private void initCells() { 
		class _calendar {
			public int year;
			public int month;
	    	public int day;
	    	public int whichMonth;  // -1 为上月  1为下月  0为此月
	    	public _calendar(int y, int m, int d, int b) {
	    		year = y;
	    		month = m;
	    		day = d;
	    		whichMonth = b;
	    	}
	    	public _calendar(int y, int m, int d) { // 上个月 默认为
	    		this(y, m, d, PREVIOUS_MOUNT);
	    	}
	    };
	    _calendar tmp[][] = new _calendar[6][7];
	    
	    for(int i=0; i<tmp.length; i++) {
	    	int n[] = mHelper.getDigitsForRow(i);
	    	for(int d=0; d<n.length; d++) {
	    		if(mHelper.isWithinCurrentMonth(i,d))
	    			tmp[i][d] = new _calendar(mHelper.getYear(), mHelper.getMonth()+1, n[d], CURRENT_MOUNT);
	    		else if(i == 0) {
	    			tmp[i][d] = new _calendar(mHelper.getYear(), mHelper.getMonth(), n[d]);
	    		} else {
	    			tmp[i][d] = new _calendar(mHelper.getYear(), mHelper.getMonth()+2, n[d], NEXT_MOUNT);
	    		}
	    		
	    	}
	    }
	    
	    Calendar today = Calendar.getInstance();
	    int thisDay = 0;
	    mToday = null;
	    if(mHelper.getYear()==today.get(Calendar.YEAR) && mHelper.getMonth()==today.get(Calendar.MONTH)) {
	    	thisDay = today.get(Calendar.DAY_OF_MONTH);
	    }
	    // build cells
		Rect Bound = new Rect(getPaddingLeft(), mCellHeight+getPaddingTop(), mCellWidth+getPaddingLeft(), 2*mCellHeight+getPaddingTop());
		for(int week=0; week<mCells.length; week++) {
			for(int day=0; day<mCells[week].length; day++) {
				if(tmp[week][day].whichMonth == CURRENT_MOUNT) { // 此月  开始设置cell
					if(day==0 || day==6 ){//周末
						mCells[week][day] = new Cell(tmp[week][day].year, tmp[week][day].month, tmp[week][day].day, new Rect(Bound), CELL_TEXT_SIZE);
						mCells[week][day].whichMonth = CURRENT_MOUNT;
					}else{ 
						mCells[week][day] = new Cell(tmp[week][day].year, tmp[week][day].month, tmp[week][day].day, new Rect(Bound), CELL_TEXT_SIZE);
						mCells[week][day].whichMonth = CURRENT_MOUNT;
					}
				} else if(tmp[week][day].whichMonth == PREVIOUS_MOUNT) {  // 上月为gray
					mCells[week][day] = new GrayCell(tmp[week][day].year, tmp[week][day].month, tmp[week][day].day, new Rect(Bound), CELL_TEXT_SIZE);
					mCells[week][day].whichMonth = PREVIOUS_MOUNT;
				} else { // 下月为LTGray
					mCells[week][day] = new LTGrayCell(tmp[week][day].year, tmp[week][day].month, tmp[week][day].day, new Rect(Bound), CELL_TEXT_SIZE);
					mCells[week][day].whichMonth = NEXT_MOUNT;
				}
				
				Bound.offset(mCellWidth, 0); // move to next column 
				
				// get today
				if(tmp[week][day].day==thisDay && tmp[week][day].whichMonth == 0) {
					mToday = mCells[week][day];
				}
			}
			Bound.offset(0, mCellHeight); // move to next row and first column
			Bound.left = getPaddingLeft();
			Bound.right = getPaddingLeft()+mCellWidth;
			
		}
	}

	public int getYear() {
		return mHelper.getYear();
	}
	    
	public int getMonth() {
		return mHelper.getMonth()+1;
	}
	
	public void nextMonth() {
		mHelper.nextMonth();
		initCells();
		invalidate();
		if(monthChangeListener!=null)
			monthChangeListener.onMonthChanged();
	}
	    
	public void previousMonth() {
		mHelper.previousMonth();
		initCells();
		invalidate();
		if(monthChangeListener!=null)
			monthChangeListener.onMonthChanged();
	}
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int width = -1, height = -1;
        if (widthMode == MeasureSpec.EXACTLY) {
        	width = widthSize;
        } else {
        	width = DEFAULT_BOARD_SIZE;
        	if (widthMode == MeasureSpec.AT_MOST && width > widthSize ) {
        		width = widthSize;
        	}
        }
        if (heightMode == MeasureSpec.EXACTLY) {
        	height = heightSize;
        } else {
        	height = DEFAULT_BOARD_SIZE;
        	if (heightMode == MeasureSpec.AT_MOST && height > heightSize ) {
        		height = heightSize;
        	}
        }
        
        if (widthMode != MeasureSpec.EXACTLY) {
        	width = height;
        }
        
        if (heightMode != MeasureSpec.EXACTLY) {
        	height = width;
        }
        
    	if (widthMode == MeasureSpec.AT_MOST && width > widthSize ) {
    		width = widthSize;
    	}
    	if (heightMode == MeasureSpec.AT_MOST && height > heightSize ) {
    		height = heightSize;
    	}
    	
    	mCellWidth = (width - getPaddingLeft() - getPaddingRight()) / 7;
        mCellHeight = (height - getPaddingTop() - getPaddingBottom()) / 7;
        setMeasuredDimension(width, height);
        CELL_TEXT_SIZE = mCellHeight * 0.3f;
        mWeekTitle.setTextSize(mCellHeight * 0.4f);
        initCells();
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		try {
			String payoffDayTmp = payOffDay;
			//0 的发工资日是月末
			if("0".equals(payOffDay)){
				
				Calendar calendar = Calendar.getInstance(); 
				calendar.set(Calendar.DATE, 1);
				calendar.set(Calendar.YEAR, mHelper.getYear());   
				calendar.set(Calendar.MONTH, mHelper.getMonth());   
				int endday = calendar.getActualMaximum(Calendar.DATE);  
				
				payoffDayTmp = endday+"";
				System.out.println("year:"+mHelper.getYear()+" month:"+mHelper.getMonth());
				System.out.println("payoffDayTmp:"+payoffDayTmp);
			}
			
			//draw backrgound
			canvas.drawRect(getPaddingLeft(), getPaddingTop(), 7*mCellWidth+getPaddingLeft(), 7*mCellHeight+getPaddingTop(), mBackgroundColor);
			
			Rect tempBound = new Rect(getPaddingLeft(), getPaddingTop(), getPaddingLeft()+mCellWidth, getPaddingTop()+mCellHeight);
			for(String str:weekTitle){
				int dx,dy;
				dx = (int) (mWeekTitle.measureText(str)/2);
				dy = (int) ((-mWeekTitle.ascent() + mWeekTitle.descent()) / 2);
				
				Paint weekTitleBG = new Paint();
				weekTitleBG.setColor(Color.rgb(153, 204, 0));
				weekTitleBG.setAlpha(255);
				canvas.drawRect(tempBound.left, tempBound.top, tempBound.right, tempBound.bottom, weekTitleBG);
				canvas.drawText(str, tempBound.centerX()-dx, tempBound.centerY()+dy, mWeekTitle);
				
				tempBound.offset(mCellWidth, 0);
			}
			
			//draw touched
			if(mTouchedCell!=null){
				mTouchedCell.txtWhite = true;
				Rect bound = mTouchedCell.getBound();
				canvas.drawRect(bound.left, bound.top, bound.right, bound.bottom, mBackgroundColorTouched);
			}
			
			// draw today
			if(mToday!=null){
				mToday.txtWhite = true;
				Rect bound = mToday.getBound();
				canvas.drawRect(bound.left, bound.top, bound.right, bound.bottom, mBackgroundColorToday);
			}
			
			// draw cells
			for(Cell[] week : mCells) {
				for(Cell day : week) {
					if(!day.equals(mToday) && !day.equals(mTouchedCell)){
						day.txtWhite = false;
					}
					//画标记的背景
					if(markedArr!=null&&day.whichMonth==Cell.CURRENT_MOUNT){
						for (String markedday : markedArr) {
							if((day.mDayOfMonth+"").equals(markedday) && !day.equals(mTouchedCell) && !day.equals(mToday)){
								day.txtWhite = true;
								Rect bound = day.getBound();
								canvas.drawRect(bound.left, bound.top, bound.right, bound.bottom, mBackgroundColorMarked);
								break;
							}
						}
					}
					
					if((day.mDayOfMonth+"").equals(payoffDayTmp)){
						int width = badgeBitmap.getWidth();
						int height = badgeBitmap.getHeight();
						Rect bound = day.getBound();
						canvas.drawBitmap(badgeBitmap, bound.right-width, bound.bottom-height, null);
					}
					
					day.draw(canvas);
				}
			}
			
			//draw vertical lines
			for (int c=0; c <= 7; c++) {
				float x = c * mCellWidth + getPaddingLeft();
				canvas.drawLine(x-0.5f, getPaddingTop(), x-0.5f, 7*mCellHeight+getPaddingTop(), mLinePaint2);
				canvas.drawLine(x+0.5f, getPaddingTop(), x+0.5f, 7*mCellHeight+getPaddingTop(), mLinePaint);
				
			}
			// draw horizontal lines
			for (int r=0; r <= 7; r++) {
				float y = r * mCellHeight + getPaddingTop();
				canvas.drawLine(getPaddingLeft(), y-0.5f, 7*mCellWidth+getPaddingLeft(), y-0.5f, mLinePaint);
				canvas.drawLine(getPaddingLeft(), y+0.5f, 7*mCellWidth+getPaddingLeft(), y+0.5f, mLinePaint2);
			}
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Cell getCellAtPoint(int x, int y){
		int lx = x - getPaddingLeft();
		int ly = y - getPaddingTop();
		
		int row = (ly / mCellHeight);
		int col = (lx / mCellWidth);
		
		if(col>=0 && col<7 && row>=1 && row<7){
			mTouchedCell = mCells[row-1][col];
		}else {
			mTouchedCell = null;
		}
		return mTouchedCell;
	}
	
	public void clearTouchedCell(){
		mTouchedCell = null;
	}
	
	private class GrayCell extends Cell {
		public GrayCell(int year, int month, int dayOfMon, Rect rect, float s) {
			super(year, month, dayOfMon, rect, s);
			mPaint.setColor(Color.rgb(220, 220, 220));
		}
	}
	
	
	private class LTGrayCell extends Cell {
		public LTGrayCell(int year, int month, int dayOfMon, Rect rect, float s) {
			super(year, month, dayOfMon, rect, s);
			mPaint.setColor(Color.rgb(220, 220, 220));
		}
	}
	
	public Cell getmTouchedCell() {
		return mTouchedCell;
	}
	
	public void setmTouchedCell(Cell mTouchedCell) {
		this.mTouchedCell = mTouchedCell;
	}
	
	
	public void setMarkedArr(ArrayList<String> strings){
		this.markedArr = strings;
		postInvalidate();
	}
	
	public void setPayoffDay(String day){
		this.payOffDay = day;
		postInvalidate();
	}

	public void setMonthChangeListener(OnMonthChangeListener monthChangeListener) {
		this.monthChangeListener = monthChangeListener;
	}

	public interface OnMonthChangeListener{
		public void onMonthChanged();
	}
	
	
}
