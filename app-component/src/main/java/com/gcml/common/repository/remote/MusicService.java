package com.gcml.common.repository.remote;


import com.gcml.common.repository.entity.FingerBean;
import com.gcml.common.repository.entity.SheetEntity;
import com.gcml.common.repository.entity.SongEntity;
import com.gcml.common.http.ApiResult;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface MusicService {
    @GET("ZZB/rep/sel_music_danforapp")
    Observable<ApiResult<List<SheetEntity>>> sheets(
            @Query("mname") String name,
            @Query("page") int page,
            @Query("limit") int limit
    );

    @GET("ZZB/rep/selSomeImitate")
    Observable<ApiResult<List<SongEntity>>> songs(
            @Query("type") int type,
            @Query("wr") String singer,
            @Query("mid") int sheetId,
            @Query("page") int page,
            @Query("limit") int limit
    );

    @Headers({"Domain-Name: abcd"})
    @GET("ZZB/api/user/finger/query/")
    Observable<ApiResult<List<FingerBean>>> getFingers(
            @Query("start") String start,
            @Query("end") String end
    );
}
