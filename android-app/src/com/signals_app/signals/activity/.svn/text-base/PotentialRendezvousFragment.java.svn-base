package com.signals_app.signals.activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

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
import com.signals_app.signals.model.rendezvous.PotentialRendezvous;
import com.signals_app.signals.model.rendezvous.PotentialRendezvous;
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
import com.signals_app.signals.tasks.rendezvous.FetchPotentialRendezvousTaskMethod;
import com.signals_app.signals.tasks.rendezvous.FetchPotentialRendezvousTaskMethod.FetchPotentialRendezvousTaskEvent;
import com.signals_app.signals.tasks.rendezvous.FetchPotentialRendezvousTaskMethod.FetchPotentialRendezvousTaskListener;
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
import android.support.v4.app.FragmentManager;
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

public class PotentialRendezvousFragment extends SherlockFragment implements PlaceListener, ProfilesTaskListener, FetchPotentialRendezvousTaskMethod.FetchPotentialRendezvousTaskListener
{
	public static final int ACTION_REFRESH = 1;
    

	private ProfilesTaskMethod mProfilesTaskMethod;
    private FetchPotentialRendezvousTaskMethod mFetchPotentialRendezvousTaskMethod;

    private DisplayImageOptions mImageOptions;
	
	// WIDGETS
    private PotentialRendezvousListAdapter mListAdapter;
    private PotentialRendezvousGridAdapter mGridAdapter;
	
	private int mIndexPager = 0;
	
	
	
  	public static final PotentialRendezvousFragment newInstance()
  	{
  	    PotentialRendezvousFragment f = new PotentialRendezvousFragment();
        Bundle bundle = new Bundle();
        f.setArguments(bundle);
        
  	    return f;
  	}
  	
	@Override
	public void onCreate(Bundle savedInstanceState) { 
		super.onCreate(savedInstanceState); 
		
        mProfilesTaskMethod = new ProfilesTaskMethod(getSherlockActivity());
        mFetchPotentialRendezvousTaskMethod = new FetchPotentialRendezvousTaskMethod(getSherlockActivity());
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_people_inside, container, false);

        getActivity().setTitle(getResources().getString(R.string.potential_rendezvous));
		getSherlockActivity().getSupportActionBar().setSubtitle(getString(R.string.tonight));
		

		
		mImageOptions = new DisplayImageOptions.Builder()
        .cacheInMemory()
        .cacheOnDisc()
        //.displayer(new RoundedBitmapDisplayer(10))
        .build();
        
		
		// LISTENERS
        
		User u = User.getInstance();
        u.addPlaceListener(this);
        mProfilesTaskMethod.addProfilesTaskListener(this);
        mFetchPotentialRendezvousTaskMethod.addFetchPotentialRendezvousTaskListener(this);
        
        
        // LIST OR GRID
        
        switch (getActivity().getResources().getConfiguration().orientation ) 
        {
            case Configuration.ORIENTATION_LANDSCAPE:
                GridView gv = (GridView)view.findViewById(R.id.gridview);
                mGridAdapter = new PotentialRendezvousGridAdapter(this.getActivity(), 0, User.getInstance().getPotentialRendezvous());
                
                gv.setAdapter(mGridAdapter);
                gv.setOnItemClickListener(new OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> l, View v, int position, long id) {
                        PotentialRendezvous pm = User.getInstance().getPotentialRendezvous().get(position);
                        
                        mIndexPager = position;
                        
                        onClickRendezvous(pm);
                    }}
                );
                break;
                
            default:
                ListView lv = (ListView)view.findViewById(R.id.listview);
                mListAdapter = new PotentialRendezvousListAdapter(this.getActivity(), R.layout.row_person, User.getInstance().getPotentialRendezvous());
                
                lv.setAdapter(mListAdapter);
                lv.setOnItemClickListener(new OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> l, View v, int position, long id) 
                    {
                        PotentialRendezvous pm = User.getInstance().getPotentialRendezvous().get(position);
                        
                        mIndexPager = position;
                        
                        onClickRendezvous(pm);
                    }}
                );
                
                break;
        }  
		        
        
        

        setHasOptionsMenu(true);
        
		return view;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
	    // TODO Add your menu entries here
	    super.onCreateOptionsMenu(menu, inflater);
	    
	    menu.clear();

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
	            fetchPotentialRendezvous();
	            return true;
	            
        }
        
        return false;
    }
    
	
    @Override
    public void onResume() {
        super.onResume();

        fetchPotentialRendezvous();
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
        mProfilesTaskMethod.removeProfilesTaskListener(this);
        mFetchPotentialRendezvousTaskMethod.removeFetchPotentialRendezvousTaskListener(this);

        mProfilesTaskMethod.cleanUp();
        mFetchPotentialRendezvousTaskMethod.cleanUp();
    }
	
    @Override
    public void onDestroy()
    {
    	super.onDestroy();
    }
    
    
    private void fetchPotentialRendezvous()
    {
        mFetchPotentialRendezvousTaskMethod.doTask(User.getInstance().getUserId());
    }
    

    
    public void updateList()
    {
        // Update!
        mListAdapter.notifyDataSetChanged();
    }
    
    public void updateGrid()
    {
        // Update!
        mGridAdapter.notifyDataSetChanged();
    }

	public void updateView()
	{
	    
	    RelativeLayout root = (RelativeLayout)getView().findViewById(R.id.people_layout);
	    root.setVisibility(View.VISIBLE);
	    
		// VIEW
        
	    if( User.getInstance().getPotentialRendezvous().size() != 0 )
	    {
            
            // List | Grid
            switch (getActivity().getResources().getConfiguration().orientation ) 
            {
                case Configuration.ORIENTATION_LANDSCAPE:
                    updateGrid();
                    break;
                    
                default:
                    updateList();
                    break;
            } 
	    }

	}


    private class PotentialRendezvousListAdapter extends ArrayAdapter<PotentialRendezvous> {

	    public PotentialRendezvousListAdapter(Context context, int textViewResourceId, ArrayList<PotentialRendezvous> pms ) {
	            super(context, textViewResourceId, pms);
	    }

        @Override
        public int getCount(){
            return User.getInstance().getPotentialRendezvous().size();
        }


	    @Override
	    public View getView(int position, View convertView, ViewGroup parent) 
	    {
            View v = convertView;
            
            if( v == null )
            {
                LayoutInflater vi = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.row_person, null);
            }
            

            /**** PEOPLE ****/

            Person p = User.getInstance().getPotentialRendezvous().get(position).getCandidate();
       
            
            
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
	    


	}

	private class PotentialRendezvousGridAdapter extends ArrayAdapter<PotentialRendezvous> {

        private Context mContext;
        
        public PotentialRendezvousGridAdapter(Context context, int textViewResourceId, ArrayList<PotentialRendezvous> pms ) {
                super(context, 0, pms);
                this.mContext = context;
        }

        @Override
        public int getCount(){
            return User.getInstance().getPotentialRendezvous().size();
        }


        @Override
	    public View getView(int position, View convertView, ViewGroup parent) 
	    {
	        View v = convertView;
         
            DisplayMetrics metrics = new DisplayMetrics();
            getSherlockActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams((int)(metrics.widthPixels/6), (int)(metrics.widthPixels/6));
            
            
            if( v == null )
            {
                LayoutInflater vi = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.box_person, null);
            }
            
            
            /**** PEOPLE ****/
            
            Person p = User.getInstance().getPotentialRendezvous().get(position).getCandidate();
            
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
	
   
	private void onClickRendezvous( final PotentialRendezvous rendezvous )
    {
	    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        
        // set title
        alertDialogBuilder.setTitle(getString(R.string.meet) + " " + rendezvous.getCandidate().getUsername() + " " + getString(R.string.tonight));

        // set dialog message
        alertDialogBuilder
            .setCancelable(true)
            .setPositiveButton(getActivity().getString(R.string.plan_rendezvous),new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int id) {

                    SherlockFragment frag;

                    if( rendezvous.firstAnswer() )
                        frag = PotentialRendezvousPlanFragment.newInstance(rendezvous);
                    else
                        frag = PotentialRendezvousConfirmFragment.newInstance(rendezvous);

                    getSherlockActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content_frame, frag)
                    .addToBackStack(null)
                    .commit();
                }
              })
            .setNegativeButton(getActivity().getString(R.string.view_profile),new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int id) {
                    ArrayList<Long> peopleIds = new ArrayList<Long>();

                    for( PotentialRendezvous pm : User.getInstance().getPotentialRendezvous() )
                    {
                        peopleIds.add(pm.getCandidate().getUserId());
                    }

                    mProfilesTaskMethod.doTask(User.getInstance().getUserId(), peopleIds);
                }
            });

            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();

            // show it
            alertDialog.show();
            
            

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
    public void fetchPotentialRendezvousTaskEventReceived(FetchPotentialRendezvousTaskEvent evt)
    {
        FetchPotentialRendezvousTaskMethod met = (FetchPotentialRendezvousTaskMethod)evt.getSource();

        if( met != null && met.success() )
        {
            // POTENTIAL RENDEZVOUS
            User.getInstance().setPotentialRendezvous(met.getPotentialRendezvous());

            updateView();
        }
    }
}
