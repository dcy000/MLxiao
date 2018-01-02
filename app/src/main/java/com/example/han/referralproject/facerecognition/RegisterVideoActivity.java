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
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;

import com.example.han.referralproject.MainActivity;
import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.util.LocalShared;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.FaceRequest;
import com.iflytek.cloud.RequestListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.util.Accelerometer;

import org.json.JSONException;
import org.json.JSONObject;


public class RegisterVideoActivity extends BaseActivity {
    private SurfaceView mPreviewSurface;
    private SurfaceView mFaceSurface;
    private Camera mCamera;
    private int mCameraId = CameraInfo.CAMERA_FACING_FRONT;
    // Camera nv21格式预览帧的尺寸，默认设置640*480
    private int PREVIEW_WIDTH = 1280;
    private int PREVIEW_HEIGHT = 720;

    // 预览帧数据存储数组和缓存数组
    private byte[] nv21;
    private byte[] buffer;
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


    public RelativeLayout mButton;
    public boolean isTest = false;


    private Handler mHandler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(final Message msg) {
            switch (msg.what) {
                case 0:

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (sign) {

                                try {
                                    Thread.sleep(2000);
                                } catch (InterruptedException e) {
                                }


                                ByteArrayOutputStream baos = new ByteArrayOutputStream();


                  /*  if (b3 != null) {

                        Bitmap bitmap = centerSquareScaleBitmap(b3, 300);

                        //可根据流量及网络状况对图片进行压缩
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        mImageData = baos.toByteArray();

                    }*/


                                if (b3 != null) {

                                    Bitmap bitmap = centerSquareScaleBitmap(b3, 300);


                                    //可根据流量及网络状况对图片进行压缩
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                                    mImageData = baos.toByteArray();

                                }


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



                      /*  String imageBase64 = new String(Base64.encodeToString(mImageData, Base64.DEFAULT));
                        editor.putString("imageData", imageBase64);
                        editor.commit();*/


                                    mFaceRequest.setParameter(SpeechConstant.AUTH_ID, mAuthid);
                                    mFaceRequest.setParameter(SpeechConstant.WFR_SST, "reg");
                                    mFaceRequest.sendRequest(mImageData, mRequestListener);
                                    Log.e("发送前", mAuthid + "");

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


    //private MediaPlayer mediaPlayer;//MediaPlayer对象


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_demo);

      //  mediaPlayer = MediaPlayer.create(this, R.raw.face_register);

      //  mediaPlayer.start();//播放音乐

        isTest = getIntent().getBooleanExtra("isTest", false);

        mButton = (RelativeLayout) findViewById(R.id.tiao_guo);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isTest){
                    startActivity(new Intent(mContext, MainActivity.class));
                }
                finish();
            }
        });


        initUI();

        nv21 = new byte[PREVIEW_WIDTH * PREVIEW_HEIGHT * 2];
        buffer = new byte[PREVIEW_WIDTH * PREVIEW_HEIGHT * 2];
        //mAcc = new Accelerometer(RegisterVideoActivity.this);


        mFaceRequest = new FaceRequest(this);
//        findViewById(R.id.tiao_RelativeLayout).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(getApplicationContext(), HeadiconActivity.class));
//                finish();
//            }
//        });
    }


    private Callback mPreviewCallback = new Callback() {

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            closeCamera();
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            openCamera();
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            mScaleMatrix.setScale(width / (float) PREVIEW_HEIGHT, height / (float) PREVIEW_WIDTH);
        }
    };

    @Override
    protected void onStop() {
        super.onStop();

        //mediaPlayer.pause();
    }

    private void setSurfaceSize() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        int width = metrics.widthPixels;
        int height = (int) (width * PREVIEW_WIDTH / (float) PREVIEW_HEIGHT);
        RelativeLayout.LayoutParams params = new LayoutParams(width, height);
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP);

        mPreviewSurface.setLayoutParams(params);
        mFaceSurface.setLayoutParams(params);
    }

    @SuppressLint("ShowToast")
    @SuppressWarnings("deprecation")
    private void initUI() {
        mPreviewSurface = (SurfaceView) findViewById(R.id.sfv_preview);
        mFaceSurface = (SurfaceView) findViewById(R.id.sfv_face);

        mPreviewSurface.getHolder().addCallback(mPreviewCallback);
        mPreviewSurface.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mFaceSurface.setZOrderOnTop(true);
        mFaceSurface.getHolder().setFormat(PixelFormat.TRANSLUCENT);

        setSurfaceSize();
        mToast = Toast.makeText(RegisterVideoActivity.this, "", Toast.LENGTH_SHORT);
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

        mCamera.setPreviewCallback(new PreviewCallback() {

            @Override
            public void onPreviewFrame(byte[] data, Camera camera) {
                System.arraycopy(data, 0, nv21, 0, data.length);
                b3 = decodeToBitMap(nv21, camera);

                //mImageView.setImageBitmap(b3);


            }
        });

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


    @Override
    protected void onStart() {
        super.onStart();
        if (isTest){
            return;
        }
        mHandler.sendEmptyMessageDelayed(0, 2500);
    }

    private RequestListener mRequestListener = new RequestListener() {

        @Override
        public void onEvent(int eventType, Bundle params) {
        }

        @Override
        public void onBufferReceived(byte[] buffer) {
            try {
                String result = new String(buffer, "utf-8");
                Log.d("FaceDemo", result);
                JSONObject object = new JSONObject(result);
                String type = object.optString("sst");
                if ("reg".equals(type)) {
                    register(object);
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
                        //  showTip(error.getPlainDescription(true));
                        //  sign = false;
                        //   finish();
                        break;
                }
            }

        }
    };


    private void register(JSONObject obj) throws JSONException {
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


    /**
     * NV21格式(所有相机都支持的格式)转换为bitmap
     */
    public Bitmap decodeToBitMap(byte[] data, Camera mCamera) {
        Camera.Size size = mCamera.getParameters().getPreviewSize();
        try {
            YuvImage image = new YuvImage(data, ImageFormat.NV21, size.width, size.height, null);
            if (image != null) {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                image.compressToJpeg(new Rect(0, 0, size.width, size.height), 80, stream);
                Bitmap bmp = BitmapFactory.decodeByteArray(stream.toByteArray(), 0, stream.size());
                stream.close();
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
//            speak(R.string.tips_face);
//        if (null != mAcc) {
//            mAcc.start();
//        }
//
//        mStopTrack = false;
//        new Thread(new Runnable() {
//
//            @Override
//            public void run() {
//                while (!mStopTrack) {
//                    if (null == nv21) {
//                        continue;
//                    }
//
//                    synchronized (nv21) {
//                        System.arraycopy(nv21, 0, buffer, 0, nv21.length);
//                    }
//
//                    // 获取手机朝向，返回值0,1,2,3分别表示0,90,180和270度
//                    int direction = Accelerometer.getDirection();
//                    boolean frontCamera = (Camera.CameraInfo.CAMERA_FACING_FRONT == mCameraId);
//                    // 前置摄像头预览显示的是镜像，需要将手机朝向换算成摄相头视角下的朝向。
//                    // 转换公式：a' = (360 - a)%360，a为人眼视角下的朝向（单位：角度）
//                    if (frontCamera) {
//                        // SDK中使用0,1,2,3,4分别表示0,90,180,270和360度
//                        direction = (4 - direction) % 4;
//                    }
//
//                    if (mFaceDetector == null) {
//                        /**
//                         * 离线视频流检测功能需要单独下载支持离线人脸的SDK
//                         * 请开发者前往语音云官网下载对应SDK
//                         */
//                        // 创建单例失败，与 21001 错误为同样原因，参考 http://bbs.xfyun.cn/forum.php?mod=viewthread&tid=9688
//                        showTip("创建对象失败，请确认 libmsc.so 放置正确，\n 且有调用 createUtility 进行初始化");
//                        break;
//                    }
//
//                    String result = mFaceDetector.trackNV21(buffer, PREVIEW_WIDTH, PREVIEW_HEIGHT, isAlign, direction);
//                    Log.d(TAG, "result:" + result);
//
//                    FaceRect[] faces = ParseResult.parseResult(result);
//
//                    Canvas canvas = mFaceSurface.getHolder().lockCanvas();
//                    if (null == canvas) {
//                        continue;
//                    }
//
//                    canvas.drawColor(0, PorterDuff.Mode.CLEAR);
//                    canvas.setMatrix(mScaleMatrix);
//
//                    if (faces == null || faces.length <= 0) {
//                        mFaceSurface.getHolder().unlockCanvasAndPost(canvas);
//                        continue;
//                    }
//
//                    if (null != faces && frontCamera == (Camera.CameraInfo.CAMERA_FACING_FRONT == mCameraId)) {
//                        for (FaceRect face : faces) {
//                            face.bound = FaceUtil.RotateDeg90(face.bound, PREVIEW_WIDTH, PREVIEW_HEIGHT);
//                            if (face.point != null) {
//                                for (int i = 0; i < face.point.length; i++) {
//                                    face.point[i] = FaceUtil.RotateDeg90(face.point[i], PREVIEW_WIDTH, PREVIEW_HEIGHT);
//                                }
//                            }
//                            FaceUtil.drawFaceRect(canvas, face, PREVIEW_WIDTH, PREVIEW_HEIGHT, frontCamera, false);
//                        }
//                    } else {
//                        Log.d(TAG, "faces:0");
//                    }
//
//                    mFaceSurface.getHolder().unlockCanvasAndPost(canvas);
//                }
//            }
//        }).start();
    }


    @Override
    protected void onPause() {
        super.onPause();
        closeCamera();
      /*  if (null != mAcc) {
            mAcc.stop();
        }*/
     //   mStopTrack = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sign = false;
//        if (null != mFaceDetector) {
//            // 销毁对象
//            mFaceDetector.destroy();
//        }
    }

    private void showTip(final String str) {
        mToast.setText(str);
        mToast.show();
    }

    @Override
    protected void onSpeakListenerResult(String result) {

    }
}
