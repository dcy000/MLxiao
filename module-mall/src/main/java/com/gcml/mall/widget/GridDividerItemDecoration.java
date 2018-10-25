package com.gcml.mall.widget;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;


/**
 * Created by admin on 2017/9/4.
 */

public class GridDividerItemDecoration extends RecyclerView.ItemDecoration {
    private int leftRight;
    private int topBottom;

    private Drawable mDivider;
    public GridDividerItemDecoration(int leftRight, int topBottom, int mColor) {
        this.leftRight = leftRight;
        this.topBottom = topBottom;
        if (mColor != 0) {
            mDivider = new ColorDrawable(mColor);
        }
    }
    public GridDividerItemDecoration(int leftRight, int topBottom){
        this.leftRight = leftRight;
        this.topBottom = topBottom;
        mDivider=new ColorDrawable(Color.TRANSPARENT);
    }
    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        GridLayoutManager layoutManager = (GridLayoutManager) parent.getLayoutManager();
        if (mDivider == null || layoutManager.getChildCount() == 0) {
            return;
        }
        //判断总的数量是否可以整除
        int totalCount = layoutManager.getItemCount();
        int surplusCount = totalCount % layoutManager.getSpanCount();

        int left;
        int right;
        int top;
        int bottom;

        final int childCount = parent.getChildCount();
        if (layoutManager.getOrientation() == GridLayoutManager.VERTICAL) {

            for (int i = 0; i < childCount; i++) {
                final View child = parent.getChildAt(i);
                final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
                //得到它在总数里面的位置
                final int position = parent.getChildAdapterPosition(child);
                //将带有颜色的分割线处于中间位置
                final float centerLeft = (layoutManager.getLeftDecorationWidth(child) - leftRight) / 2;
                final float centerTop = (layoutManager.getTopDecorationHeight(child) - topBottom) / 2;
                //是否为最后一排
                boolean isLast = surplusCount == 0 ?
                        position > totalCount - layoutManager.getSpanCount() - 1 :
                        position > totalCount - surplusCount - 1;
                //画下边的,最后一排不需要画
                if ((position + 1) % layoutManager.getSpanCount() == 1 && !isLast) {
                    //计算下边的
                    left = layoutManager.getLeftDecorationWidth(child);
                    right = parent.getWidth() - layoutManager.getLeftDecorationWidth(child);
                    top = (int) (child.getBottom() + params.bottomMargin + centerTop);
                    bottom = top + topBottom;
                    mDivider.setBounds(left, top, right, bottom);
                    mDivider.draw(c);
                }
                //画右边的，能被整除的不需要右边,并且当数量不足的时候最后一项不需要右边
                boolean first = totalCount > layoutManager.getSpanCount() && (position + 1) % layoutManager.getSpanCount() != 0;
                boolean second = totalCount < layoutManager.getSpanCount() && position + 1 != totalCount;
                if (first || second) {
                    //计算右边的
                    left = (int) (child.getRight() + params.rightMargin + centerLeft);
                    right = left + leftRight;
                    top = child.getTop() + params.topMargin;
                    //第一排的不需要上面那一丢丢
                    if (position > layoutManager.getSpanCount() - 1) {
                        top -= centerTop;
                    }
                    bottom = child.getBottom() - params.bottomMargin;
                    //最后一排的不需要最底下那一丢丢
                    if (!isLast) {
                        bottom += centerTop;
                    }
                    mDivider.setBounds(left, top, right, bottom);
                    mDivider.draw(c);
                }
            }
        } else {

            for (int i = 0; i < childCount; i++) {
                final View child = parent.getChildAt(i);
                final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
                //得到它在总数里面的位置
                final int position = parent.getChildAdapterPosition(child);
                //将带有颜色的分割线处于中间位置
                final float centerLeft = (layoutManager.getLeftDecorationWidth(child) - leftRight) / 2;
                final float centerTop = (layoutManager.getTopDecorationHeight(child) - topBottom) / 2;
                //是否为最后一排
                boolean isLast = surplusCount == 0 ?
                        position > totalCount - layoutManager.getSpanCount() - 1 :
                        position > totalCount - surplusCount - 1;
                //画右边的，最后一排不需要画
                if ((position + 1) % layoutManager.getSpanCount() == 1 && !isLast) {
                    //计算右边的
                    left = (int) (child.getRight() + params.rightMargin + centerLeft);
                    right = left + leftRight;
                    top = layoutManager.getTopDecorationHeight(child);
                    bottom = parent.getHeight() - layoutManager.getTopDecorationHeight(child);
                    mDivider.setBounds(left, top, right, bottom);
                    mDivider.draw(c);
                }
                boolean first = totalCount > layoutManager.getSpanCount() && (position + 1) % layoutManager.getSpanCount() != 0;
                boolean second = totalCount < layoutManager.getSpanCount() && position + 1 != totalCount;
                //画下边的，能被整除的不需要下边
                if (first || second) {
                    left = child.getLeft() + params.leftMargin;
                    if (position > layoutManager.getSpanCount() - 1) {
                        left -= centerLeft;
                    }
                    right = child.getRight() - params.rightMargin;
                    if (!isLast) {
                        right += centerLeft;
                    }
                    top = (int) (child.getBottom() + params.bottomMargin + centerTop);
                    bottom = top + topBottom;
                    mDivider.setBounds(left, top, right, bottom);
                    mDivider.draw(c);
                }
            }
        }

    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        GridLayoutManager layoutManager = (GridLayoutManager) parent.getLayoutManager();
        //判断总的数量是否可以整除
        int totalCount = layoutManager.getItemCount();
        int surplusCount = totalCount % layoutManager.getSpanCount();
        int childPosition = parent.getChildAdapterPosition(view);
        if (layoutManager.getOrientation() == GridLayoutManager.VERTICAL) {//竖直方向的
            if (surplusCount == 0 && childPosition > totalCount - layoutManager.getSpanCount() - 1) {
                //后面几项需要bottom
                outRect.bottom = topBottom;
            } else if (surplusCount != 0 && childPosition > totalCount - surplusCount - 1) {
                outRect.bottom = topBottom;
            }
            if ((childPosition + 1) % layoutManager.getSpanCount() == 0) {//被整除的需要右边
                outRect.right = leftRight;
            }
            outRect.top = topBottom;
            outRect.left = leftRight;
        } else {
            if (surplusCount == 0 && childPosition > totalCount - layoutManager.getSpanCount() - 1) {
                //后面几项需要右边
                outRect.right = leftRight;
            } else if (surplusCount != 0 && childPosition > totalCount - surplusCount - 1) {
                outRect.right = leftRight;
            }
            if ((childPosition + 1) % layoutManager.getSpanCount() == 0) {//被整除的需要下边
                outRect.bottom = topBottom;
            }
            outRect.top = topBottom;
            outRect.left = leftRight;
        }

    }
}