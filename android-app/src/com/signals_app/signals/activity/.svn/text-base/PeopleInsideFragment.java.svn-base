package com.signals_app.signals.activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

import android.os.Handler;
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

import com.signals_app.signals.model.Constants;
import com.signals_app.signals.model.Place;
import com.signals_app.signals.model.Profile.User.PlaceEvent;
import com.signals_app.signals.model.Profile.User.PlaceListener;
import com.signals_app.signals.model.Profile.GpsPosition;
import com.signals_app.signals.model.Profile.Person;
import com.signals_app.signals.model.Profile.User;
import com.signals_app.signals.model.Profile.User.GpsPositionEvent;
import com.signals_app.signals.model.Profile.User.GpsPositionListener;
import com.signals_app.signals.tasks.MyProfileEditApproachTaskMethod;
import com.signals_app.signals.tasks.PeopleTaskMethod;
import com.signals_app.signals.tasks.ProfilesTaskMethod;
import com.signals_app.signals.tasks.PeopleTaskMethod.PeopleTaskEvent;
import com.signals_app.signals.tasks.PlaceCheckinTaskMethod;
import com.signals_app.signals.tasks.PlaceCheckinTaskMethod.PlaceCheckinTaskEvent;
import com.signals_app.signals.tasks.PlaceCheckinTaskMethod.PlaceCheckinTaskListener;
import com.signals_app.signals.tasks.PlaceCheckoutTaskMethod;
import com.signals_app.signals.tasks.PlaceCheckoutTaskMethod.PlaceCheckoutTaskEvent;
import com.signals_app.signals.tasks.PlaceCheckoutTaskMethod.PlaceCheckoutTaskListener;
import com.signals_app.signals.tasks.ProfilesTaskMethod.ProfilesTaskEvent;
import com.signals_app.signals.tasks.ProfilesTaskMethod.ProfilesTaskListener;
import com.signals_app.signals.util.Utility;

import android.app.ActionBar.LayoutParams;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class PeopleInsideFragment extends SherlockFragment implements PlaceListener, PeopleTaskMethod.PeopleTaskListener, ProfilesTaskListener, PlaceCheckoutTaskListener
{
	public static final int ACTION_REFRESH = 2;
    public static final int ACTION_CHECK_OUT = 1;
    
	private PeopleTaskMethod mPeopleTaskMethod;
	private ProfilesTaskMethod mProfilesTaskMethod;
	private PlaceCheckoutTaskMethod mPlaceCheckoutTaskMethod;
	
	private DisplayImageOptions mImageOptions;
	
	// WIDGETS
	private ArrayList<Person> mPeople = null; 
    private PeopleListAdapter mListAdapter;
    private PeopleGridAdapter mGridAdapter;
    private boolean mFlashRows[] = new boolean[0];
	
    private LinearLayout mBubbleLayout;
	private TextView mBubbleTv;
	
	
	private int mIndexPager = 0;
	
	
  	public static final PeopleInsideFragment newInstance()
  	{
  		PeopleInsideFragment f = new PeopleInsideFragment();
        Bundle bundle = new Bundle();
        f.setArguments(bundle);
        
  	    return f;
  	}
  	
	@Override
	public void onCreate(Bundle savedInstanceState) { 
		super.onCreate(savedInstanceState);

        mPlaceCheckoutTaskMethod = new PlaceCheckoutTaskMethod(getSherlockActivity());
        mPeopleTaskMethod = new PeopleTaskMethod(getSherlockActivity());
        mProfilesTaskMethod = new ProfilesTaskMethod(getSherlockActivity());
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_people_inside, container, false);	
		getSherlockActivity().getSupportActionBar().setSubtitle(null);
		

		
		
		mImageOptions = new DisplayImageOptions.Builder()
        .cacheInMemory()
        .cacheOnDisc()
        //.displayer(new RoundedBitmapDisplayer(10))
        .build();
        
		
		// LISTENERS
        
		User u = User.getInstance();
        u.addPlaceListener(this);
        mPeopleTaskMethod.addPeopleTaskListener(this);
        mProfilesTaskMethod.addProfilesTaskListener(this);
        mPlaceCheckoutTaskMethod.addPlaceCheckoutTaskListener(this);
        
        
        // LIST OR GRID
        mPeople = new ArrayList<Person>();
        
        switch (getActivity().getResources().getConfiguration().orientation ) 
        {
            case Configuration.ORIENTATION_LANDSCAPE:
                GridView gv = (GridView)view.findViewById(R.id.gridview);
                mGridAdapter = new PeopleGridAdapter(this.getActivity(), 0, mPeople);
                
                gv.setAdapter(mGridAdapter);
                gv.setOnItemClickListener(new OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> l, View v, int position, long id) {
                        if( mPeople.get(position).getUserId() != 0) 
                        {
                            Place place = User.getInstance().getCheckPlace();
                            
                            if( place == null || place.getPeopleNewHashMap() == null || place.getPeopleNewHashMap().size() == 0 )
                                position = position+1;
                            
                            Person p = mPeople.get(position);
                            
                            onClickPerson(p);
                        }
                    }}
                );
                break;
                
            default:
                ListView lv = (ListView)view.findViewById(R.id.listview);
                mListAdapter = new PeopleListAdapter(this.getActivity(), R.layout.row_person_inside, mPeople);
                
                lv.setAdapter(mListAdapter);
                lv.setOnItemClickListener(new OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> l, View v, int position, long id) 
                    {
                        Place place = User.getInstance().getCheckPlace();
                        
                        if( place == null || place.getPeopleNewHashMap() == null || place.getPeopleNewHashMap().size() == 0 )
                            position = position+1;
                        
                        if( mPeople.get(position).getUserId() != 0) 
                        {
                            Person p = mPeople.get(position);
                            
                            onClickPerson(p);
                        }
                    }}
                );
                
                break;
        }  
		        
        
        
        // BUBBLE
        mBubbleLayout = (LinearLayout)view.findViewById(R.id.bubble_layout);
        mBubbleTv = (TextView)view.findViewById(R.id.bubble_tv);


        setHasOptionsMenu(true);
        
		return view;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
	    // TODO Add your menu entries here
	    super.onCreateOptionsMenu(menu, inflater);
	    
	    menu.clear();
	    
        if( User.getInstance().checkedIn() )
        {
            menu.add(0,0,ACTION_CHECK_OUT,getString(R.string.check_out))
                    .setIcon( R.drawable.door)
                    .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

            menu.add(0,0,ACTION_REFRESH,getString(R.string.refresh))
            .setIcon( R.drawable.ic_action_refresh)
            .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        }
	}
	
	
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getOrder())
        {
	        case ACTION_REFRESH:
	            fetchPeople();
	            return true;

            case ACTION_CHECK_OUT:
                User u = User.getInstance();

                if( u.checkedIn() )
                {
                    Place place = u.getCheckPlace();

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                    Resources r = getActivity().getResources();

                    alertDialogBuilder.setTitle(r.getString(R.string.checkout));

                    alertDialogBuilder
                            .setMessage(r.getString(R.string.checkout_from) + " " + place.getName() + "?")
                            .setCancelable(true)
                            .setPositiveButton(r.getString(R.string.checkout),new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    mPlaceCheckoutTaskMethod.doTask(User.getInstance().getUserId(), PlaceCheckoutTaskMethod.CHECK_OUT_BY_USER);
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
                return true;
	            
        }
        
        return false;
    }
    
	
    @Override
    public void onResume() {
        super.onResume();
        
        fetchPeople();
    } 
    
    
    @Override
    public void onPause() {
        super.onPause();
        

    }

    

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        
        User.getInstance().removePlaceListener(this);
        mPeopleTaskMethod.removePeopleTaskListener(this);
        mProfilesTaskMethod.removeProfilesTaskListener(this);
        mPlaceCheckoutTaskMethod.removePlaceCheckoutTaskListener(this);

        mPeopleTaskMethod.cleanUp();
        mProfilesTaskMethod.cleanUp();
        mPlaceCheckoutTaskMethod.cleanUp();
    }
	
    @Override
    public void onDestroy()
    {
    	super.onDestroy();
    }
    
    
    
    public void fetchPeople()
    {   
        User u = User.getInstance();
        Place place = u.getPlace();
        GpsPosition pos = u.getGpsPosition();
        
        if( pos != null )
        {
            if( place != null )
                mPeopleTaskMethod.doTask(u.getUserId(), place.getPlaceId(), pos.getLatitude(), pos.getLongitude(), pos.getAccuracy(), 20); 
            else
                mPeopleTaskMethod.doTask(u.getUserId(), 0L, pos.getLatitude(), pos.getLongitude(), pos.getAccuracy(), 20); 
        }
    }
    
    
    private void updatePeople( boolean showNewPeople )
    {
        mPeople.clear();
        mPeople.add(new Person());  // For new people row
        
        Place place = User.getInstance().getCheckPlace();
        Person.PersonComparator comparator = new Person.PersonComparator();
        
        
        if( place != null )
        {
            HashMap<Long,Person> peopleBeforeHashMap = place.getPeopleBeforeHashMap();
            HashMap<Long,Person> peopleNewHashMap = place.getPeopleNewHashMap();
            
            if( peopleBeforeHashMap != null && peopleNewHashMap != null )
            {
                ArrayList<Person> peopleNew = new ArrayList<Person>(peopleNewHashMap.values());   
                ArrayList<Person> peopleBefore = new ArrayList<Person>(peopleBeforeHashMap.values());
                
                if(showNewPeople)
                {
                    getSherlockActivity().setTitle(place.getName());
                    
                    Collections.sort(peopleNew, comparator);
                    Collections.sort(peopleBefore, comparator);
                    
                    mPeople.addAll(peopleNew);
                    mPeople.addAll(peopleBefore);  
                    
                    // for flashing rows
                    mFlashRows = new boolean[mPeople.size()];
                    Arrays.fill(mFlashRows,false);
                    for( int i=0;i<peopleNew.size();++i )
                        mFlashRows[i+1 /*NEW PEOPLE OFFSET*/] = true;
                    
                    // set new people to before people
                    for( Person p : peopleNewHashMap.values() )
                    {
                        if(!peopleBeforeHashMap.containsKey(p.getUserId()))
                            peopleBeforeHashMap.put(p.getUserId(), p);
                    }
                    place.setPeopleNewHashMap(new HashMap<Long,Person>());
                    

                }
                else
                {
                    getSherlockActivity().setTitle(place.getName() + (peopleNewHashMap.size() != 0 ? " (" + peopleNewHashMap.size() + ")" : ""));
                     
                    Collections.sort(peopleBefore, comparator);
                    
                    mPeople.addAll(peopleBefore); 
                    
                    // set new people to before people
                    mFlashRows = new boolean[mPeople.size()];
                    Arrays.fill(mFlashRows,false);
                }
                
                switch( User.getInstance().getInterestedIn() )
                {
                    case Person.INTERESTED_IN_WOMEN:
                        getSherlockActivity().getSupportActionBar().setSubtitle( place.getNbUsersFemale() + " " + getString(R.string.girls));
                        break;
                        
                    case Person.INTERESTED_IN_MEN:
                        getSherlockActivity().getSupportActionBar().setSubtitle( place.getNbUsersMale() + " " + getString(R.string.guys));
                        break;
                        
                    case Person.INTERESTED_IN_BOTH:
                        getSherlockActivity().getSupportActionBar().setSubtitle( place.getNbUsersTotal() + " " + getString(R.string.users));
                        break;
                }
                
            }
        }
        

        
    }
    
    public void updateList( boolean showNewPeople )
    {
        updatePeople( showNewPeople );
		
        // Update!
        mListAdapter.notifyDataSetChanged();
    }
    
    public void updateGrid( boolean showNewPeople )
    {
        updatePeople( showNewPeople );
        
        // Update!
        mGridAdapter.notifyDataSetChanged();
    }

	public void updateView()
	{
	    
	    RelativeLayout root = (RelativeLayout)getView().findViewById(R.id.people_layout);
	    root.setVisibility(View.VISIBLE);
	    
		// VIEW
		if( !User.getInstance().checkedIn() )
		{
		    getActivity().setTitle(getResources().getString(R.string.people));
		    getSherlockActivity().getSupportActionBar().setSubtitle(null);
		}
		else
		{
		    getActivity().setTitle(User.getInstance().getCheckPlace().getName());
		    getSherlockActivity().getSupportActionBar().setSubtitle(null);
		    
		    Place place = User.getInstance().getCheckPlace();
		    
		    // Bubble
		    if( place.getPeopleBeforeHashMap() != null && place.getPeopleNewHashMap() != null )
            {
                int nbPeoplePeekPlace = User.getInstance().getPlace().getPeopleBeforeHashMap().size() + User.getInstance().getPlace().getPeopleNewHashMap().size();
                if( nbPeoplePeekPlace > 0 )
                    mBubbleLayout.setVisibility(View.GONE);
                else
                    mBubbleLayout.setVisibility(View.VISIBLE);
            }
            else
                mBubbleLayout.setVisibility(View.VISIBLE);
            
            mBubbleTv.setText(getString(R.string.checkedin_no_one_there_msg));
		}
		
        // List | Grid
        switch (getActivity().getResources().getConfiguration().orientation ) 
        {
            case Configuration.ORIENTATION_LANDSCAPE:
                updateGrid(false);
                break;
                
            default:
                updateList(false);
                break;
        } 
        
        getSherlockActivity().invalidateOptionsMenu();

	}



    private class PeopleListAdapter extends ArrayAdapter<Person> {

	    private ArrayList<Person> mPeople;
		
	    public PeopleListAdapter(Context context, int textViewResourceId, ArrayList<Person> people ) {
	            super(context, textViewResourceId, people);
	            this.mPeople = people;
	    }

	    
	    @Override
	    public View getView(int position, View convertView, ViewGroup parent) 
	    {
            View v = convertView;
            Place place = User.getInstance().getPlace();
            
            if( v == null )
            {
                LayoutInflater vi = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.row_person_inside, null);
                Button newPeopleBtn = (Button)v.findViewById(R.id.new_people_btn); 
                newPeopleBtn.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {  
                        onClickNewPeople();
                    }
                });
            }
            
            Button newPeopleBtn = (Button)v.findViewById(R.id.new_people_btn); 
            newPeopleBtn.setVisibility(View.GONE);
            
            RelativeLayout personLayout = (RelativeLayout)v.findViewById(R.id.person_layout); 
            personLayout.setVisibility(View.VISIBLE);
            
            final LinearLayout coverLayout = (LinearLayout)v.findViewById(R.id.cover_layout);
            coverLayout.setVisibility(View.GONE);
            
            
            if( place == null || place.getPeopleNewHashMap() == null || place.getPeopleNewHashMap().size() == 0 )
                position = position+1;
            else if( position == 0 )
            {
                newPeopleBtn.setVisibility(View.VISIBLE);
                personLayout.setVisibility(View.GONE);
                HashMap<Long, Person> peopleNewHashMap = place.getPeopleNewHashMap();
                newPeopleBtn.setText(peopleNewHashMap.size() + " " + (peopleNewHashMap.size() == 1 ? getResources().getString(R.string.new_person): getResources().getString(R.string.new_people)));
                
                return v;
            }


            /**** PEOPLE ****/
            
            Person p = mPeople.get(position);
       
            
            // NEW PEOPLE
            if( mFlashRows[position] )
            {
                coverLayout.setVisibility(View.VISIBLE);
                Animation fadeOutAnimation = AnimationUtils.loadAnimation(getSherlockActivity(), R.anim.fadeout);
                fadeOutAnimation.setAnimationListener(new AnimationListener() {
                    public void onAnimationStart(Animation anim)
                    {
                    };
                    public void onAnimationRepeat(Animation anim)
                    {
                    };
                    public void onAnimationEnd(Animation anim)
                    {
                        if( coverLayout != null )
                            coverLayout.setVisibility(View.GONE);
                    };
                });  
                
                coverLayout.startAnimation(fadeOutAnimation);
                
                mFlashRows[position] = false;           // flash only once
            }
            
            
        	// PIC
        	
        	ImageView iv = (ImageView)v.findViewById(R.id.profile_pic);
			Animation fadeInAnimation = AnimationUtils.loadAnimation(getSherlockActivity(), R.anim.fadein);
			ImageLoader.getInstance().displayImage("http://s3.amazonaws.com/signals/user_images/" + p.getUserId() + "/profilePhotoThumb.jpg", iv, mImageOptions);
			iv.startAnimation(fadeInAnimation); 
    		 
			
    		// USERNAME
    		TextView usernameText = (TextView)v.findViewById(R.id.username_text);
    		usernameText.setText(p.getUsername());
    		
    		
    		// BASIC INFO
    		TextView basicInfoText = (TextView)v.findViewById(R.id.basic_info_text);
    		final String[] orientations = getResources().getStringArray(R.array.orientation_list);
    		basicInfoText.setText(p.getAge() + " / " 
    							+ (p.getSex() == 1 ? getResources().getString(R.string.male_short): getResources().getString(R.string.female_short)) 
    							+ " / " + orientations[p.getOrientation()-1]  );

    		
     		
    		// LOOKING FOR
    		ImageView lookingImg = (ImageView)v.findViewById(R.id.looking_img);
    		
    		if( p.getLookingFor() != Person.LOOKING_NONE )
    		{
    			lookingImg.setVisibility(View.VISIBLE);
    		
    			switch( p.getLookingFor() )
    			{
    				case Person.LOOKING_RIGHT_MATCH:
    					lookingImg.setImageDrawable(getResources().getDrawable(R.drawable.citrus_r));
    					break;
    				case Person.LOOKING_IM_OPEN:
    					lookingImg.setImageDrawable(getResources().getDrawable(R.drawable.citrus_o));
    					break;		
    				case Person.LOOKING_NO_STRINGS:
    					lookingImg.setImageDrawable(getResources().getDrawable(R.drawable.citrus_g));
    					break;		
    			}
    		}
    		else
    			lookingImg.setVisibility(View.INVISIBLE);
    		
    		
    		// STAR
    		ImageView starIv = (ImageView)v.findViewById(R.id.star_iv);
    		
    		if( p.star() )
    		    starIv.setVisibility(View.VISIBLE);
    		else
    		    starIv.setVisibility(View.INVISIBLE);
    		
    		
            return v;
	    }
	    
	    @Override
	    public int getCount() {
	        Place place = User.getInstance().getPlace();
	        
	        if(null != mPeople)
            {
                try 
                {
                    if(place == null || place.getPeopleNewHashMap() == null || place.getPeopleNewHashMap().size() == 0)
                        return mPeople.size()-1;
                    else
                        return mPeople.size();
                } 
                catch (IndexOutOfBoundsException e) {
                    return 0;
                }
            }
	        
	        return 0;
	    }


	}

	private class PeopleGridAdapter extends ArrayAdapter<Person> {

        private ArrayList<Person> mPeople;
        private Context mContext;
        
        public PeopleGridAdapter(Context context, int textViewResourceId, ArrayList<Person> people ) {
                super(context, 0, people);
                this.mPeople = people;
                this.mContext = context;
        }
	 
        @Override
        public int getCount() {
            Place place = User.getInstance().getPlace();
            
            if(null != mPeople)
            {
                try 
                {
                    if(place == null || place.getPeopleNewHashMap() == null || place.getPeopleNewHashMap().size() == 0)
                        return mPeople.size()-1;
                    else
                        return mPeople.size();
                } 
                catch (IndexOutOfBoundsException e) {
                    return 0;
                }
            }
            
            return 0;
        }
	 
	    @Override
	    public Person getItem(int position) {
	        return mPeople.get(position);
	    }
	 
	    @Override
	    public long getItemId(int position) {
	        return 0;
	    }
	 
	    @Override
	    public View getView(int position, View convertView, ViewGroup parent) 
	    {
	        View v = convertView;
            Place place = User.getInstance().getPlace();
         
            DisplayMetrics metrics = new DisplayMetrics();
            getSherlockActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
            int padding = (int)Utility.convertDpToPixel(10, mContext);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams((int)(metrics.widthPixels/6), (int)(metrics.widthPixels/6));
            
            
            if( v == null )
            {
                LayoutInflater vi = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.box_person_inside, null);
                Button newPeopleBtn = (Button)v.findViewById(R.id.new_people_btn); 
                FrameLayout.LayoutParams btnParams = new FrameLayout.LayoutParams((int)(metrics.widthPixels/6)-2*padding, (int)(metrics.widthPixels/6)-2*padding, Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
                btnParams.setMargins(padding, padding, padding, padding);
                newPeopleBtn.setLayoutParams(btnParams);
                
                
                // Button backround
                LayerDrawable btnBlackLayerList = (LayerDrawable)getResources().getDrawable(R.drawable.btn_black_background).mutate();
                btnBlackLayerList.setLayerInset(1, (int)Utility.convertDpToPixel(1, mContext), (int)Utility.convertDpToPixel(1, mContext), (int)Utility.convertDpToPixel(1, mContext), ((metrics.widthPixels)/6-2*padding)/2);
                StateListDrawable states = new StateListDrawable();
                states.addState(new int[] {android.R.attr.state_pressed},getResources().getDrawable(R.drawable.btn_black_background_pressed));
                states.addState(new int[] {android.R.attr.state_focused},getResources().getDrawable(R.drawable.btn_black_background_pressed));
                states.addState(new int[] { },btnBlackLayerList);
                newPeopleBtn.setBackground(states);
                
                newPeopleBtn.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {  
                        onClickNewPeople();
                    }
                });
            }
            
            Button newPeopleBtn = (Button)v.findViewById(R.id.new_people_btn); 
            newPeopleBtn.setVisibility(View.GONE);
            
            RelativeLayout personLayout = (RelativeLayout)v.findViewById(R.id.person_layout); 
            personLayout.setVisibility(View.VISIBLE);
            
            final LinearLayout coverLayout = (LinearLayout)v.findViewById(R.id.cover_layout);
            coverLayout.setVisibility(View.GONE);
            
            
            if( place == null || place.getPeopleNewHashMap() == null || place.getPeopleNewHashMap().size() == 0 )
                position = position+1;
            else if( position == 0 )
            {
                newPeopleBtn.setVisibility(View.VISIBLE);
                personLayout.setVisibility(View.GONE);
                HashMap<Long, Person> peopleNewHashMap = place.getPeopleNewHashMap();
                newPeopleBtn.setText(peopleNewHashMap.size() + " " + getResources().getString(R.string.new1));
                return v;
            }
	        
            
            
            /**** PEOPLE ****/
            
            Person p = mPeople.get(position);
            
            // NEW PEOPLE
            if( mFlashRows[position] )
            {
                coverLayout.setVisibility(View.VISIBLE);
                Animation fadeOutAnimation = AnimationUtils.loadAnimation(getSherlockActivity(), R.anim.fadeout);
                fadeOutAnimation.setAnimationListener(new AnimationListener() {
                    public void onAnimationStart(Animation anim)
                    {
                    };
                    public void onAnimationRepeat(Animation anim)
                    {
                    };
                    public void onAnimationEnd(Animation anim)
                    {
                        if( coverLayout != null )
                            coverLayout.setVisibility(View.GONE);
                    };
                });  
                
                coverLayout.startAnimation(fadeOutAnimation);
                
                mFlashRows[position] = false;           // flash only once
            }
            
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
	
	private void onClickNewPeople()
    {
	    Place place = User.getInstance().getPlace();
	    
	    if( place != null ) 
	    {
	        switch (getActivity().getResources().getConfiguration().orientation ) 
            {
                case Configuration.ORIENTATION_LANDSCAPE: 
                    updateGrid(true);
                    break;
                    
                default:  
                    updateList(true);
                    break;
            }  
	        
	        MainActivity act = (MainActivity)getSherlockActivity();

            if( act.getMenuFragment().getView() != null )
	            act.getMenuFragment().updateView(act.getMenuFragment().getView());
	    }
    }
   
	private void onClickPerson( Person personClicked )
    {
	    ArrayList<Long> peopleIds = new ArrayList<Long>();
	    Person personArtifice = new Person();
	    
	    int index = 0;
	    int i = 0;
	    
	    for( Person p : mPeople )
	    {
	        if( !p.equals(personArtifice) )
	        {
	            peopleIds.add(p.getUserId());
	            
	            if( p.equals(personClicked) )
	                index = i;
	            
	            ++i;
	        }
	    }
	    
	    mIndexPager = index;
	    mProfilesTaskMethod.doTask(User.getInstance().getUserId(), peopleIds);
    }
	


	@Override
	public void placeEventReceived(PlaceEvent evt) {
		User user = (User)evt.getSource();
		
		if( user != null )
		{
			updateView();
		}
	}

    @Override
    public void peopleTaskEventReceived(PeopleTaskEvent evt)
    {       
        //mHandler.removeCallbacks(mRunnable);
        PeopleTaskMethod met = (PeopleTaskMethod)evt.getSource();
        
        if( met != null && met.success() )
        {
            // PEOPLE BAR
            ArrayList<Person> peopleBar = met.getPeopleBar();
            
            User u = User.getInstance();
            Place place = u.getPlace();
            
            if( place != null )
            {
                HashMap<Long, Person> peopleBeforeHashMapOriginal = place.getPeopleBeforeHashMap();
                HashMap<Long, Person> peopleNewHashMap = new HashMap<Long, Person>();                       // in case new people have left
                HashMap<Long, Person> peopleBeforeHashMap = new HashMap<Long, Person>();                    // in case before people have left
                
                if( peopleBeforeHashMapOriginal != null )
                {
                    
                    for( Person p : peopleBar )
                    {
                        if(!peopleBeforeHashMapOriginal.containsKey(p.getUserId()) && !p.equals(User.getInstance()))
                        {
                            if(!peopleNewHashMap.containsKey(p.getUserId()))
                                peopleNewHashMap.put(p.getUserId(), p);
                        }
                        else
                        {
                            if(!peopleBeforeHashMap.containsKey(p.getUserId()))
                                peopleBeforeHashMap.put(p.getUserId(), p);
                        }
                    }
                }
                else
                {
                    for( Person p : peopleBar )
                    {
                        if(!peopleBeforeHashMap.containsKey(p.getUserId()))
                            peopleBeforeHashMap.put(p.getUserId(), p);
                    }
                }
                    
                place.setPeopleBeforeHashMap(peopleBeforeHashMap);
                place.setPeopleNewHashMap(peopleNewHashMap);
                
                // Notify menu
                MainActivity act = (MainActivity)getSherlockActivity();

                if( act.getMenuFragment().getView() != null )
                    act.getMenuFragment().updateView(act.getMenuFragment().getView());
            }
            
            updateView();
        }
    }


    @Override
    public void profilesTaskEventReceived(ProfilesTaskEvent evt) {
        ProfilesTaskMethod met = (ProfilesTaskMethod)evt.getSource();
        
        if( met != null && met.success() )
        {
            ProfilesPagerFragment frag = ProfilesPagerFragment.newInstance(met.getPeople(), mIndexPager);
            
            getSherlockActivity().getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.content_frame, frag, "ProfilesPagerFragment")
            .addToBackStack(null)
            .commit();
        }   
    }


    @Override
    public void placeCheckoutTaskEventReceived(PlaceCheckoutTaskEvent evt) {
        PlaceCheckoutTaskMethod met = (PlaceCheckoutTaskMethod)evt.getSource();

        if( met != null && met.success() )
        {
            User.getInstance().setCheckPlace(null);
            getActivity().getSupportFragmentManager().popBackStack();
        }
    }
}
