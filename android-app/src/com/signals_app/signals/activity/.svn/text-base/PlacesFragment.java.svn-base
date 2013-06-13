package com.signals_app.signals.activity;

import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.SearchView;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdate; 
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import com.signals_app.signals.tasks.PlaceCheckoutTaskMethod;
import com.signals_app.signals.tasks.PlacesTaskMethod;
import com.signals_app.signals.tasks.PlacesTaskMethod.PlacesTaskEvent;
import com.signals_app.signals.util.Utility;
import com.signals_app.signals.activity.SignalsApplication.PlacesViewState;
import com.signals_app.signals.model.*;
import com.signals_app.signals.model.Profile.GpsPosition;
import com.signals_app.signals.model.Profile.Person;
import com.signals_app.signals.model.Profile.User;
import com.signals_app.signals.model.Profile.User.GpsPositionEvent;
import com.signals_app.signals.model.Profile.User.GpsPositionListener;
import com.signals_app.signals.R;
import com.signals_app.signals.R.id;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.widget.ImageButton;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;

import com.google.android.gms.maps.GoogleMap;


public class PlacesFragment extends SherlockListFragment implements PlacesTaskMethod.PlacesTaskListener, GpsPositionListener
{
    public static final int PICK_PLACE_ON_MAP_REQUEST = 1;  // The request code
    
    
    public static final int ACTION_REFRESH = 2;
    public static final int ACTION_MAP = 1;
    public static final int ACTION_SEARCH = 3;
    
    private EditText mSearchEt;
    private TextWatcher filterTextWatcher = new TextWatcher() {
        public void afterTextChanged(Editable s) {
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // your search logic here
        }

    };
    
	private ArrayList<Place> mPlaces = null;
	private ArrayList<Place> mPlacesOriginal = null;
    private PlacesAdapter mAdapter;
	 
	private PlacesTaskMethod mPlacesTaskMethod;
	
	private LinearLayout mClubsBtnLayout;
	private LinearLayout mLoungesBtnLayout;
	private LinearLayout mBarsBtnLayout;
	private LinearLayout mPeopleBtnLayout;
	
	
  	public static final PlacesFragment newInstance()
  	{
  		PlacesFragment f = new PlacesFragment();
  	    return new PlacesFragment();
  	}
  	
  	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        mPlacesTaskMethod = new PlacesTaskMethod(getSherlockActivity());
	}

	@Override 
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		View view = inflater.inflate(R.layout.fragment_places, container, false);
		
		getSherlockActivity().setTitle(getString(R.string.bars));
		getSherlockActivity().getSupportActionBar().setSubtitle(null);
		
		
		// LISTENERS 

        mPlacesTaskMethod.addPlacesTaskListener(this);
        User.getInstance().addGpsPositionListener(this);
		
        
		// LIST
		
		
		mPlacesOriginal = new ArrayList<Place>();
		mPlaces = new ArrayList<Place>();
		
		mAdapter = new PlacesAdapter(this.getActivity(), R.layout.row_place, mPlaces);
		setListAdapter(this.mAdapter);
		
		 
		
		// BUTTONS
		
		mClubsBtnLayout = (LinearLayout)view.findViewById(R.id.clubs_btn_layout);
		mLoungesBtnLayout = (LinearLayout)view.findViewById(R.id.lounges_btn_layout);
		mBarsBtnLayout = (LinearLayout)view.findViewById(R.id.bars_btn_layout);
		mPeopleBtnLayout = (LinearLayout)view.findViewById(R.id.people_btn_layout);
		ImageButton clubsButton = (ImageButton)view.findViewById(R.id.clubs_btn);
		ImageButton loungesButton = (ImageButton)view.findViewById(R.id.lounges_btn);
		ImageButton barsButton = (ImageButton)view.findViewById(R.id.bars_btn);
		ImageButton peopleButton = (ImageButton)view.findViewById(R.id.people_btn);
        
		PlacesViewState pvs = SignalsApplication.getApplication(getSherlockActivity()).getPlacesViewState();
		
		if( pvs.clubsBtnClicked() )
            mClubsBtnLayout.setBackground(getResources().getDrawable(R.drawable.border_white_blue_back));
        else
            mClubsBtnLayout.setBackground(getResources().getDrawable(R.drawable.border_white));
		
		if( pvs.loungesBtnClicked() )
		    mLoungesBtnLayout.setBackground(getResources().getDrawable(R.drawable.border_white_blue_back));
        else
            mLoungesBtnLayout.setBackground(getResources().getDrawable(R.drawable.border_white));
		
		if( pvs.barsBtnClicked() )
		    mBarsBtnLayout.setBackground(getResources().getDrawable(R.drawable.border_white_blue_back));
        else
            mBarsBtnLayout.setBackground(getResources().getDrawable(R.drawable.border_white));
		
		if( pvs.peopleBtnClicked() )
		    mPeopleBtnLayout.setBackground(getResources().getDrawable(R.drawable.border_white_blue_back));
        else
            mPeopleBtnLayout.setBackground(getResources().getDrawable(R.drawable.border_white));

		clubsButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	onClickClubs();
        	}
        });
		
		loungesButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
        		onClickLounges();
            }
        });
		
		barsButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
        		onClickBars();
            }
        });
		
		peopleButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
        		onClickPeople();
            }
        });
        
		setHasOptionsMenu(true);
		
		return view; 
	}
	
	@Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        setUserVisibleHint(true);
    }
	
    @Override
    public void onResume() {
        super.onResume();
		
        fetchPlaces();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        
        // Remove listeners
        User.getInstance().removeGpsPositionListener(this);
        mPlacesTaskMethod.removePlacesTaskListener(this);
        
        mPlacesTaskMethod.cleanUp();
    } 
	
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if (requestCode == PICK_PLACE_ON_MAP_REQUEST) {
            // Make sure the request was successful
            if (resultCode == MainActivity.RESULT_OK) {
                // The user picked a place
                
                Place returnedPlace = data.getParcelableExtra("place");
                
                if( returnedPlace != null )
                {
                    PlaceFragment frag =  PlaceFragment.newInstance( returnedPlace );
                    
                    getSherlockActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content_frame, frag, "PlaceFragment")
                    .addToBackStack(null)
                    .commit();
                }
            }
        }

    }
    
    
    @Override
    public void onCreateOptionsMenu(final Menu menu, MenuInflater inflater)
    {
        menu.clear();
        getSherlockActivity().getSupportMenuInflater().inflate(R.menu.places_menu, menu);
        
        SearchManager searchManager = (SearchManager) getSherlockActivity().getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (SearchView) menu.findItem(R.id.item_menu_search).getActionView();
        
        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() 
        {
            public boolean onQueryTextChange(String newText) 
            {
                // this is your adapter that will be filtered
                mAdapter.getFilter().filter(newText);
                return true;
            }

            public boolean onQueryTextSubmit(String query) 
            {
                // this is your adapter that will be filtered
                mAdapter.getFilter().filter(query);
                return true;
            }
        };
        
        if (null != searchView )
        {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getSherlockActivity().getComponentName()));
            searchView.setIconifiedByDefault(false);  
            searchView.setOnQueryTextListener(queryTextListener);
            
            // CLOSE SEARCH WHEN SOFT KEYBOARD DISSMISSES
            searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean queryTextFocused) {
                    if(!queryTextFocused) {
                        menu.findItem(R.id.item_menu_search).collapseActionView();
                        searchView.setQuery("", false);
                    }
                }
            });
        }

        super.onCreateOptionsMenu(menu, inflater);
    }
    
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
            case R.id.item_menu_refresh:
                fetchPlaces();
                return true;
                
            case R.id.item_menu_map:
                Intent intent = new Intent(getSherlockActivity(), MapActivity.class);  
                Bundle bundle = new Bundle();    
                bundle.putParcelableArrayList("bars", mPlaces); 
                intent.putExtras(bundle);
                startActivityForResult(intent, PICK_PLACE_ON_MAP_REQUEST);
                
                return true;
        }
        
        return false;
    }
    
    
	private void fetchPlaces()
	{
        User u = User.getInstance();
        mPlacesTaskMethod.doTask( u.getUserId(), u.getCity().getId() );
	}
	
	private void filterPlaces()
	{
		PlacesViewState pvs = SignalsApplication.getApplication(getSherlockActivity()).getPlacesViewState();
		
		mPlaces.clear();
		
		ArrayList<Place> clubs = new ArrayList<Place>();
		ArrayList<Place> lounges = new ArrayList<Place>();
		ArrayList<Place> bars = new ArrayList<Place>();
		
		Comparator<Place> comparator;
		
		if( !pvs.peopleBtnClicked() )
			comparator = new Place.DistanceComparator();
		else
			comparator = new Place.NumberUsersComparator();
		
		for (Place p : mPlacesOriginal) {
			if( p.getGenre() == Place.CLUB)
				clubs.add(p);	
			else if( p.getGenre() == Place.LOUNGE)
				lounges.add(p);	
			else
				bars.add(p);	
		}
		
		if( (!pvs.clubsBtnClicked() && !pvs.loungesBtnClicked() && !pvs.barsBtnClicked()) || (pvs.clubsBtnClicked() && pvs.loungesBtnClicked() && pvs.barsBtnClicked()) )
		{
			mPlaces.addAll(clubs);
			mPlaces.addAll(lounges);
			mPlaces.addAll(bars);
			Collections.sort(mPlaces, comparator);
		}
		else if( pvs.clubsBtnClicked() && !pvs.loungesBtnClicked() && !pvs.barsBtnClicked() )
		{
			Collections.sort(clubs, new Place.DistanceComparator());
			mPlaces.addAll(clubs);
			
			ArrayList<Place> combine = new ArrayList<Place>();
			combine.addAll(lounges);
			combine.addAll(bars);
			Collections.sort(combine, comparator);
			
			mPlaces.addAll(combine);
		}
		else if( !pvs.clubsBtnClicked() && pvs.loungesBtnClicked() && !pvs.barsBtnClicked() )
		{
			Collections.sort(lounges, comparator);
			mPlaces.addAll(lounges);
			
			ArrayList<Place> combine = new ArrayList<Place>();
			combine.addAll(clubs);
			combine.addAll(bars);
			Collections.sort(combine, comparator);
			
			mPlaces.addAll(combine);
		}			
		else if( !pvs.clubsBtnClicked() && !pvs.loungesBtnClicked() && pvs.barsBtnClicked() )
		{
			Collections.sort(bars, comparator);
			mPlaces.addAll(bars);
			
			ArrayList<Place> combine = new ArrayList<Place>();
			combine.addAll(clubs);
			combine.addAll(lounges);
			Collections.sort(combine, comparator);
			
			mPlaces.addAll(combine);
		}	
		else if( pvs.clubsBtnClicked() && pvs.loungesBtnClicked() && !pvs.barsBtnClicked() )
		{
			ArrayList<Place> combine = new ArrayList<Place>();
			combine.addAll(clubs);
			combine.addAll(lounges);
			Collections.sort(combine, comparator);
			mPlaces.addAll(combine);
			
			Collections.sort(bars, comparator);
			mPlaces.addAll(bars);
		}	
		else if( pvs.clubsBtnClicked() && !pvs.loungesBtnClicked() && pvs.barsBtnClicked() )
		{
			ArrayList<Place> combine = new ArrayList<Place>();
			combine.addAll(clubs);
			combine.addAll(bars);
			Collections.sort(combine, comparator);
			mPlaces.addAll(combine);
			
			Collections.sort(lounges, comparator);
			mPlaces.addAll(lounges);
		}			
		else if( !pvs.clubsBtnClicked() && pvs.loungesBtnClicked() && pvs.barsBtnClicked() )
		{
			ArrayList<Place> combine = new ArrayList<Place>();
			combine.addAll(lounges);
			combine.addAll(bars);
			Collections.sort(combine, comparator);
			mPlaces.addAll(combine);
			
			Collections.sort(clubs, comparator);
			mPlaces.addAll(clubs);
		}
		
		
		mAdapter.notifyDataSetChanged();

	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id)
	{
		super.onListItemClick(l, v, position, id);

		
        //create a new intent and specify that it's target is PlaceActivity...
		
		
		
        if( mPlaces.get(position).getPlaceId() != 0)
        {
        	Place place = mPlaces.get(position);
        	PlaceFragment frag =  PlaceFragment.newInstance(place);
			
			getSherlockActivity().getSupportFragmentManager()
			.beginTransaction()
			.replace(R.id.content_frame, frag, "PlaceFragment")
			.addToBackStack(null)
			.commit();
        }
	}
	
 
    
	
	private class PlacesAdapter extends ArrayAdapter<Place> {

	    private ArrayList<Place> mItems;
	    private ResultFilter mFilter;
	    public PlacesAdapter(Context context, int textViewResourceId, ArrayList<Place> mItems) {
	            super(context, textViewResourceId, mItems);
	            this.mItems = mItems;

        }

	    @Override
	    public View getView(int position, View convertView, ViewGroup parent) {
	            View v = convertView;
	            if (v == null) {
	                LayoutInflater vi = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	                v = vi.inflate(R.layout.row_place, null);
	            }
	            Place p = mItems.get(position); 
	            if (p != null) 
	            {
        			User u = User.getInstance();
	            	ImageView icon = (ImageView)v.findViewById(R.id.icon);
	            	
	            	switch(p.getGenre())
	            	{
	            		case Place.CLUB:
	            			icon.setImageResource(R.drawable.speaker);
	            			break;
	            		case Place.LOUNGE:
	            			icon.setImageResource(R.drawable.martini);
	            			break;
	            		case Place.BAR:
	            			icon.setImageResource(R.drawable.beer);
	            			break;
	            	}

	            	GpsPosition posUser = u.getGpsPosition();
	            	GpsPosition posPlace = p.getGpsPosition();
	            	
            		String address = p.getAddress();

                    if( posUser != null )
                    {
                        DecimalFormat twoDForm = new DecimalFormat("#.##");
                        address += " (" + twoDForm.format(Utility.getDistance(posUser.getLatitude(), posUser.getLongitude(), posPlace.getLatitude(), posPlace.getLongitude(), "k")) +"km)";
                    }
            		
                    TextView tt = (TextView) v.findViewById(R.id.top_text);
                    TextView bt = (TextView) v.findViewById(R.id.bottom_text);

                    
                    if (tt != null) {
                          tt.setText(p.getName());                            
                    }
                    if(bt != null){
                          bt.setText(address);
                    }
                    
                    
                    // NUMBER OF USERS
                    
                    LinearLayout inBetweenLayout = (LinearLayout)v.findViewById(R.id.in_between_layout);
                    LinearLayout maleLayout = (LinearLayout)v.findViewById(R.id.male_layout);
                    LinearLayout femaleLayout = (LinearLayout)v.findViewById(R.id.female_layout);
                    
                    TextView msgText = (TextView) v.findViewById(R.id.msg_text);
                    TextView femaleText = (TextView) v.findViewById(R.id.female_text);
                    TextView maleText = (TextView) v.findViewById(R.id.male_text);                   
                    
                    if(u.getInterestedIn() == Person.INTERESTED_IN_MEN)
                    {
                    	inBetweenLayout.setVisibility(View.GONE);
                    	maleLayout.setVisibility(View.VISIBLE);
                    	femaleLayout.setVisibility(View.GONE);
                    	
                        maleText.setText(Integer.toString(p.getNbUsersMale()));
                    }
                    else if(u.getInterestedIn() == Person.INTERESTED_IN_WOMEN)
                    {
                    	inBetweenLayout.setVisibility(View.GONE);
                    	femaleLayout.setVisibility(View.VISIBLE);
                    	maleLayout.setVisibility(View.GONE);
                    	
                    	femaleText.setText(Integer.toString(p.getNbUsersFemale()));
                    }
                    
                    else if(u.getInterestedIn() == Person.INTERESTED_IN_BOTH )
                    {
                    	inBetweenLayout.setVisibility(View.VISIBLE);
                    	femaleLayout.setVisibility(View.VISIBLE);
                    	maleLayout.setVisibility(View.VISIBLE);
                    	
                    	maleText.setText(Integer.toString(p.getNbUsersMale()));
                    	femaleText.setText(Integer.toString(p.getNbUsersFemale()));
                    }	                    
                    
                    
                    // MESSAGE
                    /*
                    if( position == 3 || position == 8 )
                    {
                    	msgText.setVisibility(View.VISIBLE);
                    	v.setAlpha(Float.valueOf("0.6"));
                    	
                    	femaleLayout.setVisibility(View.GONE);
                    	maleLayout.setVisibility(View.GONE);
                    }
                    */
                    	if( position == 2 || position == 13)
                    	{
                    		msgText.setVisibility(View.VISIBLE);
                    		msgText.setTextColor(getActivity().getResources().getColor(R.color.honeycombish_blue));
                    		msgText.setText("Opening Night");
                    	}
        	        
                    
	            }
	            
	            return v;
	    }
	    
	    @Override
	    public Filter getFilter() {
	        if (mFilter == null) {
	            mFilter = new ResultFilter();
	        }
	        return mFilter;
	    }
	    
	    @Override
	    public int getCount() {
	        return mItems.size();
	    }

	    @Override
	    public Place getItem(int position) {
	        return mItems.get(position);
	    }
	    
	    
	    private class ResultFilter extends Filter {

	        @Override
	        protected FilterResults performFiltering(CharSequence constraint) {
	            FilterResults results = new FilterResults();

	            String filterText = constraint.toString().toLowerCase();
	            if (filterText == null || filterText.length() == 0) {
	                synchronized (this) {
	                    results.values = mPlaces;
	                    results.count = mPlaces.size();
	                }
	            } else {
	                ArrayList<Place> placesResult = new ArrayList<Place>();
	                ArrayList<Place> placesClone = new ArrayList<Place>();
	                
	                synchronized (this) {
	                    placesClone.addAll(mPlaces);
	                }
	                for (int i = 0, l = placesClone.size(); i < l; i++) {
	                    Place p = placesClone.get(i);
	                    if (p.getName().toLowerCase().contains(filterText)) {
	                        placesResult.add(p);
	                    }
	                }
	                results.values = placesResult;
	                results.count = placesResult.size();

	            }

	            return results;
	        }

	        @SuppressWarnings("unchecked")
            @Override
	        protected void publishResults(CharSequence constraint,
	                FilterResults results) {

	            mItems = (ArrayList<Place>) results.values;
	            if(results.count > 0)
	            {
	                notifyDataSetChanged();
	            }else{
	                notifyDataSetInvalidated();
	            }


	        }

	    }
	    
	}

	
	
	
	// EVENTS
	
	private void onClickClubs()
	{
		PlacesViewState pvs = SignalsApplication.getApplication(getSherlockActivity()).getPlacesViewState();
		pvs.setClubsBtnClicked(!pvs.clubsBtnClicked());
		
		if( pvs.clubsBtnClicked() )
			mClubsBtnLayout.setBackground(getResources().getDrawable(R.drawable.border_white_blue_back));
		else
			mClubsBtnLayout.setBackground(getResources().getDrawable(R.drawable.border_white));
		
		filterPlaces();
	}

    private void onClickLounges()
	{
		PlacesViewState pvs = SignalsApplication.getApplication(getSherlockActivity()).getPlacesViewState();
		pvs.setLoungesBtnClicked(!pvs.loungesBtnClicked());
		
		if( pvs.loungesBtnClicked() )
			mLoungesBtnLayout.setBackground(getResources().getDrawable(R.drawable.border_white_blue_back));
		else
			mLoungesBtnLayout.setBackground(getResources().getDrawable(R.drawable.border_white));
		
		filterPlaces();
	}

    private void onClickBars()
	{
		PlacesViewState pvs = SignalsApplication.getApplication(getSherlockActivity()).getPlacesViewState();
		pvs.setBarsBtnClicked(!pvs.barsBtnClicked());
		
		if( pvs.barsBtnClicked() )
			mBarsBtnLayout.setBackground(getResources().getDrawable(R.drawable.border_white_blue_back));
		else
			mBarsBtnLayout.setBackground(getResources().getDrawable(R.drawable.border_white));
		
		filterPlaces();
	}

    private void onClickPeople()
	{
		PlacesViewState pvs = SignalsApplication.getApplication(getSherlockActivity()).getPlacesViewState();
		pvs.setPeopleBtnClicked(!pvs.peopleBtnClicked());
		
		if( pvs.peopleBtnClicked() )
			mPeopleBtnLayout.setBackground(getResources().getDrawable(R.drawable.border_white_blue_back));
		else
			mPeopleBtnLayout.setBackground(getResources().getDrawable(R.drawable.border_white));
		
		filterPlaces();
	}


	public void placesTaskEventReceived(PlacesTaskEvent evt)
	{		
		PlacesTaskMethod met = (PlacesTaskMethod)evt.getSource();
		
		if( met != null && met.success() )
		{
			mPlacesOriginal = met.getPlaces();
			filterPlaces();		
		}
		
	}


	@Override
	public void gpsPositionEventReceived(GpsPositionEvent evt) 
	{
        filterPlaces();

	}
	

}


