package com.signals_app.signals.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.actionbarsherlock.app.SherlockFragment;
import com.facebook.*;
import com.facebook.internal.Utility;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.signals_app.signals.R;
import com.signals_app.signals.model.Profile.Person;
import com.signals_app.signals.model.Profile.User;
import com.signals_app.signals.tasks.FetchCitiesTaskMethod;
import com.signals_app.signals.tasks.FetchCitiesTaskMethod.FetchCitiesTaskEvent;
import com.signals_app.signals.tasks.FetchCitiesTaskMethod.FetchCitiesTaskListener;
import com.signals_app.signals.tasks.LoginTaskMethod;
import com.signals_app.signals.tasks.LoginTaskMethod.LoginTaskListener;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by omegatai on 2013-05-24.
 */
public class Intro1Fragment extends SherlockFragment implements LoginTaskListener, FetchCitiesTaskListener
{
    public static final int MIN_NB_FRIENDS = 50;

    private GraphUser mFacebookUser = null;
    private Session.StatusCallback mCallback = new Session.StatusCallback() {
        @Override
        public void call(final Session session, final SessionState state, final Exception exception) {
            onSessionStateChange(session, state, exception);
        }
    };


    LoginTaskMethod mLoginTaskMethod;
    FetchCitiesTaskMethod mFetchCitiesTaskMethod;


    private Button mLoginButton;
    private ImageView mLogoIv;
    private LinearLayout mWeWillNeverLayout;


    public static final Intro1Fragment newInstance()
    {
        Intro1Fragment f = new Intro1Fragment();

        Bundle args = f.getArguments();
        if (args == null) {
            args = new Bundle();
        }

        f.setArguments(args);

        return f;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mLoginTaskMethod = new LoginTaskMethod(getSherlockActivity());
        mFetchCitiesTaskMethod = new FetchCitiesTaskMethod(getSherlockActivity());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_intro1, container, false);


        mLogoIv = (ImageView) view.findViewById(R.id.logo_iv);
        mLoginButton = (Button)view.findViewById(R.id.login_btn);
        mWeWillNeverLayout = (LinearLayout)view.findViewById(R.id.we_will_never_layout);

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickLogin();
            }
        });


        mLoginTaskMethod.addLoginTaskListener(this);
        mFetchCitiesTaskMethod.addFetchCitiesTaskListener(this);




        return view;
    }



    @Override
    public void onResume()
    {
        super.onResume();

        /*
        Session session = Session.getActiveSession();
        if (session != null &&
                (session.isOpened() || session.isClosed()) ) {
            onSessionStateChange(session, session.getState(), null);
        }
        */
    }



    // Fragment no longer visible
    @Override
    public void onPause()
    {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();

        mLoginTaskMethod.removeLoginTaskListener(this);
        mFetchCitiesTaskMethod.removeFetchCitiesTaskListener(this);

        mLoginTaskMethod.cleanUp();
        mFetchCitiesTaskMethod.cleanUp();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (Session.getActiveSession() != null)
            Session.getActiveSession().onActivityResult(getSherlockActivity(), requestCode, resultCode, data);

        Session currentSession = Session.getActiveSession();

        if (currentSession == null || currentSession.getState().isClosed()) {
            Session session = new Session.Builder(getSherlockActivity()).build();
            Session.setActiveSession(session);
            currentSession = session;
        }

        if (currentSession.isOpened()) {
            Session.openActiveSession(getSherlockActivity(), true, mCallback);
        }
    }


    /*
    private void fillProfileWithFacebook(GraphUser user)
    {
        mFacebookUser = user;

        if( mFacebookUser != null )
        {


            JSONObject jsonObject = user.getInnerJSONObject();

            // Profile Pic
            String url = "http://graph.facebook.com/" + user.getId() + "/picture?width=300&height=300";

            mFbProfilePicTaskMethod.doTask(url);




            // Birthday
            String birthday = user.getBirthday();

            if( birthday != null )
            {
                String[] strArr = birthday.split("/");

                Calendar cal = Calendar.getInstance();
                cal.set(Integer.valueOf(strArr[2]), Integer.valueOf(strArr[0])-1, Integer.valueOf(strArr[1]));
                setBirthday(cal);
            }


            // Gender
            try {

                String sex = jsonObject.getString("gender");

                if( sex.equals("female") )
                {
                    mSex = Person.SEX_FEMALE;

                    mFemaleImg.setVisibility(View.VISIBLE);
                    mMaleImg.setVisibility(View.GONE);
                }
                else
                {
                    mSex = Person.SEX_MALE;

                    mFemaleImg.setVisibility(View.GONE);
                    mMaleImg.setVisibility(View.VISIBLE);
                }
            }
            catch (JSONException ignored) {}


            // Interested In
            try {

                JSONArray jsonArr = jsonObject.getJSONArray("interested_in");
                mInterestedIn = Person.INTERESTED_IN_NONE;
                mInterestedInWomenImg.setVisibility(View.GONE);
                mInterestedInMenImg.setVisibility(View.GONE);

                for(int i = 0; i < jsonArr.length(); i++)
                {
                    String interestedIn = jsonArr.getString(i);

                    if( interestedIn.equals("female"))
                    {
                        mInterestedInWomenImg.setVisibility(View.VISIBLE);

                        if( mInterestedIn == Person.INTERESTED_IN_NONE  )
                        {
                            mInterestedIn = Person.INTERESTED_IN_WOMEN;
                        }
                        else if( mInterestedIn == Person.INTERESTED_IN_MEN  )
                        {
                            mInterestedIn = Person.INTERESTED_IN_BOTH;
                        }
                    }
                    else
                    {
                        mInterestedInMenImg.setVisibility(View.VISIBLE);

                        if( mInterestedIn == Person.INTERESTED_IN_NONE  )
                        {
                            mInterestedIn = Person.INTERESTED_IN_MEN;
                        }
                        else if( mInterestedIn == Person.INTERESTED_IN_WOMEN  )
                        {
                            mInterestedIn = Person.INTERESTED_IN_BOTH;
                        }
                    }
                }
            }
            catch (JSONException ignored) {}
        }

        LinearLayout useFbLayout = (LinearLayout)this.findViewById(R.id.use_facebook_layout);
        useFbLayout.setVisibility(View.GONE);


        mUseFacebook = false;
    }
*/

    private void onSessionStateChange(Session session, SessionState state, Exception exception) {
        if (session.isOpened()) {

            Request.executeMeRequestAsync(session,
                new Request.GraphUserCallback() {

                    @Override
                    public void onCompleted(GraphUser user, Response response) {
                        if (user != null) {
                            mFacebookUser = user;
                            mLoginTaskMethod.doTask(mFacebookUser.getId());
                        }
                    }
                });
        }
    }


    // EVENTS

    private void onClickLogin()
    {
        Session currentSession = Session.getActiveSession();

        if (currentSession == null || currentSession.getState().isClosed()) {
            Session session = new Session.Builder(getSherlockActivity()).build();
            Session.setActiveSession(session);
            currentSession = session;
        }

        // Logout to allow new user
        //currentSession.closeAndClearTokenInformation();


        if (currentSession.isOpened()) {
            Session.openActiveSession(getSherlockActivity(), true, mCallback);

        } else if (!currentSession.isOpened()) {
            // Ask for username and password
            Session.OpenRequest op = new Session.OpenRequest(getSherlockActivity());

            op.setLoginBehavior(SessionLoginBehavior.SUPPRESS_SSO);
            op.setCallback(mCallback);

            List<String> permissions = new ArrayList<String>();
            permissions.add("publish_stream");
            permissions.add("email");

            permissions.add("user_birthday");
            permissions.add("user_relationships");
            permissions.add("user_relationship_details");
            permissions.add("user_education_history");
            permissions.add("user_work_history");
            permissions.add("user_work_history");

            permissions.add("user_about_me");
            permissions.add("user_interests");
            permissions.add("user_activities");
            permissions.add("user_likes");

            permissions.add("user_photos");
            permissions.add("user_location");


            op.setPermissions(permissions);

            Session session = new Session.Builder(getSherlockActivity()).build();
            Session.setActiveSession(session);
            session.openForPublish(op);
        }
    }

    @Override
    public void loginTaskEventReceived(LoginTaskMethod.LoginTaskEvent evt) {
        LoginTaskMethod met = (LoginTaskMethod)evt.getSource();

        // LOGIN SUCCEEDED
        if( met != null && met.success() )
        {
            User.getInstance().setLoggedIn(true);

            Intent broadcastIntent = new Intent();
            broadcastIntent.setAction("com.package.ACTION_LOGIN");
            getSherlockActivity().sendBroadcast(broadcastIntent);

            Intent intent = new Intent(getSherlockActivity(), MainActivity.class);
            startActivity(intent);
            getSherlockActivity().finish();
        }
        else
        {
            mFetchCitiesTaskMethod.doTask();
        }

    }

    @Override
    public void fetchCitiesTaskEventReceived(FetchCitiesTaskEvent evt) {
        final FetchCitiesTaskMethod met = (FetchCitiesTaskMethod)evt.getSource();

        // LOGIN SUCCEEDED
        if( met != null && met.success() )
        {
            // CHECK IF ENOUGH FRIENDS
            if( mFacebookUser != null )
            {
                String fqlQuery = "{" +
                        "'user':'SELECT friend_count, email, sex, meeting_sex, about_me, education, work, interests, activities, sports, music  FROM user where uid =" + mFacebookUser.getId() + "',"  +
                        "'photos':'SELECT src_big FROM photo WHERE aid IN (SELECT aid FROM album WHERE owner=" + mFacebookUser.getId() + " AND type=\"profile\")'"  +
                        "}";



                Bundle bundle = new Bundle();
                bundle.putString("q", fqlQuery);
                Request request = new Request(Session.getActiveSession(), "/fql", bundle, HttpMethod.GET, new Request.Callback() {
                    @Override
                    public void onCompleted(Response response)
                    {
                        JSONObject graphResponse = response.getGraphObject().getInnerJSONObject();

                        try {
                            JSONArray jsonResult = graphResponse.getJSONArray("data");

                            JSONArray jsonPhotos = jsonResult.getJSONObject(0).getJSONArray("fql_result_set");
                            JSONObject jsonUser = jsonResult.getJSONObject(1).getJSONArray("fql_result_set").getJSONObject(0);

                            if( jsonUser.getInt("friend_count") >= MIN_NB_FRIENDS )
                            {

                                Intent intent = new Intent(getSherlockActivity(), RegisterActivity.class);
                                Bundle args = new Bundle();

                                // register1
                                args.putString("facebookId", mFacebookUser.getId());
                                args.putString("email", jsonUser.getString("email"));
                                args.putString("firstName", mFacebookUser.getFirstName());
                                args.putString("birthday", mFacebookUser.getBirthday());
                                args.putString("sex", jsonUser.getString("sex"));
                                args.putSerializable("cities", met.getCities());

                                ArrayList<String> meetingSexes = new ArrayList<String>();
                                JSONArray jsonMeetingSexes = jsonUser.getJSONArray("meeting_sex");

                                for(int i=0; i < jsonMeetingSexes.length(); ++i)
                                    meetingSexes.add(jsonMeetingSexes.getString(i));

                                args.putStringArrayList("meetingSexes", meetingSexes);

                                if( mFacebookUser.getLocation() != null )
                                {
                                    String city[] = mFacebookUser.getLocation().getProperty("name").toString().split(", ");

                                    args.putString("city", city[0]);
                                    args.putString("country", city[1]);
                                }
                                else
                                {
                                    args.putString("city", "");
                                    args.putString("country", "");
                                }


                                // register2
                                ArrayList<String> photoUrls = new ArrayList<String>();

                                for(int i=0; i < jsonPhotos.length(); ++i)
                                    photoUrls.add(jsonPhotos.getJSONObject(i).getString("src_big"));

                                args.putStringArrayList("fbPhotoUrls", photoUrls);


                                // register3
                                args.putString("aboutMe", jsonUser.getString("about_me"));
                                args.putString("education", jsonUser.getString("education"));
                                args.putString("work", jsonUser.getString("work"));
                                args.putString("interests", jsonUser.getString("interests"));
                                args.putString("activities", jsonUser.getString("activities"));
                                args.putString("sports", jsonUser.getString("sports"));
                                args.putString("music", jsonUser.getString("music"));

                                intent.putExtras(args);
                                startActivity(intent);
                                getSherlockActivity().finish();


                            }
                            else
                            {

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                });
                Request.executeBatchAsync(request);
            }
        }
    }
}
