package com.gcml.common.utils;


import android.annotation.SuppressLint;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.ObservableField;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.annotation.NonNull;

import com.gcml.common.repository.http.ApiException;
import com.gcml.common.repository.http.ApiResult;
import com.gcml.common.repository.utils.Serializer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.uber.autodispose.AutoDispose;
import com.uber.autodispose.AutoDisposeConverter;
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;

import java.lang.reflect.Type;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Cancellable;
import io.reactivex.functions.Function;

/**
 * Created by afirez on 18-2-6.
 */

public class RxUtils {

    public static <T> ObservableTransformer<ApiResult<T>, T> apiResultTransformer() {
        return new ObservableTransformer<ApiResult<T>, T>() {
            @Override
            public ObservableSource<T> apply(Observable<ApiResult<T>> upstream) {
                return upstream.flatMap(
                        new Function<ApiResult<T>, Observable<T>>() {
                            @Override
                            public Observable<T> apply(ApiResult<T> result) {
                                if (result.isSuccessful()) {
                                    if (result.getData() == null) {
                                        Type type = new TypeToken<T>() {}.getType();
                                        T t = Serializer.getInstance().deserialize("{}", type);
                                        return Observable.just(t);
                                    }
                                    return Observable.just(result.getData());
                                } else {
                                    return Observable.error(new ApiException(result.getMessage()));
                                }
                            }
                        }
                );
            }
        };
    }

    public static <T> AutoDisposeConverter<T> autoDisposeConverter(LifecycleOwner owner) {
        return AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(owner));
    }

    public static <T> AutoDisposeConverter<T> autoDisposeConverter(LifecycleOwner owner, Lifecycle.Event event) {
        return AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(owner, event));
    }

    public static <T> Observable<T> toObservable(@NonNull final ObservableField<T> observableField) {
        return Observable.create(new ObservableOnSubscribe<T>() {
            @Override
            public void subscribe(ObservableEmitter<T> emitter) throws Exception {
                emitter.onNext(observableField.get());
                android.databinding.Observable.OnPropertyChangedCallback callback = new android.databinding.Observable.OnPropertyChangedCallback() {
                    @Override
                    public void onPropertyChanged(android.databinding.Observable sender, int propertyId) {
                        if (sender == observableField) {
                            emitter.onNext(observableField.get());
                        }
                    }
                };
                emitter.setCancellable(new Cancellable() {
                    @Override
                    public void cancel() throws Exception {
                        observableField.removeOnPropertyChangedCallback(callback);
                    }
                });
                observableField.addOnPropertyChangedCallback(callback);
            }
        });
    }

    public static Observable<Integer> rxWifiLevel(Context context, int numsLevel) {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                emitter.onNext("1");
                BroadcastReceiver receiver = new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        emitter.onNext("1");
                    }
                };
                IntentFilter filter = new IntentFilter(WifiManager.RSSI_CHANGED_ACTION);
                context.getApplicationContext().registerReceiver(receiver, filter);
                emitter.setCancellable(new Cancellable() {
                    @Override
                    public void cancel() throws Exception {
                        context.getApplicationContext().unregisterReceiver(receiver);
                    }
                });
            }
        }).map(new Function<String, Integer>() {
            @Override
            public Integer apply(String s) throws Exception {
                @SuppressLint("WifiManagerPotentialLeak")
                WifiManager wm = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                if (wm == null) {
                    return 0;
                }
                @SuppressLint("MissingPermission")
                WifiInfo wifiInfo = wm.getConnectionInfo();
                String bssid = wifiInfo.getBSSID();
                if (bssid == null) {
                    return 0;
                }
                return WifiManager.calculateSignalLevel(wifiInfo.getRssi(), numsLevel);
            }
        }).distinct();
    }

    public static Observable<Integer> rxCountDown(int interval, int times) {
        return Observable.interval(0, interval, TimeUnit.SECONDS)
                .map(new Function<Long, Integer>() {
                    @Override
                    public Integer apply(Long aLong) throws Exception {
                        return times - aLong.intValue();
                    }
                })
                .take(times + 1);

    }
}
