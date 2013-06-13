package com.signals_app.signals.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.signals_app.signals.R;
import com.signals_app.signals.model.Profile.Person;
import com.signals_app.signals.model.Profile.User;

public class VipPrivilegesFragment extends SherlockFragment
{
    private Button mBecomeVipButton;
    
    
    
    public static final VipPrivilegesFragment newInstance( User u )
    {
        VipPrivilegesFragment f = new VipPrivilegesFragment();
        
        // Get arguments passed in, if any
        Bundle args = f.getArguments();
        if (args == null) {
            args = new Bundle();
        }
        
        // Add parameters to the argument bundle
        args.putBoolean("vip", u.vip());
        args.putLong("userId", u.getUserId());
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
        View view = inflater.inflate(R.layout.fragment_vip_privileges, container, false);  
        getSherlockActivity().setTitle(getResources().getString(R.string.vip_privileges));
        getSherlockActivity().getSupportActionBar().setSubtitle(null);
        
        mBecomeVipButton = (Button)view.findViewById(R.id.become_vip_btn);
                
                
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
        
        getSherlockActivity().setSupportProgressBarIndeterminateVisibility(false);
    } 


}
