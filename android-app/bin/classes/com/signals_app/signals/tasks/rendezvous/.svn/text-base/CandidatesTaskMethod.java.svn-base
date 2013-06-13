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


public class CandidatesTaskMethod
{
    static private CopyOnWriteArrayList<CandidatesTaskListener> mCandidatesTaskListeners = new CopyOnWriteArrayList<CandidatesTaskListener>();
    
    private SherlockFragmentActivity mAct = null;;
    private CandidatesTask mCandidatesTask = null;
    
    
    public CandidatesTaskMethod(SherlockFragmentActivity act)
    {
        mAct = act;
        mCandidatesTask = new CandidatesTask();
    }
    
    
    public void doTask(Long userId)
    {
        mCandidatesTask = new CandidatesTask();
        mCandidatesTask.mActivity = mAct;
        mCandidatesTask.mMethod = this;
         
        MyParams params = new MyParams();
        params.mUserId = userId;
        
        mCandidatesTask.execute(params);
    }
    
    public void cleanUp()
    {
        mAct.setSupportProgressBarIndeterminateVisibility(false);
        mCandidatesTask.mActivity = null;
        
        if (mAct.isFinishing())
            mCandidatesTask.cancel(false);
    }
    
    class MyParams
    {
        Long mUserId;
    }
       
       
    static class CandidatesTask extends AsyncTask<MyParams, Void, String>
    {
        @Override
        protected String doInBackground(MyParams... params)
        {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://ec2-107-22-191-241.compute-1.amazonaws.com/rendezvous/candidates.php"); 
            
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
            mPeople = new ArrayList<Person>();
            
            try 
            {
                json = new JSONObject(result); 
             
                if(json.getInt(Constants.RESULT_OK) != 0)
                {
                    JSONObject jsonResult = json.getJSONObject(Constants.RESULT);
                    
                    JSONArray jsonCandidates = jsonResult.getJSONArray(Constants.CANDIDATES);
                     
                    for( int i = 0; i < jsonCandidates.length(); ++i )
                    {
                        Person person = new Person();
                        
                        json = jsonCandidates.getJSONObject(i);
                        
                        
                        // ACCOUNT
                        person.setUserId(Long.parseLong(json.getString(Constants.USER_ID))); 
                        person.setUsername(json.getString(Constants.USERNAME));
                        
                        
                        // BASIC INFO
                        person.setSex(json.getInt(Constants.SEX));
                        person.setInterestedIn(json.getInt(Constants.INTERESTED_IN));
                        
                        String[] strArr = json.getString(Constants.BIRTHDAY).split("-");
                        Calendar dob = Calendar.getInstance();
                        dob.set(Integer.valueOf(strArr[0]), Integer.valueOf(strArr[1])-1, Integer.valueOf(strArr[2]));
                        person.setBirthday(dob);
                        person.setAge(Utility.getAge(dob)); 
                        
                        
                        // PHOTOS
                        JSONArray jsonPhotosFilenames = json.getJSONArray(Constants.PROFILE_PHOTOS_FILENAMES);
                        ArrayList<String> filenames = new ArrayList<String>();
                        
                        for(int j = 0; j < jsonPhotosFilenames.length(); j++) 
                            filenames.add(jsonPhotosFilenames.getString(j));
                        
                        Collections.reverse(filenames);
                        person.setProfilePhotosFilenames(filenames); 
                        
                        
                        
                        // ABOUT
                        person.setAboutMe(json.getString(Constants.ABOUT_ME));
                        
                        person.setOccupation(json.getString(Constants.OCCUPATION));
                        person.setEducation(json.getString(Constants.EDUCATION));
                        person.setInterests(json.getString(Constants.INTERESTS)); 
                        person.setActivities(json.getString(Constants.ACTIVITIES));
                        person.setTravel(json.getString(Constants.TRAVEL)); 
                        person.setMusic(json.getString(Constants.MUSIC));
                        person.setDrinks(json.getString(Constants.DRINKS));
                        person.setMyPerfectNightOut(json.getString(Constants.MY_PERFECT_NIGHT_OUT)); 
                        
                        
                        // ACTIVITY
                        if( !json.getString(Constants.PLACE_ID).equals("0") )
                        {
                            Place p = new Place();
                            p.setPlaceId(Long.parseLong(json.getString(Constants.PLACE_ID)));
                            p.setGenre(json.getInt(Constants.GENRE));
                            p.setName(json.getString(Constants.NAME));
                            GpsPosition gpsPos = new GpsPosition((float)json.getDouble(Constants.LATITUDE), (float)json.getDouble(Constants.LONGITUDE), 0.0f);
                            p.setGpsPosition(gpsPos);
                            person.setCheckPlace(p);
                            
                            person.setStar(json.getInt(Constants.STAR) == 1);
                        }
                        
                        JSONArray jsonTopFrequentedBars = json.getJSONArray(Constants.TOP_FREQUENTED_BARS); 
                        ArrayList<Place> topFrequentedBars = new ArrayList<Place>();
                        
                        for(int j = 0; j < jsonTopFrequentedBars.length(); j++)
                        {
                            JSONObject jsonBar = jsonTopFrequentedBars.getJSONObject(j);
                            Place place = new Place();
                            place.setPlaceId(jsonBar.getLong(Constants.PLACE_ID));
                            place.setGenre(jsonBar.getInt(Constants.GENRE));
                            place.setName(jsonBar.getString(Constants.NAME));
                            topFrequentedBars.add(place);
                        }
                        person.setTopFrequentedBars(topFrequentedBars);
                        
                        JSONArray jsonCollectedBarStars = json.getJSONArray(Constants.COLLECTED_BAR_STARS);
                        ArrayList<BarStars> collectedBarStars = new ArrayList<BarStars>();
                        
                        for(int j = 0; j < jsonCollectedBarStars.length(); j++)
                        {
                            JSONObject jsonBarStar = jsonCollectedBarStars.getJSONObject(j);
                            Place place = new Place();
                            place.setPlaceId(jsonBarStar.getLong(Constants.PLACE_ID));
                            place.setGenre(jsonBarStar.getInt(Constants.GENRE));
                            place.setName(jsonBarStar.getString(Constants.NAME));
                            
                            BarStars pair = new BarStars(place, jsonBarStar.getInt(Constants.STAR_COUNT));
                        
                            collectedBarStars.add(pair);
                        }
                        person.setCollectedBarStars(collectedBarStars);
                        
                        
                        // APPROACH
                        person.setTipComeAndSayHi(json.getInt(Constants.TIP_COME_AND_SAY_HI) == 1);
                        person.setTipComeAndSayHi(json.getInt(Constants.TIP_COME_AND_SAY_HI) == 1);
                        person.setTipBuyMeADrink(json.getInt(Constants.TIP_BUY_ME_A_DRINK) == 1);
                        person.setTipInviteMeToDance(json.getInt(Constants.TIP_INVITE_ME_TO_DANCE) == 1);
                        person.setTipMakeMeLaugh(json.getInt(Constants.TIP_MAKE_ME_LAUGH) == 1);
                        person.setTipMeetMyFriends(json.getInt(Constants.TIP_MEET_MY_FRIENDS) == 1);
                        person.setTipSurpriseMe(json.getInt(Constants.TIP_SURPRISE_ME) == 1);
                        person.setDontExpectAnything(json.getInt(Constants.DONT_EXPECT_ANYTHING) == 1);
                        person.setDontBePersistent(json.getInt(Constants.DONT_BE_PERSISTENT) == 1);
                        person.setDontBeDrunk(json.getInt(Constants.DONT_BE_DRUNK) == 1);
                        person.setPersonalAdvice(json.getString(Constants.PERSONAL_ADVICE));
                        
                        
                        
                        // MORE
                        person.setTonight(json.getString(Constants.TONIGHT));
                        person.setDontApproach(json.getInt(Constants.DONT_APPROACH) == 1);
                        person.setLookingFor(json.getInt(Constants.LOOKING_FOR));
                   
                        
                        // INVITATIONS
                        person.setInvitationReceived(json.getInt(Constants.INVITATION_RECEIVED) == 1);
                        
                        mPeople.add(person);
                    }    
                      
                    
                    
                    // POTENTIAL RENDEZVOUS
                    JSONArray jsonPms = jsonResult.getJSONArray(Constants.POTENTIAL_RENDEZVOUS);
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

                            for( int j = 0; j < jsonTimes.length(); ++j )
                                times.add(jsonTimes.getInt(j)==1);
                        }
                        
                        
                        // CREATE TIME
                        Calendar createTime = Utility.convertStrToCalendar(jsonPr.getString(Constants.CREATE_TIME), "yyyy-MM-dd hh:mm");
                        Calendar now = Calendar.getInstance();
                        boolean isSameDay = createTime.get(Calendar.DAY_OF_YEAR) == now.get(Calendar.DAY_OF_YEAR);

                        if( (isSameDay && createTime.get(Calendar.HOUR_OF_DAY) > 2) || (!isSameDay && now.get(Calendar.HOUR_OF_DAY) <= 2) || (isSameDay && createTime.get(Calendar.HOUR_OF_DAY) <= 2 && now.get(Calendar.HOUR_OF_DAY) <= 2) )
                        {
                            PotentialRendezvous pm = new PotentialRendezvous(potentialRendezvousId, createTime, person, seen, places, times, firstAnswer );
                            mPotentialRendezvous.add(pm);
                        }
                    }


                    // CONFIRMED RENDEZVOUS
                    JSONArray jsonCrvs = jsonResult.getJSONArray(Constants.CONFIRMED_RENDEZVOUS);
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
                mMethod.fireCandidatesTaskEvent();
            }            
        }
 
        // These should never be accessed from within doInBackground()
        CandidatesTaskMethod mMethod = null;
        SherlockFragmentActivity mActivity = null;
        
        // Results
        boolean mSuccess = false;
        ArrayList<Person> mPeople = new ArrayList<Person>();
        ArrayList<PotentialRendezvous> mPotentialRendezvous;
        ArrayList<ConfirmedRendezvous> mConfirmedRendezvous;
    }
    

    // RESULTS
    public boolean success()
    {
        return mCandidatesTask.mSuccess;
    }
    
    public ArrayList<Person> getPeople()
    {
        return mCandidatesTask.mPeople;
    }
    
    public ArrayList<PotentialRendezvous> getPotentialRendezvous()
    {
        return mCandidatesTask.mPotentialRendezvous;
    }

    public ArrayList<ConfirmedRendezvous> getConfirmedRendezvous()
    {
        return mCandidatesTask.mConfirmedRendezvous;
    }

    
    // EVENTS
    public void addCandidatesTaskListener(CandidatesTaskListener l) 
    {
        this.mCandidatesTaskListeners.add(l);
    }

    public void removeCandidatesTaskListener(CandidatesTaskListener l) 
    {
        this.mCandidatesTaskListeners.remove(l);
    }
    
    public void fireCandidatesTaskEvent() {
        CandidatesTaskEvent evt = new CandidatesTaskEvent(this);

        for (CandidatesTaskListener l : mCandidatesTaskListeners) 
        {
            if( l != null)
                l.candidatesTaskEventReceived(evt);
        }
    }
    
    public class CandidatesTaskEvent extends EventObject {
        private static final long serialVersionUID = 0;
          public CandidatesTaskEvent(Object source) {
            super(source);
          }
        }
    
    public interface CandidatesTaskListener {
        void candidatesTaskEventReceived(CandidatesTaskEvent evt);
    }
}
