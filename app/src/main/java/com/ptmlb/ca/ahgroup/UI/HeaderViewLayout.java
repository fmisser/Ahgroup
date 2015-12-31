package com.ptmlb.ca.ahgroup.ui;

import android.content.Context;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.NestedScrollingParentHelper;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2015/12/31.
 *
 */

public class HeaderViewLayout extends ViewGroup implements NestedScrollingParent {

    private final NestedScrollingParentHelper nestedScrollingParentHelper;

    public HeaderViewLayout(Context context) {
        this(context, null);
    }

    public HeaderViewLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HeaderViewLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        nestedScrollingParentHelper = new NestedScrollingParentHelper(this);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

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
    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        nestedScrollingParentHelper
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {

    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        return false;
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        return false;
    }

    @Override
    public int getNestedScrollAxes() {
        return 0;
    }
}
