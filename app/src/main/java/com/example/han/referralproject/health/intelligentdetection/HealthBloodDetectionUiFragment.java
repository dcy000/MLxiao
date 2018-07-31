package com.example.han.referralproject.health.intelligentdetection;

import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.IntDef;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.SparseIntArray;
import android.view.View;
import android.widget.ImageView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.application.MyApplication;
import com.example.han.referralproject.health.intelligentdetection.entity.ApiResponse;
import com.example.han.referralproject.health.intelligentdetection.entity.DetectionData;
import com.example.han.referralproject.network.NetworkApi;
import com.gcml.lib_utils.display.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HealthBloodDetectionUiFragment extends HealthBloodDetectionFragment {

    private ImageView ivRight;

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        super.initView(view, savedInstanceState);
        ivRight = ((ImageView) view.findViewById(R.id.iv_top_right));
        tvDetectionAgain.setVisibility(View.GONE);
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
        tvNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchNextDetectionStep();
            }
        });
    }

    private SparseIntArray highPressures = new SparseIntArray();
    private SparseIntArray lowPressures = new SparseIntArray();
    private SparseIntArray pulses = new SparseIntArray();

    private View.OnClickListener actionOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startDetection();
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        notifyDetectionStepChanged(detectionStep);
    }

    @Override
    protected void onBloodResult(int highPressure, int lowPressure, int pulse) {
        highPressures.put(detectionStep, highPressure);
        lowPressures.put(detectionStep, lowPressure);
        pulses.put(detectionStep, pulse);
    }

    private void uploadDataIfDone() {

    }

    @IntDef({
            DetectionStep.LEFT_1,
            DetectionStep.LEFT_2,
            DetectionStep.LEFT_3,
            DetectionStep.RIGHT_1,
            DetectionStep.RIGHT_2,
            DetectionStep.RIGHT_3,
            DetectionStep.DONE
    })
    private @interface DetectionStep {
        int LEFT_1 = 1;
        int LEFT_2 = 2;
        int LEFT_3 = 3;
        int RIGHT_1 = 4;
        int RIGHT_2 = 5;
        int RIGHT_3 = 6;
        int DONE = 7;
    }

    @DetectionStep
    private int detectionStep = DetectionStep.LEFT_1;

    private boolean hasLeft3;

    private boolean hasRight3;


    private void dispatchNextDetectionStep() {
        switch (detectionStep) {
            case DetectionStep.LEFT_1:
                notifyDetectionStepChanged(DetectionStep.LEFT_2);
                break;
            case DetectionStep.LEFT_2:
                if (5 < Math.abs(highPressures.get(DetectionStep.LEFT_1)
                        - highPressures.get(DetectionStep.LEFT_2))
                        || 5 < Math.abs(lowPressures.get(DetectionStep.LEFT_1)
                        - lowPressures.get(DetectionStep.LEFT_2))) {
                    notifyDetectionStepChanged(DetectionStep.LEFT_3);
                } else {
                    notifyDetectionStepChanged(DetectionStep.RIGHT_1);
                }
                break;
            case DetectionStep.LEFT_3:
                notifyDetectionStepChanged(DetectionStep.RIGHT_1);
                break;
            case DetectionStep.RIGHT_1:
                notifyDetectionStepChanged(DetectionStep.RIGHT_2);
                break;
            case DetectionStep.RIGHT_2:
                if (5 < Math.abs(highPressures.get(DetectionStep.RIGHT_1)
                        - highPressures.get(DetectionStep.RIGHT_2))
                        || 5 < Math.abs(lowPressures.get(DetectionStep.RIGHT_1)
                        - lowPressures.get(DetectionStep.RIGHT_2))) {
                    notifyDetectionStepChanged(DetectionStep.RIGHT_3);
                } else {
                    notifyDetectionStepChanged(DetectionStep.DONE);
                }
                break;
            case DetectionStep.RIGHT_3:
                notifyDetectionStepChanged(DetectionStep.DONE);
                break;
            case DetectionStep.DONE:
                notifyDetectionStepChanged(DetectionStep.DONE);
                break;
        }
    }

    private void notifyDetectionStepChanged(@DetectionStep int detectionStep) {
        this.detectionStep = detectionStep;
        switch (detectionStep) {
            case DetectionStep.LEFT_1:
                showTips(
                        getString(R.string.health_tips_left_1),
                        "2–3次",
                        R.color.f56c6c,
                        actionOnClickListener);
                break;
            case DetectionStep.LEFT_2:
                showTips(
                        getString(R.string.health_tips_left_2),
                        "",
                        0,
                        actionOnClickListener);
                break;
            case DetectionStep.LEFT_3:
                hasLeft3 = true;
                showTips(
                        getString(R.string.health_tips_left_3),
                        "",
                        0,
                        actionOnClickListener);
                break;
            case DetectionStep.RIGHT_1:
                showTips(
                        getString(R.string.health_tips_right_1),
                        "",
                        0,
                        actionOnClickListener);
                break;
            case DetectionStep.RIGHT_2:
                showTips(
                        getString(R.string.health_tips_right_2),
                        "",
                        0,
                        actionOnClickListener);
                break;
            case DetectionStep.RIGHT_3:
                hasRight3 = true;
                showTips(
                        getString(R.string.health_tips_right_3),
                        "",
                        0,
                        actionOnClickListener);
                break;
            case DetectionStep.DONE:
                int detectionCount = 4;
                if (hasLeft3) {
                    detectionCount++;
                }
                if (hasRight3) {
                    detectionCount++;
                }
                if (highPressures.size() != detectionCount) {
                    navToNext();
                    return;
                }
                uploadHandData(prepareData());
                break;
        }
    }

    private void uploadHandData(Data data) {
        if (getFragmentManager() != null) {
            DataCacheFragment.get(getFragmentManager()).getDataCache().put("pressure", data);
        }
        OkGo.<String>post(NetworkApi.DETECTION_BLOOD_HAND + MyApplication.getInstance().userId + "/")
                .params("handState", data.right)
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
                                uploadData(data);
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

    private void uploadData(Data data) {
        ArrayList<DetectionData> datas = new ArrayList<>();
        DetectionData pressureData = new DetectionData();
        DetectionData pulseData = new DetectionData();
        //detectionType (string, optional): 检测数据类型 0血压 1血糖 2心电 3体重 4体温 6血氧 7胆固醇 8血尿酸 9脉搏 ,
        pressureData.setDetectionType(data.type);
        int highPressure = data.right == 1 ? data.rightHighPressure : data.leftHighPressure;
        pressureData.setHighPressure(highPressure);
        int lowPressure = data.right == 1 ? data.rightLowPressure : data.leftLowPressure;
        pressureData.setLowPressure(lowPressure);
        pulseData.setDetectionType("9");
        int pulse = data.right == 1 ? data.rightPulse : data.leftPulse;
        pulseData.setPulse(pulse);
        datas.add(pressureData);
        datas.add(pulseData);
        DataCacheFragment dataCacheFragment = DataCacheFragment.get(getFragmentManager());
        HashMap<String, Object> dataCache = dataCacheFragment.getDataCache();
        List<DetectionData> dataList = (List<DetectionData>) dataCache.get("dataList");
        if (dataList == null) {
            dataList = new ArrayList<>();
            dataCache.put("dataList", dataList);
        }
        dataList.add(pressureData);
        dataList.add(pulseData);

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
                            ApiResponse<Object> apiResponse = new Gson().fromJson(body, new TypeToken<ApiResponse<Object>>() {
                            }.getType());
                            if (apiResponse.isSuccessful()) {
                                navToNext();
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

    private void navToNext() {
        FragmentManager fm = getFragmentManager();
        if (fm == null) {
            return;
        }
        FragmentTransaction transaction = fm.beginTransaction();
        Fragment fragment = new HealthWeightDetectionUiFragment();
        transaction.replace(R.id.fl_container, fragment);
        transaction.addToBackStack(null);
        transaction.commitAllowingStateLoss();
    }

    /**
     * @return prepareData = int[1], lowPressure = int[2], pulse = int[3], left = int[4] == 1
     */
    private Data prepareData() {
        int leftHighPressure;
        int rightHighPressure;
        if (hasLeft3 && highPressures.get(DetectionStep.LEFT_3) > 0) {
            leftHighPressure = (highPressures.get(DetectionStep.LEFT_1)
                    + highPressures.get(DetectionStep.LEFT_2)
                    + highPressures.get(DetectionStep.LEFT_3)) / 3;
        } else {
            leftHighPressure = (highPressures.get(DetectionStep.LEFT_1)
                    + highPressures.get(DetectionStep.LEFT_2)) / 2;
        }

        if (hasRight3 && highPressures.get(DetectionStep.RIGHT_3) > 0) {
            rightHighPressure = (highPressures.get(DetectionStep.RIGHT_1)
                    + highPressures.get(DetectionStep.RIGHT_2)
                    + highPressures.get(DetectionStep.RIGHT_3)) / 3;
        } else {
            rightHighPressure = (highPressures.get(DetectionStep.RIGHT_1)
                    + highPressures.get(DetectionStep.RIGHT_2)) / 2;
        }

        int leftLowPressure;
        int rightLowPressure;
        if (hasLeft3 && lowPressures.get(DetectionStep.LEFT_3) > 0) {
            leftLowPressure = (lowPressures.get(DetectionStep.LEFT_1)
                    + lowPressures.get(DetectionStep.LEFT_2)
                    + lowPressures.get(DetectionStep.LEFT_3)) / 3;
        } else {
            leftLowPressure = (lowPressures.get(DetectionStep.LEFT_1)
                    + lowPressures.get(DetectionStep.LEFT_2)) / 2;
        }

        if (hasRight3 && lowPressures.get(DetectionStep.RIGHT_3) > 0) {
            rightLowPressure = (lowPressures.get(DetectionStep.RIGHT_1)
                    + lowPressures.get(DetectionStep.RIGHT_2)
                    + lowPressures.get(DetectionStep.RIGHT_3)) / 3;
        } else {
            rightLowPressure = (lowPressures.get(DetectionStep.RIGHT_1)
                    + lowPressures.get(DetectionStep.RIGHT_2)) / 2;
        }

        int leftPulse;
        int rightPulse;
        if (hasLeft3 && pulses.get(DetectionStep.LEFT_3) > 0) {
            leftPulse = (pulses.get(DetectionStep.LEFT_1)
                    + pulses.get(DetectionStep.LEFT_2)
                    + pulses.get(DetectionStep.LEFT_3)) / 3;
        } else {
            leftPulse = (pulses.get(DetectionStep.LEFT_1)
                    + pulses.get(DetectionStep.LEFT_2)) / 2;
        }

        if (hasRight3 && pulses.get(DetectionStep.RIGHT_3) > 0) {
            rightPulse = (pulses.get(DetectionStep.RIGHT_1)
                    + pulses.get(DetectionStep.RIGHT_2)
                    + pulses.get(DetectionStep.RIGHT_3)) / 3;
        } else {
            rightPulse = (pulses.get(DetectionStep.RIGHT_1)
                    + pulses.get(DetectionStep.RIGHT_2)) / 2;
        }

        Data data = new Data();
        data.right = rightHighPressure > leftHighPressure ? 1 : 0;
        data.leftHighPressure = leftHighPressure;
        data.rightHighPressure = rightHighPressure;
        data.leftLowPressure = leftLowPressure;
        data.rightLowPressure = rightLowPressure;
        data.leftPulse = leftPulse;
        data.rightPulse = rightPulse;
        return data;
    }

    private void showTips(
            String tips,
            String colorText,
            @ColorRes int color,
            View.OnClickListener actionOnClickListener) {
        CommonTipsDialogFragment df = CommonTipsDialogFragment.newInstance(tips, colorText, color);
        df.setActionOnClickListener(actionOnClickListener);
        FragmentManager fm = getFragmentManager();
        String tag = CommonTipsDialogFragment.class.getName();
//        Fragment fragment = fm.findFragmentByTag(tag);
//        if (fragment != null) {
//            fm.beginTransaction().remove(fragment).commitAllowingStateLoss();
//        }
        df.show(fm, tag);
    }

    public static class Data {
        public String type = "0";
        public int right; // { 0:left, 1: right }
        public int leftHighPressure;
        public int rightHighPressure;
        public int leftLowPressure;
        public int rightLowPressure;
        public int leftPulse;
        public int rightPulse;
    }
}
