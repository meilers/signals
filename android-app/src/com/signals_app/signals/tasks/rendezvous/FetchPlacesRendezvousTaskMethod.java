package com.signals_app.signals.tasks.rendezvous;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.signals_app.signals.model.Constants;
import com.signals_app.signals.model.Place;
import com.signals_app.signals.model.Profile.GpsPosition;
import com.signals_app.signals.model.Profile.Person;
import com.signals_app.signals.model.Profile.User;
import com.signals_app.signals.model.rendezvous.PotentialRendezvous;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.EventObject;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.os.AsyncTask;
import android.util.Log;


public class FetchPlacesRendezvousTaskMethod
{
    private CopyOnWriteArrayList<FetchPlacesRendezvousTaskListener> mFetchPlacesRendezvousTaskListeners;

    private SherlockFragmentActivity mAct = null;;
    private FetchPlacesRendezvousTask mFetchPlacesRendezvousTask = null;

    private Person mPerson;

    public FetchPlacesRendezvousTaskMethod(SherlockFragmentActivity act)
    {
        mAct = act;
        mFetchPlacesRendezvousTaskListeners = new CopyOnWriteArrayList<FetchPlacesRendezvousTaskListener>();
        mFetchPlacesRendezvousTask = new FetchPlacesRendezvousTask();
    }


    public void doTask(PotentialRendezvous rendezvous, ArrayList<Long> placesNotWantedIds)
    {
        mFetchPlacesRendezvousTask = new FetchPlacesRendezvousTask();
        mFetchPlacesRendezvousTask.mActivity = mAct;
        mFetchPlacesRendezvousTask.mMethod = this;

        MyParams params = new MyParams();
        params.mUserId = User.getInstance().getUserId();
        params.mPersonId = rendezvous.getCandidate().getUserId();
        params.mRendezvousId = rendezvous.getId();
        params.mCityId = User.getInstance().getCity().getId();
        params.mPlacesIds = placesNotWantedIds;

        mFetchPlacesRendezvousTask.execute(params);
    }

    class MyParams
    {
        Long mUserId;
        Long mPersonId;
        Long mRendezvousId;

        int mCityId;
        ArrayList<Long> mPlacesIds;
    }

    public void cleanUp()
    {
        mAct.setSupportProgressBarIndeterminateVisibility(false);
        mFetchPlacesRendezvousTask.mActivity = null;

        if (mAct.isFinishing())
            mFetchPlacesRendezvousTask.cancel(false);
    }

    static class FetchPlacesRendezvousTask extends AsyncTask<MyParams, Void, String>
    {
        @Override
        protected String doInBackground(MyParams... params)
        {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://ec2-107-22-191-241.compute-1.amazonaws.com/rendezvous/rendezvousFetchPlaces.php");

            try {
                JSONObject json = new JSONObject();

                json.put(Constants.USER_ID, params[0].mUserId);
                json.put(Constants.PERSON_ID, params[0].mPersonId);
                json.put(Constants.RENDEZVOUS_ID, params[0].mRendezvousId);
                json.put(Constants.CITY_ID, params[0].mCityId);
                json.put(Constants.PLACES_SELECTED, params[0].mPlacesIds);


                StringEntity se = new StringEntity(json.toString());
                se.setContentType("application/json;charset=UTF-8");
                httppost.setEntity(se);

                // Execute HTTP Post 
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
                // TODO Auto-generated catch fetchPlacesRendezvous
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

            mPlaces = new ArrayList<Place>();

            try
            {
                json = new JSONObject(result);


                if(json.getInt(Constants.RESULT_OK) != 0)
                {
                    // PLACES
                    JSONArray jsonPlaces = json.getJSONArray(Constants.RESULT);

                    for( int j = 0; j < jsonPlaces.length(); ++j )
                    {
                        if( jsonPlaces.getJSONObject(j).has("placeId"))
                        {
                            JSONObject jsonPlace = jsonPlaces.getJSONObject(j);
                            Place place = new Place();

                            Long id = Long.parseLong(jsonPlace.getString(Constants.PLACE_ID));
                            Integer genre = Integer.parseInt(jsonPlace.getString(Constants.GENRE));
                            String name = jsonPlace.getString(Constants.NAME);
                            String address = jsonPlace.getString(Constants.ADDRESS);
                            GpsPosition pos = new GpsPosition((float)jsonPlace.getDouble(Constants.LATITUDE), (float)jsonPlace.getDouble(Constants.LONGITUDE), 0.0f);

                            place.setPlaceId(id);
                            place.setGenre(genre);
                            place.setName(name);
                            place.setAddress(address);
                            place.setGpsPosition(pos);

                            mPlaces.add(place);
                        }
                    }

                    Comparator<Place> comparator;
                    comparator = new Place.DistanceComparator();

                    Collections.sort(mPlaces, comparator);

                    if( mPlaces.size() != 0 )
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
                mMethod.fireFetchPlacesRendezvousTaskEvent();


            }
        }

        // These should never be accessed from within doInBackground()
        FetchPlacesRendezvousTaskMethod mMethod = null;
        SherlockFragmentActivity mActivity = null;

        boolean mSuccess = false;
        int mError = 0;

        ArrayList<Place> mPlaces = null;
    }

    public boolean success()
    {
        return mFetchPlacesRendezvousTask.mSuccess;
    }

    public int getError()
    {
        return mFetchPlacesRendezvousTask.mError;
    }

    public ArrayList<Place> getPlaces()
    {
        return mFetchPlacesRendezvousTask.mPlaces;
    }

    public void addFetchPlacesRendezvousTaskListener(FetchPlacesRendezvousTaskListener l)
    {
        this.mFetchPlacesRendezvousTaskListeners.add(l);
    }

    public void removeFetchPlacesRendezvousTaskListener(FetchPlacesRendezvousTaskListener l)
    {
        this.mFetchPlacesRendezvousTaskListeners.remove(l);
    }

    public void fireFetchPlacesRendezvousTaskEvent() {
        FetchPlacesRendezvousTaskEvent evt = new FetchPlacesRendezvousTaskEvent(this);

        for (FetchPlacesRendezvousTaskListener l : mFetchPlacesRendezvousTaskListeners)
        {
            if( l != null)
                l.fetchPlacesRendezvousTaskEventReceived(evt);
        }
    }

    public class FetchPlacesRendezvousTaskEvent extends EventObject {
        private static final long serialVersionUID = 0;

        // This event definition is stateless but you could always
        // add other information here.
        public FetchPlacesRendezvousTaskEvent(Object source) {
            super(source);
        }
    }

    public interface FetchPlacesRendezvousTaskListener {
        void fetchPlacesRendezvousTaskEventReceived(FetchPlacesRendezvousTaskEvent evt);
    }
}
