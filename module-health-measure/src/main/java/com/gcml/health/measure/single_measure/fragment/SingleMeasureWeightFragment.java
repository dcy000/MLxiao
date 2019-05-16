package com.gcml.health.measure.single_measure.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.gcml.common.data.UserEntity;
import com.gcml.common.recommend.bean.post.DetectionData;
import com.gcml.common.router.AppRouter;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.UM;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.health.measure.first_diagnosis.bean.DetectionResult;
import com.gcml.health.measure.network.HealthMeasureRepository;
import com.gcml.health.measure.utils.LifecycleUtils;
import com.gcml.module_blutooth_devices.base.IBleConstants;
import com.gcml.module_blutooth_devices.weight.WeightFragment;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.sjtu.yifei.route.Routerfit;

import java.util.ArrayList;
import java.util.List;

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
public class SingleMeasureWeightFragment extends WeightFragment {

    private boolean isMeasureTask = false;

    @Override
    protected void initView(View view, Bundle bundle) {
        super.initView(view, bundle);
        if (bundle != null) {
            isMeasureTask = bundle.getBoolean(IBleConstants.IS_MEASURE_TASK);
        }
    }

    @SuppressLint("CheckResult")
    @Override
    protected void onMeasureFinished(DetectionData detectionData) {
        //得到身高和体重，再计算一下体质
        if (mTvTizhi != null) {
            Routerfit.register(AppRouter.class)
                    .getUserProvider()
                    .getUserEntity()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .as(RxUtils.autoDisposeConverter(this, LifecycleUtils.LIFE))
                    .subscribe(new Consumer<UserEntity>() {
                        @Override
                        public void accept(UserEntity userEntity) throws Exception {
                            if (userEntity != null) {
                                String userHeight = userEntity.height;
                                if (!TextUtils.isEmpty(userHeight)) {
                                    float parseFloat = Float.parseFloat(userHeight);
                                    if (mTvTizhi != null) {
                                        mTvTizhi.setText(String.format("%.2f", detectionData.getWeight() / (parseFloat * parseFloat / 10000)));
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
        MLVoiceSynthetize.startSynthesize(UM.getApp(), "您本次测量体重" + detectionData.getWeight() + "公斤", false);
        ArrayList<DetectionData> datas = new ArrayList<>();
        DetectionData data = new DetectionData();
        //detectionType (string, optional): 检测数据类型 0血压 1血糖 2心电 3体重 4体温 6血氧 7胆固醇 8血尿酸 9脉搏 ,
        data.setDetectionType("3");
        data.setWeight(detectionData.getWeight());
        datas.add(data);

        HealthMeasureRepository.postMeasureData(datas)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxUtils.autoDisposeConverter(this, LifecycleUtils.LIFE))
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
                        showUploadDataFailedDialog(detectionData);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        MLVoiceSynthetize.destory();
    }
}
