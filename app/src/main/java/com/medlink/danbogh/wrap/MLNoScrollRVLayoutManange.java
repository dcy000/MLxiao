package com.medlink.danbogh.wrap;

import github.hellocsl.layoutmanager.gallery.GalleryLayoutManager;

/**
 * Created by lenovo on 2019/2/15.
 * 身高选择的rv 禁止滑动
 */

public class MLNoScrollRVLayoutManange extends GalleryLayoutManager {
    public MLNoScrollRVLayoutManange(int orientation) {
        super(orientation);
    }

    public boolean canScrollVertically;
    public boolean canScrollHorizontally;

    public void setCanScrollVertically(boolean canScroll) {
        this.canScrollVertically = canScroll;
    }

    public void setCanScrollHorizontally(boolean canScrollHorizontally) {
        this.canScrollHorizontally = canScrollHorizontally;
    }

    @Override
    public boolean canScrollVertically() {
        return canScrollVertically && super.canScrollVertically();
    }

    @Override
    public boolean canScrollHorizontally() {
        return canScrollHorizontally && super.canScrollHorizontally();
    }
}
