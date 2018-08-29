package com.gcml.auth.face.model;

import android.content.Context;

import com.gcml.common.repository.IRepositoryHelper;
import com.gcml.common.repository.RepositoryApp;
import com.gcml.common.utils.RxUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class FaceRepository {

    private Context mContext = RepositoryApp.INSTANCE.app();

    private IRepositoryHelper mRepositoryHelper = RepositoryApp.INSTANCE.repositoryComponent().repositoryHelper();

    private FaceService mFaceService = mRepositoryHelper.retrofitService(FaceService.class);

    /**
     * 此前的人脸识别引擎用的是讯飞的
     */
    private FaceIdHelper mFaceIdHelper = new FaceIdHelper();

    private UploadHelper mUploadHelper = new UploadHelper();

    public static final int ERROR_ON_ENGINE_INIT = -1;
    public static final int ERROR_ON_FACE_SIGN_UP = -2;
    public static final int ERROR_ON_JOIN_GROUP_NOT_EXIST = -3;
    public static final int ERROR_ON_JOIN_GROUP_UNKNOWN = -4;
    public static final int ERROR_ON_CREATE_GROUP = -5;
    public static final int ERROR_ON_FACE_SIGN_IN = -6;

    public static class FaceError extends RuntimeException {
        private int code;

        public FaceError(int code, String message, Throwable cause) {
            super(message, cause);
            this.code = code;
        }

        public int getCode() {
            return code;
        }
    }

    public Observable<String> uploadAvatar(byte[] avatarData, String userId, String faceId) {
        return mFaceService.getQiniuToken()
                .compose(RxUtils.apiResultTransformer())
                .flatMap(new Function<String, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(String token) throws Exception {
                        String time = new SimpleDateFormat("yyyyMMddHHmmss",
                                Locale.getDefault()).format(new Date());
                        String key = String.format("%s_%s.jpg", time, userId);
                        return mUploadHelper.upload(avatarData, key, token)
                                .subscribeOn(Schedulers.io());
                    }
                })
                .flatMap(new Function<String, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(String url) throws Exception {
                        return mFaceService.uploadAvatarUrl(url, userId, faceId)
                                .compose(RxUtils.apiResultTransformer())
                                .map(new Function<Object, String>() {
                                    @Override
                                    public String apply(Object o) throws Exception {
                                        return url;
                                    }
                                })
                                .subscribeOn(Schedulers.io());
                    }
                });
    }

    public Observable<Boolean> signUp(byte[] faceData, String faceId) {
        return mFaceIdHelper.signUp(mContext, faceData, faceId);
    }


}
