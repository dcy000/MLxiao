package com.example.han.referralproject.ecg;

import com.example.han.referralproject.R;
import com.example.han.referralproject.bean.DetectionData;
import com.example.han.referralproject.bean.DetectionResult;
import com.example.han.referralproject.service.API;
import com.gcml.lib_ecg.SelfECGDetectionFragment;
import com.gzq.lib_core.base.Box;
import com.gzq.lib_core.bean.UserInfoBean;
import com.gzq.lib_core.http.exception.ApiException;
import com.gzq.lib_core.http.observer.CommonObserver;
import com.gzq.lib_core.utils.RxUtils;
import com.gzq.lib_core.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

public class SelfFragment extends SelfECGDetectionFragment {
    @Override
    protected void uploadEcg(int ecg, int heartRate) {
        super.uploadEcg(ecg, heartRate);
        //todo 上传数据
        ArrayList<DetectionData> datas = new ArrayList<>();
        DetectionData ecgData = new DetectionData();
        //detectionType (string, optional): 检测数据类型 0血压 1血糖 2心电 3体重 4体温 6血氧 7胆固醇 8血尿酸 9脉搏 ,
        ecgData.setDetectionType("2");
        ecgData.setEcg(ecg + "");
        ecgData.setResult(Box.getStrings(R.array.ecg_measureres)[ecg]);
        ecgData.setHeartRate(heartRate);
        datas.add(ecgData);
        String userId = ((UserInfoBean) Box.getSessionManager().getUser()).bid;
        Box.getRetrofit(API.class)
                .postMeasureData(userId, datas)
                .compose(RxUtils.httpResponseTransformer())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new CommonObserver<List<DetectionResult>>() {
                    @Override
                    public void onNext(List<DetectionResult> detectionResults) {
                        ToastUtils.showShort("上传数据成功");
                    }

                    @Override
                    protected void onError(ApiException ex) {
                        super.onError(ex);
                        ToastUtils.showShort(ex.message);
                    }
                });
    }
}
