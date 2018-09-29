package com.example.han.referralproject.qianyue;

import com.example.han.referralproject.health.intelligentdetection.entity.ApiResponse;
import com.example.han.referralproject.qianyue.bean.DoctorInfoBean;
import com.gcml.common.repository.http.ApiResult;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * Created by lenovo on 2018/9/28.
 */

public interface QianYueService {

    @GET("/ZZB//docter/chaDocter")
    Observable<ApiResult<DoctorInfoBean>> getDoctInfo(String doctorId);

}
