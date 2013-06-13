package com.signals_app.signals.model.Profile;

import java.util.Calendar;
import java.util.Comparator;

public class Work {
    private String mEmployer;
    private String mPosition;
    private String mLocation;
    private Calendar mStartDate = null;
    private Calendar mEndDate = null;
    
    public static class WorkComparator implements Comparator<Work> {
        @Override
        public int compare(Work o1, Work o2) 
        {	
        	Calendar endDate1 = o1.getEndDate();
        	Calendar endDate2 = o2.getEndDate();
        	
        	if( endDate1 == null )
        		endDate1 = Calendar.getInstance();
        	
        	if( endDate2 == null )
        		endDate2 = Calendar.getInstance();   	
        	
            return endDate1.compareTo(endDate2);
        }
    }
    
    public Work()
    {
    	mEmployer = new String("");
    	mPosition = new String("");
    	mLocation = new String("");
    }
    
   public String getEmployer() {
        return mEmployer;
   }

   public void setEmployer(String employer) {
       this.mEmployer = employer;
   }

   public String getPosition() {
        return mPosition;
   }

   public void setPosition(String position) {
       this.mPosition = position;
   }
   
   public String getLocation() {
       return mLocation;
  }

  public void setLocation(String location) {
      this.mLocation = location;
  }
   
   public Calendar getStartDate() {
        return mStartDate;
   }

   public void setStartDate(Calendar startDate) {
       this.mStartDate = startDate;
   }
   
   public Calendar getEndDate() {
        return mEndDate;
   }

   public void setEndDate(Calendar endDate) {
       this.mEndDate = endDate;
   }
   
}