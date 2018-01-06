package com.witspring.view.pageflow;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.HorizontalScrollView;

/**
 * @author Created by Goven on 16/12/23 上午11:44
 * @email gxl3999@gmail.com
 */
public class PageScrollView extends HorizontalScrollView {

    public static final String TAG = "PageScrollView";
    private PageFlowLayout flowLayout;
    private PageFlowIndicator indicator;
    private OnTagClickListener tagClickListener;
    private GestureDetector detector;
    private int currentPage;
    private int origWidth, origHeight, flingMinDistance;
    int measureHeight;

    public PageScrollView(Context context) {
        super(context);
        init();
    }

    public PageScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setHorizontalScrollBarEnabled(false);
        if (flowLayout == null) {
            View child = getChildAt(0);
            if (child != null && child instanceof PageFlowLayout) {
                flowLayout = (PageFlowLayout) child;
            } else {
                removeAllViews();
                flowLayout = new PageFlowLayout(getContext());
                addView(flowLayout, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            }
        }
    }

    public void setIndicator(PageFlowIndicator indicator) {
        this.indicator = indicator;
        indicator.setPageView(this);
    }

    public void setMaxRowsAndPages(int maxRows, int maxPages) {
        flowLayout.setMaxRowsAndPages(maxRows, maxPages);
    }

    public void setData(String[] data, TagStyle tagStyle) {
        if (origHeight != 0) {
            // 还原初始高度
            ViewGroup.LayoutParams lp = getLayoutParams();
            lp.height = origHeight;
            setLayoutParams(lp);
            origHeight = 0;
        }
        int childCount = flowLayout.getChildCount();
        for (int i = 0; i < data.length; i++) {
            if (i < childCount) {
                CheckedTextView tvItem = (CheckedTextView) flowLayout.getChildAt(i);
                updateTag(tvItem, data[i], tagStyle);
            } else {
                addTag(data[i], tagStyle);
            }
        }
        while (flowLayout.getChildCount() > data.length) {
            flowLayout.removeViewAt(data.length);
        }
    }

    public CheckedTextView addTag(String text, TagStyle tagStyle) {
        return addTag(text, tagStyle, -1);
    }

    public CheckedTextView addTag(String text, TagStyle tagStyle, int index) {
        CheckedTextView tvItem = new CheckedTextView(getContext());
        PageFlowLayout.LayoutParams lp = new PageFlowLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        if (tagStyle != null) {
            tvItem.setBackgroundResource(tagStyle.backgroundResId);
            if (tagStyle.textColorState != null) {
                tvItem.setTextColor(tagStyle.textColorState);
            } else {
                tvItem.setTextColor(tagStyle.textColor);
            }
            if (tagStyle.textSize != 0) {
                tvItem.setTextSize(TypedValue.COMPLEX_UNIT_PX, tagStyle.textSize);
            }
            tvItem.setPadding(tagStyle.LeftPadding, tagStyle.topPadding, tagStyle.rightPadding, tagStyle.bottomPadding);
            lp.topMargin = tagStyle.topMargin;
            lp.bottomMargin = tagStyle.bottomMargin;
            lp.leftMargin = tagStyle.leftMargin;
            lp.rightMargin = tagStyle.rightMargin;
            tvItem.setGravity(tagStyle.gravity);
            if (tagStyle.maxWidth > 0) {
                tvItem.setMaxWidth(tagStyle.maxWidth);
            }
        } else {
            tvItem.setBackgroundColor(Color.GRAY);
            tvItem.setTextColor(Color.WHITE);
            tvItem.setGravity(Gravity.CENTER);
            int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics());
            tvItem.setPadding(3 * margin, (int)(1.2f * margin), 3 * margin, (int)(1.2f * margin));
            lp.topMargin = 2 * margin;
            lp.bottomMargin = 2 * margin;
            lp.leftMargin = 2 * margin;
            lp.rightMargin = 2 * margin;
        }
        tvItem.setMaxLines(1);
        tvItem.setEllipsize(TextUtils.TruncateAt.END);
        tvItem.setText(text);
        tvItem.setTag(text);
        tvItem.setOnClickListener(clickListener);
        tvItem.setLayoutParams(lp);
        if (index >= 0) {
            flowLayout.addView(tvItem, index);
        } else {
            flowLayout.addView(tvItem);
        }
        return tvItem;
    }

    public void updateTag(CheckedTextView tvItem, String text, TagStyle tagStyle) {
        tvItem.setText(text);
        tvItem.setTag(text);
        if (tvItem.getVisibility() == GONE) {
            tvItem.setVisibility(VISIBLE);
            if (tagStyle != null) {
                PageFlowLayout.LayoutParams lp = (PageFlowLayout.LayoutParams) tvItem.getLayoutParams();
                tvItem.setBackgroundResource(tagStyle.backgroundResId);
                if (tagStyle.textColorState != null) {
                    tvItem.setTextColor(tagStyle.textColorState);
                } else {
                    tvItem.setTextColor(tagStyle.textColor);
                }
                if (tagStyle.textSize != 0) {
                    tvItem.setTextSize(TypedValue.COMPLEX_UNIT_PX, tagStyle.textSize);
                }
                tvItem.setPadding(tagStyle.LeftPadding, tagStyle.topPadding, tagStyle.rightPadding, tagStyle.bottomPadding);
                lp.topMargin = tagStyle.topMargin;
                lp.bottomMargin = tagStyle.bottomMargin;
                lp.leftMargin = tagStyle.leftMargin;
                lp.rightMargin = tagStyle.rightMargin;
                tvItem.setLayoutParams(lp);
            }
        }
    }

    public void removeTagView(View view) {
        if (view != null) {
            flowLayout.removeView(view);
        }
    }

    public void removeTagViewAt(int index) {
        flowLayout.removeViewAt(index);
    }

    public void removeAllTagView() {
        flowLayout.removeAllViews();
    }

    public View findTagView(Object obj) {
        return flowLayout.findViewWithTag(obj);
    }

    private OnClickListener clickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            Object obj = view.getTag();
            if (tagClickListener != null) {
                tagClickListener.onClick(view, obj);
            }
        }
    };

    public void setOnTagClickListener(OnTagClickListener tagClickListener) {
        this.tagClickListener = tagClickListener;
    }

    public interface OnTagClickListener {
        void onClick(View view, Object obj);
    }

    public static class TagStyle {

        int backgroundResId;
        int textColor;
        ColorStateList textColorState;
        int textSize;
        int LeftPadding, topPadding, rightPadding, bottomPadding;
        int leftMargin, topMargin, rightMargin, bottomMargin;
        int gravity = Gravity.CENTER;
        int maxWidth;

        private TagStyle () {
        }

        public static TagStyle build() {
            return new TagStyle();
        }

        public TagStyle backgroundResId(int backgroundResId) {
            this.backgroundResId = backgroundResId;
            return this;
        }

        public TagStyle textColor(int textColor) {
            this.textColor = textColor;
            return this;
        }

        public TagStyle textColorState(ColorStateList textColorState) {
            this.textColorState = textColorState;
            return this;
        }

        public TagStyle textSize(int textSize) {
            this.textSize = textSize;
            return this;
        }

        public TagStyle maxWidth(int maxWidth) {
            this.maxWidth = maxWidth;
            return this;
        }

        public TagStyle padding(int padding) {
            this.LeftPadding = padding;
            this.topPadding = padding;
            this.rightPadding = padding;
            this.bottomPadding = padding;
            return this;
        }

        public TagStyle padding(int left, int top, int right, int bottom) {
            this.LeftPadding = left;
            this.topPadding = top;
            this.rightPadding = right;
            this.bottomPadding = bottom;
            return this;
        }

        public TagStyle margin(int margin) {
            this.leftMargin = margin;
            this.topMargin = margin;
            this.rightMargin = margin;
            this.bottomMargin = margin;
            return this;
        }

        public TagStyle margin(int left, int top, int right, int bottom) {
            this.leftMargin = left;
            this.topMargin = top;
            this.rightMargin = right;
            this.bottomMargin = bottom;
            return this;
        }

        public TagStyle gravity(int gravity) {
            this.gravity = gravity;
            return this;
        }

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(measureHeight, MeasureSpec.getMode(heightMeasureSpec));
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = measureHeight;
        if (sizeWidth != 0 && sizeHeight != 0 && getTagCount() > 0) {
            if (origHeight == 0 && indicator != null) {
                setCurrentPage(0);
            }
            origWidth = sizeWidth;
            origHeight = sizeHeight;
            flowLayout.setPageWidth(origWidth);
            flowLayout.setPageHeight(origHeight);
            measureChild(flowLayout, widthMeasureSpec, heightMeasureSpec);
            int childHeight = flowLayout.getRealHeight();
            if (childHeight > 0 && sizeHeight > childHeight) {
                setMeasuredDimension(origWidth, childHeight);
                flingMinDistance = origWidth / 5;
            }
        }
    }

    private float downX = -1, upX = -1;
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (detector == null) {
            detector = new GestureDetector(getContext(), new SimpleOnGestureListener() {
                @Override
                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                    upX = e2.getX();
                    float offsetX = downX - upX;
                    Log.e(TAG, "GestureDetector处理滑动，offsetX:" + offsetX);
                    if (Math.abs(offsetX) >= flingMinDistance) {
                        if (offsetX > 0 && currentPage != flowLayout.getPageSize() - 1) {
                            setCurrentPage(currentPage + 1);
                        } else if (offsetX < 0 && currentPage != 0) {
                            setCurrentPage(currentPage - 1);
                        }
                    } else {
                        setCurrentPage(currentPage);
                    }
                    return true;
                }
            });
        }
        boolean done = detector.onTouchEvent(ev);
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            Log.e(TAG, "MotionEvent.getAction: ACTION_DOWN, down_x:" + ev.getX());
            downX = ev.getX();
        } else if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            Log.e(TAG, "MotionEvent.getAction: ACTION_MOVE, move_x:" + ev.getX());
            if (downX == -1) {
                downX = ev.getX();
            }
        } else if (ev.getAction() == MotionEvent.ACTION_UP) {
            upX = ev.getX();
            Log.e(TAG, "MotionEvent.getAction: ACTION_DOWN, down_x:" +downX);
            Log.e(TAG, "MotionEvent.getAction: ACTION_UP, up_x:" + upX);
            if (!done) {
                float offsetX = downX - upX;
                Log.e(TAG, "ScrollView处理滑动，offsetX:" + offsetX);
                if (Math.abs(offsetX) >= flingMinDistance) {
                    if (offsetX > 0 && currentPage != flowLayout.getPageSize() - 1) {
                        setCurrentPage(currentPage + 1);
                    } else if (offsetX < 0 && currentPage != 0) {
                        setCurrentPage(currentPage - 1);
                    }
                } else {
                    setCurrentPage(currentPage);
                }
            }
            downX = -1;
            upX = -1;
            Log.e(TAG, "完成一次滑动");
        }
        return super.onTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            downX = ev.getX();
            Log.e(TAG, "onInterceptTouchEvent，downX:" + downX);
        }
        return super.onInterceptTouchEvent(ev);
    }

    public int getCount() {
        return flowLayout.getPageSize();
    }

    public int getTagCount() {
        return flowLayout.getChildCount();
    }

    public int getPageSize() {
        return flowLayout.getPageSize();
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
        float sx = getScrollX();
        float sy = getScrollY();
        final int  scrollX = currentPage * getWidth();
        // scrollTo直接滑到指定位置，smoothScrollTo平滑滑动（需要异步操作）
        post(new Runnable() {
            @Override
            public void run() {
                smoothScrollTo(scrollX, 0);
                if (indicator != null) {
                    indicator.setCurrentPage(PageScrollView.this.currentPage);
                }
            }
        });
        Log.e(TAG, "old scrollX:" + sx + ", old scrollY:" + sy + ", scrollToX:" + scrollX);
    }

}
