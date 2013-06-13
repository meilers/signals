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
import com.signals_app.signals.model.Signals.Message;
import com.signals_app.signals.model.Signals.Signal;


public class FetchMessagesTaskMethod
{
	private CopyOnWriteArrayList<FetchMessagesTaskListener> mFetchMessagesTaskListeners;
	
	private SherlockFragmentActivity mAct = null;;
	private FetchMessagesTask mFetchMessagesTask = null;
	
	public FetchMessagesTaskMethod(SherlockFragmentActivity act)
	{
		mAct = act;
		mFetchMessagesTaskListeners = new CopyOnWriteArrayList<FetchMessagesTaskListener>();
		mFetchMessagesTask = new FetchMessagesTask();
	}
	
	 
	public void doTask(Long userId)
	{
    	mFetchMessagesTask = new FetchMessagesTask();
    	mFetchMessagesTask.mActivity = mAct;
    	mFetchMessagesTask.mMethod = this;
    	
    	MyParams params = new MyParams();
    	params.mUserId = userId;
    	mFetchMessagesTask.execute(params);
	}
	
	public void cleanUp()
	{
		mAct.setSupportProgressBarIndeterminateVisibility(false);
        mFetchMessagesTask.mActivity = null;
        
        if (mAct.isFinishing())
        	mFetchMessagesTask.cancel(false);
	}
	
	class MyParams
	{
		Long mUserId;
	}
	
	
    static class FetchMessagesTask extends AsyncTask<MyParams, Void, String>
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
		    HttpPost httppost = new HttpPost("http://ec2-107-22-191-241.compute-1.amazonaws.com/fetchMessages.php");

		    
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
			
			mMessages = new ArrayList<Message>();
			mNbNewMsgs = 0;
			mSuccess = false;
			
	        try  
	        {
	        	json = new JSONObject(result);
	         
	            if(json.getInt(Constants.RESULT_OK) != 0)
	            {
	            	
	            	JSONArray jsonMessages = json.getJSONArray(Constants.RESULT);
	            	 
	            	
		            for(int i = 0; i < jsonMessages.length(); i++)
		            {
		            	JSONObject jsonMessage = jsonMessages.getJSONObject(i);
		            	
		            	Person p = new Person();
		            	p.setUserId(jsonMessage.getLong(Constants.USER_ID));
		            	p.setUsername(jsonMessage.getString(Constants.USERNAME));
		            	p.setStar(jsonMessage.getInt(Constants.STAR) == 1);
		            	
		            	Place place = new Place();
                        place.setPlaceId(Long.parseLong(jsonMessage.getString(Constants.PLACE_ID)));
                        place.setName(jsonMessage.getString(Constants.NAME));
                        p.setCheckPlace(place);
                        
		            	Calendar time = parseCalandar(jsonMessage.getString(Constants.MESSAGE_TIME));
		            	
		            	// New msgs
		            	int nbNewMsgs = jsonMessage.getInt(Constants.NB_NEW_MSGS); 
		            	mNbNewMsgs += nbNewMsgs;
		            	
		            	Message m = new Message(p, jsonMessage.getInt(Constants.SIGNAL_IN) == 1, place, time, jsonMessage.getString(Constants.MESSAGE), jsonMessage.getInt(Constants.SIGNAL_SEEN) == 1 );
		            	mMessages.add(m); 
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
            	mMethod.fireFetchMessagesTaskEvent();
            }            
        }
 
        // These should never be accessed from within doInBackground()
        FetchMessagesTaskMethod mMethod = null;
        SherlockFragmentActivity mActivity = null;
        
        // Results
        boolean mSuccess = false;
        ArrayList<Message> mMessages = new ArrayList<Message>();
        int mNbNewMsgs = 0;
    }
    
	public boolean success()
	{
		return mFetchMessagesTask.mSuccess;
	}
	
	
    public ArrayList<Message> getMessages()
    {
    	return mFetchMessagesTask.mMessages;
    }
    
    public int getNbNewMessages()
    {
    	return mFetchMessagesTask.mNbNewMsgs;
    }
    
    
	public void addFetchMessagesTaskListener(FetchMessagesTaskListener l) 
	{
		this.mFetchMessagesTaskListeners.add(l);
	}

	public void removeFetchMessagesTaskListener(FetchMessagesTaskListener l) 
	{
	    this.mFetchMessagesTaskListeners.remove(l);
	}
	
	public void fireFetchMessagesTaskEvent() {
		FetchMessagesTaskEvent evt = new FetchMessagesTaskEvent(this);

	    for (FetchMessagesTaskListener l : mFetchMessagesTaskListeners) 
    	{
	    	if( l != null)
	    		l.fetchMessagesTaskEventReceived(evt);
	    }
	}
	
	public class FetchMessagesTaskEvent extends EventObject {
		private static final long serialVersionUID = 0;
		
		public FetchMessagesTaskEvent(Object source) {
	    	super(source);
	  	}
	}
	
	public interface FetchMessagesTaskListener {
		void fetchMessagesTaskEventReceived(FetchMessagesTaskEvent evt);
	}
}
