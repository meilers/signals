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


public class VoteTaskMethod
{
    
	public static final int ERROR_VOTE_PERSON_CHECKED_OUT = 3;
	public static final int ERROR_VOTE_TOO_RECENT = 4;
    public static final int ERROR_VOTE_PERSON_BLOCKED_YOU = 5;
    public static final int ERROR_VOTE_PROCESS = 6;
    
	private CopyOnWriteArrayList<VoteTaskListener> mVoteTaskListeners;
	
	private SherlockFragmentActivity mAct = null;;
	private VoteTask mVoteTask = null;
	
	public VoteTaskMethod(SherlockFragmentActivity act)
	{
		mAct = act;
		mVoteTaskListeners = new CopyOnWriteArrayList<VoteTaskListener>();
		mVoteTask = new VoteTask();
	}
	
	
	public void doTask(Long userId, Long personId, Long placeId)
	{
    	mVoteTask = new VoteTask();
    	mVoteTask.mActivity = mAct;
    	mVoteTask.mMethod = this;
    	mVoteTask.execute(userId, personId, placeId);
	}
	
	public void cleanUp()
	{
		mAct.setSupportProgressBarIndeterminateVisibility(false);
        mVoteTask.mActivity = null;
        
        if (mAct.isFinishing())
        	mVoteTask.cancel(false);
	}
	
    static class VoteTask extends AsyncTask<Long, Void, String>
    {
        @Override
        protected String doInBackground(Long... params)  
        {
		    HttpClient httpclient = new DefaultHttpClient();
		    HttpPost httppost = new HttpPost("http://ec2-107-22-191-241.compute-1.amazonaws.com/signals/vote.php");

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
		        // TODO Auto-generated catch vote
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
            	mMethod.fireVoteTaskEvent();
            }            
        }
 
        // These should never be accessed from within doInBackground()
        VoteTaskMethod mMethod = null;
        SherlockFragmentActivity mActivity = null;
        
        boolean mSuccess = false;   
        int mError = 0; 
    }
    
    public boolean success()
    {
    	return mVoteTask.mSuccess;
    }
    
    public int getError()
    {
    	return mVoteTask.mError;
    }
    
    
    
	public void addVoteTaskListener(VoteTaskListener l) 
	{
		this.mVoteTaskListeners.add(l);
	}

	public void removeVoteTaskListener(VoteTaskListener l) 
	{
	    this.mVoteTaskListeners.remove(l);
	}
	
	public void fireVoteTaskEvent() {
		VoteTaskEvent evt = new VoteTaskEvent(this);

	    for (VoteTaskListener l : mVoteTaskListeners) 
    	{
	    	if( l != null)
	    		l.voteTaskEventReceived(evt);
	    }
	}
	
	public class VoteTaskEvent extends EventObject {
		private static final long serialVersionUID = 0;
				
		  // This event definition is stateless but you could always
		  // add other information here.
		  public VoteTaskEvent(Object source) {
		    super(source);
		  }
		}
	
	public interface VoteTaskListener {
		void voteTaskEventReceived(VoteTaskEvent evt);
	}
}
