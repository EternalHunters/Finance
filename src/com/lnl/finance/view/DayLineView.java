package com.lnl.finance.view;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lnl.finance.R;
import com.lnl.finance.util.AppUtil;
import com.lnl.finance.widget.XYChartView;

public class DayLineView extends RelativeLayout{
	
	private Activity activity;
	private XYChartView xyChart;
	
	private View view;
	private TextView duringTextView;
	private RelativeLayout relativeLayout;
	
	private String[] coordinateX;
	
	private OnDiaryHistoryTabButtonClickListener historyTabButtonClickListener;
	
	public void setHistoryTabButtonClickListener(
			OnDiaryHistoryTabButtonClickListener historyTabButtonClickListener) {
		this.historyTabButtonClickListener = historyTabButtonClickListener;
	}

	public DayLineView(Context context,Activity activity) {
		super(context);
		this.activity = activity;
		initLineView();
	}
	
	private void initLineView() {
		
		int screenWidth = AppUtil.getSreenWidth(activity);
		int height = (int)(screenWidth/1.5);
		
		int tabLineHeight = (int)(height*0.21);

//		relativeLayout = new RelativeLayout(activity);
		LayoutInflater inflater = activity.getLayoutInflater();
		view = inflater.inflate(R.layout.view_day_line, null);
		
		duringTextView = (TextView)view.findViewById(R.id.tv_during);
		relativeLayout = (RelativeLayout)view.findViewById(R.id.rl_line_content);
		
		RelativeLayout.LayoutParams relativeParams = (RelativeLayout.LayoutParams)relativeLayout.getLayoutParams();
		relativeParams.height = height;
		relativeLayout.setLayoutParams(relativeParams);
		
        xyChart = new XYChartView(getContext(),activity);
		xyChart.setLayoutParams(new LayoutParams((screenWidth-40)/4*29+40, height-tabLineHeight));
        relativeLayout.addView(xyChart);
        
        this.addView(view);
	}
	
	
	public void configData(String[] coordinateX,float[] line){
		this.coordinateX = coordinateX;
		xyChart.clearLineList();
		xyChart.addLine("每日", coordinateX, line);
		xyChart.redraw();
//		addTabButton(coordinateX);
//		int width = xyChart.getWidth();
//		scrollView.scrollTo(width, 0);
		
	}

	public interface OnDiaryHistoryTabButtonClickListener{
		public void buttonClick(View v);
	}

}
