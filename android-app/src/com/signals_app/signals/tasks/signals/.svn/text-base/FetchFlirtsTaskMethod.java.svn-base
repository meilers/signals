package com.signals_app.signals.tasks.signals;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.signals_app.signals.model.Constants;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.EventObject;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.TimeZone;
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

import android.app.AlertDialog;
import android.os.AsyncTask;
import android.util.Log;
import com.signals_app.signals.model.Place;
import com.signals_app.signals.model.Profile.GpsPosition;
import com.signals_app.signals.model.Profile.Person;
import com.signals_app.signals.model.Signals.Flirt;
import com.signals_app.signals.model.Signals.Signal;
import com.signals_app.signals.util.Utility;


public class FetchFlirtsTaskMethod
{
	private CopyOnWriteArrayList<FetchFlirtsTaskListener> mFetchFlirtsTaskListeners;
	
	private SherlockFragmentActivity mAct = null;;
	private FetchFlirtsTask mFetchFlirtsTask = null;
	
	public FetchFlirtsTaskMethod(SherlockFragmentActivity act)
	{
		mAct = act;
		mFetchFlirtsTaskListeners = new CopyOnWriteArrayList<FetchFlirtsTaskListener>();
		mFetchFlirtsTask = new FetchFlirtsTask();
	}
	
	 
	public void doTask(Long userId)
	{
    	mFetchFlirtsTask = new FetchFlirtsTask();
    	mFetchFlirtsTask.mActivity = mAct;
    	mFetchFlirtsTask.mMethod = this;
    	
    	MyParams params = new MyParams();
    	params.mUserId = userId;
    	mFetchFlirtsTask.execute(params);
	}
	
	public void cleanUp()
	{
		mAct.setSupportProgressBarIndeterminateVisibility(false);
        mFetchFlirtsTask.mActivity = null;
        
        if (mAct.isFinishing())
        	mFetchFlirtsTask.cancel(false);
	}
	
	class MyParams
	{
		Long mUserId;
	}
	
	
    static class FetchFlirtsTask extends AsyncTask<MyParams, Void, String>
    {

    	
        @Override 
        protected String doInBackground(MyParams... params)  
        {
		    HttpClient httpclient = new DefaultHttpClient();
		    HttpPost httppost = new HttpPost("http://ec2-107-22-191-241.compute-1.amazonaws.com/fetchFlirts.php");

		    
		    try {
		    	
		    	JSONObject json = new JSONObject();
		    
		    	json.put(Constants.USER_ID, params[0].mUserId);
		    	
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
		    	mActivity.runOnUiThread(new Runnable() {
		    		public void run() {
				    	AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mActivity);
				    	alertDialogBuilder.setTitle("Connection Error");
				    	alertDialogBuilder
						.setMessage("You've been disconnected from the internet.")
						.setCancelable(true);
				    	
				    	AlertDialog alertDialog = alertDialogBuilder.create();
				    	 
						// show it
						alertDialog.show();
		    		}});
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
			
			mFlirts = new ArrayList<Flirt>();
			mNbNewFlirts = 0;
			mSuccess = false;
			
	        try  
	        {
	        	json = new JSONObject(result);
	         
	            if(json.getInt(Constants.RESULT_OK) != 0)
	            {
	            	
	            	JSONArray jsonFlirts = json.getJSONArray(Constants.RESULT);
	            	
	            	
		            for(int i = 0; i < jsonFlirts.length(); i++)
		            {
		            	JSONObject jsonFlirt = jsonFlirts.getJSONObject(i);
		            	
		            	Person p = new Person();
		            	p.setUserId(jsonFlirt.getLong(Constants.USER_ID));
		            	p.setUsername(jsonFlirt.getString(Constants.USERNAME));
		            	p.setStar(jsonFlirt.getInt(Constants.STAR) == 1);
		            	
		            	Place place = new Place();
		            	place.setPlaceId(jsonFlirt.getLong(Constants.PLACE_ID));
		            	place.setName(jsonFlirt.getString(Constants.NAME));
		            	
		            	Calendar time = Utility.convertStrToCalendar(jsonFlirt.getString(Constants.FLIRT_TIME), "yyyy-MM-dd hh:mm");
		            	
		            	Flirt f = new Flirt(p, true, place, time, jsonFlirt.getInt(Constants.SIGNAL_SEEN) != 0);
		            	mFlirts.add(f);
		            	
		            	if( jsonFlirt.getInt(Constants.SIGNAL_SEEN) == 0 )
		            		mNbNewFlirts++;
		            } 

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
            	mMethod.fireFetchFlirtsTaskEvent();
            
            }            
        }
 
        // These should never be accessed from within doInBackground()
        FetchFlirtsTaskMethod mMethod = null;
        SherlockFragmentActivity mActivity = null;
        
        // Results
        boolean mSuccess = false;
        ArrayList<Flirt> mFlirts = new ArrayList<Flirt>();
        int mNbNewFlirts = 0;
    }
    
	public boolean success()
	{
		return mFetchFlirtsTask.mSuccess;
	}
	
	
    public ArrayList<Flirt> getFlirts()
    {
    	return mFetchFlirtsTask.mFlirts;
    }
    
    public int getNbNewFlirts()
    {
    	return mFetchFlirtsTask.mNbNewFlirts;
    }
    
    
    
	public void addFetchFlirtsTaskListener(FetchFlirtsTaskListener l) 
	{
		this.mFetchFlirtsTaskListeners.add(l);
	}

	public void removeFetchFlirtsTaskListener(FetchFlirtsTaskListener l) 
	{
	    this.mFetchFlirtsTaskListeners.remove(l);
	}
	
	public void fireFetchFlirtsTaskEvent() {
		FetchFlirtsTaskEvent evt = new FetchFlirtsTaskEvent(this);

	    for (FetchFlirtsTaskListener l : mFetchFlirtsTaskListeners) 
    	{
	    	if( l != null)
	    		l.fetchFlirtsTaskEventReceived(evt);
	    }
	}
	
	public class FetchFlirtsTaskEvent extends EventObject {
		private static final long serialVersionUID = 0;
		
		public FetchFlirtsTaskEvent(Object source) {
	    	super(source);
	  	}
	}
	
	public interface FetchFlirtsTaskListener {
		void fetchFlirtsTaskEventReceived(FetchFlirtsTaskEvent evt);
	}
}
