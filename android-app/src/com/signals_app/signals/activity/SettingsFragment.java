package com.signals_app.signals.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONObject;

import com.actionbarsherlock.app.SherlockFragment;
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
import com.signals_app.signals.tasks.MyProfileEditTonightTaskMethod;
import com.signals_app.signals.tasks.MyProfileEditTonightTaskMethod.MyProfileEditTonightTaskEvent;
import com.signals_app.signals.tasks.MyProfileEditTonightTaskMethod.MyProfileEditTonightTaskListener;
import com.signals_app.signals.tasks.ProfilesTaskMethod;
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

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
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

import android.text.InputFilter;
import android.text.InputType;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;

public class SettingsFragment extends SherlockFragment
{
    
    private Person mPerson = null;
    private Button mVipBtn;
    
    
    
    public static final MyProfileFragment newInstance( Long personId, String personUsername )
    {
        MyProfileFragment f = new MyProfileFragment();
        
        // Get arguments passed in, if any
        Bundle args = f.getArguments();
        if (args == null) {
            args = new Bundle();
        }
        
        // Add parameters to the argument bundle
        args.putLong("personId", personId );
        args.putString("personUsername", personUsername );
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
    }
 
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        getSherlockActivity().setTitle(getArguments().getString("personUsername"));
        
        
        
        mVipBtn = (Button)view.findViewById(R.id.become_vip_btn);
        
        
        
        mVipBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickBecomeVip();
            }
        });

        
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
    } 

    


    
    
    

    
    
    // EVENTS
    
    
    private void onClickBecomeVip()
    {
    }
    
        


    
}
