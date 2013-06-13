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
import com.signals_app.signals.model.Signals.Visit;
import com.signals_app.signals.model.Signals.Signal;


public class FetchSignalsTaskMethod
{
	private CopyOnWriteArrayList<FetchSignalsTaskListener> mFetchSignalsTaskListeners;
	
	private SherlockFragmentActivity mAct = null;;
	private FetchSignalsTask mFetchSignalsTask = null;
	
	public FetchSignalsTaskMethod(SherlockFragmentActivity act)
	{
		mAct = act;
		mFetchSignalsTaskListeners = new CopyOnWriteArrayList<FetchSignalsTaskListener>();
		mFetchSignalsTask = new FetchSignalsTask();
	}
	
	 
	public void doTask(Long userId)
	{
    	mFetchSignalsTask = new FetchSignalsTask();
    	mFetchSignalsTask.mActivity = mAct;
    	mFetchSignalsTask.mMethod = this;
    	
    	MyParams params = new MyParams();
    	params.mUserId = userId;
    	mFetchSignalsTask.execute(params);
	}
	
	public void cleanUp()
	{
		mAct.setSupportProgressBarIndeterminateVisibility(false);
        mFetchSignalsTask.mActivity = null;
        
        if (mAct.isFinishing())
        	mFetchSignalsTask.cancel(false);
	}
	
	class MyParams
	{
		Long mUserId;
	}
	
	
    static class FetchSignalsTask extends AsyncTask<MyParams, Void, String>
    {
    	static public Calendar parseCalandar( String str )
    	{
    		str = str.substring(0, 16);
    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm"); 
    		sdf.setTimeZone(TimeZone.getTimeZone("UTC")); 
    		
    		Date scheduleTime = new Date();
            try
            {
            	scheduleTime =  (Date)sdf.parse(str); 
            }
            catch (Exception e)
            {
              e.printStackTrace();
            }
            
    		
    		Calendar calendar = new GregorianCalendar();
    		calendar.setFirstDayOfWeek(Calendar.SUNDAY); 
    		calendar.setTime(scheduleTime);

    		return calendar;
    	}
    	
        @Override 
        protected String doInBackground(MyParams... params)  
        {
		    HttpClient httpclient = new DefaultHttpClient();
		    HttpPost httppost = new HttpPost("http://ec2-107-22-191-241.compute-1.amazonaws.com/fetchSignals.php");

		    
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
			
			mSignals = new ArrayList<Visit>();
	        mNbNewFlirts = 0;
	        mNbNewMessages = 0;
	        mNbNewVotes = 0;
	        mNbNewVisits = 0;
			mSuccess = false;
			
	        try  
	        {
	        	json = new JSONObject(result);
	         
	            if(json.getInt(Constants.RESULT_OK) != 0)
	            {
	            	json = json.getJSONObject(Constants.RESULT);
	            	
		        	mNbNewFlirts = json.getInt(Constants.NB_NEW_FLIRTS);
		        	mNbNewMessages = json.getInt(Constants.NB_NEW_MSGS);
		        	mNbNewVotes = json.getInt(Constants.NB_NEW_VOTES);
		        	mNbNewVisits = json.getInt(Constants.NB_NEW_VISITS);
		        	
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
                mMethod.fireFetchSignalsTaskEvent();
            	
            	
            }            
        }
 
        // These should never be accessed from within doInBackground()
        FetchSignalsTaskMethod mMethod = null;
        SherlockFragmentActivity mActivity = null;
        
        // Results
        boolean mSuccess = false;
        ArrayList<Visit> mSignals = new ArrayList<Visit>();
        int mNbNewFlirts = 0;
        int mNbNewMessages = 0;
        int mNbNewVotes = 0;
        int mNbNewVisits = 0;
        
    }
    
	public boolean success()
	{
		return mFetchSignalsTask.mSuccess;
	}
	
	
    public ArrayList<Visit> getSignals()
    {
    	return mFetchSignalsTask.mSignals;
    }
    
    
    public int getNbNewFlirts()
    {
    	return mFetchSignalsTask.mNbNewFlirts;
    }
    
    public int getNbNewMessages()
    {
    	return mFetchSignalsTask.mNbNewMessages;
    }
    
    public int getNbNewVotes()
    {
    	return mFetchSignalsTask.mNbNewVotes;
    }
    
    public int getNbNewVisits()
    {
    	return mFetchSignalsTask.mNbNewVisits;
    }
    
    
	public void addFetchSignalsTaskListener(FetchSignalsTaskListener l) 
	{
		this.mFetchSignalsTaskListeners.add(l);
	}

	public void removeFetchSignalsTaskListener(FetchSignalsTaskListener l) 
	{
	    this.mFetchSignalsTaskListeners.remove(l);
	}
	
	public void fireFetchSignalsTaskEvent() {
		FetchSignalsTaskEvent evt = new FetchSignalsTaskEvent(this);

	    for (FetchSignalsTaskListener l : mFetchSignalsTaskListeners) 
    	{
	    	if( l != null)
	    		l.fetchSignalsTaskEventReceived(evt);
	    }
	}
	
	public class FetchSignalsTaskEvent extends EventObject {
		private static final long serialVersionUID = 0;
		
		public FetchSignalsTaskEvent(Object source) {
	    	super(source);
	  	}
	}
	
	public interface FetchSignalsTaskListener {
		void fetchSignalsTaskEventReceived(FetchSignalsTaskEvent evt);
	}
}
