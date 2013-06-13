package com.signals_app.signals.activity;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;

import android.app.ActionBar.LayoutParams;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.signals_app.signals.R;
import com.signals_app.signals.model.Place;
import com.signals_app.signals.model.Profile.Person;
import com.signals_app.signals.model.Profile.User;
import com.signals_app.signals.model.Profile.User.MessageEvent;
import com.signals_app.signals.model.Signals.Message;
import com.signals_app.signals.model.Signals.Signal;
import com.signals_app.signals.tasks.ProfilesTaskMethod;
import com.signals_app.signals.tasks.ProfilesTaskMethod.ProfilesTaskEvent;
import com.signals_app.signals.tasks.ProfilesTaskMethod.ProfilesTaskListener;
import com.signals_app.signals.tasks.signals.FetchMessagesChatTaskMethod;
import com.signals_app.signals.tasks.signals.UnblockTaskMethod;
import com.signals_app.signals.tasks.signals.FetchMessagesChatTaskMethod.FetchMessagesChatTaskListener;
import com.signals_app.signals.tasks.signals.FetchMessagesTaskMethod;
import com.signals_app.signals.tasks.signals.FetchMessagesChatTaskMethod.FetchMessagesChatTaskEvent;
import com.signals_app.signals.tasks.signals.MessageTaskMethod;
import com.signals_app.signals.tasks.signals.MessageTaskMethod.MessageTaskEvent;
import com.signals_app.signals.tasks.signals.MessageTaskMethod.MessageTaskListener;
import com.signals_app.signals.tasks.signals.UnblockTaskMethod.UnblockTaskEvent;
import com.signals_app.signals.tasks.signals.UnblockTaskMethod.UnblockTaskListener;
import com.signals_app.signals.util.PushReceiverActivity;
import com.signals_app.signals.util.Utility;
import android.graphics.Bitmap;
public class MessagesChatFragment extends SherlockListFragment implements FetchMessagesChatTaskListener, MessageTaskListener, UnblockTaskListener, ProfilesTaskListener
{
    public static final int ACTION_REFRESH = 1;
    
	private DisplayImageOptions mImageOptions;
	
	private FetchMessagesChatTaskMethod mFetchMessagesChatTaskMethod;
	private MessageTaskMethod mMessageTaskMethod;
	private UnblockTaskMethod mUnblockTaskMethod;
	private ProfilesTaskMethod mProfilesTaskMethod;
	
	private Person mPerson;
	
    private MessagesAdapter mAdapter;
	private ArrayList<Message> mMessages = null;
	
	
	// WIDGETS
	private Animation mAnimation;
    private EditText mMsgEt;
    private Button mSendBtn;
    

	
  	public static final MessagesChatFragment newInstance( Person person )
  	{
  	    MessagesChatFragment f = new MessagesChatFragment();
        
        // Get arguments passed in, if any
        Bundle args = f.getArguments();
        if (args == null) {
            args = new Bundle();
        }
        
        // Add parameters to the argument bundle
        args.putInt("index", 0);
        args.putParcelable("person", person );          // may be cloned, but is basically referenced
        f.setArguments(args);
        
  	    return f;
  	}
  	
  	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); 
		
		Bundle args = getArguments();
		
		mPerson = args.getParcelable("person");
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
	    
		View view = inflater.inflate(R.layout.fragment_messages_chat, container, false);
		getSherlockActivity().setTitle(mPerson.getUsername());
		getSherlockActivity().getSupportActionBar().setSubtitle(null);
		
		// IMAGE LOADER
		mImageOptions = new DisplayImageOptions.Builder()
		.cacheInMemory()
		.cacheInMemory()
        .cacheOnDisc()
		.bitmapConfig(Bitmap.Config.RGB_565)
		
		.imageScaleType(ImageScaleType.EXACTLY)
		.build();
		
		
		// TASKS
		mFetchMessagesChatTaskMethod = new FetchMessagesChatTaskMethod(getSherlockActivity());
		mMessageTaskMethod = new MessageTaskMethod(getSherlockActivity());
		mUnblockTaskMethod = new UnblockTaskMethod(getSherlockActivity());
		mProfilesTaskMethod = new ProfilesTaskMethod(getSherlockActivity());
		
        // LISTENERS
        mFetchMessagesChatTaskMethod.addFetchMessagesChatTaskListener(this);
        mMessageTaskMethod.addMessageTaskListener(this);
        mUnblockTaskMethod.addUnblockTaskListener(this);
        mProfilesTaskMethod.addProfilesTaskListener(this);
        
        
		// LIST
		
        mAnimation = AnimationUtils.loadAnimation(getSherlockActivity(), R.anim.fadein);
		mMessages = new ArrayList<Message>();
		mAdapter = new MessagesAdapter(getSherlockActivity(), R.layout.row_signal_chat, mMessages);
		setListAdapter(this.mAdapter);
		
		
		
		// WIGETS
		
        mMsgEt = (EditText)view.findViewById(R.id.msg_edit_text);
        mSendBtn = (Button)view.findViewById(R.id.send_btn);
        mSendBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickSend();
            }}
        );
        
        setHasOptionsMenu(true);
        
        InputMethodManager imm = (InputMethodManager)getSherlockActivity().getSystemService(MainActivity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mMsgEt.getWindowToken(), 0);
        getSherlockActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); 
        
		return view;
	}

	
    @Override
    public void onResume() {
        super.onResume();
		
        fetchMessages();
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
        
        // LISTENERS
        mFetchMessagesChatTaskMethod.removeFetchMessagesChatTaskListener(this);
        mMessageTaskMethod.removeMessageTaskListener(this);
        mProfilesTaskMethod.removeProfilesTaskListener(this);
        
        mFetchMessagesChatTaskMethod.cleanUp();
        mMessageTaskMethod.cleanUp();
        mProfilesTaskMethod.cleanUp();
    }
    
    
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Add your menu entries here
        super.onCreateOptionsMenu(menu, inflater);
        
        menu.add(0,0,ACTION_REFRESH,getString(R.string.refresh))
        .setIcon( R.drawable.ic_action_refresh)
        .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getOrder())
        {
            case ACTION_REFRESH:
                fetchMessages();
                return true;
        }
        
        return false;
    }
    
    
    
    private void fetchMessages()
    {
    	mFetchMessagesChatTaskMethod.doTask(User.getInstance().getUserId(), mPerson);
    }
    
    private void updateButtons()
    {
        Place personPlace = mPerson.getCheckPlace();
        Place userPlace = User.getInstance().getCheckPlace();
        
        if( personPlace == null || userPlace == null || !personPlace.getPlaceId().equals(userPlace.getPlaceId()) || mPerson.dontApproach() || mPerson.blocked() )
        {
            mMsgEt.setEnabled(false);
            mSendBtn.setBackground(getResources().getDrawable(R.drawable.btn_grey));
        }
        else
        {
            mMsgEt.setEnabled(true);
            mSendBtn.setBackground(getResources().getDrawable(R.drawable.btn_green));
            
        }
    }
    
    
	private void updateView( ArrayList<Message> messages )
	{
	    updateButtons();
	    
		mMessages.clear();
		
	    for( Message m : messages)
	    	mMessages.add(m);
	    
		Collections.sort(mMessages, new Message.SignalChatComparator());

		mAdapter.notifyDataSetChanged();
		this.getListView().startAnimation(mAnimation); 
	}
	
    
 
    
	private class MessagesAdapter extends ArrayAdapter<Message> {
		 
	    private ArrayList<Message> items = new ArrayList<Message>();

	    public MessagesAdapter(Context context, int textViewResourceId, ArrayList<Message> items) {
	            super(context, textViewResourceId, items);
	            this.items = items;
	    }

	    @Override
	    public View getView(int position, View convertView, ViewGroup parent) {
	            View v = convertView;
	            Message sig = items.get(position);
                final Person p = sig.incoming() ? sig.getPerson() : User.getInstance(); 
	            
	            if( v == null )
	            {
    	            LayoutInflater vi = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    v = vi.inflate(R.layout.row_signal_chat, null);
                    
                    ImageView profileIv = (ImageView)v.findViewById(R.id.profile_iv);
                    profileIv.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View arg0) {
                            onClickPhoto(p);
                        }}
                    );
	            }   
	            v.invalidate();
	            
	            // Adjust view
                ImageView triangleIv = (ImageView)v.findViewById(R.id.triangle_iv);
                ImageView profileIv = (ImageView)v.findViewById(R.id.profile_iv);
                LinearLayout bubbleLayout = (LinearLayout)v.findViewById(R.id.bubble_layout);
                TextView placeTv = (TextView)v.findViewById(R.id.place_time_tv);
                TextView signalTv = (TextView)v.findViewById(R.id.msg_tv);
                
                
                RelativeLayout.LayoutParams profileIvParams = (RelativeLayout.LayoutParams)profileIv.getLayoutParams();
                RelativeLayout.LayoutParams triangleIvParams = (RelativeLayout.LayoutParams)triangleIv.getLayoutParams();
                RelativeLayout.LayoutParams bubbleIvParams = (RelativeLayout.LayoutParams)bubbleLayout.getLayoutParams();
                
                
                if( sig.incoming() )
                {
                    profileIvParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,0);
                    profileIvParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                    profileIvParams.setMargins(0, 0, (int)Utility.convertDpToPixel(10, getSherlockActivity()), 0);
                    profileIv.setLayoutParams(profileIvParams);
                    
                    triangleIv.setImageResource(R.drawable.full_triangle1);
                    triangleIvParams.addRule(RelativeLayout.LEFT_OF, 0);
                    triangleIvParams.addRule(RelativeLayout.RIGHT_OF, profileIv.getId());
                    triangleIv.setLayoutParams(triangleIvParams);
                    
                    bubbleIvParams.addRule(RelativeLayout.LEFT_OF, 0);
                    bubbleIvParams.addRule(RelativeLayout.RIGHT_OF, triangleIv.getId());
                    bubbleLayout.setLayoutParams(bubbleIvParams);
                    
                }
                else
                {
                    profileIvParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT,0);
                    profileIvParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    profileIvParams.setMargins((int)Utility.convertDpToPixel(10, getSherlockActivity()), 0, 0, 0);
                    profileIv.setLayoutParams(profileIvParams);
                    
                    triangleIv.setImageResource(R.drawable.full_triangle2);
                    triangleIvParams.addRule(RelativeLayout.RIGHT_OF,0);
                    triangleIvParams.addRule(RelativeLayout.LEFT_OF, profileIv.getId());
                    triangleIv.setLayoutParams(triangleIvParams);
                    
                    bubbleIvParams.addRule(RelativeLayout.RIGHT_OF, 0);
                    bubbleIvParams.addRule(RelativeLayout.LEFT_OF, triangleIv.getId());
                    bubbleLayout.setLayoutParams(bubbleIvParams);
                }
                
                v.setEnabled(false);
	            
	            Resources r = getResources();
	           	int day = 0;
	           	int lastDay = 0;
	           	int month = 0;
	           	int lastMonth = 0;
	           	int year = 0;
	           	int lastYear = 0;
	           	
	           	
	           	Message lastSig;
	           	
	           	if( position > 0)
	           	{ 
	           		lastSig = items.get(position-1);
	            
		           	day = sig.getCreateTime().get(Calendar.DAY_OF_MONTH);
		           	lastDay = lastSig.getCreateTime().get(Calendar.DAY_OF_MONTH);
		           	
		           	month = sig.getCreateTime().get(Calendar.MONTH);
		           	lastMonth = lastSig.getCreateTime().get(Calendar.MONTH);
		           	
		           	year = sig.getCreateTime().get(Calendar.YEAR);
		           	lastYear = lastSig.getCreateTime().get(Calendar.YEAR);
		           	
	           	}
	           	
            	TextView separatorText = (TextView)v.findViewById(R.id.separator);
            	
            	if( position == 0 || day != lastDay || month != lastMonth || year != lastYear )
            	{
            		separatorText.setVisibility(View.VISIBLE);
            		
            		String dayStr = "";
            		String monthStr = "";
            		
            		int dayOfWeek = sig.getCreateTime().get(Calendar.DAY_OF_WEEK); 
            		int monthNb = sig.getCreateTime().get(Calendar.MONTH);
            		
            		switch( dayOfWeek )
            		{
        			case 1:
        				dayStr = getResources().getString(R.string.sunday);
        				break;
        			case 2:
        				dayStr = getResources().getString(R.string.monday);
        				break;
        			case 3:
        				dayStr = getResources().getString(R.string.tuesday);
        				break;
        			case 4:
        				dayStr = getResources().getString(R.string.wednesday);
        				break;
        			case 5:
        				dayStr = getResources().getString(R.string.thursday);
        				break;
        			case 6:
        				dayStr = getResources().getString(R.string.friday);
        				break;
        			case 7:
        				dayStr = getResources().getString(R.string.saturday);
        				break;

            		}
            		
            		switch( monthNb )
            		{
            			case 0:
            				monthStr = getResources().getString(R.string.january);
            				break;
            			case 1:
            				monthStr = getResources().getString(R.string.february);
            				break;
            			case 2:
            				monthStr = getResources().getString(R.string.march);
            				break;
            			case 3:
            				monthStr = getResources().getString(R.string.april);
            				break;
            			case 4:
            				monthStr = getResources().getString(R.string.may);
            				break;  
            			case 5:
            				monthStr = getResources().getString(R.string.june);
            				break; 
            			case 6:
            				monthStr = getResources().getString(R.string.july);
            				break;
            			case 7:
            				monthStr = getResources().getString(R.string.august);
            				break;
            			case 8:
            				monthStr = getResources().getString(R.string.september);
            				break;
            			case 9:
            				monthStr = getResources().getString(R.string.october);
            				break;
            			case 10:
            				monthStr = getResources().getString(R.string.november);  
            				break;
            			case 11:
            				monthStr = getResources().getString(R.string.december);
            				break;
            		}
            		
            		separatorText.setText(dayStr + ", " + sig.getCreateTime().get(Calendar.DAY_OF_MONTH) + " " + monthStr + " " + sig.getCreateTime().get(Calendar.YEAR));

        			final float scale = getResources().getDisplayMetrics().density;
            		int padding = (int) (10 * scale + 0.5f);
	            	separatorText.setPadding(0,padding,0,0);

            	}
            	else
            		separatorText.setVisibility(View.GONE);
            	
            	
            	// PIC
    			ImageLoader.getInstance().displayImage("http://s3.amazonaws.com/signals/user_images/" + p.getUserId() + "/profilePhotoThumb.jpg", profileIv, mImageOptions);
    			//profileIv.startAnimation(mAnimation); 
    			
        				
        		// PLACE  		
        		
        		DecimalFormat nft = new  java.text.DecimalFormat("#00");  
        		nft.setDecimalSeparatorAlwaysShown(false); 
        		
        		String text = sig.getPlace().getName() + ", " + sig.getCreateTime().get(Calendar.HOUR) + ":" + nft.format(sig.getCreateTime().get(Calendar.MINUTE)) + " ";
    			if (sig.getCreateTime().get(Calendar.AM_PM) == Calendar.PM) 
    				text += r.getString(R.string.pm);
    			else
    				text += r.getString(R.string.am);
    			
        		placeTv.setText(text );
        		
        		
        				
         		
        		// SIGNAL
    			signalTv.setText(sig.getMessage());
    			
    			
        		switch( sig.getType() )
        		{
        			
        		case Message.MESSAGE:
        			Message msg = (Message)sig;
        			signalTv.setText(msg.getMessage());
        			break;
        			
        		}
        		
        		
        		
	            return v;
	    }
	    
	    @Override
	    public int getCount() {
	        
	        return items.size();
	    }
	    
	    
	    @Override
	    public boolean isEnabled(int position)
	    {
	    	return false;
	    } 
	}
	
	
	private boolean msgIsEmpty(EditText etText) 
	{
	    return etText.getText().toString().trim().length() == 0;
	}
	
	
	
	// EVENTS
	
	private void onClickSend()
	{
	    final User u = User.getInstance();
	    
	    final Place userPlace = u.getCheckPlace();
	    final Place personPlace = mPerson.getCheckPlace();
	    
	    if( userPlace == null || personPlace == null || !personPlace.getPlaceId().equals(userPlace.getPlaceId()) )
	    {
	        Toast.makeText(getSherlockActivity(), getString(R.string.error_you_have_to_be_in_same_bar), Toast.LENGTH_LONG).show();
	    }
	    else if( mPerson.dontApproach() )
	    {
	        Toast.makeText(getSherlockActivity(), mPerson.getUsername() + " " + getString(R.string.error_dont_bother), Toast.LENGTH_LONG).show();
	    }
	    else if( mPerson.blocked() )
	    {
	        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
            
            alertDialogBuilder.setTitle(getActivity().getString(R.string.blocked_user));
 
            alertDialogBuilder
                .setMessage(getActivity().getString(R.string.error_blocked_user))
                .setCancelable(false)
                .setPositiveButton(getActivity().getString(R.string.unblock),new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {

                        mUnblockTaskMethod.doTask(u.getUserId(), mPerson.getUserId());
                    }
                  })
                .setNegativeButton(getActivity().getString(R.string.cancel),new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.cancel();
                    }
                });
 
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
	    }
	    else
	    {
    	    if( userPlace != null && !msgIsEmpty( mMsgEt )  )
    	    {
    	        int nbMsgs = mMessages.size();
    	         
    	        if( nbMsgs > 1 && !mMessages.get(nbMsgs-1).incoming() && !mMessages.get(nbMsgs-2).incoming() )
    	        {       
    	            Toast.makeText(getSherlockActivity(), getString(R.string.error_wait_for_a_reply1) + " " + mPerson.getUsername() + " " + getString(R.string.error_wait_for_a_reply2), Toast.LENGTH_LONG).show();
    	        }
    	        else 
    	        {
    	            mMessageTaskMethod.doTask(User.getInstance().getUserId(), mPerson.getUserId(), User.getInstance().getCheckPlace().getPlaceId(), mMsgEt.getText().toString());    
    	        }
    	        
                InputMethodManager imm = (InputMethodManager)getSherlockActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mMsgEt.getWindowToken(), 0);
    	    }
    	    

	    }
	}

	private void onClickPhoto( Person p )
	{
	    ArrayList<Long> peopleIds = new ArrayList<Long>();
	    peopleIds.add(p.getUserId());
	    mProfilesTaskMethod.doTask(User.getInstance().getUserId(), peopleIds);
	}
	
	
	
	@Override
	public void fetchMessagesChatTaskEventReceived(FetchMessagesChatTaskEvent evt) 
	{
		FetchMessagesChatTaskMethod met = (FetchMessagesChatTaskMethod)evt.getSource();
		
		if( met != null && met.success() )
		{
            mPerson.setCheckPlace(met.getCheckPlace());
		    
		    
			int nbNewMessages = User.getInstance().getNbNewMessages() - met.getNbNewMessages();
			
			if( nbNewMessages > 0 )
				User.getInstance().setNbNewMessages(nbNewMessages);
			else
				User.getInstance().setNbNewMessages(0);	
						
			updateView(met.getMessages());
		}
		
	}


    @Override
    public void messageTaskEventReceived(MessageTaskEvent evt) 
    {
        MessageTaskMethod met = (MessageTaskMethod)evt.getSource();
        
        
        if( met != null && met.success() )
        {
            mMsgEt.getText().clear();
            fetchMessages();
        }
        else
        {
            int error = met.getError();
            
            switch( error )
            {
                case MessageTaskMethod.ERROR_MESSAGE_PERSON_CHECKED_OUT:
                    Place place = mPerson.getCheckPlace();
                    
                    if( place != null )
                        Toast.makeText(getSherlockActivity(), mPerson.getUsername() + " " + getString(R.string.has_checked_out_from) + " " + mPerson.getCheckPlace().getName(), Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(getSherlockActivity(), mPerson.getUsername() + " " + getString(R.string.has_checked_out), Toast.LENGTH_LONG).show();
                    
                    mPerson.setCheckPlace(null);
                    break;
                    
                case MessageTaskMethod.ERROR_MESSAGE_PERSON_DONT_APPROACH:
                    Toast.makeText(getSherlockActivity(), mPerson.getUsername() + " " + getString(R.string.error_dont_bother), Toast.LENGTH_LONG).show();
                    mPerson.setDontApproach(true);
                    break;
                
                case MessageTaskMethod.ERROR_MESSAGE_PERSON_BLOCKED_YOU:
                    
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                    alertDialogBuilder.setTitle(getActivity().getString(R.string.user_unavailable));
         
                    alertDialogBuilder
                        .setMessage(mPerson.getUsername() + " " + getString(R.string.is_unavailable))
                        .setCancelable(false)
                        .setPositiveButton(getActivity().getString(R.string.ok),new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                mPerson.setDontApproach(true);
                                getActivity().getSupportFragmentManager().popBackStack();
                                getActivity().getSupportFragmentManager().beginTransaction().remove(MessagesChatFragment.this).commit();
                            }
                          });
         
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                    break;
            }
            
            updateButtons();
            
            
        }
        
    }


    @Override
    public void unblockTaskEventReceived(UnblockTaskEvent evt) {
        UnblockTaskMethod met = (UnblockTaskMethod)evt.getSource();
        
        if( met != null && met.success() )
        {
            
            mPerson.setBlocked(false);
            
            Toast.makeText(getSherlockActivity(), getString(R.string.you_unblocked) + " " + mPerson.getUsername() + ".", Toast.LENGTH_LONG).show();
            updateButtons();
        }   
        
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
