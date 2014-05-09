
package com.lnl.finance.widget;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;


public class Cell {
	public static final int CURRENT_MOUNT = 0;
    public static final int NEXT_MOUNT = 1;
    public static final int PREVIOUS_MOUNT = -1;
    
	protected Rect mBound = null;
	protected int year;
	protected int month;
	protected int mDayOfMonth = 1;	// from 1 to 31
	protected Paint mPaint = new Paint(Paint.SUBPIXEL_TEXT_FLAG |Paint.ANTI_ALIAS_FLAG);
	public int whichMonth;  // -1 为上月  1为下月  0为此月
	public boolean txtWhite = false;
	int dx, dy;
	public Cell(int year, int month, int dayOfMon, Rect rect, float textSize, boolean bold) {
		this.year = year;
		this.month = month;
		mDayOfMonth = dayOfMon;
		mBound = rect;
		mPaint.setTextSize(textSize);
		
		if(bold) mPaint.setFakeBoldText(true);
		
		dx = (int) mPaint.measureText(String.valueOf(mDayOfMonth)) / 2;
		dy = (int) (-mPaint.ascent() + mPaint.descent()) / 2;
	}
	
	public Cell(int year, int month, int dayOfMon, Rect rect, float textSize) {
		this(year, month, dayOfMon, rect, textSize, true);
	}
	
	protected void draw(Canvas canvas) {
		
		if(whichMonth==PREVIOUS_MOUNT||whichMonth==NEXT_MOUNT){
			Paint paint = new Paint();
			paint.setColor(Color.rgb(240, 240, 240));
			paint.setAlpha(255);
			canvas.drawRect(mBound.left, mBound.top, mBound.right, mBound.bottom, paint);
			
			canvas.drawText(String.valueOf(mDayOfMonth), mBound.centerX() - dx, mBound.centerY() + dy-3, mPaint);
		}else{
			
			if(txtWhite){
				mPaint.setColor(Color.rgb(255, 255, 255));
			}else{
				mPaint.setColor(Color.rgb(51, 181, 229));
			}
			
			canvas.drawText(String.valueOf(mDayOfMonth), mBound.centerX() - dx, mBound.centerY() + dy-3, mPaint);
		}
	}
	
	public int getYear() {
		return year;
	}

	public int getMonth() {
		return month;
	}

	public int getDayOfMonth() {
		return mDayOfMonth;
	}
	
	public boolean hitTest(int x, int y) {
		return mBound.contains(x, y); 
	}
	
	public Rect getBound() {
		return mBound;
	}
	
	@Override
	public String toString() {
		return String.valueOf(mDayOfMonth)+"("+mBound.toString()+")";
	}

}

