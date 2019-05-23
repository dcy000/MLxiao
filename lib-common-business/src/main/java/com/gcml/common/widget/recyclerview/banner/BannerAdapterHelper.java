package com.gcml.common.widget.recyclerview.banner;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

public class BannerAdapterHelper {
    private int pagePadding = 15;
    private int showLeftCardWidth = 15;

    public void onCreateViewHolder(ViewGroup parent, View itemView) {
        RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) itemView.getLayoutParams();
        lp.width = parent.getWidth() - 2 * (pagePadding + showLeftCardWidth);
        itemView.setLayoutParams(lp);
    }

    public void onBindViewHolder(View itemView, final int position, int itemCount) {
        int padding = pagePadding;
        itemView.setPadding(padding, 0, padding, 0);
        int leftMarin = position == 0 ? padding + showLeftCardWidth : 0;
        int rightMarin = position == itemCount - 1 ? padding + showLeftCardWidth : 0;
        setViewMargin(itemView, leftMarin, 0, rightMarin, 0);
    }

    private void setViewMargin(View view, int left, int top, int right, int bottom) {
        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        if (lp.leftMargin != left || lp.topMargin != top || lp.rightMargin != right || lp.bottomMargin != bottom) {
            lp.setMargins(left, top, right, bottom);
            view.setLayoutParams(lp);
        }
    }

    public void setPagePadding(int pagePadding) {
        this.pagePadding = pagePadding;
    }

    public int getPagePadding() {
        return pagePadding;
    }

    public void setShowLeftCardWidth(int showLeftCardWidth) {
        this.showLeftCardWidth = showLeftCardWidth;
    }

    public int getShowLeftCardWidth() {
        return showLeftCardWidth;
    }
}
