package com.witspring.view.pageflow;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;

import com.witspring.mlrobot.R;

/**
 * @author Created by Goven on 16/12/26 上午10:02
 * @email gxl3999@gmail.com
 */
public class PageFlowView extends LinearLayout {

    private PageScrollView scrollView;
    private PageFlowIndicator indicator;
    private int indicatorHeight, indicatorMargin, spacing, maxRows, maxPages;
    private int oldPageSize;
    private int scrollStatus;
    public static final int SCROLL_TO_HEAD = 1, SCROLL_TO_END = 2, SCROLL_HOLD = 0;

    public PageFlowView(Context context) {
        super(context);
        init();
    }

    public PageFlowView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.WSPageFlowView);
        try {
            indicatorMargin = a.getInt(R.styleable.WSPageFlowView_ws_indicatorMargin, 0);
            maxRows = a.getInt(R.styleable.WSPageFlowView_ws_maxRows, 0);
            maxPages = a.getInt(R.styleable.WSPageFlowView_ws_maxPages, 0);
        } finally {
            a.recycle();
        }
        init();
    }

    public PageFlowView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.WSPageFlowView, defStyle, 0);
        try {
            indicatorMargin = a.getInt(R.styleable.WSPageFlowView_ws_indicatorMargin, 0);
            maxRows = a.getInt(R.styleable.WSPageFlowView_ws_maxRows, 0);
            maxPages = a.getInt(R.styleable.WSPageFlowView_ws_maxPages, 0);
        } finally {
            a.recycle();
        }
        init();
    }

    private void init() {
        setOrientation(VERTICAL);
        if (scrollView == null) {
            scrollView = new PageScrollView(getContext());
            indicator = new PageFlowIndicator(getContext());
            LayoutParams svParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
            svParams.weight = 1;
            addView(scrollView, svParams);
            LayoutParams inParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            indicatorMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics());
            inParams.topMargin = indicatorMargin;
            addView(indicator, inParams);
            scrollView.setIndicator(indicator);
            scrollView.setMaxRowsAndPages(maxRows, maxPages);
            indicatorHeight = 17;
        }
    }

    public void setData(String[] data, PageScrollView.TagStyle tagStyle) {
        if (scrollView != null) {
            scrollView.setData(data, tagStyle);
        }
    }

    public CheckedTextView addTag(String text, PageScrollView.TagStyle tagStyle) {
        return addTag(SCROLL_HOLD, text, tagStyle);
    }

    public CheckedTextView addTag(int scrollTo, String text, PageScrollView.TagStyle tagStyle) {
        if (scrollView != null) {
            this.scrollStatus = scrollTo;
            if (scrollStatus == SCROLL_TO_END) {
                oldPageSize = scrollView.getPageSize();
            }
            CheckedTextView tag = scrollView.addTag(text, tagStyle);
            return tag;
        }
        return null;
    }

    public CheckedTextView addTag(String text, PageScrollView.TagStyle tagStyle, int index) {
        if (scrollView != null) {
            return scrollView.addTag(text, tagStyle, index);
        }
        return null;
    }

    public int getTagCount() {
        return scrollView.getTagCount();
    }

    public void removeTagView(View view) {
        oldPageSize = scrollView.getPageSize();
        scrollStatus = SCROLL_TO_END;
        oldPageSize = scrollView.getPageSize();
        scrollView.removeTagView(view);
    }

    public void removeTagViewAt(int index) {
        oldPageSize = scrollView.getPageSize();
        scrollStatus = SCROLL_TO_END;
        oldPageSize = scrollView.getPageSize();
        scrollView.removeTagViewAt(index);
    }

    public void removeAllTagView() {
        scrollView.removeAllTagView();
    }

    public View findTagView(Object obj) {
        return scrollView.findTagView(obj);
    }

    public void setOnTagClickListener(PageScrollView.OnTagClickListener tagClickListener) {
        if (scrollView != null) {
            scrollView.setOnTagClickListener(tagClickListener);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        scrollView.measureHeight = sizeHeight - indicatorHeight - indicatorMargin - getPaddingTop() - getPaddingBottom();
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (sizeWidth == 0 || sizeHeight == 0) {
            return;
        }
        if (scrollView != null && scrollView.getTagCount() > 0) {
            int scrollViewHeight = scrollView.getMeasuredHeight();
            int topPadding = getPaddingTop();
            int bottomPadding = getPaddingBottom();
            int totalHeight = scrollViewHeight + topPadding + bottomPadding;
            totalHeight += indicatorHeight + indicatorMargin;
            if (sizeHeight >= totalHeight) {
                spacing = sizeHeight - totalHeight;
                setMeasuredDimension(sizeWidth, totalHeight);
                if (scrollView.getPageSize() > 1) {
                    indicator.setVisibility(VISIBLE);
                } else {
                    indicator.setVisibility(INVISIBLE);
                }
            }
            if (scrollStatus == SCROLL_TO_END) {
                int newSize = scrollView.getPageSize();
                if (newSize != oldPageSize) {
                    scrollView.setCurrentPage(newSize - 1);
                }
                scrollStatus = SCROLL_HOLD;
                oldPageSize = 0;
            } else if (scrollStatus == SCROLL_TO_HEAD) {
                scrollView.setCurrentPage(0);
            }
        } else {
            setMeasuredDimension(sizeWidth, 0);
        }
        Log.e("PageFlowView", "MeasureSpec_Width: " + sizeWidth + ", MeasureSpec_Height: " + sizeHeight);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    public int getSpacing() {
        return spacing;
    }
}