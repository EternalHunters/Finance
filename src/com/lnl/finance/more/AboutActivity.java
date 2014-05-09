package com.lnl.finance.more;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;

import com.lnl.finance.BaseActivity;
import com.lnl.finance.R;

public class AboutActivity extends BaseActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		
		initView();
	}
	
	
	private void initView() {

		TextView versionTextView  = (TextView)findViewById(R.id.tv_version);
		
		try {
			 // 获取packagemanager的实例
	        PackageManager packageManager = getPackageManager();
	        // getPackageName()是你当前类的包名，0代表是获取版本信息
	        PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(),0);
	        String version = packInfo.versionName;

	        versionTextView.setText("版本： "+version);
	        
		} catch (Exception e) {
			e.printStackTrace();
		}
//		appTextView.setText("        做这个应用原因很简单，大多大型记账软件功能太复杂，他们基本针对大型财务类的。我们主要针对个人财务记录，方便基本查看个人每天流出的money有多少。希望能达到不记不知道，一记回头吓一跳的效果。另外重申本应用属于个人开发，无美工（我也想很漂亮的界面，但...）..当然，软件的任何问题欢迎吐槽反馈。");
        
	}
}
