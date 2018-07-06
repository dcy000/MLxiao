package com.ml.bci.game.common.widget.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by afirez on 2018/6/29.
 */

public class RandomLayoutManager extends RecyclerView.LayoutManager {
    private static final String TAG = "RandomLayoutManager";

    public RandomLayoutManager() {

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

        layoutChildren(recycler, state, 0);
    }

    private int mWidth;
    private int mHeight;
    private ArrayList<View> hidden = new ArrayList<>();
    private boolean hasRemoved = false;
    private int mOffset = 0;

    private void layoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state, int dx) {

        if (dx >= 0) {
            return;
        }
        mOffset += -dx;

        if (getChildCount() == 0) {
            hasRemoved = false;
            View scrap = recycler.getViewForPosition(0);
            addView(scrap);
            measureChildWithMargins(scrap, 0, 0);
            mWidth = getDecoratedMeasuredWidth(scrap);
            mHeight = getDecoratedMeasuredHeight(scrap);
            detachAndScrapView(scrap, recycler);
        }

        // find children out of range to remove
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            int left = mOffset - mWidth * (i + 1) - calcTotalLeftMargin(i);
            if (outOfRange(left)) {
                hasRemoved = true;
                hidden.add(view);
            }
        }
        // remove children out of range
        for (View view : hidden) {
            mOffset -= mWidth + getLeftMargin(view);
            removeAndRecycleView(view, recycler);
        }
        hidden.clear();

        // layout children
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = getChildAt(i);
            int left = mOffset - mWidth * (i + 1) - calcTotalLeftMargin(i);
            Log.d(TAG, "layoutChildren: LayoutPosition=" + getPosition(view) + " AdapterPosition=" + getAdapterPosition(view));
            layoutDecorated(view, left, getDecoratedTop(view), left + mWidth, getDecoratedBottom(view));
        }

        if (!hasRemoved) {
            for (int i = childCount; i < getItemCount(); i++) {
                View scrap;
                int randomPosition = makeAvailableRandomPosition();
                Log.d(TAG, "layoutChildren: randomPosition" + randomPosition);
                scrap = recycler.getViewForPosition(randomPosition);
                ((RecyclerView.LayoutParams) scrap.getLayoutParams()).leftMargin = makeRandomLeftMargin();
                int left;
                if (i == 0) {
                    mOffset = -dx;
                    left = mOffset - mWidth;
                } else {
                    left = mOffset - mWidth * (i + 1) - calcTotalLeftMargin(i);
                }
                if (outOfRange(left)) {
                    break;
                }
                measureChildWithMargins(scrap, 0, 0);
                addView(scrap);
                int top = getDecoratedTop(scrap);
                int right = left + mWidth;
                int bottom = top + mHeight;
                layoutDecorated(scrap, left, top, right, bottom);
            }
        }

        int mid = getWidth() / 2;
        for (int i = 0; i < getChildCount(); i++) {
            int left = mOffset - mWidth * i - calcTotalLeftMargin(i);
            if (mid > left && mid < left + mWidth) {
                View view = getChildAt(i);
                int position = getAdapterPosition(view);
                if (position != this.position) {
                    onSelect(view, position);
                }
            } else {
                onUnselect();
            }
        }
    }

    private void onUnselect() {
        mSelectedView = null;
        this.position = -1;
        if (mOnSelectionListener != null) {
            mOnSelectionListener.onUnselect();
        }
    }

    private int position = -1;

    private View mSelectedView;

    private void onSelect(View view, int position) {
        mSelectedView = view;
        this.position = position;

        if (mOnSelectionListener != null) {
            mOnSelectionListener.onSelect(view, position);
        }
    }

    public int getSelectedPosition() {
        return position;
    }

    public View getSelectedView() {
        return mSelectedView;
    }

    public interface OnSelectionListener {
        void onSelect(View view, int position);

        void onUnselect();
    }

    public void setOnSelectionListener(OnSelectionListener onSelectionListener) {
        mOnSelectionListener = onSelectionListener;
    }

    private OnSelectionListener mOnSelectionListener;

    public View remove(int position) {
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            int adapterPosition = getAdapterPosition(view);
            if (adapterPosition == position) {
                return view;
            }
        }
        return null;
    }

    private int getLeftMargin(View view) {
        return ((RecyclerView.LayoutParams) view.getLayoutParams()).leftMargin;
    }

    private int getHorizontalSpace() {
        return getWidth() - getPaddingRight() - getPaddingLeft();
    }

    private int getAdapterPosition(View view) {
        return ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewAdapterPosition();
    }

    private boolean outOfRange(int left) {
        return left + mWidth <= 0 || left >= getWidth();
    }

    private int calcTotalLeftMargin(int count) {
        int totalLeftMargin = 0;
        for (int i = 0; i < count; i++) {
            View view = getChildAt(i);
            totalLeftMargin += ((RecyclerView.LayoutParams) view.getLayoutParams()).leftMargin;
        }
        return totalLeftMargin;
    }

    private Random mLeftMarginRandom = new Random();
    private Random mAdapterPositionRandom = new Random();

    private int maxInterval = 50;

    private int makeRandomLeftMargin() {
        return mLeftMarginRandom.nextInt(maxInterval);
    }

    private int makeAvailableRandomPosition() {
        int position = mAdapterPositionRandom.nextInt(getItemCount());
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            int adapterPosition = getAdapterPosition(getChildAt(i));
            if (adapterPosition == position) {
                position = mAdapterPositionRandom.nextInt(getItemCount());
                i = 0;
            }
        }
        return position;
    }

    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        layoutChildren(recycler, state, dx);
        return dx;
    }

    @Override
    public boolean canScrollHorizontally() {
        return true;
    }
}
