package com.signals_app.signals.model.Signals;

import java.util.Calendar;

import com.signals_app.signals.model.Place;
import com.signals_app.signals.model.Profile.Person;

public class Vote extends Signal
{
	private boolean mFromLastHour;
	
	
	public Vote( Person person, boolean incoming, Place place, Calendar calandar, boolean seen, boolean fromLastHour )  
	{
		super(person, incoming, place, calandar, seen);
		
		mFromLastHour = fromLastHour;
	}
	
	public int getType()
 	{
		return VOTE;
 	}
	
	public boolean fromLastHour()
	{
		return mFromLastHour;
	}
}
