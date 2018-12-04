package com.gcml.auth.face.ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.gcml.auth.face.BR;
import com.gcml.auth.face.FaceConstants;
import com.gcml.auth.face.R;
import com.gcml.auth.face.databinding.AuthActivityFaceSignUpBinding;
import com.gcml.auth.face.model.PreviewHelper;
import com.gcml.auth.face.mvvm.BaseActivity;
import com.gcml.auth.face.utils.FaceUtils;
import com.gcml.lib_widget.dialog.IconDialog;
import com.gcml.lib_widget.dialog.LoadingDialog;
import com.gzq.lib_core.base.App;
import com.gzq.lib_core.base.Box;
import com.gzq.lib_core.base.ui.IEvents;
import com.gzq.lib_core.utils.NetworkUtils;
import com.gzq.lib_core.utils.RxUtils;
import com.gzq.lib_core.utils.ToastUtils;
import com.iflytek.cloud.SpeechError;
import com.iflytek.synthetize.MLSynthesizerListener;
import com.iflytek.synthetize.MLVoiceSynthetize;

import java.io.ByteArrayOutputStream;
import java.util.Set;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.observers.DefaultObserver;
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
            faceId = FaceUtils.produceFaceId();
        }
        callId = getIntent().getStringExtra("callId");
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
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        FaceUtils.rxWifiLevel(getApplication(), 4)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        binding.ivWifiState.setImageLevel(integer);
                    }
                });
    }

    private void start(int delayMillis) {
        if (!NetworkUtils.isConnected()) {
            binding.ivTips.setText("请连接Wifi!");
            ToastUtils.showShort("请连接Wifi!");
            return;
        }
        binding.ivTips.setText("请把脸对准框内");
        MLVoiceSynthetize.startSynthesize(
                "请把脸对准框内 。三，二，衣。茄子",
                false,
                new MLSynthesizerListener() {
                    @Override
                    public void onCompleted(SpeechError speechError) {
                        mPreviewHelper.addBuffer(delayMillis);
                        /**
                         * @see FaceSignUpActivity#onPreviewStatusChanged(PreviewHelper.Status status)
                         * @see PreviewHelper.Status.EVENT_CROPPED
                         */
                    }
                }
        );
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
            start(0);
        } else if (status.code == PreviewHelper.Status.EVENT_CROPPED) {
            Bitmap faceBitmap = (Bitmap) status.payload;
            showFace(faceBitmap);
        }
    }

    private IconDialog iconDialog;

    private void showFace(Bitmap faceBitmap) {
        if (!NetworkUtils.isConnected()) {
            binding.ivTips.setText("请连接Wifi!");
            ToastUtils.showShort("请连接Wifi!");
            return;
        }
        MLVoiceSynthetize.startSynthesize("请确认是否是您的头像，如果不是请选择重新拍摄。");
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
                        signUpFace(faceBitmap);
                    }
                });
        iconDialog.show();
    }

    private void signUpFace(Bitmap faceBitmap) {
        String userId = Box.getUserId();
        if (TextUtils.isEmpty(userId)) {
            ToastUtils.showShort("请先登录！");
            setResult(RESULT_OK,new Intent().putExtra(FaceConstants.KEY_AUTH_FACE_RESULT,FaceConstants.AUTH_FACE_FAIL));
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
                        binding.ivTips.setText("人脸录入成功");
                        error = false;
                        setResult(RESULT_OK,new Intent().putExtra(FaceConstants.KEY_AUTH_FACE_RESULT,FaceConstants.AUTH_FACE_SUCCESS));
                        finish();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        faceId = FaceUtils.produceFaceId();
                        error = true;
                        start(0);
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    public void goBack() {
        setResult(RESULT_OK,new Intent().putExtra(FaceConstants.KEY_AUTH_FACE_RESULT,FaceConstants.AUTH_FACE_BACK));
        finish();
    }

    public void goWifi() {
        Set<IEvents> events = App.getEvents();
        for (IEvents event : events) {
            event.onEvent("AuthFace>Skip2Wifi");
        }
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

    private String callId;
    private volatile boolean error = true;
}
