package com.lnl.finance;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TabHost;

import com.lnl.finance.calendar.CalendarActivity;
import com.lnl.finance.index.AddActivity;
import com.lnl.finance.index.IndexActivity;
import com.lnl.finance.line.LineActivity;
import com.lnl.finance.util.BackupTask;
import com.lnl.finance.util.DBOperation;
import com.lnl.finance.util.MySharedPreference;

public class MainActivity extends TabActivity {

	public static TabHost tabHost;
	public TabHost.TabSpec spec = null;
	
	private static ImageButton lineButton;
	private static ImageButton	calendarButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		MySharedPreference sp = new MySharedPreference(this);
		if(!sp.getKeyBoolean("appstarted")){
			sp.setKeyBoolean("appstarted", true);
			System.out.println("input");
			DBOperation.insertLogoColor(this);
			DBOperation.insertCategory(this);
		}
		initTabHost();
		initView();
	}

	private void initTabHost() {
		tabHost = this.getTabHost();
		
		View tabIndex = View.inflate(MainActivity.this, R.layout.item_tab_main, null);
		spec = tabHost.newTabSpec("tab0").setIndicator(tabIndex).setContent(new Intent(this, IndexActivity.class));
		tabHost.addTab(spec);
        
		View tabDiary = View.inflate(MainActivity.this, R.layout.item_tab_main, null);
		spec = tabHost.newTabSpec("tab1").setIndicator(tabDiary).setContent(new Intent(this, LineActivity.class));
		tabHost.addTab(spec);
				
		View tabConsult = View.inflate(MainActivity.this, R.layout.item_tab_main, null);
		spec = tabHost.newTabSpec("tab2").setIndicator(tabConsult).setContent(new Intent(this, CalendarActivity.class));
		tabHost.addTab(spec);
		
		tabHost.setCurrentTab(0);
	}
	
	
	private void initView() {
		lineButton = (ImageButton)findViewById(R.id.ib_line_button);
		calendarButton = (ImageButton)findViewById(R.id.ib_calendar_button);
		
		lineButton.setTag(1);
		calendarButton.setTag(2);
		
		lineButton.setOnClickListener(clickListener);
		calendarButton.setOnClickListener(clickListener);
	}
	
	private OnClickListener clickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {

			tabHost.setCurrentTab(Integer.valueOf(v.getTag().toString()));
			
			if(Integer.valueOf(v.getTag().toString())==1){
				lineButton.setEnabled(false);
				calendarButton.setEnabled(true);
			}else{
				lineButton.setEnabled(true);
				calendarButton.setEnabled(false);
			}
		}
	};
	
	
	
	public void toAddView(View view){
		Intent intent = new Intent(this, AddActivity.class);
		startActivity(intent);
	}
	
	public static void backIndexView(){
		tabHost.setCurrentTab(0);
		lineButton.setEnabled(true);
		calendarButton.setEnabled(true);
	}
	
	
}
