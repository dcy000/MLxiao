package com.gcml.auth.face2.doctor;

import android.app.Application;
import android.support.annotation.NonNull;

import com.gcml.auth.face2.model.FaceBdRepository;
import com.gcml.common.data.UserEntity;
import com.gcml.common.mvvm.BaseViewModel;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;

public class FaceBdDoctorSignInViewModel extends BaseViewModel {
    private FaceBdRepository mFaceBdRepository = new FaceBdRepository();

    public FaceBdDoctorSignInViewModel(@NonNull Application application) {
        super(application);
    }

    public Observable<String> addFace(String image, String userId) {
        return mFaceBdRepository.addFace(image, userId, "");
    }

    public ObservableTransformer<String, DoctorEntity> ensureDoctorSignInByFace(String faceId) {
        return mFaceBdRepository.ensureDoctorSignInByFace(faceId);
    }

    public Observable<String> verifyLive(List<String> images) {
        return mFaceBdRepository.verifyLive(images);
    }

    public ObservableTransformer<List<String>, String> ensureLive() {
        return mFaceBdRepository.ensureLive();
    }



}