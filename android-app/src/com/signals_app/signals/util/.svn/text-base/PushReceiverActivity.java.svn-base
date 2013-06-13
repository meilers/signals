package com.signals_app.signals.util;

import java.util.EventObject;
import java.util.concurrent.CopyOnWriteArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.signals_app.signals.R;
import com.signals_app.signals.model.Signals.Message;
import com.signals_app.signals.model.Signals.Signal;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.widget.Toast;

import android.support.v4.app.FragmentActivity;

public class PushReceiverActivity extends SherlockFragmentActivity 
{
	static final String ACTION = "MyAction";
	

	public static CopyOnWriteArrayList<PushListener> mPushListeners = new CopyOnWriteArrayList<PushListener>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	
	    IntentFilter filter = new IntentFilter(ACTION);
	    this.registerReceiver(mPushReceiver, filter);
	}

	private void displayAlert(Intent intent)
	{
		Bundle extras = intent.getExtras();
		String message = extras != null ? extras.getString("com.parse.Data") : "";
		JSONObject jObject;
		  
		try {
			jObject = new JSONObject(message);
		        
			int code = jObject.getInt("messCode"); 
		    
		    Bundle b  = new Bundle();
		    FragmentManager manager = this.getSupportFragmentManager();
		    
		    switch(code)
		    {
		        	
				case Signal.FLIRT:
		            
					break;
				case Signal.MESSAGE:
					break;
					
				case Signal.VOTE:
					break;
		    }
	                    
	        
	        firePushEvent();
	    } catch (JSONException e) {
	            e.printStackTrace();
	    }
	}
	
	static public void addPushListener(PushListener l) 
	{
		PushReceiverActivity.mPushListeners.add(l);
	}

	static public void removePushListener(PushListener l) 
	{
		PushReceiverActivity.mPushListeners.remove(l);
	}
	
	public void firePushEvent() {
		PushEvent evt = new PushEvent(this);

	    for (PushListener l : mPushListeners) 
    	{
	    	if( l != null)
	    		l.pushEventReceived(evt);
	    }
	}
	
	public class PushEvent extends EventObject {
		private static final long serialVersionUID = 0;
				
		  // This event definition is stateless but you could always
		  // add other information here.
		  public PushEvent(Object source) {
		    super(source);
		  }
		}
	
	public interface PushListener {
		void pushEventReceived(PushEvent evt);
	}


	private final BroadcastReceiver mPushReceiver = new BroadcastReceiver() {
		
		
		@Override
	    public void onReceive(Context context, Intent intent) 
		{
	        String action = intent.getAction();

	        if (ACTION.equals(action)) 
	        {
	        	displayAlert(intent);
	        }
	    }
		
	};
}
