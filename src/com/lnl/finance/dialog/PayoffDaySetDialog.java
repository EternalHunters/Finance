package com.lnl.finance.dialog;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import antistatic.spinnerwheel.AbstractWheel;
import antistatic.spinnerwheel.adapters.AbstractWheelTextAdapter;

import com.fourmob.datetimepicker.date.DatePickerDialog.OnDateSetListener;
import com.lnl.finance.R;

public class PayoffDaySetDialog extends DialogFragment {

	
	private ArrayList<String> dayArr = new ArrayList<String>();
	private AbstractWheel daySelect;
	
	private static OnSelectedListener onSelectedListener;
	
	private static PayoffDaySetDialog datePickerDialog;
	private static int defaultSeleted;
	public PayoffDaySetDialog() {
		// Empty constructor required for dialog fragment. DO NOT REMOVE
	}

	
	
	public static void setDatePickerDialog(PayoffDaySetDialog datePickerDialog) {
		PayoffDaySetDialog.datePickerDialog = datePickerDialog;
	}


	public static PayoffDaySetDialog newInstance(OnSelectedListener onSelectedListener1, int defaultSeleted1) {
		
		if(null==datePickerDialog){
			datePickerDialog = new PayoffDaySetDialog();
		}
		onSelectedListener = onSelectedListener1;
		defaultSeleted = defaultSeleted1;
		
		return datePickerDialog;
	}

	public void initialize(OnDateSetListener onDateSetListener, int year, int month, int day, boolean vibrate) {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Activity activity = getActivity();
		activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		dayArr.clear();
		dayArr.add("月初");
		dayArr.add("月末");
		for(int i=2;i<=28;i++){
			dayArr.add(i+"");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		View view = inflater.inflate(R.layout.dialog_payoff_day_set, null);
		
		daySelect = (AbstractWheel) view.findViewById(R.id.wvv_day_select);
		DayArrayAdapter adapter = new DayArrayAdapter(getActivity());
		daySelect.setViewAdapter(adapter);
		daySelect.setCurrentItem(defaultSeleted);
		
		
		Button doneButton = (Button)view.findViewById(R.id.done);
		doneButton.setOnClickListener(clickListener);

		return view;
	}
	
	private OnClickListener clickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			
			System.out.println("current seleted :"+daySelect.getCurrentItem());
			if(onSelectedListener!=null){
				
				int selectedDay = 1;
				if(daySelect.getCurrentItem()==0){
					selectedDay = 1;
				}else if(daySelect.getCurrentItem()==1){
					selectedDay = 0;
				}else if(daySelect.getCurrentItem()<dayArr.size()){
					selectedDay = Integer.valueOf(dayArr.get(daySelect.getCurrentItem()));
				}
				onSelectedListener.doneSelected(selectedDay);
			}
			dismiss();
		}
	};
	
	
	/**
     * Day adapter
     *
     */
    private class DayArrayAdapter extends AbstractWheelTextAdapter {
        // Count of days to be shown
        
        /**
         * Constructor
         */
        protected DayArrayAdapter(Context context) {
            super(context, R.layout.time_picker_custom_day, NO_RESOURCE);
            
            setItemTextResource(R.id.time2_monthday);
        }

        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            
            View view = super.getItem(index, cachedView, parent);

            TextView monthday = (TextView) view.findViewById(R.id.time2_monthday);
            monthday.setText(dayArr.get(index));
            
            return view;
        }
        
        @Override
        public int getItemsCount() {
            return dayArr.size();
        }
        
        @Override
        protected CharSequence getItemText(int index) {
            return "";
        }
    }
    
    public interface OnSelectedListener{
    	public void doneSelected(int selectedDay);
    }
}
