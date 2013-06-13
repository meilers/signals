package com.signals_app.signals.tasks;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.signals_app.signals.R;
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

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;


public class MyProfileEditLookingForTaskMethod
{
    private CopyOnWriteArrayList<MyProfileEditLookingForTaskListener> mMyProfileEditLookingForTaskListeners;
    
    private SherlockFragmentActivity mAct = null;;
    private LookingForTask mMyProfileEditLookingForTask = null;
    private ProgressDialog mProgressDialog;
    
    public MyProfileEditLookingForTaskMethod(SherlockFragmentActivity act)
    {
        mAct = act;
        mMyProfileEditLookingForTaskListeners = new CopyOnWriteArrayList<MyProfileEditLookingForTaskListener>();
        mMyProfileEditLookingForTask = new LookingForTask();
        
        
        
        mProgressDialog = new ProgressDialog(act);
        mProgressDialog.setCancelable(false);
    }
    
    
    public void doTask( Long userId, int lookingFor )
    {
        mMyProfileEditLookingForTask = new LookingForTask();
        mMyProfileEditLookingForTask.mActivity = mAct;
        mMyProfileEditLookingForTask.mMethod = this;
        
        MyParams params = new MyParams();
        params.mUserId = userId;
        params.mLookingFor = lookingFor;
        
        mMyProfileEditLookingForTask.execute(params);
    }
    
    public void cleanUp()
    {
        mMyProfileEditLookingForTask.mActivity = null;
        
        if (mAct.isFinishing())
            mMyProfileEditLookingForTask.cancel(false);
    }
    
    class MyParams
    {
        Long mUserId;
        int mLookingFor;
    }
    
    static class LookingForTask extends AsyncTask<MyParams, Void, String>
    {
        @Override
        protected String doInBackground(MyParams... params)   
        {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://ec2-107-22-191-241.compute-1.amazonaws.com/myProfileEditLookingFor.php");

            try {
                
                JSONObject json = new JSONObject();
            
                json.put(Constants.USER_ID, params[0].mUserId);
                json.put(Constants.LOOKING_FOR, params[0].mLookingFor);
                
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
                mMethod.mProgressDialog.setMessage(mActivity.getString(R.string.saving_changes));
                mMethod.mProgressDialog.show(); 
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
                mMethod.fireMyProfileEditLookingForTaskEvent();
            }            
        }
 
        // These should never be accessed from within doInBackground()
        MyProfileEditLookingForTaskMethod mMethod = null;
        SherlockFragmentActivity mActivity = null;
       
        boolean mSuccess = false;
    }
    
    // RESULTS
    public boolean success()
    {
        return mMyProfileEditLookingForTask.mSuccess;
    }
    
    
    // EVENTS
    public void addMyProfileEditLookingForTaskListener(MyProfileEditLookingForTaskListener l) 
    {
        this.mMyProfileEditLookingForTaskListeners.add(l);
    }

    public void removeMyProfileEditLookingForTaskListener(MyProfileEditLookingForTaskListener l) 
    {
        this.mMyProfileEditLookingForTaskListeners.remove(l);
    }
    
    public void fireMyProfileEditLookingForTaskEvent() {
        MyProfileEditLookingForTaskEvent evt = new MyProfileEditLookingForTaskEvent(this);

        for (MyProfileEditLookingForTaskListener l : mMyProfileEditLookingForTaskListeners) 
        {
            if( l != null)
                l.myProfileEditLookingForTaskEventReceived(evt);
        }
    }
    
    public class MyProfileEditLookingForTaskEvent extends EventObject {
        private static final long serialVersionUID = 0;
          public MyProfileEditLookingForTaskEvent(Object source) {
            super(source);
          }
        }
    
    public interface MyProfileEditLookingForTaskListener {
        void myProfileEditLookingForTaskEventReceived(MyProfileEditLookingForTaskEvent evt);
    }
}
