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


public class FetchMessagesChatTaskMethod
{
    static final public int ERROR_NO_MESSAGES = 1;
    
	private CopyOnWriteArrayList<FetchMessagesChatTaskListener> mFetchMessagesChatTaskListeners;
	
	
	private SherlockFragmentActivity mAct = null;;
	private FetchMessagesChatTask mFetchMessagesChatTask = null;
	
	private Person mPerson;
	
	public FetchMessagesChatTaskMethod(SherlockFragmentActivity act)
	{
		mAct = act;
		mFetchMessagesChatTaskListeners = new CopyOnWriteArrayList<FetchMessagesChatTaskListener>();
		mFetchMessagesChatTask = new FetchMessagesChatTask();
	}
	
	 
	public void doTask(Long userId, Person person)
	{
    	mFetchMessagesChatTask = new FetchMessagesChatTask();
    	mFetchMessagesChatTask.mActivity = mAct;
    	mFetchMessagesChatTask.mMethod = this;
    	
    	mPerson = person;
    	
    	if( mPerson != null )
    	{
            MyParams params = new MyParams();
            params.mUserId = userId;
            params.mPersonId = person.getUserId();
            mFetchMessagesChatTask.execute(params);    	    
    	}

	}
	
	public void cleanUp()
	{
		mAct.setSupportProgressBarIndeterminateVisibility(false);
        mFetchMessagesChatTask.mActivity = null;
        
        if (mAct.isFinishing())
        	mFetchMessagesChatTask.cancel(false);
	}
	
	class MyParams
	{
		Long mUserId;
		Long mPersonId;
	}
	
	
    static class FetchMessagesChatTask extends AsyncTask<MyParams, Void, String>
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
		    HttpPost httppost = new HttpPost("http://ec2-107-22-191-241.compute-1.amazonaws.com/fetchMessagesChat.php");

		    
		    try {
		    	
		    	JSONObject json = new JSONObject();
		    
		    	json.put(Constants.USER_ID, params[0].mUserId);
		    	json.put(Constants.PERSON_ID, params[0].mPersonId);
		    	
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
			
			mPlace = new Place();
			mMessages = new ArrayList<Message>();
			mNbNewMsgs = 0;
			
			mSuccess = false;
			
	        try  
	        {
	        	json = new JSONObject(result);
	         
	            if(json.getInt(Constants.RESULT_OK) != 0)
	            {
	                json = json.getJSONObject(Constants.RESULT);
	                
	                if( !json.getString(Constants.PLACE_ID).equals("0") )
                    {
	                    mPlace.setPlaceId(Long.parseLong(json.getString(Constants.PLACE_ID)));
	                    mPlace.setGenre(json.getInt(Constants.GENRE));
	                    mPlace.setName(json.getString(Constants.NAME));
                    }
	                
	                mMethod.mPerson.setDontApproach(json.getInt(Constants.DONT_APPROACH) == 1);
	                mMethod.mPerson.setBlocked(json.getInt(Constants.BLOCKED) == 1);
	                
	            	JSONArray jsonMessages = json.getJSONArray(Constants.MESSAGES);
	            	
		            for(int i = 0; i < jsonMessages.length(); i++)
		            {
		            	JSONObject jsonMessage = jsonMessages.getJSONObject(i);
		            	
		            	Place place = new Place();
		            	place.setName(jsonMessage.getString(Constants.NAME));
                        
		            	Calendar time = parseCalandar(jsonMessage.getString(Constants.MESSAGE_TIME));
		            	
		            	if( mMethod != null )
		            	{
    		            	Message m = new Message(mMethod.mPerson, jsonMessage.getInt(Constants.SIGNAL_IN) == 1, place, time, jsonMessage.getString(Constants.MESSAGE), jsonMessage.getInt(Constants.SIGNAL_SEEN) != 0);
    		            	mMessages.add(m);
		            	}
		            	
		            	if( jsonMessage.getInt(Constants.SIGNAL_IN) == 1 && jsonMessage.getInt(Constants.SIGNAL_SEEN) == 0 )
		            		mNbNewMsgs++;
		            } 
		            
		            
		            
		            mSuccess = true;
	            }
	            else
	            {
	                if( json.getInt(Constants.ERROR) == FetchMessagesChatTaskMethod.ERROR_NO_MESSAGES )
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
                mMethod.fireFetchMessagesChatTaskEvent();
            	
            	
            }            
        }
 
        // These should never be accessed from within doInBackground()
        FetchMessagesChatTaskMethod mMethod = null;
        SherlockFragmentActivity mActivity = null;
        
        // Results
        boolean mSuccess = false;
        ArrayList<Message> mMessages = new ArrayList<Message>();
        int mNbNewMsgs = 0;
        Place mPlace = new Place();
    }
    
	public boolean success()
	{
		return mFetchMessagesChatTask.mSuccess;
	}
	
	
    
	
    public ArrayList<Message> getMessages()
    {
    	return mFetchMessagesChatTask.mMessages;
    }
    
    
    public int getNbNewMessages()
    {
    	return mFetchMessagesChatTask.mNbNewMsgs;
    }
    
    public Place getCheckPlace()
    {
        return mFetchMessagesChatTask.mPlace;
    }
    
	public void addFetchMessagesChatTaskListener(FetchMessagesChatTaskListener l) 
	{
		this.mFetchMessagesChatTaskListeners.add(l);
	}

	public void removeFetchMessagesChatTaskListener(FetchMessagesChatTaskListener l) 
	{
	    this.mFetchMessagesChatTaskListeners.remove(l);
	}
	
	public void fireFetchMessagesChatTaskEvent() {
		FetchMessagesChatTaskEvent evt = new FetchMessagesChatTaskEvent(this);

	    for (FetchMessagesChatTaskListener l : mFetchMessagesChatTaskListeners) 
    	{
	    	if( l != null)
	    		l.fetchMessagesChatTaskEventReceived(evt);
	    }
	}
	
	public class FetchMessagesChatTaskEvent extends EventObject {
		private static final long serialVersionUID = 0;
		
		public FetchMessagesChatTaskEvent(Object source) {
	    	super(source);
	  	}
	}
	
	public interface FetchMessagesChatTaskListener {
		void fetchMessagesChatTaskEventReceived(FetchMessagesChatTaskEvent evt);
	}
}
