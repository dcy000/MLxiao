package com.example.han.referralproject.facerecognition;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.han.referralproject.R;
import com.example.han.referralproject.Test_mainActivity;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.activity.DetectActivity;
import com.example.han.referralproject.application.MyApplication;
import com.example.han.referralproject.bean.UserInfoBean;
import com.example.han.referralproject.homepage.MainActivity;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.example.han.referralproject.util.LocalShared;
import com.example.han.referralproject.util.Utils;
import com.example.han.referralproject.xindian.XinDianDetectActivity;
import com.gcml.lib_utils.camera.CameraUtils;
import com.gcml.lib_utils.display.ImageUtils;
import com.gcml.lib_utils.display.LoadingProgressUtils;
import com.gcml.lib_utils.display.ToastUtils;
import com.gcml.lib_utils.thread.ThreadUtils;
import com.gzq.administrator.lib_common.base.BaseApplication;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.IdentityResult;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.medlink.danbogh.signin.SignInActivity;
import com.medlink.danbogh.utils.JpushAliasUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import timber.log.Timber;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/8/8 10:01
 * created by:gzq
 * description:新的人脸识别Activity。
 * 优化点：1.优化相机操作；
 * 2.优化识别业务流程；
 * 3.解决点击跳过或者返回按钮会卡顿的问题
 */
public class FaceRecognitionActivity extends BaseActivity implements View.OnClickListener,
        CameraUtils.CameraPreviewCallBack, SynthesizerListener, IVertifyFaceListener {

    private SurfaceView mSurfaceview;
    private LottieAnimationView mLottAnimation;
    private ImageView mIvCircle;
    private ImageView mIvBack;
    private Button mTiaoGuo;
    private TextView mTvTip;
    private ImageView mPreImg;
    private String fromWhere;
    private boolean isTest;
    private String orderid;
    private String fromType;
    private Animation circleAnimation;
    private String mAuthid;
    private String groupid;
    private String currentXfid;
    private int authenticationNum;
    private String[] accounts;
    private ArrayList<UserInfoBean> mUsersInfo;
    private boolean isPhoto;

    public static void startActivity(Context context, Bundle bundle, boolean isForResult) {
        if (isForResult && bundle != null) {
            int requestCode = bundle.getInt("requestCode", 1);
            ((FragmentActivity) context).startActivityForResult(new Intent(context,
                    FaceRecognitionActivity.class).putExtras(bundle), requestCode);
        } else
            context.startActivity(new Intent(context, FaceRecognitionActivity.class)
                    .putExtras(bundle));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_recognition);
        initView();
        initParameter();
        leakCanary();
    }

    private void leakCanary() {
//        BaseApplication.getRefWatcher(this).watch(this);
    }

    private void initParameter() {
        Bundle params = getIntent().getExtras();
        if (params != null) {
            fromWhere = params.getString("from");
            isTest = params.getBoolean("isTest", false);
            orderid = params.getString("orderid");
            fromType = params.getString("fromType");
        }

        CameraUtils.getInstance().init(this, mSurfaceview,
                1920, 1200, 856, 856);
        CameraUtils.getInstance().setOnCameraPreviewCallback(this);
        circleAnimation = AnimationUtils.loadAnimation(this, R.anim.rotate_face_check);
        mAuthid = LocalShared.getInstance(this).getXunfeiId();
        groupid = LocalShared.getInstance(this).getGroupId();
        currentXfid = LocalShared.getInstance(this).getXunfeiId();
        accounts = LocalShared.getInstance(this).getAccounts();
        getUsers();
    }

    private void getUsers() {
        ThreadUtils.executeByIo(new ThreadUtils.SimpleTask<Void>() {
            @Nullable
            @Override
            public Void doInBackground() {

                if (accounts == null) {
                    return null;
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
                return null;
            }

            @Override
            public void onSuccess(@Nullable Void result) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        CameraUtils.getInstance().openCamera(Camera.CameraInfo.CAMERA_FACING_BACK);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MLVoiceSynthetize.startSynthesize(MyApplication.getInstance(), "主人，请对准摄像头",
                this, false);

    }

    @Override
    protected void onStop() {
        super.onStop();
        destroyAnimation();
        CameraUtils.getInstance().closeCamera();
        MLVoiceSynthetize.stop();
        LoadingProgressUtils.dismissView();
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
                    startActivity(new Intent(this, Test_mainActivity.class)
                            .putExtra("isTest", isTest));
                    return;
                }

                if ("Test".equals(fromWhere)) {
                    Intent intent = new Intent();
                    if (TextUtils.isEmpty(fromType)) {
                        intent.setClass(this, Test_mainActivity.class);
                    } else if ("xindian".equals(fromType)) {
                        intent.setClass(this, XinDianDetectActivity.class);
                    } else {
                        intent.setClass(this, DetectActivity.class);
                        intent.putExtra("type", fromType);
                    }
                    startActivity(intent);
                } else if ("Welcome".equals(fromWhere)) {
                    startActivity(new Intent(this, SignInActivity.class));
                }
                finish();
                break;
        }
    }

    private void initView() {
        mSurfaceview = findViewById(R.id.surfaceview);
        mLottAnimation = findViewById(R.id.lott_animation);
        mIvCircle = findViewById(R.id.iv_circle);
        mIvBack = findViewById(R.id.iv_back);
        mIvBack.setOnClickListener(this);
        mTiaoGuo = findViewById(R.id.tiao_guo);
        mTiaoGuo.setOnClickListener(this);
        mTvTip = findViewById(R.id.tv_tip);
        mPreImg = findViewById(R.id.pre_img);
    }

    //==================================摄像头操作监听=============================================
    @Override
    public void onStartPreview() {
        mIvCircle.startAnimation(circleAnimation);
    }

    @Override
    public void onStartResolveImage() {
        if (isPhoto)
            LoadingProgressUtils.showViewWithLabel(this, "正在解析头像信息");
    }

    @Override
    public void previewSuccess(byte[] datas, Bitmap preBitmap, Bitmap cropBitmap, int prewidth,
                               int preheight, int cropwidth, int cropheight) {
        if (isPhoto && !isFinishing()) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    LoadingProgressUtils.showViewWithLabel(FaceRecognitionActivity.this,
                            "正在头像库中搜索");
                    mTvTip.setText("努力验证中...");
                    closeAnimation();

                }
            });
            Timber.e("正在进行比对");
            FaceAuthenticationUtils.getInstance(this).verificationFace(
                    ImageUtils.bitmap2Bytes(cropBitmap, Bitmap.CompressFormat.JPEG), groupid);
            FaceAuthenticationUtils.getInstance(this).setOnVertifyFaceListener(this);
        }
    }

    @Override
    public void openCameraFail(Exception e) {
        ToastUtils.showShort("打开摄像头失败");
        closeAnimation();
    }

    @Override
    public void cameraClosed() {
        Timber.i("摄像头正在关闭");
        closeAnimation();
    }

    //================================语音播报监听================================================
    @Override
    public void onSpeakBegin() {

    }

    @Override
    public void onBufferProgress(int i, int i1, int i2, String s) {

    }

    @Override
    public void onSpeakPaused() {

    }

    @Override
    public void onSpeakResumed() {

    }

    @Override
    public void onSpeakProgress(int i, int i1, int i2) {

    }

    @Override
    public void onCompleted(SpeechError speechError) {

        retryPreviewHead(1000);
    }

    private void openAnimation() {
        mLottAnimation.playAnimation();
    }

    private void closeAnimation() {
        mIvCircle.clearAnimation();
        mLottAnimation.reverseAnimation();
    }
    private void destroyAnimation(){
        mIvCircle.clearAnimation();
        mLottAnimation.cancelAnimation();
    }
    //==========================头像识别监听======================================================
    @Override
    public void onResult(IdentityResult result, boolean islast) {
        Timber.e("线程：" + Thread.currentThread().getName());
        LoadingProgressUtils.dismissView();
        try {
            String resultStr = result.getResultString();
            JSONObject resultJson = new JSONObject(resultStr);
            if (ErrorCode.SUCCESS == resultJson.getInt("ret")) {//此处检验百分比
                JSONArray scoreList = resultJson.getJSONObject("ifv_result").getJSONArray("candidates");
                Timber.d(scoreList.toString());
                String scoreFirstXfid = scoreList.getJSONObject(0).optString("user");
                Timber.d("最高分数的讯飞id" + scoreFirstXfid);
                final double firstScore = scoreList.getJSONObject(0).optDouble("score");
                if (firstScore > 80) {
                    if ("Test".equals(fromWhere) || "Welcome".equals(fromWhere)) {
                        Timber.e("验证分数》80的讯飞id" + scoreFirstXfid);
                        authenticationSuccessForTest(scoreFirstXfid);
                    } else if ("Pay".equals(fromWhere)) {
                        dealPayResult(scoreFirstXfid);
                    }

                } else {
                    if (firstScore > 30) {
                        authenticationNum = 0;
                        ToastUtils.showShort("请将您的面孔靠近摄像头，再试一次");
                        retryPreviewHead(3000);
                    } else {
                        Timber.e("相似度低于30");
                        ToastUtils.showShort("验证未通过，相似度" + String.format("%.2f", firstScore) + "%");
                        recognitionFail();
                    }
                }
            } else {
                Timber.e("验证失败：ErrorCode==Error");
                ToastUtils.showShort("识别失败");
                recognitionFail();
            }
        } catch (JSONException e) {
            Timber.e("JSONException e");
            recognitionFail();
        }
    }

    @Override
    public void onEvent(int i, int i1, int i2, Bundle bundle) {

    }

    @Override
    public void onError(SpeechError error) {
        Timber.e("线程：" + Thread.currentThread().getName());
        Timber.e(error.getPlainDescription(true));
        LoadingProgressUtils.dismissView();
        if (authenticationNum < 5) {
            authenticationNum++;
            ToastUtils.showShort("第" + Utils.getChineseNumber(authenticationNum) + "次验证失败");
            retryPreviewHead(3000);
        } else {
            Timber.e("验证次数超过5次");
            recognitionFail();
        }
    }

    private void dealPayResult(String scoreFirstXfid) {
        if (mAuthid.equals(scoreFirstXfid)) {
            NetworkApi.pay_status(MyApplication.getInstance().userId, Utils.getDeviceId(), orderid,
                    new NetworkManager.SuccessCallback<String>() {
                        @Override
                        public void onSuccess(String response) {
                            recognitionSuccess(MyApplication.getInstance().userId);
                        }

                    }, new NetworkManager.FailedCallback() {
                        @Override
                        public void onFailed(String message) {
                            Timber.e("支付同步订单失败");
                            recognitionFail();
                        }
                    });
        } else {
            NetworkApi.pay_cancel("3", "0", "1", orderid,
                    new NetworkManager.SuccessCallback<String>() {
                        @Override
                        public void onSuccess(String response) {
                            Timber.e("非本人支付");
                            recognitionFail();
                        }

                    }, new NetworkManager.FailedCallback() {
                        @Override
                        public void onFailed(String message) {

                        }
                    });
        }
    }

    private void authenticationSuccessForTest(String scoreFirstXfid) {
        if (mUsersInfo != null) {
            boolean isInclude_PassPerson = false;
            String userId = null;
//            Timber.e("人数："+mUsersInfo.size());
            for (int i = 0; i < mUsersInfo.size(); i++) {
                UserInfoBean user = mUsersInfo.get(i);
//                Timber.e("讯飞id："+user.xfid);
                if (user.xfid.equals(scoreFirstXfid)) {
//                    Timber.e("识别到的讯飞id" + user.xfid + "++++识别到的人" + user.bname);
                    userId = user.bid;
                    LocalShared.getInstance(this).setUserInfo(user);
                    LocalShared.getInstance(this).setSex(user.sex);
                    LocalShared.getInstance(this).setUserPhoto(user.userPhoto);
                    LocalShared.getInstance(this).setUserAge(user.age);
                    LocalShared.getInstance(this).setUserHeight(user.height);
                    isInclude_PassPerson = true;
                    break;
                } else {
                    isInclude_PassPerson = false;
                }
            }
            if (isInclude_PassPerson) {
                recognitionSuccess(userId);
            } else {
                Timber.e("不包含这个人");
                recognitionFail();
            }
        }
    }

    /**
     * 重新预览
     */
    private void retryPreviewHead(int time) {
        openAnimation();
        isPhoto = true;
        CameraUtils.getInstance().restartPreview(time);
    }

    /**
     * 验证失败
     */
    private void recognitionFail() {
        if ("Welcome".equals(fromWhere)) {
            ToastUtils.showLong("该机器人没有此账号的人脸认证信息，请手动登录");
            startActivity(new Intent(this, SignInActivity.class));
        } else if ("Test".equals(fromWhere) || "Pay".equals(fromType)) {
            ToastUtils.showShort("验证未通过");
//            MLVoiceSynthetize.startSynthesize(this, getString(R.string.shop_yanzheng), false);
        }
        finish();
    }

    /**
     * 验证成功
     *
     * @param userId
     */
    private void recognitionSuccess(String userId) {

        ToastUtils.showShort("通过验证");
        if ("Welcome".equals(fromWhere)) {
            startActivity(new Intent(this, MainActivity.class));
        } else if ("Test".equals(fromWhere)) {
            new JpushAliasUtils(this).setAlias("user_" + userId);
            Intent intent = new Intent();
            if (TextUtils.isEmpty(fromType)) {
                intent.setClass(this, Test_mainActivity.class);
            } else if ("xindian".equals(fromType)) {
                intent.setClass(this, XinDianDetectActivity.class);
            } else {
                intent.setClass(this, DetectActivity.class);
                intent.putExtra("type", fromType);
            }
            startActivity(intent);
        } else if ("Pay".equals(fromType)) {
            setResult(RESULT_OK);
        }
        finish();
    }
}
