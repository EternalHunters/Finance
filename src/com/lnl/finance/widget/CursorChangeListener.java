package com.lnl.finance.widget;

import android.view.MotionEvent;

public interface CursorChangeListener {
	public void onFingerTouch(MotionEvent motionEvent);
	public void onFingerUp(MotionEvent motionEvent);
	public void onFingerMove(MotionEvent motionEvent,int position);
}
