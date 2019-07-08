package com.gcml.module_detection.fragment;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.gcml.common.utils.ui.UiUtils;
import com.gcml.module_blutooth_devices.base.BluetoothBaseFragment;
import com.gcml.module_detection.R;

public class ImageFragment extends BluetoothBaseFragment {
    @Override
    protected int initLayout() {
        return R.layout.fragment_image;
    }

    @Override
    protected void initView(View view, Bundle bundle) {
        ImageView imageView = (ImageView) view.findViewById(R.id.image);

//        if (UiUtils.sDesignWidth != 1920) {
//            ViewGroup.MarginLayoutParams params = ((ViewGroup.MarginLayoutParams) imageView.getLayoutParams());
//            if (params != null) {
//                int size = UiUtils.pt((int) (UiUtils.sDesignWidth / 1920 * 560 + 0.5));
//                int marginTop = UiUtils.pt((int) (UiUtils.sDesignWidth / 1920 * 210 + 0.5));
//                params.width = size;
//                params.height = size;
//                params.topMargin = marginTop;
//                imageView.setLayoutParams(params);
//            }
//        }

        if (bundle != null) {
            int image = bundle.getInt("image");
            imageView.setImageResource(image);
        }
    }
}
