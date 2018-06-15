package com.example.han.referralproject.yiyuan.fragment;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.han.referralproject.R;

/**
 * Created by lenovo on 2018/6/15.
 */

public class BloodPressureNoticeDialog extends DialogFragment implements View.OnClickListener {
//    private Ontouch ontouch;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(android.support.v4.app.DialogFragment.STYLE_NO_TITLE, R.style.XDialog);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_blood_pressure, container, false);
        view.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        dismiss();
    }

//    public interface Ontouch {
//        void OnTouch();
//        void OnTime();
//    }
//
//    public void setOntouch(BloodPressureNoticeDialog.Ontouch ontouch) {
//        this.ontouch = ontouch;
//    }
}
