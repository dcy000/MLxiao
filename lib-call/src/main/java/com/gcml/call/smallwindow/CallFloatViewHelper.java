package com.gcml.call.smallwindow;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.gcml.call.CallHelper;
import com.gcml.call.CallState;
import com.gcml.call.R;

/**
 * Created by afirez on 2018/6/1.
 */

public class CallFloatViewHelper extends FloatViewHelper
        implements View.OnClickListener,
        CallHelper.OnCallStateChangeListener,
        CallHelper.CallTimeCallback,
        CallHelper.OnCloseSessionListener {

    private ConstraintLayout mClContainer;
    private View.OnClickListener mFullScreenOnClickListener;
    private View.OnClickListener mCloseOnClickListener;
    private FrameLayout mFlLargeContainer;
    private FrameLayout mFlSmallContainer;
    private ImageView mIvSmallCover;
    private Button mBtnFullScreen;
    private Button mBtnClose;

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
        mBtnFullScreen = (Button) findViewById(R.id.call_btn_full_screen);
//        mBtnClose = (Button) findViewById(R.id.call_btn_close);
        mBtnFullScreen.setOnClickListener(this);
//        mBtnClose.setOnClickListener(this);
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

    public void setFullScreenOnClickListener(View.OnClickListener onClickListener) {
        mFullScreenOnClickListener = onClickListener;
    }

    public void setCloseOnClickListener(View.OnClickListener closeOnClickListener) {
        mCloseOnClickListener = closeOnClickListener;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.call_btn_full_screen) {
            if (mFullScreenOnClickListener != null) {
                mFullScreenOnClickListener.onClick(v);
            }
        } else if (id == R.id.call_btn_close) {
            if (mCloseOnClickListener != null) {
                mCloseOnClickListener.onClick(v);
            }
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
