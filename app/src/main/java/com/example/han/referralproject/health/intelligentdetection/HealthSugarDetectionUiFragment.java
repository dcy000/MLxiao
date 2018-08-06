package com.example.han.referralproject.health.intelligentdetection;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.application.MyApplication;
import com.example.han.referralproject.health.intelligentdetection.entity.ApiResponse;
import com.example.han.referralproject.health.intelligentdetection.entity.DetectionData;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkCallback;
import com.example.han.referralproject.video.MeasureVideoPlayActivity;
import com.example.han.referralproject.xindian.XinDianDetectActivity;
import com.gcml.lib_utils.display.ToastUtils;
import com.gcml.module_blutooth_devices.bloodsugar_devices.Bloodsugar_Fragment;
import com.gcml.module_blutooth_devices.bloodsugar_devices.Bloodsugar_Self_PresenterImp;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.util.ArrayList;
import java.util.HashMap;

public class HealthSugarDetectionUiFragment extends Bloodsugar_Fragment {


    private int selectMeasureSugarTime;
    private boolean isJump2Next = false;

    @Override
    public void onStart() {
        super.onStart();
        mBtnVideoDemo.setVisibility(View.GONE);
        mBtnHealthHistory.setText("下一步");
        Bundle arguments = getArguments();
        if (arguments != null) {
            selectMeasureSugarTime = arguments.getInt("selectMeasureSugarTime", 0);
        }
    }

    @Override
    protected void clickHealthHistory(View view) {
        if (fragmentChanged != null && !isJump2Next) {
            isJump2Next=true;
            fragmentChanged.onFragmentChanged(this, null);
        }
    }

    @Override
    protected void onMeasureFinished(String... results) {
        if (results.length == 1) {
            if (getFragmentManager() != null) {
                Object obj = DataFragment.get(getFragmentManager()).getData();
                if (obj == null) {
                    obj = new HashMap<String, Object>();
                }
                HashMap<String, Object> dataMap = (HashMap<String, Object>) obj;
                dataMap.put("sugar", Float.parseFloat(results[0]));
            }

            ArrayList<DetectionData> datas = new ArrayList<>();
            DetectionData data = new DetectionData();
            //detectionType (string, optional): 检测数据类型 0血压 1血糖 2心电 3体重 4体温 6血氧 7胆固醇 8血尿酸 9脉搏 ,
            data.setDetectionType("1");
            data.setSugarTime(selectMeasureSugarTime);
            data.setBloodSugar(Float.parseFloat(results[0]));
            datas.add(data);
            NetworkApi.postMeasureData(datas, new NetworkCallback() {
                @Override
                public void onSuccess(String callbackString) {
                    if (fragmentChanged != null&&!isJump2Next) {
                        isJump2Next=true;
                        fragmentChanged.onFragmentChanged(HealthSugarDetectionUiFragment.this, null);
                    }
                }

                @Override
                public void onError() {
                    ToastUtils.showLong("数据上传失败");
                }
            });
        }
    }
}
