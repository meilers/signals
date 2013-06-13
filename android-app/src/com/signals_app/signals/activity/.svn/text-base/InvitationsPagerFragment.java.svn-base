package com.signals_app.signals.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Random;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.signals_app.signals.R;
import com.signals_app.signals.model.Profile.Person;
import com.signals_app.signals.model.Profile.User;
import com.signals_app.signals.tasks.ProfilesTaskMethod;
import com.signals_app.signals.tasks.ProfilesTaskMethod.ProfilesTaskEvent;
import com.signals_app.signals.tasks.ProfilesTaskMethod.ProfilesTaskListener;
import com.signals_app.signals.tasks.rendezvous.CandidatesTaskMethod;
import com.signals_app.signals.tasks.rendezvous.CandidatesTaskMethod.CandidatesTaskEvent;
import com.signals_app.signals.tasks.rendezvous.CandidatesTaskMethod.CandidatesTaskListener;
import com.signals_app.signals.tasks.signals.FetchVisitsTaskMethod;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.TextView;

public class InvitationsPagerFragment extends SherlockFragment implements CandidatesTaskListener
{
    public static final int ACTION_REFRESH = 1;
    
    private CandidatesTaskMethod mCandidatesTaskMethod;  
    
    private ArrayList<Person> mPeople = null;
    
    private ViewPager mViewPager;
    private InvitationsPagerAdapter mInvitationsPagerAdapter;
    private int mPos = 0;
    
    private int mNbLetsInvitationReceived = 0;
    private int mNbFollowingPeople = 0;
    
    public static InvitationsPagerFragment newInstance() {

        InvitationsPagerFragment pageFragment = new InvitationsPagerFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("people", null);
        bundle.putInt("pos", 0);
        bundle.putInt("nbLetsInvitationReceived", 0);
        bundle.putInt("nbFollowingPeople", 0);
        pageFragment.setArguments(bundle);
        return pageFragment;
    }

    @Override  
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Bundle args = getArguments();
        
        if( savedInstanceState != null ) 
            args = savedInstanceState;
        
        
        mPeople = args.getParcelableArrayList("people");
        mPos = args.getInt("pos");
        mNbLetsInvitationReceived = args.getInt("nbLetsInvitationReceived");
        mNbFollowingPeople = args.getInt("nbFollowingPeople");
        
        mCandidatesTaskMethod = new CandidatesTaskMethod(getSherlockActivity());
        
    }

    // Called once because of setRetainInstance
    @Override  
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getActivity().setTitle(getString(R.string.rendezvous));
        getSherlockActivity().getSupportActionBar().setSubtitle(getString(R.string.tonight));
        
        
        // WIDGETS 
        View view = inflater.inflate(R.layout.fragment_invitations_pager, container, false);
        mViewPager = (ViewPager)view.findViewById(R.id.viewpager);
        mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) 
            {
                mPos = position;
                
                setTitles(position);
                
                if( position == 0 )
                {
                    setHasOptionsMenu(true);
                }
                else
                    setHasOptionsMenu(false);
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {                
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {                
            }
        });
        
        
        mCandidatesTaskMethod.addCandidatesTaskListener(this);
        
        if(mPeople == null)
            fetchCandidates();
        else
        {
            // update pager
            mInvitationsPagerAdapter = new InvitationsPagerAdapter(this.getChildFragmentManager(),  mPeople, mNbLetsInvitationReceived, mNbFollowingPeople);
            mViewPager.setAdapter(mInvitationsPagerAdapter);
            mViewPager.setCurrentItem(mPos);
            mInvitationsPagerAdapter.notifyDataSetChanged();
        }
        
        if( mPos == 0)
            setHasOptionsMenu(true);
        else
            setHasOptionsMenu(false);
        
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
        
        mCandidatesTaskMethod.removeCandidatesTaskListener(this);
        mCandidatesTaskMethod.cleanUp();
    } 
    
    @Override
    public void onDestroy()
    {
        super.onDestroy();
        
        /*
        // because we set setRetainInstance, we have to send the views to garbage collection ourselves
        mPeople = null;
        mViewPager = null;
        mInvitationsPagerAdapter = null;
        */
    } 

    @Override
    public void onSaveInstanceState(Bundle outState) 
    {
        super.onSaveInstanceState(outState);
        outState.putInt("pos", mPos);
        outState.putInt("nbLetsInvitationReceived", mNbLetsInvitationReceived);
        outState.putInt("nbFollowingPeople", mNbFollowingPeople);
        
        if( mPeople != null)
        {
            outState.putParcelableArrayList("people", mPeople);
        }
    } 
    
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Add your menu entries here
        super.onCreateOptionsMenu(menu, inflater);
        
        menu.clear();
        
        menu.add(0,0,ACTION_REFRESH,getString(R.string.refresh))
        .setIcon( R.drawable.ic_action_refresh)
        .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getOrder())
        {
            case ACTION_REFRESH:
                InvitationsPagerFragment invitationsPagerFrag = (InvitationsPagerFragment)getSherlockActivity().getSupportFragmentManager().findFragmentByTag("InvitationsPagerFragment");
                
                if(invitationsPagerFrag != null)
                    invitationsPagerFrag.fetchCandidates();
                return true;
                
        }
        
        return false;
    }
    
    
    private void setTitles( int pagePosition )
    {
        int persItr = 0;
        
        if( mInvitationsPagerAdapter.mViews.get(pagePosition) instanceof ProfileFragment )
        {
            for( int i=0; i < pagePosition; ++i )
            {
                if( mInvitationsPagerAdapter.mViews.get(i) instanceof ProfileFragment )
                    ++persItr;
            }
            
            Person person = mPeople.get(persItr);
            getActivity().setTitle(person.getUsername());
            getSherlockActivity().getSupportActionBar().setSubtitle(person.checkedIn() ? person.getCheckPlace().getName() : null);
        }
        else
        {
            getActivity().setTitle(getString(R.string.rendezvous));
            getSherlockActivity().getSupportActionBar().setSubtitle(getString(R.string.tonight));
        }
    }
    
    
    private static class InvitationsPagerAdapter extends FragmentStatePagerAdapter
    {
        private ArrayList<SherlockFragment> mViews = new ArrayList<SherlockFragment>();
        
        
        public InvitationsPagerAdapter(FragmentManager fm, ArrayList<Person> people, int nbLetsInvitationReceived, int nbFollowingPeople ) {
            this(fm);
            
            User u = User.getInstance();
            InvitationsClueFragment startFrag = InvitationsClueFragment.newInstance(User.getInstance().getNbNewPotentialRendezvous(),User.getInstance().getNbNewConfirmedRendezvous(),nbLetsInvitationReceived, nbFollowingPeople);
            mViews.add(startFrag);
            
            for( Person p : people )
            {
                ProfileFragment f = ProfileFragment.newInstance(p, true);
                mViews.add(f);
            }
        }  

        public InvitationsPagerAdapter(FragmentManager fm ) {
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
        if( getView() != null )
        {
            int persItr = 0;
            
            // TODO Auto-generated method stub
            if( f != null )
            {
                Person personToRemove = f.getPerson();
                
                for( int i=0; i<mInvitationsPagerAdapter.mViews.size(); ++i)
                { 
                    SherlockFragment frag = mInvitationsPagerAdapter.mViews.get(i);
                    
                    if( frag instanceof ProfileFragment )
                    {
                        Person p = mPeople.get(persItr);
                        
                        if( p.equals(personToRemove))
                        {
                            removePage(i, persItr);
                            return;
                        }
                        ++persItr;
                    }
                }
            } 
        }
    }
    
    
    
    private void removePage(int position, int personPosition) {

        
        int newPos = 0;
        
        if( position < mInvitationsPagerAdapter.mViews.size()-1 )
            newPos = position;
        else if( position == mInvitationsPagerAdapter.mViews.size()-1 )
            newPos = position-1;
        
        mPeople.remove(personPosition);
        mInvitationsPagerAdapter.mViews.remove(position);
        
        if( mInvitationsPagerAdapter.mViews.size() != 0 )
        {
            mViewPager.setAdapter(null);
            mViewPager.setAdapter(mInvitationsPagerAdapter);
            mViewPager.setCurrentItem(newPos);
            
            if(mInvitationsPagerAdapter.mViews.get(mViewPager.getCurrentItem()) instanceof ProfileFragment )
            {
                Person person = mPeople.get(personPosition == mPeople.size() ? personPosition-1: personPosition);
                getActivity().setTitle(person.getUsername());
                getSherlockActivity().getSupportActionBar().setSubtitle(person.checkedIn() ? person.getCheckPlace().getName() : null);
            }
            else
            {
                getActivity().setTitle(getString(R.string.rendezvous));
                getSherlockActivity().getSupportActionBar().setSubtitle(null);
            }
            
            // reload people
            if( mInvitationsPagerAdapter.mViews.size() == 1) 
                fetchCandidates();
        }
    }
    
    private void updatePager()
    {
        mInvitationsPagerAdapter = new InvitationsPagerAdapter(this.getChildFragmentManager(),  mPeople, mNbLetsInvitationReceived, mNbFollowingPeople);
        mViewPager.setAdapter(mInvitationsPagerAdapter);
        mViewPager.setCurrentItem(mPos);
        //mInvitationsPagerAdapter.notifyDataSetChanged();
        
    }
    
    public void fetchCandidates()
    {
        mCandidatesTaskMethod.doTask(User.getInstance().getUserId());
    }
    
    // EVENTS
    @Override
    public void candidatesTaskEventReceived(CandidatesTaskEvent evt) {
        CandidatesTaskMethod met = (CandidatesTaskMethod)evt.getSource();
        
        if( met != null && met.success() )
        {
            mNbLetsInvitationReceived = 0;
            mNbFollowingPeople = 0;
            
            // CANDIDATES
            mPeople = met.getPeople();
            Person.PersonComparator comparator = new Person.PersonComparator();
            Collections.sort(mPeople, comparator);
            
            for( Person p : mPeople )
            {
                if( p.letsInvitationReceived() )
                    ++mNbLetsInvitationReceived;
            }
            
            mNbFollowingPeople = mNbLetsInvitationReceived;
            
            if( mNbLetsInvitationReceived < 5 )
                mNbFollowingPeople = Math.min(mNbFollowingPeople*4, mPeople.size());
            else if( mNbLetsInvitationReceived < 10 )
                mNbFollowingPeople = Math.min(mNbFollowingPeople*3, mPeople.size());
            else
                mNbFollowingPeople = Math.min(mNbFollowingPeople*2, mPeople.size());
            
            ArrayList<Person> shuffled = new ArrayList<Person>(mPeople.subList(0, mNbFollowingPeople));
            
            Collections.shuffle(shuffled, new Random());
            for( int i=0; i<shuffled.size(); ++i )
                mPeople.set(i, shuffled.get(i)); 
            
            
            // POTENTIAL RENDEZVOUS
            User.getInstance().setPotentialRendezvous(met.getPotentialRendezvous());


            // CONFIRMED RENDEZVOUS
            User.getInstance().setConfirmedRendezvous(met.getConfirmedRendezvous());


            updatePager();
        }   
    }
    
}