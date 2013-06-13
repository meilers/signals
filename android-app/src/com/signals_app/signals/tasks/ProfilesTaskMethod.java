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
import java.util.Collections;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
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

import com.signals_app.signals.model.Profile.BarStars;
import com.signals_app.signals.model.Profile.GpsPosition;
import com.signals_app.signals.model.Profile.Person;
import com.signals_app.signals.tasks.PlacesTaskMethod.MyParams;
import com.signals_app.signals.util.Utility;


public class ProfilesTaskMethod
{

	static private CopyOnWriteArrayList<ProfilesTaskListener> mProfilesTaskListeners = new CopyOnWriteArrayList<ProfilesTaskListener>();
	
	private SherlockFragmentActivity mAct = null;;
	private ProfilesTask mProfilesTask = null;
	
	public ProfilesTaskMethod(SherlockFragmentActivity act)
	{
		mAct = act;
		mProfilesTask = new ProfilesTask();
	}
	
	
	public void doTask(Long userId, ArrayList<Long> peopleIds)
	{
    	mProfilesTask = new ProfilesTask();
    	mProfilesTask.mActivity = mAct;
    	mProfilesTask.mMethod = this;
    	 
    	MyParams params = new MyParams();
    	params.mUserId = userId;
    	params.mPeopleIds = peopleIds;
    	
    	mProfilesTask.execute(params);
	}
	
	public void cleanUp()
	{
		mAct.setSupportProgressBarIndeterminateVisibility(false);
        mProfilesTask.mActivity = null;
        
        if (mAct.isFinishing())
        	mProfilesTask.cancel(false);
	}
	
	class MyParams
    {
        Long mUserId;
        ArrayList<Long> mPeopleIds;
    }
	   
	   
    static class ProfilesTask extends AsyncTask<MyParams, Void, String>
    {
        @Override
        protected String doInBackground(MyParams... params)
        {
		    HttpClient httpclient = new DefaultHttpClient();
		    HttpPost httppost = new HttpPost("http://ec2-107-22-191-241.compute-1.amazonaws.com/profiles.php"); 
			
		    try {
		    	JSONObject json = new JSONObject();
			    
		    	ArrayList<Long> peopleIds = params[0].mPeopleIds;
		    	JSONArray jsonPersonIds = new JSONArray();
		    	
		    	for( int i = 0; i<peopleIds.size(); ++i )
		    	    jsonPersonIds.put(Long.toString(peopleIds.get(i)));
		    	
		    	json.put(Constants.USER_ID, Long.toString(params[0].mUserId));
		    	json.put(Constants.PERSON_IDS, jsonPersonIds);
		    	
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
			mPeople = new ArrayList<Person>();
			
	        try 
	        {
	        	json = new JSONObject(result); 
	         
	            if(json.getInt(Constants.RESULT_OK) != 0)
	            {
	            	
	            	JSONArray results = json.getJSONArray(Constants.RESULT);
			         
	            	for( int i = 0; i < results.length(); ++i )
	            	{
	            	    Person person = new Person();
	            	    
	            	    json = results.getJSONObject(i);
	            	    
	            	    
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
    					
    					
    					// PHOTOS
    					JSONArray jsonPhotosFilenames = json.getJSONArray(Constants.PROFILE_PHOTOS_FILENAMES);
    	                ArrayList<String> filenames = new ArrayList<String>();
    	                
    	                for(int j = 0; j < jsonPhotosFilenames.length(); j++) 
    	                	filenames.add(jsonPhotosFilenames.getString(j));
    	                
    	                Collections.reverse(filenames);
    	                person.setProfilePhotosFilenames(filenames); 
    					
    	                
    					
    					
    					// ABOUT
    					person.setAboutMe(json.getString(Constants.ABOUT_ME));
    					
    					person.setOccupation(json.getString(Constants.OCCUPATION));
    					person.setEducation(json.getString(Constants.EDUCATION));
    					person.setInterests(json.getString(Constants.INTERESTS)); 
    					person.setActivities(json.getString(Constants.ACTIVITIES));
    					person.setTravel(json.getString(Constants.TRAVEL)); 
    					person.setMusic(json.getString(Constants.MUSIC));
    					person.setDrinks(json.getString(Constants.DRINKS));
    					person.setMyPerfectNightOut(json.getString(Constants.MY_PERFECT_NIGHT_OUT)); 
    					
    					
    					// ACTIVITY
    					if( !json.getString(Constants.PLACE_ID).equals("0") )
    					{
    						Place p = new Place();
    						p.setPlaceId(Long.parseLong(json.getString(Constants.PLACE_ID)));
    						p.setGenre(json.getInt(Constants.GENRE));
    						p.setName(json.getString(Constants.NAME));
    						GpsPosition gpsPos = new GpsPosition((float)json.getDouble(Constants.LATITUDE), (float)json.getDouble(Constants.LONGITUDE), 0.0f);
                            p.setGpsPosition(gpsPos);
    						person.setCheckPlace(p);
    						
    						person.setStar(json.getInt(Constants.STAR) == 1);
    					}
    					
    					JSONArray jsonTopFrequentedBars = json.getJSONArray(Constants.TOP_FREQUENTED_BARS); 
    					ArrayList<Place> topFrequentedBars = new ArrayList<Place>();
    					
    					for(int j = 0; j < jsonTopFrequentedBars.length(); j++)
    					{
    						JSONObject jsonBar = jsonTopFrequentedBars.getJSONObject(j);
    						Place place = new Place();
    						place.setPlaceId(jsonBar.getLong(Constants.PLACE_ID));
    						place.setGenre(jsonBar.getInt(Constants.GENRE));
    						place.setName(jsonBar.getString(Constants.NAME));
    						topFrequentedBars.add(place);
    					}
    					person.setTopFrequentedBars(topFrequentedBars);
    					
    					JSONArray jsonCollectedBarStars = json.getJSONArray(Constants.COLLECTED_BAR_STARS);
    					ArrayList<BarStars> collectedBarStars = new ArrayList<BarStars>();
    					
    					for(int j = 0; j < jsonCollectedBarStars.length(); j++)
    					{
    						JSONObject jsonBarStar = jsonCollectedBarStars.getJSONObject(j);
    						Place place = new Place();
    						place.setPlaceId(jsonBarStar.getLong(Constants.PLACE_ID));
    						place.setGenre(jsonBarStar.getInt(Constants.GENRE));
    						place.setName(jsonBarStar.getString(Constants.NAME));
    						
    						BarStars pair = new BarStars(place, jsonBarStar.getInt(Constants.STAR_COUNT));
    					
    						collectedBarStars.add(pair);
    					}
    					person.setCollectedBarStars(collectedBarStars);
    					
    					
    					// APPROACH
    					person.setTipComeAndSayHi(json.getInt(Constants.TIP_COME_AND_SAY_HI) == 1);
    					person.setTipComeAndSayHi(json.getInt(Constants.TIP_COME_AND_SAY_HI) == 1);
    					person.setTipBuyMeADrink(json.getInt(Constants.TIP_BUY_ME_A_DRINK) == 1);
    					person.setTipInviteMeToDance(json.getInt(Constants.TIP_INVITE_ME_TO_DANCE) == 1);
    					person.setTipMakeMeLaugh(json.getInt(Constants.TIP_MAKE_ME_LAUGH) == 1);
    					person.setTipMeetMyFriends(json.getInt(Constants.TIP_MEET_MY_FRIENDS) == 1);
    					person.setTipSurpriseMe(json.getInt(Constants.TIP_SURPRISE_ME) == 1);
    					person.setDontExpectAnything(json.getInt(Constants.DONT_EXPECT_ANYTHING) == 1);
    					person.setDontBePersistent(json.getInt(Constants.DONT_BE_PERSISTENT) == 1);
    					person.setDontBeDrunk(json.getInt(Constants.DONT_BE_DRUNK) == 1);
    					person.setPersonalAdvice(json.getString(Constants.PERSONAL_ADVICE));
    					
    					// PICS
    					/*
    	            	JSONArray jsonProfilesPicsFilenames = json.getJSONArray(Constants.FILENAMES);
    	                ArrayList<String> filenames = new ArrayList<String>();
    	                
    	                for(int i = 0; i < jsonProfilesPicsFilenames.length(); i++)
    	                	filenames.add(jsonProfilesPicsFilenames.getString(i));
    	                
    	                person.setPicsFilenames(filenames);
    	                */
    	                
    	                
    					// MORE
    					person.setTonight(json.getString(Constants.TONIGHT));
    					person.setDontApproach(json.getInt(Constants.DONT_APPROACH) == 1);
    					person.setLookingFor(json.getInt(Constants.LOOKING_FOR));
    	           
    					
    					// STATUS
    					person.setFlirted(json.getInt(Constants.FLIRTED) == 1);
    					person.setVoted(json.getInt(Constants.VOTED) == 1);
    					person.setBlocked(json.getInt(Constants.BLOCKED) == 1);
    					person.setCanBeVoted(json.getInt(Constants.CAN_BE_VOTED) == 1);
    
    
    					mPeople.add(person);
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
            	mMethod.fireProfilesTaskEvent();
            }            
        }
 
        // These should never be accessed from within doInBackground()
        ProfilesTaskMethod mMethod = null;
        SherlockFragmentActivity mActivity = null;
        
        // Results
        boolean mSuccess = false;
        ArrayList<Person> mPeople = new ArrayList<Person>();
    }
    

	// RESULTS
	public boolean success()
	{
		return mProfilesTask.mSuccess;
	}
	
    public ArrayList<Person> getPeople()
    {
    	return mProfilesTask.mPeople;
    }
    
    
    // EVENTS
	public void addProfilesTaskListener(ProfilesTaskListener l) 
	{
		this.mProfilesTaskListeners.add(l);
	}

	public void removeProfilesTaskListener(ProfilesTaskListener l) 
	{
	    this.mProfilesTaskListeners.remove(l);
	}
	
	public void fireProfilesTaskEvent() {
		ProfilesTaskEvent evt = new ProfilesTaskEvent(this);

	    for (ProfilesTaskListener l : mProfilesTaskListeners) 
    	{
	    	if( l != null)
	    		l.profilesTaskEventReceived(evt);
	    }
	}
	
	public class ProfilesTaskEvent extends EventObject {
		private static final long serialVersionUID = 0;
		  public ProfilesTaskEvent(Object source) {
		    super(source);
		  }
		}
	
	public interface ProfilesTaskListener {
		void profilesTaskEventReceived(ProfilesTaskEvent evt);
	}
}
