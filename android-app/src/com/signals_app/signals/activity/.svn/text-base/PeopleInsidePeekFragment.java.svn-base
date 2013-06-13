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
import android.widget.ImageView.ScaleType;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class PeopleInsidePeekFragment extends SherlockFragment implements GpsPositionListener, PlaceListener, PlaceCheckoutTaskListener, PlaceCheckinTaskListener, PeopleTaskMethod.PeopleTaskListener, ProfilesTaskListener
{
    public static final int ACTION_REFRESH = 1;
    
    private boolean mTooFar = false;
    private int mNbPhotosLoaded = 0;
    private int mNbPhotosToLoad = 0;
    
    private PeopleTaskMethod mPeopleTaskMethod;
    private PlaceCheckinTaskMethod mPlaceCheckInTaskMethod; 
    private ProfilesTaskMethod mProfilesTaskMethod;  
    
    private DisplayImageOptions mImageOptions;
    
    // WIDGETS
    private Place mPlace = null;
    private ArrayList<Person> mPeople = null; 
    private PeopleListAdapter mListAdapter;
    private PeopleGridAdapter mGridAdapter;
    
    
    public static final PeopleInsidePeekFragment newInstance(Place place)
    {
        PeopleInsidePeekFragment f = new PeopleInsidePeekFragment();
        
        
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_people_inside_peek, container, false);
        
        mPlace = getArguments().getParcelable("place");

        getSherlockActivity().setTitle(mPlace.getName());
        getSherlockActivity().getSupportActionBar().setSubtitle(null);
        
        
        mImageOptions = new DisplayImageOptions.Builder()
        .cacheInMemory()
        .cacheOnDisc()
        //.displayer(new RoundedBitmapDisplayer(10))
        .build();
        
        mPeopleTaskMethod = new PeopleTaskMethod(getSherlockActivity());
        mPlaceCheckInTaskMethod = new PlaceCheckinTaskMethod(getSherlockActivity());
        mProfilesTaskMethod = new ProfilesTaskMethod(getSherlockActivity());
        
        
        // LISTENERS
        
        User u = User.getInstance();
        u.addGpsPositionListener(this);
        u.addPlaceListener(this);
        
        mPlaceCheckInTaskMethod.addPlaceCheckinTaskListener(this);
        mPeopleTaskMethod.addPeopleTaskListener(this);
        mProfilesTaskMethod.addProfilesTaskListener(this);
        
        
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
                            if( !User.getInstance().vip() )
                            {
                                onClickNotVip(position);
                            }
                            else
                            { 
                                Person p = mPeople.get(position);
                                onClickPerson(p);  
                            }
                        }
                    }}
                );
                break;
                
            default:
                ListView lv = (ListView)view.findViewById(R.id.listview);
                mListAdapter = new PeopleListAdapter(this.getActivity(), R.layout.row_person_inside_peek, mPeople);
                
                lv.setAdapter(mListAdapter);
                lv.setOnItemClickListener(new OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> l, View v, int position, long id) {
                        if( mPeople.get(position).getUserId() != 0) 
                        {
                            if( !User.getInstance().vip() )
                            {
                                onClickNotVip(position);
                            }
                            else
                            {
                                Person p = mPeople.get(position);
                                onClickPerson(p);  
                            }
                        }
                    }}
                );
                
                break;
        }  
                
        
        // BUTTONS
        Button stopPeekBtn = (Button)view.findViewById(R.id.stop_peeking_btn);
        Button checkinBtn = (Button)view.findViewById(R.id.checkin_btn);
        stopPeekBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {  
                onClickStopPeek();
            }
        });
        checkinBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {  
                onClickCheckin();
            }
        });
        
        
        
        
        
        setHasOptionsMenu(true);
        
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Add your menu entries here
        super.onCreateOptionsMenu(menu, inflater);
        
        
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
                fetchPeople();
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
        
        User.getInstance().removeGpsPositionListener(this);
        User.getInstance().removePlaceListener(this);
        
        mPlaceCheckInTaskMethod.removePlaceCheckinTaskListener(this);
        mProfilesTaskMethod.removeProfilesTaskListener(this);
        mPeopleTaskMethod.removePeopleTaskListener(this);
        
        mPeopleTaskMethod.cleanUp();
        mPlaceCheckInTaskMethod.cleanUp();
        mProfilesTaskMethod.cleanUp();
    }
    
    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }
    
    
    
    public void fetchPeople()
    {   
        User u = User.getInstance();
        GpsPosition pos = u.getGpsPosition();
        
        if( pos != null )
        {
            mPeopleTaskMethod.doTask(u.getUserId(), mPlace.getPlaceId(), pos.getLatitude(), pos.getLongitude(), pos.getAccuracy(), 20); 
        }
    }
    
    
    
    public void updateButtons()
    {
        Place peekPlace = User.getInstance().getPeekPlace();
        
        if( peekPlace != null )
        {
            Button stopPeekButton = (Button)getView().findViewById(R.id.stop_peeking_btn);
            Button checkInButton = (Button)getView().findViewById(R.id.checkin_btn);
            
            User u = User.getInstance();
            GpsPosition placePos = peekPlace.getGpsPosition();
            GpsPosition userPos = u.getGpsPosition();
            
            if( placePos != null && userPos != null )
            {
                int distanceInMeters = (int)(Utility.getDistance(userPos.getLatitude(), userPos.getLongitude(), placePos.getLatitude(), placePos.getLongitude(), "k")*1000);
                
                if( distanceInMeters - userPos.getAccuracy() > Constants.MIN_CHECKIN_RADIUS )
                {
                    stopPeekButton.setBackground(getResources().getDrawable(R.drawable.btn_black_square));
                    
                    checkInButton.setBackground(getResources().getDrawable(R.drawable.btn_grey_square));
                    checkInButton.setText(Html.fromHtml(getResources().getString(R.string.checkin) + "<br/><small><font color='#888888'>" + 
                                                        getResources().getString(R.string.in) + " " + 
                                                        (distanceInMeters-Constants.MIN_CHECKIN_RADIUS) + " " + getResources().getString(R.string.meters) + "</small></font>"));
                    
                    mTooFar = true;
                }
                else
                {
                    checkInButton.setBackground(getResources().getDrawable(R.drawable.btn_green_square));
                    stopPeekButton.setBackground(getResources().getDrawable(R.drawable.btn_black_square));
                    checkInButton.setText(getResources().getString(R.string.checkin));
                    
                    mTooFar = false;
                }
                
                stopPeekButton.setClickable(true);
                checkInButton.setClickable(true);
            }
            else
            {
                Button checkinBtn = (Button)getView().findViewById(R.id.checkin_btn);
                checkinBtn.setBackground(getResources().getDrawable(R.drawable.btn_grey_square));
                checkinBtn.setText(getSherlockActivity().getResources().getString(R.string.checkin));
                
                stopPeekButton.setClickable(false);
                checkInButton.setClickable(false);
            }
        }
    }
    
    
    private void updatePeople()
    {
        mPeople.clear();
        
        Person.PersonComparator comparator = new Person.PersonComparator();
        
        HashMap<Long,Person> peopleBeforeHashMap = mPlace.getPeopleBeforeHashMap();
        HashMap<Long,Person> peopleNewHashMap = mPlace.getPeopleNewHashMap();
        
        if( peopleBeforeHashMap != null && peopleNewHashMap != null )
        {
            ArrayList<Person> peopleNew = new ArrayList<Person>(peopleNewHashMap.values());   
            ArrayList<Person> peopleBefore = new ArrayList<Person>(peopleBeforeHashMap.values());
            
            
            
            Collections.sort(peopleNew, comparator);
            Collections.sort(peopleBefore, comparator);
            
            mPeople.addAll(peopleNew);
            mPeople.addAll(peopleBefore); 
            
            
            // set new people to before people
            for( Person p : peopleNewHashMap.values() )
            {
                if(!peopleBeforeHashMap.containsKey(p.getUserId()))
                    peopleBeforeHashMap.put(p.getUserId(), p);
            }
            mPlace.setPeopleNewHashMap(new HashMap<Long,Person>());
            
            
            
            switch( User.getInstance().getInterestedIn() )
            {
                case Person.INTERESTED_IN_WOMEN:
                    getSherlockActivity().getSupportActionBar().setSubtitle( mPlace.getNbUsersFemale() + " " + getString(R.string.girls));
                    break;
                    
                case Person.INTERESTED_IN_MEN:
                    getSherlockActivity().getSupportActionBar().setSubtitle( mPlace.getNbUsersMale() + " " + getString(R.string.guys));
                    break;
                    
                case Person.INTERESTED_IN_BOTH:
                    getSherlockActivity().getSupportActionBar().setSubtitle( mPlace.getNbUsersTotal() + " " + getString(R.string.users));
                    break;
            }
            
        }
        
    }
    
    public void updateList()
    {
        updatePeople();
        
        // Update!
        mListAdapter.notifyDataSetChanged();
    }
    
    public void updateGrid()
    {
        updatePeople();
        
        // Update!
        mGridAdapter.notifyDataSetChanged();
    }

    public void updateView()
    {
        RelativeLayout root = (RelativeLayout)getView().findViewById(R.id.people_layout);
        root.setVisibility(View.VISIBLE);
        
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
        
        updateButtons();

    }
    
    
    
   

    
    private class PeopleListAdapter extends ArrayAdapter<Person> {

        private ArrayList<Person> mPeople = new ArrayList<Person>();
        
        public PeopleListAdapter(Context context, int textViewResourceId, ArrayList<Person> people ) {
                super(context, textViewResourceId, people);
                this.mPeople = people;
        }

        @Override
        public int getCount() 
        {
            if( !User.getInstance().vip() )
                return Math.min(mPeople.size(), 6);
            
            return mPeople.size();
        }
        
        
        @Override
        public View getView(int position, View convertView, ViewGroup parent) 
        {
            View v = convertView;
            
            if( v == null )
            {
                LayoutInflater vi = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.row_person_inside_peek, null);
            }
            
            TextView moreTv = (TextView)v.findViewById(R.id.more_tv); 
            moreTv.setVisibility(View.GONE);
            
            RelativeLayout personLayout = (RelativeLayout)v.findViewById(R.id.person_layout); 
            personLayout.setVisibility(View.VISIBLE);
            

            if( !User.getInstance().vip() && position == 5 )
            {
                personLayout.setVisibility(View.GONE);
                moreTv.setVisibility(View.VISIBLE);
                return v;
            }


            /**** PEOPLE ****/
            
            Person p = mPeople.get(position);
       
            
            
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

    private class PeopleGridAdapter extends ArrayAdapter<Person> {

        private ArrayList<Person> mPeople;
        private Context mContext;
        
        public PeopleGridAdapter(Context context, int textViewResourceId, ArrayList<Person> people ) {
                super(context, 0, people);
                this.mPeople = people;
                this.mContext = context;
        }
     
        @Override
        public int getCount() 
        {
            if( !User.getInstance().vip() )
                return Math.min(mPeople.size(), 6);
            
            return mPeople.size();
        }
     
        @Override
        public Person getItem(int position) 
        {
            
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
                        onClickNotVip(5);
                    }
                });
            }
            
            Button newPeopleBtn = (Button)v.findViewById(R.id.new_people_btn); 
            newPeopleBtn.setVisibility(View.GONE);
            
            RelativeLayout personLayout = (RelativeLayout)v.findViewById(R.id.person_layout); 
            personLayout.setVisibility(View.VISIBLE);
            
            final LinearLayout coverLayout = (LinearLayout)v.findViewById(R.id.cover_layout);
            coverLayout.setVisibility(View.GONE);
            
            
            if( !User.getInstance().vip() && position == 5 )
            {
                newPeopleBtn.setVisibility(View.VISIBLE);
                personLayout.setVisibility(View.GONE);
                newPeopleBtn.setText(getResources().getString(R.string.more));
                return v;
            }
            
            
            
            /**** PEOPLE ****/
            
            Person p = mPeople.get(position);
            
            
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
    
    private void onClickNotVip( int position )
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getSherlockActivity());
        Resources r = getSherlockActivity().getResources();
        
        alertDialogBuilder.setTitle(r.getString(R.string.vip_privilege));
        
        if( position < 5 )
        {
            alertDialogBuilder
                .setMessage(r.getString(R.string.become_vip_to_browse_profiles))
                .setCancelable(true)
                .setPositiveButton(r.getString(R.string.learn_more),new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        VipPrivilegesFragment frag = (VipPrivilegesFragment)getSherlockActivity().getSupportFragmentManager().findFragmentByTag("VipPrivilegesFragment");
                        
                        if( frag == null )
                            frag = VipPrivilegesFragment.newInstance( User.getInstance() );
                    
                        getSherlockActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, frag, "VipPrivilegesFragment")
                        .addToBackStack(null)
                        .commit();
                    }
                  })
                .setNegativeButton(r.getString(R.string.cancel),new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.cancel();
                    }
                });
        }
        else
        {
            alertDialogBuilder
            .setMessage(r.getString(R.string.become_vip_to_see_more_people))
            .setCancelable(true)
            .setPositiveButton(r.getString(R.string.learn_more),new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int id) {
                    VipPrivilegesFragment frag = (VipPrivilegesFragment)getSherlockActivity().getSupportFragmentManager().findFragmentByTag("VipPrivilegesFragment");
                    
                    if( frag == null )
                        frag = VipPrivilegesFragment.newInstance( User.getInstance() );
                
                    getSherlockActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content_frame, frag, "VipPrivilegesFragment")
                    .addToBackStack(null)
                    .commit();
                }
              })
            .setNegativeButton(r.getString(R.string.cancel),new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int id) {
                    dialog.cancel();
                }
            });
        }
        
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();  
    }
    
    
    private void onClickPerson( Person personClicked )
    {
        ArrayList<Long> peopleIds = new ArrayList<Long>();
        
        int index = 0;
        int i = 0;
        
        for( Person p : mPeople )
        {
            peopleIds.add(p.getUserId());
            
            if( p.equals(personClicked) )
                index = i;
            
            ++i;
        }
        
        getArguments().putInt("index", index);
        mProfilesTaskMethod.doTask(User.getInstance().getUserId(), peopleIds);
    }
    
    
        
    private void onClickStopPeek()
    {
        User.getInstance().setPeekPlace(null);
        
        getActivity().getSupportFragmentManager().popBackStack();
        getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
    }
    
    private void onClickCheckin()
    {
        User u = User.getInstance();
        Place peekPlace = u.getPeekPlace();
        
        if( !mTooFar && peekPlace != null )
        {     
            
            if( u.getCheckPlace() != null )
            {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getSherlockActivity());
                Resources r = getSherlockActivity().getResources();
                
                
                // set title
                alertDialogBuilder.setTitle(r.getString(R.string.checkout));
         
                // set dialog message
                alertDialogBuilder
                    .setMessage(r.getString(R.string.checkout_change_place_msg1) + " " + u.getCheckPlace().getName() + ". " + r.getString(R.string.checkout_change_place_msg2))
                    .setCancelable(false)
                    .setPositiveButton(r.getString(R.string.checkout),new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            mPlaceCheckInTaskMethod.doTask(User.getInstance().getUserId(), User.getInstance().getPeekPlace().getPlaceId());
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
                mPlaceCheckInTaskMethod.doTask(u.getUserId(), peekPlace.getPlaceId());
        }
    }
    

    @Override
    public void gpsPositionEventReceived(GpsPositionEvent evt) 
    {
        User user = (User)evt.getSource();
        
        if( user != null )
        {
            if( user.getState() == User.PEEKING )
            {
                updateButtons();
            }
        }
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
    public void placeCheckoutTaskEventReceived(PlaceCheckoutTaskEvent evt) {
        PlaceCheckoutTaskMethod met = (PlaceCheckoutTaskMethod)evt.getSource();
        
        if( met != null && met.success() )
        {
            User.getInstance().setCheckPlace(null);
            
            updateView();
        }
    }

    @Override
    public void placeCheckinTaskEventReceived(PlaceCheckinTaskEvent evt) {
        PlaceCheckinTaskMethod met = (PlaceCheckinTaskMethod)evt.getSource();
        
        if( met != null && met.success() )
        {
            User u = User.getInstance();
            Place peekPlace = u.getPeekPlace();
            
            u.setCheckPlace(new Place(peekPlace));
            u.setPeekPlace(null);
            
            getActivity().getSupportFragmentManager().popBackStack();
            getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
            
            
            // Open new frag
            PeopleInsideFragment frag = (PeopleInsideFragment)getActivity().getSupportFragmentManager().findFragmentByTag("PeopleInsideFragment");
            
            if( frag == null )
                frag = PeopleInsideFragment.newInstance();
            
            getActivity().getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.content_frame, frag, "PeopleInsideFragment")
            .addToBackStack(null)
            .commit();
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
            
            HashMap<Long, Person> peopleBeforeHashMapOriginal = mPlace.getPeopleBeforeHashMap();
            HashMap<Long, Person> peopleNewHashMap = new HashMap<Long, Person>();                       // in case new people have left
            HashMap<Long, Person> peopleBeforeHashMap = new HashMap<Long, Person>();                    // in case before people have left
            
            if( peopleBeforeHashMapOriginal != null )
            {
                
                for( Person p : peopleBar )
                {
                    if(!peopleBeforeHashMapOriginal.containsKey(p.getUserId()))
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
                
            mPlace.setPeopleBeforeHashMap(peopleBeforeHashMap);
            mPlace.setPeopleNewHashMap(peopleNewHashMap);
            
            // Notify menu
            MainActivity act = (MainActivity)getSherlockActivity();

            if( act.getMenuFragment().getView() != null )
                act.getMenuFragment().updateView(act.getMenuFragment().getView());
        }
        
        updateView();
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
