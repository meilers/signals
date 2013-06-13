package com.signals_app.signals.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.actionbarsherlock.app.SherlockFragment;
import com.signals_app.signals.R;

/**
 * Created by omegatai on 2013-05-24.
 */
public class Intro3Fragment extends SherlockFragment
{

    public static final Intro3Fragment newInstance()
    {
        Intro3Fragment f = new Intro3Fragment();

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
        View view = inflater.inflate(R.layout.fragment_intro3, container, false);
        getSherlockActivity().getActionBar().setSubtitle(getString(R.string.Fun_Stuff));


        return view;
    }

}
