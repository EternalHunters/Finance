package com.lnl.finance.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper{

	private static final String DATABASE_NAME = "FinanceApp.db";      
	private static final int DATABASE_VERSION = 2; 
	
	public DBHelper(Context context) {      
		super(context, DATABASE_NAME, null, DATABASE_VERSION);      
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String tableSQL = "";
		

		tableSQL = "CREATE TABLE IF NOT EXISTS finance" +
					"([f_id] INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +//id
					"[f_money] integer(200)," +//钱
					"[f_c_id] integer(200)," +//
					"[f_c_name] varchar(200)," +//
					"[f_c_logo] varchar(200)," +//
					"[f_c_color] varchar(200)," +//
					"[f_add_time] integer(200)," +//
					"[f_type] integer(16)," +//收入还是支出  1:收入 2：支出
					"[f_is_plan] integer(16)," +
					"[f_year] integer(200)," +
					"[f_month] integer(200)," +
					"[f_status] integer(200)," +
					"[f_desc] varchar(200)," +
					"[f_photo] varchar(200)," +
					"[f_day] integer(200));";
		db.execSQL(tableSQL);
		
		tableSQL = "CREATE TABLE IF NOT EXISTS logocolor" +//logo和颜色对照
				"([lc_id] INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +//id
				"[lc_logo] varchar(200)," +//
				"[lc_color] varchar(200));";//
		db.execSQL(tableSQL);
		
		tableSQL = "CREATE TABLE IF NOT EXISTS category" +//支出和收入的类型
				"([c_id] INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +//id
				"[c_name] varchar(200)," +//
				"[c_add_time] integer(200)," +//
				"[c_update_time] integer(200)," +//
				"[c_not_remove] integer(16)," +//
				"[c_color] varchar(200)," +//
				"[c_type] integer(16)," +//
				"[c_logo] varchar(200));";//
		db.execSQL(tableSQL);
		
		
		tableSQL = "CREATE TABLE IF NOT EXISTS dayuse" +//日支出
				"([du_id] INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +//id
				"[du_money] integer(200)," +//
				"[du_year] integer(200)," +
				"[du_month] integer(200)," +
				"[du_day] integer(200)," +
				"[du_status] integer(11)," +
				"[du_update_time] integer(200));";//
		db.execSQL(tableSQL);
		
		tableSQL = "CREATE TABLE IF NOT EXISTS monthuse" +//月支出
				"([mu_id] INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +//id
				"[mu_money] integer(200)," +//
				"[mu_year] integer(200)," +
				"[mu_month] integer(200)," +
				"[mu_status] integer(11)," +
				"[mu_update_time] integer(200));";//
		db.execSQL(tableSQL);
		
		
		tableSQL = "CREATE TABLE IF NOT EXISTS categorymonthuse" +//月分类支出
				"([cmu_id] INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +//id
				"[cmu_c_name] varchar(200)," +
				"[cmu_money] integer(200)," +//
				"[cmu_year] integer(200)," +
				"[cmu_month] integer(200)," +
				"[cmu_status] integer(11)," +
				"[cmu_update_time] integer(200));";//
		db.execSQL(tableSQL);
		
		
		tableSQL = "CREATE TABLE IF NOT EXISTS dayin" +//日收入
				"([di_id] INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +//id
				"[di_money] integer(200)," +//
				"[di_year] integer(200)," +
				"[di_month] integer(200)," +
				"[di_day] integer(200)," +
				"[di_status] integer(11)," +
				"[di_update_time] integer(200));";//
		db.execSQL(tableSQL);
		
		tableSQL = "CREATE TABLE IF NOT EXISTS monthin" +//月支出
				"([mi_id] INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +//id
				"[mi_money] integer(200)," +//
				"[mi_year] integer(200)," +
				"[mi_month] integer(200)," +
				"[mi_status] integer(11)," +
				"[mi_update_time] integer(200));";//
		db.execSQL(tableSQL);
		
		
		tableSQL = "CREATE TABLE IF NOT EXISTS categorymonthin" +//月分类支出
				"([cmi_id] INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +//id
				"[cmi_c_name] varchar(200)," +
				"[cmi_money] integer(200)," +//
				"[cmi_year] integer(200)," +
				"[cmi_month] integer(200)," +
				"[cmi_status] integer(11)," +
				"[cmi_update_time] integer(200));";//
		db.execSQL(tableSQL);
	}
	
	

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		System.out.println("onUpgrade"+oldVersion+"   "+newVersion);
		String tableSQL = "ALTER TABLE category ADD c_update_time integer(200) DEFAULT 0";
		db.execSQL(tableSQL);
	}
	/**查询表
	 * @param sSql 查询语句
	 * @param arrSelectionArgs 查询条件里值
	 * @return 查询记录集
	 */
	public List<Map<String,Object>> queryTable(String sSql,String[] arrSelectionArgs) {
		Cursor cur = null;
		SQLiteDatabase db = null;
		List<Map<String,Object>> lReturn = new ArrayList<Map<String, Object>>();
		try {
			db = getReadableDatabase();  
	    	cur = db.rawQuery(sSql, arrSelectionArgs);
	    	if (cur.getCount()>0) {
	        	cur.moveToFirst();
	        	for (int i = 0; i < cur.getCount(); i++) {
	        		Map<String,Object> map = new TreeMap<String, Object>();
	        		for(int j = 0; j < cur.getColumnCount(); j++) {
	        			if(cur.getString(j) != null && cur.getString(j).length() > 0) {
	        				map.put(cur.getColumnName(j),cur.getString(j));
	        			}
	        			else {
	        				map.put(cur.getColumnName(j),"");
	        			}
	        		}
	        		lReturn.add(map);
	        		cur.moveToNext();
	        	}
	    		cur.close();
	    		db.close();
	    	}
	    	else {
	    		cur.close();
	    		db.close();
	    		return null;
	    	}
		}
		catch(Exception e){
			if(cur!=null){
				cur.close();
			}
			if(db!=null){
				db.close();
			}
			return null;
		}
    	return lReturn;
	}
	
	/**插入到数据表
	 * @param sTableName 数据表名
	 * @param arrColName 字段名
	 * @param arrField 字段内容
	 * @return 布尔类型是否操作成功
	 */
	public boolean insertTable(String sTableName,String[] arrColName,String[] arrField) {
		String sSql = "",sColName = "",sField = "";
		//参数不能为空
		if(sTableName.length()==0 && arrColName.length==0 && arrField.length==0) {
			return false;
		}
		//字段名和字段内容必须相同
		if(arrColName.length != arrField.length) {
			return false;
		}
		//字段名
		for(int i=0;i<arrColName.length;i++) {
			if(i==0) {
				sColName += arrColName[i];
			}
			else {
				sColName += "," + arrColName[i];
			}
		}
		//字段内容
		for(int i=0;i<arrField.length;i++) {
			if(i==0) {
				sField += "\"" + arrField[i] + "\"";
			}
			else {
				sField += ",\"" + arrField[i] + "\"";
			}
		}
		//组成SQL语句
		try {
			sSql = "insert into " + sTableName + "(" + sColName + ") values(" + sField + ")";
			SQLiteDatabase db = getWritableDatabase();
			db.execSQL(sSql);
			db.close();
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**修改数据表
	 * @param sTableName 数据表名
	 * @param arrColName 字段名
	 * @param arrField 字段内容
	 * @param sWhere 修改条件
	 * @return 布尔类型是否操作成功
	 */
	public boolean updateTable(String sTableName,String[] arrColName,String[] arrField,String sWhere) {
		String sSql = "",sField = "";
		//参数不能为空
		if(sTableName.length()==0 && arrColName.length==0 && arrField.length==0) {
			return false;
		}
		//字段名和字段内容必须相同
		if(arrColName.length != arrField.length) {
			return false;
		}
		//修改字段
		for(int i=0;i<arrColName.length;i++) {
			if(i==0) {
				sField += arrColName[i] + "='" + arrField[i] + "'";
			}
			else {
				sField += "," + arrColName[i] + "='" + arrField[i] + "'";
			}
		}
		//组成SQL语句
		try {
			sSql = "update " + sTableName + " set " + sField;
			if(sWhere != null) {
				if(sWhere.length()>0) {
					sSql += " where " + sWhere;
				}
			}
			SQLiteDatabase db = getWritableDatabase();
			db.execSQL(sSql);
			db.close();
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**删除数据
	 * @param sTableName 数据表名
	 * @param sWhere 删除条件
	 * @return 布尔类型是否操作成功
	 */
	public boolean deleteTable(String sTableName,String sWhere) {
		String sSql = "";
		if(sTableName.length()==0) {
			return false;
		}
		//组成SQL语句
		try {
			sSql = "delete from " + sTableName;
			if(sWhere != null) {
				if(sWhere.length()>0) {
					sSql += " where " + sWhere;
				}
			}
			SQLiteDatabase db = getWritableDatabase();
			db.execSQL(sSql);
			db.close();
		}
		catch(Exception e){
			return false;
		}
		return true;
	}
	/**查询数据总数
	 * @param sTableName 数据表名
	 * @param sWhere 查询条件
	 * @return Integer类型总数
	 */
	public Integer countTable(String sSql,String[] arrSelectionArgs) {
		Cursor cur = null;
		SQLiteDatabase db = null;
		int count=0;
		try {
			db = getReadableDatabase();  
		    cur = db.rawQuery(sSql, arrSelectionArgs);
		    if (cur.getCount()>0) {
		    	count=cur.getCount();
		    }
		}
		catch(Exception e){
			if(cur!=null){
				cur.close();
			}
			if(db!=null){
				db.close();
			}
			return count;
		}
		cur.close();
		db.close();
		return count;
	}

}
