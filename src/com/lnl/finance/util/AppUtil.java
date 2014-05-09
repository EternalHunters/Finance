package com.lnl.finance.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.Intent.ShortcutIconResource;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.ColorStateList;
import android.content.res.XmlResourceParser;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;

import com.lnl.finance.R;

public class AppUtil {
	
	private static float DEVICE_DENSITY = 0;
	
	/**
	 * 退出app
	 */
	public static void exitApp(){
		System.exit(0);
	}
	
	public static boolean isAppRunning(Context context){
		String packageName = getPackageName(context);
        String topActivityClassName= getTopActivityName(context);
        System.out.println("packageName="+packageName+",topActivityClassName="+topActivityClassName);
        if (packageName!=null&&topActivityClassName!=null&&topActivityClassName.startsWith(packageName)) {
            System.out.println("---> isRunningForeGround");
            return true;
        } else {
            System.out.println("---> isRunningBackGround");
            return false;
        }
	}
	
	public static String getTopActivityName(Context context){
        String topActivityClassName=null;
         ActivityManager activityManager =
        (ActivityManager)(context.getSystemService(android.content.Context.ACTIVITY_SERVICE )) ;
         List<RunningTaskInfo> runningTaskInfos = activityManager.getRunningTasks(1) ;
         if(runningTaskInfos != null){
             ComponentName f=runningTaskInfos.get(0).topActivity;
             topActivityClassName=f.getClassName();
         }
         return topActivityClassName;
    }
     
    public static String getPackageName(Context context){
         String packageName = context.getPackageName();  
         return packageName;
    }
	
	/**
	 * 获取屏幕宽度
	 * @param activity
	 * @return
	 */
	public static int getSreenWidth(Activity activity){
		String width_key = "share_prefrence_screent_width";
		MySharedPreference sp = new MySharedPreference(activity);
		if(0!=sp.getKeyInt(width_key, 0))
		{
			return sp.getKeyInt(width_key,0);
		}else {
			int screenWidth = activity.getWindowManager().getDefaultDisplay().getWidth();
			sp.setKeyInt(width_key, screenWidth);
			return screenWidth;
		}
	}
	
	/**
	 * 获取屏幕高度
	 */
	public static int getSreenHeight(Activity activity){
		String height_key = "share_prefrence_screent_height";
		MySharedPreference sp = new MySharedPreference(activity);
		if(0!=sp.getKeyInt(height_key,0))
		{
			return sp.getKeyInt(height_key,0);
		}else {
			int screenHeight = activity.getWindowManager().getDefaultDisplay().getHeight();
			sp.setKeyInt(height_key, screenHeight);
			return screenHeight;
		}
	}
	
	
	/**
	 * 判断是否是平板
	 * @param mContext
	 * @return
	 */
	public static boolean isTabletDevice(Context mContext) {
        TelephonyManager telephony = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        int type = telephony.getPhoneType();
        if (type == TelephonyManager.PHONE_TYPE_NONE) {
            return true;
        } else {
        	return false;
        }
    }
	
	/**
	 * 获取当前应用的版本号
	 * @param context
	 * @return
	 */
	public static int getVersionCode(Context context){      

		try {
	      	return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	/**
	 * 获取当前应用的版本名
	 * @param context
	 * @return
	 */
	public static String getVersionName(Context context){      

		try {
	      	return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return "1.0";
	}
	
	
	/**
	 * 匹配图片
	 */
	public static String fitImage(Context context,String imageUrl, int needWidth, int needHeight){
		if(imageUrl==null||"".equals(imageUrl))return "";
		
		String finalImgUrlStr=imageUrl;
		finalImgUrlStr = imageUrl+"."+needWidth+"x"+needHeight+".jpg";
		
		return finalImgUrlStr;
	}
	
	/**
	 * 获取手机联网状态
	 * @param context
	 * @return
	 */
	public static String getNetState(Context context) {
		
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		ConnectivityManager mConnectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = mConnectivity.getActiveNetworkInfo();
		if (info == null || !mConnectivity.getBackgroundDataSetting()) {
			return null;
		}
		if (info.isConnected()) {
			int netType = info.getType();
			int netSubtype = info.getSubtype();

			if (netType == ConnectivityManager.TYPE_WIFI) {
				return "WIFI";
			} else if (netType == ConnectivityManager.TYPE_MOBILE && netSubtype == TelephonyManager.NETWORK_TYPE_UMTS && !tm.isNetworkRoaming()) {
				return "3G";
			} else if (netType == ConnectivityManager.TYPE_MOBILE) {
				return "GPRS";
			} else {
				return "未知";
			}
		} else {
			return null;
		}
	}
	
	public static void getDensity(Activity activity){
		if(DEVICE_DENSITY==0)
		{
			DisplayMetrics metrics = new DisplayMetrics(); 
	        float density = metrics.density;
	        
	        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
	        DEVICE_DENSITY = metrics.density;
		}
	}
	
	 /** 
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素) 
     */  
    public static int dip2px(Activity activity, double dpValue) {  
    	getDensity(activity);
    	return (int) (dpValue * DEVICE_DENSITY);
    }  
  
    /** 
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp 
     */  
    public static int px2dip(Activity activity, double pxValue) {  
    	getDensity(activity); 
        return (int) (pxValue / DEVICE_DENSITY);  
    }  
    
    /**
     * 将px值转换为sp值，保证文字大小不变
     * 
     * @param pxValue
     * @param fontScale（DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int px2sp(Activity activity, float pxValue) {
    	getDensity(activity); 
    	return (int) (pxValue / DEVICE_DENSITY);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     * 
     * @param spValue
     * @param fontScale（DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int sp2px(Activity activity, float spValue) {
    	getDensity(activity); 
    	return (int) (spValue * DEVICE_DENSITY);
   }
    
    
    public static boolean hasShortcut(Context context)
    {
        boolean isInstallShortcut = false;
        final ContentResolver cr = context.getContentResolver();
        final String AUTHORITY ="com.android.launcher.settings";
        final Uri CONTENT_URI = Uri.parse("content://" +AUTHORITY + "/favorites?notify=true");
        Cursor c = cr.query(CONTENT_URI,new String[] {"title","iconResource" },"title=?",
        new String[] {context.getResources().getString(R.string.app_name).trim()}, null);
        if(c!=null && c.getCount()>0){
            isInstallShortcut = true ;
        }
        return isInstallShortcut ;
    }
    
    /** 
     * 为程序创建桌面快捷方式 
     */ 
    public static void addShortcut(Context context){  
    	if(hasShortcut(context))return;
        Intent shortcut = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");  
               
        //快捷方式的名称  
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, context.getResources().getString(R.string.app_name));  
        shortcut.putExtra("duplicate", false); //不允许重复创建  
 
        /****************************此方法已失效*************************/
        //ComponentName comp = new ComponentName(this.getPackageName(), "."+this.getLocalClassName());  
        //shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(Intent.ACTION_MAIN).setComponent(comp));  　　
        Intent shortcutIntent = new Intent(Intent.ACTION_MAIN);
        shortcutIntent.setClassName(context, context.getClass().getName());
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
 
        //快捷方式的图标  
        ShortcutIconResource iconRes = Intent.ShortcutIconResource.fromContext(context, R.drawable.ic_launcher);  
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconRes);  
               
        context.sendBroadcast(shortcut);  
    }  
    
    /**
     * 时间转换 date转string
     * @param date
     * @param formatter
     * @return
     */
    public static String timeFormat(Date date,String formatter){
    	SimpleDateFormat dateformat = new SimpleDateFormat(formatter,Locale.CHINA);   
        String formattime = dateformat.format(date);
        return formattime;
    }
    
    /**
     * 从xml获取动态color
     * @param context
     * @param colorid
     * @return
     */
    public static ColorStateList getXMLColor(Context context,int colorid){
    	XmlResourceParser xrp = context.getResources().getXml(colorid);  
    	try {  
    	    ColorStateList csl = ColorStateList.createFromXml(context.getResources(), xrp);  
			return csl; 
    	} catch (Exception e) {  
    	} 
    	return null;
    }
}
