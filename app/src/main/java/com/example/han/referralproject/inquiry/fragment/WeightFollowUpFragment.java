package com.example.han.referralproject.inquiry.fragment;

import android.os.Bundle;
import android.view.View;

import com.example.han.referralproject.bean.DataInfoBean;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.util.LocalShared;
import com.gcml.common.utils.UM;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.module_blutooth_devices.weight.WeightFragment;
import com.iflytek.synthetize.MLVoiceSynthetize;

/**
 * Created by lenovo on 2018/11/12.
 */

public class WeightFollowUpFragment extends WeightFragment {

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
        isOnResume = false;
    }

    @Override
    public void onResume() {
        super.onResume();
        isOnResume = true;
    }

    @Override
    protected void onMeasureFinished(String... results) {
        super.onMeasureFinished(results);
        if (results.length == 1 && isOnResume) {

            //得到身高和体重，再计算一下体质
            if (mTvTizhi != null) {
                String height = LocalShared.getInstance(mContext).getUserHeight();
                try {
                    float parseFloat = Float.parseFloat(height);
                    if (parseFloat > 0) {
                        float weight = Float.parseFloat(results[0]);
                        if (mTvTizhi != null) {
                            height = String.format("%.2f", weight / (parseFloat * parseFloat / 10000));
                            mTvTizhi.setText(height);
                        }
                    } else {
                        mTvTizhi.setText("--");
                    }
                } catch (Exception e) {
                }
            }
            data.putString("weight", results[0]);
            MLVoiceSynthetize.startSynthesize(UM.getApp(), "主人，您本次测量体重" + results[0] + "公斤", false);
            DataInfoBean info = new DataInfoBean();
            info.weight = Float.parseFloat(results[0]);
            NetworkApi.postData(info, response -> {
                ToastUtils.showShort("数据上传成功");
            }, message -> {
                ToastUtils.showShort("数据上传失败");
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
