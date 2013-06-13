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


public class FetchVisitsTaskMethod
{
	private CopyOnWriteArrayList<FetchVisitsTaskListener> mFetchVisitsTaskListeners;
	
	private SherlockFragmentActivity mAct = null;;
	private FetchVisitsTask mFetchVisitsTask = null;
	
	public FetchVisitsTaskMethod(SherlockFragmentActivity act)
	{
		mAct = act;
		mFetchVisitsTaskListeners = new CopyOnWriteArrayList<FetchVisitsTaskListener>();
		mFetchVisitsTask = new FetchVisitsTask();
	}
	
	 
	public void doTask(Long userId)
	{
    	mFetchVisitsTask = new FetchVisitsTask();
    	mFetchVisitsTask.mActivity = mAct;
    	mFetchVisitsTask.mMethod = this;
    	
    	MyParams params = new MyParams();
    	params.mUserId = userId;
    	mFetchVisitsTask.execute(params);
	}
	
	public void cleanUp()
	{
		mAct.setSupportProgressBarIndeterminateVisibility(false);
        mFetchVisitsTask.mActivity = null;
        
        if (mAct.isFinishing())
        	mFetchVisitsTask.cancel(false);
	}
	
	class MyParams
	{
		Long mUserId;
	}
	
	
    static class FetchVisitsTask extends AsyncTask<MyParams, Void, String>
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
		    HttpPost httppost = new HttpPost("http://ec2-107-22-191-241.compute-1.amazonaws.com/fetchVisits.php");

		    
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
			
			mVisits = new ArrayList<Visit>();
			mNbNewVisits = 0;
			mSuccess = false;
		
	        try  
	        {
	        	json = new JSONObject(result);
	         
	            if(json.getInt(Constants.RESULT_OK) != 0)
	            {
	            	
	            	JSONArray jsonVisits = json.getJSONArray(Constants.RESULT);
	            	
		            for(int i = 0; i < jsonVisits.length(); i++)
		            {
		            	JSONObject jsonVisit = jsonVisits.getJSONObject(i);
		            	
		            	Person p = new Person();
		            	p.setUserId(jsonVisit.getLong(Constants.USER_ID));
		            	p.setUsername(jsonVisit.getString(Constants.USERNAME));
		            	p.setStar(jsonVisit.getInt(Constants.STAR) == 1);
		            	
		            	Place place = new Place();
		            	place.setPlaceId(jsonVisit.getLong(Constants.PLACE_ID));
		            	place.setName(jsonVisit.getString(Constants.NAME));
		            	
		            	Calendar time = parseCalandar(jsonVisit.getString(Constants.VISIT_TIME));
		            	
		            	Visit f = new Visit(p, true, place, time, jsonVisit.getInt(Constants.SIGNAL_SEEN) != 0); 
		            	mVisits.add(f);
		            	
		            	if( jsonVisit.getInt(Constants.SIGNAL_SEEN) == 0 )
		            		mNbNewVisits++;
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
            	mMethod.fireFetchVisitsTaskEvent();
            }            
        }
 
        // These should never be accessed from within doInBackground()
        FetchVisitsTaskMethod mMethod = null;
        SherlockFragmentActivity mActivity = null;
        
        // Results
        boolean mSuccess = false;
        ArrayList<Visit> mVisits = new ArrayList<Visit>();
        int mNbNewVisits = 0;
    }
    
	public boolean success()
	{
		return mFetchVisitsTask.mSuccess;
	}
	
	
    public ArrayList<Visit> getVisits()
    {
    	return mFetchVisitsTask.mVisits;
    }
    
    
    public int getNbNewVisits()
    {
    	return mFetchVisitsTask.mNbNewVisits;
    }
    
    
    
	public void addFetchVisitsTaskListener(FetchVisitsTaskListener l) 
	{
		this.mFetchVisitsTaskListeners.add(l);
	}

	public void removeFetchVisitsTaskListener(FetchVisitsTaskListener l) 
	{
	    this.mFetchVisitsTaskListeners.remove(l);
	}
	
	public void fireFetchVisitsTaskEvent() {
		FetchVisitsTaskEvent evt = new FetchVisitsTaskEvent(this);

	    for (FetchVisitsTaskListener l : mFetchVisitsTaskListeners) 
    	{
	    	if( l != null)
	    		l.fetchVisitsTaskEventReceived(evt);
	    }
	}
	
	public class FetchVisitsTaskEvent extends EventObject {
		private static final long serialVersionUID = 0;
		
		public FetchVisitsTaskEvent(Object source) {
	    	super(source);
	  	}
	}
	
	public interface FetchVisitsTaskListener {
		void fetchVisitsTaskEventReceived(FetchVisitsTaskEvent evt);
	}
}
