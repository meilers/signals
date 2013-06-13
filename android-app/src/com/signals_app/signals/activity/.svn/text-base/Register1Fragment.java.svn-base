package com.signals_app.signals.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Html;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.view.*;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.widget.*;
import com.actionbarsherlock.app.SherlockFragment;
import com.signals_app.signals.R;
import com.signals_app.signals.model.City;
import com.signals_app.signals.model.Profile.Person;
import com.signals_app.signals.tasks.LoginTaskMethod;
import com.signals_app.signals.tasks.RegisterTaskMethod;
import com.signals_app.signals.tasks.RegisterTaskMethod.RegisterTaskEvent;
import com.signals_app.signals.tasks.RegisterTaskMethod.RegisterTaskListener;
import com.signals_app.signals.util.Utility;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by omegatai on 2013-05-24.
 */
public class Register1Fragment extends SherlockFragment implements RegisterTaskListener
{
    public static final String TAG = "Register1Fragment";


    private RegisterTaskMethod mRegisterTaskMethod;

    private String mFacebookId;
    private String mEmail;
    private String mUsername;
    private Integer mSex;
    private Integer mInterestedIn;
    private int mLookingFor = Person.LOOKING_NONE;
    private int mWantedMinAge;
    private int mWantedMaxAge;
    private Calendar mBirthday;
    private ArrayList<City> mCities;
    private int mCityIndex = 0;

    private boolean mLooking1Hack = false;
    private boolean mLooking2Hack = false;
    private boolean mLooking3Hack = false;

    private EditText mUsernameEt;

    private RadioButton mMaleRb;
    private RadioButton mFemaleRb;
    private CheckBox mWomenCb;
    private CheckBox mMenCb;

    private RelativeLayout mBetweenAgesRow;
    private RelativeLayout mBirthdayRow;
    private RelativeLayout mLocationRow;

    private RadioButton mLooking1Rb;
    private RadioButton mLooking2Rb;
    private RadioButton mLooking3Rb;


    private TextView mBetweenAgesTv;
    private TextView mBirthdayTv;
    private TextView mLocationTv;

    public static final Register1Fragment newInstance( String facebookId, String email, String firstName, String sex, ArrayList<String> meetingSexes, String birthday, String city, String country, ArrayList<City> cities )
    {
        Register1Fragment f = new Register1Fragment();

        Bundle args = f.getArguments();
        if (args == null) {
            args = new Bundle();
        }

        // Birthday
        String[] strArr = birthday.split("/");
        Calendar cal = Calendar.getInstance();
        cal.set(Integer.valueOf(strArr[2]), Integer.valueOf(strArr[0])-1, Integer.valueOf(strArr[1]));

        int age = Utility.getAge(cal);
        int wantedMinAge = 18;
        int wantedMaxAge = 25;

        if( age > 25 && age <= 35 )
        {
            wantedMinAge = 26;
            wantedMaxAge = 35;
        }
        else if( age > 35 )
        {
            wantedMinAge = 36;
            wantedMaxAge = 50;
        }


        // InterestedIn
        int interestedIn = Person.INTERESTED_IN_NONE;

        if( !meetingSexes.isEmpty() )
        {
            if( meetingSexes.size() == 2 )
                interestedIn = Person.INTERESTED_IN_BOTH;
            else
            {
                if(meetingSexes.get(0) == "men" )
                    interestedIn = Person.INTERESTED_IN_MEN;
                else
                    interestedIn = Person.INTERESTED_IN_WOMEN;
            }
        }

        // City
        int cityIndex = 0;

        for(int i = 0; i < cities.size(); ++i )
        {
            City c = cities.get(i);

            if( c.getName().equals(city) && c.getCountry().equals(country))
                cityIndex = i;
        }


        args.putString("facebookId", facebookId);
        args.putString("email", email);
        args.putString("username", firstName);
        args.putInt("sex", (sex.equals("female") ? Person.SEX_FEMALE : Person.SEX_MALE ) );
        args.putInt("interestedIn", interestedIn);
        args.putInt("lookingFor", Person.LOOKING_NONE);
        args.putInt("wantedMinAge", wantedMinAge);
        args.putInt("wantedMaxAge", wantedMaxAge);
        args.putSerializable("birthday", cal);
        args.putParcelableArrayList("cities", cities);
        args.putInt("cityIndex", cityIndex);

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

        Bundle args = getArguments();

        if( savedInstanceState != null )
            args = savedInstanceState;

        mFacebookId = args.getString("facebookId");
        mEmail = args.getString("email");
        mUsername = args.getString("username");
        mSex = args.getInt("sex");
        mInterestedIn = args.getInt("interestedIn");
        mLookingFor = args.getInt("lookingFor");
        mWantedMinAge = args.getInt("wantedMinAge");
        mWantedMaxAge = args.getInt("wantedMaxAge");
        mBirthday = (Calendar)args.getSerializable("birthday");
        mCities = args.getParcelableArrayList("cities");
        mCityIndex = args.getInt("cityIndex");

        mRegisterTaskMethod = new RegisterTaskMethod(getSherlockActivity());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_register1, container, false);
        getSherlockActivity().getActionBar().setSubtitle(getString(R.string.basic_information));

        mUsernameEt = (EditText)view.findViewById(R.id.username_et);
        mMaleRb = (RadioButton) view.findViewById(R.id.male_rb);
        mFemaleRb = (RadioButton) view.findViewById(R.id.female_rb);
        mMenCb = (CheckBox) view.findViewById(R.id.men_cb);
        mWomenCb = (CheckBox) view.findViewById(R.id.women_cb);
        mLooking1Rb = (RadioButton) view.findViewById(R.id.looking1_rb);
        mLooking2Rb = (RadioButton) view.findViewById(R.id.looking2_rb);
        mLooking3Rb = (RadioButton) view.findViewById(R.id.looking3_rb);

        mBetweenAgesRow = (RelativeLayout) view.findViewById(R.id.between_ages_row);
        mBirthdayRow = (RelativeLayout) view.findViewById(R.id.birthday_row);
        mLocationRow = (RelativeLayout) view.findViewById(R.id.location_row);

        mBetweenAgesTv = (TextView)view.findViewById(R.id.between_ages_text);
        mBirthdayTv = (TextView)view.findViewById(R.id.birthday_text);
        mLocationTv = (TextView)view.findViewById(R.id.location_tv);

        TextView iAgreeTv = (TextView)view.findViewById(R.id.i_agree_tv);

        Spanned text = Html.fromHtml(getString(R.string.i_agree));
        ForegroundColorSpan spans[] = text.getSpans(0, text.length(),
                ForegroundColorSpan.class);
        if (spans.length > 0) {
            iAgreeTv.setLinkTextColor(getResources().getColor(R.color.honeycombish_blue));
        }
        iAgreeTv.setText(text);



        // MODEL
        mUsernameEt.setText(mUsername);
        mUsernameEt.setSelection(mUsernameEt.getText().length());
        mBetweenAgesTv.setText(mWantedMinAge + " " + getResources().getString(R.string.and) + " " + mWantedMaxAge);
        mBirthdayTv.setText((mBirthday.get(Calendar.MONTH)+1) + "-" + mBirthday.get(Calendar.DAY_OF_MONTH) + "-" + mBirthday.get(Calendar.YEAR));

        mMaleRb.setChecked(mSex == Person.SEX_MALE);
        mFemaleRb.setChecked(mSex == Person.SEX_FEMALE);

        if( mInterestedIn == Person.INTERESTED_IN_BOTH )
        {
            mWomenCb.setChecked(true);
            mMenCb.setChecked(true);
        }
        else if( mInterestedIn == Person.INTERESTED_IN_MEN )
        {
            mWomenCb.setChecked(false);
            mMenCb.setChecked(true);
        }
        else if( mInterestedIn == Person.INTERESTED_IN_WOMEN )
        {
            mWomenCb.setChecked(true);
            mMenCb.setChecked(false);
        }
        else
        {
            mWomenCb.setChecked(false);
            mMenCb.setChecked(false);
        }

        City c = mCities.get(mCityIndex);
        mLocationTv.setText(c.getName() + ", " + c.getCountry());


        // LISTENERS

        InputFilter filterUsername = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend)
            {
                for (int i = start; i < end; i++) {
                    if (!Character.isLetterOrDigit(source.charAt(i)) && !Character.toString(source.charAt(i)).equals("_") && !Character.toString(source.charAt(i)) .equals("-")) {
                        return "";
                    }
                }

                return null;
            }
        };
        mUsernameEt.setFilters(new InputFilter[]{filterUsername, new InputFilter.LengthFilter(16)});

        mMaleRb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mMaleRb.setChecked(isChecked);
                mFemaleRb.setChecked(!isChecked);

                mSex = isChecked ? Person.SEX_MALE : Person.SEX_FEMALE;
            }
        });

        mFemaleRb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mMaleRb.setChecked(!isChecked);
                mFemaleRb.setChecked(isChecked);

                mSex = isChecked ? Person.SEX_FEMALE : Person.SEX_MALE;
            }
        });

        mWomenCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if( b )
                {
                    if( mInterestedIn == Person.INTERESTED_IN_NONE)
                        mInterestedIn = Person.INTERESTED_IN_WOMEN;
                    else
                        mInterestedIn = Person.INTERESTED_IN_BOTH;
                }
                else
                {
                    if( mInterestedIn == Person.INTERESTED_IN_WOMEN)
                        mInterestedIn = Person.INTERESTED_IN_NONE;
                    else
                        mInterestedIn = Person.INTERESTED_IN_MEN;
                }
            }
        });

        mMenCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if( b )
                {
                    if( mInterestedIn == Person.INTERESTED_IN_NONE)
                        mInterestedIn = Person.INTERESTED_IN_MEN;
                    else
                        mInterestedIn = Person.INTERESTED_IN_BOTH;
                }
                else
                {
                    if( mInterestedIn == Person.INTERESTED_IN_MEN)
                        mInterestedIn = Person.INTERESTED_IN_NONE;
                    else
                        mInterestedIn = Person.INTERESTED_IN_WOMEN;
                }
            }
        });

        mLooking1Rb.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if( mLooking1Hack )
                {
                    mLooking1Rb.setChecked(false);
                    mLooking1Hack = false;
                }
                else
                {
                    mLooking1Rb.setChecked(true);
                    mLooking1Hack = true;
                }

                mLooking2Rb.setChecked(false);
                mLooking3Rb.setChecked(false);
                mLooking2Hack = false;
                mLooking3Hack = false;

                mLookingFor = mLooking1Rb.isChecked() ? Person.LOOKING_RIGHT_MATCH : Person.LOOKING_NONE;
            }
        });




        mLooking2Rb.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                if (mLooking2Hack) {
                    mLooking2Rb.setChecked(false);
                    mLooking2Hack = false;
                } else {
                    mLooking2Rb.setChecked(true);
                    mLooking2Hack = true;
                }

                mLooking1Rb.setChecked(false);
                mLooking3Rb.setChecked(false);
                mLooking1Hack = false;
                mLooking3Hack = false;

                mLookingFor = mLooking2Rb.isChecked() ? Person.LOOKING_IM_OPEN : Person.LOOKING_NONE;
            }
        });

        mLooking3Rb.setOnClickListener(new View.OnClickListener() {
            boolean hack = false;

            @Override
            public void onClick(View view) {
                if (mLooking3Hack) {
                    mLooking3Rb.setChecked(false);
                    mLooking3Hack = false;
                } else {
                    mLooking3Rb.setChecked(true);
                    mLooking3Hack = true;
                }

                mLooking1Rb.setChecked(false);
                mLooking2Rb.setChecked(false);
                mLooking1Hack = false;
                mLooking2Hack = false;

                mLookingFor = mLooking3Rb.isChecked() ? Person.LOOKING_NO_STRINGS : Person.LOOKING_NONE;
            }
        });


        mBetweenAgesRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickBetweenAges();
            }
        });

        mBirthdayRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickBirthday();
            }
        });

        mLocationRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickLocation();
            }
        });


        Button continueButton = (Button)view.findViewById(R.id.continue_btn);
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickContinue();
            }
        });

        mRegisterTaskMethod.addRegisterTaskListener(this);


        getSherlockActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        return view;
    }


    @Override
    public void onResume()
    {
        super.onResume();
    }


    @Override
    public void onDestroyView()
    {
        super.onDestroyView();

        mRegisterTaskMethod.removeRegisterTaskListener(this);
    }



    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        if ( mFacebookId != null && mEmail != null && mUsername != null && mSex != null && mInterestedIn != null && mBirthday != null && mCities != null)
        {
            super.onSaveInstanceState(outState);

            outState.putString("facebookId", mFacebookId);
            outState.putString("email", mEmail);
            outState.putString("username", mUsernameEt.getText().toString());
            outState.putInt("sex", mSex);
            outState.putInt("interestedIn", mInterestedIn);
            outState.putInt("lookingFor", mLookingFor);
            outState.putInt("wantedMinAge", mWantedMinAge);
            outState.putInt("wantedMaxAge", mWantedMaxAge);
            outState.putSerializable("birthday", mBirthday);
            outState.putInt("cityIndex", mCityIndex);
            outState.putParcelableArrayList("cities", mCities);
        }
    }

    public void setWantedAges( int wantedMinAge, int wantedMaxAge )
    {
        mWantedMinAge = Math.min(wantedMinAge, wantedMaxAge);
        mWantedMaxAge = Math.max(wantedMinAge, wantedMaxAge);

        mBetweenAgesTv.setText(mWantedMinAge + " " + getResources().getString(R.string.and) + " " + mWantedMaxAge);
    }


    // EVENTS

    private void onClickBirthday() {
        DatePickerDialog dateDlg = new DatePickerDialog( getSherlockActivity(), new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
            {
                Calendar cal = Calendar.getInstance();
                cal.set(year, monthOfYear, dayOfMonth);
                mBirthday.setTime(cal.getTime());

                mBirthdayTv.setText((mBirthday.get(Calendar.MONTH)+1) + "-" + mBirthday.get(Calendar.DAY_OF_MONTH) + "-" + mBirthday.get(Calendar.YEAR));
            }
        }, mBirthday.get(Calendar.YEAR), mBirthday.get(Calendar.MONTH), mBirthday.get(Calendar.DAY_OF_MONTH));

        dateDlg.setButton(DatePickerDialog.BUTTON_POSITIVE, getResources().getString(R.string.done), dateDlg);
        dateDlg.show();

    }

    private void onClickBetweenAges() {
        PickAgesDialogFragment.newInstance(mWantedMinAge, mWantedMaxAge).show(getSherlockActivity().getSupportFragmentManager(), "pickAgesDialog");
    }

    private void onClickLocation()
    {
        final CharSequence[] sequences = new CharSequence[mCities.size()];

        for(int i=0; i<mCities.size(); ++i)
        {
            City c = mCities.get(i);
            sequences[i] = c.getName() + ", " + c.getCountry();
        }


        AlertDialog.Builder builder = new AlertDialog.Builder(getSherlockActivity());
        builder.setTitle("Choose City");
        builder.setItems(sequences, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item)
            {
                mCityIndex = item;
                mLocationTv.setText(sequences[item]);
            }
        }).show();
    }


    private void onClickContinue()
    {
        RegisterActivity act = (RegisterActivity)getSherlockActivity();

        if( act != null )
        {
            boolean error = false;

            mUsername = mUsernameEt.getText().toString();

            // ERROR: Username too short
            if( mUsername.length() < 3 )
            {
                Toast.makeText(getSherlockActivity().getApplicationContext(), getString(R.string.error_username_too_short), Toast.LENGTH_LONG).show();
                error = true;
            }

            // ERROR: No interests
            if( mInterestedIn == Person.INTERESTED_IN_NONE )
            {
                Toast.makeText(getSherlockActivity(), getString(R.string.error_no_interests), Toast.LENGTH_LONG).show();
                error = true;
            }

            // ERROR: Not 18
            if( com.signals_app.signals.util.Utility.getAge(mBirthday) < 18 )
            {
                Toast.makeText(getSherlockActivity(), getString(R.string.error_not_18), Toast.LENGTH_LONG).show();
                error = true;
            }

            // ERROR: Did not agree
            CheckBox box = (CheckBox)getView().findViewById(R.id.i_agree_cb);
            if( !box.isChecked() )
            {
                Toast.makeText(getSherlockActivity(), getString(R.string.error_did_not_agree), Toast.LENGTH_LONG).show();
                error = true;
            }

            if( error )
                return;


            mRegisterTaskMethod.doTask(mFacebookId, mEmail, mUsername, mSex, mInterestedIn, mWantedMinAge, mWantedMaxAge, mLookingFor, mBirthday, mCityIndex);
        }
    }

    @Override
    public void registerTaskEventReceived(RegisterTaskEvent evt) {
        RegisterTaskMethod met = (RegisterTaskMethod)evt.getSource();

        if( met != null && met.success() )
        {
            RegisterActivity act = (RegisterActivity)getSherlockActivity();

            if( act != null )
            {
                act.onClickContinue(this);
            }
        }

    }
}
