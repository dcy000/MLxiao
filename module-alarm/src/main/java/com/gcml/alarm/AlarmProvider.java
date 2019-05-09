package com.gcml.alarm;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.rx_cache2.EvictProvider;
import io.rx_cache2.ProviderKey;

public interface AlarmProvider {

    @ProviderKey("alarms-local")
    Single<List<AlarmEntity>> alarmsLocal(
            Observable<List<AlarmEntity>> rxUserSrc,
            EvictProvider update
    );
}
