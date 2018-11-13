package com.example.han.referralproject.yiyuan.newdetect.followupfragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.example.han.referralproject.bean.DataInfoBean;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.util.LocalShared;
import com.gcml.module_blutooth_devices.utils.UtilsManager;
import com.gcml.module_blutooth_devices.weight_devices.Weight_Fragment;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.medlink.danbogh.utils.T;

/**
 * Created by lenovo on 2018/11/12.
 */

public class WeightFollowUpFragment extends Weight_Fragment {

    private boolean isOnResume;

    @Override
    public void onStart() {
        super.onStart();
        mBtnVideoDemo.setVisibility(View.GONE);
        mBtnHealthHistory.setText("下一步");
    }

    @Override
    public void onPause() {
        super.onPause();
        isOnResume = true;
    }

    @Override
    public void onResume() {
        super.onResume();
        isOnResume = false;
    }

    @Override
    protected void onMeasureFinished(String... results) {
        super.onMeasureFinished(results);
        if (results.length == 1 && isOnResume) {

            //得到身高和体重，再计算一下体质
            if (mTvTizhi != null) {
                String height = LocalShared.getInstance(mContext).getUserHeight();
                if (!TextUtils.isEmpty(height)) {
                    float parseFloat = Float.parseFloat(height);
                    float weight = Float.parseFloat(results[0]);
                    if (mTvTizhi != null) {
                        mTvTizhi.setText(String.format("%.2f", weight / (parseFloat * parseFloat / 10000)));
                    }
                }
            }
            data.putString("weight", results[0]);
            MLVoiceSynthetize.startSynthesize(UtilsManager.getApplication(), "主人，您本次测量体重" + results[0] + "公斤", false);
            DataInfoBean info = new DataInfoBean();
            info.weight = Float.parseFloat(results[0]);
            NetworkApi.postData(info, response -> {
                T.show("数据上传成功");
            }, message -> {
                T.show("数据上传失败");
            });

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
