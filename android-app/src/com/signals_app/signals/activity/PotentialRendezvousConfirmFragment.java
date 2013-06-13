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
import com.signals_app.signals.R;
import com.signals_app.signals.model.Place;
import com.signals_app.signals.model.Profile.GpsPosition;
import com.signals_app.signals.model.Profile.Person;
import com.signals_app.signals.model.Profile.User;
import com.signals_app.signals.model.Profile.User.GpsPositionEvent;
import com.signals_app.signals.model.Profile.User.GpsPositionListener;
import com.signals_app.signals.model.rendezvous.ConfirmedRendezvous;
import com.signals_app.signals.model.rendezvous.PotentialRendezvous;
import com.signals_app.signals.tasks.rendezvous.ConfirmRendezvousTaskMethod;
import com.signals_app.signals.tasks.rendezvous.ConfirmRendezvousTaskMethod.ConfirmRendezvousTaskEvent;
import com.signals_app.signals.tasks.rendezvous.ConfirmRendezvousTaskMethod.ConfirmRendezvousTaskListener;
import com.signals_app.signals.util.Utility;

public class PotentialRendezvousConfirmFragment extends SherlockFragment implements GpsPositionListener, ConfirmRendezvousTaskListener
{
    static final int NB_CHOICES = 10;

    private PotentialRendezvous mPotentialRendezvous;
    private ArrayList<Boolean> mPlacesSelected;
    private ArrayList<Boolean> mTimesSelected;

    private ConfirmRendezvousTaskMethod mConfirmRendezvousTaskMethod;



    // WIDGETS
    private ArrayList<RelativeLayout> mPlaceViews;
    private ArrayList<View> mTimeViews;


    private RadioGroup mTimesLayout;


    public static final PotentialRendezvousConfirmFragment newInstance(PotentialRendezvous pr)
    {
        PotentialRendezvousConfirmFragment f = new PotentialRendezvousConfirmFragment();

        // Get arguments passed in, if any
        Bundle args = f.getArguments();
        if (args == null) {
            args = new Bundle();
        }

        ArrayList<Boolean> placesSelected = new ArrayList<Boolean>(NB_CHOICES);
        ArrayList<Boolean> timesSelected = new ArrayList<Boolean>(NB_CHOICES);

        for(int i=0;i<NB_CHOICES;++i)
        {
            placesSelected.add(false);
            timesSelected.add(false);
        }

        // Add parameters to the argument bundle
        args.putParcelable("potentialRendezvous", pr);
        args.putSerializable("placesSelected", placesSelected);
        args.putSerializable("timesSelected", timesSelected);
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
        mPotentialRendezvous = args.getParcelable("potentialRendezvous");

        mConfirmRendezvousTaskMethod = new ConfirmRendezvousTaskMethod(getSherlockActivity());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_potential_rendezvous_confirm, container, false);
        getSherlockActivity().setTitle(getResources().getString(R.string.plan_rendezvous));
        getSherlockActivity().getSupportActionBar().setSubtitle(getString(R.string.tonight_with) + " " +  mPotentialRendezvous.getCandidate().getUsername());

        TextView bubbleTv = (TextView)view.findViewById(R.id.bubble_tv);
        bubbleTv.setText(mPotentialRendezvous.getCandidate().getUsername() + " " + getString(R.string.has_suggested_the_following_places_and_times_for_your_rendezvous));

        mTimesLayout = (RadioGroup)view.findViewById(R.id.times_layout);
        mPlaceViews = new ArrayList<RelativeLayout>();
        mTimeViews = new ArrayList<View>();

        LinearLayout timeLayout = (LinearLayout)inflater.inflate(R.layout.layout_time_rb, null);
        RelativeLayout placeLayout = null;
        RadioButton rb = null;

        for( int i=0; i<NB_CHOICES; ++i)
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

            rb = (RadioButton)placeLayout.findViewById(R.id.place_rb);
            rb.setVisibility(View.VISIBLE);
            rb.setTag(i);
            rb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if (isChecked) {
                        //set all buttons to false;
                        for(RelativeLayout placeLayout : mPlaceViews)
                        {
                            RadioButton rb = (RadioButton)placeLayout.findViewById(R.id.place_rb);
                            rb.setChecked(false);
                            mPlacesSelected.set((Integer)rb.getTag(), false);
                        }
                        //set new selected button to true;
                        buttonView.setChecked(true);
                        mPlacesSelected.set((Integer)buttonView.getTag(), true);
                    }
                }
            });

            mPlaceViews.add(placeLayout);


            // TIMES
            timeLayout = (LinearLayout)inflater.inflate(R.layout.layout_time_rb, null);
            rb = (RadioButton)timeLayout.findViewById(R.id.time_rb);
            rb.setVisibility(View.VISIBLE);
            rb.setTag(i);
            rb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if (isChecked) {
                        //set all buttons to false;
                        for(View viewLayout : mTimeViews)
                        {
                            RadioButton rb = (RadioButton)viewLayout.findViewById(R.id.time_rb);
                            rb.setChecked(false);
                            mTimesSelected.set((Integer)rb.getTag(), false);
                        }
                        //set new selected button to true;
                        buttonView.setChecked(true);
                        mTimesSelected.set((Integer)buttonView.getTag(), true);
                    }

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

        // LISTENERS
        User.getInstance().addGpsPositionListener(this);
        mConfirmRendezvousTaskMethod.addConfirmRendezvousTaskListener(this);


        return view;
    }



    @Override
    public void onResume()
    {
        super.onResume();

        updateView(getView());
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
        mConfirmRendezvousTaskMethod.removeConfirmRendezvousTaskListener(this);

        mConfirmRendezvousTaskMethod.cleanUp();
    }


    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        if ( mPlacesSelected != null && mTimesSelected != null && mPotentialRendezvous != null)
        {
            super.onSaveInstanceState(outState);

            outState.putSerializable("placesSelected", mPlacesSelected);
            outState.putSerializable("timesSelected", mTimesSelected);
            outState.putParcelable("potentialRendezvous", mPotentialRendezvous);
        }
    }





    private void updateView( View view )
    {
        // PLACES
        ArrayList<Place> places = mPotentialRendezvous.getPlaces();
        Place p = null;

        for( int i=0; i<places.size(); ++i)
        {
            p = places.get(i);
            buildPlaceLayout(mPlaceViews.get(i), p, mPlacesSelected.get(i));
        }


        // TIMES
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        ArrayList<Boolean> times = mPotentialRendezvous.getTimes();

        if( hour >= 17 )
        {
            for( int i=0;i <= hour-17; ++i)
                times.set(i, false);
        }
        else if( hour < 3 )
        {
            for( int i=0;i <= hour+7; ++i)
                times.set(i, false);
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

        RadioButton rb = (RadioButton)placeInfoLayout.findViewById(R.id.place_rb);

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

        rb.setChecked(selected);
    }


    private void populateText(RadioGroup ll, ArrayList<View> timeViews, Context c)
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

        int padding = (int)Utility.convertDpToPixel(4,c);
        int widthSoFar = 0;

        ArrayList<Boolean> times = mPotentialRendezvous.getTimes();

        for( int i=0; i < timeViews.size(); ++i )
        {
            if( times.get(i) )
            {
                View v = timeViews.get(i);
                RadioButton rb = (RadioButton)v.findViewById(R.id.time_rb);
                rb.setChecked(mTimesSelected.get(i));


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
        int index = times.indexOf(true);

        if( index == -1 )
        {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
            Person candidate = mPotentialRendezvous.getCandidate();

            alertDialogBuilder.setTitle(getString(R.string.rendezvous_cancelled));

            alertDialogBuilder
                    .setMessage(getString(R.string.the_rendezvous_time_has_passed))
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
        long placeId = 0;
        int timeSlot = 0;

        if( index == -1 )
        {
            Toast.makeText(getSherlockActivity(), getString(R.string.please_pick_a_place_for_a_rendezvous), Toast.LENGTH_LONG).show();
            return;
        }

        placeId = mPotentialRendezvous.getPlaces().get(index).getPlaceId();
        index = mTimesSelected.indexOf(true);

        if( index == -1 )
        {
            Toast.makeText(getSherlockActivity(), getString(R.string.please_pick_a_time_for_a_rendezvous), Toast.LENGTH_LONG ).show();
            return;
        }

        timeSlot = index;
        mConfirmRendezvousTaskMethod.doTask(User.getInstance().getUserId(), mPotentialRendezvous.getCandidate().getUserId(), mPotentialRendezvous.getId(), placeId, timeSlot);
    }

    @Override
    public void gpsPositionEventReceived(GpsPositionEvent evt) {

        if( getView() != null )
            updateView(getView());
    }


    @Override
    public void confirmRendezvousTaskEventReceived(ConfirmRendezvousTaskEvent evt) {
        ConfirmRendezvousTaskMethod met = (ConfirmRendezvousTaskMethod)evt.getSource();

        if( met != null && met.success() )
        {
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View view = inflater.inflate(R.layout.row_place_with_time, null);
            TextView tt = (TextView) view.findViewById(R.id.top_text);
            TextView bt = (TextView) view.findViewById(R.id.bottom_text);

            ConfirmedRendezvous rendezvous = met.getConfirmedRendezvous();
            Person person = rendezvous.getCandidate();
            final Place p = rendezvous.getPlace();
            GpsPosition posUser = User.getInstance().getGpsPosition();
            GpsPosition posPlace = p.getGpsPosition();
            String address = p.getAddress();
            DecimalFormat twoDForm = new DecimalFormat("#.##");



            // PLACE
            ImageView icon = (ImageView)view.findViewById(R.id.icon);

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

            tt = (TextView) view.findViewById(R.id.top_text);
            bt = (TextView) view.findViewById(R.id.bottom_text);

            if (tt != null) {
                tt.setText(p.getName());
            }
            if(bt != null){
                bt.setText(address);
            }


            // TIME
            String timeTxt = "";
            int timeSlot = rendezvous.getTimeSlot();
            TextView timeTv = (TextView) view.findViewById(R.id.time_tv);

            if( timeSlot+5 < 12 )
                timeTxt += timeSlot+5+12 + ":00";
            else if( timeSlot+5 == 12)
                timeTxt = getString(R.string.midnight);
            else
                timeTxt += timeSlot-7 + ":00";

            timeTv.setText(timeTxt);

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
            alertDialogBuilder.setTitle(getString(R.string.rendezvous_confirmed));

            alertDialogBuilder
                    .setView(view)
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
}
