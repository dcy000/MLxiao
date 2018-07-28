package com.example.han.referralproject.health.intelligentdetection;


import android.Manifest;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.polidea.rxandroidble2.RxBleClient;
import com.polidea.rxandroidble2.scan.ScanSettings;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 */
public class HealthSugarDetectionFragment extends Fragment {

    private TextView tvSugar;

    public HealthSugarDetectionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rxPermissions = new RxPermissions(getActivity());
        rxBleClient = RxBleClient.create(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(layoutId(), container, false);
        initView(view, savedInstanceState);
        return view;
    }

    protected int layoutId() {
        return R.layout.fragment_health_sugar_detection;
    }

    protected void initView(View view, Bundle savedInstanceState) {
        tvSugar = (TextView) view.findViewById(R.id.tv_xuetang);
    }

    private static final String DEVICE_NAME_SUGAR = "Bioland-BGM";
    private static final UUID UUID_SUGAR_WRITEABLE_SERVICE = UUID.fromString("00001000-0000-1000-8000-00805f9b34fb");
    private static final UUID UUID_SUGAR_WRITEABLE = UUID.fromString("00001001-0000-1000-8000-00805f9b34fb");
    private static final byte[] DATA_SUGAR_TO_WRITE = {0x5A, 0x0A, 0x03, 0x10, 0x05, 0x02, 0x0F, 0x21, 0x3B, (byte) 0xEB};
    private static final UUID UUID_SUGAR_NOTIFY = UUID.fromString("00001002-0000-1000-8000-00805f9b34fb");
    private static final UUID UUID_SUGAR_DESCRIPTOR = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");
    private static final UUID UUID_SUGAR_READABLE = UUID.fromString("00001003-0000-1000-8000-00805f9b34fb");

    private RxBleClient rxBleClient;
    private RxPermissions rxPermissions;
    private Disposable disposable = Disposables.empty();

    protected void startDetection() {
        disposable.dispose();

        String deviceName = DEVICE_NAME_SUGAR;
        UUID nitofy = UUID_SUGAR_NOTIFY;
        AtomicBoolean hasFind = new AtomicBoolean(false);
        AtomicBoolean hasComplete = new AtomicBoolean(false);
        AtomicBoolean hasWrite = new AtomicBoolean(false);
        disposable = Observable.just(1)
                .compose(rxPermissions.ensure(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.BLUETOOTH,
                        Manifest.permission.BLUETOOTH_ADMIN
                ))
                .flatMap(grated -> {
                    if (grated) {
                        return rxBleClient
                                .scanBleDevices(new ScanSettings.Builder()
                                        .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                                        .setCallbackType(ScanSettings.CALLBACK_TYPE_ALL_MATCHES)
                                        .build())
                                .doOnSubscribe(disposable -> {
                                    Timber.i("scan");
                                });
                    }
                    return Observable.error(new RuntimeException());
                })
                .doOnNext(scanResult -> Timber.i("scan %s %s",
                        scanResult.getBleDevice().getName(),
                        scanResult.getBleDevice().getMacAddress()))
                .filter(scanResult -> deviceName.equals(scanResult.getBleDevice().getName()))
                .doOnNext(scanResult ->  Timber.i("<- scan [Found]: %s %s",
                        scanResult.getBleDevice().getName(),
                        scanResult.getBleDevice().getMacAddress()))
                .flatMap(scanResult -> {
                    if (hasFind.compareAndSet(false, true)) {
                        return scanResult.getBleDevice().establishConnection(false);
                    }
                    return Observable.empty();
                })
                .flatMap(rxBleConnection ->
                        rxBleConnection.setupNotification(nitofy)
                                .doOnSubscribe(disposable -> Timber.i("setupNotification %s", nitofy.toString()))
                                .doOnNext(observable -> {
                                    if (hasWrite.compareAndSet(false, true)) {
                                        rxBleConnection.writeCharacteristic(UUID_SUGAR_WRITEABLE, DATA_SUGAR_TO_WRITE)
                                                .doOnSubscribe(disposable -> Timber.i("writeCharacteristic: %s", UUID_SUGAR_WRITEABLE))
                                                .doOnSuccess(bytes -> Timber.i("<- writeCharacteristic: length = %s", String.valueOf(bytes.length)))
                                                .doOnDispose(() -> Timber.i("writeCharacteristic Dispose"))
                                                .repeat(2)
                                                .subscribeOn(Schedulers.io())
                                                .subscribe();
                                    }
                                }))
                .flatMap(notification -> notification)
                .doOnNext(bytes -> Timber.i("<- Notification: length = %s", bytes.length))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bytes -> parseData(hasComplete, bytes),
                        Timber::i
                );
    }

    protected void parseData(AtomicBoolean hasComplete, byte[] bytes) {
        if (bytes.length >= 12
                && hasComplete.compareAndSet(false, true)) {
            float sugar = ((float) (bytes[10] << 8) + (float) (bytes[9] & 0xff)) / 18;
            Timber.i("<- sugar = %s", sugar);
            onSugarResult(sugar);
            tvSugar.setText(String.format(Locale.getDefault(), "%.1f", sugar));
        }
    }

    protected void onSugarResult(float sugar) {

    }

    @Override
    public void onStop() {
        super.onStop();
        disposable.dispose();
    }

}
