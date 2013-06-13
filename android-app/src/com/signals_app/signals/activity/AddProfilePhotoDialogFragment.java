package com.signals_app.signals.activity;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.signals_app.signals.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.text.Html;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class AddProfilePhotoDialogFragment extends DialogFragment 
{
	public static final int CAMERA_REQUEST = 1888; 
	public static final int GALLERY_REQUEST = 2888; 
	public static final int CROP_REQUEST = 3888; 
	
	
	private Uri mOriginalUri;
	private Uri mCropUri;
	
	private boolean mForRegister = false;
	
	public static AddProfilePhotoDialogFragment newInstance( boolean forRegister ) 
	{
	    AddProfilePhotoDialogFragment f = new AddProfilePhotoDialogFragment();
		
       // Get arguments passed in, if any
        Bundle args = f.getArguments();
        if (args == null) {
            args = new Bundle();
        }
        
        // Add parameters to the argument bundle
        args.putByte("forRegister", (byte)(forRegister?1:0));
        
        args.putParcelable("originalUri", Uri.fromFile(new File(Environment.getExternalStorageDirectory(),
                    "original" + String.valueOf(System.currentTimeMillis()) + ".jpg")));
        
        args.putParcelable("cropUri", Uri.fromFile(new File(Environment.getExternalStorageDirectory(),
                "crop" + String.valueOf(System.currentTimeMillis()) + ".jpg")));
        
        
        f.setArguments(args);
        
	    
		return f;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) 
	{
	    mForRegister = getArguments().getByte("forRegister") == 1;
	        
	    mOriginalUri = getArguments().getParcelable("originalUri");
	    mCropUri = getArguments().getParcelable("cropUri");
	    
	    
		setStyle(STYLE_NO_FRAME, 0);
	    
		ContextThemeWrapper context = new ContextThemeWrapper(getActivity(), R.style.SignalsThemeDialog);
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
	    builder.setTitle(getActivity().getString(R.string.add_a_photo));
	    builder.setView(getContentView()); 
		Dialog dialog = builder.create();

		return dialog;
	}
	
	@Override
    public void onDestroyView()
    {
        super.onDestroyView();
        
    }
	
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) 
	{
	    //savedInstanceState.putParcelable("originalUri", mOriginalUri);
	    //savedInstanceState.putParcelable("cropUri", mCropUri);
	    
	    // Always call the superclass so it can save the view hierarchy state
	    super.onSaveInstanceState(savedInstanceState);
	}
	
 

	private View getContentView() {
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View v = inflater.inflate(R.layout.dialog_add_profile_photo, null);
		ListView lv = (ListView)v.findViewById(R.id.photo_action_listview);
		
		TextView box = (TextView)v.findViewById(R.id.add_photo_text);
		box.setText(Html.fromHtml(getActivity().getString(R.string.add_photo_msg)));
		
		
		final PhotoActionsAdapter adapter = new PhotoActionsAdapter(getActivity(), new String[]{getActivity().getString(R.string.take_a_new_photo), getActivity().getString(R.string.choose_existing_photo)});
		
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(new OnItemClickListener() {


			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int which,long arg3) {
				
				switch( which )
				{
				case 0:
					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			 
			        Log.d("pathd", mOriginalUri.getPath()); 
			        
			        intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mOriginalUri);
			 
			        try {
				        intent.putExtra("return-data", true);
				 
				        startActivityForResult(intent, CAMERA_REQUEST);
			        } catch (ActivityNotFoundException e) {
			        	e.printStackTrace();
			        }
	                break;
	                
				case 1:
					Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
					photoPickerIntent.setType("image/*");
					startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
					break;
				}
				
			}
        });
		
		return v;
	}
		
	 
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
	    // because onActivityResult is called before onCreate
	    mOriginalUri = getArguments().getParcelable("originalUri");
        mCropUri = getArguments().getParcelable("cropUri");
        
		switch( requestCode )
		{
			case GALLERY_REQUEST:
				if(resultCode == RegisterActivity.RESULT_OK)
				{
					mOriginalUri = data.getData();
					
					getArguments().putParcelable("originalUri", mOriginalUri);
					
					doCrop();
				}
				break;
				
			case CAMERA_REQUEST:
				if(resultCode == RegisterActivity.RESULT_OK)
				{
		            doCrop();
				}
				break;
				
			case CROP_REQUEST:
				if(resultCode == RegisterActivity.RESULT_OK)
				{
					/*
					Bundle extras = data.getExtras();
					 
		            if (extras != null) {
		            	RegisterActivity act = (RegisterActivity)getActivity();
		            	
		            	act.setProfilePic((Bitmap)extras.getParcelable("data"));
		            }
					*/
				    
					Bundle extras = data.getExtras(); 
					 
		            if (extras != null) 
		            {
		                if( mForRegister )
		                {
    		                RegisterActivity act = (RegisterActivity)getActivity();

                            /*
    		                if( act != null )
    		                    act.setProfilePhotosUris(getRealPathFromURI(mOriginalUri), mCropUri.getPath());
    		                    */
		                }
		                else
		                {
		                    MyProfileEditPhotosFragment f = (MyProfileEditPhotosFragment)getActivity().getSupportFragmentManager().findFragmentByTag("MyProfileEditPhotosFragment");
	                        
	                        if( f != null )
	                            f.uploadProfilePhoto(getRealPathFromURI(mOriginalUri), mCropUri.getPath());
		                }
		                
		                this.dismiss();
		                
		            }
		            
			        
				}
				break;
		}
		
	}
	
	public String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = getActivity().getContentResolver().query(contentUri, proj, null, null, null);
        
        if( cursor != null )
        {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        else
            return contentUri.getPath();
    }


	
	private void doCrop() {
        final ArrayList<CropOption> cropOptions = new ArrayList<CropOption>(); 
 
        Intent intent = new Intent(getActivity(), com.android.camera.CropImage.class);
        intent.setType("image/*");
 
        List<ResolveInfo> list = getActivity().getPackageManager().queryIntentActivities( intent, 0 );
 
        int size = list.size();
 
        if (size == 0) {
            Toast.makeText(getActivity(), "Can not find image crop app", Toast.LENGTH_SHORT).show();
 
            return;
        } 
        else {
            intent.setData(mOriginalUri);
 
            intent.putExtra("outputX", 500);
            intent.putExtra("outputY", 500); 
            intent.putExtra("aspectX", 1); 
            intent.putExtra("aspectY", 1);
            intent.putExtra("scale", true);
 
            // moi
            
	 
            intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mCropUri);
	        // end-moi 
	        
            if (size == 1) {
                Intent i        = new Intent(intent);
                ResolveInfo res = list.get(0);
 
                i.setComponent( new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
 

		        
                startActivityForResult(i, CROP_REQUEST);
            } 
            else {
                for (ResolveInfo res : list) {
                    final CropOption co = new CropOption();
 
                    co.title    = getActivity().getPackageManager().getApplicationLabel(res.activityInfo.applicationInfo);
                    co.icon     = getActivity().getPackageManager().getApplicationIcon(res.activityInfo.applicationInfo);
                    co.appIntent= new Intent(intent);
 
                    co.appIntent.setComponent( new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
 
                    cropOptions.add(co);
                }
 
                CropOptionAdapter adapter = new CropOptionAdapter(getActivity().getApplicationContext(), cropOptions);
 
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Choose Crop App");
                builder.setAdapter( adapter, new DialogInterface.OnClickListener() {
                    public void onClick( DialogInterface dialog, int item ) {
                    	startActivityForResult( cropOptions.get(item).appIntent, CROP_REQUEST);
                    }
                });
 
                builder.setOnCancelListener( new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel( DialogInterface dialog ) {
 
                        if (mOriginalUri != null ) {
                        	getActivity().getContentResolver().delete(mOriginalUri, null, null );
                            mOriginalUri = null;
                        }
                    }
                } );
 
                AlertDialog alert = builder.create();
 
                alert.show();
            }
        }
    }

	public class CropOptionAdapter extends ArrayAdapter<CropOption> {
        private ArrayList<CropOption> mOptions;
        private LayoutInflater mInflater;

        public CropOptionAdapter(Context context, ArrayList<CropOption> options) {
                super(context, R.layout.row_crop, options);

                // ArrayList containing data (CropOption) for all CROP applications installed on the device
                mOptions        = options;

                mInflater       = LayoutInflater.from(context);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup group) {
                if (convertView == null)
                        convertView = mInflater.inflate(R.layout.row_crop, null);

                // gets the CropOption data for the current position in list and then sets the icon and the app name
                CropOption item = mOptions.get(position);

                if (item != null) {
                        ((ImageView) convertView.findViewById(R.id.iv_icon)).setImageDrawable(item.icon);
                        ((TextView) convertView.findViewById(R.id.tv_name)).setText(item.title);

                        return convertView;
                }

                return null;
        }
	}

	
	public class CropOption {
	    public CharSequence title;
	    public Drawable icon;
	    public Intent appIntent;
	}
	
	
	public class PhotoActionsAdapter extends ArrayAdapter<String> {
		private final Context context;
		private final String[] values;
	 
		public PhotoActionsAdapter(Context context, String[] values) {
			super(context, R.layout.row_photo_action, values);
			this.context = context;
			this.values = values;
		}
	 
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	 
			View rowView = inflater.inflate(R.layout.row_photo_action, parent, false);
			TextView textView = (TextView) rowView.findViewById(R.id.photo_action_text);
			textView.setText(values[position]);
			
			return rowView;
		}
	}

	


}
