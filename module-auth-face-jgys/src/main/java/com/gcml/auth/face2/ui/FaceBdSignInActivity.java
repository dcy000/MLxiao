package com.gcml.auth.face2.ui;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.gcml.auth.face2.R;
import com.gcml.auth.face2.databinding.FaceActivityBdSignInBinding;
import com.gcml.auth.face2.model.FaceBdErrorUtils;
import com.gcml.auth.face2.model.exception.FaceBdError;
import com.gcml.common.data.UserEntity;
import com.gcml.common.data.UserSpHelper;
import com.gcml.common.mvvm.BaseActivity;
import com.gcml.common.utils.DefaultObserver;
import com.gcml.common.utils.JpushAliasUtils;
import com.gcml.common.utils.PreviewHelper;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.common.utils.network.NetUitls;
import com.gcml.common.widget.dialog.IconDialog;
import com.gcml.common.widget.dialog.LoadingDialog;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.sjtu.yifei.annotation.Route;
import com.sjtu.yifei.route.Routerfit;

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

@Route(path = "/auth/face/bd/signin/activity")
public class FaceBdSignInActivity extends BaseActivity<FaceActivityBdSignInBinding, FaceBdSignInViewModel> {

    private PreviewHelper mPreviewHelper;
    private Animation mAnimation;

    @Override
    protected int layoutId() {
        return R.layout.face_activity_bd_sign_in;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(savedInstanceState);
    }

    protected void init(Bundle savedInstanceState) {
        callId = getIntent().getStringExtra("callId");
        skip = getIntent().getBooleanExtra("skip", false);
        verify = getIntent().getBooleanExtra("verify", false);
        faceId = getIntent().getStringExtra("faceId");
        boolean hidden = getIntent().getBooleanExtra("hidden", false);
        binding.setPresenter(this);
        binding.tvSkip.setVisibility(skip ? View.VISIBLE : View.GONE);
        if (hidden) {
            binding.tvSkip.setTextColor(ContextCompat.getColor(this, R.color.white));
            binding.tvSkip.setBackground(null);
        }
        mPreviewHelper = new PreviewHelper(this);
        mPreviewHelper.setSurfaceHolder(binding.svPreview.getHolder());
        mPreviewHelper.setPreviewView(binding.svPreview);
        mAnimation = AnimationUtils.loadAnimation(
                getApplicationContext(), R.anim.face_anim_rotate);
        binding.ivAnimation.setAnimation(mAnimation);
        binding.ivAnimation.post(new Runnable() {
            @Override
            public void run() {
                int[] rectLocation = new int[2];
                int[] rootLocation = new int[2];
                binding.ivAnimation.getLocationInWindow(rectLocation);
                binding.clRoot.getLocationInWindow(rootLocation);
                int left = rectLocation[0];
                int top = rectLocation[1] - rootLocation[1];
                mPreviewHelper.setCropRect(new Rect(
                        left,
                        top,
                        left + binding.ivAnimation.getWidth(),
                        top + binding.ivAnimation.getHeight()
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
        UserSpHelper.setNoNetwork(false);
        mPreviewHelper.rxFrame()
                .buffer(1)
                .map(bitmapToBase64Mapper())
                .compose(viewModel.ensureLive())
                .flatMap(new Function<String, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(String img) throws Exception {
                        image = img;
                        if (TextUtils.isEmpty(img)) {
                            return Observable.error(new FaceBdError(FaceBdErrorUtils.ERROR_FACE_LIVELESS, ""));
                        }
                        return Observable.just(img);
                    }
                })
                .compose(viewModel.ensureSignInByFace(faceId))
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
                        String msg = FaceBdErrorUtils.getMsg(wrapped.getCode(), wrapped.getMessage());
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
                    images.add(PreviewHelper.bitmapToBase64(bitmap, true));
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
        if (error) {
            if (hasSkip){
                Routerfit.setResult(Activity.RESULT_OK,"skip");
            }else{
                Routerfit.setResult(Activity.RESULT_OK,"failed");
            }
        } else {
            Routerfit.setResult(Activity.RESULT_OK, "success");
            JpushAliasUtils.setAlias(UserSpHelper.getUserId());
        }
//        if (!TextUtils.isEmpty(callId)) {
//            CCResult result;
//            if (error) {
//                if (hasSkip) {
//                    result = CCResult.error("skip");
//                } else {
//                    result = CCResult.error("人脸验证未通过");
//                }
//            } else {
//                result = CCResult.success();
//                // Token 1.0
//                JpushAliasUtils.setAlias(UserSpHelper.getUserId());
//
//                //Token 2.0
////                    Observable<UserEntity> rxUser = CC.obtainBuilder("com.gcml.auth.refreshToken")
////                            .addParam("userId", theUserId)
////                            .build()
////                            .call()
////                            .getDataItem("data");
////                    rxUser.subscribeOn(Schedulers.io())
////                            .observeOn(AndroidSchedulers.mainThread())
////                            .as(RxUtils.autoDisposeConverter(this))
////                            .subscribe(new DefaultObserver<UserEntity>() {
////                                @Override
////                                public void onNext(UserEntity user) {
////                                    CC.obtainBuilder("com.gcml.zzb.common.push.setTag")
////                                            .addParam("userId", user.id)
////                                            .build()
////                                            .callAsync();
////                                    CC.sendCCResult(callId, result);
////                                    FaceSignInActivity.super.finish();
////                                }
////
////                                @Override
////                                public void onError(Throwable throwable) {
////                                    super.onError(throwable);
////                                    ToastUtils.showShort(throwable.getMessage());
////                                    CC.sendCCResult(callId, CCResult.error(throwable.getMessage()));
////                                    FaceSignInActivity.super.finish();
////                                }
////                            });
////                    return;
//            }
//            //为确保不管登录成功与否都会调用CC.sendCCResult，在onDestroy方法中调用
//            CC.sendCCResult(callId, result);
//        }
        super.finish();
    }

    private String callId;
    private String faceId;

    private volatile boolean error = true;
    private volatile boolean skip = true;
    private volatile boolean verify = false;
}
