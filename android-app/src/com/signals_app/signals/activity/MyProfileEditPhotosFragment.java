package com.signals_app.signals.activity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.signals_app.signals.R;
import com.signals_app.signals.model.Profile.Person;
import com.signals_app.signals.model.Profile.User;
import com.signals_app.signals.tasks.photos.UploadPhotoTaskMethod;
import com.signals_app.signals.tasks.photos.UploadProfilePhotoTaskMethod;
import com.signals_app.signals.tasks.photos.UploadPhotoTaskMethod.UploadPhotoTaskEvent;
import com.signals_app.signals.tasks.photos.UploadPhotoTaskMethod.UploadPhotoTaskListener;
import com.signals_app.signals.tasks.photos.UploadProfilePhotoTaskMethod.UploadProfilePhotoTaskEvent;
import com.signals_app.signals.tasks.photos.UploadProfilePhotoTaskMethod.UploadProfilePhotoTaskListener;
import com.signals_app.signals.util.Utility;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;


public class MyProfileEditPhotosFragment extends SherlockFragment implements UploadProfilePhotoTaskListener, UploadPhotoTaskListener
{
    public static final int MENU_ADD_PHOTO = 0;
    
    private String mUsername;
    private ArrayList<String> mProfilePhotoFilenames;
    
    
    
    private DisplayImageOptions mImageOptions;
    
    private UploadProfilePhotoTaskMethod mUploadProfilePhotoTaskMethod;
    private UploadPhotoTaskMethod mUploadPhotoTaskMethod;
    
    private int mNbPhotosLoaded = 0;
    
    
    public static final MyProfileEditPhotosFragment newInstance( User u )
    {
        MyProfileEditPhotosFragment f = new MyProfileEditPhotosFragment();
        
        // Get arguments passed in, if any
        Bundle args = f.getArguments();
        if (args == null) {
            args = new Bundle();
        }
        
        // Add parameters to the argument bundle
        args.putString("username", u.getUsername());
        args.putStringArrayList("profilePhotoFilenames", u.getProfilePhotosFilenames());
        f.setArguments(args);
        
        return f;
    }
    
    
   
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
 
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
        Bundle args = getArguments();
        
        if (savedInstanceState != null) 
            args = savedInstanceState;
        
        mUsername = args.getString("username");
        mProfilePhotoFilenames = args.getStringArrayList("profilePhotoFilenames");
        
        View view = inflater.inflate(R.layout.fragment_my_profile_edit_photos, container, false);  
        getSherlockActivity().setTitle(mUsername);
        getSherlockActivity().getSupportActionBar().setSubtitle(mProfilePhotoFilenames.size() + " " + getResources().getString(R.string.photos));
        
        mImageOptions = new DisplayImageOptions.Builder()
        .cacheInMemory()
        .cacheOnDisc()
        .displayer(new RoundedBitmapDisplayer(10))
        .build();
        
        mUploadProfilePhotoTaskMethod = new UploadProfilePhotoTaskMethod(getSherlockActivity());
        mUploadProfilePhotoTaskMethod.addUploadProfilePhotoTaskListener(this);
        
        mUploadPhotoTaskMethod = new UploadPhotoTaskMethod(getSherlockActivity());
        mUploadPhotoTaskMethod.addUploadPhotoTaskListener(this);
        
        updateView(view);
        
        setHasOptionsMenu(true);
        
        return view;
    }
    
    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        
        mUploadProfilePhotoTaskMethod.removeUploadProfilePhotoTaskListener(this);
        mUploadPhotoTaskMethod.removeUploadPhotoTaskListener(this);
        
        mUploadProfilePhotoTaskMethod.cleanUp();
        mUploadPhotoTaskMethod.cleanUp();
    } 
    
    @Override
    public void onStart()
    {
        super.onStart();
    }
    
    @Override
    public void onStop()
    {
        super.onStop();
    }
    
    @Override
    public void onResume()
    {
        super.onResume();
        
       
    }
    
    // Fragment no longer visible
    @Override
    public void onPause()
    {
        super.onPause();
    }


    
    
    @Override
    public void onSaveInstanceState(Bundle outState) 
    {
        if (outState != null) 
        {
            // bug sinon, car il appelle dautres fragments dans le backstack
            super.onSaveInstanceState(outState);
            outState.putString("username", mUsername);
            outState.putStringArrayList("profilePhotoFilenames", mProfilePhotoFilenames);
        }
    }
    
    
    
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Add your menu entries here
        super.onCreateOptionsMenu(menu, inflater);
        
        menu.add(0,0,MENU_ADD_PHOTO,getString(R.string.add))
        .setIcon( R.drawable.plus)
        .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
    }
    
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getOrder())
        {
            case MENU_ADD_PHOTO:
                onClickAddPhoto();
                
                break;
        }
        
        return false;
    }
    
    
    
    private void updateView(View view)
    {
        
        final Person person = User.getInstance();
        
        int padding = (int)Utility.convertDpToPixel(5, getSherlockActivity());
        DisplayMetrics metrics = new DisplayMetrics();
        getSherlockActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        
        
        
        // PROFILE PHOTOS 

        LinearLayout profilePhotosLayout = (LinearLayout)view.findViewById(R.id.profile_photos_layout);
        ArrayList<String> profilePhotoFilenames = person.getProfilePhotosFilenames();
        
        profilePhotosLayout.removeAllViews();
        mNbPhotosLoaded = 0;
        
        int profilePhotoItr = 0;
        profilePhotosLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        TableLayout tableLayout = new TableLayout(getSherlockActivity());
        tableLayout.setPadding(0, 0, 0, (int)Utility.convertDpToPixel(15, getSherlockActivity()));
        tableLayout.setStretchAllColumns(false);
        
        
        while( profilePhotoItr < profilePhotoFilenames.size() )
        {
            TableRow tableRow = new TableRow(getSherlockActivity());
            tableRow.setGravity(Gravity.LEFT|Gravity.CENTER_VERTICAL);
            tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));

            for (int i = 0; i < 4 && profilePhotoItr < profilePhotoFilenames.size(); ++i) 
            {
                ImageView iv = new ImageView(getSherlockActivity());
                
                iv.setLayoutParams(new TableRow.LayoutParams((metrics.widthPixels)/4, (metrics.widthPixels)/4));
                iv.setPadding(padding, padding, padding, padding);
                iv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                iv.setAdjustViewBounds(false);
                
                iv.setTag(Integer.valueOf(profilePhotoItr));
                iv.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        onClickPhoto( person, (Integer)arg0.getTag() );
                    }
                });
                
                iv.setClickable(true);
                Animation fadeInAnimation = AnimationUtils.loadAnimation(getSherlockActivity(), R.anim.fadein);
                ImageLoader.getInstance().displayImage("http://s3.amazonaws.com/signals/user_images/" + person.getUserId() + "/profilePhotos/" + profilePhotoFilenames.get(profilePhotoItr), iv, mImageOptions, new SimpleImageLoadingListener() {

                    
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                        if( getSherlockActivity() != null )
                            getSherlockActivity().setSupportProgressBarIndeterminateVisibility(true);
                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                        if( getSherlockActivity() != null )
                            getSherlockActivity().setSupportProgressBarIndeterminateVisibility(false);
                    }
                    
                    @Override
                    public void onLoadingCancelled(String imageUri, View view) {
                        if( getSherlockActivity() != null )
                            getSherlockActivity().setSupportProgressBarIndeterminateVisibility(false);
                    }

                    @Override
                    public void onLoadingComplete(String imageUri,View view, Bitmap loadedImage) {
                        
                        ++mNbPhotosLoaded;
                        
                        if( mNbPhotosLoaded == person.getProfilePhotosFilenames().size()  && getSherlockActivity() != null )
                            getSherlockActivity().setSupportProgressBarIndeterminateVisibility(false);
                    }
                });
                
                iv.startAnimation(fadeInAnimation); 

                tableRow.addView(iv);
                
                
                ++profilePhotoItr;
            }
            tableRow.setGravity(Gravity.LEFT);
            tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, Gravity.LEFT));
            tableLayout.addView(tableRow);
        }

        profilePhotosLayout.addView(tableLayout);
            
            
        
       
    }
    
    
    
    public void uploadProfilePhoto( String originalPath, String cropPath )
    {
        User u = User.getInstance();
        byte[] original;
        byte[] crop;
        try {
            original = Utility.decodeFile(new File(originalPath));
            crop = Utility.decodeFile(new File(cropPath));
            mUploadProfilePhotoTaskMethod.doTask(u.getUserId(),  original, crop);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void uploadPhoto( String originalPath )
    {
        User u = User.getInstance();
        byte[] original;
        
        try {
            original = Utility.decodeFile(new File(originalPath));
            mUploadPhotoTaskMethod.doTask(u.getUserId(),  original);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    
    // EVENTS
    
    private void onClickAddPhoto()
    {
        AddPhotoDialogFragment.newInstance().show(getSherlockActivity().getSupportFragmentManager(), "addPhotoDialog"); 
    }
    
    private void onClickPhoto( Person person, int position ) 
    {
        ArrayList<String> urls = new ArrayList<String>();
        ArrayList<String> profilePhotoFilenames = person.getProfilePhotosFilenames();
        ArrayList<String> otherPhotoFilenames = person.getOtherPhotosFilenames();
        
        for( String filename : profilePhotoFilenames )
        {
            urls.add("http://s3.amazonaws.com/signals/user_images/" + person.getUserId() + "/profilePhotos/" + filename);
        }
        
        for( String filename : otherPhotoFilenames )
        {
            urls.add("http://s3.amazonaws.com/signals/user_images/" + person.getUserId() + "/otherPhotos/" + filename);
        }
        
        Intent intent = new Intent(getSherlockActivity(), MyProfilePicsPagerActivity.class);  
        Bundle bundle = new Bundle();    
        bundle.putString("username", person.getUsername() );  
        bundle.putStringArrayList("urls", urls );  
        bundle.putInt("pos", position );  
        intent.putExtras(bundle);
        startActivity(intent);
    }



/*
    @Override
    public void addProfilePhotoDialogEventReceived(AddProfilePhotoDialogEvent evt) 
    {
        AddProfilePhotoDialogFragment f = (AddProfilePhotoDialogFragment)evt.getSource();
        
        if( f != null )  
        {
            // UPLOAD PICTURE
            User u = User.getInstance();
            byte[] original;
            byte[] crop;
            try {
                original = Utility.decodeFile(new File(f.getOriginalUri().getPath()));
                crop = Utility.decodeFile(new File(f.getCropUri().getPath()));
                mUploadProfilePhotoTaskMethod.doTask(13L, u.getUsername(), original, crop);
            } catch (IOException e) {
                e.printStackTrace();
            }
            
            f.dismiss();
        }
        
    }
*/


    @Override
    public void uploadProfilePhotoTaskEventReceived(UploadProfilePhotoTaskEvent evt) {
        UploadProfilePhotoTaskMethod met = (UploadProfilePhotoTaskMethod)evt.getSource();
        
        if( met != null && met.success() )
        {       
            User u = User.getInstance();
            ArrayList<String> filenames = new ArrayList<String>();
            filenames.add(met.getPicFilename());
            filenames.addAll(u.getProfilePhotosFilenames());
            u.setProfilePhotosFilenames(filenames);
            
            if( getView() != null )
            {
                View view = getView();
             
                updateView(view);
            }
            //refresh
            
            
            // ERASE FILES
            /*
            File cropFile = new File(mProfileCropUri.getPath());
            if (cropFile.exists()) cropFile.delete();
            
            File originalFile = new File(mProfileOriginalUri.getPath());
            if (originalFile.exists()) originalFile.delete();
            */
        }
    }



    @Override
    public void uploadPhotoTaskEventReceived(UploadPhotoTaskEvent evt) 
    {
        UploadPhotoTaskMethod met = (UploadPhotoTaskMethod)evt.getSource();
        
        if( met != null && met.success() )
        {    
            
            User u = User.getInstance();
            ArrayList<String> filenames = new ArrayList<String>();
            filenames.add(met.getPicFilename());
            filenames.addAll(u.getProfilePhotosFilenames());
            u.setProfilePhotosFilenames(filenames);
            
            mProfilePhotoFilenames = filenames;
            
            
            if( getView() != null )
            {
                View view = getView();
             
                updateView(view);
            }
        }
    }
}