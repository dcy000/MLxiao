package com.example.han.referralproject.recyclerview;


import com.example.han.referralproject.bean.Doctor;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RetrofitService {

    @GET("/referralProject/{link}")
    Call<List<String>> GetInfo(@Path("link") String link);

    @GET("/referralProject/{link}")
    Call<List<Doctor>> LoardMore(@Path("link") String link, @Query("limit") int limit);


    @GET("/referralProject/{link}")
    Call<List<String>> GetInfos(@Path("link") String link, @Query("position") int position);


    @GET("/referralProject/{link}")
    Call<List<Doctor>> ShowDocMsg(@Path("link") String link, @Query("position") int position);


}
