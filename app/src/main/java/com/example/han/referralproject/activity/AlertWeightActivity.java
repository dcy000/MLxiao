package com.example.han.referralproject.activity;

import android.view.View;

import com.example.han.referralproject.R;
import com.example.han.referralproject.application.MyApplication;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.example.han.referralproject.new_music.ToastUtils;
import com.example.han.referralproject.util.ToastTool;
import com.iflytek.synthetize.MLVoiceSynthetize;

import java.util.ArrayList;
import java.util.List;

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
        NetworkApi.alertBasedata(MyApplication.getInstance().userId, data.height, weight, eat, smoke, drink, exercise,
                buffer==null?"":buffer.substring(0,buffer.length()-1),data.dz,new NetworkManager.SuccessCallback<Object>() {
            @Override
            public void onSuccess(Object response) {
                ToastTool.showShort("修改成功");
                MLVoiceSynthetize.startSynthesize("主人，您的体重已经修改为"+weight+"千克");
            }
        }, new NetworkManager.FailedCallback() {
            @Override
            public void onFailed(String message) {

            }
        });
    }
    @Override
    protected int geTip() {
        return R.string.sign_up_weight_tip;
    }
}
