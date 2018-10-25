package com.gcml.auth.ui.profile.update;

import com.billy.cc.core.component.CC;
import com.gcml.auth.R;
import com.gcml.common.data.UserEntity;
import com.gcml.common.repository.utils.DefaultObserver;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.common.widget.toolbar.ToolBarClickListener;
import com.iflytek.synthetize.MLVoiceSynthetize;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class AlertWeightActivity extends AlertHeightActivity {

    @Override
    protected void initView() {
        super.initView();
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

        UserEntity user = new UserEntity();
        user.weight = weight;
        Observable<UserEntity> data = CC.obtainBuilder("com.gcml.auth.putUser")
                .addParam("user", user)
                .build()
                .call()
                .getDataItem("data");
        data.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new DefaultObserver<UserEntity>() {
                    @Override
                    public void onNext(UserEntity o) {
                        speak("修改成功");
                        ToastUtils.showShort("修改成功");
                        finish();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        speak("修改失败");
                        ToastUtils.showShort("修改失败");
                    }
                });
    }

    @Override
    protected int geTip() {
        return R.string.common_sign_up_weight_tip;
    }

    private void speak(String text) {
        MLVoiceSynthetize.startSynthesize(getApplicationContext(), text);
    }
}
