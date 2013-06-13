package com.signals_app.signals.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;

import com.actionbarsherlock.view.Window;
import com.facebook.Session;
import com.signals_app.signals.R;
import com.signals_app.signals.model.City;
import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.PageIndicator;

import java.util.ArrayList;

public class RegisterActivity extends SherlockFragmentActivity
{

    private Register1Fragment mFrag1 = null;
    private Register2Fragment mFrag2 = null;
    private Register3Fragment mFrag3 = null;
    private Register4Fragment mFrag4 = null;



    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setTitle(R.string.sign_up);
        setContentView(R.layout.activity_register);


        if( savedInstanceState != null )
        {
            FragmentManager manager = getSupportFragmentManager();
            mFrag1 = (Register1Fragment) manager.getFragment(savedInstanceState, Register1Fragment.TAG);
            mFrag2 = (Register2Fragment) manager.getFragment(savedInstanceState, Register2Fragment.TAG);
            mFrag3 = (Register3Fragment) manager.getFragment(savedInstanceState, Register3Fragment.TAG);
            mFrag4 = (Register4Fragment) manager.getFragment(savedInstanceState, Register4Fragment.TAG);
        }
        else
        {
            Bundle bundle = getIntent().getExtras();

            mFrag1 = Register1Fragment.newInstance(bundle.getString("facebookId"), bundle.getString("email"), bundle.getString("firstName"), bundle.getString("sex"),
                    bundle.getStringArrayList("meetingSexes"), bundle.getString("birthday"), bundle.getString("city"),
                    bundle.getString("country"), (ArrayList<City>)bundle.getSerializable("cities"));

            mFrag2 = Register2Fragment.newInstance(63L, bundle.getString("facebookId"), bundle.getStringArrayList("fbPhotoUrls"));
            mFrag3 = Register3Fragment.newInstance();
            mFrag4 = Register4Fragment.newInstance();

            getSupportFragmentManager().beginTransaction()
            .add(mFrag1, Register1Fragment.TAG)
            .add(mFrag2, Register2Fragment.TAG)
            .add(mFrag3, Register3Fragment.TAG)
            .add(mFrag4, Register4Fragment.TAG)
            .replace(R.id.content_frame, mFrag2, "Register2Fragment")
            .commit();

        }

    }


    @Override
    public void onResume() {
        super.onResume();
    }


    // Called before child is created
    @Override
    public void onPause()
    {
        super.onPause();
    }


    // Called after child has been created
    @Override
    public void onStop()
    {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        FragmentManager manager = getSupportFragmentManager();
        manager.putFragment(outState, Register1Fragment.TAG, mFrag1);
        manager.putFragment(outState, Register2Fragment.TAG, mFrag2);
        manager.putFragment(outState, Register3Fragment.TAG, mFrag3);
        manager.putFragment(outState, Register4Fragment.TAG, mFrag4);

    }


    // EVENTS
    public void onClickContinue( SherlockFragment frag )
    {
        if( frag instanceof Register1Fragment )
        {
            Bundle bundle = getIntent().getExtras();
            mFrag2 = Register2Fragment.newInstance(63L, bundle.getString("facebookId"), bundle.getStringArrayList("fbPhotoUrls"));

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content_frame, mFrag2)
                    .commit();
        }
        else if( frag instanceof Register2Fragment )
        {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content_frame, mFrag3)
                    .commit();
        }
        else if( frag instanceof Register3Fragment )
        {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content_frame, mFrag4)
                    .commit();
        }
        else if( frag instanceof Register4Fragment )
        {

        }
    }






}
