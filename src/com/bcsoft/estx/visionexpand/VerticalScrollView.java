package com.bcsoft.estx.visionexpand;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.VelocityTrackerCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.ScrollView;

public class VerticalScrollView extends ScrollView {
    View.OnTouchListener mGestureListener;
    private VelocityTracker mVelocityTracker;
    private int mMaximumVelocity;

    public VerticalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFadingEdgeLength(0);
        final ViewConfiguration configuration = ViewConfiguration.get(context);
        mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();
    }

    private boolean doNotInterceptUntilUp = false;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        // System.out.println("onInterceptTouchEvent");
        int action = ev.getAction();
        int index = MotionEventCompat.getActionIndex(ev);
        int mActivePointerId = MotionEventCompat.getPointerId(ev, index);

        if (action == MotionEvent.ACTION_DOWN) {
            if (mVelocityTracker == null) {
                mVelocityTracker = VelocityTracker.obtain();
            }
            mVelocityTracker.addMovement(ev);
        } else if (action == MotionEvent.ACTION_MOVE) {
            if (doNotInterceptUntilUp) {
                return false;
            }
            mVelocityTracker.addMovement(ev);
            mVelocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
            int initialVelocity = (int) VelocityTrackerCompat.getXVelocity(
                    mVelocityTracker, mActivePointerId);
            int initialYVelocity = (int) VelocityTrackerCompat.getYVelocity(
                    mVelocityTracker, mActivePointerId);
            if (Math.abs(initialVelocity) > 100) {
                // 140 is the minimum threshold it seems.
                if (!(Math.abs(initialYVelocity) > 140))
                    doNotInterceptUntilUp = true;
                return false;
            }
        } else if (action == MotionEvent.ACTION_UP) {
            doNotInterceptUntilUp = false;
            if (mVelocityTracker != null) {
                mVelocityTracker.clear();
            }
        }
        boolean val = super.onInterceptTouchEvent(ev);
        return val;
    }
}