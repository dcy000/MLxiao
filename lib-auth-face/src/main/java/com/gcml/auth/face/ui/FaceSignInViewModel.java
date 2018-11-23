package com.gcml.auth.face.ui;

import android.app.Application;
import android.support.annotation.NonNull;

import com.gcml.auth.face.model.FaceRepository;
import com.gcml.auth.face.mvvm.BaseViewModel;

import io.reactivex.Observable;

public class FaceSignInViewModel extends BaseViewModel {
    private FaceRepository mFaceRepository = new FaceRepository();

    public FaceSignInViewModel(@NonNull Application application) {
        super(application);
    }

    public Observable<String> signIn(byte[] faceData, String groupId) {
        return mFaceRepository.signIn(faceData, groupId);
    }
}
