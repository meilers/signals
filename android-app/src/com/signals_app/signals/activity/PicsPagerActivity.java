package com.signals_app.signals.activity;

import java.util.ArrayList;
import java.util.Vector;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.signals_app.signals.R;
import com.signals_app.signals.model.Profile.Person;
import com.signals_app.signals.model.Profile.User;
import com.signals_app.signals.util.Utility;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
 
public class PicsPagerActivity extends SherlockFragmentActivity 
{

	private DisplayImageOptions mImageOptions;
	
	
	private ViewPager mViewPager;
	private PicsAdapter mViewPagerAdapter;;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Bundle extras = getIntent().getExtras(); 
		
		if( savedInstanceState != null )
		    extras = savedInstanceState;
		
		int pos = extras.getInt("pos");
		ArrayList<String> urls = extras.getStringArrayList("urls");
		
		
		// IMAGE LOADER
		mImageOptions = new DisplayImageOptions.Builder()
		.cacheInMemory()
		.cacheOnDisc()
		.build();
		

		
		mViewPager = new ViewPager(this);
		setContentView(mViewPager);
		
		mViewPagerAdapter = new PicsAdapter(PicsPagerActivity.this,  urls);
		mViewPager.setAdapter(mViewPagerAdapter);
		mViewPager.setCurrentItem(pos);
		mViewPager.setOnPageChangeListener(new DetailOnPageChangeListener()); 

		setTitle(extras.getString("username"));
        getSupportActionBar().setSubtitle(getString(R.string.photo) + " " + (pos+1) + "/" + urls.size());
	}

	@Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        int position = mViewPager.getCurrentItem();
        savedInstanceState.putInt("pos", position);
    }
	
	
	public class PicsAdapter extends PagerAdapter 
	{
		private final ArrayList<String> mUrls;
		private final Context mContext;

		public PicsAdapter(Context context, ArrayList<String> urls) {
			mUrls = urls;
			mContext = context;
		}

		@Override
		public int getCount() { 
			return null != mUrls ? mUrls.size() : 0;
		}

		@Override
		public View instantiateItem(ViewGroup container, final int position) 
		{
		    
		    
        	ImageView imageView = new ImageView(mContext);
        	imageView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        	ImageLoader.getInstance().displayImage(mUrls.get(position), imageView, mImageOptions, new SimpleImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {
                    
                    if( PicsPagerActivity.this != null )
                        PicsPagerActivity.this.setSupportProgressBarIndeterminateVisibility(true);
                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    if( this != null )
                        PicsPagerActivity.this.setSupportProgressBarIndeterminateVisibility(false);
                }
                
                @Override
                public void onLoadingCancelled(String imageUri, View view) {
                    if( PicsPagerActivity.this != null )
                        PicsPagerActivity.this.setSupportProgressBarIndeterminateVisibility(false);
                }

                @Override
                public void onLoadingComplete(String imageUri,View view, Bitmap loadedImage) {
                    
                    
                    if( PicsPagerActivity.this != null )
                        PicsPagerActivity.this.setSupportProgressBarIndeterminateVisibility(false);
                }
            });
			container.addView(imageView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

			return imageView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}
	}
	
	public class DetailOnPageChangeListener extends ViewPager.SimpleOnPageChangeListener {

        private int currentPage;

        @Override
        public void onPageSelected(int position) {

            getSupportActionBar().setSubtitle(getString(R.string.photo) + " " + (position+1) + "/" + getIntent().getExtras().getStringArrayList("urls").size());
            currentPage = position;
        }

        public int getCurrentPage() {
            return currentPage;
        }
    }

}
