package com.ptmlb.ca.ahgroup.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.OvershootInterpolator;

import com.ptmlb.ca.ahgroup.R;

/**
 * Created by fmisser on 2016/7/8.
 *
 */

public class FooterPullToRefreshView extends View {

    private Bitmap bmpCircle;
    private Bitmap bmpLogo;
    private float verticalSpace;
    private int state;
    private float percent;
    private boolean refreshing = false;
    private int degree;
    private static final int DEGREE_INCREASE = 12;
    private int total;
    private int move;
    private static final int MOVE_INCREASE = 6;
    private static Interpolator moveInterpolator = new OvershootInterpolator(4.0f);

    public FooterPullToRefreshView(Context context) {
        this(context, null);
    }

    public FooterPullToRefreshView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FooterPullToRefreshView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    private void initView() {
        bmpCircle = BitmapFactory.decodeResource(getResources(), R.mipmap.circle);
        bmpLogo = BitmapFactory.decodeResource(getResources(), R.mipmap.sharp_all);
        verticalSpace = getResources().getDimension(R.dimen.pull_refresh_vertical_space);
    }

    public void setRefreshing(boolean refreshing) {
        this.refreshing = refreshing;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();

        int top = getHeight();
        int width = getWidth();
        if (state == HeaderViewLayout.PTR_STATE_IDLE || state == HeaderViewLayout.PTR_STATE_PREPARE) {
            this.move = 0;
            canvas.drawBitmap(bmpLogo, width/2 - bmpLogo.getWidth()/2, top-bmpLogo.getHeight()*interpolator.getInterpolation(percent), null);
        } else if (state == HeaderViewLayout.PTR_STATE_LOADING) {
            move = Math.min(move+MOVE_INCREASE, total);
            int dy = (int) (moveInterpolator.getInterpolation((float) move/(float) total) * total);
            int topY = top-bmpLogo.getHeight() - dy;
            canvas.drawBitmap(bmpLogo, width/2 - bmpLogo.getWidth()/2, topY, null);
            if (refreshing) {
                degree = (degree+DEGREE_INCREASE)%360;
                canvas.rotate(degree, width/2, topY + bmpLogo.getHeight()/2);
                canvas.drawBitmap(bmpCircle, width/2 - bmpCircle.getWidth(), topY + bmpLogo.getHeight()/2 - bmpCircle.getHeight(), null);
            }
            invalidate();
        }
        canvas.restore();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = bmpLogo.getHeight();
        height += 2 * verticalSpace;

        total = getHeight() - bmpLogo.getHeight() - (int) verticalSpace;

        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void onPositionChanged(HeaderViewLayout layout, int state, float percent) {

        //状态切换成不是加载数据时候取消相关动画
        if (state != HeaderViewLayout.PTR_STATE_LOADING) {
            this.refreshing = false;
        }
        this.state = state;
        this.percent = Math.min(percent, 1.0f);
        invalidate();
    }

    private final Interpolator interpolator = new Interpolator() {
        @Override
        public float getInterpolation(float input) {
            return (float) (Math.sin(input+0.25) - Math.sin(0.25));
        }
    };
}
