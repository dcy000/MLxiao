package com.example.module_person.service;

import io.reactivex.Observable;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface PersonAPI {
    /**
     * 修改个人基本信息
     *
     * @param userId
     * @param height
     * @param weight
     * @param eatingHabits
     * @param smoke
     * @param drink
     * @param exerciseHabits
     * @param mh
     * @param dz
     * @return
     */
    @POST("ZZB/br/update_user_onecon")
    Observable<Object> alertUserInfo(
            @Query("bid") String userId,
            @Query("height") String height,
            @Query("weight") String weight,
            @Query("eating_habits") String eatingHabits,
            @Query("smoke") String smoke,
            @Query("drink") String drink,
            @Query("exercise_habits") String exerciseHabits,
            @Query("mh") String mh,
            @Query("dz") String dz
    );
}
