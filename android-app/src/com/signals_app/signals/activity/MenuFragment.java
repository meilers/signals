package com.signals_app.signals.activity;


import java.io.File;
import java.util.ArrayList;

import com.actionbarsherlock.app.SherlockFragment;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.MemoryCacheUtil;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.signals_app.signals.R;
import com.signals_app.signals.model.Place;
import com.signals_app.signals.model.Profile.User.ConfirmedRendezvousEvent;
import com.signals_app.signals.model.Profile.User.ConfirmedRendezvousListener;
import com.signals_app.signals.model.Profile.User.FlirtEvent;
import com.signals_app.signals.model.Profile.User.FlirtListener;
import com.signals_app.signals.model.Profile.User.MessageEvent;
import com.signals_app.signals.model.Profile.User.MessageListener;
import com.signals_app.signals.model.Profile.User.PlaceEvent;
import com.signals_app.signals.model.Profile.User.PlaceListener;
import com.signals_app.signals.model.Profile.User.PotentialRendezvousEvent;
import com.signals_app.signals.model.Profile.User.PotentialRendezvousListener;
import com.signals_app.signals.model.Profile.User.VisitEvent;
import com.signals_app.signals.model.Profile.User.VisitListener;
import com.signals_app.signals.model.Profile.User.VoteEvent;
import com.signals_app.signals.model.Profile.User.VoteListener;
import com.signals_app.signals.model.Profile.GpsPosition;
import com.signals_app.signals.model.Profile.Person;
import com.signals_app.signals.model.Profile.User;
import com.signals_app.signals.tasks.PlaceCheckoutTaskMethod;
import com.signals_app.signals.tasks.PlaceCheckoutTaskMethod.PlaceCheckoutTaskEvent;
import com.signals_app.signals.tasks.PlaceCheckoutTaskMethod.PlaceCheckoutTaskListener;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MenuFragment extends SherlockFragment implements PlaceListener, PotentialRendezvousListener, ConfirmedRendezvousListener, FlirtListener, MessageListener, VoteListener, VisitListener
{
	private OnTabSelectedListener mCallback;

    private LinearLayout mNotCheckedinLayout;
	private LinearLayout mLocationLayout;
	
	private LinearLayout mBarsRow;
	private LinearLayout mInvitationsRow;
	private LinearLayout mProfileRow;
	private LinearLayout mPeopleRow;
	private LinearLayout mSignalsRow;
	
	private TextView mProfileTv;
	private TextView mLocationTv;
	private TextView mInvitationsNotifTv;
	private TextView mPeopleNotifTv;
	private TextView mSignalsNotifTv;
	
	private ImageView mProfileIv;


	public static final MenuFragment newInstance()
    {
	    MenuFragment f = new MenuFragment();
        return f;
    }
    
	   
    @Override
    public void onAttach(Activity activity){
      super.onAttach(activity);
      
      // This makes sure that the container activity has implemented
      // the callback interface. If not, it throws an exception
      try {
          mCallback = (OnTabSelectedListener) activity;
      } catch (ClassCastException e) {
          throw new ClassCastException(activity.toString()
                  + " must implement OnHeadlineSelectedListener");
      }
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) { 
        super.onCreate(savedInstanceState);
    }
    
    
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		if (container == null) {
			return null;
		}
                  
		View view = inflater.inflate(R.layout.fragment_menu, null);
		
		
		// LISTENERS
		User.getInstance().addPotentialRendezvousListener(this);
		User.getInstance().addConfirmedRendezvousListener(this);
		User.getInstance().addFlirtListener(this);
		User.getInstance().addMessageListener(this);
		User.getInstance().addVoteListener(this);
		User.getInstance().addVisitListener(this);
        User.getInstance().addPlaceListener(this);

        mNotCheckedinLayout = (LinearLayout)view.findViewById(R.id.not_checked_in_layout);
        mLocationLayout = (LinearLayout)view.findViewById(R.id.location_layout);
        
        mBarsRow = (LinearLayout)view.findViewById(R.id.bars_row);
        mInvitationsRow = (LinearLayout)view.findViewById(R.id.invitations_row);
        mProfileRow = (LinearLayout)view.findViewById(R.id.profile_row);
        mPeopleRow = (LinearLayout)view.findViewById(R.id.people_row);
        mSignalsRow = (LinearLayout)view.findViewById(R.id.signals_row);
        
        mProfileTv = (TextView)view.findViewById(R.id.profile_tv);
        mInvitationsNotifTv = (TextView)view.findViewById(R.id.invitations_notif_tv);
        mLocationTv = (TextView)view.findViewById(R.id.location_tv);
        mPeopleNotifTv = (TextView)view.findViewById(R.id.people_notif_tv);
        mSignalsNotifTv = (TextView)view.findViewById(R.id.signals_notif_tv);
        
        mProfileIv = (ImageView)view.findViewById(R.id.profile_iv);
		
        mBarsRow.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onPlacesSelected();
            }
        });
        
        mInvitationsRow.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onInvitationsSelected();
            }
        });

        mProfileRow.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onProfileSelected();
            }
        });
        
        mPeopleRow.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onPeopleInsideSelected();
            }
        });
        
        mSignalsRow.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onSignalsSelected();
            }
        });

        
        updateView(view);
        
        // Clear caches because profile pics might have been changed
        /*
        ImageLoader imageLoader = ImageLoader.getInstance();
        String imageUri = "http://s3.amazonaws.com/signals/user_images/" + User.getInstance().getUserId() + "/profilePhotoThumb.jpg";
        
        
        MemoryCacheUtil.removeFromCache(imageUri, imageLoader.getMemoryCache());
        File imageFile = imageLoader.getDiscCache().get(imageUri);
        if (imageFile.exists()) {
            imageFile.delete();
        }
        */
        
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	}
	
	@Override
    public void onResume()
    {
        super.onResume();
        
        updateView(getView());
    }

	
    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        
        // LISTENERS
        User.getInstance().removePotentialRendezvousListener(this);
        User.getInstance().removeConfirmedRendezvousListener(this);
		User.getInstance().removeFlirtListener(this);
		User.getInstance().removeMessageListener(this);
		User.getInstance().removeVoteListener(this);
		User.getInstance().removeVisitListener(this);
		User.getInstance().removePlaceListener(this);
    }
    
    
    
    
    public void updateView(View view)
    {
        User u = User.getInstance();
        
        if( u.getNbNewInvitations() > 0 )
        {
            mInvitationsNotifTv.setVisibility(View.VISIBLE);
            mInvitationsNotifTv.setText(u.getNbNewInvitations()+"");
        }
        else
            mInvitationsNotifTv.setText("");


        if( u.checkedIn() )
        {
            Place place = u.getCheckPlace();
            
            mLocationLayout.setVisibility(View.VISIBLE);
            mLocationTv.setText(place.getName());
            
            if( place.getPeopleNewHashMap() != null && place.getPeopleNewHashMap().size() > 0 )
                mPeopleNotifTv.setText(User.getInstance().getPlace().getPeopleNewHashMap().size()+"");
            else
                mPeopleNotifTv.setText(""); 
            
            if( u.getNbNewSignals() > 0 )
                mSignalsNotifTv.setText(u.getNbNewSignals()+"");
            else
                mSignalsNotifTv.setText("");


            mNotCheckedinLayout.setVisibility(View.GONE);
            mLocationLayout.setVisibility(View.VISIBLE);
        }
        else
        {
            mNotCheckedinLayout.setVisibility(View.VISIBLE);
            mLocationLayout.setVisibility(View.GONE);
        }
        
        
        ImageLoader imageLoader = ImageLoader.getInstance();
        DisplayImageOptions imageOptions = new DisplayImageOptions.Builder()
        .cacheInMemory()
        .cacheOnDisc()
        .build();
        
        String imageUri = "http://s3.amazonaws.com/signals/user_images/" + u.getUserId() + "/profilePhotoThumb.jpg";
        
        imageLoader.displayImage(imageUri, mProfileIv, imageOptions);
        
        mProfileTv.setText(u.getUsername());
    }
    
	
	
	// LISTENER
    public interface OnTabSelectedListener {
        public void onPlacesSelected();
        public void onPeopleInsideSelected();
        public void onSignalsSelected();
        public void onInvitationsSelected();
        public void onProfileSelected();
    }
    
    
    
    
	// EVENTS
    @Override
	public void placeEventReceived(PlaceEvent evt)
	{
        if( getView() != null )
            updateView(getView());

	}


	@Override
	public void visitEventReceived(VisitEvent evt) {
        if( getView() != null )
            updateView(getView());

		
	}


	@Override
	public void voteEventReceived(VoteEvent evt) {
        if( getView() != null )
            updateView(getView());

		
	}


	@Override
	public void messageEventReceived(MessageEvent evt) {
        if( getView() != null )
            updateView(getView());

		
	}


	@Override
	public void flirtEventReceived(FlirtEvent evt) {
        if( getView() != null )
            updateView(getView());

	}



    @Override
    public void confirmedInvitationEventReceived(ConfirmedRendezvousEvent evt) {
        if( getView() != null )
            updateView(getView());

    }


    @Override
    public void potentialInvitationEventReceived(PotentialRendezvousEvent evt) {
        if( getView() != null )
            updateView(getView());

    }
    


}
