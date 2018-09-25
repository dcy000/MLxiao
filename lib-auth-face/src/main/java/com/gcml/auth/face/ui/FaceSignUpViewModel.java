package com.gcml.auth.face.ui;

import android.app.Application;
import android.support.annotation.NonNull;

import com.billy.cc.core.component.CC;
import com.gcml.auth.face.model.FaceRepository;
import com.gcml.common.data.UserEntity;
import com.gcml.common.mvvm.BaseViewModel;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class FaceSignUpViewModel extends BaseViewModel {

    private FaceRepository mFaceRepository = new FaceRepository();

    public FaceSignUpViewModel(@NonNull Application application) {
        super(application);
    }

    public Observable<String> signUp(byte[] faceData, String faceId) {
        return mFaceRepository.signUp(faceData, faceId)
                .flatMap(new Function<String, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(String url) throws Exception {
                        Observable<UserEntity> rxUser =
                                CC.obtainBuilder("com.gcml.auth.fetchUser")
                                        .build()
                                        .call()
                                        .getDataItem("data");
                        return rxUser.map(new Function<UserEntity, String>() {
                            @Override
                            public String apply(UserEntity userEntity) throws Exception {
                                return userEntity.avatar;
                            }
                        }).subscribeOn(Schedulers.io());
                    }
                });
    }
}
