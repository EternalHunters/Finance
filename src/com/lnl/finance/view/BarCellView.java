package com.lnl.finance.view;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.echo.holographlibrary.Bar;
import com.echo.holographlibrary.BarGraph;
import com.lnl.finance.R;

public class BarCellView  extends RelativeLayout{

	private View view;
	private Activity activity;
	private ArrayList<Bar> points;
	
	public BarCellView(Context context,Activity activity,ArrayList<Bar> points) {
		super(context);
		this.activity = activity;
		this.points = points;
		
		initView();
	}
	
	private void initView() {
		System.out.println("barcell "+points.size());
		LayoutInflater inflater = activity.getLayoutInflater();
		view = inflater.inflate(R.layout.view_cell_bar, null);
		
		BarGraph bg = (BarGraph)view.findViewById(R.id.bg_bargraph);
		assert bg != null;
		bg.setActivity(activity);
		bg.setUnit("元");
		bg.appendUnit(true);
		bg.setBars(points);
		
		this.addView(view);
	}
	
}
