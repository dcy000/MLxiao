package com.medlink.danbogh.alarm;

import com.gcml.common.RxCacheHelper;
import com.gcml.common.http.ApiException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import io.rx_cache2.EvictProvider;

public class AlarmRepository {

    private AlarmProvider alarmProvider = RxCacheHelper.provider(AlarmProvider.class);

    public Observable<List<AlarmModel>> addAll(Observable<List<AlarmModel>> rxModels) {
        return alarmProvider.alarmsLocal(
                rxModels,
                new EvictProvider(true))
                .toObservable()
                .onErrorResumeNext(Observable.just(Collections.emptyList()));
    }

    public Observable<List<AlarmModel>> findAll() {
        return alarmProvider.alarmsLocal(
                Observable.empty(),
                new EvictProvider(false))
                .toObservable()
                .onErrorResumeNext(Observable.just(Collections.emptyList()));
    }


    public Observable<AlarmModel> findOneById(long id) {
        return findAll()
                .flatMap(new Function<List<AlarmModel>, ObservableSource<AlarmModel>>() {
                    @Override
                    public ObservableSource<AlarmModel> apply(List<AlarmModel> alarmModels) throws Exception {
                        for (AlarmModel alarmModel : alarmModels) {
                            if (alarmModel.getId() == id) {
                                return Observable.just(alarmModel);
                            }
                        }
                        return Observable.error(new ApiException("no alarm for id： " + id));
                    }
                });
    }

    public Observable<Object> delete(long id) {
        return findAll()
                .map(new Function<List<AlarmModel>, List<AlarmModel>>() {
                    @Override
                    public List<AlarmModel> apply(List<AlarmModel> alarmModels) throws Exception {
                        ArrayList<AlarmModel> entities = new ArrayList<>();
                        for (AlarmModel alarmModel : alarmModels) {
                            if (alarmModel.getId() != id) {
                                entities.add(alarmModel);
                            }
                        }
                        return entities;
                    }
                })
                .flatMap(new Function<List<AlarmModel>, ObservableSource<Object>>() {
                    @Override
                    public ObservableSource<Object> apply(List<AlarmModel> alarmModels) throws Exception {
                        return addAll(Observable.just(alarmModels))
                                .map(new Function<List<AlarmModel>, Object>() {
                                    @Override
                                    public Object apply(List<AlarmModel> models) throws Exception {
                                        return models;
                                    }
                                });
                    }
                });
    }

    public Observable<Object> add(AlarmModel model) {
        return findAll()
                .map(new Function<List<AlarmModel>, List<AlarmModel>>() {
                    @Override
                    public List<AlarmModel> apply(List<AlarmModel> models) throws Exception {
                        ArrayList<AlarmModel> entities = new ArrayList<>();
                        model.setId(System.currentTimeMillis());
                        entities.add(model);
                        for (AlarmModel alarmModel : models) {
                            if (alarmModel.getId() != model.getId()) {
                                entities.add(model);
                            }
                        }
                        return entities;
                    }
                })
                .flatMap(new Function<List<AlarmModel>, ObservableSource<Object>>() {
                    @Override
                    public ObservableSource<Object> apply(List<AlarmModel> alarmModels) throws Exception {
                        return addAll(Observable.just(alarmModels))
                                .map(new Function<List<AlarmModel>, Object>() {
                                    @Override
                                    public Object apply(List<AlarmModel> models) throws Exception {
                                        return models;
                                    }
                                });
                    }
                });
    }

    public Observable<Object> update(AlarmModel model) {
        return findAll()
                .map(new Function<List<AlarmModel>, List<AlarmModel>>() {
                    @Override
                    public List<AlarmModel> apply(List<AlarmModel> models) throws Exception {
                        ArrayList<AlarmModel> entities = new ArrayList<>();
                        if (model.getId() <= 0) {
                            model.setId(System.currentTimeMillis());
                        }
                        entities.add(model);
                        for (AlarmModel alarmModel : models) {
                            if (alarmModel.getId() != model.getId()) {
                                entities.add(model);
                            }
                        }
                        return entities;
                    }
                })
                .flatMap(new Function<List<AlarmModel>, ObservableSource<Object>>() {
                    @Override
                    public ObservableSource<Object> apply(List<AlarmModel> alarmModels) throws Exception {
                        return addAll(Observable.just(alarmModels))
                                .map(new Function<List<AlarmModel>, Object>() {
                                    @Override
                                    public Object apply(List<AlarmModel> models) throws Exception {
                                        return models;
                                    }
                                });
                    }
                });
    }
}
