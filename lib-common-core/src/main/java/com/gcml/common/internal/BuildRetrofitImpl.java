package com.gcml.common.internal;

import android.content.Context;

import com.gcml.common.BuildConfig;
import com.gcml.common.GsonHelper;
import com.gcml.common.OkHttpClientHelper;
import com.gcml.common.api.BuildRetrofit;
import com.google.auto.service.AutoService;

import me.jessyan.retrofiturlmanager.RetrofitUrlManager;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@AutoService(BuildRetrofit.class)
public class BuildRetrofitImpl implements BuildRetrofit {
    @Override
    public void buildRetrofit(Context context, Retrofit.Builder builder) {
        RetrofitUrlManager.getInstance().putDomain("seniverse", BuildConfig.API_SENIVERSE);
        RetrofitUrlManager.getInstance().putDomain("baidubce", BuildConfig.API_BAIDUBCE);
        RetrofitUrlManager.getInstance().putDomain("hegui", BuildConfig.API_HEGUI);
        builder.baseUrl(BuildConfig.SERVER_ADDRESS)
                .client(OkHttpClientHelper.get())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(GsonHelper.get()));

    }
}
