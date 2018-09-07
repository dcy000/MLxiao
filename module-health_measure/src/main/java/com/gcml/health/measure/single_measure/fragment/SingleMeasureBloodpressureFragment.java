package com.gcml.health.measure.single_measure.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.gcml.common.data.UserSpHelper;
import com.gcml.health.measure.bloodpressure_habit.GetHypertensionHandActivity;
import com.gcml.health.measure.first_diagnosis.bean.ApiResponse;
import com.gcml.health.measure.first_diagnosis.bean.DetectionData;
import com.gcml.health.measure.first_diagnosis.bean.DetectionResult;
import com.gcml.health.measure.network.HealthMeasureApi;
import com.gcml.health.measure.network.NetworkCallback;
import com.gcml.health.measure.single_measure.ShowMeasureBloodpressureResultActivity;
import com.gcml.lib_utils.UtilsManager;
import com.gcml.lib_utils.display.ToastUtils;
import com.gcml.lib_utils.ui.dialog.BaseDialog;
import com.gcml.lib_utils.ui.dialog.DialogClickSureListener;
import com.gcml.lib_utils.ui.dialog.DialogSure;
import com.gcml.module_blutooth_devices.bloodpressure_devices.Bloodpressure_Fragment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.iflytek.synthetize.MLVoiceSynthetize;

import java.util.ArrayList;
import java.util.List;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/8/6 9:54
 * created by:gzq
 * description:单次血压测量
 */
public class SingleMeasureBloodpressureFragment extends Bloodpressure_Fragment {


    public SingleMeasureBloodpressureFragment() {
    }

    @Override
    protected void initView(View view, Bundle bundle) {
        super.initView(view, bundle);
//        getHypertensionHand();
    }

    /**
     * 获取惯用手
     */
    private void getHypertensionHand() {
        String userHypertensionHand = UserSpHelper.getUserHypertensionHand();
        if (TextUtils.isEmpty(userHypertensionHand)) {
            //还没有录入惯用手，则跳转到惯用手录入activity
            mContext.startActivity(new Intent(mContext, GetHypertensionHandActivity.class));
        } else {
            if ("0".equals(userHypertensionHand)) {
                showHypertensionHandDialog("左手");
            } else if ("1".equals(userHypertensionHand)) {
                showHypertensionHandDialog("右手");
            }
        }
    }

    private void showHypertensionHandDialog(String hand) {
        DialogSure dialogSure = new DialogSure(mContext);
        dialogSure.setContent("请使用" + hand + "测量");
        dialogSure.show();
        MLVoiceSynthetize.startSynthesize(UtilsManager.getApplication(), "主人，请使用" + hand + "测量");
        dialogSure.setOnClickSureListener(new DialogClickSureListener() {
            @Override
            public void clickSure(BaseDialog dialog) {
                dialog.dismiss();
            }
        });
    }

    @Override
    protected void onMeasureFinished(String... results) {
        if (results.length == 3) {
            MLVoiceSynthetize.startSynthesize(UtilsManager.getApplication(), "主人，您本次测量高压" + results[0] + ",低压" + results[1] + ",脉搏" + results[2], false);

            ArrayList<DetectionData> datas = new ArrayList<>();
            DetectionData pressureData = new DetectionData();
            DetectionData dataPulse = new DetectionData();
            //detectionType (string, optional): 检测数据类型 0血压 1血糖 2心电 3体重 4体温 6血氧 7胆固醇 8血尿酸 9脉搏 ,
            pressureData.setDetectionType("0");
            int highPressure = Integer.parseInt(results[0]);
            pressureData.setHighPressure(highPressure);
            int lowPressure = Integer.parseInt(results[1]);
            pressureData.setLowPressure(lowPressure);
            dataPulse.setDetectionType("9");
            dataPulse.setPulse(Integer.parseInt(results[2]));
            datas.add(pressureData);
            datas.add(dataPulse);
            HealthMeasureApi.postMeasureData(datas, new NetworkCallback() {
                @Override
                public void onSuccess(String callbackString) {
                    try {
                        ApiResponse<List<DetectionResult>> apiResponse = new Gson().fromJson(callbackString,
                                new TypeToken<ApiResponse<List<DetectionResult>>>() {
                                }.getType());
                        if (apiResponse.isSuccessful()) {
                            ToastUtils.showLong("上传数据成功");
                            DetectionResult result = apiResponse.getData().get(0);
                            ShowMeasureBloodpressureResultActivity.startActivity(getContext(), result.getDiagnose(),
                                    result.getScore(), highPressure, lowPressure, result.getResult());
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError() {
                    ToastUtils.showShort("上传数据失败");
                }
            });
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        MLVoiceSynthetize.destory();
    }
}
