package com.lnl.finance;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.hb.views.PinnedSectionListView;
import com.hb.views.PinnedSectionListView.PinnedSectionListAdapter;
import com.lnl.finance.bean.Item;
import com.lnl.finance.dialog.FinanceModifyDialog;
import com.lnl.finance.dialog.FinanceModifyDialog.OnFinanceModifyListener;
import com.lnl.finance.index.AddActivity;
import com.lnl.finance.listener.FragmentReloadListener;
import com.lnl.finance.util.BitmapUtil;
import com.lnl.finance.util.DBOperation;
import com.lnl.finance.util.TimeUtil;
import com.lnl.finance.widget.CircleImageView;

public class IndexFragment extends BaseFragment implements FragmentReloadListener{
	
	
	private FinanceIndexAdapter adapter;
	private PinnedSectionListView listView;
	
	private int totalCount = 0;
	private int pageNum = 20;// 单页数
	private int currentPage = 0;
	
	private boolean isLoading = false;
	
	private Context context;
	
	private boolean empty = false;
	
	private List<Item> finances = new ArrayList<Item>();
	
//	private InterstitialAd interstitialAd;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		context = getActivity();
		adapter = new FinanceIndexAdapter();

		getFinanceList();
		
//		loadAD();
	}
	
//	private void loadAD(){
//		interstitialAd = new InterstitialAd(getActivity());
//		interstitialAd.setListener(new InterstitialAdListener() {
//			
//			@Override
//			public void onAdReady() {
//				
//			}
//			
//			@Override
//			public void onAdPresent() {
//				
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
//				
//			}
//		});
//		interstitialAd.loadAd();
//	}
	
	@Override
	public void onResume() {
		super.onResume();
	}

	private void getFinanceList() {

		try {
			isLoading = true;

			List<Map<String, Object>> list = DBOperation.financeList(getActivity(),
					currentPage + 1, pageNum);
			totalCount = DBOperation.countFinance(getActivity());

			if (totalCount != 0) {
				currentPage++;
				if (currentPage == 1) {
					finances.clear();
				}

				int lastMonth = -1;
				int sectionPosition = 0, listPosition = 0;

				if (null != list) {
					for (Map<String, Object> map : list) {

						int month = Integer.valueOf(map.get("f_month").toString());
						if (month != lastMonth) {

							Map<String, Object> sectionMap = new HashMap<String, Object>();
							sectionMap.put("f_name", month);
							Item section = new Item(Item.SECTION, "section",
									sectionMap);
							section.listPosition = listPosition;
							section.sectionPosition = sectionPosition;

							finances.add(section);

							sectionPosition++;
							listPosition++;
						}

						Item cell = new Item(Item.ITEM, "cell", map);
						cell.listPosition = listPosition;
						cell.sectionPosition = sectionPosition - 1;
						finances.add(cell);

						if (month != lastMonth) {
							lastMonth = month;
						}
						listPosition++;
					}
				}

				empty = false;
			} else {
				finances.clear();
				empty = true;
			}

			if (empty) {
				Intent intent = new Intent(getActivity(), AddActivity.class);
				startActivityForResult(intent, 101);
			}

		} catch (Exception e) {
		}

		adapter.notifyDataSetChanged();
		isLoading = false;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_index, null);
		Button addFinanceButton = (Button)view.findViewById(R.id.btn_add_finance);
		addFinanceButton.setOnClickListener(addFinanceListener);
		
		listView = (PinnedSectionListView)view.findViewById(R.id.lv_finance_list);
		listView.setAdapter(adapter);
		listView.setOnScrollListener(scrollListener);
		listView.setOnItemClickListener(itemClickListener);
		
		return view;
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		
	}
	
	private OnClickListener addFinanceListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			toAddView();
		}
	};
	
	private void toAddView() {

		Intent intent = new Intent(getActivity(), AddActivity.class);
		startActivityForResult(intent, 101);
	}
	
	
	private OnItemClickListener itemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			
			if(finances.get(arg2).type!=Item.SECTION){
				
				Map<String, Object> item = finances.get(arg2).financeItem;
				
				if(item!=null){

					FinanceModifyDialog dialog = FinanceModifyDialog.newInstance(item, financeModifyListener);
					dialog.show(IndexFragment.this.getFragmentManager(), "FinanceModifyDialog");
					
				}else{
					showAppMsg("账单失效", STYLE_CONFIRM);
				}
				
			}
			
		}
	};
	
	private OnFinanceModifyListener financeModifyListener = new OnFinanceModifyListener() {
		
		@Override
		public void financeModify() {
//			if(interstitialAd.isAdReady()){
//				interstitialAd.showAd(getActivity());
//			}
			reload();
		}
	};
	
	/**
	 * 下拉刷新
	 */
	private OnScrollListener scrollListener = new OnScrollListener() {
		int scrollState;
		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {

			this.scrollState = scrollState;
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

			// 满足下列条件后可开始加载下一页
			if ((scrollState == OnScrollListener.SCROLL_STATE_TOUCH_SCROLL || scrollState == OnScrollListener.SCROLL_STATE_FLING) && !isLoading) {

				if (firstVisibleItem + visibleItemCount > totalItemCount - 2 && totalCount > currentPage * pageNum) {
					getFinanceList();
				}
			}
		}
	};
	
	
	private class FinanceIndexAdapter extends BaseAdapter  implements PinnedSectionListAdapter {

		private LayoutInflater inflater;
		
		private final int[] COLORS = new int[] {R.color.header_color_1,  R.color.header_color_3};
		
		private final int[] COLORSFORCELL = new int[] {R.color.header_color_3, R.color.header_color_1, R.color.header_color_2, R.color.header_color_4 };
		
		public FinanceIndexAdapter() {
			this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
		@Override
		public int getCount() {
			return finances.size();
		}

		@Override
		public Item getItem(int position) {
			return finances.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			Map<String, Object> cellMap = finances.get(position).financeItem;
			
			if(finances.get(position).type==Item.SECTION){
				
				convertView = inflater.inflate(R.layout.item_section_finance, null);
				
				TextView titleView = (TextView)convertView.findViewById(R.id.tv_section_title);
				titleView.setText(cellMap.get("f_name").toString()+" 月");
				
				convertView.setBackgroundColor(parent.getResources().getColor(COLORS[finances.get(position).sectionPosition % COLORS.length]));

			}else{
				convertView = inflater.inflate(R.layout.item_cell_finance, null);
				
				CircleImageView categoryLogo = (CircleImageView)convertView.findViewById(R.id.iv_logo);
				int drawableId = getActivity().getResources().getIdentifier(cellMap.get("f_c_logo").toString() ,"drawable", "com.lnl.finance"); 
//				int color = Integer.parseInt(cellMap.get("f_c_color").toString(), 16)+0xFF000000;
				
				int color = getResources().getColor(COLORSFORCELL[(finances.get(position).listPosition-finances.get(position).sectionPosition)%COLORSFORCELL.length]);
				categoryLogo.setBorderWidth(0);
				categoryLogo.setBorderColor(color);
				Bitmap b = BitmapFactory.decodeResource(getResources(), drawableId);
				Bitmap newBitmap = BitmapUtil.createRGBImage(b, color);
				categoryLogo.setImageBitmap(newBitmap);
				
				TextView categoryName = (TextView)convertView.findViewById(R.id.tv_category);
				categoryName.setText(cellMap.get("f_c_name").toString());
				
				
				TextView dateTime = (TextView)convertView.findViewById(R.id.tv_date);
				String timeStr = cellMap.get("f_add_time").toString();
				dateTime.setText(TimeUtil.formatDate(new Date(Long.parseLong(timeStr)), "MM-dd"));
				dateTime.setTextColor(Integer.parseInt(cellMap.get("f_c_color").toString(), 16)+0xFF000000);
				
				TextView typeView = (TextView)convertView.findViewById(R.id.tv_type);
				int textcolor = 0;
				if("1".equals(cellMap.get("f_type").toString())){
					typeView.setText("(收入)");
					textcolor = 0x99cc00+0xFF000000;
				}else{
					typeView.setText("(支出)");
					textcolor = 0xff4444+0xFF000000;
				}
				String descString = cellMap.get("f_desc").toString();
				TextView descTextView = (TextView)convertView.findViewById(R.id.tv_desc);
				if(!"".equals(descString)){
					descTextView.setVisibility(View.VISIBLE);
					descTextView.setText("--"+descString);
				}else{
					descTextView.setVisibility(View.GONE);
				}
				
				TextView moneyTextView = (TextView)convertView.findViewById(R.id.tv_money);
				moneyTextView.setTextColor(textcolor);
				moneyTextView.setTypeface(Typeface.createFromAsset(getActivity().getAssets() , "comic sans ms.ttf"));
				String moneyString = cellMap.get("f_money").toString();
				
				DecimalFormat a = new DecimalFormat("0.##");
				moneyString = a.format(Double.valueOf(moneyString));
				
				if(moneyString.length()>5){
					moneyTextView.setTextSize(15);
				}
				moneyTextView.setText("￥"+moneyString);
			}
			
			return convertView;
		}
		
		@Override 
		public int getViewTypeCount() {
            return 2;
        }

        @Override 
        public int getItemViewType(int position) {
            return getItem(position).type;
        }

        @Override
        public boolean isItemViewTypePinned(int viewType) {
            return viewType == Item.SECTION;
        }
	}
	
	@Override
	public void reload() {
		currentPage = 0;
		getFinanceList();
	}
}
