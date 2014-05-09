package com.lnl.finance.view;

import com.echo.holographlibrary.Line;
import com.echo.holographlibrary.LineGraph;
import com.lnl.finance.R;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class LineCellView  extends RelativeLayout{

	private View view;
	private Activity activity;
	private Line l;
	
	
	public LineCellView(Context context,Activity activity,Line l) {
		super(context);
		this.activity = activity;
		this.l = l;
		
		initView();
	}
	
	private void initView() {
		LayoutInflater inflater = activity.getLayoutInflater();
		view = inflater.inflate(R.layout.view_cell_line, null);
		
		LineGraph li = (LineGraph)view.findViewById(R.id.lg_linegraph);
		li.setActivity(activity);
		li.addLine(l);
		li.setLineToFill(0);
		
		this.addView(view);
	}
}
