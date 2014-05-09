package com.lnl.finance.util;

import java.util.Date;

import android.R.integer;
import android.content.Context;
import android.content.SharedPreferences;

public class VersionControlUtil {

	/**
	 * from v2
	 */
	public static void updateDataToNewVersion(Context context){
		
		MySharedPreference sp = new MySharedPreference(context);
		
		int oldVersionCode = sp.getKeyInt(StaticValue.SP_APP_OLD_VERSION_CODE, 0);
		
		if(oldVersionCode==0){
			sp.setKeyInt(StaticValue.SP_APP_OLD_VERSION_CODE, AppUtil.getVersionCode(context));
			DBOperation.insertCategory(context, "娱乐", new Date().getTime(), 1, "fa6b44", "cat_entern",2,true);
			DBOperation.insertCategory(context, "学习教育", new Date().getTime(), 1, "abe9bd", "cat_school",2,true);
		}
	}
}
