package com.signals_app.signals.activity;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.signals_app.signals.R;
import com.signals_app.signals.model.Profile.Person;
import com.signals_app.signals.model.Profile.User;

public class InvitationsClueFragment extends SherlockFragment
{
    private int mNbPotentialInvitations = 0;
    private int mNbConfirmedInvitations = 0;
    private int mNbInvitationsReceived = 0;
    private int mNbPeople = 0;
    
    private View mPotentialSeparator;
    private View mConfirmedSeparator;
    private LinearLayout mPotentialLayout;
    private LinearLayout mConfirmedLayout;
    private TextView mPotentialNotifTv;
    private TextView mConfirmedNotifTv;
    private TextView mInvitationsClueTv;
    
    public static final InvitationsClueFragment newInstance( int nbPotentialInvitations, int nbConfirmedInvitations, int nbInvitationsReceived, int nbPeople )
    {
        InvitationsClueFragment f = new InvitationsClueFragment();
        
        // Get arguments passed in, if any
        Bundle args = f.getArguments();
        if (args == null) {
            args = new Bundle();
        }
        
        // Add parameters to the argument bundle
        args.putInt("nbPotentialInvitations", nbPotentialInvitations );
        args.putInt("nbConfirmedInvitations", nbConfirmedInvitations );
        args.putInt("nbInvitationsReceived", nbInvitationsReceived );
        args.putInt("nbPeople", nbPeople );
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
        
        mNbPotentialInvitations = args.getInt("nbPotentialInvitations");
        mNbConfirmedInvitations = args.getInt("nbConfirmedInvitations");
        mNbInvitationsReceived = args.getInt("nbInvitationsReceived");
        mNbPeople = args.getInt("nbPeople");
    }
 
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
        View view = inflater.inflate(R.layout.fragment_invitations_clue, container, false);  
         
        mPotentialSeparator = view.findViewById(R.id.potential_meetups_separator);
        mConfirmedSeparator = view.findViewById(R.id.confirmed_meetups_separator);
        mPotentialLayout = (LinearLayout)view.findViewById(R.id.potential_meetups_layout);
        mConfirmedLayout = (LinearLayout)view.findViewById(R.id.confirmed_meetups_layout);
        mPotentialNotifTv = (TextView)view.findViewById(R.id.potential_meetups_notif_tv);
        mConfirmedNotifTv = (TextView)view.findViewById(R.id.confirmed_meetups_notif_tv);
        mInvitationsClueTv = (TextView)view.findViewById(R.id.invitations_clue_tv);
        
        Resources res = getResources();
        String text = ""; 
                
        
        // CLUE
        if(mNbInvitationsReceived != 0)
            text = mNbInvitationsReceived + " " + res.getString(R.string.invitations_clue_msg1) + " " + mNbPeople + res.getString(R.string.invitations_clue_msg2);
        else
            text = res.getString(R.string.invitations_no_clue);
            
        mInvitationsClueTv.setText(text);
        
        
        // CLUE DESCRIPTION
        switch (getActivity().getResources().getConfiguration().orientation ) 
        {
            case Configuration.ORIENTATION_PORTRAIT: 
                
                TextView clueDescTv = (TextView)view.findViewById(R.id.invitations_clue_desc_tv);
                
                if(mNbInvitationsReceived != 0)
                    clueDescTv.setText(getString(R.string.invitations_desc_clue));
                else
                    clueDescTv.setText(getString(R.string.invitations_desc_no_clue));
                
                break;
                
        }  
        
        
        // CONFIRMED ** DONT SWITCH ORDER ** 1
        if( mNbConfirmedInvitations == 0 )
        {
            mConfirmedSeparator.setVisibility(View.GONE);
            mConfirmedLayout.setVisibility(View.GONE);
        }
        else
        {
            mConfirmedSeparator.setVisibility(View.VISIBLE);
            mConfirmedLayout.setVisibility(View.VISIBLE);
            mConfirmedNotifTv.setText(mNbConfirmedInvitations+"");
        }    
        
        
        // POTENTIAL ** 2
        if( mNbPotentialInvitations == 0 )
        {
            mPotentialSeparator.setVisibility(View.GONE);
            mPotentialLayout.setVisibility(View.GONE);
            
            switch (getActivity().getResources().getConfiguration().orientation ) 
            {
                case Configuration.ORIENTATION_LANDSCAPE: 
                    mConfirmedSeparator.setVisibility(View.GONE);
                    break;
            }  
        }
        else
        {
            mPotentialSeparator.setVisibility(View.VISIBLE);
            mPotentialLayout.setVisibility(View.VISIBLE);
            mPotentialNotifTv.setText(mNbPotentialInvitations+"");
        }
        
        mPotentialLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickPotentialRendezvous();
            }
        });
        
        mConfirmedLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickConfirmedRendezvous();
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
    
    private void onClickPotentialRendezvous()
    {
        // Enter
        PotentialRendezvousFragment frag =  PotentialRendezvousFragment.newInstance();
        
        getSherlockActivity().getSupportFragmentManager()
        .beginTransaction()
        .replace(R.id.content_frame, frag, "PotentialRendezvousFragment")
        .addToBackStack(null)
        .commit();
    }
    
    private void onClickConfirmedRendezvous()
    {
        // Enter
        ConfirmedRendezvousFragment frag =  ConfirmedRendezvousFragment.newInstance();
        
        getSherlockActivity().getSupportFragmentManager()
        .beginTransaction()
        .replace(R.id.content_frame, frag, "ConfirmedRendezvousFragment")
        .addToBackStack(null)
        .commit();
    }

}
