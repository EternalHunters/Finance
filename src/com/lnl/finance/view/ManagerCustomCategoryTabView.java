package com.lnl.finance.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.lnl.finance.R;
import com.lnl.finance.util.AppUtil;
import com.lnl.finance.util.BitmapUtil;
import com.lnl.finance.util.DBOperation;

public class ManagerCustomCategoryTabView extends RelativeLayout{

	private List<Map<String, Object>> list;
	private RelativeLayout diaryTabView;
	private Activity activity;
	
	private boolean editable = false;
	private Map<String,Map<String, Object>> selectCategoryMap = new HashMap<String, Map<String,Object>>();
	
	private OnManagerCustomCategoryTabViewListener onManagerCustomCategoryTabViewListener;
	
	public void setOnManagerCustomCategoryTabViewListener(OnManagerCustomCategoryTabViewListener onManagerCustomCategoryTabViewListener){
		this.onManagerCustomCategoryTabViewListener = onManagerCustomCategoryTabViewListener;
	}
	public interface OnManagerCustomCategoryTabViewListener{
		
		public void categoryLoaded(boolean isCategoryEmpty);
	}
	
	public void setEditable(boolean _editable){
		this.editable = _editable;
	}
	
	public ManagerCustomCategoryTabView(Context context, Activity activity, OnManagerCustomCategoryTabViewListener onManagerCustomCategoryTabViewListener) {
		super(context);
		this.activity = activity;
		this.onManagerCustomCategoryTabViewListener = onManagerCustomCategoryTabViewListener;
		initTabView();
	}
	
	private void initTabView() {
		
		LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		diaryTabView = (RelativeLayout)inflater.inflate(R.layout.view_diary_tab, null);
		
		loadButtonTabView();
		this.addView(diaryTabView);
	}
	
	public List<Map<String, Object>> getSelectCategoryMap(){
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		for (String key : selectCategoryMap.keySet()) {
			list.add(selectCategoryMap.get(key));
		}
		return list;
	}
	
	private void loadButtonTabView(){
		
		diaryTabView.removeAllViews();
		list = DBOperation.findOutCustomCategory(activity);
		
		if(list==null||list.size()==0){
			if(null!=onManagerCustomCategoryTabViewListener){
				onManagerCustomCategoryTabViewListener.categoryLoaded(true);
			}
			return;
		}
		
		int screenWidth = AppUtil.getSreenWidth(activity);

		int linenum = 3;
		int margin = 10;
		
		int x = 0;
		int y = 0;
		for (int i = 0; i < list.size(); i++) {
			
			int drawableId = getResources().getIdentifier(list.get(i).get("c_logo").toString()+"_1", "drawable", "com.lnl.finance"); 
			String name = list.get(i).get("c_name").toString();
			
			Button button = new Button(getContext());
			button.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(drawableId), null, null);
			button.setPadding(0, AppUtil.dip2px(activity, 20), 0, 0);
			button.setText(name);
			button.setTextSize(15);
			button.setTag((i+1)+"");
			button.setTextColor(Integer.parseInt(list.get(i).get("c_color").toString(), 16)+0xFF000000);
			button.setBackgroundDrawable(null);
			button.setOnClickListener(clickListener);
			
			LayoutParams layoutParams = new LayoutParams((screenWidth-margin)/linenum, screenWidth*4/13);
			layoutParams.leftMargin = margin/2+x*(screenWidth-margin)/linenum;
			layoutParams.topMargin = margin+y*(screenWidth*4/13);
			diaryTabView.addView(button,layoutParams);
			
			x++;
			if(x%3==0){
				y++;
				x=0;
				
				//判断是否最后，如果不是最后，决定画整排的分割线
				if(i!=list.size()-1){
					View view = new View(getContext());
					LayoutParams horDividerParams = new LayoutParams(screenWidth-margin,4);
					horDividerParams.leftMargin = margin/2;
					horDividerParams.topMargin = margin+y*(screenWidth*4/13);
					view.setBackgroundDrawable(BitmapUtil.bitmap2Drawable(BitmapUtil.createWidthRepeater(screenWidth-margin,BitmapUtil.drawable2Bitmap(getResources().getDrawable(R.drawable.diary_divider_hor)))));
					diaryTabView.addView(view, horDividerParams);
				}
			}else{
				
				//画竖的分割线
				View view = new View(getContext());
				LayoutParams verDividerParams = new LayoutParams(2, screenWidth*4/13);
				verDividerParams.leftMargin = margin/2+x*(screenWidth-margin)/linenum;
				verDividerParams.topMargin = margin+y*(screenWidth*4/13);
				view.setBackgroundDrawable(BitmapUtil.bitmap2Drawable(BitmapUtil.createHeightRepeater(screenWidth*4/13,BitmapUtil.drawable2Bitmap(getResources().getDrawable(R.drawable.diary_divider_ver)))));
				diaryTabView.addView(view, verDividerParams);
			}
		}
		if(null!=onManagerCustomCategoryTabViewListener){
			onManagerCustomCategoryTabViewListener.categoryLoaded(false);
		}
	}
	
	private OnClickListener clickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			
			if(!editable)return;

			int position = Integer.valueOf(v.getTag().toString());
			if(selectCategoryMap.containsKey(list.get(position-1).get("c_id").toString())){
				selectCategoryMap.remove(list.get(position-1).get("c_id").toString());
				
				v.setBackgroundColor(Color.TRANSPARENT);
				int drawableId = getResources().getIdentifier(list.get(position-1).get("c_logo").toString()+"_1", "drawable", "com.lnl.finance"); 
				((Button)v).setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(drawableId), null, null);
				((Button)v).setTextColor(Integer.parseInt(list.get(position-1).get("c_color").toString(), 16)+0xFF000000);
				
			}else{
				selectCategoryMap.put(list.get(position-1).get("c_id").toString(),list.get(position-1));
				
				v.setBackgroundColor(Integer.parseInt(list.get(position-1).get("c_color").toString(), 16)+0xFF000000);
				int drawableId = getResources().getIdentifier(list.get(position-1).get("c_logo").toString(), "drawable", "com.lnl.finance"); 
				((Button)v).setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(drawableId), null, null);
				((Button)v).setTextColor(Color.WHITE);
			}
		}
	};
	
	public void clearSelected(){
		
		selectCategoryMap.clear();
		loadButtonTabView();
	}
	
	public void reloadTabView(){
		
		selectCategoryMap.clear();
		loadButtonTabView();
	}
}