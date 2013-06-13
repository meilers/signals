package com.signals_app.signals.activity;

import java.util.Calendar;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.ActionMode;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.signals_app.signals.R;
import com.signals_app.signals.model.Profile.Person;
import com.signals_app.signals.model.Profile.User;
import com.signals_app.signals.tasks.MyProfileEditBasicInfoTaskMethod;
import com.signals_app.signals.tasks.MyProfileEditBasicInfoTaskMethod.MyProfileEditBasicInfoTaskEvent;
import com.signals_app.signals.tasks.MyProfileEditBasicInfoTaskMethod.MyProfileEditBasicInfoTaskListener;
import com.signals_app.signals.tasks.MyProfileEditLookingForTaskMethod;
import com.signals_app.signals.util.Utility;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;


public class MyProfileEditBasicInfoFragment extends SherlockFragment implements MyProfileEditBasicInfoTaskListener
{
    public static final int ACTION_EDIT = 0;
    public static final int CONTEXTUAL_ACTION_SAVE = 0;
    
    private MyProfileEditBasicInfoTaskMethod mMyProfileEditBasicInfoTaskMethod;
    
    private ActionMode mMode;
    private boolean mEditState = false;
    
    private String mUsername;
    private Integer mSex;
    private Integer mInterestedIn;
    private int mLookingFor = Person.LOOKING_NONE;
    private int mWantedMinAge;
    private int mWantedMaxAge;
    private Calendar mBirthday; 
    
    
    private EditText mUsernameEt;
    
    private LinearLayout mMaleRow;
    private LinearLayout mFemaleRow;
    private LinearLayout mInterestedInMenRow;
    private LinearLayout mInterestedInWomenRow;
    
    private TableRow mLookingRow1;
    private TableRow mLookingRow2;
    private TableRow mLookingRow3;
    
    private RelativeLayout mBetweenAgesRow;
    private RelativeLayout mBirthdayRow;
    
    private ImageView mMaleIv;
    private ImageView mFemaleIv;
    private ImageView mInterestedInMenIv;
    private ImageView mInterestedInWomenIv;
    private ImageView mBetweenAgesIv;
    private ImageView mLookingIv1;
    private ImageView mLookingIv2;
    private ImageView mLookingIv3;
    private ImageView mBirthdayIv;
    
    private TextView mUsernameTv;
    private TextView mBetweenAgesTv;
    private TextView mBirthdayTv;
    
    public static final MyProfileEditBasicInfoFragment newInstance( User u  )
    {
        MyProfileEditBasicInfoFragment f = new MyProfileEditBasicInfoFragment();
        
        // Get arguments passed in, if any
        Bundle args = f.getArguments();
        if (args == null) {
            args = new Bundle();
        }
        
        // Add parameters to the argument bundle
        args.putBoolean("editState", false);
        args.putString("username", u.getUsername());
        args.putInt("sex", u.getSex());
        args.putInt("interestedIn", u.getInterestedIn());
        args.putInt("lookingFor", u.getLookingFor());
        args.putInt("wantedMinAge", u.getWantedMinAge());
        args.putInt("wantedMaxAge", u.getWantedMaxAge());
        args.putSerializable("birthday", u.getBirthday());
        f.setArguments(args);
        
        
        return f;
    }
    
    

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
 
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
        Bundle args = getArguments();
        
        if (savedInstanceState != null) 
            args = savedInstanceState;
        
        mUsername = args.getString("username");
        mSex = args.getInt("sex");
        mInterestedIn = args.getInt("interestedIn");
        mLookingFor = args.getInt("lookingFor");
        mWantedMinAge = args.getInt("wantedMinAge");
        mWantedMaxAge = args.getInt("wantedMaxAge");
        mBirthday = (Calendar)args.getSerializable("birthday");
        
        
        mMyProfileEditBasicInfoTaskMethod = new MyProfileEditBasicInfoTaskMethod(getSherlockActivity());
        mMyProfileEditBasicInfoTaskMethod.addMyProfileEditBasicInfoTaskListener(this);
        
        
        // WIDGETS
        View view = inflater.inflate(R.layout.fragment_my_profile_edit_basic_info, container, false);  
        getSherlockActivity().setTitle(mUsername);  
        getSherlockActivity().getSupportActionBar().setSubtitle(getString(R.string.basic_info));
        
        
        mUsernameEt = (EditText) view.findViewById(R.id.username_et); 
        mMaleRow = (LinearLayout) view.findViewById(R.id.male_pref_row);
        mFemaleRow = (LinearLayout) view.findViewById(R.id.female_pref_row);
        mInterestedInMenRow = (LinearLayout) view.findViewById(R.id.interested_in_men_row);
        mInterestedInWomenRow = (LinearLayout) view.findViewById(R.id.interested_in_women_row);
        mBetweenAgesRow = (RelativeLayout) view.findViewById(R.id.between_ages_row);
        mBirthdayRow = (RelativeLayout) view.findViewById(R.id.birthday_row);
        mFemaleIv = (ImageView)view.findViewById(R.id.check_female_img);
        mMaleIv = (ImageView)view.findViewById(R.id.check_male_img);
        mInterestedInWomenIv = (ImageView)view.findViewById(R.id.check_interested_in_women_img);
        mInterestedInMenIv = (ImageView)view.findViewById(R.id.check_interested_in_men_img); 
        mBetweenAgesTv = (TextView)view.findViewById(R.id.between_ages_text);
        mBirthdayTv = (TextView)view.findViewById(R.id.birthday_text);
        mBetweenAgesIv = (ImageView)view.findViewById(R.id.between_ages_iv);
        mBirthdayIv = (ImageView)view.findViewById(R.id.birthday_iv);
         
        mUsernameTv = (TextView)view.findViewById(R.id.username_tv);

        
        mLookingRow1 = (TableRow) view.findViewById(R.id.looking1_pref_row);
        mLookingRow2 = (TableRow) view.findViewById(R.id.looking2_pref_row);
        mLookingRow3 = (TableRow) view.findViewById(R.id.looking3_pref_row);
        mLookingIv1 = (ImageView)view.findViewById(R.id.check_looking1_img);
        mLookingIv2 = (ImageView)view.findViewById(R.id.check_looking2_img);
        mLookingIv3 = (ImageView)view.findViewById(R.id.check_looking3_img);
        
        
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
        
        
        mBetweenAgesRow.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickBetweenAges();
            }   
        });

        mBirthdayRow.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickBirthday();
            }   
        });
        
        mMaleRow.setOnClickListener(new OnClickListener() {
            @Override
             public void onClick(View v) {
                mFemaleIv.setVisibility(View.GONE);
                mMaleIv.setVisibility(View.VISIBLE);
                 
                mSex = Person.SEX_MALE;
             }   
        });
        
        
        mFemaleRow.setOnClickListener(new OnClickListener() {
            @Override
             public void onClick(View v) {
                mFemaleIv.setVisibility(View.VISIBLE);
                mMaleIv.setVisibility(View.GONE);
                 
                mSex = Person.SEX_FEMALE;
             }   
        });
        
        
        mInterestedInMenRow.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                
                if( mInterestedIn == Person.INTERESTED_IN_NONE  )
                {
                    mInterestedInMenIv.setVisibility(View.VISIBLE);
                    mInterestedIn = Person.INTERESTED_IN_MEN;
                }
                else if( mInterestedIn == Person.INTERESTED_IN_WOMEN  )
                {
                    mInterestedInMenIv.setVisibility(View.VISIBLE);
                    mInterestedIn = Person.INTERESTED_IN_BOTH;
                }
                else if( mInterestedIn == Person.INTERESTED_IN_MEN  )
                {
                    mInterestedInMenIv.setVisibility(View.GONE);
                    mInterestedIn = Person.INTERESTED_IN_NONE;
                }
                else if( mInterestedIn == Person.INTERESTED_IN_BOTH  )
                {
                    mInterestedInMenIv.setVisibility(View.GONE);
                    mInterestedIn = Person.INTERESTED_IN_WOMEN;
                }
                
            }   
        });
        
        mInterestedInWomenRow.setOnClickListener(new OnClickListener() {
            @Override
             public void onClick(View v) {
             if( mInterestedIn == Person.INTERESTED_IN_NONE  )
             {
                 mInterestedInWomenIv.setVisibility(View.VISIBLE);
                 mInterestedIn = Person.INTERESTED_IN_WOMEN;
             }
             else if( mInterestedIn == Person.INTERESTED_IN_MEN  )
             {
                 mInterestedInWomenIv.setVisibility(View.VISIBLE);
                 mInterestedIn = Person.INTERESTED_IN_BOTH;
             }
             else if( mInterestedIn == Person.INTERESTED_IN_WOMEN  )
             {
                 mInterestedInWomenIv.setVisibility(View.GONE);
                 mInterestedIn = Person.INTERESTED_IN_NONE;
             }
             else if( mInterestedIn == Person.INTERESTED_IN_BOTH  )
             {
                 mInterestedInWomenIv.setVisibility(View.GONE);
                 mInterestedIn = Person.INTERESTED_IN_MEN;
             }
             }   
        });
        
        
        mLookingRow1.setOnClickListener(new OnClickListener() {
           @Override
            public void onClick(View v) {
                    
                    if( mLookingFor == Person.LOOKING_RIGHT_MATCH)
                    {
                        mLookingFor = Person.LOOKING_NONE;
                        
                        mLookingIv1.setVisibility(View.GONE);
                    }
                    else
                    {
                        mLookingFor = Person.LOOKING_RIGHT_MATCH;
                        
                        mLookingIv1.setVisibility(View.VISIBLE);
                        mLookingIv2.setVisibility(View.GONE);
                        mLookingIv3.setVisibility(View.GONE);
                    }
            }   
        });
        
        mLookingRow2.setOnClickListener(new OnClickListener() {
               @Override
                public void onClick(View v) 
               {
                    if( mLookingFor == Person.LOOKING_IM_OPEN)
                    {
                        mLookingFor = Person.LOOKING_NONE;
                        mLookingIv2.setVisibility(View.GONE);
                    }
                    else 
                    {
                        mLookingFor = Person.LOOKING_IM_OPEN; 
                        
                        mLookingIv2.setVisibility(View.VISIBLE);
                        mLookingIv1.setVisibility(View.GONE);
                        mLookingIv3.setVisibility(View.GONE);
                    }
                }   
        }); 
        
        mLookingRow3.setOnClickListener(new OnClickListener() {
           @Override
            public void onClick(View v) 
           {
                    if( mLookingFor == Person.LOOKING_NO_STRINGS)
                    {
                        mLookingFor = Person.LOOKING_NONE;
                        mLookingIv3.setVisibility(View.GONE);
                    }
                    else
                    {
                        mLookingFor = Person.LOOKING_NO_STRINGS;
                        
                        mLookingIv3.setVisibility(View.VISIBLE);
                        mLookingIv1.setVisibility(View.GONE);
                        mLookingIv2.setVisibility(View.GONE);
                    }
            }   
        });
        
        
        getSherlockActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        
        
        setHasOptionsMenu(true);
        
        updateView( args.getBoolean("editState") );
        
        return view;
    }
    
    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
    } 
    
    @Override
    public void onResume()
    {
        super.onResume();
    }
    
    // Fragment no longer visible
    @Override
    public void onPause()
    {
        super.onPause();
    }
    
    
    
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Add your menu entries here
        super.onCreateOptionsMenu(menu, inflater);
        
        menu.add(0,0,ACTION_EDIT,getString(R.string.edit))
        .setIcon( R.drawable.ic_action_edit)
        .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
    }
    
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getOrder())
        {
            case ACTION_EDIT:
                mMode = getSherlockActivity().startActionMode(new AnActionModeOfEpicProportions());
                
                break;
        }
        
        return false;
    }
    
    private final class AnActionModeOfEpicProportions implements ActionMode.Callback {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) 
        {
            menu.add(getString(R.string.save))
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);


            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            mEditState = true;
            updateView(mEditState);
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) 
        {
            switch(item.getOrder())
            {
                case CONTEXTUAL_ACTION_SAVE:
                    save();
                    break;
            }
            
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mEditState = false;
            updateView(mEditState);
        }
    }
    

    
    private void updateView( boolean editState )
    {
        String username = mUsername;
        Integer sex = mSex;
        Integer interestedIn = mInterestedIn;
        int lookingFor = mLookingFor;
        int wantedMinAge = mWantedMinAge;
        int wantedMaxAge = mWantedMaxAge;
        Calendar birthday = mBirthday; 
        
        
        if( mEditState )
        {
            mUsernameTv.setVisibility(View.GONE);
            mUsernameEt.setVisibility(View.VISIBLE);
            mBetweenAgesIv.setVisibility(View.VISIBLE);
            mBirthdayIv.setVisibility(View.VISIBLE);
            
            mMaleRow.setClickable(true);
            mFemaleRow.setClickable(true);
            mInterestedInMenRow.setClickable(true);
            mInterestedInWomenRow.setClickable(true);
            mBetweenAgesRow.setClickable(true);
            mBirthdayRow.setClickable(true);
            
            mLookingRow1.setClickable(true);
            mLookingRow2.setClickable(true);
            mLookingRow3.setClickable(true);
            
            mLookingIv1.setVisibility( lookingFor == Person.LOOKING_RIGHT_MATCH ? View.VISIBLE : View.GONE );
            mLookingIv2.setVisibility( lookingFor == Person.LOOKING_IM_OPEN ? View.VISIBLE : View.GONE );
            mLookingIv3.setVisibility( lookingFor == Person.LOOKING_NO_STRINGS ? View.VISIBLE : View.GONE );
        }
        else
        {
            User u = User.getInstance();
            username = u.getUsername();
            sex = u.getSex();
            interestedIn = u.getInterestedIn();
            lookingFor = u.getLookingFor();
            wantedMinAge = u.getWantedMinAge();
            wantedMaxAge = u.getWantedMaxAge();
            birthday = u.getBirthday();
            
            
            mUsernameTv.setVisibility(View.VISIBLE);
            mUsernameEt.setVisibility(View.GONE);
            mBetweenAgesIv.setVisibility(View.GONE);
            mBirthdayIv.setVisibility(View.GONE);
            
            mMaleRow.setClickable(false);
            mFemaleRow.setClickable(false);
            mInterestedInMenRow.setClickable(false);
            mInterestedInWomenRow.setClickable(false);
            mBetweenAgesRow.setClickable(false);
            mBirthdayRow.setClickable(false);
            
            mLookingRow1.setClickable(false);
            mLookingRow2.setClickable(false);
            mLookingRow3.setClickable(false);
            
            
            mLookingIv1.setVisibility(u.getLookingFor() == Person.LOOKING_RIGHT_MATCH ? View.VISIBLE : View.GONE );
            mLookingIv2.setVisibility(u.getLookingFor() == Person.LOOKING_IM_OPEN ? View.VISIBLE : View.GONE );
            mLookingIv3.setVisibility(u.getLookingFor() == Person.LOOKING_NO_STRINGS ? View.VISIBLE : View.GONE );
        }
        
        
        // USERNAME
        mUsernameEt.setText(username);
        mUsernameTv.setText(username);
        
        
        // SEX
            
        if( sex == Person.SEX_FEMALE )
        {
            mFemaleIv.setVisibility(View.VISIBLE);
            mMaleIv.setVisibility(View.GONE);
        }
        else
        {
            mFemaleIv.setVisibility(View.GONE);
            mMaleIv.setVisibility(View.VISIBLE);
        }
            
        
        
        // ORIENTATION
        
        if( interestedIn == Person.INTERESTED_IN_WOMEN)
        {
            mInterestedInWomenIv.setVisibility(View.VISIBLE);
            mInterestedInMenIv.setVisibility(View.GONE);
        }
        else if( interestedIn == Person.INTERESTED_IN_MEN)
        {
            mInterestedInMenIv.setVisibility(View.VISIBLE);
            mInterestedInWomenIv.setVisibility(View.GONE);
        }
        else if( interestedIn == Person.INTERESTED_IN_BOTH)
        {
            mInterestedInMenIv.setVisibility(View.VISIBLE);
            mInterestedInWomenIv.setVisibility(View.VISIBLE);
        }
        else
        {
            mInterestedInWomenIv.setVisibility(View.GONE);
            mInterestedInMenIv.setVisibility(View.GONE);
        }
        
        
        // BETWEEN AGES
        mBetweenAgesTv.setText(wantedMinAge + " " + getResources().getString(R.string.and) + " " + wantedMaxAge);
        
        
        // LOOKING FOR
        if( lookingFor == Person.LOOKING_RIGHT_MATCH )
        {
            mLookingIv1.setVisibility(View.VISIBLE);
            mLookingIv2.setVisibility(View.GONE);
            mLookingIv3.setVisibility(View.GONE);
        }
        else if( lookingFor == Person.LOOKING_IM_OPEN )
        {
            mLookingIv2.setVisibility(View.VISIBLE);
            mLookingIv3.setVisibility(View.GONE);
            mLookingIv1.setVisibility(View.GONE);
        }
        else if( lookingFor == Person.LOOKING_NO_STRINGS )
        {
            mLookingIv3.setVisibility(View.VISIBLE);
            mLookingIv2.setVisibility(View.GONE);
            mLookingIv1.setVisibility(View.GONE);
        }
        else
        {
            mLookingIv3.setVisibility(View.GONE);
            mLookingIv2.setVisibility(View.GONE);
            mLookingIv1.setVisibility(View.GONE);
        }
        
        
        // BIRTHDAY
        Calendar cal = Calendar.getInstance();
        cal.set(birthday.get(Calendar.YEAR), birthday.get(Calendar.MONTH), birthday.get(Calendar.DAY_OF_MONTH));
        setBirthday(cal);
        
        
        if( getSherlockActivity() != null )
        {
            InputMethodManager inputMethodManager = (InputMethodManager)  getSherlockActivity().getSystemService(MainActivity.INPUT_METHOD_SERVICE);
            
            if(getSherlockActivity().getCurrentFocus() != null)
                inputMethodManager.hideSoftInputFromWindow(getSherlockActivity().getCurrentFocus().getWindowToken(), 0);
        }
        
        

    }
    
    private void save()
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
            Toast.makeText(getSherlockActivity().getApplicationContext(), getString(R.string.error_no_interests), Toast.LENGTH_LONG).show();
            error = true;
        }
        
        // ERROR:
        if( Utility.getAge(mBirthday) < 18 )
        {
            Toast.makeText(getSherlockActivity().getApplicationContext(), getString(R.string.error_not_18), Toast.LENGTH_LONG).show();
            error = true;
        }
         
        

        if( error )
            return;
        
        mMyProfileEditBasicInfoTaskMethod.doTask(User.getInstance().getUserId(), mUsername, mSex, mInterestedIn, mWantedMinAge, mWantedMaxAge, mLookingFor, mBirthday);
    }


    
    
    @Override
    public void onSaveInstanceState(Bundle outState) 
    {
        if ( mUsername != null && mSex != null && mInterestedIn != null && mBirthday != null) 
        {
            super.onSaveInstanceState(outState);
            outState.putBoolean("editState", mEditState);
            outState.putString("username", mUsername);
            outState.putInt("sex", mSex);
            outState.putInt("interestedIn", mInterestedIn);
            outState.putInt("lookingFor", mLookingFor);
            outState.putInt("wantedMinAge", mWantedMinAge);
            outState.putInt("wantedMaxAge", mWantedMaxAge);
            outState.putSerializable("birthday", mBirthday);
        }
    }
    
    
    public void setBirthday( Calendar cal )
    {
        mBirthday.setTime(cal.getTime());
        
        String str = (mBirthday.get(Calendar.MONTH)+1) + "-" + mBirthday.get(Calendar.DAY_OF_MONTH) + "-" + mBirthday.get(Calendar.YEAR);
        mBirthdayTv.setText(str);
    }
    
    
    public void setWantedAges( int wantedMinAge, int wantedMaxAge )
    {
        mWantedMinAge = Math.min(wantedMinAge, wantedMaxAge);
        mWantedMaxAge = Math.max(wantedMinAge, wantedMaxAge);
        
        mBetweenAgesTv.setText(mWantedMinAge + " " + getResources().getString(R.string.and) + " " + mWantedMaxAge);
    }
    
    
    
    // EVENTS
    
    private void onClickBirthday() {
        DatePickerDialog dateDlg = new DatePickerDialog( new ContextThemeWrapper(getSherlockActivity(), R.style.SignalsThemeDialog), new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) 
            {
                Calendar cal = Calendar.getInstance();
                cal.set(year, monthOfYear, dayOfMonth);
                setBirthday(cal);
                
                updateView( mEditState );
            }
        }, mBirthday.get(Calendar.YEAR), mBirthday.get(Calendar.MONTH), mBirthday.get(Calendar.DAY_OF_MONTH));
        
        dateDlg.setButton(DatePickerDialog.BUTTON_POSITIVE, getResources().getString(R.string.done), dateDlg);
        dateDlg.show();

    }
    
    private void onClickBetweenAges() {
        PickAgesDialogFragment.newInstance(mWantedMinAge, mWantedMaxAge).show(getSherlockActivity().getSupportFragmentManager(), "pickAgesDialog");
    }



    @Override
    public void myProfileEditBasicInfoTaskEventReceived(MyProfileEditBasicInfoTaskEvent evt) 
    {
        MyProfileEditBasicInfoTaskMethod met = (MyProfileEditBasicInfoTaskMethod)evt.getSource();
        
        if( met != null && met.success() )
        {
            User u = User.getInstance();
            
            u.setUsername(mUsername);
            u.setSex(mSex);
            u.setInterestedIn(mInterestedIn);
            u.setWantedMinAge(mWantedMinAge);
            u.setWantedMaxAge(mWantedMaxAge);
            u.setLookingFor(mLookingFor);
            u.setBirthday(mBirthday);
            
            getSherlockActivity().setTitle(mUsername);  
            getSherlockActivity().getSupportActionBar().setSubtitle(getString(R.string.basic_info)); 
            
            Toast.makeText(getSherlockActivity(), getString(R.string.changes_saved), Toast.LENGTH_LONG).show();
            
            if( mMode != null )
                mMode.finish();
        }
    }
    
    
}