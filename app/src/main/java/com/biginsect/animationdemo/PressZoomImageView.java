package com.biginsect.animationdemo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.animation.DynamicAnimation;
import android.support.animation.SpringAnimation;
import android.support.animation.SpringForce;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

/**
 * 按压下缩小，控件区域内松开弹性动画，控件区域外松开直接恢复原来大小。
 *
 * @author lipeng
 * Created at 2020/11/20 17:35
 */
public class PressZoomImageView extends BaseAnimImageView {


    private final static long DURATION_DEFAULT = 100L;

    private AnimatorSet mZoomOutSet;
    //根据当前事件有效或无效给不同的动画效果
    private AnimatorSet mZoomInSet;
    private SpringAnimation mReboundAnimX;
    private SpringAnimation mReboundAnimY;

    //按压缩小动画结束并且按压释放之后才播放恢复动画
    private boolean mZoomOutIsEnd = false;
    private boolean mTouchIsUp = false;

    public PressZoomImageView(Context context) {
        this(context, null);
    }

    public PressZoomImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PressZoomImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void prepare() {
        prepareZoomOutAnim();
        prepareZoomInAnim();
    }

    private void prepareZoomOutAnim() {
        mZoomOutSet = new AnimatorSet();
        ObjectAnimator animatorX = ObjectAnimator.ofFloat(this, "scaleX", 1.0f, 0.8f);
        ObjectAnimator animatorY = ObjectAnimator.ofFloat(this, "scaleY", 1.0f, 0.8f);
        mZoomOutSet.play(animatorX).with(animatorY);
        mZoomOutSet.setDuration(DURATION_DEFAULT);
        mZoomOutSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mZoomOutIsEnd = true;
                checkAndPlayEndAnim();
            }
        });
    }

    private void prepareZoomInAnim() {
        mZoomInSet = new AnimatorSet();
        ObjectAnimator animatorX = ObjectAnimator.ofFloat(this, "scaleX", 0.8f, 1.0f);
        ObjectAnimator animatorY = ObjectAnimator.ofFloat(this, "scaleY", 0.8f, 1.0f);
        mZoomInSet.play(animatorX).with(animatorY);
        mZoomInSet.setDuration(DURATION_DEFAULT);

        SpringForce force = new SpringForce()
                .setDampingRatio(SpringForce.DAMPING_RATIO_HIGH_BOUNCY)
                .setStiffness(1000f)
                .setFinalPosition(1.0f);
        mReboundAnimX = new SpringAnimation(this, SpringAnimation.SCALE_X)
                .setMinimumVisibleChange(SpringAnimation.MIN_VISIBLE_CHANGE_ALPHA)
                .setStartValue(0.8f)
                .setSpring(force);
        mReboundAnimY = new SpringAnimation(this, SpringAnimation.SCALE_Y)
                .setMinimumVisibleChange(SpringAnimation.MIN_VISIBLE_CHANGE_ALPHA)
                .setStartValue(0.8f)
                .setSpring(force);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.d("view", "down");
                playZoomOutAnim();
                break;
            case MotionEvent.ACTION_UP:
                Log.d("view", "up");
                mTouchIsUp = true;
                checkAndPlayEndAnim();
                break;
            case MotionEvent.ACTION_CANCEL:
                Log.d("view", "cancel");
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    private void checkAndPlayEndAnim() {
        if (!hasOnClickListeners()){
            return;
        }
        if (!(mZoomOutIsEnd && mTouchIsUp)) {
            return;
        }
        if (isTouchInView()) {
            playZoomInRebound();
        } else {
            playZoomInNormal();
        }
    }

    public void playZoomOutAnim() {
        if (!hasOnClickListeners()){
            return;
        }
        if (mReboundAnimX.isRunning() || mZoomInSet.isRunning()) {//上次动画未结束
            mReboundAnimX.cancel();
            mReboundAnimY.cancel();
            mZoomInSet.cancel();
        }
        mZoomOutSet.start();
    }

    private void playZoomInRebound() {
        mReboundAnimX.start();
        mReboundAnimX.addEndListener(new DynamicAnimation.OnAnimationEndListener() {
            @Override
            public void onAnimationEnd(DynamicAnimation animation, boolean canceled, float value, float velocity) {
                if (mEndListener != null){
                    mEndListener.onAnimEnd();
                }
                mZoomOutIsEnd = false;
                mTouchIsUp = false;
                //防止多次执行
                mReboundAnimX.removeEndListener(this);
            }
        });
        mReboundAnimY.start();
    }

    private void playZoomInNormal() {
        mZoomInSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (mEndListener != null){
                    mEndListener.onAnimEnd();
                }
                mZoomOutIsEnd = false;
                mTouchIsUp = false;
            }
        });
        mZoomInSet.start();

    }
}
