package com.signals_app.signals.model.Signals;

import java.util.Calendar;

import com.signals_app.signals.model.Place;
import com.signals_app.signals.model.Profile.Person;

public class Visit extends Signal
{
	public Visit( Person person, boolean incoming, Place place, Calendar calandar, boolean seen )  
	{
		super(person, incoming, place, calandar, seen);
	}
	
	public int getType()
 	{
		return VISIT;
 	}
}
