package com.gcml.common.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

/**
 * 作者：wecent
 * 时间：2018/4/25
 * 描述：
 */

public class RecyclerForScrollView extends RecyclerView {

    public RecyclerForScrollView(Context context) {
        super(context);
    }

    public RecyclerForScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RecyclerForScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = View.MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                View.MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
