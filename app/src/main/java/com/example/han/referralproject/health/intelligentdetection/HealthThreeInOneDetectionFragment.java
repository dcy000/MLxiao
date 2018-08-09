package com.example.han.referralproject.health.intelligentdetection;


import android.Manifest;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.polidea.rxandroidble2.RxBleClient;
import com.polidea.rxandroidble2.RxBleDevice;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class HealthThreeInOneDetectionFragment extends Fragment {


    private ImageView ivRight;
    private ImageView tvNext;
    private TextView tvOne;
    private TextView tvTwo;
    private TextView tvThree;

    public HealthThreeInOneDetectionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentActivity activity = getActivity();
        rxPermissions = new RxPermissions(activity);
        rxBleClient = RxBleClient.create(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(layoutId(), container, false);
        initView(view, savedInstanceState);
        return view;
    }

    protected int layoutId() {
        return R.layout.health_fragment_three_in_one_detection;
    }

    protected void initView(View view, Bundle savedInstanceState) {
        ((TextView) view.findViewById(R.id.tv_top_title)).setText(R.string.test_sanheyi);
        ivRight = view.findViewById(R.id.iv_top_right);
        view.findViewById(R.id.ll_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getFragmentManager() != null) {
                    getFragmentManager().popBackStack();
                }
            }
        });
        ivRight.setImageResource(R.drawable.health_ic_blutooth);
        ivRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDetection();
            }
        });
        tvOne = view.findViewById(R.id.tv_san_one);
        tvTwo = view.findViewById(R.id.tv_san_two);
        tvThree = view.findViewById(R.id.tv_san_three);
    }

    private static final String DEVICE_NAME_THREE_IN_ONE = "BeneCheck";
    private static final UUID UUID_THREE_IN_ONE_NOTIFY = UUID.fromString("00002a18-0000-1000-8000-00805f9b34fb");

    private RxBleClient rxBleClient;
    private RxPermissions rxPermissions;
    private Disposable scanDisposable = Disposables.empty();
    private Disposable connectDisposable = Disposables.empty();
    private RxBleDevice bleDevice;

    protected void startDetection() {
        scanDisposable.dispose();
        AtomicBoolean hasFind = new AtomicBoolean(false);
        String deviceName = DEVICE_NAME_THREE_IN_ONE;
        scanDisposable = Observable.just(1)
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
                .filter(scanResult -> deviceName.equals(scanResult.getBleDevice().getName())
                        || (!TextUtils.isEmpty(scanResult.getBleDevice().getName())
                        && scanResult.getBleDevice().getName().contains(deviceName)))
                .doOnNext(scanResult -> {
                    scanDisposable.dispose();
                    bleDevice = scanResult.getBleDevice();
                    Timber.i("<- scan [Found]: %s %s",
                            bleDevice.getName(),
                            bleDevice.getMacAddress());
                    if (hasFind.compareAndSet(false, true)) {
                        connect(bleDevice);
                    }
                })
                .subscribe();
    }

    private void connect(RxBleDevice bleDevice) {
        AtomicBoolean hasComplete = new AtomicBoolean(false);
        connectDisposable.dispose();
        connectDisposable = Observable.just(1)
                .delay(2, TimeUnit.SECONDS)
                .flatMap(a -> bleDevice.establishConnection(false))
                .flatMap(rxBleConnection -> rxBleConnection.setupNotification(UUID_THREE_IN_ONE_NOTIFY)
                        .doOnSubscribe(disposable1 -> Timber.i("setupNotification: %s", UUID_THREE_IN_ONE_NOTIFY)))
                .flatMap(notification -> notification)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bytes -> {
                    parseData(hasComplete, bytes);
                }, Timber::i);

    }

    protected void parseData(AtomicBoolean hasComplete, byte[] bytes) {
        if (bytes.length < 13) {
            return;
        }
        int temp = ((bytes[11] & 0xff) << 8) + (bytes[10] & 0xff);
        int basic = (int) Math.pow(16, 3);
        int flag = temp / basic;
        int number = temp % basic;
        float result = (float) (number / Math.pow(10, 13 - flag));
        if (hasComplete.compareAndSet(false, true)) {
            int catogery = 0;
            if (bytes[1] == 65) {//血糖
                catogery = 1;
                tvOne.setText(String.valueOf(result));
            } else if (bytes[1] == 81) {//尿酸
                catogery = 8;
                tvTwo.setText(String.valueOf(result));
            } else if (bytes[1] == 97) {//胆固醇
                tvThree.setText(String.valueOf(result));
                catogery = 7;
            }
            onThreeInOneResult(catogery, result);
            Timber.i("%s %s", catogery, result);
        }
    }


    /**
     * 测量结果回调
     *
     * @param category 1. 血糖 , 8. 尿酸, 7. 胆固醇
     * @param result   测量值
     */
    protected void onThreeInOneResult(int category, float result) {

    }


    @Override
    public void onStop() {
        super.onStop();
        scanDisposable.dispose();
        connectDisposable.dispose();
    }
}
