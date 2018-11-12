package com.example.han.referralproject.single_measure;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import com.example.han.referralproject.bean.DataInfoBean;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.single_measure.bean.DetectionData;
import com.gcml.module_blutooth_devices.others.ThreeInOne_Fragment;
import com.gcml.module_blutooth_devices.utils.UtilsManager;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.medlink.danbogh.utils.T;

import java.util.ArrayList;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/8/6 11:13
 * created by:gzq
 * description:单次三合一测量
 */
public class SingleMeasureThreeInOneFragment extends ThreeInOne_Fragment {
    private ArrayList<DetectionData> datas = new ArrayList<>();
    DetectionData sugarData;
    DetectionData cholesterolData;
    DetectionData lithicAcidData;
    private int selectMeasureSugarTime;

    @Override
    protected void initView(View view, Bundle bundle) {
        super.initView(view, bundle);
        if (bundle != null) {
            selectMeasureSugarTime = bundle.getInt("selectMeasureSugarTime");
        }
        mTitle11.setText("<3.9");
        switch (selectMeasureSugarTime) {
            case 0:
                //空腹
                mTitle1.setText("血糖(空腹)");
                mTitle12.setText("3.9~6.1");
                mTitle13.setText(">6.1");
                break;
            case 1:
                //饭后1小时
                mTitle1.setText("血糖(饭后1小时)");
                mTitle12.setText("3.9~7.8");
                mTitle13.setText(">7.8");
                break;
            case 2:
                //饭后2小时
                mTitle1.setText("血糖(饭后2小时)");
                mTitle12.setText("3.9~7.8");
                mTitle13.setText(">7.8");
                break;
            case 3:
                //其他时间
                mTitle1.setText("血糖(其他时间)");
                mTitle12.setText("3.9~11.1");
                mTitle13.setText(">11.1");
                break;
            default:
                break;
        }
    }

    //三合一 血糖的位置2，血尿酸位置：6；胆固醇位置：5
    @Override
    protected void onMeasureFinished(String... results) {
        if (results.length == 2) {
           /* if (results[0].equals("bloodsugar")) {
                sugarData = new DetectionData();
                sugarData.setDetectionType("1");
                sugarData.setSugarTime(selectMeasureSugarTime);
                sugarData.setBloodSugar(Float.parseFloat(results[1]));
                datas.add(sugarData);
                MLVoiceSynthetize.startSynthesize(UtilsManager.getApplication(), "主人，您本次测量血糖" + sugarData.getBloodSugar());
                uploadData(datas);

                if (measureItemChanged != null) {
                    measureItemChanged.onChanged(2);
                }
            }
            if (results[0].equals("cholesterol")) {
                cholesterolData = new DetectionData();
                cholesterolData.setDetectionType("7");
                cholesterolData.setCholesterol(Float.parseFloat(results[1]));
                datas.add(cholesterolData);
                MLVoiceSynthetize.startSynthesize(UtilsManager.getApplication(), "主人，您本次测量胆固醇" + cholesterolData.getCholesterol());
                uploadData(datas);
                if (measureItemChanged != null) {
                    measureItemChanged.onChanged(5);
                }
            }

            if (results[0].equals("bua")) {
                lithicAcidData = new DetectionData();
                lithicAcidData.setDetectionType("8");
                lithicAcidData.setUricAcid(Float.parseFloat(results[1]));

                datas.add(lithicAcidData);
                MLVoiceSynthetize.startSynthesize(UtilsManager.getApplication(), "主人，您本次测量尿酸" + lithicAcidData.getUricAcid());
                uploadData(datas);
                if (measureItemChanged != null) {
                    measureItemChanged.onChanged(6);
                }
            }*/
            DataInfoBean info = new DataInfoBean();
            if (results[0].equals("bloodsugar")) {
                info.blood_sugar = results[1];
                MLVoiceSynthetize.startSynthesize(UtilsManager.getApplication(), "主人，您本次测量血糖" + results[1]);
            }
            if (results[0].equals("cholesterol")) {
                info.cholesterol = results[1];
                MLVoiceSynthetize.startSynthesize(UtilsManager.getApplication(), "主人，您本次测量胆固醇" + results[1]);
            }

            if (results[0].equals("bua")) {
                info.uric_acid = results[1];
                MLVoiceSynthetize.startSynthesize(UtilsManager.getApplication(), "主人，您本次测量尿酸" + results[1]);
            }

            NetworkApi.postData(info, response -> {
                T.show("数据上传成功");
            }, message -> {
                T.show("数据上传失败");
            });


        }
    }

    @SuppressLint("CheckResult")
    private void uploadData(ArrayList<DetectionData> datas) {
     /*   HealthMeasureRepository.postMeasureData(datas)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxUtils.autoDisposeConverter(this, LifecycleUtils.LIFE))
                .subscribeWith(new DefaultObserver<List<DetectionResult>>() {
                    @Override
                    public void onNext(List<DetectionResult> o) {
                        ToastUtils.showLong("数据上传成功");
                        datas.clear();
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.showLong("数据上传失败:" + e.getMessage());
                        datas.clear();
                    }

                    @Override
                    public void onComplete() {

                    }
                });*/


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        MLVoiceSynthetize.destory();
    }
}
