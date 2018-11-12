package com.example.han.referralproject.yiyuan.newdetect.followupfragment;

import android.os.Bundle;
import android.view.View;

import com.example.han.referralproject.bean.DataInfoBean;
import com.example.han.referralproject.network.NetworkApi;
import com.gcml.module_blutooth_devices.bloodsugar_devices.Bloodsugar_Fragment;
import com.gcml.module_blutooth_devices.utils.DataUtils;
import com.gcml.module_blutooth_devices.utils.UtilsManager;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.medlink.danbogh.utils.T;

/**
 * Created by lenovo on 2018/11/12.
 */

public class SugarFollowUpFragment extends Bloodsugar_Fragment {

    private boolean isOnPause;
    private Bundle bundle;

    @Override
    protected void initView(View view, Bundle bundle) {
        super.initView(view, bundle);
        this.bundle=bundle;
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
                    response -> T.show("数据上传成功"),
                    message -> T.show("数据上传失败"));

        }
    }

    @Override
    protected void clickHealthHistory(View view) {
        super.clickHealthHistory(view);
        if (fragmentChanged != null) {
            fragmentChanged.onFragmentChanged(this, null);
        }
    }
}
