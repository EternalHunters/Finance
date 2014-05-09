package com.lnl.finance.more;

import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.ScrollView;

import com.lnl.finance.BaseFragmentActivity;
import com.lnl.finance.R;
import com.lnl.finance.util.DBOperation;
import com.lnl.finance.view.ManagerCustomCategoryTabView;
import com.lnl.finance.view.ManagerCustomCategoryTabView.OnManagerCustomCategoryTabViewListener;

public class ManagerCustomCategoryActivity extends BaseFragmentActivity implements OnManagerCustomCategoryTabViewListener{

	private ManagerCustomCategoryTabView tabView;
	
	private enum CategoryViewMode {Edit, Done};
	private CategoryViewMode mode = CategoryViewMode.Done;
	
	private Button editButton;
	private Button addButton;
	private ScrollView scrollView;
	
	private LinearLayout emptyLayout;
	
	private boolean customCategoryEmpty = true;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		
		setContentView(R.layout.activity_manager_custom_category);
		
		initView();
	}
	private void initView() {

		editButton = (Button)findViewById(R.id.btn_edit);
		addButton = (Button)findViewById(R.id.btn_add_category);
		
		scrollView = (ScrollView)findViewById(R.id.sv_scrollview);
		
		emptyLayout = (LinearLayout)findViewById(R.id.ll_empty_view);
		emptyLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ManagerCustomCategoryActivity.this,  AddCustomCategoryActivity.class);
				startActivityForResult(intent, 111);
			}
		});
		
		RelativeLayout diaryTabLayout = (RelativeLayout)findViewById(R.id.rl_finance_category);
		tabView = new ManagerCustomCategoryTabView(this, this, this);
		tabView.setOnManagerCustomCategoryTabViewListener(this);
		diaryTabLayout.addView(tabView, new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT,android.view.ViewGroup.LayoutParams.MATCH_PARENT));
		
//		loadAD();
	}
	
	public void addCategoryAction(View view){
		
		if(mode == CategoryViewMode.Edit){
			deleteAction();
		}else if(mode == CategoryViewMode.Done){
			Intent intent = new Intent(this,  AddCustomCategoryActivity.class);
			startActivityForResult(intent, 111);
		}
		
	}
	
	public void editAction(View view){
		
		if(mode == CategoryViewMode.Edit){
			mode = CategoryViewMode.Done;
			tabView.setEditable(false);
			tabView.clearSelected();
		}else if(mode == CategoryViewMode.Done){
			mode = CategoryViewMode.Edit;
			tabView.setEditable(true);
			tabView.clearSelected();
			showAppMsg("编辑模式下你可以选择分类后点击删除该分类", STYLE_INFO);
		}
	}
	
	
	private void deleteAction(){
		
		List<Map<String, Object>> selectList = tabView.getSelectCategoryMap();
		
		int count = selectList.size();
		if(count==0){
			showAppMsg("请先选择要删除的分类，再点击删除", STYLE_CONFIRM);
			return;
		}
		
		StringBuffer categoryNames = new StringBuffer();
		final StringBuffer idsString = new StringBuffer();
		idsString.append("(");
		int i = 0;
		
		for (Map<String, Object> map : selectList) {
			categoryNames.append(map.get("c_name").toString());
			idsString.append(map.get("c_id").toString());
			if(i!=count-1){
				categoryNames.append(",");
				idsString.append(",");
			}
			i++;
		}
		idsString.append(")");
		new AlertDialog.Builder(this).setTitle("提示").setMessage("删除自定义的“"+categoryNames+"”分类？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				DBOperation.deleteCustomCategory(ManagerCustomCategoryActivity.this, idsString.toString());
				showAppMsg("已删除", STYLE_INFO);
				tabView.reloadTabView();
			}
		}).setNegativeButton("取消", null).create().show();
		
	}
	
	private void setStateView() {
		System.out.println("setstateview");
		if(customCategoryEmpty){
			 
			mode = CategoryViewMode.Done;
			
			System.out.println("customCategoryEmpty");
			editButton.setText(getResources().getString(R.string.edit));
			addButton.setText(getResources().getString(R.string.addcategory));
			editButton.setVisibility(View.INVISIBLE);
			scrollView.setVisibility(View.GONE);
			emptyLayout.setVisibility(View.VISIBLE);
		}else if(mode == CategoryViewMode.Done){
			editButton.setVisibility(View.VISIBLE);
			editButton.setText(getResources().getString(R.string.edit));
			addButton.setText(getResources().getString(R.string.addcategory));
			
			emptyLayout.setVisibility(View.GONE);
			scrollView.setVisibility(View.VISIBLE);
		}else if(mode == CategoryViewMode.Edit){
			editButton.setVisibility(View.VISIBLE);
			editButton.setText(getResources().getString(R.string.done));
			addButton.setText(getResources().getString(R.string.delcategory));
			
			emptyLayout.setVisibility(View.GONE);
			scrollView.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void categoryLoaded(boolean isCategoryEmpty) {
		System.out.println("categoryLoaded");
		customCategoryEmpty = isCategoryEmpty;
		
		setStateView();
	}
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		if(keyCode==KeyEvent.KEYCODE_BACK){
			if(mode == CategoryViewMode.Edit){
				mode = CategoryViewMode.Done;
				tabView.setEditable(false);
				tabView.clearSelected();
				
				return false;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	
	
//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		super.onActivityResult(requestCode, resultCode, data);
//		
//		if(requestCode == 111){//密码页面返回
//			if(resultCode!=RESULT_CANCELED){
//				
//				tabView.reloadTabView();
//				interstitialAd.showAd(ManagerCustomCategoryActivity.this);
//			}
//		}
//	}
//	
//	private InterstitialAd interstitialAd;
//	private void loadAD(){
//		interstitialAd = new InterstitialAd(this);
//		interstitialAd.setListener(new InterstitialAdListener() {
//			
//			@Override
//			public void onAdReady() {
//			}
//			
//			@Override
//			public void onAdPresent() {
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
//			}
//		});
//		interstitialAd.loadAd();
//	}
}
