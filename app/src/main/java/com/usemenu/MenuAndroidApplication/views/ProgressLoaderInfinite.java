package com.usemenu.MenuAndroidApplication.views;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.usemenu.MenuAndroidApplication.R;

public class ProgressLoaderInfinite extends View {

    private ObjectAnimator animFill;
    private ObjectAnimator animAlpha;
    private AnimatorSet anim;
    private long duration = 600l;
    private Paint paint;
    private Context ctx;
    private float rotation = 90f;
    private RectF rectF;
    private float strokeWidth;
    private float strokeWidthHalf;
    private boolean toRight = true;
    private boolean isInitialized = false;

    public ProgressLoaderInfinite(Context context) {
        super(context);
        init(context);
    }

    public ProgressLoaderInfinite(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ProgressLoaderInfinite(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context ctx) {
        this.ctx = ctx;

        paint = new Paint();

        animAlpha = ObjectAnimator.ofFloat(this, "alpha", 1.0f, 0.0f);
        animAlpha.setDuration(duration);
        animAlpha.setInterpolator(new LinearInterpolator());
        animAlpha.setRepeatMode(ObjectAnimator.RESTART);
        animAlpha.setRepeatCount(ObjectAnimator.INFINITE);

        animFill = ObjectAnimator.ofFloat(this, "angle", 0f, 181f);
        animFill.setDuration(duration);
        animFill.setInterpolator(new LinearInterpolator());
        animFill.setRepeatMode(ObjectAnimator.RESTART);
        animFill.setRepeatCount(ObjectAnimator.INFINITE);
        anim = new AnimatorSet();
        anim.playTogether(animFill, animAlpha);
        animFill.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                toRight = !toRight;
            }
        });
//        anim.start();
        setVisibility(View.VISIBLE);
    }

    public void setAngle(float rotation) {
        this.rotation = rotation;
        invalidate();
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        setRotation(-90);
        if (visibility == View.VISIBLE) {
            if (anim != null && !anim.isRunning()) {
                anim.start();
            }
            System.out.println("ProgressLoaderInfinite.setVisibility.animatorStart()");
        } else {
            anim.cancel();
            System.out.println("ProgressLoaderInfinite.setVisibility.animatorCancel()");
        }
    }

    private void initPaint() {
        isInitialized = true;
        paint.setAntiAlias(true);
        paint.setColor(ctx.getResources().getColor(R.color.menu_main_orange));
        paint.setStyle(Paint.Style.STROKE);
        strokeWidth = getWidth() * 0.05f;
        strokeWidthHalf = strokeWidth * 0.5f;
        paint.setStrokeWidth(strokeWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!isInitialized) {
            initPaint();
        }
        if (rectF == null) {
            rectF = new RectF();
            rectF.set(strokeWidth, strokeWidth, getWidth() - strokeWidthHalf, getHeight() - strokeWidthHalf);
        }
        canvas.drawArc(rectF, toRight ? -rotation : 180 - rotation, rotation * 2, false, paint);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        anim.cancel();
        System.out.println("ProgressLoaderInfinite.onDetachedFromWindow.animatorCancel()");
    }
}