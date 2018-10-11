package com.example.han.referralproject.qianyue;

import com.example.han.referralproject.qianyue.bean.DoctorInfoBean;
import com.gcml.common.repository.http.ApiResult;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by lenovo on 2018/9/28.
 */

public interface QianYueService {

    @GET("/ZZB/docter/sel_one_doctor_con")
    Observable<ApiResult<DoctorInfoBean>> getDoctInfo(@Query ("docterid") String doctorId);

}
