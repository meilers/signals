package com.signals_app.signals.model.Profile;

import java.util.*;

import android.os.Parcel;
import android.os.Parcelable;

import com.signals_app.signals.model.Place;


public class Person implements Parcelable
{
	public static final int INTERESTED_IN_NONE = 0;
	public static final int INTERESTED_IN_MEN = 1;
	public static final int INTERESTED_IN_WOMEN = 2;
	public static final int INTERESTED_IN_BOTH = 3;
   
	public static final int ORIENTATION_STRAIGHT = 1;
	public static final int ORIENTATION_GAY = 2;
	public static final int ORIENTATION_BI = 3;
	
	public static final int SEX_MALE = 1;
	public static final int SEX_FEMALE = 2;
	
	  
	public static final int LOOKING_NONE = 0;
	public static final int LOOKING_RIGHT_MATCH = 1;
	public static final int LOOKING_IM_OPEN = 2;
	public static final int LOOKING_NO_STRINGS = 3;
	   
	
	
	// Account
	private Long mUserId = Long.valueOf(0L);
    private boolean mVip = false;
    
    
	// Basic info
	private String mUsername = "";
	private Calendar mBirthday = Calendar.getInstance();
	private Integer mSex = Integer.valueOf(0);
	private Integer mInterestedIn = Integer.valueOf(0);
	private Integer mAge = Integer.valueOf(0);
	
	// About
	private String mAboutMe = "";
	private String mOccupation = "";
	private String mEducation = "";
	private String mInterests = "";
	private String mActivities = "";
	private String mTravel = "";
	private String mMusic = "";
	private String mDrinks = "";
	private String mMyPerfectNightOut = "";
	
	// Activity
	private Place mCheckedPlace = null;
	private ArrayList<Place> mTopFrequentedBars = new ArrayList<Place>();
	private ArrayList<BarStars> mCollectedBarStars = new ArrayList<BarStars>();
	
	
	// Approach
	private boolean mTipComeAndSayHi = false;
	private boolean mTipBuyMeADrink = false;
	private boolean mTipInviteMeToDance = false;
	private boolean mTipMakeMeLaugh = false;
	private boolean mTipMeetMyFriends = false;
	private boolean mTipSurpriseMe = false;
	private boolean mDontExpectAnything = false;
	private boolean mDontBePersistent = false;
	private boolean mDontBeDrunk = false;
	private String mPersonalAdvice = "";
	
	
	// Mood & Looking for
	private String mTonight = "";
	private boolean mDontApproach = false;
	private int mLookingFor = 0;
	
	// Pics
	private ArrayList<String> mProfilePhotosFilenames = new ArrayList<String>();	
	private ArrayList<String> mOtherPhotosFilenames = new ArrayList<String>();	
	
	
	
	
	// Status
	private boolean mStar = false;
	private boolean mBlocked = false;	
	private boolean mFlirted = false;
	private boolean mVoted = false;
	private boolean mCanBeVoted = true;
	
	
	private boolean mInvitationReceived = false;
	private boolean mInvitationSent = false;
	private boolean mNoThanksFlagged = false;
	
	
	
	
	public Person() { 
	}	

	@Override
    public boolean equals(Object v) 
    {
        boolean retVal = false;
        
        if (v instanceof Person){
            Person ptr = (Person) v;
            retVal = ptr.getUserId().longValue() == this.getUserId().longValue();
        }

        return retVal;
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + (this.getUserId() != null ? this.getUserId().hashCode() : 0);
        return hash;
    }
    
    


    // PARCELLING
    
    public Person(Parcel in)
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
        // Account
        dest.writeLong(mUserId);
        dest.writeByte((byte) (mVip ? 1 : 0));
        
        
        // Basic info
        dest.writeString(mUsername);
        dest.writeSerializable(mBirthday);
        dest.writeInt(mSex);
        dest.writeInt(mInterestedIn);
        dest.writeInt(mAge);
        
        // About
        dest.writeString(mAboutMe);
        dest.writeString(mOccupation);
        dest.writeString(mEducation);
        dest.writeString(mInterests);
        dest.writeString(mActivities);
        dest.writeString(mTravel);
        dest.writeString(mMusic);
        dest.writeString(mDrinks);
        dest.writeString(mMyPerfectNightOut);
        
        // Activity
        dest.writeByte((byte) (mCheckedPlace != null ? 1 : 0));
        
        if( mCheckedPlace != null )
            dest.writeParcelable(mCheckedPlace, flags);
        
        
        dest.writeInt(mTopFrequentedBars.size());
        for(int i=0;i<mTopFrequentedBars.size();++i) 
            dest.writeParcelable(mTopFrequentedBars.get(i), flags);
        
        dest.writeInt(mCollectedBarStars.size());
        for(int i=0;i<mCollectedBarStars.size();++i) 
            dest.writeParcelable(mCollectedBarStars.get(i), flags);
        
        
        // Approach
        dest.writeByte((byte) (mTipComeAndSayHi ? 1 : 0));
        dest.writeByte((byte) (mTipBuyMeADrink ? 1 : 0));
        dest.writeByte((byte) (mTipInviteMeToDance ? 1 : 0));
        dest.writeByte((byte) (mTipMakeMeLaugh ? 1 : 0));
        dest.writeByte((byte) (mTipMeetMyFriends ? 1 : 0));
        dest.writeByte((byte) (mTipSurpriseMe ? 1 : 0));
        dest.writeByte((byte) (mDontBeDrunk ? 1 : 0));
        dest.writeByte((byte) (mDontExpectAnything ? 1 : 0));
        dest.writeByte((byte) (mDontBePersistent ? 1 : 0));
        
        dest.writeString(mPersonalAdvice);
        
        
        // Mood & Looking for
        dest.writeString(mTonight);
        dest.writeByte((byte) (mDontApproach ? 1 : 0));
        dest.writeInt(mLookingFor);
        
        
        // Pics
        dest.writeStringList(mProfilePhotosFilenames);
        dest.writeStringList(mOtherPhotosFilenames);
        
        
        
        // Status
        dest.writeByte((byte) (mStar ? 1 : 0));
        dest.writeByte((byte) (mBlocked ? 1 : 0));
        dest.writeByte((byte) (mFlirted ? 1 : 0));
        dest.writeByte((byte) (mVoted ? 1 : 0));
        dest.writeByte((byte) (mCanBeVoted ? 1 : 0));
        
        dest.writeByte((byte) (mInvitationSent ? 1 : 0));
        dest.writeByte((byte) (mNoThanksFlagged ? 1 : 0));
    }
   
    
    public void readFromParcel(Parcel in) 
    {
        // Account
        mUserId = in.readLong();
        mVip = in.readByte() == 1;
        
        
        // Basic info
        mUsername = in.readString();
        mBirthday = (Calendar)in.readSerializable();
        mSex = in.readInt();
        mInterestedIn = in.readInt();
        mAge = in.readInt();
        
        // About
        mAboutMe = in.readString();
        mOccupation = in.readString();
        mEducation = in.readString();
        mInterests = in.readString();
        mActivities = in.readString();
        mTravel = in.readString();
        mMusic = in.readString();
        mDrinks = in.readString();
        mMyPerfectNightOut = in.readString();
        
        // Activity
        boolean checkPlaceExists = in.readByte() == 1;
        
        if( checkPlaceExists )
            mCheckedPlace = in.readParcelable(Place.class.getClassLoader());
        else
            mCheckedPlace = null;
        
        int size = in.readInt();
        mTopFrequentedBars = new ArrayList<Place>();
        for(int i=0;i<size;++i) 
            mTopFrequentedBars.add((Place)in.readParcelable(Place.class.getClassLoader()));
        
        size = in.readInt();
        mCollectedBarStars = new ArrayList<BarStars>();
        for(int i=0;i<size;++i) 
            mCollectedBarStars.add((BarStars)in.readParcelable(BarStars.class.getClassLoader()));
        
        
        
        // Approach
        mTipComeAndSayHi = in.readByte() == 1;
        mTipBuyMeADrink = in.readByte() == 1;
        mTipInviteMeToDance = in.readByte() == 1;
        mTipMakeMeLaugh = in.readByte() == 1;
        mTipMeetMyFriends = in.readByte() == 1;
        mTipSurpriseMe = in.readByte() == 1;
        mDontBeDrunk = in.readByte() == 1;
        mDontExpectAnything = in.readByte() == 1;
        mDontBePersistent = in.readByte() == 1;
        
        mPersonalAdvice = in.readString();
        
        
        // Mood & Looking for
        mTonight = in.readString();
        mDontApproach = in.readByte() == 1;
        mLookingFor = in.readInt();
        
        // Pics
        in.readStringList(mProfilePhotosFilenames);
        in.readStringList(mOtherPhotosFilenames);  
        
        
        
        
        
        // Status
        mStar = in.readByte() == 1;
        mBlocked = in.readByte() == 1;
        mFlirted = in.readByte() == 1;
        mVoted = in.readByte() == 1;
        mCanBeVoted = in.readByte() == 1;
        
        mInvitationSent = in.readByte() == 1;
        mNoThanksFlagged = in.readByte() == 1;
    }
    
    public static final Parcelable.Creator<Person> CREATOR = new Parcelable.Creator<Person>() 
    {
        public Person createFromParcel(Parcel in) {
            return new Person(in); 
        }

        public Person[] newArray(int size) {
            return new Person[size];
        }
    };
    
    
	
	/***** GETTERS SETTERS *****/

	public Long getUserId() {
	    return mUserId;
	}  

	public void setUserId(Long uId) {
	    this.mUserId = uId;
	}
	
	
	public String getUsername() {
	    return mUsername;
	}
	
	public void setUsername(String username) {
	    this.mUsername = username;
	}
	
	
	public Calendar getBirthday() {
	    return mBirthday;
	}  

	public void setBirthday(Calendar birthday) {
	    this.mBirthday = birthday;
	}
   
	public Integer getSex() {
	    return mSex;
	}

	public void setSex(Integer sex) {
	    this.mSex = sex;
	}
	
	public Integer getInterestedIn() {
	    return mInterestedIn;
	}  

	public void setInterestedIn(Integer interestedIn) {
	    this.mInterestedIn = interestedIn;
	}
	
	public Integer getOrientation() {
	   
	    if( mSex == SEX_FEMALE && mInterestedIn == INTERESTED_IN_MEN || 
            mSex == SEX_MALE && mInterestedIn == INTERESTED_IN_WOMEN )
	        return ORIENTATION_STRAIGHT;
	    if( mSex == SEX_FEMALE && mInterestedIn == INTERESTED_IN_WOMEN || 
	            mSex == SEX_MALE && mInterestedIn == INTERESTED_IN_MEN )
	        return ORIENTATION_GAY;
	    else
	        return ORIENTATION_BI;
	}
	
	
	public Integer getAge() {
	    return mAge;
	}  
	
	public void setAge(Integer age) {
	    this.mAge = age;
	}
	
	
	// Pics
	
	public ArrayList<String> getProfilePhotosFilenames() {
	    return mProfilePhotosFilenames;
	}
	
	public void setProfilePhotosFilenames(ArrayList<String> filenames) {
	    this.mProfilePhotosFilenames = filenames;
	}
   
	public ArrayList<String> getOtherPhotosFilenames() {
	    return mOtherPhotosFilenames;
	}
	
	public void setOtherPhotosFilenames(ArrayList<String> filenames) {
	    this.mOtherPhotosFilenames = filenames;
	}
	
	
	    
	
	// About
	
	public String getAboutMe() {
	    return mAboutMe;
	}  
	
	public void setAboutMe(String aboutMe) {
	    this.mAboutMe = aboutMe;
	}
	
    public String getOccupation() {
        return mOccupation;
    }  
    
    public void setOccupation(String occupation) {
        this.mOccupation = occupation;
    }  
    
    public String getEducation() {
        return mEducation;
    }  
    
    public void setEducation(String education) {
        this.mEducation = education;
    }
    
	public String getInterests() {
        return mInterests;
    }  
    
    public void setInterests(String interests) {
        this.mInterests = interests;
    }    
    
	public String getActivities() {
	    return mActivities;
	}  
	
	public void setActivities(String activities) {
	    this.mActivities = activities;
	}
	
	public String getTravel() {
        return mTravel;
    }  
    
    public void setTravel(String travel) {
        this.mTravel = travel;
    }
	
	
	public String getMusic() {
	    return mMusic;
	}  
	
	public void setMusic(String Music) {
	    this.mMusic = Music;
	}
	
	public String getDrinks() {
	    return mDrinks;
	}  
	
	public void setDrinks(String Drinks) {
	    this.mDrinks = Drinks;
	}  
	
	public String getMyPerfectNightOut() {
        return mMyPerfectNightOut;
    }  
    
    public void setMyPerfectNightOut(String nightOut) {
        this.mMyPerfectNightOut = nightOut;
    }

	
	
	
	// ACTIVITY
	    

    public boolean checkedIn() 
    {
        if( getCheckPlace() != null && !getCheckPlace().getPlaceId().equals(0L))
            return true;
        else
            return false;
    }
    
	public Place getCheckPlace() {
	    return mCheckedPlace;
	}
	
	public void setCheckPlace(Place p) {
	    mCheckedPlace = p;
	}
	
	
	public ArrayList<Place> getTopFrequentedBars()
	{
	    return mTopFrequentedBars;
	}    
	
	public void setTopFrequentedBars( ArrayList<Place> topFrequentedBars )
	{
	    mTopFrequentedBars = topFrequentedBars;
	}
	
	public ArrayList<BarStars> getCollectedBarStars()
	{
	    return mCollectedBarStars;
	}
	
	public void setCollectedBarStars( ArrayList<BarStars> barStars )
	{
	    mCollectedBarStars = barStars;
	}
	
	
	
	// APPROACH
	
	public boolean tipComeAndSayHi()
	{
	    return mTipComeAndSayHi;
	}
	
	public void setTipComeAndSayHi( boolean activate )
	{
	    mTipComeAndSayHi = activate;
	}
	
	public boolean tipBuyMeADrink()
	{
	    return mTipBuyMeADrink;
	}
	
	public void setTipBuyMeADrink( boolean activate )
	{
	    mTipBuyMeADrink = activate;
	}
	
	public boolean tipInviteMeToDance()
	{
	    return mTipInviteMeToDance;
	}
	
	public void setTipInviteMeToDance( boolean activate )
	{
	    mTipInviteMeToDance = activate;
	}
	
	public boolean tipMakeMeLaugh()
    {
        return mTipMakeMeLaugh;
    }
    
    public void setTipMakeMeLaugh( boolean tipMakeMeLaugh )
    {
        mTipMakeMeLaugh = tipMakeMeLaugh;
    }
    
    public boolean tipMeetMyFriends()
    {
        return mTipMeetMyFriends;
    }
    
    public void setTipMeetMyFriends( boolean tipMeetMyFriends )
    {
        mTipMeetMyFriends = tipMeetMyFriends;
    }
	
	public boolean tipSurpriseMe()
	{
	    return mTipSurpriseMe;
	}
	
	public void setTipSurpriseMe( boolean activate )
	{
	    mTipSurpriseMe = activate;
	}
	
	
	public boolean dontExpectAnything()
	{
	    return mDontExpectAnything;
	}    
	
	public void setDontExpectAnything( boolean activate )
	{
	    mDontExpectAnything = activate;
	}
	
	public boolean dontBePersistent()
	{
	    return mDontBePersistent;
	}
	
	public void setDontBePersistent( boolean activate )
	{    
	    mDontBePersistent = activate;
	}
	
	public boolean dontBeDrunk()
	{    
	    return mDontBeDrunk;
	}
	
	public void setDontBeDrunk( boolean activate )
	{
	    mDontBeDrunk = activate;
	}
	
	public String getPersonalAdvice() {
	    return mPersonalAdvice;
	}      

	public void setPersonalAdvice(String advice) {
	    this.mPersonalAdvice = advice;
	}
	
	
	
	// MORE
	
	public String getTonight() {
	    return mTonight;
	}       
	
	public void setTonight(String tonight) {
	    this.mTonight = tonight;
	}       
	
	public boolean dontApproach() {
	    return mDontApproach;
	}
	    
	public void setDontApproach( boolean dontApproach ) {
	    mDontApproach = dontApproach;
	}
	
	public int getLookingFor() {
	    return mLookingFor;
	}
	    
	public void setLookingFor( int lookingFor ) {
	    mLookingFor = lookingFor;
	}
	
	
	    
	
	public boolean vip() {
	    return mVip;
	}
	
	public void setVip(boolean vip) {
	    this.mVip = vip;
	}
	
	public boolean star() {
	    return mStar;
	}

	public void setStar(boolean star) {
	    this.mStar = star;
	}
	
	
	// STATUS
	
	public boolean flirted() {
	    return mFlirted;
	}  
	
	public void setFlirted(boolean flirted) {
	    this.mFlirted = flirted;
	}
	
	
	
	public boolean voted() {
	    return mVoted;
	}
	
	public void setVoted(boolean voted) {
	    this.mVoted = voted;
	}
	
	public boolean blocked() {
	    return mBlocked;
	}  
	
	public void setBlocked(boolean blocked) {
	    this.mBlocked = blocked;
	}
	
	
	
	public boolean canBeVoted() {
	    return mCanBeVoted;
	}  
	
	public void setCanBeVoted(boolean canBeVoted) {
	    this.mCanBeVoted = canBeVoted;
	}
	
	
	public boolean letsInvitationSent() {
        return mInvitationSent;
    }  
    
    public void setInvitationSent(boolean letsInvitationSent) {
        this.mInvitationSent = letsInvitationSent;
    }
    
    public boolean letsInvitationReceived() {
        return mInvitationReceived;
    }  
    
    public void setInvitationReceived(boolean letsInvitationReceived) {
        this.mInvitationReceived = letsInvitationReceived;
    }
    
    public boolean noThanksFlagged() {
        return mNoThanksFlagged;
    }  
    
    public void setNoThanksFlagged(boolean noThanksFlagged) {
        this.mNoThanksFlagged = noThanksFlagged;
    }
    
    
	
	// COMPARATORS
	
    public static class PersonComparator implements Comparator<Person> 
    {
        private static final int LETS_MEETUP_RECEIVED_POINTS = 100000;
        private static final int STAR_POINTS = 10000;
        
        private static final int SUPER_USER_POINTS = 1000;
        private static final int AGE_POINTS = 20;
        private static final int LOOKING_FOR_POINTS = 50;
        private static final int HOMETOWN_POINTS = 10;
        
        private static final int BLOCKED_POINTS = -100000;
        
        
        public <T> Vector<T> intersection(Vector<T> v1, List<T> v2) {
            Vector<T> v = new Vector<T>();
            
            for (T t : v1) {
                if(v2.contains(t)) {
                    v.add(t);
                }
            } 
            return v;
        }
            
            
        @Override
        public int compare(Person p1, Person p2) 
        {
            User u = User.getInstance();
            
            int p1Total = 0;
            int p2Total = 0;
            
            // STAR
            int p1StarPoints = p1.star() ? STAR_POINTS : 0;
            int p2StarPoints = p2.star() ? STAR_POINTS : 0;
            
            
            // VIP
            int p1VipPoints = p1.vip() ? SUPER_USER_POINTS : 0;
            int p2VipPoints = p2.vip() ? SUPER_USER_POINTS : 0;
            
            // AGE
            int p1AgePoints = AGE_POINTS - Math.abs(p1.getAge() - u.getAge())*4;
            int p2AgePoints = AGE_POINTS - Math.abs(p2.getAge() - u.getAge())*4;
            
            
            // HOMETOWN
            int p1ActivitiesPoints = p1.getActivities().equals(u.getActivities()) ? HOMETOWN_POINTS : 0;
            int p2ActivitiesPoints = p2.getActivities().equals(u.getActivities()) ? HOMETOWN_POINTS : 0;
            
            
            // LOOKING FOR
            int p1LookingForPoints = 0;
            int p2LookingForPoints = 0;
            
            if( p1.getLookingFor() != Person.LOOKING_NONE && p1.getLookingFor() == u.getLookingFor() )
                p1LookingForPoints = LOOKING_FOR_POINTS;
            
            if( p2.getLookingFor() != Person.LOOKING_NONE && p2.getLookingFor() == u.getLookingFor() )
                p2LookingForPoints = LOOKING_FOR_POINTS;
            
            
            // BLOCKED
            int p1BlockedPoints = p1.blocked() ? BLOCKED_POINTS : 0;
            int p2BlockedPoints = p2.blocked() ? BLOCKED_POINTS : 0;
            
            
            // LETS MEET UP RECEIVED
            int p1InvitationReceivedPoints = p1.letsInvitationReceived() ? LETS_MEETUP_RECEIVED_POINTS : 0;
            int p2InvitationReceivedPoints = p2.letsInvitationReceived() ? LETS_MEETUP_RECEIVED_POINTS : 0;
            
            
            p1Total = p1StarPoints + p1VipPoints + p1AgePoints + p1ActivitiesPoints + p1LookingForPoints + p1BlockedPoints + p1InvitationReceivedPoints;
            p2Total = p2StarPoints + p2VipPoints + p2AgePoints + p2ActivitiesPoints + p2LookingForPoints + p2BlockedPoints + p2InvitationReceivedPoints;
            
            return p2Total - p1Total;
        }
    }

}