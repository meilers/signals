package com.signals_app.signals.tasks.rendezvous;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.signals_app.signals.model.Constants;
import com.signals_app.signals.model.Place;
import com.signals_app.signals.model.Profile.GpsPosition;
import com.signals_app.signals.model.Profile.Person;
import com.signals_app.signals.model.rendezvous.ConfirmedRendezvous;
import com.signals_app.signals.model.rendezvous.Rendezvous;
import com.signals_app.signals.model.rendezvous.ConfirmedRendezvous;
import com.signals_app.signals.tasks.rendezvous.CandidatesTaskMethod.MyParams;
import com.signals_app.signals.util.Utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
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


public class ConfirmRendezvousTaskMethod
{
    private CopyOnWriteArrayList<ConfirmRendezvousTaskListener> mConfirmRendezvousTaskListeners;
    
    private SherlockFragmentActivity mAct = null;;
    private ConfirmRendezvousTask mConfirmRendezvousTask = null;
    
    private Person mPerson;
    
    public ConfirmRendezvousTaskMethod(SherlockFragmentActivity act)
    {
        mAct = act;
        mConfirmRendezvousTaskListeners = new CopyOnWriteArrayList<ConfirmRendezvousTaskListener>();
        mConfirmRendezvousTask = new ConfirmRendezvousTask();
    }
    
    
    public void doTask(Long userId, Long candidateId, Long potentialRendezvousId, long placeId, int timeSlot )
    {
        mConfirmRendezvousTask = new ConfirmRendezvousTask();
        mConfirmRendezvousTask.mActivity = mAct;
        mConfirmRendezvousTask.mMethod = this;
        
        MyParams params = new MyParams();
        params.mUserId = userId;
        params.mCandidateId = candidateId;
        params.mRendezvousId = potentialRendezvousId;
        params.mPlaceId = placeId;
        params.mTimeSlot = timeSlot;
        
        
        
        mConfirmRendezvousTask.execute(params);
    }
    
    class MyParams
    {
        Long mUserId;
        Long mCandidateId;
        Long mRendezvousId;
        long mPlaceId;
        int mTimeSlot;

    }
    
    public void cleanUp()
    {
        mAct.setSupportProgressBarIndeterminateVisibility(false);
        mConfirmRendezvousTask.mActivity = null;
        
        if (mAct.isFinishing())
            mConfirmRendezvousTask.cancel(false);
    }
    
    static class ConfirmRendezvousTask extends AsyncTask<MyParams, Void, String>
    {
        @Override
        protected String doInBackground(MyParams... params)  
        {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://ec2-107-22-191-241.compute-1.amazonaws.com/rendezvous/rendezvousConfirm.php");

            try {
                JSONObject json = new JSONObject();
            
                json.put(Constants.USER_ID, params[0].mUserId);
                json.put(Constants.PERSON_ID, params[0].mCandidateId);
                json.put(Constants.RENDEZVOUS_ID, params[0].mRendezvousId);
                json.put(Constants.PLACE_SELECTED, params[0].mPlaceId);
                json.put(Constants.TIME_SELECTED, params[0].mTimeSlot);
                
                
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
                // TODO Auto-generated catch confirmRendezvous
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
                    JSONObject jsonConfirmedRv = json.getJSONObject(Constants.RESULT);
                    Long confirmedRendezvousId = Long.parseLong(jsonConfirmedRv.getString(Constants.ID));


                    // PERSON
                    Person person = new Person();
                    JSONObject jsonPerson = jsonConfirmedRv.getJSONObject(Constants.PERSON);

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
                    JSONObject jsonPlace = jsonConfirmedRv.getJSONObject(Constants.PLACE);
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
                    int timeSlot = jsonConfirmedRv.getInt(Constants.TIME_SLOT);



                    // CREATE TIME
                    Calendar createTime = Utility.convertStrToCalendar(jsonConfirmedRv.getString(Constants.CREATE_TIME), "yyyy-MM-dd hh:mm");
                    Calendar now = Calendar.getInstance();

                    if( createTime.get(Calendar.DAY_OF_YEAR) == now.get(Calendar.DAY_OF_YEAR) || now.get(Calendar.HOUR_OF_DAY) < 2 )
                    {
                        mConfirmedRendezvous = new ConfirmedRendezvous(confirmedRendezvousId, createTime, person, place, timeSlot, false, "", false, false);
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
                mMethod.fireConfirmRendezvousTaskEvent();
                
                
            }            
        }
 
        // These should never be accessed from within doInBackground()
        ConfirmRendezvousTaskMethod mMethod = null;
        SherlockFragmentActivity mActivity = null;
        
        boolean mSuccess = false;
        int mError = 0;
        
        ConfirmedRendezvous mConfirmedRendezvous = null;
    }
    
    public boolean success()
    {
        return mConfirmRendezvousTask.mSuccess;
    }
    
    public int getError()
    {
        return mConfirmRendezvousTask.mError;
    }
    
    public ConfirmedRendezvous getConfirmedRendezvous()
    {
        return mConfirmRendezvousTask.mConfirmedRendezvous;
    }
    
    public void addConfirmRendezvousTaskListener(ConfirmRendezvousTaskListener l) 
    {
        this.mConfirmRendezvousTaskListeners.add(l);
    }

    public void removeConfirmRendezvousTaskListener(ConfirmRendezvousTaskListener l) 
    {
        this.mConfirmRendezvousTaskListeners.remove(l);
    }
    
    public void fireConfirmRendezvousTaskEvent() {
        ConfirmRendezvousTaskEvent evt = new ConfirmRendezvousTaskEvent(this);

        for (ConfirmRendezvousTaskListener l : mConfirmRendezvousTaskListeners) 
        {
            if( l != null)
                l.confirmRendezvousTaskEventReceived(evt);
        }
    }
    
    public class ConfirmRendezvousTaskEvent extends EventObject {
        private static final long serialVersionUID = 0;
                
          // This event definition is stateless but you could always
          // add other information here.
          public ConfirmRendezvousTaskEvent(Object source) {
            super(source);
          }
        }
    
    public interface ConfirmRendezvousTaskListener {
        void confirmRendezvousTaskEventReceived(ConfirmRendezvousTaskEvent evt);
    }
}
