package com.signals_app.signals.tasks;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.EventObject;
import java.util.Vector;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.os.AsyncTask;
import android.util.Log;


public class FbPicsUrlsTaskMethod
{
	private CopyOnWriteArrayList<FbPicsUrlsTaskListener> mFbPicsUrlsTaskListeners;
	
	private SherlockFragmentActivity mAct = null;;
	private FbPicsUrlsTask mFbPicsUrlsTask = null;
	
	public FbPicsUrlsTaskMethod(SherlockFragmentActivity act)
	{
		mAct = act;
		mFbPicsUrlsTaskListeners = new CopyOnWriteArrayList<FbPicsUrlsTaskListener>();
		mFbPicsUrlsTask = new FbPicsUrlsTask();
	}
	
	
	public void doTask(String url)
	{
    	mFbPicsUrlsTask = new FbPicsUrlsTask();
    	mFbPicsUrlsTask.mActivity = mAct;
    	mFbPicsUrlsTask.mMethod = this;
    	mFbPicsUrlsTask.execute(url);
	}
	
	public void cleanUp()
	{
		mAct.setSupportProgressBarIndeterminateVisibility(false);
        mFbPicsUrlsTask.mActivity = null;
        
        if (mAct.isFinishing())
        	mFbPicsUrlsTask.cancel(false);
	}
	
    static class FbPicsUrlsTask extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... params)
        {
		    HttpClient httpclient = new DefaultHttpClient();
		    HttpGet httpget= new HttpGet(params[0].toString());

		    try {
		        HttpResponse response = httpclient.execute(httpget);
				InputStream inputStream = response.getEntity().getContent();
				InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
				BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
				StringBuilder stringBuilder = new StringBuilder();
				String bufferedStrChunk = null;

				while((bufferedStrChunk = bufferedReader.readLine()) != null){
					stringBuilder.append(bufferedStrChunk);
				}
				inputStream.close();
				
				return stringBuilder.toString();
		        
		    } catch (ClientProtocolException e) {
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

			mSuccess = false;
			mProfileFbPicsUrlsUrls = new Vector<String>();
			
			JSONObject json;
			
	        try 
	        {
	        	json = new JSONObject(result);
	        	
	            if(json != null)
	            {
	            	JSONArray data = json.getJSONArray("data");
	            	
		            for(int i = 0; i < data.length(); i++)
		            {
		            	JSONObject pic = data.getJSONObject(i);
		            	mProfileFbPicsUrlsUrls.add(pic.getString("source"));
		            } 
		            
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
            	mMethod.fireFbPicsUrlsTaskEvent();
            }            
        }
 
        // These should never be accessed from within doInBackground()
        FbPicsUrlsTaskMethod mMethod = null;
        SherlockFragmentActivity mActivity = null;
        
        boolean mSuccess = false;
        Vector<String> mProfileFbPicsUrlsUrls;
    }
    
    
    // RESULTS
    public boolean success()
    {
    	return mFbPicsUrlsTask.mSuccess;
    }
    
    
    public Vector<String> getFbPicsUrls()
    {
    	return mFbPicsUrlsTask.mProfileFbPicsUrlsUrls;
    }
    
    
    // EVENTS
	public void addFbPicsUrlsTaskListener(FbPicsUrlsTaskListener l) 
	{
		this.mFbPicsUrlsTaskListeners.add(l);
	}

	public void removeFbPicsUrlsTaskListener(FbPicsUrlsTaskListener l) 
	{
	    this.mFbPicsUrlsTaskListeners.remove(l);
	}
	
	public void fireFbPicsUrlsTaskEvent() {
		FbPicsUrlsTaskEvent evt = new FbPicsUrlsTaskEvent(this);

	    for (FbPicsUrlsTaskListener l : mFbPicsUrlsTaskListeners) 
    	{
	    	if( l != null)
	    		l.fbPicsUrlsTaskEventReceived(evt);
	    }
	}
	
	public class FbPicsUrlsTaskEvent extends EventObject {
		private static final long serialVersionUID = 0;
		
		  public FbPicsUrlsTaskEvent(Object source) {
		    super(source);
		  }
		}
	
	public interface FbPicsUrlsTaskListener {
		void fbPicsUrlsTaskEventReceived(FbPicsUrlsTaskEvent evt);
	}
}
