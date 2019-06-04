package com.gcml.auth.face3.ui;


import com.gcml.auth.face3.model.FaceBdRepository;
import com.gcml.auth.face3.model.entity.FaceBdUser;
import com.gcml.common.mvvm.BaseViewModel;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;

public class FaceBdMainViewModel extends BaseViewModel {
    private FaceBdRepository mFaceBdRepository = new FaceBdRepository();

    public Observable<String> addFace(String image, String userId) {
        return mFaceBdRepository.addFace(image, userId, "");
    }

    public Observable<String> verifyLive(List<String> images) {
        return mFaceBdRepository.verifyLive(images);
    }

    public ObservableTransformer<List<String>, String> ensureLive() {
        return mFaceBdRepository.ensureLive();
    }

    public ObservableTransformer<String, FaceBdUser> ensureFaceAdded() {
        return mFaceBdRepository.ensureFaceAdded("");
    }
}