package com.signals_app.signals.tasks;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.signals_app.signals.model.Constants;
import com.signals_app.signals.model.Profile.Person;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

import android.os.AsyncTask;
import android.util.Log;


public class MyProfileEditTonightTaskMethod
{
    private CopyOnWriteArrayList<MyProfileEditTonightTaskListener> mMyProfileEditTonightTaskListeners;
    
    private SherlockFragmentActivity mAct = null;;
    private TonightTask mMyProfileEditTonightTask = null;
    
    public MyProfileEditTonightTaskMethod(SherlockFragmentActivity act)
    {
        mAct = act;
        mMyProfileEditTonightTaskListeners = new CopyOnWriteArrayList<MyProfileEditTonightTaskListener>();
        mMyProfileEditTonightTask = new TonightTask();
    }
    
    
    public void doTask( Long userId, String tonightText )
    {
        mMyProfileEditTonightTask = new TonightTask();
        mMyProfileEditTonightTask.mActivity = mAct;
        mMyProfileEditTonightTask.mMethod = this;
        
        MyParams params = new MyParams();
        params.mUserId = userId;
        params.mTonightText = tonightText;
        
        mMyProfileEditTonightTask.execute(params);
    }
    
    public void cleanUp()
    {
        mAct.setSupportProgressBarIndeterminateVisibility(false);
        mMyProfileEditTonightTask.mActivity = null;
        
        if (mAct.isFinishing())
            mMyProfileEditTonightTask.cancel(false);
    }
    
    class MyParams
    {
        Long mUserId;
        String mTonightText;
    }
    
    static class TonightTask extends AsyncTask<MyParams, Void, String>
    {
        @Override
        protected String doInBackground(MyParams... params)   
        {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://ec2-107-22-191-241.compute-1.amazonaws.com/myProfileEditTonight.php");

            try {
                
                JSONObject json = new JSONObject();
            
                json.put(Constants.USER_ID, params[0].mUserId);
                json.put(Constants.TONIGHT, params[0].mTonightText);
                
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
                mMethod.fireMyProfileEditTonightTaskEvent();
                
            }            
        }
 
        // These should never be accessed from within doInBackground()
        MyProfileEditTonightTaskMethod mMethod = null;
        SherlockFragmentActivity mActivity = null;
       
        boolean mSuccess = false;
    }
    
    // RESULTS
    public boolean success()
    {
        return mMyProfileEditTonightTask.mSuccess;
    }
    
    
    // EVENTS
    public void addMyProfileEditTonightTaskListener(MyProfileEditTonightTaskListener l) 
    {
        this.mMyProfileEditTonightTaskListeners.add(l);
    }

    public void removeMyProfileEditTonightTaskListener(MyProfileEditTonightTaskListener l) 
    {
        this.mMyProfileEditTonightTaskListeners.remove(l);
    }
    
    public void fireMyProfileEditTonightTaskEvent() {
        MyProfileEditTonightTaskEvent evt = new MyProfileEditTonightTaskEvent(this);

        for (MyProfileEditTonightTaskListener l : mMyProfileEditTonightTaskListeners) 
        {
            if( l != null)
                l.myProfileEditTonightTaskEventReceived(evt);
        }
    }
    
    public class MyProfileEditTonightTaskEvent extends EventObject {
        private static final long serialVersionUID = 0;
          public MyProfileEditTonightTaskEvent(Object source) {
            super(source);
          }
        }
    
    public interface MyProfileEditTonightTaskListener {
        void myProfileEditTonightTaskEventReceived(MyProfileEditTonightTaskEvent evt);
    }
}
