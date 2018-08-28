package com.gcml.auth.face.ui;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;

import com.gcml.auth.face.BR;
import com.gcml.auth.face.R;
import com.gcml.auth.face.databinding.AuthActivityFaceSignUpBinding;
import com.gcml.auth.face.model.PreviewHelper;
import com.gcml.common.mvvm.BaseActivity;
import com.gcml.common.utils.RxUtils;

import java.io.ByteArrayOutputStream;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class FaceSignUpActivity extends BaseActivity<AuthActivityFaceSignUpBinding, FaceSignUpViewModel> {

    private PreviewHelper mPreviewHelper;

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
        binding.setPresenter(this);
        mPreviewHelper = new PreviewHelper(this);
        mPreviewHelper.setSurfaceHolder(binding.svPreview.getHolder());
        mPreviewHelper.setPreviewView(binding.svPreview);
        binding.ivAnimation.post(new Runnable() {
            @Override
            public void run() {
                int[] outLocation = new int[2];
                Timber.i("%s x %s", outLocation[0], outLocation[1]);
                binding.ivAnimation.getLocationInWindow(outLocation);
                mPreviewHelper.setCropRect(new Rect(
                        outLocation[0],
                        outLocation[1],
                        outLocation[0] + binding.ivAnimation.getWidth(),
                        outLocation[1] + binding.ivAnimation.getHeight()
                ));
            }
        });
        mPreviewHelper.rxStatus()
                .observeOn(Schedulers.io())
                .doOnNext(new Consumer<PreviewHelper.Status>() {
                    @Override
                    public void accept(PreviewHelper.Status status) throws Exception {
                        if (status.code == PreviewHelper.Status.EVENT_CROPPED) {
                            Bitmap faceBitmap = (Bitmap) status.payload;
                            signUpFace(faceBitmap);
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<PreviewHelper.Status>() {
                    @Override
                    public void accept(PreviewHelper.Status status) throws Exception {
                        if (status.code == PreviewHelper.Status.EVENT_CROPPED) {
                            Bitmap faceBitmap = (Bitmap) status.payload;
                            binding.ivAnimation.setImageBitmap(faceBitmap);
                        }
                    }
                })
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe();
    }

    private void signUpFace(Bitmap faceBitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        faceBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] faceData = baos.toByteArray();
        String faceId = "";
        viewModel.signUp(faceData, faceId)
                .subscribeOn(Schedulers.io())
                .doOnNext(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {

                    }
                })
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                })
                .subscribe();
    }

    public void goBack() {
        mPreviewHelper.addBuffer(0);
//        finish();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (mPreviewHelper != null) {
            mPreviewHelper.configCamera();
        }
    }
}
