package com.example.han.referralproject.yisuotang.wrap;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by lenovo on 2018/7/4.
 */

public class NoScrollVP extends ViewPager {
    public NoScrollVP(@NonNull Context context) {
        super(context);
    }

    public NoScrollVP(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;
    }
}
