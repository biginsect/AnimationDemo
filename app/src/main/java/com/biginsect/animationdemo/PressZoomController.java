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
    private SpringAnimation mReboundAnimY;

    private SpringAnimation mRX;
    private SpringAnimation mRY;

    public PressZoomController(@NonNull View targetView) {
        mTargetView = targetView;
        prepare();
    }

    private void prepare() {
        SpringForce springForce = new SpringForce();
        springForce.setDampingRatio(SpringForce.DAMPING_RATIO_HIGH_BOUNCY)
                .setStiffness(370f)
                .setFinalPosition(0.8f);
        mReboundAnimX = new SpringAnimation(mTargetView, SpringAnimation.SCALE_X)
                .setMinimumVisibleChange(SpringAnimation.MIN_VISIBLE_CHANGE_ALPHA)
                .setStartValue(1.0f)
                .setSpring(springForce)
                .addEndListener(new DynamicAnimation.OnAnimationEndListener() {
                    @Override
                    public void onAnimationEnd(DynamicAnimation animation, boolean canceled, float value, float velocity) {
                        mRX.start();
                        mRY.start();
                    }
                });

        mReboundAnimY = new SpringAnimation(mTargetView, SpringAnimation.SCALE_Y)
                .setMinimumVisibleChange(SpringAnimation.MIN_VISIBLE_CHANGE_ALPHA)
                .setStartValue(1.0f)
                .setSpring(springForce);

        SpringForce force = new SpringForce()
                .setDampingRatio(SpringForce.DAMPING_RATIO_HIGH_BOUNCY)
                .setStiffness(370f)
                .setFinalPosition(1.0f);
        mRX = new SpringAnimation(mTargetView, SpringAnimation.SCALE_X)
                .setMinimumVisibleChange(SpringAnimation.MIN_VISIBLE_CHANGE_ALPHA)
                .setStartValue(0.8f)
                .setSpring(force);

        mRY = new SpringAnimation(mTargetView, SpringAnimation.SCALE_Y)
                .setMinimumVisibleChange(SpringAnimation.MIN_VISIBLE_CHANGE_ALPHA)
                .setStartValue(0.8f)
                .setSpring(force);
    }

    public void playAnim() {
        mReboundAnimX.start();
        mReboundAnimY.start();
    }
}
