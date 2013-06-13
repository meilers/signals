package com.signals_app.signals.tasks;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.signals_app.signals.model.Constants;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.HashMap;
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
import com.signals_app.signals.model.Profile.User;


public class PlacesTaskMethod
{
	private CopyOnWriteArrayList<PlacesTaskListener> mPlacesTaskListeners;
	
	private SherlockFragmentActivity mAct = null;;
	private PlacesTask mPlacesTask = null;
	
	public PlacesTaskMethod(SherlockFragmentActivity act)
	{
		mAct = act;
		mPlacesTaskListeners = new CopyOnWriteArrayList<PlacesTaskListener>();
		mPlacesTask = new PlacesTask();
	}
	
	 
	public void doTask( Long userId, int cityId )
	{
		
    	mPlacesTask = new PlacesTask();
    	mPlacesTask.mActivity = mAct; 
    	mPlacesTask.mMethod = this;
    	
		MyParams params = new MyParams();
		params.mUserId = userId;
		params.mCityId = cityId;
		
    	mPlacesTask.execute(params);
	}
	
	public void cleanUp()
	{
		mAct.setSupportProgressBarIndeterminateVisibility(false);
        mPlacesTask.mActivity = null;
        
        if (mAct.isFinishing())
        	mPlacesTask.cancel(false);
	}
	
	class MyParams
	{
		Long mUserId;
		int mCityId;
	}
	
    static class PlacesTask extends AsyncTask<MyParams, Void, String>
    {
        @Override 
        protected String doInBackground(MyParams... params)  
        {
		    HttpClient httpclient = new DefaultHttpClient();
		    HttpPost httppost = new HttpPost("http://ec2-107-22-191-241.compute-1.amazonaws.com/places.php");

		    try {
		        
		    	JSONObject json = new JSONObject();
		    	
		    	json.put(Constants.USER_ID, params[0].mUserId);
		    	json.put(Constants.CITY_ID, params[0].mCityId);
		    	
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
			mPlaces = new ArrayList<Place>();
			
	        try  
	        {
	        	json = new JSONObject(result);
	         
	            if(json.getInt(Constants.RESULT_OK) != 0)
	            {
	            	
	            	JSONArray results = json.getJSONArray(Constants.RESULT);
	            	
		            // looping through All Contacts
		            for(int i = 0; i < results.length(); i++)
		            {
		            
		                json = results.getJSONObject(i);
		         
		                Place place = new Place();
		                
		                // Storing each json item in variable
		                Long id = Long.parseLong(json.getString(Constants.PLACE_ID));
		                Integer genre = Integer.parseInt(json.getString(Constants.GENRE));
		                String name = json.getString(Constants.NAME); 
		                String address = json.getString(Constants.ADDRESS);
		                GpsPosition pos = new GpsPosition((float)json.getDouble(Constants.LATITUDE), (float)json.getDouble(Constants.LONGITUDE), 0.0f);
		                
		                place.setPlaceId(id);
		                place.setGenre(genre);
		                place.setName(name);
		                place.setAddress(address);
		                place.setGpsPosition(pos); 
		                
		                HashMap<Long,Person> people = new HashMap<Long,Person>();
		                for( int k=0; k < json.getInt(Constants.NB_USERS_FEMALE); ++k)
		                {
		                    Person p = new Person();
		                    p.setSex(Person.SEX_FEMALE);
		                    people.put(-Long.valueOf(k+1), p);
		                }
                        for( int k=0; k < json.getInt(Constants.NB_USERS_MALE); ++k)
                        {
                            Person p = new Person();
                            p.setSex(Person.SEX_MALE);
                            people.put(Long.valueOf(k+1), p);
                        }
		                
		                place.setPeopleBeforeHashMap(people);
		                

		                
		                mPlaces.add(place);
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
            	mMethod.firePlacesTaskEvent();
            }            
        }
 
        // These should never be accessed from within doInBackground()
        PlacesTaskMethod mMethod = null;
        SherlockFragmentActivity mActivity = null;
        
        // Results
        boolean mSuccess = false;
        ArrayList<Place> mPlaces = new ArrayList<Place>();
    }
    
    

	// RESULTS
	public boolean success()
	{
		return mPlacesTask.mSuccess;
	}
	
    public ArrayList<Place> getPlaces()
    {
    	return mPlacesTask.mPlaces;
    }
    
    
    // EVENTS
	public void addPlacesTaskListener(PlacesTaskListener l) 
	{
		this.mPlacesTaskListeners.add(l);
	}

	public void removePlacesTaskListener(PlacesTaskListener l) 
	{
	    this.mPlacesTaskListeners.remove(l);
	}
	
	public void firePlacesTaskEvent() {
		PlacesTaskEvent evt = new PlacesTaskEvent(this);

	    for (PlacesTaskListener l : mPlacesTaskListeners) 
    	{
	    	if( l != null)
	    		l.placesTaskEventReceived(evt);
	    }
	}
	
	public class PlacesTaskEvent extends EventObject {

		private static final long serialVersionUID = 0;
		
		  public PlacesTaskEvent(Object source) {
		    super(source);
		  }
		}
	
	public interface PlacesTaskListener {
		void placesTaskEventReceived(PlacesTaskEvent evt);
	}
}
