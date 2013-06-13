package com.signals_app.signals.model;

public class PlacesViewState {
	private static PlacesViewState instance_ = null;
	 
	private boolean clubBtnClicked_;
	private boolean loungeBtnClicked_;
	private boolean barBtnClicked_;
	private boolean peopleBtnClicked_;
	
	protected PlacesViewState()
	{
	}
	
	public static PlacesViewState getInstance() 
	{
		if(instance_ == null) {
			instance_ = new PlacesViewState();
         
			instance_.clubBtnClicked_ = false;
			instance_.loungeBtnClicked_ = false;
			instance_.barBtnClicked_ = false;
			instance_.peopleBtnClicked_ = false;
		}
		
		return instance_;
   }
	   
	public boolean clubBtnClicked() {
		return clubBtnClicked_;
	}

	public void setClubBtnClicked_(boolean clubBtnClicked) {
		this.clubBtnClicked_ = clubBtnClicked;
	}
	
	public boolean loungeBtnClicked() {
		return loungeBtnClicked_;
	}

	public void setLoungeBtnClicked_(boolean loungeBtnClicked) {
		this.loungeBtnClicked_ = loungeBtnClicked;
	}
	
	public boolean barBtnClicked() {
		return barBtnClicked_;
	}

	public void setBarBtnClicked_(boolean barBtnClicked) {
		this.barBtnClicked_ = barBtnClicked;
	}
	
	public boolean peopleBtnClicked() {
		return peopleBtnClicked_;
	}

	public void setPeopleBtnClicked_(boolean peopleBtnClicked) {
		this.peopleBtnClicked_ = peopleBtnClicked;
	}
}
