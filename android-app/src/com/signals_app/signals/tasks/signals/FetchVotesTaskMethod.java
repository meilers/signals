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
import com.signals_app.signals.model.Signals.Vote;
import com.signals_app.signals.model.Signals.Signal;


public class FetchVotesTaskMethod
{
	private CopyOnWriteArrayList<FetchVotesTaskListener> mFetchVotesTaskListeners;
	
	private SherlockFragmentActivity mAct = null;;
	private FetchVotesTask mFetchVotesTask = null;
	
	public FetchVotesTaskMethod(SherlockFragmentActivity act)
	{
		mAct = act;
		mFetchVotesTaskListeners = new CopyOnWriteArrayList<FetchVotesTaskListener>();
		mFetchVotesTask = new FetchVotesTask();
	}
	
	 
	public void doTask(Long userId)
	{
    	mFetchVotesTask = new FetchVotesTask();
    	mFetchVotesTask.mActivity = mAct;
    	mFetchVotesTask.mMethod = this;
    	
    	MyParams params = new MyParams();
    	params.mUserId = userId;
    	mFetchVotesTask.execute(params);
	}
	
	public void cleanUp()
	{
		mAct.setSupportProgressBarIndeterminateVisibility(false);
        mFetchVotesTask.mActivity = null;
        
        if (mAct.isFinishing())
        	mFetchVotesTask.cancel(false);
	}
	
	class MyParams
	{
		Long mUserId;
	}
	
	
    static class FetchVotesTask extends AsyncTask<MyParams, Void, String>
    {
    	static public Calendar parseCalandar( String str )
    	{
    		str = str.substring(0, 16);
    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm"); 
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
		    HttpPost httppost = new HttpPost("http://ec2-107-22-191-241.compute-1.amazonaws.com/fetchVotes.php");

		    
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
			
			mVotes = new ArrayList<Vote>();
			mNbNewVotes = 0;
			mSuccess = false;
			
	        try  
	        {
	        	json = new JSONObject(result);
	         
	            if(json.getInt(Constants.RESULT_OK) != 0)
	            {
	            	
	            	JSONArray jsonVotes = json.getJSONArray(Constants.RESULT);
	            	
		            for(int i = 0; i < jsonVotes.length(); i++)
		            {
		            	JSONObject jsonVote = jsonVotes.getJSONObject(i);
		            	
		            	Person p = new Person();
		            	p.setUserId(jsonVote.getLong(Constants.USER_ID));
		            	p.setUsername(jsonVote.getString(Constants.USERNAME));
		            	p.setStar(jsonVote.getInt(Constants.STAR) == 1);
		            	
		            	Place place = new Place();
		            	place.setPlaceId(jsonVote.getLong(Constants.PLACE_ID));
		            	place.setName(jsonVote.getString(Constants.NAME));
		            	
		            	
		            	Calendar time = parseCalandar(jsonVote.getString(Constants.VOTE_TIME));
		            	
		            	Vote f = new Vote(p, true, place, time, jsonVote.getInt(Constants.SIGNAL_SEEN) != 0, true);
		            	mVotes.add(f);
		            	
		            	if( jsonVote.getInt(Constants.SIGNAL_SEEN) == 0 )
		            		mNbNewVotes++;
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
            	mMethod.fireFetchVotesTaskEvent();
            }            
        }
 
        // These should never be accessed from within doInBackground()
        FetchVotesTaskMethod mMethod = null;
        SherlockFragmentActivity mActivity = null;
        
        // Results
        boolean mSuccess = false;
        ArrayList<Vote> mVotes = new ArrayList<Vote>();
        int mNbNewVotes = 0;
    }
    
	public boolean success()
	{
		return mFetchVotesTask.mSuccess;
	}
	
	
    public ArrayList<Vote> getVotes()
    {
    	return mFetchVotesTask.mVotes;
    }
    
    public int getNbNewVotes()
    {
    	return mFetchVotesTask.mNbNewVotes;
    }
    
    
    
    
	public void addFetchVotesTaskListener(FetchVotesTaskListener l) 
	{
		this.mFetchVotesTaskListeners.add(l);
	}

	public void removeFetchVotesTaskListener(FetchVotesTaskListener l) 
	{
	    this.mFetchVotesTaskListeners.remove(l);
	}
	
	public void fireFetchVotesTaskEvent() {
		FetchVotesTaskEvent evt = new FetchVotesTaskEvent(this);

	    for (FetchVotesTaskListener l : mFetchVotesTaskListeners) 
    	{
	    	if( l != null)
	    		l.fetchVotesTaskEventReceived(evt);
	    }
	}
	
	public class FetchVotesTaskEvent extends EventObject {
		private static final long serialVersionUID = 0;
		
		public FetchVotesTaskEvent(Object source) {
	    	super(source);
	  	}
	}
	
	public interface FetchVotesTaskListener {
		void fetchVotesTaskEventReceived(FetchVotesTaskEvent evt);
	}
}
