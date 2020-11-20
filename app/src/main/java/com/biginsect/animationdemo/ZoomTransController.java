package com.biginsect.animationdemo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.widget.ImageView;

/**
 * 缩放 + 渐变动画
 *
 * @author lipeng
 * Created at 2020/11/19 16:52
 */
public class ZoomTransController {

    private final static long DURATION_DEFAULT = 150L;

    private ImageView mTargetView;
    private AnimatorSet mZoomOutDisappear;
    private AnimatorSet mZoomInAppear;

    private int mCurrentResId;
    private int mNextResId;

    private boolean mSwitchEnabled = true;
    //用于切换背景图片
    private boolean mIsCurrent;

    public ZoomTransController(@NonNull ImageView targetView) {
        mTargetView = targetView;
    }

    public void addSwitchResource(@DrawableRes int current, @DrawableRes int another) {
        mCurrentResId = current;
        mNextResId = another;
        if (mTargetView != null) {
            mTargetView.setImageResource(mCurrentResId);
        }
        mIsCurrent = true;
    }

    public void setAnimDuration(long totalDuration) {
        if (totalDuration <= 0) {
            return;
        }
        long duration = totalDuration / 2L;
        mZoomInAppear.setDuration(duration);
        mZoomOutDisappear.setDuration(duration);
    }

    public void setSwitchEnable(boolean isEnabled) {
        mSwitchEnabled = isEnabled;
    }

    public void playAnim() {
        if (mTargetView == null) {
            return;
        }

        prepare();
        if (mSwitchEnabled) {
            mIsCurrent = !mIsCurrent;
        }

        mZoomOutDisappear.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mTargetView.setImageResource(mIsCurrent ? mCurrentResId : mNextResId);
                mZoomInAppear.start();
            }
        });
        mZoomOutDisappear.start();
    }

    public void cancelAnim() {
        if (mZoomOutDisappear != null) {
            mZoomOutDisappear.cancel();
        }
        if (mZoomInAppear != null) {
            mZoomInAppear.cancel();
        }
    }

    private void prepare() {
        prepareZoomOutDisappear();
        prepareZoomInAppear();
    }

    private void prepareZoomOutDisappear() {
        if (mZoomOutDisappear != null) {
            return;
        }
        ObjectAnimator animatorX = ObjectAnimator.ofFloat(mTargetView, "scaleX", 1f, 0.8f);
        ObjectAnimator animatorY = ObjectAnimator.ofFloat(mTargetView, "scaleY", 1f, 0.8f);
        ObjectAnimator animatorAlpha = ObjectAnimator.ofFloat(mTargetView, "alpha", 1f, 0f);

        mZoomOutDisappear = new AnimatorSet();
        mZoomOutDisappear.play(animatorX).with(animatorY).with(animatorAlpha);
        mZoomOutDisappear.setDuration(DURATION_DEFAULT);
    }

    private void prepareZoomInAppear() {
        if (mZoomInAppear != null) {
            return;
        }
        ObjectAnimator animatorX = ObjectAnimator.ofFloat(mTargetView, "scaleX", 0.8f, 1f);
        ObjectAnimator animatorY = ObjectAnimator.ofFloat(mTargetView, "scaleY", 0.8f, 1f);
        ObjectAnimator animatorAlpha = ObjectAnimator.ofFloat(mTargetView, "alpha", 0f, 1f);

        mZoomInAppear = new AnimatorSet();
        mZoomInAppear.play(animatorX).with(animatorY).with(animatorAlpha);
        mZoomInAppear.setDuration(DURATION_DEFAULT);
    }
}
