package com.medlink.danbogh.cache;

import android.content.Context;
import android.text.TextUtils;

import com.example.han.referralproject.bean.DataInfoBean;
import com.example.han.referralproject.bean.UserInfoBean;
import com.medlink.danbogh.cache.exception.UserExistException;
import com.medlink.danbogh.cache.exception.UserNotExistException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.exceptions.CompositeException;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.rx_cache2.DynamicKey;
import io.rx_cache2.EvictDynamicKey;
import io.rx_cache2.EvictProvider;
import io.rx_cache2.ProviderHelper;
import io.rx_cache2.internal.RxCache;
import io.victoralbertos.jolyglot.GsonSpeaker;

/**
 * Created by afirez on 2018/5/9.
 */

public class Repository {

    private Providers mProviders;

    private static volatile Repository repository;

    private File mCacheDir;

    public File getCacheDir() {
        return mCacheDir;
    }

    public static Repository getInstance(Context context) {
        if (repository == null) {
            synchronized (Repository.class) {
                if (repository == null) {
                    repository = new Repository(CacheUtils.getCacheDirectory(context.getApplicationContext(), "rxcache"));
                }
            }
        }
        return repository;
    }

    public Repository(File cacheDir) {
        mCacheDir = cacheDir;
        mProviders = new RxCache.Builder()
                .persistence(cacheDir, new GsonSpeaker())
                .using(Providers.class);
    }

    public Observable<UserInfoBean> isRegistered(final UserInfoBean user) {
        return mProviders.getUsers(
                ProviderHelper.<List<UserInfoBean>>withoutLoader(),
                new EvictProvider(false)
        ).flatMap(new Function<List<UserInfoBean>, ObservableSource<UserInfoBean>>() {
            @Override
            public ObservableSource<UserInfoBean> apply(List<UserInfoBean> users) throws Exception {
                if (users == null || users.isEmpty()) {
                    return Observable.error(new UserNotExistException(user.sfz));
                }

                for (UserInfoBean theUser : users) {
                    if (user.sfz != null && user.sfz.equals(theUser.sfz)) {
                        return Observable.just(theUser);
                    }
                }

                return Observable.error(new UserNotExistException(user.sfz));
            }
        });
    }

    public Observable<UserInfoBean> register(final UserInfoBean user) {
        return mProviders.getUsers(
                ProviderHelper.<List<UserInfoBean>>withoutLoader(),
                new EvictProvider(false)
        ).flatMap(new Function<List<UserInfoBean>, ObservableSource<UserInfoBean>>() {
            @Override
            public ObservableSource<UserInfoBean> apply(List<UserInfoBean> users) throws Exception {
                if (users == null || users.isEmpty()) {
                    return Observable.error(new UserExistException(user.sfz));
                }

                for (UserInfoBean theUser : users) {
                    if (user.sfz != null && user.sfz.equals(theUser.sfz)) {
                        return Observable.error(new UserExistException(user.sfz));
                    }
                }

                users.add(user);
                mProviders.getUsers(Observable.just(users), new EvictProvider(true)).subscribe();
                Observable<UserInfoBean> rxUserSrc = Observable.just(user);
                mProviders.getUser(rxUserSrc, new DynamicKey("current"), new EvictDynamicKey(true)).subscribe();
                return rxUserSrc;
            }
        });
    }

    public Observable<UserInfoBean> registerOrLogin(final UserInfoBean user) {
        return mProviders.getUsers(
                ProviderHelper.<List<UserInfoBean>>withoutLoader(),
                new EvictProvider(false)
        ).flatMap(new Function<List<UserInfoBean>, ObservableSource<UserInfoBean>>() {
            @Override
            public ObservableSource<UserInfoBean> apply(List<UserInfoBean> users) throws Exception {
                if (users != null && !users.isEmpty()) {
                    for (UserInfoBean theUser : users) {
                        if (user.sfz != null && user.sfz.equals(theUser.sfz)) {
                            Observable<UserInfoBean> rxTheUser = Observable.just(theUser);
                            mProviders.getUser(rxTheUser, new DynamicKey("current"), new EvictDynamicKey(true)).subscribe();
                            return Observable.just(theUser);
                        }
                    }
                }

                List<UserInfoBean> newUsers = new ArrayList<>();
                newUsers.add(user);
                if (users != null) {
                    newUsers.addAll(users);
                }
                mProviders.getUsers(Observable.just(newUsers), new EvictProvider(true)).subscribe();
                Observable<UserInfoBean> rxUserSrc = Observable.just(user);
                mProviders.getUser(rxUserSrc, new DynamicKey("current"), new EvictDynamicKey(true)).subscribe();
                return Observable.just(user);
            }
        }).onErrorReturn(new Function<Throwable, UserInfoBean>() {
            @Override
            public UserInfoBean apply(Throwable throwable) throws Exception {
                if (throwable instanceof CompositeException) {
                    List<UserInfoBean> newUsers = new ArrayList<>();
                    newUsers.add(user);
                    mProviders.getUsers(Observable.just(newUsers), new EvictProvider(true)).subscribe();
                    Observable<UserInfoBean> rxUserSrc = Observable.just(user);
                    mProviders.getUser(rxUserSrc, new DynamicKey("current"), new EvictDynamicKey(true)).subscribe();
                    return user;
                }
                throw new RuntimeException(throwable);
            }
        });
    }

    public Observable<UserInfoBean> login(final UserInfoBean user) {
        return mProviders.getUsers(
                ProviderHelper.<List<UserInfoBean>>withoutLoader(),
                new EvictProvider(false)
        ).flatMap(new Function<List<UserInfoBean>, ObservableSource<UserInfoBean>>() {
            @Override
            public ObservableSource<UserInfoBean> apply(List<UserInfoBean> users) throws Exception {
                if (users != null && !users.isEmpty()) {
                    for (UserInfoBean theUser : users) {
                        if (user.sfz != null && user.sfz.equals(theUser.sfz)) {
                            Observable<UserInfoBean> rxTheUser = Observable.just(theUser);
                            mProviders.getUser(rxTheUser, new DynamicKey("current"), new EvictDynamicKey(true)).subscribe();
                            return rxTheUser;
                        }
                    }
                }
                return Observable.error(new UserNotExistException(user.sfz));
            }
        });
    }

    public Observable<UserInfoBean> current() {
        return mProviders.getUser(
                ProviderHelper.<UserInfoBean>withoutLoader(), new DynamicKey("current"),
                new EvictDynamicKey(false)
        );
    }

    public Observable<DataInfoBean> measureData(final String idCard, final DataInfoBean measureData) {
        return mProviders.measureData(
                ProviderHelper.<List<DataInfoBean>>withoutLoader(),
                new DynamicKey(idCard),
                new EvictDynamicKey(false)
        ).flatMap(new Function<List<DataInfoBean>, ObservableSource<DataInfoBean>>() {
            @Override
            public ObservableSource<DataInfoBean> apply(List<DataInfoBean> measureDatas) throws Exception {
                List<DataInfoBean> newMeasureDatas = new ArrayList<>();
                if (measureDatas != null) {
                    newMeasureDatas.add(measureData);
                    newMeasureDatas.addAll(measureDatas);
                    mProviders.measureData(Observable.just(newMeasureDatas), new DynamicKey(idCard), new EvictDynamicKey(true)).subscribe();
                }
                return Observable.just(measureData);
            }
        }).onErrorReturn(new Function<Throwable, DataInfoBean>() {
            @Override
            public DataInfoBean apply(Throwable throwable) throws Exception {
                if (throwable instanceof CompositeException) {
                    List<DataInfoBean> newMeasureDatas = new ArrayList<>();
                    newMeasureDatas.add(measureData);
                    mProviders.measureData(Observable.just(newMeasureDatas), new DynamicKey(idCard), new EvictDynamicKey(true)).subscribe();
                    return measureData;
                }
                throw new RuntimeException(throwable);
            }
        });
    }

    //    1：温度；2：血压；3：心率；4：血糖，5：血氧，6：脉搏,7:胆固醇，8：血尿酸，9：心电
    public Observable<List<DataInfoBean>> measureDatas(final String idCard, final String start, final String end, final String temp) {
        return mProviders.measureData(
                ProviderHelper.<List<DataInfoBean>>withoutLoader(),
                new DynamicKey(idCard),
                new EvictDynamicKey(false)
        ).flatMap(new Function<List<DataInfoBean>, ObservableSource<List<DataInfoBean>>>() {
            @Override
            public ObservableSource<List<DataInfoBean>> apply(List<DataInfoBean> measureDatas) throws Exception {
                return Observable.fromIterable(measureDatas)
                        .filter(new Predicate<DataInfoBean>() {
                            @Override
                            public boolean test(DataInfoBean data) throws Exception {
                                boolean validCategory = ("1".equals(temp) && !TextUtils.isEmpty(data.temper_ature))
                                        || ("2".equals(temp) && data.high_pressure != 0)
                                        || ("3".equals(temp) && data.heart_rate != 0)
                                        || ("4".equals(temp) && !TextUtils.isEmpty(data.blood_sugar))
                                        || ("5".equals(temp) && !TextUtils.isEmpty(data.blood_oxygen))
                                        || ("6".equals(temp) && data.pulse != 0)
                                        || ("7".equals(temp) && !TextUtils.isEmpty(data.cholesterol))
                                        || ("8".equals(temp) && !TextUtils.isEmpty(data.uric_acid))
                                        || ("9".equals(temp) && data.ecg != 0);
                                return validCategory && data.time >= Long.valueOf(start) && data.time <= Long.valueOf(end);
                            }
                        }).toList().toObservable();
            }
        }).onErrorReturn(new Function<Throwable, List<DataInfoBean>>() {
            @Override
            public List<DataInfoBean> apply(Throwable throwable) throws Exception {
                if (throwable instanceof CompositeException) {
                    return new ArrayList<>();
                }
                throw new RuntimeException(throwable);
            }
        });
    }
}
