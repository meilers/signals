package com.signals_app.signals.tasks.rendezvous;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.signals_app.signals.model.Constants;
import com.signals_app.signals.model.Place;
import com.signals_app.signals.model.Profile.GpsPosition;
import com.signals_app.signals.model.Profile.Person;
import com.signals_app.signals.model.rendezvous.Rendezvous;
import com.signals_app.signals.model.rendezvous.PotentialRendezvous;
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


public class InvitationTaskMethod
{
    private CopyOnWriteArrayList<InvitationTaskListener> mInvitationTaskListeners;
    
    private SherlockFragmentActivity mAct = null;;
    private InvitationTask mInvitationTask = null;
    
    private Person mPerson;
    
    public InvitationTaskMethod(SherlockFragmentActivity act)
    {
        mAct = act;
        mInvitationTaskListeners = new CopyOnWriteArrayList<InvitationTaskListener>();
        mInvitationTask = new InvitationTask();
    }
    
    
    public void doTask(Long userId, Person person)
    {
        mInvitationTask = new InvitationTask();
        mInvitationTask.mActivity = mAct;
        mInvitationTask.mMethod = this;
        
        mPerson = person;
        
        mInvitationTask.execute(userId, person.getUserId());
    }
    
    public void cleanUp()
    {
        mAct.setSupportProgressBarIndeterminateVisibility(false);
        mInvitationTask.mActivity = null;
        
        if (mAct.isFinishing())
            mInvitationTask.cancel(false);
    }
    
    static class InvitationTask extends AsyncTask<Long, Void, String>
    {
        @Override
        protected String doInBackground(Long... params)  
        {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://ec2-107-22-191-241.compute-1.amazonaws.com/rendezvous/invite.php");

            try {
                JSONObject json = new JSONObject();
            
                json.put(Constants.USER_ID, params[0]);
                json.put(Constants.PERSON_ID, params[1]);
                
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
                // TODO Auto-generated catch invitation
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
                    if( !json.isNull(Constants.RESULT) )
                    {
                        JSONObject jsonResult = json.getJSONObject(Constants.RESULT);
                        Long potentialRendezvousId = Long.parseLong(jsonResult.getString(Constants.ID));
                        
                        // PLACES
                        JSONArray jsonPlaces = jsonResult.getJSONArray(Constants.PLACES);
                        ArrayList<Place> places = new ArrayList<Place>();
                        
                        for( int i = 0; i < jsonPlaces.length(); ++i )
                        {
                            JSONObject jsonPlace = jsonPlaces.getJSONObject(i);
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
                        
                        // TIME
                        ArrayList<Boolean> times = new ArrayList<Boolean>(10);
                        
                        for( int j=0; j<10; ++j )
                            times.add(true);
                        
                        
                        // CREATE TIME
                        Calendar createTime = Utility.convertStrToCalendar(jsonResult.getString(Constants.CREATE_TIME), "yyyy-MM-dd hh:mm");
                        Calendar now = Calendar.getInstance();
                        
                        if( createTime.get(Calendar.DAY_OF_YEAR) == now.get(Calendar.DAY_OF_YEAR) || now.get(Calendar.HOUR_OF_DAY) < 2 )
                        {
                            if(mMethod != null)
                                mPotentialRendezvous = new PotentialRendezvous(potentialRendezvousId, createTime, mMethod.mPerson, false, places, times, true );
                        }
                        
                    }
                    
                    
                    
                    
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
                mMethod.fireInvitationTaskEvent();
                
                
            }            
        }
 
        // These should never be accessed from within doInBackground()
        InvitationTaskMethod mMethod = null;
        SherlockFragmentActivity mActivity = null;
        
        boolean mSuccess = false;
        int mError = 0;
        
        PotentialRendezvous mPotentialRendezvous = null;
    }
    
    public boolean success()
    {
        return mInvitationTask.mSuccess;
    }
    
    public int getError()
    {
        return mInvitationTask.mError;
    }
    
    public PotentialRendezvous getPotentialRendezvous()
    {
        return mInvitationTask.mPotentialRendezvous;
    }
    
    public void addInvitationTaskListener(InvitationTaskListener l) 
    {
        this.mInvitationTaskListeners.add(l);
    }

    public void removeInvitationTaskListener(InvitationTaskListener l) 
    {
        this.mInvitationTaskListeners.remove(l);
    }
    
    public void fireInvitationTaskEvent() {
        InvitationTaskEvent evt = new InvitationTaskEvent(this);

        for (InvitationTaskListener l : mInvitationTaskListeners) 
        {
            if( l != null)
                l.invitationTaskEventReceived(evt);
        }
    }
    
    public class InvitationTaskEvent extends EventObject {
        private static final long serialVersionUID = 0;
                
          // This event definition is stateless but you could always
          // add other information here.
          public InvitationTaskEvent(Object source) {
            super(source);
          }
        }
    
    public interface InvitationTaskListener {
        void invitationTaskEventReceived(InvitationTaskEvent evt);
    }
}
