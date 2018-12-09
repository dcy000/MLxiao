package com.example.module_child_edu;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by lenovo on 2018/3/30.
 */

public class AutoLoadMoreHelper extends RecyclerView.OnScrollListener {

    private RecyclerView mRecyclerView;

    public void attachToRecyclerView(RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
        recyclerView.addOnScrollListener(this);
    }

    private int dy;

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        this.dy = dy;
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager != null && layoutManager instanceof LinearLayoutManager) {
            position = ((LinearLayoutManager) layoutManager).findLastCompletelyVisibleItemPosition();
        }
    }

    private int position;

    private boolean isLoading;

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        if (newState == RecyclerView.SCROLL_STATE_IDLE
                && dy < 0
                && adapter != null
                && adapter.getItemCount() != 0
                && position + 3 >= adapter.getItemCount()) {
            onAutoLoadMore(this);
        }
    }

    private void onAutoLoadMore(AutoLoadMoreHelper autoLoadMoreHelper) {
        if (mOnAutoLoadMoreListener != null) {
            mOnAutoLoadMoreListener.onAutoLoadMore(autoLoadMoreHelper);
        }
    }

    private OnAutoLoadMoreListener mOnAutoLoadMoreListener;

    public void setOnAutoLoadMoreListener(OnAutoLoadMoreListener onAutoLoadMoreListener) {
        mOnAutoLoadMoreListener = onAutoLoadMoreListener;
    }

    public interface OnAutoLoadMoreListener {
        void onAutoLoadMore(AutoLoadMoreHelper autoLoadMoreHelper);
    }
}
