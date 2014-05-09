package com.lnl.finance.view;

import java.security.PublicKey;
import java.util.List;
import java.util.Map;

import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.lnl.finance.R;
import com.lnl.finance.util.AppUtil;
import com.lnl.finance.util.BitmapUtil;

public class CategoryTabView extends RelativeLayout{

	private List<Map<String, Object>> list;
	private RelativeLayout diaryTabView;
	private Activity activity;
	private int lastSelectedPosition;
	
	private OnTabViewSeletedListener onTabViewSeletedListener;
	
	public void setOnTabViewSeletedListener(OnTabViewSeletedListener onTabViewSeletedListener){
		this.onTabViewSeletedListener = onTabViewSeletedListener;
	}
	
	public CategoryTabView(Context context, Activity activity,List<Map<String, Object>> list) {
		super(context);
		this.list = list;
		this.activity = activity;
		loadView();
	}
	
	public void reload(List<Map<String, Object>> list){
		this.list = list;
		
		initTabView();
	}
	
	private void loadView(){
		
		LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		diaryTabView = (RelativeLayout)inflater.inflate(R.layout.view_diary_tab, null);
		
		this.addView(diaryTabView);
		initTabView();
		
	}
	
	private void initTabView() {
		
		if(diaryTabView!=null){
			diaryTabView.removeAllViews();
		}
		
		
		int screenWidth = AppUtil.getSreenWidth(activity);

		int linenum = 3;
		int margin = 10;
		
		int x = 0;
		int y = 0;
		
		if(list==null)return;
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
		
	}
	
	private OnClickListener clickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if(lastSelectedPosition>0){
				Button button = (Button)((ViewGroup)v.getParent()).findViewWithTag(lastSelectedPosition+"");
				if(button!=null){
					button.setBackgroundDrawable(null);
					int drawableId1 = getResources().getIdentifier(list.get(lastSelectedPosition-1).get("c_logo").toString()+"_1", "drawable", "com.lnl.finance"); 
					button.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(drawableId1), null, null);
					button.setTextColor(Integer.parseInt(list.get(lastSelectedPosition-1).get("c_color").toString(), 16)+0xFF000000);
				}
			}
			
			
			int position = Integer.valueOf(v.getTag().toString());
			v.setBackgroundColor(Integer.parseInt(list.get(position-1).get("c_color").toString(), 16)+0xFF000000);
			int drawableId = getResources().getIdentifier(list.get(position-1).get("c_logo").toString(), "drawable", "com.lnl.finance"); 
			((Button)v).setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(drawableId), null, null);
			((Button)v).setTextColor(Color.WHITE);
			
			lastSelectedPosition = position;
			
			if(onTabViewSeletedListener!=null){
				onTabViewSeletedListener.tabViewSeleted(list.get(position-1).get("c_id").toString(), list.get(position-1).get("c_name").toString(), list.get(position-1).get("c_logo").toString(), list.get(position-1).get("c_color").toString());
			}
		}
	};
	
	public interface OnTabViewSeletedListener{
		
		public void tabViewSeleted(String id, String name, String logo,String color); 
	}
	
}
