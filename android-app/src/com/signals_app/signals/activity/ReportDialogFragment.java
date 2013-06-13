package com.signals_app.signals.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import com.signals_app.signals.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.text.Html;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class ReportDialogFragment extends DialogFragment {

	private ProfileFragment mParentFrag;
	
	public static ReportDialogFragment newInstance( ProfileFragment parent ) 
	{
		ReportDialogFragment dialogFragment = new ReportDialogFragment(parent);
		return dialogFragment;
	}
	
	public ReportDialogFragment()
	{
		this( null );
	}
	
	public ReportDialogFragment( ProfileFragment parent )
	{
		mParentFrag = parent;
	}
	
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
	
		//setStyle(STYLE_NO_FRAME, 0);
	    
		//ContextThemeWrapper context = new ContextThemeWrapper(getActivity(), R.style.SignalsThemeDialog);
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    builder.setTitle(getActivity().getString(R.string.report_user));
	    builder.setView(getContentView());
	    
		Dialog dialog = builder.create();

		return dialog;
	}
	


	private View getContentView() {
		LayoutInflater inflater = getActivity().getLayoutInflater();
		final View view = inflater.inflate(R.layout.dialog_report, null);
		
		final RadioGroup reportRadioGroup = (RadioGroup) view.findViewById(R.id.report_radio_group);
		final EditText commentsEditText = (EditText) view.findViewById(R.id.comments_edit_text);
		Button submitBtn = (Button) view.findViewById(R.id.submit_btn);
	 
		submitBtn.setOnClickListener(new OnClickListener() {
	 
			@Override
			public void onClick(View v) {
	 
			    String comments = commentsEditText.getText().toString();
			    Resources res = getResources();
			    
			    if( comments.length() < 10 )
			    {
			    	Toast.makeText(getActivity(), res.getString(R.string.error_report_short_comment) , Toast.LENGTH_LONG).show();
			    	return;
			    }
			    
				int selectedId = reportRadioGroup.getCheckedRadioButtonId();
			    RadioButton reportRadioBtn = (RadioButton) view.findViewById(selectedId);
			    String reportTypeStr = reportRadioBtn.getText().toString();
			    int reportType = 0;
			    
			    if( reportTypeStr.equals(res.getString(R.string.photo_issue)) )
			    {
			    	reportType = 1;
			    }
			    else if( reportTypeStr.equals(res.getString(R.string.photo_content_issue)) )
			    {
			    	reportType = 2;
			    }
			    else if( reportTypeStr.equals(res.getString(R.string.underage_user)) )
			    {
			    	reportType = 3;
			    }
			    else if( reportTypeStr.equals(res.getString(R.string.scammer)) )
			    {
			    	reportType = 4;
			    }
			    else if( reportTypeStr.equals(res.getString(R.string.fake_profile)) )
			    {
			    	reportType = 5;
			    }
			    else if( reportTypeStr.equals(res.getString(R.string.innapropriate_behavior)) )
			    {
			    	reportType = 6;
			    }
			    else if( reportTypeStr.equals(res.getString(R.string.other)) )
			    {
			    	reportType = 7;
			    }
			    
			    
			    mParentFrag.sendReport(reportType, comments);
			}
	 
		});
		
		return view;
	}
		
	
}
