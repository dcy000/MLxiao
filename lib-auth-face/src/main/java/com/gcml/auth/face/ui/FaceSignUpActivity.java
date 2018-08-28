package com.gcml.auth.face.ui;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;

import com.gcml.auth.face.BR;
import com.gcml.auth.face.R;
import com.gcml.auth.face.databinding.AuthActivityFaceSignUpBinding;
import com.gcml.auth.face.utils.PreviewHelper;
import com.gcml.common.mvvm.BaseActivity;
import com.gcml.common.utils.RxUtils;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
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
                binding.ivAnimation.getLocationOnScreen(outLocation);
                mPreviewHelper.setUiRect(new Rect(
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
                        if (status.code == PreviewHelper.Status.EVENT_CROPPED_BITMAP) {
                            binding.ivAnimation.setImageBitmap((Bitmap) status.payload);
                        }
                    }
                })
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe();
    }


    public void goBack() {
        mPreviewHelper.addBuffer();
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
