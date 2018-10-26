package com.gcml.auth.face.debug.ui;

import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;

import com.gcml.auth.face.BR;
import com.gcml.auth.face.R;
import com.gcml.auth.face.databinding.FaceActivityBdMainBinding;
import com.gcml.auth.face.model.PreviewHelper;
import com.gcml.auth.face.ui.FaceSignUpActivity;
import com.gcml.common.data.UserSpHelper;
import com.gcml.common.mvvm.BaseActivity;
import com.gcml.common.repository.utils.DefaultObserver;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.common.utils.network.NetUitls;
import com.gcml.common.widget.dialog.IconDialog;
import com.gcml.common.widget.dialog.InputDialog;
import com.gcml.common.widget.dialog.LoadingDialog;
import com.iflytek.cloud.SpeechError;
import com.iflytek.synthetize.MLSynthesizerListener;
import com.iflytek.synthetize.MLVoiceSynthetize;

import java.io.ByteArrayOutputStream;
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
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class FaceBdMainActivity extends BaseActivity<FaceActivityBdMainBinding, FaceBdMainViewModel> {

    @Override
    protected int layoutId() {
        return R.layout.face_activity_bd_main;
    }

    @Override
    protected int variableId() {
        return BR.viewModel;
    }

    private PreviewHelper mPreviewHelper;
    private String faceId;

    @Override
    protected void init(Bundle savedInstanceState) {
        binding.setPresenter(this);
        mPreviewHelper = new PreviewHelper(this);
        mPreviewHelper.setSurfaceHolder(binding.svPreview.getHolder());
        mPreviewHelper.setPreviewView(binding.svPreview);
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
                .compose(viewModel.ensureFaceAdded())
                .compose(ensureAddFace())
                .doOnTerminate(new Action() {
                    @Override
                    public void run() throws Exception {
                        image = "";
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new DefaultObserver<String>() {
                    @Override
                    public void onNext(String s) {
                        binding.ivTips.setText(s);
                        ToastUtils.showShort(s);
                    }
                });

    }

    private String image = "";

    @NonNull
    private Function<List<Bitmap>, List<String>> bitmapToBase64Mapper() {
        return new Function<List<Bitmap>, List<String>>() {
            @Override
            public List<String> apply(List<Bitmap> bitmaps) throws Exception {
                ArrayList<String> images = new ArrayList<>();
                for (Bitmap bitmap : bitmaps) {
                    images.add(PreviewHelper.bitmapToBase64(bitmap));
                }
                return images;
            }
        };
    }


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
            takeFrames();
        } else if (status.code == PreviewHelper.Status.EVENT_CROPPED) {
//            Bitmap bitmap = (Bitmap) status.payload;
//            onBitmapPrepared(bitmap);
        }
    }

    private void onBitmapPrepared(Bitmap bitmap) {
        if (!NetUitls.isConnected()) {
            binding.ivTips.setText("请连接Wifi!");
            ToastUtils.showShort("请连接Wifi!");
            return;
        }
        addFace(bitmap);
    }

    private IconDialog iconDialog;

    private void showFace(Bitmap faceBitmap) {
        if (!NetUitls.isConnected()) {
            binding.ivTips.setText("请连接Wifi!");
            ToastUtils.showShort("请连接Wifi!");
            return;
        }
        MLVoiceSynthetize.startSynthesize(getApplicationContext(),
                "请确认是否是您的头像，如果不是请选择重新拍摄。");
        iconDialog = new IconDialog(this).builder()
                .setCancelable(false)
                .setIcon(faceBitmap)
                .setNegativeButton("重拍", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        start(0);
                    }
                })
                .setPositiveButton("确认头像", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addFace(faceBitmap);
                    }
                });
        iconDialog.show();
    }

    public Observable<String> userId() {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                View.OnClickListener cancelOnClickListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (emitter.isDisposed()) {
                            emitter.onError(new RuntimeException("no user id"));
                        }
                    }
                };
                InputDialog.OnInputChangeListener onInputChangeListener = new InputDialog.OnInputChangeListener() {
                    @Override
                    public void onInput(String s) {
                        if (TextUtils.isEmpty(s)) {
                            if (!emitter.isDisposed()) {
                                emitter.onError(new RuntimeException("no user id"));
                            }
                            return;
                        }
                        if (!emitter.isDisposed()) {
                            emitter.onNext(s);
                        }
                    }
                };
                new InputDialog(FaceBdMainActivity.this)
                        .builder()
                        .setMsg("输入 user id")
                        .setCancelable(true)
                        .setNegativeButton("取消", cancelOnClickListener)
                        .setPositiveButton("确定", onInputChangeListener)
                        .show();

            }
        }).subscribeOn(AndroidSchedulers.mainThread());
    }

    public ObservableTransformer<String, String> ensureAddFace() {
        return new ObservableTransformer<String, String>() {
            @Override
            public ObservableSource<String> apply(Observable<String> upstream) {
                return upstream
                        .onErrorResumeNext(new Function<Throwable, ObservableSource<? extends String>>() {
                            @Override
                            public ObservableSource<? extends String> apply(Throwable throwable) throws Exception {
                                return Observable.just(image);
                            }
                        })
                        .zipWith(userId(), new BiFunction<String, String, String>() {
                            @Override
                            public String apply(String image, String userId) throws Exception {
                                return viewModel.addFace(image, userId)
                                        .subscribeOn(Schedulers.io())
                                        .blockingFirst();
                            }
                        });
            }
        };
    }

    private void addFace(Bitmap bitmap) {


        Observable.just(bitmap)
                .map(new Function<Bitmap, String>() {
                    @Override
                    public String apply(Bitmap bitmap) throws Exception {
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        if (!bitmap.isRecycled()) {
                            bitmap.recycle();
                        }
                        return Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
                    }
                })
                .zipWith(userId(), new BiFunction<String, String, String>() {
                    @Override
                    public String apply(String image, String userId) throws Exception {
                        return viewModel.addFace(image, userId)
                                .subscribeOn(Schedulers.io())
                                .blockingFirst();
                    }
                })
//                .flatMap(new Function<String, ObservableSource<String>>() {
//                    @Override
//                    public ObservableSource<String> apply(String img) throws Exception {
////                        String faceId = UserSpHelper.getFaceId();
//                        return viewModel.addFace(img, faceId)
//                                .subscribeOn(Schedulers.io());
//                    }
//                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        binding.ivTips.setText("人脸录入...");
                        showLoading("人脸录入...");
                    }
                })
                .doOnTerminate(new Action() {
                    @Override
                    public void run() throws Exception {
                        dismissLoading();
                    }
                })
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new DefaultObserver<String>() {
                    @Override
                    public void onNext(String s) {
                        Timber.i(s);
                        ToastUtils.showShort(s);
//                        binding.ivTips.setText("人脸录入成功");
//                        error = false;
//                        finish();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        faceId = UserSpHelper.produceFaceId();
//                        error = true;
//                        start(0);
                    }
                });

    }

    private void start(int delayMillis) {
        if (!NetUitls.isConnected()) {
            binding.ivTips.setText("请连接Wifi!");
            ToastUtils.showShort("请连接Wifi!");
            return;
        }
        binding.ivTips.setText("请把脸对准框内");
        MLVoiceSynthetize.startSynthesize(
                getApplicationContext(),
                "请把脸对准框内 。三，二，衣。茄子",
                new MLSynthesizerListener() {
                    @Override
                    public void onCompleted(SpeechError speechError) {
                        mPreviewHelper.addBuffer(delayMillis);
                        /**
                         * @see FaceSignUpActivity#onPreviewStatusChanged(PreviewHelper.Status status)
                         * @see PreviewHelper.Status.EVENT_CROPPED
                         */
                    }
                },
                false
        );
    }

    private void takeFrames() {
        if (!NetUitls.isConnected()) {
            binding.ivTips.setText("请连接Wifi!");
            ToastUtils.showShort("请连接Wifi!");
            return;
        }
        binding.ivTips.setText("请把脸对准框内");
        MLVoiceSynthetize.startSynthesize(getApplicationContext(), "请把脸对准框内。");

        mPreviewHelper.takeFrames(1, 1000, 500);
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
    }
}
