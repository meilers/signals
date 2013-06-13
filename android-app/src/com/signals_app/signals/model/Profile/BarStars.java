package com.signals_app.signals.model.Profile;

import android.os.Parcel;
import android.os.Parcelable;

import com.signals_app.signals.model.Place;


public class BarStars implements Parcelable
{
	Place mPlace;
	Integer mStarCount;
	
	public BarStars( Place place, Integer starCount )
	{
		mPlace = place;
		mStarCount = starCount;
	}
	
	
	/**** PARCELLING ****/
    
    public BarStars(Parcel in)
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
        dest.writeParcelable(mPlace, flags);
        dest.writeInt(mStarCount);
    }
    
    public static final Parcelable.Creator<BarStars> CREATOR = new Parcelable.Creator<BarStars>() 
    {
        public BarStars createFromParcel(Parcel in) {
            return new BarStars(in); 
        }

        public BarStars[] newArray(int size) {
            return new BarStars[size];
        }
    };
    
    public void readFromParcel(Parcel in) 
    {
        mPlace = in.readParcelable(Place.class.getClassLoader());
        mStarCount = in.readInt();
    }
    
    
    /**** GET SET ****/
	public Place getPlace()
	{
		return mPlace;
	}
	
	public Integer getStarCount()
	{
		return mStarCount;
	}
}