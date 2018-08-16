package com.gcml.module_face_recognition.network;

import com.gcml.common.repository.http.ApiResult;
import com.gcml.module_face_recognition.bean.XfGroupInfo;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/8/13 11:58
 * created by:gzq
 * description:所有该模块下的网络请求清单
 */
public interface FaceRecognitionService {

    @POST("ZZB/xf/insert_group_record")
    Observable<ApiResult<List<XfGroupInfo>>> recordXfGroupInformation(
            @Query("userid") String userid,
            @Query("gid") String gid,
            @Query("xfid") String xfid
    );
    @GET("ZZB/br/seltoken")
    Observable<ApiResult<String>> getQiniuToken();

    @POST("ZZB/br/upUser_photo")
    Observable<ApiResult<Object>> syncRegistHeadUrl(
            @Query("user_photo") String userPhoto,
            @Query("bid") String userId,
            @Query("xfid") String xfid
    );
}
