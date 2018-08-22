package com.gcml.module_face_recognition;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.gcml.common.utils.RxUtils;
import com.gcml.lib_utils.UtilsManager;
import com.gcml.lib_utils.camera.CameraUtils;
import com.gcml.lib_utils.data.DataUtils;
import com.gcml.lib_utils.device.DeviceUtils;
import com.gcml.lib_utils.display.ImageUtils;
import com.gcml.lib_utils.display.LoadingProgressUtils;
import com.gcml.lib_utils.display.ToastUtils;
import com.gcml.module_face_recognition.bean.UserInfoBean;
import com.gcml.module_face_recognition.cc.CCResultActions;
import com.gcml.module_face_recognition.faceutils.FaceAuthenticationUtils;
import com.gcml.module_face_recognition.faceutils.IVertifyFaceListener;
import com.gcml.module_face_recognition.manifests.FaceRecognitionSPManifest;
import com.gcml.module_face_recognition.network.FaceRepository;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.IdentityResult;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.synthetize.MLVoiceSynthetize;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DefaultObserver;
import io.reactivex.schedulers.Schedulers;
import pub.devrel.easypermissions.EasyPermissions;
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
public class FaceRecognitionActivity extends AppCompatActivity implements View.OnClickListener, EasyPermissions.PermissionCallbacks {

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
    private List<UserInfoBean> mUsersInfo;
    private boolean isPhoto;
    private static final int REQUEST_CAMERA_PERMISSION = 1001;
    private FaceRecognitionActivity mContext;
    private MyCameraListener cameraListener;
    private FaceListener faceListener;
    private VoiceListener voiceListener;

    public interface SendResultActionNames {
        /**
         * 点击了返回按钮
         */
        String PRESSED_BACK_BUTTON = "pressedBackButton";
        /**
         * 点击了跳过按钮
         */
        String PRESSED_JUMP_BUTTON = "pressedJumpButton";
        /**
         * 出错了
         */
        String ON_ERROR = "onError";
        /**
         * 用户拒绝了摄像头的权限
         */
        String USER_REFUSED_CAMERA_PERMISSION = "userRefusedCameraPermission";
        /**
         * 头像验证成功
         */
        String FACE_RECOGNITION_SUCCESS = "faceRecognitionSuccess";
    }

    public static void startActivity(Context context, Bundle bundle) {
        Intent intent = new Intent(context, FaceRecognitionActivity.class)
                .putExtras(bundle);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.face_recognition_activity_scan_face);
        initView();
        initParameter();
        requestPermissions(this);
    }

    private void requestPermissions(Activity context) {
        if (EasyPermissions.hasPermissions(context, Manifest.permission.CAMERA)) {
            CameraUtils.getInstance().openCamera(Camera.CameraInfo.CAMERA_FACING_BACK);
        } else {
            EasyPermissions.requestPermissions(context, "为您拍照需要摄像头权限，您同意打开吗？",
                    REQUEST_CAMERA_PERMISSION, Manifest.permission.CAMERA);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
        Timber.e("onRequestPermissionsResult");
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        Timber.e("onPermissionsGranted");
        CameraUtils.getInstance().openCamera(Camera.CameraInfo.CAMERA_FACING_BACK);
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        ToastUtils.showShort("没有使用摄像头的权限");
        CCResultActions.onCCResultAction(RegisterHead2XunfeiActivity.SendResultActionNames.USER_REFUSED_CAMERA_PERMISSION);
        finish();
    }

    private void initParameter() {
        mContext = this;
        Bundle params = getIntent().getExtras();
        if (params != null) {
            fromWhere = params.getString("from");
            isTest = params.getBoolean("isTest", false);
            orderid = params.getString("orderid");
            fromType = params.getString("fromType");
        }

        CameraUtils.getInstance().init(this, mSurfaceview,
                1920, 1200, 856, 856);
        cameraListener = new MyCameraListener();
        CameraUtils.getInstance().setOnCameraPreviewCallback(cameraListener);
        circleAnimation = AnimationUtils.loadAnimation(this, R.anim.face_recognition_anim_rotate_face_check);
        mAuthid = FaceRecognitionSPManifest.getXunfeiId();
        groupid = FaceRecognitionSPManifest.getGroupId();
        currentXfid = FaceRecognitionSPManifest.getXunfeiId();
        accounts = FaceRecognitionSPManifest.getAccounts();
        getUsers();
    }

    class MyCameraListener implements CameraUtils.CameraPreviewCallBack {

        @Override
        public void onStartPreview() {
            mIvCircle.startAnimation(circleAnimation);
        }

        @Override
        public void onStartResolveImage() {
            if (isPhoto) {
                LoadingProgressUtils.showViewWithLabel(mContext,
                        "正在解析头像信息");
            }
        }

        @Override
        public void previewSuccess(byte[] datas, Bitmap preBitmap, Bitmap cropBitmap, int prewidth, int preheight, int cropwidth, int cropheight) {
            if (isPhoto && !isFinishing()) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        LoadingProgressUtils.showViewWithLabel(mContext,
                                "正在头像库中搜索");
                        mTvTip.setText("努力验证中...");
                        closeAnimation();

                    }
                });
                Timber.e("正在进行比对");
                FaceAuthenticationUtils.getInstance(mContext).verificationFace(
                        ImageUtils.bitmap2Bytes(cropBitmap, Bitmap.CompressFormat.JPEG), groupid);
                faceListener = new FaceListener();
                FaceAuthenticationUtils.getInstance(mContext).setOnVertifyFaceListener(faceListener);
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
    }

    class FaceListener implements IVertifyFaceListener {

        @Override
        public void onResult(IdentityResult result, boolean islast) {
            Timber.e("人脸识别：" + Thread.currentThread().getName());
            LoadingProgressUtils.dismissView();
            try {
                String resultStr = result.getResultString();
                JSONObject resultJson = new JSONObject(resultStr);
                if (ErrorCode.SUCCESS == resultJson.getInt("ret")) {//此处检验百分比
                    JSONArray scoreList = resultJson.getJSONObject("ifv_result").getJSONArray("candidates");
                    Timber.d(scoreList.toString());
                    String scoreFirstXfid = scoreList.getJSONObject(0).optString("user");
                    Timber.e("人脸识别" + scoreFirstXfid);
                    final double firstScore = scoreList.getJSONObject(0).optDouble("score");
                    if (firstScore > 80) {
                        if ("Test".equals(fromWhere) || "Welcome".equals(fromWhere)) {
                            Timber.e("人脸识别验证分数》80的讯飞id" + scoreFirstXfid);
                            authenticationSuccessForTest(scoreFirstXfid);
                        } else if ("Pay".equals(fromWhere)) {
                            Timber.e("人脸识别：从支付过来");
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
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {

        }

        @Override
        public void onError(SpeechError error) {
            Timber.e("线程：" + Thread.currentThread().getName());
            Timber.e(error.getPlainDescription(true));
            LoadingProgressUtils.dismissView();
            if (authenticationNum < 5) {
                authenticationNum++;
                ToastUtils.showShort("第" + DataUtils.getChineseNumber(authenticationNum) + "次验证失败");
                retryPreviewHead(3000);
            } else {
                Timber.e("验证次数超过5次");
                recognitionFail();
            }
        }
    }

    @SuppressLint("CheckResult")
    private void getUsers() {
        if (accounts == null) {
            return;
        }
        StringBuilder mAccountIdBuilder = new StringBuilder();
        for (String item : accounts) {
            mAccountIdBuilder.append(item.split(",")[0]).append(",");
        }
        FaceRepository.getAllUsersInformation(mAccountIdBuilder.substring(0, mAccountIdBuilder.length() - 1))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribeWith(new DefaultObserver<List<UserInfoBean>>() {
                    @Override
                    public void onNext(List<UserInfoBean> userInfoBeans) {
                        if (userInfoBeans == null) {
                            return;
                        }
                        mUsersInfo = userInfoBeans;
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        voiceListener = new VoiceListener();
        MLVoiceSynthetize.startSynthesize(UtilsManager.getApplication(), "主人，请对准摄像头",
                voiceListener, false);

    }

    class VoiceListener implements SynthesizerListener {
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

        @Override
        public void onEvent(int i, int i1, int i2, Bundle bundle) {

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        destroyAnimation();
        CameraUtils.getInstance().closeCamera();
        LoadingProgressUtils.dismissView();
        FaceAuthenticationUtils.getInstance(mContext).cancelIdentityVerifier();
        cameraListener = null;
        faceListener = null;
        voiceListener = null;
        mContext = null;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.iv_back) {
            CCResultActions.onCCResultAction(SendResultActionNames.PRESSED_BACK_BUTTON);
            finish();

        } else if (i == R.id.tiao_guos) {
            CCResultActions.onCCResultAction(SendResultActionNames.PRESSED_JUMP_BUTTON);
            finish();

        } else {
        }
    }

    private void initView() {
        mSurfaceview = findViewById(R.id.sfv_preview);
        mLottAnimation = findViewById(R.id.lott_animation);
        mLottAnimation.setImageAssetsFolder("lav_imgs/");
        mLottAnimation.setAnimation("camera_pre.json");
        mIvCircle = findViewById(R.id.iv_circle);
        mIvBack = findViewById(R.id.iv_back);
        mIvBack.setOnClickListener(this);
        mTiaoGuo = findViewById(R.id.tiao_guos);
        mTiaoGuo.setOnClickListener(this);
        mTvTip = findViewById(R.id.tv_tip);
        mPreImg = findViewById(R.id.pre_img);
    }

    private void openAnimation() {
        mLottAnimation.playAnimation();
    }

    private void closeAnimation() {
        mIvCircle.clearAnimation();
        mLottAnimation.reverseAnimation();
    }

    private void destroyAnimation() {
        mIvCircle.clearAnimation();
        mLottAnimation.cancelAnimation();
    }

    @SuppressLint("CheckResult")
    private void dealPayResult(String scoreFirstXfid) {
        if (mAuthid.equals(scoreFirstXfid)) {
            Timber.e("人脸识别支付识别成功");
            FaceRepository.syncPayOrderId(FaceRecognitionSPManifest.getUserId(), DeviceUtils.getIMEI(), orderid)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .as(RxUtils.autoDisposeConverter(this))
                    .subscribeWith(new DefaultObserver<String>() {
                        @Override
                        public void onNext(String s) {
                            recognitionSuccess(null);
                        }

                        @Override
                        public void onError(Throwable e) {
                            Timber.e("支付同步订单失败");
                            recognitionFail();
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        } else {
            Timber.e("人脸识别支付识别失败：原因是不是本人支付");
            FaceRepository.cancelPayOrderId("3", "0", "1", orderid)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .as(RxUtils.autoDisposeConverter(this))
                    .subscribeWith(new DefaultObserver<String>() {
                        @Override
                        public void onNext(String s) {
                            Timber.e("非本人支付");
                            recognitionFail();
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }
    }

    private void authenticationSuccessForTest(String scoreFirstXfid) {
        if (mUsersInfo != null) {
            boolean isInclude_PassPerson = false;
            UserInfoBean userInfoBean = null;
            for (int i = 0; i < mUsersInfo.size(); i++) {
                UserInfoBean user = mUsersInfo.get(i);
                if (user.xfid.equals(scoreFirstXfid)) {
                    userInfoBean = user;
                    isInclude_PassPerson = true;
                    break;
                } else {
                    isInclude_PassPerson = false;
                }
            }
            if (isInclude_PassPerson) {
                recognitionSuccess(userInfoBean);
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
        CCResultActions.onCCResultAction(SendResultActionNames.ON_ERROR);
        finish();
    }

    /**
     * 验证成功
     */
    private void recognitionSuccess(UserInfoBean userInfoBean) {
        ToastUtils.showShort("通过验证");
        CCResultActions.onCCResultAction(SendResultActionNames.FACE_RECOGNITION_SUCCESS, userInfoBean);
        finish();
    }
}
