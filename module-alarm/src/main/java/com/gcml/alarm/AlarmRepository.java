package com.gcml.alarm;

import com.gcml.common.RetrofitHelper;
import com.gcml.common.RxCacheHelper;
import com.gcml.common.data.UserSpHelper;
import com.gcml.common.http.ApiException;
import com.gcml.common.recommend.bean.get.Doctor;
import com.gcml.common.utils.RxUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import io.rx_cache2.EvictProvider;

public class AlarmRepository {

    private AlarmProvider alarmProvider = RxCacheHelper.provider(AlarmProvider.class);

    private AlarmApiService alarmApiService = RetrofitHelper.service(AlarmApiService.class);


    public Observable<List<ClueInfoBean>> getClue() {
        return alarmApiService.getClue(UserSpHelper.getUserId())
                .compose(RxUtils.apiResultTransformer());
    }

    public Observable<List<AlreadyYuyue>> contractAlready(String doctorId) {
        return alarmApiService.contractAlready(doctorId)
                .compose(RxUtils.apiResultTransformer());
    }

    public Observable<String> updateStatus(
            String rid,
            String state) {
        return alarmApiService.updateStatus(rid, state)
                .compose(RxUtils.apiResultTransformer());
    }


    public Observable<Object> addEatMedicalRecord(
            String userName,
            String content,
           String state) {
        String time = String.valueOf(Calendar.getInstance().getTimeInMillis());
        return  alarmApiService.addEatMedicalRecord(userName, content, time, state)
                .compose(RxUtils.apiResultTransformer());
    }

    public Observable<Doctor> doctor(){
        return alarmApiService.doctor(UserSpHelper.getUserId())
                .compose(RxUtils.apiResultTransformer());
    }

    public Observable<List<AlarmEntity>> addAll(Observable<List<AlarmEntity>> rxModels) {
        return alarmProvider.alarmsLocal(
                rxModels,
                new EvictProvider(true))
                .toObservable()
                .onErrorResumeNext(Observable.just(Collections.emptyList()));
    }

    public Observable<List<AlarmEntity>> findAll() {
        return alarmProvider.alarmsLocal(
                Observable.empty(),
                new EvictProvider(false))
                .toObservable()
                .onErrorResumeNext(Observable.just(Collections.emptyList()));
    }


    public Observable<AlarmEntity> findOneById(long id) {
        return findAll()
                .flatMap(new Function<List<AlarmEntity>, ObservableSource<AlarmEntity>>() {
                    @Override
                    public ObservableSource<AlarmEntity> apply(List<AlarmEntity> alarmEntities) throws Exception {
                        for (AlarmEntity alarmEntity : alarmEntities) {
                            if (alarmEntity.getId() == id) {
                                return Observable.just(alarmEntity);
                            }
                        }
                        return Observable.error(new ApiException("no alarm for id： " + id));
                    }
                });
    }

    public Observable<Object> delete(long id) {
        return findAll()
                .map(new Function<List<AlarmEntity>, List<AlarmEntity>>() {
                    @Override
                    public List<AlarmEntity> apply(List<AlarmEntity> alarmEntities) throws Exception {
                        ArrayList<AlarmEntity> entities = new ArrayList<>();
                        for (AlarmEntity alarmEntity : alarmEntities) {
                            if (alarmEntity.getId() != id) {
                                entities.add(alarmEntity);
                            }
                        }
                        return entities;
                    }
                })
                .flatMap(new Function<List<AlarmEntity>, ObservableSource<Object>>() {
                    @Override
                    public ObservableSource<Object> apply(List<AlarmEntity> alarmEntities) throws Exception {
                        return addAll(Observable.just(alarmEntities))
                                .map(new Function<List<AlarmEntity>, Object>() {
                                    @Override
                                    public Object apply(List<AlarmEntity> models) throws Exception {
                                        return models;
                                    }
                                });
                    }
                });
    }

    public Observable<Object> add(AlarmEntity model) {
        return findAll()
                .map(new Function<List<AlarmEntity>, List<AlarmEntity>>() {
                    @Override
                    public List<AlarmEntity> apply(List<AlarmEntity> models) throws Exception {
                        ArrayList<AlarmEntity> entities = new ArrayList<>();
                        model.setId(System.currentTimeMillis());
                        entities.add(model);
                        for (AlarmEntity alarmEntity : models) {
                            if (alarmEntity.getId() != model.getId()) {
                                entities.add(alarmEntity);
                            }
                        }
                        return entities;
                    }
                })
                .flatMap(new Function<List<AlarmEntity>, ObservableSource<Object>>() {
                    @Override
                    public ObservableSource<Object> apply(List<AlarmEntity> alarmEntities) throws Exception {
                        return addAll(Observable.just(alarmEntities))
                                .map(new Function<List<AlarmEntity>, Object>() {
                                    @Override
                                    public Object apply(List<AlarmEntity> models) throws Exception {
                                        return models;
                                    }
                                });
                    }
                });
    }

    public Observable<Object> update(AlarmEntity model) {
        return findAll()
                .map(new Function<List<AlarmEntity>, List<AlarmEntity>>() {
                    @Override
                    public List<AlarmEntity> apply(List<AlarmEntity> models) throws Exception {
                        ArrayList<AlarmEntity> entities = new ArrayList<>();
                        if (model.getId() <= 0) {
                            model.setId(System.currentTimeMillis());
                        }
                        entities.add(model);
                        for (AlarmEntity alarmEntity : models) {
                            if (alarmEntity.getId() != model.getId()) {
                                entities.add(alarmEntity);
                            }
                        }
                        return entities;
                    }
                })
                .flatMap(new Function<List<AlarmEntity>, ObservableSource<Object>>() {
                    @Override
                    public ObservableSource<Object> apply(List<AlarmEntity> alarmEntities) throws Exception {
                        return addAll(Observable.just(alarmEntities))
                                .map(new Function<List<AlarmEntity>, Object>() {
                                    @Override
                                    public Object apply(List<AlarmEntity> models) throws Exception {
                                        return models;
                                    }
                                });
                    }
                });
    }
}
