package com.gcml.auth.face2.ui;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.gcml.auth.face2.BR;
import com.gcml.auth.face2.R;
import com.gcml.auth.face2.databinding.FaceActivityBdSignInBinding;
import com.gcml.auth.face2.model.FaceBdErrorUtils;
import com.gcml.auth.face2.model.entity.FaceBdUser;
import com.gcml.auth.face2.model.exception.FaceBdError;
import com.gcml.common.data.UserEntity;
import com.gcml.common.data.UserSpHelper;
import com.gcml.common.mvvm.BaseActivity;
import com.gcml.common.repository.utils.DefaultObserver;
import com.gcml.common.utils.PreviewHelper;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.common.utils.network.NetUitls;
import com.gcml.common.widget.dialog.IconDialog;
import com.gcml.common.widget.dialog.LoadingDialog;
import com.iflytek.synthetize.MLVoiceSynthetize;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class FaceBdSignInActivity extends BaseActivity<FaceActivityBdSignInBinding, FaceBdSignInViewModel> {


    @Override
    protected int layoutId() {
        return R.layout.face_activity_bd_sign_in;
    }

    @Override
    protected int variableId() {
        return BR.viewModel;
    }

    private PreviewHelper mPreviewHelper;
    private Animation mAnimation;

    @Override
    protected void init(Bundle savedInstanceState) {
        callId = getIntent().getStringExtra("callId");
        skip = getIntent().getBooleanExtra("skip", false);
        binding.setPresenter(this);
        binding.tvSkip.setVisibility(skip ? View.VISIBLE : View.GONE);
        mPreviewHelper = new PreviewHelper(this);
        mPreviewHelper.setSurfaceHolder(binding.svPreview.getHolder());
        mPreviewHelper.setPreviewView(binding.svPreview);
        mAnimation = AnimationUtils.loadAnimation(
                getApplicationContext(), R.anim.face_anim_rotate);
        binding.ivAnimation.setAnimation(mAnimation);
        binding.ivAnimation.post(new Runnable() {
            @Override
            public void run() {
                int[] outLocation = new int[2];
                Timber.i("Face CropRect: %s x %s", outLocation[0], outLocation[1]);
                binding.ivAnimation.getLocationOnScreen(outLocation);
                mPreviewHelper.setCropRect(new Rect(
                        outLocation[0],
                        outLocation[1],
                        outLocation[0] + binding.ivAnimation.getWidth(),
                        outLocation[1] + binding.ivAnimation.getHeight()
                ));
            }
        });
        binding.previewMask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                start();
//                takeFrames("");
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPreviewHelper.rxStatus()
                .observeOn(AndroidSchedulers.mainThread())
                .doOnDispose(new Action() {
                    @Override
                    public void run() throws Exception {
                        if (iconDialog != null) {
                            iconDialog.dismiss();
                            iconDialog = null;
                        }
                    }
                })
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new DefaultObserver<PreviewHelper.Status>() {
                    @Override
                    public void onNext(PreviewHelper.Status status) {
                        onPreviewStatusChanged(status);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });
        start();
    }

    private void start() {
        mPreviewHelper.rxFrame()
                .buffer(1)
                .map(bitmapToBase64Mapper())
                .compose(viewModel.ensureLive())
                .doOnNext(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        image = s;
                    }
                })
                .compose(viewModel.ensureSignInByFace())
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
                        image = "";
                    }
                })
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Timber.w(throwable);
                        FaceBdError wrapped = FaceBdErrorUtils.wrap(throwable);
                        String msg = FaceBdErrorUtils.getMsg(wrapped.getCode());
                        binding.ivTips.setText(msg);
                        error = true;
                        start();
                        takeFrames(msg);
                    }
                })
                .doOnComplete(new Action() {
                    @Override
                    public void run() throws Exception {
                        binding.previewMask.setEnabled(true);
                    }
                })
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new DefaultObserver<UserEntity>() {
                    @Override
                    public void onNext(UserEntity s) {
                        error = false;
                        finish();
                    }
                });
    }

    private volatile String image = "";

    private volatile ArrayList<String> images;

    @NonNull
    private Function<List<Bitmap>, List<String>> bitmapToBase64Mapper() {
        return new Function<List<Bitmap>, List<String>>() {
            @Override
            public List<String> apply(List<Bitmap> bitmaps) throws Exception {
                images = new ArrayList<>();
                for (Bitmap bitmap : bitmaps) {
                    images.add(PreviewHelper.bitmapToBase64(bitmap));
                }
                return images;
            }
        };
    }

    private IconDialog iconDialog;

    private void onPreviewStatusChanged(PreviewHelper.Status status) {
        if (status.code == PreviewHelper.Status.ERROR_ON_OPEN_CAMERA) {
            if (iconDialog != null) {
                iconDialog.dismiss();
                iconDialog = null;
            }
            binding.ivTips.setText("打开相机失败");
            ToastUtils.showShort("打开相机失败");
        } else if (status.code == PreviewHelper.Status.EVENT_CAMERA_OPENED) {
            if (iconDialog != null) {
                iconDialog.dismiss();
                iconDialog = null;
            }
//            start(0);
            takeFrames("");
        }
    }

    private void takeFrames(String msg) {
        binding.previewMask.setEnabled(false);
        if (!NetUitls.isConnected()) {
            binding.previewMask.setEnabled(true);
            binding.ivTips.setText("请连接Wifi!");
            ToastUtils.showShort("请连接Wifi!");
            return;
        }
        if (TextUtils.isEmpty(msg)) {
            msg = "请把脸对准框内";
        }
        binding.ivTips.setText(msg);
        MLVoiceSynthetize.startSynthesize(getApplicationContext(), msg);

        mPreviewHelper.takeFrames(1, 1800, 500);
    }

    public ObservableTransformer<String, String> ensureAddFace() {
        return new ObservableTransformer<String, String>() {
            @Override
            public ObservableSource<String> apply(Observable<String> upstream) {
                return upstream
                        .onErrorResumeNext(new Function<Throwable, ObservableSource<? extends String>>() {
                            @Override
                            public ObservableSource<? extends String> apply(Throwable throwable) throws Exception {
                                if (throwable instanceof FaceBdError) {
                                    FaceBdError error = (FaceBdError) throwable;
                                    if (error.getCode() == FaceBdErrorUtils.ERROR_USER_NOT_EXIST
                                            || error.getCode() == FaceBdErrorUtils.ERROR_USER_NOT_FOUND) {
                                        return viewModel.addFace(image, "")
                                                .subscribeOn(Schedulers.io());
                                    }
                                }
                                return Observable.error(throwable);
                            }
                        });
            }
        };
    }

    public void goBack() {
        finish();
    }

    public void goWifi() {

    }

    private boolean hasSkip;

    public void skip() {
        hasSkip = true;
        finish();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (mPreviewHelper != null) {
            mPreviewHelper.configCamera();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        MLVoiceSynthetize.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dismissLoading();
    }

    private LoadingDialog mLoadingDialog;

    private void showLoading(String tips) {
        if (mLoadingDialog != null) {
            LoadingDialog loadingDialog = mLoadingDialog;
            mLoadingDialog = null;
            loadingDialog.dismiss();
        }
        mLoadingDialog = new LoadingDialog.Builder(this)
                .setIconType(LoadingDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord(tips)
                .create();
        mLoadingDialog.show();
    }

    private void dismissLoading() {
        if (mLoadingDialog != null) {
            LoadingDialog loadingDialog = mLoadingDialog;
            mLoadingDialog = null;
            loadingDialog.dismiss();
        }

    }

    @Override
    public void finish() {
        if (!TextUtils.isEmpty(callId)) {
            CCResult result;
            if (error) {
                if (hasSkip) {
                    result = CCResult.error("skip");
                } else {
                    result = CCResult.error("人脸验证未通过");
                }
            } else {
                result = CCResult.success();
                // Token 1.0
                CC.obtainBuilder("com.gcml.zzb.common.push.setTag")
                        .addParam("userId", UserSpHelper.getUserId())
                        .build()
                        .callAsync();

                //Token 2.0
//                    Observable<UserEntity> rxUser = CC.obtainBuilder("com.gcml.auth.refreshToken")
//                            .addParam("userId", theUserId)
//                            .build()
//                            .call()
//                            .getDataItem("data");
//                    rxUser.subscribeOn(Schedulers.io())
//                            .observeOn(AndroidSchedulers.mainThread())
//                            .as(RxUtils.autoDisposeConverter(this))
//                            .subscribe(new DefaultObserver<UserEntity>() {
//                                @Override
//                                public void onNext(UserEntity user) {
//                                    CC.obtainBuilder("com.gcml.zzb.common.push.setTag")
//                                            .addParam("userId", user.id)
//                                            .build()
//                                            .callAsync();
//                                    CC.sendCCResult(callId, result);
//                                    FaceSignInActivity.super.finish();
//                                }
//
//                                @Override
//                                public void onError(Throwable throwable) {
//                                    super.onError(throwable);
//                                    ToastUtils.showShort(throwable.getMessage());
//                                    CC.sendCCResult(callId, CCResult.error(throwable.getMessage()));
//                                    FaceSignInActivity.super.finish();
//                                }
//                            });
//                    return;
            }
            //为确保不管登录成功与否都会调用CC.sendCCResult，在onDestroy方法中调用
            CC.sendCCResult(callId, result);
        }
        super.finish();
    }

    private String callId;

    private volatile boolean error = true;
    private volatile boolean skip = true;
}
