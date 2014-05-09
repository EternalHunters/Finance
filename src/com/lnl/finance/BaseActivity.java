package com.lnl.finance;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;

public class BaseActivity extends Activity{

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	
	
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
	
	public void back(View view) {
		finish();
	}
	
	public void showToast(String content) {
		if(content!=null&&!"".equals(content)){

			Toast.makeText(this, content, Toast.LENGTH_SHORT).show();
		}
	}
}
