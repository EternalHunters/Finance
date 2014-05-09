package com.lnl.finance.dialog;

import java.text.DecimalFormat;
import java.util.Map;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lnl.finance.R;
import com.lnl.finance.util.AppUtil;
import com.lnl.finance.util.DBOperation;

public class FinanceModifyDialog extends DialogFragment {

	private static FinanceModifyDialog financeModifyDialog;
	private static Map<String, Object> financeItem;
	
	private EditText modifyEditText;
	
	private static OnFinanceModifyListener onFinanceModifyListener;
	
	public void setOnFinanceModifyListener(OnFinanceModifyListener onFinanceModifyListener){
		this.onFinanceModifyListener = onFinanceModifyListener;
	}
	
	public interface OnFinanceModifyListener{
		public void financeModify();
	}
	
	public FinanceModifyDialog() {
	}

	public static void setDatePickerDialog(FinanceModifyDialog financeModifyDialog) {
		FinanceModifyDialog.financeModifyDialog = financeModifyDialog;
	}


	public static FinanceModifyDialog newInstance(Map<String, Object> item, OnFinanceModifyListener onFinanceModifyListener1) {
		
		financeModifyDialog = new FinanceModifyDialog();
		financeItem = item;
		onFinanceModifyListener = onFinanceModifyListener1;
		return financeModifyDialog;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	private int maxLength = 0;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		final int dialogWidth = AppUtil.dip2px(getActivity(), 270);
		
		
		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		View view = inflater.inflate(R.layout.dialog_finance_modify, null);
		
		final TextView unitTextView = (TextView)view.findViewById(R.id.tv_unit);
		modifyEditText = (EditText)view.findViewById(R.id.et_modify_finance);
		
		Button delButton = (Button)view.findViewById(R.id.btn_delete);
		Button modifyButton = (Button)view.findViewById(R.id.btn_modify);
		delButton.setOnClickListener(delClickListener);
		modifyButton.setOnClickListener(modifyClickListener);
		
		String moneyString = "";
		try {
			DecimalFormat a = new DecimalFormat("0.##");
			moneyString = a.format(Double.valueOf(financeItem.get("f_money").toString()));
			
		} catch (Exception e) {
		}
		modifyEditText.setText(moneyString);
		
		modifyEditText.addTextChangedListener(new TextWatcher(){ 
 
            @Override 
            public void afterTextChanged(Editable s) { 
            	String temp = s.toString();
            	
            	if((maxLength!=0&&maxLength<=temp.length()+1)||(maxLength==0&&modifyEditText.getWidth()+unitTextView.getWidth()>dialogWidth-AppUtil.dip2px(getActivity(), 20)-15)){
            		if(maxLength==0)maxLength = temp.length();
            		
            		s.delete(temp.length()-1, temp.length());
            	}else{
            		 int posDot = temp.indexOf(".");
                     if (posDot <= 0) return;
                     if (temp.length() - posDot - 1 > 2)
                     {
                         s.delete(posDot + 3, posDot + 4);
                     }
            	}
            } 
            @Override 
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { 
            }
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			} 
        }); 
		
		String type = financeItem.get("f_type").toString();
		if("2".equals(type)){
			unitTextView.setTextColor(Color.rgb(223, 92, 92));
			modifyEditText.setTextColor(Color.rgb(223, 92, 92));
		}else{
			unitTextView.setTextColor(Color.rgb(37, 175, 63));
			modifyEditText.setTextColor(Color.rgb(37, 175, 63));
		}
		
		return view;
	}
	
	
	@Override
	public void onResume() {
		super.onResume();
		
		InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        //显示软键盘
        imm.showSoftInputFromInputMethod(modifyEditText.getWindowToken(), 0);
		
	}
	
	public void keyboardAction(View view){
		
		int screenWidth = AppUtil.getSreenWidth(getActivity());
		
		String numberStr = modifyEditText.getText().toString();
		String addStr = view.getTag().toString();
		//删除
		if("del".equals(addStr)){
			if(!"0".equals(numberStr)&&numberStr.length()>1){
				numberStr = numberStr.substring(0, numberStr.length()-1);
			}else{
				numberStr = "0";
			}
		}else if(screenWidth-50>modifyEditText.getWidth()+modifyEditText.getWidth()){
			
			if(".".equals(addStr)){
				
				if(numberStr.indexOf(".")!=-1){
					return;
				}
				numberStr = numberStr+addStr;
			}else{
				
				//判断是否已经到小数点两位
				if(numberStr.indexOf(".")!=-1){
					String[] numberStrs = numberStr.split("\\.");
					if(numberStrs.length>1&&numberStrs[1].length()>1){
						return;
					}
				}
				
				
				if("0".equals(numberStr)){
					numberStr = "";
				}
				numberStr = numberStr+addStr;
			}
		}
		modifyEditText.setText(numberStr);
	}
	
	private OnClickListener delClickListener = new OnClickListener(){
		@Override
		public void onClick(View v) {
			
			 new AlertDialog.Builder(getActivity()).setTitle("提示").setMessage("确定删除这项账单").setPositiveButton("确定", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					DBOperation.delFinance(getActivity(), financeItem);
					if(onFinanceModifyListener!=null){
						onFinanceModifyListener.financeModify();
					}
					dismiss();
				}
			}).setNegativeButton("取消",null).create().show();
		}
	};
	
	
	private OnClickListener modifyClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			
			try {
				String numberStr = modifyEditText.getText().toString();
				if("".equals(numberStr)||"0".equals(numberStr)){
					Toast.makeText(getActivity(), "请输入有效的内容", Toast.LENGTH_SHORT).show();
					return;
				}
				
				DBOperation.modifyFinance(getActivity(), financeItem, Double.valueOf(numberStr)+"");
				if(onFinanceModifyListener!=null){
					onFinanceModifyListener.financeModify();
				}
				dismiss();
			} catch (Exception e) {
				
				Toast.makeText(getActivity(), "请输入有效的内容", Toast.LENGTH_SHORT).show();
			}
			
			
		}
	};
}
