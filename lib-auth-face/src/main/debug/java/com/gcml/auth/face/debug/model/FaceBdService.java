package com.gcml.auth.face.debug.model;


import com.gcml.auth.face.debug.model.entity.FaceBdAccessToken;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Headers;
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
    @GET("rest/2.0/face/v3/faceset/user/add")
    Observable<FaceBdAccessToken> addFace(
            @Query("grant_type") String grantType,
            @Query("client_id") String apiKey,
            @Query("client_secret") String secretKey
    );


}
