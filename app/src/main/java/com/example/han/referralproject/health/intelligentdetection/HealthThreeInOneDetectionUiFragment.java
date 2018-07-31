package com.example.han.referralproject.health.intelligentdetection;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;

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

public class HealthThreeInOneDetectionUiFragment extends HealthThreeInOneDetectionFragment {

    private TextView tvNext;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        super.initView(view, savedInstanceState);
        tvNext = ((TextView) view.findViewById(R.id.tv_next));
        tvNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navToNext();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        startDetection();
    }

    private SparseArray<Float> results = new SparseArray<>();

    @Override
    protected void onThreeInOneResult(int category, float result) {
        results.put(category, result);
    }

    private void navToNext() {
        if (getFragmentManager() != null) {
            DataCacheFragment.get(getFragmentManager()).getDataCache().put("threeInOne", results);
        }
        if (results.size() == 0) {
            navToReport();
            return;
        }
        ArrayList<DetectionData> datas = new ArrayList<>();
        DetectionData sugarData = new DetectionData();
        DetectionData cholesterolData = new DetectionData();
        DetectionData lithicAcidData = new DetectionData();
        //detectionType (string, optional): 检测数据类型 0血压 1血糖 2心电 3体重 4体温 6血氧 7胆固醇 8血尿酸 9脉搏 ,
        sugarData.setDetectionType("1");
        sugarData.setSugarTime(0);
        sugarData.setBloodSugar(results.get(1));
        cholesterolData.setDetectionType("7");
        cholesterolData.setCholesterol(results.get(7));
        lithicAcidData.setDetectionType("8");
        lithicAcidData.setUricAcid(results.get(8));
        datas.add(sugarData);
        datas.add(cholesterolData);
        datas.add(lithicAcidData);
        DataCacheFragment dataCacheFragment = DataCacheFragment.get(getFragmentManager());
        HashMap<String, Object> dataCache = dataCacheFragment.getDataCache();
        List<DetectionData> dataList = (List<DetectionData>) dataCache.get("dataList");
        if (dataList == null) {
            dataList = new ArrayList<>();
            dataCache.put("dataList", dataList);
        }
        dataList.addAll(datas);
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
                                navToReport();
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

    private void navToReport() {
        FragmentManager fm = getFragmentManager();
        if (fm == null) {
            return;
        }
        Fragment fragment = new HealthDetectionIntelligentReportFragment();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.fl_container, fragment);
        transaction.addToBackStack(null);
        transaction.commitAllowingStateLoss();
    }

}
