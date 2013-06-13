package com.signals_app.signals.activity;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.signals_app.signals.R;

import com.signals_app.signals.model.Profile.Person;
import com.signals_app.signals.model.Profile.User;
import com.signals_app.signals.model.Signals.Message;
import com.signals_app.signals.tasks.signals.FetchMessagesTaskMethod;
import com.signals_app.signals.tasks.signals.FetchMessagesTaskMethod.FetchMessagesTaskEvent;
import com.signals_app.signals.util.PushReceiverActivity;
import com.signals_app.signals.util.PushReceiverActivity.PushEvent;
import com.signals_app.signals.util.PushReceiverActivity.PushListener;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class MessagesFragment extends SherlockListFragment implements FetchMessagesTaskMethod.FetchMessagesTaskListener, PushListener
{
    public static final int ACTION_REFRESH = 1;
    
    
	private DisplayImageOptions mImageOptions;
	
	
	ListView mPullToRefreshView;
    private MessagesAdapter mAdapter;
	private ArrayList<Message> mMessages = null;
	
	FetchMessagesTaskMethod mFetchMessagesTaskMethod;
	
	private EditText mMsgEditText; 
	
	
	
  	public static final MessagesFragment newInstance()
  	{
  		MessagesFragment f = new MessagesFragment();
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
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		View view = inflater.inflate(R.layout.fragment_messages, container, false);
		getSherlockActivity().setTitle(getSherlockActivity().getResources().getString(R.string.messages));
		
		if( User.getInstance().checkedIn() )
            getSherlockActivity().getSupportActionBar().setSubtitle(getString(R.string.tonight) + " " + getString(R.string.at) + " " + User.getInstance().getCheckPlace().getName());
        else
            getSherlockActivity().getSupportActionBar().setSubtitle(null); 
		
		
		// IMAGE LOADER
		mImageOptions = new DisplayImageOptions.Builder()
		.cacheInMemory()
		.cacheOnDisc()
		.build();
		
		
		
		// TASKS
		mFetchMessagesTaskMethod = new FetchMessagesTaskMethod(getSherlockActivity());
		
		

		
		
        
		
		// LIST
		
		mPullToRefreshView = (ListView) view.findViewById(R.id.listview);
		
		mMessages = new ArrayList<Message>();
		
		mAdapter = new MessagesAdapter(this.getActivity(), R.layout.row_signal, mMessages);
		setListAdapter(this.mAdapter);
		
		
		setHasOptionsMenu(true);
		
		return view;
	}

    @Override
    public void onResume() {
        super.onResume();

		// LISTENERS
		PushReceiverActivity.addPushListener(this);
		mFetchMessagesTaskMethod.addFetchMessagesTaskListener(this);
		
		
        fetchMessages();
    }
    
    
    @Override
    public void onPause() {
        super.onPause();
        
        // LISTENERS
        PushReceiverActivity.removePushListener(this);
        mFetchMessagesTaskMethod.removeFetchMessagesTaskListener(this);
    }

    
    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        
        mFetchMessagesTaskMethod.cleanUp();
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
    	mFetchMessagesTaskMethod.doTask(User.getInstance().getUserId());
    }
     
	private void updateView( ArrayList<Message> messages )
	{
		mMessages.clear();
		
	    for( Message m : messages)
	    	mMessages.add(m);
	    
		Collections.sort(mMessages, new Message.SignalComparator());

		mAdapter.notifyDataSetChanged();
	}
	
	
	private class MessagesAdapter extends ArrayAdapter<Message> {
 
	    private final ArrayList<Message> items;

	    public MessagesAdapter(Context context, int textViewResourceId, ArrayList<Message> items) {
	            super(context, textViewResourceId, items);
	            this.items = items;
	    }

	    @Override
	    public View getView(int position, View convertView, ViewGroup parent) {
	            View v = convertView;
	            if (v == null) {
	                LayoutInflater vi = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	                v = vi.inflate(R.layout.row_message, parent, false);
	            }
	            
	            Resources r = getSherlockActivity().getResources();
	           	Message msg = items.get(position);
	           	Calendar today = Calendar.getInstance();
	           	today.setTime(new Date());
	           	
	            if (msg != null) 
	            {
	            	// PIC
	             	Person p = msg.getPerson(); 
	            	ImageView imageView = (ImageView)v.findViewById(R.id.profile_pic);
	    			Animation fadeInAnimation = AnimationUtils.loadAnimation(getSherlockActivity(), R.anim.fadein);
	    			ImageLoader.getInstance().displayImage("http://s3.amazonaws.com/signals/user_images/" + p.getUserId() + "/profilePhotoThumb.jpg", imageView, mImageOptions);
	    			imageView.startAnimation(fadeInAnimation); 
	    			
	        		
	        				
	        		// PLACE
	        		TextView usernameText = (TextView)v.findViewById(R.id.username_text);	        		
	        		usernameText.setText(p.getUsername() ); 
	        		
	        		
	        		// DATE
	        		TextView dateText = (TextView)v.findViewById(R.id.date_text);
	        	    
	        		DecimalFormat nft = new  java.text.DecimalFormat("#00");  
                    nft.setDecimalSeparatorAlwaysShown(false); 
                    
                    String text = msg.getCreateTime().get(Calendar.HOUR) + ":" + nft.format(msg.getCreateTime().get(Calendar.MINUTE)) + " ";
                    if (msg.getCreateTime().get(Calendar.AM_PM) == Calendar.PM) 
                        text += r.getString(R.string.pm);
                    else
                        text += r.getString(R.string.am);
                    
                    dateText.setText(msg.getPlace().getName() + ", " + text);
	        		
	        		
	        		// SIGNAL
	        		ImageView statusImg = (ImageView)v.findViewById(R.id.status_img);
	        		TextView signalText = (TextView)v.findViewById(R.id.signal_text);
	        		signalText.setText(msg.getMessage());
	        		
	        		if( !msg.incoming() )
	        		{
	        			// status
	        			statusImg.setVisibility(View.VISIBLE);
	        			
	        			if( !msg.seen() )
	        				statusImg.setImageDrawable(r.getDrawable(R.drawable.reply));
	        			else
	        				statusImg.setImageDrawable(r.getDrawable(R.drawable.checkmark));
	        		}
	        		else
	        		{
	        			// status
	        			statusImg.setVisibility(View.GONE);
	        		}
	        		
	        		
	                // STAR
	                ImageView starIv = (ImageView)v.findViewById(R.id.star_iv);
	                
	                if( p.star() )
	                    starIv.setVisibility(View.VISIBLE);
	                else
	                    starIv.setVisibility(View.INVISIBLE);
	            }
	            return v;
	    }
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id)
	{
		super.onListItemClick(l, v, position, id);

		Message sig = mMessages.get(position);
		Person p = sig.getPerson();
		
    	MessagesChatFragment frag =  MessagesChatFragment.newInstance(p);
    	
		getSherlockActivity().getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.content_frame, frag)
		.addToBackStack(null)
		.commit();
	}

	
	
	


	public void addEmoticonToMessage( final int index )
	{
		/*
		TypedArray imgs = getResources().obtainTypedArray(R.array.emoticons);
		final Drawable d = getResources().getDrawable(imgs.getResourceId(index, -1));
		
		ImageGetter imageGetter = new ImageGetter() 
		{
		    public Drawable getDrawable(String source) {
		        d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
		        return d;
		    }
		};
		
		Spanned cs = Html.fromHtml("<img src='" + d + "'/>", imageGetter, null);
		
		mMsgEditText.setText(cs);
		*/
		String[] emoCodes = getResources().getStringArray(R.array.emoticons_codes);
		
		if( index < emoCodes.length )
			mMsgEditText.append( " " + emoCodes[index] + " ");
		
		return;
	}
	
	
	
	// EVENTS
	


	
    
	@Override
	public void fetchMessagesTaskEventReceived(FetchMessagesTaskEvent evt) {
		FetchMessagesTaskMethod met = (FetchMessagesTaskMethod)evt.getSource();
		
		if( met != null && met.success() )
		{
			User.getInstance().setNbNewMessages(met.getNbNewMessages());
			
			if( met.getNbNewMessages() > 0 )
				getSherlockActivity().setTitle(getResources().getString(R.string.messages) + " (" + met.getNbNewMessages() + ")");
			else
				getSherlockActivity().setTitle(getResources().getString(R.string.messages));
			
			updateView(met.getMessages());
		}
		
	}
	
	public void pushEventReceived(PushEvent evt)
	{
	}











}
