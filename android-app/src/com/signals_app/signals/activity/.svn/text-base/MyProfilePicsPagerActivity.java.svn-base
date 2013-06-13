package com.signals_app.signals.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.signals_app.signals.R;
import com.signals_app.signals.activity.AddProfilePhotoDialogFragment.CropOption;
import com.signals_app.signals.activity.AddProfilePhotoDialogFragment.CropOptionAdapter;
import com.signals_app.signals.activity.PicsPagerActivity.DetailOnPageChangeListener;
import com.signals_app.signals.model.Profile.Person;
import com.signals_app.signals.model.Profile.User;
import com.signals_app.signals.tasks.photos.DeletePhotoTaskMethod;
import com.signals_app.signals.tasks.photos.DeletePhotoTaskMethod.DeletePhotoTaskEvent;
import com.signals_app.signals.tasks.photos.DeletePhotoTaskMethod.DeletePhotoTaskListener;
import com.signals_app.signals.tasks.photos.MakeProfilePhotoTaskMethod;
import com.signals_app.signals.tasks.photos.MakeProfilePhotoTaskMethod.MakeProfilePhotoTaskEvent;
import com.signals_app.signals.tasks.photos.MakeProfilePhotoTaskMethod.MakeProfilePhotoTaskListener;
import com.signals_app.signals.tasks.photos.UploadPhotoTaskMethod;
import com.signals_app.signals.tasks.photos.UploadProfilePhotoTaskMethod.UploadProfilePhotoTaskListener;
import com.signals_app.signals.util.Utility;

public class MyProfilePicsPagerActivity extends SherlockFragmentActivity implements DeletePhotoTaskListener, MakeProfilePhotoTaskListener
{
    public static final int CROP_REQUEST = 3888; 
    
    public static final int MENU_SET_AS_PROFILE_PIC = 0;
    public static final int MENU_DELETE_PIC = 1;
    
    private DeletePhotoTaskMethod mDeletePhotoTaskMethod;
    private MakeProfilePhotoTaskMethod mMakeProfilePhotoTaskMethod;
    
    
    private DisplayImageOptions mImageOptions;
    
    
    private ViewPager mViewPager;
    private PicsAdapter mViewPagerAdapter;
    
    private Uri mOriginalUri;
    private Uri mCropUri;
    
    
    
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_PROGRESS);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_my_pics_pager); 
        
        Bundle extras = getIntent().getExtras(); 
        ArrayList<String> urls = extras.getStringArrayList("urls");
         
        
        mDeletePhotoTaskMethod = new DeletePhotoTaskMethod(this);
        mDeletePhotoTaskMethod.addDeletePhotoTaskListener(this);
        
        mMakeProfilePhotoTaskMethod = new MakeProfilePhotoTaskMethod(this);
        mMakeProfilePhotoTaskMethod.addMakeProfilePhotoTaskListener(this);
        
        
        // IMAGE LOADER
        mImageOptions = new DisplayImageOptions.Builder()
        .cacheInMemory()
        .cacheOnDisc()
        .build();
        

        
        mViewPager = (ViewPager) findViewById(R.id.pager); 
        mViewPagerAdapter = new PicsAdapter(this,  urls);
        mViewPager.setAdapter(mViewPagerAdapter);
        
        
        int pos = extras.getInt("pos");
        mCropUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),"crop" + String.valueOf(System.currentTimeMillis()) + ".jpg"));
        mOriginalUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "original" + String.valueOf(System.currentTimeMillis()) + ".jpg"));
        
        if (savedInstanceState != null) 
        {
            pos = savedInstanceState.getInt("pos");
            mCropUri = savedInstanceState.getParcelable("cropUri");
            mOriginalUri = savedInstanceState.getParcelable("originalUri");
        }    
        
        
        mViewPager.setCurrentItem(pos);
        mViewPager.setOnPageChangeListener(new DetailOnPageChangeListener()); 

        setTitle(extras.getString("username"));
        getSupportActionBar().setSubtitle(getString(R.string.photo) + " " + (pos+1) + "/" + urls.size());
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        
        mDeletePhotoTaskMethod.removeDeletePhotoTaskListener(this);
        mDeletePhotoTaskMethod.cleanUp();
        
        mMakeProfilePhotoTaskMethod.removeMakeProfilePhotoTaskListener(this);
        mMakeProfilePhotoTaskMethod.cleanUp();
    }
    
    
    
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) 
    {
        int position = mViewPager.getCurrentItem();
        
        savedInstanceState.putInt("pos", position);
        savedInstanceState.putParcelable("cropUri", mCropUri);
        savedInstanceState.putParcelable("originalUri", mOriginalUri);
    }
    
    
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) 
    {
        switch( requestCode )
        {
            case CROP_REQUEST:
                if(resultCode == MyProfilePicsPagerActivity.RESULT_OK)
                {
                    try {
                        mMakeProfilePhotoTaskMethod.doTask(User.getInstance().getUserId(), Utility.decodeFile(new File(mCropUri.getPath())));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    
                }
                break;
                
        }
    }
    
    
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
        super.onCreateOptionsMenu(menu);
        
        User u = User.getInstance();
        
        menu.add(0,0,MENU_SET_AS_PROFILE_PIC,"Profile")
        .setIcon( u.getSex() == Person.SEX_FEMALE ? R.drawable.lady_grey : R.drawable.gent_grey)
        .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        
        menu.add(0,0,MENU_DELETE_PIC,"Delete")
        .setIcon( R.drawable.ic_action_delete)
        .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

        return true;
    }
    
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        AlertDialog.Builder alertDialogBuilder;
        AlertDialog alertDialog;
        
        
        switch(item.getOrder())
        {
            case MENU_SET_AS_PROFILE_PIC:
                alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setTitle(getString(R.string.make_profile_picture));
     
                // set dialog message
                alertDialogBuilder
                    .setCancelable(true)
                    .setPositiveButton(getString(R.string.yes),new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) 
                        {
                            final FileOutputStream out;
                            
                            try {
                                out = new FileOutputStream(mOriginalUri.getPath());
                                
                                final ImageView iv = new ImageView(MyProfilePicsPagerActivity.this);
                                iv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                                ImageLoader.getInstance().displayImage(mViewPagerAdapter.mUrls.get(mViewPager.getCurrentItem()), iv, mImageOptions, new SimpleImageLoadingListener() {
                                    
                                    @Override
                                    public void onLoadingStarted(String imageUri, View view) {
                                    }

                                    @Override
                                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                                    }
                                    
                                    @Override
                                    public void onLoadingCancelled(String imageUri, View view) {
                                    }

                                    @Override
                                    public void onLoadingComplete(String imageUri,View view, Bitmap loadedImage) {
                                        if( MyProfilePicsPagerActivity.this != null )
                                        {
                                            setSupportProgressBarIndeterminateVisibility(false);
                                            
                                            loadedImage.compress(Bitmap.CompressFormat.JPEG, 100, out);
                                            doCrop();
                                        }
                                    }
                                    
                                    
                                    
                                });
                                
                                
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                            
                        }
                      })
                    .setNegativeButton(getString(R.string.cancel),new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            dialog.cancel();
                        }
                    });
     
                    // create alert dialog
                    alertDialog = alertDialogBuilder.create();
     
                    // show it
                    alertDialog.show();

                
                return true;
                
            case MENU_DELETE_PIC:
                
                alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setTitle(getString(R.string.delete));
     
                // set dialog message
                alertDialogBuilder
                    .setMessage(getString(R.string.are_you_sure_you_want_to_delete_this_photo))
                    .setCancelable(true)
                    .setPositiveButton(getString(R.string.delete),new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) 
                        {
                            String url = mViewPagerAdapter.mUrls.get(mViewPager.getCurrentItem());
                            String[] parts = url.split("/");
                            String path = "/" + parts[parts.length-2] + "/" + parts[parts.length-1];
                            
                            
                            mDeletePhotoTaskMethod.doTask(User.getInstance().getUserId(), path);
                        }
                      })
                    .setNegativeButton(getString(R.string.cancel),new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            dialog.cancel();
                        } 
                    });
     
                    // create alert dialog
                    alertDialog = alertDialogBuilder.create();
     
                    // show it
                    alertDialog.show();
                    
                    
                return true;
                
        }
        
        return false;
    }
    
    public class PicsAdapter extends PagerAdapter 
    {
        private final ArrayList<String> mUrls;
        private final Context mContext;

        public PicsAdapter(Context context, ArrayList<String> urls) {
            mUrls = urls;
            mContext = context;
        }

        @Override
        public int getCount() { 
            return null != mUrls ? mUrls.size() : 0;
        }

        @Override
        public View instantiateItem( ViewGroup container, int position) 
        {
            final ImageView imageView = new ImageView(mContext);
            imageView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            ImageLoader.getInstance().displayImage(mUrls.get(position), imageView, mImageOptions, new SimpleImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {
                    if( MyProfilePicsPagerActivity.this != null )
                        MyProfilePicsPagerActivity.this.setSupportProgressBarIndeterminateVisibility(true);
                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    if( this != null )
                        MyProfilePicsPagerActivity.this.setSupportProgressBarIndeterminateVisibility(false);
                }
                
                @Override
                public void onLoadingCancelled(String imageUri, View view) {
                    if( MyProfilePicsPagerActivity.this != null )
                        MyProfilePicsPagerActivity.this.setSupportProgressBarIndeterminateVisibility(false);
                }

                @Override
                public void onLoadingComplete(String imageUri,View view, Bitmap loadedImage) {
                    if( MyProfilePicsPagerActivity.this != null )
                        MyProfilePicsPagerActivity.this.setSupportProgressBarIndeterminateVisibility(false);
                }
            });
            
            container.addView(imageView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
        
        @Override
        public int getItemPosition(Object object){
            return PagerAdapter.POSITION_NONE;
        }
    }

    public class DetailOnPageChangeListener extends ViewPager.SimpleOnPageChangeListener {

        private int currentPage;

        @Override
        public void onPageSelected(int position) {

            getSupportActionBar().setSubtitle(getString(R.string.photo) + " " + (position+1) + "/" + getIntent().getExtras().getStringArrayList("urls").size());
            currentPage = position;
        }

        public int getCurrentPage() {
            return currentPage;
        }
    }

    
    
    private void doCrop() 
    {
        final ArrayList<CropOption> cropOptions = new ArrayList<CropOption>(); 
 
        Intent intent = new Intent(this, com.android.camera.CropImage.class);
        intent.setType("image/*");
 
        List<ResolveInfo> list = getPackageManager().queryIntentActivities( intent, 0 );
 
        int size = list.size();
 
        if (size == 0) {
            Toast.makeText(this, "Can not find image crop app", Toast.LENGTH_SHORT).show();
 
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
 
                    co.title    = getPackageManager().getApplicationLabel(res.activityInfo.applicationInfo);
                    co.icon     = getPackageManager().getApplicationIcon(res.activityInfo.applicationInfo);
                    co.appIntent= new Intent(intent);
 
                    co.appIntent.setComponent( new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
 
                    cropOptions.add(co);
                }
 
                CropOptionAdapter adapter = new CropOptionAdapter(getApplicationContext(), cropOptions);
 
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
                            getContentResolver().delete(mOriginalUri, null, null );
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
    
    
    
    
    // EVENTS
    
    @Override
    public void deletePhotoTaskEventReceived(DeletePhotoTaskEvent evt) {
        DeletePhotoTaskMethod met = (DeletePhotoTaskMethod)evt.getSource();
        
        if( met != null && met.success() )
        {   
           
            
            int position = mViewPager.getCurrentItem();
            mViewPagerAdapter.mUrls.remove(position);
            mViewPagerAdapter.notifyDataSetChanged();
            
            // Current snapshot
            User u = User.getInstance();
            ArrayList<String> urls = new ArrayList<String>(mViewPagerAdapter.mUrls); 
            ArrayList<String> profilePhotosFilenames = new ArrayList<String>(); 
            ArrayList<String> otherPhotosFilenames = new ArrayList<String>(); 
            
            for( String url : urls )
            {
                String[] parts = url.split("/");
                
                if( parts[parts.length-2].equals("profilePhotos")) 
                    profilePhotosFilenames.add(parts[parts.length-1]);
                else
                    otherPhotosFilenames.add(parts[parts.length-1]);
                    
            }
            
            u.setProfilePhotosFilenames(profilePhotosFilenames);
            u.setOtherPhotosFilenames(otherPhotosFilenames);
        }
    }

    @Override
    public void makeProfilePhotoTaskEventReceived(MakeProfilePhotoTaskEvent evt) {
        MakeProfilePhotoTaskMethod met = (MakeProfilePhotoTaskMethod)evt.getSource();
        
        if( met != null && met.success() )
        {   
            
        }
    }
    


}
