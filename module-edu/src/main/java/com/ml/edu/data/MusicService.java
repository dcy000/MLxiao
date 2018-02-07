package com.ml.edu.data;

import com.ml.edu.data.entity.SheetEntity;
import com.ml.edu.data.entity.SongEntity;

import java.util.List;


import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by afirez on 18-2-6.
 */

public interface MusicService {

    //    curl -XPOST http://192.168.200.182:8080/ZZB/rep/sel_music_danforapp -d "sheetName=\"\"&page=1&limit=4"

    @GET("rep/sel_music_danforapp")
    Observable<ApiResult<List<SheetEntity>>> sheets(
            @Query("sheetName") String name,
            @Query("page") int page,
            @Query("limit") int limit
    );

    @GET("rep/selSomeImitate")
    Observable<ApiResult<List<SongEntity>>> songs(
            @Query("type") int type,
            @Query("wr") String singer,
            @Query("mid") int sheetId,
            @Query("page") int page,
            @Query("limit") int limit
    );
}
