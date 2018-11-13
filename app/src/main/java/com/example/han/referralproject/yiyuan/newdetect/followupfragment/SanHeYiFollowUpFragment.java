package com.example.han.referralproject.yiyuan.newdetect.followupfragment;

import android.os.Bundle;
import android.view.View;

import com.example.han.referralproject.bean.DataInfoBean;
import com.example.han.referralproject.network.NetworkApi;
import com.gcml.module_blutooth_devices.others.ThreeInOne_Fragment;
import com.gcml.module_blutooth_devices.utils.UtilsManager;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.medlink.danbogh.utils.T;

/**
 * Created by lenovo on 2018/11/12.
 */

public class SanHeYiFollowUpFragment extends ThreeInOne_Fragment {
    private Bundle bundle;
    private int selectMeasureSugarTime;
    private boolean isOnResume;

    @Override
    protected void initView(View view, Bundle bundle) {
        super.initView(view, bundle);
        this.bundle = bundle;
        if (bundle != null) {
            selectMeasureSugarTime = bundle.getInt("selectMeasureSugarTime");
        }
        mTitle11.setText("<3.9");
        switch (selectMeasureSugarTime) {
            case 0:
                //空腹
                mTitle1.setText("血糖(空腹)");
                mTitle12.setText("3.9~6.1");
                mTitle13.setText(">6.1");
                break;
            case 1:
                //饭后1小时
                mTitle1.setText("血糖(饭后1小时)");
                mTitle12.setText("3.9~7.8");
                mTitle13.setText(">7.8");
                break;
            case 2:
                //饭后2小时
                mTitle1.setText("血糖(饭后2小时)");
                mTitle12.setText("3.9~7.8");
                mTitle13.setText(">7.8");
                break;
            case 3:
                //其他时间
                mTitle1.setText("血糖(其他时间)");
                mTitle12.setText("3.9~11.1");
                mTitle13.setText(">11.1");
                break;
            default:
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        isOnResume = true;
    }

    @Override
    public void onPause() {
        super.onPause();
        isOnResume = false;
    }

    Bundle data = new Bundle();

    @Override
    protected void onMeasureFinished(String... results) {
        super.onMeasureFinished(results);
        if (results.length == 2 && isOnResume) {
            DataInfoBean info = new DataInfoBean();
            if (results[0].equals("bloodsugar")) {
                info.blood_sugar = results[1];
                MLVoiceSynthetize.startSynthesize(UtilsManager.getApplication(), "主人，您本次测量血糖" + results[1]);
            }
            if (results[0].equals("cholesterol")) {
                info.cholesterol = results[1];
                data.putString("cholesterol", results[1]);
                MLVoiceSynthetize.startSynthesize(UtilsManager.getApplication(), "主人，您本次测量胆固醇" + results[1]);
            }

            if (results[0].equals("bua")) {
                info.uric_acid = results[1];
                data.putString("niaosuan", results[1]);
                MLVoiceSynthetize.startSynthesize(UtilsManager.getApplication(), "主人，您本次测量尿酸" + results[1]);
            }

            NetworkApi.postData(info, response -> {
                T.show("数据上传成功");
            }, message -> {
                T.show("数据上传失败");
            });


        }
    }

    @Override
    protected void clickHealthHistory(View view) {
        super.clickHealthHistory(view);
        if (fragmentChanged != null) {
            fragmentChanged.onFragmentChanged(this, data);
        }
    }
}

