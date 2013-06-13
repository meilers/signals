package com.signals_app.signals.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Random;

import com.actionbarsherlock.app.SherlockFragment;
import com.signals_app.signals.R;
import com.signals_app.signals.model.Profile.Person;
import com.signals_app.signals.tasks.ProfilesTaskMethod;
import com.signals_app.signals.tasks.ProfilesTaskMethod.ProfilesTaskEvent;
import com.signals_app.signals.tasks.ProfilesTaskMethod.ProfilesTaskListener;
import com.signals_app.signals.tasks.signals.FetchVisitsTaskMethod;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ProfilesPagerFragment extends SherlockFragment
{

    private ArrayList<Person> mPeople = null;
    
    private ViewPager mViewPager;
    private ProfilesPagerAdapter mProfilesPagerAdapter;
    private int mPos = 0;
    
    public static ProfilesPagerFragment newInstance(ArrayList<Person> people, int index) {

        ProfilesPagerFragment pageFragment = new ProfilesPagerFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("people", people);
        bundle.putInt("pos", index);
        pageFragment.setArguments(bundle);
        return pageFragment;
    }

    @Override  
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Bundle args = getArguments();
        mPeople = getArguments().getParcelableArrayList("people");
        mPos = args.getInt("pos");
    }

    // Called once because of setRetainInstance
    @Override  
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        
        
        // WIDGETS 
        View view = inflater.inflate(R.layout.fragment_profiles_pager, container, false);

        
        mProfilesPagerAdapter = new ProfilesPagerAdapter(this.getChildFragmentManager(), this, mPeople);
        mViewPager = (ViewPager)view.findViewById(R.id.viewpager);
        mViewPager.setAdapter(mProfilesPagerAdapter);
        mViewPager.setCurrentItem(mPos);
        mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) 
            {
                mPos = position;
                
                Person person = mPeople.get(mPos);
                getActivity().setTitle(person.getUsername());
                getSherlockActivity().getSupportActionBar().setSubtitle(person.checkedIn() ? person.getCheckPlace().getName() : null);
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {                
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {                
            }
        });
        
        Person person = mPeople.get(mPos);
        getActivity().setTitle(person.getUsername());
        getSherlockActivity().getSupportActionBar().setSubtitle(person.checkedIn() ? person.getCheckPlace().getName() : null);
        
        
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }
    
    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        

    } 
    
    @Override
    public void onDestroy()
    {
        super.onDestroy();
        
        /*
        // because we set setRetainInstance, we have to send the views to garbage collection ourselves
        mPeople = null;
        mViewPager = null;
        mProfilesPagerAdapter = null;
        */
    } 

    
    
    private static class ProfilesPagerAdapter extends FragmentStatePagerAdapter
    {
        private ArrayList<ProfileFragment> mViews = new ArrayList<ProfileFragment>();
        
        
        public ProfilesPagerAdapter(FragmentManager fm, ProfilesPagerFragment parentFrag, ArrayList<Person> people) {
            this(fm);
            
            for( int i=0; i<people.size(); ++i )
            {
                Person p = people.get(i);
                ProfileFragment f = ProfileFragment.newInstance(p, false);
                
                mViews.add(f);
            }
        }  

        public ProfilesPagerAdapter(FragmentManager fm ) {
            super(fm);
        }
        
        
        @Override
        public Fragment getItem(int index) { 

            return mViews.get(index);
        }  

        @Override 
        public int getItemPosition (Object object)
        {
          int index = mViews.indexOf (object);
          if (index == -1)
            return POSITION_NONE;
          else
            return index;
        }
        
        @Override 
        public int getCount() {

            return mViews.size();
        }
    }
    
    
    public void removeProfilePage( ProfileFragment f )
    {
        if( getView() != null && f != null )
        {
            Person personToRemove = f.getPerson();
                
            for( int i=0; i<mProfilesPagerAdapter.mViews.size(); ++i)
            { 
                Person p = mPeople.get(i);
                
                if( p.equals(personToRemove))
                {
                    removePage(i);
                    return;
                }
            }
        }
    }
    
    
    
    private void removePage(int position) {

        /*
        if (position == mViewPager.getCurrentItem()) {
            if(position == (mProfilesPagerAdapter.getCount()-1)) {
                mViewPager.setCurrentItem(position-1);
            } else
            {
                mViewPager.setCurrentItem(position+1);
            }
        }*/
        int newPos = 0;
        
        if( position < mProfilesPagerAdapter.mViews.size()-1 )
            newPos = position;
        else if( position == mProfilesPagerAdapter.mViews.size()-1 )
            newPos = position-1;
        
        mPeople.remove(position);
        mProfilesPagerAdapter.mViews.remove(position);
        
        if( mPeople.size() != 0 )
        {
            mViewPager.setAdapter(null);
            mViewPager.setAdapter(mProfilesPagerAdapter);
            mViewPager.setCurrentItem(newPos);
            
            Person person = mPeople.get(position == mPeople.size() ? position-1: position);
            getActivity().setTitle(person.getUsername());
            getSherlockActivity().getSupportActionBar().setSubtitle(person.checkedIn() ? person.getCheckPlace().getName() : null);
        }
        else
        {
            getActivity().getSupportFragmentManager().popBackStack();
            getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
        }
        
            
            

    }

}