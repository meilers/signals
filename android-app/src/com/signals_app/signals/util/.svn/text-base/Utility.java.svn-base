package com.signals_app.signals.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.TimeZone;

import android.widget.EditText;
import org.json.JSONObject;


import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.net.Uri;
import android.net.http.AndroidHttpClient;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;

import java.lang.Math;

public class Utility extends Application {

    public static JSONObject mFriendsList;
    public static AndroidHttpClient httpclient = null;
    public static Hashtable<String, String> currentPermissions = new Hashtable<String, String>();

    private static int MAX_IMAGE_DIMENSION = 500;

    static public Calendar convertStrToCalendar( String str, String format )
    {
        str = str.substring(0, 16);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm"); 
        sdf.setTimeZone(TimeZone.getTimeZone("UTC")); 
        
        Date scheduleTime = new Date();
        try
        {
            scheduleTime =  (Date)sdf.parse(str); 
        }
        catch (Exception e)
        {
          e.printStackTrace();
        }
        
        
        Calendar calendar = new GregorianCalendar();
        calendar.setFirstDayOfWeek(Calendar.SUNDAY); 
        calendar.setTime(scheduleTime);

        return calendar;
    }
    
    
    public static Bitmap getBitmap(String url) {
        Bitmap bm = null;
        try {
            URL aURL = new URL(url);
            URLConnection conn = aURL.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            bm = BitmapFactory.decodeStream(new FlushedInputStream(is));
            bis.close();
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (httpclient != null) {
                httpclient.close();
            }
        }
        
        return bm;
    }
    
    public static Bitmap cropBitmap(Bitmap bm, int width, int height) 
    {
    	Bitmap result;
    	
    	int size = Math.min(height, width);
    	
        if( width > height )
        {
        	result = Bitmap.createBitmap(bm, (width-height)/2,0,size, size);
        }
        else
        {
        	result = Bitmap.createBitmap(bm,0,(height-width)/2,size,size );
        }
        
        return result;
    }

    static class FlushedInputStream extends FilterInputStream {
        public FlushedInputStream(InputStream inputStream) {
            super(inputStream);
        }

        @Override
        public long skip(long n) throws IOException {
            long totalBytesSkipped = 0L;
            while (totalBytesSkipped < n) {
                long bytesSkipped = in.skip(n - totalBytesSkipped);
                if (bytesSkipped == 0L) {
                    int b = read();
                    if (b < 0) {
                        break; // we reached EOF
                    } else {
                        bytesSkipped = 1; // we read one byte
                    }
                }
                totalBytesSkipped += bytesSkipped;
            }
            return totalBytesSkipped;
        }
    }
    
    public static byte[] decodeFile(File f) throws IOException{
        try {
            //Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f),null,o);

            //Find the correct scale value. It should be the power of 2.
            int scale=1;
            while(o.outWidth/scale/2>=MAX_IMAGE_DIMENSION && o.outHeight/scale/2>=MAX_IMAGE_DIMENSION)
                scale*=2;

            //Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize=scale;
            Bitmap bm = BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
            
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 80, baos);
            byte[] bMapArray = baos.toByteArray();
            baos.close();
            return bMapArray;
            
        } catch (FileNotFoundException e) {}
        return null;
    }

    public static byte[] scaleImage(Context context, Uri photoUri) throws IOException {
        InputStream is = context.getContentResolver().openInputStream(photoUri);
        BitmapFactory.Options dbo = new BitmapFactory.Options();
        dbo.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(is, null, dbo);
        is.close();

        int rotatedWidth, rotatedHeight;
        int orientation = getOrientation(context, photoUri);

        if (orientation == 90 || orientation == 270) {
            rotatedWidth = dbo.outHeight;
            rotatedHeight = dbo.outWidth;
        } else {
            rotatedWidth = dbo.outWidth;
            rotatedHeight = dbo.outHeight;
        }

        Bitmap srcBitmap;
        is = context.getContentResolver().openInputStream(photoUri);
        if (rotatedWidth > MAX_IMAGE_DIMENSION || rotatedHeight > MAX_IMAGE_DIMENSION) {
            float widthRatio = ((float) rotatedWidth) / ((float) MAX_IMAGE_DIMENSION);
            float heightRatio = ((float) rotatedHeight) / ((float) MAX_IMAGE_DIMENSION);
            float maxRatio = Math.max(widthRatio, heightRatio);

            // Create the bitmap from file
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = (int) maxRatio;
            srcBitmap = BitmapFactory.decodeStream(is, null, options);
        } else {
            srcBitmap = BitmapFactory.decodeStream(is);
        }
        is.close();

        /*
         * if the orientation is not 0 (or -1, which means we don't know), we
         * have to do a rotation.
         */
        if (orientation > 0) {
            Matrix matrix = new Matrix();
            matrix.postRotate(orientation);

            srcBitmap = Bitmap.createBitmap(srcBitmap, 0, 0, srcBitmap.getWidth(),
                    srcBitmap.getHeight(), matrix, true);
        }

        String type = context.getContentResolver().getType(photoUri);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if (type.equals("image/png")) {
            srcBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        } else if (type.equals("image/jpg") || type.equals("image/jpeg")) {
            srcBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        }
        byte[] bMapArray = baos.toByteArray();
        baos.close();
        return bMapArray;
    }

    public static Bitmap loadBitmapFromView(View v) {
        Bitmap b = Bitmap.createBitmap( v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);                
        Canvas c = new Canvas(b);
        v.layout(0, 0, v.getLayoutParams().width, v.getLayoutParams().height);
        v.draw(c);
        return b;
    }
    
    
    public static int getOrientation(Context context, Uri photoUri) {
        /* it's on the external media. */
        Cursor cursor = context.getContentResolver().query(photoUri,
                new String[] { MediaStore.Images.ImageColumns.ORIENTATION }, null, null, null);

        if (cursor.getCount() != 1) {
            return -1;
        }

        cursor.moveToFirst();
        return cursor.getInt(0);
    }

    public static boolean isEmpty(String str) {
        if (str.trim().length() > 0) {
            return false;
        } else {
            return true;
        }
    }

    public static double getDistance(double lat1, double lng1, double lat2, double lng2, String unit) 
    { 
    	  double theta = lng1 - lng2; 
    	  double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) +  Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta)); 
    	  dist = Math.acos(dist); 
    	  dist = rad2deg(dist); 
    	  double miles = dist * 60 * 1.1515;
    	  unit = unit.toUpperCase();

    	  if (unit == "K") {
    	    return (miles * 1.609344); 
    	  } else if (unit == "N") {
    	      return (miles * 0.8684);
    	    } else {
    	        return miles;
    	      }
    	}
    
    private static double deg2rad(double deg) {
    	
    	return (deg * Math.PI / 180.0);
    }
    
    private static double rad2deg(double rad) {
    	return (rad * 180.0 / Math.PI);
    }
    
    
	@SuppressLint("SimpleDateFormat")
	public static int getAge( Calendar dob )
	{
		Calendar today = Calendar.getInstance();  
		
		int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);  
		if (today.get(Calendar.MONTH) < dob.get(Calendar.MONTH)) {
		  age--;  
		} else if (today.get(Calendar.MONTH) == dob.get(Calendar.MONTH)
		    && today.get(Calendar.DAY_OF_MONTH) < dob.get(Calendar.DAY_OF_MONTH)) {
		  age--;  
		}
		
		return age;
	}
	
	
	public static float convertDpToPixel(float dp,Context context){
	    Resources resources = context.getResources();
	    DisplayMetrics metrics = resources.getDisplayMetrics();
	    float px = dp * (metrics.densityDpi/160f);
	    return px;
	}
	
	public static float convertPixelsToDp(float px,Context context){
	    Resources resources = context.getResources();
	    DisplayMetrics metrics = resources.getDisplayMetrics();
	    float dp = px / (metrics.densityDpi / 160f);
	    return dp;

	}
}
