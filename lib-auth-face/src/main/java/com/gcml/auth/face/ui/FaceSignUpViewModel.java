package com.gcml.auth.face.ui;

import android.app.Application;
import android.support.annotation.NonNull;

import com.gcml.auth.face.model.FaceRepository;
import com.gcml.common.mvvm.BaseViewModel;

import io.reactivex.Observable;

public class FaceSignUpViewModel extends BaseViewModel {

    private FaceRepository mFaceRepository = new FaceRepository();

    public FaceSignUpViewModel(@NonNull Application application) {
        super(application);
    }

    public Observable<Boolean> signUp(byte[] faceData, String faceId) {
        return mFaceRepository.signUp(faceData, faceId);
    }
}
