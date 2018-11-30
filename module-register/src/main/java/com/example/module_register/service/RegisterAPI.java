package com.example.module_register.service;

import com.example.module_register.bean.ContractInfo;
import com.gzq.lib_core.bean.UserInfoBean;
import com.gzq.lib_core.http.model.HttpResult;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RegisterAPI {
    /**
     * 注册账号
     *
     * @param age
     * @param bname
     * @param sex
     * @param eqid
     * @param tel
     * @param pwd
     * @param dz
     * @param sfz
     * @param height
     * @param weight
     * @param bloodType
     * @param eatingHabits
     * @param smoke
     * @param drink
     * @param exerciseHabits
     * @return
     */
    @POST("ZZB/br/appadd")
    Observable<HttpResult<UserInfoBean>> registerAccount(
            @Query("age") String age,
            @Query("bname") String bname,
            @Query("sex") String sex,
            @Query("eqid") String eqid,
            @Query("tel") String tel,
            @Query("pwd") String pwd,
            @Query("dz") String dz,
            @Query("sfz") String sfz,
            @Query("height") String height,
            @Query("weight") String weight,
            @Query("bloodType") String bloodType,
            @Query("eatingHabits") String eatingHabits,
            @Query("smoke") String smoke,
            @Query("drink") String drink,
            @Query("exerciseHabits") String exerciseHabits
    );


    @GET("ZZB/docter/docter_user")
    Observable<HttpResult<ContractInfo>> getContractInfo(@Query("userid") String userId, @Query("docterid") String docId);
    /**
     * 绑定签约医生
     *
     * @param userId
     * @param doctorId
     * @return
     */
    @POST("ZZB/br/qianyue")
    Observable<Object> bindDoctor(@Query("bid") String userId, @Query("doid") String doctorId);

    /**
     * 上传疾病历史
     *
     * @param userId
     * @param mh
     * @return
     */
    @POST("ZZB/br/mhrecord")
    Observable<Object> setDisableHistory(@Query("bid") String userId, @Query("mh") String mh);

}
