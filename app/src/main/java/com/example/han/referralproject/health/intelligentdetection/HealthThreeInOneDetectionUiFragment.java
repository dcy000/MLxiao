package com.example.han.referralproject.health.intelligentdetection;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.health.intelligentdetection.entity.ApiResponse;
import com.example.han.referralproject.health.intelligentdetection.entity.DetectionData;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkCallback;
import com.example.han.referralproject.video.MeasureVideoPlayActivity;
import com.gcml.lib_utils.display.ToastUtils;
import com.gcml.module_blutooth_devices.others.ThreeInOne_Fragment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.util.ArrayList;
import java.util.HashMap;

public class HealthThreeInOneDetectionUiFragment extends ThreeInOne_Fragment {
    private boolean isJump2Next = false;

    @Override
    public void onStart() {
        super.onStart();
        mBtnVideoDemo.setVisibility(View.GONE);
        mBtnHealthHistory.setText("下一步");
    }

    private HashMap<String, Float> results = new HashMap<>();

    @Override
    protected void onMeasureFinished(String... result) {
        if (result.length == 2) {
            results.put(result[0], Float.parseFloat(result[1]));
        }
    }

    @Override
    protected void clickHealthHistory(View view) {
        uploadData();
    }

    private void uploadData() {
        if (getFragmentManager() != null) {
            Object obj = DataFragment.get(getFragmentManager()).getData();
            if (obj == null) {
                obj = new HashMap<String, Object>();
            }
            HashMap<String, Object> dataMap = (HashMap<String, Object>) obj;
            dataMap.put("threeInOne", results);
        }
        if (results.size() == 0) {
            if (fragmentChanged != null && !isJump2Next) {
                isJump2Next = true;
                fragmentChanged.onFragmentChanged(this, null);
            }
            return;
        }
        ArrayList<DetectionData> datas = new ArrayList<>();
        DetectionData sugarData = new DetectionData();
        DetectionData cholesterolData = new DetectionData();
        DetectionData lithicAcidData = new DetectionData();
        //detectionType (string, optional): 检测数据类型 0血压 1血糖 2心电 3体重 4体温 6血氧 7胆固醇 8血尿酸 9脉搏 ,
        sugarData.setDetectionType("1");
        sugarData.setSugarTime(0);
        sugarData.setBloodSugar(results.get("bloodsugar"));
        cholesterolData.setDetectionType("7");
        cholesterolData.setCholesterol(results.get("cholesterol"));
        lithicAcidData.setDetectionType("8");
        lithicAcidData.setUricAcid(results.get("bua"));
        datas.add(sugarData);
        datas.add(cholesterolData);
        datas.add(lithicAcidData);
        NetworkApi.postMeasureData(datas, new NetworkCallback() {
            @Override
            public void onSuccess(String callbackString) {
                if (fragmentChanged != null && !isJump2Next) {
                    isJump2Next = true;
                    fragmentChanged.onFragmentChanged(
                            HealthThreeInOneDetectionUiFragment.this, null);
                }
            }

            @Override
            public void onError() {
                ToastUtils.showLong("数据上传失败");
            }
        });
    }
}
