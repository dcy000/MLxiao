package com.example.han.referralproject.new_music;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface MusicAPI {
    @Headers({"Domain-Name: music"})
    @GET("v1/restserver/ting")
    Observable<HttpCallback<DownloadInfo>> getMusicDownloadInfo(
            @Query("method") String method,
            @Query("songid") String songId
    );
}
