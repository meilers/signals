package com.signals_app.signals.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.ActionMode;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.signals_app.signals.R;
import com.signals_app.signals.activity.Intro2Fragment;
import com.signals_app.signals.model.Place;
import com.signals_app.signals.model.Profile.Person;
import com.signals_app.signals.model.Profile.User;
import com.signals_app.signals.model.Signals.Vote;
import com.signals_app.signals.tasks.LoginTaskMethod;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by omegatai on 2013-05-24.
 */
public class Register2Fragment extends SherlockFragment
{
    public static final int ACTION_EDIT = 0;
    public static final int CONTEXTUAL_ACTION_DELETE = 0;

    public static final String TAG = "Register2Fragment";

    private ActionMode mMode;
    private boolean mEditState = false;

    private Long mUserId;
    private String mFacebookId;

    private DisplayImageOptions mImageOptions;
    private HashMap<String, Bitmap> mFbPhotosSelected;

    private PhotosGridAdapter mPhotosGridAdapter;
    private Button mContinueBtn;

    public static final Register2Fragment newInstance(Long userId, String facebookId, ArrayList<String> fbPhotoUrls )
    {
        Register2Fragment f = new Register2Fragment();

        Bundle args = f.getArguments();
        if (args == null) {
            args = new Bundle();
        }

        HashMap<String, Boolean> fbPhotosSelected = new HashMap<String, Boolean>();

        for( String s : fbPhotoUrls )
            fbPhotosSelected.put(s, null);

        args.putSerializable("fbPhotosSelected", fbPhotosSelected);
        args.putLong("userId", userId);
        args.putString("facebookId", facebookId);


        f.setArguments(args);

        return f;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();

        mImageOptions = new DisplayImageOptions.Builder()
                .cacheInMemory()
                .cacheOnDisc()
                .build();


        mFbPhotosSelected = (HashMap<String, Bitmap>)args.getSerializable("fbPhotosSelected");
        mUserId = args.getLong("userId");
        mFacebookId = args.getString("facebookId");

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_register2, container, false);
        getSherlockActivity().getActionBar().setSubtitle(getString(R.string.photos));

        ExpandableHeightGridView gv = (ExpandableHeightGridView)view.findViewById(R.id.gridview);
        gv.setExpanded(true);

        String[] keys = (String[])( mFbPhotosSelected.keySet().toArray( new String[mFbPhotosSelected.size()] ) );
        mPhotosGridAdapter = new PhotosGridAdapter(this.getActivity(), 0, keys);

        gv.setAdapter(mPhotosGridAdapter);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> l, View v, int position, long id) {
            }}
        );

        switch (getActivity().getResources().getConfiguration().orientation )
        {
            case Configuration.ORIENTATION_LANDSCAPE:
                gv.setNumColumns(6);
                break;

            default:
                gv.setNumColumns(4);
                break;
        }


        mContinueBtn = (Button)view.findViewById(R.id.continue_btn);
        mContinueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickContinue();
            }
        });



        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        menu.clear();
        menu.add(0,0,ACTION_EDIT,getString(R.string.edit))
                .setIcon( R.drawable.ic_action_edit)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getOrder())
        {

            case ACTION_EDIT:

                mMode = getSherlockActivity().startActionMode(new AnActionModeOfEpicProportions());

                return true;
        }

        return false;
    }

    private final class AnActionModeOfEpicProportions implements ActionMode.Callback {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu)
        {
            menu.add(getString(R.string.delete))
                    .setIcon( R.drawable.ic_action_delete)
                    .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);


            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            mEditState = true;
            mContinueBtn.setVisibility(View.GONE);
            mPhotosGridAdapter.notifyDataSetChanged();

            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item)
        {
            switch(item.getOrder())
            {
                case CONTEXTUAL_ACTION_DELETE:
                    CheckBox[] checkBoxes = mPhotosGridAdapter.getCheckBoxes();
                    String[] photoUrls = ( mFbPhotosSelected.keySet().toArray( new String[mFbPhotosSelected.size()] ) );

                    for( int i=0; i < checkBoxes.length; ++i )
                    {
                        if( checkBoxes[i].isChecked()  )
                            mFbPhotosSelected.remove(photoUrls[i]);
                    }

                    photoUrls = ( mFbPhotosSelected.keySet().toArray( new String[mFbPhotosSelected.size()] ) );
                    mPhotosGridAdapter.setPhotoUrls(photoUrls);
                    mPhotosGridAdapter.notifyDataSetChanged();

                    mContinueBtn.setVisibility(View.VISIBLE);

                    mMode.finish();

                    break;
            }

            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mEditState = false;
        }
    }



    private class PhotosGridAdapter extends ArrayAdapter<String> {

        private Context mContext;
        private String[] mPhotoUrls;
        private CheckBox[] mCheckBoxes;
        private int nbPhotosLoaded = 0;

        public PhotosGridAdapter(Context context, int textViewResourceId, String[] photoUrls ) {
            super(context, 0, photoUrls);
            this.mPhotoUrls = photoUrls;
            this.mContext = context;
            this.mCheckBoxes = new CheckBox[photoUrls.length];
        }

        @Override
        public int getCount() {
            return mPhotoUrls.length;
        }

        @Override
        public String getItem(int position) {
            return mPhotoUrls[position];
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent)
        {
            View v = convertView;


            if( v == null )
            {
                LayoutInflater vi = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.box_photo, null);
            }

            DisplayMetrics metrics = new DisplayMetrics();
            getSherlockActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
            RelativeLayout.LayoutParams params;
            switch (getActivity().getResources().getConfiguration().orientation )
            {
                case Configuration.ORIENTATION_LANDSCAPE:
                    params = new RelativeLayout.LayoutParams((int)(metrics.widthPixels/6), (int)(metrics.widthPixels/6));
                    break;

                default:
                    params = new RelativeLayout.LayoutParams((int)(metrics.widthPixels/4), (int)(metrics.widthPixels/4));
                    break;
            }


            final String photoUrl = mPhotoUrls[position];

            // PIC
            ImageView iv = (ImageView)v.findViewById(R.id.photo_iv);
            iv.setLayoutParams(params);
            iv.setAdjustViewBounds(true);
            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);

            Animation fadeInAnimation = AnimationUtils.loadAnimation(getSherlockActivity(), R.anim.fadein);
            ImageLoader.getInstance().displayImage(photoUrl, iv, mImageOptions, new SimpleImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view)
                {
                    if( nbPhotosLoaded == 0 )
                    {
                        if( getSherlockActivity() != null )
                            getSherlockActivity().setSupportProgressBarIndeterminateVisibility(true);
                    }
                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    ++nbPhotosLoaded;

                    if( nbPhotosLoaded == mPhotoUrls.length-1 )
                    {
                        if( getSherlockActivity() != null )
                            getSherlockActivity().setSupportProgressBarIndeterminateVisibility(false);

                        nbPhotosLoaded = 0;
                    }
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {
                    ++nbPhotosLoaded;

                    if( nbPhotosLoaded == mPhotoUrls.length-1 )
                    {
                        if( getSherlockActivity() != null )
                            getSherlockActivity().setSupportProgressBarIndeterminateVisibility(false);

                        nbPhotosLoaded = 0;
                    }
                }

                @Override
                public void onLoadingComplete(String imageUri,View view, Bitmap loadedImage) {
                    ++nbPhotosLoaded;
                    mFbPhotosSelected.put(photoUrl, loadedImage);

                    if( nbPhotosLoaded == mPhotoUrls.length-1 )
                    {
                        if( getSherlockActivity() != null )
                            getSherlockActivity().setSupportProgressBarIndeterminateVisibility(false);
                    }
                }
            });
            iv.startAnimation(fadeInAnimation);



            // Checkbox
            mCheckBoxes[position] = (CheckBox)v.findViewById(R.id.photo_cb);

            if(mEditState)
                mCheckBoxes[position].setVisibility(View.VISIBLE);
            else
            {
                mCheckBoxes[position].setVisibility(View.GONE);
                mCheckBoxes[position].setChecked(false);
            }
            return v;
        }

        public void setPhotoUrls( String[] photoUrls )
        {
            this.mPhotoUrls = photoUrls;
            this.mCheckBoxes = new CheckBox[photoUrls.length];
        }

        public CheckBox[] getCheckBoxes()
        {
            return mCheckBoxes;
        }
    }



    // EVENTS
    private void onClickContinue()
    {
        RegisterActivity act = (RegisterActivity)getSherlockActivity();

        if( act != null )
        {
            boolean error = false;

        }
    }
}
