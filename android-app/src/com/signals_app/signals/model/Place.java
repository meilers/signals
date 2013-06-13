package com.signals_app.signals.model;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

import android.os.Parcel;
import android.os.Parcelable;

import com.signals_app.signals.model.Profile.GpsPosition;
import com.signals_app.signals.model.Profile.Person;
import com.signals_app.signals.model.Profile.User;
import com.signals_app.signals.util.Utility;

public class Place implements Parcelable
{
    public static final int CLUB = 1; 
	public static final int LOUNGE = 2;
	public static final int BAR = 3;

	private Long mPlaceId = Long.valueOf(0L);
	private Integer mGenre = Integer.valueOf(0);
	private String mName = new String("");
	private String mAddress = new String("");
	private String mMessage = new String("");
	private String mWebsite = new String("");
	
	private GpsPosition mGpsPosition;
	    
	private HashMap<Long,Person> mPeopleBeforeHashMap = null;
	private HashMap<Long,Person> mPeopleNewHashMap = null;
		
   
	public Place()
	{
	    this(0L);
    }
	
	public Place(Long placeId )
    {
	    mPlaceId = placeId;
    }
	
	public Place(Place otherPlace)
	{
		this.mPlaceId = Long.valueOf(otherPlace.mPlaceId);
		this.mGenre = Integer.valueOf(otherPlace.mGenre);
		this.mName = new String(otherPlace.mName);
		this.mAddress = new String(otherPlace.mAddress);
		this.mGpsPosition = new GpsPosition(otherPlace.mGpsPosition);
		this.mMessage = new String(otherPlace.mMessage);
		this.mWebsite = new String(otherPlace.mWebsite);
		
		if( otherPlace.mPeopleBeforeHashMap != null )
		    this.mPeopleBeforeHashMap = new HashMap<Long,Person>(otherPlace.mPeopleBeforeHashMap);
		
		if( otherPlace.mPeopleNewHashMap != null )
		    this.mPeopleNewHashMap = new HashMap<Long,Person>(otherPlace.mPeopleNewHashMap);
	}
	
	@Override
	public boolean equals(Object v) 
	{
		boolean retVal = false;
		
		if (v instanceof Place){
			Place ptr = (Place) v;
		    retVal = ptr.getPlaceId().longValue() == this.getPlaceId();
		}

		return retVal;
	}
	
	@Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + (this.getPlaceId() != null ? this.getPlaceId().hashCode() : 0);
        return hash;
    }
	
	
	
	// PARCELLING
	
    public Place(Parcel in)
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
        dest.writeLong(mPlaceId);
        dest.writeInt(mGenre);
        dest.writeString(mName);
        dest.writeString(mAddress);
        dest.writeString(mMessage);
        dest.writeString(mWebsite);
        
        dest.writeParcelable(mGpsPosition, flags);
        
        dest.writeByte((byte) (mPeopleBeforeHashMap != null ? 1 : 0)); 
        if(mPeopleBeforeHashMap != null)
        {
            int N = mPeopleBeforeHashMap.size();
            dest.writeInt(N);
            if (N > 0) {
                for (Map.Entry<Long, Person> entry : mPeopleBeforeHashMap.entrySet()) {
                    dest.writeLong(entry.getKey());
                    Person dat = entry.getValue();
                    dest.writeParcelable(dat, flags);
                }
            }            
        }

        dest.writeByte((byte) (mPeopleNewHashMap != null ? 1 : 0));
        if(mPeopleNewHashMap != null)
        {
            int N = mPeopleNewHashMap.size();
            dest.writeInt(N);
            if (N > 0) {
                for (Map.Entry<Long, Person> entry : mPeopleNewHashMap.entrySet()) {
                    dest.writeLong(entry.getKey());
                    Person dat = entry.getValue();
                    dest.writeParcelable(dat, flags);
                }
            }            
        }

        
    }
    
    public static final Parcelable.Creator<Place> CREATOR = new Parcelable.Creator<Place>() 
    {
        public Place createFromParcel(Parcel in) {
            return new Place(in); 
        }

        public Place[] newArray(int size) {
            return new Place[size];
        }
    };
    
    public void readFromParcel(Parcel in) 
    {
        mPlaceId = in.readLong();
        mGenre = in.readInt();
        mName = in.readString();
        mAddress = in.readString();
        mMessage = in.readString();
        mWebsite = in.readString();
        
        mGpsPosition = in.readParcelable(GpsPosition.class.getClassLoader());
        
        boolean beforePeopleExists = in.readByte() == 1;
        
        if(beforePeopleExists)
        {
            int N = in.readInt();
            mPeopleBeforeHashMap = new HashMap<Long,Person>();
            for (int i=0; i<N; i++) {
                Long key = in.readLong();
                Person dat = in.readParcelable(Person.class.getClassLoader());
                mPeopleBeforeHashMap.put(key, dat);
            }
        }
        else
            mPeopleBeforeHashMap = null;
        
        boolean newPeopleExists = in.readByte() == 1;
        
        if( newPeopleExists )
        {
            int N = in.readInt();
            mPeopleNewHashMap = new HashMap<Long,Person>();
            for (int i=0; i<N; i++) {
                Long key = in.readLong();
                Person dat = in.readParcelable(Person.class.getClassLoader());
                mPeopleNewHashMap.put(key, dat);
            }            
        }
        else
            mPeopleNewHashMap = null;

    }
    
    
    
    // GETTERS SETTERS
    
	public Long getPlaceId() {
        return mPlaceId;
	}

	public void setPlaceId( Long id )
	{
	    mPlaceId = id;
	}
   
	public GpsPosition getGpsPosition() {
	    return mGpsPosition;
	}

	public void setGpsPosition( GpsPosition pos )
	{
	    mGpsPosition = pos;
	}
   
  
	public Integer getGenre() {
	    return mGenre;
	}

	public void setGenre( Integer genre )
	{
	    mGenre = genre;
	}
   
   
	public String getName() {
        return mName;
	}

	public void setName( String name )
	{
	    mName = name;
	}
	
	public String getAddress() {
	    return mAddress;
	}
	
	public void setAddress( String address )
	{
	    mAddress = address;
	}
	
	
	public String getMessage() {
	    return mMessage;
	}  

	public void setMessage(String msg) {
	    this.mMessage = msg;
	}  
	
	public String getWebsite() {
	    return mWebsite;
	}
	
	public void setWebsite(String website) {
	    this.mWebsite = website;
	}
	
	public Person getStarMale()
	{
	    if( mPeopleBeforeHashMap != null )
        {
            for( Person p : mPeopleBeforeHashMap.values() )
            {
                if( p.getSex() == Person.SEX_MALE && p.star() )
                    return p;
            }
        }
        
        if( mPeopleNewHashMap != null )
        {
            for( Person p : mPeopleNewHashMap.values() )
            {
                if( p.getSex() == Person.SEX_MALE && p.star() )
                    return p;
            }
        }
        return null;
	}
	
	public Person getStarFemale()
    {
        if( mPeopleBeforeHashMap != null )
        {
            for( Person p : mPeopleBeforeHashMap.values() )
            {
                if( p.getSex() == Person.SEX_FEMALE && p.star() )
                    return p;
            }
        }
        
        if( mPeopleNewHashMap != null )
        {
            for( Person p : mPeopleNewHashMap.values() )
            {
                if( p.getSex() == Person.SEX_FEMALE && p.star() )
                    return p;
            }
        }
        return null;
    }
	
	
	public Integer getNbUsersMale() 
	{
	    int total = 0;
	    
	    if( mPeopleBeforeHashMap != null )
	    {
	        for( Person p : mPeopleBeforeHashMap.values() )
	        {
	            if( p.getSex() == Person.SEX_MALE )
	                ++total;
	        }
	    }
	    
	    if( mPeopleNewHashMap != null )
        {
            for( Person p : mPeopleNewHashMap.values() )
            {
                if( p.getSex() == Person.SEX_MALE )
                    ++total;
            }
        }
	    
	    return total;
	}  

	
	public Integer getNbUsersFemale() 
	{
        int total = 0;
        
        if( mPeopleBeforeHashMap != null )
        {
            for( Person p : mPeopleBeforeHashMap.values() )
            {
                if( p.getSex() == Person.SEX_FEMALE )
                    ++total;
            }
        }
        
        if( mPeopleNewHashMap != null )
        {
            for( Person p : mPeopleNewHashMap.values() )
            {
                if( p.getSex() == Person.SEX_FEMALE )
                    ++total;
            }
        }
        
        return total;
	}

   
	public Integer getNbUsersTotal() 
	{
	    return getNbUsersMale()+getNbUsersFemale();
	}
   
	public Integer getAverageAge() 
    {
        int sum = 0;
        int total = 0;
        
        if( mPeopleBeforeHashMap != null )
        {
            for( Person p : mPeopleBeforeHashMap.values() )
            {
                ++total;
                sum += p.getAge();
            }
        }
        
        if( mPeopleNewHashMap != null )
        {
            for( Person p : mPeopleNewHashMap.values() )
            {
                ++total;
                sum += p.getAge();
            }
        }
        return total == 0 ? 0 : sum/total;
    }
	
	
    public Integer getNbUsersLookingRightMatch() 
    {
        int total = 0;
        
        if( mPeopleBeforeHashMap != null )
        {
            for( Person p : mPeopleBeforeHashMap.values() )
            {
                if( p.getLookingFor() == Person.LOOKING_RIGHT_MATCH )
                    ++total;
            }
        }
        
        if( mPeopleNewHashMap != null )
        {
            for( Person p : mPeopleNewHashMap.values() )
            {
                if( p.getLookingFor() == Person.LOOKING_RIGHT_MATCH )
                    ++total;
            }
        }
        return total;
    }
    
    public Integer getNbUsersLookingImOpen() 
    {
        int total = 0;
        
        if( mPeopleBeforeHashMap != null )
        {
            for( Person p : mPeopleBeforeHashMap.values() )
            {
                if( p.getLookingFor() == Person.LOOKING_IM_OPEN )
                    ++total;
            }
        }
        
        if( mPeopleNewHashMap != null )
        {
            for( Person p : mPeopleNewHashMap.values() )
            {
                if( p.getLookingFor() == Person.LOOKING_IM_OPEN )
                    ++total;
            }
        }
        return total;
    }
    
    public Integer getNbUsersLookingNoStrings() 
    {
        int total = 0;
        
        if( mPeopleBeforeHashMap != null )
        {
            for( Person p : mPeopleBeforeHashMap.values() )
            {
                if( p.getLookingFor() == Person.LOOKING_NO_STRINGS )
                    ++total;
            }
        }
        
        if( mPeopleNewHashMap != null )
        {
            for( Person p : mPeopleNewHashMap.values() )
            {
                if( p.getLookingFor() == Person.LOOKING_NO_STRINGS )
                    ++total;
            }
        }
        return total;
    }
	
	
	
	
	public HashMap<Long,Person> getPeopleBeforeHashMap() {
	    return mPeopleBeforeHashMap;
	}

	public void setPeopleBeforeHashMap(HashMap<Long,Person> peopleBefore) {
	    this.mPeopleBeforeHashMap = peopleBefore;
	}

   
	public HashMap<Long,Person> getPeopleNewHashMap() {
	    return mPeopleNewHashMap;
	}

	public void setPeopleNewHashMap(HashMap<Long,Person> peopleNew) {
	    this.mPeopleNewHashMap = peopleNew;
	}

	

    
    
    
	// COMPARATORS
	
	public static class DistanceComparator implements Comparator<Place> {
	    @Override
	    public int compare(Place o1, Place o2) 
	    {
	        GpsPosition userPos = User.getInstance().getGpsPosition();
	        GpsPosition placePos1 = o1.getGpsPosition();
	        GpsPosition placePos2 = o2.getGpsPosition();

            if( userPos != null && placePos1 != null && placePos2 != null )
            {
                Double dist1 = Utility.getDistance(userPos.getLatitude(), userPos.getLongitude(), placePos1.getLatitude(), placePos1.getLongitude(), "k");
                Double dist2 = Utility.getDistance(userPos.getLatitude(), userPos.getLongitude(), placePos2.getLatitude(), placePos2.getLongitude(), "k");

                return dist1.compareTo(dist2);
            }

	        return 0;
	    }
	}
 
	public static class NumberUsersComparator implements Comparator<Place> {
	    @Override
	    public int compare(Place o1, Place o2) {
	        
            if( o2.getNbUsersTotal().equals(o1.getNbUsersTotal()) )
            {
                DistanceComparator distanceComp = new DistanceComparator();
                return distanceComp.compare(o1, o2);
            }  
             
            return o2.getNbUsersTotal().compareTo(o1.getNbUsersTotal());
	    }
	}
	
	public static class NameComparator implements Comparator<Place> {
	    @Override
	    public int compare(Place o1, Place o2) {
	        return o1.getName().compareTo(o2.getName());
	    }
	}
}