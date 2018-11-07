package com.gcml.call.floatwindow;

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
    private ImageView mIvLargeCover;
    private Button mBtnFullScreen;
    private Button mBtnClose;

    public CallFloatViewHelper(Context context) {
        super(context);
        initView();
    }

    @Override
    public int layoutId() {
        return R.layout.call_float_window;
    }

    private void initView() {
        mClContainer = (ConstraintLayout) findViewById(R.id.cl_float_container);
        mBtnFullScreen = (Button) findViewById(R.id.btn_full_screen);
        mBtnClose = (Button) findViewById(R.id.btn_close);
        mFlLargeContainer = (FrameLayout) findViewById(R.id.fl_large_container);
        mFlSmallContainer = (FrameLayout) findViewById(R.id.fl_small_container);
        mIvSmallCover = (ImageView) findViewById(R.id.iv_small_cover);
        mIvLargeCover = (ImageView) findViewById(R.id.iv_large_cover);
        mBtnFullScreen.setOnClickListener(this);
        mBtnClose.setOnClickListener(this);
    }

    public void setFullScreenOnClickListener(View.OnClickListener fullScreenOnClickListener) {
        mFullScreenOnClickListener = fullScreenOnClickListener;
    }

    public void setCloseOnClickListener(View.OnClickListener closeOnClickListener) {
        mCloseOnClickListener = closeOnClickListener;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_full_screen) {
            if (mFullScreenOnClickListener != null) {
                mFullScreenOnClickListener.onClick(v);
            }
        } else if (id == R.id.btn_close) {
            if (mCloseOnClickListener != null) {
                mCloseOnClickListener.onClick(v);
            }
        }
    }

    @Override
    public void onCallStateChanged(CallState state) {
        switch (state) {
            case VIDEO:
                mIvLargeCover.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void onCallTime(int seconds) {

    }

    @Override
    public void onCloseSession() {
        dismiss();
    }

    @Override
    public void show() {
        CallHelper.INSTANCE.setSmallContainer(mFlSmallContainer);
        CallHelper.INSTANCE.setLargeContainer(mFlLargeContainer);
        CallHelper.INSTANCE.addOnCallStateChangeListener(this);
        CallHelper.INSTANCE.setCallTimeCallback(this);
        CallHelper.INSTANCE.setOnCloseSessionListener(this);
        super.show();
    }

    @Override
    public void dismiss() {
        setFullScreenOnClickListener(null);
        setCloseOnClickListener(null);
        CallHelper.INSTANCE.setSmallContainer(null);
        CallHelper.INSTANCE.setLargeContainer(null);
        CallHelper.INSTANCE.removeOnCallStateChangeListener(this);
        CallHelper.INSTANCE.setCallTimeCallback(null);
        CallHelper.INSTANCE.setOnCloseSessionListener(null);
        super.dismiss();
    }
}
