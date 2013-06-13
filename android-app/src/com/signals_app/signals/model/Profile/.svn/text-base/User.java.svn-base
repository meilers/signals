package com.signals_app.signals.model.Profile;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import com.facebook.model.GraphUser;
import com.signals_app.signals.model.City;
import com.signals_app.signals.model.Place;
import com.signals_app.signals.model.Signals.Flirt;
import com.signals_app.signals.model.Signals.Message;
import com.signals_app.signals.model.Signals.Signal;
import com.signals_app.signals.model.Signals.Visit;
import com.signals_app.signals.model.Signals.Vote;
import com.signals_app.signals.model.rendezvous.ConfirmedRendezvous;
import com.signals_app.signals.model.rendezvous.PotentialRendezvous;


public class User extends Person
{	
    public final static int OUTSIDE = 0;
    public final static int PEEKING = 1;
    public final static int AT_CHECKED_IN_PLACE = 2;
    
    private static User mInstance = null;
    
    
    
	private CopyOnWriteArrayList<GpsPositionListener> mGpsPositionListeners;
	private CopyOnWriteArrayList<PlaceListener> mPlaceListeners;
	
	private CopyOnWriteArrayList<PotentialRendezvousListener> mPotentialRendezvousListeners;
	private CopyOnWriteArrayList<ConfirmedRendezvousListener> mConfirmedRendezvousListeners;
	
	private CopyOnWriteArrayList<FlirtListener> mFlirtListeners;
	private CopyOnWriteArrayList<MessageListener> mMessageListeners;
	private CopyOnWriteArrayList<VoteListener> mVoteListeners;
	private CopyOnWriteArrayList<VisitListener> mVisitListeners;
	
	
	// BASIC INFO
	private boolean mLoggedIn = false;
	private String mAccessToken;
	private String mEmail;
   

	
	// POSITION
	private GpsPosition mGpsPosition = null;
	
	// PLACE
	private City mCity;
	private Place mPeekPlace;
	   
	   
	// MEETUPS
	private ArrayList<PotentialRendezvous> mPotentialRendezvous;
    private ArrayList<ConfirmedRendezvous> mConfirmedRendezvous;
	
	// SIGNALS
	private int mNbNewFlirts;
	private int mNbNewMessages;
	private int mNbNewVotes;
	private int mNbNewVisits;
	
	private ArrayList<Flirt> mFlirts;
	private ArrayList<Vote> mVotes;
	private ArrayList<Visit> mVisits;
	private HashMap<Long,ArrayList<Message>> mMessagesHashMap;
	
	
	
	
	
	// PREFERENCES
	private int mWantedMinAge = 21;
	private int mWantedMaxAge = 35;
	private int mGpsRadius = 10000;	
	
	
	protected User() {
		super();
		// Exists only to defeat instantiation.
	}
	   
	public static User getInstance() 
	{
		if(mInstance == null) 
		{
		    synchronized(User.class)
		    {
		        if(mInstance == null) 
		        {
        			mInstance = new User();
        			mInstance.mGpsPositionListeners = new CopyOnWriteArrayList<GpsPositionListener>();
        			mInstance.mPlaceListeners = new CopyOnWriteArrayList<PlaceListener>();
        			mInstance.mPotentialRendezvousListeners = new CopyOnWriteArrayList<PotentialRendezvousListener>();
                    mInstance.mConfirmedRendezvousListeners = new CopyOnWriteArrayList<ConfirmedRendezvousListener>();
        			mInstance.mFlirtListeners = new CopyOnWriteArrayList<FlirtListener>();
        			mInstance.mMessageListeners = new CopyOnWriteArrayList<MessageListener>();
        			mInstance.mVoteListeners = new CopyOnWriteArrayList<VoteListener>();
        			mInstance.mVisitListeners = new CopyOnWriteArrayList<VisitListener>();
        			
        			// BASIC INFO
        			mInstance.mLoggedIn = false;
        			mInstance.mAccessToken = "";
        			mInstance.mEmail = "";
        			
        			
        			// PLACES
        			mInstance.mCity = null;
        			mInstance.mPeekPlace = null;
        			mInstance.mGpsPosition = null;
        			
        			
        			// MEETUPS
        			mInstance.mPotentialRendezvous = new ArrayList<PotentialRendezvous>();
                    mInstance.mConfirmedRendezvous = new ArrayList<ConfirmedRendezvous>();
        			
        			
        			
        			// SIGNALS
        			mInstance.mFlirts = new ArrayList<Flirt>();
        			mInstance.mMessagesHashMap = new HashMap<Long,ArrayList<Message>>();
        			
        			
        			// PREFERENCES
        			mInstance.mWantedMinAge = 21;
        			mInstance.mWantedMaxAge = 35;
        			mInstance.mGpsRadius = 10000;	
		        }
		    }
	     }
	      
		return mInstance;
	}
	  
	public static void init()
	{
		mInstance = null;
	}
	
	
	public boolean loggedIn() {
		return this.mLoggedIn;
	}

	public void setLoggedIn(boolean loggedIn) {
		this.mLoggedIn = loggedIn;
	}
	
	
	// BASIC INFO
	public String getAccessToken() {
		return this.mAccessToken;
	}

	public void setAccessToken(String accessToken) {
		this.mAccessToken = accessToken;
	}
	
	public String getEmail() {
		return this.mEmail;
	}

	public void setEmail(String email) {
		this.mEmail = email;
	}
	   
	
	// PREFERENCES
	public int getWantedMinAge()
	{
		return mWantedMinAge;
	}
	   
	public void setWantedMinAge( int wantedMinAge )
	{
		mWantedMinAge = wantedMinAge;
	}
	
	public int getWantedMaxAge()
	{
		return mWantedMaxAge;
	}
	   
	public void setWantedMaxAge( int wantedMaxAge )
	{
		mWantedMaxAge = wantedMaxAge;
	}
	
	
	public int getGpsRadius() {
		return mGpsRadius;
	}

	public void setGpsRadius(int radius) {
		this.mGpsRadius = radius;
		fireGpsPositionEvent();
	}
   
   
	public GpsPosition getGpsPosition() {
		return mGpsPosition;
	}
   
	public void setGpsPosition(GpsPosition pos) {
		mGpsPosition = pos;
		fireGpsPositionEvent();
	}
	
	
	// PLACES
	
	public City getCity()
	{
	    return mCity;
	}
	
	public void setCity(City city)
	{
	    mCity = city;
	}
	
	public Place getPeekPlace() {
		return mPeekPlace;
	}

	public void setPeekPlace(Place p) {
		mPeekPlace = p;
		firePlaceEvent();
	}
	
	@Override
	public void setCheckPlace(Place p) {
		super.setCheckPlace(p);
		firePlaceEvent();
	}
   
	public Place getPlace() 
	{
		switch( this.getState() )
		{
		case PEEKING:
			return getPeekPlace();
		case AT_CHECKED_IN_PLACE:
			return getCheckPlace();
		}
		return null;
	}
   
	public boolean peeking()
	{
	    if( getPeekPlace() != null && !getPeekPlace().getPlaceId().equals(0L) )
	        return true;
	    else
	        return false;
	}
	

	public int getState() {
		
		if( peeking() )
			return PEEKING;
		else if( checkedIn() )
			return AT_CHECKED_IN_PLACE;
		else
			return OUTSIDE; 
	}


	public ArrayList<PotentialRendezvous> getPotentialRendezvous() 
	{
	    return mPotentialRendezvous;
	}

	public void setPotentialRendezvous(ArrayList<PotentialRendezvous> potentialInvitations)
    {
        mPotentialRendezvous = potentialInvitations;
        this.firePotentialRendezvousEvent();
    }
	
	public ArrayList<ConfirmedRendezvous> getConfirmedRendezvous()
    {
        return mConfirmedRendezvous;
    }

    public void setConfirmedRendezvous(ArrayList<ConfirmedRendezvous> confirmedInvitations)
    {
        mConfirmedRendezvous = confirmedInvitations;
        this.fireConfirmedRendezvousEvent();
    }
    
    public int getNbNewPotentialRendezvous()
    {
        int cnt = 0;
        
        for( PotentialRendezvous pm : mPotentialRendezvous )
        {
            if( !pm.seen() )
                ++cnt;
        }
        
        return cnt;
    }
    
    public int getNbNewConfirmedRendezvous()
    {
        int cnt = 0;
        
        for( ConfirmedRendezvous cm : mConfirmedRendezvous )
        {
            if( !cm.seen() )
                ++cnt;
        }
        
        return cnt;
    }
    
    public int getNbNewInvitations()
    {
        return getNbNewPotentialRendezvous() + getNbNewConfirmedRendezvous();
    }
    
    
	
	public int getNbNewFlirts()
	{
		return mNbNewFlirts;
	}
	
	public void setNbNewFlirts( int nbNewFlirts )
	{
		mNbNewFlirts = nbNewFlirts;
		fireFlirtEvent();
	}
   
	public int getNbNewMessages()
	{
		return mNbNewMessages;
	}
	
	public void setNbNewMessages( int nbNewMessages )
	{
		mNbNewMessages = nbNewMessages;
		fireMessageEvent();
	}
	
	public int getNbNewVotes()
	{
		return mNbNewVotes;
	}
	
	public void setNbNewVotes( int nbNewVotes )
	{
		mNbNewVotes = nbNewVotes;
		fireVoteEvent();
	}
	
	public int getNbNewVisits()
	{
		return mNbNewVisits;
	}
	
	public void setNbNewVisits( int nbNewVisits )
	{
		mNbNewVisits = nbNewVisits;
		fireVisitEvent();
	}
	
	public int getNbNewSignals()
	{
		return mNbNewFlirts + mNbNewMessages + mNbNewVotes + mNbNewVisits;
	}
	
	
	public ArrayList<Flirt> getFlirts() {
		return mFlirts;
	}

	public void setFlirts(ArrayList<Flirt> flirts) {
		this.mFlirts = flirts;
		fireFlirtEvent();
	}
	
	
	
	public HashMap<Long,ArrayList<Message>> getMessagesHashMap() {
		return mMessagesHashMap;
	}

	public void setMessagesHashMap(HashMap<Long,ArrayList<Message>> messages) {
		this.mMessagesHashMap = messages;
		fireMessageEvent();
	}
	
	public ArrayList<Vote> getVotes() {
		return mVotes;
	}

	public void setVotes(ArrayList<Vote> votes) {
		this.mVotes = votes;
		fireVoteEvent();
	}
  
	public ArrayList<Visit> getVisits() {
		return mVisits;
	}

	public void setVisits(ArrayList<Visit> visits) {
		this.mVisits = visits;
		fireVisitEvent();
	}
  
	
	
	
	
	// LISTENERS
	
	// gps
	public interface GpsPositionListener {
		void gpsPositionEventReceived(GpsPositionEvent evt);
	}
	
	public void addGpsPositionListener(GpsPositionListener l) 
	{
		this.mGpsPositionListeners.add(l);
	}

	public void removeGpsPositionListener(GpsPositionListener l) 
	{
	    this.mGpsPositionListeners.remove(l);
	}
		
	public void fireGpsPositionEvent() {
		GpsPositionEvent evt = new GpsPositionEvent(this);

	    for (GpsPositionListener l : mGpsPositionListeners) 
    	{
	    	if( l != null)
	    		l.gpsPositionEventReceived(evt);
	    }
	}
		
	public class GpsPositionEvent extends EventObject {
		private static final long serialVersionUID = 0;
		
		public GpsPositionEvent(Object source) {
			super(source);
		}
	}
	

	//place	
	public interface PlaceListener {
		void placeEventReceived(PlaceEvent evt);
	}
	
	public void addPlaceListener(PlaceListener l) 
	{
		this.mPlaceListeners.add(l);
	}

	public void removePlaceListener(PlaceListener l) 
	{
	    this.mPlaceListeners.remove(l);
	}
		
	public void firePlaceEvent() {
		PlaceEvent evt = new PlaceEvent(this);

	    for (PlaceListener l : mPlaceListeners) 
    	{
	    	if( l != null)
	    		l.placeEventReceived(evt);
	    }
	}
		
	public class PlaceEvent extends EventObject {
		private static final long serialVersionUID = 0;
		
		public PlaceEvent(Object source) {
			super(source);
		}
	}
		
	
	
	// potentialInvitation
    public interface PotentialRendezvousListener {
        void potentialInvitationEventReceived(PotentialRendezvousEvent evt);
    }

    public void addPotentialRendezvousListener(PotentialRendezvousListener l) 
    {
        this.mPotentialRendezvousListeners.add(l);
    }
    
    public void removePotentialRendezvousListener(PotentialRendezvousListener l) 
    {
        this.mPotentialRendezvousListeners.remove(l);
    }
        
    public void firePotentialRendezvousEvent() {
        PotentialRendezvousEvent evt = new PotentialRendezvousEvent(this);

        for (PotentialRendezvousListener l : mPotentialRendezvousListeners) 
        {
            if( l != null)
                l.potentialInvitationEventReceived(evt);
        }
    }
        
    public class PotentialRendezvousEvent extends EventObject {
        private static final long serialVersionUID = 0;
        
        public PotentialRendezvousEvent(Object source) {
            super(source);
        }
    }
    
    // confirmedInvitation
    public interface ConfirmedRendezvousListener {
        void confirmedInvitationEventReceived(ConfirmedRendezvousEvent evt);
    }

    public void addConfirmedRendezvousListener(ConfirmedRendezvousListener l) 
    {
        this.mConfirmedRendezvousListeners.add(l);
    }
    
    public void removeConfirmedRendezvousListener(ConfirmedRendezvousListener l) 
    {
        this.mConfirmedRendezvousListeners.remove(l);
    }
        
    public void fireConfirmedRendezvousEvent() {
        ConfirmedRendezvousEvent evt = new ConfirmedRendezvousEvent(this);

        for (ConfirmedRendezvousListener l : mConfirmedRendezvousListeners) 
        {
            if( l != null)
                l.confirmedInvitationEventReceived(evt);
        }
    }
        
    public class ConfirmedRendezvousEvent extends EventObject {
        private static final long serialVersionUID = 0;
        
        public ConfirmedRendezvousEvent(Object source) {
            super(source);
        }
    }
    
    
	// flirt
	public interface FlirtListener {
		void flirtEventReceived(FlirtEvent evt);
	}

	public void addFlirtListener(FlirtListener l) 
	{
		this.mFlirtListeners.add(l);
	}
	
	public void removeFlirtListener(FlirtListener l) 
	{
	    this.mFlirtListeners.remove(l);
	}
		
	public void fireFlirtEvent() {
		FlirtEvent evt = new FlirtEvent(this);

	    for (FlirtListener l : mFlirtListeners) 
    	{
	    	if( l != null)
	    		l.flirtEventReceived(evt);
	    }
	}
		
	public class FlirtEvent extends EventObject {
		private static final long serialVersionUID = 0;
		
		public FlirtEvent(Object source) {
			super(source);
		}
	}
	
	// message
	public interface MessageListener {
		void messageEventReceived(MessageEvent evt);
	}

	public void addMessageListener(MessageListener l) 
	{
		this.mMessageListeners.add(l);
	}
	
	public void removeMessageListener(MessageListener l) 
	{
	    this.mMessageListeners.remove(l);
	}
		
	public void fireMessageEvent() {
		MessageEvent evt = new MessageEvent(this);

	    for (MessageListener l : mMessageListeners) 
    	{
	    	if( l != null)
	    		l.messageEventReceived(evt);
	    }
	}
		
	public class MessageEvent extends EventObject {
		private static final long serialVersionUID = 0;
		
		public MessageEvent(Object source) {
			super(source);
		}
	}
	
	//vote
	public interface VoteListener {
		void voteEventReceived(VoteEvent evt);
	}

	public void addVoteListener(VoteListener l) 
	{
		this.mVoteListeners.add(l);
	}
	
	public void removeVoteListener(VoteListener l) 
	{
	    this.mVoteListeners.remove(l);
	}
		
	public void fireVoteEvent() {
		VoteEvent evt = new VoteEvent(this);

	    for (VoteListener l : mVoteListeners) 
    	{
	    	if( l != null)
	    		l.voteEventReceived(evt);
	    }
	}
		
	public class VoteEvent extends EventObject {
		private static final long serialVersionUID = 0;
		
		public VoteEvent(Object source) {
			super(source);
		}
	}
	
	
	//visit
	public interface VisitListener {
		void visitEventReceived(VisitEvent evt);
	}

	public void addVisitListener(VisitListener l) 
	{
		this.mVisitListeners.add(l);
	}
	
	public void removeVisitListener(VisitListener l) 
	{
	    this.mVisitListeners.remove(l);
	}
		
	public void fireVisitEvent() {
		VisitEvent evt = new VisitEvent(this);

	    for (VisitListener l : mVisitListeners) 
    	{
	    	if( l != null)
	    		l.visitEventReceived(evt);
	    }
	}
		
	public class VisitEvent extends EventObject {
		private static final long serialVersionUID = 0;
		
		public VisitEvent(Object source) {
			super(source);
		}
	}
}