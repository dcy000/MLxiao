package com.ml.bci.game.common.widget.recyclerview;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by afirez on 2018/6/27.
 */

public class MyLayoutManager extends RecyclerView.LayoutManager {

    private static final String TAG = "MyLayoutManager";

    private int mScrollX;
    private int mFirstNewVisiblePosition;
    private int mLastVisiblePosition;
    private int mLeftOffset;
    private int mTopOffset;

    private SparseArray<Rect> mItemRects = new SparseArray<>();
    private int mChildWidth;

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
            View view = getChildAt(i);
            if (view == null) {
                view = recycler.getViewForPosition(i);
                addView(view);
                measureChildWithMargins(view, 0, 0);
            }
            int width = getDecoratedMeasuredWidth(view);
            int height = getDecoratedMeasuredHeight(view);



            int left = mLeftOffset - width * (i - mFirstNewVisiblePosition);
            if (left >= getWidth()) {
                if (getWidth() > (getChildCount() + 1) * width) {
                    left = left % getWidth() - width;
                    if (left + width < 0) {
                        removeAndRecycleView(view, recycler);
                        continue;
                    }
                }
            }
            int top = mTopOffset;
            int right = left + width;
            int bottom = top + height;

            if (width < (getWidth() - width * getChildCount())) {
                layoutDecorated(view, left, top, right, bottom);
            }
        }
    }

    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        mOffsetX += dx;
        if ((-mOffsetX) > 2 * getWidth()) {
            mOffsetX = mOffsetX % (2 * getWidth());
        }
        Log.d(TAG, "scrollHorizontallyBy: offsetX = " + mOffsetX);
        fillVisibleChildren(recycler, state);
        return dx;
    }

//    private void fill(RecyclerView.Recycler recycler, RecyclerView.State state) {
//        fill(recycler, state, 0);
//    }
//
//    private int fill(RecyclerView.Recycler recycler, RecyclerView.State state, int dx) {
//        int offsetLeft = getPaddingLeft();
//
//        Log.d(TAG, "fill: dx = " + dx);
//
//        //回收越界子View
//        int childCount = getChildCount();
//        if (childCount > 0) {//滑动时进来的
//            for (int i = childCount - 1; i >= 0; i--) {
//                View view = getChildAt(i);
//                if (dx > 0) { //需要回收当前屏幕，左越界的View
//                    if (getDecoratedRight(view) - dx < offsetLeft) {
//                        removeAndRecycleView(view, recycler);
//                        mFirstNewVisiblePosition++;
//                        continue;
//                    }
//                } else if (dx < 0) {//回收当前屏幕，右越界的View
//                    if (getDecoratedLeft(view) - dx > getHeight() - getPaddingRight()) {
//                        removeAndRecycleView(view, recycler);
//                        mLastVisiblePosition--;
//                        continue;
//                    }
//                }
//            }
//        }
//
//        //布局子View阶段
//        if (dx >= 0) {
//            int minPos = mFirstNewVisiblePosition;
//            mLastVisiblePosition = getItemCount() - 1;
//            if (getChildCount() > 0) {
//                View lastView = getChildAt(getChildCount() - 1);
//                minPos = getPosition(lastView) + 1;
//                mLeftOffset = getDecoratedLeft(lastView);
//                mTopOffset = getDecoratedTop(lastView);
//            }
//
//            for (int i = minPos; i <= mLastVisiblePosition; i++) {
//                View view = recycler.getViewForPosition(i);
//                addView(view);
//                measureChildWithMargins(view, 0, 0);
//                int width = getDecoratedMeasuredWidth(view);
//                int height = getDecoratedMeasuredHeight(view);
//                int left = mLeftOffset + width * (i - minPos);
//                int top = mTopOffset;
//                int right = left + width;
//                int bottom = top + height;
//                layoutDecorated(view, left, top, right, bottom);
//                mItemRects.put(i, new Rect(left, top, right, bottom));
//            }
//
//            View lastChild = getChildAt(getChildCount() - 1);
//            if (getPosition(lastChild) == getItemCount() - 1) {
//                int gap = getWidth() - getPaddingRight() - getDecoratedRight(lastChild);
//                if (gap > 0) {
//                    Log.d(TAG, "fill: gap = " + gap);
//                    dx -= gap;
//                }
//            }
//
//        } else {
//            int maxPos = getItemCount() - 1;
//            mFirstNewVisiblePosition = 0;
//            if (getChildCount() > 0) {
//                View firstView = getChildAt(0);
//                maxPos = getPosition(firstView) - 1;
//            }
//
//            for (int i = maxPos; i >= mFirstNewVisiblePosition; i--) {
//                Rect rect = mItemRects.get(i);
//
//                if (rect.right - mScrollX - dx < getPaddingLeft()) {
//                    mFirstNewVisiblePosition = i + 1;
//                    break;
//                } else {
//                    View view = recycler.getViewForPosition(i);
//                    addView(view);
//                    measureChildWithMargins(view, 0, 0);
//                    layoutDecoratedWithMargins(view, rect.left - mScrollX, rect.top, rect.right - mScrollX, rect.bottom);
//                }
//            }
//        }
//
//        return dx;
//    }

    @Override
    public boolean canScrollHorizontally() {
        return true;
    }


//    @Override
//    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
//        //位移0、没有子View 当然不移动
//        if (dx >= 0 || getChildCount() == 0) {
//            return 0;
//        }
//
//        int realOffset = dx;//实际滑动的距离， 可能会在边界处被修复
//        //边界修复代码
//        if (mScrollX + realOffset < 0) {//左边界
//            realOffset = -mScrollX;
//        } else if (realOffset > 0) {//右边界
//            //利用最后一个子View比较修正
//            View lastChild = getChildAt(getChildCount() - 1);
//            if (getPosition(lastChild) == getItemCount() - 1) {
//                int gap = getWidth() - getPaddingRight() - getDecoratedRight(lastChild);
//                if (gap > 0) {
//                    realOffset = -gap;
//                } else if (gap == 0) {
//                    realOffset = 0;
//                } else {
//                    realOffset = Math.min(realOffset, -gap);
//                }
//            }
//        }
//
//        realOffset = fill(recycler, state, realOffset);//先填充，再位移。
//
//        mScrollX += realOffset;//累加实际滑动距离
//
//        offsetChildrenHorizontal(-realOffset);//滑动
//
//        return realOffset;
//    }
}
