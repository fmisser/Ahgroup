package com.ptmlb.ca.ahgroup.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

import com.ptmlb.ca.ahgroup.R;

/**
 * Created by fmisser on 2016/7/7.
 *
 */

public class TopPullToRefreshView extends View {

    private Paint textPaint;
    private String refreshString;
    private String goHomeString;
    private Bitmap bmpCircle;
    private Bitmap bmpLogoLeft;
    private Bitmap bmpLogoRight;
    private float horizontalSpace;
    private float verticalSpace;
    private float verticalOffset;
    private float verticalMoveSpace;
    private float percent;
    private int state;
    private Interpolator interpolator = new DecelerateInterpolator(1.0f);
    private boolean refreshing = false;
    private int degree;
    private static final int DEGREE_INCREASE = 12;
    private int angle;
    private static final int ANGLE_INCREASE = 20;

    public TopPullToRefreshView(Context context) {
        super(context);
        initView();
    }

    public TopPullToRefreshView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public TopPullToRefreshView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public void setRefreshing(boolean refreshing) {
        this.refreshing = refreshing;
        invalidate();
    }

    private void initView() {
        bmpCircle = BitmapFactory.decodeResource(getResources(), R.mipmap.circle);
        bmpLogoLeft = BitmapFactory.decodeResource(getResources(), R.mipmap.sharp_left);
        bmpLogoRight = BitmapFactory.decodeResource(getResources(), R.mipmap.sharp_right);
        horizontalSpace = getResources().getDimension(R.dimen.pull_refresh_horizontal_space);
        verticalSpace = getResources().getDimension(R.dimen.pull_refresh_vertical_space);
        verticalOffset = getResources().getDimension(R.dimen.pull_refresh_vertical_offset);
        verticalMoveSpace = getResources().getDimension(R.dimen.top_pull_fresh_vertical_move_space);

        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(getResources().getDimension(R.dimen.pull_refresh_text_size));
        textPaint.setTextAlign(Paint.Align.CENTER);

        refreshString = getResources().getString(R.string.top_pull_refresh_content);
        goHomeString = getResources().getString(R.string.top_pull_go_home_content);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        canvas.save();

        int width = getWidth();
        int bmpLogoWidth = bmpLogoLeft.getWidth();
        int bmpLogoHeight = bmpLogoLeft.getHeight();
        int bmpLogoActionWidth = width / 2 - (int)horizontalSpace;
        if (state == HeaderViewLayout.PTR_STATE_IDLE || state == HeaderViewLayout.PTR_STATE_PREPARE) {
            int top = (int) verticalSpace - (int) verticalOffset;
            angle = 0;
            canvas.drawBitmap(bmpLogoLeft, (int)horizontalSpace - bmpLogoWidth + bmpLogoActionWidth*percent, top, null);
            canvas.drawBitmap(bmpLogoRight, width - (int)horizontalSpace - bmpLogoActionWidth*percent, top, null);
        } else if (state == HeaderViewLayout.PTR_STATE_LOADING) {
            int top = (int) verticalSpace - (int) verticalOffset + (int) (verticalMoveSpace*percent);
            canvas.save();
            angle = Math.max(angle-ANGLE_INCREASE, 0);
            canvas.rotate(angle, width/2, top + bmpLogoHeight/2);
            canvas.drawBitmap(bmpLogoLeft, (int)horizontalSpace - bmpLogoWidth + bmpLogoActionWidth, top, null);
            canvas.drawBitmap(bmpLogoRight, width - (int)horizontalSpace - bmpLogoActionWidth, top, null);
            canvas.restore();
            invalidate();
            textPaint.setAlpha((int) (256*interpolator.getInterpolation(1-percent)));
            canvas.drawText(refreshString, canvas.getWidth()/2, (int) verticalSpace + bmpLogoHeight + (int) verticalOffset + verticalMoveSpace*percent, textPaint);
            if (refreshing) {
                degree = (degree+DEGREE_INCREASE)%360;
                canvas.save();
                canvas.rotate(degree, width/2, top + bmpLogoHeight/2);
                canvas.drawBitmap(bmpCircle, width/2 - bmpCircle.getWidth(), top + bmpLogoHeight/2 - bmpCircle.getHeight(), null);
                canvas.restore();
                invalidate();
            }
        } else if (state == HeaderViewLayout.PTR_STATE_RESTORE) {
            int top = (int) verticalSpace - (int) verticalOffset + (int) verticalMoveSpace;
            canvas.save();
            angle = Math.min(angle+ANGLE_INCREASE, 180);
            canvas.rotate(angle, width/2, top + bmpLogoHeight/2);
            canvas.drawBitmap(bmpLogoLeft, (int)horizontalSpace - bmpLogoWidth + bmpLogoActionWidth, top, null);
            canvas.drawBitmap(bmpLogoRight, width - (int)horizontalSpace - bmpLogoActionWidth, top, null);
            canvas.restore();
            invalidate();
            textPaint.setAlpha(255);
            canvas.drawText(goHomeString, canvas.getWidth()/2, (int) verticalSpace + bmpLogoHeight + (int) verticalOffset + verticalMoveSpace, textPaint);
        }

        canvas.restore();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int height = bmpLogoLeft.getHeight();
        height += 2 * verticalSpace + verticalMoveSpace;

        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {

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
}
