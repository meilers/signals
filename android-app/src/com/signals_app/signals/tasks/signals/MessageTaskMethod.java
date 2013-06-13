package com.signals_app.signals.tasks.signals;

import com.actionbarsherlock.app.SherlockFragmentActivity;
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

import android.app.AlertDialog;
import android.os.AsyncTask;
import android.util.Log;


public class MessageTaskMethod
{
    public static final int ERROR_MESSAGE_PERSON_CHECKED_OUT = 3;
    public static final int ERROR_MESSAGE_PERSON_DONT_APPROACH = 4;
    public static final int ERROR_MESSAGE_PERSON_BLOCKED_YOU = 5;
    public static final int ERROR_MESSAGE_PROCESS = 6;
    
	private CopyOnWriteArrayList<MessageTaskListener> mMessageTaskListeners;
	
	private SherlockFragmentActivity mAct = null;;
	private MessageTask mMessageTask = null;
	
	public MessageTaskMethod(SherlockFragmentActivity act)
	{
		mAct = act;
		mMessageTaskListeners = new CopyOnWriteArrayList<MessageTaskListener>();
		mMessageTask = new MessageTask();
	}
	
	
	public void doTask(Long userId, Long personId, Long placeId, String message )
	{
    	mMessageTask = new MessageTask();
    	mMessageTask.mActivity = mAct;
    	mMessageTask.mMethod = this;
    	mMessageTask.mSuccess = false;
    	
    	MyParams p = new MyParams();
    	p.mUserId = userId;
    	p.mPersonId = personId;
    	p.mPlaceId = placeId;
    	p.mMessage = message;
    	
    	
    	mMessageTask.execute(p);
	}
	
	public void cleanUp()
	{
		mAct.setSupportProgressBarIndeterminateVisibility(false);
        mMessageTask.mActivity = null;
        
        if (mAct.isFinishing())
        	mMessageTask.cancel(false);
	}
	
   class MyParams
    {
        Long mUserId;
        Long mPersonId;
        Long mPlaceId;
        String mMessage;
    }
	    
	   
	   
    static class MessageTask extends AsyncTask<MyParams, Void, String>
    {
        @Override
        protected String doInBackground(MyParams... params)  
        {
		    HttpClient httpclient = new DefaultHttpClient();
		    HttpPost httppost = new HttpPost("http://ec2-107-22-191-241.compute-1.amazonaws.com/signals/message.php");

		    try {
		    	JSONObject json = new JSONObject();
		    
		    	json.put(Constants.USER_ID, params[0].mUserId);
		    	json.put(Constants.PERSON_ID, params[0].mPersonId);
		    	json.put(Constants.PLACE_ID, params[0].mPlaceId);
		    	json.put(Constants.MESSAGE, params[0].mMessage);
		    	
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
		        // TODO Auto-generated catch message
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
			
			mError = 0;
			JSONObject json;
			
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
            	mMethod.fireMessageTaskEvent();
            }            
        }
 
        // These should never be accessed from within doInBackground()
        MessageTaskMethod mMethod = null;
        SherlockFragmentActivity mActivity = null;
        boolean mSuccess = false; 
        int mError = 0;
    }
    
    public boolean success()
    {
    	return mMessageTask.mSuccess;
    }
    
    public int getError()
    {
        return mMessageTask.mError;
    }
    
    
    
	public void addMessageTaskListener(MessageTaskListener l) 
	{
		this.mMessageTaskListeners.add(l);
	}

	public void removeMessageTaskListener(MessageTaskListener l) 
	{
	    this.mMessageTaskListeners.remove(l);
	}
	
	public void fireMessageTaskEvent() {
		MessageTaskEvent evt = new MessageTaskEvent(this);

	    for (MessageTaskListener l : mMessageTaskListeners) 
    	{
	    	if( l != null)
	    		l.messageTaskEventReceived(evt);
	    }
	}
	
	public class MessageTaskEvent extends EventObject {
		private static final long serialVersionUID = 0;
				
		  // This event definition is stateless but you could always
		  // add other information here.
		  public MessageTaskEvent(Object source) {
		    super(source);
		  }
		}
	
	public interface MessageTaskListener {
		void messageTaskEventReceived(MessageTaskEvent evt);
	}
}
