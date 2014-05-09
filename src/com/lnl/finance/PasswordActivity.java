package com.lnl.finance;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.lnl.finance.more.SettingActivity;
import com.lnl.finance.util.MySharedPreference;
import com.lnl.finance.util.StaticValue;
import com.lnl.finance.widget.locuspasswordview.LocusPassWordView;
import com.lnl.finance.widget.locuspasswordview.LocusPassWordView.OnCompleteListener;

public class PasswordActivity extends BaseFragmentActivity{

	private LocusPassWordView lpwv;
	
	private TextView titleView;
	private int type;
	
	private String setPasswordString = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_password);
		
		initView();
	}
	
	public void back(View view) {
		
		if(type==StaticValue.PasswordType_SECONDSET){
			type=StaticValue.PasswordType_SET;
			
			lpwv.clearPassword();
			setTitleString();
			setPasswordString = null;
		}else{
			finish();
		}
	}
	
	private void initView() {
		
		type = getIntent().getIntExtra("passwordtype", StaticValue.PasswordType_SET);
		
		titleView = (TextView)findViewById(R.id.tv_title);
		setTitleString();
		
		if(type==StaticValue.PasswordType_CHECK){
			Button backButton = (Button)findViewById(R.id.btn_back);
			backButton.setVisibility(View.INVISIBLE);
		}
		
		
		lpwv = (LocusPassWordView) this.findViewById(R.id.mLocusPassWordView);
		lpwv.setOnCompleteListener(new OnCompleteListener() {
			@Override
			public void onComplete(String mPassword) {
				
				System.out.println(mPassword);
				
				if(type==StaticValue.PasswordType_SET){
					setPasswordString = mPassword;
					lpwv.clearPassword();
					
					showAppMsg("再输一次刚才的密码", STYLE_CONFIRM);
					type = StaticValue.PasswordType_SECONDSET;
					setTitleString();
				}else if(type==StaticValue.PasswordType_SECONDSET){
					
					if(setPasswordString.equals(mPassword)){
						MySharedPreference sp = new MySharedPreference(PasswordActivity.this);
						sp.setKeyStr(StaticValue.SP_APP_PASSWORD, mPassword);
						
						Intent intent = new Intent(PasswordActivity.this, SettingActivity.class);
						setResult(RESULT_OK, intent);
						finish();
					}else{
						lpwv.clearPassword();
						showAppMsg("两次输入的密码不一致，请重试", STYLE_INFO);
					}
				}else if(type==StaticValue.PasswordType_CHECK){
					MySharedPreference sp = new MySharedPreference(PasswordActivity.this);
					String key = sp.getKeyStr(StaticValue.SP_APP_PASSWORD);
					if(key.equals(mPassword)){
						Intent intent = new Intent(PasswordActivity.this, MainNewActivity.class);
						setResult(RESULT_OK,intent);
						finish();
					}else{
						lpwv.clearPassword();
						showAppMsg("密码不正确，请重试", STYLE_INFO);
					}
				}else if(type==StaticValue.PasswordType_CLEAR){
					
					MySharedPreference sp = new MySharedPreference(PasswordActivity.this);
					String key = sp.getKeyStr(StaticValue.SP_APP_PASSWORD);
					if(key.equals(mPassword)){
						
						sp.removeKey(StaticValue.SP_APP_PASSWORD);
						
						Intent intent = new Intent(PasswordActivity.this, SettingActivity.class);
						setResult(RESULT_OK, intent);
						finish();
					}
					else{
						lpwv.clearPassword();
						showAppMsg("密码不正确，请重试", STYLE_INFO);
					}
				}
				
			}
		});
	}
	
	private void setTitleString() {
		
		if(type==StaticValue.PasswordType_SET){
			titleView.setText(getResources().getString(R.string.setpassword));
		}else if(type==StaticValue.PasswordType_CHECK){
			titleView.setText(getResources().getString(R.string.inputpassword));
		}else if(type==StaticValue.PasswordType_CLEAR){
			titleView.setText(getResources().getString(R.string.inputoldpassword));
		}else if(type==StaticValue.PasswordType_SECONDSET){
			titleView.setText(getResources().getString(R.string.reinputpassword));
		}
	}
}
