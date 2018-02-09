package com.example.han.referralproject.facerecognition;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import android.Manifest.permission;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PreviewCallback;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;

import com.example.han.referralproject.MainActivity;
import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.util.LocalShared;
import com.example.han.referralproject.util.ToastUtil;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.FaceRequest;
import com.iflytek.cloud.RequestListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.util.Accelerometer;

import org.json.JSONException;
import org.json.JSONObject;


public class RegisterVideoActivity extends BaseActivity implements PreviewCallback {
    private SurfaceView mPreviewSurface;
    //    private SurfaceView mFaceSurface;
    private Camera mCamera;
    private int mCameraId = CameraInfo.CAMERA_FACING_FRONT;
    // Camera nv21格式预览帧的尺寸，默认设置640*480
    private int PREVIEW_WIDTH = 1280;
    private int PREVIEW_HEIGHT = 720;

    // 预览帧数据存储数组和缓存数组
//    private byte[] nv21;
//    private byte[] data;
    // 缩放矩阵
    private Matrix mScaleMatrix = new Matrix();
    // 加速度感应器，用于获取手机的朝向
    //  private Accelerometer mAcc;
    // FaceDetector对象，集成了离线人脸识别：人脸检测、视频流检测功能
//    private FaceDetector mFaceDetector;
    //private boolean mStopTrack;
    private Toast mToast;
    //private int isAlign = 0;
    private byte[] mImageData = null;
    boolean sign = true;

    String mAuthid;
    // FaceRequest对象，集成了人脸识别的各种功能
    private FaceRequest mFaceRequest;


    public RelativeLayout rlBack;
    public boolean isTest = false;
    private ByteArrayOutputStream stream;
    private Handler mHandler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(final Message msg) {
            switch (msg.what) {
                case 0:
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (sign) {
                                mFaceRequest = new FaceRequest(RegisterVideoActivity.this);
                                if (null != mImageData) {
                                    Date date = new Date();
                                    SimpleDateFormat simple = new SimpleDateFormat("yyyyMMddhhmmss");
                                    StringBuilder str = new StringBuilder();//定义变长字符串
                                    Random random = new Random();
                                    for (int i = 0; i < 8; i++) {
                                        str.append(random.nextInt(10));
                                    }
                                    //将字符串转换为数字并输出
                                    mAuthid = simple.format(date) + str;
                                    mFaceRequest.setParameter(SpeechConstant.AUTH_ID, mAuthid);
                                    mFaceRequest.setParameter(SpeechConstant.WFR_SST, "reg");
                                    mFaceRequest.sendRequest(mImageData, mRequestListener);
                                    //5秒之后如果上一次的图像还没有上传成功，多半原因是拍摄的图像不包含头像信息或者头像太模糊。这5秒是给用户重新调整姿态进行拍摄

                                    try {
                                        //如果3秒之后sign的状态仍然没有改变，极大可能上传头像失败，这时候有必要提醒用户重新调整姿态，进行一下一次拍摄
                                        Thread.sleep(2000);
                                        if (sign) {
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    ToastUtil.showShort(RegisterVideoActivity.this, "请调整您的姿态");
                                                }
                                            });

                                        }

                                    } catch (InterruptedException e) {

                                    }
                                    //再给用户2秒进行姿态调整
                                    try {
                                        Thread.sleep(2000);
                                        if (sign&&mCamera!=null) {
                                            mCamera.setOneShotPreviewCallback(RegisterVideoActivity.this);
                                        }
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    }

                    ).start();
                    break;
            }

            return true;
        }
    });


    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_demo);
        isTest = getIntent().getBooleanExtra("isTest", false);
        initUI();

        mPreviewSurface = (SurfaceView) findViewById(R.id.sfv_preview);

        mPreviewSurface.getHolder().addCallback(mPreviewCallback);
        mPreviewSurface.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        setSurfaceSize();
        stream = new ByteArrayOutputStream();
    }


    private Callback mPreviewCallback = new Callback() {

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            closeCamera();
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            Log.e("Surface创建成功", "surfaceCreated: ");
            // 启动相机
            new Thread(new Runnable() {
                @Override
                public void run() {
                    openCamera();
                }
            }).start();
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            Log.e("Surface改变回调", "surfaceChanged: ");
            mScaleMatrix.setScale(width / (float) PREVIEW_HEIGHT, height / (float) PREVIEW_WIDTH);
        }
    };

    @Override
    protected void onStop() {
        super.onStop();
        sign = false;
    }

    private void setSurfaceSize() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        int width = metrics.widthPixels;
        int height = (int) (width * PREVIEW_WIDTH / (float) PREVIEW_HEIGHT);
        LayoutParams params = new LayoutParams(width, height);
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        mPreviewSurface.setLayoutParams(params);
//        mFaceSurface.setLayoutParams(params);
    }

    private void initUI() {
        Animation rotateAnim = AnimationUtils.loadAnimation(mContext, R.anim.rotate_face_check);
        findViewById(R.id.iv_circle).startAnimation(rotateAnim);
        rlBack = (RelativeLayout) findViewById(R.id.rl_back);
        findViewById(R.id.tiao_guos).setVisibility(View.GONE);
        rlBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isTest) {
                    startActivity(new Intent(mContext, MainActivity.class));
                }
                finish();
            }
        });
    }

    Bitmap b3;

    private void openCamera() {
        if (null != mCamera) {
            return;
        }

        if (!checkCameraPermission()) {
            showTip("摄像头权限未打开，请打开后再试");
            //mStopTrack = true;
            return;
        }

        // 只有一个摄相头，打开后置
        if (Camera.getNumberOfCameras() == 1) {
            mCameraId = CameraInfo.CAMERA_FACING_BACK;
        }

        try {
            mCamera = Camera.open(mCameraId);

        } catch (Exception e) {
            e.printStackTrace();
            closeCamera();
            return;
        }

        Parameters params = mCamera.getParameters();
        params.setPreviewFormat(ImageFormat.NV21);
        params.setPreviewSize(PREVIEW_WIDTH, PREVIEW_HEIGHT);
        mCamera.setParameters(params);

        // 设置显示的偏转角度，大部分机器是顺时针90度，某些机器需要按情况设置
        mCamera.setDisplayOrientation(0);
        try {
            mCamera.setPreviewDisplay(mPreviewSurface.getHolder());
            mCamera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    public static Bitmap centerSquareScaleBitmap(Bitmap bitmap, int edgeLength) {
        if (null == bitmap || edgeLength <= 0) {
            return null;
        }

        Bitmap result = bitmap;
        int widthOrg = bitmap.getWidth();
        int heightOrg = bitmap.getHeight();

        if (widthOrg > edgeLength && heightOrg > edgeLength) {
            //压缩到一个最小长度是edgeLength的bitmap
            int longerEdge = (int) (edgeLength * Math.max(widthOrg, heightOrg) / Math.min(widthOrg, heightOrg));
            int scaledWidth = widthOrg > heightOrg ? longerEdge : edgeLength;
            int scaledHeight = widthOrg > heightOrg ? edgeLength : longerEdge;
            Bitmap scaledBitmap;

            try {
                scaledBitmap = Bitmap.createScaledBitmap(bitmap, scaledWidth, scaledHeight, true);
            } catch (Exception e) {
                return null;
            }

            //从图中截取正中间的正方形部分。
            int xTopLeft = (scaledWidth - edgeLength) / 2;
            int yTopLeft = (scaledHeight - edgeLength) / 2;

            try {
                result = Bitmap.createBitmap(scaledBitmap, xTopLeft, yTopLeft, edgeLength, edgeLength - 50);
                scaledBitmap.recycle();
            } catch (Exception e) {
                return null;
            }
        }

        return result;
    }

    private RequestListener mRequestListener = new RequestListener() {

        @Override
        public void onEvent(int eventType, Bundle params) {
            Log.e("上传头像的测试", "onEvent: ");
        }

        @Override
        public void onBufferReceived(byte[] buffer) {
            try {
                String result = new String(buffer, "utf-8");
                Log.e("上传头像返回的信息", "onBufferReceived: " + result);
                JSONObject obj = new JSONObject(result);
                Log.e("获取注册时候讯飞的信息", "onBufferReceived: " +obj.toString() );
                String type = obj.optString("sst");
                if ("reg".equals(type)) {
                    int ret = obj.getInt("ret");
                    if (ret != 0) {
                        if (sign == true) {
                            showTip("注册失败");
                            sign = false;
                            finish();
                            return;
                        }
                    }
                    if ("success".equals(obj.get("rst")) && sign == true) {
                        // showTip("注册成功");
                        sign = false;
                        LocalShared.getInstance(getApplicationContext()).setXunfeiID(mAuthid);
                        String imageBase64 = new String(Base64.encodeToString(mImageData, Base64.DEFAULT));

                        LocalShared.getInstance(getApplicationContext()).setUserImg(imageBase64);


                        Intent intent = new Intent(getApplicationContext(), HeadiconActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        if (sign == true) {
                            showTip("注册失败");
                            sign = false;
                            finish();
                        }

                    }
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (JSONException e) {
            }
        }

        @Override
        public void onCompleted(SpeechError error) {

            if (error != null) {
                switch (error.getErrorCode()) {
                    case ErrorCode.MSP_ERROR_ALREADY_EXIST:
                        showTip("账号已经被注册，请更换后再试");
                        sign = false;
                        finish();
                        break;
                    default:
                        break;
                }
            }

        }
    };


    /**
     * NV21格式(所有相机都支持的格式)转换为bitmap
     */
    public Bitmap decodeToBitMap(byte[] data, Camera mCamera) {
        Camera.Size size = mCamera.getParameters().getPreviewSize();
        try {
            YuvImage image = new YuvImage(data, ImageFormat.NV21, size.width, size.height, null);
            if (image != null) {
                stream.reset();
                image.compressToJpeg(new Rect(0, 0, size.width, size.height), 80, stream);
                Bitmap bmp = BitmapFactory.decodeByteArray(stream.toByteArray(), 0, stream.size());
                Matrix matrix = new Matrix();
                matrix.postRotate(0);
                //上面得到的图片旋转了270度,下面将图片旋转回来,前置摄像头270度,后置只需要90度
                Bitmap nbmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
                return nbmp;
            }
        } catch (Exception ex) {
            Log.e("tag", "Error:" + ex.getMessage());
        }
        return null;
    }


    private void closeCamera() {
        if (null != mCamera) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    private boolean checkCameraPermission() {
        int status = checkPermission(permission.CAMERA, Process.myPid(), Process.myUid());
        if (PackageManager.PERMISSION_GRANTED == status) {
            return true;
        }

        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        setDisableGlobalListen(true);
        speak(getString(R.string.facc_register));
    }


    @Override
    protected void onPause() {
        super.onPause();
        closeCamera();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (stream != null) {
            try {
                stream.close();
                stream = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void showTip(final String str) {
        mToast.setText(str);
        mToast.show();
    }

    @Override
    protected void onActivitySpeakFinish() {
        mCamera.setOneShotPreviewCallback(this);
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        b3 = decodeToBitMap(data, camera);
        if (b3 != null) {
            stream.reset();
            Bitmap bitmap = centerSquareScaleBitmap(b3, 300);
            //可根据流量及网络状况对图片进行压缩
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            mImageData = stream.toByteArray();
        }
        mHandler.sendEmptyMessage(0);
        Log.e("测试该方法调用的次数", "onPreviewFrame: ");
    }
}
