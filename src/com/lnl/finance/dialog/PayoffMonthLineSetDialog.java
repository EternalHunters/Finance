package com.lnl.finance.dialog;

import java.util.Map;

import android.app.AlertDialog;
import android.app.Dialog;
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
import com.lnl.finance.util.MySharedPreference;
import com.lnl.finance.util.StaticValue;

public class PayoffMonthLineSetDialog extends DialogFragment {

	private static PayoffMonthLineSetDialog financeModifyDialog;
	
	private EditText modifyEditText;
	
	private static OnPayoffMonthSetListener onPayoffMonthSetListener;
	
	public void setOnFinanceModifyListener(OnPayoffMonthSetListener onPayoffMonthSetListener){
		this.onPayoffMonthSetListener = onPayoffMonthSetListener;
	}
	
	public interface OnPayoffMonthSetListener{
		public void payoffMonthLineSetted();
	}
	
	public PayoffMonthLineSetDialog() {
	}

	public static void setDatePickerDialog(PayoffMonthLineSetDialog financeModifyDialog) {
		PayoffMonthLineSetDialog.financeModifyDialog = financeModifyDialog;
	}


	public static PayoffMonthLineSetDialog newInstance(OnPayoffMonthSetListener onPayoffMonthSetListener1) {
		
		financeModifyDialog = new PayoffMonthLineSetDialog();
		onPayoffMonthSetListener = onPayoffMonthSetListener1;
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
		View view = inflater.inflate(R.layout.dialog_payoff_month_line_set, null);
		
		final TextView unitTextView = (TextView)view.findViewById(R.id.tv_unit);
		modifyEditText = (EditText)view.findViewById(R.id.et_modify_finance);
		
		Button doneButton = (Button)view.findViewById(R.id.done);
		doneButton.setOnClickListener(doneClickListener);
		
		MySharedPreference sp = new MySharedPreference(getActivity());
		String monthPayoffLine = sp.getKeyStr(StaticValue.SP_PAYOFF_MONTH_LINE);
		modifyEditText.setText(monthPayoffLine.equals("")?"0":monthPayoffLine);
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
		
		unitTextView.setTextColor(Color.rgb(37, 175, 63));
		modifyEditText.setTextColor(Color.rgb(37, 175, 63));
		
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
	
	private OnClickListener doneClickListener = new OnClickListener(){
		@Override
		public void onClick(View v) {
			
			String modifyString = modifyEditText.getText().toString();
			if(modifyString==null || "".equals(modifyString) || "0".equals(modifyString)){
				new AlertDialog.Builder(getActivity()).setTitle("提示").setMessage("请先输入有效数字").setPositiveButton("确定", null).create().show();
				return;
			}
			
			MySharedPreference sp = new MySharedPreference(getActivity());
			sp.setKeyStr(StaticValue.SP_PAYOFF_MONTH_LINE, modifyString);
			if(onPayoffMonthSetListener!=null){
				onPayoffMonthSetListener.payoffMonthLineSetted();
			}
			
			dismiss();
		}
	};
	
}
