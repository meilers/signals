package com.signals_app.signals.tasks;

import com.actionbarsherlock.app.SherlockFragmentActivity;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.app.AlertDialog;
import android.os.AsyncTask;
import android.util.Log;


import com.signals_app.signals.activity.RegisterActivity;
import com.signals_app.signals.model.Profile.User;
import com.signals_app.signals.tasks.LoginTaskMethod.MyParams;

public class RegisterTaskMethod
{
	public static final int ERROR_USERNAME_UNAVAILABLE = 1;
	public static final int ERROR_EMAIL_UNAVAILABLE = 2;

	private CopyOnWriteArrayList<RegisterTaskListener> mRegisterTaskListeners;
	
	private SherlockFragmentActivity mAct = null;
	private RegisterTask mRegisterTask = null;
	
	public RegisterTaskMethod(SherlockFragmentActivity act)
	{
		mAct = act;
		mRegisterTaskListeners = new CopyOnWriteArrayList<RegisterTaskListener>();
		mRegisterTask = new RegisterTask();
	}
	
	
	public void doTask(String facebookId, String email, String username, int sex, int interestedIn, int wantedMinAge, int wantedMaxAge, int lookingFor, Calendar birthday, int cityId)
	{
    	mRegisterTask = new RegisterTask();
    	mRegisterTask.mActivity = mAct;
    	mRegisterTask.mMethod = this;

    	
    	MyParams params = new MyParams();
        params.mFacebookId = facebookId;
        params.mEmail = email;
    	params.mUsername = username;
    	params.mSex = sex;
    	params.mInterestedIn = interestedIn;
    	params.mWantedMinAge = wantedMinAge;
    	params.mWantedMaxAge = wantedMaxAge;
        params.mLookingFor = lookingFor;
    	params.mBirthday = birthday;
        params.mCityId = cityId;

    	mRegisterTask.execute(params);
	}
	
	public void cleanUp()
	{
		mRegisterTask.mActivity = null;
    	mRegisterTask.mMethod = null;
        
        if (mAct.isFinishing())
        	mRegisterTask.cancel(false);
	}
	
	class MyParams
	{
        String mFacebookId;
        String mEmail;
		String mUsername;
		int mSex;
		int mInterestedIn;
		int mWantedMinAge;
		int mWantedMaxAge;
        int mLookingFor;
		Calendar mBirthday;
        int mCityId;
	} 
	
	
    static class RegisterTask extends AsyncTask<MyParams, Void, String>
    {
        @Override
        protected String doInBackground(MyParams... params)
        {
		    HttpClient httpclient = new DefaultHttpClient();
		    HttpPost httppost = new HttpPost("http://ec2-107-22-191-241.compute-1.amazonaws.com/register.php");

		    try {
		    	JSONObject json = new JSONObject();

                json.put(Constants.FACEBOOK_ID, params[0].mFacebookId);
                json.put(Constants.EMAIL, params[0].mEmail);
		    	json.put(Constants.USERNAME, params[0].mUsername);
		    	
		    	// LOGIN BASICS
		    	
		    	json.put(Constants.SEX, params[0].mSex);
		    	json.put(Constants.INTERESTED_IN, params[0].mInterestedIn);
		    	json.put(Constants.WANTED_MIN_AGE, params[0].mWantedMinAge);
		    	json.put(Constants.WANTED_MAX_AGE, params[0].mWantedMaxAge);
                json.put(Constants.LOOKING_FOR, params[0].mLookingFor);

		    	Calendar birthday = params[0].mBirthday;
		    	String birthdayStr = birthday.get(Calendar.YEAR) + "-" + (birthday.get(Calendar.MONTH)+1) + "-" + birthday.get(Calendar.DAY_OF_MONTH);
		    	
		    	json.put(Constants.BIRTHDAY, birthdayStr);
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

			User u = User.getInstance();
			JSONObject json;
			
			mSuccess = false;
			
	        try 
	        {
	        	json = new JSONObject(result);
	        	 
	            if(json.getInt(Constants.RESULT_OK) != 0)
	            {
	            	json = json.getJSONObject(Constants.RESULT);
	   	         
	                u.setUserId(Long.parseLong(json.getString(Constants.USER_ID)));
	                u.setAccessToken(json.getString(Constants.ACCESS_TOKEN));
	            
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
            	mMethod.fireRegisterTaskEvent();
            }            
        }
 
        // These should never be accessed from within doInBackground()
        SherlockFragmentActivity mActivity = null;
        RegisterTaskMethod mMethod = null;
        
        private boolean mSuccess = false;
        private int mError = 0;
    }
    
	// RESULTS
	public boolean success()
	{
		return mRegisterTask.mSuccess;
	}
	
    public int getError()
    {
    	return mRegisterTask.mError;
    }
    
    
    // EVENTS
	public void addRegisterTaskListener(RegisterTaskListener l) 
	{
		this.mRegisterTaskListeners.add(l);
	}

	public void removeRegisterTaskListener(RegisterTaskListener l) 
	{
	    this.mRegisterTaskListeners.remove(l);
	}
	
	public void fireRegisterTaskEvent() {
		RegisterTaskEvent evt = new RegisterTaskEvent(this);

	    for (RegisterTaskListener l : mRegisterTaskListeners) 
    	{
	    	if( l != null)
	    		l.registerTaskEventReceived(evt);
	    }
	}
	
	public class RegisterTaskEvent extends EventObject {
		private static final long serialVersionUID = 0;
		  public RegisterTaskEvent(Object source) {
		    super(source);
		  }
		}
	
	public interface RegisterTaskListener {
		void registerTaskEventReceived(RegisterTaskEvent evt);
	}
	


}
