package com.lnl.finance.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;

public class CalendarLayout extends FrameLayout {

	private static final String TAG = "CalendarLayout";
	private CalendarView mainView;
	private CalendarView anotherView;
	private OnDayClickListener dayClickListener;
	private boolean hasmoved;
	private boolean isAnimationStarted = false;
	private static final int SWIPE_MIN_DISTANCE = 60;
	private static final int SWIPE_THRESHOLD_VELOCITY = 180; 
	
	public CalendarLayout(Context context) {
		this(context, null);
	}

	public CalendarLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		mainView = new CalendarView(context, null);
		mainView.setLayoutParams(new LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT,
				android.view.ViewGroup.LayoutParams.FILL_PARENT));

		anotherView = new CalendarView(context, null);
		anotherView.setLayoutParams(new LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT,
				android.view.ViewGroup.LayoutParams.FILL_PARENT));
		
		this.addView(anotherView);
		this.addView(mainView);
		
	}

	public void setDayClickListener(OnDayClickListener dayClickListener) {
		this.dayClickListener = dayClickListener;
	}

	public CalendarView getMainView() {
		return mainView;
	}

	public CalendarView getAnotherView() {
		return anotherView;
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.v("TOUCH SCREEN", "test");
        return super.dispatchTouchEvent(ev);

    } 

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int x = (int)event.getX();
		int y = (int)event.getY();
		System.out.println("onTouchEvent x:"+x+"   y:"+y);
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			System.out.println("touchdown");
			hasmoved = false;
			mainView.getCellAtPoint(x, y);
			break;
		case MotionEvent.ACTION_MOVE:
			hasmoved = true;
			mainView.getCellAtPoint(x, y);
			break;
			
		case MotionEvent.ACTION_UP:
			Cell touchedCell = mainView.getCellAtPoint(x, y);
			if(touchedCell!=null){
				if(touchedCell.whichMonth==Cell.PREVIOUS_MOUNT){
					mainView.clearTouchedCell();
					swipeToPresentMonth();
				}else if(touchedCell.whichMonth==Cell.NEXT_MOUNT){
					mainView.clearTouchedCell();
					swipeToNextMonth();
				}else{
					if(dayClickListener!=null){
						dayClickListener.onDayClick(mainView.getmTouchedCell());
					}
				}
			}
			
			break;
			
		case MotionEvent.ACTION_CANCEL:
			mainView.setmTouchedCell(null);
			break;
			
		default:
			break;
		}
		mainView.postInvalidate();
		return true;
	}
	
	public void swipeToPresentMonth(){
		if(isAnimationStarted)
			return;
		anotherView.previousMonth();
		Animation hideAnimation = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, 1.0f);
		hideAnimation.setDuration(500);
		mainView.startAnimation(hideAnimation);
		
		Animation showAnimation = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, -1.0f,
				Animation.RELATIVE_TO_SELF, 0.0f);
		showAnimation.setDuration(500);
		anotherView.startAnimation(showAnimation);
		showAnimation.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				isAnimationStarted = true;
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				mainView.previousMonth();
				isAnimationStarted = false;
			}
		});
	}
	
	public void swipeToNextMonth(){
		if(isAnimationStarted)
			return;
		anotherView.nextMonth();
		Animation hideAnimation = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, -1.0f);
		hideAnimation.setDuration(500);
		mainView.startAnimation(hideAnimation);
		
		Animation showAnimation = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, 1.0f,
				Animation.RELATIVE_TO_SELF, 0.0f);
		showAnimation.setDuration(500);
		anotherView.startAnimation(showAnimation);
		showAnimation.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				isAnimationStarted = true;
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				mainView.nextMonth();
				isAnimationStarted = false;
			}
		});
	}
	
	public interface OnDayClickListener{
		public void onDayClick(Cell day);
	}

}
