package com.example.han.referralproject.facerecognition;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

import android.Manifest.permission;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PreviewCallback;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
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
import android.widget.Toast;

import com.example.han.referralproject.MainActivity;
import com.example.han.referralproject.R;
import com.example.han.referralproject.Test_mainActivity;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.application.MyApplication;
import com.example.han.referralproject.bean.NDialog;
import com.example.han.referralproject.bean.NDialog2;
import com.example.han.referralproject.bean.UserInfoBean;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.example.han.referralproject.shopping.GoodDetailActivity;
import com.example.han.referralproject.shopping.OrderListActivity;
import com.example.han.referralproject.util.LocalShared;
import com.example.han.referralproject.util.ToastUtil;
import com.example.han.referralproject.util.Utils;
import com.iflytek.cloud.FaceRequest;
import com.iflytek.cloud.RequestListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.util.Accelerometer;
import com.medlink.danbogh.signin.SignInActivity;
import com.medlink.danbogh.utils.JpushAliasUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 头像身份验证
 */

public class AuthenticationActivity extends BaseActivity {
    private SurfaceView mPreviewSurface;
    private Camera mCamera;
    private int mCameraId = CameraInfo.CAMERA_FACING_FRONT;
    // Camera nv21格式预览帧的尺寸，默认设置640*480  1280*720
    private int PREVIEW_WIDTH = 1280;
    private int PREVIEW_HEIGHT = 720;
    // 预览帧数据存储数组和缓存数组
    private byte[] nv21;
    // 缩放矩阵
    private Matrix mScaleMatrix = new Matrix();
    // 加速度感应器，用于获取手机的朝向
    private Accelerometer mAcc;
    private byte[] mImageData = null;
    private String mAuthid;
    // FaceRequest对象，集成了人脸识别的各种功能
    private FaceRequest mFaceRequest;
    public RelativeLayout mImageView;
    private Bitmap b3;
    private String orderid;
    private NDialog2 dialog2;
    private MediaPlayer mediaPlayer;//MediaPlayer对象
    private String[] xfids;//存放本地取得所有xfid
    private String fromString;//标识从哪个页面过来的
    private int indexXfid;//记录讯飞id匹配到第几个了
    private int xfTime = 10;//默认轮训的次数,如果需要修改，记得下面还有两个地方
    private String choosedXfid;//选中的讯飞id;
    private HashMap<String, String> xfid_userid;
    private Button mTiaoguo;
    private ByteArrayOutputStream baos;
    private int unDentified = 10;//未识别的次数，最多10寸
    private ArrayList<UserInfoBean> mDataList;
    private boolean isGetImageFlag = true;//获取图像标志位

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    sendPipei();
                    break;
                case 2:
                    isGetImageFlag = true;
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_demo);
        Intent intent = getIntent();
        orderid = intent.getStringExtra("orderid");
        fromString = intent.getStringExtra("from");


        mediaPlayer = MediaPlayer.create(this, R.raw.face_validation);
        mediaPlayer.start();//播放音乐
        xfid_userid = new HashMap<>();
        //获取本地所有账号
        String[] accounts = LocalShared.getInstance(this).getAccounts();
        if (accounts != null) {
            xfids = new String[accounts.length];
            for (int i = 0; i < accounts.length; i++) {
                xfids[i] = accounts[i].split(",")[1];
                xfid_userid.put(accounts[i].split(",")[1], accounts[i].split(",")[0]);
            }
        }
        getAllUsersInfo(accounts);
        if ("Test".equals(fromString)) {//测试
            indexXfid = 0;
            xfTime = 10;
            unDentified = 10;
        } else if ("Pay".equals(fromString)) {
            indexXfid = xfids.length;//这样做的目的是为了在下面的判断中不走"测试"的分支
            xfTime = 10;
            unDentified = 10;
        } else if ("Welcome".equals(fromString)) {
            indexXfid = 0;
            xfTime = 5;
            unDentified = 5;
        }
        dialog2 = new NDialog2(AuthenticationActivity.this);
        mImageView = (RelativeLayout) findViewById(R.id.rl_back);
        mImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mTiaoguo = (Button) findViewById(R.id.tiao_guos);

        if ("Pay".equals(fromString) || "Welcome".equals(fromString)) {//支付过来
            mTiaoguo.setVisibility(View.GONE);
        }
        if ("Test".equals(fromString)) {
            mTiaoguo.setVisibility(View.VISIBLE);
        }
        mTiaoguo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (faceThread!=null){
                    Log.e(TAG, "onClick: "+"销毁子线程" );
                    faceThread.interrupt();
                }
                if (mediaPlayer.isPlaying()){
                    mediaPlayer.stop();
                }
                if (mFaceRequest!=null){
                    mFaceRequest.cancel();
                }
                if (mAcc!=null){
                    mAcc.stop();
                }
                closeCamera();

                if ("Test".equals(fromString)) {
                    Intent intent = new Intent(getApplicationContext(), Test_mainActivity.class);
                    startActivity(intent);
                } else if ("Welcome".equals(fromString)) {
                    startActivity(new Intent(AuthenticationActivity.this, SignInActivity.class));
                }
                finish();
            }
        });
        mAuthid = LocalShared.getInstance(this).getXunfeiId();
        initUI();
        nv21 = new byte[PREVIEW_WIDTH * PREVIEW_HEIGHT * 2];
        mAcc = new Accelerometer(AuthenticationActivity.this);
        mFaceRequest = new FaceRequest(this);

        Animation rotateAnim = AnimationUtils.loadAnimation(mContext, R.anim.rotate_face_check);
        findViewById(R.id.iv_circle).startAnimation(rotateAnim);
    }

    private void getAllUsersInfo(String[] accounts) {
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

    private Thread faceThread;
    private Callback mPreviewCallback = new Callback() {

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            closeCamera();
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            Log.e(TAG, "surfaceCreated: "+"创建预览对象" );
            // 启动相机
            faceThread=new Thread(new Runnable() {
                @Override
                public void run() {

                    openCamera();
                }
            });
            faceThread.start();
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
    }

    @SuppressLint("ShowToast")
    @SuppressWarnings("deprecation")
    private void initUI() {
        mPreviewSurface = (SurfaceView) findViewById(R.id.sfv_preview);
        mPreviewSurface.getHolder().addCallback(mPreviewCallback);
        mPreviewSurface.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        setSurfaceSize();
    }

    private void openCamera() {
        if (null != mCamera) {
            return;
        }

        if (!checkCameraPermission()) {
            ToastUtil.showShort(this, "摄像头权限未打开，请打开后再试");
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
                if (isGetImageFlag){
                    isGetImageFlag = false;
                    new GetImageTask(data).execute();
                }
//                System.arraycopy(data, 0, nv21, 0, data.length);
//                b3 = decodeToBitMap(nv21, camera);
//                baos = new ByteArrayOutputStream();
//                if (b3 != null) {
//                    Bitmap bitmap = centerSquareScaleBitmap(b3, 300);
//                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//                    mImageData = baos.toByteArray();
//                }


                if (isFirstSend) {
                    mHandler.sendEmptyMessageDelayed(1, 2000);
                    isFirstSend = false;
                }

//                if (isFirstSend) {
//                    try {
//                        Thread.sleep(1500);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    sendPipei();
//                    isFirstSend = false;
//                }
//                //每半秒钟刷一次图片
//                try {
//                    Thread.sleep(500);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
                Log.e(TAG, "onPreviewFrame: 预览刷新");
            }
        });

        try {
            mCamera.setPreviewDisplay(mPreviewSurface.getHolder());
            mCamera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class GetImageTask extends AsyncTask<Void, Void, Void> {
        private byte[] mData;

        public GetImageTask(byte[] data){
            mData = data;
        }

        @Override
        protected Void doInBackground(Void... voids) {
//            mHandler.sendEmptyMessageDelayed(2, 500);
            if (mData == null){
                return null;
            }
            System.arraycopy(mData, 0, nv21, 0, mData.length);
            b3 = decodeToBitMap(nv21, mCamera);
            baos = new ByteArrayOutputStream();
            if (b3 != null) {
                Bitmap bitmap = centerSquareScaleBitmap(b3, 300);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                mImageData = baos.toByteArray();
            }
            mHandler.sendEmptyMessage(2);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mHandler.sendEmptyMessage(2);
        }
    }


    private boolean isFirstSend = true;

    private void sendPipei() {
        if (mImageData != null) {
            if ("Test".equals(fromString) || "Welcome".equals(fromString)) {//标识从MainActivity过来
                if (xfids != null && xfids.length > 0 && indexXfid < xfids.length) {//把数组中第一个头像发送过去
                    mFaceRequest.setParameter(SpeechConstant.AUTH_ID, xfids[indexXfid]);
                    mFaceRequest.setParameter(SpeechConstant.WFR_SST, "verify");
                    mFaceRequest.sendRequest(mImageData, mRequestListener);
                    Log.e(TAG, "run: " + "此前轮询下表：" + indexXfid + "讯飞id" + xfids[indexXfid]);
                }
            } else if ("Pay".equals(fromString) && mAuthid != null) {//支付走这个分支
                mFaceRequest.setParameter(SpeechConstant.AUTH_ID, mAuthid);
                mFaceRequest.setParameter(SpeechConstant.WFR_SST, "verify");
                mFaceRequest.sendRequest(mImageData, mRequestListener);
            }
        }
    }


    private static final String TAG = "人脸验证";
    private RequestListener mRequestListener = new RequestListener() {

        @Override
        public void onEvent(int eventType, Bundle params) {
            Log.e("讯飞onEvent", "onEvent: " + eventType + "");
        }

        @Override
        public void onBufferReceived(byte[] buffer) {
            try {
                String result = new String(buffer, "utf-8");
                JSONObject object = new JSONObject(result);
                if (object.getInt("ret") != 0) {
                    finish();
                    Log.e(TAG, "讯飞出问题了");
                    return;
                }
                if (!AuthenticationActivity.this.isFinishing() && !AuthenticationActivity.this.isDestroyed())
                    if (!object.getBoolean("verf")) {//验证失败，进行下一次验证
                        if (indexXfid == xfids.length) {//标识这一遍已经轮询到最后一个元素了
                            if ("Test".equals(fromString) || "Welcome".equals(fromString)) {//测试或者是登录
                                if (xfTime > 0) {
                                    Log.e(TAG, "onBufferReceived:xfTime " + xfTime + "");
                                    xfTime--;
                                    indexXfid = 0;//重置index，再次开始轮询
                                    sendPipei();

                                } else {//10轮验证结束，验证失败
                                    speak(getString(R.string.shop_yanzheng));
                                    ToastUtil.showShort(AuthenticationActivity.this, "验证不通过");
                                    finish();
                                }
                            } else if ("Pay".equals(fromString)) {//支付
                                if (xfTime > 0) {
                                    xfTime--;//再次发起查询
                                    sendPipei();
                                } else {
                                    //10轮验证结束，验证失败取消订单
                                    NetworkApi.pay_cancel("3", "0", "1", orderid, new NetworkManager.SuccessCallback<String>() {
                                        @Override
                                        public void onSuccess(String response) {
                                            speak(getString(R.string.shop_yanzheng));
                                            ToastUtil.showShort(AuthenticationActivity.this, "验证不通过");
                                            finish();

                                        }

                                    }, new NetworkManager.FailedCallback() {
                                        @Override
                                        public void onFailed(String message) {


                                        }
                                    });
                                }
                            }

                        } else {
                            Log.e(TAG, "onBufferReceived:indexXfid" + "-------" + indexXfid + "");
                            indexXfid++;
                            sendPipei();
                        }
                    } else {//验证通过 将所有flag初始化
                        xfTime = 10;
                        Log.e(TAG, "onBufferReceived: 成功");
                        closeCamera();
                        if ("Test".equals(fromString)) {//测量验证成功
                            choosedXfid = xfids[indexXfid];
                            indexXfid = 0;
                            ToastUtil.showShort(AuthenticationActivity.this, "通过验证，欢迎回来！");
                            if (!TextUtils.isEmpty(choosedXfid)) {
                                MyApplication.getInstance().userId = xfid_userid.get(choosedXfid);
                                MyApplication.getInstance().xfid = choosedXfid;
                                sendBroadcast(new Intent("change_account"));
                            }
                            startActivity(new Intent(getApplicationContext(), Test_mainActivity.class));
                            finish();
                        } else if ("Pay".equals(fromString)) {//支付验证成功了
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

                        } else if ("Welcome".equals(fromString)) {//登录验证成功
                            choosedXfid = xfids[indexXfid];
                            indexXfid = 0;
                            ToastUtil.showShort(AuthenticationActivity.this, "通过验证，欢迎回来！");
                            boolean isInclude = false;
                            if (mDataList != null) {
                                for (int i = 0; i < mDataList.size(); i++) {
                                    UserInfoBean user = mDataList.get(i);
                                    if (user.xfid.equals(choosedXfid)) {
                                        new JpushAliasUtils(AuthenticationActivity.this).setAlias("user_" + user.bid);
                                        LocalShared.getInstance(mContext).setUserInfo(user);
                                        LocalShared.getInstance(mContext).addAccount(user.bid, user.xfid);
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
                                if (isInclude)
                                    startActivity(new Intent(AuthenticationActivity.this, MainActivity.class));
                                else {
                                    ToastUtil.showLong(AuthenticationActivity.this, "该机器人没有此账号的人脸认证信息，请手动登录");
                                    startActivity(new Intent(AuthenticationActivity.this, SignInActivity.class));
                                    finish();
                                }
                            }

                        }
                    }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onCompleted(SpeechError error) {
            //未识别到人脸走这里
            if (!AuthenticationActivity.this.isFinishing() && !AuthenticationActivity.this.isDestroyed())
                if (unDentified > 0) {
                    unDentified--;
                    if ("Test".equals(fromString) || "Welcome".equals(fromString)) {
                        if (indexXfid == xfids.length) {
                            indexXfid = 0;
                        }
                        sendPipei();
                        Log.e(TAG, "onCompleted: unDentified > 0");
                    } else {
                        indexXfid = xfids.length;
                    }
                    xfTime = 10;
                } else {
                    Log.e(TAG, "onCompleted: unDentified<0");
//                    if ("Test".equals(fromString)||"Pay".equals(fromString)) {//因为人脸登录的容错机会只有5次，很容易出现成功提示信息和错误提示信息同时出现
                        ToastUtil.showShort(AuthenticationActivity.this, "验证不通过");
//                    }
                    finish();
                }
        }
    };

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
        if (mCamera == null){
            return null;
        }
        try {
            Camera.Size size = mCamera.getParameters().getPreviewSize();
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
    protected void onPause() {
        super.onPause();
        closeCamera();
        if (null != mAcc) {
            mAcc.stop();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.pause();
            mediaPlayer = null;
        }
        if (baos != null) {
            try {
                baos.close();
                baos = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (mFaceRequest != null) {
            mFaceRequest.cancel();
        }
    }
}
