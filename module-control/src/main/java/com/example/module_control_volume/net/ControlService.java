package com.example.module_control_volume.net;

import com.gcml.common.data.UserEntity;
import com.gcml.common.http.ApiResult;
import com.gcml.common.recommend.bean.get.VersionInfoBean;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ControlService {
    @GET("ZZB/vc/selone")
    Observable<ApiResult<VersionInfoBean>> getVersionInfo(@Query("vname")String vname);


    @GET("ZZB/br/selOneUser_con")
    Observable<ApiResult<UserEntity>> PersonInfo(@Query("bid") String userId);
}
