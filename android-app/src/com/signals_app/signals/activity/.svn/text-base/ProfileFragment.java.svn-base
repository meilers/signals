package com.signals_app.signals.activity;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.EventObject;
import java.util.concurrent.CopyOnWriteArrayList;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.signals_app.signals.R;
import com.signals_app.signals.model.Place;
import com.signals_app.signals.model.Profile.BarStars;
import com.signals_app.signals.model.Profile.Person;
import com.signals_app.signals.model.Profile.User;
import com.signals_app.signals.model.rendezvous.PotentialRendezvous;
import com.signals_app.signals.tasks.ProfilesTaskMethod;
import com.signals_app.signals.tasks.ProfilesTaskMethod.ProfilesTaskEvent;
import com.signals_app.signals.tasks.ProfilesTaskMethod.ProfilesTaskListener;
import com.signals_app.signals.tasks.rendezvous.InvitationTaskMethod;
import com.signals_app.signals.tasks.rendezvous.NoThanksTaskMethod;
import com.signals_app.signals.tasks.rendezvous.InvitationTaskMethod.InvitationTaskEvent;
import com.signals_app.signals.tasks.rendezvous.InvitationTaskMethod.InvitationTaskListener;
import com.signals_app.signals.tasks.rendezvous.NoThanksTaskMethod.NoThanksTaskEvent;
import com.signals_app.signals.tasks.rendezvous.NoThanksTaskMethod.NoThanksTaskListener;
import com.signals_app.signals.tasks.signals.BlockTaskMethod;
import com.signals_app.signals.tasks.signals.MessageTaskMethod;
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

import android.app.ActionBar.LayoutParams;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ScaleDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import android.support.v4.app.FragmentTransaction;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;

public class ProfileFragment extends SherlockFragment implements FlirtTaskListener, VoteTaskListener, ReportTaskListener, BlockTaskListener, UnblockTaskListener, NoThanksTaskListener, InvitationTaskListener
{
	private Person mPerson = null;
	private boolean mForInvitation = false;
	
	private FlirtTaskMethod mFlirtTaskMethod;
	private VoteTaskMethod mVoteTaskMethod;
	private BlockTaskMethod mBlockTaskMethod;
	private UnblockTaskMethod mUnblockTaskMethod;
	private ReportTaskMethod mReportTaskMethod;

	private InvitationTaskMethod mInvitationTaskMethod;
	private NoThanksTaskMethod mNoThanksTaskMethod;
	
	private ScrollView mScrollView;
	private ImageView mBarsStarIv;
	private ImageView mProfilePicIv;
	private TextView mPhotosTopTv;
	private RelativeLayout mPhotosTopRl;
	private LinearLayout mApproachLayout;
	
	private TableRow mInvitationRow;
	private TableRow mSignalsRow;
    private LinearLayout mReportLayout;
    
	private Button mFlirtBtn;
	private Button mMessageBtn;
	private Button mVoteBtn;
	private Button mNoThanksBtn;
	private Button mInvitationBtn;
	private TextView mBlockTv;
	private TextView mReportTv;
	
	// TIMER
	private int msDec = 5;
    private Handler mHandler = new Handler();
    private Runnable mRunnable = new Runnable() {

        @Override 
        public void run() {
            

            msDec--;
            
            if( getView() != null )
            {
                
                if( msDec <= 0 )
                {
                    ProfilesPagerFragment profilesPagerFrag = (ProfilesPagerFragment)getSherlockActivity().getSupportFragmentManager().findFragmentByTag("ProfilesPagerFragment");
                    
                    if( profilesPagerFrag != null )
                        profilesPagerFrag.removeProfilePage(ProfileFragment.this);
                    else
                    {
                        InvitationsPagerFragment invitationsPagerFrag = (InvitationsPagerFragment)getSherlockActivity().getSupportFragmentManager().findFragmentByTag("InvitationsPagerFragment");
                        
                        if( invitationsPagerFrag != null )
                            invitationsPagerFrag.removeProfilePage(ProfileFragment.this);
                    }
                    
                    return;
                }
            }
            
            
            // RESTART TIMER
            mHandler.postDelayed(mRunnable, 1);
        }
    };
    
    
	
  	public static final ProfileFragment newInstance( Person person, boolean forInvitation )
  	{
  	    ProfileFragment f = new ProfileFragment();

        // Get arguments passed in, if any
        Bundle args = f.getArguments();
        if (args == null) {
            args = new Bundle();
        }
        
        // Add parameters to the argument bundle
        args.putBoolean("forInvitation", forInvitation);
        args.putParcelable("person", person);
        f.setArguments(args);
        
        return f;
  	}
  	
  	 
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}
	
	
	@SuppressWarnings("unchecked")
    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Bundle args = getArguments();
		
        mPerson = args.getParcelable("person");
        mForInvitation = args.getBoolean("forInvitation");
        
        
		mFlirtTaskMethod = new FlirtTaskMethod(getSherlockActivity());
        mVoteTaskMethod = new VoteTaskMethod(getSherlockActivity());
        mBlockTaskMethod = new BlockTaskMethod(getSherlockActivity());
        mUnblockTaskMethod = new UnblockTaskMethod(getSherlockActivity());
        mReportTaskMethod = new ReportTaskMethod(getSherlockActivity());
        
        mNoThanksTaskMethod = new NoThanksTaskMethod(getSherlockActivity());
        mInvitationTaskMethod = new InvitationTaskMethod(getSherlockActivity());
	}
 
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
        
		// LISTENERS
	    mFlirtTaskMethod.addFlirtTaskListener(this);
        mVoteTaskMethod.addVoteTaskListener(this);
        mBlockTaskMethod.addBlockTaskListener(this);
        mUnblockTaskMethod.addUnblockTaskListener(this);
        mReportTaskMethod.addReportTaskListener(this);
	        
        mNoThanksTaskMethod.addNoThanksTaskListener(this);
        mInvitationTaskMethod.addInvitationTaskListener(this);
        
		// WIDGETS

        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        
		mScrollView = (ScrollView)view.findViewById(R.id.scroll_view); 
		mBarsStarIv = (ImageView)view.findViewById(R.id.bar_star_img); 
		mProfilePicIv = (ImageView)view.findViewById(R.id.profile_pic_img); 
		mPhotosTopTv = (TextView)view.findViewById(R.id.photos_top_text);
		mPhotosTopRl = (RelativeLayout)view.findViewById(R.id.photos_top_rl);
		
		mApproachLayout = (LinearLayout)view.findViewById(R.id.approach_layout);
		
		mInvitationRow = (TableRow)view.findViewById(R.id.invite_row);
		mSignalsRow = (TableRow)view.findViewById(R.id.signals_row);
        mReportLayout = (LinearLayout)view.findViewById(R.id.report_layout);
        
		mFlirtBtn = (Button)view.findViewById(R.id.flirt_btn);
		mMessageBtn = (Button)view.findViewById(R.id.message_btn);
		mVoteBtn = (Button)view.findViewById(R.id.vote_btn);
		
		mNoThanksBtn = (Button)view.findViewById(R.id.no_thanks_btn);
		mInvitationBtn = (Button)view.findViewById(R.id.lets_meet_up_btn);
		
		mBlockTv = (TextView)view.findViewById(R.id.block_text);
		mReportTv = (TextView)view.findViewById(R.id.report_text);
		
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
                onClickPhoto();
            }
        });
		
		mFlirtBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
        		onClickFlirt();
            }
        });
		
		mMessageBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
        		onClickMessage();
            }
        });
		
		mVoteBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
        		onClickVote();
            }
        });
		
		mNoThanksBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickNoThanks();
            }
        });
		
		
		mInvitationBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickInvitation();
            }
        });
		
		mBlockTv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	onClickBlock();
            }
        });
		
		mReportTv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	onClickReport();
            }
        });
        

		
		return view;
	}


    
    @Override
    public void onResume()
    {
    	super.onResume();
    	
    	updateView();
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
        
        // Remove Listeners
        mFlirtTaskMethod.removeFlirtTaskListener(this);
        mVoteTaskMethod.removeVoteTaskListener(this);
        mBlockTaskMethod.removeBlockTaskListener(this);
        mUnblockTaskMethod.removeUnblockTaskListener(this);
        mReportTaskMethod.removeReportTaskListener(this);
        mNoThanksTaskMethod.removeNoThanksTaskListener(this);
        mInvitationTaskMethod.removeInvitationTaskListener(this);
        
        mFlirtTaskMethod.cleanUp();
        mVoteTaskMethod.cleanUp();
        mBlockTaskMethod.cleanUp();
        mUnblockTaskMethod.cleanUp();
        mReportTaskMethod.cleanUp();
        mNoThanksTaskMethod.cleanUp();
    } 

    
    
	private void updateView()
	{	
        Context c = getSherlockActivity().getApplicationContext();
        int padding = (int)Utility.convertDpToPixel(5, c);
        
        
    	// PROFILE PIC
		DisplayImageOptions imageOptions = new DisplayImageOptions.Builder()
		.cacheInMemory()
		.cacheOnDisc()
		.showStubImage(mPerson.getSex().equals(Person.SEX_FEMALE) ? R.drawable.lady_grey : R.drawable.gent_grey)
		.build();
		
		Animation fadeInAnimation = AnimationUtils.loadAnimation(getSherlockActivity(), R.anim.fadein);
		ImageLoader.getInstance().displayImage("http://s3.amazonaws.com/signals/user_images/" + mPerson.getUserId() + "/profilePhotoSquare.jpg", mProfilePicIv, imageOptions);
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
		TextView basicInfoText = (TextView)getView().findViewById(R.id.basic_info_text);
		basicInfoText.setText(mPerson.getAge() + " / " 
							+ (mPerson.getSex() == 1 ? getResources().getString(R.string.male_short): getResources().getString(R.string.female_short)) 
							+ " / " + orientations[mPerson.getOrientation()-1]  );

		
		// LOOKING FOR
		
    	int lookingFor = mPerson.getLookingFor();
		
		ImageView lookingIv = (ImageView)getView().findViewById(R.id.looking_for_img);
		TextView lookingText = (TextView)getView().findViewById(R.id.looking_for_text);
    	
		if( lookingFor != Person.LOOKING_NONE )
		{
			lookingIv.setVisibility(View.VISIBLE);
			lookingText.setVisibility(View.VISIBLE);
			
			switch( lookingFor )
			{
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
		}
		else
		{
			lookingIv.setVisibility(View.GONE);
			lookingText.setVisibility(View.GONE);
		}

		
		// TONIGHT
		
		TextView tonightText = (TextView) getView().findViewById(R.id.tonight_text); 
		LinearLayout tonightLayout = (LinearLayout) getView().findViewById(R.id.tonight_layout); 
		
		if( !mPerson.getTonight().equals(""))   
		{
		    tonightLayout.setVisibility(View.VISIBLE);
			tonightText.setText("\"" + mPerson.getTonight() + "\"");
		}
		else
		{
		    tonightLayout.setVisibility(View.GONE);
		}
		
		
		// PROFILE
		View view = getView();
        RelativeLayout aboutMeRl = (RelativeLayout)view.findViewById(R.id.about_me_layout);
        RelativeLayout occupationRl = (RelativeLayout)view.findViewById(R.id.occupation_layout);
        RelativeLayout educationRl = (RelativeLayout)view.findViewById(R.id.education_layout);
        RelativeLayout interestsRl = (RelativeLayout)view.findViewById(R.id.interests_layout);
        RelativeLayout activitiesRl = (RelativeLayout)view.findViewById(R.id.activities_layout);
        RelativeLayout travelRl = (RelativeLayout)view.findViewById(R.id.travel_layout);
        RelativeLayout favoriteMusicRl = (RelativeLayout)view.findViewById(R.id.favorite_music_layout);
        RelativeLayout favoriteDrinksRl = (RelativeLayout)view.findViewById(R.id.favorite_drinks_layout);
        RelativeLayout myPerfectNightOutRl = (RelativeLayout)view.findViewById(R.id.my_perfect_night_out_layout);
        
        TextView aboutMeTv = (TextView)view.findViewById(R.id.about_me_text);
        
        TextView occupationTv = (TextView)view.findViewById(R.id.occupation_text);
        TextView educationTv = (TextView)view.findViewById(R.id.education_text);
        TextView interestsTv = (TextView)view.findViewById(R.id.interests_text);
        TextView activitiesTv = (TextView)view.findViewById(R.id.activities_text);
        TextView travelTv = (TextView)view.findViewById(R.id.travel_text);
        TextView favoriteMusicTv = (TextView)view.findViewById(R.id.favorite_music_text);
        TextView favoriteDrinksTv = (TextView)view.findViewById(R.id.favorite_drinks_text);
        TextView myPerfectNightOutTv = (TextView)view.findViewById(R.id.my_perfect_night_out_text);
        
        LinearLayout profileLayout = (LinearLayout)getView().findViewById(R.id.profile_layout);
        profileLayout.setVisibility(View.VISIBLE);
        
        if( mPerson.getAboutMe().equals("") && mPerson.getOccupation().equals("") && mPerson.getEducation().equals("") && mPerson.getInterests().equals("") &&
                mPerson.getActivities().equals("") && mPerson.getTravel().equals("") && mPerson.getMusic().equals("") && mPerson.getDrinks().equals("") && mPerson.getMyPerfectNightOut().equals("")  )
        {
            profileLayout.setVisibility(View.GONE);
        }
        else
            profileLayout.setVisibility(View.VISIBLE);
        
        
        if( mPerson.getAboutMe().equals(""))
            aboutMeRl.setVisibility(View.GONE);
        else
        {
            aboutMeTv.setText(mPerson.getAboutMe());
        }

        if( mPerson.getOccupation().equals(""))
            occupationRl.setVisibility(View.GONE);
        else
            occupationTv.setText(mPerson.getOccupation());
        
        if( mPerson.getEducation().equals(""))
            educationRl.setVisibility(View.GONE);
        else
            educationTv.setText(mPerson.getEducation());
        
        
        if( mPerson.getInterests().equals(""))
            interestsRl.setVisibility(View.GONE);
        else
            interestsTv.setText(mPerson.getInterests());
        
        if( mPerson.getActivities().equals(""))
            activitiesRl.setVisibility(View.GONE);
        else
            activitiesTv.setText(mPerson.getActivities());
        
        if( mPerson.getTravel().equals(""))
            travelRl.setVisibility(View.GONE);
        else
            travelTv.setText(mPerson.getTravel());
        
        
        if( mPerson.getMusic().equals(""))
            favoriteMusicRl.setVisibility(View.GONE);
        else
            favoriteMusicTv.setText(mPerson.getMusic());
        
        if( mPerson.getDrinks().equals(""))
            favoriteDrinksRl.setVisibility(View.GONE);
        else
            favoriteDrinksTv.setText(mPerson.getDrinks());

        if( mPerson.getMyPerfectNightOut().equals(""))
            myPerfectNightOutRl.setVisibility(View.GONE);
        else
            myPerfectNightOutTv.setText(mPerson.getMyPerfectNightOut());
        
		
		
		
		// ACTIVITY
		
        ArrayList<Place> topFrequentedBars = mPerson.getTopFrequentedBars();
        ArrayList<BarStars> collectedBarStars = mPerson.getCollectedBarStars();
        
        LinearLayout statsLayout = (LinearLayout)view.findViewById(R.id.stats_layout);
        if( topFrequentedBars.size() == 0 && collectedBarStars.size() == 0)
            statsLayout.setVisibility(View.GONE);
        else
            statsLayout.setVisibility(View.VISIBLE);
            
        TextView topFrequentedBarsSeparator = (TextView)view.findViewById(R.id.top_frequented_bars_separator);
        TextView topFrequentedBarTv1 = (TextView)view.findViewById(R.id.top_frequented_bar1_text);
        TextView topFrequentedBarTv2 = (TextView)view.findViewById(R.id.top_frequented_bar2_text);
        TextView topFrequentedBarTv3 = (TextView)view.findViewById(R.id.top_frequented_bar3_text); 
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
        
		
        DisplayMetrics metrics = new DisplayMetrics();
        getSherlockActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int maxWidth = metrics.widthPixels - 2*(int)Utility.convertDpToPixel(15, c);
        
				
		// APPROACH
        mApproachLayout.removeAllViews();
        Resources r = getResources();

        if( !mPerson.tipComeAndSayHi() && !mPerson.tipBuyMeADrink() && !mPerson.tipInviteMeToDance() && !mPerson.tipMakeMeLaugh() && !mPerson.tipMeetMyFriends() &&
                !mPerson.tipSurpriseMe() && !mPerson.dontBeDrunk() && !mPerson.dontExpectAnything() && !mPerson.dontBePersistent() && mPerson.getPersonalAdvice().equals("")  )
                mApproachLayout.setVisibility(View.GONE);
        else
        {
            mApproachLayout.setVisibility(View.VISIBLE);
                
            LinearLayout llHorizontal = new LinearLayout(getSherlockActivity());
            llHorizontal.setOrientation(LinearLayout.HORIZONTAL);
            llHorizontal.setGravity(Gravity.CENTER);
            
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
		    
		/*
		if( mPerson.tipBuyMeADrink() )
		   ;
		
		if( mPerson.tipInviteMeToDance()  )
		    ;   
		    
		    !mPerson.tipSurpriseMe() && !mPerson.dontUsePickupLines() && !mPerson.dontCornerMe() 
		        && !mPerson.dontBePersistent() && !mPerson.dontBeDrunk() && mPerson.getExtraAdvice().equals("") 
				&& !mPerson.dontApproach() )
			mApproachLayout.setVisibility(View.GONE);
		else
		    mApproachLayout.setVisibility(View.VISIBLE);
*/
	
		// SIGNALS OR MEETUP
		
        
        User u = User.getInstance();
        
        if( mForInvitation )
        {
            mInvitationRow.setVisibility(View.VISIBLE);
            mSignalsRow.setVisibility(View.GONE);

            // Drawables
            Drawable drawable1 = getResources().getDrawable(R.drawable.circlex);
            Drawable drawable2 = getResources().getDrawable(R.drawable.drinks);
            float scaleFactor1 = Utility.convertDpToPixel(20, getSherlockActivity())/drawable1.getIntrinsicHeight();
            float scaleFactor2 = Utility.convertDpToPixel(20, getSherlockActivity())/drawable2.getIntrinsicHeight();
            
            switch (getActivity().getResources().getConfiguration().orientation ) 
            {
                case Configuration.ORIENTATION_LANDSCAPE: 
                    scaleFactor1 = Utility.convertDpToPixel(15, getSherlockActivity())/drawable1.getIntrinsicHeight();
                    scaleFactor2 = Utility.convertDpToPixel(15, getSherlockActivity())/drawable2.getIntrinsicHeight();
                    break;
            }
            
            drawable1.setBounds(0, 0, (int)(drawable1.getIntrinsicWidth()*scaleFactor1), 
                    (int)(drawable1.getIntrinsicHeight()*scaleFactor1));
            
            drawable2.setBounds(0, 0, (int)(drawable2.getIntrinsicWidth()*scaleFactor2), 
                    (int)(drawable2.getIntrinsicHeight()*scaleFactor2));
            
            switch (getActivity().getResources().getConfiguration().orientation ) 
            {
                case Configuration.ORIENTATION_LANDSCAPE: 
                    
                    mInvitationBtn.setCompoundDrawables(drawable1, null, null, null);
                    mNoThanksBtn.setCompoundDrawables(drawable2, null, null, null);
                    break;
                case Configuration.ORIENTATION_PORTRAIT:
                    
                    mInvitationBtn.setCompoundDrawables(null, drawable2, null, null);
                    mNoThanksBtn.setCompoundDrawables(null, drawable1, null, null); 
                    break;
            }
            
            
            // Backgrounds
            mInvitationBtn.measure(0, 0);
            mNoThanksBtn.measure(0, 0);
            
            LayerDrawable btnBlackLayerList1 = (LayerDrawable)getResources().getDrawable(R.drawable.btn_black_square_background).mutate();
            LayerDrawable btnBlackLayerList2 = (LayerDrawable)getResources().getDrawable(R.drawable.btn_black_square_background).mutate();
            
            btnBlackLayerList1.setLayerInset(1, (int)Utility.convertDpToPixel(1, c), (int)Utility.convertDpToPixel(1, c), (int)Utility.convertDpToPixel(1, c), mInvitationBtn.getMeasuredHeight()/2);
            btnBlackLayerList2.setLayerInset(1, (int)Utility.convertDpToPixel(1, c), (int)Utility.convertDpToPixel(1, c), (int)Utility.convertDpToPixel(1, c), mNoThanksBtn.getMeasuredHeight()/2);
            
            StateListDrawable states1 = new StateListDrawable();
            states1.addState(new int[] {android.R.attr.state_pressed},getResources().getDrawable(R.drawable.btn_black_square_background_pressed));
            states1.addState(new int[] {android.R.attr.state_focused},getResources().getDrawable(R.drawable.btn_black_square_background_pressed));
            states1.addState(new int[] { },btnBlackLayerList1);
            
            StateListDrawable states2 = new StateListDrawable();
            states2.addState(new int[] {android.R.attr.state_pressed},getResources().getDrawable(R.drawable.btn_black_square_background_pressed));
            states2.addState(new int[] {android.R.attr.state_focused},getResources().getDrawable(R.drawable.btn_black_square_background_pressed));
            states2.addState(new int[] { },btnBlackLayerList2);
            
            mInvitationBtn.setBackground(states1);
            mNoThanksBtn.setBackground(states2);
            
            padding = (int)Utility.convertDpToPixel(5, c);
            mNoThanksBtn.setPadding(padding, padding, padding, padding);
            mInvitationBtn.setPadding(padding, padding, padding, padding);
        }
        else
        {
            mInvitationRow.setVisibility(View.GONE);
            
            if( (u.getInterestedIn().equals(mPerson.getSex()) || u.getInterestedIn().equals(Person.INTERESTED_IN_BOTH)) && 
                    (mPerson.getInterestedIn().equals(u.getSex()) || mPerson.getInterestedIn().equals(Person.INTERESTED_IN_BOTH)) &&
                    !mPerson.equals(User.getInstance()) )
            {
                mSignalsRow.setVisibility(View.VISIBLE);
                mReportLayout.setVisibility(View.VISIBLE);
  

                Place mePlace = User.getInstance().getCheckPlace();
                Place youPlace = mPerson.getCheckPlace();

                Resources res = getResources();     
                float logicalDensity = metrics.density;
                int px = (int) (5 * logicalDensity + 0.5);
                
                
                
                mFlirtBtn.setAlpha(1.0f);
                mMessageBtn.setAlpha(1.0f);
                mVoteBtn.setAlpha(1.0f);
                
                if( mePlace != null && youPlace != null && mePlace.equals(youPlace))
                {
                    if( mPerson.dontApproach() )
                    {
                        mFlirtBtn.setBackground(res.getDrawable(R.drawable.btn_grey));
                        mFlirtBtn.setAlpha(0.6f);
                        mFlirtBtn.setTextColor(res.getColor(R.color.white));
                        mFlirtBtn.setText(res.getString(R.string.flirt));
                        mFlirtBtn.setEnabled(true);
                    }
                    else if( mPerson.flirted() )
                    {
                        mFlirtBtn.setBackground(res.getDrawable(R.drawable.btn_black_square_white_border_pressed));
                        mFlirtBtn.setTextColor(res.getColor(R.color.white));
                        mFlirtBtn.setText(res.getString(R.string.flirted));
                        mFlirtBtn.setEnabled(false);
                    }
                    else
                    {
                        mFlirtBtn.setBackground(res.getDrawable(R.drawable.btn_black_square));
                        mFlirtBtn.setTextColor(res.getColor(R.color.citrus_red));
                        mFlirtBtn.setText(res.getString(R.string.flirt));
                        mFlirtBtn.setEnabled(true);
                    }
                    
                    if( mPerson.dontApproach() )
                    {
                        mMessageBtn.setBackground(res.getDrawable(R.drawable.btn_grey));
                        mMessageBtn.setAlpha(0.6f);
                        mMessageBtn.setTextColor(res.getColor(R.color.white));
                        mMessageBtn.setText(res.getString(R.string.message));
                        mMessageBtn.setEnabled(true);
                    }
                    else
                    {
                        mMessageBtn.setBackground(res.getDrawable(R.drawable.btn_black_square));
                        mMessageBtn.setTextColor(res.getColor(R.color.citrus_orange));
                        mMessageBtn.setText(res.getString(R.string.message));
                        mMessageBtn.setEnabled(true);
                    }
                    
                    if( mPerson.voted() )
                    {
                        mVoteBtn.setBackground(res.getDrawable(R.drawable.btn_black_square_white_border_pressed));
                        mVoteBtn.setTextColor(res.getColor(R.color.white));
                        mVoteBtn.setText(res.getString(R.string.voted));
                        mVoteBtn.setEnabled(false);
                    }
                    else if( !mPerson.canBeVoted() )
                        mVoteBtn.setEnabled(true);
                    else
                    {
                        mVoteBtn.setBackground(res.getDrawable(R.drawable.btn_black_square));
                        mVoteBtn.setTextColor(res.getColor(R.color.citrus_green));
                        mVoteBtn.setText(res.getString(R.string.vote));
                        mVoteBtn.setEnabled(true);
                    }
                }
                else
                {
                    mFlirtBtn.setBackground(res.getDrawable(R.drawable.btn_grey));
                    mFlirtBtn.setAlpha(0.6f);
                    mFlirtBtn.setTextColor(res.getColor(R.color.white));
                    mFlirtBtn.setText(res.getString(R.string.flirt));
                    mFlirtBtn.setEnabled(true);
                    
                    mMessageBtn.setBackground(res.getDrawable(R.drawable.btn_grey));
                    mMessageBtn.setAlpha(0.6f);
                    mMessageBtn.setTextColor(res.getColor(R.color.white));
                    mMessageBtn.setText(res.getString(R.string.message));
                    mMessageBtn.setEnabled(true);
                    
                    mVoteBtn.setBackground(res.getDrawable(R.drawable.btn_grey));
                    mVoteBtn.setAlpha(0.6f);
                    mVoteBtn.setTextColor(res.getColor(R.color.white));
                    mVoteBtn.setText(res.getString(R.string.vote));
                    mVoteBtn.setEnabled(true);
                }
                
                mFlirtBtn.setPadding(px,px,px,px);
                mMessageBtn.setPadding(px,px,px,px);
                mVoteBtn.setPadding(px,px,px,px);
                
                
                
                // Drawables
                float scaleFactor1, scaleFactor2, scaleFactor3;
                
                Drawable drawable1 = getResources().getDrawable(R.drawable.signal_flirt);
                Drawable drawable2 = getResources().getDrawable(R.drawable.signal_message);
                Drawable drawable3 = getResources().getDrawable(R.drawable.signal_vote);
                
                scaleFactor1 = Utility.convertDpToPixel(20, getSherlockActivity())/drawable1.getIntrinsicHeight();
                scaleFactor2 = Utility.convertDpToPixel(20, getSherlockActivity())/drawable2.getIntrinsicHeight();
                scaleFactor3 = Utility.convertDpToPixel(20, getSherlockActivity())/drawable3.getIntrinsicHeight();
    
                
                switch (getActivity().getResources().getConfiguration().orientation ) 
                {
                    case Configuration.ORIENTATION_LANDSCAPE: 
                        
                        scaleFactor1 = Utility.convertDpToPixel(20, getSherlockActivity())/drawable1.getIntrinsicHeight();
                        scaleFactor2 = Utility.convertDpToPixel(20, getSherlockActivity())/drawable2.getIntrinsicHeight();
                        scaleFactor3 = Utility.convertDpToPixel(20, getSherlockActivity())/drawable3.getIntrinsicHeight();
                        drawable1.setBounds(0, 0, (int)(drawable1.getIntrinsicWidth()*scaleFactor1), (int)(drawable1.getIntrinsicHeight()*scaleFactor1));
                        drawable2.setBounds(0, 0, (int)(drawable2.getIntrinsicWidth()*scaleFactor2), (int)(drawable2.getIntrinsicHeight()*scaleFactor2));
                        drawable3.setBounds(0, 0, (int)(drawable3.getIntrinsicWidth()*scaleFactor3), (int)(drawable3.getIntrinsicHeight()*scaleFactor3));   
                        mFlirtBtn.setCompoundDrawables(drawable1, null, null, null);
                        mMessageBtn.setCompoundDrawables(drawable2, null, null, null);
                        mVoteBtn.setCompoundDrawables(drawable3, null, null, null);
                        break;
                    case Configuration.ORIENTATION_PORTRAIT:
                        
                        scaleFactor1 = Utility.convertDpToPixel(30, getSherlockActivity())/drawable1.getIntrinsicHeight();
                        scaleFactor2 = Utility.convertDpToPixel(30, getSherlockActivity())/drawable2.getIntrinsicHeight();
                        scaleFactor3 = Utility.convertDpToPixel(30, getSherlockActivity())/drawable3.getIntrinsicHeight();
                        drawable1.setBounds(0, 0, (int)(drawable1.getIntrinsicWidth()*scaleFactor1), (int)(drawable1.getIntrinsicHeight()*scaleFactor1));
                        drawable2.setBounds(0, 0, (int)(drawable2.getIntrinsicWidth()*scaleFactor2), (int)(drawable2.getIntrinsicHeight()*scaleFactor2));
                        drawable3.setBounds(0, 0, (int)(drawable3.getIntrinsicWidth()*scaleFactor3), (int)(drawable3.getIntrinsicHeight()*scaleFactor3));   
                        mFlirtBtn.setCompoundDrawables(null, drawable1, null, null);
                        mMessageBtn.setCompoundDrawables(null, drawable2, null, null);
                        mVoteBtn.setCompoundDrawables(null, drawable3, null, null);
                        break;
                }
                
                // Backgrounds
                
                mFlirtBtn.measure(0, 0);
                mMessageBtn.measure(0, 0);
                mVoteBtn.measure(0, 0);
                
                LayerDrawable btnBlackLayerList1 = (LayerDrawable)getResources().getDrawable(R.drawable.btn_black_square_background).mutate();
                LayerDrawable btnBlackLayerList2 = (LayerDrawable)getResources().getDrawable(R.drawable.btn_black_square_background).mutate();
                LayerDrawable btnBlackLayerList3 = (LayerDrawable)getResources().getDrawable(R.drawable.btn_black_square_background).mutate();
                
                if( !mPerson.flirted() )
                {
                    btnBlackLayerList1.setLayerInset(1, (int)Utility.convertDpToPixel(1, c), (int)Utility.convertDpToPixel(1, c), (int)Utility.convertDpToPixel(1, c), mFlirtBtn.getMeasuredHeight()/2);
                    StateListDrawable states1 = new StateListDrawable();
                    states1.addState(new int[] {android.R.attr.state_pressed},getResources().getDrawable(R.drawable.btn_black_square_background_pressed));
                    states1.addState(new int[] {android.R.attr.state_focused},getResources().getDrawable(R.drawable.btn_black_square_background_pressed));
                    states1.addState(new int[] { },btnBlackLayerList1);
                    
                    mFlirtBtn.setBackground(states1);
                }
                
                btnBlackLayerList2.setLayerInset(1, (int)Utility.convertDpToPixel(1, c), (int)Utility.convertDpToPixel(1, c), (int)Utility.convertDpToPixel(1, c), mMessageBtn.getMeasuredHeight()/2);
                StateListDrawable states2 = new StateListDrawable();
                states2.addState(new int[] {android.R.attr.state_pressed},getResources().getDrawable(R.drawable.btn_black_square_background_pressed));
                states2.addState(new int[] {android.R.attr.state_focused},getResources().getDrawable(R.drawable.btn_black_square_background_pressed));
                states2.addState(new int[] { },btnBlackLayerList2);
                mMessageBtn.setBackground(states2);
                
                if( !mPerson.voted() )
                {
                    btnBlackLayerList3.setLayerInset(1, (int)Utility.convertDpToPixel(1, c), (int)Utility.convertDpToPixel(1, c), (int)Utility.convertDpToPixel(1, c), mVoteBtn.getMeasuredHeight()/2);
                    StateListDrawable states3 = new StateListDrawable();
                    states3.addState(new int[] {android.R.attr.state_pressed},getResources().getDrawable(R.drawable.btn_black_square_background_pressed));
                    states3.addState(new int[] {android.R.attr.state_focused},getResources().getDrawable(R.drawable.btn_black_square_background_pressed));
                    states3.addState(new int[] { },btnBlackLayerList3);
                    
                    mVoteBtn.setBackground(states3);
                }
                
                padding = (int)Utility.convertDpToPixel(5, c);
                mFlirtBtn.setPadding(padding, padding, padding, padding);
                mMessageBtn.setPadding(padding, padding, padding, padding);
                mVoteBtn.setPadding(padding, padding, padding, padding);
            }
            else
            {
                mSignalsRow.setVisibility(View.GONE);
                mReportLayout.setVisibility(View.GONE);
            }
            
        }
        
        
        // REPORT AND BLOCK
        String reportTextStr = getResources().getString(R.string.report);
        SpannableString content = new SpannableString(reportTextStr);
        content.setSpan(new UnderlineSpan(), 0, reportTextStr.length(), 0);
        mReportTv.setText(content);
        
        String blockText = "";
        if( mPerson.blocked())
            blockText = getResources().getString(R.string.unblock);
        else
            blockText = getResources().getString(R.string.block);
        
        content = new SpannableString(blockText);
        content.setSpan(new UnderlineSpan(), 0, blockText.length(), 0);
        mBlockTv.setText(content);
        
        
		
		// SHOW
		
		RelativeLayout rootLayout = (RelativeLayout)getView().findViewById(R.id.root_layout);
		rootLayout.setVisibility(View.VISIBLE);
	}
	
	
	
	
	void sendReport( int reportType, String comments )
	{
		mReportTaskMethod.doTask(User.getInstance().getUserId(), mPerson.getUserId(), reportType, comments);
	}
	
	
	public Person getPerson()
	{
	    return mPerson;
	}
	
	// EVENTS
	
    private void onClickPhoto() 
    {
        ArrayList<String> urls = new ArrayList<String>();
        ArrayList<String> profilePhotoFilenames = mPerson.getProfilePhotosFilenames();
        ArrayList<String> otherPhotoFilenames = mPerson.getOtherPhotosFilenames();
        
        for( String filename : profilePhotoFilenames )
        {
            urls.add("http://s3.amazonaws.com/signals/user_images/" + mPerson.getUserId() + "/profilePhotos/" + filename);
        }
        
        for( String filename : otherPhotoFilenames )
        {
            urls.add("http://s3.amazonaws.com/signals/user_images/" + mPerson.getUserId() + "/otherPhotos/" + filename);
        }
        
        PhotosPagerFragment frag = PhotosPagerFragment.newInstance(urls,0); 
        FragmentTransaction ft = getSherlockActivity().getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.push_right_in, R.anim.push_right_out);
        
        ft.replace(R.id.content_frame, frag)
        .addToBackStack(null)
        .commit();
    }
	
	
	
	private void onClickFlirt()
	{
	    Place mePlace = User.getInstance().getCheckPlace();
	    Place youPlace = mPerson.getCheckPlace();

        if( youPlace == null || mePlace == null || !mePlace.getPlaceId().equals(youPlace.getPlaceId()) )
        {
            Toast.makeText(getSherlockActivity(), getString(R.string.error_you_have_to_be_in_same_bar), Toast.LENGTH_LONG).show();
        }
        else if( mPerson.dontApproach() )
        {
            Toast.makeText(getSherlockActivity(), mPerson.getUsername() + " " + getString(R.string.error_dont_bother), Toast.LENGTH_LONG).show(); 
        }
	    else if( mPerson.blocked() )
		{
	        Toast.makeText(getSherlockActivity(), getString(R.string.error_blocked_user), Toast.LENGTH_LONG).show();
		}
		else
		{
		    mFlirtTaskMethod.doTask(User.getInstance().getUserId(), mPerson.getUserId(), User.getInstance().getCheckPlace().getPlaceId());
		}
			

	}
	
	private void onClickMessage()
	{
        Place mePlace = User.getInstance().getCheckPlace();
        Place youPlace = mPerson.getCheckPlace();

        if( youPlace == null || mePlace == null || !mePlace.getPlaceId().equals(youPlace.getPlaceId()) )
        {
            Toast.makeText(getSherlockActivity(), getString(R.string.error_you_have_to_be_in_same_bar), Toast.LENGTH_LONG).show();
        }
        else if( mPerson.dontApproach() )   
        {
            Toast.makeText(getSherlockActivity(), mPerson.getUsername() + " " + getString(R.string.error_dont_bother), Toast.LENGTH_LONG).show();
        }
        else if( mPerson.blocked() )
		{
            Toast.makeText(getSherlockActivity(), getString(R.string.error_blocked_user), Toast.LENGTH_LONG).show();
		}
		else
		{
            MessagesChatFragment frag = MessagesChatFragment.newInstance( mPerson );
        
            getSherlockActivity().getSupportFragmentManager()   
            .beginTransaction()
            .replace(R.id.content_frame, frag) 
            .addToBackStack(null)
            .commit();	    
		}
			
	}
	
	private void onClickVote()
	{
        Place mePlace = User.getInstance().getCheckPlace();
        Place youPlace = mPerson.getCheckPlace();

        if( youPlace == null || mePlace == null || !mePlace.getPlaceId().equals(youPlace.getPlaceId()) )
        {
            Toast.makeText(getSherlockActivity(), getString(R.string.error_you_have_to_be_in_same_bar), Toast.LENGTH_LONG).show();
        }
        else if( mPerson.blocked() )
		{
		    Toast.makeText(getSherlockActivity(), getString(R.string.error_blocked_user), Toast.LENGTH_LONG).show();  
		}
		else
		{
            mVoteTaskMethod.doTask(User.getInstance().getUserId(), mPerson.getUserId(), User.getInstance().getCheckPlace().getPlaceId());		    
		}
			
	}
	
	
	private void onClickNoThanks()
	{
	    mNoThanksTaskMethod.doTask(User.getInstance().getUserId(), mPerson.getUserId());
	}
	
	private void onClickInvitation()
    {
	    mInvitationTaskMethod.doTask(User.getInstance().getUserId(), mPerson);
    }
	
	
	private void onClickReport()
	{
		ReportDialogFragment.newInstance(this).show(getSherlockActivity().getSupportFragmentManager(), "reportDialog");
	}
	
	private void onClickBlock()
	{
		if( !mPerson.blocked() )
		{
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
            
            alertDialogBuilder.setTitle(getActivity().getString(R.string.block_user));
 
            alertDialogBuilder
                .setMessage(getActivity().getString(R.string.are_you_sure_you_want_to_block_this_user))
                .setCancelable(false)
                .setPositiveButton(getActivity().getString(R.string.block),new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {

                        mBlockTaskMethod.doTask(User.getInstance().getUserId(), mPerson.getUserId());
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
		}
		else
			mUnblockTaskMethod.doTask(User.getInstance().getUserId(), mPerson.getUserId());
	}




	



	@Override
	public void flirtTaskEventReceived(FlirtTaskEvent evt) {
		FlirtTaskMethod met = (FlirtTaskMethod)evt.getSource();
		
		if( met != null && met.success() )
		{
			mPerson.setFlirted(true);
			
			Resources res = getSherlockActivity().getResources();
			mFlirtBtn.setBackground(res.getDrawable(R.drawable.btn_black_square_white_border_pressed));
            mFlirtBtn.setTextColor(res.getColor(R.color.white));
            mFlirtBtn.setText(res.getString(R.string.flirted));
            mFlirtBtn.setEnabled(false);
		}
		else
		{
			int error = met.getError();
			
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
			AlertDialog alertDialog;
			
			switch( error )
			{
			case FlirtTaskMethod.ERROR_FLIRT_TOO_RECENT:
				Toast.makeText(getSherlockActivity(), getString(R.string.error_flirt_too_recent), Toast.LENGTH_LONG).show();
				break;
				
			case FlirtTaskMethod.ERROR_FLIRT_PERSON_CHECKED_OUT:
			    if( User.getInstance().checkedIn())
                {
                    alertDialogBuilder = new AlertDialog.Builder(getActivity());
                    alertDialogBuilder.setTitle(getActivity().getString(R.string.user_checked_out));
         
                    alertDialogBuilder
                        .setMessage(getString(R.string.error_checked_out1) + " " + mPerson.getUsername() + " " + getString(R.string.error_checked_out2) + " " + User.getInstance().getCheckPlace().getName() + ".")
                        .setCancelable(false)
                        .setPositiveButton(getActivity().getString(R.string.ok),new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {

                                mPerson.setCheckPlace(null);
                                updateView();
                            }
                          });
         
                    alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
                
                mPerson.setCheckPlace(null);
				break;
				
			case FlirtTaskMethod.ERROR_FLIRT_PERSON_DONT_APPROACH:
                alertDialogBuilder.setTitle(getActivity().getString(R.string.user_changed_status));
     
                alertDialogBuilder
                    .setMessage(mPerson.getUsername() + " " + getString(R.string.error_dont_bother))
                    .setCancelable(false)
                    .setPositiveButton(getActivity().getString(R.string.ok),new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            mPerson.setDontApproach(true);
                            updateView();
                        }
                      });
     
                alertDialog = alertDialogBuilder.create();
                alertDialog.show();
			    break;
			    
			case FlirtTaskMethod.ERROR_FLIRT_PERSON_BLOCKED_YOU:
                
                alertDialogBuilder.setTitle(getActivity().getString(R.string.user_unavailable));
     
                alertDialogBuilder
                    .setMessage(mPerson.getUsername() + " " + getString(R.string.is_unavailable))
                    .setCancelable(false)
                    .setPositiveButton(getActivity().getString(R.string.ok),new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            mPerson.setDontApproach(true);
                            mHandler.postDelayed(mRunnable, 1);
                        }
                      });
     
                alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                break;
                
                
			case FlirtTaskMethod.ERROR_FLIRT_PROCESS:
				Toast.makeText(getSherlockActivity(), getString(R.string.error_flirt_process), Toast.LENGTH_LONG).show();
				break;
			}
		}
	}
	
	


	@Override
	public void voteTaskEventReceived(VoteTaskEvent evt) 
	{
		VoteTaskMethod met = (VoteTaskMethod)evt.getSource();
		
		if( met != null && met.success() )
		{
			mPerson.setVoted(true);
			
			Resources res = getSherlockActivity().getResources();
			mVoteBtn.setBackground(res.getDrawable(R.drawable.btn_black_square_white_border_pressed));
            mVoteBtn.setTextColor(res.getColor(R.color.white));
            mVoteBtn.setText(res.getString(R.string.voted));
            mVoteBtn.setEnabled(false);
		}
		else
		{
			int error = met.getError();
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
            AlertDialog alertDialog;
            
			switch( error )
			{
			case VoteTaskMethod.ERROR_VOTE_TOO_RECENT:
				String errTxt = getString(R.string.error_vote_too_recent);
				
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.HOUR, 1);
				
				errTxt += " " + cal.get(Calendar.HOUR) + ":00 ";
				
    			if (cal.get(Calendar.AM_PM) == Calendar.PM) 
    				errTxt += getResources().getString(R.string.pm);
    			else
    				errTxt += getResources().getString(R.string.am);
    			
				Toast.makeText(getSherlockActivity(), errTxt, Toast.LENGTH_LONG).show();
				break;
				
			case VoteTaskMethod.ERROR_VOTE_PERSON_CHECKED_OUT:
			    
			    if( User.getInstance().checkedIn())
			    {
			        alertDialogBuilder = new AlertDialog.Builder(getActivity());
		            alertDialogBuilder.setTitle(getActivity().getString(R.string.user_checked_out));
		 
		            alertDialogBuilder
		                .setMessage(getString(R.string.error_checked_out1) + " " + mPerson.getUsername() + " " + getString(R.string.error_checked_out2) + " " + User.getInstance().getCheckPlace().getName() + ".")
		                .setCancelable(false)
		                .setPositiveButton(getActivity().getString(R.string.ok),new DialogInterface.OnClickListener() {
		                    public void onClick(DialogInterface dialog,int id) {

		                        mPerson.setCheckPlace(null);
                                updateView();
		                    }
		                  });
		 
		            alertDialog = alertDialogBuilder.create();
		            alertDialog.show();
			    }
			    
				mPerson.setCheckPlace(null);
				
				break;
				
			case VoteTaskMethod.ERROR_VOTE_PERSON_BLOCKED_YOU:
                
                alertDialogBuilder.setTitle(getActivity().getString(R.string.user_unavailable));
     
                alertDialogBuilder
                    .setMessage(mPerson.getUsername() + " " + getString(R.string.is_unavailable))
                    .setCancelable(false)
                    .setPositiveButton(getActivity().getString(R.string.ok),new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            mPerson.setDontApproach(true);
                            mHandler.postDelayed(mRunnable, 1);
                        }
                      });
     
                alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                break;
                
			case VoteTaskMethod.ERROR_VOTE_PROCESS:
				Toast.makeText(getSherlockActivity(), getString(R.string.error_vote_process), Toast.LENGTH_LONG).show();
				break;
			}
		}
	}

	





	@Override
	public void reportTaskEventReceived(ReportTaskEvent evt) 
	{
		ReportTaskMethod met = (ReportTaskMethod)evt.getSource();
		
		if( met != null && met.success() )
		{
	    	// close dialog
			ReportDialogFragment frag = (ReportDialogFragment) getSherlockActivity().getSupportFragmentManager().findFragmentByTag("reportDialog");
			
			if( frag != null )
				frag.getDialog().dismiss();
			
			Toast.makeText(getSherlockActivity(), getString(R.string.report_submitted), Toast.LENGTH_LONG).show();
		} 
		
	}



	@Override
	public void blockTaskEventReceived(BlockTaskEvent evt) 
	{
		BlockTaskMethod met = (BlockTaskMethod)evt.getSource();
		
		if( met != null && met.success() )
		{
			mPerson.setBlocked(true);
			
		    String blockText = getResources().getString(R.string.unblock);
	        SpannableString content = new SpannableString(blockText);
	        content.setSpan(new UnderlineSpan(), 0, blockText.length(), 0);
	        mBlockTv.setText(content);
		}	
		
	}


	@Override
	public void unblockTaskEventReceived(UnblockTaskEvent evt) {
		UnblockTaskMethod met = (UnblockTaskMethod)evt.getSource();
		
		if( met != null && met.success() )
		{
			mPerson.setBlocked(false);
			
            String blockText = getResources().getString(R.string.block);
            SpannableString content = new SpannableString(blockText);
            content.setSpan(new UnderlineSpan(), 0, blockText.length(), 0);
            mBlockTv.setText(content);
		}	
		
	}

    @Override
    public void noThanksTaskEventReceived(NoThanksTaskEvent evt) 
    {
        NoThanksTaskMethod met = (NoThanksTaskMethod)evt.getSource();
        
        if( met != null && met.success() )
        {
            mPerson.setNoThanksFlagged(true);
            mNoThanksBtn.setBackground(getResources().getDrawable(R.drawable.btn_black_square_white_border_pressed));
            
            mHandler.postDelayed(mRunnable, 1);
        }
    }


    @Override
    public void invitationTaskEventReceived(InvitationTaskEvent evt) {
        InvitationTaskMethod met = (InvitationTaskMethod)evt.getSource();
        
        if( met != null && met.success() )
        {
            mPerson.setInvitationSent(true);
            mInvitationBtn.setBackground(getResources().getDrawable(R.drawable.btn_black_square_white_border_pressed));
            mInvitationBtn.setTextColor(getResources().getColor(R.color.white));
            mHandler.postDelayed(mRunnable, 1);
            
            if( met.getPotentialRendezvous() != null )
            {
                ArrayList<PotentialRendezvous> potentialInvitations = User.getInstance().getPotentialRendezvous(); 
                potentialInvitations.add(met.getPotentialRendezvous());
                User.getInstance().setPotentialRendezvous(potentialInvitations);
                
                
                // Alert
                MainActivity act = (MainActivity)getSherlockActivity();
                
                if(!act.getSlidingMenu().isMenuShowing())
                {
                    ActionBar actionBar = getSherlockActivity().getSupportActionBar();
                    final AnimationDrawable homeDrawable = (AnimationDrawable) getResources().getDrawable(R.drawable.anim_logo);
                    actionBar.setIcon(homeDrawable);
                    
                    getSherlockActivity().getWindow().getDecorView().post(new Runnable() {
                        @Override public void run() {
                          homeDrawable.start();
                        }
                    });
                }
            }
        }
    }
	    


	
}
