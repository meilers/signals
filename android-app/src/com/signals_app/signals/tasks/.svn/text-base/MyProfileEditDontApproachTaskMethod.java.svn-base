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


public class MyProfileEditDontApproachTaskMethod
{
    private CopyOnWriteArrayList<MyProfileEditDontApproachTaskListener> mMyProfileEditDontApproachTaskListeners;
    
    private SherlockFragmentActivity mAct = null;;
    private DontApproachTask mMyProfileEditDontApproachTask = null;
    
    public MyProfileEditDontApproachTaskMethod(SherlockFragmentActivity act)
    {
        mAct = act;
        mMyProfileEditDontApproachTaskListeners = new CopyOnWriteArrayList<MyProfileEditDontApproachTaskListener>();
        mMyProfileEditDontApproachTask = new DontApproachTask();
    }
    
    
    public void doTask( Long userId, boolean dontApproach )
    {
        mMyProfileEditDontApproachTask = new DontApproachTask();
        mMyProfileEditDontApproachTask.mActivity = mAct;
        mMyProfileEditDontApproachTask.mMethod = this;
        
        MyParams params = new MyParams();
        params.mUserId = userId;
        params.mDontApproach = dontApproach;
        
        mMyProfileEditDontApproachTask.execute(params);
    }
    
    public void cleanUp()
    {
        mAct.setSupportProgressBarIndeterminateVisibility(false);
        mMyProfileEditDontApproachTask.mActivity = null;
        
        if (mAct.isFinishing())
            mMyProfileEditDontApproachTask.cancel(false);
    }
    
    class MyParams
    {
        Long mUserId;
        boolean mDontApproach;
    }
    
    static class DontApproachTask extends AsyncTask<MyParams, Void, String>
    {
        @Override
        protected String doInBackground(MyParams... params)   
        {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://ec2-107-22-191-241.compute-1.amazonaws.com/myProfileEditDontApproach.php");

            try {
                
                JSONObject json = new JSONObject();
            
                json.put(Constants.USER_ID, params[0].mUserId);
                json.put(Constants.DONT_APPROACH, params[0].mDontApproach ? 1 : 0);
                
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
                mMethod.fireMyProfileEditDontApproachTaskEvent();
            }            
        }
 
        // These should never be accessed from within doInBackground()
        MyProfileEditDontApproachTaskMethod mMethod = null;
        SherlockFragmentActivity mActivity = null;
       
        boolean mSuccess = false;
    }
    
    // RESULTS
    public boolean success()
    {
        return mMyProfileEditDontApproachTask.mSuccess;
    }
    
    
    // EVENTS
    public void addMyProfileEditDontApproachTaskListener(MyProfileEditDontApproachTaskListener l) 
    {
        this.mMyProfileEditDontApproachTaskListeners.add(l);
    }

    public void removeMyProfileEditDontApproachTaskListener(MyProfileEditDontApproachTaskListener l) 
    {
        this.mMyProfileEditDontApproachTaskListeners.remove(l);
    }
    
    public void fireMyProfileEditDontApproachTaskEvent() {
        MyProfileEditDontApproachTaskEvent evt = new MyProfileEditDontApproachTaskEvent(this);

        for (MyProfileEditDontApproachTaskListener l : mMyProfileEditDontApproachTaskListeners) 
        {
            if( l != null)
                l.myProfileEditDontApproachTaskEventReceived(evt);
        }
    }
    
    public class MyProfileEditDontApproachTaskEvent extends EventObject {
        private static final long serialVersionUID = 0;
          public MyProfileEditDontApproachTaskEvent(Object source) {
            super(source);
          }
        }
    
    public interface MyProfileEditDontApproachTaskListener {
        void myProfileEditDontApproachTaskEventReceived(MyProfileEditDontApproachTaskEvent evt);
    }
}
