package com.gcml.common.stateview;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.view.ViewStub;

/**
 * Created by lenovo on 2019/2/22.
 */

public abstract class AbsViewStubLayout {
    protected ViewStub mLayoutVs;
    protected View mContentView;

    protected void initLayout(Context context, @LayoutRes int layoutResId) {
        mLayoutVs = new ViewStub(context);
        mLayoutVs.setLayoutResource(layoutResId);
    }

    protected ViewStub getLayoutVs() {
        return mLayoutVs;
    }

    protected void setView(View contentView) {
        mContentView = contentView;
    }

    protected abstract void setData(Object... objects);
}
