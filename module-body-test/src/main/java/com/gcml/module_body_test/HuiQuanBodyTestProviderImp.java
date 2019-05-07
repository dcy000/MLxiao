package com.gcml.module_body_test;

import android.content.Context;
import android.content.Intent;

import com.gcml.common.data.UserEntity;
import com.gcml.common.recommend.bean.get.DiseaseUser;
import com.gcml.common.router.AppRouter;
import com.gcml.common.service.IHuiQuanBodyTestProvider;
import com.google.gson.Gson;
import com.sjtu.yifei.annotation.Route;
import com.sjtu.yifei.route.Routerfit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@Route(path = "/huiquan/body/test/provider")
public class HuiQuanBodyTestProviderImp implements IHuiQuanBodyTestProvider {
    @Override
    public void gotoPage(Context context) {
        Routerfit.register(AppRouter.class).getUserProvider()
                .getUserEntity()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.observers.DefaultObserver<UserEntity>() {
                    @Override
                    public void onNext(UserEntity userEntity) {
                        DiseaseUser diseaseUser = new DiseaseUser(
                                userEntity.name,
                                userEntity.sex.equals("男") ? 1 : 2,
                                Integer.parseInt(userEntity.age) * 12,
                                userEntity.avatar
                        );
                        String currentUser = new Gson().toJson(diseaseUser);
                        Intent intent = new Intent(context, com.witspring.unitbody.ChooseMemberActivity.class);
                        intent.putExtra("currentUser", currentUser);
                        context.startActivity(intent);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
