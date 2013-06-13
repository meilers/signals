package com.signals_app.signals.tasks.rendezvous;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.signals_app.signals.model.Constants;
import com.signals_app.signals.model.Place;
import com.signals_app.signals.model.Profile.GpsPosition;
import com.signals_app.signals.model.Profile.Person;
import com.signals_app.signals.model.rendezvous.Rendezvous;
import com.signals_app.signals.model.rendezvous.PotentialRendezvous;
import com.signals_app.signals.tasks.rendezvous.CandidatesTaskMethod.MyParams;
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

import android.app.AlertDialog;
import android.os.AsyncTask;
import android.util.Log;


public class SuggestRendezvousTaskMethod
{
    private CopyOnWriteArrayList<SuggestRendezvousTaskListener> mSuggestRendezvousTaskListeners;
    
    private SherlockFragmentActivity mAct = null;;
    private SuggestRendezvousTask mSuggestRendezvousTask = null;
    
    private Person mPerson;
    
    public SuggestRendezvousTaskMethod(SherlockFragmentActivity act)
    {
        mAct = act;
        mSuggestRendezvousTaskListeners = new CopyOnWriteArrayList<SuggestRendezvousTaskListener>();
        mSuggestRendezvousTask = new SuggestRendezvousTask();
    }
    
    
    public void doTask(Long userId, Long rendezvousId, ArrayList<Long> placesSelected, ArrayList<Boolean> timesSelected)
    {
        mSuggestRendezvousTask = new SuggestRendezvousTask();
        mSuggestRendezvousTask.mActivity = mAct;
        mSuggestRendezvousTask.mMethod = this;
        
        MyParams params = new MyParams();
        params.mUserId = userId;
        params.mRendezvousId = rendezvousId;
        params.mPlacesSelected = placesSelected;
        params.mTimesSelected = timesSelected;
        
        
        
        mSuggestRendezvousTask.execute(params);
    }
    
    class MyParams
    {
        Long mUserId;
        Long mRendezvousId;
        ArrayList<Long> mPlacesSelected;
        ArrayList<Boolean> mTimesSelected;
    }
    
    public void cleanUp()
    { 
        mAct.setSupportProgressBarIndeterminateVisibility(false);
        mSuggestRendezvousTask.mActivity = null;
        
        if (mAct.isFinishing())
            mSuggestRendezvousTask.cancel(false);
    }
    
    static class SuggestRendezvousTask extends AsyncTask<MyParams, Void, String>
    {
        @Override
        protected String doInBackground(MyParams... params)  
        {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://ec2-107-22-191-241.compute-1.amazonaws.com/rendezvous/rendezvousSuggest.php");

            try {
                JSONObject json = new JSONObject();

                ArrayList<Integer> timesSelected = new ArrayList<Integer>();
                
                for( int i=0; i < params[0].mTimesSelected.size(); ++i )
                    timesSelected.add(params[0].mTimesSelected.get(i) ? 1:0);
                
                json.put(Constants.USER_ID, params[0].mUserId);
                json.put(Constants.RENDEZVOUS_ID, params[0].mRendezvousId);
                json.put(Constants.PLACES_SELECTED, params[0].mPlacesSelected);
                json.put(Constants.TIMES_SELECTED, timesSelected);
                
                
                StringEntity se = new StringEntity(json.toString());
                se.setContentType("application/json;charset=UTF-8");
                httppost.setEntity(se);

                // Execute HTTP Post 
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
                // TODO Auto-generated catch suggestRendezvous
            } catch (IOException e) {
                mActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mActivity);
                        alertDialogBuilder.setTitle("Connection Error");
                        alertDialogBuilder
                        .setMessage("You've been disconnected from the internet.")
                        .setCancelable(true);
                        
                        AlertDialog alertDialog = alertDialogBuilder.create();
                         
                        // show it
                        alertDialog.show();
                    }});
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
            mError = 0;
            
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
                mMethod.fireSuggestRendezvousTaskEvent();
                
                
            }            
        }
 
        // These should never be accessed from within doInBackground()
        SuggestRendezvousTaskMethod mMethod = null;
        SherlockFragmentActivity mActivity = null;
        
        boolean mSuccess = false;
        int mError = 0;
        
        PotentialRendezvous mPotentialRendezvous = null;
    }
    
    public boolean success()
    {
        return mSuggestRendezvousTask.mSuccess;
    }
    
    public int getError()
    {
        return mSuggestRendezvousTask.mError;
    }
    
    public PotentialRendezvous getPotentialRendezvous()
    {
        return mSuggestRendezvousTask.mPotentialRendezvous;
    }
    
    public void addSuggestRendezvousTaskListener(SuggestRendezvousTaskListener l) 
    {
        this.mSuggestRendezvousTaskListeners.add(l);
    }

    public void removeSuggestRendezvousTaskListener(SuggestRendezvousTaskListener l) 
    {
        this.mSuggestRendezvousTaskListeners.remove(l);
    }
    
    public void fireSuggestRendezvousTaskEvent() {
        SuggestRendezvousTaskEvent evt = new SuggestRendezvousTaskEvent(this);

        for (SuggestRendezvousTaskListener l : mSuggestRendezvousTaskListeners) 
        {
            if( l != null)
                l.suggestRendezvousTaskEventReceived(evt);
        }
    }
    
    public class SuggestRendezvousTaskEvent extends EventObject {
        private static final long serialVersionUID = 0;
                
          // This event definition is stateless but you could always
          // add other information here.
          public SuggestRendezvousTaskEvent(Object source) {
            super(source);
          }
        }
    
    public interface SuggestRendezvousTaskListener {
        void suggestRendezvousTaskEventReceived(SuggestRendezvousTaskEvent evt);
    }
}
