package com.ptmlb.ca.ahgroup.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.IntDef;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.NestedScrollingParentHelper;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ScrollerCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

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

    @IntDef({MODE_TOP, MODE_BOTTOM})
    @Retention(RetentionPolicy.SOURCE)
    private @interface Mode {}
    private int mode = MODE_BOTTOM;
    private static final int MODE_TOP = 0;
    private static final int MODE_BOTTOM = 1;
    private boolean onlyTopMode = false;    //是否只有顶部模式

    private static final int BASE_SETTLE_DURATION = 200; // ms
    private static final int MAX_SETTLE_DURATION = 400; // ms

    private final NestedScrollingParentHelper nestedScrollingParentHelper;

    private ScrollerCompat scrollerCompat;
    private float mMinVelocity;
    private float mMaxVelocity = 24000.0f;
    private float mTension = 0.8f;
    private static final float MIN_TENSION = 0.6f;

    private View headerView;
    private View contentView;

    private int maxScrollY = 0;

    private int mScrimColor = 0x99000000;
    private float mScrimOpacity;
    private Paint mScrimPaint = new Paint();

    //pull to refresh part
    public static final int PTR_STATE_IDLE = 0;
    public static final int PTR_STATE_PREPARE = 1;
    public static final int PTR_STATE_LOADING = 2;
    public static final int PTR_STATE_RESTORE = 3;

    private static final int PTR_PREPARE_HEIGHT = 200;
    private static final int PTR_MAX_HEIGHT = 400;
    private static final int PTR_RESTORE_HEIGHT = 600;

    private PullToRefreshView pullToRefreshView;
    private int ptrState = 0;

    private TopPullToRefreshView topPullToRefreshView;
    private int topPtrState = 0;

    private FooterPullToRefreshView footerPullToRefreshView;
    private int footerPtrState = 0;

    public HeaderViewLayout(Context context) {
        this(context, null);
    }

    public HeaderViewLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HeaderViewLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        nestedScrollingParentHelper = new NestedScrollingParentHelper(this);

        final float density = getResources().getDisplayMetrics().density;
        mMinVelocity = 400.0f * density;
        scrollerCompat = ScrollerCompat.create(context, sInterpolator);

        if (onlyTopMode) {
            mode = MODE_TOP;
        }
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
        if (continueSettling()) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
        headerViewScrollAnimation();
        topScrollAnimation();
        bottomScrollAnimation();
        footerScrollAnimation();
    }

    //顶部刷新回调
    public void topRefresh() {

    }

    //底部刷新回调
    public void footerRefresh() {

    }

    //模式改变回调
    public void modeChanged(int mode) {

    }

    //结束刷新动画
    public void stopRefresh() {
        topPtrState = PTR_STATE_IDLE;
        footerPtrState = PTR_STATE_IDLE;
        settle(0, 0);
    }

    private void headerViewScrollAnimation() {
        int currY = contentView.getTop();
        int offset = currY - maxScrollY;
        if (offset < 0) {
            float factor = (float) Math.abs(offset) / (float) maxScrollY;
            float scale = (float) Math.max(0.95, 1 - factor/10);
            headerView.setScaleX(scale);
            headerView.setScaleY(scale);
            mScrimOpacity = factor;
            headerView.setTranslationY(offset/2);
        } else {
            headerView.setScaleX(1.0f);
            headerView.setScaleY(1.0f);
            mScrimOpacity = 0.0f;
            headerView.setTranslationY(offset);
        }
    }

    private void topScrollAnimation() {
        if (mode != MODE_TOP) {
            return;
        }
        int currY = contentView.getTop();
        if (currY < 0) {
            return;
        }
        if (currY == 0) {
            topPtrState = PTR_STATE_IDLE;
            topPullToRefreshView.onPositionChanged(this, topPtrState, 0.0f);
        } else if (currY < PTR_PREPARE_HEIGHT) {
            topPtrState = PTR_STATE_PREPARE;
            topPullToRefreshView.onPositionChanged(this, topPtrState, (float) currY / (float) PTR_PREPARE_HEIGHT);
        } else if (currY < PTR_MAX_HEIGHT) {
            topPtrState = PTR_STATE_LOADING;
            topPullToRefreshView.onPositionChanged(this, topPtrState, (float) (currY-PTR_PREPARE_HEIGHT) / (float) (PTR_MAX_HEIGHT-PTR_PREPARE_HEIGHT));
        } else if (currY < PTR_RESTORE_HEIGHT) {
            topPtrState = PTR_STATE_RESTORE;
            topPullToRefreshView.onPositionChanged(this, topPtrState, 1.0f);
        }
    }

    private void bottomScrollAnimation() {
        if (mode != MODE_BOTTOM) {
            return;
        }
        int currY = contentView.getTop();
        int offset = currY - maxScrollY;
        if (offset < 0) {
            ptrState = PTR_STATE_IDLE;
            pullToRefreshView.onPositionChanged(this, ptrState, 0.0f);
        } else if (offset < PTR_PREPARE_HEIGHT) {
            ptrState = PTR_STATE_PREPARE;
            pullToRefreshView.onPositionChanged(this, ptrState, (float) offset/(float) PTR_PREPARE_HEIGHT);
        } else {
            ptrState = PTR_STATE_LOADING;
            pullToRefreshView.onPositionChanged(this, ptrState, (float) (offset-PTR_PREPARE_HEIGHT)/(float) PTR_MAX_HEIGHT);
        }
    }

    private void footerScrollAnimation() {
        if (mode != MODE_TOP) {
            return;
        }

        int currY = contentView.getTop();
        if (currY > 0) {
            return;
        }

        if (currY == 0) {
            footerPtrState = PTR_STATE_IDLE;
            footerPullToRefreshView.onPositionChanged(this, footerPtrState, 0.0f);
        } else if (currY > -PTR_PREPARE_HEIGHT) {
            footerPtrState = PTR_STATE_PREPARE;
            footerPullToRefreshView.onPositionChanged(this, footerPtrState, (float) -currY / (float) PTR_PREPARE_HEIGHT);
        } else {
            footerPtrState = PTR_STATE_LOADING;
            footerPullToRefreshView.onPositionChanged(this, footerPtrState, 1.0f);
        }
    }

    //内部滚动前,优先处理外部滚动事件
    private int preScrollY(int deltaY) {
        state = STATE_DRAGGING;
        int consumedY;

        int currY = contentView.getTop();
        if (currY == 0) {
            //已经位于顶部,则优先内部滚动
            consumedY = 0;
        } else if (currY > 0) {
            //计算滚动到顶部最多消耗的距离
            consumedY = currY - deltaY > 0 ? deltaY : currY;
            //限制下拉滚动的最大距离
            int dy;
            if (mode == MODE_BOTTOM) {
                dy = currY - consumedY > maxScrollY + PTR_MAX_HEIGHT ? currY - maxScrollY - PTR_MAX_HEIGHT : consumedY;
                if (currY - dy > maxScrollY) {
                    //底部的偏移值
                    float offset = Math.min ((float) (currY - dy - maxScrollY) / (float) PTR_MAX_HEIGHT, 1.0f);
                    float dstOffset = ptrInterpolator.getInterpolation(offset);
                    int dstY = (int) (dstOffset * PTR_MAX_HEIGHT);
                    contentView.offsetTopAndBottom(dstY-(currY-maxScrollY));
                } else {
                    contentView.offsetTopAndBottom(-dy);
                }
            } else {
                int maxHeight;
                if (onlyTopMode) {
                    maxHeight = PTR_MAX_HEIGHT - 20;
                } else {
                    maxHeight = PTR_RESTORE_HEIGHT;
                }
                dy = currY - consumedY > maxHeight ? currY - maxHeight : consumedY;
//                if (currY - dy > 0) {
//                    float offset = Math.min((float) (currY - dy) / (float) maxHeight, 1.0f);
//                    float dstOffset = ptrInterpolator.getInterpolation(offset);
//                    int dstY = (int) (dstOffset * maxHeight);
//                    contentView.offsetTopAndBottom(dstY-currY);
//                } else {
//                    contentView.offsetTopAndBottom(-dy);
//                }
                contentView.offsetTopAndBottom(-dy);
            }
        } else {
            consumedY = currY - deltaY < 0 ? deltaY : currY;
            int dy = currY - consumedY < -PTR_MAX_HEIGHT ? currY + PTR_MAX_HEIGHT : consumedY;
            contentView.offsetTopAndBottom(-dy);
        }

        //滚动到顶部时则切换至Top模式
        if (contentView.getTop() == 0) {
            setMode(MODE_TOP);
        }

        if (consumedY != 0) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
        return consumedY;
    }

    //内部滚动结束后,处理剩余滚动量
    private void scrollY(int deltaY) {
        state = STATE_DRAGGING;
//        int currY = contentView.getTop();
//        if (currY == 0 && deltaY < 0) {
//            //当位于顶部,并且内部滚动已到上边界,这时候继续往下滚动的量用来处理外部滚动;
//            contentView.offsetTopAndBottom(-deltaY);
//        } else if (currY == 0 && deltaY > 0) {
//            //内部滚动到达下边界,继续往上滚动的量用来处理外部滚动
//            contentView.offsetTopAndBottom(-deltaY);
//        }

        contentView.offsetTopAndBottom(-deltaY);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        pullToRefreshView = (PullToRefreshView) getChildAt(0);
        topPullToRefreshView = (TopPullToRefreshView) getChildAt(1);
        topPullToRefreshView.setVisibility(INVISIBLE);
        footerPullToRefreshView = (FooterPullToRefreshView) getChildAt(2);
        headerView = getChildAt(3);
        contentView = getChildAt(4);
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
            int top;
            if (onlyTopMode) {
                top = 0;
            } else {
                top = maxScrollY;
            }
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

        {
            int left = 0;
            int top = 0;
            int right = left + topPullToRefreshView.getMeasuredWidth();
            int bottom = top + topPullToRefreshView.getMeasuredHeight();
            topPullToRefreshView.layout(left, top, right, bottom);
        }

        {
            int left = 0;
            int top = getHeight() - footerPullToRefreshView.getMeasuredHeight();
            int right = left + footerPullToRefreshView.getMeasuredWidth();
            int bottom = getHeight();
            footerPullToRefreshView.layout(left, top, right, bottom);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
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
        settle(0, 0);
    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        scrollY(dyUnconsumed);
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {

        int consumedY = preScrollY(dy);
        consumed[0] = 0;
        consumed[1] = consumedY;
    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        return false;
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        return settle(velocityX, velocityY);
    }

    @Override
    public int getNestedScrollAxes() {
        return nestedScrollingParentHelper.getNestedScrollAxes();
    }

    private boolean settle(float velocityX, float velocityY) {

        //已经在惯性滑动时滑动事件外部不再处理
        if (state == STATE_SETTLING) {
            return false;
        }

        int top = contentView.getTop();
        if (top == 0) {
            //当位于顶部时,滑动交给内部处理,外部不处理
            return false;
        }

        mTension = Math.abs(velocityY / mMaxVelocity) * 3;
        if (mTension < MIN_TENSION) {
            mTension = MIN_TENSION;
        }

        if (top <= maxScrollY) {

            int finalTopY;

            if (mode == MODE_BOTTOM) {
                final float offset = 1 - (float)top / (float)maxScrollY;
                final float yvel = clampMag(velocityY, mMinVelocity, mMaxVelocity);
                finalTopY = yvel > 0 || yvel == 0 && offset > 0.5f ? 0 : maxScrollY;
            } else {
                if (topPtrState == PTR_STATE_LOADING) {
                    //多加的20像素为防止弹性效果引起的动画上的Bug
                    finalTopY = PTR_PREPARE_HEIGHT + 20;
                    topPullToRefreshView.setRefreshing(true);
                    topRefresh();
                } else if (topPtrState == PTR_STATE_RESTORE) {
                    finalTopY = maxScrollY;
                    topPullToRefreshView.setVisibility(INVISIBLE);
                    headerView.setVisibility(VISIBLE);
                } else if (footerPtrState == PTR_STATE_LOADING) {
                    finalTopY = -PTR_PREPARE_HEIGHT - 20;
                    footerPullToRefreshView.setRefreshing(true);
                    footerRefresh();
                } else {
                    finalTopY = 0;
                }
            }
            return forceSettleCapturedViewAt(0, finalTopY, 0, (int)velocityY);
        } else {
            int finalTopY = ptrState == PTR_STATE_LOADING ? 0 : maxScrollY;
            return forceSettleCapturedViewAt(0, finalTopY, 0, (int)velocityY);
        }
    }

    public boolean continueSettling() {

        if (state == STATE_IDLE || state == STATE_DRAGGING) {
            return false;
        }

        boolean keepGoing = scrollerCompat.computeScrollOffset();
        final int y = scrollerCompat.getCurrY();
        final int dy = y - contentView.getTop();
        contentView.offsetTopAndBottom(dy);

//        if (keepGoing && y == scrollerCompat.getFinalY()) {
//            // Close enough. The interpolator/scroller might think we're still moving
//            // but the user sure doesn't.
//            scrollerCompat.abortAnimation();
//            state = STATE_IDLE;
//            keepGoing = false;
//        }

        if (!keepGoing) {
            state = STATE_IDLE;
            if (contentView.getTop() == 0) {
                //设置为顶部模式
                setMode(MODE_TOP);
            } else if (contentView.getTop() == maxScrollY) {
                //设置为底部模式
                setMode(MODE_BOTTOM);
            }
        }

        return keepGoing;
    }

    private void setMode(int mode) {
        if (mode == MODE_TOP) {
            //设置为顶部模式
            this.mode = MODE_TOP;
            pullToRefreshView.setVisibility(INVISIBLE);
            topPullToRefreshView.setVisibility(VISIBLE);
            headerView.setVisibility(INVISIBLE);
        } else {
            this.mode = MODE_BOTTOM;
            pullToRefreshView.setVisibility(VISIBLE);
        }
        modeChanged(mode);
    }

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
            t -= 1.0f;
            return t * t * ((mTension + 1) * t + mTension) + 1.0f;
        }
    };

    private final Interpolator ptrInterpolator = new Interpolator() {

        @Override
        public float getInterpolation(float input) {
            return (float) (Math.sin(input+0.25) - Math.sin(0.25));
        }
    };
}
