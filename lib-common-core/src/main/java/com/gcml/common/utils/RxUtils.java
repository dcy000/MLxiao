package com.gcml.common.utils;


import android.annotation.SuppressLint;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.ObservableField;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.annotation.NonNull;

import com.gcml.common.AppDelegate;
import com.gcml.common.R;
import com.gcml.common.http.ApiException;
import com.gcml.common.http.ApiResult;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.uber.autodispose.AutoDispose;
import com.uber.autodispose.AutoDisposeConverter;
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.functions.Cancellable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.plugins.RxJavaPlugins;
import okhttp3.ResponseBody;
import retrofit2.HttpException;

/**
 * Created by afirez on 18-2-6.
 */

public class RxUtils {

    static {
        RxJavaPlugins.setErrorHandler(new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                throwable.printStackTrace();
            }
        });
    }

    public static <T> ObservableTransformer<ApiResult<T>, T> apiResultTransformer() {
        return new ObservableTransformer<ApiResult<T>, T>() {
            @Override
            public ObservableSource<T> apply(Observable<ApiResult<T>> upstream) {
                return upstream
                        .onErrorResumeNext(httpErrorProcessor())
                        .flatMap(apiResultMapper());
            }
        };
    }

    @NonNull
    private static <T> Function<ApiResult<T>, Observable<T>> apiResultMapper() {
        return new Function<ApiResult<T>, Observable<T>>() {
            @Override
            public Observable<T> apply(ApiResult<T> result) {
                if (result.isSuccessful()) {
                    if (result.getData() == null) {
                        TypeToken<T> typeToken = new TypeToken<T>() {
                        };
                        Type type = typeToken.getType();
                        T t;
                        try {
                            t = (T) new ArrayList<>();
                        } catch (Throwable e) {
                            t = Serializer.getInstance().deserialize("{}", type);
                        }
                        return Observable.just(t);
                    }
                    return Observable.just(result.getData());
                }
                return Observable.error(new ApiException(result.getMessage(), result.getCode()));
            }
        };
    }

    @NonNull
    private static <T> Function<Throwable, ObservableSource<? extends ApiResult<T>>> httpErrorProcessor() {
        return new Function<Throwable, ObservableSource<? extends ApiResult<T>>>() {
            @Override
            public ObservableSource<? extends ApiResult<T>> apply(Throwable throwable) throws Exception {
//                if (throwable instanceof HttpException
//                        && ((HttpException) throwable).code() >= 500) {
//                    HttpException error = (HttpException) throwable;
//                    ResponseBody body = error.response().errorBody();
//                    String resultJson;
//                    if (body != null) {
//                        resultJson = body.string();
//                        Type type = new TypeToken<ApiResult<T>>() {
//                        }.getType();
//                        ApiResult<T> result = Serializer.getInstance().deserialize(resultJson, type);
//                        return Observable.just(result);
//                    }
//                }
                return Observable.error(new ApiException(AppDelegate.INSTANCE.app().getString(R.string.busy_network)));
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
        }).distinctUntilChanged();
    }

    public static Observable<Integer> rxWifiLevels(Context context, int numsLevel, ScanResult wifi) {
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
                String bssid = wifi.BSSID;
                if (bssid == null) {
                    return 0;
                }
                return WifiManager.calculateSignalLevel(wifi.level, numsLevel);
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


    public static Observable<Long> rxTimer(int times) {
        return Observable.timer(times, TimeUnit.SECONDS);
    }
}
