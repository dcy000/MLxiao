package com.ml.bci.game.common.widget.recyclerview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by afirez on 2018/6/22.
 */

public class NoTouchRecyclerView extends RecyclerView {
    private boolean interceptTouch;

    public NoTouchRecyclerView(Context context) {
        super(context);
    }

    public NoTouchRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public NoTouchRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        if (interceptTouch) {
            return super.onInterceptTouchEvent(e);
        }
        return false;
    }

    public void setInterceptTouch(boolean interceptTouch) {
        this.interceptTouch = interceptTouch;
    }

    public boolean isInterceptTouch() {
        return interceptTouch;
    }
}
