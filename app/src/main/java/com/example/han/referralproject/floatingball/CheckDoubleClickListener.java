package com.example.han.referralproject.floatingball;

import android.view.View;

import java.util.Calendar;

/**
 * Created by han on 2017/12/11.
 */

public class CheckDoubleClickListener implements View.OnClickListener {
    public static final int MIN_CLICK_DELAY_TIME = 1000;
    private long lastClickTime = 0;
    private OnCheckDoubleClick checkDoubleClick;

    public CheckDoubleClickListener(OnCheckDoubleClick checkDoubleClick){
        this.checkDoubleClick = checkDoubleClick;
    }

    @Override
    public void onClick(View v) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime;
            checkDoubleClick.onCheckDoubleClick(v);
        }
    }
}
