package com.ml.bci.game.common.widget.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by afirez on 2018/6/27.
 */

public class MyLayoutManager extends RecyclerView.LayoutManager {

    private static final String TAG = "MyLayoutManager";

    private int mLeftOffset;
    private int mTopOffset;

    public MyLayoutManager() {

    }

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        Log.d(TAG, "onLayoutChildren: ");

        //We have nothing to show for an empty data set but clear any existing views
        if (getItemCount() == 0) {
            detachAndScrapAttachedViews(recycler);
            return;
        }

        if (state.isPreLayout()) {
            //Nothing to do during prelayout when empty
            return;
        }

        detachAndScrapAttachedViews(recycler);

        fillVisibleChildren(recycler, state);
    }

    private int mOffsetX = 0;

    private void fillVisibleChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {

        if (mOffsetX >= 0) {
            return;
        }

        int itemCount = getItemCount();

        for (int i = 0; i < itemCount; i++) {
            int childCount = getChildCount();
            int width;
            int height;

            View child;
            if (childCount == 0) {
                View view = recycler.getViewForPosition(0);
                addView(view);
                measureChildWithMargins(view, 0, 0);
                width = getDecoratedMeasuredWidth(view);
                height = getDecoratedMeasuredHeight(view);
                mLeftOffset = getDecoratedLeft(view) - mOffsetX;
                mTopOffset = getDecoratedTop(view);
                child = view;
            } else {
                View view = getChildAt(0);
                width = getDecoratedMeasuredWidth(view);
                height = getDecoratedMeasuredHeight(view);
                mLeftOffset = getDecoratedLeft(view) - mOffsetX;
                mTopOffset = getDecoratedTop(view);
                child = getChildAt(i);
            }
            Log.d(TAG, "fillVisibleChildren: width" + width);
            int left = mLeftOffset - width * i;
            if (child != null && left >= getWidth()) {
                if (getWidth() > (getChildCount() + 1) * width) {
                    left = left % getWidth() - width;
                    if (left + width < 0) {
                        removeAndRecycleView(child, recycler);
                        continue;
                    }
                }
            }
            int top = mTopOffset;
            int right = left + width;
            int bottom = top + height;

            if (width < (getWidth() - width * getChildCount())) {
                if (child == null) {
                    child = recycler.getViewForPosition(i);
                    addView(child);
                    measureChildWithMargins(child, 0, 0);
                }
                Log.d(TAG, "fillVisibleChildren:" + getPosition(child) + " left = " + left + " top=" + top + " right=" + right + " bottom=" + bottom);
                layoutDecorated(child, left, top, right, bottom);
            }
        }
    }

    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        mOffsetX = dx;
        Log.d(TAG, "scrollHorizontallyBy: offsetX = " + mOffsetX);
        fillVisibleChildren(recycler, state);
        return dx;
    }

    @Override
    public boolean canScrollHorizontally() {
        return true;
    }
}
