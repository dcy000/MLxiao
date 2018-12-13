package com.gcml.module_factory_test.ui;

import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.SurfaceView;
import android.view.View;

import com.gcml.module_factory_test.R;
import com.gcml.module_factory_test.utils.CameraUtils;

public class CameraActivity extends ToolbarBaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.factory_activity_camera_test);

        SurfaceView surfaceView = findViewById(R.id.surface);

        CameraUtils.getInstance().init(this,surfaceView,1920,1200,856,856);
        CameraUtils.getInstance().openCamera(Camera.CameraInfo.CAMERA_FACING_BACK);

        initTitle();
    }

    private void initTitle() {
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("相机检测");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CameraUtils.getInstance().closeCamera();
    }
}
