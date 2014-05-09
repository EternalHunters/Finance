package com.lnl.finance.index;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.fourmob.datetimepicker.date.DatePickerDialog.OnDateSetListener;
import com.lnl.finance.BaseFragmentActivity;
import com.lnl.finance.R;
import com.lnl.finance.more.AddCustomCategoryActivity;
import com.lnl.finance.util.DBOperation;
import com.lnl.finance.util.MySharedPreference;
import com.lnl.finance.util.StaticValue;
import com.lnl.finance.util.TimeUtil;
import com.lnl.finance.view.CategoryTabView;
import com.lnl.finance.view.CategoryTabView.OnTabViewSeletedListener;
import com.sleepbot.datetimepicker.time.RadialPickerLayout;
import com.sleepbot.datetimepicker.time.TimePickerDialog;

public class AddDetailActivity extends BaseFragmentActivity implements OnDateSetListener, TimePickerDialog.OnTimeSetListener,OnTabViewSeletedListener {

	private RelativeLayout financeCategory;
	private CategoryTabView tabView;
	
	private TextView timeLabel;
	private EditText remarkEditText;
	
	public TextView state;
	
	private String selectDate;
	private String selectTime;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_detail);
		
		initView();
	}
	
	public void back(View view) {
		finish();
	}
	
	private void initView() {
		
		TextView numberLabel = (TextView)findViewById(R.id.tv_number_content);
		numberLabel.setTypeface(Typeface.createFromAsset(getAssets(), "comic sans ms.ttf"));
		numberLabel.setText("￥"+getIntent().getStringExtra("money"));
		
		timeLabel = (TextView)findViewById(R.id.tv_time);
		remarkEditText = (EditText)findViewById(R.id.et_remark);
		initSelectTime(null);
		
		List<Map<String, Object>> list = DBOperation.findOutCategory(this,getIntent().getIntExtra("tabType",2)+"");
		
		RelativeLayout diaryTabLayout = (RelativeLayout)findViewById(R.id.rl_finance_category);
		tabView = new CategoryTabView(this, this, list);
		tabView.setOnTabViewSeletedListener(this);
		diaryTabLayout.addView(tabView, new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT,android.view.ViewGroup.LayoutParams.MATCH_PARENT));
		
	}
	
	public void toSelectDate(View view){
		
		 final Calendar calendar = Calendar.getInstance();
		 
		 MySharedPreference sp = new MySharedPreference(this);
		 boolean vibration = sp.getKeyBoolean(StaticValue.SP_DATESELECTVIBRATION);
		 final DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), vibration);

         datePickerDialog.setYearRange(1985, 2028);
         datePickerDialog.show(getSupportFragmentManager(), "datepicker");
	}

	@Override
	public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
		
		String timeStr = timeLabel.getText().toString();
        String[] times = timeStr.split(" ");
        
        String hourStr = hourOfDay<10?"0"+hourOfDay:""+hourOfDay;
        String minuteStr = minute<10?"0"+minute:""+minute;
        
        if(times.length>=1){
            timeLabel.setText(times[0]+" "+hourStr+":"+minuteStr);
        }else{
        	Calendar calendar = Calendar.getInstance();
            timeLabel.setText(calendar.get(Calendar.MONTH+1)+"月"+calendar.get(Calendar.DAY_OF_MONTH)+"日 "+hourOfDay+":"+minute);
        }
        selectTime = hourStr+":"+minuteStr;
	}

	@Override
	public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
		
		MySharedPreference sp = new MySharedPreference(this);
		boolean vibration = sp.getKeyBoolean(StaticValue.SP_DATESELECTVIBRATION);
		 
		final Calendar calendar = Calendar.getInstance();
		final TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(this, calendar.get(Calendar.HOUR_OF_DAY) ,calendar.get(Calendar.MINUTE), true, vibration);
        timePickerDialog.show(getSupportFragmentManager(), "timepicker");
        
        String monthStr = (month+1)<10?"0"+(month+1):""+(month+1);
        String dayStr = day<10?"0"+day:""+day;
        
        timeLabel.setText(monthStr+"月"+dayStr+"日 "+selectTime);
        selectDate = year+"-"+monthStr+"-"+dayStr;
	}
	
	public void initSelectTime(View view){

		Date nowDate  = new Date();
		timeLabel.setText(TimeUtil.formatDate(nowDate, "MM月dd日 HH:mm"));
		selectDate = TimeUtil.formatDate(nowDate, "yyyy-MM-dd");
		selectTime = TimeUtil.formatDate(nowDate, "HH:mm");
	}
	
	public void addCustomCategoryAction(View view){
		
		Intent intent = new Intent(this, AddCustomCategoryActivity.class);
		intent.putExtra("from", "addDetailActivity");
		startActivityForResult(intent, 119);
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);

		List<Map<String, Object>> list = DBOperation.findOutCategory(this,getIntent().getIntExtra("tabType",2)+"");
		tabView.reload(list);
		

		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(remarkEditText.getWindowToken(), 0);
	}
	
	private long getSelectTime(){
		long returnTime = new Date().getTime();
		try {
			
			returnTime = TimeUtil.stringToLong(selectDate+" "+selectTime, "yyyy-MM-dd HH:mm");
		} catch (Exception e) {
		}
		
		return returnTime;
	}
	
	private String getSelectYear(){
		return selectDate.split("-")[0];
	}
	
	private String getSelectMonth(){
		return selectDate.split("-")[1];
	}
	
	private String getSelectDay(){
		return selectDate.split("-")[2];
	}

	private String cId;
	private String name;
	private String logo;
	private String color;
	@Override
	public void tabViewSeleted(String id, String name, String logo,String color) {
		System.out.println(name+" " +logo);
		this.cId = id;
		this.name = name;
		this.logo = logo;
		this.color = color;
	}
	
	public void saveFinance(View view){
		
		if(cId==null||name==null||logo==null){
			showAppMsg("请先选择项目",STYLE_STICKY);
			return;
		}
		
		String money = getIntent().getStringExtra("money");
		String c_id = cId;
		String c_name = name;
		String c_logo = logo;
		String c_color = color;
		String add_time = getSelectTime()+"";
		String type = getIntent().getIntExtra("tabType",2)+"";
		String isPlan = 0+"";
		String year = getSelectYear();
		String month = getSelectMonth();
		String day = getSelectDay();
		String remark = remarkEditText.getText().toString();
		
		if(DBOperation.saveFinance(this, money, c_id, c_name, c_logo, c_color, add_time, type, isPlan, year, month, day, remark)){
			showToast("保存成功");
			Intent intent = new Intent(this, AddActivity.class);
			setResult(RESULT_OK, intent);
			finish();
			
		}else{
			showAppMsg("保存失败，请重试",STYLE_CONFIRM);
		}
	}
}
