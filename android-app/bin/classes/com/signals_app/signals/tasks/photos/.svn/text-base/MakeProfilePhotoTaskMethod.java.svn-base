package com.signals_app.signals.tasks.photos;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.signals_app.signals.R;
import com.signals_app.signals.model.Constants;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.EventObject;
import java.util.Vector;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import com.signals_app.signals.model.Profile.User;

public class MakeProfilePhotoTaskMethod
{

    private CopyOnWriteArrayList<MakeProfilePhotoTaskListener> mMakeProfilePhotoTaskListeners;
    
    private SherlockFragmentActivity mAct = null;
    private MakeProfilePhotoTask mMakeProfilePhotoTask = null;
    
    private ProgressDialog mProgressDialog;
    
    public MakeProfilePhotoTaskMethod(SherlockFragmentActivity act)
    {
        mAct = act;
        mMakeProfilePhotoTaskListeners = new CopyOnWriteArrayList<MakeProfilePhotoTaskListener>();
        mMakeProfilePhotoTask = new MakeProfilePhotoTask();
        
        mProgressDialog = new ProgressDialog(act);
        mProgressDialog.setCancelable(false);
    }
    
    
    public void doTask(Long userId, byte[] crop)
    {
        mMakeProfilePhotoTask = new MakeProfilePhotoTask();
        mMakeProfilePhotoTask.mActivity = mAct;
        mMakeProfilePhotoTask.mMethod = this;
        
        MyParams params = new MyParams();
        params.mUserId = userId;
        params.mCrop = crop;
        mMakeProfilePhotoTask.execute(params);
    }
    
    public void cleanUp()
    {
        mMakeProfilePhotoTask.mActivity = null;
        mMakeProfilePhotoTask.mMethod = null;
        
        if (mAct.isFinishing())
            mMakeProfilePhotoTask.cancel(false);
    }
    
    class MyParams
    {
        Long mUserId;
        byte[] mCrop;
    } 
    
    static class MakeProfilePhotoTask extends AsyncTask<MyParams, Void, String>
    {
        @Override 
        protected String doInBackground(MyParams... params)
        {
            String cropString = Base64.encodeToString(params[0].mCrop,Base64.DEFAULT);
            
               
            ArrayList<NameValuePair> nameValuePairs = new  ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair(Constants.USER_ID,String.valueOf(params[0].mUserId)));
            nameValuePairs.add(new BasicNameValuePair(Constants.STRING_BASE_64_CROP,cropString));
            
            try{
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://ec2-174-129-201-49.compute-1.amazonaws.com/makeProfilePhoto.php");
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs)); 
                  
                HttpResponse response = httpclient.execute(httppost);  
                BufferedReader reader = new BufferedReader(new InputStreamReader( response.getEntity().getContent(), "UTF-8"));  
                String sResponse = reader.readLine();  
                
                return sResponse;
                 
            } 
            catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
            } catch (IOException e) {

            }

            return "";
        }
 
        
        @Override
        protected void onPreExecute() 
        {
            if (mActivity != null && mMethod != null)
            {
                mMethod.mProgressDialog.setMessage(mActivity.getString(R.string.making_profile_picture));
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
                    mPicFilename = json.getString(Constants.RESULT);
                    
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
                mMethod.fireMakeProfilePhotoTaskEvent();
                
                mMethod.mProgressDialog.dismiss();
            }            
        }
 
        // These should never be accessed from within doInBackground()
        SherlockFragmentActivity mActivity = null;
        MakeProfilePhotoTaskMethod mMethod = null;
        
        private boolean mSuccess = false;
        private int mError = 0;
        
        private String mPicFilename = "";
    }
    
    // RESULTS
    public boolean success()
    {
        return mMakeProfilePhotoTask.mSuccess;
    }
    
    public int getError()
    {
        return mMakeProfilePhotoTask.mError;
    }
    
    public String getPicFilename()
    {
        return mMakeProfilePhotoTask.mPicFilename;
    }
    
    
    
    // EVENTS
    public void addMakeProfilePhotoTaskListener(MakeProfilePhotoTaskListener l) 
    {
        this.mMakeProfilePhotoTaskListeners.add(l);
    }

    public void removeMakeProfilePhotoTaskListener(MakeProfilePhotoTaskListener l) 
    {
        this.mMakeProfilePhotoTaskListeners.remove(l);
    }
    
    public void fireMakeProfilePhotoTaskEvent() {
        MakeProfilePhotoTaskEvent evt = new MakeProfilePhotoTaskEvent(this);

        for (MakeProfilePhotoTaskListener l : mMakeProfilePhotoTaskListeners) 
        {
            if( l != null)
                l.makeProfilePhotoTaskEventReceived(evt);
        }
    }
    
    public class MakeProfilePhotoTaskEvent extends EventObject {
        private static final long serialVersionUID = 0;
          public MakeProfilePhotoTaskEvent(Object source) {
            super(source);
          }
        }
    
    public interface MakeProfilePhotoTaskListener {
        void makeProfilePhotoTaskEventReceived(MakeProfilePhotoTaskEvent evt);
    }
    


}
