package com.signals_app.signals.activity;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

import android.graphics.Typeface;
import android.text.InputFilter;
import android.view.inputmethod.EditorInfo;
import android.widget.*;
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
import com.signals_app.signals.model.rendezvous.ConfirmedRendezvous;
import com.signals_app.signals.model.rendezvous.ConfirmedRendezvous;
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
import com.signals_app.signals.tasks.rendezvous.CancelRendezvousTaskMethod;
import com.signals_app.signals.tasks.rendezvous.CancelRendezvousTaskMethod.CancelRendezvousTaskListener;
import com.signals_app.signals.tasks.rendezvous.FetchConfirmedRendezvousTaskMethod;
import com.signals_app.signals.tasks.rendezvous.FetchConfirmedRendezvousTaskMethod.FetchConfirmedRendezvousTaskEvent;
import com.signals_app.signals.tasks.rendezvous.FetchConfirmedRendezvousTaskMethod.FetchConfirmedRendezvousTaskListener;
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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView.ScaleType;

public class ConfirmedRendezvousFragment extends SherlockFragment implements PlaceListener, FetchConfirmedRendezvousTaskListener, ProfilesTaskListener, CancelRendezvousTaskListener
{
    public static final int ACTION_REFRESH = 1;

    private FetchConfirmedRendezvousTaskMethod mFetchConfirmedRendezvousTaskMethod;
    private ProfilesTaskMethod mProfilesTaskMethod;
    private CancelRendezvousTaskMethod mCancelRendezvousTaskMethod;

    private DisplayImageOptions mImageOptions;

    // WIDGETS
    private ConfirmedRendezvousListAdapter mListAdapter;
    private ConfirmedRendezvousGridAdapter mGridAdapter;

    private int mIndexPager = 0;



    public static final ConfirmedRendezvousFragment newInstance()
    {
        ConfirmedRendezvousFragment f = new ConfirmedRendezvousFragment();
        Bundle bundle = new Bundle();
        f.setArguments(bundle);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFetchConfirmedRendezvousTaskMethod = new FetchConfirmedRendezvousTaskMethod(getSherlockActivity());
        mProfilesTaskMethod = new ProfilesTaskMethod(getSherlockActivity());
        mCancelRendezvousTaskMethod = new CancelRendezvousTaskMethod(getSherlockActivity());
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_people_inside, container, false);
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
        mCancelRendezvousTaskMethod.addCancelRendezvousTaskListener(this);
        mFetchConfirmedRendezvousTaskMethod.addFetchConfirmedRendezvousTaskListener(this);


        // LIST OR GRID

        switch (getActivity().getResources().getConfiguration().orientation )
        {
            case Configuration.ORIENTATION_LANDSCAPE:
                GridView gv = (GridView)view.findViewById(R.id.gridview);
                mGridAdapter = new ConfirmedRendezvousGridAdapter(this.getActivity(), 0, User.getInstance().getConfirmedRendezvous());

                gv.setAdapter(mGridAdapter);
                gv.setOnItemClickListener(new OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> l, View v, int position, long id) {
                        ConfirmedRendezvous pm = User.getInstance().getConfirmedRendezvous().get(position);

                        mIndexPager = position;

                        onClickRendezvous(pm);
                    }}
                );
                break;

            default:
                ListView lv = (ListView)view.findViewById(R.id.listview);
                mListAdapter = new ConfirmedRendezvousListAdapter(this.getActivity(), R.layout.row_person, User.getInstance().getConfirmedRendezvous());

                lv.setAdapter(mListAdapter);
                lv.setOnItemClickListener(new OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> l, View v, int position, long id)
                    {
                        ConfirmedRendezvous pm = User.getInstance().getConfirmedRendezvous().get(position);

                        mIndexPager = position;

                        onClickRendezvous(pm);
                    }}
                );

                break;
        }




        setHasOptionsMenu(true);

        updateView(view);

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
                fetchConfirmedRendezvous();
                return true;

        }

        return false;
    }


    @Override
    public void onResume() {
        super.onResume();

        fetchConfirmedRendezvous();
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
        mCancelRendezvousTaskMethod.removeCancelRendezvousTaskListener(this);
        mFetchConfirmedRendezvousTaskMethod.removeFetchConfirmedRendezvousTaskListener(this);

        mProfilesTaskMethod.cleanUp();
        mCancelRendezvousTaskMethod.cleanUp();
        mFetchConfirmedRendezvousTaskMethod.cleanUp();

    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }



    private void fetchConfirmedRendezvous()
    {
        mFetchConfirmedRendezvousTaskMethod.doTask(User.getInstance().getUserId());
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

    public void updateView(View view)
    {

        RelativeLayout root = (RelativeLayout)view.findViewById(R.id.people_layout);
        root.setVisibility(View.VISIBLE);

        // VIEW
        getActivity().setTitle(getResources().getString(R.string.confirmed_rendezvous));

        if( User.getInstance().getConfirmedRendezvous().size() != 0 )
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

        getSherlockActivity().invalidateOptionsMenu();

    }



    private class ConfirmedRendezvousListAdapter extends ArrayAdapter<ConfirmedRendezvous> {


        public ConfirmedRendezvousListAdapter(Context context, int textViewResourceId, ArrayList<ConfirmedRendezvous> pms ) {
            super(context, textViewResourceId, pms);
        }

        @Override
        public int getCount(){
            return User.getInstance().getConfirmedRendezvous().size();
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

            Person p = User.getInstance().getConfirmedRendezvous().get(position).getCandidate();



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



            // CANCELLED
            if( User.getInstance().getConfirmedRendezvous().get(position).cancelled() )
                v.setAlpha(0.3f);
            else
                v.setAlpha(1f);

            return v;
        }



    }

    private class ConfirmedRendezvousGridAdapter extends ArrayAdapter<ConfirmedRendezvous> {

        private Context mContext;

        public ConfirmedRendezvousGridAdapter(Context context, int textViewResourceId, ArrayList<ConfirmedRendezvous> pms ) {
            super(context, 0, pms);
            this.mContext = context;
        }

        @Override
        public int getCount(){
            return User.getInstance().getConfirmedRendezvous().size();
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

            Person p = User.getInstance().getConfirmedRendezvous().get(position).getCandidate();

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



            // CANCELLED
            if( User.getInstance().getConfirmedRendezvous().get(position).cancelled() )
                v.setAlpha(0.3f);
            else
                v.setAlpha(1f);

            return v;


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


    private void onClickRendezvous( final ConfirmedRendezvous rendezvous )
    {

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.row_place_with_time, null);

        if( rendezvous.cancelled() )
        {
            view = inflater.inflate(R.layout.layout_rendezvous_cancelled, null);

            TextView candidateTv = (TextView) view.findViewById(R.id.candidate_tv);
            TextView cancelReasonTv = (TextView) view.findViewById(R.id.cancel_reason_tv);

            if(! Utility.isEmpty(rendezvous.getCancelReason()) )
            {
                candidateTv.setText((rendezvous.cancelledByCandidate() ? rendezvous.getCandidate().getUsername() : User.getInstance().getUsername() ) + " " + getString(R.string.says));
                candidateTv.setTypeface(null, Typeface.ITALIC);
            }
            else
            {
                candidateTv.setVisibility(View.GONE);
                cancelReasonTv.setVisibility(View.GONE);
            }


            cancelReasonTv.setText(rendezvous.getCancelReason());
        }

        TextView tt = (TextView) view.findViewById(R.id.top_text);
        TextView bt = (TextView) view.findViewById(R.id.bottom_text);

        final Person person = rendezvous.getCandidate();
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


        // DIALOG
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

        alertDialogBuilder.setView(view);

        if( rendezvous.cancelled() )
        {
            alertDialogBuilder.setTitle(getString(R.string.rendezvous_with) + " " + person.getUsername() + " " + getString(R.string.CANCELLED));
            alertDialogBuilder
                    .setCancelable(true)
                    .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });
        }
        else
        {
            alertDialogBuilder.setTitle(getString(R.string.rendezvous_with) + " " + person.getUsername());
            alertDialogBuilder
                    .setCancelable(true)
                    .setPositiveButton(getString(R.string.decline), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

                            final EditText et = new EditText(getActivity());
                            et.setImeOptions(EditorInfo.IME_ACTION_DONE);
                            et.setFilters(new InputFilter[]{new InputFilter.LengthFilter(140)});

                            alertDialogBuilder.setView(et);
                            alertDialogBuilder.setTitle(getString(R.string.decline_reason));
                            alertDialogBuilder
                                    .setCancelable(true)
                                    .setPositiveButton(getString(R.string.send_to) + " " + person.getUsername(), new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            mCancelRendezvousTaskMethod.doTask(User.getInstance().getUserId(), rendezvous.getId(), et.getText().toString());
                                        }
                                    })
                                    .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {

                                        }
                                    });
                            AlertDialog alertDialog = alertDialogBuilder.create();
                            alertDialog.show();
                        }
                    })
                    .setNeutralButton(getString(R.string.view_bar),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    onClickPlace(p);
                                }
                            })
                    .setNegativeButton(getString(R.string.view_profile), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            ArrayList<Long> peopleIds = new ArrayList<Long>();

                            for (ConfirmedRendezvous pm : User.getInstance().getConfirmedRendezvous()) {
                                peopleIds.add(pm.getCandidate().getUserId());
                            }

                            mProfilesTaskMethod.doTask(User.getInstance().getUserId(), peopleIds);
                        }
                    });
        }


        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();



    }



    @Override
    public void placeEventReceived(PlaceEvent evt) {
        User user = (User)evt.getSource();

        if( user != null )
        {
            if( getView() != null )
                updateView(getView());
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
    public void cancelRendezvousTaskEventReceived(CancelRendezvousTaskMethod.CancelRendezvousTaskEvent evt) {
        CancelRendezvousTaskMethod met = (CancelRendezvousTaskMethod)evt.getSource();

        if( met != null && met.success() )
        {
            this.fetchConfirmedRendezvous();
        }
    }


    @Override
    public void fetchConfirmedRendezvousTaskEventReceived(FetchConfirmedRendezvousTaskEvent evt)
    {
        FetchConfirmedRendezvousTaskMethod met = (FetchConfirmedRendezvousTaskMethod)evt.getSource();

        if( met != null && met.success() )
        {
            User.getInstance().setConfirmedRendezvous(met.getConfirmedRendezvous());


            if( getView() != null )
                updateView(getView());
        }
    }

}
