package com.ml.bci.game.common.widget.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;

import java.util.Random;

/**
 * Created by lenovo on 2018/6/29.
 */

public class RandomLayoutManager extends RecyclerView.LayoutManager {
    private static final String TAG = "RandomLayoutManager";
    private int mWidth;
    private int mHeight;

    private RecyclerView mRecyclerView;
    private int mUnused;

    public RandomLayoutManager(RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
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
            mUnused = 0;
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

    private SparseBooleanArray mVisibles = new SparseBooleanArray();

    private int mLeftOffset0;
    private int mTopOffset0;
    private int mLeftOffset1;
    private int mTopOffset1;
    private int mLeftOffset;
    private int mTopOffset;


    private void fillVisibleChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (mOffsetX >= 0) {
            return;
        }

        int itemCount = getItemCount();

        for (int i = 0; i < itemCount; i++) {
            View child;
            if (getChildCount() == 0) {
                View view = recycler.getViewForPosition(0);
                addView(view);
                measureChildWithMargins(view, 0, 0);
                mWidth = getDecoratedMeasuredWidth(view);
                mHeight = getDecoratedMeasuredHeight(view);

                mTopOffset0 = getDecoratedTop(view);
                removeAndRecycleView(view, recycler);
            } else {
                for (int j = 0; j < getChildCount(); j++) {
                    View view = getChildAt(j);
                    int theLeft = getDecoratedLeft(view) - ((RecyclerView.LayoutParams) view.getLayoutParams()).leftMargin - mOffsetX;
                    if (theLeft > getWidth()) {
                        mUnused = getWidth() - mWidth * getChildCount() - calcTotalLeftMargin() - mOffsetX;
                        removeAndRecycleView(view, recycler);
                    }
                }
                View view = getChildAt(0);
                mWidth = getDecoratedMeasuredWidth(view);
                mHeight = getDecoratedMeasuredHeight(view);
                mLeftOffset0 = getDecoratedLeft(view) - mOffsetX;
                mTopOffset0 = getDecoratedTop(view);
            }
            mLeftOffset0 = mLeftOffset0 - mOffsetX - mWidth;
            child = getChildAt(i);
            int left = mLeftOffset0 - mWidth * i;

            int leftMargin;
            for (int j = 0; j < i; j++) {
                leftMargin = ((RecyclerView.LayoutParams) getChildAt(j).getLayoutParams()).leftMargin;
                left -= leftMargin;
            }

            if (mLeftOffset0 + mWidth >= getWidth()) {
                left -= mUnused;
            }
            int top = mTopOffset0;
            int right = left + mWidth;
            int bottom = top + mHeight;

            if (left + mWidth < 0) {
                break;
            }

            if (mWidth < (getWidth() - mWidth * getChildCount() - calcTotalLeftMargin())) {
                int adapterPosition = makeAvailableRandomPosition();
                if (child == null) {
                    child = recycler.getViewForPosition(adapterPosition);
                    RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();
                    layoutParams.leftMargin = makeRandomLeftMargin();
                    addView(child);
                    measureChildWithMargins(child, 0, 0);
                }
                Log.d(TAG, "fillVisibleChildren:" + getPosition(child)
                        + " \nleft = " + left
                        + " \ntop=" + top
                        + " \nright=" + right
                        + " \nbottom=" + bottom
                        + " \nleftMargin= " + ((RecyclerView.LayoutParams) child.getLayoutParams()).leftMargin
                        + " \nunused= " + mUnused
                        + " \nadapterPosition=" + adapterPosition);
                layoutDecorated(child, left, top, right, bottom);
            }
        }
    }

    private int calcTotalLeftMargin() {
        int totalLeftMargin = 0;
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = getChildAt(i);
            totalLeftMargin += ((RecyclerView.LayoutParams) view.getLayoutParams()).leftMargin;
        }
        return totalLeftMargin;
    }

    private Random mRandom = new Random();

    private int maxInteval = 50;

    private int makeRandomLeftMargin() {
        return mRandom.nextInt(maxInteval);
    }

    private int makeAvailableRandomPosition() {
        int position = mRandom.nextInt(getItemCount());
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = getChildAt(i);
            RecyclerView.ViewHolder viewHolder = mRecyclerView.getChildViewHolder(view);
            if (viewHolder.getAdapterPosition() == position) {
                position = mRandom.nextInt(getItemCount());
                i = 0;
            }
        }
        return position;
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
