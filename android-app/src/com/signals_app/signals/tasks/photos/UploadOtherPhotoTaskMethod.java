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


public class UploadOtherPhotoTaskMethod
{

    private CopyOnWriteArrayList<UploadOtherPhotoTaskListener> mUploadOtherPhotoTaskListeners;
    
    private SherlockFragmentActivity mAct = null;
    private UploadOtherPhotoTask mUploadOtherPhotoTask = null;
    
    private ProgressDialog mProgressDialog;
    
    public UploadOtherPhotoTaskMethod(SherlockFragmentActivity act)
    {
        mAct = act;
        mUploadOtherPhotoTaskListeners = new CopyOnWriteArrayList<UploadOtherPhotoTaskListener>();
        mUploadOtherPhotoTask = new UploadOtherPhotoTask();
        
        mProgressDialog = new ProgressDialog(act);
        mProgressDialog.setCancelable(false);
    }
    
    
    public void doTask(Long userId, byte[] original)
    {
        mUploadOtherPhotoTask = new UploadOtherPhotoTask();
        mUploadOtherPhotoTask.mActivity = mAct;
        mUploadOtherPhotoTask.mMethod = this;
        
        MyParams params = new MyParams();
        params.mUserId = userId;
        params.mOriginal = original;
        mUploadOtherPhotoTask.execute(params);
    }
    
    public void cleanUp()
    {
        mUploadOtherPhotoTask.mActivity = null;
        mUploadOtherPhotoTask.mMethod = null;
        
        if (mAct.isFinishing())
            mUploadOtherPhotoTask.cancel(false);
    }
    
    class MyParams
    {
        Long mUserId;
        byte[] mOriginal;
    } 
    
    static class UploadOtherPhotoTask extends AsyncTask<MyParams, Void, String>
    {
        @Override 
        protected String doInBackground(MyParams... params)
        {
            String originalString = Base64.encodeToString(params[0].mOriginal,Base64.DEFAULT);
            
               
            ArrayList<NameValuePair> nameValuePairs = new  ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair(Constants.USER_ID,String.valueOf(params[0].mUserId)));
            nameValuePairs.add(new BasicNameValuePair(Constants.STRING_BASE_64_ORIGINAL,originalString));
            
            try{
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://ec2-174-129-201-49.compute-1.amazonaws.com/uploadOtherPhoto.php");
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
                mMethod.mProgressDialog.setMessage(mActivity.getString(R.string.uploading_photo));
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
                mMethod.fireUploadOtherPhotoTaskEvent();
                
                mMethod.mProgressDialog.dismiss();
            }            
        }
 
        // These should never be accessed from within doInBackground()
        SherlockFragmentActivity mActivity = null;
        UploadOtherPhotoTaskMethod mMethod = null;
        
        private boolean mSuccess = false;
        private int mError = 0;
        
        private String mPicFilename = "";
    }
    
    // RESULTS
    public boolean success()
    {
        return mUploadOtherPhotoTask.mSuccess;
    }
    
    public int getError()
    {
        return mUploadOtherPhotoTask.mError;
    }
    
    public String getPicFilename()
    {
        return mUploadOtherPhotoTask.mPicFilename;
    }
    
    
    
    // EVENTS
    public void addUploadOtherPhotoTaskListener(UploadOtherPhotoTaskListener l) 
    {
        this.mUploadOtherPhotoTaskListeners.add(l);
    }

    public void removeUploadOtherPhotoTaskListener(UploadOtherPhotoTaskListener l) 
    {
        this.mUploadOtherPhotoTaskListeners.remove(l);
    }
    
    public void fireUploadOtherPhotoTaskEvent() {
        UploadOtherPhotoTaskEvent evt = new UploadOtherPhotoTaskEvent(this);

        for (UploadOtherPhotoTaskListener l : mUploadOtherPhotoTaskListeners) 
        {
            if( l != null)
                l.uploadOtherPhotoTaskEventReceived(evt);
        }
    }
    
    public class UploadOtherPhotoTaskEvent extends EventObject {
        private static final long serialVersionUID = 0;
          public UploadOtherPhotoTaskEvent(Object source) {
            super(source);
          }
        }
    
    public interface UploadOtherPhotoTaskListener {
        void uploadOtherPhotoTaskEventReceived(UploadOtherPhotoTaskEvent evt);
    }
    


}
