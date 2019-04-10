package com.gcml.common.recommend.bean.post;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.rx_cache2.DynamicKey;
import io.rx_cache2.EvictDynamicKey;
import io.rx_cache2.ProviderKey;

public interface DetectionDataProvider {

    @ProviderKey("detection-data-local")
    Single<List<DetectionData>> detectionDataLocal(
            Observable<List<DetectionData>> rxMeasureDataSrc,
            DynamicKey userId,
            EvictDynamicKey evictDynamicKey
    );
}
