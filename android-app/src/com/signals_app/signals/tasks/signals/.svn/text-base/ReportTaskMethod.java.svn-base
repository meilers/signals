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


public class ReportTaskMethod
{
	private CopyOnWriteArrayList<ReportTaskListener> mReportTaskListeners;
	
	private SherlockFragmentActivity mAct = null;;
	private ReportTask mReportTask = null;
	
	public ReportTaskMethod(SherlockFragmentActivity act)
	{
		mAct = act;
		mReportTaskListeners = new CopyOnWriteArrayList<ReportTaskListener>();
		mReportTask = new ReportTask();
	}
	
	
	public void doTask(Long userId, Long personId, int reportType, String comments )
	{
    	mReportTask = new ReportTask();
    	mReportTask.mActivity = mAct;
    	mReportTask.mMethod = this;
    	
    	MyParams params = new MyParams();
    	params.mUserId = userId;
    	params.mPersonId = personId;
    	params.mReportType = reportType;
    	params.mComments = comments;
    	
    	mReportTask.execute(params);
	}
	
	public void cleanUp()
	{
		mAct.setSupportProgressBarIndeterminateVisibility(false);
        mReportTask.mActivity = null;
        
        if (mAct.isFinishing())
        	mReportTask.cancel(false);
	}
	
	class MyParams
	{
		Long mUserId;
		Long mPersonId;
		
		int mReportType;
		String mComments;
	}
	
    static class ReportTask extends AsyncTask<MyParams, Void, String>
    {
        @Override
        protected String doInBackground(MyParams... params)  
        {
		    HttpClient httpclient = new DefaultHttpClient();
		    HttpPost httppost = new HttpPost("http://ec2-107-22-191-241.compute-1.amazonaws.com/signals/report.php");

		    try {
		    	JSONObject json = new JSONObject();
		    
		    	json.put(Constants.USER_ID, params[0].mUserId);
		    	json.put(Constants.PERSON_ID, params[0].mPersonId);
		    	json.put(Constants.REPORT_TYPE, params[0].mReportType);
		    	json.put(Constants.COMMENTS, params[0].mComments);
		    	
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
		        // TODO Auto-generated catch report
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
            	mMethod.fireReportTaskEvent();
            }            
        }
 
        // These should never be accessed from within doInBackground()
        ReportTaskMethod mMethod = null;
        SherlockFragmentActivity mActivity = null;
        
        boolean mSuccess = false;
        int mError = 0;
    }
    
    public boolean success()
    {
    	return mReportTask.mSuccess;
    }
    
    public int getError()
    {
    	return mReportTask.mError;
    }
    
	public void addReportTaskListener(ReportTaskListener l) 
	{
		this.mReportTaskListeners.add(l);
	}

	public void removeReportTaskListener(ReportTaskListener l) 
	{
	    this.mReportTaskListeners.remove(l);
	}
	
	public void fireReportTaskEvent() {
		ReportTaskEvent evt = new ReportTaskEvent(this);

	    for (ReportTaskListener l : mReportTaskListeners) 
    	{
	    	if( l != null)
	    		l.reportTaskEventReceived(evt);
	    }
	}
	
	public class ReportTaskEvent extends EventObject {
		private static final long serialVersionUID = 0;
				
		  // This event definition is stateless but you could always
		  // add other information here.
		  public ReportTaskEvent(Object source) {
		    super(source);
		  }
		}
	
	public interface ReportTaskListener {
		void reportTaskEventReceived(ReportTaskEvent evt);
	}
}
