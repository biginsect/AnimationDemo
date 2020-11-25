package com.biginsect.animationdemo;

import android.content.Context;
import android.support.animation.DynamicAnimation;
import android.support.animation.SpringAnimation;
import android.support.animation.SpringForce;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * @author lipeng
 * Created at 2020/11/24 20:53
 */
public class RotationImageView extends AppCompatImageView {

    private SpringAnimation mRotationS;
    private SpringAnimation mRotationE;

    public RotationImageView(Context context) {
        this(context, null);
    }

    public RotationImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RotationImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        prepareS();
        prepareE();
    }

    private void prepareS() {
        SpringForce force = new SpringForce()
                .setDampingRatio(SpringForce.DAMPING_RATIO_HIGH_BOUNCY)
                .setStiffness(370f)
                .setFinalPosition(-22f);
        mRotationS = new SpringAnimation(this, SpringAnimation.ROTATION)
                .setMinimumVisibleChange(SpringAnimation.MIN_VISIBLE_CHANGE_ALPHA)
                .setStartValue(0.f)
                .setSpring(force);
        mRotationS.addEndListener(new DynamicAnimation.OnAnimationEndListener() {
            @Override
            public void onAnimationEnd(DynamicAnimation animation, boolean canceled, float value, float velocity) {
                mRotationE.start();
            }
        });
    }

    private void prepareE() {
        SpringForce force = new SpringForce()
                .setDampingRatio(SpringForce.DAMPING_RATIO_HIGH_BOUNCY)
                .setStiffness(370f)
                .setFinalPosition(0.f);
        mRotationE = new SpringAnimation(this, SpringAnimation.ROTATION)
                .setMinimumVisibleChange(SpringAnimation.MIN_VISIBLE_CHANGE_ALPHA)
                .setStartValue(-22f)
                .setSpring(force);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            mRotationS.start();
        }
        return super.onTouchEvent(event);
    }
}
