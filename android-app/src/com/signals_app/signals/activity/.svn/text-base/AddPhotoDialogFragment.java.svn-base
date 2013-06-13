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

public class AddPhotoDialogFragment extends DialogFragment 
{
    public static final int CAMERA_REQUEST = 1888; 
    public static final int GALLERY_REQUEST = 2888; 
    public static final int CROP_REQUEST = 3888; 
    
    
    private Uri mOriginalUri;
    
    public static AddPhotoDialogFragment newInstance() 
    {
        AddPhotoDialogFragment f = new AddPhotoDialogFragment();
        
       // Get arguments passed in, if any
        Bundle args = f.getArguments();
        if (args == null) {
            args = new Bundle();
        }
        
        args.putParcelable("originalUri", Uri.fromFile(new File(Environment.getExternalStorageDirectory(),
                "profile" + String.valueOf(System.currentTimeMillis()) + ".jpg")));
        
        // Add parameters to the argument bundle
        f.setArguments(args);
        
        
        return f;
    }
    
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) 
    {
        mOriginalUri = getArguments().getParcelable("originalUri");

        
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
        
        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }
    
 

    private View getContentView() {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_add_photo, null);
        ListView lv = (ListView)v.findViewById(R.id.photo_action_listview);
        
        
        final PhotoActionsAdapter adapter = new PhotoActionsAdapter(getActivity(), new String[]{getActivity().getString(R.string.take_a_new_photo), getActivity().getString(R.string.choose_existing_photo)});
        
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int which,long arg3) {
                
                switch( which )
                {
                case 0:
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    
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
        mOriginalUri = getArguments().getParcelable("originalUri");
        
        switch( requestCode )
        {
            case GALLERY_REQUEST:
                if(resultCode == MainActivity.RESULT_OK)
                {
                    mOriginalUri = data.getData();
                    
                    MyProfileEditPhotosFragment f = (MyProfileEditPhotosFragment)getActivity().getSupportFragmentManager().findFragmentByTag("MyProfileEditPhotosFragment");
                    
                    if( f != null )
                        f.uploadPhoto(getRealPathFromURI(mOriginalUri));
                    
                    this.dismiss();
                }
                break;
                
            case CAMERA_REQUEST:
                if(resultCode == MainActivity.RESULT_OK)
                {
                    MyProfileEditPhotosFragment f = (MyProfileEditPhotosFragment)getActivity().getSupportFragmentManager().findFragmentByTag("MyProfileEditPhotosFragment");
                    
                    if( f != null )
                        f.uploadPhoto(getRealPathFromURI(mOriginalUri));
                    
                    this.dismiss();
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
