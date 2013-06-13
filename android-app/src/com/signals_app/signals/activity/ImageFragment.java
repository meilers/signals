package com.signals_app.signals.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

import com.actionbarsherlock.app.SherlockFragment;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

public class ImageFragment extends SherlockFragment
{
    String mUrl = "";
    private DisplayImageOptions mImageOptions;
    
    public static ImageFragment newInstance(String url)
    {
        ImageFragment f = new ImageFragment();
        Bundle args = new Bundle();
        args.putString("url", url);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mUrl = getArguments().getString("url");
        
        mImageOptions = new DisplayImageOptions.Builder()
        .cacheInMemory()
        .build();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ImageView imageView = new ImageView(getSherlockActivity().getApplicationContext());
        imageView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        ImageLoader.getInstance().displayImage(mUrl, imageView, mImageOptions, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                
                if( getSherlockActivity() != null )
                    getSherlockActivity().setSupportProgressBarIndeterminateVisibility(true);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                if( getSherlockActivity() != null )
                    getSherlockActivity().setSupportProgressBarIndeterminateVisibility(false);
            }
            
            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                if( getSherlockActivity() != null )
                    getSherlockActivity().setSupportProgressBarIndeterminateVisibility(false);
            }

            @Override
            public void onLoadingComplete(String imageUri,View view, Bitmap loadedImage) {
                
                
                if( getSherlockActivity() != null )
                    getSherlockActivity().setSupportProgressBarIndeterminateVisibility(false);
            }
        });

        return imageView;
    }
}