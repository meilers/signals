package com.signals_app.signals.model.rendezvous;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import android.os.Parcel;
import android.os.Parcelable;

import com.signals_app.signals.model.Place;
import com.signals_app.signals.model.Profile.GpsPosition;
import com.signals_app.signals.model.Profile.Person;

public class Rendezvous implements Parcelable
{
    public static final int FIVE = 0;
    public static final int SIX = 1;
    public static final int SEVEN = 2;
    public static final int EIGHT = 3;
    public static final int NINE = 4;
    public static final int TEN = 5;
    public static final int ELEVEN = 6;
    public static final int TWELVE = 7;
    public static final int ONE = 8;
    public static final int TWO = 9;
    
    private Long mId;
    private Calendar mCreateTime;
    private Person mCandidate;
     
    private boolean mSeen = false;
    
    public Rendezvous(Long id, Calendar createTime, Person candidate, boolean seen)
    {
        mId = id;
        mCreateTime = createTime;
        mCandidate = candidate;
        
        mSeen = seen;
    }
    

    
    
    
    // PARCELLING
    
    public Rendezvous(Parcel in)
    {
        readFromParcel(in);
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) 
    {
        dest.writeParcelable(mCandidate, flags);
        dest.writeByte((byte) (mSeen ? 1 : 0));
    }
    
    public static final Parcelable.Creator<Rendezvous> CREATOR = new Parcelable.Creator<Rendezvous>() 
    {
        public Rendezvous createFromParcel(Parcel in) {
            return new Rendezvous(in); 
        }

        public Rendezvous[] newArray(int size) {
            return new Rendezvous[size];
        }
    };
    
    public void readFromParcel(Parcel in) 
    {
        mCandidate = in.readParcelable(Person.class.getClassLoader());
        mSeen = in.readByte() == 1;
        
        
    }
    
    
    // GETTERS SETTERS
    
    public Long getId()
    {
        return mId;
    }
    
    public Person getCandidate()
    {
        return mCandidate;
    }
 
    
    public boolean seen() 
    {
        return mSeen;
    }
    
    public void setSeen( boolean seen ) 
    {
        mSeen = seen;
    }
    
    public Calendar getCreateTime()
    {
        return mCreateTime;
    }
}
