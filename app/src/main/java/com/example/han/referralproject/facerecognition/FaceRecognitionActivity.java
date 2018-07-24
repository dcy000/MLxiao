package com.example.han.referralproject.facerecognition;

import android.Manifest;
import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.han.referralproject.MainActivity;
import com.example.han.referralproject.R;
import com.example.han.referralproject.Test_mainActivity;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.activity.DetectActivity;
import com.example.han.referralproject.application.MyApplication;
import com.example.han.referralproject.bean.UserInfoBean;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.example.han.referralproject.util.LocalShared;
import com.example.han.referralproject.util.ThreadPoolTool;
import com.example.han.referralproject.util.Utils;
import com.example.han.referralproject.xindian.XinDianDetectActivity;
import com.gcml.lib_utils.display.ToastUtils;
import com.gcml.lib_utils.permission.PermissionsManager;
import com.gcml.lib_utils.permission.PermissionsResultAction;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.IdentityResult;
import com.iflytek.cloud.SpeechError;
import com.medlink.danbogh.signin.SignInActivity;
import com.medlink.danbogh.utils.JpushAliasUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * Created by gzq on 2018/4/29.
 */

public class FaceRecognitionActivity extends BaseActivity implements View.OnClickListener {
    private static final int CORE_THREAD = 2;
    private static final int PREVIEW_WIDTH = 1280;
    private static final int PREVIEW_HEIGHT = 720;
    private static final int OPEN_CAMERA_ERROR = 1001;
    private static final int GET_PREVIEW_IMG = 1002;
    private static final int RESOLVE_IMG_SUCCESS = 1003;
    private static final int RECOGNITION_SUCCESS = 1004;
    private static final int RECOGNITION_FAIL = 1005;
    private static Camera mCamera;
    private static int orientationResult;
    private LottieAnimationView mLottAnimation;
    private ImageView mIvBack;
    private Button mTiaoGuo;
    private TextView mTvTip;
    private static ThreadPoolTool threadPoolTool;
    private boolean isTest;
    private static boolean isNeedCreateGroup = false;
    private static String groupid;
    private static String currentXfid;
    private static List<UserInfoBean> mUsersInfo;
    private SurfaceView mSurfaceview;
    private static SurfaceHolder holder;
    private LottieAnimationViewAnimationListener animatorListener;
    // 缩放矩阵
    private static Matrix mScaleMatrix;
    private static SurfaceCallBack surfaceCallBack;
    private static OneShotPreview oneShotPreview;
    private ImageView mIvCircle;
    private static MyHandle handler;
    private static String fromString;
    private static String orderid;
    private static String fromType;
    private static String mAuthid;
    private static boolean isOnPause = false;
    private static int authenticationNum;
    private static final String[] permissions = new String[]{Manifest.permission.CAMERA};
    private ImageView mPreImg;
    private static Animation circleAnimation;

    public static void startActivity(Context context, Class clazz, Bundle bundle, boolean isForResult) {
        if (isForResult && bundle != null) {
            int requestCode = bundle.getInt("requestCode", 1);
            ((FragmentActivity) context).startActivityForResult(new Intent(context, clazz).putExtras(bundle), requestCode);
        } else
            context.startActivity(new Intent(context, clazz).putExtras(bundle));
    }

    private static class MyHandle extends Handler {
        private WeakReference<FaceRecognitionActivity> weakContext;
        private WeakReference<ImageView> weakCircleImage;
        private WeakReference<TextView> weakTips;
        private WeakReference<ImageView> weakPreImage;

        public MyHandle(FaceRecognitionActivity context, ImageView circle, TextView tips, ImageView pre) {
            weakContext = new WeakReference<FaceRecognitionActivity>(context);
            weakCircleImage = new WeakReference<ImageView>(circle);
            weakTips = new WeakReference<>(tips);
            weakPreImage = new WeakReference<ImageView>(pre);
        }


        @Override
        public void handleMessage(Message msg) {
            FaceRecognitionActivity faceRecognitionActivity = weakContext.get();
            if (faceRecognitionActivity == null) {
                return;
            }
            switch (msg.what) {
                case OPEN_CAMERA_ERROR://打开相机失败
                    ToastUtils.showShort("启动相机失败");
                    break;
                case GET_PREVIEW_IMG://获取帧图像
                    oneShotPreview = new OneShotPreview();
                    mCamera.setOneShotPreviewCallback(oneShotPreview);
                    break;
                case RESOLVE_IMG_SUCCESS://解析图像成功
                    byte[] byteData = (byte[]) msg.obj;
                    if (circleAnimation != null) {
                        ImageView imageView = weakCircleImage.get();
                        if (imageView != null) {
                            imageView.startAnimation(circleAnimation);
                        }
                    }
                    TextView tips = weakTips.get();
                    if (threadPoolTool != null && faceRecognitionActivity != null && tips != null) {

                        threadPoolTool.execute(new FaceRecognitionRunnable(faceRecognitionActivity, byteData, tips));
                    }
                    break;
                case RECOGNITION_SUCCESS:
                    ToastUtils.showShort("通过验证");
                    if ("Welcome".equals(fromString)) {
                        faceRecognitionActivity.startActivity(new Intent(faceRecognitionActivity, MainActivity.class));
                    } else if ("Test".equals(fromString)) {
                        new JpushAliasUtils(faceRecognitionActivity).setAlias("user_" + msg.obj);
                        Intent intent = new Intent();
                        if (TextUtils.isEmpty(fromType)) {
                            intent.setClass(faceRecognitionActivity, Test_mainActivity.class);
                        } else if ("xindian".equals(fromType)) {
                            intent.setClass(faceRecognitionActivity, XinDianDetectActivity.class);
                        } else {
                            intent.setClass(faceRecognitionActivity, DetectActivity.class);
                            intent.putExtra("type", fromType);
                        }
                        faceRecognitionActivity.startActivity(intent);
                    } else if ("Pay".equals(fromType)) {
                        faceRecognitionActivity.setResult(RESULT_OK);
                    }
                    faceRecognitionActivity.finish();
                    break;
                case RECOGNITION_FAIL:
                    if ("Welcome".equals(fromString)) {
                        ToastUtils.showLong("该机器人没有此账号的人脸认证信息，请手动登录");
                        faceRecognitionActivity.startActivity(new Intent(faceRecognitionActivity, SignInActivity.class));
                    } else if ("Test".equals(fromString) || "Pay".equals(fromType)) {
                        faceRecognitionActivity.speak(faceRecognitionActivity.getString(R.string.shop_yanzheng));
                        Object o = msg.obj;
                        if (null == o) {
                            ToastUtils.showShort("验证不通过");
                        } else {
                            ToastUtils.showLong("匹配度" + String.format("%.2f", (double) msg.obj) + "%,验证不通过!");
                        }
                    }
                    faceRecognitionActivity.finish();
                    break;
            }
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_recognition);
        initView();
        dealLogic();
    }


    private void dealLogic() {
        if ("Pay".equals(fromString) || "Welcome".equals(fromString)) {//支付过来
            mTiaoGuo.setVisibility(View.GONE);
        }
        if ("Test".equals(fromString)) {
            mTiaoGuo.setVisibility(View.VISIBLE);
        }
        //先检查权限
        if (PermissionsManager.getInstance().hasPermission(this, permissions[0])) {
            if (isTest) {
                openAnimation(mLottAnimation);
            } else {
                threadPoolTool.execute(new JoinGroupRunnable(mTvTip, mLottAnimation, this));
                threadPoolTool.execute(new GetUserInforRunable(this));
            }
        } else {
            PermissionsManager.getInstance().requestPermissionsIfNecessaryForResult(this, permissions, new PermissionsResultAction() {
                @Override
                public void onGranted() {
                    if (isTest) {
                        openAnimation(mLottAnimation);
                    } else {
                        threadPoolTool.execute(new JoinGroupRunnable(mTvTip, mLottAnimation, FaceRecognitionActivity.this));
                        threadPoolTool.execute(new GetUserInforRunable(FaceRecognitionActivity.this));
                    }
                }

                @Override
                public void onDenied(String permission) {
                    handler.sendEmptyMessage(RECOGNITION_FAIL);
                }
            });
        }
    }

    private static class FaceRecognitionSuccess implements Runnable {
        private IdentityResult result;
        private FaceRecognitionActivity context;


        public FaceRecognitionSuccess(FaceRecognitionActivity context, IdentityResult result) {
            this.result = result;
            this.context = context;
        }

        @Override
        public void run() {
            try {
                String resultStr = result.getResultString();
                JSONObject resultJson = new JSONObject(resultStr);
                if (ErrorCode.SUCCESS == resultJson.getInt("ret")) {//此处检验百分比
                    JSONArray scoreList = resultJson.getJSONObject("ifv_result").getJSONArray("candidates");
                    Timber.e(scoreList.toString());
                    String scoreFirstXfid = scoreList.getJSONObject(0).optString("user");
                    Timber.e("最高分数的讯飞id" + scoreFirstXfid);
                    final double firstScore = scoreList.getJSONObject(0).optDouble("score");
                    if (firstScore > 80) {
                        if ("Test".equals(fromString) || "Welcome".equals(fromString)) {
                            authenticationSuccessForTest(context, scoreFirstXfid);
                        } else if ("Pay".equals(fromString)) {
                            dealPayResult(scoreFirstXfid);
                        }

                    } else {
                        if (firstScore > 30) {
                            authenticationNum = 0;
                            ToastUtils.showShort("请将您的面孔靠近摄像头，再试一次");
                            handler.sendEmptyMessageDelayed(GET_PREVIEW_IMG, 1000);
                        } else {
                            Message message = new Message();
                            message.what = RECOGNITION_FAIL;
                            message.obj = firstScore;
                            handler.sendMessage(message);
                        }
                    }
                } else {
                    ToastUtils.showShort("识别失败");
                    handler.sendEmptyMessage(RECOGNITION_FAIL);
                }
            } catch (JSONException e) {
                Timber.e(e, "验证失败");
            }
        }

    }

    private static void authenticationSuccessForTest(Context context, String scoreFirstXfid) {
        if (mUsersInfo != null) {
            boolean isInclude_PassPerson = false;
            String userId = null;
            for (int i = 0; i < mUsersInfo.size(); i++) {
                UserInfoBean user = mUsersInfo.get(i);
                if (user.xfid.equals(scoreFirstXfid)) {
                    Timber.e("识别到的讯飞id" + user.xfid + "++++识别到的人" + user.bname);
                    userId = user.bid;
                    LocalShared.getInstance(context).setUserInfo(user);
                    LocalShared.getInstance(context).setSex(user.sex);
                    LocalShared.getInstance(context).setUserPhoto(user.user_photo);
                    LocalShared.getInstance(context).setUserAge(user.age);
                    LocalShared.getInstance(context).setUserHeight(user.height);
                    isInclude_PassPerson = true;
                    break;
                } else {
                    isInclude_PassPerson = false;
                }
            }
            Log.e("查有此人", "authenticationSuccessForTest: " + isInclude_PassPerson);
            if (isInclude_PassPerson) {
                Log.e("有这个人", "authenticationSuccessForTest: ");
                Message message = new Message();
                message.what = RECOGNITION_SUCCESS;
                message.obj = userId;
                handler.sendMessage(message);
            } else {
                Log.e("没有这个人", "authenticationSuccessForTest: ");
                handler.sendEmptyMessage(RECOGNITION_FAIL);
            }
        }
    }

    private static void dealPayResult(String scoreFirstXfid) {
        if (mAuthid.equals(scoreFirstXfid) && !isOnPause) {
            NetworkApi.pay_status(MyApplication.getInstance().userId, Utils.getDeviceId(), orderid, new NetworkManager.SuccessCallback<String>() {
                @Override
                public void onSuccess(String response) {
                    Message message = new Message();
                    message.what = RECOGNITION_SUCCESS;
                    message.obj = MyApplication.getInstance().userId;
                    handler.sendMessage(message);
                }

            }, new NetworkManager.FailedCallback() {
                @Override
                public void onFailed(String message) {
                    handler.sendEmptyMessage(RECOGNITION_FAIL);
                }
            });
        } else {
            NetworkApi.pay_cancel("3", "0", "1", orderid, new NetworkManager.SuccessCallback<String>() {
                @Override
                public void onSuccess(String response) {
                    handler.sendEmptyMessage(RECOGNITION_FAIL);
                }

            }, new NetworkManager.FailedCallback() {
                @Override
                public void onFailed(String message) {

                }
            });
        }
    }

    private static class FaceRecognitionRunnable implements Runnable {
        private WeakReference<FaceRecognitionActivity> weakContext;
        private WeakReference<byte[]> weakDatas;
        private TextView tips;

        public FaceRecognitionRunnable(FaceRecognitionActivity context, byte[] datas, TextView tips) {
            weakContext = new WeakReference<>(context);
            weakDatas = new WeakReference<>(datas);
            this.tips = tips;
        }

        @Override
        public void run() {
            final FaceRecognitionActivity context = weakContext.get();
            byte[] mImageData = weakDatas.get();
            if (context == null || mImageData == null) {
                return;
            }
            FaceAuthenticationUtils.getInstance(context).verificationFace(mImageData, groupid);
            FaceAuthenticationUtils.getInstance(context).setOnVertifyFaceListener(new IVertifyFaceListener() {
                @Override
                public void onResult(IdentityResult result, boolean islast) {
                    if (null == result) {
                        handler.sendEmptyMessage(GET_PREVIEW_IMG);
                        return;
                    }
                    threadPoolTool.execute(new FaceRecognitionSuccess(context, result));
                }

                @Override
                public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
                    tips.setText("努力验证中...");
                }

                @Override
                public void onError(SpeechError error) {
                    if (authenticationNum < 5) {
                        authenticationNum++;
                        ToastUtils.showShort("第" + Utils.getChineseNumber(authenticationNum) + "次验证失败");
                        handler.sendEmptyMessageDelayed(GET_PREVIEW_IMG, 1000);
                    } else {
                        handler.sendEmptyMessage(RECOGNITION_FAIL);
                    }
                }
            });
        }
    }

    private static class DealPreviewImg implements Runnable {
        private WeakReference<Camera> weakCamer;
        private byte[] data;

        public DealPreviewImg(byte[] data, Camera camera) {
            this.data = data;
            weakCamer = new WeakReference<Camera>(camera);
        }

        @Override
        public void run() {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Bitmap sourceBitmap = decodeToBitMap(data, mCamera, baos);
            if (sourceBitmap != null) {
                Bitmap bitmap = Utils.centerSquareScaleBitmap(sourceBitmap, 300);
                if (bitmap != null) {
                    baos.reset();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] mImageData = baos.toByteArray();
                    try {
                        baos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Message message = new Message();
                    message.what = RESOLVE_IMG_SUCCESS;
                    message.obj = mImageData;
                    if (handler != null) {
                        handler.sendMessage(message);
                    }
                }
            }
        }
    }

    private static Bitmap decodeToBitMap(byte[] data, Camera mCamera, ByteArrayOutputStream baos) {
        if (mCamera == null) {
            return null;
        }
        try {
            Camera.Size size = mCamera.getParameters().getPreviewSize();
            YuvImage image = new YuvImage(data, ImageFormat.NV21, size.width, size.height, null);
            if (image != null) {
                image.compressToJpeg(new Rect(0, 0, size.width, size.height), 80, baos);
                Bitmap bmp = BitmapFactory.decodeByteArray(baos.toByteArray(), 0, baos.size());
                Matrix matrix = new Matrix();
                switch (orientationResult) {
                    case 90:
                        matrix.postRotate(270);
                        break;
                    case 270:
                        matrix.postRotate(90);
                        break;
                    default:
                        matrix.postRotate(orientationResult);
                        break;
                }
                Bitmap nbmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
                return nbmp;
            }
        } catch (Exception ex) {
            Log.e("tag", "Error:" + ex.getMessage());
        }
        return null;
    }

    private static class OpenCameraAndPreview implements Runnable {
        private WeakReference<Context> weaContext;

        public OpenCameraAndPreview(Context context) {
            weaContext = new WeakReference<Context>(context);
        }

        @Override
        public void run() {
            try {
                mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
            } catch (Exception e) {
                e.printStackTrace();
                mCamera = null;
            }
            if (mCamera == null) {
                handler.sendEmptyMessage(OPEN_CAMERA_ERROR);
                return;
            }
            Camera.Parameters params = mCamera.getParameters();
            params.setPreviewFormat(ImageFormat.NV21);
            params.setPreviewSize(PREVIEW_WIDTH, PREVIEW_HEIGHT);
            mCamera.setParameters(params);
            setCameraDisplayOrientation((FaceRecognitionActivity) weaContext.get(), Camera.CameraInfo.CAMERA_FACING_BACK, mCamera);
            try {
                mCamera.setPreviewDisplay(holder);
            } catch (IOException e) {
                Timber.e(e, "设置预览失败");
                handler.sendEmptyMessage(OPEN_CAMERA_ERROR);
            }
            mCamera.startPreview();
        }
    }

    private static class JoinGroupRunnable implements Runnable {
        private WeakReference<TextView> weakTips;
        private WeakReference<LottieAnimationView> weakLott;
        private WeakReference<Context> weakContext;

        public JoinGroupRunnable(TextView tips, LottieAnimationView lott, Context context) {
            weakTips = new WeakReference<TextView>(tips);
            weakLott = new WeakReference<LottieAnimationView>(lott);
            weakContext = new WeakReference<Context>(context);
        }

        @Override
        public void run() {
            if (!isNeedCreateGroup) {
                joinGroup(weakTips.get(), weakLott.get(), weakContext.get());
            } else {
                createGroup(weakTips.get(), weakLott.get(), weakContext.get(), currentXfid);
            }
        }
    }

    private static class GetUserInforRunable implements Runnable {
        private WeakReference<Context> weakContext;

        public GetUserInforRunable(Context context) {
            weakContext = new WeakReference<Context>(context);
        }

        @Override
        public void run() {
            String[] accounts = LocalShared.getInstance(weakContext.get()).getAccounts();
            if (accounts == null) {
                return;
            }
            StringBuilder mAccountIdBuilder = new StringBuilder();
            for (String item : accounts) {
                mAccountIdBuilder.append(item.split(",")[0]).append(",");
            }
            NetworkApi.getAllUsers(mAccountIdBuilder.substring(0, mAccountIdBuilder.length() - 1),
                    new NetworkManager.SuccessCallback<ArrayList<UserInfoBean>>() {
                        @Override
                        public void onSuccess(ArrayList<UserInfoBean> response) {
                            if (response == null) {
                                return;
                            }
                            mUsersInfo = response;
                        }
                    });
        }
    }

    private static class LottieAnimationViewAnimationListener implements Animator.AnimatorListener {

        @Override
        public void onAnimationStart(Animator animation) {

        }

        @Override
        public void onAnimationEnd(Animator animation) {
            if (handler != null && mCamera != null)
                handler.sendEmptyMessage(GET_PREVIEW_IMG);
        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    }

    private static class OneShotPreview implements Camera.PreviewCallback {

        @Override
        public void onPreviewFrame(byte[] data, Camera camera) {
            if (threadPoolTool != null) {
                threadPoolTool.execute(new DealPreviewImg(data, camera));
            }
        }
    }

    /**
     * 设置预览图像的方向
     *
     * @param activity
     * @param cameraId
     * @param camera
     */
    private static void setCameraDisplayOrientation(Activity activity,
                                                    int cameraId, Camera camera) {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);
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

        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            orientationResult = (info.orientation + degrees) % 360;
            orientationResult = (360 - orientationResult) % 360;  // compensate the mirror
        } else {  // back-facing
            orientationResult = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(orientationResult);
    }

    private static void createGroup(final TextView tips, final LottieAnimationView lott, final Context context, final String xfid) {
        FaceAuthenticationUtils.getInstance(context).createGroup(xfid);
        FaceAuthenticationUtils.getInstance(context).setOnCreateGroupListener(new ICreateGroupListener() {
            @Override
            public void onResult(IdentityResult result, boolean islast) {
                try {
                    JSONObject resObj = new JSONObject(result.getResultString());
                    String groupId = resObj.getString("group_id");
                    LocalShared.getInstance(context).setGroupId(groupId);
                    LocalShared.getInstance(context).setGroupFirstXfid(xfid);
                    isNeedCreateGroup = false;
                    threadPoolTool.execute(new JoinGroupRunnable(tips, lott, context));
                    FaceAuthenticationUtils.getInstance(context).updateGroupInformation(groupId, xfid);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            }

            @Override
            public void onError(SpeechError error) {
                Timber.e(error, "创建组失败");
            }
        });
    }

    private static void joinGroup(final TextView tips, final LottieAnimationView lott, final Context context) {
        FaceAuthenticationUtils.getInstance(context).joinGroup(groupid, currentXfid);
        FaceAuthenticationUtils.getInstance(context).setOnJoinGroupListener(new IJoinGroupListener() {
            @Override
            public void onResult(IdentityResult result, boolean islast) {
                tips.setText("请将人脸对准识别框");
                openAnimation(lott);
            }

            @Override
            public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {

            }

            @Override
            public void onError(SpeechError error) {
                Timber.e(error, "添加成员出现异常");
                if (error.getErrorCode() == 10143 || error.getErrorCode() == 10106) {//该组不存在;无效的参数
                    isNeedCreateGroup = true;
                    threadPoolTool.execute(new JoinGroupRunnable(tips, lott, context));
                } else {
                    openAnimation(lott);
                }

            }
        });
    }

    private static void openAnimation(LottieAnimationView lott) {
        lott.playAnimation();
    }

    private static void closeAnimation(ImageView circle, LottieAnimationView lott) {
        circle.clearAnimation();
        lott.reverseAnimation();
    }

    private static class SurfaceCallBack implements SurfaceHolder.Callback {
        private WeakReference<FaceRecognitionActivity> weakContext;

        public SurfaceCallBack(FaceRecognitionActivity context) {
            weakContext = new WeakReference<FaceRecognitionActivity>(context);
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            if (threadPoolTool != null) {
                threadPoolTool.execute(new OpenCameraAndPreview(weakContext.get()));
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            mScaleMatrix.setScale(width / (float) PREVIEW_HEIGHT, height / (float) PREVIEW_WIDTH);
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            weakContext.get().finish();
        }
    }

    private void initView() {
        Intent intent = getIntent();
        if (intent != null) {
            isTest = intent.getBooleanExtra("isTest", false);
            orderid = intent.getStringExtra("orderid");
            fromString = intent.getStringExtra("from");
            fromType = intent.getStringExtra("fromType");
        }

        mAuthid = LocalShared.getInstance(this).getXunfeiId();
        groupid = LocalShared.getInstance(this).getGroupId();
        currentXfid = LocalShared.getInstance(this).getXunfeiId();
        mLottAnimation = (LottieAnimationView) findViewById(R.id.lott_animation);
        animatorListener = new LottieAnimationViewAnimationListener();
        mLottAnimation.addAnimatorListener(animatorListener);
        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mIvBack.setOnClickListener(this);
        mTiaoGuo = (Button) findViewById(R.id.tiao_guo);
        mTiaoGuo.setOnClickListener(this);
        mTvTip = (TextView) findViewById(R.id.tv_tip);
        mSurfaceview = (SurfaceView) findViewById(R.id.surfaceview);
        mIvCircle = (ImageView) findViewById(R.id.iv_circle);
        mPreImg = (ImageView) findViewById(R.id.pre_img);
        threadPoolTool = new ThreadPoolTool(ThreadPoolTool.Type.CachedThread, CORE_THREAD);
        circleAnimation = AnimationUtils.loadAnimation(this, R.anim.rotate_face_check);
        handler = new MyHandle(this, mIvCircle, mTvTip, mPreImg);

        //配置摄像头基本参数
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;
        int height = (int) (width * PREVIEW_WIDTH / (float) PREVIEW_HEIGHT);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, height);
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        mSurfaceview.setLayoutParams(params);
        holder = mSurfaceview.getHolder();
        mScaleMatrix = new Matrix();
        surfaceCallBack = new SurfaceCallBack(this);
        holder.addCallback(surfaceCallBack);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.tiao_guo:
                if (isTest) {
                    startActivity(new Intent(FaceRecognitionActivity.this, Test_mainActivity.class)
                            .putExtra("isTest", isTest));
                    return;
                }

                if ("Test".equals(fromString)) {
                    Intent intent = new Intent();
                    if (TextUtils.isEmpty(fromType)) {
                        intent.setClass(FaceRecognitionActivity.this, Test_mainActivity.class);
                    } else if ("xindian".equals(fromType)) {
                        intent.setClass(FaceRecognitionActivity.this, XinDianDetectActivity.class);
                    } else {
                        intent.setClass(FaceRecognitionActivity.this, DetectActivity.class);
                        intent.putExtra("type", fromType);
                    }
                    startActivity(intent);
                } else if ("Welcome".equals(fromString)) {
                    startActivity(new Intent(FaceRecognitionActivity.this, SignInActivity.class));
                }
                finish();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        isOnPause = false;
        setDisableGlobalListen(false);
        setEnableListeningLoop(false);
    }

    @Override
    protected void onPause() {
        super.onPause();
        isOnPause = true;
        closeAnimation(mIvCircle, mLottAnimation);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (threadPoolTool != null) {
            threadPoolTool.execute(new CloseCameraRunnable());
        }

    }

    private static class CloseCameraRunnable implements Runnable {

        @Override
        public void run() {
            if (mCamera != null) {
                mCamera.setPreviewCallback(null);
                mCamera.stopPreview();
                mCamera.release();
                mCamera = null;
            }
            if (holder != null) {
                holder.removeCallback(surfaceCallBack);
                holder = null;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
        authenticationNum = 0;
        orientationResult = 0;
        isNeedCreateGroup = false;
        groupid = null;
        currentXfid = null;
        mUsersInfo = null;
        surfaceCallBack = null;
        oneShotPreview = null;
        circleAnimation = null;
        mLottAnimation.removeAnimatorListener(animatorListener);
        FaceAuthenticationUtils.getInstance(this).cancelIdentityVerifier();
        if (threadPoolTool != null && !threadPoolTool.isShutDown()) {
            threadPoolTool.shutDownNow();
            threadPoolTool = null;
        }
    }
}
