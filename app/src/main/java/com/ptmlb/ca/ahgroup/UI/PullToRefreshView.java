package com.ptmlb.ca.ahgroup.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * Created by Administrator on 2016/1/11.
 */
public class PullToRefreshView extends View {

    private MaterialProgressDrawable drawable;
    private float scale = 1.0f;

    private Animation mScaleAnimation = new Animation() {
        @Override
        public void applyTransformation(float interpolatedTime, Transformation t) {
            scale = 1f - interpolatedTime;
            drawable.setAlpha((int) (255 * scale));
            invalidate();
        }
    };

    public PullToRefreshView(Context context) {
        super(context);
        initView();
    }


    public PullToRefreshView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public PullToRefreshView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        final int saveCount = canvas.save();
        Rect rect = drawable.getBounds();
        int l = getPaddingLeft() + (getMeasuredWidth() - drawable.getIntrinsicWidth()) / 2;
        canvas.translate(l, getPaddingTop());
        canvas.scale(scale, scale, rect.exactCenterX(), rect.exactCenterY());
        drawable.draw(canvas);
        canvas.restoreToCount(saveCount);
    }

    private void initView() {
        drawable = new MaterialProgressDrawable(getContext(), this);
        drawable.setBackgroundColor(Color.BLUE);
        drawable.setCallback(this);
    }

    @Override
    public void invalidateDrawable(Drawable dr) {
        if (drawable == dr) {
            invalidate();
        } else {
            super.invalidateDrawable(dr);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = drawable.getIntrinsicHeight();
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        final int size = drawable.getIntrinsicHeight();
        drawable.setBounds(0, 0, size, size);

        if (changed) {

        }
    }

    public void onReset(HeaderViewLayout layout) {
        drawable.stop();

    }

    public void onRefreshPrepare(HeaderViewLayout layout) {

    }
    public void onRefreshBegin(HeaderViewLayout layout) {
        drawable.setAlpha(255);
        drawable.start();
        drawable.showArrow(true);
    }
    public void onRefreshComplete(HeaderViewLayout layout) {
        drawable.stop();
    }

    public void onPositionChanged(HeaderViewLayout layout, int state, float percent) {
        if (state == HeaderViewLayout.PTR_STATE_PREPARE) {
            drawable.setAlpha((int) (255 * percent));
            drawable.showArrow(true);

            float strokeStart = ((percent) * .8f);
            drawable.setStartEndTrim(0f, Math.min(0.8f, strokeStart));
            drawable.setArrowScale(Math.min(1f, percent));

            // magic
            float rotation = (-0.25f + .4f * percent + percent * 2) * .5f;
            drawable.setProgressRotation(rotation);
            invalidate();
        }
    }
}
