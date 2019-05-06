package com.example.module_control_volume.ui;

import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.SurfaceView;
import android.view.View;

import com.example.module_control_volume.R;
import com.gcml.common.utils.base.ToolbarBaseActivity;
import com.gcml.common.utils.camera.CameraUtils;

public class CameraActivity extends ToolbarBaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_test);

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
