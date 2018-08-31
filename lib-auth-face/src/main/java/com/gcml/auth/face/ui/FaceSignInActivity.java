package com.gcml.auth.face.ui;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.gcml.auth.face.BR;
import com.gcml.auth.face.R;
import com.gcml.auth.face.databinding.AuthActivityFaceSignInBinding;
import com.gcml.auth.face.model.PreviewHelper;
import com.gcml.common.data.UserEntity;
import com.gcml.common.data.UserSpHelper;
import com.gcml.common.mvvm.BaseActivity;
import com.gcml.common.repository.utils.DefaultObserver;
import com.gcml.common.utils.RxUtils;
import com.gcml.lib_utils.display.ToastUtils;
import com.iflytek.cloud.SpeechError;
import com.iflytek.synthetize.MLSynthesizerListener;
import com.iflytek.synthetize.MLVoiceSynthetize;

import java.io.ByteArrayOutputStream;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class FaceSignInActivity extends BaseActivity<AuthActivityFaceSignInBinding, FaceSignInViewModel> {

    private PreviewHelper mPreviewHelper;
    private Animation mAnimation;

    @Override
    protected int layoutId() {
        return R.layout.auth_activity_face_sign_in;
    }

    @Override
    protected int variableId() {
        return BR.viewModel;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        callId = getIntent().getStringExtra("callId");
        componentName = getIntent().getStringExtra("componentName");
        binding.setPresenter(this);
        RxUtils.rxWifiLevel(getApplication(), 4)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        binding.ivWifiState.setImageLevel(integer);
                    }
                });
        mPreviewHelper = new PreviewHelper(this);
        mPreviewHelper.setSurfaceHolder(binding.svPreview.getHolder());
        mPreviewHelper.setPreviewView(binding.svPreview);
        mAnimation = AnimationUtils.loadAnimation(
                getApplicationContext(), R.anim.auth_anim_face_rotate);
        binding.ivAnimation.setAnimation(mAnimation);
        binding.ivAnimation.post(new Runnable() {
            @Override
            public void run() {
                int[] outLocation = new int[2];
                Timber.i("%s x %s", outLocation[0], outLocation[1]);
                binding.ivAnimation.getLocationOnScreen(outLocation);
                mPreviewHelper.setCropRect(new Rect(
                        outLocation[0],
                        outLocation[1],
                        outLocation[0] + binding.ivAnimation.getWidth(),
                        outLocation[1] + binding.ivAnimation.getHeight()
                ));
            }
        });
        mPreviewHelper.rxStatus()
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new DefaultObserver<PreviewHelper.Status>() {
                    @Override
                    public void onNext(PreviewHelper.Status status) {
                        onPreviewStatus(status);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });
    }

    private void start(String tips, String voiceTips, int delayMillis) {
        binding.ivTips.setText(tips);
        MLVoiceSynthetize.startSynthesize(
                getApplicationContext(),
                voiceTips,
                new MLSynthesizerListener() {
                    @Override
                    public void onCompleted(SpeechError speechError) {
                        mPreviewHelper.addBuffer(delayMillis);
                        // see onPreviewStatus(PreviewHelper.Status status)
                    }
                },
                true
        );
    }

    private void onPreviewStatus(PreviewHelper.Status status) {
        if (status.code == PreviewHelper.Status.EVENT_CROPPED) {
            Bitmap faceBitmap = (Bitmap) status.payload;
            signInFace(faceBitmap);
        } else if (status.code == PreviewHelper.Status.ERROR_ON_OPEN_CAMERA) {
            ToastUtils.showShort("打开相机失败");
        }
    }

    private void signInFace(Bitmap faceBitmap) {
//        String userId = UserSpHelper.getUserId();
//        if (TextUtils.isEmpty(userId)) {
//            ToastUtils.showShort("请先登录！");
//            finish();
//        }

        Observable.just(faceBitmap)
                .map(new Function<Bitmap, byte[]>() {
                    @Override
                    public byte[] apply(Bitmap bitmap) throws Exception {
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        faceBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        if (!faceBitmap.isRecycled()) {
                            faceBitmap.recycle();
                        }
                        return baos.toByteArray();
                    }
                })
                .flatMap(new Function<byte[], ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(byte[] faceData) throws Exception {
                        String groupId = UserSpHelper.getGroupId();
                        return viewModel.signIn(faceData, groupId)
                                .subscribeOn(Schedulers.io());
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        binding.ivAnimation.startAnimation(mAnimation);
                        binding.ivTips.setText("人脸识别中");
                    }
                })
                .doOnTerminate(new Action() {
                    @Override
                    public void run() throws Exception {
                        binding.ivAnimation.clearAnimation();
                    }
                })
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new DefaultObserver<String>() {
                    @Override
                    public void onNext(String faceIdWithSore) {
                        binding.ivAnimation.clearAnimation();
                        String[] faceIdAndScore = faceIdWithSore.split(":");
                        String faceId = faceIdAndScore[0];
                        float score = Float.parseFloat(faceIdAndScore[1]);
                        processFaceIdAndScore(faceId, score);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        start("请把人脸放在框内",
                                "请把人脸放在框内 。三，二，一",
                                3000);
                    }
                });

    }

    private String theFaceId = "";
    private boolean currentUser;


    private void processFaceIdAndScore(String faceId, float score) {
        if (score < 30) {
            // 验证不通过
            finish();
        } else if (score < 80) {
            // 重新验证
            start("请把人脸靠近一点",
                    "请把人脸靠近一点",
                    3000);
        } else {
            // 当前组存在人脸
            viewModel.getLocalUsers()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .as(RxUtils.autoDisposeConverter(this))
                    .subscribe(new DefaultObserver<List<UserEntity>>() {
                        @Override
                        public void onNext(List<UserEntity> users) {
                            if (users.isEmpty()) {
                                finish();
                                return;
                            }
                            for (UserEntity user : users) {
                                if (user != null) {
                                    if (!TextUtils.isEmpty(user.xfid) && user.xfid.equals(faceId)) {
                                        theFaceId = faceId;
                                        if (theFaceId.equals(UserSpHelper.getFaceId())) {
                                            currentUser = true;
                                        }
                                        error = false;
                                        finish();
                                        return;
                                    }
                                }
                            }
                            finish();
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            super.onError(throwable);
                            finish();
                        }
                    });
        }

    }

    public void goBack() {
        finish();
    }

    public void goWifi() {
        CC.obtainBuilder("com.gcml.old.wifi").build().callAsync();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (mPreviewHelper != null) {
            mPreviewHelper.configCamera();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        start("请把人脸放在框内",
                "请把人脸放在框内 。三，二，一",
                3000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        binding.ivAnimation.clearAnimation();
        MLVoiceSynthetize.stop();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (TextUtils.isEmpty(componentName) && !TextUtils.isEmpty(callId)) {
            CCResult result;
            if (error) {
                result = CCResult.error("faceId", theFaceId);
                result.addData("currentUser", currentUser);
            } else {
                result = CCResult.success("faceId", theFaceId);
                result.addData("currentUser", currentUser);
            }
            //为确保不管登录成功与否都会调用CC.sendCCResult，在onDestroy方法中调用
            CC.sendCCResult(callId, result);
        }
    }

    @Override
    public void finish() {
        if (!TextUtils.isEmpty(componentName)) {
            if (!error) {
                CC.obtainBuilder(componentName)
                        .build()
                        .call();
                CC.sendCCResult(callId, CCResult.success());
            } else {
                CC.sendCCResult(callId, CCResult.error(""));
            }
        }
        super.finish();
    }

    private String componentName;
    private String callId;
    private volatile boolean error = true;
}
