package com.signals_app.signals.tasks.rendezvous;

import com.actionbarsherlock.app.SherlockFragmentActivity;
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

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.signals_app.signals.model.Profile.BarStars;
import com.signals_app.signals.model.Profile.GpsPosition;
import com.signals_app.signals.model.Profile.Person;
import com.signals_app.signals.model.rendezvous.Rendezvous;
import com.signals_app.signals.model.rendezvous.PotentialRendezvous;
import com.signals_app.signals.util.Utility;


public class FetchPotentialRendezvousTaskMethod
{
    static private CopyOnWriteArrayList<FetchPotentialRendezvousTaskListener> mFetchPotentialRendezvousTaskListeners = new CopyOnWriteArrayList<FetchPotentialRendezvousTaskListener>();

    private SherlockFragmentActivity mAct = null;;
    private FetchPotentialRendezvousTask mFetchPotentialRendezvousTask = null;


    public FetchPotentialRendezvousTaskMethod(SherlockFragmentActivity act)
    {
        mAct = act;
        mFetchPotentialRendezvousTask = new FetchPotentialRendezvousTask();
    }


    public void doTask(Long userId)
    {
        mFetchPotentialRendezvousTask = new FetchPotentialRendezvousTask();
        mFetchPotentialRendezvousTask.mActivity = mAct;
        mFetchPotentialRendezvousTask.mMethod = this;

        MyParams params = new MyParams();
        params.mUserId = userId;

        mFetchPotentialRendezvousTask.execute(params);
    }

    public void cleanUp()
    {
        mAct.setSupportProgressBarIndeterminateVisibility(false);
        mFetchPotentialRendezvousTask.mActivity = null;

        if (mAct.isFinishing())
            mFetchPotentialRendezvousTask.cancel(false);
    }

    class MyParams
    {
        Long mUserId;
    }


    static class FetchPotentialRendezvousTask extends AsyncTask<MyParams, Void, String>
    {
        @Override
        protected String doInBackground(MyParams... params)
        {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://ec2-107-22-191-241.compute-1.amazonaws.com/rendezvous/fetchPotentialRendezvous.php");

            try {
                JSONObject json = new JSONObject();

                json.put(Constants.USER_ID, Long.toString(params[0].mUserId));

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
                    JSONArray jsonPms = json.getJSONArray(Constants.RESULT);
                    mPotentialRendezvous = new ArrayList<PotentialRendezvous>(jsonPms.length());

                    for( int i = 0; i < jsonPms.length(); ++i )
                    {
                        JSONObject jsonPr = jsonPms.getJSONObject(i);
                        Long potentialRendezvousId = Long.parseLong(jsonPr.getString(Constants.ID));


                        // SEEN
                        boolean seen = jsonPr.getInt(Constants.SEEN) == 1;



                        // IS FIRST ANSWER
                        boolean firstAnswer = jsonPr.getInt(Constants.FIRST_ANSWER) == 1;


                        // PERSON
                        Person person = new Person();
                        JSONObject jsonPerson = jsonPr.getJSONObject(Constants.PERSON);

                        person.setUserId(Long.parseLong(jsonPerson.getString(Constants.USER_ID)));
                        person.setUsername(jsonPerson.getString(Constants.USERNAME));
                        person.setSex(jsonPerson.getInt(Constants.SEX));
                        person.setInterestedIn(jsonPerson.getInt(Constants.INTERESTED_IN));
                        person.setLookingFor(jsonPerson.getInt(Constants.LOOKING_FOR));

                        String[] strArr = jsonPerson.getString(Constants.BIRTHDAY).split("-");
                        Calendar dob = Calendar.getInstance();
                        dob.set(Integer.valueOf(strArr[0]), Integer.valueOf(strArr[1])-1, Integer.valueOf(strArr[2]));
                        person.setBirthday(dob);
                        person.setAge(Utility.getAge(dob));


                        // PLACES & TIMES
                        ArrayList<Place> places = new ArrayList<Place>();
                        ArrayList<Boolean> times = new ArrayList<Boolean>();

                        if( !firstAnswer )
                        {
                            JSONArray jsonPlaces = jsonPr.getJSONArray(Constants.PLACES);


                            for( int j = 0; j < jsonPlaces.length(); ++j )
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

                                places.add(place);
                            }


                            // TIMES
                            JSONArray jsonTimes = jsonPr.getJSONArray(Constants.TIMES);

                            for( int j = 0; j < PotentialRendezvous.NB_CHOICES; ++j )
                                times.add(jsonTimes.getInt(j)==1);
                        }


                        // CREATE TIME
                        Calendar createTime = Utility.convertStrToCalendar(jsonPr.getString(Constants.CREATE_TIME), "yyyy-MM-dd hh:mm");
                        Calendar now = Calendar.getInstance();
                        boolean isSameDay = createTime.get(Calendar.DAY_OF_YEAR) == now.get(Calendar.DAY_OF_YEAR);

                        // rendezvous tonight?
                        if( (isSameDay && createTime.get(Calendar.HOUR_OF_DAY) >= 2) || (!isSameDay && now.get(Calendar.HOUR_OF_DAY) < 2) || (isSameDay && createTime.get(Calendar.HOUR_OF_DAY) < 2 && now.get(Calendar.HOUR_OF_DAY) < 2) )
                        {
                            boolean addToList = true;

                            // rendezvous potential has passed?
                            if( !firstAnswer )
                            {
                                addToList = false;
                                int lastPossibleTime = times.lastIndexOf(true);

                                if( lastPossibleTime < 7 )  // before midnight
                                {
                                    if( now.get(Calendar.HOUR_OF_DAY) >= 2 && now.get(Calendar.HOUR_OF_DAY) < lastPossibleTime+17)
                                        addToList = true;
                                }
                                else
                                {
                                    if( now.get(Calendar.HOUR_OF_DAY) < 2 && now.get(Calendar.HOUR_OF_DAY) < lastPossibleTime-7 )
                                        addToList = true;
                                }
                            }

                            if( addToList )
                            {
                                PotentialRendezvous pm = new PotentialRendezvous(potentialRendezvousId, createTime, person, seen, places, times, firstAnswer );
                                mPotentialRendezvous.add(pm);
                            }
                        }
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
                mMethod.fireFetchPotentialRendezvousTaskEvent();
            }
        }

        // These should never be accessed from within doInBackground()
        FetchPotentialRendezvousTaskMethod mMethod = null;
        SherlockFragmentActivity mActivity = null;

        // Results
        boolean mSuccess = false;
        ArrayList<PotentialRendezvous> mPotentialRendezvous;
    }


    // RESULTS
    public boolean success()
    {
        return mFetchPotentialRendezvousTask.mSuccess;
    }

    public ArrayList<PotentialRendezvous> getPotentialRendezvous()
    {
        return mFetchPotentialRendezvousTask.mPotentialRendezvous;
    }

    // EVENTS
    public void addFetchPotentialRendezvousTaskListener(FetchPotentialRendezvousTaskListener l)
    {
        this.mFetchPotentialRendezvousTaskListeners.add(l);
    }

    public void removeFetchPotentialRendezvousTaskListener(FetchPotentialRendezvousTaskListener l)
    {
        this.mFetchPotentialRendezvousTaskListeners.remove(l);
    }

    public void fireFetchPotentialRendezvousTaskEvent() {
        FetchPotentialRendezvousTaskEvent evt = new FetchPotentialRendezvousTaskEvent(this);

        for (FetchPotentialRendezvousTaskListener l : mFetchPotentialRendezvousTaskListeners)
        {
            if( l != null)
                l.fetchPotentialRendezvousTaskEventReceived(evt);
        }
    }

    public class FetchPotentialRendezvousTaskEvent extends EventObject {
        private static final long serialVersionUID = 0;
        public FetchPotentialRendezvousTaskEvent(Object source) {
            super(source);
        }
    }

    public interface FetchPotentialRendezvousTaskListener {
        void fetchPotentialRendezvousTaskEventReceived(FetchPotentialRendezvousTaskEvent evt);
    }
}
