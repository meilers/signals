package com.signals_app.signals.model;

import java.util.HashMap;
import java.util.Map;

import android.os.Parcel;
import android.os.Parcelable;

import com.signals_app.signals.model.Profile.GpsPosition;
import com.signals_app.signals.model.Profile.Person;

public class City implements Parcelable {

    private int mId;
    private String mName;
    private String mCountry;


    public City( int id, String name, String country )
    {
        mId = id;
        mName = name;
        mCountry = country;
    }
    
    public City(City otherCity)
    {
        this.mId = otherCity.mId;
        this.mName = new String(otherCity.mName);
        this.mCountry = new String(otherCity.mCountry);
        
    }
    
    @Override
    public boolean equals(Object v) 
    {
        boolean retVal = false;
        
        if (v instanceof City){
            City ptr = (City) v;
            retVal = ptr.getId() == this.getId();
        }

        return retVal;
    }
    
    
    // PARCELLING
    
    public City(Parcel in)
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
        dest.writeInt(mId);
        dest.writeString(mName);
        dest.writeString(mCountry);
    }
    
    public static final Parcelable.Creator<City> CREATOR = new Parcelable.Creator<City>() 
    {
        public City createFromParcel(Parcel in) {
            return new City(in); 
        }

        public City[] newArray(int size) {
            return new City[size];
        }
    };
    
    public void readFromParcel(Parcel in) 
    {
        mId = in.readInt();
        mName = in.readString();
        mCountry = in.readString();
        
    }
    
    
    // GETTERS SETTERS
    
    public int getId()
    {
        return mId;
    }
    
    public String getName()
    {
        return mName;
    }

    
    public String getCountry()
    {
        return mCountry;
    }
    
}
