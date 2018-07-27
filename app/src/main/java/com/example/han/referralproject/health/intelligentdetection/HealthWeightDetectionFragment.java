package com.example.han.referralproject.health.intelligentdetection;


import android.Manifest;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.util.LocalShared;
import com.polidea.rxandroidble2.RxBleClient;
import com.polidea.rxandroidble2.RxBleDevice;
import com.polidea.rxandroidble2.scan.ScanResult;
import com.polidea.rxandroidble2.scan.ScanSettings;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 */
public class HealthWeightDetectionFragment extends Fragment {

    private TextView tvWeight;
    private TextView tvBodyFat;

    public HealthWeightDetectionFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(layoutId(), container, false);
        initView(view, savedInstanceState);
        return view;
    }

    private int layoutId() {
        return R.layout.health_fragment_weight_detection;
    }

    private void initView(View view, Bundle savedInstanceState) {
        tvWeight = (TextView) view.findViewById(R.id.tv_tizhong);
        tvBodyFat = (TextView) view.findViewById(R.id.tv_tizhi);
    }

    private static final String DEVICE_NAME = "000FatScale01";
    private static final UUID UUID_WEIGHT = UUID.fromString("0000fff1-0000-1000-8000-00805f9b34fb");
    private static final UUID UUID_WEIGHT_SERVICE = UUID.fromString("0000fff0-0000-1000-8000-00805f9b34fb");

    private RxBleClient rxBleClient;
    private RxPermissions rxPermissions;
    private Disposable disposable = Disposables.empty();

    private void startDetection() {
        disposable.dispose();
        AtomicBoolean hasFind = new AtomicBoolean(false);
        AtomicBoolean hasComplete = new AtomicBoolean(false);
        disposable = Observable.just(1)
                .compose(rxPermissions.ensure(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.BLUETOOTH,
                        Manifest.permission.BLUETOOTH_ADMIN
                ))
                .flatMap(grated -> {
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
                .doOnNext(scanResult ->  Timber.i("<- scan [Found]: %s %s",
                        scanResult.getBleDevice().getName(),
                        scanResult.getBleDevice().getMacAddress()))
                .flatMap(scanResult -> {
                    if (hasFind.compareAndSet(false, true)) {
                        return scanResult.getBleDevice().establishConnection(false);
                    }
                    return Observable.empty();
                })
                .flatMap(rxBleConnection -> rxBleConnection.setupNotification(UUID_WEIGHT)
                        .doOnSubscribe(disposable -> Timber.i("setupNotification: %s", UUID_WEIGHT)))
                .flatMap(notification -> notification)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bytes -> {
                    parseData(hasComplete, bytes);
                }, Timber::i);
    }

    private void parseData(AtomicBoolean hasComplete, byte[] bytes) {
        Timber.i("data");
        if (bytes.length == 14 && (bytes[1] & 0xff) == 221
                && hasComplete.compareAndSet(false, true)) {
            float weight = ((float) (bytes[2] << 8) + (float) (bytes[3] & 0xff)) / 10;
            String userHeight = LocalShared.getInstance(getActivity()).getUserHeight();
            float height = TextUtils.isEmpty(userHeight) ? 0 : Float.parseFloat(userHeight) / 100;
            // 18.5 < bodyFat < 23.9
            float bodyFat = weight / (height * height);
            tvWeight.setText(String.format(Locale.getDefault(), "%.2f", weight));
            tvBodyFat.setText(String.format(Locale.getDefault(), "%1$.2f", bodyFat));
            Timber.i("weight = %s, height = %s, bodyFat = %s", weight, height, bodyFat);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        startDetection();
    }

    @Override
    public void onStop() {
        super.onStop();
        disposable.dispose();
    }

}
