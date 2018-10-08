package com.gcml.common.location;

import android.content.Context;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Cancellable;

public class BdLocationHelper {

    public Observable<BDLocation> startLocation(Context context) {
        return Observable.create(new ObservableOnSubscribe<BDLocation>() {
            @Override
            public void subscribe(ObservableEmitter<BDLocation> emitter) throws Exception {
                LocationClient client = new LocationClient(context.getApplicationContext());
                LocationClientOption locOption = new LocationClientOption();
                locOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
                locOption.setCoorType("bd09ll");
                locOption.setIsNeedAddress(true);
                locOption.setOpenGps(true);
                locOption.setScanSpan(3000);
                client.setLocOption(locOption);
                BDAbstractLocationListener listener = new BDAbstractLocationListener() {
                    @Override
                    public void onReceiveLocation(BDLocation bdLocation) {
                        if (!emitter.isDisposed()) {
                            emitter.onNext(bdLocation);
                            emitter.onComplete();
                        }
                    }
                };
                emitter.setCancellable(new Cancellable() {
                    @Override
                    public void cancel() throws Exception {
                        if (client.isStarted()) {
                            client.stop();
                        }
                        client.unRegisterLocationListener(listener);
                    }
                });
                client.registerLocationListener(listener);
                client.start();
            }
        });
    }
}
