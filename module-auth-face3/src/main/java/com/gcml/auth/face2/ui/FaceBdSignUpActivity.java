package com.gcml.auth.face2.ui;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.gcml.auth.face2.R;
import com.gcml.auth.face2.databinding.FaceActivityBdSignUpBinding;
import com.gcml.auth.face2.model.FaceBdErrorUtils;
import com.gcml.auth.face2.model.exception.FaceBdError;
import com.gcml.common.mvvm.BaseActivity;
import com.gcml.common.utils.DefaultObserver;
import com.gcml.common.utils.PreviewHelper;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.common.utils.network.NetUitls;
import com.gcml.common.widget.dialog.AlertDialog;
import com.gcml.common.widget.dialog.IconDialog;
import com.gcml.common.widget.dialog.LoadingDialog;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.sjtu.yifei.annotation.Route;
import com.sjtu.yifei.route.Routerfit;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Cancellable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

@Route(path = "/auth/face3/face/bd/signup/activity")
public class FaceBdSignUpActivity extends BaseActivity<FaceActivityBdSignUpBinding, FaceBdSignUpViewModel> {
    private String userId;
    private PreviewHelper mPreviewHelper;
    private Animation mAnimation;

    @Override
    protected int layoutId() {
        return R.layout.face_activity_bd_sign_up;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(savedInstanceState);
    }

    protected void init(Bundle savedInstanceState) {
        callId = getIntent().getStringExtra("callId");
        userId = getIntent().getStringExtra("userId");
        binding.setPresenter(this);
        binding.setViewModel(viewModel);
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

    private IconDialog iconDialog;

//    private void showFace(Bitmap faceBitmap) {
//        if (!NetUitls.isConnected()) {
//            binding.ivTips.setText("请连接Wifi!");
//            ToastUtils.showShort("请连接Wifi!");
//            return;
//        }
//        MLVoiceSynthetize.startSynthesize(getApplicationContext(),
//                "请确认是否是您的头像，如果不是请选择重新拍摄。");
//        iconDialog = new IconDialog(this).builder()
//                .setCancelable(false)
//                .setIcon(faceBitmap)
//                .setNegativeButton("重拍", new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        start();
//                    }
//                })
//                .setPositiveButton("确认头像", new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
////                        signUpFace(faceBitmap);
//                    }
//                });
//        iconDialog.show();
//    }

    private <T> ObservableTransformer<T, T> checkFace() {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream
                        .flatMap(new Function<T, ObservableSource<T>>() {
                            @Override
                            public ObservableSource<T> apply(T t) throws Exception {
                                return rxShowAvatar(t);
                            }
                        });
            }
        };
    }

    private <T> Observable<T> rxShowAvatar(T t) {
        return Observable.create(new ObservableOnSubscribe<T>() {
            @Override
            public void subscribe(ObservableEmitter<T> emitter) throws Exception {
                iconDialog = new IconDialog(FaceBdSignUpActivity.this).builder()
                        .setCancelable(false)
                        .setIcon(maps.get(0))
                        .setNegativeButton("重拍", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (!emitter.isDisposed()) {
                                    emitter.onError(new RuntimeException("retry"));
                                }
                            }
                        })
                        .setPositiveButton("确认头像", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (!emitter.isDisposed()) {
                                    emitter.onNext(t);
                                }
                            }
                        });
                emitter.setCancellable(new Cancellable() {
                    @Override
                    public void cancel() throws Exception {
                        if (iconDialog != null) {
                            iconDialog.dismiss();
                        }
                    }
                });
                iconDialog.show();
            }
        }).subscribeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(AndroidSchedulers.mainThread());
    }

    private void start() {
        mPreviewHelper.rxFrame()
                .buffer(1)
                .map(bitmapToBase64Mapper())
                .compose(viewModel.ensureLive())
                .flatMap(new Function<String, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(String img) throws Exception {
                        imageData = img;
                        if (TextUtils.isEmpty(img)) {
                            return Observable.error(new FaceBdError(FaceBdErrorUtils.ERROR_FACE_LIVELESS, ""));
                        }
                        return Observable.just(img);
                    }
                })
                .compose(checkFace())
//                .compose(viewModel.ensureFaceAdded())UtUt
//                .compose(ensureAddFace())
                .flatMap(new Function<String, ObservableSource<String>>() {

                    @Override
                    public ObservableSource<String> apply(String s) throws Exception {
                        return viewModel.addFaceByApi(userId, s, image)
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
                        imageData = "";

                    }
                })
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Timber.w(throwable);
                        FaceBdError wrapped = FaceBdErrorUtils.wrap(throwable);
                        int code = wrapped.getCode();
                        String msg = FaceBdErrorUtils.getMsg(code, wrapped.getMessage());
                        error = true;

                        if (code >= 500 && code <= 3005) {
                            onFaceError(msg);
                            return;
                        }

                        binding.ivTips.setText(msg);
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
                .subscribe(new DefaultObserver<String>() {
                    @Override
                    public void onNext(String s) {
                        error = false;
                        finish();
                    }
                });
    }

    private AlertDialog alertDialog;

    private void onFaceError(String msg) {
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
        alertDialog = new AlertDialog(this)
                .builder()
                .setMsg(msg)
                .setNegativeButton("退出", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                })
                .setPositiveButton("继续", new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        binding.ivTips.setText(msg);
                        start();
                        takeFrames(msg);
                    }
                });
        alertDialog.show();
    }

    // "${imageType},${image}"
    private volatile String imageData = "";

    private volatile ArrayList<String> images;
    private volatile List<Bitmap> maps;
    private volatile byte[] image;

    @NonNull
    private Function<List<Bitmap>, List<String>> bitmapToBase64Mapper() {
        return new Function<List<Bitmap>, List<String>>() {
            @Override
            public List<String> apply(List<Bitmap> bitmaps) throws Exception {
                maps = bitmaps;
                images = new ArrayList<>();
                for (Bitmap bitmap : bitmaps) {
                    image = PreviewHelper.bitmapToBytes(bitmaps.get(0));
                    images.add(PreviewHelper.bitmapToBase64(bitmap, false));
                }
                return images;
            }
        };
    }

//    private IconDialog iconDialog;

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
                                        return viewModel.addFace(imageData, userId)
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

        if (alertDialog != null) {
            alertDialog.dismiss();
        }
    }

    @Override
    public void finish() {
        if (error){
            Routerfit.setResult(Activity.RESULT_OK,"failed");
        }else{
            Routerfit.setResult(Activity.RESULT_OK,"success");
        }
//        if (!TextUtils.isEmpty(callId)) {
//            CCResult result;
//            if (error) {
//                result = CCResult.error("人脸录入失败");
//            } else {
//                result = CCResult.success("faceId", userId);
//            }
//            //为确保不管登录成功与否都会调用CC.sendCCResult，在onDestroy方法中调用
//            CC.sendCCResult(callId, result);
//        }
        super.finish();
    }

    private String callId;
    private volatile boolean error = true;
}
