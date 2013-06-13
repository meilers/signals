package com.signals_app.signals.model;

import java.util.Comparator;
import java.util.Date;

public class Work {
    private String mEmployer;
    private String mPosition;
    private String mLocation;
    private Date mStartDate = null;
    private Date mEndDate = null;
    
    public static class WorkComparator implements Comparator<Work> {
        @Override
        public int compare(Work o1, Work o2) 
        {	
        	Date endDate1 = o1.getEndDate();
        	Date endDate2 = o2.getEndDate();
        	
        	if( endDate1 == null )
        		endDate1 = new Date();
        	
        	if( endDate2 == null )
        		endDate2 = new Date();    	
        	
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
   
   public Date getStartDate() {
        return mStartDate;
   }

   public void setStartDate(Date startDate) {
       this.mStartDate = startDate;
   }
   
   public Date getEndDate() {
        return mEndDate;
   }

   public void setEndDate(Date endDate) {
       this.mEndDate = endDate;
   }
   
}