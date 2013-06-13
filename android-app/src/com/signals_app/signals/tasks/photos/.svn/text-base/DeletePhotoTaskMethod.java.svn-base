package com.signals_app.signals.tasks.photos;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.signals_app.signals.model.Constants;
import com.signals_app.signals.R;

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

public class DeletePhotoTaskMethod
{

    private CopyOnWriteArrayList<DeletePhotoTaskListener> mDeletePhotoTaskListeners;
    
    private SherlockFragmentActivity mAct = null;
    private DeletePhotoTask mDeletePhotoTask = null;
    
    private ProgressDialog mProgressDialog;
    
    public DeletePhotoTaskMethod(SherlockFragmentActivity act)
    {
        mAct = act;
        mDeletePhotoTaskListeners = new CopyOnWriteArrayList<DeletePhotoTaskListener>();
        mDeletePhotoTask = new DeletePhotoTask();
        
        mProgressDialog = new ProgressDialog(act);
        mProgressDialog.setCancelable(false);
    }
    
    
    public void doTask(Long userId, String filename )
    {
        mDeletePhotoTask = new DeletePhotoTask();
        mDeletePhotoTask.mActivity = mAct;
        mDeletePhotoTask.mMethod = this;
        
        MyParams params = new MyParams();
        params.mUserId = userId;
        params.mFilename = filename;
        
        mDeletePhotoTask.execute(params);
    }
    
    public void cleanUp()
    {
        mDeletePhotoTask.mActivity = null;
        mDeletePhotoTask.mMethod = null;
        
        if (mAct.isFinishing())
            mDeletePhotoTask.cancel(false);
    }
    
    class MyParams
    {
        Long mUserId;
        String mFilename;
    } 
    
    static class DeletePhotoTask extends AsyncTask<MyParams, Void, String>
    {
        @Override 
        protected String doInBackground(MyParams... params)
        {
            ArrayList<NameValuePair> nameValuePairs = new  ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair(Constants.USER_ID,String.valueOf(params[0].mUserId)));
            nameValuePairs.add(new BasicNameValuePair(Constants.FILENAME,String.valueOf(params[0].mFilename)));
            
            try{
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://ec2-174-129-201-49.compute-1.amazonaws.com/deletePhoto.php");
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
                mMethod.mProgressDialog.setMessage(mActivity.getString(R.string.deleting_photo));
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
                mMethod.fireDeletePhotoTaskEvent();
                
                mMethod.mProgressDialog.dismiss();
            }            
        }
 
        // These should never be accessed from within doInBackground()
        SherlockFragmentActivity mActivity = null;
        DeletePhotoTaskMethod mMethod = null;
        
        private boolean mSuccess = false;
        private int mError = 0;
    }
    
    // RESULTS
    public boolean success()
    {
        return mDeletePhotoTask.mSuccess;
    }
    
    public int getError()
    {
        return mDeletePhotoTask.mError;
    }
    
    
    
    
    // EVENTS
    public void addDeletePhotoTaskListener(DeletePhotoTaskListener l) 
    {
        this.mDeletePhotoTaskListeners.add(l);
    }

    public void removeDeletePhotoTaskListener(DeletePhotoTaskListener l) 
    {
        this.mDeletePhotoTaskListeners.remove(l);
    }
    
    public void fireDeletePhotoTaskEvent() {
        DeletePhotoTaskEvent evt = new DeletePhotoTaskEvent(this);

        for (DeletePhotoTaskListener l : mDeletePhotoTaskListeners) 
        {
            if( l != null)
                l.deletePhotoTaskEventReceived(evt);
        }
    }
    
    public class DeletePhotoTaskEvent extends EventObject {
        private static final long serialVersionUID = 0;
          public DeletePhotoTaskEvent(Object source) {
            super(source);
          }
        }
    
    public interface DeletePhotoTaskListener {
        void deletePhotoTaskEventReceived(DeletePhotoTaskEvent evt);
    }
    


}
