package com.example.han.referralproject.yiyuan.fragment;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.han.referralproject.R;

/**
 * Created by lenovo on 2018/6/15.
 */

public class BloodPressureNoticeDialog extends DialogFragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(android.support.v4.app.DialogFragment.STYLE_NO_TITLE, R.style.XDialog);
    }

}
