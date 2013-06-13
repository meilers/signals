package com.signals_app.signals.activity;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

import com.signals_app.signals.R;

import com.signals_app.signals.activity.MessagesFragment;
import com.signals_app.signals.model.Place;
import com.signals_app.signals.model.Profile.Person;
import com.signals_app.signals.model.Profile.User;
import com.signals_app.signals.model.Profile.User.FlirtEvent;
import com.signals_app.signals.model.Profile.User.FlirtListener;
import com.signals_app.signals.model.Profile.User.MessageEvent;
import com.signals_app.signals.model.Profile.User.MessageListener;
import com.signals_app.signals.model.Profile.User.PlaceEvent;
import com.signals_app.signals.model.Profile.User.PlaceListener;
import com.signals_app.signals.model.Profile.User.VisitEvent;
import com.signals_app.signals.model.Profile.User.VisitListener;
import com.signals_app.signals.model.Profile.User.VoteEvent;
import com.signals_app.signals.model.Profile.User.VoteListener;
import com.signals_app.signals.model.Signals.Message;
import com.signals_app.signals.model.Signals.Signal;
import com.signals_app.signals.tasks.PlaceCheckoutTaskMethod;
import com.signals_app.signals.tasks.PlaceCheckoutTaskMethod.PlaceCheckoutTaskEvent;
import com.signals_app.signals.tasks.PlaceCheckoutTaskMethod.PlaceCheckoutTaskListener;
import com.signals_app.signals.tasks.signals.FetchSignalsTaskMethod;
import com.signals_app.signals.tasks.signals.FetchSignalsTaskMethod.FetchSignalsTaskEvent;
import com.signals_app.signals.tasks.signals.FetchSignalsTaskMethod.FetchSignalsTaskListener;
import com.signals_app.signals.tasks.signals.FetchVisitsTaskMethod;
import com.signals_app.signals.tasks.signals.FlirtTaskMethod;
import com.signals_app.signals.tasks.signals.FlirtTaskMethod.FlirtTaskEvent;
import com.signals_app.signals.tasks.signals.MessageTaskMethod;
import com.signals_app.signals.tasks.signals.MessageTaskMethod.MessageTaskEvent;
import com.signals_app.signals.tasks.signals.FlirtTaskMethod.FlirtTaskListener;
import com.signals_app.signals.tasks.signals.MessageTaskMethod.MessageTaskListener;
import com.signals_app.signals.util.PushReceiverActivity;
import com.signals_app.signals.util.PushReceiverActivity.PushEvent;
import com.signals_app.signals.util.PushReceiverActivity.PushListener;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnShowListener;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

public class SignalsFragment extends SherlockFragment implements FetchSignalsTaskListener, PlaceListener, PlaceCheckoutTaskListener
{
    public static final int ACTION_REFRESH = 2;
    public static final int ACTION_CHECK_OUT = 1;

    private PlaceCheckoutTaskMethod mPlaceCheckoutTaskMethod;
	private FetchSignalsTaskMethod mFetchSignalsTaskMethod;
	
	// WIDGETS
	private LinearLayout mBubbleLayout;
	private LinearLayout mSignalsLayout;
	
	private LinearLayout mFlirtsLayout;
	private LinearLayout mMessagesLayout;
	private LinearLayout mVotesLayout;
	private LinearLayout mVisitsLayout;
	
	private TextView mNbNewFlirtsTv;
	private TextView mNbNewMessagesTv;
	private TextView mNbNewVotesTv;
	private TextView mNbNewVisitsTv;
	
  	public static final SignalsFragment newInstance()
  	{
  		SignalsFragment f = new SignalsFragment(); 
  	    return f;  
  	}
  	
  	
  	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        mPlaceCheckoutTaskMethod = new PlaceCheckoutTaskMethod(getSherlockActivity());
        mFetchSignalsTaskMethod = new FetchSignalsTaskMethod(getSherlockActivity());
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		View view = inflater.inflate(R.layout.fragment_signals, container, false);
		getSherlockActivity().setTitle(getSherlockActivity().getResources().getString(R.string.signals));
		getSherlockActivity().getSupportActionBar().setSubtitle(null);


		
		// LISTENERS
		User.getInstance().addPlaceListener(this);
		mFetchSignalsTaskMethod.addFetchSignalsTaskListener(this);
        mPlaceCheckoutTaskMethod.addPlaceCheckoutTaskListener(this);


		// WIDGETS
		mBubbleLayout = (LinearLayout)view.findViewById(R.id.bubble_layout);
		mSignalsLayout = (LinearLayout)view.findViewById(R.id.signals_layout);
		
		mFlirtsLayout = (LinearLayout)view.findViewById(R.id.flirts_row);
		mMessagesLayout = (LinearLayout)view.findViewById(R.id.messages_row);
		mVotesLayout = (LinearLayout)view.findViewById(R.id.votes_row);
		mVisitsLayout = (LinearLayout)view.findViewById(R.id.visits_row);
		
		mNbNewFlirtsTv = (TextView)view.findViewById(R.id.nb_new_flirts_tv);
		mNbNewMessagesTv = (TextView)view.findViewById(R.id.nb_new_messages_tv);
		mNbNewVotesTv = (TextView)view.findViewById(R.id.nb_new_votes_tv);
		mNbNewVisitsTv = (TextView)view.findViewById(R.id.nb_new_visits_tv);
		
		mFlirtsLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickFlirts();
            }
		});
		mMessagesLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickMessages();
            }
        });
		mVotesLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickVotes();
            }
        });
		mVisitsLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickVisits();
            }
        });
		
		
		
		setHasOptionsMenu(true);
		
		updateView();
		
		return view;
	}

    @Override
    public void onResume() {
        super.onResume();
        
        fetchSignals();
    }
    
    
    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        
        User.getInstance().removePlaceListener(this);
        mFetchSignalsTaskMethod.removeFetchSignalsTaskListener(this);
        mPlaceCheckoutTaskMethod.removePlaceCheckoutTaskListener(this);

        mFetchSignalsTaskMethod.cleanUp();
        mPlaceCheckoutTaskMethod.cleanUp();
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
                fetchSignals();
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
                            .setCancelable(false)
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
    
    
    
    
    private void fetchSignals()
    {
    	mFetchSignalsTaskMethod.doTask(User.getInstance().getUserId());
    }
    

	private void updateView()
	{
	    User u = User.getInstance();
	    
        if( !u.checkedIn() )
        {
            getSherlockActivity().getSupportActionBar().setSubtitle(null);
            
            mBubbleLayout.setVisibility(View.VISIBLE);
            mSignalsLayout.setVisibility(View.GONE);
        }
        else
        {
            getSherlockActivity().getSupportActionBar().setSubtitle(getString(R.string.tonight_at) + " " + User.getInstance().getCheckPlace().getName());
            
            
            mBubbleLayout.setVisibility(View.GONE);
            mSignalsLayout.setVisibility(View.VISIBLE);

            if( !u.vip() )
                mVisitsLayout.setAlpha(0.4f);
            else
                mVisitsLayout.setAlpha(1f);
            
            
            if( u.getNbNewFlirts() > 0 )
                mNbNewFlirtsTv.setText(u.getNbNewFlirts() + "");
            else
                mNbNewFlirtsTv.setText("");
            
            if( u.getNbNewMessages() > 0 )
                mNbNewMessagesTv.setText(u.getNbNewMessages() + "");
            else
                mNbNewMessagesTv.setText("");
            
            if( u.getNbNewVotes() > 0 )
                mNbNewVotesTv.setText(u.getNbNewVotes() + "");
            else
                mNbNewVotesTv.setText("");
            
            if( u.getNbNewVisits() > 0 )
                mNbNewVisitsTv.setText(u.getNbNewVisits() + "");
            else
                mNbNewVisitsTv.setText("");
        }
        

        getSherlockActivity().invalidateOptionsMenu();   
	}

	
	

	// EVENTS

	private void onClickFlirts()
	{
        FlirtsFragment flirtsFrag =  FlirtsFragment.newInstance();
        
        getSherlockActivity().getSupportFragmentManager()
        .beginTransaction()
        .replace(R.id.content_frame, flirtsFrag, "FlirtsFragment")
        .addToBackStack(null)
        .commit();
	}
	
	private void onClickMessages()
    {
        MessagesFragment messagesFrag =  MessagesFragment.newInstance();
        
        getSherlockActivity().getSupportFragmentManager()
        .beginTransaction()
        .replace(R.id.content_frame, messagesFrag, "MessagesFragment")
        .addToBackStack(null)
        .commit();
         
    }
	
	private void onClickVotes()
    {
        VotesFragment votesFrag =  VotesFragment.newInstance();
        
        getSherlockActivity().getSupportFragmentManager()
        .beginTransaction()
        .replace(R.id.content_frame, votesFrag, "VotesFragment")
        .addToBackStack(null)
        .commit();
    }
	
	private void onClickVisits()
    {
        
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getSherlockActivity());
    
        alertDialogBuilder.setTitle(getResources().getString(R.string.vip_restricted));
    
        alertDialogBuilder
            .setMessage(getResources().getString(R.string.become_vip_to_see_visits))
            .setCancelable(true)
            .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int id) {
                    VisitsFragment visitsFrag =  VisitsFragment.newInstance();
                    
                    getSherlockActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content_frame, visitsFrag, "VisitsFragment")
                    .addToBackStack(null)
                    .commit();
                }
              })
            .setNegativeButton(getResources().getString(R.string.not_now),new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int id) {
                    // if this button is clicked, just close
                    // the dialog box and do nothing
                    dialog.cancel();
                }
            });
        
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
	
	
	@Override
	public void fetchSignalsTaskEventReceived(FetchSignalsTaskEvent evt) 
	{
		FetchSignalsTaskMethod met = (FetchSignalsTaskMethod)evt.getSource();
		
		if( met != null && met.success() )
		{
			User.getInstance().setNbNewFlirts(met.getNbNewFlirts());
			User.getInstance().setNbNewMessages(met.getNbNewMessages());
			User.getInstance().setNbNewVotes(met.getNbNewVotes());
			User.getInstance().setNbNewVisits(met.getNbNewVisits());
			
			updateView();
		}
	}



    @Override
    public void placeEventReceived(PlaceEvent evt) {
        // TODO Auto-generated method stub
        updateView();
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
