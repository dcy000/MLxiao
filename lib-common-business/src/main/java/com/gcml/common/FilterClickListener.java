package com.gcml.common;

import android.view.View;

import com.gcml.common.utils.display.ToastUtils;

/**
 * Created by lenovo on 2019/1/21.
 */

public class FilterClickListener implements View.OnClickListener {
    public View.OnClickListener clickListener;
    public static final int FILTER_TIME = 1000;
    public int filterTime = FILTER_TIME;
    private long lastTime;

    public FilterClickListener() {
    }

    public FilterClickListener(View.OnClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void setFilterTime(int filterTime) {
        this.filterTime = filterTime;
    }

    public void setClickListener(View.OnClickListener clickListener) {
        this.clickListener = clickListener;
    }


    @Override
    public void onClick(View view) {
        if (clickListener != null) {
            if (System.currentTimeMillis() - lastTime >= filterTime) {
                lastTime = System.currentTimeMillis();
                clickListener.onClick(view);
            }else {
//                ToastUtils.showShort("点击过快");
            }
        }
    }
}
