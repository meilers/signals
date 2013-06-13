package com.signals_app.signals.activity;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.concurrent.CopyOnWriteArrayList;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.signals_app.signals.model.Profile.Person;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.StrictMode;

public class SignalsApplication extends Application 
{
	private PlacesViewState mPlacesViewState; 
	
	@Override
	public void onCreate() {
		super.onCreate();

        mPlacesViewState = new PlacesViewState(); 
	        
	        
		// IMAGE LOADER
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build());
			StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyLog().build());
		}

		initImageLoader(getApplicationContext());
		

	}

	public static SignalsApplication getApplication(Context context) 
	{
		return (SignalsApplication) context.getApplicationContext();
	}
	
	public static void initImageLoader(Context context) {
		// This configuration tuning is custom. You can tune every option, you may tune some of them, 
		// or you can create default configuration by
		//  ImageLoaderConfiguration.createDefault(this);
		// method.
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
			.threadPriority(Thread.NORM_PRIORITY - 2)
			.memoryCacheSize(2 * 1024 * 1024) // 2 Mb
			.denyCacheImageMultipleSizesInMemory()
			.discCacheFileNameGenerator(new Md5FileNameGenerator())
			.tasksProcessingOrder(QueueProcessingType.LIFO)
			
			.build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	}
	
	
	public void init()
	{
        mPlacesViewState = new PlacesViewState(); 
	}
	
	
	
	public PlacesViewState getPlacesViewState() {
		return mPlacesViewState;
	}
	
	
	public class PlacesViewState 
	{
		private boolean mClubsBtnClicked = false;
		private boolean mLoungesBtnClicked = false;
		private boolean mBarsBtnClicked = false;
		private boolean mPeopleBtnClicked = false;
		
		   
		public boolean clubsBtnClicked() {
			return mClubsBtnClicked;
		}

		public void setClubsBtnClicked(boolean clubsBtnClicked) {
			this.mClubsBtnClicked = clubsBtnClicked;
		}
		
		public boolean loungesBtnClicked() {
			return mLoungesBtnClicked;
		}

		public void setLoungesBtnClicked(boolean loungesBtnClicked) {
			this.mLoungesBtnClicked = loungesBtnClicked;
		}
		
		public boolean barsBtnClicked() {
			return mBarsBtnClicked;
		}

		public void setBarsBtnClicked(boolean barsBtnClicked) {
			this.mBarsBtnClicked = barsBtnClicked;
		}
		
		public boolean peopleBtnClicked() {
			return mPeopleBtnClicked;
		}

		public void setPeopleBtnClicked(boolean peopleBtnClicked) {
			this.mPeopleBtnClicked = peopleBtnClicked;
		}
	};
	
	
    
}
