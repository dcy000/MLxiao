package com.example.han.referralproject.health.intelligentdetection;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.polidea.rxandroidble2.RxBleClient;
import com.polidea.rxandroidble2.scan.ScanSettings;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class HealthBloodDetectionFragment extends Fragment {

    private TextView tvHighPressure;
    private TextView tvLowPressure;
    private TextView tvPulsePressure;
    protected TextView tvDetectionAgain;
    protected TextView tvNext;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentActivity activity = getActivity();
        rxPermissions = new RxPermissions(activity);
        rxBleClient = RxBleClient.create(activity);
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(layoutId(), container, false);
        initView(view, savedInstanceState);
        view.setClickable(true);
        return view;
    }

    protected int layoutId() {
        return R.layout.health_fragment_blood_detection;
    }

    protected void initView(View view, Bundle savedInstanceState) {
        ((TextView) view.findViewById(R.id.tv_top_title)).setText(R.string.test_xueya);
        tvHighPressure = (TextView) view.findViewById(R.id.high_pressure);
        tvLowPressure = (TextView) view.findViewById(R.id.low_pressure);
        tvPulsePressure = (TextView) view.findViewById(R.id.pulse);
        tvDetectionAgain = (TextView) view.findViewById(R.id.tv_detection_again);
        tvNext = (TextView) view.findViewById(R.id.tv_next);
    }

    private static final String DEVICE_NAME = "eBlood-Pressure";
    private static final UUID UUID_BLOOD = UUID.fromString("0000fff4-0000-1000-8000-00805f9b34fb");

    private RxBleClient rxBleClient;
    private RxPermissions rxPermissions;
    private Disposable disposable = Disposables.empty();
    protected void startDetection() {
        disposable.dispose();
        AtomicBoolean hasFind = new AtomicBoolean(false);
        AtomicBoolean hasComplete = new AtomicBoolean(false);
        disposable = Observable.just(1)
                .delay(1, TimeUnit.SECONDS)
                .compose(rxPermissions.ensure(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.BLUETOOTH,
                        Manifest.permission.BLUETOOTH_ADMIN
                )).flatMap(grated -> {
                    if (grated) {
                        return rxBleClient.scanBleDevices(new ScanSettings.Builder()
                                .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                                .setCallbackType(ScanSettings.CALLBACK_TYPE_ALL_MATCHES)
                                .build()
                        );
                    }
                    return Observable.error(new RuntimeException("permission not granted"));
                })
                .doOnNext(scanResult -> Timber.i("scan %s %s",
                        scanResult.getBleDevice().getName(),
                        scanResult.getBleDevice().getMacAddress()))
                .filter(scanResult -> DEVICE_NAME.equals(scanResult.getBleDevice().getName()))
                .doOnNext(scanResult -> Timber.i("<- scan [Found]: %s %s",
                        scanResult.getBleDevice().getName(),
                        scanResult.getBleDevice().getMacAddress()))
                .flatMap(scanResult -> {
                    if (hasFind.compareAndSet(false, true)) {
                        return scanResult.getBleDevice().establishConnection(false);
                    }
                    return Observable.empty();
                })
                .flatMap(rxBleConnection -> rxBleConnection.setupNotification(UUID_BLOOD)
                        .doOnSubscribe(disposable1 -> Timber.i("setupNotification: %s", UUID_BLOOD)))
                .flatMap(notification -> notification)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bytes -> {
                    parseData(hasComplete, bytes);
                }, Timber::i);
    }

    private void parseData(AtomicBoolean hasComplete, byte[] bytes) {
        int highPressure;
        if (bytes.length == 2) {
            highPressure = bytes[1] & 0xff;
            Timber.i("data: highPressure = %s, lowPressure = %s, pulse = %s", highPressure, 0, 0);
            tvHighPressure.setText(String.valueOf(highPressure));
            tvLowPressure.setText(String.valueOf(0));
            tvPulsePressure.setText(String.valueOf(0));
            return;
        }
        if (bytes.length == 12 && hasComplete.compareAndSet(false, true)) {
            highPressure = bytes[2] & 0xff;
            int lowPressure = bytes[4] & 0xff;
            int pulse = bytes[8] & 0xff;
            tvHighPressure.setText(String.valueOf(highPressure));
            tvLowPressure.setText(String.valueOf(lowPressure));
            tvPulsePressure.setText(String.valueOf(pulse));
            onBloodResult(highPressure, lowPressure, pulse);
            Timber.i("data [ complete ]: highPressure = %s, lowPressure = %s, pulse = %s", highPressure, lowPressure, pulse);
        }
    }

    protected void onBloodResult(int highPressure, int lowPressure, int pulse) {

    }

    @Override
    public void onStop() {
        super.onStop();
        disposable.dispose();
    }
}