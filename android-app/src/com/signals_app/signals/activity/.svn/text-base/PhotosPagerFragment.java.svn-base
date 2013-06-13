package com.signals_app.signals.activity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import com.actionbarsherlock.app.SherlockFragment;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.signals_app.signals.R;
import com.signals_app.signals.model.Profile.Person;
import com.signals_app.signals.tasks.ProfilesTaskMethod;
import com.signals_app.signals.tasks.ProfilesTaskMethod.ProfilesTaskEvent;
import com.signals_app.signals.tasks.ProfilesTaskMethod.ProfilesTaskListener;
import com.signals_app.signals.tasks.signals.FetchVisitsTaskMethod;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

public class PhotosPagerFragment extends SherlockFragment {

    ArrayList<String> mUrls;
    private ViewPager mViewPager;
    private PhotosAdapter mViewPagerAdapter;;
    private int mPos = 0;
    
    public static PhotosPagerFragment newInstance(ArrayList<String> urls, int index) {

        PhotosPagerFragment pageFragment = new PhotosPagerFragment();
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("urls", urls);
        bundle.putInt("pos", index);
        pageFragment.setArguments(bundle);
        return pageFragment;
    }

    @Override  
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Bundle args = getArguments();
                
        if( savedInstanceState != null ) 
            args = savedInstanceState;
        
        mUrls = getArguments().getStringArrayList("urls");
        mPos = args.getInt("pos");
        
    }

    // Called once because of setRetainInstance
    @Override  
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        
        
        // WIDGETS 
        View view = inflater.inflate(R.layout.fragment_profiles_pager, container, false);

        
        mViewPagerAdapter = new PhotosAdapter(this.getChildFragmentManager(), mUrls);
        mViewPager = (ViewPager)view.findViewById(R.id.viewpager);
        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPager.setCurrentItem(mPos);
        mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) 
            {
                getSherlockActivity().getSupportActionBar().setSubtitle(getString(R.string.photo) + " " + (position+1) + "/" + getArguments().getStringArrayList("urls").size());
                mPos = position;
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {                
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {                
            }
        });
        
        getSherlockActivity().getSupportActionBar().setSubtitle(getString(R.string.photo) + " " + (mPos+1) + "/" + getArguments().getStringArrayList("urls").size());
        
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }
    
    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        

    } 
    
    @Override
    public void onDestroy()
    {
        super.onDestroy();
    } 

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        int position = mViewPager.getCurrentItem();
        savedInstanceState.putInt("pos", position);
        savedInstanceState.putStringArrayList("urls", mUrls);
    }
    
    
    public class PhotosAdapter extends FragmentStatePagerAdapter 
    {
        private ArrayList<ImageFragment> mViews = new ArrayList<ImageFragment>();

        public PhotosAdapter(FragmentManager fm, ArrayList<String> urls ) {
            this(fm);
            
            for( int i=0; i<urls.size(); ++i )
            {
                mViews.add(ImageFragment.newInstance(urls.get(i)));
            }
        }

        public PhotosAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public ImageFragment getItem(int index) { 

            return mViews.get(index);
        }  

        @Override 
        public int getItemPosition (Object object)
        {
          int index = mViews.indexOf (object);
          if (index == -1)
            return POSITION_NONE;
          else
            return index;
        }
        
        @Override 
        public int getCount() {

            return mViews.size();
        }
    }
    
    public class DetailOnPageChangeListener extends ViewPager.SimpleOnPageChangeListener {

        private int currentPage;

        @Override
        public void onPageSelected(int position) {

            getSherlockActivity().getSupportActionBar().setSubtitle(getString(R.string.photo) + " " + (position+1) + "/" + getArguments().getStringArrayList("urls").size());
            currentPage = position;
        }

        public int getCurrentPage() {
            return currentPage;
        }
    }


}