package com.ptmlb.ca.ahgroup.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.IntDef;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.NestedScrollingChildHelper;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.NestedScrollingParentHelper;
import android.support.v4.view.VelocityTrackerCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v4.widget.ScrollerCompat;
import android.support.v4.widget.SlidingPaneLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.IllegalFormatCodePointException;

/**
 * Created by Administrator on 2015/12/31.
 *
 */

public class HeaderViewLayout extends ViewGroup implements NestedScrollingParent {

    @IntDef({STATE_IDLE, STATE_DRAGGING, STATE_SETTLING})
    @Retention(RetentionPolicy.SOURCE)
    private @interface State {}

    private int state = STATE_IDLE;
    private static final int STATE_IDLE = 0;
    private static final int STATE_DRAGGING = 1;
    private static final int STATE_SETTLING = 2;

    private static final int BASE_SETTLE_DURATION = 200; // ms
    private static final int MAX_SETTLE_DURATION = 400; // ms

    private final NestedScrollingParentHelper nestedScrollingParentHelper;
    //private final NestedScrollingChildHelper nestedScrollingChildHelper;
    //private final GestureDetectorCompat gestureDetectorCompat;
//    private ViewDragHelper viewDragHelper;
    private ScrollerCompat scrollerCompat;
    private float mMinVelocity;
    private float mMaxVelocity = 24000.0f;
    private float mTension = 0.8f;
    private final float MIN_TENSION = 0.6f;

    private View headerView;
    private View contentView;

    private int maxScrollY = 0;

    private int mScrimColor = 0x99000000;
    private float mScrimOpacity;
    private Paint mScrimPaint = new Paint();

    //pull to refresh part
    private int ptrState = 0;
    public static final int PTR_STATE_IDLE = 0;
    public static final int PTR_STATE_PREPARE = 1;
    public static final int PTR_STATE_LOADING = 2;
    public static final int PTR_STATE_RESTORE = 3;

    private PullToRefreshView pullToRefreshView;

    private static final int PTR_MAX_HEIGHT = 350;
    private static final int PTR_PREPARE_HEIGHT = 200;

//    private DecelerateInterpolator decelerateInterpolator = new DecelerateInterpolator(0.3f);

    public HeaderViewLayout(Context context) {
        this(context, null);
    }

    public HeaderViewLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HeaderViewLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //gestureDetectorCompat = new GestureDetectorCompat(context, this);

        nestedScrollingParentHelper = new NestedScrollingParentHelper(this);
        //nestedScrollingChildHelper = new NestedScrollingChildHelper(this);
        //setNestedScrollingEnabled(true);

//        viewDragHelper = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {
//            @Override
//            public boolean tryCaptureView(View child, int pointerId) {
//                return true;
//            }
//        });

        final float density = getResources().getDisplayMetrics().density;
        mMinVelocity = 400.0f * density;
        scrollerCompat = ScrollerCompat.create(context, sInterpolator);
    }

    @Override
    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {

        final int restoreCount = canvas.save();
        final boolean result = super.drawChild(canvas, child, drawingTime);
        canvas.restoreToCount(restoreCount);

        if (child == headerView) {
            final int baseAlpha = (mScrimColor & 0xff000000) >>> 24;
            final int imag = (int) (baseAlpha * mScrimOpacity);
            final int color = imag << 24 | (mScrimColor & 0xffffff);
            mScrimPaint.setColor(color);

            canvas.drawRect(0, 0, getWidth(), getHeight(), mScrimPaint);
        }

        return result;
    }

    @Override
    public void computeScroll() {
//        if (viewDragHelper.continueSettling(true)) {
//            ViewCompat.postInvalidateOnAnimation(this);
//        }

        if (continueSettling()) {
            ViewCompat.postInvalidateOnAnimation(this);
        }

//        if (scrollerCompat.computeScrollOffset()) {
//            int y = scrollerCompat.getCurrY();
////            Log.d("currY:", String.valueOf(y));
////            Log.d("scrollY:", String.valueOf(scrollY));
//            if (y != scrollY) {
//                move(y - scrollY);
//            }
//            ViewCompat.postInvalidateOnAnimation(this);
//        }
    }

    private int move(int deltaY) {

        state = STATE_DRAGGING;

        int consumedY;
        int currY = contentView.getTop();

        if (currY - deltaY < 0) {
            //超出顶部
            consumedY = currY;
            contentView.offsetTopAndBottom(-consumedY);
            headerAction();

        } else if (currY - deltaY > maxScrollY) {

            Log.d("currY:", String.valueOf(currY));
            Log.d("deltaY:", String.valueOf(deltaY));

            //超出底部
            if (currY < maxScrollY) {
                consumedY = currY - maxScrollY;
                contentView.offsetTopAndBottom(-consumedY);
                bottomPullToRefresh(deltaY - consumedY, false);
                headerAction();
            } else {
                bottomPullToRefresh(deltaY, false);
            }

            //consumed all scroll
            consumedY = deltaY;

        } else {

            consumedY = deltaY;
            contentView.offsetTopAndBottom(-consumedY);
            headerAction();
            bottomPullToRefresh(0, false);
        }

        if (consumedY != 0) {
//            contentView.offsetTopAndBottom(-consumedY);
//            pullToRefreshView.offsetTopAndBottom(-consumedY);
//            pullToRefreshView.onRefreshBegin(this);
//            headerView.offsetTopAndBottom(-consumedY/2);
//            headerAction();
            ViewCompat.postInvalidateOnAnimation(this);
        }

        return consumedY;
    }

    private void headerAction() {

        int top = contentView.getTop();
        if (top > maxScrollY) {
            top = maxScrollY;
        }
        float offset = 1 - (float)top / (float)maxScrollY;
        if (offset > 1.0f) {
            offset = 1.0f;
        } else if (offset < 0.0f) {
            offset = 0.0f;
        }

//        float deltaY = -offset * maxScrollY / 2;

        headerView.setTranslationY(-offset * maxScrollY / 2);

        float scale = 1 - offset / 10;
        if (scale < 0.95f) {
            scale = 0.95f;
        }
        headerView.setScaleX(scale);
        headerView.setScaleY(scale);
        mScrimOpacity = offset;
    }

//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        return viewDragHelper.shouldInterceptTouchEvent(ev);
//    }
//
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        viewDragHelper.processTouchEvent(event);
//        return true;
//    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        headerView = getChildAt(0);
        pullToRefreshView = (PullToRefreshView) getChildAt(1);
        contentView = getChildAt(2);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        maxScrollY = headerView.getHeight();

        {
            int left = 0;
            int top = 0;
            int right = left + headerView.getMeasuredWidth();
            int bottom = top + headerView.getMeasuredHeight();
            headerView.layout(left, top, right, bottom);
        }

        {
            int left = 0;
            int top = maxScrollY;
            int right = left + contentView.getMeasuredWidth();
            int bottom = top + contentView.getMeasuredHeight();
            contentView.layout(left, top, right, bottom);
        }

        {
            int left = 0;
            int top = 0;
            int right = left + pullToRefreshView.getMeasuredWidth();
            int bottom = top + pullToRefreshView.getMeasuredHeight();
            pullToRefreshView.layout(left, top, right, bottom);
        }
    }

    //NestedScrollingParent

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    @Override
    public void onNestedScrollAccepted(View child, View target, int nestedScrollAxes) {
        nestedScrollingParentHelper.onNestedScrollAccepted(child, target, nestedScrollAxes);
    }

    @Override
    public void onStopNestedScroll(View target) {
        nestedScrollingParentHelper.onStopNestedScroll(target);
//        if (viewDragHelper.smoothSlideViewTo(contentView, 0, 0)) {
//            ViewCompat.postInvalidateOnAnimation(this);
//            totalY = headerView.getHeight();
//        }

        settle(0, 0);
    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {

    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {

        int consumedY = move(dy);

        consumed[1] = consumedY;
        consumed[0] = 0;

//        Log.d("dy:", String.valueOf(dy));
    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        return false;
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {

//        Log.d("velocityY:", String.valueOf(velocityY));

        return settle(velocityX, velocityY);

//        scrollerCompat.fling(0, scrollY, 0, (int) velocityY, 0, 0, 0, maxScrollY, 0, 0);
//        ViewCompat.postInvalidateOnAnimation(this);
//
//        fling(velocityX, velocityX);
//        return true;

//        boolean ret;
//        if (velocityY > 0) {
//            ret = forceSettleCapturedViewAt(0, maxScrollY, 0, (int)velocityY);
//        } else {
//            ret = forceSettleCapturedViewAt(0, 0, 0, (int)velocityY);
//        }
//        ViewCompat.postInvalidateOnAnimation(this);
//        return ret;
    }

    @Override
    public int getNestedScrollAxes() {
        return nestedScrollingParentHelper.getNestedScrollAxes();
    }

    //NestedScrollingChild

//    @Override
//    public void setNestedScrollingEnabled(boolean enabled) {
//        nestedScrollingChildHelper.setNestedScrollingEnabled(enabled);
//    }
//
//    @Override
//    public boolean isNestedScrollingEnabled() {
//        return nestedScrollingChildHelper.isNestedScrollingEnabled();
//    }
//
//    @Override
//    public boolean startNestedScroll(int axes) {
//        return nestedScrollingChildHelper.startNestedScroll(axes);
//    }
//
//    @Override
//    public void stopNestedScroll() {
//        nestedScrollingChildHelper.stopNestedScroll();
//    }
//
//    @Override
//    public boolean hasNestedScrollingParent() {
//        return nestedScrollingChildHelper.hasNestedScrollingParent();
//    }
//
//    @Override
//    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed,
//                                        int dxUnconsumed, int dyUnconsumed, int[] offsetInWindow) {
//        return nestedScrollingChildHelper.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow);
//    }
//
//    @Override
//    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow) {
//        return nestedScrollingChildHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow);
//    }
//
//    @Override
//    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
//        return nestedScrollingChildHelper.dispatchNestedFling(velocityX, velocityY, consumed);
//    }
//
//    @Override
//    public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
//        return nestedScrollingChildHelper.dispatchNestedPreFling(velocityX, velocityY);
//    }
//
//    @Override
//    protected void onDetachedFromWindow() {
//        super.onDetachedFromWindow();
//        nestedScrollingChildHelper.onDetachedFromWindow();
//    }

    //OnGestureListener

//    @Override
//    public boolean onDown(MotionEvent e) {
//        startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL);
//        return true;
//    }
//
//    @Override
//    public void onShowPress(MotionEvent e) {
//
//    }
//
//    @Override
//    public boolean onSingleTapUp(MotionEvent e) {
//        return false;
//    }
//
//    @Override
//    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
//        dispatchNestedPreScroll(0, (int) distanceY, null, null);
//        dispatchNestedScroll(0, 0, 0, 0, null);
//        return true;
//    }
//
//    @Override
//    public void onLongPress(MotionEvent e) {
//
//    }
//
//    @Override
//    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
//        return true;
//    }
//
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        final boolean handled = gestureDetectorCompat.onTouchEvent(event);
//        if (!handled && event.getAction() == MotionEvent.ACTION_UP) {
//            stopNestedScroll();
//        }
//        return true;
//    }

    private float clampMag(float value, float absMin, float absMax) {
        final float absValue = Math.abs(value);
        if (absValue < absMin) return 0;
        if (absValue > absMax) return value > 0 ? absMax : -absMax;
        return value;
    }

    private int clampMag(int value, int absMin, int absMax) {
        final int absValue = Math.abs(value);
        if (absValue < absMin) return 0;
        if (absValue > absMax) return value > 0 ? absMax : -absMax;
        return value;
    }

    private float distanceInfluenceForSnapDuration(float f) {
        f -= 0.5f; // center the values about 0.
        f *= 0.3f * Math.PI / 2.0f;
        return (float) Math.sin(f);
    }

    private int computeAxisDuration(int delta, int velocity, int motionRange) {
        if (delta == 0) {
            return 0;
        }

        final int width = getWidth();
        final int halfWidth = width / 2;
        final float distanceRatio = Math.min(1f, (float) Math.abs(delta) / width);
        final float distance = halfWidth + halfWidth *
                distanceInfluenceForSnapDuration(distanceRatio);

        int duration;
        velocity = Math.abs(velocity);
        if (velocity > 0) {
            duration = 4 * Math.round(1000 * Math.abs(distance / velocity));
        } else {
            final float range = (float) Math.abs(delta) / motionRange;
            duration = (int) ((range + 1) * BASE_SETTLE_DURATION);
        }
        return Math.min(duration, MAX_SETTLE_DURATION);
    }

    private int computeSettleDuration(View child, int dx, int dy, int xvel, int yvel) {
        xvel = clampMag(xvel, (int) mMinVelocity, (int) mMaxVelocity);
        yvel = clampMag(yvel, (int) mMinVelocity, (int) mMaxVelocity);
        final int absDx = Math.abs(dx);
        final int absDy = Math.abs(dy);
        final int absXVel = Math.abs(xvel);
        final int absYVel = Math.abs(yvel);
        final int addedVel = absXVel + absYVel;
        final int addedDistance = absDx + absDy;

        final float xweight = xvel != 0 ? (float) absXVel / addedVel :
                (float) absDx / addedDistance;
        final float yweight = yvel != 0 ? (float) absYVel / addedVel :
                (float) absDy / addedDistance;

        int xduration = computeAxisDuration(dx, xvel, 0);
        int yduration = computeAxisDuration(dy, yvel, maxScrollY);

        return (int) (xduration * xweight + yduration * yweight);
    }

    private boolean forceSettleCapturedViewAt(int finalLeft, int finalTop, int xvel, int yvel) {
        final int startTop = contentView.getTop();
//        final int startTop = scrollY;
        final int dy = finalTop - startTop;

        if (dy == 0) {
            return false;
        }

        final int duration = computeSettleDuration(contentView, 0, dy, 0, yvel);
        scrollerCompat.startScroll(0, startTop, 0, dy, duration);
        ViewCompat.postInvalidateOnAnimation(this);

        state = STATE_SETTLING;

        return true;
    }

    private final Interpolator sInterpolator = new Interpolator() {
        public float getInterpolation(float t) {
//            t -= 1.0f;
//            return t * t * t * t * t + 1.0f;

            t -= 1.0f;
            return t * t * ((mTension + 1) * t + mTension) + 1.0f;
        }
    };

    private boolean settle(float velocityX, float velocityY) {

        if (state == STATE_SETTLING) {
            return false;
        }

        int top = contentView.getTop();

        if (top <= maxScrollY) {
            final float offset = 1 - (float)top / (float)maxScrollY;
            final float yvel = clampMag(velocityY, mMinVelocity, mMaxVelocity);
            int finalTopY = yvel > 0 || yvel == 0 && offset > 0.5f ? 0 : maxScrollY;

            mTension = Math.abs(velocityY / mMaxVelocity) * 3;
            if (mTension < MIN_TENSION) {
                mTension = MIN_TENSION;
            }
            Log.d("mTension:", String.valueOf(mTension));
            return forceSettleCapturedViewAt(0, finalTopY, 0, (int)velocityY);
        } else {
            return forceSettleCapturedViewAt(0, maxScrollY, 0, (int)velocityY);
        }
    }

    public boolean continueSettling() {

        if (state == STATE_IDLE || state == STATE_DRAGGING) {
            return false;
        }

        boolean keepGoing = scrollerCompat.computeScrollOffset();
        final int y = scrollerCompat.getCurrY();
        final int dy = y - contentView.getTop();

        if (dy != 0) {
            contentView.offsetTopAndBottom(dy);
            bottomPullToRefresh(dy, true);
//            headerView.offsetTopAndBottom(dy/2);

            if (contentView.getTop() > 900) {
                headerView.offsetTopAndBottom(dy);
            } else {
                headerAction();
            }
        }

        if (keepGoing && y == scrollerCompat.getFinalY()) {
            // Close enough. The interpolator/scroller might think we're still moving
            // but the user sure doesn't.
            scrollerCompat.abortAnimation();
            state = STATE_IDLE;
            keepGoing = false;
        }

//        if (!keepGoing) {
//            if (scrollY != 0 && scrollY != maxScrollY) {
//                if (scrollY < maxScrollY / 2 ) {
//                    forceSettleCapturedViewAt(0, 0, 0, 0);
//                } else {
//                    forceSettleCapturedViewAt(0, maxScrollY, 0, 0);
//                }
//                ViewCompat.postInvalidateOnAnimation(this);
//            }
//        }

        return keepGoing;
    }

    //pull to refresh part
    private void bottomPullToRefresh(int deltaY, boolean isSettle) {

        int dstY = contentView.getTop() - maxScrollY - deltaY;
        if (dstY >= PTR_MAX_HEIGHT) {
            dstY = PTR_MAX_HEIGHT;
        }

        if (!isSettle) {
            int actualY = dstY;
            int offset = contentView.getTop() - maxScrollY - actualY;
            contentView.offsetTopAndBottom(-offset);
            headerView.offsetTopAndBottom(-offset);
        }

//        pullToRefreshView.offsetTopAndBottom(-offset);
//        LayoutParams layoutParams = pullToRefreshView.getLayoutParams();
//        layoutParams.height = -offset;
//        pullToRefreshView.setLayoutParams(layoutParams);

        if (dstY > 0 && dstY <= PTR_PREPARE_HEIGHT ) {
            ptrState = PTR_STATE_PREPARE;
            float percent = (float)dstY / (float)PTR_PREPARE_HEIGHT;
            pullToRefreshView.onPositionChanged(this, ptrState, percent);
        } else if (dstY > PTR_PREPARE_HEIGHT) {
            ptrState = PTR_STATE_LOADING;
            float percent = (float)(dstY - PTR_PREPARE_HEIGHT) / (float)(PTR_MAX_HEIGHT - PTR_PREPARE_HEIGHT);
            pullToRefreshView.onPositionChanged(this, ptrState, percent);
        } else {
            ptrState = PTR_STATE_IDLE;
            pullToRefreshView.onPositionChanged(this, ptrState, 1.0f);
        }



//        int currY = contentView.getTop() - maxScrollY - deltaY;
////        Log.d("getTop:", String.valueOf(contentView.getTop()));
//        if (currY >= PTR_MAX_HEIGHT) {
//            currY = PTR_MAX_HEIGHT;
//        }
//        int actualY = (int) ( decelerateInterpolator.getInterpolation((float)currY / (float)(PTR_MAX_HEIGHT) ) * (float)currY);
//        Log.d("actualY:", String.valueOf(actualY));
//
//        int originalY = contentView.getTop() - maxScrollY;
//        int lastY = (int) (decelerateInterpolator.getInterpolation((float)originalY / (float)(PTR_MAX_HEIGHT) ) * (float)originalY);
//
//        contentView.offsetTopAndBottom(actualY - lastY);

//        int actualY = currY;

//        if (actualY >= PTR_MAX_HEIGHT) {
//            actualY = PTR_MAX_HEIGHT;
//        }


//        if (deltaY < 0) {
//            contentView.offsetTopAndBottom(actualY);
//        } else {
//            contentView.offsetTopAndBottom(-actualY);
//        }


//        if (deltaY < 0) {
//            int offset = contentView.getTop() - maxScrollY + actualY;
//            Log.d("offset:", String.valueOf(offset));
//            contentView.offsetTopAndBottom(offset);
//        } else {
//            int offset = contentView.getTop() - maxScrollY - actualY;
//            Log.d("offset:", String.valueOf(offset));
//            contentView.offsetTopAndBottom(offset);
//        }


//        Log.d("currY:", String.valueOf(currY));
//        Log.d("actualY:", String.valueOf(actualY));
    }

}
