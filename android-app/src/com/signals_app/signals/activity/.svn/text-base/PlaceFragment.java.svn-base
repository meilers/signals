package com.signals_app.signals.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Vector;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableRow;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
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
import com.signals_app.signals.model.Constants;
import com.signals_app.signals.model.Place;
import com.signals_app.signals.model.Profile.GpsPosition;
import com.signals_app.signals.model.Profile.Person;
import com.signals_app.signals.model.Profile.User;
import com.signals_app.signals.model.Profile.User.GpsPositionEvent;
import com.signals_app.signals.model.Profile.User.GpsPositionListener;
import com.signals_app.signals.model.Profile.User.PlaceEvent;
import com.signals_app.signals.model.Profile.User.PlaceListener;
import com.signals_app.signals.tasks.PeopleTaskMethod;
import com.signals_app.signals.tasks.PlaceCheckinTaskMethod;
import com.signals_app.signals.tasks.PlaceCheckoutTaskMethod;
import com.signals_app.signals.tasks.ProfilesTaskMethod;
import com.signals_app.signals.tasks.PlaceCheckoutTaskMethod.PlaceCheckoutTaskEvent;
import com.signals_app.signals.tasks.PlaceCheckoutTaskMethod.PlaceCheckoutTaskListener;
import com.signals_app.signals.tasks.PlaceTaskMethod;
import com.signals_app.signals.tasks.PeopleTaskMethod.PeopleTaskEvent;
import com.signals_app.signals.tasks.PlaceCheckinTaskMethod.PlaceCheckinTaskEvent;
import com.signals_app.signals.tasks.PlaceTaskMethod.PlaceTaskEvent;
import com.signals_app.signals.tasks.ProfilesTaskMethod.ProfilesTaskEvent;
import com.signals_app.signals.tasks.ProfilesTaskMethod.ProfilesTaskListener;
import com.signals_app.signals.util.Utility;

public class PlaceFragment extends SherlockFragment implements PlaceTaskMethod.PlaceTaskListener, PlaceCheckinTaskMethod.PlaceCheckinTaskListener, GpsPositionListener, PlaceListener, PlaceCheckoutTaskListener, ProfilesTaskListener
{
    public static final int ACTION_MAP = 1;
    public static final int ACTION_REFRESH = 2;

	private DisplayImageOptions mImageOptions;
	
	
	
	private PlaceTaskMethod mPlaceTaskMethod;
	private PlaceCheckinTaskMethod mPlaceCheckInTaskMethod; 	
	private PlaceCheckoutTaskMethod mPlaceCheckoutTaskMethod;  
	private ProfilesTaskMethod mProfilesTaskMethod;

	private Place mPlace; 
	
	
	private boolean mTooFar = false;
	private boolean mCheckedInHere = false;
	
	
	// Widgets
	private Button mPeekBtn;
	private Button mCheckinBtn;
	
	private ScrollView mScrollView;
	private LinearLayout mBubbleLayout;
	private LinearLayout mBtnsLayout;
	private LinearLayout mInBetweenLayout;
	private LinearLayout mMaleLayout;
	private LinearLayout mFemaleLayout;
	private LinearLayout mRow1;
	private LinearLayout mRow2;
	private LinearLayout mRow3;
	private LinearLayout mRow4;
	
	private TextView mFemaleNbText;
	private TextView mMaleNbText;
	private TextView mAgeText;
	private TextView mLookingRightMatchNbText;
	private TextView mLookingWhateverNbText;
	private TextView mLookingNoStringsNbText;
	
  	public static final PlaceFragment newInstance( Place place )
  	{
  		PlaceFragment f = new PlaceFragment();
  		
        // Get arguments passed in, if any
        Bundle args = f.getArguments();
        if (args == null) {
            args = new Bundle();
        }
        
        // Add parameters to the argument bundle
        args.putParcelable("place", place );
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
		View view = inflater.inflate(R.layout.fragment_place, container, false);
		
		
		// IMAGE LOADER
		mImageOptions = new DisplayImageOptions.Builder()
		.cacheInMemory()
		.cacheOnDisc()
		.build();
		
        mPlaceTaskMethod = new PlaceTaskMethod(getSherlockActivity());
        mPlaceCheckInTaskMethod = new PlaceCheckinTaskMethod(getSherlockActivity());
        mPlaceCheckoutTaskMethod = new PlaceCheckoutTaskMethod(getSherlockActivity());		
        mProfilesTaskMethod = new ProfilesTaskMethod(getSherlockActivity());
		
		// LISTENERS

        mPlaceTaskMethod.addPlaceTaskListener(this);
        mPlaceCheckInTaskMethod.addPlaceCheckinTaskListener(this);
        mPlaceCheckoutTaskMethod.addPlaceCheckoutTaskListener(this);
        mProfilesTaskMethod.addProfilesTaskListener(this);
        
        User u = User.getInstance();
        u.addGpsPositionListener(this);
        u.addPlaceListener(this);
        
        

        // ATTRIBUTES 
        
        Bundle extras = getArguments();
        mPlace = extras.getParcelable("place");

        getSherlockActivity().setTitle(mPlace.getName());
        getSherlockActivity().getSupportActionBar().setSubtitle(null);

	    
	    
        // WIDGETS
        
        mPeekBtn = (Button)view.findViewById(R.id.peekAction);
        mCheckinBtn = (Button)view.findViewById(R.id.checkInAction);

        mScrollView = (ScrollView)view.findViewById(R.id.scrollview);
        mBubbleLayout = (LinearLayout)view.findViewById(R.id.bubble_layout);
        mBtnsLayout = (LinearLayout)view.findViewById(R.id.buttons_layout);
    	mInBetweenLayout = (LinearLayout)view.findViewById(R.id.in_between_layout);
    	mMaleLayout = (LinearLayout)view.findViewById(R.id.male_layout);
    	mFemaleLayout = (LinearLayout)view.findViewById(R.id.female_layout);
    	mRow1 = (LinearLayout)view.findViewById(R.id.row1);
    	mRow2 = (LinearLayout)view.findViewById(R.id.row2);
    	mRow3 = (LinearLayout)view.findViewById(R.id.row3);
    	mRow4 = (LinearLayout)view.findViewById(R.id.row4);
    	
        mFemaleNbText = (TextView) view.findViewById(R.id.female_nb_text);
    	mMaleNbText = (TextView) view.findViewById(R.id.male_nb_text);    
        mAgeText = (TextView) view.findViewById(R.id.age_text);  
    	mLookingRightMatchNbText = (TextView) view.findViewById(R.id.looking_right_match_nb_text);
    	mLookingWhateverNbText = (TextView) view.findViewById(R.id.looking_whatever_nb_text);
    	mLookingNoStringsNbText = (TextView) view.findViewById(R.id.looking_no_strings_nb_text);
    	
    	
		// SET VIEW
    	
		ImageView iconImg = (ImageView)view.findViewById(R.id.icon_img);
		
    	switch(mPlace.getGenre())
    	{
    		case Place.CLUB:
    			iconImg.setImageResource(R.drawable.speaker);
    			break;
    		case Place.LOUNGE:
    			iconImg.setImageResource(R.drawable.martini);
    			break;
    		case Place.BAR:
    			iconImg.setImageResource(R.drawable.beer);
    			break;
    	};
		
		TextView placeNameText = (TextView)view.findViewById(R.id.place_name_text);
		placeNameText.setText(mPlace.getName());

		TextView placeAddressText = (TextView)view.findViewById(R.id.place_address_text);
		placeAddressText.setText(mPlace.getAddress());
		
        
        mPeekBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {   
            	onClickPeek();
            }
        });
        mCheckinBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {   
            	onClickCheckin();
            }
        });


        setHasOptionsMenu(true);


        return view;
    }
	
	@Override
	public void onResume() 
	{
		super.onResume();
		
		fetchPlace();
		updateButtons();
	}

	
	@Override
	public void onPause()
	{
		super.onPause();
		
	}
	
    
    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        
        User u = User.getInstance();
        u.removeGpsPositionListener(this);
        u.removePlaceListener(this);
        
        mPlaceTaskMethod.removePlaceTaskListener(this);
        mPlaceCheckInTaskMethod.removePlaceCheckinTaskListener(this);
        mProfilesTaskMethod.removeProfilesTaskListener(this);
        
        mPlaceTaskMethod.cleanUp();
        mPlaceCheckInTaskMethod.cleanUp();
        mProfilesTaskMethod.cleanUp();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Add your menu entries here
        super.onCreateOptionsMenu(menu, inflater);

        menu.clear();

        menu.add(0,0,ACTION_MAP,getString(R.string.map))
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        menu.add(0,0,ACTION_REFRESH,getString(R.string.refresh))
                .setIcon( R.drawable.ic_action_refresh)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getOrder())
        {
            case ACTION_REFRESH:
                fetchPlace();
                return true;

            case ACTION_MAP:
                Intent intent = new Intent(getSherlockActivity(), MapActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("selectedBar", mPlace);
                intent.putExtras(bundle);
                startActivityForResult(intent, PlacesFragment.PICK_PLACE_ON_MAP_REQUEST);
                return true;

        }

        return false;
    }



    void fetchPlace()
    {

	    mPlaceTaskMethod.doTask(User.getInstance().getUserId(), mPlace.getPlaceId()); 
    }
     
    
    void updateButtons()
    {
        mBtnsLayout.setVisibility(View.VISIBLE);
        
	    mCheckedInHere = false;
        Place p = User.getInstance().getCheckPlace();
        
        if( p != null && p.getPlaceId().equals(mPlace.getPlaceId()))
        {
        	mPeekBtn.setVisibility(View.INVISIBLE);
        	mCheckinBtn.setVisibility(View.VISIBLE);
        	mCheckinBtn.setClickable(true);
        	mCheckinBtn.setBackground(getResources().getDrawable(R.drawable.btn_black));
        	mCheckinBtn.setText(getString(R.string.checkout));
        	mCheckedInHere = true;
        }
        else
        {
        	mPeekBtn.setVisibility(View.VISIBLE);
            
        	mCheckinBtn.setVisibility(View.VISIBLE);
        	mCheckinBtn.setBackground(getResources().getDrawable(R.drawable.btn_grey));
        	mCheckinBtn.setText(getString(R.string.checkin));
        	mCheckedInHere = false;

    	
        	// Check if can check in
	    	GpsPosition userPos = User.getInstance().getGpsPosition();
			GpsPosition placePos = mPlace.getGpsPosition();
			int distanceInMeters = (int)(Utility.getDistance(userPos.getLatitude(), userPos.getLongitude(), placePos.getLatitude(), placePos.getLongitude(), "k")*1000);
			
			if( distanceInMeters-userPos.getAccuracy() > Constants.MIN_CHECKIN_RADIUS )
			{
				mCheckinBtn.setClickable(false);
				mCheckinBtn.setBackground(getResources().getDrawable(R.drawable.btn_grey));
				mCheckinBtn.setText(Html.fromHtml(getString(R.string.checkin) + "<br/><small><font color='#888888'>" + 
													getString(R.string.in) + " " + 
													(distanceInMeters-Constants.MIN_CHECKIN_RADIUS) + " " + getString(R.string.meters) + "</small></font>"));
				
				mTooFar = true;
			}
			else
			{
				mCheckinBtn.setBackground(getResources().getDrawable(R.drawable.btn_green));
				mCheckinBtn.setText(getString(R.string.checkin));
				mCheckinBtn.setClickable(true);
				
				mTooFar = false;
			}
        }
    }
    
	private void updateBubble()
	{
        // NUMBER OF USERS
        User u = User.getInstance();
        if(u.getInterestedIn() == Person.INTERESTED_IN_MEN)
        {
            mInBetweenLayout.setVisibility(View.GONE);
            mMaleLayout.setVisibility(View.VISIBLE);
            mFemaleLayout.setVisibility(View.GONE);
            
            mMaleNbText.setText(Integer.toString(mPlace.getNbUsersMale()));
        }
        
        else if(u.getInterestedIn() == Person.INTERESTED_IN_WOMEN)
        {
            mInBetweenLayout.setVisibility(View.GONE);
            mFemaleLayout.setVisibility(View.VISIBLE);
            mMaleLayout.setVisibility(View.GONE);
            
            mFemaleNbText.setText(Integer.toString(mPlace.getNbUsersFemale()));
        }
        
        if(u.getInterestedIn() == Person.INTERESTED_IN_BOTH )
        {
            mInBetweenLayout.setVisibility(View.VISIBLE);
            mFemaleLayout.setVisibility(View.VISIBLE);
            mMaleLayout.setVisibility(View.VISIBLE);
            
            mMaleNbText.setText(Integer.toString(mPlace.getNbUsersMale()));
            mFemaleNbText.setText(Integer.toString(mPlace.getNbUsersFemale()));
        }   
        
        if( mPlace.getNbUsersTotal() > 0 )
        {
            mAgeText.setText(getString(R.string.age) + " " + Integer.toString(mPlace.getAverageAge()));

            
            int nbUsersLookingRightMatch = mPlace.getNbUsersLookingRightMatch();
            int nbUsersLookingImOpen = mPlace.getNbUsersLookingImOpen();
            int nbUsersLookingNoStrings = mPlace.getNbUsersLookingNoStrings();
            
            // LOOKING FOR STATS 
            
            if( nbUsersLookingRightMatch > 0 ) 
            {
                mRow2.setVisibility(View.VISIBLE);
                mLookingRightMatchNbText.setText(Integer.toString(nbUsersLookingRightMatch));
            }
            else
                mRow2.setVisibility(View.GONE);
             
            if( nbUsersLookingImOpen > 0 )
            {
                mRow3.setVisibility(View.VISIBLE); 
                mLookingWhateverNbText.setText(Integer.toString(nbUsersLookingImOpen));
            }
            else 
                mRow3.setVisibility(View.GONE);
            
            if( nbUsersLookingNoStrings > 0 )
            {
                mRow4.setVisibility(View.VISIBLE); 
                mLookingNoStringsNbText.setText(Integer.toString(nbUsersLookingNoStrings));
            }
            else
                mRow4.setVisibility(View.GONE);
        }
        else  
        { 
            mInBetweenLayout.setVisibility(View.GONE);
            mFemaleLayout.setVisibility(View.GONE);
            mMaleLayout.setVisibility(View.GONE);
            
            mAgeText.setText(getString(R.string.no_users_msg));
            mAgeText.setTextSize(18);
            
            mRow1.getLayoutParams().height = LayoutParams.WRAP_CONTENT; 
        }
        
        
        // PICS
        
        final Person starFemale = mPlace.getStarFemale(); 
        final Person starMale = mPlace.getStarMale();
        
        RelativeLayout femalePicLayout = (RelativeLayout)getView().findViewById(R.id.female_pic_layout);
        RelativeLayout malePicLayout = (RelativeLayout)getView().findViewById(R.id.male_pic_layout);
        
        if( starFemale != null )
        {
            femalePicLayout.setVisibility(View.VISIBLE);
            
            ImageView femaleIv = (ImageView)getView().findViewById(R.id.female_pic_iv);
            femaleIv.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickPerson(starFemale);
                }}
            );
            
            Animation fadeInAnimation = AnimationUtils.loadAnimation(getSherlockActivity(), R.anim.fadein);
            ImageLoader.getInstance().displayImage("http://s3.amazonaws.com/signals/user_images/" + starFemale.getUserId() + "/profilePhotoThumb.jpg", femaleIv, mImageOptions);
            femaleIv.startAnimation(fadeInAnimation); 
        }
        else
            femalePicLayout.setVisibility(View.GONE);
        
        if( starMale != null )
        {
            malePicLayout.setVisibility(View.VISIBLE);
            
            ImageView maleIv = (ImageView)getView().findViewById(R.id.male_pic_iv);
            maleIv.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickPerson(starMale);
                }}
            );
            
            Animation fadeInAnimation = AnimationUtils.loadAnimation(getSherlockActivity(), R.anim.fadein);
            ImageLoader.getInstance().displayImage("http://s3.amazonaws.com/signals/user_images/" + starMale.getUserId() + "/profilePhotoThumb.jpg", maleIv, mImageOptions);
            maleIv.startAnimation(fadeInAnimation); 
        }
        else
            malePicLayout.setVisibility(View.GONE);
	}
    
	
	private void updateView()
	{
	    mScrollView.setVisibility(View.VISIBLE);
	    
	    updateBubble();
	    updateButtons();
	}
	
    // EVENTS

	private void onClickPerson( Person p )
    {
        ArrayList<Long> peopleIds = new ArrayList<Long>();
        
        final Person starFemale = mPlace.getStarFemale(); 
        final Person starMale = mPlace.getStarMale();
        
        int index = 0;
        
        if( starFemale != null )
            peopleIds.add(starFemale.getUserId());
        
        if( starMale != null)
            peopleIds.add(starMale.getUserId());
        
        if( starFemale != null && p.equals(starMale) )
            index = 1;
        
        getArguments().putInt("index", index);
        mProfilesTaskMethod.doTask(User.getInstance().getUserId(), peopleIds);
    }
	    
	   
	public void onClickPeek()
	{
	    // Set place
	    User.getInstance().setPeekPlace(new Place(mPlace));
	    
	    
        // Peek
	    PeopleInsidePeekFragment frag =  PeopleInsidePeekFragment.newInstance( User.getInstance().getPeekPlace() );
	    
	    getSherlockActivity().getSupportFragmentManager()
	    .beginTransaction()
	    .replace(R.id.content_frame, frag, "PeopleInsidePeekFragment")
	    .addToBackStack(null)
	    .commit();
	}
	
	public void onClickCheckin()
	{
    	if( mCheckedInHere )
    	{
    	    mPlaceCheckoutTaskMethod.doTask(User.getInstance().getUserId(), PlaceCheckoutTaskMethod.CHECK_OUT_BY_USER);
    	}
    	else
    	{
        	if( !mTooFar )
        	{
        	    User u = User.getInstance();
        	    
                if( u.checkedIn() )
                {
                    Place place = u.getCheckPlace();
                    
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getSherlockActivity());
                    Resources r = getSherlockActivity().getResources();
                    
                    alertDialogBuilder.setTitle(r.getString(R.string.checkout));
             
                    alertDialogBuilder
                        .setMessage(r.getString(R.string.checkout_from) + " " + place.getName() + "?")
                        .setCancelable(false)
                        .setPositiveButton(r.getString(R.string.checkout),new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                mPlaceCheckInTaskMethod.doTask(User.getInstance().getUserId(), mPlace.getPlaceId());
                            }
                          })
                        .setNegativeButton(r.getString(R.string.cancel),new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });
             
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();                
                }
                else
                    mPlaceCheckInTaskMethod.doTask(User.getInstance().getUserId(), mPlace.getPlaceId());
        	}
    	}
	}
	
	public void placeTaskEventReceived(PlaceTaskEvent evt)
	{	
		PlaceTaskMethod met = (PlaceTaskMethod)evt.getSource();
		
		if( met != null && met.success() )
		{
		    Place p = met.getPlace();
		    mPlace.setPeopleBeforeHashMap(p.getPeopleBeforeHashMap());
		    
	
		    updateView();
		}
	}
	
	public void placeCheckinTaskEventReceived(PlaceCheckinTaskEvent evt)
	{		
		PlaceCheckinTaskMethod met = (PlaceCheckinTaskMethod)evt.getSource();
		
		if( met != null && met.success() )
		{
            User.getInstance().removePlaceListener(this);


		    // Set place
		    User.getInstance().setPeekPlace(null);
		    User.getInstance().setCheckPlace(new Place(mPlace));
            
            // Enter
		    PeopleInsideFragment frag =  PeopleInsideFragment.newInstance();
		    
		    getSherlockActivity().getSupportFragmentManager()
		    .beginTransaction()
		    .replace(R.id.content_frame, frag, "PeopleInsideFragment")
		    .addToBackStack(null)
		    .commit();



		}
	}
	
	@Override
	public void gpsPositionEventReceived(GpsPositionEvent evt) 
	{
		User user = (User)evt.getSource();
		
		if( user != null )
		{
			updateButtons();

		}
	}


	@Override
	public void placeEventReceived(PlaceEvent evt) {
		User user = (User)evt.getSource();
		
		if( user != null )
		{
		    updateButtons();
		}
	}


    @Override
    public void placeCheckoutTaskEventReceived(PlaceCheckoutTaskEvent evt) 
    {
        PlaceCheckoutTaskMethod met = (PlaceCheckoutTaskMethod)evt.getSource();
        
        if( met != null && met.success() )
        {
            User.getInstance().setCheckPlace(null);
            updateButtons();
        }
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
 
