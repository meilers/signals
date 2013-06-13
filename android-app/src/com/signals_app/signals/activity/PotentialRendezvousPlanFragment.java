package com.signals_app.signals.activity;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.*;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.signals_app.signals.R;
import com.signals_app.signals.model.Place;
import com.signals_app.signals.model.Profile.GpsPosition;
import com.signals_app.signals.model.Profile.Person;
import com.signals_app.signals.model.Profile.User;
import com.signals_app.signals.model.Profile.User.GpsPositionEvent;
import com.signals_app.signals.model.Profile.User.GpsPositionListener;
import com.signals_app.signals.model.rendezvous.PotentialRendezvous;
import com.signals_app.signals.tasks.rendezvous.FetchPlacesRendezvousTaskMethod;
import com.signals_app.signals.tasks.rendezvous.FetchPlacesRendezvousTaskMethod.FetchPlacesRendezvousTaskListener;
import com.signals_app.signals.tasks.rendezvous.SuggestRendezvousTaskMethod;
import com.signals_app.signals.tasks.rendezvous.SuggestRendezvousTaskMethod.SuggestRendezvousTaskEvent;
import com.signals_app.signals.tasks.rendezvous.SuggestRendezvousTaskMethod.SuggestRendezvousTaskListener;
import com.signals_app.signals.util.Utility;

public class PotentialRendezvousPlanFragment extends SherlockFragment implements GpsPositionListener, SuggestRendezvousTaskListener, FetchPlacesRendezvousTaskListener
{
    public static final int ACTION_REFRESH = 1;


    private PotentialRendezvous mPotentialRendezvous;

    private ArrayList<Place> mPlaces;
    private ArrayList<Boolean> mTimes;
    private ArrayList<Long> mPlacesNotWantedIds;

    private ArrayList<Boolean> mPlacesSelected;
    private ArrayList<Boolean> mTimesSelected;    


    private FetchPlacesRendezvousTaskMethod mFetchPlacesRendezvousTaskMethod;
    private SuggestRendezvousTaskMethod mSuggestRendezvousTaskMethod;
    
    
    // WIDGETS
    private ArrayList<RelativeLayout> mPlaceViews;
    private ArrayList<View> mTimeViews;
    
    
    private LinearLayout mTimesLayout;
    
    
    public static final PotentialRendezvousPlanFragment newInstance(PotentialRendezvous pr)
    {
        PotentialRendezvousPlanFragment f = new PotentialRendezvousPlanFragment();
        
        // Get arguments passed in, if any
        Bundle args = f.getArguments();
        if (args == null) {
            args = new Bundle();
        }
        
        ArrayList<Boolean> placesSelected = new ArrayList<Boolean>(PotentialRendezvous.NB_CHOICES);
        ArrayList<Boolean> timesSelected = new ArrayList<Boolean>(PotentialRendezvous.NB_CHOICES);

        ArrayList<Place> places = new ArrayList<Place>();
        ArrayList<Boolean> times = new ArrayList<Boolean>();
        ArrayList<Long> placesNotWantedIds = new ArrayList<Long>();

        for(int i=0;i<PotentialRendezvous.NB_CHOICES;++i)
        {
            times.add(true);

            placesSelected.add(false);
            timesSelected.add(false);
        }
        
        // Add parameters to the argument bundle
        args.putParcelable("potentialRendezvous", pr);
        args.putSerializable("placesSelected", placesSelected);
        args.putSerializable("timesSelected", timesSelected);

        args.putSerializable("places", places);
        args.putSerializable("times", times);
        args.putSerializable("placesNotWantedIds", placesNotWantedIds);


        f.setArguments(args);
        
        return f;
    }
    
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
    
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        
        if( savedInstanceState != null )
        {
            args = savedInstanceState;
        }
        
        mPlacesSelected = (ArrayList<Boolean>)args.getSerializable("placesSelected");
        mTimesSelected = (ArrayList<Boolean>)args.getSerializable("timesSelected");

        mPlaces = (ArrayList<Place>)args.getSerializable("places");
        mTimes = (ArrayList<Boolean>)args.getSerializable("times");
        mPlacesNotWantedIds = (ArrayList<Long>)args.getSerializable("placesNotWantedIds");
        mPotentialRendezvous = args.getParcelable("potentialRendezvous");
        
        mSuggestRendezvousTaskMethod = new SuggestRendezvousTaskMethod(getSherlockActivity());
        mFetchPlacesRendezvousTaskMethod = new FetchPlacesRendezvousTaskMethod(getSherlockActivity());
    }
 
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
        View view = inflater.inflate(R.layout.fragment_potential_rendezvous_plan, container, false);
        getSherlockActivity().setTitle(getResources().getString(R.string.plan_rendezvous));
        getSherlockActivity().getSupportActionBar().setSubtitle(getString(R.string.tonight_with) + " " + mPotentialRendezvous.getCandidate().getUsername());
        
        mTimesLayout = (LinearLayout)view.findViewById(R.id.times_layout);
        mPlaceViews = new ArrayList<RelativeLayout>();
        mTimeViews = new ArrayList<View>();
        
        View timeLayout = inflater.inflate(R.layout.layout_time, null);
        RelativeLayout placeLayout = null;
        CheckBox cb = null;      
        
        for( int i=0; i<PotentialRendezvous.NB_CHOICES; ++i)
        {
            
            switch(i)
            {
                case 0:
                    placeLayout = (RelativeLayout)view.findViewById(R.id.place0_row);
                    break;
                    
                case 1:
                    placeLayout = (RelativeLayout)view.findViewById(R.id.place1_row);
                    break;
                    
                case 2:
                    placeLayout = (RelativeLayout)view.findViewById(R.id.place2_row);
                    break;
                    
                case 3:
                    placeLayout = (RelativeLayout)view.findViewById(R.id.place3_row);
                    break;
                    
                case 4:
                    placeLayout = (RelativeLayout)view.findViewById(R.id.place4_row);
                    break;
                    
                case 5:
                    placeLayout = (RelativeLayout)view.findViewById(R.id.place5_row);
                    break;
                    
                case 6:
                    placeLayout = (RelativeLayout)view.findViewById(R.id.place6_row);
                    break;
                    
                case 7:
                    placeLayout = (RelativeLayout)view.findViewById(R.id.place7_row);
                    break;
                    
                case 8:
                    placeLayout = (RelativeLayout)view.findViewById(R.id.place8_row);
                    break;
                    
                case 9:
                    placeLayout = (RelativeLayout)view.findViewById(R.id.place9_row);
                    break;
            }      
            
            cb = (CheckBox)placeLayout.findViewById(R.id.place_cb);
            cb.setVisibility(View.VISIBLE);
            cb.setTag(i);
            cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    mPlacesSelected.set(((Integer)buttonView.getTag()).intValue(), isChecked);
                }
            });
            
            mPlaceViews.add(placeLayout);
            
            
            // TIMES
            timeLayout = inflater.inflate(R.layout.layout_time, null);
            cb = (CheckBox)timeLayout.findViewById(R.id.time_cb);
            cb.setVisibility(View.VISIBLE);
            cb.setTag(i);
            cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    mTimesSelected.set(((Integer)buttonView.getTag()).intValue(), isChecked);
                }
            });
            
            TextView timeTv = (TextView)timeLayout.findViewById(R.id.time_tv);
            
            String timeTxt = "";
            
            if( i+5 < 12 )
                timeTxt += i+5+12 + ":00";
            else if( i+5 == 12)
                timeTxt = getString(R.string.midnight);
            else
                timeTxt += i-7 + ":00";
            
            timeTv.setText(timeTxt);
            mTimeViews.add(timeLayout);
            

        }
        
        Button doneBtn = (Button)view.findViewById(R.id.done_btn);
        doneBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickDone();
            }
        });
        doneBtn.setText(getString(R.string.send_to) + " " + mPotentialRendezvous.getCandidate().getUsername());

        // LISTENERS
        User.getInstance().addGpsPositionListener(this);
        mSuggestRendezvousTaskMethod.addSuggestRendezvousTaskListener(this);
        mFetchPlacesRendezvousTaskMethod.addFetchPlacesRendezvousTaskListener(this);

        setHasOptionsMenu(true);
        fetchPlacesRendezvous();


        return view; 
    }


    
    @Override
    public void onResume()
    {
        super.onResume();
    }
    
    
    
    // Fragment no longer visible
    @Override
    public void onPause()
    {
        super.onPause();
    }
    
    
    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        
        // LISTENERS
        User.getInstance().removeGpsPositionListener(this);
        mSuggestRendezvousTaskMethod.removeSuggestRendezvousTaskListener(this);
        mFetchPlacesRendezvousTaskMethod.removeFetchPlacesRendezvousTaskListener(this);
        mSuggestRendezvousTaskMethod.cleanUp();
        mFetchPlacesRendezvousTaskMethod.cleanUp();
    } 

    
    @Override
    public void onSaveInstanceState(Bundle outState) 
    {
        if ( mPlacesSelected != null && mTimesSelected != null && mPlaces != null && mTimes != null && mPlacesNotWantedIds != null && mPotentialRendezvous != null)
        {
            super.onSaveInstanceState(outState);
            
            outState.putSerializable("placesSelected", mPlacesSelected);
            outState.putSerializable("timesSelected", mTimesSelected);
            outState.putSerializable("places", mPlaces);
            outState.putSerializable("times", mTimes);
            outState.putSerializable("placesNotWantedIds", mPlacesNotWantedIds);
            outState.putParcelable("potentialRendezvous", mPotentialRendezvous);
        }
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
                fetchPlacesRendezvous();
                return true;

        }

        return false;
    }


    private void fetchPlacesRendezvous()
    {
        for( Place place : mPlaces )
            mPlacesNotWantedIds.add(place.getPlaceId());

        mFetchPlacesRendezvousTaskMethod.doTask(mPotentialRendezvous,mPlacesNotWantedIds);
    }
    
    private void updateView( View view )
    {
        ScrollView sv = (ScrollView)view.findViewById(R.id.scroll_view);
        sv.setVisibility(View.VISIBLE);

        // PLACES
        Place p = null;
        
        for( int i=0; i<PotentialRendezvous.NB_CHOICES; ++i)
        {
            if( i<mPlaces.size() )
            {
                p = mPlaces.get(i);

                mPlaceViews.get(i).setVisibility(View.VISIBLE);
                buildPlaceLayout(mPlaceViews.get(i), p, mPlacesSelected.get(i));
            }
            else
                mPlaceViews.get(i).setVisibility(View.GONE);
        }
        
        
        // TIMES
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        
        if( hour >= 17 )
        {
            for( int i=0;i <= hour-17; ++i)
                mTimes.set(i, false);
        }
        else if( hour < 3 )
        {
            for( int i=0;i <= hour+7; ++i)
                mTimes.set(i, false);
        }
        
        // Check if too late
        Calendar createTime = mPotentialRendezvous.getCreateTime();
        Calendar now = Calendar.getInstance();

        populateText(mTimesLayout, mTimeViews, getSherlockActivity().getApplicationContext() );
    }
    
    private void buildPlaceLayout(RelativeLayout placeLayout, final Place p, boolean selected )
    {
        RelativeLayout placeInfoLayout = (RelativeLayout)placeLayout.findViewById(R.id.place_info_layout);
        ImageView icon = (ImageView)placeInfoLayout.findViewById(R.id.icon);
        TextView tt = (TextView) placeInfoLayout.findViewById(R.id.top_text);
        TextView bt = (TextView) placeInfoLayout.findViewById(R.id.bottom_text);
        
        CheckBox cb = (CheckBox)placeInfoLayout.findViewById(R.id.place_cb);
        
        GpsPosition posUser = User.getInstance().getGpsPosition();
        GpsPosition posPlace = p.getGpsPosition();
        String address = new String(p.getAddress());
        DecimalFormat twoDForm = new DecimalFormat("#.##");
        
        placeLayout.setVisibility(View.VISIBLE);
        placeLayout.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                onClickPlace(p);
                return false;
            }
        });
        
        icon = (ImageView)placeInfoLayout.findViewById(R.id.icon);
        
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
        };
    
        if( posUser != null )
            address += " (" + twoDForm.format(Utility.getDistance(posUser.getLatitude(), posUser.getLongitude(), posPlace.getLatitude(), posPlace.getLongitude(), "k")) +"km)";
        
        tt = (TextView) placeInfoLayout.findViewById(R.id.top_text);
        bt = (TextView) placeInfoLayout.findViewById(R.id.bottom_text);
        
        if (tt != null) {
              tt.setText(p.getName());                            
        }
        if(bt != null){
              bt.setText(address);
        }
        
        cb.setChecked(selected);
    }
    
    
    private void populateText(LinearLayout ll, ArrayList<View> timeViews, Context c)
    { 
        DisplayMetrics metrics = new DisplayMetrics();
        getSherlockActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int maxWidth =  metrics.widthPixels - 2*(int)Utility.convertDpToPixel((int)getResources().getDimension(R.dimen.padding_large),c);
        
        for(int i=0; i<((ViewGroup)ll).getChildCount(); ++i) {
            LinearLayout nextChild = (LinearLayout)((ViewGroup)ll).getChildAt(i);
            nextChild.removeAllViews();
        }
        ll.removeAllViews(); 

        LinearLayout newLL = new LinearLayout(c);
        newLL.setGravity(Gravity.LEFT);
        newLL.setOrientation(LinearLayout.HORIZONTAL);

        int padding = (int)Utility.convertDpToPixel(4 ,c);
        int widthSoFar = 0;
        
        for( int i=0; i < timeViews.size(); ++i )
        { 
            if( mTimes.get(i) )
            {
                View v = timeViews.get(i);
                CheckBox cb = (CheckBox)v.findViewById(R.id.time_cb);
                cb.setChecked(mTimesSelected.get(i));
                
                
                v.setPadding(0, 0, 0, 0);
                v.measure(0, 0);
                
                widthSoFar += v.getMeasuredWidth();// YOU MAY NEED TO ADD THE MARGINS
                if (widthSoFar >= maxWidth) {
                    ll.addView(newLL);
    
                    newLL = new LinearLayout(c);
                    newLL.setOrientation(LinearLayout.HORIZONTAL);
                    newLL.setGravity(Gravity.LEFT);
                    
                    v.setPadding(0, padding, 4*padding, padding);
                    //params = new LinearLayout.LayoutParams(v.getMeasuredWidth()+2*padding, v.getMeasuredHeight()+2*padding);
                    newLL.addView(v);
                    widthSoFar = v.getMeasuredWidth();
                } else 
                {
                    
                    if( widthSoFar + 4*padding < maxWidth )
                        v.setPadding(0, padding, 4*padding, padding);
                    else
                        v.setPadding(0, padding, 0, padding);
                    
                    newLL.addView(v);
                }

            }
        }
        ll.addView(newLL);


        // Check if after 2 AM
        int index = mTimes.indexOf(true);

        if( index == -1 )
        {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
            Person candidate = mPotentialRendezvous.getCandidate();

            alertDialogBuilder.setTitle(getString(R.string.rendezvous_cancelled));

            alertDialogBuilder
                    .setMessage(getString(R.string.its_after_two_am))
                    .setCancelable(false)
                    .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            getActivity().getSupportFragmentManager().popBackStack();
                        }
                    });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }
    
    
    // EVENTS
    private void onClickPlace( Place p )
    {
        PlaceFragment frag =  PlaceFragment.newInstance(p);
        
        getSherlockActivity().getSupportFragmentManager()
        .beginTransaction()
        .replace(R.id.content_frame, frag, "PlaceFragment")
        .addToBackStack(null)
        .commit();
    }

    private void onClickDone()
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        int index = mPlacesSelected.indexOf(true);

        if( index == -1 )
        {
            Toast.makeText(getSherlockActivity(), getString(R.string.please_choose_at_least_one_place_for_a_rendezvous), Toast.LENGTH_LONG).show();
            return;
        }

        index = mTimesSelected.indexOf(true);

        if( index == -1 )
        {
            Toast.makeText(getSherlockActivity(), getString(R.string.please_choose_at_least_one_time_for_a_rendezvous), Toast.LENGTH_LONG ).show();
            return;
        }


        ArrayList<Long> placesIds = new ArrayList<Long>();

        for( int i = 0; i < mPlaces.size(); ++i )
        {
            if( mPlacesSelected.get(i) )
                placesIds.add(mPlaces.get(i).getPlaceId());
        }


        mSuggestRendezvousTaskMethod.doTask(User.getInstance().getUserId(), mPotentialRendezvous.getId(), placesIds, mTimesSelected);
    }
    
    @Override
    public void gpsPositionEventReceived(GpsPositionEvent evt) {
        
        if( getView() != null )
            updateView(getView());
    }


    @Override
    public void suggestRendezvousTaskEventReceived(SuggestRendezvousTaskEvent evt) {
        SuggestRendezvousTaskMethod met = (SuggestRendezvousTaskMethod)evt.getSource();
        
        if( met != null && met.success() )
        {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
            Person candidate = mPotentialRendezvous.getCandidate();

            alertDialogBuilder.setTitle(getString(R.string.suggestions_sent));
     
            alertDialogBuilder
                .setMessage(getString(R.string.once) + " " + candidate.getUsername() + " " + getString(R.string.picks_out_a_time_and_place))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        getActivity().getSupportFragmentManager().popBackStack();
                    }
                });
     
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();    
        }
    }

    @Override
    public void fetchPlacesRendezvousTaskEventReceived(FetchPlacesRendezvousTaskMethod.FetchPlacesRendezvousTaskEvent evt) {
        FetchPlacesRendezvousTaskMethod met = (FetchPlacesRendezvousTaskMethod)evt.getSource();

        if( met != null && met.success() )
        {
            mPlaces = met.getPlaces();

            for(int i=0;i<PotentialRendezvous.NB_CHOICES;++i)
                mPlacesSelected.set(i,false);

            updateView(getView());
        }
        else
        {
            mPlacesNotWantedIds.clear();
        }
    }
}
