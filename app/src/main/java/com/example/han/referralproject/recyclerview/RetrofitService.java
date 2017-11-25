package com.example.han.referralproject.recyclerview;


import com.example.han.referralproject.bean.Doctor;
import com.example.han.referralproject.bean.Doctors;
import com.example.han.referralproject.shopping.Goods;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RetrofitService {

    @GET("/referralProject/{link}")
    Call<List<String>> GetInfo(@Path("link") String link);

    @GET("/referralProject/{link}")
    Call<List<Doctors>> LoardMore(@Path("link") String link, @Query("limit") int limit);


    @GET("/referralProject/{link}")
    Call<List<String>> GetInfos(@Path("link") String link, @Query("position") int position);


    @GET("/referralProject/{link}")
    Call<List<Doctors>> ShowDocMsg(@Path("link") String link, @Query("position") int position);

    @GET("/referralProject/{link}")
    Call<List<Goods>> GoodsList(@Path("link") String link);


}
