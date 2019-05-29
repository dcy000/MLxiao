package com.gcml.common.service;

import android.text.TextUtils;

import com.gcml.common.constant.EUserInfo;
import com.gcml.common.data.UserEntity;
import com.gcml.common.router.AppRouter;
import com.gcml.common.utils.Handlers;
import com.sjtu.yifei.annotation.Route;
import com.sjtu.yifei.route.Routerfit;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DefaultObserver;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

@Route(path = "/common/business/check/user/info/provider")
public class CheckUserInfoProviderImp implements ICheckUserInfoProvider {
    private List<EUserInfo> inCompleteConditions = new ArrayList<>();
    private StringBuilder builder = new StringBuilder(" ");

    @Override
    public void check(CheckUserInfo checkUserInfo, EUserInfo... conditions) {
        if (conditions.length == 0)
            throw new IllegalArgumentException("must  at least one check condition");
        Routerfit.register(AppRouter.class)
                .getUserProvider()
                .getUserEntity()
                .subscribeOn(Schedulers.io())
                .doOnNext(new Consumer<UserEntity>() {
                    @Override
                    public void accept(UserEntity entity) throws Exception {
                        for (EUserInfo info : conditions) {
                            switch (info) {
                                case NAME:
                                    if (TextUtils.isEmpty(entity.name)) {
                                        inCompleteConditions.add(EUserInfo.NAME);
                                    }
                                    break;
                                case AGE:
                                    if (TextUtils.isEmpty(entity.age) || TextUtils.equals(entity.age, "0")) {
                                        inCompleteConditions.add(EUserInfo.AGE);
                                    }
                                    break;
                                case BIRTHDAY:
                                    if (TextUtils.isEmpty(entity.birthday)) {
                                        inCompleteConditions.add(EUserInfo.BIRTHDAY);
                                    }
                                    break;
                                case PHONE:
                                    if (TextUtils.isEmpty(entity.phone)) {
                                        inCompleteConditions.add(EUserInfo.PHONE);
                                    }
                                    break;
                                case GENDER:
                                    if (TextUtils.isEmpty(entity.sex)) {
                                        inCompleteConditions.add(EUserInfo.GENDER);
                                    }
                                    break;
                                case HEIGHT:
                                    if (TextUtils.isEmpty(entity.height) || TextUtils.equals("0", entity.height)) {
                                        inCompleteConditions.add(EUserInfo.HEIGHT);
                                    }
                                    break;
                                case ADDRESS:
                                    if (TextUtils.isEmpty(entity.address)) {
                                        inCompleteConditions.add(EUserInfo.ADDRESS);
                                    }
                                    break;
                            }
                        }
                        for (EUserInfo info : inCompleteConditions) {
                            switch (info) {
                                case NAME:
                                    builder.append("姓名、");
                                    break;
                                case AGE:
                                    builder.append("年龄、");
                                case BIRTHDAY:
                                    builder.append("生日、");
                                    break;
                                case PHONE:
                                    builder.append("手机号码、");
                                    break;
                                case GENDER:
                                    builder.append("性别、");
                                    break;
                                case HEIGHT:
                                    builder.append("身高、");
                                    break;
                                case ADDRESS:
                                    builder.append("地址、");
                                    break;
                            }
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<UserEntity>() {
                    @Override
                    public void onNext(UserEntity entity) {
                        if (inCompleteConditions.size() == 0 && checkUserInfo != null) {
                            checkUserInfo.complete(entity);
                        } else {
                            if (builder.length() == 1) {
                                checkUserInfo.incomplete(entity,inCompleteConditions, "");
                            } else {
                                checkUserInfo.incomplete(entity,inCompleteConditions, builder.substring(1, builder.length() - 1));
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (checkUserInfo != null) {
                            checkUserInfo.onError(e);
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public interface CheckUserInfo {
        void complete(UserEntity userEntity);

        void incomplete(UserEntity entity,List<EUserInfo> args, String s);

        void onError(Throwable e);
    }
}
