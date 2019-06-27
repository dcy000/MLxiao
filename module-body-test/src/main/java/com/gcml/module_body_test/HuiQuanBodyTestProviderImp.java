package com.gcml.module_body_test;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;

import com.gcml.common.constant.EUserInfo;
import com.gcml.common.data.UserEntity;
import com.gcml.common.recommend.bean.get.DiseaseUser;
import com.gcml.common.router.AppRouter;
import com.gcml.common.service.CheckUserInfoProviderImp;
import com.gcml.common.service.IHuiQuanBodyTestProvider;
import com.gcml.common.utils.ChannelUtils;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.common.widget.fdialog.BaseNiceDialog;
import com.gcml.common.widget.fdialog.NiceDialog;
import com.gcml.common.widget.fdialog.ViewConvertListener;
import com.gcml.common.widget.fdialog.ViewHolder;
import com.google.gson.Gson;
import com.sjtu.yifei.annotation.Route;
import com.sjtu.yifei.route.ActivityLifecycleMonitor;
import com.sjtu.yifei.route.Routerfit;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@Route(path = "/huiquan/body/test/provider")
public class HuiQuanBodyTestProviderImp implements IHuiQuanBodyTestProvider {
    @Override
    public void gotoPage(Context context) {
        checkGender(new Runnable() {
            @Override
            public void run() {
                gotoSelfCheck(context);
            }
        });
    }

    private void gotoSelfCheck(Context context) {
        Routerfit.register(AppRouter.class).getUserProvider()
                .getUserEntity()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.observers.DefaultObserver<UserEntity>() {
                    @Override
                    public void onNext(UserEntity userEntity) {
                        if (TextUtils.isEmpty(userEntity.sex)) {
                            ToastUtils.showShort("请先去个人中心完善性别");
                            return;
                        }
                        int gender = userEntity.sex.equals("男") ? 1 : 2;
//                        int gender = 1;

                        DiseaseUser diseaseUser = new DiseaseUser(
                                userEntity.name,
                                gender,
                                Integer.parseInt(userEntity.age) * 12,
                                userEntity.avatar
                        );
                        String currentUser = new Gson().toJson(diseaseUser);
                        Intent intent = new Intent(context, com.witspring.unitbody.ChooseMemberActivity.class);
                        if (!(context instanceof Activity)) {
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        }
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

    private void checkGender(Runnable action) {
        Routerfit.register(AppRouter.class).getCheckUserInfoProvider()
                .check(new CheckUserInfoProviderImp.CheckUserInfo() {
                    @Override
                    public void complete(UserEntity userEntity) {
                        if (action != null) {
                            action.run();
                        }
//                        Routerfit.register(AppRouter.class).skipConnectActivity(IBleConstants.MEASURE_WEIGHT, true);
                    }

                    @Override
                    public void incomplete(UserEntity entity, List<EUserInfo> args, String s) {
                        showFinishGenderDialog("请先去个人中心完善性别");
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                }, EUserInfo.GENDER);
    }

    private void showFinishGenderDialog(String msg) {
        Activity activity = ActivityLifecycleMonitor.getTopActivity();
        if (!(activity instanceof FragmentActivity)) {
            return;
        }
        NiceDialog.init()
                .setLayoutId(R.layout.dialog_not_person_msg)
                .setConvertListener(new ViewConvertListener() {
                    @Override
                    protected void convertView(ViewHolder holder, BaseNiceDialog dialog) {
                        holder.setText(R.id.txt_msg, msg);
                        holder.setOnClickListener(R.id.btn_neg, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        holder.setOnClickListener(R.id.btn_pos, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                if (ChannelUtils.isXiongAn() || ChannelUtils.isBj()) {
                                    Routerfit.register(AppRouter.class).skipUserInfoActivity();
                                    return;
                                }
                                Routerfit.register(AppRouter.class).skipPersonDetailActivity();
                            }
                        });
                    }
                })
                .setWidth(700)
                .setHeight(350)
                .show(((FragmentActivity) activity).getSupportFragmentManager());
    }
}
