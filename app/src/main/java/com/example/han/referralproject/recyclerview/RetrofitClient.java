package com.example.han.referralproject.recyclerview;


import com.example.han.referralproject.constant.ConstantData;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static RetrofitService retrofitService;

    public static RetrofitService getClient() {
        if (retrofitService == null) {

            OkHttpClient okHttpClient = new OkHttpClient();
            Retrofit client = new Retrofit.Builder()
                    .baseUrl(ConstantData.BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            retrofitService = client.create(RetrofitService.class);

        }
        return retrofitService;
    }
}
