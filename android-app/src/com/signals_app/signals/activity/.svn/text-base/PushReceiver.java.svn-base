package com.signals_app.signals.activity;

import java.util.EventObject;
import java.util.concurrent.CopyOnWriteArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.signals_app.signals.R;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class PushReceiver extends BroadcastReceiver {
	
	public static CopyOnWriteArrayList<PushListener> mPushListeners = new CopyOnWriteArrayList<PushListener>();
	
	@Override
    public void onReceive(Context context, Intent intent) {
		Bundle extras = intent.getExtras();
		String message = extras != null ? extras.getString("com.parse.Data") : "";
		JSONObject jObject;
		
		try {
			jObject = new JSONObject(message);
		        
			int code = jObject.getInt("messCode");
		    String mess = "";
		    
		    switch(code)
		    {
		        case 1:
		        	break;
		        	
		        case 2:
		        	break;
		        	
		        case 3:
		        	break;
		    }
	                    
	        Toast toast = Toast.makeText(context, jObject.getString("username") + " " + mess + "!", Toast.LENGTH_LONG);
	        toast.show();
	        
	        firePushEvent();
	    } catch (JSONException e) {
	            e.printStackTrace();
	    }
    }
	
	
	static public void addPushListener(PushListener l) 
	{
		PushReceiver.mPushListeners.add(l);
	}

	static public void removePushListener(PushListener l) 
	{
		PushReceiver.mPushListeners.remove(l);
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
}
