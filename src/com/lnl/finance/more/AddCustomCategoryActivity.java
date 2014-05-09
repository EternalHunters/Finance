package com.lnl.finance.more;

import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.lnl.finance.BaseFragmentActivity;
import com.lnl.finance.R;
import com.lnl.finance.index.AddDetailActivity;
import com.lnl.finance.util.DBOperation;
import com.lnl.finance.view.CustomCategorySelectTabView;
import com.lnl.finance.view.CustomCategorySelectTabView.OnCustomCategoryTabViewSeletedListener;

public class AddCustomCategoryActivity extends BaseFragmentActivity implements OnCustomCategoryTabViewSeletedListener{
	
	private CustomCategorySelectTabView tabView;
	
	
	private int type = 2;
	private TextView typeTextView;
	
	private String selectLogo = null;
	private String selectColor = null;
	
	private EditText categoryNameEditText;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_add_custom_category);
		
		initView();
	}
	
	private void initView() {
		
		LinearLayout typeLayout = (LinearLayout)findViewById(R.id.ll_type);
		typeLayout.setOnClickListener(typeClickListener);
		typeTextView = (TextView)findViewById(R.id.tv_category_type);
		setTypeView();
		
		categoryNameEditText = (EditText)findViewById(R.id.et_category_name);
		
		Button saveButton = (Button)findViewById(R.id.btn_save_category);
		saveButton.setOnClickListener(saveClickListener);
		
		List<Map<String, Object>> list = DBOperation.findLogoColor(this);
		
		RelativeLayout diaryTabLayout = (RelativeLayout)findViewById(R.id.rl_finance_category);
		tabView = new CustomCategorySelectTabView(this, this, list);
		tabView.setOnCustomCategoryTabViewSeletedListener(this);
		diaryTabLayout.addView(tabView, new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT,android.view.ViewGroup.LayoutParams.MATCH_PARENT));
	}
	
	
	private void setTypeView() {
		if(type==2){
			typeTextView.setText("支出 >");
		}else{
			typeTextView.setText("收入 >");
		}
	}
	
	private OnClickListener saveClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			saveAction(v);
		}
	};
	
	private OnClickListener typeClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if(type==1){
				type=2;
			}else{
				type = 1;
			}
			setTypeView();
		}
	};
	
	public void saveAction(View view) {
		String categoryName = categoryNameEditText.getText().toString();
		if(categoryName==null || "".equals(categoryName)){
			showAppMsg("请先输入分类名", STYLE_CONFIRM);
			return;
		}
		
		if(null==selectLogo||null==selectColor){
			showAppMsg("请先选择分类图标", STYLE_CONFIRM);
			return;
		}
		if(DBOperation.checkCategoryNameIsExsit(this, categoryName)){
			showAppMsg("该分类名已经存在，请更改分类名", STYLE_CONFIRM);
			return;
		}
		
		if(DBOperation.saveCustomCategory(this, categoryName, selectLogo, selectColor, type)){
			
			if("addDetailActivity".equals(getIntent().getStringExtra("from"))){
				Intent intent = new Intent(this, AddDetailActivity.class);
				setResult(RESULT_OK, intent);
			}else{
				Intent intent = new Intent(this, ManagerCustomCategoryActivity.class);
				setResult(RESULT_OK, intent);
			}
			
			finish();
		}else{
			showAppMsg("添加分类失败，请重试", STYLE_INFO);
		}
		
	}

	@Override
	public void tabViewSeleted(String id, String logo, String color) {
		
		selectLogo = logo;
		selectColor = color;
	}
}
