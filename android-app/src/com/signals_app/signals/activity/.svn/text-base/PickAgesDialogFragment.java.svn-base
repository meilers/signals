package com.signals_app.signals.activity;

import com.actionbarsherlock.app.SherlockDialogFragment;
import com.signals_app.signals.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;

public class PickAgesDialogFragment extends SherlockDialogFragment {
	
	private final int MIN_AGE = 18;
	private final int MAX_AGE = 99;
	
	private int mWantedMinAge = 18;
	private int mWantedMaxAge= 99;
	
	private NumberPicker mPicker1;
	private NumberPicker mPicker2;
	
	public static DialogFragment newInstance(int wantedMinAge, int wantedMaxAge) 
	{
		
		PickAgesDialogFragment dialogFragment = new PickAgesDialogFragment();
		dialogFragment.setWantedMinAge(wantedMinAge);
		dialogFragment.setWantedMaxAge(wantedMaxAge);
		
		return dialogFragment;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
	
		setStyle(STYLE_NO_FRAME, 0);
	    
		//ContextThemeWrapper context = new ContextThemeWrapper(getActivity(), R.style.SignalsThemeDialog);
		AlertDialog.Builder builder = new AlertDialog.Builder(getSherlockActivity());
	    builder.setTitle(getActivity().getString(R.string.between_the_ages_of));
	    builder.setView(getContentView());
	    builder.setPositiveButton(getActivity().getResources().getString(R.string.done), new DialogInterface.OnClickListener() {                     
            @Override
            public void onClick(DialogInterface dialog, int which) {
            	if( getActivity() instanceof RegisterActivity)
            	{
                    Register1Fragment f = (Register1Fragment)getSherlockActivity().getSupportFragmentManager().findFragmentByTag("Register1Fragment");
                    f.setWantedAges(mPicker1.getCurrent(), mPicker2.getCurrent());
            	}
            	else
            	{
            	    MyProfileEditBasicInfoFragment f = (MyProfileEditBasicInfoFragment)getActivity().getSupportFragmentManager().findFragmentByTag("MyProfileEditBasicInfoFragment");
                    f.setWantedAges(mPicker1.getCurrent(), mPicker2.getCurrent());
            	}
            } 
        });
	    
		Dialog dialog = builder.create();

		return dialog;
	}
	
 

	private View getContentView() {
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View v = inflater.inflate(R.layout.dialog_pick_ages, null);
		
		mPicker1 = (NumberPicker)v.findViewById(R.id.num_picker_1);
		mPicker1.setRange(MIN_AGE, MAX_AGE);
		mPicker1.setCurrent(getWantedMinAge());
		
		mPicker2 = (NumberPicker)v.findViewById(R.id.num_picker_2);
		mPicker2.setRange(MIN_AGE, MAX_AGE);
		mPicker2.setCurrent(getWantedMaxAge());
		
		return v;
	}
	
	public void setWantedMinAge( int wantedMinAge )
	{
		mWantedMinAge = wantedMinAge;
	}
	
	public void setWantedMaxAge( int wantedMaxAge )
	{
		mWantedMaxAge = wantedMaxAge;
	}
	
	public int getWantedMinAge( )
	{
		return mWantedMinAge;
	}
	
	public int getWantedMaxAge( )
	{
		return mWantedMaxAge;
	}
	
}
