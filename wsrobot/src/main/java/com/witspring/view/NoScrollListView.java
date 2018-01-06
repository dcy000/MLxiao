package com.witspring.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * 不能滚动的ListView
 * @author Created by Goven on 14/12/17 下午6:54
 * @email gxl3999@gmail.com
 */
public class NoScrollListView extends ListView {

    public NoScrollListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NoScrollListView(Context context) {
        super(context);
    }

    /**
     * 设置list不能滚动 直接显示所有item
     */
    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}