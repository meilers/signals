package com.signals_app.signals.model.Signals;

import java.util.Calendar;

import android.content.Context;

import com.signals_app.signals.R;
import com.signals_app.signals.model.Place;
import com.signals_app.signals.model.Profile.Person;

public class Message extends Signal 
{
	final public String mMsg;
	
	public Message( Person person, boolean incoming, Place place, Calendar calandar, String message, boolean seen )  
	{
		super(person, incoming, place, calandar, seen);
		
		mMsg = message;
	}
	
	public String getMessage()
	{
		return mMsg;
	}
	
	public int getType()
 	{
		return MESSAGE;
 	}
	
	

}
