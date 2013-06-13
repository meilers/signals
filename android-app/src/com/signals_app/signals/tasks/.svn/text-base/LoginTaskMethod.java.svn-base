package com.signals_app.signals.tasks;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.signals_app.signals.model.City;
import com.signals_app.signals.model.Constants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.EventObject;
import java.util.Vector;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import com.signals_app.signals.activity.LoginActivity;
import com.signals_app.signals.model.Profile.User;
import com.signals_app.signals.util.Utility;

public class LoginTaskMethod
{
	private CopyOnWriteArrayList<LoginTaskListener> mLoginTaskListeners;
	
	private SherlockFragmentActivity mAct = null;
	private LoginTask mLoginTask = null;

	public LoginTaskMethod(SherlockFragmentActivity act)
	{
		mAct = act;
		mLoginTaskListeners = new CopyOnWriteArrayList<LoginTaskListener>();
		mLoginTask = new LoginTask();
	}
	
	public void doTask(String facebookId)
	{
		mLoginTask = new LoginTask(); 
		mLoginTask.mActivity = mAct;
		mLoginTask.mMethod = this;
		
    	
    	MyParams params = new MyParams();
    	params.mFacebookId = facebookId;
    	mLoginTask.execute(params);
	}
	
	public void cleanUp()
	{
		mLoginTask.mActivity = null;
        
        if (mAct.isFinishing())
        	mLoginTask.cancel(false);
	}
	
	
	class MyParams
	{
		String mFacebookId;
	} 
	
	
    static class LoginTask extends AsyncTask<MyParams, Void, String>
    {
        @Override
        protected String doInBackground(MyParams... params)
        {
        	// Two levels to set the charset in the Android client
            // 1. The rest client itself itself
            // 2. The StringEntity 

        	HttpParams httpParameters = new BasicHttpParams();
        	HttpProtocolParams.setContentCharset(httpParameters, HTTP.UTF_8);
        	HttpProtocolParams.setHttpElementCharset(httpParameters, HTTP.UTF_8);
		    HttpClient httpclient = new DefaultHttpClient(httpParameters);
		    HttpPost httppost = new HttpPost("http://ec2-107-22-191-241.compute-1.amazonaws.com/login.php");
		    
		    try {
		    	User u = User.getInstance();
		    	
		    	JSONObject json = new JSONObject();
		    	json.put(Constants.FACEBOOK_ID, params[0].mFacebookId);
		    	
		    	
		    	/*
		    	json.put(Constants.SEX, u.getSex());
		    	
		    	JSONArray jsonArray = new JSONArray(u.getProfilePicsFilenames());
		    	json.put(Constants.PROFILE_PICS_URLS, jsonArray);

		    	json.put(Constants.TONIGHT, u.getTonight());
		    	json.put(Constants.HOMETOWN, u.getHometown());  
		    	
		    	Vector<Work> workArr = u.getWork();
		    	JSONArray workJsonArr = new JSONArray();
		    	
		    	for(int i=0; i < workArr.size(); ++i)
		    	{
		    		JSONObject wJson = new JSONObject();
		    		Work w = workArr.elementAt(i);
		    		
		    		if( !w.getEmployer().equals(""))
		    			wJson.put(Constants.WORK_EMP, w.getEmployer());
		    		
		    		if( !w.getLocation().equals(""))
		    			wJson.put(Constants.WORK_LOC, w.getLocation());
		    		
		    		if( !w.getPosition().equals(""))
		    			wJson.put(Constants.WORK_POS, w.getPosition());
		    		
		    		if( w.getStartDate() != null )
		    		{
		    			String wStartDateMonth = Integer.toString(w.getStartDate().get(Calendar.MONTH));
		    			String wStartDateYear = Integer.toString(w.getStartDate().get(Calendar.YEAR));
			    		wJson.put(Constants.WORK_SD_M, wStartDateMonth);
			    		wJson.put(Constants.WORK_SD_Y, wStartDateYear);
		    		}
		    		
		    		if( w.getEndDate() != null )
		    		{
		    			String wEndDateMonth = Integer.toString(w.getEndDate().get(Calendar.MONTH));
		    			String wEndDateYear = Integer.toString(w.getEndDate().get(Calendar.YEAR));
			    		wJson.put(Constants.WORK_ED_M, wEndDateMonth);
			    		wJson.put(Constants.WORK_ED_Y, wEndDateYear);
		    		}
		    		
		    		workJsonArr.put(wJson);
		    	}
		    	json.put(Constants.WORK, workJsonArr);
		    	
		    	
		    	Vector<Education> eduArr = u.getEducation();
		    	JSONArray eduJsonArr = new JSONArray();
		    	
		    	for(int i=0; i < eduArr.size(); ++i)
		    	{
		    		JSONObject eJson = new JSONObject();
		    		Education e = eduArr.elementAt(i);
		    		
		    		if( !e.getType().equals(""))
		    			eJson.put(Constants.EDUCATION_TYPE, e.getType());
		    		
		    		if( !e.getSchoolName().equals(""))
		    			eJson.put(Constants.EDUCATION_SCHOOL_NAME, e.getSchoolName());
		    		
		    		if( !e.getDegree().equals(""))
		    			eJson.put(Constants.EDUCATION_DEGREE, e.getDegree()); 
		    		
		    		if( !e.getConcentration().equals(""))
		    			eJson.put(Constants.EDUCATION_CONC, e.getConcentration());

		    		if( !e.getYear().equals(0))
		    			eJson.put(Constants.EDUCATION_YEAR, e.getYear().toString());
		    		
		    		eduJsonArr.put(eJson);
		    	}
		    	json.put(Constants.EDUCATION, eduJsonArr);
		    	
		    	
		    	jsonArray = new JSONArray(u.getLikesMusic());
		    	json.put(Constants.LIKES_MUSIC, jsonArray);
		    	
		    	jsonArray = new JSONArray(u.getLikesMovies());
		    	json.put(Constants.LIKES_MOVIES, jsonArray); 
		    	
		    	jsonArray = new JSONArray(u.getLikesTv());
		    	json.put(Constants.LIKES_TV, jsonArray);
		    	
		    	jsonArray = new JSONArray(u.getLikesBooks());
		    	json.put(Constants.LIKES_BOOKS, jsonArray);
		    	
		    	jsonArray = new JSONArray(u.getLikesOther());
		    	json.put(Constants.LIKES_OTHER, jsonArray);
		    	
		    	jsonArray = new JSONArray(u.getLanguages());
		    	json.put(Constants.LANGUAGES, jsonArray);
		    	
		    	json.put(Constants.ABOUT_ME, User.getInstance().getAboutMe());
		    	json.put(Constants.QUOTATIONS, User.getInstance().getQuotations());
		    	*/
		    	
		    	
		    	StringEntity se = new StringEntity(json.toString(), HTTP.UTF_8);
	            se.setContentType("application/json;charset=UTF-8");
	            httppost.setEntity(se);

		        // Execute HTTP Post Request
		        HttpResponse response = httpclient.execute(httppost);
				InputStream inputStream = response.getEntity().getContent();
				InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
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
 			
			User u = User.getInstance();
			JSONObject json;
			 
			mSuccess = false;  
			mError = 0;
			 
	        try  
	        { 
	        	json = new JSONObject(result); 
	        	  
	            if(json.getInt(Constants.RESULT_OK) != 0)
	            {   
	            	json = json.getJSONObject(Constants.RESULT); 
	            	
	            	// ACCOUNT
	                u.setUserId(Long.parseLong(json.getString(Constants.USER_ID))); 
	                u.setUsername(json.getString(Constants.USERNAME));
	                u.setAccessToken(json.getString(Constants.ACCESS_TOKEN));
	                u.setVip(json.getInt(Constants.VIP) == 1);
	                
	                
	                // BASIC INFO
	                u.setSex(json.getInt(Constants.SEX));
	                u.setInterestedIn(json.getInt(Constants.INTERESTED_IN));
	                u.setWantedMinAge(json.getInt(Constants.WANTED_MIN_AGE));
	                u.setWantedMaxAge(json.getInt(Constants.WANTED_MAX_AGE));
	                
	                String[] strArr = json.getString(Constants.BIRTHDAY).split("-");
					Calendar dob = Calendar.getInstance();
					dob.set(Integer.valueOf(strArr[0]), Integer.valueOf(strArr[1])-1, Integer.valueOf(strArr[2]));
					u.setBirthday(dob);
					u.setAge(Utility.getAge(dob));


                    // CITY
                    JSONObject jsonCity = json.getJSONObject(Constants.CITY);
                    int cityId = jsonCity.getInt(Constants.ID);
                    String cityName = jsonCity.getString(Constants.CITY);
                    String cityCountry = jsonCity.getString(Constants.COUNTRY);
                    City city = new City(cityId, cityName, cityCountry);
                    u.setCity(city);

                    mSuccess = true;
	            }
	            else
	            {
	            	mError = json.getInt(Constants.ERROR);
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
            	mMethod.fireLoginTaskEvent();
            }            
        }
 
        // These should never be accessed from within doInBackground()
        SherlockFragmentActivity mActivity = null;
        LoginTaskMethod mMethod = null;
        
        boolean mSuccess = false; 
        int mError = 0; 
        
    }

    public boolean success()
    {
    	return mLoginTask.mSuccess;
    }
    
    public int getError()
    {
    	return mLoginTask.mError;
    }
    
    
    
    
    
    
	public void addLoginTaskListener(LoginTaskListener l) 
	{
		this.mLoginTaskListeners.add(l);
	}

	public void removeLoginTaskListener(LoginTaskListener l) 
	{
	    this.mLoginTaskListeners.remove(l);
	}
	
	public void fireLoginTaskEvent() {
		LoginTaskEvent evt = new LoginTaskEvent(this);

	    for (LoginTaskListener l : mLoginTaskListeners) 
    	{
	    	if( l != null)
	    		l.loginTaskEventReceived(evt);
	    }
	}
	
	public class LoginTaskEvent extends EventObject {
		private static final long serialVersionUID = 0;
		
		  public LoginTaskEvent(Object source) {
		    super(source);
		  }
		}
	
	public interface LoginTaskListener {
		void loginTaskEventReceived(LoginTaskEvent evt);
	}

}
