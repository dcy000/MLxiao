package com.gcml.auth.face.debug.model;


import com.gcml.auth.face.debug.model.entity.FaceBdAccessToken;
import com.gcml.auth.face.debug.model.entity.FaceBdAddFace;
import com.gcml.auth.face.debug.model.entity.FaceBdAddFaceResponse;
import com.gcml.auth.face.debug.model.entity.FaceBdResult;
import com.gcml.auth.face.debug.model.entity.FaceBdVerifyLive;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface FaceBdService {

    String GRANT_TYPE = "client_credentials";
    String API_KEY = "HVVhGHwxMbOQetB2wzcqaV53";
    String SECRET_KEY = "o1zeFFQAKfyQ81HECVbATrDolWdyYLhj";

    @Headers({"Domain-Name:baidubce"})
    @GET("oauth/2.0/token")
    Observable<FaceBdAccessToken> refreshToken(
            @Query("grant_type") String grantType,
            @Query("client_id") String apiKey,
            @Query("client_secret") String secretKey
    );

    @Headers({"Domain-Name:baidubce"})
    @POST("rest/2.0/face/v3/faceverify")
    Observable<String> verifyLive(
            @Query("access_token") String accessToken,
            @Body List<FaceBdVerifyLive> faces
    );

    @Headers({"Domain-Name:baidubce"})
    @POST("rest/2.0/face/v3/faceset/user/add")
    Observable<FaceBdResult<FaceBdAddFaceResponse>> addFace(
            @Query("access_token") String accessToken,
            @Body FaceBdAddFace addFace
    );


}
