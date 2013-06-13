package com.signals_app.signals.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;

import com.facebook.Session;
import com.signals_app.signals.R;
import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.PageIndicator;

import java.util.ArrayList;

public class IntroActivity extends SherlockFragmentActivity
{

    private ViewPager mPager;
    private IntroPagerAdapter mPagerAdapter;
    private PageIndicator mIndicator;

	private BroadcastReceiver mLoginReceiver;
	private IntentFilter mLoginIntentFilter;

    private Intro1Fragment mFrag1;
    private Intro2Fragment mFrag2;
    private Intro3Fragment mFrag3;
    private Intro4Fragment mFrag4;

    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);

        getActionBar().hide();
        setContentView(R.layout.activity_intro);
        

        // Fragments
        mFrag1 = Intro1Fragment.newInstance();
        mFrag2 = Intro2Fragment.newInstance();
        mFrag3 = Intro3Fragment.newInstance();
        mFrag4 = Intro4Fragment.newInstance();


        // Pager
        mPagerAdapter = new IntroPagerAdapter(getSupportFragmentManager());

        mPager = (ViewPager)findViewById(R.id.pager);
        mPager.setAdapter(mPagerAdapter);
        mPager.setOffscreenPageLimit(4);

        mIndicator = (CirclePageIndicator)findViewById(R.id.indicator);
        mIndicator.setViewPager(mPager);


        
    	// LISTEN TO LOGIN
    	mLoginIntentFilter = new IntentFilter();
    	mLoginIntentFilter.addAction("com.package.ACTION_LOGIN");
    	
    	mLoginReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                finish();
            }
        };
        
        registerReceiver(mLoginReceiver, mLoginIntentFilter);
    }

    
    @Override
    public void onResume() {
        super.onResume();
    }
    
    
    // Called before child is created
    @Override
    public void onPause()
    {
    	super.onPause();
    }

    
    // Called after child has been created
    @Override
    public void onStop()
    {
        super.onStop();
    }

    @Override
    public void onDestroy() {
    	super.onDestroy();
    	unregisterReceiver(mLoginReceiver);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Facebook
        mFrag1.onActivityResult(requestCode, resultCode, data);
    }

    
	public void addListenerLoginButton(Button b) {
		b.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {   
            	
		        Intent intent = new Intent(IntroActivity.this,LoginActivity.class);
		        IntroActivity.this.startActivity(intent);
            }
        });
	}


    private class IntroPagerAdapter extends FragmentPagerAdapter
    {
        private ArrayList<SherlockFragment> mViews = new ArrayList<SherlockFragment>();


        public IntroPagerAdapter(FragmentManager fm ) {
            super(fm);

            mViews.add(mFrag1);
            mViews.add(mFrag2);
            mViews.add(mFrag3);
            mViews.add(mFrag4);
        }



        @Override
        public Fragment getItem(int index) {

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
	







}
