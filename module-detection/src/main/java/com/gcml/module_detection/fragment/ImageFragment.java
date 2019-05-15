package com.gcml.module_detection.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.gcml.module_blutooth_devices.base.BluetoothBaseFragment;
import com.gcml.module_detection.R;

public class ImageFragment extends BluetoothBaseFragment {
    @Override
    protected int initLayout() {
        return R.layout.fragment_image;
    }

    @Override
    protected void initView(View view, Bundle bundle) {
        if (bundle != null) {
            int image = bundle.getInt("image");
            ((ImageView) view.findViewById(R.id.image)).setImageResource(image);
        }
    }
}
