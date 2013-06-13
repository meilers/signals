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
import com.signals_app.signals.model.rendezvous.ConfirmedRendezvous;
import com.signals_app.signals.util.Utility;


public class FetchConfirmedRendezvousTaskMethod
{
    static private CopyOnWriteArrayList<FetchConfirmedRendezvousTaskListener> mFetchConfirmedRendezvousTaskListeners = new CopyOnWriteArrayList<FetchConfirmedRendezvousTaskListener>();

    private SherlockFragmentActivity mAct = null;;
    private FetchConfirmedRendezvousTask mFetchConfirmedRendezvousTask = null;


    public FetchConfirmedRendezvousTaskMethod(SherlockFragmentActivity act)
    {
        mAct = act;
        mFetchConfirmedRendezvousTask = new FetchConfirmedRendezvousTask();
    }


    public void doTask(Long userId)
    {
        mFetchConfirmedRendezvousTask = new FetchConfirmedRendezvousTask();
        mFetchConfirmedRendezvousTask.mActivity = mAct;
        mFetchConfirmedRendezvousTask.mMethod = this;

        MyParams params = new MyParams();
        params.mUserId = userId;

        mFetchConfirmedRendezvousTask.execute(params);
    }

    public void cleanUp()
    {
        mAct.setSupportProgressBarIndeterminateVisibility(false);
        mFetchConfirmedRendezvousTask.mActivity = null;

        if (mAct.isFinishing())
            mFetchConfirmedRendezvousTask.cancel(false);
    }

    class MyParams
    {
        Long mUserId;
    }


    static class FetchConfirmedRendezvousTask extends AsyncTask<MyParams, Void, String>
    {
        @Override
        protected String doInBackground(MyParams... params)
        {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://ec2-107-22-191-241.compute-1.amazonaws.com/rendezvous/fetchConfirmedRendezvous.php");

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
                    // CONFIRMED RENDEZVOUS
                    JSONArray jsonCrvs = json.getJSONArray(Constants.RESULT);
                    mConfirmedRendezvous = new ArrayList<ConfirmedRendezvous>(jsonCrvs.length());

                    for( int i = 0; i < jsonCrvs.length(); ++i )
                    {
                        JSONObject jsonCrv = jsonCrvs.getJSONObject(i);
                        Long confirmedRendezvousId = Long.parseLong(jsonCrv.getString(Constants.ID));


                        // IS CANCELLED
                        boolean cancelled = jsonCrv.getInt(Constants.CANCELLED) == 1;
                        String cancelReason = jsonCrv.getString(Constants.CANCEL_REASON);
                        boolean cancelledByCandidate = jsonCrv.getInt(Constants.CANCELLED_BY_CANDIDATE) == 1;

                        // SEEN
                        boolean seen = jsonCrv.getInt(Constants.SEEN) == 1;


                        // PERSON
                        Person person = new Person();
                        JSONObject jsonPerson = jsonCrv.getJSONObject(Constants.PERSON);

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


                        // PLACE
                        JSONObject jsonPlace = jsonCrv.getJSONObject(Constants.PLACE);
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



                        // TIME
                        int timeSlot = jsonCrv.getInt(Constants.TIME_SLOT);



                        // CREATE TIME
                        Calendar createTime = Utility.convertStrToCalendar(jsonCrv.getString(Constants.CREATE_TIME), "yyyy-MM-dd hh:mm");
                        Calendar now = Calendar.getInstance();
                        boolean isSameDay = createTime.get(Calendar.DAY_OF_YEAR) == now.get(Calendar.DAY_OF_YEAR);

                        if( (isSameDay && createTime.get(Calendar.HOUR_OF_DAY) > 2) || (!isSameDay && now.get(Calendar.HOUR_OF_DAY) <= 2) || (isSameDay && createTime.get(Calendar.HOUR_OF_DAY) <= 2 && now.get(Calendar.HOUR_OF_DAY) <= 2) )
                        {


                            ConfirmedRendezvous confirmedRendezvous = new ConfirmedRendezvous(confirmedRendezvousId, createTime, person, place, timeSlot, cancelled, cancelReason, cancelledByCandidate, seen);
                            mConfirmedRendezvous.add(confirmedRendezvous);
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
                mMethod.fireFetchConfirmedRendezvousTaskEvent();
            }
        }

        // These should never be accessed from within doInBackground()
        FetchConfirmedRendezvousTaskMethod mMethod = null;
        SherlockFragmentActivity mActivity = null;

        // Results
        boolean mSuccess = false;
        ArrayList<ConfirmedRendezvous> mConfirmedRendezvous;
    }


    // RESULTS
    public boolean success()
    {
        return mFetchConfirmedRendezvousTask.mSuccess;
    }

    public ArrayList<ConfirmedRendezvous> getConfirmedRendezvous()
    {
        return mFetchConfirmedRendezvousTask.mConfirmedRendezvous;
    }

    // EVENTS
    public void addFetchConfirmedRendezvousTaskListener(FetchConfirmedRendezvousTaskListener l)
    {
        this.mFetchConfirmedRendezvousTaskListeners.add(l);
    }

    public void removeFetchConfirmedRendezvousTaskListener(FetchConfirmedRendezvousTaskListener l)
    {
        this.mFetchConfirmedRendezvousTaskListeners.remove(l);
    }

    public void fireFetchConfirmedRendezvousTaskEvent() {
        FetchConfirmedRendezvousTaskEvent evt = new FetchConfirmedRendezvousTaskEvent(this);

        for (FetchConfirmedRendezvousTaskListener l : mFetchConfirmedRendezvousTaskListeners)
        {
            if( l != null)
                l.fetchConfirmedRendezvousTaskEventReceived(evt);
        }
    }

    public class FetchConfirmedRendezvousTaskEvent extends EventObject {
        private static final long serialVersionUID = 0;
        public FetchConfirmedRendezvousTaskEvent(Object source) {
            super(source);
        }
    }

    public interface FetchConfirmedRendezvousTaskListener {
        void fetchConfirmedRendezvousTaskEventReceived(FetchConfirmedRendezvousTaskEvent evt);
    }
}
