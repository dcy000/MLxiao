package com.gcml.module_health_profile.checklist.wrap;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class EqualFlowLayout extends ViewGroup {

    private int paddingTop;
    private int paddingLeft;
    private int paddingRight;
    private int equalWidthSize;

    public EqualFlowLayout(Context context) {
        this(context, null);
    }

    public EqualFlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EqualFlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        //获取padding
        int lineX = paddingLeft;
        int lineY = paddingTop;
        int lineWight = r - l;
        if (equalWidthSize != 0) {

        }
        for (int i = 0; i < getChildCount(); i++) {
            View childAt = getChildAt(i);
            if (childAt.getVisibility() == View.GONE) {
                continue;
            }
            int childWidth = childAt.getMeasuredWidth();
            int childHeight = childAt.getMeasuredHeight();

            LayoutParams layoutParams = childAt.getLayoutParams();
            if (layoutParams instanceof MarginLayoutParams) {
                MarginLayoutParams marginLayoutParams = (MarginLayoutParams) layoutParams;
                childWidth += marginLayoutParams.leftMargin;
                childWidth += marginLayoutParams.rightMargin;
                childHeight += marginLayoutParams.topMargin;
                childHeight += marginLayoutParams.bottomMargin;
            }

            if (equal) {
                childWidth = quiteWidthSize(childWidth, equalWidthSize);
            }

            if (lineX + childWidth > lineWight) {
                lineX = paddingLeft;
                lineY += childHeight;
            }

            childAt.layout(lineX, lineY, lineX + childWidth, lineY + childHeight);
            lineX += childWidth;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //获取控件padding值
        paddingTop = getPaddingTop();
        paddingLeft = getPaddingLeft();
        paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();
        //获取控件宽高
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        //等分的空间宽度
        equalWidthSize = (int) ((widthSize - paddingLeft - paddingRight) / equalScale);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        //初始化
        int lineWidth = paddingLeft + paddingRight;
        int lineY = paddingTop;
        int lineHeight = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View childAt = getChildAt(i);
            if (childAt.getVisibility() == View.GONE) {
                continue;
            }
            int spaceWidth = 0;
            int spaceHeight = 0;

            LayoutParams layoutParams = childAt.getLayoutParams();
            if (layoutParams instanceof MarginLayoutParams) {
                measureChildWithMargins(childAt, widthMeasureSpec, 0, heightMeasureSpec, lineY);
                MarginLayoutParams marginLayoutParams = (MarginLayoutParams) layoutParams;
                spaceWidth = marginLayoutParams.leftMargin + marginLayoutParams.rightMargin;
                spaceHeight = marginLayoutParams.topMargin + marginLayoutParams.bottomMargin;
            } else {
                measureChild(childAt, widthMeasureSpec, heightMeasureSpec);
            }
            int childWidth = childAt.getMeasuredWidth();
            int childHeight = childAt.getMeasuredHeight();
            spaceWidth += childWidth;
            spaceHeight += childHeight;
            if (spaceHeight > lineHeight) {
                lineHeight = spaceHeight;
            }
            if (equal) {
                spaceWidth = quiteWidthSize(spaceWidth, equalWidthSize);
            }

            if (lineWidth + spaceWidth > widthSize) {
                lineY += lineHeight;
                lineWidth = this.paddingLeft + this.paddingRight;
                lineHeight = spaceHeight;
//                lineHeight = 0;
            }

            lineWidth += spaceWidth;
        }
        setMeasuredDimension(widthSize, heightMode == MeasureSpec.EXACTLY ? heightSize : lineY + lineHeight + paddingBottom);
    }

    /**
     * 比较宽度
     *
     * @param spaceWidth
     * @param equalWidthSize
     * @return
     */
    private int quiteWidthSize(int spaceWidth, int equalWidthSize) {
        if (spaceWidth <= equalWidthSize) {
            return equalWidthSize;
        } else if (spaceWidth <= equalWidthSize * 2) {
            return equalWidthSize * 2;
        } else if (spaceWidth <= equalWidthSize * 3) {
            return equalWidthSize * 3;
        } else {
            return equalWidthSize * 4;
        }
    }

    //平分属性->默认4等分
    boolean equal = true;
    float equalScale = 4f;

    /**
     * 设置等分,并设置等分份数
     *
     * @param equal
     * @param equalScale
     * @return
     */
    public EqualFlowLayout isEqual(boolean equal, int equalScale) {
        this.equal = equal;
        this.equalScale = equalScale;
        return this;
    }

    /**
     * 直接设置不等分
     *
     * @return
     */
    public EqualFlowLayout unEqual() {
        this.equal = false;
        return this;
    }


    FlowAdapte adapte;

    public void setAdapte(FlowAdapte adapte) {
        this.adapte = adapte;
        //这里处理数据
        initData();
    }

    private void initData() {
        removeAllViews();
        int count = adapte.getCount();
        for (int i = 0; i < count; i++) {
            View view = adapte.getChildView(i);
            if (view != null)
                addView(view);
        }
    }

}
