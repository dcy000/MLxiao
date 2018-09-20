package com.gcml.old.auth.profile;

import com.billy.cc.core.component.CC;
import com.example.han.referralproject.R;
import com.example.han.referralproject.network.NetworkApi;
import com.gcml.common.data.UserSpHelper;
import com.gcml.common.widget.toolbar.ToolBarClickListener;
import com.gcml.old.auth.entity.PUTUserBean;
import com.google.gson.Gson;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AlertWeightActivity extends AlertHeightActivity {

    @Override
    protected void initView() {
        super.initView();
//        mToolbar.setVisibility(View.VISIBLE);
//        mTitleText.setText("修改体重");

        toolBar.setData("修改体重", R.drawable.common_icon_back, "返回",
                R.drawable.common_icon_home, null, new ToolBarClickListener() {
                    @Override
                    public void onLeftClick() {
                        finish();
                    }

                    @Override
                    public void onRightClick() {
                        CC.obtainBuilder("com.gcml.old.home")
                                .build()
                                .callAsync();
                        finish();
                    }
                });


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
//        NetworkApi.alertBasedata(MyApplication.getInstance().userId, data.height, weight, eat, smoke, drink, exercise,
//                TextUtils.isEmpty(buffer) ? "" : buffer.substring(0, buffer.length() - 1), data.dz, new NetworkManager.SuccessCallback<Object>() {
//                    @Override
//                    public void onSuccess(Object response) {
//                        ToastUtils.showShort("修改成功");
//                        speak("主人，您的体重已经修改为" + weight + "千克");
//                    }
//                }, new NetworkManager.FailedCallback() {
//                    @Override
//                    public void onFailed(String message) {
//
//                    }
//                });

        PUTUserBean bean = new PUTUserBean();
        bean.bid = Integer.parseInt(UserSpHelper.getUserId());
        bean.weight = weight;

        NetworkApi.putUserInfo(bean.bid, new Gson().toJson(bean), new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                String body = response.body();
                try {
                    JSONObject json = new JSONObject(body);
                    boolean tag = json.getBoolean("tag");
                    if (tag) {
                        runOnUiThread(() -> speak("修改成功"));
                        finish();
                    } else {
                        runOnUiThread(() -> speak("修改失败"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

//        UserEntity user = new UserEntity();
//        user.weight = weight;
//        CCResult result = CC.obtainBuilder("com.gcml.auth.putUser")
//                .addParam("user", user)
//                .build()
//                .call();
//        Observable<Object> data = result.getDataItem("data");
//        data.subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .as(RxUtils.autoDisposeConverter(this))
//                .subscribe(new DefaultObserver<Object>() {
//                    @Override
//                    public void onNext(Object o) {
//                        super.onNext(o);
//                        runOnUiThread(() -> speak("修改成功"));
//                    }
//
//                    @Override
//                    public void onError(Throwable throwable) {
//                        super.onError(throwable);
//                        runOnUiThread(() -> speak("修改失败"));
//                    }
//                });
    }

    @Override
    protected int geTip() {
        return R.string.sign_up_weight_tip;
    }


    private void speak(String text) {
        MLVoiceSynthetize.startSynthesize(getApplicationContext(), text);
    }
}
