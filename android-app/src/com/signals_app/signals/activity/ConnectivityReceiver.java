package com.signals_app.signals.activity;

import com.signals_app.signals.model.AppState;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class ConnectivityReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) 
	{
	    Log.d(ConnectivityReceiver.class.getSimpleName(), "action: " + intent.getAction());
	    
	    AppState.getInstance().setNetworkAvailable(hasNetworkConnection(context));
	} 
	
	
    private boolean hasNetworkConnection(Context context) 
    {
	    boolean hasConnectedWifi = false;
	    boolean hasConnectedMobile = false;
	
	    ConnectivityManager cm = (ConnectivityManager)   context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo[] netInfo = cm.getAllNetworkInfo();
	    
	    for (NetworkInfo ni : netInfo) {
	        if (ni.getTypeName().equalsIgnoreCase("WIFI"))
	            if (ni.isConnected())
	                hasConnectedWifi = true;
	        if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
	            if (ni.isConnected())
	                hasConnectedMobile = true;
	    }
	    
	    return hasConnectedWifi || hasConnectedMobile;
    }
}
