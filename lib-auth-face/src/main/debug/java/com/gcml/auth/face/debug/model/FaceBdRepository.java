package com.gcml.auth.face.debug.model;

import android.text.TextUtils;

import com.gcml.auth.face.debug.model.entity.FaceBdAccessToken;
import com.gcml.auth.face.debug.model.entity.FaceBdAddFace;
import com.gcml.auth.face.debug.model.entity.FaceBdAddFaceResponse;
import com.gcml.auth.face.debug.model.entity.FaceBdVerifyLive;
import com.gcml.common.repository.RepositoryApp;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

public class FaceBdRepository {

    private FaceBdService mFaceBdService =
            RepositoryApp.INSTANCE
                    .repositoryComponent()
                    .repositoryHelper()
                    .retrofitService(FaceBdService.class);

    private Observable<String> accessToken() {
        return mFaceBdService.refreshToken(
                FaceBdService.GRANT_TYPE,
                FaceBdService.API_KEY,
                FaceBdService.SECRET_KEY
        ).map(new Function<FaceBdAccessToken, String>() {
            @Override
            public String apply(FaceBdAccessToken faceBdAccessToken) throws Exception {
                return faceBdAccessToken.getAccessToken();
            }
        });
    }

    public Observable<String> verifyLive(
            String image1,
            String image2) {
        return accessToken()
                .flatMap(new Function<String, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(String token) throws Exception {
                        ArrayList<FaceBdVerifyLive> imgs = new ArrayList<>();
                        FaceBdVerifyLive img1 = new FaceBdVerifyLive();
                        img1.setImage(image1);
                        img1.setImageType("BASE64");
                        img1.setFaceField("age,beauty,expression");
                        imgs.add(img1);
                        FaceBdVerifyLive img2 = new FaceBdVerifyLive();
                        img1.setImage(image2);
                        img1.setImageType("BASE64");
                        img1.setFaceField("age,beauty,expression");
                        imgs.add(img2);
                        return mFaceBdService.verifyLive(token, imgs);
                    }
                });
    }

    public Observable<String> addFace(
            String image,
            String userId,
            String groupId) {
        if (TextUtils.isEmpty(groupId)) {
            groupId = "test_base";
        }
        return addFace(image, "BASE64", userId, userId, groupId, "LOW", "NONE");
    }

    public Observable<String> addFace(
            String image,
            String imageType,
            String userId,
            String userInfo,
            String groupId,
            String qualityControl,
            String livenessControl) {
        return accessToken()
                .flatMap(new Function<String, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(String token) throws Exception {
                        FaceBdAddFace addFace = new FaceBdAddFace();
                        addFace.setImage(image);
                        addFace.setImageType(imageType);
                        addFace.setUserId(userId);
                        addFace.setUserInfo(userInfo);
                        addFace.setGroupId(groupId);
                        addFace.setQualityControl(qualityControl);
                        addFace.setLivenessControl(livenessControl);
                        return mFaceBdService.addFace(token, addFace)
                                .compose(FaceBdResultUtils.faceBdResultTransformer())
                                .map(new Function<FaceBdAddFaceResponse, String>() {
                                    @Override
                                    public String apply(FaceBdAddFaceResponse response) throws Exception {
                                        return response.getFaceToken();
                                    }
                                });
                    }
                });
    }


}
