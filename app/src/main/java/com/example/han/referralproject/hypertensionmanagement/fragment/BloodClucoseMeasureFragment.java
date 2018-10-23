package com.example.han.referralproject.hypertensionmanagement.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.health.intelligentdetection.HealthSugarDetectionFragment;
import com.example.han.referralproject.health.intelligentdetection.entity.DetectionData;
import com.example.han.referralproject.hypertensionmanagement.activity.WeightMeasureActivity;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkCallback;
import com.gcml.common.utils.display.ToastUtils;

import java.util.ArrayList;

/**
 * Created by lenovo on 2018/7/30.
 */

public class BloodClucoseMeasureFragment extends HealthSugarDetectionFragment {
    private TextView tvNext;
    private float sugar;
    private ImageView ivRight;

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        super.initView(view, savedInstanceState);
        ((TextView) view.findViewById(R.id.tv_top_title)).setText(R.string.test_xuetang);
        view.findViewById(R.id.ll_back).setOnClickListener(v -> {
            if (getFragmentManager() != null) {
                getFragmentManager().popBackStack();
            }
            getActivity().finish();
        });
        ivRight = view.findViewById(R.id.iv_top_right);
        ivRight.setImageResource(R.drawable.health_ic_blutooth);
        ivRight.setOnClickListener(v -> startDetection());
        tvNext = view.findViewById(R.id.tv_xuetang);
        tvNext.setOnClickListener(v -> {
            postBloodClucoseData(sugar);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        startDetection();
    }

    protected void onSugarResult(float sugar) {
        this.sugar = sugar;

    }

    private void postBloodClucoseData(float sugar) {
        ArrayList<DetectionData> datas = new ArrayList<>();
        DetectionData data = new DetectionData();
        //detectionType (string, optional): 检测数据类型 0血压 1血糖 2心电 3体重 4体温 6血氧 7胆固醇 8血尿酸 9脉搏 ,
        data.setDetectionType("1");
        data.setBloodSugar(sugar);
        datas.add(data);
        NetworkApi.postMeasureData(datas, new NetworkCallback() {
            @Override
            public void onSuccess(String callback) {
                startActivity(new Intent(getActivity(), WeightMeasureActivity.class));
            }

            @Override
            public void onError() {
                ToastUtils.showLong("数据上传失败");
            }
        });
//        OkGo.<String>post(NetworkApi.DETECTION_DATA + LocalShared.getInstance(getContext()).getUserId() + "/")
//                .upJson(new Gson().toJson(datas))
//                .execute(new StringCallback() {
//                    @Override
//                    public void onSuccess(Response<String> response) {
//                        if (!response.isSuccessful()) {
//                            ToastUtils.showLong("数据上传失败");
//                            return;
//                        }
//                        String body = response.body();
//                        try {
//                            ApiResponse<Object> apiResponse = new Gson().fromJson(body, new TypeToken<ApiResponse<Object>>() {
//                            }.getType());
//                            if (apiResponse.isSuccessful()) {
//                                startActivityForResult(new Intent(getActivity(), WeightMeasureActivity.class));
//                                return;
//                            }
//                        } catch (Throwable e) {
//                            e.printStackTrace();
//                        }
//                        ToastUtils.showLong("数据上传失败");
//                    }
//                });
    }


}
