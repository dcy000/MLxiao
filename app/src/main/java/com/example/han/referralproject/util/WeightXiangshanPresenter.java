package com.example.han.referralproject.util;

import android.annotation.SuppressLint;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.support.v4.app.SupportActivity;

import java.math.BigDecimal;

import senssun.blelib.device.scale.cloudblelib.BleCloudProtocolUtils;
import senssun.blelib.scan.BleScan;

public class WeightXiangshanPresenter implements LifecycleObserver {
    private SupportActivity activity;
    private String name;
    private String address;
    private final BleScan bleScan;
    private final BleCloudProtocolUtils bleCloudProtocolUtils;
    private MeasureResult measureResult;

    @SuppressLint("RestrictedApi")
    public WeightXiangshanPresenter(SupportActivity activity, MeasureResult measureResult, String name, String address) {
        this.activity = activity;
        this.measureResult = measureResult;
        this.name = name;
        this.address = address;
        this.activity.getLifecycle().addObserver(this);
        bleCloudProtocolUtils = BleCloudProtocolUtils.getInstance(activity);
        bleScan = new BleScan();
        bleScan.Create(activity);
        connect();
    }

    private void connect() {
        bleCloudProtocolUtils.setOnConnectState(new BleCloudProtocolUtils.OnConnectState() {
            @Override
            public void OnState(boolean b) {
                if (b) {
                    if (measureResult != null) {
                        measureResult.state("设备已连接");
                    }
                    LocalShared.getInstance(activity).setTizhongMac(address);
                } else {
                    if (measureResult != null) {
                        measureResult.state("设备已断开");
                    }
                }
            }
        });


        bleCloudProtocolUtils.setOnDisplayDATA(new BleCloudProtocolUtils.OnDisplayDATA() {
            @Override
            public void OnDATA(String data) {
                String[] strdata = data.split("-");
                switch (strdata[5]) {
                    case "03":
                        switch (strdata[6]) {
                            case "80":
                                if (strdata[12].equals("A0")) {
                                    String tmpNum = strdata[7] + strdata[8];
                                   /* if (measureResult != null) {
                                        measureResult.weight(Integer.valueOf(tmpNum, 16) / 10f);
                                    }*/
                                } else {
                                    String tmpNum = strdata[7] + strdata[8];
                                    if (measureResult != null) {
                                        measureResult.weight(Integer.valueOf(tmpNum, 16) / 10f);
                                    }
                                }
                                break;
                            case "82":
                                switch (strdata[7]) {
                                    case "00": {
                                        float eigenvalue = new BigDecimal(Integer.valueOf(strdata[14] + strdata[15], 16)).floatValue();
                                        float weight = new BigDecimal(Integer.valueOf(strdata[10] + strdata[11], 16) / 10f).setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
                                        //TODO:请求香山的接口 获取更加详细的健康分析数据
                                    }
                                    break;
                                }

                        }
                        break;
                }
            }
        });
        bleCloudProtocolUtils.Connect(address);
    }

    @SuppressLint("RestrictedApi")
    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onStop() {
        if (bleCloudProtocolUtils != null) {
            bleCloudProtocolUtils.Disconnect();
            bleCloudProtocolUtils.Close();
        }
        if (activity != null) {
            activity.getLifecycle().removeObserver(this);
        }
        activity = null;
    }

    public interface MeasureResult {
        void state(String state);

        void weight(float weight);
    }
}
