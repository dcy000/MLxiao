package com.gcml.module_face_recognition;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.gcml.common.utils.RxUtils;
import com.gcml.lib_utils.camera.CameraUtils;
import com.gcml.lib_utils.data.TimeUtils;
import com.gcml.lib_utils.display.ImageUtils;
import com.gcml.lib_utils.display.LoadingProgressUtils;
import com.gcml.lib_utils.display.ToastUtils;
import com.gcml.lib_utils.ui.dialog.DialogSureCancel;
import com.gcml.lib_widget.CircleImageView;
import com.gcml.module_face_recognition.faceutils.FaceAuthenticationUtils;
import com.gcml.module_face_recognition.faceutils.ICreateGroupListener;
import com.gcml.module_face_recognition.faceutils.IJoinGroupListener;
import com.gcml.module_face_recognition.faceutils.IRegisterFaceListener;
import com.gcml.module_face_recognition.manifests.SPManifest;
import com.gcml.module_face_recognition.network.FaceRepository;
import com.iflytek.cloud.IdentityResult;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DefaultObserver;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class RegisterHead2XunfeiActivity extends AppCompatActivity implements View.OnClickListener {
    private SurfaceView mSfvPreview;
    private LottieAnimationView mLottAnimation;
    private TextView mTvTip;
    private ImageView mIvCircle;
    private ImageView mIvBack;
    private Button mTiaoGuos;
    private ImageView mPreImg;
    private boolean isPhoto = false;
    private DialogSureCancel dialogSureCancel;
    private byte[] compressImage;
    private CircleImageView headImg;
    private Activity mContext;
    private String qiniuToken;
    private String headPostfix;
    private String userId;
    private String xfid;
    private String groupId;
    private String firstXfidOfGroup;
    private static final String KEY_EXTRA_XFID = "key_xfid";
    private static final String KEY_EXTRA_CC_ID = "key_cc_id";
    private static final String KEY_EXTRA_CC_CALLBACK = "key_cc_callback";
    private String ccId;

    public static void startActivity(Context context, String xfid, String ccId) {
        Intent intent = new Intent(context, RegisterHead2XunfeiActivity.class)
                .putExtra(KEY_EXTRA_XFID, xfid)
                .putExtra(KEY_EXTRA_CC_ID, ccId);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.face_recognition_activity_scan_face);
        mContext = this;
        initView();
        initHeadDialog();
        getQiniuToken();
        CameraUtils.getInstance().init(this, mSfvPreview, 1920, 1200, 856, 856);
        CameraUtils.getInstance().setOnCameraPreviewCallback(preCallBack);
    }

    private CameraUtils.CameraPreviewCallBack preCallBack = new CameraUtils.CameraPreviewCallBack() {
        @Override
        public void onStartPreview() {

        }

        @Override
        public void onStartResolveImage() {
            if (isPhoto) {
                LoadingProgressUtils.showViewWithLabel(RegisterHead2XunfeiActivity.this, "正在解析头像");
            }
        }

        @Override
        public void previewSuccess(byte[] datas, final Bitmap preBitmap, final Bitmap cropBitmap, int prewidth, int preheight, int cropwidth, int cropheight) {
            if (isPhoto) {
                LoadingProgressUtils.dismissView();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        headImg.setImageBitmap(cropBitmap);
                        if (dialogSureCancel != null) {
                            dialogSureCancel.show();
                        }
                    }
                });
                compressImage = ImageUtils.bitmap2Bytes(cropBitmap, Bitmap.CompressFormat.JPEG);
            }

        }

        @Override
        public void openCameraFail(Exception e) {
            Timber.e("启动设备失败");
        }

        @Override
        public void cameraClosed() {
            Timber.e("相机关闭");
        }
    };

    private void initHeadDialog() {
        dialogSureCancel = new DialogSureCancel(this);
        View inflate = LayoutInflater.from(this).inflate(R.layout.face_recognition_dialog_affirm_head, null);
        headImg = inflate.findViewById(R.id.iv_head);
        TextView sureTv = inflate.findViewById(R.id.confirm);
        TextView cancelTv = inflate.findViewById(R.id.cancel);
        dialogSureCancel.setContentView(inflate);
        dialogSureCancel.getWindow().setDimAmount(0);
        dialogSureCancel.setCanceledOnTouchOutside(false);
        sureTv.setOnClickListener(
                v -> {
                    if (compressImage != null) {
                        registerHead();
                        dialogSureCancel.dismiss();
                        LoadingProgressUtils.showViewWithLabel(mContext, "正在保存头像");
                    }
                });
        cancelTv.setOnClickListener(
                v -> {
                    //重新开始拍摄，3s之后重新开始预览
                    dialogSureCancel.dismiss();
                    CameraUtils.getInstance().restartPreview(3000);
                });
    }

    private void initView() {
        mSfvPreview = findViewById(R.id.sfv_preview);
        mLottAnimation = findViewById(R.id.lott_animation);
        mTvTip = findViewById(R.id.tv_tip);
        mIvCircle = findViewById(R.id.iv_circle);
        mIvBack = findViewById(R.id.iv_back);
        mIvBack.setOnClickListener(this);
        mTiaoGuos = findViewById(R.id.tiao_guos);
        mTiaoGuos.setOnClickListener(this);
        mPreImg = findViewById(R.id.pre_img);
        mLottAnimation.setImageAssetsFolder("lav_imgs/");
        mLottAnimation.setAnimation("camera_pre.json");
        mLottAnimation.playAnimation();

        userId = SPManifest.getUserId();
        xfid = getIntent().getStringExtra(KEY_EXTRA_XFID);
        ccId = getIntent().getStringExtra(KEY_EXTRA_CC_ID);
        groupId = SPManifest.getGroupId();
        firstXfidOfGroup = SPManifest.getGroupFirstXfid();
        //保存到七牛云上头像的名称
        headPostfix = TimeUtils.getCurTimeString(
                new SimpleDateFormat("yyyyMMddHHmmss")) + "_" + userId + ".jpg";
    }


    @Override
    protected void onStart() {
        super.onStart();
        CameraUtils.getInstance().openCamera(Camera.CameraInfo.CAMERA_FACING_BACK);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MLVoiceSynthetize.startSynthesize(this, "主人，让我给你拍个照。三，二，一，茄子", new SynthesizerListener() {
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
                //话说完了之后的操作
                isPhoto = true;
                CameraUtils.getInstance().restartPreview(0);
            }

            @Override
            public void onEvent(int i, int i1, int i2, Bundle bundle) {

            }
        }, false);

    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.iv_back) {
            //因为CC启动可能是异步的，所以此处给个反馈
            onCCAction("pressedBackButton");
            finish();
        } else if (i == R.id.tiao_guos) {
            onCCAction("pressedJumpButton");
            finish();
        } else {

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        LoadingProgressUtils.dismissView();
        CameraUtils.getInstance().closeCamera();
        MLVoiceSynthetize.stop();
        FaceAuthenticationUtils.getInstance(this).cancelIdentityVerifier();
    }


    //在讯飞平台注册头像
    private void registerHead() {
        FaceAuthenticationUtils.getInstance(mContext).setOnRegisterListener(new IRegisterFaceListener() {
            @Override
            public void onResult(IdentityResult result, boolean islast) {
                checkGroup(userId, xfid);
            }

            @Override
            public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {

            }

            @Override
            public void onError(SpeechError error) {
                ToastUtils.showShort(error.getPlainDescription(true));
                LoadingProgressUtils.dismissView();
                finish();
            }
        });
        FaceAuthenticationUtils.getInstance(mContext).registerFace(compressImage, xfid);
    }

    //检查组
    private void checkGroup(final String userid, final String xfid) {
        //在登录的时候判断该台机器有没有创建人脸识别组，如果没有则创建
        if (!TextUtils.isEmpty(groupId) && !TextUtils.isEmpty(firstXfidOfGroup)) {
            joinGroup(userid, groupId, xfid);
        } else {
            createGroup(userid, xfid);
        }
    }

    //创建组
    private void createGroup(final String userid, final String xfid) {

        FaceAuthenticationUtils.getInstance(mContext).setOnCreateGroupListener(new ICreateGroupListener() {
            @Override
            public void onResult(IdentityResult result, boolean islast) {
                Timber.e("创建组成功" + result);
                try {
                    JSONObject resObj = new JSONObject(result.getResultString());
                    String groupId = resObj.getString("group_id");
                    SPManifest.setGroupId(groupId);
                    SPManifest.setGroupFirstXfid(xfid);
                    //组创建好以后把自己加入到组中去
                    joinGroup(userid, groupId, xfid);
                    //加组完成以后把头像上传到我们自己的服务器
                    uploadHeadToSelf(userid, xfid);
                    FaceAuthenticationUtils.getInstance(mContext).updateGroupInformation(groupId, xfid);

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
                //如果在此处创建组失败就跳过创建
                uploadHeadToSelf(userid, xfid);
            }
        });
        FaceAuthenticationUtils.getInstance(mContext).createGroup(xfid);
    }

    //加组
    private void joinGroup(final String userid, String groupid, final String xfid) {
        FaceAuthenticationUtils.getInstance(mContext).setOnJoinGroupListener(new IJoinGroupListener() {
            @Override
            public void onResult(IdentityResult result, boolean islast) {
                uploadHeadToSelf(userid, xfid);
            }

            @Override
            public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {

            }

            @Override
            public void onError(SpeechError error) {
                Timber.e(error, "添加成员出现异常");
                if (error.getErrorCode() == 10143 || error.getErrorCode() == 10106) {
                    //该组不存在;无效的参数
                    createGroup(userid, xfid);
                } else {
                    uploadHeadToSelf(userid, xfid);
                }

            }
        });
        FaceAuthenticationUtils.getInstance(this).joinGroup(groupid, xfid);
    }

    //获取七牛云的token
    @SuppressLint("CheckResult")
    private void getQiniuToken() {
        FaceRepository.getQiniuToken()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribeWith(new DefaultObserver<String>() {
                    @Override
                    public void onNext(String s) {
                        Timber.e("Retrofit:TOKEN-->" + s);
                        qiniuToken = s;
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    //将头像图片上传七牛云 再把地址绑定到个人用户信息中
    private void uploadHeadToSelf(final String userid, final String xfid) {
        new UploadManager().put(compressImage, headPostfix, qiniuToken, new UpCompletionHandler() {
            @SuppressLint("CheckResult")
            @Override
            public void complete(String key, ResponseInfo info, JSONObject res) {
                if (info.isOK()) {
                    String imageUrl = "http://oyptcv2pb.bkt.clouddn.com/" + key;
                    if (TextUtils.isEmpty(userid)) {
                        ToastUtils.showShort("userId==null");
                        LoadingProgressUtils.dismissView();
                        onCCAction("onError");
                        return;
                    }
                    FaceRepository.syncRegistHeadUrl(imageUrl)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .doOnNext(new Consumer<Object>() {
                                @Override
                                public void accept(Object o) throws Exception {
                                    //将账号在本地缓存
                                    SPManifest.addAccount(userid, xfid);
                                }
                            })
                            .as(RxUtils.autoDisposeConverter(RegisterHead2XunfeiActivity.this))
                            .subscribeWith(new DefaultObserver<Object>() {
                                @Override
                                public void onNext(Object o) {
                                    //隐藏提示loadding
                                    LoadingProgressUtils.dismissView();
                                    onCCAction("RegistHeadSuccess");
                                }

                                @Override
                                public void onError(Throwable e) {
                                    ToastUtils.showShort("save to our server fail");
                                }

                                @Override
                                public void onComplete() {

                                }
                            });
                } else {
                    ToastUtils.showShort("upload head image fail");
                }
            }
        }, null);

    }

    /**
     * CC框架结果反馈
     *
     * @param action
     */
    private void onCCAction(String action) {
        CC.sendCCResult(ccId, CCResult.success(KEY_EXTRA_CC_CALLBACK, action));
    }
}
