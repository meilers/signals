package com.signals_app.signals.tasks;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.signals_app.signals.R;
import com.signals_app.signals.model.Constants;
import com.signals_app.signals.model.Profile.Person;
import com.signals_app.signals.tasks.RegisterTaskMethod.MyParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.EventObject;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;


public class MyProfileEditBasicInfoTaskMethod
{
    private CopyOnWriteArrayList<MyProfileEditBasicInfoTaskListener> mMyProfileEditBasicInfoTaskListeners;
    
    private SherlockFragmentActivity mAct = null;;
    private BasicInfoTask mMyProfileEditBasicInfoTask = null;
    private ProgressDialog mProgressDialog;
    
    public MyProfileEditBasicInfoTaskMethod(SherlockFragmentActivity act)
    {
        mAct = act;
        mMyProfileEditBasicInfoTaskListeners = new CopyOnWriteArrayList<MyProfileEditBasicInfoTaskListener>();
        mMyProfileEditBasicInfoTask = new BasicInfoTask();
        
        
        mProgressDialog = new ProgressDialog(act);
        mProgressDialog.setCancelable(false);
    }
    
    
    public void doTask( Long userId, String username, int sex, int interestedIn, int wantedMinAge, int wantedMaxAge, int lookingFor, Calendar birthday )
    {
        mMyProfileEditBasicInfoTask = new BasicInfoTask();
        mMyProfileEditBasicInfoTask.mActivity = mAct;
        mMyProfileEditBasicInfoTask.mMethod = this;
        
        MyParams params = new MyParams();
        params.mUserId = userId;
        params.mUsername = username;
        params.mSex = sex;
        params.mInterestedIn = interestedIn;
        params.mWantedMinAge = wantedMinAge;
        params.mWantedMaxAge = wantedMaxAge;
        params.mLookingFor = lookingFor;
        params.mBirthday = birthday;
        
        mMyProfileEditBasicInfoTask.execute(params);
    }
    
    public void cleanUp()
    {
        mMyProfileEditBasicInfoTask.mActivity = null;
        
        if (mAct.isFinishing())
            mMyProfileEditBasicInfoTask.cancel(false);
    }
    
    class MyParams
    {
        Long mUserId;
        String mUsername;
        int mSex;
        int mInterestedIn;
        int mWantedMinAge;
        int mWantedMaxAge;
        int mLookingFor;
        Calendar mBirthday;
    }
    
    static class BasicInfoTask extends AsyncTask<MyParams, Void, String>
    {
        @Override
        protected String doInBackground(MyParams... params)   
        {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://ec2-107-22-191-241.compute-1.amazonaws.com/myProfileEditBasicInfo.php");

            try {
                
                JSONObject json = new JSONObject();
            
                json.put(Constants.USER_ID, params[0].mUserId);
                json.put(Constants.USERNAME, params[0].mUsername);
                json.put(Constants.SEX, params[0].mSex);
                json.put(Constants.INTERESTED_IN, params[0].mInterestedIn);
                json.put(Constants.WANTED_MIN_AGE, params[0].mWantedMinAge);
                json.put(Constants.WANTED_MAX_AGE, params[0].mWantedMaxAge);
                json.put(Constants.LOOKING_FOR, params[0].mLookingFor);
                
                Calendar birthday = params[0].mBirthday;
                String birthdayStr = birthday.get(Calendar.YEAR) + "-" + (birthday.get(Calendar.MONTH)+1) + "-" + birthday.get(Calendar.DAY_OF_MONTH);
                
                json.put(Constants.BIRTHDAY, birthdayStr);
                
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
            if (mActivity != null && mMethod != null)
            {
                mActivity.setSupportProgressBarIndeterminateVisibility(true);
            }
        }
        
        
        @Override
        protected void onPostExecute(String result) 
        {
            super.onPostExecute(result);
            
            JSONObject json;
            mSuccess = false;
            
            try  
            {
                json = new JSONObject(result);
             
                if(json.getInt(Constants.RESULT_OK) != 0)
                {
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
                mMethod.fireMyProfileEditBasicInfoTaskEvent();
                
            }            
        }
 
        // These should never be accessed from within doInBackground()
        MyProfileEditBasicInfoTaskMethod mMethod = null;
        SherlockFragmentActivity mActivity = null;
       
        boolean mSuccess = false;
    }
    
    // RESULTS
    public boolean success()
    {
        return mMyProfileEditBasicInfoTask.mSuccess;
    }
    
    
    // EVENTS
    public void addMyProfileEditBasicInfoTaskListener(MyProfileEditBasicInfoTaskListener l) 
    {
        this.mMyProfileEditBasicInfoTaskListeners.add(l);
    }

    public void removeMyProfileEditBasicInfoTaskListener(MyProfileEditBasicInfoTaskListener l) 
    {
        this.mMyProfileEditBasicInfoTaskListeners.remove(l);
    }
    
    public void fireMyProfileEditBasicInfoTaskEvent() {
        MyProfileEditBasicInfoTaskEvent evt = new MyProfileEditBasicInfoTaskEvent(this);

        for (MyProfileEditBasicInfoTaskListener l : mMyProfileEditBasicInfoTaskListeners) 
        {
            if( l != null)
                l.myProfileEditBasicInfoTaskEventReceived(evt);
        }
    }
    
    public class MyProfileEditBasicInfoTaskEvent extends EventObject {
        private static final long serialVersionUID = 0;
          public MyProfileEditBasicInfoTaskEvent(Object source) {
            super(source);
          }
        }
    
    public interface MyProfileEditBasicInfoTaskListener {
        void myProfileEditBasicInfoTaskEventReceived(MyProfileEditBasicInfoTaskEvent evt);
    }
}
