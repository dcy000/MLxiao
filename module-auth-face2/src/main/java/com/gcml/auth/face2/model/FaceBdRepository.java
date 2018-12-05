package com.gcml.auth.face2.model;

import android.text.TextUtils;

import com.billy.cc.core.component.CC;
import com.gcml.auth.face2.model.entity.FaceBdAccessToken;
import com.gcml.auth.face2.model.entity.FaceBdAddFace;
import com.gcml.auth.face2.model.entity.FaceBdAddFaceParam;
import com.gcml.auth.face2.model.entity.FaceBdSearch;
import com.gcml.auth.face2.model.entity.FaceBdSearchParam;
import com.gcml.auth.face2.model.entity.FaceBdUser;
import com.gcml.auth.face2.model.entity.FaceBdVerify;
import com.gcml.auth.face2.model.entity.FaceBdVerifyParam;
import com.gcml.auth.face2.model.exception.FaceBdError;
import com.gcml.common.data.UserEntity;
import com.gcml.common.data.UserSpHelper;
import com.gcml.common.repository.RepositoryApp;
import com.gcml.common.user.UserToken;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.UploadHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Function3;
import io.reactivex.schedulers.Schedulers;

public class FaceBdRepository {

    private FaceBdService mFaceBdService =
            RepositoryApp.INSTANCE
                    .repositoryComponent()
                    .repositoryHelper()
                    .retrofitService(FaceBdService.class);

    private UploadHelper mUploadHelper = new UploadHelper();

    public Observable<String> uploadAvatar(byte[] avatarData, String userId, String faceId) {
        return mFaceBdService.getQiniuToken()
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
                        return mFaceBdService.uploadAvatarUrl(url, userId, faceId)
                                .compose(RxUtils.apiResultTransformer())
                                .map(new Function<Object, String>() {
                                    @Override
                                    public String apply(Object o) throws Exception {
                                        return url;
                                    }
                                })
                                .subscribeOn(Schedulers.io());
                    }
                })
                .subscribeOn(Schedulers.io());
    }

    public Observable<String> uploadImg(byte[] avatarData, String userId, String faceId) {
        return mFaceBdService.getQiniuToken()
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
                .subscribeOn(Schedulers.io());
    }

    private Observable<String> accessToken() {
        if (!TextUtils.isEmpty(FaceBdErrorUtils.accessToken)) {
            return Observable.just(FaceBdErrorUtils.accessToken);
        }
        return mFaceBdService.refreshToken(
                FaceBdService.GRANT_TYPE,
                FaceBdService.API_KEY,
                FaceBdService.SECRET_KEY
        ).map(new Function<FaceBdAccessToken, String>() {
            @Override
            public String apply(FaceBdAccessToken faceBdAccessToken) throws Exception {
                String accessToken = faceBdAccessToken.getAccessToken();
                FaceBdErrorUtils.accessToken = accessToken;
                return accessToken;
            }
        }).subscribeOn(Schedulers.io());
    }

    public Observable<String> verifyLive(List<String> images) {
        return accessToken()
                .flatMap(new Function<String, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(String token) throws Exception {
                        ArrayList<FaceBdVerifyParam> imgs = new ArrayList<>();
                        for (String image : images) {
                            FaceBdVerifyParam img = new FaceBdVerifyParam();
                            img.setImage(image);
                            img.setImageType("BASE64");
                            img.setFaceField("age,beauty,expression");
                            imgs.add(img);
                        }
                        return mFaceBdService.verify(token, imgs)
                                .compose(FaceBdResultUtils.faceBdResultTransformer())
                                .map(new Function<FaceBdVerify, String>() {
                                    @Override
                                    public String apply(FaceBdVerify faceBdVerify) throws Exception {
                                        return "";
                                    }
                                })
                                .subscribeOn(Schedulers.io());
                    }
                });
    }

    public ObservableTransformer<List<String>, String> ensureLive() {
        return new ObservableTransformer<List<String>, String>() {
            @Override
            public ObservableSource<String> apply(Observable<List<String>> upstream) {
                return upstream
                        .zipWith(accessToken(), new BiFunction<List<String>, String, String>() {
                            @Override
                            public String apply(List<String> images, String token) throws Exception {
                                ArrayList<FaceBdVerifyParam> imgs = new ArrayList<>();
                                for (String image : images) {
                                    FaceBdVerifyParam img = new FaceBdVerifyParam();
                                    img.setImage(image);
                                    img.setImageType("BASE64");
                                    img.setFaceField("age,beauty,expression");
                                    imgs.add(img);
                                }
                                return mFaceBdService.verify(token, imgs)
                                        .compose(FaceBdResultUtils.faceBdResultTransformer())
                                        .flatMap(new Function<FaceBdVerify, ObservableSource<String>>() {
                                            @Override
                                            public ObservableSource<String> apply(FaceBdVerify result) throws Exception {
                                                String image = FaceBdErrorUtils.liveFace(result);
                                                if (!TextUtils.isEmpty(image)) {
                                                    return Observable.just(image);
                                                }
                                                return Observable.error(new FaceBdError(FaceBdErrorUtils.ERROR_FACE_LIVELESS, ""));
                                            }
                                        })
                                        .subscribeOn(Schedulers.io())
                                        .blockingFirst();
                            }
                        });
            }
        };
    }

    public Observable<String> addFaceByApi(
            String userId,
            String imageData) {
        String[] imgData = imageData.split(",");
        return mFaceBdService.addFace(userId, imgData[1], imgData[0])
                .compose(RxUtils.apiResultTransformer())
                .map(new Function<Object, String>() {
                    @Override
                    public String apply(Object o) throws Exception {
                        return imageData;
                    }
                })
                .subscribeOn(Schedulers.io());
    }

    public Observable<String> addFaceByApi(
            byte[] imageData,
            String userId) {
        return uploadImg(imageData, userId, userId)
                .flatMap(new Function<String, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(String url) throws Exception {
                        return mFaceBdService.addFace(userId, url, "URL")
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

    public ObservableTransformer<String, UserEntity> ensureSignInByFace() {
        return new ObservableTransformer<String, UserEntity>() {

            @Override
            public ObservableSource<UserEntity> apply(Observable<String> upstream) {
                return upstream.compose(ensureFaceAdded())
                        .flatMap(new Function<FaceBdUser, ObservableSource<UserEntity>>() {
                            @Override
                            public ObservableSource<UserEntity> apply(FaceBdUser bdUser) throws Exception {
                                return mFaceBdService.signInByFace(bdUser.getUserId(), bdUser.getGroupId())
                                        .compose(RxUtils.apiResultTransformer())
                                        .compose(userTokenTransformer())
                                        .subscribeOn(Schedulers.io());
                            }
                        });
            }
        };
    }

    private ObservableTransformer<UserToken, UserEntity> userTokenTransformer() {
        return new ObservableTransformer<UserToken, UserEntity>() {
            @Override
            public ObservableSource<UserEntity> apply(Observable<UserToken> upstream) {
                return upstream
                        .doOnNext(new Consumer<UserToken>() {
                            @Override
                            public void accept(UserToken userToken) throws Exception {
                                UserSpHelper.setUserId(userToken.getUserId());
                                UserSpHelper.setToken(userToken.getToken());
                                UserSpHelper.setRefreshToken(userToken.getRefreshToken());
                            }
                        })
                        .flatMap(new Function<UserToken, ObservableSource<UserEntity>>() {
                            @Override
                            public ObservableSource<UserEntity> apply(UserToken userToken) throws Exception {
                                Observable<UserEntity> rxUser =
                                        CC.obtainBuilder("com.gcml.auth.fetchUser")
                                                .build()
                                                .call()
                                                .getDataItem("data");
                                return rxUser;
                            }
                        })
                        .doOnNext(new Consumer<UserEntity>() {
                            @Override
                            public void accept(UserEntity user) throws Exception {
                                UserSpHelper.setFaceId(user.xfid);
                                UserSpHelper.setEqId(user.deviceId);
                            }
                        });
            }
        };
    }

    public Observable<String> addFace(
            String image,
            String userId,
            String groupId
    ) {
        String[] imageData = image.split(",");
        return addFace(imageData[0], imageData[1], userId, userId, groupId, null, null);
    }

    public Observable<String> addFace(
            String imageType,
            String image,
            String userId,
            String userInfo,
            String groupId,
            String qualityControl,
            String livenessControl) {
        return accessToken()
                .flatMap(new Function<String, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(String token) throws Exception {
                        FaceBdAddFaceParam addFace = new FaceBdAddFaceParam();
                        addFace.setImageType(imageType);
                        addFace.setImage(image);
                        addFace.setUserId(userId);
                        addFace.setUserInfo(userInfo);
                        addFace.setGroupId(groupId);
                        addFace.setQualityControl(qualityControl);
                        addFace.setLivenessControl(livenessControl);
                        return mFaceBdService.addFace(token, addFace)
                                .compose(FaceBdResultUtils.faceBdResultTransformer())
                                .map(new Function<FaceBdAddFace, String>() {
                                    @Override
                                    public String apply(FaceBdAddFace result) throws Exception {
                                        return "FACE_TOKEN," + result.getFaceToken();
                                    }
                                })
                                .subscribeOn(Schedulers.io());
                    }
                });
    }

    public Observable<Object> searchVerify(String image) {
        return accessToken()
                .flatMap(new Function<String, ObservableSource<FaceBdSearch>>() {
                    @Override
                    public ObservableSource<FaceBdSearch> apply(String token) throws Exception {
                        FaceBdSearchParam param = new FaceBdSearchParam();
                        String[] imgData = image.split(",");
                        param.setImageType(imgData[0]);
                        param.setImage(imgData[1]);
                        param.setGroupIdList("test_base");
                        return mFaceBdService.search(token, param)
                                .compose(FaceBdResultUtils.faceBdResultTransformer());
                    }
                })
                .map(new Function<FaceBdSearch, Object>() {
                    @Override
                    public Object apply(FaceBdSearch faceBdSearch) throws Exception {
                        return faceBdSearch;
                    }
                });
    }


    public ObservableTransformer<String, FaceBdUser> ensureFaceAdded() {
        return new ObservableTransformer<String, FaceBdUser>() {
            @Override
            public ObservableSource<FaceBdUser> apply(Observable<String> upstream) {
                return Observable.zip(upstream, accessToken(), getGroups(), new Function3<String, String, String, FaceBdUser>() {
                    @Override
                    public FaceBdUser apply(String image, String token, String groups) throws Exception {
                        FaceBdSearchParam param = new FaceBdSearchParam();
                        String[] imgData = image.split(",");
                        param.setImageType(imgData[0]);
                        param.setImage(imgData[1]);
                        param.setGroupIdList(groups);
                        return mFaceBdService.search(token, param)
                                .compose(FaceBdResultUtils.faceBdResultTransformer())
                                .flatMap(new Function<FaceBdSearch, ObservableSource<FaceBdUser>>() {
                                    @Override
                                    public ObservableSource<FaceBdUser> apply(FaceBdSearch search) throws Exception {
                                        try {
                                            FaceBdUser bdUser = FaceBdErrorUtils.checkSearch(search);
                                            return Observable.just(bdUser);
                                        } catch (Exception e) {
                                            return Observable.error(e);
                                        }
                                    }
                                })
                                .subscribeOn(Schedulers.io())
                                .blockingFirst();
                    }
                });
            }
        };
    }

    private Observable<String> getGroups() {
        return mFaceBdService.getGroups()
                .compose(RxUtils.apiResultTransformer())
                .map(new Function<List<String>, String>() {
                    @Override
                    public String apply(List<String> strings) throws Exception {
                        StringBuilder builder = new StringBuilder();
                        for (String g : strings) {
                            if (!TextUtils.isEmpty(g)) {
                                builder.append(g).append(",");
                            }
                        }
                        return builder.toString();
                    }
                })
                .subscribeOn(Schedulers.io());
    }
}
