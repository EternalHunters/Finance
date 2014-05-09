package com.lnl.finance.view;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.kenyang.piechart.PieChart;
import net.kenyang.piechart.PieChart.OnSelectedLisenter;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lnl.finance.R;
import com.lnl.finance.util.DBOperation;

public class PieCellView extends RelativeLayout {

	private View view;
	private Activity activity;

	private PieChart pieChart;
	private ArrayList<Float> alPercentage;
	private int maxCategoryIndex;
	
	private List<Map<String, Object>> monthCategoryList;
	
	private TextView pieDesc;

	public PieCellView(Context context, Activity activity,
			ArrayList<Float> alPercentage, int maxCategoryIndex,List<Map<String, Object>> monthCategoryList) {
		super(context);
		this.activity = activity;
		this.alPercentage = alPercentage;
		this.maxCategoryIndex = maxCategoryIndex;
		this.monthCategoryList = monthCategoryList;

		initView();
	}

	private void initView() {

		LayoutInflater inflater = activity.getLayoutInflater();
		view = inflater.inflate(R.layout.view_cell_pie, null);

		pieChart = (PieChart) view.findViewById(R.id.pieChart);

		try {
			// setting data
			pieChart.setAdapter(alPercentage);
			// setting a listener
			pieChart.setOnSelectedListener(new OnSelectedLisenter() {
				@Override
				public void onSelected(int iSelectedIndex) {
					
					String categorynameString = monthCategoryList.get(iSelectedIndex).get("cmu_c_name").toString();
					String categoryPercentString  = alPercentage.get(iSelectedIndex).toString();
					
					DecimalFormat a = new DecimalFormat("0.##");
					String moneyString = a.format(Double.valueOf(monthCategoryList.get(iSelectedIndex).get("cmu_money").toString()));
					
					pieDesc.setText(categorynameString+"("+moneyString+"元)  "+categoryPercentString+"%");
				}
			});

		} catch (Exception e) {
			if (e.getMessage().equals(PieChart.ERROR_NOT_EQUAL_TO_100)) {
				Log.e("kenyang", "percentage is not equal to 100");
			}
		}
		

		pieDesc = (TextView)view.findViewById(R.id.tv_pie_desc);
		if(monthCategoryList!=null){
			String categorynameString = monthCategoryList.get(maxCategoryIndex).get("cmu_c_name").toString();
			String categoryPercentString  = alPercentage.get(maxCategoryIndex).toString();
			
			DecimalFormat a = new DecimalFormat("0.##");
			String moneyString = a.format(Double.valueOf(monthCategoryList.get(maxCategoryIndex).get("cmu_money").toString()));
			
			pieDesc.setText(categorynameString+"("+moneyString+"元)  "+categoryPercentString+"%");
		}else{
			pieDesc.setText("暂无分类支出数据");
		}
		this.addView(view);

		pieChart.setISelectedIndex(maxCategoryIndex);
	}
}
