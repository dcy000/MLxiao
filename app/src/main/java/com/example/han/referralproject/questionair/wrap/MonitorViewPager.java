package com.example.han.referralproject.questionair.wrap;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by lenovo on 2018/5/8.
 */

public class MonitorViewPager extends ViewPager {
    public MonitorViewPager(@NonNull Context context) {
        super(context);
    }

    public MonitorViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
//        return super.onTouchEvent(ev);
        //不消费
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        return super.onInterceptTouchEvent(ev);
        //不拦截
        return false;
    }
}
