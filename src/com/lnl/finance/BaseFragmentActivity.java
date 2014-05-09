package com.lnl.finance;

import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH;
import static android.view.Gravity.BOTTOM;
import static com.devspark.appmsg.AppMsg.LENGTH_SHORT;
import static com.devspark.appmsg.AppMsg.LENGTH_STICKY;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.devspark.appmsg.AppMsg;
import com.umeng.analytics.MobclickAgent;

public class BaseFragmentActivity extends FragmentActivity{

	protected static final int STYLE_ALERT = 0;
	protected static final int STYLE_CONFIRM = 1;
	protected static final int STYLE_INFO = 2;
	protected static final int STYLE_CUSTOM = 3;
	protected static final int STYLE_STICKY = 4;
	
	
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}
	
	public void back(View view) {
		finish();
	}
	
	public void showToast(String content) {
		if(content!=null&&!"".equals(content)){

			Toast.makeText(this, content, Toast.LENGTH_SHORT).show();
		}
	}
	
	 @Override
	    protected void onPause() {
	        super.onPause();
	        MobclickAgent.onPause(this);
	        // This is optional for 14+,
	        // also you may want to call it at your later convenience, e.g. onDestroy
	        if (SDK_INT < ICE_CREAM_SANDWICH) {
	            AppMsg.cancelAll(this);
	        }
	    }
	
	public void showAppMsg(String msg, int styleSelected) {
		showAppMsg(msg, styleSelected, AppMsg.PRIORITY_HIGH, true, false, null);
	}
	
	public void showAppMsg(String msg, int styleSelected,int priority) {
		showAppMsg(msg, styleSelected, priority, true, false, null);
	}
	
	 public void showAppMsg(String msg, int styleSelected,int priority,boolean showInBottom,boolean showInCustomParent,ViewGroup customParent) {
	        
	        final AppMsg.Style style;
	        boolean customAnimations = false;
	        AppMsg provided = null;
	        switch (styleSelected) {
	            case 0:
	                style = AppMsg.STYLE_ALERT;
	                break;
	            case 1:
	                style = AppMsg.STYLE_CONFIRM;
	                break;
	            case 3:
	                style = new AppMsg.Style(LENGTH_SHORT, R.color.custom);
	                customAnimations = true;
	                break;
	            case 4:
	                style = new AppMsg.Style(LENGTH_STICKY, R.color.sticky);
	                provided = AppMsg.makeText(this, msg, style, R.layout.sticky);
	                provided.getView()
	                        .findViewById(R.id.remove_btn)
	                        .setOnClickListener(new CancelAppMsg(provided));
	                break;
	            default:
	                style = AppMsg.STYLE_INFO;
	                break;
	        }
	        // create {@link AppMsg} with specify type
	        AppMsg appMsg = provided != null ? provided : AppMsg.makeText(this, msg, style);
	        appMsg.setPriority(priority);
	        if (showInCustomParent) {
	            appMsg.setParent(customParent);
	        } else {
	            if (showInBottom) {
	                appMsg.setLayoutGravity(BOTTOM);
	            }
	        }

	        if (customAnimations) {
	            appMsg.setAnimation(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
	        }
	        appMsg.show();
	    }
	 
	 static class CancelAppMsg implements View.OnClickListener {
	        private final AppMsg mAppMsg;

	        CancelAppMsg(AppMsg appMsg) {
	            mAppMsg = appMsg;
	        }

	        @Override
	        public void onClick(View v) {
	            mAppMsg.cancel();
	        }
	    }

}
