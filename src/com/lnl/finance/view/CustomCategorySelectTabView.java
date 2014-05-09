package com.lnl.finance.view;

import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.lnl.finance.R;
import com.lnl.finance.util.AppUtil;
import com.lnl.finance.util.BitmapUtil;

public class CustomCategorySelectTabView extends RelativeLayout{

	private List<Map<String, Object>> list;
	private RelativeLayout diaryTabView;
	private Activity activity;
	private int lastSelectedPosition;
	
	private OnCustomCategoryTabViewSeletedListener onTabViewSeletedListener;
	
	public void setOnCustomCategoryTabViewSeletedListener(OnCustomCategoryTabViewSeletedListener onTabViewSeletedListener){
		this.onTabViewSeletedListener = onTabViewSeletedListener;
	}
	
	public CustomCategorySelectTabView(Context context, Activity activity,List<Map<String, Object>> list) {
		super(context);
		this.list = list;
		this.activity = activity;
		initTabView();
	}
	
	private void initTabView() {
		
		LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		diaryTabView = (RelativeLayout)inflater.inflate(R.layout.view_diary_tab, null);
		
		int screenWidth = AppUtil.getSreenWidth(activity);

		System.out.println("screenwidth:"+screenWidth);
		int linenum = 5;
		int margin = 10;
		
		int x = 0;
		int y = 0;
		
		if(list==null)return;
		for (int i = 0; i < list.size(); i++) {
			
			int drawableId = getResources().getIdentifier(list.get(i).get("lc_color").toString()+"_1", "drawable", "com.lnl.finance"); 
			int color = Integer.parseInt(list.get(i).get("lc_logo").toString(), 16)+0xFF000000;
			
			ImageButton button = new ImageButton(getContext());
			button.setTag((i+1)+"");
//			button.setImageBitmap(BitmapUtil.replaceColorInImage(BitmapUtil.drawable2Bitmap(getResources().getDrawable(drawableId)), color));
			button.setImageBitmap(BitmapUtil.drawable2Bitmap(getResources().getDrawable(drawableId)));
			button.setBackgroundDrawable(null);
			button.setOnClickListener(clickListener);
			
			LayoutParams layoutParams = new LayoutParams((screenWidth-margin)/linenum, (screenWidth-margin)/linenum);
			layoutParams.leftMargin = margin/2+x*(screenWidth-margin)/linenum;
			layoutParams.topMargin = margin+y*((screenWidth-margin)/linenum);
			diaryTabView.addView(button,layoutParams);
			
			x++;
			if(x%5==0){
				y++;
				x=0;
				
				//判断是否最后，如果不是最后，决定画整排的分割线
				if(i!=list.size()-1){
					View view = new View(getContext());
					LayoutParams horDividerParams = new LayoutParams(screenWidth-margin,4);
					horDividerParams.leftMargin = margin/2;
					horDividerParams.topMargin = margin+y*((screenWidth-margin)/linenum);
					view.setBackgroundDrawable(BitmapUtil.bitmap2Drawable(BitmapUtil.createWidthRepeater(screenWidth-margin,BitmapUtil.drawable2Bitmap(getResources().getDrawable(R.drawable.diary_divider_hor)))));
					diaryTabView.addView(view, horDividerParams);
				}
			}else{
				
				//画竖的分割线
				View view = new View(getContext());
				LayoutParams verDividerParams = new LayoutParams(2, (screenWidth-margin)/linenum);
				verDividerParams.leftMargin = margin/2+x*(screenWidth-margin)/linenum;
				verDividerParams.topMargin = margin+y*((screenWidth-margin)/linenum);
				view.setBackgroundDrawable(BitmapUtil.bitmap2Drawable(BitmapUtil.createHeightRepeater(screenWidth*4/13,BitmapUtil.drawable2Bitmap(getResources().getDrawable(R.drawable.diary_divider_ver)))));
				diaryTabView.addView(view, verDividerParams);
				
			}
		}
		
		this.addView(diaryTabView);
	}
	
	private OnClickListener clickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if(lastSelectedPosition>0){
				ImageButton button = (ImageButton)((ViewGroup)v.getParent()).findViewWithTag(lastSelectedPosition+"");
				if(button!=null){
					button.setBackgroundDrawable(null);
					int drawableId1 = getResources().getIdentifier(list.get(lastSelectedPosition-1).get("lc_color").toString()+"_1", "drawable", "com.lnl.finance"); 
					button.setImageDrawable(getResources().getDrawable(drawableId1));
				}
			}
			
			
			int position = Integer.valueOf(v.getTag().toString());
			v.setBackgroundColor(Integer.parseInt(list.get(position-1).get("lc_logo").toString(), 16)+0xFF000000);
			int drawableId = getResources().getIdentifier(list.get(position-1).get("lc_color").toString(), "drawable", "com.lnl.finance"); 
			((ImageButton)v).setImageDrawable(getResources().getDrawable(drawableId));
			
			lastSelectedPosition = position;
			
			if(onTabViewSeletedListener!=null){
				onTabViewSeletedListener.tabViewSeleted(list.get(position-1).get("lc_id").toString(), list.get(position-1).get("lc_color").toString(), list.get(position-1).get("lc_logo").toString());
			}
		}
	};
	
	public interface OnCustomCategoryTabViewSeletedListener{
		public void tabViewSeleted(String id, String logo,String color); 
	}
	
}