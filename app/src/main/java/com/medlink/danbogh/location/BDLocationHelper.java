package com.medlink.danbogh.location;

import android.annotation.SuppressLint;
import android.content.Context;

import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

/**
 * Created by lenovo on 2017/11/6.
 */

public class BDLocationHelper {

    @SuppressLint("StaticFieldLeak")
    private static Context sContext;

    public static void init(Context context) {
        sContext = context;
    }

    public static BDLocationHelper getInstance() {
        return Holder.INSTANCE;
    }

    private static final class Holder {
        @SuppressLint("StaticFieldLeak")
        private static final BDLocationHelper INSTANCE = new BDLocationHelper();
    }

    private LocationClient mLocationClient;

    private BDLocationHelper() {
        if (sContext == null) {
            throw new IllegalStateException("must init");
        }
        mLocationClient = new LocationClient(sContext);
        mLocationClient.setLocOption(provideDefaultLocOption());
    }

    private LocationClientOption mDefaultOption;

    public LocationClientOption provideDefaultLocOption() {
        if (mDefaultOption == null) {
            mDefaultOption = new LocationClientOption();
            mDefaultOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
            mDefaultOption.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系，如果配合百度地图使用，建议设置为bd09ll;
            mDefaultOption.setScanSpan(3000);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
            mDefaultOption.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
            mDefaultOption.setIsNeedLocationDescribe(true);//可选，设置是否需要地址描述
            mDefaultOption.setNeedDeviceDirect(false);//可选，设置是否需要设备方向结果
            mDefaultOption.setLocationNotify(false);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
            mDefaultOption.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
            mDefaultOption.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
            mDefaultOption.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
            mDefaultOption.SetIgnoreCacheException(true);//可选，默认false，设置是否收集CRASH信息，默认收集
            mDefaultOption.setIsNeedAltitude(false);//可选，默认false，设置定位时是否需要海拔信息，默认不需要，除基础定位版本都可用
        }
        return mDefaultOption;
    }

    private LocationClientOption mOption;

    public void setLocOption(LocationClientOption option) {
        if (option != null) {
            if (mLocationClient.isStarted()) {
                mLocationClient.stop();
            }
            mOption = option;
            mLocationClient.setLocOption(option);
        }
    }

    public LocationClientOption getLocOption() {
        return mOption;
    }

    public void registerListener(BDLocationListener listener) {
        if (listener != null) {
            mLocationClient.registerLocationListener(listener);
        }
    }

    public void unregisterListener(BDLocationListener listener) {
        if (listener != null) {
            mLocationClient.unRegisterLocationListener(listener);
        }
    }

    public void start() {
        if (mLocationClient != null && !mLocationClient.isStarted()) {
            synchronized (this) {
                if (mLocationClient != null && !mLocationClient.isStarted()) {
                    mLocationClient.start();
                }
            }
        }
    }

    public void stop() {
        if (mLocationClient != null && mLocationClient.isStarted()) {
            synchronized (this) {
                if (mLocationClient != null && mLocationClient.isStarted()) {
                    mLocationClient.stop();
                }
            }
        }
    }

    public boolean requestHotSpotState(){
        return mLocationClient.requestHotSpotState();
    }
}