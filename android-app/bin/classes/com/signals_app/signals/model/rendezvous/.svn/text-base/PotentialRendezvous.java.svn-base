package com.signals_app.signals.model.rendezvous;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import com.signals_app.signals.model.Place;
import com.signals_app.signals.model.Profile.Person;

@SuppressLint("UseSparseArrays")
public class PotentialRendezvous extends Rendezvous
{
    public static final int NB_CHOICES = 10;


    private ArrayList<Place> mPlaces;
    private ArrayList<Boolean> mTimes;
    
    private boolean mFirstAnswer = false;
    
    @SuppressLint("UseSparseArrays")
    public PotentialRendezvous( Long id, Calendar createTime, Person candidate, boolean seen, ArrayList<Place> places, ArrayList<Boolean> times, boolean firstAnswer )  
    {
        super(id, createTime, candidate, seen);
         
        mPlaces = places;
        mTimes = times;
        
        
        mFirstAnswer = firstAnswer;
    }
    

   
    
    // PARCELLING
    
    public PotentialRendezvous(Parcel in)
    {
        super(in);
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) 
    {
        super.writeToParcel(dest, flags);
        dest.writeTypedList(mPlaces);
        dest.writeList(mTimes);
        
    }
    
    public void readFromParcel(Parcel in) 
    {
        super.readFromParcel(in);
        
        in.readTypedList(mPlaces, Place.CREATOR);
        in.readList(mTimes, Boolean.class.getClassLoader());
        
    }
    
    
    // GETTERS SETTERS

    public void setPlaces(  ArrayList<Place> places )
    {
        mPlaces = places;
    }

    public ArrayList<Place> getPlaces()
    {
        return mPlaces;
    }
    
    public ArrayList<Boolean> getTimes()
    {
        return mTimes;
    }
    
    public boolean firstAnswer() 
    {
        return mFirstAnswer;
    }
    
}
