package com.signals_app.signals.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;


import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Window;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.Session.StatusCallback;
import com.facebook.SessionLoginBehavior;
import com.facebook.SessionState;
import com.facebook.internal.SessionTracker;
import com.facebook.internal.Utility;
import com.facebook.model.GraphUser;
import com.signals_app.signals.R;
import com.signals_app.signals.model.Profile.Person;
import com.signals_app.signals.model.Profile.User;
import com.signals_app.signals.tasks.FbProfilePicTaskMethod;
import com.signals_app.signals.tasks.FbProfilePicTaskMethod.FbProfilePicTaskEvent;

import android.text.Html;
import android.view.ContextThemeWrapper;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


public class RegisterAccountActivity extends SherlockFragmentActivity implements FbProfilePicTaskMethod.FbProfilePicTaskListener
{
    private Session mCurrentSession;
    private boolean mUseFacebook = false;


    private Integer mSex;
    private Integer mInterestedIn;
    private int mWantedMinAge;
    private int mWantedMaxAge;

    private Calendar mBirthday;
    private String mPhotoCropPath = "";
    private String mPhotoOriginalPath = "";



    private AddProfilePhotoDialogFragment mAddProfilePhotoDialogFragment;

    private ImageView mMaleImg;
    private ImageView mFemaleImg;
    private ImageView mInterestedInMenImg;
    private ImageView mInterestedInWomenImg;
    private TextView mBetweenAgesText;

    private FbProfilePicTaskMethod mFbProfilePicTaskMethod;

    private BroadcastReceiver mLoginReceiver;
    private IntentFilter mLoginIntentFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_PROGRESS);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setTitle(R.string.sign_up);
        setContentView(R.layout.activity_register);




        // ATTRIBUTES & TASKS
        mSex = Person.SEX_FEMALE;
        mInterestedIn = Person.INTERESTED_IN_NONE;
        mWantedMinAge = 21;
        mWantedMaxAge = 35;
        mBirthday = Calendar.getInstance();

        mFbProfilePicTaskMethod = new FbProfilePicTaskMethod(this);



        // FACEBOOK

        final ImageView circlexImg =  (ImageView)findViewById(R.id.circlex_btn);
        final LinearLayout useFbLayout = (LinearLayout)this.findViewById(R.id.use_facebook_layout);


        useFbLayout.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                circlexImg.setVisibility(View.VISIBLE);
                return false;
            }
        });



        circlexImg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                useFbLayout.setVisibility(View.GONE);
            }
        });


        ImageButton fbBtn = (ImageButton)findViewById(R.id.facebook_btn);
        fbBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View view) { onClickFacebook(); }
        });




        // PHOTO

        mAddProfilePhotoDialogFragment = AddProfilePhotoDialogFragment.newInstance(true);

        RelativeLayout photoLayout = (RelativeLayout)findViewById(R.id.photo_layout);
        photoLayout.setOnClickListener(new OnClickListener() {
            public void onClick(View view) { onClickPhoto(); }
        });





        // SEX

        mFemaleImg = (ImageView)findViewById(R.id.check_female_img);
        mMaleImg = (ImageView)findViewById(R.id.check_male_img);

        if( mSex == Person.SEX_FEMALE )
        {
            mFemaleImg.setVisibility(View.VISIBLE);
            mMaleImg.setVisibility(View.GONE);
        }
        else
        {
            mFemaleImg.setVisibility(View.GONE);
            mMaleImg.setVisibility(View.VISIBLE);
        }


        LinearLayout maleRow = (LinearLayout) findViewById(R.id.male_pref_row);
        maleRow.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mFemaleImg.setVisibility(View.GONE);
                mMaleImg.setVisibility(View.VISIBLE);

                mSex = Person.SEX_MALE;
            }
        });

        LinearLayout femaleRow = (LinearLayout) findViewById(R.id.female_pref_row);
        femaleRow.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mFemaleImg.setVisibility(View.VISIBLE);
                mMaleImg.setVisibility(View.GONE);

                mSex = Person.SEX_FEMALE;
            }
        });



        // ORIENTATION

        mInterestedInWomenImg = (ImageView)findViewById(R.id.check_interested_in_women_img);
        mInterestedInMenImg = (ImageView)findViewById(R.id.check_interested_in_men_img);


        if( mInterestedIn == Person.INTERESTED_IN_WOMEN)
        {
            mInterestedInWomenImg.setVisibility(View.VISIBLE);
            mInterestedInMenImg.setVisibility(View.GONE);
        }
        else if( mInterestedIn == Person.INTERESTED_IN_MEN)
        {
            mInterestedInMenImg.setVisibility(View.VISIBLE);
            mInterestedInWomenImg.setVisibility(View.GONE);
        }
        else if( mInterestedIn == Person.INTERESTED_IN_BOTH)
        {
            mInterestedInMenImg.setVisibility(View.VISIBLE);
            mInterestedInWomenImg.setVisibility(View.VISIBLE);
        }
        else
        {
            mInterestedInWomenImg.setVisibility(View.GONE);
            mInterestedInMenImg.setVisibility(View.GONE);
        }


        LinearLayout interestedInMenRow = (LinearLayout) findViewById(R.id.interested_in_men_row);
        interestedInMenRow.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if( mInterestedIn == Person.INTERESTED_IN_NONE  )
                {
                    mInterestedInMenImg.setVisibility(View.VISIBLE);
                    mInterestedIn = Person.INTERESTED_IN_MEN;
                }
                else if( mInterestedIn == Person.INTERESTED_IN_WOMEN  )
                {
                    mInterestedInMenImg.setVisibility(View.VISIBLE);
                    mInterestedIn = Person.INTERESTED_IN_BOTH;
                }
                else if( mInterestedIn == Person.INTERESTED_IN_MEN  )
                {
                    mInterestedInMenImg.setVisibility(View.GONE);
                    mInterestedIn = Person.INTERESTED_IN_NONE;
                }
                else if( mInterestedIn == Person.INTERESTED_IN_BOTH  )
                {
                    mInterestedInMenImg.setVisibility(View.GONE);
                    mInterestedIn = Person.INTERESTED_IN_WOMEN;
                }

            }
        });

        LinearLayout interestedInWomenRow = (LinearLayout) findViewById(R.id.interested_in_women_row);
        interestedInWomenRow.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if( mInterestedIn == Person.INTERESTED_IN_NONE  )
                {
                    mInterestedInWomenImg.setVisibility(View.VISIBLE);
                    mInterestedIn = Person.INTERESTED_IN_WOMEN;
                }
                else if( mInterestedIn == Person.INTERESTED_IN_MEN  )
                {
                    mInterestedInWomenImg.setVisibility(View.VISIBLE);
                    mInterestedIn = Person.INTERESTED_IN_BOTH;
                }
                else if( mInterestedIn == Person.INTERESTED_IN_WOMEN  )
                {
                    mInterestedInWomenImg.setVisibility(View.GONE);
                    mInterestedIn = Person.INTERESTED_IN_NONE;
                }
                else if( mInterestedIn == Person.INTERESTED_IN_BOTH  )
                {
                    mInterestedInWomenImg.setVisibility(View.GONE);
                    mInterestedIn = Person.INTERESTED_IN_MEN;
                }
            }
        });


        // BETWEEN AGES
        RelativeLayout betweenAgesRow = (RelativeLayout) findViewById(R.id.between_ages_row);
        betweenAgesRow.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickBetweenAges();
            }
        });
        mBetweenAgesText = (TextView)RegisterAccountActivity.this.findViewById(R.id.between_ages_text);
        mBetweenAgesText.setText(mWantedMinAge + " " + getResources().getString(R.string.and) + " " + mWantedMaxAge);


        // BIRTHDAY
        RelativeLayout birthdayRow = (RelativeLayout) findViewById(R.id.birthday_row);
        birthdayRow.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickBirthday();
            }
        });

        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR)-21, cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        setBirthday(cal);



        // I AGREE

        CheckBox box = (CheckBox)findViewById(R.id.i_agree_toggle);
        box.setText(Html.fromHtml(getString(R.string.i_agree)));


        // DONE BUTTON
        Button continueButton = (Button)findViewById(R.id.continue_btn);
        continueButton.setOnClickListener( new View.OnClickListener()
        {
            public void onClick(View view)
            {
                onClickContinue();
            }
        });



        // LISTEN TO LOGIN
        mLoginIntentFilter = new IntentFilter();
        mLoginIntentFilter.addAction("com.package.ACTION_LOGIN");

        mLoginReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                finish();
            }
        };

        registerReceiver(mLoginReceiver, mLoginIntentFilter);
    }


    @Override
    public void onStart() {
        super.onStart();
        mFbProfilePicTaskMethod.addFbProfilePicTaskListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        mFbProfilePicTaskMethod.removeFbProfilePicTaskListener(this);
        mFbProfilePicTaskMethod.cleanUp();
    }




    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mLoginReceiver);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if( mUseFacebook )
        {
            Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);

            if (mCurrentSession.isOpened()) {
                Request.executeMeRequestAsync(mCurrentSession, new Request.GraphUserCallback() {

                    // callback after Graph API response with user object
                    @Override
                    public void onCompleted(GraphUser user, Response response) {
                        fillProfileWithFacebook( user );

                    }
                });
            }
        }

    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }



    // FACEBOOK

    private void signInWithFacebook() {

        mUseFacebook = true;

        String applicationId = Utility.getMetadataApplicationId(getBaseContext());
        mCurrentSession = Session.getActiveSession();

        if(mCurrentSession != null)
        {
            mCurrentSession.closeAndClearTokenInformation();
            mCurrentSession = null;
        }

        if (mCurrentSession == null) {
            mCurrentSession = new Session.Builder(this).setApplicationId(applicationId).build();
            Session.setActiveSession(mCurrentSession);
        }

        if (!mCurrentSession.isOpened()) {
            Session.OpenRequest openRequest = null;
            openRequest = new Session.OpenRequest(RegisterAccountActivity.this);

            if (openRequest != null) {
                //openRequest.setDefaultAudience(SessionDefaultAudience.FRIENDS);
                openRequest.setPermissions(null);//Arrays.asList("user_birthday", "user_location", "email"));
                openRequest.setLoginBehavior(SessionLoginBehavior.SSO_WITH_FALLBACK);

                mCurrentSession.openForRead(openRequest);
            }
        }else {
            Request.executeMeRequestAsync(mCurrentSession, new Request.GraphUserCallback() {
                @Override
                public void onCompleted(GraphUser user, Response response) {
                    fillProfileWithFacebook( user );
                }
            });
        }
    }

    private void fillProfileWithFacebook(GraphUser user)
    {
        if( user != null )
        {
            JSONObject jsonObject = user.getInnerJSONObject();

            // Profile Pic
            String url = "http://graph.facebook.com/" + user.getId() + "/picture?width=300&height=300";
            setSupportProgressBarIndeterminateVisibility(true);
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



    public void setBirthday( Calendar cal )
    {
        mBirthday.setTime(cal.getTime());

        String str = (mBirthday.get(Calendar.MONTH)+1) + "-" + mBirthday.get(Calendar.DAY_OF_MONTH) + "-" + mBirthday.get(Calendar.YEAR);
        TextView birthdayText = (TextView)RegisterAccountActivity.this.findViewById(R.id.birthday_text);
        birthdayText.setText(str);
    }


    public void setWantedAges( int wantedMinAge, int wantedMaxAge )
    {
        mWantedMinAge = Math.min(wantedMinAge, wantedMaxAge);
        mWantedMaxAge = Math.max(wantedMinAge, wantedMaxAge);

        mBetweenAgesText.setText(mWantedMinAge + " " + getResources().getString(R.string.and) + " " + mWantedMaxAge);
    }

    public void setProfilePhotosUris( String originalPath, String cropPath )
    {
        mPhotoCropPath = cropPath;
        mPhotoOriginalPath = originalPath;

        Bitmap bm = (Bitmap)BitmapFactory.decodeFile(mPhotoCropPath);
        setProfilePicImg( bm );


    }

    public void setProfilePicImg( Bitmap bm )
    {
        ImageView profileIv = (ImageView)this.findViewById(R.id.profile_pic_img);
        profileIv.setImageBitmap(bm);
        profileIv.setVisibility(View.VISIBLE);
    }



    // EVENTS

    private void onClickFacebook() {

        signInWithFacebook();
    }


    private void onClickPhoto() {
        AddProfilePhotoDialogFragment.newInstance(true).show(getSupportFragmentManager(), "addPhotoDialog");
    }

    private void onClickBirthday() {
        DatePickerDialog dateDlg = new DatePickerDialog( new ContextThemeWrapper(this, R.style.SignalsThemeDialog), new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
            {
                Calendar cal = Calendar.getInstance();
                cal.set(year, monthOfYear, dayOfMonth);
                setBirthday(cal);
            }
        }, mBirthday.get(Calendar.YEAR), mBirthday.get(Calendar.MONTH), mBirthday.get(Calendar.DAY_OF_MONTH));

        dateDlg.setButton(DatePickerDialog.BUTTON_POSITIVE, getResources().getString(R.string.done), dateDlg);
        dateDlg.show();

    }

    private void onClickBetweenAges() {
        PickAgesDialogFragment.newInstance(mWantedMinAge, mWantedMaxAge).show(getSupportFragmentManager(), "pickAgesDialog");
    }

    private void onClickContinue()
    {
        boolean error = false;

        // ERROR: No interests
        if( mInterestedIn == Person.INTERESTED_IN_NONE )
        {
            Toast.makeText(getApplicationContext(), getString(R.string.error_no_interests), Toast.LENGTH_LONG).show();
            error = true;
        }

        // ERROR:
        if( com.signals_app.signals.util.Utility.getAge(mBirthday) < 18 )
        {
            Toast.makeText(getApplicationContext(), getString(R.string.error_not_18), Toast.LENGTH_LONG).show();
            error = true;
        }


        // ERROR: No profile pic
        if( mPhotoCropPath.equals("") ||  mPhotoOriginalPath.equals("") )
        {
            Toast.makeText(getApplicationContext(), getString(R.string.error_no_profile_pic), Toast.LENGTH_LONG).show();
            error = true;
        }



        // ERROR: Did not agree
        CheckBox box = (CheckBox)findViewById(R.id.i_agree_toggle);
        if( !box.isChecked() )
        {
            Toast.makeText(getApplicationContext(), getString(R.string.error_did_not_agree), Toast.LENGTH_LONG).show();
            error = true;
        }

        if( error )
            return;

        User u = User.getInstance();
        u.setSex(mSex);
        u.setInterestedIn(mInterestedIn);
        u.setWantedMinAge(mWantedMinAge);
        u.setWantedMaxAge(mWantedMaxAge);
        u.setBirthday(mBirthday);
        u.setAge(com.signals_app.signals.util.Utility.getAge(mBirthday));


        Intent intent = new Intent(getApplicationContext(),RegisterAccountActivity.class);
        intent.putExtra("profilePhotoSquarePath", mPhotoCropPath);
        intent.putExtra("profilePhotoPath", mPhotoOriginalPath);
        startActivity(intent);
    }


    @Override
    public void fbProfilePicTaskEventReceived(FbProfilePicTaskEvent evt) {
        FbProfilePicTaskMethod met = (FbProfilePicTaskMethod)evt.getSource();

        if( met != null && met.success() )
        {
            try {
                File f = new File(Environment.getExternalStorageDirectory(),"crop" + String.valueOf(System.currentTimeMillis()) + ".jpg");
                FileOutputStream out = new FileOutputStream(f);
                Bitmap bm = met.getFbProfilePic();
                bm.compress(Bitmap.CompressFormat.JPEG, 80, out);

                mPhotoCropPath = f.getPath();
                mPhotoOriginalPath = f.getPath();
            } catch (Exception e) {
                e.printStackTrace();
            }



            setProfilePicImg(met.getFbProfilePic());
        }

        setSupportProgressBarIndeterminateVisibility(false);

    }
















}

