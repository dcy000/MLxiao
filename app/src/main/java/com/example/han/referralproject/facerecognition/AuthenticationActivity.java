package com.example.han.referralproject.facerecognition;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import android.Manifest.permission;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PreviewCallback;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.han.referralproject.MainActivity;
import com.example.han.referralproject.R;
import com.example.han.referralproject.Test_mainActivity;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.activity.DetectActivity;
import com.example.han.referralproject.application.MyApplication;
import com.example.han.referralproject.bean.NDialog;
import com.example.han.referralproject.bean.NDialog2;
import com.example.han.referralproject.bean.UserInfoBean;
import com.example.han.referralproject.bean.XfGroupInfo;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.example.han.referralproject.shopping.GoodDetailActivity;
import com.example.han.referralproject.shopping.OrderListActivity;
import com.example.han.referralproject.util.LocalShared;
import com.example.han.referralproject.util.ToastTool;
import com.example.han.referralproject.util.Utils;
import com.example.han.referralproject.xindian.XinDianDetectActivity;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.IdentityResult;
import com.iflytek.cloud.SpeechError;
import com.medlink.danbogh.signin.SignInActivity;
import com.medlink.danbogh.utils.Handlers;
import com.medlink.danbogh.utils.JpushAliasUtils;
import com.orhanobut.logger.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 头像身份验证
 */

public class AuthenticationActivity extends BaseActivity {
    private SurfaceView mPreviewSurface;
    private Camera mCamera;
    // Camera nv21格式预览帧的尺寸，默认设置640*480  1280*720
    private int PREVIEW_WIDTH = 1280;
    private int PREVIEW_HEIGHT = 720;
    // 预览帧数据存储数组和缓存数组
//    private byte[] nv21;
    // 缩放矩阵
    private Matrix mScaleMatrix = new Matrix();
    //    // 加速度感应器，用于获取手机的朝向
//    private Accelerometer mAcc;
    private static byte[] mImageData = null;
    private Bitmap b3;
    private String orderid;
    private NDialog2 dialog2;
    private String[] xfids;//存放本地取得所有xfid
    private String fromString;//标识从哪个页面过来的
    private String fromType;
    private Button mTiaoguo;
    private ByteArrayOutputStream baos;
    private static boolean isGetImageFlag = true;
    private String mAuthid;
    private MyHandler myHandler;
    private ArrayList<UserInfoBean> mDataList;
    private boolean isInclude;
    private int authenticationNum = 0;
    private TextView tvTips;
    private boolean isTest;
    private LottieAnimationView lottAnimation;

    class MyHandler extends Handler {
        private WeakReference<AuthenticationActivity> weakReference;

        public MyHandler(AuthenticationActivity activity) {
            weakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    FaceAuthenticationUtils.getInstance(weakReference.get()).verificationFace(mImageData, LocalShared.getInstance(weakReference.get()).getGroupId());
                    FaceAuthenticationUtils.getInstance(weakReference.get()).setOnVertifyFaceListener(new VertifyFaceListener() {
                        @Override
                        public void onResult(IdentityResult result, boolean islast) {
                            if (null == result) {
                                myHandler.sendEmptyMessage(1);
                                return;
                            }
                            closeAnimation();
                            try {
                                String resultStr = result.getResultString();
                                JSONObject resultJson = new JSONObject(resultStr);
                                if (ErrorCode.SUCCESS == resultJson.getInt("ret")) {//此处检验百分比
                                    JSONArray scoreList = resultJson.getJSONObject("ifv_result").getJSONArray("candidates");
                                    String scoreFirstXfid = scoreList.getJSONObject(0).optString("user");
                                    final double firstScore = scoreList.getJSONObject(0).optDouble("score");
                                    if (firstScore > 80) {
                                        if ("Test".equals(fromString) || "Welcome".equals(fromString)) {
                                            authenticationSuccessForTest$Welcome(scoreFirstXfid, weakReference);
                                        } else if ("Pay".equals(fromString)) {
                                            if (mAuthid.equals(scoreFirstXfid)) {
                                                paySuccess();
                                            } else {
                                                payFail();
                                            }
                                        }

                                    } else {
                                        if (firstScore > 30) {
                                            authenticationNum = 0;
                                            ToastTool.showShort("请将您的面孔靠近摄像头，再试一次");
                                            myHandler.sendEmptyMessage(1);
                                        } else {
                                            ToastTool.showLong("匹配度" + String.format("%.2f", firstScore) + "%,验证不通过!");
                                            finishActivity();
                                            finish();
                                        }
                                    }
                                } else {
                                    ToastTool.showShort("识别失败");
                                }
                            } catch (JSONException e) {
                                Logger.e(e, "验证失败");
                            }
                        }

                        @Override
                        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
                            tvTips.setText("努力验证中...");
                        }

                        @Override
                        public void onError(SpeechError error) {
                            if (!AuthenticationActivity.this.isFinishing() && !AuthenticationActivity.this.isDestroyed())
                                if (authenticationNum < 5) {
                                    authenticationNum++;
                                    ToastTool.showShort("第" + Utils.getChineseNumber(authenticationNum) + "次验证失败");
                                    myHandler.sendEmptyMessageDelayed(1, 1500);
                                } else {
                                    finishActivity();
                                    finish();
                                }
                        }
                    });
                    break;
            }
        }
    }

    /**
     * 通过验证
     *
     * @param scoreFirstXfid
     * @param weakReference
     */
    private void authenticationSuccessForTest$Welcome(String scoreFirstXfid, WeakReference<AuthenticationActivity> weakReference) {
        finishActivity();
        ToastTool.showShort("通过验证，欢迎回来！");
        if (mDataList != null) {
            for (int i = 0; i < mDataList.size(); i++) {
                UserInfoBean user = mDataList.get(i);
                if (user.xfid.equals(scoreFirstXfid)) {
                    new JpushAliasUtils(AuthenticationActivity.this).setAlias("user_" + user.bid);
                    LocalShared.getInstance(mContext).setUserInfo(user);
                    LocalShared.getInstance(mContext).setSex(user.sex);
                    LocalShared.getInstance(mContext).setUserPhoto(user.user_photo);
                    LocalShared.getInstance(mContext).setUserAge(user.age);
                    LocalShared.getInstance(mContext).setUserHeight(user.height);
                    isInclude = true;
                    break;
                } else {
                    isInclude = false;
                }
            }
            if (isInclude) {
                if ("Welcome".equals(fromString)) {
                    startActivity(new Intent(weakReference.get(), MainActivity.class));
                } else if ("Test".equals(fromString)) {
                    Intent intent = new Intent();
                    if (TextUtils.isEmpty(fromType)) {
                        intent.setClass(weakReference.get(), Test_mainActivity.class);
                    } else if ("xindian".equals(fromType)) {
                        intent.setClass(weakReference.get(), XinDianDetectActivity.class);
                    } else {
                        intent.setClass(weakReference.get(), DetectActivity.class);
                        intent.putExtra("type", fromType);
                    }
                    startActivity(intent);
                }
            } else {
                if ("Welcome".equals(fromString)) {
                    ToastTool.showLong("该机器人没有此账号的人脸认证信息，请手动登录");
                    startActivity(new Intent(weakReference.get(), SignInActivity.class));
                } else if ("Test".equals(fromString)) {
                    ToastTool.showLong("验证不通过!");
                }
            }
            finish();
        }
    }

    /**
     * 支付成功
     */
    private void paySuccess() {
        NetworkApi.pay_status(MyApplication.getInstance().userId, Utils.getDeviceId(), orderid, new NetworkManager.SuccessCallback<String>() {
            @Override
            public void onSuccess(String response) {
                speak(getString(R.string.shop_success));
                showNormal("支付成功", "1");
                finishActivity();
                GoodDetailActivity.mActivity.finish();
            }

        }, new NetworkManager.FailedCallback() {
            @Override
            public void onFailed(String message) {
//                payFail();
//                showNormal("支付失败", "0");
            }
        });
    }

    private void payFail() {
        NetworkApi.pay_cancel("3", "0", "1", orderid, new NetworkManager.SuccessCallback<String>() {
            @Override
            public void onSuccess(String response) {
                speak(getString(R.string.shop_yanzheng));
                ToastTool.showShort("验证不通过");
                finish();

            }

        }, new NetworkManager.FailedCallback() {
            @Override
            public void onFailed(String message) {


            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_demo);
        //工厂测试专用
        isTest = getIntent().getBooleanExtra("isTest", false);

        init();
        openCameraPreview();
        if (isTest) {
            openAnimation();
        } else {
            joinGroup();
        }
        setClick();
        getAllUsersInfo();
    }

    private void joinGroup() {
        tvTips.setText("正在连接人脸库,请稍等...");
        String groupid = LocalShared.getInstance(this).getGroupId();
        String firstXfid = LocalShared.getInstance(this).getGroupFirstXfid();
        final String currentXfid = LocalShared.getInstance(this).getXunfeiId();
        FaceAuthenticationUtils.getInstance(this).joinGroup(groupid, currentXfid);
        FaceAuthenticationUtils.getInstance(AuthenticationActivity.this).setOnJoinGroupListener(new JoinGroupListener() {
            @Override
            public void onResult(IdentityResult result, boolean islast) {
                tvTips.setText("请将人脸对准识别框");
                openAnimation();
                //添加完成以后，马上进行人脸匹配
                if (isFirstSend) {
                    myHandler.sendEmptyMessageDelayed(1, 2000);
                    isFirstSend = false;
                }
            }

            @Override
            public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {

            }

            @Override
            public void onError(SpeechError error) {
                Logger.e(error, "添加成员出现异常");
                if (error.getErrorCode() == 10143 || error.getErrorCode() == 10106) {//该组不存在;无效的参数
                    createGroup(currentXfid);
                } else {
                    openAnimation();
                    myHandler.sendEmptyMessageDelayed(1, 2000);
                }

            }
        });
    }

    private void createGroup(final String xfid) {
        FaceAuthenticationUtils.getInstance(this).createGroup(xfid);
        FaceAuthenticationUtils.getInstance(this).setOnCreateGroupListener(new CreateGroupListener() {
            @Override
            public void onResult(IdentityResult result, boolean islast) {
                try {
                    JSONObject resObj = new JSONObject(result.getResultString());
                    String groupId = resObj.getString("group_id");
                    LocalShared.getInstance(AuthenticationActivity.this).setGroupId(groupId);
                    LocalShared.getInstance(AuthenticationActivity.this).setGroupFirstXfid(xfid);
                    //组创建好以后把自己加入到组中去
                    FaceAuthenticationUtils.getInstance(AuthenticationActivity.this).
                            joinGroup(groupId, xfid);
                    FaceAuthenticationUtils.getInstance(AuthenticationActivity.this).setOnJoinGroupListener(new JoinGroupListener() {
                        @Override
                        public void onResult(IdentityResult result, boolean islast) {
                            Log.d("SignInActivity", "onResult:添加自己到组中成功");
                            openAnimation();
                            myHandler.sendEmptyMessageDelayed(1, 2000);
                        }

                        @Override
                        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {

                        }

                        @Override
                        public void onError(SpeechError error) {
                            finishActivity();
                            finish();
                            ToastTool.showShort("验证失败");
                        }
                    });
                    FaceAuthenticationUtils.getInstance(AuthenticationActivity.this).updateGroupInformation(groupId, xfid);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            }

            @Override
            public void onError(SpeechError error) {
                Logger.e(error, "创建组失败");
                ToastTool.showShort("出现技术故障，请致电客服咨询" + error.getErrorCode());
            }
        });
    }

    private void openAnimation() {
//        Animation left = AnimationUtils.loadAnimation(this, R.anim.door_out_left);
//        Animation right = AnimationUtils.loadAnimation(this, R.anim.door_out_right);
//        findViewById(R.id.door_left).startAnimation(left);
//        findViewById(R.id.door_right).startAnimation(right);

    }

    private void closeAnimation() {
        Animation left = AnimationUtils.loadAnimation(this, R.anim.door_in_left);
        Animation right = AnimationUtils.loadAnimation(this, R.anim.door_in_right);
        findViewById(R.id.door_left).startAnimation(left);
        findViewById(R.id.door_right).startAnimation(right);
    }

    private void getAllUsersInfo() {
        String[] accounts = LocalShared.getInstance(this).getAccounts();
        if (accounts == null) {
            return;
        }
        StringBuilder mAccountIdBuilder = new StringBuilder();
        for (String item : accounts) {
            mAccountIdBuilder.append(item.split(",")[0]).append(",");
        }
        NetworkApi.getAllUsers(mAccountIdBuilder.substring(0, mAccountIdBuilder.length() - 1), new NetworkManager.SuccessCallback<ArrayList<UserInfoBean>>() {
            @Override
            public void onSuccess(ArrayList<UserInfoBean> response) {
                if (response == null) {
                    return;
                }
                mDataList = response;
            }
        });
    }

    private void init() {
        Intent intent = getIntent();
        orderid = intent.getStringExtra("orderid");
        fromString = intent.getStringExtra("from");
        fromType = intent.getStringExtra("fromType");

        mAuthid = LocalShared.getInstance(this).getXunfeiId();

        mTiaoguo = (Button) findViewById(R.id.tiao_guos);
        if ("Pay".equals(fromString) || "Welcome".equals(fromString)) {//支付过来
            mTiaoguo.setVisibility(View.GONE);
        }
        if ("Test".equals(fromString)) {
            mTiaoguo.setVisibility(View.VISIBLE);
        }
        dialog2 = new NDialog2(AuthenticationActivity.this);

        Animation rotateAnim = AnimationUtils.loadAnimation(mContext, R.anim.rotate_face_check);
        findViewById(R.id.iv_circle).startAnimation(rotateAnim);
        baos = new ByteArrayOutputStream();
        myHandler = new MyHandler(this);
        tvTips = findViewById(R.id.tv_tip);
        lottAnimation=findViewById(R.id.lott_animation);
        lottAnimation.setAnimation("camera_pre.json");

    }


    private void openCameraPreview() {
//        nv21 = new byte[PREVIEW_WIDTH * PREVIEW_HEIGHT * 2];
        mPreviewSurface = findViewById(R.id.sfv_preview);
        mPreviewSurface.getHolder().addCallback(new Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                Handlers.bg().post(new Runnable() {
                    @Override
                    public void run() {
                        openCamera();//启动相机
                    }
                });
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                mScaleMatrix.setScale(width / (float) PREVIEW_HEIGHT, height / (float) PREVIEW_WIDTH);
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                finish();
            }
        });
        mPreviewSurface.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;
        int height = (int) (width * PREVIEW_WIDTH / (float) PREVIEW_HEIGHT);
        RelativeLayout.LayoutParams params = new LayoutParams(width, height);
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        mPreviewSurface.setLayoutParams(params);


    }

    private void setClick() {
        findViewById(R.id.iv_back).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finishActivity();
                finish();
            }
        });

        mTiaoguo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                finishActivity();
                if (isTest){
                    startActivity(new Intent(AuthenticationActivity.this,Test_mainActivity.class)
                            .putExtra("isTest",isTest));
                    return;
                }

                if ("Test".equals(fromString)) {
                    Intent intent = new Intent();
                    if (TextUtils.isEmpty(fromType)) {
                        intent.setClass(AuthenticationActivity.this, Test_mainActivity.class);
                    } else if ("xindian".equals(fromType)) {
                        intent.setClass(AuthenticationActivity.this, XinDianDetectActivity.class);
                    } else {
                        intent.setClass(AuthenticationActivity.this, DetectActivity.class);
                        intent.putExtra("type", fromType);
                    }
                    startActivity(intent);
                } else if ("Welcome".equals(fromString)) {
                    startActivity(new Intent(AuthenticationActivity.this, SignInActivity.class));
                }
                finish();
            }
        });
    }


    private void openCamera() {
        if (null != mCamera) {
            return;
        }
        if (!checkCameraPermission()) {
            ToastTool.showShort("摄像头权限未打开，请打开后再试");
            return;
        }
        int mCameraId = CameraInfo.CAMERA_FACING_BACK;
        // 只有一个摄相头，打开后置
        if (Camera.getNumberOfCameras() == 1) {
            mCameraId = CameraInfo.CAMERA_FACING_BACK;
        }
        try {
            mCamera = Camera.open(mCameraId);
        } catch (Exception e) {
            e.printStackTrace();
            finishActivity();
            finish();
            return;
        }
        Parameters params = mCamera.getParameters();
        params.setPreviewFormat(ImageFormat.NV21);
        params.setPreviewSize(PREVIEW_WIDTH, PREVIEW_HEIGHT);
        mCamera.setParameters(params);

        // 设置显示的偏转角度，大部分机器是顺时针90度，某些机器需要按情况设置
        setCameraDisplayOrientation(AuthenticationActivity.this, mCameraId, mCamera);
        mCamera.setPreviewCallback(new PreviewCallback() {

            @Override
            public void onPreviewFrame(final byte[] data, Camera camera) {
                if (isGetImageFlag) {
                    isGetImageFlag = false;
                    Handlers.bg().post(new Runnable() {
                        @Override
                        public void run() {
                            b3 = decodeToBitMap(data, mCamera);
                            if (b3 != null) {
                                Bitmap bitmap = Utils.centerSquareScaleBitmap(b3, 300);
                                if (bitmap != null) {
                                    if (null != baos) {
                                        baos.reset();
                                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                                    }
                                    if (null != baos) {
                                        mImageData = baos.toByteArray();
                                    }
                                }
                            }
                            isGetImageFlag = true;
                        }
                    });
                }
            }
        });

        try {
            mCamera.setPreviewDisplay(mPreviewSurface.getHolder());
            mCamera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置预览图像的方向
     *
     * @param activity
     * @param cameraId
     * @param camera
     */
    private void setCameraDisplayOrientation(Activity activity,
                                             int cameraId, android.hardware.Camera camera) {
        android.hardware.Camera.CameraInfo info =
                new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay()
                .getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }
        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);
    }

    private boolean isFirstSend = true;


    public void showNormal(String message, final String sign) {
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
        if (mCamera == null) {
            return null;
        }
        try {
            Camera.Size size = mCamera.getParameters().getPreviewSize();
            YuvImage image = new YuvImage(data, ImageFormat.NV21, size.width, size.height, null);
            if (image != null) {
//                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                baos.reset();
                image.compressToJpeg(new Rect(0, 0, size.width, size.height), 80, baos);
                Bitmap bmp = BitmapFactory.decodeByteArray(baos.toByteArray(), 0, baos.size());
//                baos.close();
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

    private void finishActivity() {
        closeCamera();
        Handlers.bg().removeCallbacksAndMessages(null);
        myHandler.removeCallbacksAndMessages(null);
        findViewById(R.id.iv_circle).clearAnimation();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (baos != null) {
            try {
                baos.close();
                baos = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
