package com.gcml.old.auth.profile;

import android.text.TextUtils;
import android.view.View;

import com.example.han.referralproject.R;
import com.example.han.referralproject.application.MyApplication;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.gcml.lib_utils.display.ToastUtils;

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
                TextUtils.isEmpty(buffer) ? "" : buffer.substring(0, buffer.length() - 1), data.dz, new NetworkManager.SuccessCallback<Object>() {
                    @Override
                    public void onSuccess(Object response) {
                        ToastUtils.showShort("修改成功");
                        speak("主人，您的体重已经修改为" + weight + "千克");
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
