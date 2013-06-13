package com.signals_app.signals.activity;


import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.MemoryCacheUtil;
import com.signals_app.signals.activity.MenuFragment.OnTabSelectedListener;
import com.signals_app.signals.model.AppState;
import com.signals_app.signals.model.Constants;
import com.signals_app.signals.model.Place;
import com.signals_app.signals.model.Profile.GpsPosition;
import com.signals_app.signals.model.Profile.Person;
import com.signals_app.signals.model.Profile.User;
import com.signals_app.signals.model.Profile.User.PlaceEvent;
import com.signals_app.signals.model.Profile.User.PlaceListener;
import com.signals_app.signals.model.Signals.Signal;
import com.signals_app.signals.tasks.PeopleTaskMethod;
import com.signals_app.signals.tasks.PlaceCheckinTaskMethod;
import com.signals_app.signals.tasks.PeopleTaskMethod.PeopleTaskEvent;
import com.signals_app.signals.tasks.PlaceCheckoutTaskMethod;
import com.signals_app.signals.tasks.PlaceCheckoutTaskMethod.PlaceCheckoutTaskEvent;
import com.signals_app.signals.tasks.PlaceCheckoutTaskMethod.PlaceCheckoutTaskListener;
import com.signals_app.signals.util.MyLocation;
import com.signals_app.signals.util.MyLocation.LocationResult;
import com.signals_app.signals.util.Utility;

import android.app.AlertDialog;
import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Canvas;
import android.graphics.drawable.AnimationDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.text.Html;
import android.util.Log;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.Window;
import com.actionbarsherlock.view.MenuItem;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;

import com.signals_app.signals.R;
import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.SlidingMenu.CanvasTransformer;
import com.slidingmenu.lib.app.SlidingFragmentActivity;




public class MainActivity extends SlidingFragmentActivity implements OnTabSelectedListener, PlaceListener, PlaceCheckoutTaskListener
{

	// IMAGE LOADER
	protected ImageLoader mImageLoader = ImageLoader.getInstance();
	private boolean mInstanceStateSaved;	
	
	// GPS
	private LocationResult mLocationResult;
	private MyLocation mLocation;
	
	
	// INTENTS
	static final String ACTION = "MyAction";
	
	
	// TASKS
    
	private PlaceCheckoutTaskMethod mPlaceCheckoutTaskMethod;    
	
	// TIMERS
	/*
	private Handler mHandler = new Handler();
  	private Runnable mRunnable = new Runnable() {

        @Override 
        public void run() {
    		fetchPeople();
        }
  	};
  	*/ 
  	
	
	// FRAGMENTS
	private MenuFragment mMenuFragment;
	
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setIcon(getResources().getDrawable(R.drawable.logo));
        
		//getSupportActionBar().setDisplayShowHomeEnabled(false);
		
        
        // GPS
        mLocationResult = new LocationResult(){
    	    @Override
    	    public void gotLocation(final Location location){
    	    	MainActivity.this.runOnUiThread(new Runnable() {
    	    		public void run() 
    	    		{
    	    			GpsPosition userPos = new GpsPosition((float)location.getLatitude(), (float)location.getLongitude(), location.getAccuracy());
    	      	       	User.getInstance().setGpsPosition(userPos);
    	      	       	
    	      	       	// Check if user isn't too far away
    	      			Place checkPlace = User.getInstance().getCheckPlace();
    	      			
    	      			if( checkPlace != null )
    	      			{
        	      			GpsPosition placePos = checkPlace.getGpsPosition();
    	      				int distanceInMeters = (int)(Utility.getDistance(userPos.getLatitude(), userPos.getLongitude(), placePos.getLatitude(), placePos.getLongitude(), "k")*1000);
    	      				
    	      				if( distanceInMeters - userPos.getAccuracy() > Constants.MIN_CHECKIN_RADIUS )
    	      				{
    	      				    mPlaceCheckoutTaskMethod.doTask(User.getInstance().getUserId(), PlaceCheckoutTaskMethod.CHECK_OUT_BECAUSE_TOO_FAR);
    	      				}
    	      			}
    	      	       	
    	      	       	// Recheck location in 60 seconds (and give it an extra minute for GPS)
    	      	       	mLocation.getLocation(MainActivity.this, mLocationResult, 0, 50, 60000, false);
    	    		}
    	    	});
    	        
    	        
    	    }
    	};
    	mLocation = new MyLocation();
    	
		mPlaceCheckoutTaskMethod = new PlaceCheckoutTaskMethod(this);
        
		
		
		// LISTENERS
		
		User.getInstance().addPlaceListener(this);
        mPlaceCheckoutTaskMethod.addPlaceCheckoutTaskListener(this);
	        
	        
	        
		// PARSE
        Notification notification = new Notification(R.drawable.logo, "", System.currentTimeMillis());
        notification.icon = R.drawable.icon;
        
        /*
		Parse.initialize(this, "ujzhrnPvHlLj7oX5wrHdZguIjfLGKZuLhFfqFcnn", "NTniCa0ITcJCn692onH8W1fLHfPXYHX7zW8coNcx");
		PushService.subscribe(this, "user" + User.getInstance().getUserId().toString(), MainActivity.class);
		
		ParseUser.enableAutomaticUser();
		ParseACL defaultACL = new ParseACL();
	    
		defaultACL.setPublicReadAccess(true); // If you would like all objects to be private by default, remove this line.
		ParseACL.setDefaultACL(defaultACL, true);
		
	    IntentFilter filter = new IntentFilter(ACTION);
	    this.registerReceiver(mPushReceiver, filter);
	    */
        
		
		// SLIDING MENU
		setBehindContentView(R.layout.menu_frame);
		getSlidingMenu().setSlidingEnabled(true);
		getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);  			// show home as up so we can toggle      
		

		
        if (savedInstanceState == null) 
        {
            mMenuFragment = MenuFragment.newInstance();

            getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.menu_frame, mMenuFragment, "MenuFragment")
            .commit();
           
            int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
            
            if( false ) //hour >= 17 || hour <= 7 )
            {
                getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, PlacesFragment.newInstance(), "PlacesFragment")
                .commit();
            }
            else
            {
                getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, InvitationsPagerFragment.newInstance(), "InvitationsPagerFragment")
                .commit();
            }
        }
        else
        {
            mMenuFragment = (MenuFragment)getSupportFragmentManager().findFragmentByTag("MenuFragment");
        }
        
        
		SlidingMenu sm = getSlidingMenu();
		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset); 
		sm.setShadowWidthRes(R.dimen.shadow_width);
		//sm.setShadowDrawable(R.drawable.shadow);
		sm.setBehindScrollScale(0.25f);
		sm.setFadeDegree(0.25f);
		
		sm.setBehindScrollScale(0.0f);
		
		
		
		/*
		sm.setBehindCanvasTransformer(new CanvasTransformer() {
			@Override
			public void transformCanvas(Canvas canvas, float percentOpen) {
				float scale = (float) (percentOpen*0.25 + 0.75);
				canvas.scale(scale, scale, canvas.getWidth()/2, canvas.getHeight()/2);
			}
		});
		*/
	} 
	

	private final BroadcastReceiver mPushReceiver = new BroadcastReceiver() {
		
		
		@Override
	    public void onReceive(Context context, Intent intent)  
		{
	        String action = intent.getAction();

	        if (ACTION.equals(action)) 
	        {
	        	displayAlert(intent);
	        } 
	    }
		
	};
	
	private void displayAlert(Intent intent)
	{
	}
	
	@Override
    protected void onStart()
    {
        super.onStart();
    }
	
    @Override
    protected void onResume()
    {
    	super.onResume();
    	
    	// CLEAR IMAGE CACHE
    	ImageLoader.getInstance().getMemoryCache().clear();
    	ImageLoader.getInstance().getDiscCache().clear();
    	
    	
    
    	
    	mLocation.getLocation(MainActivity.this, mLocationResult, 0, 0, 60000, true);
    	
        
    }

    @Override
    protected void onPause()
    {
    	super.onPause();
    	
        mLocation.cancelTimer();
        
        User.getInstance().removePlaceListener(this);
    	//mHandler.removeCallbacks(mRunnable);
    }
    
	
    @Override
    public void onDestroy()
    {
        super.onDestroy();
        
		if (!mInstanceStateSaved) {
			mImageLoader.stop();
		}
		
		
		// LISTENERS
        
        User.getInstance().removePlaceListener(this);
        mPlaceCheckoutTaskMethod.removePlaceCheckoutTaskListener(this);
        
        mPlaceCheckoutTaskMethod.cleanUp();
        
        //unregisterReceiver(mPushReceiver);
    }
     
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (outState != null) 
        {
            super.onSaveInstanceState(outState);
        	mInstanceStateSaved = true;
        }
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

    	/*
        menu.add(0,0,MainActivity.MENU_OPTION_EDIT_PROFILE,R.string.menu_option_edit_profile)
        .setShowAsAction(MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        
        menu.add(0,0,MainActivity.MENU_OPTION_PREFERENCES,R.string.menu_option_preferences)
        .setShowAsAction(MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        
        menu.add(0,0,MainActivity.MENU_OPTION_LOGOUT,R.string.menu_option_logout)
	        .setShowAsAction(MenuItem.SHOW_AS_ACTION_WITH_TEXT);
*/
        return true;
    }
    
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
		    toggle();
			getSupportActionBar().setIcon(getResources().getDrawable(R.drawable.logo));
			

			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	public void forceCheckOut( int reason )
	{
	    Place checkPlace = User.getInstance().getCheckPlace();
	    
	    if( checkPlace != null )
	    {
	        String reasonStr = "";
	        
	        switch( reason )
	        {
	            case PlaceCheckoutTaskMethod.CHECK_OUT_BECAUSE_TOO_FAR:
	                reasonStr = getString(R.string.because_youre_too_far_away);
	                break;
	        }
	        
	        
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
    
            alertDialogBuilder.setTitle(checkPlace.getName());
    
            alertDialogBuilder
                .setMessage(getString(R.string.error_you_checked_out) + " " + checkPlace.getName() + " " + reasonStr)
                .setCancelable(false)
                .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {}
                });
    
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
            
            User.getInstance().setCheckPlace(null);
	    }
	}
	
	public MenuFragment getMenuFragment()
	{
	    return mMenuFragment;
	}
	
	
	
    // EVENTS
    
    @Override
    public void onPlacesSelected()
    {
        PlacesFragment frag = PlacesFragment.newInstance();
        
        if( frag == null )
            frag = PlacesFragment.newInstance();
    
        // clear everything that happened before!
        getSupportFragmentManager().popBackStack(null,FragmentManager.POP_BACK_STACK_INCLUSIVE); 
        
		getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.content_frame, frag, "PlacesFragment")
		.commit();
            
		Handler h = new Handler();
		h.postDelayed(new Runnable() {
			public void run() {
				getSlidingMenu().showContent();
			}
		}, 50);
    }
    
    @Override
    public void onPeopleInsideSelected()
    {
        PeopleInsideFragment frag = PeopleInsideFragment.newInstance();
    	
    
        // clear everything that happened before!
        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE); 
        
        getSupportFragmentManager()
        .beginTransaction()
        .replace(R.id.content_frame, frag, "PeopleInsideFragment")
        .commit();
        
		Handler h = new Handler();
		h.postDelayed(new Runnable() {
			public void run() {
				getSlidingMenu().showContent();
			}
		}, 50);
    }
    
    @Override
    public void onSignalsSelected()
    {
        SignalsFragment frag = SignalsFragment.newInstance();
    
        // clear everything that happened before!
        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE); 
        
		getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.content_frame, frag, "SignalsFragment")
		.commit();
		
		Handler h = new Handler();
		h.postDelayed(new Runnable() {
			public void run() {
				getSlidingMenu().showContent();
			}
		}, 50);
    }
    
    
    @Override
    public void onInvitationsSelected() {
        InvitationsPagerFragment frag = InvitationsPagerFragment.newInstance();
        
        // clear everything that happened before!
        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE); 
        
        getSupportFragmentManager()
        .beginTransaction()
        .replace(R.id.content_frame, frag, "InvitationsPagerFragment")
        .commit();
        
        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            public void run() {
                getSlidingMenu().showContent();
            }
        }, 50);
    }
    
    
    
    
    @Override
    public void onProfileSelected()
    {
    	MyProfileFragment frag = MyProfileFragment.newInstance( );
    	
        // clear everything that happened before!
        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE); 
        
		getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.content_frame, frag, "MyProfileFragment")
		.commit();
		
		Handler h = new Handler();
		h.postDelayed(new Runnable() {
			public void run() {
				getSlidingMenu().showContent();
			}
		}, 50);
    }
    
    
    


    @Override
    public void placeEventReceived(PlaceEvent evt) 
    {
        if( mMenuFragment.getView() != null )
            mMenuFragment.updateView(mMenuFragment.getView());
        
    }

    @Override
    public void placeCheckoutTaskEventReceived(PlaceCheckoutTaskEvent evt) 
    {
        PlaceCheckoutTaskMethod met = (PlaceCheckoutTaskMethod)evt.getSource();
        
        if( met != null && met.success() )
        {
            forceCheckOut(met.getReason());
        }

    }



    
    @Override
    public void onBackPressed() 
    {
        if( getSupportFragmentManager().getBackStackEntryCount() != 0 )
            getSupportFragmentManager().popBackStack();
    }
}




