package com.gcml.module_detection.fragment;

import android.os.Bundle;
import android.view.View;

import com.gcml.module_blutooth_devices.base.BluetoothBaseFragment;
import com.gcml.module_detection.R;

public class IDCardReadFragment extends BluetoothBaseFragment {
    @Override
    protected int initLayout() {
        return R.layout.fragment_idcard_read;
    }

    @Override
    protected void initView(View view, Bundle bundle) {
        view.findViewById(R.id.container).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickPage != null) {
                    clickPage.onClick();
                }
            }
        });
    }

    ClickPage clickPage;

    public void setClickPageListener(ClickPage clickPage) {
        this.clickPage = clickPage;
    }

    public interface ClickPage {
        void onClick();
    }
}
