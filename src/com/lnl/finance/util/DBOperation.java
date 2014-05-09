package com.lnl.finance.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.R.integer;
import android.content.Context;

public class DBOperation {
	
	public static void insertCategory(Context context){
		
			insertCategory(context, "交通", new Date().getTime(), 1, "e4a54b", "cat_doprava",2,false);
			insertCategory(context, "早午晚餐", new Date().getTime(), 1, "626262", "cat_food",2,false);
			insertCategory(context, "电影", new Date().getTime(), 1, "fc77fe", "cat_movies",2,false);
			insertCategory(context, "娱乐", new Date().getTime(), 1, "fa6b44", "cat_entern",2,false);
			insertCategory(context, "学习教育", new Date().getTime(), 1, "abe9bd", "cat_school",2,false);
			insertCategory(context, "旅游出行", new Date().getTime(), 1, "539ee6", "cat_train",2,false);
			insertCategory(context, "家庭支出", new Date().getTime(), 1, "f7db79", "cat_reality",2,false);
			insertCategory(context, "服装购物", new Date().getTime(), 1, "30be91", "cat_shop",2,false);
			insertCategory(context, "油费", new Date().getTime(), 1, "312516", "cat_car",2,false);
			insertCategory(context, "超市购物", new Date().getTime(), 1, "b0afaf", "cat_house",2,false);
			insertCategory(context, "宠物", new Date().getTime(), 1, "8f845e", "cat_pet",2,false);
			insertCategory(context, "看病", new Date().getTime(), 1, "28d749", "cat_health",2,false);
			insertCategory(context, "酒水饮料", new Date().getTime(), 1, "f3db14", "cat_drink",2,false);
			insertCategory(context, "礼物", new Date().getTime(), 1, "c55ee6", "cat_gift",2,false);
			insertCategory(context, "其他", new Date().getTime(), 1, "0991c2", "cat_otaznik",2,false);
			
			insertCategory(context, "薪水工资", new Date().getTime(), 1, "8ebe30", "cat_bills",1,false);
			insertCategory(context, "额外收入", new Date().getTime(), 1, "2f82f4", "cat_penize",1,false);
			insertCategory(context, "零碎收入", new Date().getTime(), 1, "a9d3d3", "cat_personal",1,false);
			
	}
	
	public static boolean insertCategory(Context context,String name, long time, int notRemove, String color, String c_logo,int c_type, boolean addUpdateTime){

		if(!checkCategoryNameIsExsit(context, name)){
			
			String updateTimeString = "0";
			if(addUpdateTime){
				updateTimeString = new Date().getTime()+"";
			}
			
			DBHelper helper = new DBHelper(context);
			String[] arrColName = {"c_name","c_add_time","c_not_remove","c_color","c_logo","c_type","c_update_time"};
			String[] arrField = {name, ""+time, notRemove+"", color, c_logo, c_type+"",updateTimeString};
			System.out.println("insertcategory "+name+time);
			return helper.insertTable("category", arrColName, arrField);
		}
		
		return false;
	}
	
	public static void insertLogoColor(Context context){
		
		insertLogoColor(context, "e4a54b", "cat_doprava");
		insertLogoColor(context, "626262", "cat_food");
		insertLogoColor(context, "fc77fe", "cat_movies");
		insertLogoColor(context, "539ee6", "cat_train");
		insertLogoColor(context, "f7db79", "cat_reality");
		insertLogoColor(context, "30be91", "cat_shop");
		insertLogoColor(context, "312516", "cat_car");
		insertLogoColor(context, "b0afaf", "cat_house");
		insertLogoColor(context, "8f845e", "cat_pet");
		insertLogoColor(context, "28d749", "cat_health");
		insertLogoColor(context, "f3db14", "cat_drink");
		insertLogoColor(context, "c55ee6", "cat_gift");
		insertLogoColor(context, "fa6b44", "cat_entern");
		insertLogoColor(context, "515151", "cat_hotel");
		insertLogoColor(context, "ea5858", "cat_love");
		insertLogoColor(context, "757e86", "cat_money");
		insertLogoColor(context, "c55ee6", "cat_music");
		insertLogoColor(context, "5adb5a", "cat_nakup");
		insertLogoColor(context, "0991c2", "cat_otaznik");
		insertLogoColor(context, "2f82f4", "cat_penize");
		insertLogoColor(context, "a9d3d3", "cat_personal");
		insertLogoColor(context, "abe9bd", "cat_school");
		insertLogoColor(context, "2ec659", "cat_sport");
		insertLogoColor(context, "1663ac", "cat_travel");
		insertLogoColor(context, "8ebe30", "cat_bills");
	}
	
	/**
	 * 历史原因。logo存成color。互换
	 * @param context
	 * @param logo
	 * @param color
	 */
	private static void insertLogoColor(Context context,String logo, String color){
		
		DBHelper helper = new DBHelper(context);
		String[] arrColName = {"lc_logo","lc_color"};
		String[] arrField = {logo, color};
		
		helper.insertTable("logocolor", arrColName, arrField);
	}
	
	public static  List<Map<String,Object>> findLogoColor(Context context){
		
		DBHelper dbHelper = new DBHelper(context);
		
		String sSql = "select * from logocolor";
		return dbHelper.queryTable(sSql, null);
	}
	
	public static boolean isDataBaseClearByUnknow(Context context){
		
		DBHelper dbHelper = new DBHelper(context);
		String sSql = "select * from category";
	
		return dbHelper.countTable(sSql, null)==0;
	}
	
	/**
	 * 获取自定义分类列表
	 * @param context
	 * @param type
	 * @return
	 */
	public static List<Map<String,Object>> findOutCustomCategory(Context context){
		
		DBHelper dbHelper = new DBHelper(context);
		
		String sSql = "select * from category where c_not_remove = 0";
		return dbHelper.queryTable(sSql, null);
	}
	

	/**
	 * 获取分类列表
	 * @param context
	 * @param type 支出还是收入类型
	 * @return
	 */
	public static List<Map<String,Object>> findOutCategory(Context context,String type){
		
		DBHelper dbHelper = new DBHelper(context);
		
		String sSql = "select * from category where c_type = '"+type+"' order by c_update_time desc";
		return dbHelper.queryTable(sSql, null);
	}
	
	/**
	 * 保存账单
	 * @param context
	 * @param money
	 * @param c_id
	 * @param c_name
	 * @param c_logo
	 * @param c_color
	 * @param add_time
	 * @param type
	 * @param isPlan
	 * @param year
	 * @param month
	 * @param day
	 * @return
	 */
	public static boolean saveFinance(Context context,String money, String c_id,String c_name,String c_logo,String c_color,String add_time,String type,String isPlan,String year,String month,String day,String remark){
		DBHelper dbHelper = new DBHelper(context);
		
		System.out.println("savefinance money:"+money);
		
		String[] arrColName = {"f_money","f_c_id","f_c_name","f_c_logo","f_c_color","f_add_time","f_type","f_is_plan","f_year","f_month","f_day","f_status","f_desc"};
		String[] arrField = {money, c_id, c_name, c_logo, c_color, add_time, type, isPlan, year, month, day,"1",remark};
		
		if("2".equals(type)){
			//统计每日支出
			String sqlD = "select * from dayuse where du_year = "+year+" and du_month = "+month+" and du_day = "+day +" limit 0,1";
			List<Map<String,Object>> exsitDayUseList = dbHelper.queryTable(sqlD, null);
			if(exsitDayUseList==null || exsitDayUseList.size()==0){
				
				String[] arrDayName = {"du_money","du_year","du_month","du_day","du_status","du_update_time"};
				String[] arrDayValue = {money, year, month, day, 1+"", ""+new Date().getTime()};
				dbHelper.insertTable("dayuse", arrDayName, arrDayValue);
			}else{
				
				String[] arrDayName = {"du_money","du_status","du_update_time"};
				double updateMoney = Double.valueOf(money) +Double.valueOf(exsitDayUseList.get(0).get("du_money").toString());
				
				String[] arrDayValue = {updateMoney+"", 1+"", ""+new Date().getTime()};
				dbHelper.updateTable("dayuse", arrDayName, arrDayValue, "du_year = "+year+" and du_month = "+month+" and du_day = "+day);
			}
			
			//统计每月支出
			String sqlM= "select * from monthuse where mu_year = "+year+" and mu_month = "+month+" limit 0,1";
			List<Map<String,Object>> exsitMonthUseList = dbHelper.queryTable(sqlM, null);
			if(exsitMonthUseList==null || exsitMonthUseList.size()==0){
				
				String[] arrDayName = {"mu_money","mu_year","mu_month","mu_status","mu_update_time"};
				String[] arrDayValue = {money, year, month, 1+"", ""+new Date().getTime()};
				dbHelper.insertTable("monthuse", arrDayName, arrDayValue);
			}else{
				
				String[] arrDayName = {"mu_money","mu_status","mu_update_time"};
				double updateMoney = Double.valueOf(money) +Double.valueOf(exsitMonthUseList.get(0).get("mu_money").toString());
				
				String[] arrDayValue = {updateMoney+"", 1+"", ""+new Date().getTime()};
				dbHelper.updateTable("monthuse", arrDayName, arrDayValue, "mu_year = "+year+" and mu_month = "+month);
			}
			
			
			//月分类统计
			String sqlC = "select * from categorymonthuse where cmu_c_name = '"+c_name+"' and cmu_year = "+year+" and cmu_month = "+month+" limit 0,1";
			
			List<Map<String,Object>> exsitCategoryMonthUseList = dbHelper.queryTable(sqlC, null);
			if(exsitCategoryMonthUseList==null || exsitCategoryMonthUseList.size()==0){
				
				String[] arrDayName = {"cmu_c_name","cmu_money","cmu_year","cmu_month","cmu_status","cmu_update_time"};
				String[] arrDayValue = {c_name, money, year, month, 1+"", ""+new Date().getTime()};
				dbHelper.insertTable("categorymonthuse", arrDayName, arrDayValue);
			}else{
				
				String[] arrDayName = {"cmu_money","cmu_status","cmu_update_time"};
				double updateMoney = Double.valueOf(money) +Double.valueOf(exsitCategoryMonthUseList.get(0).get("cmu_money").toString());
				
				String[] arrDayValue = {updateMoney+"", 1+"", ""+new Date().getTime()};
				dbHelper.updateTable("categorymonthuse", arrDayName, arrDayValue, "cmu_c_name = '"+c_name+"' and cmu_year = "+year+" and cmu_month = "+month);
			}
		}else{
			//统计每日收入
			String sqlD = "select * from dayin where di_year = "+year+" and di_month = "+month+" and di_day = "+day +" limit 0,1";
			List<Map<String,Object>> exsitDayUseList = dbHelper.queryTable(sqlD, null);
			if(exsitDayUseList==null || exsitDayUseList.size()==0){
				System.out.println("insert day in");
				String[] arrDayName = {"di_money","di_year","di_month","di_day","di_status","di_update_time"};
				String[] arrDayValue = {money, year, month, day, 1+"", ""+new Date().getTime()};
				dbHelper.insertTable("dayin", arrDayName, arrDayValue);
			}else{
				
				String[] arrDayName = {"di_money","di_status","di_update_time"};
				double updateMoney = Double.valueOf(money) +Double.valueOf(exsitDayUseList.get(0).get("di_money").toString());
				
				String[] arrDayValue = {updateMoney+"", 1+"", ""+new Date().getTime()};
				dbHelper.updateTable("dayin", arrDayName, arrDayValue, "di_year = "+year+" and di_month = "+month+" and di_day = "+day);
			}
			
			//统计每月收入
			String sqlM= "select * from monthin where mi_year = "+year+" and mi_month = "+month+" limit 0,1";
			List<Map<String,Object>> exsitMonthUseList = dbHelper.queryTable(sqlM, null);
			if(exsitMonthUseList==null || exsitMonthUseList.size()==0){
				
				String[] arrDayName = {"mi_money","mi_year","mi_month","mi_status","mi_update_time"};
				String[] arrDayValue = {money, year, month, 1+"", ""+new Date().getTime()};
				dbHelper.insertTable("monthin", arrDayName, arrDayValue);
			}else{
				
				String[] arrDayName = {"mi_money","mi_status","mi_update_time"};
				double updateMoney = Double.valueOf(money) +Double.valueOf(exsitMonthUseList.get(0).get("mi_money").toString());
				
				String[] arrDayValue = {updateMoney+"", 1+"", ""+new Date().getTime()};
				dbHelper.updateTable("monthin", arrDayName, arrDayValue, "mi_year = "+year+" and mi_month = "+month);
			}
			
			
			//月收入分类统计
			String sqlC = "select * from categorymonthin where cmi_c_name = '"+c_name+"' and cmi_year = "+year+" and cmi_month = "+month+" limit 0,1";
			
			List<Map<String,Object>> exsitCategoryMonthUseList = dbHelper.queryTable(sqlC, null);
			if(exsitCategoryMonthUseList==null || exsitCategoryMonthUseList.size()==0){
				
				String[] arrDayName = {"cmi_c_name","cmi_money","cmi_year","cmi_month","cmi_status","cmi_update_time"};
				String[] arrDayValue = {c_name, money, year, month, 1+"", ""+new Date().getTime()};
				dbHelper.insertTable("categorymonthin", arrDayName, arrDayValue);
			}else{
				
				String[] arrDayName = {"cmi_money","cmi_status","cmi_update_time"};
				double updateMoney = Double.valueOf(money) +Double.valueOf(exsitCategoryMonthUseList.get(0).get("cmi_money").toString());
				
				String[] arrDayValue = {updateMoney+"", 1+"", ""+new Date().getTime()};
				dbHelper.updateTable("categorymonthin", arrDayName, arrDayValue, "cmi_c_name = '"+c_name+"' and  cmi_year = "+year+" and cmi_month = "+month);
			}
		}
		
		String[] categoryupdatetime = {"c_update_time"};
		String[] categoryupdatetimeValue = {new Date().getTime()+""};
		dbHelper.updateTable("category", categoryupdatetime, categoryupdatetimeValue, " c_id ="+c_id);
		
		return dbHelper.insertTable("finance", arrColName, arrField);
	}
	
	
	
	public static boolean delFinance(Context context,Map<String, Object> item){
		
		DBHelper dbHelper = new DBHelper(context);
		String[] arrColName = {"f_status"};
		String[] arrField = {"0"};
		
		String type = item.get("f_type").toString();
		String year = item.get("f_year").toString();
		String month = item.get("f_month").toString();
		String day = item.get("f_day").toString();
		String money = item.get("f_money").toString();
		String c_name = item.get("f_c_name").toString();
		
		System.out.println("type:"+type);
		
		if("2".equals(type)){
			//统计每日支出
			String sqlD = "select * from dayuse where du_year = "+year+" and du_month = "+month+" and du_day = "+day +" limit 0,1";
			List<Map<String,Object>> exsitDayUseList = dbHelper.queryTable(sqlD, null);
			System.out.println("exsitDayUseListSize:"+exsitDayUseList.size());
			if(exsitDayUseList!=null&&exsitDayUseList.size()!=0){
				
				String[] arrDayName = {"du_money","du_status","du_update_time"};
				double updateMoney = Double.valueOf(exsitDayUseList.get(0).get("du_money").toString())-Double.valueOf(money);
				System.out.println("day updatemoney:"+updateMoney);
				String[] arrDayValue = {updateMoney<0?"0":updateMoney+"", 1+"", ""+new Date().getTime()};
				dbHelper.updateTable("dayuse", arrDayName, arrDayValue, "du_year = "+year+" and du_month = "+month+" and du_day = "+day);
			}
			
			//统计每月支出
			String sqlM= "select * from monthuse where mu_year = "+year+" and mu_month = "+month+" limit 0,1";
			List<Map<String,Object>> exsitMonthUseList = dbHelper.queryTable(sqlM, null);
			if(exsitMonthUseList!=null&&exsitMonthUseList.size()!=0){
				
				String[] arrDayName = {"mu_money","mu_status","mu_update_time"};
				double updateMoney = Double.valueOf(exsitMonthUseList.get(0).get("mu_money").toString())-Double.valueOf(money);
				System.out.println("month updatemoney:"+updateMoney);
				String[] arrDayValue = {updateMoney<0?"0":updateMoney+"", 1+"", ""+new Date().getTime()};
				dbHelper.updateTable("monthuse", arrDayName, arrDayValue, "mu_year = "+year+" and mu_month = "+month);
			}
			
			//月分类统计
			String sqlC = "select * from categorymonthuse where cmu_c_name = '"+c_name+"' and cmu_year = "+year+" and cmu_month = "+month+" limit 0,1";
			
			List<Map<String,Object>> exsitCategoryMonthUseList = dbHelper.queryTable(sqlC, null);
			if(exsitCategoryMonthUseList!=null&&exsitCategoryMonthUseList.size()!=0){
				String[] arrDayName = {"cmu_money","cmu_status","cmu_update_time"};
				double updateMoney = Double.valueOf(exsitCategoryMonthUseList.get(0).get("cmu_money").toString())-Double.valueOf(money);
				System.out.println("category updatemoney:"+updateMoney);
				String[] arrDayValue = {updateMoney<0?"0":updateMoney+"", 1+"", ""+new Date().getTime()};
				dbHelper.updateTable("categorymonthuse", arrDayName, arrDayValue, "cmu_c_name = '"+c_name+"' and  cmu_year = "+year+" and cmu_month = "+month);
			}
		}else{
			//统计每日收入
			String sqlD = "select * from dayin where di_year = "+year+" and di_month = "+month+" and di_day = "+day +" limit 0,1";
			List<Map<String,Object>> exsitDayUseList = dbHelper.queryTable(sqlD, null);
			if(exsitDayUseList!=null&&exsitDayUseList.size()!=0){
				String[] arrDayName = {"di_money","di_status","di_update_time"};
				double updateMoney = Double.valueOf(exsitDayUseList.get(0).get("di_money").toString())-Double.valueOf(money);
				
				String[] arrDayValue = {updateMoney<0?"0":updateMoney+"", 1+"", ""+new Date().getTime()};
				dbHelper.updateTable("dayin", arrDayName, arrDayValue, "di_year = "+year+" and di_month = "+month+" and di_day = "+day);
			}
			
			//统计每月收入
			String sqlM= "select * from monthin where mi_year = "+year+" and mi_month = "+month+" limit 0,1";
			List<Map<String,Object>> exsitMonthUseList = dbHelper.queryTable(sqlM, null);
			if(exsitMonthUseList!=null&&exsitMonthUseList.size()!=0){
				String[] arrDayName = {"mi_money","mi_status","mi_update_time"};
				double updateMoney = Double.valueOf(exsitMonthUseList.get(0).get("mi_money").toString())-Double.valueOf(money);
				
				String[] arrDayValue = {updateMoney<0?"0":updateMoney+"", 1+"", ""+new Date().getTime()};
				dbHelper.updateTable("monthin", arrDayName, arrDayValue, "mi_year = "+year+" and mi_month = "+month);
			}
			
			//月收入分类统计
			String sqlC = "select * from categorymonthin where cmi_c_name = '"+c_name+"' and cmi_year = "+year+" and cmi_month = "+month+" limit 0,1";
			List<Map<String,Object>> exsitCategoryMonthUseList = dbHelper.queryTable(sqlC, null);
			if(exsitCategoryMonthUseList!=null&&exsitCategoryMonthUseList.size()!=0){
				
				String[] arrDayName = {"cmi_money","cmi_status","cmi_update_time"};
				double updateMoney = Double.valueOf(exsitCategoryMonthUseList.get(0).get("cmi_money").toString())-Double.valueOf(money);
				
				String[] arrDayValue = {updateMoney<0?"0":updateMoney+"", 1+"", ""+new Date().getTime()};
				dbHelper.updateTable("categorymonthin", arrDayName, arrDayValue, "cmi_c_name = '"+c_name+"' and  cmi_year = "+year+" and cmi_month = "+month);
			}
		}
		
		return dbHelper.updateTable("finance", arrColName, arrField, "f_id = '"+item.get("f_id").toString()+"'");
	}
	
	public static boolean modifyFinance(Context context,Map<String, Object> item, String modifyMoney){
		
		
		DBHelper dbHelper = new DBHelper(context);
		String[] arrColName = {"f_money","f_status"};
		String[] arrField = {modifyMoney,"1"};
		
		String type = item.get("f_type").toString();
		String year = item.get("f_year").toString();
		String month = item.get("f_month").toString();
		String day = item.get("f_day").toString();
		String money = (Double.valueOf(item.get("f_money").toString())-Double.valueOf(modifyMoney))+"";
		String c_name = item.get("f_c_name").toString();
		
		if("2".equals(type)){
			//统计每日支出
			String sqlD = "select * from dayuse where du_year = "+year+" and du_month = "+month+" and du_day = "+day +" limit 0,1";
			List<Map<String,Object>> exsitDayUseList = dbHelper.queryTable(sqlD, null);
			if(exsitDayUseList!=null&&exsitDayUseList.size()!=0){
				
				String[] arrDayName = {"du_money","du_status","du_update_time"};
				double updateMoney = Double.valueOf(exsitDayUseList.get(0).get("du_money").toString())-Double.valueOf(money);
				String[] arrDayValue = {updateMoney<0?"0":updateMoney+"", 1+"", ""+new Date().getTime()};
				dbHelper.updateTable("dayuse", arrDayName, arrDayValue, "du_year = "+year+" and du_month = "+month+" and du_day = "+day);
			}
			
			//统计每月支出
			String sqlM= "select * from monthuse where mu_year = "+year+" and mu_month = "+month+" limit 0,1";
			List<Map<String,Object>> exsitMonthUseList = dbHelper.queryTable(sqlM, null);
			if(exsitMonthUseList!=null&&exsitMonthUseList.size()!=0){
				
				String[] arrDayName = {"mu_money","mu_status","mu_update_time"};
				double updateMoney = Double.valueOf(exsitMonthUseList.get(0).get("mu_money").toString())-Double.valueOf(money);
				String[] arrDayValue = {updateMoney<0?"0":updateMoney+"", 1+"", ""+new Date().getTime()};
				dbHelper.updateTable("monthuse", arrDayName, arrDayValue, "mu_year = "+year+" and mu_month = "+month);
			}
			
			//月分类统计
			String sqlC = "select * from categorymonthuse where cmu_c_name = '"+c_name+"' and cmu_year = "+year+" and cmu_month = "+month+" limit 0,1";
			
			List<Map<String,Object>> exsitCategoryMonthUseList = dbHelper.queryTable(sqlC, null);
			if(exsitCategoryMonthUseList!=null&&exsitCategoryMonthUseList.size()!=0){
				String[] arrDayName = {"cmu_money","cmu_status","cmu_update_time"};
				double updateMoney = Double.valueOf(exsitCategoryMonthUseList.get(0).get("cmu_money").toString())-Double.valueOf(money);
				
				String[] arrDayValue = {updateMoney<0?"0":updateMoney+"", 1+"", ""+new Date().getTime()};
				dbHelper.updateTable("categorymonthuse", arrDayName, arrDayValue, "cmu_c_name = '"+c_name+"' and cmu_year = "+year+" and cmu_month = "+month);
			}
		}else{
			//统计每日收入
			String sqlD = "select * from dayin where di_year = "+year+" and di_month = "+month+" and di_day = "+day +" limit 0,1";
			List<Map<String,Object>> exsitDayUseList = dbHelper.queryTable(sqlD, null);
			if(exsitDayUseList!=null&&exsitDayUseList.size()!=0){
				String[] arrDayName = {"di_money","di_status","di_update_time"};
				double updateMoney = Double.valueOf(exsitDayUseList.get(0).get("di_money").toString())-Double.valueOf(money);
				
				String[] arrDayValue = {updateMoney<0?"0":updateMoney+"", 1+"", ""+new Date().getTime()};
				dbHelper.updateTable("dayin", arrDayName, arrDayValue, "di_year = "+year+" and di_month = "+month+" and di_day = "+day);
			}
			
			//统计每月收入
			String sqlM= "select * from monthin where mi_year = "+year+" and mi_month = "+month+" limit 0,1";
			List<Map<String,Object>> exsitMonthUseList = dbHelper.queryTable(sqlM, null);
			if(exsitMonthUseList!=null&&exsitMonthUseList.size()!=0){
				String[] arrDayName = {"mi_money","mi_status","mi_update_time"};
				double updateMoney = Double.valueOf(exsitMonthUseList.get(0).get("mi_money").toString())-Double.valueOf(money);
				
				String[] arrDayValue = {updateMoney<0?"0":updateMoney+"", 1+"", ""+new Date().getTime()};
				dbHelper.updateTable("monthin", arrDayName, arrDayValue, "mi_year = "+year+" and mi_month = "+month);
			}
			
			//月收入分类统计
			String sqlC = "select * from categorymonthin where cmi_c_name = '"+c_name+"' and cmi_year = "+year+" and cmi_month = "+month+" limit 0,1";
			List<Map<String,Object>> exsitCategoryMonthUseList = dbHelper.queryTable(sqlC, null);
			if(exsitCategoryMonthUseList!=null&&exsitCategoryMonthUseList.size()!=0){
				
				String[] arrDayName = {"cmi_money","cmi_status","cmi_update_time"};
				double updateMoney = Double.valueOf(exsitCategoryMonthUseList.get(0).get("cmi_money").toString())-Double.valueOf(money);
				
				String[] arrDayValue = {updateMoney<0?"0":updateMoney+"", 1+"", ""+new Date().getTime()};
				dbHelper.updateTable("categorymonthin", arrDayName, arrDayValue, "cmi_c_name = '"+c_name+"' and cmi_year = "+year+" and cmi_month = "+month);
			}
		}
		
		return dbHelper.updateTable("finance", arrColName, arrField, "f_id = '"+item.get("f_id").toString()+"'");
	}
	
	
	
	/**
	 * 账单列表
	 * @param context
	 * @param page
	 * @param pageCount
	 * @return
	 */
	public static List<Map<String,Object>> financeList(Context context, int page,int pageCount){
		
		DBHelper dbHelper = new DBHelper(context);
		String sSql = "select * from finance where f_status = '1' order by f_year desc,f_month desc,f_day desc,f_add_time desc limit "+(page-1)*pageCount+","+pageCount;
	
		List<Map<String,Object>> financelist = dbHelper.queryTable(sSql, null);
		return financelist;
	}
	
	public static int countFinance(Context context){
		
		DBHelper dbHelper = new DBHelper(context);
		String sSql = "select * from finance where f_status = '1'";
	
		return dbHelper.countTable(sSql, null);
	}
	
	/**
	 * 日总支出记录 7天内
	 * @param context
	 * @return
	 */
	public static List<Map<String,Object>> weekDayUseList(Context context){
		
		DBHelper dbHelper = new DBHelper(context);
		String sSql = "select * from dayuse where du_status = 1 order by du_year desc, du_month desc, du_day desc limit 0,7";
	
		List<Map<String,Object>> weekDayUseList = dbHelper.queryTable(sSql, null);
		return weekDayUseList;
	}
	
	/**
	 * 月总支出记录 12月内
	 * @param context
	 * @return
	 */
	public static List<Map<String,Object>> monthUseList(Context context){
		
		DBHelper dbHelper = new DBHelper(context);
		String sSql = "select * from monthuse where mu_status = 1 order by mu_year desc, mu_month desc limit 0,12";
	
		List<Map<String,Object>> monthUseList = dbHelper.queryTable(sSql, null);
		return monthUseList;
	}
	
	/**
	 * 分类月支出列表
	 * @param context
	 * @param year
	 * @param month
	 * @return
	 */
	public static List<Map<String,Object>> categoryMonthUseList(Context context){
		
		Date date = new Date();
		int year = date.getYear()+1900;
		int month = date.getMonth()+1;
		
		DBHelper dbHelper = new DBHelper(context);
		String sSql = "select * from categorymonthuse where cmu_status = 1 and cmu_year = "+year+" and cmu_month = "+month;
		System.out.println("categoryMonthUseList sql:"+sSql);
		List<Map<String,Object>> categoryMonthUseList = dbHelper.queryTable(sSql, null);
		return categoryMonthUseList;
	}
	/**
	 * 每月有支出的标记日列表
	 * @param context
	 * @param year
	 * @param month
	 * @return
	 */
	public static ArrayList<String> dayMarkedList(Context context,int year,int month){
		DBHelper dbHelper = new DBHelper(context);
		String sSql = "select du_day from dayuse where du_money>0 and du_status = 1 and du_year = "+year+" and du_month = "+month+" order by du_day asc";
		
		List<Map<String,Object>> categoryMonthUseList = dbHelper.queryTable(sSql, null);
		
		if(categoryMonthUseList==null){
			return null;
		}
		ArrayList<String> returnArr = new ArrayList<String>();
		for (Map<String,Object> item : categoryMonthUseList) {
			if(!"0".equals(item.get("du_day").toString())){
				returnArr.add(item.get("du_day").toString());
			}
		}
		return returnArr;
	}
	
	/**
	 * 每日使用详情列表
	 * @param context
	 * @param year
	 * @param month
	 * @param day
	 * @return
	 */
	public static Map<String, Object> dayDetailUseList(Context context, int year, int month, int day){
		
		Map<String, Object> backMap = new HashMap<String, Object>();
		
		//每日总支出 总收入
		String todayTotalInSql = "select * from dayin where di_year = "+year+" and di_month = "+month+" and di_day = "+day +" limit 0,1";
		String todayTotoalUseSql = "select * from dayuse where du_year = "+year+" and du_month = "+month+" and du_day = "+day +" limit 0,1";
		
		DBHelper dbHelper = new DBHelper(context);
		List<Map<String,Object>> todayInList = dbHelper.queryTable(todayTotalInSql, null);
		List<Map<String,Object>> todayUseList = dbHelper.queryTable(todayTotoalUseSql, null);
		
		if(todayInList!=null){
			backMap.put("totalIn", todayInList.get(0).get("di_money").toString());
		}else{
			backMap.put("totalIn", "0");
		}
		
		if(todayUseList!=null){
			backMap.put("totalUse", todayUseList.get(0).get("du_money").toString());
		}else{
			backMap.put("totalUse", "0");
		}
		
		
		String todayFinanceSql = "select * from finance where f_year = "+year+" and f_month = "+month+" and f_day = "+day+" and f_status = 1 order by f_add_time desc";
		List<Map<String,Object>> todayFinanceList = dbHelper.queryTable(todayFinanceSql, null);
		if(todayFinanceList==null){
			backMap.put("financeCount", 0);
		}else{
			backMap.put("financeCount", todayFinanceList.size());
		}
		backMap.put("finance", todayFinanceList);
		backMap.put("title", year+"年"+month+"月"+day+"日");
		
		return backMap;
	}

	
	public static double currentMonthPayoff(Context context){
		
		DBHelper dbHelper = new DBHelper(context);
		Date date = new Date();
		
		double pay = 0;
		String currentMonthPayoffSql = "select * from monthuse where mu_year ="+ (date.getYear()+1900)+" and mu_month = "+(date.getMonth()+1)+" limit 0,1";
		List<Map<String,Object>> todayFinanceList = dbHelper.queryTable(currentMonthPayoffSql, null);
		if(todayFinanceList !=null){
			try {
				pay = Double.valueOf(todayFinanceList.get(0).get("mu_money").toString());
			} catch (Exception e) {
			}
		}
		return pay;
	}
	
	/**
	 * 我的余额
	 * @return
	 */
	public static double getTotalBalance(Context context){
		
		double totalDouble = 0;
		try {
			DBHelper dbHelper = new DBHelper(context);
			String useString = "select mu_money from monthuse where mu_status = 1";
			String inString = "select mi_money from monthin where mi_status = 1";
			
			List<Map<String,Object>> useList = dbHelper.queryTable(useString, null);
			List<Map<String,Object>> inList = dbHelper.queryTable(inString, null);
			
			for (Map<String, Object> map : useList) {
				totalDouble-=Double.valueOf(map.get("mu_money").toString());
			}
			for (Map<String, Object> map : inList) {
				totalDouble+=Double.valueOf(map.get("mi_money").toString());
			}
		} catch (Exception e) {
		}
		return totalDouble;
	}
	
	public static boolean checkCategoryNameIsExsit(Context context, String name){
		
		DBHelper dbHelper = new DBHelper(context);
		String queryString = "select * from category where c_name ='"+name+"'";
		
		return dbHelper.countTable(queryString, null)!=0;
	}
	
	public static boolean saveCustomCategory(Context context,String name, String logo, String color, int type){
		
		return insertCategory(context, name, new Date().getTime(), 0, color, logo, type, true);
	}
	
	public static boolean deleteCustomCategory(Context context,String delCategoryIds){
		
		DBHelper dbHelper = new DBHelper(context);
		return dbHelper.deleteTable("category", "c_not_remove = 0 and c_id in "+delCategoryIds);
	}
}
