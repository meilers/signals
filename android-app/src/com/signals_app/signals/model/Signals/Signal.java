package com.signals_app.signals.model.Signals;

import java.util.Calendar;
import java.util.Comparator;


import android.content.Context;

import com.signals_app.signals.model.Place;
import com.signals_app.signals.model.Profile.Person;

abstract public class Signal 
{
	final private Person mPerson;
	final private boolean mIncoming;
	final private Place mPlace;
	final private Calendar mCreateTime;
	
	static final public int FLIRT = 1;
	static final public int MESSAGE = 2;
	static final public int VOTE = 3;
	static final public int VISIT = 4;
	
	
	private boolean mSeen = false;
   
    public static class SignalComparator implements Comparator<Signal> {
        @Override
        public int compare(Signal s1, Signal s2) 
        {	
            return s2.getCreateTime().compareTo(s1.getCreateTime());
        }
    }
    
    public static class SignalChatComparator implements Comparator<Signal> {
        @Override
        public int compare(Signal s1, Signal s2) 
        {	
            return s1.getCreateTime().compareTo(s2.getCreateTime());
        }
    }
    
	public Signal( Person person, boolean incoming, Place place, Calendar calendar, boolean seen ) 
	{
		mPerson = person;
		mIncoming = incoming;
		mPlace = place;
		mCreateTime = calendar;
		mSeen = seen;
	}
	
	public Person getPerson() 
	{
		return mPerson;
	}
	
	public boolean incoming() 
	{
		return mIncoming;
	}
	
	public Place getPlace() 
	{
		return mPlace;
	}
	
	public Calendar getCreateTime() 
	{
		return mCreateTime;
	}
	
	public boolean seen() 
	{
		return mSeen;
	}
	
	public void setSeen( boolean seen ) 
	{
		mSeen = seen;
	}
	
	abstract public int getType();
}
