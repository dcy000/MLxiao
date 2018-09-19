package com.example.han.referralproject.hypertensionmanagement.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.billy.cc.core.component.IComponentCallback;
import com.example.han.referralproject.R;
import com.example.han.referralproject.health_manager_program.TreatmentPlanActivity;
import com.gcml.common.repository.utils.DefaultObserver;
import com.gcml.common.utils.RxUtils;
import com.iflytek.synthetize.MLVoiceSynthetize;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;


public class DetecteTipActivity extends AppCompatActivity {

    private String fromeWhere;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detecte_tip);
        fromeWhere = getIntent().getStringExtra("fromWhere");
        MLVoiceSynthetize.startSynthesize(getApplicationContext(), "接下来，跟着小E来检测吧");
        initEvent();
    }


    private void initEvent() {
        Observable.timer(3000, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new DefaultObserver<Long>() {
                    @Override
                    public void onNext(Long o) {
                        toDetecte();
                    }
                });
    }

    private void toDetecte() {
        finish();
        Timber.d("timer--3秒--时间就到");
        switch (fromeWhere) {
            case "0"://高血压-->体重-->报告
                CC.obtainBuilder("health_measure")
                        .setActionName("To_BloodpressureManagerActivity")
                        .build().callAsyncCallbackOnMainThread(new IComponentCallback() {
                    @Override
                    public void onResult(CC cc, CCResult result) {
                        CC.obtainBuilder("health_measure")
                                .setActionName("To_WeightManagerActivity")
                                .build().callAsyncCallbackOnMainThread(new IComponentCallback() {
                            @Override
                            public void onResult(CC cc, CCResult result) {
                                startActivity(new Intent(DetecteTipActivity.this, TreatmentPlanActivity.class));
                            }
                        });

                    }
                });
                break;
            case "1"://高血压--->报告
                CC.obtainBuilder("health_measure")
                        .setActionName("To_BloodpressureManagerActivity")
                        .build().callAsyncCallbackOnMainThread(new IComponentCallback() {
                    @Override
                    public void onResult(CC cc, CCResult result) {
                        startActivity(new Intent(DetecteTipActivity.this, TreatmentPlanActivity.class));
                    }
                });

                break;
            case "2"://血糖-->体重-->报告
                CC.obtainBuilder("health_measure")
                        .setActionName("To_BloodsugarManagerActivity")
                        .build().callAsyncCallbackOnMainThread(new IComponentCallback() {
                    @Override
                    public void onResult(CC cc, CCResult result) {
                        CC.obtainBuilder("health_measure")
                                .setActionName("To_WeightManagerActivity")
                                .build().callAsyncCallbackOnMainThread(new IComponentCallback() {
                            @Override
                            public void onResult(CC cc, CCResult result) {
                                startActivity(new Intent(DetecteTipActivity.this, TreatmentPlanActivity.class));
                            }
                        });
                    }
                });

                break;
            case "3":
                CC.obtainBuilder("health_measure")
                        .setActionName("To_WeightManagerActivity")
                        .build().callAsyncCallbackOnMainThread(new IComponentCallback() {
                    @Override
                    public void onResult(CC cc, CCResult result) {
                        startActivity(new Intent(DetecteTipActivity.this, TreatmentPlanActivity.class));
                        finish();
                    }
                });
                break;
        }
    }

}
