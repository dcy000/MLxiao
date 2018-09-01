package com.ml.edu.data;

import com.ml.edu.BuildConfig;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by afirez on 18-2-6.
 */

public class RetrofitFactory {

    private volatile Retrofit retrofit;

    private static volatile RetrofitFactory sInstance;

    public static RetrofitFactory getInstance() {
        if (sInstance == null) {
            synchronized (RetrofitFactory.class) {
                if (sInstance == null) {
                    sInstance = new RetrofitFactory();
                }
            }
        }
        return sInstance;
    }

    public RetrofitFactory() {

    }

    public Retrofit retrofit() {
        if (retrofit == null) {
            synchronized (this) {
                if (retrofit == null) {
                    retrofit = new Retrofit.Builder()
                            .baseUrl(BuildConfig.SERVER_ADDRESS)
                            .addConverterFactory(GsonConverterFactory.create())
                            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                            .build();
                }
            }
        }
        return retrofit;
    }
}
