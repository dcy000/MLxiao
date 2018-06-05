package com.ml.call.smallwindow;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.ml.call.CallHelper;
import com.ml.call.CallState;
import com.ml.call.R;

/**
 * Created by afirez on 2018/6/1.
 */

public class CallFloatViewHelper extends FloatViewHelper
        implements View.OnClickListener,
        CallHelper.OnCallStateChangeListener,
        CallHelper.CallTimeCallback,
        CallHelper.OnCloseSessionListener {

    private ConstraintLayout mClContainer;
    private View.OnClickListener mOnClickListener;
    private FrameLayout mFlLargeContainer;
    private FrameLayout mFlSmallContainer;
    private ImageView mIvSmallCover;

    public CallFloatViewHelper(Context context, OnSurfaceContainerPreparedListener onSurfaceContainerPreparedListener) {
        super(context);
        mOnSurfaceContainerPreparedListener = onSurfaceContainerPreparedListener;
        initView();
    }


    @Override
    public int layoutId() {
        return R.layout.call_window_call;
    }

    private void initView() {
        mClContainer = (ConstraintLayout) findViewById(R.id.call_cl_small_container);
        mClContainer.setOnClickListener(this);
        mFlLargeContainer = (FrameLayout) findViewById(R.id.call_fl_call_large_container);
        mFlSmallContainer = (FrameLayout) findViewById(R.id.call_fl_call_small_container);
        mIvSmallCover = (ImageView) findViewById(R.id.call_iv_call_small_cover);
        if (mOnSurfaceContainerPreparedListener != null) {
            mOnSurfaceContainerPreparedListener.onSurfaceContainerPrepared(mFlSmallContainer, mFlLargeContainer);
        }
    }

    public interface OnSurfaceContainerPreparedListener {
        void onSurfaceContainerPrepared(FrameLayout smallContainer, FrameLayout largeContainer);
    }

    private OnSurfaceContainerPreparedListener mOnSurfaceContainerPreparedListener;

    public OnSurfaceContainerPreparedListener getOnSurfaceContainerPreparedListener() {
        return mOnSurfaceContainerPreparedListener;
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }

    @Override
    public void onClick(View v) {
        if (mOnClickListener != null) {
            mOnClickListener.onClick(v);
        }
    }

    @Override
    public void onCallStateChanged(CallState state) {

    }

    @Override
    public void onCallTime(int seconds) {

    }

    @Override
    public void onCloseSession() {
        dismiss();
    }
}
