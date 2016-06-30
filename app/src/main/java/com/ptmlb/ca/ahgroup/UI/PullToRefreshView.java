package com.ptmlb.ca.ahgroup.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.TextView;

import com.ptmlb.ca.ahgroup.R;

/**
 * Created by Administrator on 2016/1/11.
 */
public class PullToRefreshView extends View {

//    private MaterialProgressDrawable drawable;
    private RotateLogoDrawable logoDrawable;
    private Paint textPaint;
    private boolean show_text = false;

//    private Animation mScaleAnimation = new Animation() {
//        @Override
//        public void applyTransformation(float interpolatedTime, Transformation t) {
//            scale = 1f - interpolatedTime;
//            drawable.setAlpha((int) (255 * scale));
//            invalidate();
//        }
//    };

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
//        final int saveCount = canvas.save();
//        Rect rect = drawable.getBounds();
//        int l = getPaddingLeft() + (getMeasuredWidth() - drawable.getIntrinsicWidth()) / 2;
//        canvas.translate(l, getPaddingTop());
//        canvas.scale(scale, scale, rect.exactCenterX(), rect.exactCenterY());
//        drawable.draw(canvas);
//        canvas.restoreToCount(saveCount);

        canvas.save();
        logoDrawable.draw(canvas);
        canvas.restore();
        if (show_text) {
            String textContent = getResources().getString(R.string.bottom_pull_refresh_content);
            float width = textPaint.measureText(textContent);
            float height = textPaint.descent() - textPaint.ascent();
            canvas.drawText(textContent, canvas.getWidth()/2, canvas.getHeight() - height/2, textPaint);
        }
    }

    private void initView() {
//        drawable = new MaterialProgressDrawable(getContext(), this);
//        drawable.setBackgroundColor(Color.WHITE);
//        drawable.setCallback(this);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.sharp_full);
        logoDrawable = new RotateLogoDrawable(bitmap);

        textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(getResources().getDimension(R.dimen.pull_refresh_text_size));
        textPaint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    public void invalidateDrawable(Drawable dr) {
        super.invalidateDrawable(dr);
//        if (drawable == dr) {
//            invalidate();
//        } else {
//            super.invalidateDrawable(dr);
//        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        int height = drawable.getIntrinsicHeight();
//        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        int height = logoDrawable.getIntrinsicHeight();
        float space = getResources().getDimension(R.dimen.pull_refresh_vertical_space);
        height += 2 * space;

        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
//        final int size = drawable.getIntrinsicHeight();
//        drawable.setBounds(0, 0, size, size);


//        float space = getResources().getDimension(R.dimen.pull_refresh_vertical_space);
//
//        final int width = logoDrawable.getIntrinsicWidth();
//        final int height = logoDrawable.getIntrinsicHeight();
//        logoDrawable.setBounds(getWidth()/2 - width/2, (int)space, getWidth()/2 + width/2, (int)space + height);

        float offset = getResources().getDimension(R.dimen.pull_refresh_vertical_offset);
        logoDrawable.setCentrePoint(new Point(getWidth()/2, getHeight()/2 - (int)offset));


        if (changed) {

        }
    }

    public void onReset(HeaderViewLayout layout) {
//        drawable.stop();
    }

    public void onRefreshPrepare(HeaderViewLayout layout) {

    }
    public void onRefreshBegin(HeaderViewLayout layout) {
//        drawable.setAlpha(255);
//        drawable.start();
//        drawable.showArrow(true);

        logoDrawable.setAngle(-180.0f);
        logoDrawable.setScale(0.5f);
    }
    public void onRefreshComplete(HeaderViewLayout layout) {
//        drawable.stop();
    }

    public void onPositionChanged(HeaderViewLayout layout, int state, float percent) {
        Log.i("", "PullToRefresh percent:" + percent);

        if (state == HeaderViewLayout.PTR_STATE_IDLE) {
            show_text = false;
            logoDrawable.setAngle(-180.0f);
            logoDrawable.setScale(0.5f);
        } else if (state == HeaderViewLayout.PTR_STATE_PREPARE) {
//            drawable.setAlpha((int) (255 * percent));
//            drawable.showArrow(true);
//
//            float strokeStart = ((percent) * .8f);
//            drawable.setStartEndTrim(0f, Math.min(0.8f, strokeStart));
//            drawable.setArrowScale(Math.min(1f, percent));
//
//            // magic
//            float rotation = (-0.25f + .4f * percent + percent * 2) * .5f;
//            drawable.setProgressRotation(rotation);

            show_text = false;
            logoDrawable.setAngle(-180.0f + 180.0f * percent);
            logoDrawable.setScale((float) (0.5f + 0.5 * percent));

        } else if (state == HeaderViewLayout.PTR_STATE_LOADING) {

            show_text = true;
            logoDrawable.setAngle(0.0f);
            logoDrawable.setScale(1.0f);
        }

        invalidate();
    }
}
