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

public class UploadProfilePhotoTaskMethod
{

	private CopyOnWriteArrayList<UploadProfilePhotoTaskListener> mUploadProfilePhotoTaskListeners;
	
	private SherlockFragmentActivity mAct = null;
	private UploadProfilePhotoTask mUploadProfilePhotoTask = null;
	
	private ProgressDialog mProgressDialog;
	
	public UploadProfilePhotoTaskMethod(SherlockFragmentActivity act)
	{
		mAct = act;
		mUploadProfilePhotoTaskListeners = new CopyOnWriteArrayList<UploadProfilePhotoTaskListener>();
		mUploadProfilePhotoTask = new UploadProfilePhotoTask();
		
		mProgressDialog = new ProgressDialog(act);
        mProgressDialog.setCancelable(false);
	}
	
	
	public void doTask(Long userId, byte[] original, byte[] crop)
	{
    	mUploadProfilePhotoTask = new UploadProfilePhotoTask();
    	mUploadProfilePhotoTask.mActivity = mAct;
    	mUploadProfilePhotoTask.mMethod = this;
    	
    	MyParams params = new MyParams();
    	params.mUserId = userId;
    	params.mOriginal = original;
    	params.mCrop = crop;
    	mUploadProfilePhotoTask.execute(params);
	}
	
	public void cleanUp()
	{
		mUploadProfilePhotoTask.mActivity = null;
    	mUploadProfilePhotoTask.mMethod = null;
        
        if (mAct.isFinishing())
        	mUploadProfilePhotoTask.cancel(false);
	}
	
	class MyParams
	{
		Long mUserId;
		byte[] mOriginal;
		byte[] mCrop;
	} 
	
    static class UploadProfilePhotoTask extends AsyncTask<MyParams, Void, String>
    {
        @Override 
        protected String doInBackground(MyParams... params)
        {
            String cropString = Base64.encodeToString(params[0].mCrop,Base64.DEFAULT);
            
            String originalString = Base64.encodeToString(params[0].mOriginal,Base64.DEFAULT);
            
               
            ArrayList<NameValuePair> nameValuePairs = new  ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair(Constants.USER_ID,String.valueOf(params[0].mUserId)));
            nameValuePairs.add(new BasicNameValuePair(Constants.STRING_BASE_64_CROP,cropString));
            nameValuePairs.add(new BasicNameValuePair(Constants.STRING_BASE_64_ORIGINAL,originalString));
            
            try{
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://ec2-174-129-201-49.compute-1.amazonaws.com/uploadProfilePhoto.php");
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
            	mMethod.fireUploadProfilePhotoTaskEvent();
            	
            	mMethod.mProgressDialog.dismiss();
            }            
        }
 
        // These should never be accessed from within doInBackground()
        SherlockFragmentActivity mActivity = null;
        UploadProfilePhotoTaskMethod mMethod = null;
        
        private boolean mSuccess = false;
        private int mError = 0;
        
        private String mPicFilename = "";
    }
    
	// RESULTS
	public boolean success()
	{
		return mUploadProfilePhotoTask.mSuccess;
	}
	
    public int getError()
    {
    	return mUploadProfilePhotoTask.mError;
    }
    
    public String getPicFilename()
    {
    	return mUploadProfilePhotoTask.mPicFilename;
    }
    
    
    
    // EVENTS
	public void addUploadProfilePhotoTaskListener(UploadProfilePhotoTaskListener l) 
	{
		this.mUploadProfilePhotoTaskListeners.add(l);
	}

	public void removeUploadProfilePhotoTaskListener(UploadProfilePhotoTaskListener l) 
	{
	    this.mUploadProfilePhotoTaskListeners.remove(l);
	}
	
	public void fireUploadProfilePhotoTaskEvent() {
		UploadProfilePhotoTaskEvent evt = new UploadProfilePhotoTaskEvent(this);

	    for (UploadProfilePhotoTaskListener l : mUploadProfilePhotoTaskListeners) 
    	{
	    	if( l != null)
	    		l.uploadProfilePhotoTaskEventReceived(evt);
	    }
	}
	
	public class UploadProfilePhotoTaskEvent extends EventObject {
		private static final long serialVersionUID = 0;
		  public UploadProfilePhotoTaskEvent(Object source) {
		    super(source);
		  }
		}
	
	public interface UploadProfilePhotoTaskListener {
		void uploadProfilePhotoTaskEventReceived(UploadProfilePhotoTaskEvent evt);
	}
	


}
