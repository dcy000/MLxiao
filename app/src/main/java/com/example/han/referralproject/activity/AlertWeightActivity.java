package com.example.han.referralproject.activity;

import android.view.View;

import com.example.han.referralproject.R;
import com.example.han.referralproject.service.API;
import com.gzq.lib_core.base.Box;
import com.gzq.lib_core.bean.UserInfoBean;
import com.gzq.lib_core.http.exception.ApiException;
import com.gzq.lib_core.http.observer.CommonObserver;
import com.gzq.lib_core.utils.RxUtils;
import com.gzq.lib_core.utils.ToastUtils;
import com.iflytek.synthetize.MLVoiceSynthetize;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class AlertWeightActivity extends AlertHeightActivity {

    @Override
    protected void initView() {
        super.initView();
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("修改体重");
        tvSignUpHeight.setText("您的体重");
        tvSignUpUnit.setText("kg");
    }

    @Override
    protected List<String> getStrings() {
        mStrings = new ArrayList<>();
        for (int i = 35; i < 150; i++) {
            mStrings.add(String.valueOf(i));
        }
        return mStrings;
    }

    @Override
    public void onTvGoForwardClicked() {
        final String weight = mStrings.get(selectedPosition);

        UserInfoBean user = Box.getSessionManager().getUser();
        Box.getRetrofit(API.class)
                .alertUserInfo(
                        user.bid,
                        data.height,
                        weight,
                        data.eatingHabits,
                        data.smoke,
                        data.drink,
                        data.exerciseHabits,
                        data.mh,
                        data.dz
                )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new CommonObserver<Object>() {
                    @Override
                    public void onNext(Object o) {
                        ToastUtils.showShort("修改成功");
                        MLVoiceSynthetize.startSynthesize("主人，您的体重已经修改为" + weight + "公斤");
                    }

                    @Override
                    protected void onError(ApiException ex) {
                        super.onError(ex);
                    }
                });
    }

    @Override
    protected int geTip() {
        return R.string.sign_up_weight_tip;
    }
}
