package com.example.han.referralproject;

import android.graphics.Bitmap;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.facerecognition.FaceAuthenticationUtils;
import com.example.han.referralproject.facerecognition.IRegisterFaceListener;
import com.example.han.referralproject.facerecognition.ISingleFaceVertifyListener;
import com.gcml.lib_utils.camera.CameraUtils;
import com.gcml.lib_utils.display.ImageUtils;
import com.gcml.lib_utils.display.LoadingProgressUtils;
import com.iflytek.cloud.IdentityResult;
import com.iflytek.cloud.SpeechError;

import java.io.UnsupportedEncodingException;

public class TestCameraAndRegisterXF extends BaseActivity implements View.OnClickListener {
    private SurfaceView mSurfaceView;
    private RelativeLayout mScanBorder;
    private ImageView mPreImage;
    private byte[] cacheImage;
    /**
     * 重新预览
     */
    private Button mRePreview;
    /**
     * 注册讯飞
     */
    private Button mRegisterXF;
    private RelativeLayout mRootBorder;
    /**
     * 验证人脸
     */
    private Button mVertifyFace;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_camera_and_register_xf);
        initView();
        CameraUtils.getInstance().init(this, mSurfaceView, 1920, 1200, 856, 856);
        CameraUtils.getInstance().setOnCameraPreviewCallback(new CameraUtils.CameraPreviewCallBack() {
            @Override
            public void onStartPreview() {

            }

            @Override
            public void onStartResolveImage() {
                LoadingProgressUtils.showViewWithLabel(TestCameraAndRegisterXF.this, "正在解析图片");
            }

            @Override
            public void previewSuccess(byte[] datas, Bitmap preBitmap, Bitmap cropBitmap, int prewidth, int preheight, int cropwidth, int cropheight) {
                cacheImage = ImageUtils.bitmap2Bytes(cropBitmap, Bitmap.CompressFormat.JPEG);
//                cacheImage = datas;
                LoadingProgressUtils.dismissView();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mPreImage.setImageBitmap(cropBitmap);
                    }
                });


            }

            @Override
            public void openCameraFail(Exception e) {

            }

            @Override
            public void cameraClosed() {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        CameraUtils.getInstance().openCamera(Camera.CameraInfo.CAMERA_FACING_BACK);
    }

    @Override
    protected void onStop() {
        super.onStop();
        CameraUtils.getInstance().closeCamera();
    }

    private void initView() {
        mSurfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        mScanBorder = (RelativeLayout) findViewById(R.id.scan_border);
        mPreImage = (ImageView) findViewById(R.id.preImage);
        mRePreview = (Button) findViewById(R.id.rePreview);
        mRePreview.setOnClickListener(this);
        mRegisterXF = (Button) findViewById(R.id.registerXF);
        mRegisterXF.setOnClickListener(this);
        mRootBorder = (RelativeLayout) findViewById(R.id.root_border);
        mVertifyFace = (Button) findViewById(R.id.vertifyFace);
        mVertifyFace.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.rePreview:
                CameraUtils.getInstance().restartPreview(0);
                break;
            case R.id.registerXF:
                if (cacheImage != null) {
                    try {
                        FaceAuthenticationUtils.getInstance(this).registerFace(cacheImage, new String("1bca3d0263b30932_901221_test_ltc".getBytes(), "UTF-8"));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    FaceAuthenticationUtils.getInstance(this).setOnRegisterListener(new IRegisterFaceListener() {
                        @Override
                        public void onResult(IdentityResult result, boolean islast) {
                            Log.e("注册讯飞成功", "onResult: " + result.getResultString());
                        }

                        @Override
                        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
                        }

                        @Override
                        public void onError(SpeechError error) {
                            Log.e("注册讯飞失败", "onError: " + error.getErrorCode() +
                                    "+++" + error.getPlainDescription(true) + error.getHtmlDescription(true) +
                                    "+++" + error.getErrorDescription());
                        }
                    });
                }
                break;
            //1bca3d0263b30932_901221_test_ltc
            case R.id.vertifyFace:
                break;
        }
    }
}
