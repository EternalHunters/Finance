package com.lnl.finance.more;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hb.views.PinnedSectionListView;
import com.hb.views.PinnedSectionListView.PinnedSectionListAdapter;
import com.lnl.finance.BaseFragmentActivity;
import com.lnl.finance.PasswordActivity;
import com.lnl.finance.R;
import com.lnl.finance.dialog.PayoffDaySetDialog;
import com.lnl.finance.dialog.PayoffDaySetDialog.OnSelectedListener;
import com.lnl.finance.dialog.PayoffMonthLineSetDialog;
import com.lnl.finance.dialog.PayoffMonthLineSetDialog.OnPayoffMonthSetListener;
import com.lnl.finance.util.BackupTask;
import com.lnl.finance.util.DBOperation;
import com.lnl.finance.util.MySharedPreference;
import com.lnl.finance.util.StaticValue;
import com.lnl.finance.widget.FlipImageView;
import com.lnl.finance.widget.FlipImageView.OnFlipListener;
import com.umeng.fb.FeedbackAgent;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;

public class SettingActivity extends BaseFragmentActivity{

	private PinnedSectionListView listView;
	private SettingAdapter adapter;
	
	private List<Map<String, String>> dataList = new ArrayList<Map<String,String>>();
	
	private final static int SECTION = 0;
	private final static int CELL = 1;
	private final static int HEADER = -1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		try {  
		    getWindow().addFlags(WindowManager.LayoutParams.class.getField("FLAG_NEEDS_MENU_KEY").getInt(null));  
		}catch (NoSuchFieldException e) {  
		    // Ignore since this field won't exist in most versions of Android  
		}catch (IllegalAccessException e) {  
		    Log.w("feelyou.info", "Could not access FLAG_NEEDS_MENU_KEY in addLegacyOverflowButton()", e);  
		}
		
		overridePendingTransition(R.anim.umeng_fb_slide_in_from_right, R.anim.umeng_fb_slide_out_from_left); 

		dataList.add(initMap("00", HEADER, "我的余额", -1));
		dataList.add(initMap("10", SECTION, "程序设置",0));
		dataList.add(initMap("11", CELL, "时间选择震动",0));
		dataList.add(initMap("12", CELL, "发工资日",0));
		dataList.add(initMap("13", CELL, "设置月支出上限",0));
		dataList.add(initMap("14", CELL, "当月已支出",0));
		
		dataList.add(initMap("60", SECTION, "软件功能",1));
		dataList.add(initMap("64", CELL, "自定义分类管理",1));
		dataList.add(initMap("61", CELL, "软件密码锁",1));
		dataList.add(initMap("62", CELL, "自动备份",1));
		dataList.add(initMap("63", CELL, "清理备份",1));
		
		dataList.add(initMap("20", SECTION, "更新",2));
		dataList.add(initMap("21", CELL, "检查更新（不自动检查）",2));
		
		dataList.add(initMap("30", SECTION, "反馈",3));
		dataList.add(initMap("31", CELL, "反馈意见",3));
		
		dataList.add(initMap("40", SECTION, "关于",4));
		dataList.add(initMap("41", CELL, "关于App",4));
		
		initView();
		
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		overridePendingTransition(R.anim.umeng_fb_slide_in_from_left, R.anim.umeng_fb_slide_out_from_right); 
	}
	
	
	private void initView() {
		
		adapter = new SettingAdapter(this);
		
		listView = (PinnedSectionListView)findViewById(R.id.lv_setting_list);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(itemClickListener);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}
	
	
	
	private Map<String, String> initMap( String id, int type, String title, int sectionPos){
		Map<String, String> map = new HashMap<String, String>();
		map.put("id", id);
		map.put("type", type+"");
		map.put("title", title);
		map.put("sectionPos", sectionPos+"");
		return map;
	}
	
	
	private class SettingAdapter extends BaseAdapter implements PinnedSectionListAdapter
	{

		private LayoutInflater inflater;
		private MySharedPreference sp;
		private final int[] COLORS = new int[] {R.color.header_color_3, R.color.header_color_1, R.color.header_color_2, R.color.header_color_4 };
		
		public SettingAdapter(Context context) {
			this.inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			this.sp = new MySharedPreference(context);
		}
		
		@Override
		public int getCount() {
			return dataList.size();
		}

		@Override
		public Map<String, String> getItem(int position) {
			return dataList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			if(Integer.valueOf(getItem(position).get("type"))==HEADER){
				convertView = inflater.inflate(R.layout.item_cell_setting_header, null);
				
				TextView mybalenceView = (TextView)convertView.findViewById(R.id.tv_mybalence);
				
				Double balenceDouble = DBOperation.getTotalBalance(SettingActivity.this);
				if(balenceDouble<0){
					mybalenceView.setTextColor(getResources().getColor(R.color.header_color_4));
				}else{
					mybalenceView.setTextColor(getResources().getColor(R.color.header_color_1));
				}
				mybalenceView.setText("￥"+String.format("%.2f",balenceDouble));
				
			}else if(Integer.valueOf(getItem(position).get("type"))==SECTION){
				
				convertView = inflater.inflate(R.layout.item_section_finance, null);
				
				TextView titleView = (TextView)convertView.findViewById(R.id.tv_section_title);
				titleView.setText(getItem(position).get("title"));
				
				convertView.setBackgroundColor(parent.getResources().getColor(COLORS[Integer.valueOf(getItem(position).get("sectionPos")) % COLORS.length]));
			}else{
				
				convertView = inflater.inflate(R.layout.item_cell_setting, null);
				TextView titleView = (TextView)convertView.findViewById(R.id.tv_setting_title);
				titleView.setText(getItem(position).get("title"));
				
				
				String id = getItem(position).get("id");
				TextView descContent = (TextView)convertView.findViewById(R.id.tv_setting_content);
				FlipImageView selectorImage = (FlipImageView)convertView.findViewById(R.id.fiv_selector);
				if("11".equals(id)){
					selectorImage.setVisibility(View.VISIBLE);
					
					boolean isViration = sp.getKeyBoolean(StaticValue.SP_DATESELECTVIBRATION);
					if(isViration){
						selectorImage.setDrawable(getResources().getDrawable(R.drawable.selector_on));
						selectorImage.setFlippedDrawable(getResources().getDrawable(R.drawable.selector_off));
					}else{
						selectorImage.setDrawable(getResources().getDrawable(R.drawable.selector_off));
						selectorImage.setFlippedDrawable(getResources().getDrawable(R.drawable.selector_on));
					}
					selectorImage.setOnFlipListener(new OnFlipListener() {
						@Override
						public void onFlipStart(FlipImageView view) {
						}
						@Override
						public void onFlipEnd(FlipImageView view) {
						}
						
						@Override
						public void onClick(FlipImageView view) {
							sp.setKeyBoolean(StaticValue.SP_DATESELECTVIBRATION, !sp.getKeyBoolean(StaticValue.SP_DATESELECTVIBRATION));
						}
					});
				}else if("12".equals(id)){
					selectorImage.setVisibility(View.INVISIBLE);
					descContent.setVisibility(View.VISIBLE);
					int payoffday = sp.getKeyInt(StaticValue.SP_PAYOFFDAY);
					String payoffDayStr = "月初";
					if(payoffday==0){
						payoffDayStr = "月末";
					}else if(payoffday==1){
						payoffDayStr = "月初";
					}else{
						payoffDayStr = "每月"+payoffday+"号";
					}
					descContent.setTextColor(getResources().getColor(R.color.header_color_1));
					descContent.setText(payoffDayStr);
				}else if("13".equals(id)){
					selectorImage.setVisibility(View.INVISIBLE);
					descContent.setVisibility(View.VISIBLE);
					descContent.setTextColor(getResources().getColor(R.color.header_color_1));
					
					String payoffLine = sp.getKeyStr(StaticValue.SP_PAYOFF_MONTH_LINE);
					if(payoffLine.equals("")){
						payoffLine = "5000";
						sp.setKeyStr(StaticValue.SP_PAYOFF_MONTH_LINE, payoffLine);
					}
					
					descContent.setText("￥"+payoffLine);
				}else if("14".equals(id)){
					selectorImage.setVisibility(View.INVISIBLE);
					descContent.setVisibility(View.VISIBLE);
					descContent.setTextColor(getResources().getColor(R.color.header_color_4));
					
					DecimalFormat a = new DecimalFormat("0.##");
					String moneyString = a.format(DBOperation.currentMonthPayoff(SettingActivity.this));
					
					descContent.setText("￥"+moneyString);
				}else if("61".equals(id)){
					if(!"".equals(sp.getKeyStr(StaticValue.SP_APP_PASSWORD))){
						selectorImage.setVisibility(View.VISIBLE);
						selectorImage.setEnabled(false);
						selectorImage.setDrawable(getResources().getDrawable(R.drawable.selector_on));
						descContent.setVisibility(View.INVISIBLE);
					}else{
						selectorImage.setVisibility(View.INVISIBLE);
						descContent.setVisibility(View.VISIBLE);
						descContent.setTextColor(getResources().getColor(R.color.header_color_4));
						descContent.setText("点击设置密码锁");
					}
				}else if("62".equals(id)){
					
					selectorImage.setVisibility(View.VISIBLE);
					
					boolean autoBackup = sp.getKeyBoolean(StaticValue.SP_AUTO_BACKUP);
					if(autoBackup){
						selectorImage.setDrawable(getResources().getDrawable(R.drawable.selector_on));
						selectorImage.setFlippedDrawable(getResources().getDrawable(R.drawable.selector_off));
					}else{
						selectorImage.setDrawable(getResources().getDrawable(R.drawable.selector_off));
						selectorImage.setFlippedDrawable(getResources().getDrawable(R.drawable.selector_on));
					}
					selectorImage.setOnFlipListener(new OnFlipListener() {
						@Override
						public void onFlipStart(FlipImageView view) {
						}
						@Override
						public void onFlipEnd(FlipImageView view) {
						}
						
						@Override
						public void onClick(FlipImageView view) {
							sp.setKeyBoolean(StaticValue.SP_AUTO_BACKUP, !sp.getKeyBoolean(StaticValue.SP_AUTO_BACKUP));
						}
					});
				}else if("63".equals(id)){
					selectorImage.setVisibility(View.INVISIBLE);
					descContent.setVisibility(View.VISIBLE);
					
					descContent.setText(BackupTask.getBackupLength());
				}
			}
			return convertView;
		}
		
		@Override 
		public int getViewTypeCount() {
            return 2;
        }

        @Override 
        public int getItemViewType(int position) {
            return Integer.valueOf(getItem(position).get("type"));
        }

        @Override
        public boolean isItemViewTypePinned(int viewType) {
            return viewType == SECTION;
        }
	}
	
	private OnItemClickListener itemClickListener = new OnItemClickListener() {

		@SuppressLint("NewApi")
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			
			String iid = dataList.get(arg2).get("id");
			if("12".equals(iid)){
				
				MySharedPreference sp = new MySharedPreference(SettingActivity.this);
				int defaultSeleted = sp.getKeyInt(StaticValue.SP_PAYOFFDAY);
				if(defaultSeleted==0){
					defaultSeleted = 1;
				}else if(defaultSeleted==1){
					defaultSeleted = 0;
				}
				PayoffDaySetDialog daySetDialog = PayoffDaySetDialog.newInstance(selectedListener,defaultSeleted);
				daySetDialog.show(getSupportFragmentManager(), "payoffdayset");
			}else if("13".equals(iid)){
				PayoffMonthLineSetDialog payoffMonthLineSetDialog = PayoffMonthLineSetDialog.newInstance(payoffMonthSetListener);
				payoffMonthLineSetDialog.show(getSupportFragmentManager(), "payoffmonthlineset");
				
			}else if("41".equals(iid)){
				Intent intent = new Intent(SettingActivity.this, AboutActivity.class);
				startActivity(intent);
			}else if("21".equals(iid)){
				updateApp();
			}else if("31".equals(iid)){
				FeedbackAgent agent = new FeedbackAgent(SettingActivity.this);
			    agent.startFeedbackActivity();
			}else if("50".equals(iid)||"51".equals(iid)){
//				String aa ="https://m.alipay.com/personal/payment.htm?userId=2088602029892743&reason=支持软件作者开发&weChat=true";
//				Uri uri = Uri.parse(aa);
//				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//				startActivity(intent);
				
				ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);  
				if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB ){
					ClipData cd = ClipData.newPlainText("label", "yanglx0505@gmail.com");  
					clipboardManager.setPrimaryClip(cd);  
				}else{
					clipboardManager.setText("yanglx0505@gmail.com");
				}
				
				showAppMsg("支付宝账号已经复制到剪切板",STYLE_CONFIRM);
			}else if("61".equals(iid)){
				MySharedPreference sp = new MySharedPreference(SettingActivity.this);
				//TODO:cancle passwordview 或者 setpassword view
				if(!"".equals(sp.getKeyStr(StaticValue.SP_APP_PASSWORD))){
					
					new AlertDialog.Builder(SettingActivity.this).setTitle("提示").setMessage("是否想要取消密码锁?").setPositiveButton("是", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							Intent intent = new Intent(SettingActivity.this, PasswordActivity.class);
							intent.putExtra("passwordtype", StaticValue.PasswordType_CLEAR);
							startActivityForResult(intent, 107);
						}
					}).setNegativeButton("否",null).create().show();
					
				}else{
					Intent intent = new Intent(SettingActivity.this, PasswordActivity.class);
					intent.putExtra("passwordtype", StaticValue.PasswordType_SET);
					startActivityForResult(intent, 109);
				}
				
			}else if("63".equals(iid)){
				BackupTask.clearBackup();
				showAppMsg("备份已删除", STYLE_INFO);
				adapter.notifyDataSetChanged();
			}else if("64".equals(iid)){
				Intent intent = new Intent(SettingActivity.this, ManagerCustomCategoryActivity.class);
				startActivity(intent);
			}
		}
	};
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==107&&resultCode==RESULT_OK){//取消密码返回
			showAppMsg("软件密码锁已取消", STYLE_INFO);
		}else if(requestCode==109&&resultCode==RESULT_OK){//设置密码返回
			showAppMsg("成功设置软件密码锁", STYLE_INFO);
		}
		if(null!=adapter){
			adapter.notifyDataSetChanged();
		}
	};
	
	private OnPayoffMonthSetListener payoffMonthSetListener = new OnPayoffMonthSetListener() {
		
		@Override
		public void payoffMonthLineSetted() {
			adapter.notifyDataSetChanged();
		}
	};
	
	
	private void updateApp() {
		
		UmengUpdateAgent.setUpdateOnlyWifi(false);
		UmengUpdateAgent.setUpdateAutoPopup(true);
		UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
		    @Override
		    public void onUpdateReturned(int updateStatus,UpdateResponse updateInfo) {
		        switch (updateStatus) {
		        case UpdateStatus.Yes: // has update
		            UmengUpdateAgent.showUpdateDialog(SettingActivity.this, updateInfo);
		            break;
		        case UpdateStatus.No: // has no update
		        	showAppMsg("已经是最新版本",STYLE_CONFIRM);
		            break;
		        case UpdateStatus.NoneWifi: // none wifi
		        	showAppMsg("没有wifi连接， 只在wifi下更新",STYLE_CONFIRM);
		            break;
		        case UpdateStatus.Timeout: // time out
		        	showAppMsg("超时",STYLE_CONFIRM);
		            break;
		        }
		    }
		});
		UmengUpdateAgent.update(this);
	}
	
	private OnSelectedListener selectedListener = new OnSelectedListener() {
		
		@Override
		public void doneSelected(int selectedDay) {
			MySharedPreference sp = new MySharedPreference(SettingActivity.this);
			sp.setKeyInt(StaticValue.SP_PAYOFFDAY, selectedDay);
			
			adapter.notifyDataSetChanged();
		}
	};
	
}
