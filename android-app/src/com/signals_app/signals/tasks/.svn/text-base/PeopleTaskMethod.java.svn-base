package com.signals_app.signals.tasks;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.signals_app.signals.model.Constants;
import com.signals_app.signals.model.Place;

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

import android.app.AlertDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.signals_app.signals.model.Profile.GpsPosition;
import com.signals_app.signals.model.Profile.Person;
import com.signals_app.signals.model.Profile.User;
import com.signals_app.signals.util.Utility;


public class PeopleTaskMethod
{
	private CopyOnWriteArrayList<PeopleTaskListener> mPeopleTaskListeners;
	
	private SherlockFragmentActivity mAct = null;;
	private PeopleTask mPeopleTask = null;
	
	public PeopleTaskMethod(SherlockFragmentActivity act)
	{
		mAct = act;
		mPeopleTaskListeners = new CopyOnWriteArrayList<PeopleTaskListener>();
		mPeopleTask = new PeopleTask();
	}
	
	 
	public void doTask( Long userId, Long placeId, float lat, float lng, float accuracy, int radius )
	{
    	mPeopleTask = new PeopleTask();
    	mPeopleTask.mActivity = mAct;
    	mPeopleTask.mMethod = this;
    	
        MyParams params = new MyParams();
        params.mUserId = userId;
        params.mPlaceId = placeId;
        params.mLatitude = lat;
        params.mLongitude = lng;
        params.mAccuracy = accuracy;
        params.mRadius = radius;
        
    	mPeopleTask.execute(params);
	}
	
	public void cleanUp()
	{
		mAct.setSupportProgressBarIndeterminateVisibility(false);
        mPeopleTask.mActivity = null;
        
        if (mAct.isFinishing())
        	mPeopleTask.cancel(false);
	}
	
	class MyParams
    {
        Long mUserId;
        Long mPlaceId;
        
        float mLatitude;
        float mLongitude;
        int mRadius;
        float mAccuracy; 
    }
	   
    static class PeopleTask extends AsyncTask<MyParams, Void, String>
    {
        @Override 
        protected String doInBackground(MyParams... params)  
        {
		    HttpClient httpclient = new DefaultHttpClient();
		    HttpPost httppost = new HttpPost("http://ec2-107-22-191-241.compute-1.amazonaws.com/people.php");

		    try {
		    	JSONObject json = new JSONObject();
		    
		    	json.put(Constants.USER_ID, params[0].mUserId);
		    	json.put(Constants.PLACE_ID, params[0].mPlaceId);
		    	json.put(Constants.LATITUDE, params[0].mLatitude);
                json.put(Constants.LONGITUDE, params[0].mLongitude);
                json.put(Constants.ACCURACY, params[0].mAccuracy);
                json.put(Constants.RADIUS, params[0].mRadius); 
                
                
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
			
			mPeople = null;
			mSuccess = false;
			
	        try  
	        {
	        	json = new JSONObject(result);
	         
	            if(json.getInt(Constants.RESULT_OK) != 0)
	            {
	            	JSONObject results = json.getJSONObject(Constants.RESULT);
	            	
	            	if( !results.isNull(Constants.PEOPLE_BAR))
	            	{
	            	    mPeople = new ArrayList<Person>();
    	            	JSONArray peopleBar = results.getJSONArray(Constants.PEOPLE_BAR);
    	            	
    		            for(int i = 0; i < peopleBar.length(); i++)
    		            {
    		                json = peopleBar.getJSONObject(i); 
    		                Person person = new Person();
    		                
    		                // ACCOUNT
    		                person.setUserId(Long.parseLong(json.getString(Constants.USER_ID)));
    		                person.setUsername(json.getString(Constants.USERNAME)); 
    		                
    		                
    		                // BASIC INFO
    		                person.setSex(json.getInt(Constants.SEX));
    		                person.setInterestedIn(json.getInt(Constants.INTERESTED_IN));
    		                
    		                String[] strArr = json.getString(Constants.BIRTHDAY).split("-");
    						Calendar dob = Calendar.getInstance();
    						dob.set(Integer.valueOf(strArr[0]), Integer.valueOf(strArr[1])-1, Integer.valueOf(strArr[2]));
    						person.setBirthday(dob);
    						person.setAge(Utility.getAge(dob));
    						
    						// OTHER
    						if( !json.getString(Constants.PLACE_ID).equals("0") )
    	                    {
    	                        Place p = new Place();
    	                        p.setPlaceId(Long.parseLong(json.getString(Constants.PLACE_ID)));
    	                        p.setName(json.getString(Constants.NAME));
    	                        person.setCheckPlace(p);
    	                        person.setStar(json.getInt(Constants.STAR) == 1);
    	                    }
    						
    						
    						// OTHER
    		                person.setLookingFor(json.getInt(Constants.LOOKING_FOR));
    		                
    		                mPeople.add(person);
    		            } 
    		            
    		            mSuccess = true;
	            	}
		            
	            	
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
            	mMethod.firePeopleTaskEvent();
            }            
        }
 
        // These should never be accessed from within doInBackground()
        PeopleTaskMethod mMethod = null;
        SherlockFragmentActivity mActivity = null;
        
        // Results
        boolean mSuccess = false;
        ArrayList<Person> mPeople = new ArrayList<Person>();
    }
    

	// RESULTS
	public boolean success()
	{
		return mPeopleTask.mSuccess;
	}

    public ArrayList<Person> getPeopleBar()
    {
    	return mPeopleTask.mPeople;
    }
    
    
    
    // EVENTS
	public void addPeopleTaskListener(PeopleTaskListener l) 
	{
		this.mPeopleTaskListeners.add(l);
	}

	public void removePeopleTaskListener(PeopleTaskListener l) 
	{
	    this.mPeopleTaskListeners.remove(l);
	}
	
	public void firePeopleTaskEvent() {
		PeopleTaskEvent evt = new PeopleTaskEvent(this);

	    for (PeopleTaskListener l : mPeopleTaskListeners) 
    	{
	    	if( l != null)
	    		l.peopleTaskEventReceived(evt);
	    }
	}
	
	public class PeopleTaskEvent extends EventObject {

		private static final long serialVersionUID = 0;
		
		  public PeopleTaskEvent(Object source) {
		    super(source);
		  }
		}
	
	public interface PeopleTaskListener {
		void peopleTaskEventReceived(PeopleTaskEvent evt);
	}
}
