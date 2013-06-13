package com.signals_app.signals.tasks;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.signals_app.signals.model.Constants;
import com.signals_app.signals.model.Profile.Person;
import com.signals_app.signals.util.Utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.EventObject;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;


public class PlaceCheckinTaskMethod
{
	private CopyOnWriteArrayList<PlaceCheckinTaskListener> mPlaceCheckinTaskListeners;
	
	private SherlockFragmentActivity mAct = null;;
	private PlaceCheckinTask mPlaceCheckinTask = null;
	
	public PlaceCheckinTaskMethod(SherlockFragmentActivity act)
	{
		mAct = act;
		mPlaceCheckinTaskListeners = new CopyOnWriteArrayList<PlaceCheckinTaskListener>();
		mPlaceCheckinTask = new PlaceCheckinTask();
	}
	
	
	public void doTask( Long userId, Long placeId )
	{
    	mPlaceCheckinTask = new PlaceCheckinTask();
    	mPlaceCheckinTask.mActivity = mAct;
    	mPlaceCheckinTask.mMethod = this;
    	
    	MyParams params = new MyParams();
    	params.mUserId = userId;
    	params.mPlaceId = placeId;
    	
    	mPlaceCheckinTask.execute(params);
	}
	
	public void cleanUp()
	{
		mAct.setSupportProgressBarIndeterminateVisibility(false);
        mPlaceCheckinTask.mActivity = null;
        
        if (mAct.isFinishing())
        	mPlaceCheckinTask.cancel(false);
	}
	
	class MyParams
	{
		Long mUserId; 
		Long mPlaceId;
	} 
	
    static class PlaceCheckinTask extends AsyncTask<MyParams, Void, String>
    {
        @Override
        protected String doInBackground(MyParams... params)  
        {
		    HttpClient httpclient = new DefaultHttpClient();
		    HttpPost httppost = new HttpPost("http://ec2-107-22-191-241.compute-1.amazonaws.com/checkInPlace.php");

		    try {
		        
		    	JSONObject json = new JSONObject();
		    
		    	json.put(Constants.USER_ID, params[0].mUserId);
		    	json.put(Constants.PLACE_ID, params[0].mPlaceId);
		    	
		    	StringEntity se = new StringEntity(json.toString());
	            se.setContentType("application/json;charset=UTF-8");
	            httppost.setEntity(se);

		        // Execute HTTP Post Request
		        HttpResponse response = httpclient.execute(httppost);
				InputStream inputStream = response.getEntity().getContent();
				InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
				BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
				StringBuilder stringBuilder = new StringBuilder();
				String bufferedStrChunk = null;

				while((bufferedStrChunk = bufferedReader.readLine()) != null){
					stringBuilder.append(bufferedStrChunk);
				}
				
				return stringBuilder.toString();
		        
		    } 
		    catch (JSONException e) 
	        {
	            Log.e("JSON Parser", "Error parsing data " + e.toString());
	        }
		    catch (ClientProtocolException e) {
		        // TODO Auto-generated catch block
		    } catch (IOException e) {
		    }

	    	return "";
        }
        
        
        @Override
        protected void onPreExecute() {
            mActivity.setSupportProgressBarIndeterminateVisibility(true);
        }
        
        
 
		@Override
		protected void onPostExecute(String result) 
		{
			super.onPostExecute(result);
			
			JSONObject json;
			mSuccess = false;
			
	        try  
	        {
	        	json = new JSONObject(result);
	         
	            if(json.getInt(Constants.RESULT_OK) != 0)
	            {
	            	mSuccess = true;
	            }
	        }
	        catch (JSONException e) 
	        {
	            Log.e("JSON Parser", "Error parsing data " + e.toString());
	        }
	        
	        updateUI();
		}			

        public void updateUI()
        {
            if (mActivity != null && mMethod != null)
            {
                mActivity.setSupportProgressBarIndeterminateVisibility(false);
            	mMethod.firePlaceCheckinTaskEvent();
            }            
        }
        

        
        // These should never be accessed from within doInBackground()
        PlaceCheckinTaskMethod mMethod = null;
        SherlockFragmentActivity mActivity = null;
              
        boolean mSuccess = false;
    }
    
	// RESULTS
	public boolean success()
	{
		return mPlaceCheckinTask.mSuccess;
	}
	
    
    
	// EVENTS
	public void addPlaceCheckinTaskListener(PlaceCheckinTaskListener l) 
	{
		this.mPlaceCheckinTaskListeners.add(l);
	}

	public void removePlaceCheckinTaskListener(PlaceCheckinTaskListener l) 
	{
	    this.mPlaceCheckinTaskListeners.remove(l);
	}
	
	public void firePlaceCheckinTaskEvent() {
		PlaceCheckinTaskEvent evt = new PlaceCheckinTaskEvent(this);

	    for (PlaceCheckinTaskListener l : mPlaceCheckinTaskListeners) 
    	{
	    	if( l != null)
	    		l.placeCheckinTaskEventReceived(evt);
	    }
	}
	
	public class PlaceCheckinTaskEvent extends EventObject {
		private static final long serialVersionUID = 0;
		  public PlaceCheckinTaskEvent(Object source) {
		    super(source);
		  }
		}
	
	public interface PlaceCheckinTaskListener {
		void placeCheckinTaskEventReceived(PlaceCheckinTaskEvent evt);
	}
}

