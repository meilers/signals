package com.signals_app.signals.util;

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
import com.signals_app.signals.activity.PushReceiverActivity;

public class PushReceiver extends BroadcastReceiver {
	
	
	@Override
    public void onReceive(Context context, Intent intent) {
		
		Intent newIntent = new Intent(context, PushReceiverActivity.class);
		newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(newIntent);
    }

}
