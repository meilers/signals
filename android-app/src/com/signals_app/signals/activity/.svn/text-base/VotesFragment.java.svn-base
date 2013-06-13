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
import com.signals_app.signals.model.Profile.User.VoteEvent;
import com.signals_app.signals.model.Profile.User.VoteListener;
import com.signals_app.signals.model.Signals.Flirt;
import com.signals_app.signals.model.Signals.Vote;
import com.signals_app.signals.model.Signals.Vote;
import com.signals_app.signals.model.Signals.Signal;
import com.signals_app.signals.model.Signals.Vote;
import com.signals_app.signals.tasks.ProfilesTaskMethod;
import com.signals_app.signals.tasks.ProfilesTaskMethod.ProfilesTaskEvent;
import com.signals_app.signals.tasks.ProfilesTaskMethod.ProfilesTaskListener;
import com.signals_app.signals.tasks.signals.FetchVotesTaskMethod;
import com.signals_app.signals.tasks.signals.FetchVotesTaskMethod.FetchVotesTaskEvent;
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

public class VotesFragment extends SherlockFragment implements FetchVotesTaskMethod.FetchVotesTaskListener, PushListener, ProfilesTaskListener
{
    public static final int ACTION_REFRESH = 1;
    
	
	// TASK
	private FetchVotesTaskMethod mFetchVotesTaskMethod;
	private ProfilesTaskMethod mProfilesTaskMethod;
	
	// MODEL
    private VotesGridAdapter mVotesGridAdapter;
    private ArrayList<Vote> mVotes = null; 
    
    
	// TIMER
    private Handler mHandler = new Handler();
    private Runnable mRunnable = new Runnable() {

        @Override 
        public void run() {
            
            // CALCULATE TIME LEFT
            Calendar now = Calendar.getInstance();
            Calendar nextHour = Calendar.getInstance();
            Calendar timeLeft = Calendar.getInstance();
            
            nextHour.add(Calendar.HOUR, 1);
            nextHour.set(Calendar.MINUTE, 0);
            nextHour.set(Calendar.SECOND, 0);
            
            long milliseconds1 = now.getTimeInMillis();
            long milliseconds2 = nextHour.getTimeInMillis();
            long diff = milliseconds2 - milliseconds1;
            
            long hours = diff / (60 * 60 * 1000);
            long minutes = diff / (60 * 1000);
            minutes = minutes - 60 * hours;
            long seconds = diff / (1000) - (60*minutes); 
            
            timeLeft.set(Calendar.MINUTE, (int)minutes);
            timeLeft.set(Calendar.SECOND, (int)seconds);
            
            
            // UPDATE VIEW
            
            DecimalFormat nft = new  java.text.DecimalFormat("#00");  
            nft.setDecimalSeparatorAlwaysShown(false); 
            
            mCountdownTv.setText( nft.format(timeLeft.get(Calendar.MINUTE)) + ":" + nft.format(timeLeft.get(Calendar.SECOND)) );
            
            if( seconds == 3600L )
            {
                fetchVotes();
            }
            
            // RESTART TIMER
            mHandler.postDelayed(mRunnable, 1000);
        }
    };
	
    // WIDGETS
    private DisplayImageOptions mImageOptions;
    private LinearLayout mVotesThisHourLayout;
    private TextView mVotesThisHourNbTv;
    private TextView mCountdownTv;
	
	
	
  	public static final VotesFragment newInstance()
  	{
  		VotesFragment f = new VotesFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("index", 0);
        f.setArguments(bundle);
        
  	    return f;  
  	}
  	
  	
  	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        // IMAGE LOADER
        mImageOptions = new DisplayImageOptions.Builder()
                .cacheInMemory()
                .cacheOnDisc()
                .build();

        // MODEL
        mVotes = new ArrayList<Vote>();
        mFetchVotesTaskMethod = new FetchVotesTaskMethod(getSherlockActivity());
        mProfilesTaskMethod = new ProfilesTaskMethod(getSherlockActivity());

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		View view = inflater.inflate(R.layout.fragment_votes, container, false);
		getSherlockActivity().setTitle(getSherlockActivity().getResources().getString(R.string.voted_for_you));
		
		if( User.getInstance().checkedIn() )
		    getSherlockActivity().getSupportActionBar().setSubtitle(getString(R.string.tonight) + " " + getString(R.string.at) + " " + User.getInstance().getCheckPlace().getName());
		else
		    getSherlockActivity().getSupportActionBar().setSubtitle(null); 

		
		// LISTENERS
        PushReceiverActivity.addPushListener(this);
        mFetchVotesTaskMethod.addFetchVotesTaskListener(this);
        mProfilesTaskMethod.addProfilesTaskListener(this);
        
        
		// WIDGETS
		mVotesThisHourLayout = (LinearLayout)view.findViewById(R.id.votes_this_hour_layout);
	    mVotesThisHourNbTv = (TextView)view.findViewById(R.id.votes_this_hour_text);
	    mCountdownTv = (TextView)view.findViewById(R.id.countdown_text);
	    
	    ExpandableHeightGridView gv = (ExpandableHeightGridView)view.findViewById(R.id.gridview);
	    gv.setExpanded(true);
	    
        mVotesGridAdapter = new VotesGridAdapter(this.getActivity(), 0, mVotes);
        
        gv.setAdapter(mVotesGridAdapter);
        gv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> l, View v, int position, long id) {
                Vote f = mVotes.get(position);
                
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

		
		// CURRENT VOTES
		if( User.getInstance().checkedIn())
		{
		    mHandler.postDelayed(mRunnable, 1000);
		}
		
		
        fetchVotes();
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
        mHandler.removeCallbacks(mRunnable);
        PushReceiverActivity.removePushListener(this);
        mFetchVotesTaskMethod.removeFetchVotesTaskListener(this);
        mProfilesTaskMethod.removeProfilesTaskListener(this);
        
        mFetchVotesTaskMethod.cleanUp();
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
                fetchVotes();
                return true;
        }
        
        return false;
    }
    
    
    
    private void fetchVotes()
    {
    	mFetchVotesTaskMethod.doTask(User.getInstance().getUserId());
    }
    
    
	private void updateView( ArrayList<Vote> votes )
	{
        Place checkPlace = User.getInstance().getCheckPlace();
        
        ArrayList<Vote> votesThisHour = new ArrayList<Vote>();
        Calendar todayDate = Calendar.getInstance();
        
        for( Vote vote : votes )
        {
            Calendar voteDate = vote.getCreateTime();
            
            int todayYear = todayDate.get(Calendar.YEAR);
            int todayDay = todayDate.get(Calendar.DAY_OF_YEAR);
            int todayHour = todayDate.get(Calendar.HOUR_OF_DAY);
            
            int voteYear = voteDate.get(Calendar.YEAR);
            int voteDay = voteDate.get(Calendar.DAY_OF_YEAR);
            int voteHour = voteDate.get(Calendar.HOUR_OF_DAY);
            
            if( checkPlace != null && checkPlace.getPlaceId().equals(vote.getPlace().getPlaceId()) &&
                    todayYear == voteYear && 
                    todayDay == voteDay &&
                    todayHour ==  voteHour )
            {
                votesThisHour.add(vote);
            }      
        }
        
        if( User.getInstance().checkedIn() )
        {
            mVotesThisHourLayout.setVisibility(View.VISIBLE);
            mVotesThisHourNbTv.setText(getString(R.string.current_votes) + " " + votesThisHour.size() + ""); 
        }
        else
        {
            mVotesThisHourLayout.setVisibility(View.GONE);
        }
        
        

        
        // Update grid
        mVotes.clear();
        mVotes.addAll(votes);
        mVotesGridAdapter.notifyDataSetChanged();
	}
	
	
    private class VotesGridAdapter extends ArrayAdapter<Vote> {

        private ArrayList<Vote> mVotes;
        private Context mContext;
        
        public VotesGridAdapter(Context context, int textViewResourceId, ArrayList<Vote> votes ) {
                super(context, 0, votes);
                this.mVotes = votes;
                this.mContext = context;
        }
     
        @Override 
        public int getCount() {
            return mVotes.size();
        }
     
        @Override
        public Vote getItem(int position) {
            return mVotes.get(position);
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
            
            Vote vote = mVotes.get(position);
            Person p = vote.getPerson();
            
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
            
            
            // CURRENT VOTES
            Place checkPlace = User.getInstance().getCheckPlace();
            Calendar todayDate = Calendar.getInstance();
            
            Calendar voteDate = vote.getCreateTime();
            
            if( checkPlace != null && checkPlace.getPlaceId().equals(vote.getPlace().getPlaceId()) &&
                todayDate.get(Calendar.YEAR) == voteDate.get(Calendar.YEAR) && 
                todayDate.get(Calendar.DAY_OF_YEAR) == voteDate.get(Calendar.DAY_OF_YEAR) &&
                todayDate.get(Calendar.HOUR_OF_DAY) ==  voteDate.get(Calendar.HOUR_OF_DAY) )
            {
                v.setAlpha(1.0f);
            }       
            else
            {
                v.setAlpha(0.2f);
            }

            return v;
        }
    }



	
	
	
	// EVENTS
    
    private void onClickPerson( Person personClicked )
    {
        ArrayList<Long> peopleIds = new ArrayList<Long>();
        
        int index = 0;
        int i = 0;
        
        for( Vote v : mVotes )
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
	public void fetchVotesTaskEventReceived(FetchVotesTaskEvent evt) {
		FetchVotesTaskMethod met = (FetchVotesTaskMethod)evt.getSource();
		
		if( met != null && met.success() )
		{
			User.getInstance().setNbNewVotes(0);
			
			updateView(met.getVotes());
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
