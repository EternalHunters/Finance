package com.lnl.finance.index;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.lnl.finance.BaseActivity;
import com.lnl.finance.MainActivity;
import com.lnl.finance.MainNewActivity;
import com.lnl.finance.R;
import com.lnl.finance.util.AppUtil;

public class AddActivity extends BaseActivity{

	private TextView numberTextView;
	private TextView unitTextView;
	
	private TextView tabOut;
	private TextView tabIn;
	
	private int tabType = 2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add);
		
		initView();
	}
	
	private void initView() {
		numberTextView = (TextView)findViewById(R.id.tv_number_content);
		numberTextView.setTypeface(Typeface.createFromAsset(getAssets(), "comic sans ms.ttf"));
		unitTextView = (TextView)findViewById(R.id.tv_unit);
		
		tabOut = (TextView)findViewById(R.id.btn_tab_out);
		tabIn = (TextView)findViewById(R.id.btn_tab_in);
		tabOut.setTag(2);
		tabIn.setTag(1);
		tabOut.setOnClickListener(clickListener);
		tabIn.setOnClickListener(clickListener);
	}
	
	public void keyboardAction(View view){
		System.out.println("aaaaaaa");
		
		int screenWidth = AppUtil.getSreenWidth(this);
		System.out.println("screenwidth :"+screenWidth+"  numberTextView:"+numberTextView.getWidth()+" unitTextView.getWidth()"+unitTextView.getWidth());
		
		String numberStr = numberTextView.getText().toString();
		String addStr = view.getTag().toString();
		System.out.println("addStr :"+addStr);
		//删除
		if("del".equals(addStr)){
			System.out.println("del");
			if(!"0".equals(numberStr)&&numberStr.length()>1){
				numberStr = numberStr.substring(0, numberStr.length()-1);
			}else{
				numberStr = "0";
			}
		}else if(screenWidth-50>numberTextView.getWidth()+unitTextView.getWidth()){
			
			System.out.println("else if");
			if(".".equals(addStr)){
				System.out.println("else ...");
				if(numberStr.indexOf(".")!=-1){
					return;
				}
				numberStr = numberStr+addStr;
			}else{
				System.out.println("else ... else...");
				//判断是否已经到小数点两位
				if(numberStr.indexOf(".")!=-1){
					String[] numberStrs = numberStr.split("\\.");
					if(numberStrs.length>1&&numberStrs[1].length()>1){
						System.out.println("return");
						return;
					}
				}
				System.out.println("return");
				
				if("0".equals(numberStr)){
					numberStr = "";
				}
				numberStr = numberStr+addStr;
			}
		}
		System.out.println("number :"+numberStr);
		numberTextView.setText(numberStr);
	}
	
	private OnClickListener clickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			tabType = Integer.valueOf(v.getTag().toString());
			if(tabType==2){
				tabOut.setEnabled(false);
				tabIn.setEnabled(true);
				numberTextView.setTextColor(Color.rgb(223, 92, 92));
				unitTextView.setTextColor(Color.rgb(223, 92, 92));
				
			}else{
				tabOut.setEnabled(true);
				tabIn.setEnabled(false);
				numberTextView.setTextColor(Color.rgb(37, 175, 63));
				unitTextView.setTextColor(Color.rgb(37, 175, 63));
			}
		}
	};
	
	public void toDetailView(View view){
		
		String moneyString = numberTextView.getText().toString();
		if(!"0".equals(moneyString)){
			Intent intent = new Intent(this, AddDetailActivity.class);
			intent.putExtra("tabType", tabType);
			intent.putExtra("money", moneyString);
			startActivityForResult(intent, 103);
		}else{
			showToast("请先输入");
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		 if(resultCode!=RESULT_CANCELED){
			 
			 Intent intent = new Intent(AddActivity.this, MainNewActivity.class);
			 setResult(RESULT_OK, intent);
			 finish();
		 }
	}
}
