package com.gcml.common.face3;


import android.app.Activity;
import android.text.TextUtils;

import com.gcml.common.data.UserEntity;
import com.gcml.common.data.UserSpHelper;
import com.gcml.common.face2.VertifyFace2ProviderImp;
import com.gcml.common.router.AppRouter;
import com.gcml.common.service.IVertifyFaceProvider;
import com.gcml.common.utils.DefaultObserver;
import com.gcml.common.utils.UM;
import com.gcml.common.utils.display.ToastUtils;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.sjtu.yifei.annotation.Route;
import com.sjtu.yifei.route.ActivityCallback;
import com.sjtu.yifei.route.Routerfit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@Route(path = "/common/business/checkUserEntityAndVertifyFace3/face/provider")
public class VertifyFace3ProviderImp implements IVertifyFaceProvider {


    private boolean isShowSkipButton;
    private boolean isVertify;
    private boolean isHidden;
    private VertifyFace2ProviderImp.VertifyFaceResult result;

    private void getUserEntity() {
        Routerfit.register(AppRouter.class)
                .getUserProvider()
                .getUserEntity()
                .subscribeOn(Schedulers.io())
                .subscribe(new DefaultObserver<UserEntity>() {
                    @Override
                    public void onNext(UserEntity userEntity) {
                        if (TextUtils.isEmpty(userEntity.sex) || TextUtils.isEmpty(userEntity.birthday)) {
                            ToastUtils.showShort("请先去个人中心完善性别和年龄信息");
                            MLVoiceSynthetize.startSynthesize(UM.getApp(),
                                    "请先去个人中心完善性别和年龄信息");
                        } else {
                            getFaceId(userEntity.id);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        if (result != null) result.failed(throwable.getMessage());
                    }
                });
    }

    private void getFaceId(String userId) {
        Routerfit.register(AppRouter.class)
                .getFace3Provider()
                .getFaceId(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.observers.DefaultObserver<String>() {
                    @Override
                    public void onNext(String faceId) {
                        vertifyFace(faceId);
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (result != null) result.failed("请先注册人脸！");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void vertifyFace(String faceId) {
        Routerfit.register(AppRouter.class).skipFaceBd3SignInActivity(isShowSkipButton, isVertify, faceId, isHidden, new ActivityCallback() {
            @Override
            public void onActivityResult(int resul, Object data) {
                if (resul == Activity.RESULT_OK) {
                    String sResult = data.toString();
                    if (TextUtils.isEmpty(sResult)) {
                        if (result != null) result.failed("人脸验证失败");
                        return;
                    }
                    if (sResult.equals("success") || sResult.equals("skip")) {
                        if (result != null) result.success();
                    } else if (sResult.equals("failed")) {
                        if (result != null) result.failed("人脸验证失败");
                    }

                }
            }
        });
    }


    @Override
    public void checkUserEntityAndVertifyFace(boolean isShowSkipButton, boolean isVertify, boolean isHidden, VertifyFace2ProviderImp.VertifyFaceResult result) {
        this.result = result;
        this.isShowSkipButton = isShowSkipButton;
        this.isVertify = isVertify;
        this.isHidden = isHidden;
        getUserEntity();
    }

    @Override
    public void onlyVertifyFace(boolean isShowSkipButton, boolean isVertify, boolean isHidden, VertifyFace2ProviderImp.VertifyFaceResult result) {
        this.result = result;
        this.isShowSkipButton = isShowSkipButton;
        this.isVertify = isVertify;
        this.isHidden = isHidden;
        vertifyFace(UserSpHelper.getUserId());
    }

}
