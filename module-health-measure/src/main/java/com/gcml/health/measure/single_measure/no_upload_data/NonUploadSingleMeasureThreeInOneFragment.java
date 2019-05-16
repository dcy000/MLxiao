package com.gcml.health.measure.single_measure.no_upload_data;

import android.os.Bundle;
import android.view.View;

import com.gcml.common.recommend.bean.post.DetectionData;
import com.gcml.common.utils.UM;
import com.gcml.module_blutooth_devices.three.ThreeInOneFragment;
import com.iflytek.synthetize.MLVoiceSynthetize;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/9/14 11:50
 * created by:gzq
 * description:TODO
 */
public class NonUploadSingleMeasureThreeInOneFragment extends ThreeInOneFragment {
    private int selectMeasureSugarTime;

    @Override
    protected void initView(View view, Bundle bundle) {
        super.initView(view, bundle);
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
    protected void onMeasureFinished(DetectionData detectionData) {
        if (detectionData.getBloodSugar() != 0) {
            MLVoiceSynthetize.startSynthesize(UM.getApp(), "您本次测量血糖" + detectionData.getBloodSugar());
            if (measureItemChanged != null) {
                measureItemChanged.onChanged(2);
            }
        }
        if (detectionData.getCholesterol() != 0) {
            MLVoiceSynthetize.startSynthesize(UM.getApp(), "您本次测量胆固醇" + detectionData.getCholesterol());
            if (measureItemChanged != null) {
                measureItemChanged.onChanged(5);
            }
        }

        if (detectionData.getUricAcid() != 0) {
            MLVoiceSynthetize.startSynthesize(UM.getApp(), "您本次测量尿酸" + detectionData.getUricAcid());
            if (measureItemChanged != null) {
                measureItemChanged.onChanged(6);
            }
        }
    }
}
