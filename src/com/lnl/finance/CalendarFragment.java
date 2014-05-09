package com.lnl.finance;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.lnl.finance.listener.FragmentReloadListener;
import com.lnl.finance.util.AppUtil;
import com.lnl.finance.util.BitmapUtil;
import com.lnl.finance.util.DBOperation;
import com.lnl.finance.util.MySharedPreference;
import com.lnl.finance.util.StaticValue;
import com.lnl.finance.util.TimeUtil;
import com.lnl.finance.widget.CalendarLayout;
import com.lnl.finance.widget.CalendarLayout.OnDayClickListener;
import com.lnl.finance.widget.CalendarView;
import com.lnl.finance.widget.CalendarView.OnMonthChangeListener;
import com.lnl.finance.widget.Cell;
import com.lnl.finance.widget.CircleImageView;

public class CalendarFragment extends BaseFragment  implements FragmentReloadListener{

	private CalendarLayout calendarLayout;
	private CalendarView calendarView;
	
	private CalendarAdapter adapter;
	private ListView listView ;
	
	private Map<String, Object> todayFinanceMap;
	
	private OnCalendarMonthChangeListener onCalendarMonthChangeListener;
	
	private MySharedPreference sp;
	
	public void setOnCalendarMonthChangeListener(OnCalendarMonthChangeListener onCalendarMonthChangeListener){
		this.onCalendarMonthChangeListener = onCalendarMonthChangeListener;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		sp = new MySharedPreference(getActivity());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_calendar, null);
		initView(view);
		
		getData();
		return view;
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}
	
	public interface OnCalendarMonthChangeListener{
		public void calendarMonthChange(String month);
	}
	
	
	private void initView(View view) {
		
		View diaryCalendarView = getActivity().getLayoutInflater().inflate(R.layout.view_diary_calendar, null);
		calendarLayout = (CalendarLayout)diaryCalendarView.findViewById(R.id.cl_diary_calendar);
		calendarView = calendarLayout.getMainView();
		calendarView.setMonthChangeListener(monthChangedListener);
		calendarLayout.setDayClickListener(dayClickListener);
		
		if(onCalendarMonthChangeListener!=null){
			onCalendarMonthChangeListener.calendarMonthChange(calendarView.getMonth()+"");
		}
		
		ImageView shadow = (ImageView)diaryCalendarView.findViewById(R.id.iv_calendar_edge_shadow);
		shadow.setImageBitmap(BitmapUtil.createWidthRepeater(AppUtil.getSreenWidth(getActivity()), BitmapUtil.drawable2Bitmap(getResources().getDrawable(R.drawable.diary_calendar_edge_shadow))));

		listView= (ListView)view.findViewById(R.id.lv_calendar_list);
		listView.addHeaderView(diaryCalendarView);
		
		Date date = new Date();
		todayFinanceMap = DBOperation.dayDetailUseList(getActivity(), date.getYear()+1900, date.getMonth()+1, date.getDate());

		adapter = new CalendarAdapter();
		listView.setAdapter(adapter);
		
	}
	
	private void getData() {
		if(calendarView!=null){
			ArrayList<String> strings = DBOperation.dayMarkedList(getActivity(), calendarView.getYear(), calendarView.getMonth());
			calendarView.setMarkedArr(strings);
			calendarView.setPayoffDay(""+sp.getKeyInt(StaticValue.SP_PAYOFFDAY));
		}
	}
	
	
	public void calendarToPresent(View view) {
		calendarView.clearTouchedCell();
		calendarLayout.swipeToPresentMonth();
	}
	
	public void calendarToNext(View view) {
		calendarView.clearTouchedCell();
		calendarLayout.swipeToNextMonth();
	}
	
	private OnMonthChangeListener monthChangedListener = new OnMonthChangeListener() {
		
		@Override
		public void onMonthChanged() {
			System.out.println("onMonthChanged month:"+calendarView.getMonth());
			calendarView.setMarkedArr(null);
			if(onCalendarMonthChangeListener!=null){
				onCalendarMonthChangeListener.calendarMonthChange(calendarView.getMonth()+"");
			}
			
			ArrayList<String> strings = DBOperation.dayMarkedList(getActivity(), calendarView.getYear(), calendarView.getMonth());
			calendarView.setMarkedArr(strings);
			calendarView.setPayoffDay(""+sp.getKeyInt(StaticValue.SP_PAYOFFDAY));
		}
	};
	
	private OnDayClickListener dayClickListener = new OnDayClickListener() {
		
		@Override
		public void onDayClick(Cell day) {
			if(null!=day){
				System.out.println("onDayClick year "+day.getYear()+" month:"+day.getMonth()+" day:"+day.getDayOfMonth());
			}
			todayFinanceMap = DBOperation.dayDetailUseList(getActivity(), day.getYear(), day.getMonth(), day.getDayOfMonth());
			adapter.notifyDataSetChanged();
		}
	};
	
	
	private class CalendarAdapter extends BaseAdapter{

		private LayoutInflater inflater;
		
		public CalendarAdapter(){
			inflater = getActivity().getLayoutInflater();
		}
		
		@Override
		public int getCount() {
			System.out.println("financeCount:"+todayFinanceMap.get("financeCount").toString());
			return Integer.valueOf(todayFinanceMap.get("financeCount").toString())+1;
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
			
			
			System.out.println("getcalendar view pos:"+position);
			if(position==0){

				convertView = inflater.inflate(R.layout.item_section_calendar, null);
				TextView totalUseTextView = (TextView)convertView.findViewById(R.id.tv_today_total_use);
				TextView totalInTextView = (TextView)convertView.findViewById(R.id.tv_today_total_in);
				TextView sectionTitleView = (TextView)convertView.findViewById(R.id.tv_section_date);
				
				totalUseTextView.setText("支出："+todayFinanceMap.get("totalUse").toString()+"元");
				totalInTextView.setText("收入："+todayFinanceMap.get("totalIn").toString()+"元");
				sectionTitleView.setText(todayFinanceMap.get("title").toString());
			}else{
				Map<String, Object> cellMap = ((List<Map<String,Object>>)todayFinanceMap.get("finance")).get(position-1);
				
				convertView = inflater.inflate(R.layout.item_cell_finance, null);
				CircleImageView categoryLogo = (CircleImageView)convertView.findViewById(R.id.iv_logo);
				int drawableId = getActivity().getResources().getIdentifier(cellMap.get("f_c_logo").toString() ,"drawable", "com.lnl.finance"); 
				int color = Integer.parseInt(cellMap.get("f_c_color").toString(), 16)+0xFF000000;
				
				categoryLogo.setBorderWidth(0);
				categoryLogo.setBorderColor(color);
				Bitmap b = BitmapFactory.decodeResource(getResources(), drawableId);
				Bitmap newBitmap = BitmapUtil.createRGBImage(b, color);
				categoryLogo.setImageBitmap(newBitmap);
				
				TextView categoryName = (TextView)convertView.findViewById(R.id.tv_category);
				categoryName.setText(cellMap.get("f_c_name").toString());
				
				
				TextView dateTime = (TextView)convertView.findViewById(R.id.tv_date);
				String timeStr = cellMap.get("f_add_time").toString();
				dateTime.setText(TimeUtil.formatDate(new Date(Long.parseLong(timeStr)), "MM-dd"));
				dateTime.setTextColor(Integer.parseInt(cellMap.get("f_c_color").toString(), 16)+0xFF000000);
				
				TextView typeView = (TextView)convertView.findViewById(R.id.tv_type);
				int textcolor = 0;
				if("1".equals(cellMap.get("f_type").toString())){
					typeView.setText("(收入)");
					textcolor = 0x99cc00+0xFF000000;
				}else{
					typeView.setText("(支出)");
					textcolor = 0xff4444+0xFF000000;
				}
				String descString = cellMap.get("f_desc").toString();
				TextView descTextView = (TextView)convertView.findViewById(R.id.tv_desc);
				if(!"".equals(descString)){
					descTextView.setVisibility(View.VISIBLE);
					descTextView.setText("--"+descString);
				}else{
					descTextView.setVisibility(View.GONE);
				}
				
				TextView moneyTextView = (TextView)convertView.findViewById(R.id.tv_money);
				moneyTextView.setTextColor(textcolor);
				moneyTextView.setTypeface(Typeface.createFromAsset(getActivity().getAssets() , "comic sans ms.ttf"));
				String moneyString = cellMap.get("f_money").toString();
				
				DecimalFormat a = new DecimalFormat("0.##");
				moneyString = a.format(Double.valueOf(moneyString));
				
				if(moneyString.length()>5){
					moneyTextView.setTextSize(15);
				}
				moneyTextView.setText("￥"+cellMap.get("f_money").toString());
			}
			return convertView;
		}
	}
	
	@Override
	public void reload(){
		System.out.println("calendarFragment reload");
		getData();
	}
	
}
