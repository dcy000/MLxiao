package com.witspring.view.pageflow;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * @author Created by Goven on 2017/12/27 下午8:30
 * @email gxl3999@gmail.com
 */
public class LimitHeightLinearLayout extends LinearLayout {

    private int maxHeight;

    public LimitHeightLinearLayout(Context context) {
        super(context);
    }

    public LimitHeightLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public LimitHeightLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (heightMode == MeasureSpec.UNSPECIFIED || maxHeight <= 0 ) {
            return;
        }
        int height = getMeasuredHeight();
        int specWidthSize = MeasureSpec.getSize(widthMeasureSpec);
        if (height > maxHeight) {
//            setMeasuredDimension(specWidthSize, maxHeight);
            measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(maxHeight, heightMode));
        }
    }

}
