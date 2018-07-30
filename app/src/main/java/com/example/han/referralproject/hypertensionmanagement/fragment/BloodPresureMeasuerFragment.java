package com.example.han.referralproject.hypertensionmanagement.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageSwitcher;
import android.widget.ImageView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.health.intelligentdetection.HealthBloodDetectionFragment;
import com.example.han.referralproject.health.intelligentdetection.HealthBloodDetectionUiFragment;
import com.example.han.referralproject.health.intelligentdetection.entity.ApiResponse;
import com.example.han.referralproject.health.intelligentdetection.entity.DetectionData;
import com.example.han.referralproject.hypertensionmanagement.activity.WeightMeasureActivity;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.util.LocalShared;
import com.gcml.lib_utils.display.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.util.ArrayList;

public class BloodPresureMeasuerFragment extends HealthBloodDetectionFragment {
    private int highPressure;
    private int lowPressure;
    private int pulse;
    private ImageView ivRight;

    @Override
    public void onResume() {
        super.onResume();
        startDetection();
    }

    @Override
    protected void onBloodResult(int highPressure, int lowPressure, int pulse) {
        super.onBloodResult(highPressure, lowPressure, pulse);
        this.highPressure = highPressure;
        this.lowPressure = lowPressure;
        this.pulse = pulse;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        super.initView(view, savedInstanceState);
        view.findViewById(R.id.ll_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getFragmentManager() != null) {
                    getFragmentManager().popBackStack();
                }
                getActivity().finish();
            }
        });
        ivRight = ((ImageView) view.findViewById(R.id.iv_top_right));
        ivRight.setOnClickListener(v -> startDetection());
        tvNext.setOnClickListener(v ->
                uploadData(highPressure, lowPressure, pulse));
    }

    private void uploadData(int highPressure, int lowPressure, int pulse) {
        ArrayList<DetectionData> datas = new ArrayList<>();
        DetectionData pressureData = new DetectionData();
        DetectionData dataPulse = new DetectionData();
        //detectionType (string, optional): 检测数据类型 0血压 1血糖 2心电 3体重 4体温 6血氧 7胆固醇 8血尿酸 9脉搏 ,
        pressureData.setDetectionType("0");
        pressureData.setHighPressure(highPressure);
        pressureData.setLowPressure(lowPressure);

        dataPulse.setDetectionType("9");
        dataPulse.setPulse(pulse);
        datas.add(pressureData);
        datas.add(dataPulse);

        OkGo.<String>post(NetworkApi.DETECTION_DATA+ LocalShared.getInstance(getContext()).getUserId()+"/")
                .upJson(new Gson().toJson(datas))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (!response.isSuccessful()) {
                            ToastUtils.showLong("数据上传失败");
                            return;
                        }
                        String body = response.body();
                        try {
                            ApiResponse<Object> apiResponse = new Gson().fromJson(body, new TypeToken<ApiResponse<Object>>() {
                            }.getType());
                            if (apiResponse.isSuccessful()) {
                                startActivity(new Intent(getActivity(), WeightMeasureActivity.class));
                                return;
                            }
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                        ToastUtils.showLong("数据上传失败");
                    }
                });

    }

}

