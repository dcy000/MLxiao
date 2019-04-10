package com.gcml.auth.face.model;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.gcml.common.data.UserEntity;
import com.gcml.common.data.UserSpHelper;
import com.gcml.common.repository.IRepositoryHelper;
import com.gcml.common.repository.RepositoryApp;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.UploadHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class FaceRepository {

    private Context mContext = RepositoryApp.INSTANCE.app();

    private IRepositoryHelper mRepositoryHelper = RepositoryApp.INSTANCE.repositoryComponent().repositoryHelper();

    private FaceService mFaceService = mRepositoryHelper.retrofitService(FaceService.class);

    private FaceIdHelper mFaceIdHelper = new FaceIdHelper();

    private UploadHelper mUploadHelper = new UploadHelper();

    private FaceGroupDao mFaceGroupDao = mRepositoryHelper.roomDb(FaceGroupDb.class, FaceGroupDb.class.getName()).faceGroupDao();

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
                }).subscribeOn(Schedulers.io());
    }

    public Observable<String> faceId() {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                String faceId = UserSpHelper.getFaceId();
                if (TextUtils.isEmpty(faceId)) {
                    emitter.onError(new NullPointerException("faceId == null"));
                } else {
                    emitter.onNext(faceId);
                }
            }
        });
    }

    public Observable<String> userId() {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                String userId = UserSpHelper.getUserId();
                if (TextUtils.isEmpty(userId)) {
                    emitter.onError(new NullPointerException("userId == null"));
                } else {
                    emitter.onNext(userId);
                }
            }
        });
    }


    /**
     * 职责：
     * 1. 人脸注册
     * 2. 1:N 人脸加组
     * 3. 上传头像
     *
     * @param faceData 人脸字节数据
     * @param faceId   人脸 id
     * @return 头像外链地址
     */
    public Observable<String> signUp(@NonNull byte[] faceData, @NonNull String faceId) {
        return mFaceIdHelper
                .signUp(mContext, faceData, faceId)
                .flatMap(new Function<String, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(String tempFaceId) throws Exception {
                        return tryJoinGroup(faceId);
                    }
                })
                .onErrorResumeNext(new Function<Throwable, ObservableSource<? extends String>>() {
                    @Override
                    public ObservableSource<? extends String> apply(Throwable error) throws Exception {
                        if (error instanceof FaceError
                                && (((FaceError) error).getCode() == ERROR_ON_JOIN_GROUP_UNKNOWN
                                || ((FaceError) error).getCode() == ERROR_ON_CREATE_GROUP)) {
                            // 当出现上两种异常时依然上传头像
                            return Observable.just(UserSpHelper.getGroupId());
                        }
                        return Observable.error(error);
                    }
                })
                .flatMap(new Function<String, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(String groupId) throws Exception {
                        String userId = UserSpHelper.getUserId();
                        if (TextUtils.isEmpty(userId)) {
                            return Observable.error(new NullPointerException("userId == null"));
                        }
                        return uploadAvatar(faceData, userId, faceId);
                    }
                });
    }

    /**
     * @param faceId 人脸 id
     * @return groupId
     */
    public Observable<String> tryJoinGroup(@NonNull String faceId) {
        String userId = UserSpHelper.getUserId();
        if (TextUtils.isEmpty(userId)) {
            return Observable.error(new NullPointerException("userId == null"));
        }
        String groupId = UserSpHelper.getGroupId();
        if (TextUtils.isEmpty(groupId)) {
            Timber.i("当前机器没创建过组 尝试创建并加组");
            return createAndJoinGroup(faceId);
        }
        Timber.i("当前机器创建过组 尝试加组, 当组不存在时 尝试创建并加组");
        return joinOrCreateGroup(groupId, faceId);
    }

    /**
     * @param faceId 人脸 id
     * @return groupId
     */
    private Observable<String> createAndJoinGroup(String faceId) {
        return mFaceIdHelper.createGroup(mContext, faceId)
                .flatMap(new Function<String, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(String groupId) throws Exception {
                        UserSpHelper.setGroupId(groupId);
                        UserSpHelper.setGroupFirstFaceId(faceId);
                        String userId = UserSpHelper.getUserId();
                        return mFaceService.updateFaceGroup(userId, groupId, faceId)
                                .compose(RxUtils.apiResultTransformer())
                                .map(new Function<Object, String>() {
                                    @Override
                                    public String apply(Object obj) throws Exception {
                                        return groupId;
                                    }
                                })
                                .doOnNext(new Consumer<String>() {
                                    @Override
                                    public void accept(String s) throws Exception {
                                        Timber.i("updateFaceGroup success");
                                    }
                                })
                                .subscribeOn(Schedulers.io());
                    }
                })
                .flatMap(new Function<String, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(String groupId) throws Exception {
                        return joinOrCreateGroup(groupId, faceId);
                    }
                })
                .doOnNext(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        UserSpHelper.setGroupId(s);
                    }
                })
                .subscribeOn(Schedulers.io());
    }

    /**
     * @param faceId 人脸 id
     * @return groupId
     */
    private Observable<String> joinOrCreateGroup(String groupId, String faceId) {
        return mFaceIdHelper.joinGroup(mContext, groupId, faceId)
                .onErrorResumeNext(new Function<Throwable, ObservableSource<? extends String>>() {
                    @Override
                    public ObservableSource<? extends String> apply(Throwable error) throws Exception {
                        if (error instanceof FaceError
                                && ((FaceError) error).getCode() == ERROR_ON_JOIN_GROUP_NOT_EXIST) {
                            return createAndJoinGroup(faceId);
                        }
                        return Observable.error(error);
                    }
                })
                .doOnNext(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        UserSpHelper.setGroupId(s);
                    }
                })
                .subscribeOn(Schedulers.io());
    }

    public Observable<Object> deleteGroup() {
        return mFaceIdHelper.deleteGroup(mContext, UserSpHelper.getGroupId(), UserSpHelper.getGroupFirstFaceId())
                .onErrorResumeNext(Observable.just(new Object()))
                .doOnNext(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        UserSpHelper.setGroupId("");
                        UserSpHelper.setGroupFirstFaceId("");
                    }
                });
    }

    /**
     * @param faceData 人脸数据
     * @param groupId  组 Id
     * @return faceId:score
     */
    public Observable<String> signIn(byte[] faceData, String groupId) {
        return mFaceIdHelper.signIn(mContext, faceData, groupId)
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        Timber.i("Face SignIn : groupId = %s ", groupId);
                    }
                })
                .doOnNext(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        UserSpHelper.setNoNetwork(false);
                    }
                });
    }

    public Observable<List<UserEntity>> getLocalUsers() {
        String[] accounts = UserSpHelper.getAccounts();
        if (accounts == null) {
            return Observable.just(new ArrayList<>());
        }
        StringBuilder mAccountIdBuilder = new StringBuilder();
        for (String item : accounts) {
            mAccountIdBuilder.append(item.split(",")[0])
                    .append(",");
        }
        mAccountIdBuilder.deleteCharAt(mAccountIdBuilder.length() - 1);
        return mFaceService.getAllUsers(mAccountIdBuilder.toString())
                .compose(RxUtils.apiResultTransformer())
                .onErrorResumeNext(Observable.just(new ArrayList<>()));
    }

}
