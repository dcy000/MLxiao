package com.example.han.referralproject.health.intelligentdetection;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
import com.example.han.referralproject.video.MeasureVideoPlayActivity;
import com.example.han.referralproject.xindian.XinDianDetectActivity;
import com.gcml.lib_utils.display.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HealthSugarDetectionUiFragment extends HealthSugarDetectionFragment
        implements HealthSelectSugarDetectionTimeFragment.OnActionListener {

    private ImageView ivRight;
    private Uri uri;
    private TextView tvNext;

    private void uploadEcg(Intent data) {
        int ecg = data.getIntExtra("ecg", 0);
        int heartRate = data.getIntExtra("heartRate", 0);
        if (getFragmentManager() != null) {
            DataCacheFragment.get(getFragmentManager()).getDataCache().put("ecg", ecg);
            DataCacheFragment.get(getFragmentManager()).getDataCache().put("heartRate", heartRate);
        }
        ArrayList<DetectionData> datas = new ArrayList<>();
        DetectionData ecgData = new DetectionData();
        //detectionType (string, optional): 检测数据类型 0血压 1血糖 2心电 3体重 4体温 6血氧 7胆固醇 8血尿酸 9脉搏 ,
        ecgData.setDetectionType("2");
        ecgData.setEcg(String.valueOf(ecg));
        ecgData.setHeartRate(heartRate);
        datas.add(ecgData);
        DataCacheFragment dataCacheFragment = DataCacheFragment.get(getFragmentManager());
        HashMap<String, Object> dataCache = dataCacheFragment.getDataCache();
        List<DetectionData> dataList = (List<DetectionData>) dataCache.get("dataList");
        if (dataList == null) {
            dataList = new ArrayList<>();
            dataCache.put("dataList", dataList);
        }
        dataList.add(ecgData);
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
                                navToThreeInOne();
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

    private void navToThreeInOne() {
        Uri uri = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.tips_sanheyi);
        MeasureVideoPlayActivity.startActivity(this, MeasureVideoPlayActivity.class, uri, null, "血压测量演示视频");
    }

    private void selectSugarTime() {
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        Fragment fragment = fm.findFragmentByTag(HealthSelectSugarDetectionTimeFragment.class.getName());
        if (fragment != null) {
            transaction.show(fragment);
        } else {
            fragment = new HealthSelectSugarDetectionTimeFragment();
            transaction.add(R.id.rl_container, fragment);
        }
        transaction.addToBackStack(null);
        transaction.commitAllowingStateLoss();
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        super.initView(view, savedInstanceState);
        ((TextView) view.findViewById(R.id.tv_top_title)).setText(R.string.test_xuetang);
        view.findViewById(R.id.ll_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getFragmentManager() != null) {
                    getFragmentManager().popBackStack();
                }
            }
        });
        ivRight = ((ImageView) view.findViewById(R.id.iv_top_right));
        ivRight.setImageResource(R.drawable.health_ic_blutooth);
        ivRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDetection();
            }
        });
        tvNext = ((TextView) view.findViewById(R.id.tv_next));
        tvNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navToNext();
            }
        });
        selectSugarTime();
    }

    @Override
    protected void onSugarResult(float sugar) {
        if (getFragmentManager() == null) {
            return;
        }
        HashMap<String, Object> dataCache = DataCacheFragment
                .get(getFragmentManager()).getDataCache();
        dataCache.put("sugar", sugar);
        ArrayList<DetectionData> datas = new ArrayList<>();
        DetectionData sugarData = new DetectionData();
        //detectionType (string, optional): 检测数据类型 0血压 1血糖 2心电 3体重 4体温 6血氧 7胆固醇 8血尿酸 9脉搏 ,
        sugarData.setDetectionType("1");
        sugarData.setSugarTime(sugarTime);
        sugarData.setBloodSugar(sugar);
        datas.add(sugarData);
        List<DetectionData> dataList = (List<DetectionData>) dataCache.get("dataList");
        if (dataList == null) {
            dataList = new ArrayList<>();
            dataCache.put("dataList", dataList);
        }
        dataList.add(sugarData);
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

    @Override
    public void onResume() {
        super.onResume();
        startDetection();
    }

    private void navToNext() {
        Intent intent = new Intent(getActivity(), XinDianDetectActivity.class);
        intent.putExtra("playVideoTips", true);
        intent.putExtra("forResult", true);
        startActivityForResult(intent, 18);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 18) {
            if (resultCode == Activity.RESULT_OK) {
                if (data.getIntExtra("ecg", 0) <= 0) {
                    navToThreeInOne();
                    return;
                }
                uploadEcg(data);
            }
            return;
        }

        if (requestCode == MeasureVideoPlayActivity.REQUEST_PALY_VIDEO) {
            FragmentManager fm = getFragmentManager();
            if (fm == null) {
                return;
            }
            Fragment fragment = new HealthThreeInOneDetectionUiFragment();
            FragmentTransaction transaction = fm.beginTransaction();
            transaction.replace(R.id.fl_container, fragment);
            transaction.addToBackStack(null);
            transaction.commitAllowingStateLoss();
        }
    }

    private int sugarTime = 0;

    @Override
    public void onAction(int action) {
        sugarTime = action;
        startDetection();
    }
}
