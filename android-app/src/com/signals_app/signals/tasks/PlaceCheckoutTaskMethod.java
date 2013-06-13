package com.signals_app.signals.tasks;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.signals_app.signals.model.Constants;
import com.signals_app.signals.model.Profile.Person;
import com.signals_app.signals.util.Utility;

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

import android.os.AsyncTask;
import android.util.Log;


public class PlaceCheckoutTaskMethod
{
    public final static int CHECK_OUT_BECAUSE_TOO_FAR = 1;
    public final static int CHECK_OUT_BY_USER = 1;
    
    private CopyOnWriteArrayList<PlaceCheckoutTaskListener> mPlaceCheckoutTaskListeners;
    
    private SherlockFragmentActivity mAct = null;;
    private PlaceCheckoutTask mPlaceCheckoutTask = null;
    
    private int mReason = 0;
    
    public PlaceCheckoutTaskMethod(SherlockFragmentActivity act)
    {
        mAct = act;
        mPlaceCheckoutTaskListeners = new CopyOnWriteArrayList<PlaceCheckoutTaskListener>();
        mPlaceCheckoutTask = new PlaceCheckoutTask();
    }
    
    
    public void doTask( Long userId, int reason )
    {
        mReason = reason;
        
        mPlaceCheckoutTask = new PlaceCheckoutTask();
        mPlaceCheckoutTask.mActivity = mAct;
        mPlaceCheckoutTask.mMethod = this;
        
        MyParams params = new MyParams();
        params.mUserId = userId;
        
        mPlaceCheckoutTask.execute(params);
    }
    
    public void cleanUp()
    {
        mAct.setSupportProgressBarIndeterminateVisibility(false);
        mPlaceCheckoutTask.mActivity = null;
        
        if (mAct.isFinishing())
            mPlaceCheckoutTask.cancel(false);
    }
    
    class MyParams
    {
        Long mUserId; 
    } 
    
    static class PlaceCheckoutTask extends AsyncTask<MyParams, Void, String>
    {
        @Override
        protected String doInBackground(MyParams... params)  
        {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://ec2-107-22-191-241.compute-1.amazonaws.com/checkOutPlace.php");

            try {
                
                JSONObject json = new JSONObject();
            
                json.put(Constants.USER_ID, params[0].mUserId);
                
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
                mMethod.firePlaceCheckoutTaskEvent();
            }            
        }
        

        
        // These should never be accessed from within doInBackground()
        PlaceCheckoutTaskMethod mMethod = null;
        SherlockFragmentActivity mActivity = null;
              
        boolean mSuccess = false;
    }
    
    // RESULTS
    public boolean success()
    {
        return mPlaceCheckoutTask.mSuccess;
    }
    
    public int getReason()
    {
        return mReason;
    }
    
    // EVENTS
    public void addPlaceCheckoutTaskListener(PlaceCheckoutTaskListener l) 
    {
        this.mPlaceCheckoutTaskListeners.add(l);
    }

    public void removePlaceCheckoutTaskListener(PlaceCheckoutTaskListener l) 
    {
        this.mPlaceCheckoutTaskListeners.remove(l);
    }
    
    public void firePlaceCheckoutTaskEvent() {
        PlaceCheckoutTaskEvent evt = new PlaceCheckoutTaskEvent(this);

        for (PlaceCheckoutTaskListener l : mPlaceCheckoutTaskListeners) 
        {
            if( l != null)
                l.placeCheckoutTaskEventReceived(evt);
        }
    }
    
    public class PlaceCheckoutTaskEvent extends EventObject {
        private static final long serialVersionUID = 0;
          public PlaceCheckoutTaskEvent(Object source) {
            super(source);
          }
        }
    
    public interface PlaceCheckoutTaskListener {
        void placeCheckoutTaskEventReceived(PlaceCheckoutTaskEvent evt);
    }
}

