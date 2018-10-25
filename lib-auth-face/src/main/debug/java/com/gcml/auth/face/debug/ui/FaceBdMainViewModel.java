package com.gcml.auth.face.debug.ui;

import android.app.Application;
import android.support.annotation.NonNull;

import com.gcml.auth.face.debug.model.FaceBdRepository;
import com.gcml.common.mvvm.BaseViewModel;

import io.reactivex.Observable;

public class FaceBdMainViewModel extends BaseViewModel {
    private FaceBdRepository mFaceBdRepository = new FaceBdRepository();

    public FaceBdMainViewModel(@NonNull Application application) {
        super(application);
    }

    public Observable<String> addFace(String image, String userId) {
        return mFaceBdRepository.addFace(image, userId, "");
    }

    public Observable<String> verifyLive(String image1, String image2) {
        return mFaceBdRepository.verifyLive(image1, image2);
    }
}
