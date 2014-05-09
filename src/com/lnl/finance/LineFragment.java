package com.lnl.finance;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.R.integer;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.echo.holographlibrary.Bar;
import com.echo.holographlibrary.Line;
import com.echo.holographlibrary.LinePoint;
import com.hb.views.PinnedSectionListView;
import com.hb.views.PinnedSectionListView.PinnedSectionListAdapter;
import com.lnl.finance.listener.FragmentReloadListener;
import com.lnl.finance.util.DBOperation;
import com.lnl.finance.view.BarCellView;
import com.lnl.finance.view.LineCellView;
import com.lnl.finance.view.PieCellView;

public class LineFragment extends BaseFragment  implements FragmentReloadListener{
	
	PinnedSectionListView listView;
	LineAdapter adapter;
	
	private Line line = new Line();
	private ArrayList<Bar> points = new ArrayList<Bar>();
	private ArrayList<Float> alPercentage = new ArrayList<Float>();
	private List<Map<String, Object>> monthCategoryList = null;
	
	private int maxCategoryUseIndex = 0;
	
	private final int[] COLORS = new int[] {R.color.header_color_1, R.color.header_color_2, R.color.header_color_3, R.color.header_color_4 };
	
	private View view;
	
	private void getData(){

		new AsyncTask<String, Void, String>() {

			@Override
			protected void onPreExecute() {
				super.onPreExecute();

			}

			@Override
			protected String doInBackground(String... param) {
				
				line.clear();
				line.setColor(Color.parseColor("#FFBB33"));
				
				List<Map<String, Object>> weekDayUseList = DBOperation.weekDayUseList(getActivity());
				if(weekDayUseList!=null){

					int i = weekDayUseList.size();
					
					if(i==1){
						Map<String, Object> dayuse  = weekDayUseList.get(0);
						
						String moneyString = dayuse.get("du_money").toString();
						DecimalFormat a = new DecimalFormat("0.##");
						moneyString = a.format(Double.valueOf(moneyString));
						
						LinePoint pp = new LinePoint(0, Float.valueOf(moneyString),dayuse.get("du_month").toString()+"-"+dayuse.get("du_day").toString());
						line.addPoint(pp);
						pp = new LinePoint(1, Float.valueOf(dayuse.get("du_money").toString()),dayuse.get("du_month").toString()+"-"+dayuse.get("du_day").toString());
						line.addPoint(pp);
						
					}else{
						for (Map<String, Object> dayuse : weekDayUseList) {
							
							String moneyString = dayuse.get("du_money").toString();
							DecimalFormat a = new DecimalFormat("0.##");
							moneyString = a.format(Double.valueOf(moneyString));
							
							LinePoint pp = new LinePoint(i-1, Float.valueOf(moneyString),dayuse.get("du_month").toString()+"-"+dayuse.get("du_day").toString());
							line.addPoint(pp);
							
							i--;
						}
					}
					
				}else{
					
					Date date = new Date();
					
					LinePoint pp = new LinePoint(1, 0, (date.getMonth()+1)+"-"+date.getDate());
					line.addPoint(pp);
					
					pp = new LinePoint(0, 0, (date.getMonth()+1)+"-"+date.getDate());
					line.addPoint(pp);
				}
				
				List<Map<String, Object>> monthUseList = DBOperation.monthUseList(getActivity());
				points.clear();
				if(monthUseList!=null){
					
					for (int i = monthUseList.size(); i > 0; i--) {
						
						Map<String, Object> monthuse = monthUseList.get(i-1);
						
						Bar dd = new Bar();
						dd.setColor(getResources().getColor(COLORS[(monthUseList.size()-i) % COLORS.length]));
						dd.setName(monthuse.get("mu_month")+"月");
						
						String moneyString = monthuse.get("mu_money").toString();
						DecimalFormat a = new DecimalFormat("0.##");
						moneyString = a.format(Double.valueOf(moneyString));
						
						dd.setValue(Float.valueOf(moneyString));
						points.add(dd);
					}
					
				}else{
					
					Date date = new Date();
					
					Bar dd = new Bar();
					dd.setColor(Color.parseColor("#99CC00"));
					dd.setName((date.getMonth()+1)+"月");
					dd.setValue(Float.valueOf("0.0"));
					points.add(dd);
				}
				

				monthCategoryList = DBOperation.categoryMonthUseList(getActivity());
				alPercentage.clear();
				maxCategoryUseIndex = 0;
				double maxCategoryUseMoney = 0;
				if(monthCategoryList!=null){
					double totalMoney = 0;
					int length = monthCategoryList.size();
					for (int i = 0; i<length; i++) {
						
						Map<String, Object> monthCategoryuse = monthCategoryList.get(i);
						double money = Double.valueOf(monthCategoryuse.get("cmu_money").toString());
						totalMoney+=money;
					}

					float fill100percent = 0;
					for (int i = 0; i<length; i++) {
						Map<String, Object> monthCategoryuse = monthCategoryList.get(i);
						double money = Double.valueOf(monthCategoryuse.get("cmu_money").toString());
						if(money>maxCategoryUseMoney){
							maxCategoryUseMoney = money;
							maxCategoryUseIndex = i;
						}
						
						if(i!=length-1){
							float formatMoney = Float.valueOf(String.format(Locale.SIMPLIFIED_CHINESE,"%.1f", money*100/totalMoney));
							
							alPercentage.add(formatMoney);
							fill100percent+=formatMoney;
						}else{
							alPercentage.add(100-fill100percent);
						}
					}
				}else{
					System.out.println("monthcategorylist is null");
				}
				
				return null;
				
			}
			
			@Override
			protected void onPostExecute(String result) {
				super.onPostExecute(result);
				if(adapter!=null){
					adapter.notifyDataSetChanged();
				}
			}
		}.execute();
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		getData();
		
		View view = inflater.inflate(R.layout.fragment_line, null);

		if(adapter == null){
			adapter = new LineAdapter(getActivity());
		}
		
		listView = (PinnedSectionListView)view.findViewById(R.id.lv_line_list);
		listView.setAdapter(adapter);
		
		return view;
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}
	
	
	private class LineAdapter extends BaseAdapter implements PinnedSectionListAdapter{

		private LayoutInflater inflater;
		
		public LineAdapter(Context context){
			this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
		
		@Override
		public int getCount() {
			return 6;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			if(position==0){
				convertView = inflater.inflate(R.layout.item_section_finance, null);
				
				TextView titleView = (TextView)convertView.findViewById(R.id.tv_section_title);
				titleView.setText("七日内支出线图");
				convertView.setBackgroundColor(getResources().getColor(R.color.header_color_1));
				return convertView;
			}else if(position==1){
				
				System.out.println("getview l count:"+line.getSize());
				LineCellView lineCellView = new LineCellView(getActivity(), getActivity(), line);
				return lineCellView;
			}else if(position==2){
				
				convertView = inflater.inflate(R.layout.item_section_finance, null);
				
				TextView titleView = (TextView)convertView.findViewById(R.id.tv_section_title);
				titleView.setText("月分类支出饼图");
				convertView.setBackgroundColor(getResources().getColor(R.color.header_color_3));
				return convertView;
				
			}else if(position==3){
				
				System.out.println("alPercentage size :"+alPercentage.size());
				PieCellView pieCellView = new PieCellView(getActivity(), getActivity(), alPercentage, maxCategoryUseIndex,monthCategoryList);
				return pieCellView;
				
			}else if(position==4){
				convertView = inflater.inflate(R.layout.item_section_finance, null);
				
				TextView titleView = (TextView)convertView.findViewById(R.id.tv_section_title);
				titleView.setText("月支出柱图");
				convertView.setBackgroundColor(getResources().getColor(R.color.header_color_1));
				
				return convertView;
			}else if(position==5){
				System.out.println("getview p count:"+points.size());
				BarCellView barCellView = new BarCellView(getActivity(), getActivity(), points);
				return barCellView;
			}
			
			return null;
		}

		@Override 
		public int getViewTypeCount() {
            return 2;
        }

        @Override 
        public int getItemViewType(int position) {
        	return position%2;
        }

        @Override
        public boolean isItemViewTypePinned(int viewType) {
            return viewType==0;
        }
		
	}
	
	@Override
	public void reload() {
		System.out.println("lineFragment reload");
		getData();
		
	}
}
