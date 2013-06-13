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


public class MyProfileEditApproachTaskMethod
{
    private CopyOnWriteArrayList<MyProfileEditApproachTaskListener> mMyProfileEditApproachTaskListeners;
    
    private SherlockFragmentActivity mAct = null;;
    private ApproachTask mMyProfileEditApproachTask = null;
    
    public MyProfileEditApproachTaskMethod(SherlockFragmentActivity act)
    {
        mAct = act;
        mMyProfileEditApproachTaskListeners = new CopyOnWriteArrayList<MyProfileEditApproachTaskListener>();
        mMyProfileEditApproachTask = new ApproachTask();
    }
    
    
    public void doTask( Long userId, boolean tipComeAndSayHi, boolean tipBuyMeADrink, boolean tipInviteMeToDance, boolean tipMakeMeLaugh, boolean tipMeetMyFriends, boolean tipSurpriseMe, 
                        boolean dontExpectAnything, boolean dontBePersistent, boolean dontBeDrunk, String extraAdvice )
    {
        mMyProfileEditApproachTask = new ApproachTask();
        mMyProfileEditApproachTask.mActivity = mAct;
        mMyProfileEditApproachTask.mMethod = this;
        
        MyParams params = new MyParams();
        params.mUserId = userId;
        params.mTipComeAndSayHi = tipComeAndSayHi;
        params.mTipBuyMeADrink = tipBuyMeADrink;
        params.mTipMakeMeLaugh = tipMakeMeLaugh;
        params.mTipMeetMyFriends = tipMeetMyFriends;
        params.mTipInviteMeToDance = tipInviteMeToDance;
        params.mTipSurpriseMe = tipSurpriseMe;
        params.mDontExpectAnything = dontExpectAnything;
        params.mDontBePersistent = dontBePersistent;
        params.mDontBeDrunk = dontBeDrunk;
        params.mPersonalAdvice = extraAdvice;
        
        mMyProfileEditApproachTask.execute(params);
    }
    
    public void cleanUp()
    {
        mMyProfileEditApproachTask.mActivity = null;
        
        if (mAct.isFinishing())
            mMyProfileEditApproachTask.cancel(false);
    }
    
    class MyParams
    {
        Long mUserId;
        boolean mTipComeAndSayHi;
        boolean mTipBuyMeADrink;
        boolean mTipInviteMeToDance;
        boolean mTipMakeMeLaugh;
        boolean mTipMeetMyFriends;
        boolean mTipSurpriseMe;
        boolean mDontExpectAnything;
        boolean mDontBePersistent;
        boolean mDontBeDrunk;
        String mPersonalAdvice;
    }
    
    static class ApproachTask extends AsyncTask<MyParams, Void, String>
    {
        @Override
        protected String doInBackground(MyParams... params)   
        {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://ec2-107-22-191-241.compute-1.amazonaws.com/myProfileEditApproach.php");

            try {
                
                JSONObject json = new JSONObject();
            
                json.put(Constants.USER_ID, params[0].mUserId);
                json.put(Constants.TIP_COME_AND_SAY_HI, params[0].mTipComeAndSayHi ? 1 : 0);
                json.put(Constants.TIP_BUY_ME_A_DRINK, params[0].mTipBuyMeADrink ? 1 : 0);
                json.put(Constants.TIP_INVITE_ME_TO_DANCE, params[0].mTipInviteMeToDance ? 1 : 0);
                json.put(Constants.TIP_MAKE_ME_LAUGH, params[0].mTipMakeMeLaugh ? 1 : 0);
                json.put(Constants.TIP_MEET_MY_FRIENDS, params[0].mTipMeetMyFriends ? 1 : 0);
                json.put(Constants.TIP_SURPRISE_ME, params[0].mTipSurpriseMe ? 1 : 0);
                json.put(Constants.DONT_EXPECT_ANYTHING, params[0].mDontExpectAnything ? 1 : 0);
                json.put(Constants.DONT_BE_PERSISTENT, params[0].mDontBePersistent ? 1 : 0);
                json.put(Constants.DONT_BE_DRUNK, params[0].mDontBeDrunk ? 1 : 0);
                json.put(Constants.PERSONAL_ADVICE, params[0].mPersonalAdvice);
                
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
                mMethod.fireMyProfileEditApproachTaskEvent();
            }            
        }
 
        // These should never be accessed from within doInBackground()
        MyProfileEditApproachTaskMethod mMethod = null;
        SherlockFragmentActivity mActivity = null;
       
        boolean mSuccess = false;
    }
    
    // RESULTS
    public boolean success()
    {
        return mMyProfileEditApproachTask.mSuccess;
    }
    
    
    // EVENTS
    public void addMyProfileEditApproachTaskListener(MyProfileEditApproachTaskListener l) 
    {
        this.mMyProfileEditApproachTaskListeners.add(l);
    }

    public void removeMyProfileEditApproachTaskListener(MyProfileEditApproachTaskListener l) 
    {
        this.mMyProfileEditApproachTaskListeners.remove(l);
    }
    
    public void fireMyProfileEditApproachTaskEvent() {
        MyProfileEditApproachTaskEvent evt = new MyProfileEditApproachTaskEvent(this);

        for (MyProfileEditApproachTaskListener l : mMyProfileEditApproachTaskListeners) 
        {
            if( l != null)
                l.myProfileEditApproachTaskEventReceived(evt);
        }
    }
    
    public class MyProfileEditApproachTaskEvent extends EventObject {
        private static final long serialVersionUID = 0;
          public MyProfileEditApproachTaskEvent(Object source) {
            super(source);
          }
        }
    
    public interface MyProfileEditApproachTaskListener {
        void myProfileEditApproachTaskEventReceived(MyProfileEditApproachTaskEvent evt);
    }
}
