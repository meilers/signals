package com.signals_app.signals.tasks;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.signals_app.signals.model.Constants;
import com.signals_app.signals.model.Place;
import com.signals_app.signals.model.Profile.Person;
import com.signals_app.signals.util.Utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.EventObject;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;

import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.os.AsyncTask;
import android.util.Log;


public class PlaceTaskMethod
{

	private CopyOnWriteArrayList<PlaceTaskListener> mPlaceTaskListeners;
	
	private SherlockFragmentActivity mAct = null;;
	private PlaceTask mPlaceTask = null;
	
	public PlaceTaskMethod(SherlockFragmentActivity act)
	{
		mAct = act;
		mPlaceTaskListeners = new CopyOnWriteArrayList<PlaceTaskListener>();
		mPlaceTask = new PlaceTask();
	}
	
	
	public void doTask(Long userId, Long placeId)
	{
    	mPlaceTask = new PlaceTask();
    	mPlaceTask.mActivity = mAct;
    	mPlaceTask.mMethod = this;
    	mPlaceTask.execute(userId, placeId); 
	}
	
	public void cleanUp()
	{
		mAct.setSupportProgressBarIndeterminateVisibility(false);
        mPlaceTask.mActivity = null;
        
        if (mAct.isFinishing())
        	mPlaceTask.cancel(false);
	} 
	
    static class PlaceTask extends AsyncTask<Long, Void, String>
    {
        @Override
        protected String doInBackground(Long... params)
        {
		    HttpClient httpclient = new DefaultHttpClient();
		    HttpPost httppost = new HttpPost("http://ec2-107-22-191-241.compute-1.amazonaws.com/place.php");

	        
		    try {
		        // Add your data
		        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
				
		        nameValuePairs.add(new BasicNameValuePair(Constants.USER_ID, Long.toString(params[0])));
		        nameValuePairs.add(new BasicNameValuePair(Constants.PLACE_ID, Long.toString(params[1])));
		        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,"UTF-8")); 

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
		        
		    } catch (ClientProtocolException e) {
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

			mPlace = new Place();
			mSuccess = false;
			
			JSONObject json;
			
	        try 
	        {
	        	json = new JSONObject(result);
	         
	            if(json.getInt(Constants.RESULT_OK) != 0)
	            {
	            	JSONObject results = json.getJSONObject(Constants.RESULT);
                    
                    if( !results.isNull(Constants.PEOPLE_BAR))
                    {
                        HashMap<Long,Person> people = new HashMap<Long,Person>();
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
                            person.setStar(json.getInt(Constants.STAR) == 1);
                            person.setLookingFor(json.getInt(Constants.LOOKING_FOR));
                            
                            people.put(person.getUserId(),person);
                        } 
                        
                        mPlace.setPeopleBeforeHashMap(people);
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
            	mMethod.firePlaceTaskEvent();
            }            
        }
 
        // These should never be accessed from within doInBackground()
        PlaceTaskMethod mMethod = null;
        SherlockFragmentActivity mActivity = null;
        
        boolean mSuccess = false;
        Place mPlace = null;
        
    }
    

	// RESULTS
	public boolean success()
	{
		return mPlaceTask.mSuccess;
	}
	
    public Place getPlace()
    {
    	return mPlaceTask.mPlace; 
    }
    
    
    
    // EVENTS
	public void addPlaceTaskListener(PlaceTaskListener l) 
	{
		this.mPlaceTaskListeners.add(l);
	}

	public void removePlaceTaskListener(PlaceTaskListener l) 
	{
	    this.mPlaceTaskListeners.remove(l);
	}
	
	public void firePlaceTaskEvent() {
		PlaceTaskEvent evt = new PlaceTaskEvent(this);

	    for (PlaceTaskListener l : mPlaceTaskListeners) 
    	{
	    	if( l != null)
	    		l.placeTaskEventReceived(evt);
	    }
	}
	
	public class PlaceTaskEvent extends EventObject {

		private static final long serialVersionUID = 0;
		
		  public PlaceTaskEvent(Object source) {
		    super(source);
		  }
		}
	
	public interface PlaceTaskListener {
		void placeTaskEventReceived(PlaceTaskEvent evt);
	}
}
