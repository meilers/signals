package com.signals_app.signals.activity;

import java.util.EventObject;
import java.util.concurrent.CopyOnWriteArrayList;

import com.signals_app.signals.R;
import com.signals_app.signals.util.BitmapScaler;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;

public class MyImageView extends ImageView {

	private static final int INVALID_POINTER_ID = -1;

	private static final int MAX_WIDTH = 100;
	private static final int MAX_HEIGHT = 100;
	
	private CopyOnWriteArrayList<MoveListener> moveListeners_;
	
    private Drawable mImage;
    private boolean mCanMove = false;
     
    private float mMaxWidth;
    private float mMaxHeight;
    private int mImageWidth;
    private int mImageHeight;
    
    private float mPosX = 0;
    private float mPosY = 0;
    private float mLastTouchX;
    private float mLastTouchY;
     
    private int mActivePointerId = INVALID_POINTER_ID;

    public MyImageView(Context context) {
        this(context, null, 0);
    }

    public MyImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
 
    public MyImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        
        moveListeners_ = new CopyOnWriteArrayList<MoveListener>();
        
        Resources r = context.getResources();
        mMaxWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, MAX_WIDTH, r.getDisplayMetrics());
        mMaxHeight = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, MAX_HEIGHT, r.getDisplayMetrics());
        
        // Default image
        Bitmap bm = BitmapFactory.decodeResource(r, R.drawable.user_l);
		BitmapScaler bmScaler = new BitmapScaler(bm, mMaxHeight);
		bm = bmScaler.getScaled();
    	
    	mImage = new BitmapDrawable(this.getResources(), bm);
        mImage.setBounds(0, 0, mImage.getIntrinsicWidth(), mImage.getIntrinsicHeight());
        
        mImageWidth = mImage.getIntrinsicWidth();
        mImageHeight = mImage.getIntrinsicHeight();
    
    }


    
    @Override
    public boolean onTouchEvent(MotionEvent ev) 
    {
    	if(mCanMove)
    	{
	        final int action = ev.getAction();
	        switch (action & MotionEvent.ACTION_MASK) {
	        case MotionEvent.ACTION_DOWN: {
	            final float x = ev.getX();
	            final float y = ev.getY();
	
	            mLastTouchX = x;
	            mLastTouchY = y;
	            mActivePointerId = ev.getPointerId(0);
	            break;
	        }
	
	        case MotionEvent.ACTION_MOVE: {
	            final int pointerIndex = ev.findPointerIndex(mActivePointerId);
	            final float x = ev.getX(pointerIndex);
	            final float y = ev.getY(pointerIndex);
	            final float dx = x - mLastTouchX;
	            final float dy = y - mLastTouchY;
	
	            if( (mPosX+dx) > 0 || (mPosX+dx) + mImageWidth  < mMaxWidth )
	            	;
	            else
	            {
	            	mPosX += dx;
	            }
	
	            if( (mPosY+dy) > 0 || (mPosY+dy) + mImageHeight  < mMaxHeight )
	            	;
	            else
	            {
	            	mPosY += dy;
	            }
	            
	        	fireMoveEvent();
	            invalidate();
	
	            mLastTouchX = x;
	            mLastTouchY = y;
	
	            break;
	        }
	
	        case MotionEvent.ACTION_UP: {
	            mActivePointerId = INVALID_POINTER_ID;
	            break;
	        }
	
	        case MotionEvent.ACTION_CANCEL: {
	            mActivePointerId = INVALID_POINTER_ID;
	            break;
	        }
	
	        case MotionEvent.ACTION_POINTER_UP: {
	            final int pointerIndex = (ev.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) 
	                    >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
	            final int pointerId = ev.getPointerId(pointerIndex);
	            if (pointerId == mActivePointerId) {
	                // This was our active pointer going up. Choose a new
	                // active pointer and adjust accordingly.
	                final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
	                mLastTouchX = ev.getX(newPointerIndex);
	                mLastTouchY = ev.getY(newPointerIndex);
	                mActivePointerId = ev.getPointerId(newPointerIndex);
	            }
	            break;
	        }
	        }
    	}
        return true;
    }

    @Override
    public void onDraw(Canvas canvas) {
    	
    	super.onDraw(canvas);
        
        canvas.save();
        canvas.translate(mPosX, mPosY);
        mImage.draw(canvas);
        
        canvas.restore();
        

    }
    
    public void setMyImage(Bitmap bm)
    {
    	if( bm != null )
    	{
			BitmapScaler bmScaler = new BitmapScaler(bm, mMaxHeight);
			bm = bmScaler.getScaled();
			
	    	mImage = new BitmapDrawable(this.getResources(), bm);
	        mImage.setBounds(0, 0, mImage.getIntrinsicWidth(), mImage.getIntrinsicHeight());
	        mImageWidth = mImage.getIntrinsicWidth();
	        mImageHeight = mImage.getIntrinsicHeight();
    	}
    }
    
    
    public boolean canMove()
    {
    	return mCanMove;
    }
    
    public void setCanMove(boolean canMove)
    {
    	mCanMove = canMove;
    }

    public float getPosX()
    {
    	return mPosX;
    }
    
    public void setPosX(float posX)
    {
    	if( posX + mImageWidth < mMaxWidth || posX > 0)
    		posX = 0;
    	
    	mPosX = posX;
    }

    public float getPosY()
    {	
    	return mPosY;
    }
    
    public void setPosY(float posY)
    {
    	if( posY + mImageHeight < mMaxHeight || posY > 0)
    		posY = 0;
    	
    	mPosY = posY;
    }
    
	public void addMoveListener(MoveListener l) 
	{
		this.moveListeners_.add(l);
	}

	public void removeMoveListener(MoveListener l) 
	{
	    this.moveListeners_.remove(l);
	}
	
	public void fireMoveEvent() {
		MoveEvent evt = new MoveEvent(this);

	    for (MoveListener l : moveListeners_) 
    	{
	    	if( l != null)
	    		l.moveEventReceived(evt);
	    }
	}
	
	public class MoveEvent extends EventObject {
		  // This event definition is stateless but you could always
		  // add other information here.
		  public MoveEvent(Object source) {
		    super(source);
		  }
		}
	
	public interface MoveListener {
		void moveEventReceived(MoveEvent evt);
	}
}
