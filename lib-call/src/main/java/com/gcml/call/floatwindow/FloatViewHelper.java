package com.gcml.call.floatwindow;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by afirez on 2018/6/1.
 */

public class FloatViewHelper implements FloatWindowHelper.OnDismissListener, FloatWindowHelper.OnShowListener {
    private FloatWindowHelper mFloatWindowHelper;
    private FrameLayout mContentParent;
    private View mContentView;

    public FloatViewHelper(Context context) {
        context = context.getApplicationContext();
        mFloatWindowHelper = new FloatWindowHelper(context);
        mFloatWindowHelper.setOnShowListener(this);
        mFloatWindowHelper.setOnDismissListener(this);
        mFloatWindowHelper.setContentView(layoutId());
        mContentParent = mFloatWindowHelper.getContentParent();
        mContentView = mFloatWindowHelper.getContentView();
    }

    @LayoutRes
    public int layoutId() {
        return 0;
    }

    public <V extends View> V findViewById(@IdRes int id) {
        if (mContentParent == null) {
            return null;
        }
        return (V) mContentParent.findViewById(id);
    }

    public void show() {
        mFloatWindowHelper.show();
    }

    public void dismiss() {
        mFloatWindowHelper.dismiss();
    }

    private FloatWindowHelper.OnShowListener mOnShowListener;
    private FloatWindowHelper.OnDismissListener mOnDismissListener;

    public void setOnShowListener(FloatWindowHelper.OnShowListener onShowListener) {
        mOnShowListener = onShowListener;
    }

    public void setOnDismissListener(FloatWindowHelper.OnDismissListener onDismissListener) {
        mOnDismissListener = onDismissListener;
    }

    @Override
    public void onDismiss() {
        if (mOnDismissListener != null) {
            mOnDismissListener.onDismiss();
        }
    }

    @Override
    public void onShow() {
        if (mOnShowListener != null) {
            mOnShowListener.onShow();
        }
    }
}
