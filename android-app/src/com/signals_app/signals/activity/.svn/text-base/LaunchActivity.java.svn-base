package com.signals_app.signals.activity;

import com.signals_app.signals.model.Profile.User;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class LaunchActivity extends Activity 
{
    @Override
    protected void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);
        

    }
    
    @Override
    public void onStart() {
        super.onStart();
        
        final Class<? extends Activity> activityClass;
        if(User.getInstance().loggedIn())
            activityClass = MainActivity.class;
        else
            activityClass = IntroActivity.class;
 
        Intent newActivity = new Intent(LaunchActivity.this, activityClass);
        startActivity(newActivity);
    }
}
