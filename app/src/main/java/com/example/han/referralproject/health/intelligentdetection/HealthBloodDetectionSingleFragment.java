package com.example.han.referralproject.health.intelligentdetection;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.application.MyApplication;
import com.example.han.referralproject.health.intelligentdetection.entity.ApiResponse;
import com.example.han.referralproject.health.intelligentdetection.entity.DetectionData;
import com.example.han.referralproject.health.intelligentdetection.entity.DetectionResult;
import com.example.han.referralproject.measure.NewMeasureBloodpressureResultActivity;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.video.MeasureVideoPlayActivity;
import com.gcml.lib_utils.display.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.util.ArrayList;

public class HealthBloodDetectionSingleFragment extends HealthBloodDetectionFragment {
    private ImageView ivRight;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
//        Uri uri = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.tips_xueya);
//        MeasureVideoPlayActivity.startActivity(this, MeasureVideoPlayActivity.class, uri, null, "血压测量演示视频");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MeasureVideoPlayActivity.REQUEST_PALY_VIDEO) {
            if (getView() != null) {
                getView().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                    }
                }, 0);
            }
        }
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        super.initView(view, savedInstanceState);
        ivRight = ((ImageView) view.findViewById(R.id.iv_top_right));
        tvDetectionAgain.setVisibility(View.VISIBLE);
        view.findViewById(R.id.ll_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getFragmentManager() != null) {
                    getFragmentManager().popBackStack();
                }
            }
        });
        ivRight.setImageResource(R.drawable.health_ic_blutooth);
        ivRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDetection();
            }
        });
        tvDetectionAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = second ? secondTips : firstTips;
                ToastUtils.showShortOnTop(message);
                startDetection();
            }
        });
        tvNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navToNext();
            }
        });
    }

    private String firstTips = "1".equals(MyApplication.getInstance().hypertensionHand)
            ? getString(R.string.tips_detection_right)
            : getString(R.string.tips_detection_left);
    private String secondTips = "1".equals(MyApplication.getInstance().hypertensionHand)
            ? getString(R.string.tips_detection_right_again)
            : getString(R.string.tips_detection_left_again);

    private boolean second;

    private int firstHighPressure;
    private int firstLowPressure;
    private int firstPulse;

    private int secondHighPressure;
    private int secondLowPressure;
    private int secondPulse;

    @Override
    protected void onBloodResult(int highPressure, int lowPressure, int pulse) {
        if (!second) {
            firstHighPressure = highPressure;
            firstLowPressure = lowPressure;
            firstPulse = pulse;
        } else {
            secondHighPressure = highPressure;
            secondLowPressure = lowPressure;
            secondPulse = pulse;
            uploadData();
        }
    }

    private void uploadData() {
        ArrayList<DetectionData> datas = new ArrayList<>();
        DetectionData pressureData = new DetectionData();
        DetectionData dataPulse = new DetectionData();
        //detectionType (string, optional): 检测数据类型 0血压 1血糖 2心电 3体重 4体温 6血氧 7胆固醇 8血尿酸 9脉搏 ,
        pressureData.setDetectionType("0");
        pressureData.setHighPressure(firstHighPressure + secondHighPressure / 2);
        pressureData.setLowPressure(firstLowPressure + secondLowPressure / 2);
        dataPulse.setDetectionType("9");
        dataPulse.setPulse(firstPulse + secondPulse / 2);
        datas.add(pressureData);
        datas.add(dataPulse);
        OkGo.<String>post(NetworkApi.DETECTION_DATA + MyApplication.getInstance().userId + "/")
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
                            ApiResponse<DetectionResult> apiResponse = new Gson().fromJson(body, new TypeToken<ApiResponse<DetectionResult>>() {
                            }.getType());
                            if (apiResponse.isSuccessful()) {
                                ToastUtils.showLong("数据上传成功");
                                result = apiResponse.getData();
                                return;
                            }
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                        ToastUtils.showLong("数据上传失败");
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        ToastUtils.showLong("数据上传失败");
                    }
                });
    }

    private DetectionResult result;

    private void navToNext() {
        NewMeasureBloodpressureResultActivity.startActivity(getContext(),result.getDiagnose(),
                result.getScore(),0,0,result.getResult());
    }
}
