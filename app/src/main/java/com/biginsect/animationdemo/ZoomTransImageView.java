package com.biginsect.animationdemo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.support.annotation.DrawableRes;
import android.support.annotation.RawRes;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

/**
 * 缩放 + 渐变动画
 *
 * @author lipeng
 * Created at 2020/11/19 16:52
 */
public class ZoomTransImageView extends AppCompatImageView {

    private final static long DURATION_DEFAULT = 150L;
    private final static int ID_NO_SOUND = 0;

    private AnimatorSet mZoomOutDisappear;
    private AnimatorSet mZoomInAppear;
    private SoundPool mSoundPool;

    private int mCurrentResId;
    private int mNextResId;
    private int mSoundStreamId;

    private boolean mSwitchEnabled = true;
    //用于切换背景图片
    private boolean mIsCurrent;

    public ZoomTransImageView(Context context) {
        this(context, null);
    }

    public ZoomTransImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ZoomTransImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        prepareAnimation();
    }

    private void prepareSound() {
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build();
        mSoundPool = new SoundPool.Builder()
                .setAudioAttributes(audioAttributes)
                .build();
    }

    private void prepareAnimation() {
        prepareZoomOutDisappear();
        prepareZoomInAppear();
    }

    private void prepareZoomOutDisappear() {
        if (mZoomOutDisappear != null) {
            return;
        }
        ObjectAnimator animatorX = ObjectAnimator.ofFloat(this, "scaleX", 1f, 0.8f);
        ObjectAnimator animatorY = ObjectAnimator.ofFloat(this, "scaleY", 1f, 0.8f);
        ObjectAnimator animatorAlpha = ObjectAnimator.ofFloat(this, "alpha", 1f, 0f);

        mZoomOutDisappear = new AnimatorSet();
        mZoomOutDisappear.play(animatorX).with(animatorY).with(animatorAlpha);
        mZoomOutDisappear.setDuration(DURATION_DEFAULT);
        mZoomOutDisappear.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                setImageResource(mIsCurrent ? mCurrentResId : mNextResId);
                mZoomInAppear.start();
            }
        });
    }

    private void prepareZoomInAppear() {
        if (mZoomInAppear != null) {
            return;
        }
        ObjectAnimator animatorX = ObjectAnimator.ofFloat(this, "scaleX", 0.8f, 1f);
        ObjectAnimator animatorY = ObjectAnimator.ofFloat(this, "scaleY", 0.8f, 1f);
        ObjectAnimator animatorAlpha = ObjectAnimator.ofFloat(this, "alpha", 0f, 1f);

        mZoomInAppear = new AnimatorSet();
        mZoomInAppear.play(animatorX).with(animatorY).with(animatorAlpha);
        mZoomInAppear.setDuration(DURATION_DEFAULT);
    }

    public void addSwitchResource(@DrawableRes int current, @DrawableRes int another) {
        mCurrentResId = current;
        mNextResId = another;
        setImageResource(mCurrentResId);
        mIsCurrent = true;
    }

    public void addSoundResource(@RawRes int soundResId) {
        mSoundStreamId = mSoundPool.load(getContext(), soundResId, 1);
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
        if (mSwitchEnabled) {
            mIsCurrent = !mIsCurrent;
        }

        mZoomOutDisappear.start();
        if (mSoundStreamId != ID_NO_SOUND) {
            mSoundPool.play(mSoundStreamId, AudioManager.STREAM_MUSIC, AudioManager.STREAM_MUSIC, 1, 0, 1.0f);
        }
    }

    public void cancelAnim() {
        if (mZoomOutDisappear != null) {
            mZoomOutDisappear.cancel();
        }
        if (mZoomInAppear != null) {
            mZoomInAppear.cancel();
        }
        mSoundPool.release();
    }
}
