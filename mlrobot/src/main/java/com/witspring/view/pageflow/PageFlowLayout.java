package com.witspring.view.pageflow;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PageFlowLayout extends ViewGroup {

    private static final String TAG = "PageFlowLayout";
    private int pageWidth, pageHeight;// 建议每页的宽度、高度，根据实际情况调整
    private int realHeight;// 实际的高度
    private int pageSize;// 页数
    // 存储所有的分页view，按页记录，每页按行记录
    private Map<Integer, List<List<View>>> allPageViews = new HashMap<>(pageSize);
    // 存储所有的行高，按页记录，每页按行记录
    private Map<Integer, List<Integer>> allPageLines = new HashMap<>(pageSize);
    private int maxRows, maxPages;

    public PageFlowLayout(Context context) {
        super(context);
    }

    public PageFlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setPageWidth(int pageWidth) {
        this.pageWidth = pageWidth;
    }

    public void setPageHeight(int pageHeight) {
        this.pageHeight = pageHeight;
    }

    public void setMaxRowsAndPages(int maxRows, int maxPages) {
        this.maxRows = maxRows;
        this.maxPages = maxPages;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getRealHeight() {
        return realHeight;
    }

    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new MarginLayoutParams(p);
    }

    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    /**
     * 负责设置子控件的测量模式和大小 根据所有子控件设置自己的宽和高
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        int childCount = getChildCount();
        if (childCount == 0 || pageHeight == 0 || pageWidth == 0) {
            return;
        }
        allPageLines.clear();
        allPageViews.clear();
        // 获得它的父容器为它设置的测量模式和大小
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        Log.e(TAG, "MeasureSpec_Width: " + sizeWidth + ", MeasureSpec_Height: " + sizeHeight + ", Page_Height: " + pageHeight);

        // 记录实际的宽、高
        int width = 0;
        int height = 0;
        int maxPageHeight = 0;
        // 记录每一行的宽度，width不断取最大宽度
        int lineWidth = 0;
        // 每一行的高度，累加至height
        int lineHeight = 0;

        // 页View
        List<List<View>> lineViewsOfPage = new ArrayList<>();
        // 行View
        List<View> lineViews = new ArrayList<>();
        // 页行高
        List<Integer> lineHeightsOfPage = new ArrayList<>();
        // 初始化页数为第一页
        pageSize = 1;
        // 遍历每个子元素
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            // 测量每一个child的宽和高
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            // 得到child的lp
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
            // 当前子空间实际占据的宽度
            int childWidth = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
            // 当前子空间实际占据的高度
            int childHeight = child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
            if (childWidth > pageWidth || childHeight > pageHeight) {
                childWidth = childWidth > pageWidth ? pageWidth : childWidth;
                childHeight = childHeight > pageHeight ? pageHeight : childHeight;
                lp.width = childWidth;
                lp.height = childHeight;
                child.setLayoutParams(lp);
            }
            if (i == 0) {
                width = childWidth;
            }
            // 如果加入当前child，则超出最大宽度，则得到目前最大宽度给width，累加height然后开启新行
            if (lineWidth + childWidth > pageWidth) {
                width = Math.max(lineWidth, width);// 取最大的
                lineWidth = childWidth; // 重新开启新行，开始记录
                // 叠加当前高度，
                height += lineHeight;
                // 记录当前行高加入分页
                lineHeightsOfPage.add(lineHeight);
                // 记录当前行View加入分页
                lineViewsOfPage.add(lineViews);
                // 下一分页判断
                if (height + childHeight > pageHeight || (maxRows > 0 && lineViewsOfPage.size() == maxRows)) {
                    // 记录当前分页并开启新页行高
                    allPageLines.put(pageSize, lineHeightsOfPage);
                    lineHeightsOfPage = new ArrayList<>();
                    // 记录当前分页并开启新页View
                    allPageViews.put(pageSize, lineViewsOfPage);
                    lineViewsOfPage = new ArrayList<>();
                    // 如果实际高度大于设置的分页高度，就开始新的页面
                    pageSize++;
                    maxPageHeight = Math.max(height, maxPageHeight);
                    width = childWidth;
                    height = 0;
                }
                // 开启新行View
                lineViews = new ArrayList<>();
                // 开启记录下一行的高度
                lineHeight = childHeight;
            } else {
                // 否则累加值lineWidth,lineHeight取最大高度
                lineWidth += childWidth;
                lineHeight = Math.max(lineHeight, childHeight);
            }
            // 如果是最后一个，则将当前记录的最大宽度和当前lineWidth做比较
            if (i == childCount - 1) {
                width = Math.max(width, lineWidth);
                height += lineHeight;
                ViewGroup.LayoutParams layoutParams = getLayoutParams();
                if (pageSize == 1) {
                    layoutParams.height = height;
                    maxPageHeight = height;
                } else {
                    maxPageHeight = Math.max(height, maxPageHeight);
                    layoutParams.height = maxPageHeight;
                }
                setLayoutParams(layoutParams);
                // 最后一行
                lineHeightsOfPage.add(lineHeight);
                lineViewsOfPage.add(lineViews);
                allPageLines.put(pageSize, lineHeightsOfPage);
                allPageViews.put(pageSize, lineViewsOfPage);
            }
            // 加入行View
            lineViews.add(child);

        }
        realHeight = maxPageHeight;
        setMeasuredDimension(pageWidth * pageSize, realHeight);

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for (int i = 1; i <= pageSize; i++) {
            int pageLeft = (i - 1) * pageWidth;
            // 每页View
            List<List<View>> viewsOfPage = allPageViews.get(i);
            // 每页行高
            List<Integer> linesOfPage = allPageLines.get(i);
            // 每页行数
            int lineNumOfPage = linesOfPage.size();
            int top = 0;
            for (int j = 0; j < lineNumOfPage; j++) {
                int left = pageLeft;
                // 每行View
                List<View> viewsOfLine = viewsOfPage.get(j);
                int lineHeight = linesOfPage.get(j);
                Log.e(TAG, "第" + i + "页，第" + (j+1) + "行View数量：" + viewsOfLine.size());
                Log.e(TAG, "第" + i + "页，第" + (j+1) + "行行高：" + lineHeight);
                int viewNum = viewsOfLine.size();
                for (int k = 0; k < viewNum; k++) {
                    View child = viewsOfLine.get(k);
                    if (child.getVisibility() == GONE) {
                        continue;
                    }
                    MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
                    // 计算child的left,top,right,bottom
                    int lc = left + lp.leftMargin;
                    int tc = top + lp.topMargin;
                    int rc = lc + child.getMeasuredWidth();
                    int bc = tc + child.getMeasuredHeight();
                    Log.e(TAG, "第" + i + "页，第" + (j+1) + "行，第" + (k+1) + "个View："
                            + child + " , left = " + lc + " , top = " + tc + " , right =" + rc + " , bottom = " + bc);
                    child.layout(lc, tc, rc, bc);
                    left += child.getMeasuredWidth() + lp.rightMargin + lp.leftMargin;
                }
                top += lineHeight;
            }
        }
    }

    public static class LayoutParams extends MarginLayoutParams {

        public LayoutParams(int width, int height) {
            super(width, height);
        }

    }

}
