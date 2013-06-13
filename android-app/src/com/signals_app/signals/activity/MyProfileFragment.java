package com.signals_app.signals.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONObject;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.ActionMode;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.MemoryCacheUtil;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.signals_app.signals.R;
import com.signals_app.signals.model.Constants;
import com.signals_app.signals.model.Place;
import com.signals_app.signals.model.Profile.BarStars;
import com.signals_app.signals.model.Profile.Person;
import com.signals_app.signals.model.Profile.User;
import com.signals_app.signals.tasks.MyProfileEditDontApproachTaskMethod;
import com.signals_app.signals.tasks.MyProfileEditDontApproachTaskMethod.MyProfileEditDontApproachTaskEvent;
import com.signals_app.signals.tasks.MyProfileEditDontApproachTaskMethod.MyProfileEditDontApproachTaskListener;
import com.signals_app.signals.tasks.MyProfileEditInfoTaskMethod;
import com.signals_app.signals.tasks.MyProfileEditInfoTaskMethod.MyProfileEditInfoTaskEvent;
import com.signals_app.signals.tasks.MyProfileEditInfoTaskMethod.MyProfileEditInfoTaskListener;
import com.signals_app.signals.tasks.MyProfileEditTonightTaskMethod;
import com.signals_app.signals.tasks.MyProfileEditTonightTaskMethod.MyProfileEditTonightTaskEvent;
import com.signals_app.signals.tasks.MyProfileEditTonightTaskMethod.MyProfileEditTonightTaskListener;
import com.signals_app.signals.tasks.ProfilesTaskMethod;
import com.signals_app.signals.tasks.ProfilesTaskMethod.ProfilesTaskEvent;
import com.signals_app.signals.tasks.ProfilesTaskMethod.ProfilesTaskListener;
import com.signals_app.signals.tasks.signals.BlockTaskMethod;
import com.signals_app.signals.tasks.signals.UnblockTaskMethod;
import com.signals_app.signals.tasks.signals.BlockTaskMethod.BlockTaskEvent;
import com.signals_app.signals.tasks.signals.BlockTaskMethod.BlockTaskListener;
import com.signals_app.signals.tasks.signals.FlirtTaskMethod;
import com.signals_app.signals.tasks.signals.FlirtTaskMethod.FlirtTaskEvent;
import com.signals_app.signals.tasks.signals.FlirtTaskMethod.FlirtTaskListener;
import com.signals_app.signals.tasks.signals.ReportTaskMethod;
import com.signals_app.signals.tasks.signals.ReportTaskMethod.ReportTaskEvent;
import com.signals_app.signals.tasks.signals.ReportTaskMethod.ReportTaskListener;
import com.signals_app.signals.tasks.signals.UnblockTaskMethod.UnblockTaskEvent;
import com.signals_app.signals.tasks.signals.UnblockTaskMethod.UnblockTaskListener;
import com.signals_app.signals.tasks.signals.VoteTaskMethod;
import com.signals_app.signals.tasks.signals.VoteTaskMethod.VoteTaskEvent;
import com.signals_app.signals.tasks.signals.VoteTaskMethod.VoteTaskListener;
import com.signals_app.signals.util.Utility;

import android.view.animation.Transformation;
import android.app.AlertDialog;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import android.support.v4.app.FragmentManager;
import android.text.InputFilter;
import android.text.InputType;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;

public class MyProfileFragment extends SherlockFragment implements ProfilesTaskListener, MyProfileEditInfoTaskListener
{
    public static final int ACTION_EDIT = 0;
    public static final int ACTION_SETTINGS = 1;
    public static final int CONTEXTUAL_ACTION_SAVE = 0;
    
	private ProfilesTaskMethod mProfileTaskMethod;	
	private MyProfileEditInfoTaskMethod mMyProfileEditInfoTaskMethod;  
	
	private ActionMode mMode;
    private boolean mEditState = false;
	private User mPerson;
	
	
	// PROFILE
	private ImageView mProfilePicIv;
	private ImageView mBarsStarIv;
    private TextView mPhotosTopTv;
    private RelativeLayout mPhotosTopRl;
    private RelativeLayout mBasicInfoLayout;
    
    private TextView mTonightTv;
    private TextView mAboutMeTv;
    private TextView mOccupationTv;
    private TextView mEducationTv;
    private TextView mInterestsTv;
    private TextView mActivitiesTv;
    private TextView mTravelTv;
    private TextView mMusicTv;
    private TextView mDrinksTv;
    private TextView mMyPerfectNightOutTv;

    private LinearLayout mStatsLayout;
    private LinearLayout mApproachLayout;
	
    private Button mBecomeVipBtn;
	
    
	// EDIT
    
	private EditText mTonightEt;
    private EditText mAboutMeEt;
    private EditText mEducationEt;
    private EditText mOccupationEt;
    private EditText mInterestsEt;
    private EditText mActivitiesEt;
    private EditText mTravelEt;
    private EditText mMusicEt;
    private EditText mDrinksEt;
    private EditText mMyPerfectNightOutEt;
    
    private LinearLayout mEditApproachLayout;
    private RelativeLayout mTipComeAndSayHiLayout;
    private RelativeLayout mTipBuyMeADrinkLayout;
    private RelativeLayout mTipInviteMeToDanceLayout;
    private RelativeLayout mTipMakeMeLaughLayout;
    private RelativeLayout mTipMeetMyFriendsLayout;
    private RelativeLayout mTipSurpriseMeLayout;
    private RelativeLayout mDontBeDrunkLayout;
    private RelativeLayout mDontExpectAnythingLayout;
    private RelativeLayout mDontBePersistentLayout;
    private EditText mPersonalAdviceEt;
    
    private CheckBox mDontApproachCb;
    private CheckBox mTipComeAndSayHiCb;
    private CheckBox mTipBuyMeADrinkCb;
    private CheckBox mTipInviteMeToDanceCb;
    private CheckBox mTipMakeMeLaughCb;
    private CheckBox mTipMeetMyFriendsCb;
    private CheckBox mTipSurpriseMeCb;
    private CheckBox mDontBeDrunkCb;
    private CheckBox mDontExpectAnythingCb;
    private CheckBox mDontBePersistentCb;
    
    
	
  	public static final MyProfileFragment newInstance()
  	{
  	    
  	    MyProfileFragment f = new MyProfileFragment();
  	  
        // Get arguments passed in, if any
        Bundle args = f.getArguments();
        if (args == null) {
            args = new Bundle();
        }
        
        args.putBoolean("editState", false);
        
        User u = User.getInstance();
        args.putString("tonight", u.getTonight());
        args.putString("aboutMe", u.getAboutMe());
        args.putString("occupation",  u.getOccupation());
        args.putString("education",  u.getEducation());
        args.putString("interests",  u.getInterests());
        args.putString("activities",  u.getActivities());
        args.putString("travel",  u.getTravel());
        args.putString("favoriteMusic",  u.getMusic());
        args.putString("favoriteDrinks", u.getDrinks());
        args.putString("myPerfectNightOut", u.getMyPerfectNightOut());
        
        
        args.putBoolean("dontApproach", u.dontApproach());
        args.putBoolean("tipComeAndSayHi", u.tipComeAndSayHi());
        args.putBoolean("tipBuyMeADrink", u.tipBuyMeADrink());
        args.putBoolean("tipInviteMeToDance", u.tipInviteMeToDance());
        args.putBoolean("tipMakeMeLaugh", u.tipMakeMeLaugh());
        args.putBoolean("tipMeetMyFriends", u.tipMeetMyFriends());
        args.putBoolean("tipSurpriseMe", u.tipSurpriseMe());
        args.putBoolean("dontBeDrunk", u.dontBeDrunk());
        args.putBoolean("dontExpectAnything", u.dontExpectAnything());
        args.putBoolean("dontBePersistent", u.dontBePersistent());
        args.putString("personalAdvice", u.getPersonalAdvice());
        
        // Add parameters to the argument bundle
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
        
        if (savedInstanceState != null) 
            args = savedInstanceState; 
       
        		
		mEditState = args.getBoolean("editState");
        
        
        mPerson = User.getInstance();
        mProfileTaskMethod = new ProfilesTaskMethod(getSherlockActivity());
        mMyProfileEditInfoTaskMethod = new MyProfileEditInfoTaskMethod(getSherlockActivity());
	}
 
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{

        mProfileTaskMethod.addProfilesTaskListener(this);
        mMyProfileEditInfoTaskMethod.addMyProfileEditInfoTaskListener(this);
        
        
        // WIDGETS
        
		View view = inflater.inflate(R.layout.fragment_my_profile, container, false);
		getSherlockActivity().setTitle(mPerson.getUsername());
		getSherlockActivity().getSupportActionBar().setSubtitle(mPerson.checkedIn() ? mPerson.getCheckPlace().getName() : null);
		
		mProfilePicIv = (ImageView)view.findViewById(R.id.profile_pic_img); 
		
		mBarsStarIv = (ImageView)view.findViewById(R.id.bar_star_img);
		mPhotosTopRl = (RelativeLayout)view.findViewById(R.id.photos_top_rl);
		mPhotosTopTv = (TextView)view.findViewById(R.id.photos_top_tv);
		mBasicInfoLayout = (RelativeLayout)view.findViewById(R.id.basic_info_layout);
        
		
		// PROFILE

		mTonightTv = (TextView)view.findViewById(R.id.tonight_tv);
        mAboutMeTv = (TextView)view.findViewById(R.id.about_me_tv);
        mOccupationTv = (TextView)view.findViewById(R.id.occupation_tv);
        mEducationTv = (TextView)view.findViewById(R.id.education_tv);
        mInterestsTv = (TextView)view.findViewById(R.id.interests_tv);
        mActivitiesTv = (TextView)view.findViewById(R.id.activities_tv);
        mTravelTv = (TextView)view.findViewById(R.id.travel_tv);
        mMusicTv = (TextView)view.findViewById(R.id.music_tv);
        mDrinksTv = (TextView)view.findViewById(R.id.drinks_tv);
        mMyPerfectNightOutTv = (TextView)view.findViewById(R.id.my_perfect_night_out_tv);
        
		mApproachLayout = (LinearLayout)view.findViewById(R.id.approach_layout);
		mStatsLayout = (LinearLayout)view.findViewById(R.id.stats_layout);
		mBecomeVipBtn = (Button)view.findViewById(R.id.become_vip_btn);
		
		
		
		// EDIT
        
		
		
        InputFilter lengthFilter = new InputFilter() { 
            @Override
            public CharSequence filter(CharSequence source, int start, int end,
                    Spanned dest, int dstart, int dend) {
                for (int i = start; i < end; i++) { 
                    if (source.charAt(i) == '\n') { 
                            return ""; 
                    } 
            } 
                return null;
            }
        };
        
        InputFilter[] filter140 = new InputFilter[]{lengthFilter, new InputFilter.LengthFilter(140)};
        InputFilter[] filter30 = new InputFilter[]{lengthFilter, new InputFilter.LengthFilter(30)};
        
        mTonightEt = (EditText)view.findViewById(R.id.tonight_edit_tv);
        mAboutMeEt = (EditText)view.findViewById(R.id.about_me_et);
        mOccupationEt = (EditText)view.findViewById(R.id.occupation_et);
        mEducationEt = (EditText)view.findViewById(R.id.education_et);
        mInterestsEt = (EditText)view.findViewById(R.id.interests_et);
        mActivitiesEt = (EditText)view.findViewById(R.id.activities_et);
        mTravelEt = (EditText)view.findViewById(R.id.travel_et);
        mMusicEt = (EditText)view.findViewById(R.id.music_et);
        mDrinksEt = (EditText)view.findViewById(R.id.drinks_et);
        mMyPerfectNightOutEt = (EditText)view.findViewById(R.id.my_perfect_night_out_et);
        mPersonalAdviceEt = (EditText)view.findViewById(R.id.personal_advice_et);
        mEditApproachLayout = (LinearLayout)view.findViewById(R.id.edit_approach_layout);
        mTipComeAndSayHiLayout = (RelativeLayout)view.findViewById(R.id.tip_come_say_hi_row);
        mTipBuyMeADrinkLayout = (RelativeLayout)view.findViewById(R.id.tip_buy_me_a_drink_row);
        mTipInviteMeToDanceLayout = (RelativeLayout)view.findViewById(R.id.tip_invite_me_to_dance_row);
        mTipMakeMeLaughLayout = (RelativeLayout)view.findViewById(R.id.tip_make_me_laugh_row);
        mTipMeetMyFriendsLayout = (RelativeLayout)view.findViewById(R.id.tip_meet_my_friends_row);
        mTipSurpriseMeLayout = (RelativeLayout)view.findViewById(R.id.tip_surprise_me_row);
        mDontBeDrunkLayout = (RelativeLayout)view.findViewById(R.id.dont_be_drunk_row);
        mDontExpectAnythingLayout = (RelativeLayout)view.findViewById(R.id.dont_expect_anything_row);
        mDontBePersistentLayout = (RelativeLayout)view.findViewById(R.id.dont_be_persistent_row);
        
        mTonightEt.setHorizontallyScrolling(false);
        mAboutMeEt.setHorizontallyScrolling(false);
        mOccupationEt.setHorizontallyScrolling(false);
        mEducationEt.setHorizontallyScrolling(false);
        mInterestsEt.setHorizontallyScrolling(false);
        mActivitiesEt.setHorizontallyScrolling(false);
        mTravelEt.setHorizontallyScrolling(false);
        mMusicEt.setHorizontallyScrolling(false);
        mDrinksEt.setHorizontallyScrolling(false);
        mMyPerfectNightOutEt.setHorizontallyScrolling(false);
        mPersonalAdviceEt.setHorizontallyScrolling(false);
        
        mTonightEt.setMaxLines(Integer.MAX_VALUE);
        mAboutMeEt.setMaxLines(Integer.MAX_VALUE);
        mOccupationEt.setMaxLines(Integer.MAX_VALUE);
        mEducationEt.setMaxLines(Integer.MAX_VALUE);
        mInterestsEt.setMaxLines(Integer.MAX_VALUE);
        mActivitiesEt.setMaxLines(Integer.MAX_VALUE);
        mTravelEt.setMaxLines(Integer.MAX_VALUE);
        mMusicEt.setMaxLines(Integer.MAX_VALUE);
        mDrinksEt.setMaxLines(Integer.MAX_VALUE);
        mMyPerfectNightOutEt.setMaxLines(Integer.MAX_VALUE);
        mPersonalAdviceEt.setMaxLines(Integer.MAX_VALUE);
        
        mTonightEt.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        mAboutMeEt.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        mOccupationEt.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        mEducationEt.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        mInterestsEt.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        mActivitiesEt.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        mTravelEt.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        mMusicEt.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        mDrinksEt.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        mMyPerfectNightOutEt.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        mPersonalAdviceEt.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        
        mTonightEt.setFilters(filter140); 
        mAboutMeEt.setFilters(filter140); 
        mOccupationEt.setFilters(filter140); 
        mEducationEt.setFilters(filter140); 
        mInterestsEt.setFilters(filter140); 
        mActivitiesEt.setFilters(filter140); 
        mTravelEt.setFilters(filter140); 
        mMusicEt.setFilters(filter140); 
        mDrinksEt.setFilters(filter140); 
        mMyPerfectNightOutEt.setFilters(filter140); 
        mPersonalAdviceEt.setFilters(filter30); 

        mDontApproachCb = (CheckBox)view.findViewById(R.id.dont_approach_cb);
        mTipComeAndSayHiCb = (CheckBox)view.findViewById(R.id.tip_come_say_hi_cb);
        mTipBuyMeADrinkCb = (CheckBox)view.findViewById(R.id.tip_buy_me_a_drink_cb);
        mTipInviteMeToDanceCb = (CheckBox)view.findViewById(R.id.tip_invite_me_to_dance_cb);
        mTipMakeMeLaughCb = (CheckBox)view.findViewById(R.id.tip_make_me_laugh_cb);
        mTipMeetMyFriendsCb = (CheckBox)view.findViewById(R.id.tip_meet_my_friends_cb);
        mTipSurpriseMeCb = (CheckBox)view.findViewById(R.id.tip_surprise_me_cb);
        mDontBeDrunkCb = (CheckBox)view.findViewById(R.id.dont_be_drunk_cb);
        mDontExpectAnythingCb = (CheckBox)view.findViewById(R.id.dont_expect_anything_cb);
        mDontBePersistentCb = (CheckBox)view.findViewById(R.id.dont_be_persistent_cb);
        
        
        
        // LISTENERS
        mProfilePicIv.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                onClickPhoto();
                return false;
            }}
        );
        
        mPhotosTopRl.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickPhotos();
            }
        });
        
        
        mBasicInfoLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickBasicInfo();
            }
        });
        
        mTonightTv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mMode = getSherlockActivity().startActionMode(new AnActionModeOfEpicProportions());
            }
        });
        
        
		mDontApproachCb.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                onClickDontApproach();
                
            }});
		        
		        
		
		
		mBecomeVipBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickBecomeVip();
            }
		});
		
		fetchProfile( mPerson.getUserId() );
		
		setHasOptionsMenu(true);
		
		
		return view;
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
    public void onDestroyView()
    {
        super.onDestroyView();
        
        mProfileTaskMethod.removeProfilesTaskListener(this);
        mMyProfileEditInfoTaskMethod.removeMyProfileEditInfoTaskListener(this);
        
        mProfileTaskMethod.cleanUp();
        mMyProfileEditInfoTaskMethod.cleanUp();
    } 

    @Override
    public void onSaveInstanceState(Bundle outState)  
    {
        if ( mTonightEt != null && mAboutMeEt != null && mActivitiesEt != null && mOccupationEt != null && mEducationEt != null && mMusicEt != null && mDrinksEt != null && mInterestsEt != null && 
                mDontApproachCb != null && mTipComeAndSayHiCb != null && mTipBuyMeADrinkCb != null && mTipInviteMeToDanceCb != null && mTipMakeMeLaughCb != null && mTipMeetMyFriendsCb != null && mTipSurpriseMeCb != null && 
                mDontBeDrunkCb != null && mDontExpectAnythingCb != null && mDontBePersistentCb != null && mPersonalAdviceEt != null ) 
        {
            super.onSaveInstanceState(outState);
            
            outState.putBoolean("editState", mEditState);
            outState.putString("tonight", mTonightEt.getText().toString());
            outState.putString("aboutMe", mAboutMeEt.getText().toString());
            outState.putString("occupation",  mOccupationEt.getText().toString());
            outState.putString("education",  mEducationEt.getText().toString());
            outState.putString("activities",  mActivitiesEt.getText().toString());
            outState.putString("interests",  mInterestsEt.getText().toString());
            outState.putString("travel",  mTravelEt.getText().toString());
            outState.putString("favoriteMusic",  mMusicEt.getText().toString());
            outState.putString("favoriteDrinks",  mDrinksEt.getText().toString());
            outState.putString("myPerfectNightOut",  mMyPerfectNightOutEt.getText().toString());
            
            outState.putBoolean("dontApproach", mDontApproachCb.isChecked());
            outState.putBoolean("tipComeAndSayHi", mTipComeAndSayHiCb.isChecked());
            outState.putBoolean("tipBuyMeADrink", mTipBuyMeADrinkCb.isChecked());
            outState.putBoolean("tipInviteMeToDance",mTipInviteMeToDanceCb.isChecked());
            outState.putBoolean("tipMakeMeLaugh", mTipMakeMeLaughCb.isChecked());
            outState.putBoolean("tipMeetMyFriends",mTipMeetMyFriendsCb.isChecked());
            outState.putBoolean("tipSurpriseMe", mTipSurpriseMeCb.isChecked());
            outState.putBoolean("dontBeDrunk", mDontBeDrunkCb.isChecked());
            outState.putBoolean("dontExpectAnything", mDontExpectAnythingCb.isChecked());
            outState.putBoolean("dontBePersistent", mDontBePersistentCb.isChecked());
            outState.putString("personalAdvice", mPersonalAdviceEt.getText().toString());
        }
    }
    
    
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Add your menu entries here
        super.onCreateOptionsMenu(menu, inflater);
        
        menu.add(0,0,ACTION_SETTINGS,getString(R.string.settings))
        .setIcon( R.drawable.gear)
        .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        
        
        menu.add(0,0,ACTION_EDIT,getString(R.string.edit))
        .setIcon( R.drawable.ic_action_edit)
        .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        
        
    }
    
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getOrder())
        {
            case ACTION_SETTINGS:
                
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                
                // set title
                alertDialogBuilder.setTitle(getActivity().getString(R.string.logout));
     
                // set dialog message
                alertDialogBuilder
                    .setMessage(getActivity().getString(R.string.logout_msg))
                    .setCancelable(false)
                    .setPositiveButton(getActivity().getString(R.string.yes),new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {

                            User.init();
                            SignalsApplication.getApplication(getActivity()).init();
                            
                            getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE); 
                            
                            Intent intent = new Intent(getActivity(), LaunchActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK); 
                            startActivity(intent);
                        }
                      })
                    .setNegativeButton(getActivity().getString(R.string.cancel),new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            dialog.cancel();
                        }
                    });
     
                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();
     
                    // show it
                    alertDialog.show();
                
                return true;
                
                
            case ACTION_EDIT:
                
                mMode = getSherlockActivity().startActionMode(new AnActionModeOfEpicProportions());
                
                return true;
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
            updateView(true);
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
            
            InputMethodManager inputManager = (InputMethodManager)getSherlockActivity().getSystemService(Context.INPUT_METHOD_SERVICE); 
            
            
            if( getSherlockActivity().getCurrentFocus() != null )
                inputManager.hideSoftInputFromWindow(getSherlockActivity().getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
            
            updateView(false);
        }
    }
    
    private void fetchProfile( Long personId )
    {
        ArrayList<Long> peopleIds = new ArrayList<Long>();
        peopleIds.add(personId);
        mProfileTaskMethod.doTask(User.getInstance().getUserId(), peopleIds );
    }


	private void updateView( boolean editState )
	{		
	    getSherlockActivity().setTitle(mPerson.getUsername());
	    getSherlockActivity().getSupportActionBar().setSubtitle(mPerson.checkedIn() ? mPerson.getCheckPlace().getName() : null);
	    
	    
    	// PROFILE PIC
		DisplayImageOptions imageOptions = new DisplayImageOptions.Builder()
		.cacheInMemory()
		.cacheOnDisc()
		.showStubImage(mPerson.getSex().equals(Person.SEX_FEMALE) ? R.drawable.lady_grey : R.drawable.gent_grey)
		.build();

		ImageLoader imageLoader = ImageLoader.getInstance();
		String imageUri = "http://s3.amazonaws.com/signals/user_images/" + mPerson.getUserId() + "/profilePhotoSquare.jpg";
		
		// Clear caches because profile pics might have been changed
		MemoryCacheUtil.removeFromCache(imageUri, imageLoader.getMemoryCache());
		File imageFile = imageLoader.getDiscCache().get(imageUri);
        if (imageFile.exists()) {
            imageFile.delete();
        }
		//ImageLoader.getInstance().clearDiscCache();
		//ImageLoader.getInstance().clearMemoryCache();
		
		Animation fadeInAnimation = AnimationUtils.loadAnimation(getSherlockActivity(), R.anim.fadein);
		ImageLoader.getInstance().displayImage(imageUri, mProfilePicIv, imageOptions, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                if( getSherlockActivity() != null )
                    getSherlockActivity().setSupportProgressBarIndeterminateVisibility(true);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                if( getSherlockActivity() != null )
                    getSherlockActivity().setSupportProgressBarIndeterminateVisibility(false);
            }
            
            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                if( getSherlockActivity() != null )
                    getSherlockActivity().setSupportProgressBarIndeterminateVisibility(false);
            }

            @Override
            public void onLoadingComplete(String imageUri,View view, Bitmap loadedImage) {
                if( getSherlockActivity() != null )
                    getSherlockActivity().setSupportProgressBarIndeterminateVisibility(false);
            }
        });
		mProfilePicIv.startAnimation(fadeInAnimation); 
		
		
		// BAR STAR
        if( mPerson.star() )
            mBarsStarIv.setVisibility(View.VISIBLE);
        else
            mBarsStarIv.setVisibility(View.GONE);
        
        
		// PHOTOS TOP
		int nbPhotos = mPerson.getProfilePhotosFilenames().size() + mPerson.getOtherPhotosFilenames().size();
		mPhotosTopTv.setText(nbPhotos + "");
		
		
		
		// BASIC INFO 
		final String[] orientations = getResources().getStringArray(R.array.orientation_list);
		TextView basicInfoText = (TextView)getView().findViewById(R.id.basic_info_tv);
		basicInfoText.setText(mPerson.getAge() + " / " 
							+ (mPerson.getSex() == 1 ? getResources().getString(R.string.male_short): getResources().getString(R.string.female_short)) 
							+ " / " + orientations[mPerson.getOrientation()-1]  );


        
        
        
		// LOOKING FOR
		
    	int lookingFor = mPerson.getLookingFor();
		
		ImageView lookingIv = (ImageView)getView().findViewById(R.id.looking_for_img);
		TextView lookingText = (TextView)getView().findViewById(R.id.looking_for_tv);
    	
        switch( lookingFor )
        {
            case Person.LOOKING_NONE:
                lookingIv.setImageDrawable(getResources().getDrawable(R.drawable.citrus_grey));
                lookingText.setText("...");
                break;
                
            case Person.LOOKING_RIGHT_MATCH:
                lookingIv.setImageDrawable(getResources().getDrawable(R.drawable.citrus_r));
                lookingText.setText(getResources().getString(R.string.right_match));
                break;
                
            case Person.LOOKING_IM_OPEN:
                lookingIv.setImageDrawable(getResources().getDrawable(R.drawable.citrus_o));
                lookingText.setText(getResources().getString(R.string.im_open));
                break;
                
            case Person.LOOKING_NO_STRINGS:
                lookingIv.setImageDrawable(getResources().getDrawable(R.drawable.citrus_g));
                lookingText.setText(getResources().getString(R.string.no_strings));
                break;
        }
        
		
        if( !editState )
            updateViewProfile();
        else
            updateViewEdit();
        
        
        // SHOW
        ScrollView rootLayout = (ScrollView)getView().findViewById(R.id.root_layout);
        rootLayout.setVisibility(View.VISIBLE);
        
        
	}
	
	private void updateViewEdit()
    { 
	    
	    mTonightTv.setVisibility(View.GONE);
	    mAboutMeTv.setVisibility(View.GONE);
	    mOccupationTv.setVisibility(View.GONE);
	    mEducationTv.setVisibility(View.GONE);
	    mInterestsTv.setVisibility(View.GONE);
	    mActivitiesTv.setVisibility(View.GONE);
	    mTravelTv.setVisibility(View.GONE);
	    mMusicTv.setVisibility(View.GONE);
	    mDrinksTv.setVisibility(View.GONE);
	    mMyPerfectNightOutTv.setVisibility(View.GONE);
	    mStatsLayout.setVisibility(View.GONE);
	    mApproachLayout.setVisibility(View.GONE);
	    mBecomeVipBtn.setVisibility(View.GONE);
	    
	    mTonightEt.setVisibility(View.VISIBLE);
	    mAboutMeEt.setVisibility(View.VISIBLE);
	    mEducationEt.setVisibility(View.VISIBLE);
	    mOccupationEt.setVisibility(View.VISIBLE);
	    mInterestsEt.setVisibility(View.VISIBLE);
	    mActivitiesEt.setVisibility(View.VISIBLE);
	    mTravelEt.setVisibility(View.VISIBLE);
	    mMusicEt.setVisibility(View.VISIBLE);
	    mDrinksEt.setVisibility(View.VISIBLE);
	    mMyPerfectNightOutEt.setVisibility(View.VISIBLE);
	    mPersonalAdviceEt.setVisibility(View.VISIBLE);
	    
	    mEditApproachLayout.setVisibility(View.VISIBLE);
	    mDontApproachCb.setVisibility(View.VISIBLE);
	    mTipComeAndSayHiCb.setVisibility(View.VISIBLE);
	    mTipBuyMeADrinkCb.setVisibility(View.VISIBLE);
	    mTipInviteMeToDanceCb.setVisibility(View.VISIBLE);
	    mTipMakeMeLaughCb.setVisibility(View.VISIBLE);
	    mTipMeetMyFriendsCb.setVisibility(View.VISIBLE);
	    mTipSurpriseMeCb.setVisibility(View.VISIBLE);
	    mDontBeDrunkCb.setVisibility(View.VISIBLE);
	    mDontExpectAnythingCb.setVisibility(View.VISIBLE);
	    mDontBePersistentCb.setVisibility(View.VISIBLE);
	    
	    
	    Bundle args = getArguments();
	    
	    if(args.getString("tonight").trim().length() > 0)
	        mTonightEt.setText(args.getString("tonight"));
	    
	    if(args.getString("aboutMe").length() > 0)
            mAboutMeEt.setText(args.getString("aboutMe"));
        
        if(args.getString("occupation").trim().length() > 0)
            mOccupationEt.setText(args.getString("occupation"));
        
        if(args.getString("education").trim().length() > 0)
            mEducationEt.setText(args.getString("education"));
        
        if(args.getString("interests").trim().length() > 0)
            mInterestsEt.setText(args.getString("interests"));
        
        if(args.getString("activities").trim().length() > 0)
            mActivitiesEt.setText(args.getString("activities"));
        
        if(args.getString("travel").trim().length() > 0)
            mTravelEt.setText(args.getString("travel"));
        
        if(args.getString("favoriteMusic").trim().length() > 0)
            mMusicEt.setText(args.getString("favoriteMusic"));
        
        if(args.getString("favoriteDrinks").trim().length() > 0)
            mDrinksEt.setText(args.getString("favoriteDrinks"));
        
        if(args.getString("myPerfectNightOut").trim().length() > 0)
            mMyPerfectNightOutEt.setText(args.getString("myPerfectNightOut"));
	    
	    
        
        
        // APPROACH
        
        mDontApproachCb.setChecked(args.getBoolean("dontApproach"));
        mTipComeAndSayHiCb.setChecked(args.getBoolean("tipComeAndSayHi"));
        mTipBuyMeADrinkCb.setChecked(args.getBoolean("tipBuyMeADrink"));
        mTipInviteMeToDanceCb.setChecked(args.getBoolean("tipInviteMeToDance"));
        mTipMakeMeLaughCb.setChecked(args.getBoolean("tipMakeMeLaugh"));
        mTipMeetMyFriendsCb.setChecked(args.getBoolean("tipMeetMyFriends"));
        mTipSurpriseMeCb.setChecked(args.getBoolean("tipSurpriseMe"));
        mDontBeDrunkCb.setChecked(args.getBoolean("dontBeDrunk"));
        mDontExpectAnythingCb.setChecked(args.getBoolean("dontExpectAnything"));
        mDontBePersistentCb.setChecked(args.getBoolean("dontBePersistent"));
        
        if(args.getString("personalAdvice").trim().length() > 0)
            mPersonalAdviceEt.setText(args.getString("personalAdvice"));  
          
        if( mDontApproachCb.isChecked() )
        {
            collapse(mTipComeAndSayHiLayout);
            collapse(mTipBuyMeADrinkLayout);
            collapse(mTipInviteMeToDanceLayout);
            collapse(mTipMakeMeLaughLayout);
            collapse(mTipMeetMyFriendsLayout);
            collapse(mTipSurpriseMeLayout);
            collapse(mDontBeDrunkLayout);
            collapse(mDontExpectAnythingLayout);
            collapse(mDontBePersistentLayout);
            collapse(mPersonalAdviceEt);
        }
        

    }
	
	
    private void updateViewProfile()
    {   
        mTonightTv.setVisibility(View.VISIBLE);
        mAboutMeTv.setVisibility(View.VISIBLE);
        mOccupationTv.setVisibility(View.VISIBLE);
        mEducationTv.setVisibility(View.VISIBLE);
        mInterestsTv.setVisibility(View.VISIBLE);
        mActivitiesTv.setVisibility(View.VISIBLE);
        mTravelTv.setVisibility(View.VISIBLE);
        mMusicTv.setVisibility(View.VISIBLE);
        mDrinksTv.setVisibility(View.VISIBLE);
        mMyPerfectNightOutTv.setVisibility(View.VISIBLE);
        mStatsLayout.setVisibility(View.VISIBLE);
        mApproachLayout.setVisibility(View.VISIBLE);
        mBecomeVipBtn.setVisibility(View.VISIBLE);
        
        mTonightEt.setVisibility(View.GONE);
        mAboutMeEt.setVisibility(View.GONE);
        mEducationEt.setVisibility(View.GONE);
        mOccupationEt.setVisibility(View.GONE);
        mInterestsEt.setVisibility(View.GONE);
        mActivitiesEt.setVisibility(View.GONE);
        mTravelEt.setVisibility(View.GONE);
        mMusicEt.setVisibility(View.GONE);
        mDrinksEt.setVisibility(View.GONE);
        mMyPerfectNightOutEt.setVisibility(View.GONE);
        mPersonalAdviceEt.setVisibility(View.GONE);
        
        mEditApproachLayout.setVisibility(View.GONE);
        mDontApproachCb.setVisibility(View.GONE);
        mTipComeAndSayHiCb.setVisibility(View.GONE);
        mTipBuyMeADrinkCb.setVisibility(View.GONE);
        mTipInviteMeToDanceCb.setVisibility(View.GONE);
        mTipMakeMeLaughCb.setVisibility(View.GONE);
        mTipMeetMyFriendsCb.setVisibility(View.GONE);
        mTipSurpriseMeCb.setVisibility(View.GONE);
        mDontBeDrunkCb.setVisibility(View.GONE);
        mDontExpectAnythingCb.setVisibility(View.GONE);
        mDontBePersistentCb.setVisibility(View.GONE);
        
        
        Context c = getSherlockActivity().getApplicationContext();
        
        
        // TONIGHT
        
        TextView tonightText = (TextView) getView().findViewById(R.id.tonight_tv); 
        
        if( !mPerson.getTonight().equals(""))   
        {
            tonightText.setTypeface(null, Typeface.NORMAL);
            tonightText.setTextColor(getResources().getColor(android.R.color.black));
            tonightText.setText("\"" + mPerson.getTonight() + "\"");
        }
        else
        {
            tonightText.setText(getString(R.string.what_are_you_up_to_tonight));
            tonightText.setTextColor(getResources().getColor(R.color.black_trans));
            tonightText.setTypeface(null, Typeface.ITALIC);
        }
        
        
        // PROFILE
        
        if( mPerson.getAboutMe().equals(""))
        {
            mAboutMeTv.setTypeface(null, Typeface.ITALIC);
            mAboutMeTv.setText(getString(R.string.empty));
        }
        else
        {
            mAboutMeTv.setTypeface(null, Typeface.NORMAL);
            mAboutMeTv.setText(mPerson.getAboutMe());
        }

        if( mPerson.getOccupation().equals(""))
        {
            mOccupationTv.setTypeface(null, Typeface.ITALIC);
            mOccupationTv.setText(getString(R.string.empty));
        }
        else
        {
            mOccupationTv.setText(mPerson.getOccupation());
            mOccupationTv.setTypeface(null, Typeface.NORMAL);
        }
        
        if( mPerson.getEducation().equals(""))
        {
            mEducationTv.setTypeface(null, Typeface.ITALIC);
            mEducationTv.setText(getString(R.string.empty));
        }
        else
        {
            mEducationTv.setText(mPerson.getEducation());
            mEducationTv.setTypeface(null, Typeface.NORMAL);
        }

        
        if( mPerson.getInterests().equals(""))
        {
            mInterestsTv.setTypeface(null, Typeface.ITALIC);
            mInterestsTv.setText(getString(R.string.empty));
        }
        else
        {
            mInterestsTv.setText(mPerson.getInterests());
            mInterestsTv.setTypeface(null, Typeface.NORMAL);
        }

    
        if( mPerson.getActivities().equals(""))
        {
            mActivitiesTv.setText(getString(R.string.empty));
            mActivitiesTv.setTypeface(null, Typeface.ITALIC);
        }
        else
        {
            mActivitiesTv.setText(mPerson.getActivities());
            mActivitiesTv.setTypeface(null, Typeface.NORMAL);
        }

    
        if( mPerson.getTravel().equals(""))
        {
            mTravelTv.setTypeface(null, Typeface.ITALIC);
            mTravelTv.setText(getString(R.string.empty));
        }
        else
        {
            mTravelTv.setText(mPerson.getTravel());
            mTravelTv.setTypeface(null, Typeface.NORMAL);
        }

        
        if( mPerson.getMusic().equals(""))
        {
            mMusicTv.setTypeface(null, Typeface.ITALIC);
            mMusicTv.setText(getString(R.string.empty));
        }
        else
        {
            mMusicTv.setText(mPerson.getMusic());
            mMusicTv.setTypeface(null, Typeface.NORMAL);
        }

    
        if( mPerson.getDrinks().equals(""))
        {
            mDrinksTv.setTypeface(null, Typeface.ITALIC);
            mDrinksTv.setText(getString(R.string.empty));
        }
        else
        {
            mDrinksTv.setText(mPerson.getDrinks());
            mDrinksTv.setTypeface(null, Typeface.NORMAL);
        }

    
        if( mPerson.getMyPerfectNightOut().equals(""))
        {
            mMyPerfectNightOutTv.setTypeface(null, Typeface.ITALIC);
            mMyPerfectNightOutTv.setText(getString(R.string.empty));
        }
        else
        {
            mMyPerfectNightOutTv.setText(mPerson.getMyPerfectNightOut());
            mMyPerfectNightOutTv.setTypeface(null, Typeface.NORMAL);
        }
   
        
        
        
        // ACTIVITY
        
        ArrayList<Place> topFrequentedBars = mPerson.getTopFrequentedBars();
        ArrayList<BarStars> collectedBarStars = mPerson.getCollectedBarStars();
        
        if( topFrequentedBars.size() == 0 && collectedBarStars.size() == 0)
            mStatsLayout.setVisibility(View.GONE);
        
        View view = getView();
        TextView topFrequentedBarsSeparator = (TextView)view.findViewById(R.id.top_frequented_bars_separator);
        TextView topFrequentedBarTv1 = (TextView)view.findViewById(R.id.top_frequented_bar1_tv);
        TextView topFrequentedBarTv2 = (TextView)view.findViewById(R.id.top_frequented_bar2_tv);
        TextView topFrequentedBarTv3 = (TextView)view.findViewById(R.id.top_frequented_bar3_tv); 
        ImageView topFrequentedBarGenreIv1 = (ImageView)view.findViewById(R.id.top_frequented_bar1_genre_img);
        ImageView topFrequentedBarGenreIv2 = (ImageView)view.findViewById(R.id.top_frequented_bar2_genre_img);
        ImageView topFrequentedBarGenreIv3 = (ImageView)view.findViewById(R.id.top_frequented_bar3_genre_img);
        LinearLayout topFrequentedBarLayout1 = (LinearLayout)view.findViewById(R.id.top_frequented_bar1_row);
        LinearLayout topFrequentedBarLayout2 = (LinearLayout)view.findViewById(R.id.top_frequented_bar2_row);
        LinearLayout topFrequentedBarLayout3 = (LinearLayout)view.findViewById(R.id.top_frequented_bar3_row);
        
        TextView collectedBarStarsSeparator = (TextView)view.findViewById(R.id.collected_bar_stars_separator);
        LinearLayout collectedBarStarsLayout = (LinearLayout)view.findViewById(R.id.collected_bar_stars_layout);
        collectedBarStarsLayout.removeAllViews();
        
        if( topFrequentedBars.size() >= 1 )
        {
            Place p1 = topFrequentedBars.get(0);
            topFrequentedBarTv1.setText(p1.getName());
            switch(p1.getGenre())
            {
                case Place.CLUB:
                    topFrequentedBarGenreIv1.setImageResource(R.drawable.speaker);
                    break;
                case Place.LOUNGE:
                    topFrequentedBarGenreIv1.setImageResource(R.drawable.martini);
                    break;
                case Place.BAR:
                    topFrequentedBarGenreIv1.setImageResource(R.drawable.beer);
                    break;
            };
            
            if( topFrequentedBars.size() >= 2 )
            {
                Place p2 = topFrequentedBars.get(1);
                topFrequentedBarTv2.setText(p2.getName());
                switch(p2.getGenre())
                {
                    case Place.CLUB:
                        topFrequentedBarGenreIv2.setImageResource(R.drawable.speaker);
                        break;
                    case Place.LOUNGE:
                        topFrequentedBarGenreIv2.setImageResource(R.drawable.martini);
                        break;
                    case Place.BAR:
                        topFrequentedBarGenreIv2.setImageResource(R.drawable.beer);
                        break;
                };
                
                if( topFrequentedBars.size() == 3 )
                {
                    Place p3 = topFrequentedBars.get(2);
                    topFrequentedBarTv3.setText(p3.getName());
                    switch(p3.getGenre())
                    {
                        case Place.CLUB:
                            topFrequentedBarGenreIv3.setImageResource(R.drawable.speaker);
                            break;
                        case Place.LOUNGE:
                            topFrequentedBarGenreIv3.setImageResource(R.drawable.martini);
                            break;
                        case Place.BAR:
                            topFrequentedBarGenreIv3.setImageResource(R.drawable.beer);
                            break;
                    };
                }
                else
                    topFrequentedBarLayout3.setVisibility(View.GONE);
            }
            else
            {
                topFrequentedBarLayout2.setVisibility(View.GONE);
                topFrequentedBarLayout3.setVisibility(View.GONE);
            }
        }
        else
        {
            topFrequentedBarsSeparator.setVisibility(View.GONE);
            topFrequentedBarLayout1.setVisibility(View.GONE);
            topFrequentedBarLayout2.setVisibility(View.GONE);
            topFrequentedBarLayout3.setVisibility(View.GONE);
        }
        
        
        
        
        if( collectedBarStars.isEmpty() )
            collectedBarStarsSeparator.setVisibility(View.GONE);
        else
        {
            if( topFrequentedBars.size() == 0 )
                collectedBarStarsSeparator.setPadding(0, 0, 0, 0);
            
            
            for( BarStars bs : collectedBarStars)
            {
                LinearLayout barStarRow = (LinearLayout) getSherlockActivity().getLayoutInflater().inflate(R.layout.row_bar_star, null); 
                TextView barNameTv = (TextView)barStarRow.findViewById(R.id.place_name_tv);
                ImageView barGenreIv = (ImageView)barStarRow.findViewById(R.id.place_genre_img);
                TextView starCountTv = (TextView)barStarRow.findViewById(R.id.star_count_tv);
                
                Place p = bs.getPlace();
                barNameTv.setText(p.getName());
                switch(p.getGenre())
                {
                    case Place.CLUB:
                        barGenreIv.setImageResource(R.drawable.speaker);
                        break;
                    case Place.LOUNGE:
                        barGenreIv.setImageResource(R.drawable.martini);
                        break;
                    case Place.BAR:
                        barGenreIv.setImageResource(R.drawable.beer);
                        break;
                };
                starCountTv.setText(bs.getStarCount()+"");
                
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(0, (int)Utility.convertDpToPixel(5, getSherlockActivity()), 0, 0);
                
                collectedBarStarsLayout.addView(barStarRow,layoutParams);
            }
            
            
        }
        
        
                
        // APPROACH
        mApproachLayout.removeAllViews();
        Resources r = getResources();

        if( !mPerson.dontApproach() && !mPerson.tipComeAndSayHi() && !mPerson.tipBuyMeADrink() && !mPerson.tipInviteMeToDance() && !mPerson.tipMakeMeLaugh() && !mPerson.tipMeetMyFriends() &&
                !mPerson.tipSurpriseMe() && !mPerson.dontBeDrunk() && !mPerson.dontExpectAnything() && !mPerson.dontBePersistent() && mPerson.getPersonalAdvice().equals("")  )
                mApproachLayout.setVisibility(View.GONE);
        else
        {
            mApproachLayout.setVisibility(View.VISIBLE);
                
            LinearLayout llHorizontal = new LinearLayout(getSherlockActivity());
            llHorizontal.setOrientation(LinearLayout.HORIZONTAL);
            llHorizontal.setGravity(Gravity.CENTER);
            
            DisplayMetrics metrics = new DisplayMetrics();
            getSherlockActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
            int maxWidth = metrics.widthPixels - 2*(int)Utility.convertDpToPixel(15, c);
            
            switch (getActivity().getResources().getConfiguration().orientation ) 
            {
                case Configuration.ORIENTATION_LANDSCAPE: 
                    maxWidth = metrics.widthPixels/2 - 2*(int)Utility.convertDpToPixel(15, c);
                    break;
            }
            
            if( mPerson.dontApproach() )
            {
                LinearLayout ll = new LinearLayout(getSherlockActivity());
                ll.setBackground(r.getDrawable(R.drawable.back_white));
                ll.setPadding((int)Utility.convertDpToPixel(5, c), (int)Utility.convertDpToPixel(2, c), (int)Utility.convertDpToPixel(10, c), (int)Utility.convertDpToPixel(2, c));
                
                TextView tv = new TextView(getSherlockActivity());
                tv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                tv.setText(getString(R.string.dont_bother));
                tv.setTextColor(r.getColor(R.color.black));
                tv.setTextSize(12);
                tv.setPadding((int)Utility.convertDpToPixel(5, c), 0, 0, 0);
                
                ImageView iv = new ImageView(getSherlockActivity());
                iv.setLayoutParams(new LayoutParams((int)Utility.convertDpToPixel(12, c), (int)Utility.convertDpToPixel(12, c)));
                iv.setImageDrawable(r.getDrawable(R.drawable.circlex_gray));
                
                ll.addView(iv);
                ll.addView(tv);
                
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                params.setMargins(0, 0, (int)Utility.convertDpToPixel(5, c), 0);
                ll.setGravity(Gravity.CENTER);
                ll.setLayoutParams(params);
                
                llHorizontal.measure(0, 0);
                ll.measure(0, 0);
                
                if( llHorizontal.getMeasuredWidth()+ll.getMeasuredWidth() < maxWidth )
                    llHorizontal.addView(ll);
                else
                {
                    mApproachLayout.addView(llHorizontal);
                    llHorizontal = new LinearLayout(getSherlockActivity());
                    llHorizontal.setOrientation(LinearLayout.HORIZONTAL);
                    llHorizontal.setGravity(Gravity.CENTER);
                    llHorizontal.setPadding(0, (int)Utility.convertDpToPixel(5, c), 0, 0);
                    llHorizontal.addView(ll);
                }
            }
            else
            {
                if( mPerson.tipComeAndSayHi())
                {
                    
                    LinearLayout ll = new LinearLayout(getSherlockActivity());
                    ll.setBackground(r.getDrawable(R.drawable.back_white));
                    ll.setPadding((int)Utility.convertDpToPixel(5, c), (int)Utility.convertDpToPixel(2, c), (int)Utility.convertDpToPixel(10, c), (int)Utility.convertDpToPixel(2, c));
                    
                    TextView tv = new TextView(getSherlockActivity());
                    tv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                    tv.setText(getString(R.string.come_and_say_hi));
                    tv.setTextColor(r.getColor(R.color.black));
                    tv.setTextSize(12);
                    tv.setPadding(0, 0, (int)Utility.convertDpToPixel(5, c), 0);
                    
                    ImageView iv = new ImageView(getSherlockActivity());
                    iv.setLayoutParams(new LayoutParams((int)Utility.convertDpToPixel(12, c), (int)Utility.convertDpToPixel(12, c)));
                    iv.setImageDrawable(r.getDrawable(R.drawable.checkmark_grey));
                    
                    ll.addView(tv);
                    ll.addView(iv);
                    
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                    params.setMargins(0, 0, (int)Utility.convertDpToPixel(5, c), 0);
                    ll.setGravity(Gravity.CENTER);
                    ll.setLayoutParams(params);
                    
                    llHorizontal.measure(0, 0);
                    ll.measure(0, 0);
                    
                    if( llHorizontal.getMeasuredWidth()+ll.getMeasuredWidth() < maxWidth )
                        llHorizontal.addView(ll);
                    else
                    {
                        mApproachLayout.addView(llHorizontal);
                        llHorizontal = new LinearLayout(getSherlockActivity());
                        llHorizontal.setOrientation(LinearLayout.HORIZONTAL);
                        llHorizontal.setGravity(Gravity.CENTER);
                        llHorizontal.setPadding(0, (int)Utility.convertDpToPixel(5, c), 0, 0);
                        llHorizontal.addView(ll);
                    }
                }
                
                if( mPerson.tipBuyMeADrink())
                {
                    LinearLayout ll = new LinearLayout(getSherlockActivity());
                    ll.setBackground(r.getDrawable(R.drawable.back_white));
                    ll.setPadding((int)Utility.convertDpToPixel(5, c), (int)Utility.convertDpToPixel(2, c), (int)Utility.convertDpToPixel(10, c), (int)Utility.convertDpToPixel(2, c));
                    
                    TextView tv = new TextView(getSherlockActivity());
                    tv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                    tv.setText(getString(R.string.buy_me_a_drink));
                    tv.setTextColor(r.getColor(R.color.black));
                    tv.setTextSize(12);
                    tv.setPadding(0, 0, (int)Utility.convertDpToPixel(5, c), 0);
                    
                    ImageView iv = new ImageView(getSherlockActivity());
                    iv.setLayoutParams(new LayoutParams((int)Utility.convertDpToPixel(12, c), (int)Utility.convertDpToPixel(12, c)));
                    iv.setImageDrawable(r.getDrawable(R.drawable.checkmark_grey));
                    
                    ll.addView(tv);
                    ll.addView(iv);
                    
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                    params.setMargins(0, 0, (int)Utility.convertDpToPixel(5, c), 0);
                    ll.setGravity(Gravity.CENTER);
                    ll.setLayoutParams(params);
                    
                    llHorizontal.measure(0, 0);
                    ll.measure(0, 0);
                    
                    if( llHorizontal.getMeasuredWidth()+ll.getMeasuredWidth() < maxWidth )
                        llHorizontal.addView(ll);
                    else
                    {
                        mApproachLayout.addView(llHorizontal);
                        llHorizontal = new LinearLayout(getSherlockActivity());
                        llHorizontal.setOrientation(LinearLayout.HORIZONTAL);
                        llHorizontal.setGravity(Gravity.CENTER);
                        llHorizontal.setPadding(0, (int)Utility.convertDpToPixel(5, c), 0, 0);
                        llHorizontal.addView(ll);
                    }
                }
                
                if( mPerson.tipInviteMeToDance())
                {
                    LinearLayout ll = new LinearLayout(getSherlockActivity());
                    ll.setBackground(r.getDrawable(R.drawable.back_white));
                    ll.setPadding((int)Utility.convertDpToPixel(5, c), (int)Utility.convertDpToPixel(2, c), (int)Utility.convertDpToPixel(10, c), (int)Utility.convertDpToPixel(2, c));
                    
                    TextView tv = new TextView(getSherlockActivity());
                    tv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                    tv.setText(getString(R.string.invite_me_to_dance));
                    tv.setTextColor(r.getColor(R.color.black));
                    tv.setTextSize(12);
                    tv.setPadding(0, 0, (int)Utility.convertDpToPixel(5, c), 0);
                    
                    ImageView iv = new ImageView(getSherlockActivity());
                    iv.setLayoutParams(new LayoutParams((int)Utility.convertDpToPixel(12, c), (int)Utility.convertDpToPixel(12, c)));
                    iv.setImageDrawable(r.getDrawable(R.drawable.checkmark_grey));
                    
                    ll.addView(tv);
                    ll.addView(iv);
                    
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                    params.setMargins(0, 0, (int)Utility.convertDpToPixel(5, c), 0);
                    ll.setGravity(Gravity.CENTER);
                    ll.setLayoutParams(params);
                    
                    llHorizontal.measure(0, 0);
                    ll.measure(0, 0);
                    
                    if( llHorizontal.getMeasuredWidth()+ll.getMeasuredWidth() < maxWidth )
                        llHorizontal.addView(ll);
                    else
                    {
                        mApproachLayout.addView(llHorizontal);
                        llHorizontal = new LinearLayout(getSherlockActivity());
                        llHorizontal.setOrientation(LinearLayout.HORIZONTAL);
                        llHorizontal.setGravity(Gravity.CENTER);
                        llHorizontal.setPadding(0, (int)Utility.convertDpToPixel(5, c), 0, 0);
                        llHorizontal.addView(ll);
                    }
                }
                
                if( mPerson.tipMakeMeLaugh())
                {
                    LinearLayout ll = new LinearLayout(getSherlockActivity());
                    ll.setBackground(r.getDrawable(R.drawable.back_white));
                    ll.setPadding((int)Utility.convertDpToPixel(5, c), (int)Utility.convertDpToPixel(2, c), (int)Utility.convertDpToPixel(10, c), (int)Utility.convertDpToPixel(2, c));
                    
                    TextView tv = new TextView(getSherlockActivity());
                    tv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                    tv.setText(getString(R.string.make_me_laugh));
                    tv.setTextColor(r.getColor(R.color.black));
                    tv.setTextSize(12);
                    tv.setPadding(0, 0, (int)Utility.convertDpToPixel(5, c), 0);
                    
                    
                    ImageView iv = new ImageView(getSherlockActivity());
                    iv.setLayoutParams(new LayoutParams((int)Utility.convertDpToPixel(12, c), (int)Utility.convertDpToPixel(12, c)));
                    iv.setImageDrawable(r.getDrawable(R.drawable.checkmark_grey));
                    
                    ll.addView(tv);
                    ll.addView(iv);
                    
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                    params.setMargins(0, 0, (int)Utility.convertDpToPixel(5, c), 0);
                    ll.setGravity(Gravity.CENTER);
                    ll.setLayoutParams(params);
                    
                    llHorizontal.measure(0, 0);
                    ll.measure(0, 0);
                    
                    if( llHorizontal.getMeasuredWidth()+ll.getMeasuredWidth() < maxWidth )
                        llHorizontal.addView(ll);
                    else
                    {
                        mApproachLayout.addView(llHorizontal);
                        llHorizontal = new LinearLayout(getSherlockActivity());
                        llHorizontal.setOrientation(LinearLayout.HORIZONTAL);
                        llHorizontal.setGravity(Gravity.CENTER);
                        llHorizontal.setPadding(0, (int)Utility.convertDpToPixel(5, c), 0, 0);
                        llHorizontal.addView(ll);
                    }
                }
                
                if( mPerson.tipMeetMyFriends())
                {
                    LinearLayout ll = new LinearLayout(getSherlockActivity());
                    ll.setBackground(r.getDrawable(R.drawable.back_white));
                    ll.setPadding((int)Utility.convertDpToPixel(5, c), (int)Utility.convertDpToPixel(2, c), (int)Utility.convertDpToPixel(10, c), (int)Utility.convertDpToPixel(2, c));
                    
                    TextView tv = new TextView(getSherlockActivity());
                    tv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                    tv.setText(getString(R.string.meet_my_friends));
                    tv.setTextColor(r.getColor(R.color.black));
                    tv.setTextSize(12);
                    tv.setPadding(0, 0, (int)Utility.convertDpToPixel(5, c), 0);
                    
                    ImageView iv = new ImageView(getSherlockActivity());
                    iv.setLayoutParams(new LayoutParams((int)Utility.convertDpToPixel(12, c), (int)Utility.convertDpToPixel(12, c)));
                    iv.setImageDrawable(r.getDrawable(R.drawable.checkmark_grey));
                    
                    ll.addView(tv);
                    ll.addView(iv);
                    
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                    params.setMargins(0, 0, (int)Utility.convertDpToPixel(5, c), 0);
                    ll.setGravity(Gravity.CENTER);
                    ll.setLayoutParams(params);
                    
                    llHorizontal.measure(0, 0);
                    ll.measure(0, 0);
                    
                    if( llHorizontal.getMeasuredWidth()+ll.getMeasuredWidth() < maxWidth )
                        llHorizontal.addView(ll);
                    else
                    {
                        mApproachLayout.addView(llHorizontal);
                        llHorizontal = new LinearLayout(getSherlockActivity());
                        llHorizontal.setOrientation(LinearLayout.HORIZONTAL);
                        llHorizontal.setGravity(Gravity.CENTER);
                        llHorizontal.setPadding(0, (int)Utility.convertDpToPixel(5, c), 0, 0);
                        llHorizontal.addView(ll);
                    }
                }
                
                
                if( mPerson.tipSurpriseMe())
                {
                    LinearLayout ll = new LinearLayout(getSherlockActivity());
                    ll.setBackground(r.getDrawable(R.drawable.back_white));
                    ll.setPadding((int)Utility.convertDpToPixel(5, c), (int)Utility.convertDpToPixel(2, c), (int)Utility.convertDpToPixel(10, c), (int)Utility.convertDpToPixel(2, c));
                    
                    TextView tv = new TextView(getSherlockActivity());
                    tv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                    tv.setText(getString(R.string.surprise_me));
                    tv.setTextColor(r.getColor(R.color.black));
                    tv.setTextSize(12);
                    tv.setPadding(0, 0, (int)Utility.convertDpToPixel(5, c), 0);
                    
                    ImageView iv = new ImageView(getSherlockActivity());
                    iv.setLayoutParams(new LayoutParams((int)Utility.convertDpToPixel(12, c), (int)Utility.convertDpToPixel(12, c)));
                    iv.setImageDrawable(r.getDrawable(R.drawable.checkmark_grey));
                    
                    ll.addView(tv);
                    ll.addView(iv);
                    
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                    params.setMargins(0, 0, (int)Utility.convertDpToPixel(5, c), 0);
                    ll.setGravity(Gravity.CENTER);
                    ll.setLayoutParams(params);
                    
                    llHorizontal.measure(0, 0);
                    ll.measure(0, 0);
                    
                    if( llHorizontal.getMeasuredWidth()+ll.getMeasuredWidth() < maxWidth )
                        llHorizontal.addView(ll);
                    else
                    {
                        mApproachLayout.addView(llHorizontal);
                        llHorizontal = new LinearLayout(getSherlockActivity());
                        llHorizontal.setOrientation(LinearLayout.HORIZONTAL);
                        llHorizontal.setGravity(Gravity.CENTER);
                        llHorizontal.setPadding(0, (int)Utility.convertDpToPixel(5, c), 0, 0);
                        llHorizontal.addView(ll);
                    }
                }
                
                if( mPerson.dontBeDrunk())
                {
                    LinearLayout ll = new LinearLayout(getSherlockActivity());
                    ll.setBackground(r.getDrawable(R.drawable.back_white));
                    ll.setPadding((int)Utility.convertDpToPixel(5, c), (int)Utility.convertDpToPixel(2, c), (int)Utility.convertDpToPixel(10, c), (int)Utility.convertDpToPixel(2, c));
                    
                    TextView tv = new TextView(getSherlockActivity());
                    tv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                    tv.setText(getString(R.string.dont_be_drunk));
                    tv.setTextColor(r.getColor(R.color.black));
                    tv.setTextSize(12);
                    tv.setPadding(0, 0, (int)Utility.convertDpToPixel(5, c), 0);
                    
                    ImageView iv = new ImageView(getSherlockActivity());
                    iv.setLayoutParams(new LayoutParams((int)Utility.convertDpToPixel(12, c), (int)Utility.convertDpToPixel(12, c)));
                    iv.setImageDrawable(r.getDrawable(R.drawable.checkmark_grey));
                    
                    ll.addView(tv);
                    ll.addView(iv);
                    
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                    params.setMargins(0, 0, (int)Utility.convertDpToPixel(5, c), 0);
                    ll.setGravity(Gravity.CENTER);
                    ll.setLayoutParams(params);
                    
                    llHorizontal.measure(0, 0);
                    ll.measure(0, 0);
                    
                    if( llHorizontal.getMeasuredWidth()+ll.getMeasuredWidth() < maxWidth )
                        llHorizontal.addView(ll);
                    else
                    {
                        mApproachLayout.addView(llHorizontal);
                        llHorizontal = new LinearLayout(getSherlockActivity());
                        llHorizontal.setOrientation(LinearLayout.HORIZONTAL);
                        llHorizontal.setGravity(Gravity.CENTER);
                        llHorizontal.setPadding(0, (int)Utility.convertDpToPixel(5, c), 0, 0);
                        llHorizontal.addView(ll);
                    }
                }
                
                if( mPerson.dontExpectAnything())
                {
                    LinearLayout ll = new LinearLayout(getSherlockActivity());
                    ll.setBackground(r.getDrawable(R.drawable.back_white));
                    ll.setPadding((int)Utility.convertDpToPixel(5, c), (int)Utility.convertDpToPixel(2, c), (int)Utility.convertDpToPixel(10, c), (int)Utility.convertDpToPixel(2, c));
                    
                    TextView tv = new TextView(getSherlockActivity());
                    tv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                    tv.setText(getString(R.string.dont_expect_anything));
                    tv.setTextColor(r.getColor(R.color.black));
                    tv.setTextSize(12);
                    tv.setPadding(0, 0, (int)Utility.convertDpToPixel(5, c), 0);
                    
                    ImageView iv = new ImageView(getSherlockActivity());
                    iv.setLayoutParams(new LayoutParams((int)Utility.convertDpToPixel(12, c), (int)Utility.convertDpToPixel(12, c)));
                    iv.setImageDrawable(r.getDrawable(R.drawable.checkmark_grey));
                    
                    ll.addView(tv);
                    ll.addView(iv);
                    
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                    params.setMargins(0, 0, (int)Utility.convertDpToPixel(5, c), 0);
                    ll.setGravity(Gravity.CENTER);
                    ll.setLayoutParams(params);
                    
                    llHorizontal.measure(0, 0);
                    ll.measure(0, 0);
                    
                    if( llHorizontal.getMeasuredWidth()+ll.getMeasuredWidth() < maxWidth )
                        llHorizontal.addView(ll);
                    else
                    {
                        mApproachLayout.addView(llHorizontal);
                        llHorizontal = new LinearLayout(getSherlockActivity());
                        llHorizontal.setOrientation(LinearLayout.HORIZONTAL);
                        llHorizontal.setGravity(Gravity.CENTER);
                        llHorizontal.setPadding(0, (int)Utility.convertDpToPixel(5, c), 0, 0);
                        llHorizontal.addView(ll);
                    }
                }
                
                if( mPerson.dontBePersistent())
                {
                    LinearLayout ll = new LinearLayout(getSherlockActivity());
                    ll.setBackground(r.getDrawable(R.drawable.back_white));
                    ll.setPadding((int)Utility.convertDpToPixel(5, c), (int)Utility.convertDpToPixel(2, c), (int)Utility.convertDpToPixel(10, c), (int)Utility.convertDpToPixel(2, c));
                    
                    TextView tv = new TextView(getSherlockActivity());
                    tv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                    tv.setText(getString(R.string.dont_be_persistent));
                    tv.setTextColor(r.getColor(R.color.black));
                    tv.setTextSize(12);
                    tv.setPadding(0, 0, (int)Utility.convertDpToPixel(5, c), 0);
                    
                    ImageView iv = new ImageView(getSherlockActivity());
                    iv.setLayoutParams(new LayoutParams((int)Utility.convertDpToPixel(12, c), (int)Utility.convertDpToPixel(12, c)));
                    iv.setImageDrawable(r.getDrawable(R.drawable.checkmark_grey));
                    
                    ll.addView(tv);
                    ll.addView(iv);
                    
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                    params.setMargins(0, 0, (int)Utility.convertDpToPixel(5, c), 0);
                    ll.setGravity(Gravity.CENTER);
                    ll.setLayoutParams(params);
                    
                    llHorizontal.measure(0, 0);
                    ll.measure(0, 0);
                    
                    if( llHorizontal.getMeasuredWidth()+ll.getMeasuredWidth() < maxWidth )
                        llHorizontal.addView(ll);
                    else
                    {
                        mApproachLayout.addView(llHorizontal);
                        llHorizontal = new LinearLayout(getSherlockActivity());
                        llHorizontal.setOrientation(LinearLayout.HORIZONTAL);
                        llHorizontal.setGravity(Gravity.CENTER);
                        llHorizontal.setPadding(0, (int)Utility.convertDpToPixel(5, c), 0, 0);
                        llHorizontal.addView(ll);
                    }
                }
                
                if( !mPerson.getPersonalAdvice().equals(""))
                {
                    LinearLayout ll = new LinearLayout(getSherlockActivity());
                    ll.setBackground(r.getDrawable(R.drawable.back_white));
                    ll.setPadding((int)Utility.convertDpToPixel(5, c), (int)Utility.convertDpToPixel(2, c), (int)Utility.convertDpToPixel(10, c), (int)Utility.convertDpToPixel(2, c));
                    
                    TextView tv = new TextView(getSherlockActivity());
                    tv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                    tv.setText(mPerson.getPersonalAdvice());
                    tv.setTextColor(r.getColor(R.color.black));
                    tv.setTextSize(12);
                    tv.setPadding(0, 0, (int)Utility.convertDpToPixel(5, c), 0);
                    
                    ImageView iv = new ImageView(getSherlockActivity());
                    iv.setLayoutParams(new LayoutParams((int)Utility.convertDpToPixel(12, c), (int)Utility.convertDpToPixel(12, c)));
                    iv.setImageDrawable(r.getDrawable(R.drawable.checkmark_grey));
                    
                    ll.addView(tv);
                    ll.addView(iv);
                    
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                    params.setMargins(0, 0, (int)Utility.convertDpToPixel(5, c), 0);
                    ll.setGravity(Gravity.CENTER);
                    ll.setLayoutParams(params);
                    
                    llHorizontal.measure(0, 0);
                    ll.measure(0, 0);
                    
                    if( llHorizontal.getMeasuredWidth()+ll.getMeasuredWidth() < maxWidth )
                        llHorizontal.addView(ll);
                    else
                    {
                        mApproachLayout.addView(llHorizontal);
                        llHorizontal = new LinearLayout(getSherlockActivity());
                        llHorizontal.setOrientation(LinearLayout.HORIZONTAL);
                        llHorizontal.setGravity(Gravity.CENTER);
                        llHorizontal.setPadding(0, (int)Utility.convertDpToPixel(5, c), 0, 0);
                        llHorizontal.addView(ll);
                    }
                }
            }
            
            mApproachLayout.addView(llHorizontal);
        }
    }	
	
    public static void expand(final View v) {
        v.measure(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        final int targtetHeight = v.getMeasuredHeight();

        v.getLayoutParams().height = 0;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? LayoutParams.WRAP_CONTENT
                        : (int)(targtetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int)(targtetHeight / v.getContext().getResources().getDisplayMetrics().density)*5);
        v.startAnimation(a);
    }

    public static void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if(interpolatedTime == 1){
                    v.setVisibility(View.GONE);
                }else{
                    v.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int)(initialHeight / v.getContext().getResources().getDisplayMetrics().density)*5);
        v.startAnimation(a);
    }
    
    
    private void save()
    {
        
        mMyProfileEditInfoTaskMethod.doTask(User.getInstance().getUserId(), mTonightEt.getText().toString(), mAboutMeEt.getText().toString(), 
                mOccupationEt.getText().toString(),  mEducationEt.getText().toString(),  
                mInterestsEt.getText().toString(), mActivitiesEt.getText().toString(),  mTravelEt.getText().toString(),
                mMusicEt.getText().toString(),  mDrinksEt.getText().toString(), mMyPerfectNightOutEt.getText().toString(),
                mDontApproachCb.isChecked(), mTipComeAndSayHiCb.isChecked(), mTipBuyMeADrinkCb.isChecked(), mTipInviteMeToDanceCb.isChecked(), 
                mTipMakeMeLaughCb.isChecked(), mTipMeetMyFriendsCb.isChecked(), mTipSurpriseMeCb.isChecked(),
                mDontBeDrunkCb.isChecked(), mDontExpectAnythingCb.isChecked(), mDontBePersistentCb.isChecked(), mPersonalAdviceEt.getText().toString());
                
    }

	
	
	// EVENTS
	
    private void onClickPhoto() 
    {
        User u = User.getInstance();
        
        ArrayList<String> urls = new ArrayList<String>();
        ArrayList<String> profilePhotoFilenames = u.getProfilePhotosFilenames();
        ArrayList<String> otherPhotoFilenames = u.getOtherPhotosFilenames();
        
        for( String filename : profilePhotoFilenames )
        {
            urls.add("http://s3.amazonaws.com/signals/user_images/" + u.getUserId() + "/profilePhotos/" + filename);
        }
        
        for( String filename : otherPhotoFilenames )
        {
            urls.add("http://s3.amazonaws.com/signals/user_images/" + u.getUserId() + "/otherPhotos/" + filename);
        }
        
        Intent intent = new Intent(getSherlockActivity(), MyProfilePicsPagerActivity.class);  
        Bundle bundle = new Bundle();    
        bundle.putString("username", u.getUsername() );  
        bundle.putStringArrayList("urls", urls );  
        bundle.putInt("pos", 0 );  
        intent.putExtras(bundle);
        startActivity(intent);
    }
    
    private void onClickPhotos() 
    {
        MyProfileEditPhotosFragment frag = MyProfileEditPhotosFragment.newInstance( User.getInstance() );
        
        getSherlockActivity().getSupportFragmentManager()
        .beginTransaction()
        .replace(R.id.content_frame, frag, "MyProfileEditPhotosFragment")
        .addToBackStack(null)
        .commit();
    }
    
    private void onClickBasicInfo()
    {
        MyProfileEditBasicInfoFragment frag = MyProfileEditBasicInfoFragment.newInstance( User.getInstance() );
    
        getSherlockActivity().getSupportFragmentManager()
        .beginTransaction()
        .replace(R.id.content_frame, frag, "MyProfileEditBasicInfoFragment")
        .addToBackStack(null)
        .commit();
    }
    
    
    
	private void onClickBecomeVip()
	{
        VipPrivilegesFragment frag = (VipPrivilegesFragment)getSherlockActivity().getSupportFragmentManager().findFragmentByTag("VipPrivilegesFragment");
        
        if( frag == null )
            frag = VipPrivilegesFragment.newInstance( User.getInstance() );
    
        getSherlockActivity().getSupportFragmentManager()
        .beginTransaction()
        .replace(R.id.content_frame, frag, "VipPrivilegesFragment")
        .addToBackStack(null)
        .commit();
        
	}
	
	private void onClickDontApproach()
	{
	    if( mDontApproachCb.isChecked() )
	    {
	        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
            
            // set title
            alertDialogBuilder.setTitle(getActivity().getString(R.string.disable_approach));
 
            // set dialog message
            alertDialogBuilder
                .setMessage(getActivity().getString(R.string.dont_approach_confirmation))
                .setCancelable(false)
                .setPositiveButton(getActivity().getString(R.string.ok),new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {

                        collapse(mTipComeAndSayHiLayout);
                        collapse(mTipBuyMeADrinkLayout);
                        collapse(mTipInviteMeToDanceLayout);
                        collapse(mTipMakeMeLaughLayout);
                        collapse(mTipMeetMyFriendsLayout);
                        collapse(mTipSurpriseMeLayout);
                        collapse(mDontBeDrunkLayout);
                        collapse(mDontExpectAnythingLayout);
                        collapse(mDontBePersistentLayout);
                        collapse(mPersonalAdviceEt);
                    }
                  })
                .setNegativeButton(getActivity().getString(R.string.cancel),new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        mDontApproachCb.setChecked(false);
                        
                        dialog.cancel();
                    }
                });
 
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();   
	       
	    }
	    else
	    {
	        expand(mTipComeAndSayHiLayout);
	        expand(mTipBuyMeADrinkLayout);
	        expand(mTipInviteMeToDanceLayout);
	        expand(mTipMakeMeLaughLayout);
	        expand(mTipMeetMyFriendsLayout);
	        expand(mTipSurpriseMeLayout);
	        expand(mDontBeDrunkLayout);
	        expand(mDontExpectAnythingLayout);
	        expand(mDontBePersistentLayout);
	        expand(mPersonalAdviceEt);
	    }
	}
	
	@Override
	public void profilesTaskEventReceived(ProfilesTaskEvent evt) 
	{
		ProfilesTaskMethod met = (ProfilesTaskMethod)evt.getSource();
		
		if( met != null && met.success() )
		{
             
		    User u = User.getInstance();
		    Person p = met.getPeople().get(0);
		    
		    // ACCOUNT
		    u.setUsername(p.getUsername());
            
            // BASIC INFO
            u.setSex(p.getSex());
            u.setInterestedIn(p.getInterestedIn());
            u.setBirthday(p.getBirthday());
            u.setAge(p.getAge()); 
            
            
            // PHOTOS
            u.setProfilePhotosFilenames(p.getProfilePhotosFilenames());
            u.setOtherPhotosFilenames(p.getOtherPhotosFilenames());
            
            
            
            // ABOUT
            u.setAboutMe(p.getAboutMe());
            u.setOccupation(p.getOccupation());
            u.setEducation(p.getEducation());
            u.setInterests(p.getInterests());
            u.setActivities(p.getActivities());
            u.setTravel(p.getTravel());
            u.setMusic(p.getMusic());
            u.setDrinks(p.getDrinks());
            u.setMyPerfectNightOut(p.getMyPerfectNightOut());
            
            
            // ACTIVITY
            u.setCheckPlace(p.getCheckPlace());
            u.setStar(p.star());
            u.setTopFrequentedBars(p.getTopFrequentedBars());
            u.setCollectedBarStars(p.getCollectedBarStars());
            
            
            // APPROACH
            u.setTipComeAndSayHi(p.tipComeAndSayHi());
            u.setTipBuyMeADrink(p.tipBuyMeADrink());
            u.setTipInviteMeToDance(p.tipInviteMeToDance());
            u.setTipMakeMeLaugh(p.tipMakeMeLaugh());
            u.setTipMeetMyFriends(p.tipMeetMyFriends());
            u.setTipSurpriseMe(p.tipSurpriseMe());
            u.setDontBeDrunk(p.dontBeDrunk());
            u.setDontExpectAnything(p.dontExpectAnything());
            u.setDontBePersistent(p.dontBePersistent());
            u.setPersonalAdvice(p.getPersonalAdvice());
            
            
            // MORE
            u.setTonight(p.getTonight());
            u.setDontApproach(p.dontApproach());
            u.setLookingFor(p.getLookingFor());
       
            
            // Update args
            Bundle args = getArguments();
            args.putString("tonight", u.getTonight());
            args.putString("aboutMe", u.getAboutMe());
            args.putString("occupation",  u.getOccupation());
            args.putString("education",  u.getEducation());
            args.putString("interests",  u.getInterests());
            args.putString("activities",  u.getActivities());
            args.putString("travel",  u.getTravel());
            args.putString("favoriteMusic",  u.getMusic());
            args.putString("favoriteDrinks", u.getDrinks());
            args.putString("myPerfectNightOut", u.getMyPerfectNightOut());
            
            args.putBoolean("dontApproach", u.dontApproach());
            args.putBoolean("tipComeAndSayHi", u.tipComeAndSayHi());
            args.putBoolean("tipBuyMeADrink", u.tipBuyMeADrink());
            args.putBoolean("tipInviteMeToDance", u.tipInviteMeToDance());
            args.putBoolean("tipMakeMeLaugh", u.tipMakeMeLaugh());
            args.putBoolean("tipMeetMyFriends", u.tipMeetMyFriends());
            args.putBoolean("tipSurpriseMe", u.tipSurpriseMe());
            args.putBoolean("dontBeDrunk", u.dontBeDrunk());
            args.putBoolean("dontExpectAnything", u.dontExpectAnything());
            args.putBoolean("dontBePersistent", u.dontBePersistent());
            args.putString("personalAdvice", u.getPersonalAdvice());
            
            
			updateView( mEditState );
		}	
		
	}



    @Override
    public void myProfileEditInfoTaskEventReceived(MyProfileEditInfoTaskEvent evt) {
        MyProfileEditInfoTaskMethod met = (MyProfileEditInfoTaskMethod)evt.getSource();
        
        if( met != null && met.success() )
        {
            User u = User.getInstance();
            
            u.setTonight(mTonightEt.getText().toString());
            u.setAboutMe(mAboutMeEt.getText().toString());
            u.setOccupation(mOccupationEt.getText().toString());
            u.setEducation(mEducationEt.getText().toString());
            u.setInterests(mInterestsEt.getText().toString());
            u.setActivities(mActivitiesEt.getText().toString());
            u.setTravel(mTravelEt.getText().toString());
            u.setMusic(mMusicEt.getText().toString());
            u.setDrinks(mDrinksEt.getText().toString());
            u.setMyPerfectNightOut(mMyPerfectNightOutEt.getText().toString());
            
            u.setDontApproach(mDontApproachCb.isChecked());
            u.setTipComeAndSayHi(mTipComeAndSayHiCb.isChecked());
            u.setTipBuyMeADrink(mTipBuyMeADrinkCb.isChecked());
            u.setTipInviteMeToDance(mTipInviteMeToDanceCb.isChecked());
            u.setTipMakeMeLaugh(mTipMakeMeLaughCb.isChecked());
            u.setTipMeetMyFriends(mTipMeetMyFriendsCb.isChecked());
            u.setTipSurpriseMe(mTipSurpriseMeCb.isChecked());
            u.setDontBeDrunk(mDontBeDrunkCb.isChecked());
            u.setDontExpectAnything(mDontExpectAnythingCb.isChecked());
            u.setDontBePersistent(mDontBePersistentCb.isChecked());
            u.setPersonalAdvice(mPersonalAdviceEt.getText().toString());
            
            // Update args
            Bundle args = getArguments();
            args.putString("tonight", u.getTonight());
            args.putString("aboutMe", u.getAboutMe());
            args.putString("occupation",  u.getOccupation());
            args.putString("education",  u.getEducation());
            args.putString("interests",  u.getInterests());
            args.putString("activities",  u.getActivities());
            args.putString("travel",  u.getTravel());
            args.putString("favoriteMusic",  u.getMusic());
            args.putString("favoriteDrinks", u.getDrinks());
            args.putString("myPerfectNightOut", u.getMyPerfectNightOut());
            
            args.putBoolean("dontApproach", u.dontApproach());
            args.putBoolean("tipComeAndSayHi", u.tipComeAndSayHi());
            args.putBoolean("tipBuyMeADrink", u.tipBuyMeADrink());
            args.putBoolean("tipInviteMeToDance", u.tipInviteMeToDance());
            args.putBoolean("tipMakeMeLaugh", u.tipMakeMeLaugh());
            args.putBoolean("tipMeetMyFriends", u.tipMeetMyFriends());
            args.putBoolean("tipSurpriseMe", u.tipSurpriseMe());
            args.putBoolean("dontBeDrunk", u.dontBeDrunk());
            args.putBoolean("dontExpectAnything", u.dontExpectAnything());
            args.putBoolean("dontBePersistent", u.dontBePersistent());
            args.putString("personalAdvice", u.getPersonalAdvice());
            
            
            Toast.makeText(getSherlockActivity(), getString(R.string.changes_saved), Toast.LENGTH_LONG).show();
            
            
            if( mMode != null )
                mMode.finish();
        }
    }


	    


	
}
