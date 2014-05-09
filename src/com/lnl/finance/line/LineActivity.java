package com.lnl.finance.line;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.lnl.finance.MainActivity;
import com.lnl.finance.R;
import com.lnl.finance.more.SettingActivity;
import com.lnl.finance.util.AppUtil;
import com.lnl.finance.view.DayLineView;

public class LineActivity  extends Activity{

	private DayLineView lineView;
	
	private LineAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_line);
		
		initView();
	}
	
	
	public void toIndexView(View view) {
		MainActivity.backIndexView();
	}
	
	public void toSettingView(View view){
		Intent intent = new Intent(this, SettingActivity.class);
		startActivity(intent);
	}
	
	private void initView() {
		
		int screenWidth = AppUtil.getSreenWidth(this);
		int height = (int)(screenWidth/1.5);
		
		lineView = new DayLineView(this,this);
		lineView.setLayoutParams(new ListView.LayoutParams(screenWidth, height));
		
		ListView listView = (ListView)findViewById(R.id.lv_line_list);
		listView.addHeaderView(lineView);
		
		adapter = new LineAdapter();
		listView.setAdapter(adapter);
		
		lineView.configData(new String[]{"11","22"}, new float[]{23.3f,155656.2f});
	}
	

	private class LineAdapter extends BaseAdapter{

		private LayoutInflater inflater;
		
		public LineAdapter(){
			inflater = getLayoutInflater();
		}
		
		@Override
		public int getCount() {
			return 0;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			
			return null;
		}
	}
	
}
