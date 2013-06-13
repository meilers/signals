package com.signals_app.signals.activity;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.signals_app.signals.R;

import com.signals_app.signals.model.Place;
import com.signals_app.signals.model.Profile.Person;
import com.signals_app.signals.model.Profile.User;
import com.signals_app.signals.model.Profile.User.FlirtEvent;
import com.signals_app.signals.model.Profile.User.FlirtListener;
import com.signals_app.signals.model.Signals.Signal;
import com.signals_app.signals.model.Signals.Flirt;
import com.signals_app.signals.model.Signals.Vote;
import com.signals_app.signals.tasks.ProfilesTaskMethod;
import com.signals_app.signals.tasks.ProfilesTaskMethod.ProfilesTaskEvent;
import com.signals_app.signals.tasks.ProfilesTaskMethod.ProfilesTaskListener;
import com.signals_app.signals.tasks.signals.FetchFlirtsTaskMethod;
import com.signals_app.signals.tasks.signals.FetchFlirtsTaskMethod.FetchFlirtsTaskEvent;
import com.signals_app.signals.util.PushReceiverActivity;
import com.signals_app.signals.util.Utility;
import com.signals_app.signals.util.PushReceiverActivity.PushEvent;
import com.signals_app.signals.util.PushReceiverActivity.PushListener;

import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView.ScaleType;

public class FlirtsFragment extends SherlockFragment implements FetchFlirtsTaskMethod.FetchFlirtsTaskListener, PushListener, ProfilesTaskListener
{
    public static final int ACTION_REFRESH = 1;
    
    
	// MODEL
    private FlirtsGridAdapter mFlirtsGridAdapter;
    private ArrayList<Flirt> mFlirts = null; 
	
	
	// TASK
	private FetchFlirtsTaskMethod mFetchFlirtsTaskMethod;
	private ProfilesTaskMethod mProfilesTaskMethod;

	
    // WIDGETS
    private DisplayImageOptions mImageOptions;
	
	
	
  	public static final FlirtsFragment newInstance()
  	{
  		FlirtsFragment f = new FlirtsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("index", 0);
        f.setArguments(bundle);
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
		View view = inflater.inflate(R.layout.fragment_flirts, container, false);
		getSherlockActivity().setTitle(getSherlockActivity().getResources().getString(R.string.flirted_with_you));
		
		if( User.getInstance().checkedIn() )
            getSherlockActivity().getSupportActionBar().setSubtitle(getString(R.string.tonight) + " " + getString(R.string.at) + " " + User.getInstance().getCheckPlace().getName());
        else
            getSherlockActivity().getSupportActionBar().setSubtitle(null); 
		
		// IMAGE LOADER
		mImageOptions = new DisplayImageOptions.Builder()
		.cacheInMemory()
		.cacheOnDisc()
		.build();
		
		
		// TASKS
		mFetchFlirtsTaskMethod = new FetchFlirtsTaskMethod(getSherlockActivity());
		mProfilesTaskMethod = new ProfilesTaskMethod(getSherlockActivity());
		
		// MODEL
		mFlirts = new ArrayList<Flirt>();
		
		// LISTENERS
        PushReceiverActivity.addPushListener(this);
        mFetchFlirtsTaskMethod.addFetchFlirtsTaskListener(this);
        mProfilesTaskMethod.addProfilesTaskListener(this);
        
        
		// WIDGETS
	    GridView gv = (GridView)view.findViewById(R.id.gridview);
	    mFlirtsGridAdapter = new FlirtsGridAdapter(this.getActivity(), 0, mFlirts);
        
        gv.setAdapter(mFlirtsGridAdapter);
        gv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> l, View v, int position, long id) {
                Flirt f = mFlirts.get(position);
                
                onClickPerson(f.getPerson());
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
        
        
	    setHasOptionsMenu(true);
	    
		return view;
	}

    @Override
    public void onResume() {
        super.onResume();

		
		
        fetchFlirts();
    }
    
    
    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        
        // LISTENERS
        PushReceiverActivity.removePushListener(this);
        mFetchFlirtsTaskMethod.removeFetchFlirtsTaskListener(this);
        mProfilesTaskMethod.removeProfilesTaskListener(this);
        
        mFetchFlirtsTaskMethod.cleanUp();
        mProfilesTaskMethod.cleanUp();
        
    }
	
    
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Add your menu entries here
        super.onCreateOptionsMenu(menu, inflater);
        
        menu.add(0,0,ACTION_REFRESH,getString(R.string.refresh))
        .setIcon( R.drawable.ic_action_refresh)
        .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
    }
    
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getOrder())
        {
            case ACTION_REFRESH:
                fetchFlirts();
                return true;
        }
        
        return false;
    }
    
    
    
    private void fetchFlirts()
    {
    	mFetchFlirtsTaskMethod.doTask(User.getInstance().getUserId());
    }

    
    
	private void updateView( ArrayList<Flirt> flirts )
	{
        mFlirts.clear();
        mFlirts.addAll(flirts);
        
        
        // Update!
        mFlirtsGridAdapter.notifyDataSetChanged();
		
	}
	
	
    private class FlirtsGridAdapter extends ArrayAdapter<Flirt> {

        private ArrayList<Flirt> mFlirts;
        private Context mContext;
        
        public FlirtsGridAdapter(Context context, int textViewResourceId, ArrayList<Flirt> flirts ) {
                super(context, 0, flirts);
                this.mFlirts = flirts;
                this.mContext = context;
        }
     
        @Override 
        public int getCount() {
            return mFlirts.size();
        }
     
        @Override
        public Flirt getItem(int position) {
            return mFlirts.get(position);
        }
     
        @Override
        public long getItemId(int position) {
            return 0;
        } 
     
        @Override
        public View getView(int position, View convertView, ViewGroup parent) 
        {
            View v = convertView;
           
            
            if( v == null )
            {
                LayoutInflater vi = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.box_person, null);
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
            
            
            Button newPeopleBtn = (Button)v.findViewById(R.id.new_people_btn); 
            newPeopleBtn.setVisibility(View.GONE);
            
            RelativeLayout personLayout = (RelativeLayout)v.findViewById(R.id.person_layout); 
            personLayout.setVisibility(View.VISIBLE);
            
            final LinearLayout coverLayout = (LinearLayout)v.findViewById(R.id.cover_layout);
            coverLayout.setVisibility(View.GONE);
            
            
            
            /**** PEOPLE ****/
            
            Person p = mFlirts.get(position).getPerson();
            
            // PIC
            ImageView iv = (ImageView)v.findViewById(R.id.profile_pic);
            iv.setLayoutParams(params);
            iv.setAdjustViewBounds(true);
            iv.setScaleType(ScaleType.FIT_XY);
            
            Animation fadeInAnimation = AnimationUtils.loadAnimation(getSherlockActivity(), R.anim.fadein);
            ImageLoader.getInstance().displayImage("http://s3.amazonaws.com/signals/user_images/" + p.getUserId() + "/profilePhotoThumb.jpg", iv, mImageOptions);
            iv.startAnimation(fadeInAnimation); 
            
            
            // STAR
            ImageView starIv = (ImageView)v.findViewById(R.id.star_iv);
            
            if( p.star() )
                starIv.setVisibility(View.VISIBLE);
            else
                starIv.setVisibility(View.INVISIBLE);
            
            
            return v;
        }
    }



	
	
	
	// EVENTS
    
    private void onClickPerson( Person personClicked )
    {
        ArrayList<Long> peopleIds = new ArrayList<Long>();
        
        int index = 0;
        int i = 0;
        
        for( Flirt f : mFlirts )
        {
            Person p = f.getPerson();
            peopleIds.add(p.getUserId());
            
            if( p.equals(personClicked) )
                index = i;
            
            ++i;
        }
        
        getArguments().putInt("index", index);
        mProfilesTaskMethod.doTask(User.getInstance().getUserId(), peopleIds);
    }
    
    
	
	@Override
	public void fetchFlirtsTaskEventReceived(FetchFlirtsTaskEvent evt) {
		FetchFlirtsTaskMethod met = (FetchFlirtsTaskMethod)evt.getSource();
		
		if( met != null && met.success() )
		{
			User.getInstance().setNbNewFlirts(0);
			
			updateView(met.getFlirts());
		}
		
	}
	
	public void pushEventReceived(PushEvent evt)
	{
	}


    @Override
    public void profilesTaskEventReceived(ProfilesTaskEvent evt) {
        ProfilesTaskMethod met = (ProfilesTaskMethod)evt.getSource();
        
        if( met != null && met.success() )
        {
            ProfilesPagerFragment frag = ProfilesPagerFragment.newInstance(met.getPeople(), getArguments().getInt("index"));
            
            getSherlockActivity().getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.content_frame, frag, "ProfilesPagerFragment")
            .addToBackStack(null)
            .commit();
        }   
    }











}
