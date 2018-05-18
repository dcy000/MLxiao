package com.ml.brightness;

import android.support.annotation.IntDef;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 *
 * Created by afirez on 2018/5/17.
 */

public class GestureHelper extends GestureDetector.SimpleOnGestureListener {

    @IntDef({
            ScrollMode.NONE,
            ScrollMode.RIGHT_VERTICAL,
            ScrollMode.LEFT_VERTICAL,
            ScrollMode.HORIZONTAL
    })
    public @interface ScrollMode {
        int NONE = 0;
        int RIGHT_VERTICAL = 1;
        int LEFT_VERTICAL = 2;
        int HORIZONTAL = 3;
    }

    @ScrollMode
    private int mScrollMode = ScrollMode.NONE;

    private boolean mHasScrollModeHorizontal;

    private int offsetX = 1;

    private int mWidth;

    private int mHeight;

    @ScrollMode
    public int getScrollMode() {
        return mScrollMode;
    }

    public int getWidth() {
        return mWidth;
    }

    public int getHeight() {
        return mHeight;
    }

    private GestureDetector mDetector;

    public GestureHelper(final View view) {
        mDetector = new GestureDetector(view.getContext(), this);
        mDetector.setIsLongpressEnabled(false);
        view.post(new Runnable() {
            @Override
            public void run() {
                mWidth = view.getWidth();
                mHeight = view.getHeight();
            }
        });
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                boolean consumed = mDetector.onTouchEvent(event);
                onTouchEvent(event);
                if (MotionEvent.ACTION_UP == event.getAction()
                        && mHasScrollModeHorizontal) {
                    onScrollModeHorizontalComplete(event);
                    mHasScrollModeHorizontal = false;
                }
                return consumed;
            }
        });
    }


    public void onScrollModeHorizontalComplete(MotionEvent event) {

    }

    @Override
    public boolean onDown(MotionEvent e) {
        mScrollMode = ScrollMode.NONE;
        mHasScrollModeHorizontal = false;
        return true;
    }

    public void onTouchEvent(MotionEvent event) {

    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        switch (mScrollMode) {
            case ScrollMode.NONE:
                if (Math.abs(distanceX) - Math.abs(distanceY) > offsetX) {
                    mScrollMode = ScrollMode.HORIZONTAL;
                } else {
                    if (e1.getX() < mWidth / 2) {
                        mScrollMode = ScrollMode.LEFT_VERTICAL;
                    } else {
                        mScrollMode = ScrollMode.RIGHT_VERTICAL;
                    }
                }
                break;
            case ScrollMode.LEFT_VERTICAL:
            case ScrollMode.RIGHT_VERTICAL:
            case ScrollMode.HORIZONTAL:
                onScroll(mScrollMode, e1, e2, distanceX, distanceY);
                if (mScrollMode == ScrollMode.HORIZONTAL) {
                    mHasScrollModeHorizontal = true;
                }
                break;
        }
        return true;
    }

    public void onScroll(
            @ScrollMode int scrollMode,
            MotionEvent e1,
            MotionEvent e2,
            float distanceX,
            float distanceY) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return super.onFling(e1, e2, velocityX, velocityY);
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        return super.onSingleTapConfirmed(e);
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return super.onSingleTapUp(e);
    }

    @Override
    public void onShowPress(MotionEvent e) {
        super.onShowPress(e);
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        return super.onDoubleTap(e);
    }

}
