package com.gcml.auth.face.debug.model;

import android.text.TextUtils;

import com.gcml.auth.face.debug.model.entity.FaceBdAccessToken;
import com.gcml.auth.face.debug.model.entity.FaceBdAddFace;
import com.gcml.auth.face.debug.model.entity.FaceBdAddFaceParam;
import com.gcml.auth.face.debug.model.entity.FaceBdSearch;
import com.gcml.auth.face.debug.model.entity.FaceBdSearchParam;
import com.gcml.auth.face.debug.model.entity.FaceBdUser;
import com.gcml.auth.face.debug.model.entity.FaceBdVerify;
import com.gcml.auth.face.debug.model.entity.FaceBdVerifyParam;
import com.gcml.auth.face.debug.model.exception.FaceBdError;
import com.gcml.common.repository.RepositoryApp;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class FaceBdRepository {

    private FaceBdService mFaceBdService =
            RepositoryApp.INSTANCE
                    .repositoryComponent()
                    .repositoryHelper()
                    .retrofitService(FaceBdService.class);

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

    public Observable<String> addFace(
            String image,
            String userId,
            String groupId) {
        if (TextUtils.isEmpty(groupId)) {
            groupId = "test_base";
        }
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

    public ObservableTransformer<String, String> ensureFaceAdded() {
        return new ObservableTransformer<String, String>() {
            @Override
            public ObservableSource<String> apply(Observable<String> upstream) {
                return upstream
                        .zipWith(accessToken(), new BiFunction<String, String, String>() {
                            @Override
                            public String apply(String image, String token) throws Exception {
                                FaceBdSearchParam param = new FaceBdSearchParam();
                                String[] imgData = image.split(",");
                                param.setImageType(imgData[0]);
                                param.setImage(imgData[1]);
                                param.setGroupIdList("test_base");
                                return mFaceBdService.search(token, param)
                                        .compose(FaceBdResultUtils.faceBdResultTransformer())
                                        .flatMap(new Function<FaceBdSearch, ObservableSource<String>>() {
                                            @Override
                                            public ObservableSource<String> apply(FaceBdSearch search) throws Exception {
                                                try {
                                                    String score = FaceBdErrorUtils.checkSearch(search);
                                                    return Observable.just(score);
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
}
