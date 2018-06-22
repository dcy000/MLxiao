package com.example.han.referralproject.facerecognition;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import android.Manifest.permission;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
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
import android.text.TextUtils;
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

import com.airbnb.lottie.LottieAnimationView;
import com.example.han.referralproject.MainActivity;
import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.activity.MyBaseDataActivity;
import com.example.han.referralproject.application.MyApplication;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.example.han.referralproject.recyclerview.RecoDocActivity;
import com.example.han.referralproject.util.LocalShared;
import com.example.han.referralproject.util.ToastTool;
import com.example.han.referralproject.util.Utils;
import com.example.han.referralproject.yisuotang.fragment.AffirmHeadDialog;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.FaceRequest;
import com.iflytek.cloud.IdentityResult;
import com.iflytek.cloud.RequestListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.medlink.danbogh.utils.T;
import com.orhanobut.logger.Logger;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.json.JSONException;
import org.json.JSONObject;


public class RegisterVideoActivity extends BaseActivity implements PreviewCallback, AffirmHeadDialog.ClickListener {
    private SurfaceView mPreviewSurface;
    private Camera mCamera;
    private int mCameraId = CameraInfo.CAMERA_FACING_FRONT;
    // Camera nv21格式预览帧的尺寸，默认设置640*480
    private int PREVIEW_WIDTH = 1280;
    private int PREVIEW_HEIGHT = 720;
    // 缩放矩阵
    private Matrix mScaleMatrix = new Matrix();
    private byte[] mImageData = null;
    boolean sign = true;
    String mAuthid;
    private FaceRequest mFaceRequest;
    public ImageView rlBack;
    private ByteArrayOutputStream stream;
    private LottieAnimationView lottAnimation;
    private SurfaceHolder holder;

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
                                                    ToastTool.showShort("请调整您的姿态");
                                                }
                                            });

                                        }

                                    } catch (InterruptedException e) {

                                    }
                                    //再给用户2秒进行姿态调整
                                    try {
                                        Thread.sleep(2000);
                                        if (sign && mCamera != null) {
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
        initUI();
        setSurfaceSize();
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
        holder = mPreviewSurface.getHolder();
        holder.addCallback(mPreviewCallback);
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
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
        mPreviewSurface = (SurfaceView) findViewById(R.id.sfv_preview);
        Animation rotateAnim = AnimationUtils.loadAnimation(mContext, R.anim.rotate_face_check);
        findViewById(R.id.iv_circle).startAnimation(rotateAnim);
        rlBack = (ImageView) findViewById(R.id.iv_back);
        findViewById(R.id.tiao_guos).setVisibility(View.GONE);
        rlBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        lottAnimation = findViewById(R.id.lott_animation);
        lottAnimation.setImageAssetsFolder("lav_imgs/");
        lottAnimation.setAnimation("camera_pre.json");
        stream = new ByteArrayOutputStream();
        lottAnimation.playAnimation();
    }


    private void openCamera() {
        if (null != mCamera) {
            return;
        }
        if (!checkCameraPermission()) {
            ToastTool.showShort("摄像头权限未打开，请打开后再试");
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
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }


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
                Log.e("获取注册时候讯飞的信息", "onBufferReceived: " + obj.toString());
                String type = obj.optString("sst");
                if ("reg".equals(type)) {
                    int ret = obj.getInt("ret");
                    if (ret != 0) {
                        if (sign == true) {
                            ToastTool.showShort("注册失败");
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
                        if (closePage()) return;
                        Intent intent = new Intent(getApplicationContext(), HeadiconActivity.class);
                        intent.putExtras(getIntent());
                        startActivity(intent);
                        finish();
                    } else {
                        if (sign == true) {
                            ToastTool.showShort("注册失败");
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
                        ToastTool.showShort("账号已经被注册，请更换后再试");
                        sign = false;
                        finish();
                        break;
                    default:
                        break;
                }
            }

        }
    };

    private boolean closePage() {
        if (getIntent() != null &&
                MyBaseDataActivity.FROM_VALUE.equals(getIntent().getStringExtra(MyBaseDataActivity.FROM_KEY))) {
            showAffirmHeadDialog();
            return true;
        }
        return false;
    }

    AffirmHeadDialog affirmHeadDialog;

    private void showAffirmHeadDialog() {
        affirmHeadDialog = new AffirmHeadDialog();
        affirmHeadDialog.setListener(this);
        affirmHeadDialog.show(getFragmentManager(), "changeHead");
    }

    @Override
    public void onConfirm() {
        final String userid = MyApplication.getInstance().userId;
        final String xfid = LocalShared.getInstance(RegisterVideoActivity.this).getXunfeiId();
        checkGroup(userid, xfid);
        finish();
    }

    private void checkGroup(final String userid, final String xfid) {
        //在登录的时候判断该台机器有没有创建人脸识别组，如果没有则创建
        String groupId = LocalShared.getInstance(mContext).getGroupId();
        String firstXfid = LocalShared.getInstance(mContext).getGroupFirstXfid();
        Logger.e("组id" + groupId);
        if (!TextUtils.isEmpty(groupId) && !TextUtils.isEmpty(firstXfid)) {
            Log.e("组信息", "checkGroup: 该机器组已近存在");
            joinGroup(userid, groupId, xfid);
        } else {
            createGroup(userid, xfid);
        }
    }

    UploadManager uploadManager = new UploadManager();

    private void createGroup(final String userid, final String xfid) {
        FaceAuthenticationUtils.getInstance(RegisterVideoActivity.this).createGroup(xfid);
        FaceAuthenticationUtils.getInstance(RegisterVideoActivity.this).setOnCreateGroupListener(new CreateGroupListener() {
            @Override
            public void onResult(IdentityResult result, boolean islast) {
                Logger.e("创建组成功" + result);
                try {
                    JSONObject resObj = new JSONObject(result.getResultString());
                    String groupId = resObj.getString("group_id");
                    LocalShared.getInstance(RegisterVideoActivity.this).setGroupId(groupId);
                    LocalShared.getInstance(RegisterVideoActivity.this).setGroupFirstXfid(xfid);
                    //组创建好以后把自己加入到组中去
                    joinGroup(userid, groupId, xfid);
                    //加组完成以后把头像上传到我们自己的服务器
                    uploadHeadToSelf(userid, xfid);
                    FaceAuthenticationUtils.getInstance(RegisterVideoActivity.this).updateGroupInformation(groupId, xfid);

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
//                if (error.getErrorCode() == 10144) {//创建组的数量达到上限
//                    ToastTool.showShort("出现技术故障，请致电客服咨询");
//                }
                //如果在此处创建组失败就跳过创建
                uploadHeadToSelf(userid, xfid);
            }
        });
    }

    private void joinGroup(final String userid, String groupid, final String xfid) {
        FaceAuthenticationUtils.getInstance(this).joinGroup(groupid, xfid);
        FaceAuthenticationUtils.getInstance(RegisterVideoActivity.this).setOnJoinGroupListener(new JoinGroupListener() {
            @Override
            public void onResult(IdentityResult result, boolean islast) {
                uploadHeadToSelf(userid, xfid);
            }

            @Override
            public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {

            }

            @Override
            public void onError(SpeechError error) {
                Logger.e(error, "添加成员出现异常");
                if (error.getErrorCode() == 10143 || error.getErrorCode() == 10106) {//该组不存在;无效的参数
                    createGroup(userid, xfid);
                } else {
                    uploadHeadToSelf(userid, xfid);
                }

            }
        });
    }

    private void uploadHeadToSelf(final String userid, final String xfid) {
        NetworkApi.get_token(new NetworkManager.SuccessCallback<String>() {
            @Override
            public void onSuccess(String response) {
                byte[] data = Base64.decode(LocalShared.getInstance(getApplicationContext()).getUserImg().getBytes(), 1);
                Date date = new Date();
                SimpleDateFormat simple = new SimpleDateFormat("yyyyMMddhhmmss");
                StringBuilder str = new StringBuilder();//定义变长字符串
                Random random = new Random();
                for (int i = 0; i < 8; i++) {
                    str.append(random.nextInt(10));
                }
                //将字符串转换为数字并输出
                String key = simple.format(date) + str + ".jpg";

                uploadManager.put(data, key, response, new UpCompletionHandler() {
                    @Override
                    public void complete(String key, ResponseInfo info, JSONObject res) {
                        if (info.isOK()) {
                            String imageUrl = "http://oyptcv2pb.bkt.clouddn.com/" + key;
                            NetworkApi.return_imageUrl(imageUrl, MyApplication.getInstance().userId, LocalShared.getInstance(getApplicationContext()).getXunfeiId(),
                                    new NetworkManager.SuccessCallback<Object>() {
                                        @Override
                                        public void onSuccess(Object response) {
                                            //将账号在本地缓存
                                            LocalShared.getInstance(mContext).addAccount(userid, xfid);
                                            finish();
                                        }

                                    }, new NetworkManager.FailedCallback() {
                                        @Override
                                        public void onFailed(String message) {
                                            Log.e("注册储存讯飞id失败", "onFailed: ");
                                        }
                                    });
                        } else {

                        }
                    }
                }, null);
            }

        }, new NetworkManager.FailedCallback() {
            @Override
            public void onFailed(String message) {


            }
        });
    }

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
            if (holder != null) {
                holder.removeCallback(mPreviewCallback);
            }
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
        if (lottAnimation != null) {
            lottAnimation.cancelAnimation();
        }
        if (stream != null) {
            try {
                stream.close();
                stream = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onActivitySpeakFinish() {
        mCamera.setOneShotPreviewCallback(this);
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        Bitmap b3 = decodeToBitMap(data, camera);
        if (b3 != null) {
            stream.reset();
            Bitmap bitmap = Utils.centerSquareScaleBitmap(b3, 300);
            //可根据流量及网络状况对图片进行压缩
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            mImageData = stream.toByteArray();
        }
        mHandler.sendEmptyMessage(0);
        Log.e("测试该方法调用的次数", "onPreviewFrame: ");
    }

}
