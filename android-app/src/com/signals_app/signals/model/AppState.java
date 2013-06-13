package com.signals_app.signals.model;

import java.util.ArrayList;

import com.facebook.android.Facebook;

public class AppState 
{
    public static final String FACEBOOK_APP_ID = "433704539998488";  
    
	private static AppState mInstance = null;
    private int mCurrentTab = 0;
    private Facebook mFacebook;
    private boolean mNetworkAvailable = false;


	
	protected AppState()
	{
	}
	
	public static AppState getInstance() 
	{
		if(mInstance == null) {
			mInstance = new AppState();
			
			mInstance.mFacebook = new Facebook(FACEBOOK_APP_ID);
         
		}
		
		return mInstance;
	}
   	
   	public int getCurrentTab() {
		return mCurrentTab;
   	}

   	public void setCurrentTab(int currentTab) {
   		this.mCurrentTab = currentTab;
   	}

	   
   	public void setNetworkAvailable( boolean networkAvailable )
   	{
   		mNetworkAvailable = networkAvailable;
   	}
   	
   	
   	public boolean networkAvailable()
   	{
   		return mNetworkAvailable;
   	}
	 

	
	public Facebook getFacebook() {
		return mFacebook;
	}
}
