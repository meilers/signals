package com.signals_app.signals.model.rendezvous;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import android.annotation.SuppressLint;

import com.signals_app.signals.model.Place;
import com.signals_app.signals.model.Profile.Person;

public class ConfirmedRendezvous extends Rendezvous {

    private Place mPlace;
    private int mTimeSlot;
    private boolean mCancelled;
    private String mCancelReason;
    private boolean mCancelledByCandidate;
    
    
    @SuppressLint("UseSparseArrays")
    public ConfirmedRendezvous( Long id, Calendar createTime, Person candidate, Place place, int timeSlot, boolean isCancelled, String cancelReason, boolean cancelledByCandidate, boolean seen )
    {
        super(id, createTime, candidate, seen);

        mPlace = place;
        mTimeSlot = timeSlot;
        mCancelled = isCancelled;
        mCancelReason = cancelReason;
        mCancelledByCandidate = cancelledByCandidate;
    }

    public Place getPlace()
    {
        return mPlace;
    }

    public int getTimeSlot()
    {
        return mTimeSlot;
    }


    public boolean cancelled()
    {
        return mCancelled;
    }

    public boolean cancelledByCandidate()
    {
        return mCancelledByCandidate;
    }


    public String getCancelReason()
    {
        return mCancelReason;
    }


}
