package com.signals_app.signals.tasks.signals;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.signals_app.signals.model.Constants;
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

import android.app.AlertDialog;
import android.os.AsyncTask;
import android.util.Log;


public class FlirtTaskMethod
{
	public static final int ERROR_FLIRT_TOO_RECENT = 1;
	public static final int ERROR_FLIRT_PERSON_CHECKED_OUT = 3;
	public static final int ERROR_FLIRT_PERSON_DONT_APPROACH = 4;
    public static final int ERROR_FLIRT_PERSON_BLOCKED_YOU = 5;
	public static final int ERROR_FLIRT_PROCESS = 6;
	
	
	private CopyOnWriteArrayList<FlirtTaskListener> mFlirtTaskListeners;
	
	private SherlockFragmentActivity mAct = null;;
	private FlirtTask mFlirtTask = null;
	
	public FlirtTaskMethod(SherlockFragmentActivity act)
	{
		mAct = act;
		mFlirtTaskListeners = new CopyOnWriteArrayList<FlirtTaskListener>();
		mFlirtTask = new FlirtTask();
	}
	
	
	public void doTask(Long userId, Long personId, Long placeId)
	{
    	mFlirtTask = new FlirtTask();
    	mFlirtTask.mActivity = mAct;
    	mFlirtTask.mMethod = this;
    	mFlirtTask.execute(userId, personId, placeId);
	}
	
	public void cleanUp()
	{
		mAct.setSupportProgressBarIndeterminateVisibility(false);
        mFlirtTask.mActivity = null;
        
        if (mAct.isFinishing())
        	mFlirtTask.cancel(false);
	}
	
    static class FlirtTask extends AsyncTask<Long, Void, String>
    {
        @Override
        protected String doInBackground(Long... params)  
        {
		    HttpClient httpclient = new DefaultHttpClient();
		    HttpPost httppost = new HttpPost("http://ec2-107-22-191-241.compute-1.amazonaws.com/signals/flirt.php");

		    try {
		    	
		    	JSONObject json = new JSONObject();
		    
		    	json.put(Constants.USER_ID, params[0]);
		    	json.put(Constants.PERSON_ID, params[1]);
		    	json.put(Constants.PLACE_ID, params[2]);
		    	
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
		        // TODO Auto-generated catch flirt
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
                mActivity.setSupportProgressBarIndeterminateVisibility(false);
            	mMethod.fireFlirtTaskEvent();
            }            
        }
 
        // These should never be accessed from within doInBackground()
        FlirtTaskMethod mMethod = null;
        SherlockFragmentActivity mActivity = null;
        
        boolean mSuccess = false;   
        int mError = 0; 
    }
    
    public boolean success()
    {
    	return mFlirtTask.mSuccess;
    }
    
    public int getError()
    {
    	return mFlirtTask.mError;
    }
    
    
    
	public void addFlirtTaskListener(FlirtTaskListener l) 
	{
		this.mFlirtTaskListeners.add(l);
	}

	public void removeFlirtTaskListener(FlirtTaskListener l) 
	{
	    this.mFlirtTaskListeners.remove(l);
	}
	
	public void fireFlirtTaskEvent() {
		FlirtTaskEvent evt = new FlirtTaskEvent(this);

	    for (FlirtTaskListener l : mFlirtTaskListeners) 
    	{
	    	if( l != null)
	    		l.flirtTaskEventReceived(evt);
	    }
	}
	
	public class FlirtTaskEvent extends EventObject {
		private static final long serialVersionUID = 0;
				
		  // This event definition is stateless but you could always
		  // add other information here.
		  public FlirtTaskEvent(Object source) {
		    super(source);
		  }
		}
	
	public interface FlirtTaskListener {
		void flirtTaskEventReceived(FlirtTaskEvent evt);
	}
}
