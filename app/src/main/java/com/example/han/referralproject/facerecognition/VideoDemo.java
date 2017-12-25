package com.example.han.referralproject.facerecognition;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import android.Manifest.permission;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PreviewCallback;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Process;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;

import com.example.han.referralproject.R;
import com.example.han.referralproject.Test_mainActivity;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.application.MyApplication;
import com.example.han.referralproject.bean.NDialog;
import com.example.han.referralproject.bean.NDialog2;
import com.example.han.referralproject.music.ToastUtils;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.example.han.referralproject.recyclerview.RecoDocActivity;
import com.example.han.referralproject.shopping.GoodDetailActivity;
import com.example.han.referralproject.shopping.OrderListActivity;
import com.example.han.referralproject.shopping.ShopListActivity;
import com.example.han.referralproject.util.FaceRect;
import com.example.han.referralproject.util.FaceUtil;
import com.example.han.referralproject.util.LocalShared;
import com.example.han.referralproject.util.ParseResult;
import com.example.han.referralproject.util.Utils;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.FaceDetector;
import com.iflytek.cloud.FaceRequest;
import com.iflytek.cloud.RequestListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.util.Accelerometer;
import com.megvii.faceppidcardui.util.ConstantData;

import org.json.JSONException;
import org.json.JSONObject;


public class VideoDemo extends BaseActivity {
    private final static String TAG = VideoDemo.class.getSimpleName();
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
    private Accelerometer mAcc;
    // FaceDetector对象，集成了离线人脸识别：人脸检测、视频流检测功能
//    private FaceDetector mFaceDetector;
    private boolean mStopTrack;
    private Toast mToast;
    private long mLastClickTime;
    private int isAlign = 0;
    //ImageView mImageView;
    private byte[] mImageData = null;
    boolean sign = true;
    //   SharedPreferences sharedPreferences;

    String mAuthid;
    // FaceRequest对象，集成了人脸识别的各种功能
    private FaceRequest mFaceRequest;
    public ImageView mImageView;
    Bitmap b3;
    String signs;
    String orderid;
    NDialog2 dialog2;

    private MediaPlayer mediaPlayer;//MediaPlayer对象


    private String[] xfid;//存放本地取得所有xfid
    private String fromString;//标识从哪个页面过来的
    private int indexXfid;//记录讯飞id匹配到第几个了
    private String choosedXfid;//选中的讯飞id;
    private HashMap<String, String> map;
    private String[] accounts;

    Button mButton;

    private String jump;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_demo);

        mediaPlayer = MediaPlayer.create(this, R.raw.face_validation);

        mediaPlayer.start();//播放音乐

        map = new HashMap<>();
        accounts = LocalShared.getInstance(this).getAccounts();
        if (accounts != null) {
            indexXfid = accounts.length * 5;
            xfid = new String[accounts.length];
            for (int i = 0; i < accounts.length; i++) {
                xfid[i] = accounts[i].split(",")[1];
                map.put(accounts[i].split(",")[1], accounts[i].split(",")[0]);
            }
        }
        choosedXfid = MyApplication.getInstance().xfid;//默认选中的是当前的讯飞id;
        Intent intent = getIntent();
        signs = intent.getStringExtra("sign");
        orderid = intent.getStringExtra("orderid");
        fromString = intent.getStringExtra("from");
        jump = intent.getStringExtra("jump");


        dialog2 = new NDialog2(VideoDemo.this);


        mImageView = (ImageView) findViewById(R.id.tiao_guo);
        mImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mButton = (Button) findViewById(R.id.tiao_guos);


        if ("1".equals(signs)) {//支付过来
            mImageView.setVisibility(View.GONE);
        }
        if ("1".equals(jump)) {
            mButton.setVisibility(View.VISIBLE);

        }

        mButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Test_mainActivity.class);
                startActivity(intent);
                finish();
            }
        });


        SpeechUtility.createUtility(this, "appid=" + getString(R.string.app_id));


        mAuthid = LocalShared.getInstance(getApplicationContext()).getXunfeiId();

        Log.e("讯飞id", mAuthid);
        initUI();


        nv21 = new byte[PREVIEW_WIDTH * PREVIEW_HEIGHT * 2];
        buffer = new byte[PREVIEW_WIDTH * PREVIEW_HEIGHT * 2];
        mAcc = new Accelerometer(VideoDemo.this);

        mFaceRequest = new FaceRequest(this);


    }

    @Override
    protected void onStop() {
        super.onStop();

        mediaPlayer.pause();
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

        // 点击SurfaceView，切换摄相头
       /* mFaceSurface.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // 只有一个摄相头，不支持切换
                if (Camera.getNumberOfCameras() == 1) {
                    showTip("只有后置摄像头，不能切换");
                    return;
                }
                closeCamera();
                if (CameraInfo.CAMERA_FACING_FRONT == mCameraId) {
                    mCameraId = CameraInfo.CAMERA_FACING_BACK;
                } else {
                    mCameraId = CameraInfo.CAMERA_FACING_FRONT;
                }
                openCamera();
            }
        });*/

       /* // 长按SurfaceView 500ms后松开，摄相头聚集
        mFaceSurface.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mLastClickTime = System.currentTimeMillis();
                        break;
                    case MotionEvent.ACTION_UP:
                        if (System.currentTimeMillis() - mLastClickTime > 500) {
                            mCamera.autoFocus(null);
                            return true;
                        }
                        break;

                    default:
                        break;
                }
                return false;
            }
        });*/

        setSurfaceSize();
        mToast = Toast.makeText(VideoDemo.this, "", Toast.LENGTH_SHORT);
    }

    private void openCamera() {
        if (null != mCamera) {
            return;
        }

        if (!checkCameraPermission()) {
            showTip("摄像头权限未打开，请打开后再试");
            mStopTrack = true;
            return;
        }

        // 只有一个摄相头，打开后置
        if (Camera.getNumberOfCameras() == 1) {
            mCameraId = CameraInfo.CAMERA_FACING_BACK;
        }

        try {
            mCamera = Camera.open(mCameraId);
           /* if (CameraInfo.CAMERA_FACING_FRONT == mCameraId) {
                showTip("前置摄像头已开启，点击可切换");
            } else {
                showTip("后置摄像头已开启，点击可切换");
            }*/
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
            }
        });

        try {
            mCamera.setPreviewDisplay(mPreviewSurface.getHolder());
            mCamera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }

//        if (mFaceDetector == null) {
//            /**
//             * 离线视频流检测功能需要单独下载支持离线人脸的SDK
//             * 请开发者前往语音云官网下载对应SDK
//             */
//            // 创建单例失败，与 21001 错误为同样原因，参考 http://bbs.xfyun.cn/forum.php?mod=viewthread&tid=9688
//            showTip("创建对象失败，请确认 libmsc.so 放置正确，\n 且有调用 createUtility 进行初始化");
//        }
    }
    private int first_match=5;//第一次匹配本机账号使用
    @Override
    protected void onStart() {
        super.onStart();

        if (null != mAuthid) {

            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (sign) {

                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                        }

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();


                        if (b3 != null) {

                            Bitmap bitmap = centerSquareScaleBitmap(b3, 300);

                            //    Bitmap bitmap = getCircleBitmap(bitmap1);


                            //可根据流量及网络状况对图片进行压缩
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                            mImageData = baos.toByteArray();

                        }
                        if (null != mImageData && null != mAuthid) {
                            if("Test".equals(fromString)&&mAuthid!=null&&first_match>0){
                                mFaceRequest.setParameter(SpeechConstant.AUTH_ID, mAuthid);
                                first_match--;
                            }else if ("Test".equals(fromString) && indexXfid > 0&&accounts!=null) {
                                mFaceRequest.setParameter(SpeechConstant.AUTH_ID, xfid[indexXfid % accounts.length]);
                                choosedXfid = xfid[indexXfid % accounts.length];
                                indexXfid--;
                            } else {
                                mFaceRequest.setParameter(SpeechConstant.AUTH_ID, mAuthid);
                            }
                            mFaceRequest.setParameter(SpeechConstant.WFR_SST, "verify");
                            mFaceRequest.sendRequest(mImageData, mRequestListener);

                        }


                    }
                }
            }

            ).start();


        }
    }


    public Bitmap getCircleBitmap(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        try {
            Bitmap circleBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(circleBitmap);
            final Paint paint = new Paint();
            final Rect rect = new Rect(0, 0, bitmap.getWidth(),
                    bitmap.getHeight());
            final RectF rectF = new RectF(new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight()));
            float roundPx = 0.0f;
            // 以较短的边为标准
            if (bitmap.getWidth() > bitmap.getHeight()) {
                roundPx = bitmap.getHeight() / 2.0f;
            } else {
                roundPx = bitmap.getWidth() / 2.0f;
            }
            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(Color.WHITE);
            canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            final Rect src = new Rect(0, 0, bitmap.getWidth(),
                    bitmap.getHeight());
            canvas.drawBitmap(bitmap, src, rect, paint);
            return circleBitmap;
        } catch (Exception e) {
            return bitmap;
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
                result = Bitmap.createBitmap(scaledBitmap, xTopLeft, yTopLeft, edgeLength, edgeLength);
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
        }

        @Override
        public void onBufferReceived(byte[] buffer) {


            try {
                String result = new String(buffer, "utf-8");
                Log.d("FaceDemo", result);

                JSONObject object = new JSONObject(result);
                String type = object.optString("sst");
                if ("verify".equals(type)) {
                    verify(object);
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (JSONException e) {
            }
        }

        @Override
        public void onCompleted(SpeechError error) {

        }
    };


    private void verify(JSONObject obj) throws JSONException {
        int ret = obj.getInt("ret");
        if (ret != 0 && sign == true) {
            showTip("验证失败");
            sign = false;
            finish();
            return;
        }
        if ("success".equals(obj.get("rst"))) {
            if (obj.getBoolean("verf") && sign == true) {

                if ("0".equals(signs)) {
                    showTip("通过验证，欢迎回来！");
                    if (!TextUtils.isEmpty(choosedXfid) && !MyApplication.getInstance().xfid.equals(choosedXfid)) {//如果不是选中的讯飞id,已经改变，则切换账号
                        MyApplication.getInstance().userId = map.get(choosedXfid);
                        MyApplication.getInstance().xfid = choosedXfid;
                        sendBroadcast(new Intent("change_account"));
                    }
                    Intent intent = new Intent(getApplicationContext(), Test_mainActivity.class);
                    startActivity(intent);
                    sign = false;
                    finish();

                } else if ("1".equals(signs)) {
                    sign = false;
                    NetworkApi.pay_status(MyApplication.getInstance().userId, Utils.getDeviceId(), orderid, new NetworkManager.SuccessCallback<String>() {
                        @Override
                        public void onSuccess(String response) {
                            speak(getString(R.string.shop_success));
                            ShowNormal("支付成功", "1");
                            GoodDetailActivity.mActivity.finish();
                        }

                    }, new NetworkManager.FailedCallback() {
                        @Override
                        public void onFailed(String message) {

                            ShowNormal("支付失败", "0");


                        }
                    });


                }

            } else {
                if (sign == true) {

                    if ("0".equals(signs)) {
                        if (indexXfid <= 0) {
                            showTip("验证不通过");
                            sign = false;
                            finish();
                        }

                    } else if ("1".equals(signs)) {


                        NetworkApi.pay_cancel("3", "0", "1", orderid, new NetworkManager.SuccessCallback<String>() {
                            @Override
                            public void onSuccess(String response) {

                                speak(getString(R.string.shop_yanzheng));
                                showTip("验证不通过");
                                finish();

                            }

                        }, new NetworkManager.FailedCallback() {
                            @Override
                            public void onFailed(String message) {


                            }
                        });

                    }


                }
            }
        } else {
            if (sign == true) {
                showTip("验证失败");
                sign = false;
                finish();
            }
        }
    }


    public void ShowNormal(String message, final String sign) {
        dialog2.setMessageCenter(true)
                .setMessage(message)
                .setMessageSize(50)
                .setCancleable(false)
                .setButtonCenter(true)
                .setPositiveTextColor(Color.parseColor("#3F86FC"))
                .setButtonSize(40)
                .setOnConfirmListener(new NDialog2.OnConfirmListener() {
                    @Override
                    public void onClick(int which) {
                        if (which == 1) {

                            if ("1".equals(sign)) {
                                Intent intent = new Intent(getApplicationContext(), OrderListActivity.class);
                                startActivity(intent);
                                finish();

                            } else {

                                Intent intent = new Intent(getApplicationContext(), OrderListActivity.class);
                                startActivity(intent);
                                finish();

                            }


                        }

                    }
                }).create(NDialog.CONFIRM).show();

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
        if (null != mAcc) {
            mAcc.stop();
        }
        mStopTrack = true;
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

}
