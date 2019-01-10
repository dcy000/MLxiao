package com.gcml.health.measure.single_measure.no_upload_data;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.gcml.common.data.UserEntity;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.UtilsManager;
import com.gcml.module_blutooth_devices.weight.WeightFragment;
import com.gcml.module_blutooth_devices.weight_devices.Weight_Fragment;
import com.iflytek.synthetize.MLVoiceSynthetize;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/9/14 11:52
 * created by:gzq
 * description:TODO
 */
public class NonUploadSingleMeasureWeightFragment extends WeightFragment {
    @SuppressLint("CheckResult")
    @Override
    protected void onMeasureFinished(String... results) {
        if (results.length == 1) {
            //得到身高和体重，再计算一下体质
            if (mTvTizhi != null) {
                CCResult call = CC.obtainBuilder("com.gcml.auth.getUser").build().call();
                Observable<UserEntity> user = call.getDataItem("data");
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
            MLVoiceSynthetize.startSynthesize(UtilsManager.getApplication(), "主人，您本次测量体重" + results[0] + "公斤", false);
        }
    }
}
