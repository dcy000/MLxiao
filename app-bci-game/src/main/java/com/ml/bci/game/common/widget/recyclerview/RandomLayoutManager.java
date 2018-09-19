package com.ml.bci.game.common.widget.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

/**
 * Created by afirez on 2018/6/29.
 */

public class RandomLayoutManager extends RecyclerView.LayoutManager {
    private static final String TAG = "zzz";

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
            added.clear();
            detachAndScrapAttachedViews(recycler);
            return;
        }

        if (state.isPreLayout()) {
            //Nothing to do during prelayout when empty
            return;
        }

        removeAndRecycleAllViews(recycler);

        layoutChildren(recycler, state, 0);
    }

    private int mWidth;
    private int mHeight;
    private ArrayList<View> hidden = new ArrayList<>();
    private volatile boolean hasRemoved = false;
    private int mOffset = 0;

    private LinkedList<Integer> added = new LinkedList<>();

    private void layoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state, int dx) {

        if (dx > 0) {
            return;
        }
        mOffset += -dx;

        if (getChildCount() == 0 && getItemCount() > 0) {
            if (added.size() == 0) {
                hasRemoved = false;
            }
            View scrap = recycler.getViewForPosition(0);
            addView(scrap);
            measureChildWithMargins(scrap, 0, 0);
            mWidth = getDecoratedMeasuredWidth(scrap);
            mHeight = getDecoratedMeasuredHeight(scrap);
            removeAndRecycleView(scrap, recycler);
        }

        // find children out of range to remove
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            int left = mOffset - mWidth * (i + 1) - calcTotalLeftMargin(i);
            if (left + mWidth > getWidth()) {
                hasRemoved = true;
            }
            if (outOfRange(left)) {
                hidden.add(view);
            }
        }
        // remove children out of range
        for (View view : hidden) {
            mOffset -= mWidth + getLeftMargin(view);
            added.remove(Integer.valueOf(getAdapterPosition(view)));
            removeAndRecycleView(view, recycler);
        }
        hidden.clear();

        detachAndScrapAttachedViews(recycler);

        int unconsumed = mOffset;
//        int consumed = 0;

        int size = added.size();
        for (int i = 0; i < size && unconsumed > 0; i++) {
            View scrap = recycler.getViewForPosition(added.get(i));
            Log.d(TAG, "layoutChildren: " + getAdapterPosition(scrap));
            addView(scrap);
            measureChildWithMargins(scrap, 0, 0);
            int left = unconsumed - mWidth;
            int top = getDecoratedTop(scrap);
            int right = unconsumed;
            int bottom = top + mHeight;
            layoutDecorated(scrap, left, top, right, bottom);
//            consumed += mWidth + getLeftMargin(scrap);
            unconsumed -= mWidth + getLeftMargin(scrap);
        }

//        Log.i(TAG, String.format("%s, %s", added.size(), hasRemoved));
        if (!hasRemoved) {
            for (; unconsumed > 10; ) {
                int i = makeAvailableRandomPosition();
                if (i == -1) {
                    break;
                }
                View scrap = recycler.getViewForPosition(i);
                added.offer(i);
                if (getChildCount() == 0) {
                    mOffset = -dx;
                    unconsumed = mOffset;
                }
                addView(scrap);
                measureChildWithMargins(scrap, 0, 0);
                int leftMargin = makeRandomLeftMargin();
                setLeftMargin(scrap, leftMargin);
                int left = unconsumed - mWidth;
                int top = getDecoratedTop(scrap);
                int right = unconsumed;
                int bottom = top + mHeight;
                layoutDecorated(scrap, left, top, right, bottom);
//                consumed += mWidth + getLeftMargin(scrap);
                unconsumed -= mWidth + getLeftMargin(scrap);
            }
        }

        int mid = getWidth() / 2;
        int selectedPosition = -1;
        View selectedView = null;

        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (mid > getDecoratedLeft(child) - getLeftMargin(child) && mid < getDecoratedRight(child)) {
                selectedPosition = getAdapterPosition(child);
                selectedView = child;
                break;
            }
        }

        Log.i(TAG, "selectedPosition: " + selectedPosition);
        if (selectedPosition == -1) {
            onUnselect();
        } else {
            onSelect(selectedView, selectedPosition);
        }

    }

    private void setLeftMargin(View scrap, int leftMargin) {
        ((RecyclerView.LayoutParams) scrap.getLayoutParams()).leftMargin = leftMargin;
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
        if (added.contains(position)) {
            added.remove(Integer.valueOf(position));
            for (int i = 0; i < added.size(); i++) {
                Integer integer = added.get(i);
                if (integer > position) {
                    added.set(i, integer - 1);
                }
            }
        }
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
//        return mLeftMarginRandom.nextInt(maxInterval);
        return 0;
    }

    private int makeAvailableRandomPosition() {
        int itemCount = getItemCount();
        if (added.size() == itemCount) {
            return -1;
        }
        int position = mAdapterPositionRandom.nextInt(itemCount);
        while (added.contains(position)) {
            position = mAdapterPositionRandom.nextInt(itemCount);
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
