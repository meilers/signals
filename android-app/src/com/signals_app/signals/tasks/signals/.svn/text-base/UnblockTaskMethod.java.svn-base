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


public class UnblockTaskMethod
{
	private CopyOnWriteArrayList<UnblockTaskListener> mUnblockTaskListeners;
	
	private SherlockFragmentActivity mAct = null;;
	private UnblockTask mUnblockTask = null;
	
	public UnblockTaskMethod(SherlockFragmentActivity act)
	{
		mAct = act;
		mUnblockTaskListeners = new CopyOnWriteArrayList<UnblockTaskListener>();
		mUnblockTask = new UnblockTask();
	}
	
	
	public void doTask(Long userId, Long personId)
	{
    	mUnblockTask = new UnblockTask();
    	mUnblockTask.mActivity = mAct;
    	mUnblockTask.mMethod = this;
    	
    	mUnblockTask.execute(userId, personId);
	}
	
	public void cleanUp()
	{
		mAct.setSupportProgressBarIndeterminateVisibility(false);
        mUnblockTask.mActivity = null;
        
        if (mAct.isFinishing())
        	mUnblockTask.cancel(false);
	}
	
    static class UnblockTask extends AsyncTask<Long, Void, String>
    {
        @Override
        protected String doInBackground(Long... params)  
        {
		    HttpClient httpclient = new DefaultHttpClient();
		    HttpPost httppost = new HttpPost("http://ec2-107-22-191-241.compute-1.amazonaws.com/signals/unblock.php");

		    try {
		    	JSONObject json = new JSONObject();
		    
		    	json.put(Constants.USER_ID, params[0]);
		    	json.put(Constants.PERSON_ID, params[1]);
		    	
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
		        // TODO Auto-generated catch unblock
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
            	mMethod.fireUnblockTaskEvent();
            }            
        }
 
        // These should never be accessed from within doInBackground()
        UnblockTaskMethod mMethod = null;
        SherlockFragmentActivity mActivity = null;
        
        boolean mSuccess = false;
        int mError = 0;
    }
    
    public boolean success()
    {
    	return mUnblockTask.mSuccess;
    }
    
    public int getError()
    {
    	return mUnblockTask.mError;
    }
    
	public void addUnblockTaskListener(UnblockTaskListener l) 
	{
		this.mUnblockTaskListeners.add(l);
	}

	public void removeUnblockTaskListener(UnblockTaskListener l) 
	{
	    this.mUnblockTaskListeners.remove(l);
	}
	
	public void fireUnblockTaskEvent() {
		UnblockTaskEvent evt = new UnblockTaskEvent(this);

	    for (UnblockTaskListener l : mUnblockTaskListeners) 
    	{
	    	if( l != null)
	    		l.unblockTaskEventReceived(evt);
	    }
	}
	
	public class UnblockTaskEvent extends EventObject {
		private static final long serialVersionUID = 0;
				
		  // This event definition is stateless but you could always
		  // add other information here.
		  public UnblockTaskEvent(Object source) {
		    super(source);
		  }
		}
	
	public interface UnblockTaskListener {
		void unblockTaskEventReceived(UnblockTaskEvent evt);
	}
}
