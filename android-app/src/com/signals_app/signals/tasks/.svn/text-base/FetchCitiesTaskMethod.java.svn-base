package com.signals_app.signals.tasks;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.signals_app.signals.model.City;
import com.signals_app.signals.model.Constants;
import com.signals_app.signals.model.Place;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.CopyOnWriteArrayList;

import com.signals_app.signals.model.rendezvous.ConfirmedRendezvous;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.signals_app.signals.model.Profile.GpsPosition;
import com.signals_app.signals.model.Profile.Person;
import com.signals_app.signals.util.Utility;


public class FetchCitiesTaskMethod
{
    static private CopyOnWriteArrayList<FetchCitiesTaskListener> mFetchCitiesTaskListeners = new CopyOnWriteArrayList<FetchCitiesTaskListener>();

    private SherlockFragmentActivity mAct = null;;
    private FetchCitiesTask mFetchCitiesTask = null;


    public FetchCitiesTaskMethod(SherlockFragmentActivity act)
    {
        mAct = act;
        mFetchCitiesTask = new FetchCitiesTask();
    }


    public void doTask()
    {
        mFetchCitiesTask = new FetchCitiesTask();
        mFetchCitiesTask.mActivity = mAct;
        mFetchCitiesTask.mMethod = this;

        mFetchCitiesTask.execute();
    }

    public void cleanUp()
    {
        mAct.setSupportProgressBarIndeterminateVisibility(false);
        mFetchCitiesTask.mActivity = null;

        if (mAct.isFinishing())
            mFetchCitiesTask.cancel(false);
    }



    static class FetchCitiesTask extends AsyncTask<Void, Void, String>
    {
        @Override
        protected String doInBackground(Void... params)
        {
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpget = new HttpGet("http://ec2-107-22-191-241.compute-1.amazonaws.com/fetchCities.php");

            try {
                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httpget);
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
            catch (ClientProtocolException e) {
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

            JSONObject json;
            mSuccess = false;


            try
            {
                json = new JSONObject(result);

                if(json.getInt(Constants.RESULT_OK) != 0)
                {
                    // POTENTIAL RENDEZVOUS
                    JSONArray jsonCities = json.getJSONArray(Constants.RESULT);
                    mCities = new ArrayList<City>(jsonCities.length());

                    for( int i = 0; i < jsonCities.length(); ++i )
                    {
                        JSONObject jsonCity = jsonCities.getJSONObject(i);

                        int cityId = jsonCity.getInt(Constants.ID);
                        String city = jsonCity.getString(Constants.CITY);
                        String country = jsonCity.getString(Constants.COUNTRY);

                        City c = new City(cityId, city, country);
                        mCities.add(c);

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
                mMethod.fireFetchCitiesTaskEvent();
            }
        }

        // These should never be accessed from within doInBackground()
        FetchCitiesTaskMethod mMethod = null;
        SherlockFragmentActivity mActivity = null;

        // Results
        boolean mSuccess = false;
        ArrayList<City> mCities;
    }


    // RESULTS
    public boolean success()
    {
        return mFetchCitiesTask.mSuccess;
    }

    public ArrayList<City> getCities()
    {
        return mFetchCitiesTask.mCities;
    }

    // EVENTS
    public void addFetchCitiesTaskListener(FetchCitiesTaskListener l)
    {
        this.mFetchCitiesTaskListeners.add(l);
    }

    public void removeFetchCitiesTaskListener(FetchCitiesTaskListener l)
    {
        this.mFetchCitiesTaskListeners.remove(l);
    }

    public void fireFetchCitiesTaskEvent() {
        FetchCitiesTaskEvent evt = new FetchCitiesTaskEvent(this);

        for (FetchCitiesTaskListener l : mFetchCitiesTaskListeners)
        {
            if( l != null)
                l.fetchCitiesTaskEventReceived(evt);
        }
    }

    public class FetchCitiesTaskEvent extends EventObject {
        private static final long serialVersionUID = 0;
        public FetchCitiesTaskEvent(Object source) {
            super(source);
        }
    }

    public interface FetchCitiesTaskListener {
        void fetchCitiesTaskEventReceived(FetchCitiesTaskEvent evt);
    }
}
