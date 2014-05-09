package com.lnl.finance;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lnl.finance.CalendarFragment.OnCalendarMonthChangeListener;
import com.lnl.finance.listener.FragmentReloadListener;
import com.lnl.finance.more.SettingActivity;
import com.lnl.finance.util.BackupTask;
import com.lnl.finance.util.BackupTask.OnBakcupTaskListener;
import com.lnl.finance.util.DBOperation;
import com.lnl.finance.util.MySharedPreference;
import com.lnl.finance.util.StaticValue;
import com.lnl.finance.util.VersionControlUtil;
import com.umeng.fb.FeedbackAgent;

public class MainNewActivity extends FragmentActivity implements OnBakcupTaskListener{

	private ViewPager pager;
	private MainAdapter adapter;
	
	private TextView titleView;
	private TextView calendarTitleView;
	private LinearLayout calendarTitleLayout;

//	private InterstitialAd interstitialAd;
	
	private boolean backShowKeyView = true;
	private View loadingview;
	
	private boolean appstart = true;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏 
		setContentView(R.layout.activity_main_new);
		loadingview = getLayoutInflater().inflate(R.layout.view_loading, null);
		this.addContentView(loadingview, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		
		//清除通知
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.cancel(55555);
		//获取反馈
		FeedbackAgent agent = new FeedbackAgent(this);
		agent.sync();
	}
	

//	private void loadAD(){
//		interstitialAd = new InterstitialAd(this);
//		interstitialAd.setListener(new InterstitialAdListener() {
//			
//			@Override
//			public void onAdReady() {
//				
//			}
//			
//			@Override
//			public void onAdPresent() {
//				
//			}
//			
//			@Override
//			public void onAdFailed(String arg0) {
//			}
//			
//			@Override
//			public void onAdDismissed() {
//				interstitialAd.loadAd();
//			}
//			
//			@Override
//			public void onAdClick(InterstitialAd arg0) {
//				
//			}
//		});
//		interstitialAd.loadAd();
//	}
	
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		
		if(adapter!=null && adapter.getItem(2)!=null){
			((FragmentReloadListener)adapter.getItem(2)).reload();
		}
	}
	
	private void configView() {

		adapter = new MainAdapter(getSupportFragmentManager());
		pager = (ViewPager) findViewById(R.id.pager);
		pager.setAdapter(adapter);
		pager.setCurrentItem(1);

		pager.setOffscreenPageLimit(3);
		pager.setOnPageChangeListener(pageChangeListener);
		
		ImageButton setImageButton = (ImageButton)findViewById(R.id.ib_setting);
		setImageButton.setOnClickListener(setListener);
		
		titleView = (TextView)findViewById(R.id.tv_title);
		calendarTitleLayout = (LinearLayout)findViewById(R.id.ll_title_layout);
		calendarTitleView = (TextView)findViewById(R.id.tv_calendar_title);
		
		ImageButton presentButton = (ImageButton)findViewById(R.id.ib_calendar_left);
		ImageButton nextButton = (ImageButton)findViewById(R.id.ib_calendar_right);
		presentButton.setOnClickListener(calendarClickListener);
		nextButton.setOnClickListener(calendarClickListener);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		if (appstart) {
			appstart = false;
			//程序启动插入分类，从备份中恢复
			final MySharedPreference sp = new MySharedPreference(this);
			if(!sp.getKeyBoolean("appstarted")||DBOperation.isDataBaseClearByUnknow(this)){//是否数据被非法清除。如果清楚则恢复在onrestore中继续直接else中的代码
				
				new Thread(){
					public void run() {
						
						sp.setKeyBoolean("appstarted", true);
						sp.setKeyBoolean(StaticValue.SP_AUTO_BACKUP, true);

						DBOperation.insertLogoColor(MainNewActivity.this);
						DBOperation.insertCategory(MainNewActivity.this);
						
						myHandler.sendEmptyMessage(0);
					};
					
				}.start();
				
				
				
			}else{
				//旧版本升级新版本数据升级
				VersionControlUtil.updateDataToNewVersion(this);
				configView();
				
				if(loadingview!=null){
					loadingview.setVisibility(View.GONE);
				}
			}
			
			backShowKeyView = true;
			
//			loadAD();
		}
		
		
		MySharedPreference sp = new MySharedPreference(this);
		if(backShowKeyView && DBOperation.countFinance(this)!=0&&!"".equals(sp.getKeyStr(StaticValue.SP_APP_PASSWORD))){
			
			Intent intent = new Intent(this, PasswordActivity.class);
			intent.putExtra("passwordtype", StaticValue.PasswordType_CHECK);
			startActivityForResult(intent, 105);
		}
		backShowKeyView = true;
	}
	
	private Handler myHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			System.out.println("myhandler");
			BackupTask task = new BackupTask(MainNewActivity.this);
			task.setOnBackupTaskListener(MainNewActivity.this);
			task.execute(BackupTask.COMMAND_RESTORE);
		};
	};
	
	
	
	private OnClickListener setListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			
			backShowKeyView = false;
			Intent intent = new Intent(MainNewActivity.this, SettingActivity.class);
			startActivity(intent);
		}
	};

	private OnPageChangeListener pageChangeListener = new OnPageChangeListener() {

		@Override
		public void onPageSelected(int arg0) {
			
			if(arg0==0){
				titleView.setText("曲线图");
				titleView.setVisibility(View.VISIBLE);
				calendarTitleLayout.setVisibility(View.GONE);
				((FragmentReloadListener)adapter.getItem(arg0)).reload();
			}else if(arg0==2){
				titleView.setVisibility(View.GONE);
				calendarTitleLayout.setVisibility(View.VISIBLE);
				((FragmentReloadListener)adapter.getItem(arg0)).reload();
			}else{
				titleView.setText("首页");
				titleView.setVisibility(View.VISIBLE);
				calendarTitleLayout.setVisibility(View.GONE);
			}
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	};

	private IndexFragment indexFragment;
	private CalendarFragment canlendarFragment;
	private LineFragment lineFragment;

	private class MainAdapter extends FragmentPagerAdapter {

		public MainAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {

			if (position == 0) {
				if (lineFragment == null) {
					lineFragment = new LineFragment();
				}
				return lineFragment;
			} else if (position == 1) {
				if (indexFragment == null) {
					indexFragment = new IndexFragment();
				}
				return indexFragment;
			} else {
				if (canlendarFragment == null) {
					canlendarFragment = new CalendarFragment();
				}
				canlendarFragment.setOnCalendarMonthChangeListener(calendarMonthChangeListener);
				return canlendarFragment;
			}
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return "";
		}

		@Override
		public int getCount() {
			return 3;
		}
		
		private OnCalendarMonthChangeListener calendarMonthChangeListener = new OnCalendarMonthChangeListener() {
			
			@Override
			public void calendarMonthChange(String month) {
				
				if(null!=calendarTitleView){
					calendarTitleView.setText(month+"月");
				}
			}
		};
	}
	
	private OnClickListener calendarClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if(v.getId()==R.id.ib_calendar_left){
				calendarToPresent();
			}else if(v.getId()==R.id.ib_calendar_right){
				calendarToNext();
			}
		}
	};
	
	
	private void calendarToPresent(){
		canlendarFragment.calendarToPresent(null);
	}
	
	private void calendarToNext(){
		canlendarFragment.calendarToNext(null);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		System.out.println("requestcode "+requestCode);
		backShowKeyView = false;
		if(requestCode == 105){//密码页面返回
			if(resultCode==RESULT_CANCELED){
				finish();
			}
		}else{//财务记录为空，添加页面返回
			if(DBOperation.countFinance(this)==0){
				finish();
			}else{
				
				System.out.println("activityresult 101 index reload");
				((FragmentReloadListener)adapter.getItem(1)).reload();
				
//				if(DBOperation.countFinance(this)>3&&interstitialAd.isAdReady()){
//					interstitialAd.showAd(MainNewActivity.this);
//				}
			}
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		MySharedPreference sp = new MySharedPreference(this);
		if(!sp.getKeyBoolean(StaticValue.SP_AUTO_BACKUP)){
			return;
		}
		int exitCount = sp.getKeyInt(StaticValue.SP_EXIT_APP_COUNT);
		
		new BackupTask(this).execute("backupDatabase");
		if(exitCount>2){
			sp.setKeyInt(StaticValue.SP_EXIT_APP_COUNT, 0);
			
			
	        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
	        //定义通知栏展现的内容信息
	        int icon = R.drawable.logo;
	        CharSequence tickerText = "正在往SD卡中备份数据";
	        long when = System.currentTimeMillis();
	        Notification notification = new Notification(icon, tickerText, when);


	        //定义下拉通知栏时要展现的内容信息
	        Context context = getApplicationContext();
	        CharSequence contentTitle = "55记账";
	        CharSequence contentText = "已经在SD卡中备份了您的记录";
	        Intent notificationIntent = new Intent(this, MainNewActivity.class);
	        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
	        notification.setLatestEventInfo(context, contentTitle, contentText,contentIntent); 
	        //用mNotificationManager的notify方法通知用户生成标题栏消息通知
	        mNotificationManager.notify(55555, notification);
	        
		}else{
			sp.setKeyInt(StaticValue.SP_EXIT_APP_COUNT, exitCount+1);
		}
		
	}

	@Override
	public void backuped() {
		
	}

	@Override
	public void restroed() {
		System.out.println("restroed");
		//旧版本升级新版本数据升级
		VersionControlUtil.updateDataToNewVersion(this);
		configView();
		
		if(loadingview!=null){
			loadingview.setVisibility(View.GONE);
		}
	}
}