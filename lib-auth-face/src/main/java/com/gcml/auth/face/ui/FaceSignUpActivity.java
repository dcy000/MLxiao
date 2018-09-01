package com.gcml.auth.face.ui;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.gcml.auth.face.BR;
import com.gcml.auth.face.R;
import com.gcml.auth.face.databinding.AuthActivityFaceSignUpBinding;
import com.gcml.auth.face.model.PreviewHelper;
import com.gcml.common.data.UserSpHelper;
import com.gcml.common.mvvm.BaseActivity;
import com.gcml.common.repository.utils.DefaultObserver;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.widget.dialog.IconDialog;
import com.gcml.lib_utils.display.ToastUtils;
import com.iflytek.cloud.SpeechError;
import com.iflytek.synthetize.MLSynthesizerListener;
import com.iflytek.synthetize.MLVoiceSynthetize;

import java.io.ByteArrayOutputStream;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class FaceSignUpActivity extends BaseActivity<AuthActivityFaceSignUpBinding, FaceSignUpViewModel> {

    private PreviewHelper mPreviewHelper;
    private String faceId;

    @Override
    protected int layoutId() {
        return R.layout.auth_activity_face_sign_up;
    }

    @Override
    protected int variableId() {
        return BR.viewModel;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        faceId = getIntent().getStringExtra("faceId");
        if (TextUtils.isEmpty(faceId)) {
            faceId = UserSpHelper.produceFaceId();
        }
        callId = getIntent().getStringExtra("callId");
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
                .doOnNext(new Consumer<PreviewHelper.Status>() {
                    @Override
                    public void accept(PreviewHelper.Status status) throws Exception {
                        onPreviewStatus(status);
                    }
                })
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe();
    }

    private void start(int delayMillis) {
        binding.ivTips.setText("请把人脸放在框内");
        MLVoiceSynthetize.startSynthesize(
                getApplicationContext(),
                "请把人脸放在框内 。三，二，一",
                new MLSynthesizerListener() {
                    @Override
                    public void onCompleted(SpeechError speechError) {
                        mPreviewHelper.addBuffer(delayMillis);
                        // see onPreviewStatus(PreviewHelper.Status status)
                    }
                },
                false
        );
    }

    private void onPreviewStatus(PreviewHelper.Status status) {
        if (status.code == PreviewHelper.Status.EVENT_CROPPED) {
            Bitmap faceBitmap = (Bitmap) status.payload;
            showFace(faceBitmap);
        } else if (status.code == PreviewHelper.Status.ERROR_ON_OPEN_CAMERA) {
            ToastUtils.showShort("打开相机失败");
        }
    }

    private void showFace(Bitmap faceBitmap) {
        new IconDialog(this).builder()
                .setIcon(faceBitmap)
                .setPositiveButton("重拍", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        start(3000);
                    }
                })
                .setNegativeButton("确认头像", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        signUpFace(faceBitmap);
                    }
                }).show();
    }

    private void signUpFace(Bitmap faceBitmap) {
        String userId = UserSpHelper.getUserId();
        if (TextUtils.isEmpty(userId)) {
            ToastUtils.showShort("请先登录！");
            finish();
        }

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
//                        String faceId = UserSpHelper.getFaceId();
                        return viewModel.signUp(faceData, faceId)
                                .subscribeOn(Schedulers.io());
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        binding.ivTips.setText("人脸录入中");
                    }
                })
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new DefaultObserver<String>() {
                    @Override
                    public void onNext(String s) {
                        binding.ivTips.setText("人脸录入成功");
                        error = false;
                        finish();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        error = true;
                        start(3000);
                    }
                });

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
        start(3000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MLVoiceSynthetize.stop();
    }

    @Override
    public void finish() {
        if (!TextUtils.isEmpty(callId)) {
            CCResult result;
            if (error) {
                result = CCResult.error("人脸录入失败");
            } else {
                result = CCResult.success("faceId", UserSpHelper.getFaceId());
            }
            //为确保不管登录成功与否都会调用CC.sendCCResult，在onDestroy方法中调用
            CC.sendCCResult(callId, result);
        }
        super.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    private String callId;
    private volatile boolean error = true;
}
