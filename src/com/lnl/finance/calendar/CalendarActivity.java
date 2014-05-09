package com.lnl.finance.calendar;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.lnl.finance.MainActivity;
import com.lnl.finance.R;
import com.lnl.finance.more.SettingActivity;
import com.lnl.finance.util.AppUtil;
import com.lnl.finance.util.BitmapUtil;
import com.lnl.finance.util.MySharedPreference;
import com.lnl.finance.util.StaticValue;
import com.lnl.finance.widget.CalendarLayout;
import com.lnl.finance.widget.CalendarLayout.OnDayClickListener;
import com.lnl.finance.widget.CalendarView;
import com.lnl.finance.widget.CalendarView.OnMonthChangeListener;
import com.lnl.finance.widget.Cell;

public class CalendarActivity extends Activity{

	private CalendarLayout calendarLayout;
	private CalendarView calendarView;
	
	private TextView titleTextView;
	
	private CalendarAdapter adapter;
	
	private MySharedPreference sp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calendar);
		
		initView();
	}
	
	private void initView() {
		
		sp = new MySharedPreference(this);
		
		View diaryCalendarView = getLayoutInflater().inflate(R.layout.view_diary_calendar, null);
		calendarLayout = (CalendarLayout)diaryCalendarView.findViewById(R.id.cl_diary_calendar);
		calendarView = calendarLayout.getMainView();
		calendarView.setMonthChangeListener(monthChangedListener);
		calendarLayout.setDayClickListener(dayClickListener);
		
		titleTextView = (TextView)findViewById(R.id.tv_title);
		titleTextView.setText(calendarView.getMonth()+"月");
		
		ImageView shadow = (ImageView)diaryCalendarView.findViewById(R.id.iv_calendar_edge_shadow);
		shadow.setImageBitmap(BitmapUtil.createWidthRepeater(AppUtil.getSreenWidth(this), BitmapUtil.drawable2Bitmap(getResources().getDrawable(R.drawable.diary_calendar_edge_shadow))));

		ListView listView = (ListView)findViewById(R.id.lv_calendar_list);
		listView.addHeaderView(diaryCalendarView);
		
		adapter = new CalendarAdapter();
		listView.setAdapter(adapter);
		
		
		ArrayList<String> strings = new ArrayList<String>();
		strings.add("1");
		strings.add("2");
		strings.add("20");
		calendarView.setMarkedArr(strings);
//		calendarView.setPayoffDay("1");
		calendarView.setPayoffDay(""+sp.getKeyInt(StaticValue.SP_PAYOFFDAY));
		
	}
	
	public void toIndexView(View view) {
		MainActivity.backIndexView();
	}
	
	public void toSettingView(View view){
		Intent intent = new Intent(this, SettingActivity.class);
		startActivity(intent);
	}
	
	public void calendarToPresent(View view) {
		calendarLayout.swipeToPresentMonth();
	}
	
	public void calendarToNext(View view) {
		calendarLayout.swipeToNextMonth();
	}
	
	private OnMonthChangeListener monthChangedListener = new OnMonthChangeListener() {
		
		@Override
		public void onMonthChanged() {
			System.out.println("onMonthChanged month:"+calendarView.getMonth());
			calendarView.setMarkedArr(null);
//			getMonthCalendarData(new Date(calendarView.getYear()-1900, calendarView.getMonth()-1, 1));
			titleTextView.setText(calendarView.getMonth()+"月");
		}
	};
	
	private OnDayClickListener dayClickListener = new OnDayClickListener() {
		
		@SuppressWarnings("deprecation")
		@Override
		public void onDayClick(Cell day) {
			if(null!=day){
				System.out.println("onDayClick year "+day.getYear()+" day:"+day.getDayOfMonth());
//				getCalendarDate(new Date(day.getYear()-1900, day.getMonth()-1, day.getDayOfMonth()));
				
			}
		}
	};
	
	
	private class CalendarAdapter extends BaseAdapter{

		private LayoutInflater inflater;
		
		public CalendarAdapter(){
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
