package com.gcml.health.measure.single_measure.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.gcml.common.data.UserEntity;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.recommend.bean.post.DetectionData;
import com.gcml.health.measure.first_diagnosis.bean.DetectionResult;
import com.gcml.health.measure.network.HealthMeasureRepository;
import com.gcml.lib_utils.UtilsManager;
import com.gcml.lib_utils.display.ToastUtils;
import com.gcml.module_blutooth_devices.base.IPresenter;
import com.gcml.module_blutooth_devices.weight_devices.Weight_Fragment;
import com.iflytek.synthetize.MLVoiceSynthetize;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DefaultObserver;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/8/6 11:09
 * created by:gzq
 * description:单次体重测量
 */
public class SingleMeasureWeightFragment extends Weight_Fragment {

    private boolean isMeasureTask = false;

    @Override
    protected void initView(View view, Bundle bundle) {
        super.initView(view, bundle);
        if (bundle != null) {
            isMeasureTask = bundle.getBoolean(IPresenter.IS_MEASURE_TASK);
        }
    }

    @SuppressLint("CheckResult")
    @Override
    protected void onMeasureFinished(String... results) {
        if (results.length == 1) {
            //得到身高和体重，再计算一下体质
            if (mTvTizhi != null) {
                CCResult call = CC.obtainBuilder("com.gcml.auth.getUser").build().call();
                Observable<UserEntity> user =  call.getDataItem("data");
                user.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .as(RxUtils.autoDisposeConverter(this))
                        .subscribe(new Consumer<UserEntity>() {
                            @Override
                            public void accept(UserEntity userEntity) throws Exception {
                                if (userEntity != null) {
                                    String userHeight = userEntity.height;
                                    if (!TextUtils.isEmpty(userHeight)) {
                                        float parseFloat = Float.parseFloat(userHeight);
                                        float weight = Float.parseFloat(results[0]);
                                        if (mTvTizhi != null) {
                                            mTvTizhi.setText(String.format("%.2f", weight / (parseFloat * parseFloat / 10000)));
                                        }
                                    }
                                }

                            }

                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Timber.e(throwable);
                            }
                        });

            }
            MLVoiceSynthetize.startSynthesize(UtilsManager.getApplication(), "您好，您本次测量体重" + results[0] + "公斤",false);
            ArrayList<DetectionData> datas = new ArrayList<>();
            DetectionData data = new DetectionData();
            //detectionType (string, optional): 检测数据类型 0血压 1血糖 2心电 3体重 4体温 6血氧 7胆固醇 8血尿酸 9脉搏 ,
            data.setDetectionType("3");
            data.setWeight(Float.parseFloat(results[0]));
            datas.add(data);

            HealthMeasureRepository.postMeasureData(datas)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .as(RxUtils.autoDisposeConverter(this))
                    .subscribeWith(new DefaultObserver<List<DetectionResult>>() {
                        @Override
                        public void onNext(List<DetectionResult> o) {
                            ToastUtils.showLong("数据上传成功");
                            if (isMeasureTask && !mActivity.isFinishing()) {
                                mActivity.finish();
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            ToastUtils.showLong("数据上传失败:"+e.getMessage());
                        }

                        @Override
                        public void onComplete() {

                        }
                    });

//            HealthMeasureApi.postMeasureData(datas, new NetworkCallback() {
//                @Override
//                public void onSuccess(String callbackString) {
//                    ToastUtils.showLong("数据上传成功");
//                    if (isMeasureTask && !mActivity.isFinishing()) {
//                        mActivity.finish();
//                    }
//                }
//
//                @Override
//                public void onError() {
//                    ToastUtils.showLong("数据上传失败");
//                }
//            });
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        MLVoiceSynthetize.destory();
    }
}
