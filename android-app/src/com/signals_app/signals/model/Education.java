package com.signals_app.signals.model;

import java.util.Comparator;

public class Education implements Comparable<Education>{

    private Integer mYear;
    private String mType;
    private String mSchoolName;
    private String mDegree;
    private String mConcentration;
    
    public static class EducationComparator implements Comparator<Education> {
        @Override
        public int compare(Education o1, Education o2) 
        {
        	if( o1.getYear().equals(0) || o2.getYear().equals(0))
        		return o1.compareTo(o2);
        	
            return o2.getYear().compareTo(o1.getYear());
        }
    }

	public int compareTo(Education compareEducation) {
		 
		int myValue = 4, otherValue = 4;
		
		if( compareEducation.getType().equals("High School"))
			otherValue = 3;
		else if( compareEducation.getType().equals("College"))
			otherValue = 2;
		else if( compareEducation.getType().equals("Graduate School"))
			otherValue = 1;
		
		if( this.getType().equals("High School"))
			myValue = 3;
		else if( this.getType().equals("College"))
			myValue = 2;
		else if( this.getType().equals("Graduate School"))
			myValue = 1;
		
		return (myValue - otherValue);

	}
	
    public Education()
    {
    	mType = new String("");
    	mYear = Integer.valueOf(0);
    	mSchoolName = new String("");
    	mDegree = new String("");
    	mConcentration = new String("");
    }
    
    public String getType() {
        return mType;
   }

   public void setType(String type) {
       this.mType = type;
   }
   
   public Integer getYear() {
        return mYear;
   }

   public void setYear(Integer year) {
       this.mYear = year;
   }

   public String getSchoolName() {
        return mSchoolName;
   }

   public void setSchoolName(String schoolName) {
       this.mSchoolName = schoolName;
   }
   
   public String getDegree() {
        return mDegree;
   }

   public void setDegree(String degree) {
       this.mDegree = degree;
   }
   
   public String getConcentration() {
        return mConcentration;
   }

   public void setConcentration(String concentration) {
       this.mConcentration = concentration;
   }
}