package com.biginsect.animationdemo;

import android.support.animation.DynamicAnimation;
import android.support.animation.SpringAnimation;
import android.support.animation.SpringForce;
import android.support.annotation.NonNull;
import android.view.View;

/**
 * @author lipeng
 * Created at 2020/11/20 17:35
 */
public class PressZoomController {

    private View mTargetView;

    private SpringAnimation mReboundAnimX;
    private SpringAnimation mmReboundAnimY;

    public PressZoomController(@NonNull View targetView) {
        mTargetView = targetView;
        prepare();
    }

    private void prepare() {
        SpringForce springForce = new SpringForce();
        springForce.setDampingRatio(SpringForce.DAMPING_RATIO_HIGH_BOUNCY)
                .setStiffness(370f)
                .setFinalPosition(2.0f);
        mReboundAnimX = new SpringAnimation(mTargetView, SpringAnimation.SCALE_X);
        mReboundAnimX.setMinimumVisibleChange(SpringAnimation.MIN_VISIBLE_CHANGE_ALPHA);
        mReboundAnimX.setStartValue(1.0f);
        mReboundAnimX.setSpring(springForce);

        mmReboundAnimY = new SpringAnimation(mTargetView, SpringAnimation.SCALE_Y);
        mmReboundAnimY.setMinimumVisibleChange(SpringAnimation.MIN_VISIBLE_CHANGE_ALPHA);
        mmReboundAnimY.setStartValue(1.0f);
        mmReboundAnimY.setSpring(springForce);
        mReboundAnimX.addEndListener(new DynamicAnimation.OnAnimationEndListener() {
            @Override
            public void onAnimationEnd(DynamicAnimation animation, boolean canceled, float value, float velocity) {
                mmReboundAnimY.start();
            }
        });
    }

    public void playAnim() {
        mReboundAnimX.start();
    }
}
