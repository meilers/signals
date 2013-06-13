package com.signals_app.signals.model.Profile;

import android.os.Parcel;
import android.os.Parcelable;



public class GpsPosition implements Parcelable
{
	
	private float mLatitude = 0.0f;
	private float mLongitude = 0.0f;
	private float mAccuracy = 0.0f;
	
	
	public GpsPosition()
    {
        this(0.0f,0.0f,0.0f);
    }
	
	public GpsPosition( float latitude, float longitude, float accuracy )
	{
		mLatitude = latitude;
		mLongitude = longitude;
		mAccuracy = accuracy;
	}
	
	public GpsPosition(GpsPosition otherPos)
	{
		this(otherPos.mLatitude, otherPos.mLongitude, otherPos.mAccuracy);
	}
	
	   
	// PARCELLING
    public GpsPosition(Parcel in)
    {
        readFromParcel(in);
    }
    
    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) 
    {
        dest.writeFloat(mLatitude);
        dest.writeFloat(mLongitude);
        dest.writeFloat(mAccuracy);
        
    }
    
    public void readFromParcel(Parcel in) 
    {
        mLatitude = in.readFloat();
        mLongitude = in.readFloat();
        mAccuracy = in.readFloat();
    }
    
    
    public static final Parcelable.Creator<GpsPosition> CREATOR = new Parcelable.Creator<GpsPosition>() {
        public GpsPosition createFromParcel(Parcel in) {
            return new GpsPosition(in); 
        }

        public GpsPosition[] newArray(int size) {
            return new GpsPosition[size];
        }
    };
    
    
    
	// GETTERS SETTERS
	public float getLatitude() {
		return mLatitude;
	}
	
	public void setLatitude(float latitude) {
		this.mLatitude = latitude;
	}
		       
	public float getLongitude() {
		return mLongitude;
	}
	
	public void setLongitude(float longitude) {
		this.mLongitude = longitude;
	}
   
	 	
	public float getAccuracy() {
		return mAccuracy;
	}
	
	   	
	public void setAccuracy(float accuracy) {
		this.mAccuracy = accuracy;
	}


		   	
	   
	

}
