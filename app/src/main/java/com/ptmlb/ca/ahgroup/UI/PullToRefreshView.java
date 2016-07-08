package com.ptmlb.ca.ahgroup.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.ptmlb.ca.ahgroup.R;

/**
 * Created by Administrator on 2016/1/11.
 *
 */
public class PullToRefreshView extends View {

    private RotateLogoDrawable logoDrawable;
    private Paint textPaint;
    private String textContent;
    private boolean showText = false;

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
        canvas.save();
        logoDrawable.draw(canvas);
        canvas.restore();
        if (showText) {
//            float width = textPaint.measureText(textContent);
            float height = textPaint.descent() - textPaint.ascent();
            canvas.drawText(textContent, canvas.getWidth()/2, canvas.getHeight() - height/2, textPaint);
        }
    }

    private void initView() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.sharp_full);
        logoDrawable = new RotateLogoDrawable(bitmap);

        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(getResources().getDimension(R.dimen.pull_refresh_text_size));
        textPaint.setTextAlign(Paint.Align.CENTER);

        textContent = getResources().getString(R.string.bottom_pull_refresh_content);
    }

    @Override
    public void invalidateDrawable(Drawable dr) {
        super.invalidateDrawable(dr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = logoDrawable.getIntrinsicHeight();
        float space = getResources().getDimension(R.dimen.pull_refresh_vertical_space);
        height += 2 * space;

        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        float offset = getResources().getDimension(R.dimen.pull_refresh_vertical_offset);
        logoDrawable.setCentrePoint(new Point(getWidth()/2, getHeight()/2 - (int)offset));
    }

//    public void onReset(HeaderViewLayout layout) {
//    }
//
//    public void onRefreshPrepare(HeaderViewLayout layout) {
//
//    }
//    public void onRefreshBegin(HeaderViewLayout layout) {
//
//    }
//    public void onRefreshComplete(HeaderViewLayout layout) {
//    }

    public void onPositionChanged(HeaderViewLayout layout, int state, float percent) {
        Log.i("", "PullToRefresh percent:" + percent);
        if (state == HeaderViewLayout.PTR_STATE_IDLE) {
            showText = false;
            logoDrawable.setAngle(-180.0f);
            logoDrawable.setScale(0.5f);
        } else if (state == HeaderViewLayout.PTR_STATE_PREPARE) {
            showText = false;
            logoDrawable.setAngle(-180.0f + 180.0f * percent);
            logoDrawable.setScale((float) (0.5f + 0.5 * percent));
        } else if (state == HeaderViewLayout.PTR_STATE_LOADING) {
            showText = true;
            logoDrawable.setAngle(0.0f);
            logoDrawable.setScale(1.0f);
        }
        invalidate();
    }
}
