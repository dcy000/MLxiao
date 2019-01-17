package com.example.han.referralproject.inquiry.fragment;

import android.os.Bundle;
import android.view.View;

import com.example.han.referralproject.bean.DataInfoBean;
import com.example.han.referralproject.network.NetworkApi;
import com.gcml.common.utils.UtilsManager;
import com.gcml.common.utils.data.DataUtils;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.module_blutooth_devices.bloodsugar.BloodSugarFragment;
import com.iflytek.synthetize.MLVoiceSynthetize;

/**
 * Created by lenovo on 2018/11/12.
 */

public class SugarFollowUpFragment extends BloodSugarFragment {

    private boolean isOnPause;
    private Bundle bundle;

    @Override
    protected void initView(View view, Bundle bundle) {
        super.initView(view, bundle);
        this.bundle = bundle;
    }

    @Override
    public void onStart() {
        super.onStart();
        mBtnVideoDemo.setVisibility(View.GONE);
        mBtnHealthHistory.setText("下一步");
    }

    @Override
    public void onResume() {
        super.onResume();
        isOnPause = false;
    }

    @Override
    public void onPause() {
        super.onPause();
        isOnPause = true;
    }

    @Override
    protected void onMeasureFinished(String... results) {
        if (results.length == 1 && !isOnPause) {
            data.putString("sugar", results[0]);
            String roundUp = DataUtils.getRoundUp(results[0], 1);
            MLVoiceSynthetize.startSynthesize(UtilsManager.getApplication(), "主人，您本次测量血糖" + roundUp, false);
            DataInfoBean info = new DataInfoBean();
            info.blood_sugar = roundUp;
            info.upload_state = true;
            if (bundle != null) {
                info.sugar_time = bundle.getInt("selectMeasureSugarTime") + "";
            } else {
                info.sugar_time = 0 + "";
            }
            NetworkApi.postData(info,
                    response -> ToastUtils.showShort("数据上传成功"),
                    message -> ToastUtils.showShort("数据上传失败"));

        }
    }

    Bundle data = new Bundle();

    @Override
    protected void clickHealthHistory(View view) {
        super.clickHealthHistory(view);
        if (fragmentChanged != null) {
            fragmentChanged.onFragmentChanged(this, data);
        }
    }
}
