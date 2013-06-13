package com.signals_app.signals.activity;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import android.app.ActionBar.LayoutParams;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.SpannableString;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import com.signals_app.signals.R;
import com.signals_app.signals.model.Constants;
import com.signals_app.signals.model.Place;
import com.signals_app.signals.model.Profile.GpsPosition;
import com.signals_app.signals.model.Profile.Person;
import com.signals_app.signals.model.Profile.User;
import com.signals_app.signals.model.Profile.User.GpsPositionEvent;
import com.signals_app.signals.model.Profile.User.GpsPositionListener;
import com.signals_app.signals.tasks.PlaceCheckoutTaskMethod;
import com.signals_app.signals.tasks.PlacesTaskMethod;
import com.signals_app.signals.tasks.PlacesTaskMethod.PlacesTaskEvent;
import com.signals_app.signals.tasks.PlacesTaskMethod.PlacesTaskListener;
import com.signals_app.signals.util.MyLocation;
import com.signals_app.signals.util.Utility;


public class MapActivity extends SherlockFragmentActivity implements PlacesTaskListener, GpsPositionListener, PlaceCheckoutTaskMethod.PlaceCheckoutTaskListener
{
    private PlacesTaskMethod mPlacesTaskMethod;
    private PlaceCheckoutTaskMethod mPlaceCheckoutTaskMethod;

    // MAP
    private GoogleMap mMap;
    private ArrayList<Place> mBars;
    private HashMap<Marker, Place> mMarkersHashmap = new HashMap<Marker, Place>();

    // GPS
    private MyLocation.LocationResult mLocationResult;
    private MyLocation mLocation;
    
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        
        setTitle(getString(R.string.bar_map));
        getSupportActionBar().setSubtitle(null);

        mPlacesTaskMethod = new PlacesTaskMethod(this);
        mPlaceCheckoutTaskMethod = new PlaceCheckoutTaskMethod(this);


        // GPS
        mLocationResult = new MyLocation.LocationResult(){
            @Override
            public void gotLocation(final Location location){
                MapActivity.this.runOnUiThread(new Runnable() {
                    public void run()
                    {
                        GpsPosition userPos = new GpsPosition((float)location.getLatitude(), (float)location.getLongitude(), location.getAccuracy());
                        User.getInstance().setGpsPosition(userPos);

                        // Check if user isn't too far away
                        Place checkPlace = User.getInstance().getCheckPlace();

                        if( checkPlace != null )
                        {
                            GpsPosition placePos = checkPlace.getGpsPosition();
                            int distanceInMeters = (int)(Utility.getDistance(userPos.getLatitude(), userPos.getLongitude(), placePos.getLatitude(), placePos.getLongitude(), "k")*1000);

                            if( distanceInMeters - userPos.getAccuracy() > Constants.MIN_CHECKIN_RADIUS )
                            {
                                mPlaceCheckoutTaskMethod.doTask(User.getInstance().getUserId(), PlaceCheckoutTaskMethod.CHECK_OUT_BECAUSE_TOO_FAR);
                            }
                        }

                        // Recheck location in 60 seconds (and give it an extra minute for GPS)
                        mLocation.getLocation(MapActivity.this, mLocationResult, 0, 50, 60000, false);
                    }
                });


            }
        };
        mLocation = new MyLocation();


        // MAP
        mMap = ((SupportMapFragment) this.getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
        mMap.setMyLocationEnabled(true);


        mMap.setOnInfoWindowClickListener( new OnInfoWindowClickListener() {

            @Override
            public void onInfoWindowClick(Marker marker) {
                
                
                Place p = mMarkersHashmap.get(marker);
                
                Intent output = new Intent();
                Bundle bundle = new Bundle();   
                bundle.putParcelable("place", p);
                output.putExtras(bundle);
                setResult(RESULT_OK, output);
                finish();
            }
        });

        mMap.setInfoWindowAdapter(new InfoWindowAdapter() 
        {
            private View v = getLayoutInflater().inflate(R.layout.window_marquer, null);

            
            @Override
            public View getInfoWindow(Marker marker) {

             //Only changing the content for this tutorial

             //if you return null, it will just use the default window

             return null;

            }

            

            @Override

            public View getInfoContents(Marker marker) 
            {
                v = getLayoutInflater().inflate(R.layout.window_marquer, null);
                v.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
             

                Place p = mMarkersHashmap.get(marker);

             
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
                    };
                    
                    String address = new String(p.getAddress());
 
                    
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
                    LinearLayout femaleLayout = (LinearLayout)v.findViewById(R.id.female_layout);
                    LinearLayout maleLayout = (LinearLayout)v.findViewById(R.id.male_layout);
                    
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
                    
                    if(u.getInterestedIn() == Person.INTERESTED_IN_BOTH )
                    {
                        inBetweenLayout.setVisibility(View.VISIBLE);
                        femaleLayout.setVisibility(View.VISIBLE);
                        maleLayout.setVisibility(View.VISIBLE);
                        
                        maleText.setText(Integer.toString(p.getNbUsersMale()));
                        femaleText.setText(Integer.toString(p.getNbUsersFemale()));
                    }                       
                    
                }
    
                          
    
                return v;
    
            }

       });

        updateCamera();
    }
    
    
    @Override
    protected void onResume()
    {
        super.onResume();

        mLocation.getLocation(MapActivity.this, mLocationResult, 0, 0, 60000, true);


        // LISTENERS
        User.getInstance().addGpsPositionListener(this);
        mPlacesTaskMethod.addPlacesTaskListener(this);
        mPlaceCheckoutTaskMethod.addPlaceCheckoutTaskListener(this);

        fetchPlaces();

    }


    @Override
    protected void onPause()
    {
        super.onPause();

        mLocation.cancelTimer();

        User.getInstance().removeGpsPositionListener(this);
        mPlacesTaskMethod.removePlacesTaskListener(this);
        mPlaceCheckoutTaskMethod.removePlaceCheckoutTaskListener(this);
    }


    @Override
    public void onDestroy()
    {
        super.onDestroy();

        mPlacesTaskMethod.cleanUp();
    }



    private void updateCamera()
    {

        Place bar = getIntent().getExtras().getParcelable("selectedBar");


        if( bar != null )
        {
            Marker marker = getKeyByValue(mMarkersHashmap, bar);

            if( marker != null )
            {
                GpsPosition gpsPosition = bar.getGpsPosition();

                if( gpsPosition != null )
                {
                    LatLng latlng = new LatLng(gpsPosition.getLatitude(), gpsPosition.getLongitude());
                    CameraPosition cameraPosition = new CameraPosition.Builder().target(latlng).zoom(14.0f).build();
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);

                    mMap.moveCamera(cameraUpdate);

                    marker.showInfoWindow();
                }
            }
        }
        else
        {
            User u = User.getInstance();
            GpsPosition uPos = u.getGpsPosition();

            if( uPos != null )
            {
                LatLng userLatLng = new LatLng(uPos.getLatitude(), uPos.getLongitude());
                CameraPosition cameraPosition = new CameraPosition.Builder().target(userLatLng).zoom(14.0f).build();
                CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);

                mMap.moveCamera(cameraUpdate);
            }
        }


    }

    private void updateView()
    {
        int maxNbUsers = 0;
        for( Place b : mBars )
        {
            switch( User.getInstance().getInterestedIn() )
            {
                case Person.INTERESTED_IN_MEN:
                    if(b.getNbUsersMale() > maxNbUsers)
                        maxNbUsers = b.getNbUsersMale();
                    break;

                case Person.INTERESTED_IN_WOMEN:
                    if(b.getNbUsersFemale() > maxNbUsers)
                        maxNbUsers = b.getNbUsersFemale();
                    break;

                case Person.INTERESTED_IN_BOTH:
                    if(b.getNbUsersTotal() > maxNbUsers)
                        maxNbUsers = b.getNbUsersTotal();
                    break;
            }
        }

        int temp = 0;

        for( Place b : mBars )
        {
            int nbUsers = 0;

            switch( User.getInstance().getInterestedIn() )
            {
                case Person.INTERESTED_IN_MEN:
                    nbUsers = b.getNbUsersMale();
                    break;

                case Person.INTERESTED_IN_WOMEN:
                    nbUsers = b.getNbUsersFemale();
                    break;

                case Person.INTERESTED_IN_BOTH:
                    nbUsers = b.getNbUsersTotal();
                    break;
            }


            temp = Math.min((int)(maxNbUsers != 0 ? ((float)nbUsers/(float)maxNbUsers)*60.0f : 0), 60);

            LatLng latLng = new LatLng(b.getGpsPosition().getLatitude(), b.getGpsPosition().getLongitude());
            Marker newMarker = mMap.addMarker(new MarkerOptions().position(latLng).title("marker title").icon(BitmapDescriptorFactory.defaultMarker(60 - temp)));
            mMarkersHashmap.put(newMarker, b);
        }

        updateCamera();

    }


    private void fetchPlaces()
    {
        User u = User.getInstance();
        mPlacesTaskMethod.doTask( u.getUserId(), u.getCity().getId());
    }


    public static <T, E> T getKeyByValue(Map<T, E> map, E value) {
        for (Map.Entry<T, E> entry : map.entrySet()) {
            if (value.equals(entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }

    public void forceCheckOut( int reason )
    {
        Place checkPlace = User.getInstance().getCheckPlace();

        if( checkPlace != null )
        {
            String reasonStr = "";

            switch( reason )
            {
                case PlaceCheckoutTaskMethod.CHECK_OUT_BECAUSE_TOO_FAR:
                    reasonStr = getString(R.string.because_youre_too_far_away);
                    break;
            }


            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MapActivity.this);

            alertDialogBuilder.setTitle(checkPlace.getName());

            alertDialogBuilder
                    .setMessage(getString(R.string.error_you_checked_out) + " " + checkPlace.getName() + " " + reasonStr)
                    .setCancelable(false)
                    .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {}
                    });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();

            User.getInstance().setCheckPlace(null);
        }
    }


    // EVENTS
    public void placesTaskEventReceived(PlacesTaskEvent evt)
    {
        PlacesTaskMethod met = (PlacesTaskMethod)evt.getSource();

        if( met != null && met.success() )
        {
            mBars = met.getPlaces();

            updateView();
        }

    }


    @Override
    public void gpsPositionEventReceived(GpsPositionEvent evt)
    {
        updateCamera();
    }


    @Override
    public void placeCheckoutTaskEventReceived(PlaceCheckoutTaskMethod.PlaceCheckoutTaskEvent evt)
    {
        PlaceCheckoutTaskMethod met = (PlaceCheckoutTaskMethod)evt.getSource();

        if( met != null && met.success() )
        {
            forceCheckOut(met.getReason());
        }

    }

} 
