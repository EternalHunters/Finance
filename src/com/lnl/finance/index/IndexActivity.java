package com.lnl.finance.index;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

import com.lnl.finance.R;
import com.lnl.finance.more.SettingActivity;

public class IndexActivity  extends Activity{
	
	private ImageButton listTabButton;
	private ImageButton planTabButton;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_index);
		
		initView();
	}
	
	private void initView() {
		
		listTabButton = (ImageButton)findViewById(R.id.ib_tab_list);
		planTabButton = (ImageButton)findViewById(R.id.ib_tab_plan);
		
		listTabButton.setEnabled(false);
		listTabButton.setTag(1);
		planTabButton.setTag(2);
		
		listTabButton.setOnClickListener(clickListener);
		planTabButton.setOnClickListener(clickListener);
	}
	
	private OnClickListener  clickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			
			if(Integer.valueOf(v.getTag().toString())==1){
				listTabButton.setEnabled(false);
				planTabButton.setEnabled(true);
			}else{
				listTabButton.setEnabled(true);
				planTabButton.setEnabled(false);
			}
		}
	};
	
	public void toSettingView(View view){
		Intent intent = new Intent(this, SettingActivity.class);
		startActivity(intent);
	}
}
