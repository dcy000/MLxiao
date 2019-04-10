package com.gcml.module_health_record.network;

import android.text.TextUtils;

import com.gcml.common.data.UserSpHelper;
import com.gcml.common.recommend.bean.post.DetectionData;
import com.gcml.common.recommend.bean.post.DetectionDataProvider;
import com.gcml.common.repository.IRepositoryHelper;
import com.gcml.common.repository.RepositoryApp;
import com.gcml.common.utils.RxUtils;
import com.gcml.module_health_record.bean.BUA;
import com.gcml.module_health_record.bean.BloodOxygenHistory;
import com.gcml.module_health_record.bean.BloodPressureHistory;
import com.gcml.module_health_record.bean.BloodSugarHistory;
import com.gcml.module_health_record.bean.CholesterolHistory;
import com.gcml.module_health_record.bean.ECGHistory;
import com.gcml.module_health_record.bean.HeartRateHistory;
import com.gcml.module_health_record.bean.PulseHistory;
import com.gcml.module_health_record.bean.TemperatureHistory;
import com.gcml.module_health_record.bean.WeightHistory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import io.rx_cache2.DynamicKey;
import io.rx_cache2.EvictDynamicKey;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/9/20 10:32
 * created by:gzq
 * description:TODO
 */
public class HealthRecordRepository {
    private static IRepositoryHelper mRepositoryHelper = RepositoryApp.INSTANCE.repositoryComponent().repositoryHelper();
    private static HealthRecordServer healthRecordServer = mRepositoryHelper.retrofitService(HealthRecordServer.class);
    private static DetectionDataProvider detectionDataProvider = mRepositoryHelper.rxCacheProvider(DetectionDataProvider.class);

    private static Observable<List<DetectionData>> getDetectionDataForTime(
            String userId,
            String start,
            String end,
            String temp) {
        Observable<List<DetectionData>> detectionDataLocal = detectionDataProvider.detectionDataLocal(
                Observable.empty(),
                new DynamicKey(userId),
                new EvictDynamicKey(false))
                .toObservable().onErrorResumeNext(Observable.just(Collections.emptyList()));
        return detectionDataLocal.map(new Function<List<DetectionData>, List<DetectionData>>() {
            @Override
            public List<DetectionData> apply(List<DetectionData> detectionData) throws Exception {
                ArrayList<DetectionData> newData = new ArrayList<>();
                for (DetectionData data : detectionData) {
                    if (data != null) {
                        boolean validData = false;
                        try {
                            boolean validCategory = ("1".equals(temp) && data.getTemperAture() != 0f)
                                    || ("2".equals(temp) && data.getHighPressure() != 0)
                                    || ("3".equals(temp) && data.getHeartRate() != 0)
                                    || ("4".equals(temp) && data.getBloodSugar() != 0f)
                                    || ("5".equals(temp) && data.getBloodOxygen() != 0f)
                                    || ("6".equals(temp) && data.getPulse() != 0)
                                    || ("7".equals(temp) && data.getCholesterol() != 0f)
                                    || ("8".equals(temp) && data.getUricAcid() != 0f)
                                    || ("9".equals(temp) && !TextUtils.isEmpty(data.getEcg()))
                                    || ("10".equals(temp) && data.getWeight() != 0f);
                            validData = validCategory
                                    && Long.valueOf(data.getTime()) >= Long.valueOf(start)
                                    && Long.valueOf(data.getTime()) <= Long.valueOf(end);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (validData) {
                            newData.add(data);
                        }
                    }
                }
                return newData;
            }
        });
    }

    public static Observable<List<TemperatureHistory>> getTemperatureHistory(String start, String end, String temp) {
        String userId = UserSpHelper.getUserId();
        boolean noNetwork = UserSpHelper.isNoNetwork();
        if (!noNetwork) {
            return healthRecordServer.getTemperatureHistory(userId, start, end, temp).compose(RxUtils.apiResultTransformer());
        }

        Observable<List<DetectionData>> detectionData = getDetectionDataForTime(userId, start, end, temp);
        return detectionData.map(new Function<List<DetectionData>, List<TemperatureHistory>>() {
            @Override
            public List<TemperatureHistory> apply(List<DetectionData> detectionData) throws Exception {
                ArrayList<TemperatureHistory> newData = new ArrayList<>();
                for (DetectionData data : detectionData) {
                    if (data != null) {
                        TemperatureHistory mapData = new TemperatureHistory(
                                data.getTemperAture(),
                                Long.valueOf(data.getTime())
                        );
                        newData.add(mapData);
                    }
                }
                return newData;
            }
        });
    }

    public static Observable<List<BloodPressureHistory>> getBloodpressureHistory(String start, String end, String temp) {
        String userId = UserSpHelper.getUserId();
        boolean noNetwork = UserSpHelper.isNoNetwork();
        if (!noNetwork) {
            return healthRecordServer.getBloodpressureHistory(userId, start, end, temp).compose(RxUtils.apiResultTransformer());
        }

        Observable<List<DetectionData>> detectionData = getDetectionDataForTime(userId, start, end, temp);
        return detectionData.map(new Function<List<DetectionData>, List<BloodPressureHistory>>() {
            @Override
            public List<BloodPressureHistory> apply(List<DetectionData> detectionData) throws Exception {
                ArrayList<BloodPressureHistory> newData = new ArrayList<>();
                for (DetectionData data : detectionData) {
                    if (data != null) {
                        BloodPressureHistory mapData = new BloodPressureHistory(
                                data.getLowPressure(),
                                data.getHighPressure(),
                                Long.valueOf(data.getTime())
                        );
                        newData.add(mapData);
                    }
                }
                return newData;
            }
        });
    }

    public static Observable<List<BloodSugarHistory>> getBloodSugarHistory(String start, String end, String temp) {
        String userId = UserSpHelper.getUserId();
        boolean noNetwork = UserSpHelper.isNoNetwork();
        if (!noNetwork) {
            return healthRecordServer.getBloodSugarHistory(userId, start, end, temp).compose(RxUtils.apiResultTransformer());
        }

        Observable<List<DetectionData>> detectionData = getDetectionDataForTime(userId, start, end, temp);
        return detectionData.map(new Function<List<DetectionData>, List<BloodSugarHistory>>() {
            @Override
            public List<BloodSugarHistory> apply(List<DetectionData> detectionData) throws Exception {
                ArrayList<BloodSugarHistory> newData = new ArrayList<>();
                for (DetectionData data : detectionData) {
                    if (data != null) {
                        BloodSugarHistory mapData = new BloodSugarHistory(
                                data.getBloodSugar(),
                                Long.valueOf(data.getTime())
                        );
                        newData.add(mapData);
                    }
                }
                return newData;
            }
        });
    }

    public static Observable<List<BloodOxygenHistory>> getBloodOxygenHistory(String start, String end, String temp) {
        String userId = UserSpHelper.getUserId();
        boolean noNetwork = UserSpHelper.isNoNetwork();
        if (!noNetwork) {
            return healthRecordServer.getBloodOxygenHistory(userId, start, end, temp).compose(RxUtils.apiResultTransformer());
        }

        Observable<List<DetectionData>> detectionData = getDetectionDataForTime(userId, start, end, temp);
        return detectionData.map(new Function<List<DetectionData>, List<BloodOxygenHistory>>() {
            @Override
            public List<BloodOxygenHistory> apply(List<DetectionData> detectionData) throws Exception {
                ArrayList<BloodOxygenHistory> newData = new ArrayList<>();
                for (DetectionData data : detectionData) {
                    if (data != null) {
                        BloodOxygenHistory mapData = new BloodOxygenHistory(
                                data.getBloodOxygen(),
                                Long.valueOf(data.getTime())
                        );
                        newData.add(mapData);
                    }
                }
                return newData;
            }
        });
    }

    public static Observable<List<HeartRateHistory>> getHeartRateHistory(String start, String end, String temp) {
        String userId = UserSpHelper.getUserId();
        boolean noNetwork = UserSpHelper.isNoNetwork();
        if (!noNetwork) {
            return healthRecordServer.getHeartRateHistory(userId, start, end, temp).compose(RxUtils.apiResultTransformer());
        }

        Observable<List<DetectionData>> detectionData = getDetectionDataForTime(userId, start, end, temp);
        return detectionData.map(new Function<List<DetectionData>, List<HeartRateHistory>>() {
            @Override
            public List<HeartRateHistory> apply(List<DetectionData> detectionData) throws Exception {
                ArrayList<HeartRateHistory> newData = new ArrayList<>();
                for (DetectionData data : detectionData) {
                    if (data != null) {
                        HeartRateHistory mapData = new HeartRateHistory();
                        mapData.heart_rate = data.getHeartRate();
                        mapData.time = Long.valueOf(data.getTime());
                        newData.add(mapData);
                    }
                }
                return newData;
            }
        });
    }


    public static Observable<List<PulseHistory>> getPulseHistory(String start, String end, String temp) {
        String userId = UserSpHelper.getUserId();
        boolean noNetwork = UserSpHelper.isNoNetwork();
        if (!noNetwork) {
            return healthRecordServer.getPulseHistory(userId, start, end, temp).compose(RxUtils.apiResultTransformer());
        }

        Observable<List<DetectionData>> detectionData = getDetectionDataForTime(userId, start, end, temp);
        return detectionData.map(new Function<List<DetectionData>, List<PulseHistory>>() {
            @Override
            public List<PulseHistory> apply(List<DetectionData> detectionData) throws Exception {
                ArrayList<PulseHistory> newData = new ArrayList<>();
                for (DetectionData data : detectionData) {
                    if (data != null) {
                        PulseHistory mapData = new PulseHistory();
                        mapData.pulse = data.getPulse();
                        mapData.time = Long.valueOf(data.getTime());
                        newData.add(mapData);
                    }
                }
                return newData;
            }
        });
    }

    public static Observable<List<CholesterolHistory>> getCholesterolHistory(String start, String end, String temp) {
        String userId = UserSpHelper.getUserId();
        boolean noNetwork = UserSpHelper.isNoNetwork();
        if (!noNetwork) {
            return healthRecordServer.getCholesterolHistory(userId, start, end, temp).compose(RxUtils.apiResultTransformer());
        }

        Observable<List<DetectionData>> detectionData = getDetectionDataForTime(userId, start, end, temp);
        return detectionData.map(new Function<List<DetectionData>, List<CholesterolHistory>>() {
            @Override
            public List<CholesterolHistory> apply(List<DetectionData> detectionData) throws Exception {
                ArrayList<CholesterolHistory> newData = new ArrayList<>();
                for (DetectionData data : detectionData) {
                    if (data != null) {
                        CholesterolHistory mapData = new CholesterolHistory(
                                data.getCholesterol(),
                                Long.valueOf(data.getTime())
                        );
                        newData.add(mapData);
                    }
                }
                return newData;
            }
        });
    }

    public static Observable<List<BUA>> getBUAHistory(String start, String end, String temp) {
        String userId = UserSpHelper.getUserId();
        boolean noNetwork = UserSpHelper.isNoNetwork();
        if (!noNetwork) {
            return healthRecordServer.getBUAHistory(userId, start, end, temp).compose(RxUtils.apiResultTransformer());
        }

        Observable<List<DetectionData>> detectionData = getDetectionDataForTime(userId, start, end, temp);
        return detectionData.map(new Function<List<DetectionData>, List<BUA>>() {
            @Override
            public List<BUA> apply(List<DetectionData> detectionData) throws Exception {
                ArrayList<BUA> newData = new ArrayList<>();
                for (DetectionData data : detectionData) {
                    if (data != null) {
                        BUA mapData = new BUA(
                                data.getUricAcid(),
                                Long.valueOf(data.getTime())
                        );
                        newData.add(mapData);
                    }
                }
                return newData;
            }
        });
    }

    public static Observable<List<ECGHistory>> getECGHistory(String start, String end, String temp) {
        String userId = UserSpHelper.getUserId();
        boolean noNetwork = UserSpHelper.isNoNetwork();
        if (!noNetwork) {
            return healthRecordServer.getECGHistory(userId, start, end, temp).compose(RxUtils.apiResultTransformer());
        }

        Observable<List<DetectionData>> detectionData = getDetectionDataForTime(userId, start, end, temp);
        return detectionData.map(new Function<List<DetectionData>, List<ECGHistory>>() {
            @Override
            public List<ECGHistory> apply(List<DetectionData> detectionData) throws Exception {
                ArrayList<ECGHistory> newData = new ArrayList<>();
                for (DetectionData data : detectionData) {
                    if (data != null) {
                        ECGHistory mapData = new ECGHistory();
                        mapData.ecg = data.getEcg();
                        mapData.result = data.getResult();
                        mapData.result_url = data.getResultUrl();
                        mapData.time = Long.valueOf(data.getTime());
                        newData.add(mapData);
                    }
                }
                return newData;
            }
        });
    }

    public static Observable<List<WeightHistory>> getWeight(String start, String end, String temp) {
        String userId = UserSpHelper.getUserId();
        boolean noNetwork = UserSpHelper.isNoNetwork();
        if (!noNetwork) {
            return healthRecordServer.getWeight(userId, start, end, temp).compose(RxUtils.apiResultTransformer());
        }

        Observable<List<DetectionData>> detectionData = getDetectionDataForTime(userId, start, end, temp);
        return detectionData.map(new Function<List<DetectionData>, List<WeightHistory>>() {
            @Override
            public List<WeightHistory> apply(List<DetectionData> detectionData) throws Exception {
                ArrayList<WeightHistory> newData = new ArrayList<>();
                for (DetectionData data : detectionData) {
                    if (data != null) {
                        WeightHistory mapData = new WeightHistory(
                                data.getWeight(),
                                Long.valueOf(data.getTime())
                        );
                        newData.add(mapData);
                    }
                }
                return newData;
            }
        });
    }
}
