package com.signals_app.signals.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.actionbarsherlock.app.SherlockFragment;
import com.signals_app.signals.R;
import com.signals_app.signals.activity.Intro2Fragment;
import com.signals_app.signals.tasks.LoginTaskMethod;

/**
 * Created by omegatai on 2013-05-24.
 */
public class Register4Fragment extends SherlockFragment
{
    public static final String TAG = "Register4Fragment";


    public static final Register4Fragment newInstance()
    {
        Register4Fragment f = new Register4Fragment();

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
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_intro2, container, false);

        return view;
    }

}
