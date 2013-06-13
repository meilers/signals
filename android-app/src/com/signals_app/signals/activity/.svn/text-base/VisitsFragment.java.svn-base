package com.signals_app.signals.activity;

import java.text.DecimalFormat;
import java.util.ArrayList;
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
import com.signals_app.signals.model.Profile.User.VisitEvent;
import com.signals_app.signals.model.Profile.User.VisitListener;
import com.signals_app.signals.model.Signals.Visit;
import com.signals_app.signals.model.Signals.Visit;
import com.signals_app.signals.model.Signals.Signal;
import com.signals_app.signals.model.Signals.Visit;
import com.signals_app.signals.model.Signals.Vote;
import com.signals_app.signals.tasks.ProfilesTaskMethod;
import com.signals_app.signals.tasks.ProfilesTaskMethod.ProfilesTaskEvent;
import com.signals_app.signals.tasks.ProfilesTaskMethod.ProfilesTaskListener;
import com.signals_app.signals.tasks.signals.FetchVisitsTaskMethod;
import com.signals_app.signals.tasks.signals.FetchVisitsTaskMethod.FetchVisitsTaskEvent;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

public class VisitsFragment extends SherlockFragment implements FetchVisitsTaskMethod.FetchVisitsTaskListener, PushListener, ProfilesTaskListener
{
    public static final int ACTION_REFRESH = 1;
    
    
    
	// MODEL
    private VisitsGridAdapter mVisitsGridAdapter;
    private ArrayList<Visit> mVisits = null; 
	
	// TASK
	private FetchVisitsTaskMethod mFetchVisitsTaskMethod;
	private ProfilesTaskMethod mProfilesTaskMethod;

	
    // WIDGETS
    private DisplayImageOptions mImageOptions;
	
	
  	public static final VisitsFragment newInstance()
  	{
  		VisitsFragment f = new VisitsFragment();
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
		View view = inflater.inflate(R.layout.fragment_visits, container, false);
		getSherlockActivity().setTitle(getSherlockActivity().getResources().getString(R.string.visited_you));
		
		if( User.getInstance().checkedIn() )
            getSherlockActivity().getSupportActionBar().setSubtitle(getString(R.string.tonight) + " " + getString(R.string.at) + " " + User.getInstance().getCheckPlace().getName());
        else
            getSherlockActivity().getSupportActionBar().setSubtitle(null); 
		
		
		mVisits = new ArrayList<Visit>();
		
		mImageOptions = new DisplayImageOptions.Builder()
		.cacheInMemory()
		.cacheOnDisc()
		.build();
		
		
		mFetchVisitsTaskMethod = new FetchVisitsTaskMethod(getSherlockActivity());
		mProfilesTaskMethod = new ProfilesTaskMethod(getSherlockActivity());
		
        
        // LISTENERS
        PushReceiverActivity.addPushListener(this);
        mFetchVisitsTaskMethod.addFetchVisitsTaskListener(this);
        mProfilesTaskMethod.addProfilesTaskListener(this);
        
        
		// WIDGETS
		
        GridView gv = (GridView)view.findViewById(R.id.gridview);
        mVisitsGridAdapter = new VisitsGridAdapter(this.getActivity(), 0, mVisits);
        
        gv.setAdapter(mVisitsGridAdapter);
        gv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> l, View v, int position, long id) {
                Visit f = mVisits.get(position);
                
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


		
		
        fetchVisits();
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
        mFetchVisitsTaskMethod.removeFetchVisitsTaskListener(this);
        mProfilesTaskMethod.removeProfilesTaskListener(this);
        
        mFetchVisitsTaskMethod.cleanUp();
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
                fetchVisits();
                return true;
        }
        
        return false;
    }
    
    
    
    private void fetchVisits()
    {
    	mFetchVisitsTaskMethod.doTask(User.getInstance().getUserId());
    }
    

	private void updateView( ArrayList<Visit> visits )
	{
        mVisits.clear();
        mVisits.addAll(visits);
        
        // Update!
        mVisitsGridAdapter.notifyDataSetChanged();
		
	}
	
	
    private class VisitsGridAdapter extends ArrayAdapter<Visit> {

        private ArrayList<Visit> mVisits;
        private Context mContext;
        
        public VisitsGridAdapter(Context context, int textViewResourceId, ArrayList<Visit> visits ) {
                super(context, 0, visits);
                this.mVisits = visits;
                this.mContext = context;
        }
     
        @Override 
        public int getCount() {
            return mVisits.size();
        }
     
        @Override
        public Visit getItem(int position) {
            return mVisits.get(position);
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
            
            Person p = mVisits.get(position).getPerson();
            
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
        
        
        for( Visit v : mVisits )
        {
            Person p = v.getPerson();
            peopleIds.add(p.getUserId());
            
            if( p.equals(personClicked) )
                index = i;
            
            ++i;
        }
        
        getArguments().putInt("index", index);
        mProfilesTaskMethod.doTask(User.getInstance().getUserId(), peopleIds);
    }
    
	
	@Override
	public void fetchVisitsTaskEventReceived(FetchVisitsTaskEvent evt) {
		FetchVisitsTaskMethod met = (FetchVisitsTaskMethod)evt.getSource();
		
		if( met != null && met.success() )
		{
			User.getInstance().setNbNewVisits(0);
			
			updateView(met.getVisits());
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
