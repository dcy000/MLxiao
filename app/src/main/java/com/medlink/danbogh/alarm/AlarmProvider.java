package com.medlink.danbogh.alarm;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.rx_cache2.EvictProvider;
import io.rx_cache2.ProviderKey;

public interface AlarmProvider {

    @ProviderKey("alarms-local")
    Single<List<AlarmModel>> alarmsLocal(
            Observable<List<AlarmModel>> rxUserSrc,
            EvictProvider update
    );
}
