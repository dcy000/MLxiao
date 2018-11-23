package com.gcml.auth.face.ui;

import android.app.Application;
import android.support.annotation.NonNull;

import com.gcml.auth.face.model.FaceRepository;
import com.gcml.auth.face.mvvm.BaseViewModel;
import com.gzq.lib_core.base.Box;
import com.gzq.lib_core.bean.UserInfoBean;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
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

                        return Observable.create(new ObservableOnSubscribe<UserInfoBean>() {
                            @Override
                            public void subscribe(ObservableEmitter<UserInfoBean> emitter) throws Exception {
                                UserInfoBean user = Box.getSessionManager().getUser();
                                user.userPhoto = url;
                                Box.getSessionManager().setUser(user);
                                emitter.onNext(user);
                            }
                        }).map(new Function<UserInfoBean, String>() {
                            @Override
                            public String apply(UserInfoBean userEntity) throws Exception {
                                return userEntity.userPhoto;
                            }
                        }).subscribeOn(Schedulers.io());
                    }
                });
    }
}
