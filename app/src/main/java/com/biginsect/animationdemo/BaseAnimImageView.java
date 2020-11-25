package com.biginsect.animationdemo;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * @author lipeng
 * Created at 2020/11/25 15:51
 */
public class BaseAnimImageView extends AppCompatImageView {

    private int mCurrentTouchX;
    private int mCurrentTouchY;

    protected OnAnimEndListener mEndListener;

    public BaseAnimImageView(Context context) {
        this(context, null);
    }

    public BaseAnimImageView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public BaseAnimImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        prepare();
    }

    protected void prepare(){

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        mCurrentTouchX = (int) event.getRawX();
        mCurrentTouchY = (int) event.getRawY();
        return super.dispatchTouchEvent(event);
    }

    protected boolean isTouchInView() {
        int[] location = new int[2];
        getLocationOnScreen(location);
        int left = location[0];
        int top = location[1];
        int right = left + getMeasuredWidth();
        int bottom = top + getMeasuredHeight();
        return mCurrentTouchX >= left && mCurrentTouchX <= right && mCurrentTouchY >= top && mCurrentTouchY <= bottom;
    }

    public void setOnAnimEndListener(OnAnimEndListener listener){
        this.mEndListener = listener;
    }

    public interface OnAnimEndListener{
        void onAnimEnd();
    }
}
