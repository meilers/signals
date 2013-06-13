package com.signals_app.signals.tasks;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.EventObject;
import java.util.concurrent.CopyOnWriteArrayList;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.actionbarsherlock.app.SherlockFragmentActivity;

public class FbProfilePicTaskMethod {

	private CopyOnWriteArrayList<FbProfilePicTaskListener> mFbProfilePicTaskListeners;
	
	private SherlockFragmentActivity mAct = null;
	private FbProfilePicAsyncTask mFbProfilePicTask = null;

	public FbProfilePicTaskMethod(SherlockFragmentActivity act)
	{
		mAct = act;
		mFbProfilePicTaskListeners = new CopyOnWriteArrayList<FbProfilePicTaskListener>();
		mFbProfilePicTask = new FbProfilePicAsyncTask();
	}
	
	public void doTask(String url)
	{
		mFbProfilePicTask = new FbProfilePicAsyncTask(); 
		mFbProfilePicTask.mActivity = mAct;
		mFbProfilePicTask.mMethod = this;
		mFbProfilePicTask.execute(url);
	}
	
	public void cleanUp()
	{
		mFbProfilePicTask.mActivity = null;
        
        if (mAct.isFinishing())
        	mFbProfilePicTask.cancel(false);
	}
	
	private class FbProfilePicAsyncTask extends AsyncTask<String, Void, Bitmap> 
	{

		@Override
		protected Bitmap doInBackground(String... params) {
			try {   
				String url = params[0];

				HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
				InputStream is = new BufferedInputStream(conn.getInputStream());
				Bitmap bitmap = BitmapFactory.decodeStream(is);

				if (null != bitmap) {
					return bitmap;
				}

			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			return null;
		}

        @Override
        protected void onPreExecute() {
            mActivity.setSupportProgressBarIndeterminateVisibility(true);
        }
        
        
		@Override
		protected void onPostExecute(Bitmap result) {
			super.onPostExecute(result);
			
			mSuccess = true;
			mProfilePic = result;
			
			updateUI();
		}

        public void updateUI()
        {
            if (mActivity != null && mMethod != null)
            {
                mActivity.setSupportProgressBarIndeterminateVisibility(false);
            	mMethod.fireFbProfilePicTaskEvent();
            }            
        }
 
        // These should never be accessed from within doInBackground()
        SherlockFragmentActivity mActivity = null;
        FbProfilePicTaskMethod mMethod = null;
        
        boolean mSuccess = false; 
        Bitmap mProfilePic = null;
	}
	
    public boolean success()
    {
    	return mFbProfilePicTask.mSuccess;
    }
    
    
    public Bitmap getFbProfilePic()
    {
    	return mFbProfilePicTask.mProfilePic;
    }
    
	public void addFbProfilePicTaskListener(FbProfilePicTaskListener l) 
	{
		this.mFbProfilePicTaskListeners.add(l);
	}

	public void removeFbProfilePicTaskListener(FbProfilePicTaskListener l) 
	{
	    this.mFbProfilePicTaskListeners.remove(l);
	}
	
	public void fireFbProfilePicTaskEvent() {
		FbProfilePicTaskEvent evt = new FbProfilePicTaskEvent(this);

	    for (FbProfilePicTaskListener l : mFbProfilePicTaskListeners) 
    	{
	    	if( l != null)
	    		l.fbProfilePicTaskEventReceived(evt);
	    }
	}
	
	public class FbProfilePicTaskEvent extends EventObject {
		private static final long serialVersionUID = 0;
		
		  public FbProfilePicTaskEvent(Object source) {
		    super(source);
		  }
		}
	
	public interface FbProfilePicTaskListener {
		void fbProfilePicTaskEventReceived(FbProfilePicTaskEvent evt);
	}
}
