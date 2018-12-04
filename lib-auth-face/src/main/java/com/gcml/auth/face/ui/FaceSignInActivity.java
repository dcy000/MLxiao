package com.gcml.auth.face.ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.gcml.auth.face.BR;
import com.gcml.auth.face.FaceConstants;
import com.gcml.auth.face.R;
import com.gcml.auth.face.databinding.AuthActivityFaceSignInBinding;
import com.gcml.auth.face.model.PreviewHelper;
import com.gcml.auth.face.mvvm.BaseActivity;
import com.gzq.lib_core.base.Box;
import com.gzq.lib_core.bean.UserInfoBean;
import com.gzq.lib_core.constant.Constants;
import com.gzq.lib_core.utils.KVUtils;
import com.gzq.lib_core.utils.NetworkUtils;
import com.gzq.lib_core.utils.RxUtils;
import com.gzq.lib_core.utils.ToastUtils;
import com.iflytek.cloud.SpeechError;
import com.iflytek.synthetize.MLSynthesizerListener;
import com.iflytek.synthetize.MLVoiceSynthetize;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

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

public class FaceSignInActivity extends BaseActivity<AuthActivityFaceSignInBinding, FaceSignInViewModel> {

    private PreviewHelper mPreviewHelper;
    private Animation mAnimation;
    private boolean skip;
    private boolean currentUser;
    private String currentUserId;

    @Override
    protected int layoutId() {
        return R.layout.auth_activity_face_sign_in;
    }

    @Override
    protected int variableId() {
        return BR.viewModel;
    }

    private ArrayList<UserInfoBean> users = new ArrayList<>();

    @Override
    protected void init(Bundle savedInstanceState) {
        skip = getIntent().getBooleanExtra("skip", false);
        currentUser = getIntent().getBooleanExtra("currentUser", false);
        currentUserId = Box.getUserId();
        currentUserId = TextUtils.isEmpty(currentUserId) ? "" : currentUserId;
        users = getIntent().getParcelableArrayListExtra("users");
        if (users == null || users.isEmpty()) {
            setResult(RESULT_OK, new Intent().putExtra(FaceConstants.KEY_AUTH_FACE_RESULT, FaceConstants.AUTH_FACE_FAIL));
            finish();
            return;
        }
        binding.setPresenter(this);
        binding.tvSkip.setVisibility(skip ? View.VISIBLE : View.GONE);
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
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPreviewHelper.rxStatus()
                .observeOn(AndroidSchedulers.mainThread())
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
    }

    private void start(String tips, String voiceTips, int delayMillis) {
        if (!NetworkUtils.isAvailable()) {
            binding.ivTips.setText("请连接Wifi!");
            ToastUtils.showShort("请连接Wifi!");
            return;
        }

        binding.ivTips.setText(tips);
        mPreviewHelper.addBuffer(2000);

        /**
         * @see FaceSignInActivity#onPreviewStatusChanged(PreviewHelper.Status status)
         * @see PreviewHelper.Status.EVENT_CROPPED
         */

        MLVoiceSynthetize.startSynthesize(
                voiceTips,
                false,
                new MLSynthesizerListener() {
                    @Override
                    public void onCompleted(SpeechError speechError) {
//                        mPreviewHelper.addBuffer(delayMillis);
                    }
                }

        );
    }

    private void onPreviewStatusChanged(PreviewHelper.Status status) {
        if (!NetworkUtils.isAvailable()) {
            binding.ivTips.setText("请连接Wifi!");
            ToastUtils.showShort("请连接Wifi!");
            return;
        }
        if (status.code == PreviewHelper.Status.ERROR_ON_OPEN_CAMERA) {
            binding.ivTips.setText("打开相机失败");
            ToastUtils.showShort("打开相机失败");
        } else if (status.code == PreviewHelper.Status.EVENT_CAMERA_OPENED) {
            start("请把脸对准框内",
                    "请把脸对准框内",
                    0);
        } else if (status.code == PreviewHelper.Status.EVENT_CROPPED) {
            Bitmap faceBitmap = (Bitmap) status.payload;
            signInFace(faceBitmap);
        }
    }

    private void signInFace(Bitmap faceBitmap) {
        Observable.just(faceBitmap)
                .map(new Function<Bitmap, byte[]>() {
                    @Override
                    public byte[] apply(Bitmap bitmap) throws Exception {
                        Timber.i("Face Compress Image Data");
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
                        String groupId = (String) KVUtils.get(Constants.KEY_GROUP_ID, "");
                        Timber.i("Face signIn");
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
                        int count = retryCount.getAndIncrement();
                        if (count == 5) {
                            setResult(RESULT_OK, new Intent().putExtra(FaceConstants.KEY_AUTH_FACE_RESULT, FaceConstants.AUTH_FACE_FAIL));
                            finish();
                        } else {
                            start("请把脸对准框内",
                                    "请把脸对准框内",
                                    0);
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    AtomicInteger retryCount = new AtomicInteger(0);

    private void processFaceIdAndScore(String faceId, float score) {
        if (score < 30) {
            // 验证不通过
            start("请把脸对准框内",
                    "请把脸对准框内",
                    0);
        } else if (score < 80) {
            // 重新验证
            start("请把人脸靠近一点",
                    "请把人脸靠近一点",
                    0);
        } else {
            // 当前组存在人脸
            Timber.i("faceId: %s", faceId);
            Timber.i("users: %s", users);
            for (UserInfoBean user : users) {
                if (user == null || TextUtils.isEmpty(user.xfid)) {
                    continue;
                }
                Timber.i("%s", user);
                if (!TextUtils.isEmpty(user.xfid)
                        && user.xfid.equals(faceId)) {
                    theUserId = user.bid;
                    theDeviceId = user.eqid;

                    if (currentUser) {
                        if (!currentUserId.equals(user.bid)) {
                            continue;
                        }
                        error = false;
                        setResult(RESULT_OK, new Intent().putExtra(FaceConstants.KEY_AUTH_FACE_RESULT, FaceConstants.AUTH_FACE_SUCCESS));
                        finish();
                        return;
                    }

                    error = false;
                    setResult(RESULT_OK, new Intent().putExtra(FaceConstants.KEY_AUTH_FACE_RESULT, FaceConstants.AUTH_FACE_SUCCESS));
                    finish();
                    return;
                }
            }
            int count = retryCount.getAndIncrement();
            if (count >= 5) {
                setResult(RESULT_OK, new Intent().putExtra(FaceConstants.KEY_AUTH_FACE_RESULT, FaceConstants.AUTH_FACE_FAIL));
                finish();
            } else {
                start("请把脸对准框内",
                        "请把脸对准框内",
                        0);
            }
        }
    }

    public void goBack() {
        setResult(RESULT_OK, new Intent().putExtra(FaceConstants.KEY_AUTH_FACE_RESULT, FaceConstants.AUTH_FACE_BACK));
        finish();
    }

    private boolean hasSkip;

    public void skip() {
        hasSkip = true;
        setResult(RESULT_OK, new Intent().putExtra(FaceConstants.KEY_AUTH_FACE_RESULT, FaceConstants.AUTH_FACE_SKIP));
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
        binding.ivAnimation.clearAnimation();
        MLVoiceSynthetize.stop();
    }

    private volatile boolean error = true;

    private String theUserId = "";
    private String theDeviceId = "";
}
