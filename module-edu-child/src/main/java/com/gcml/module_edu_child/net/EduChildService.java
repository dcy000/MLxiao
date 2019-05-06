package com.gcml.module_edu_child.net;

import com.gcml.common.http.ApiResult;
import com.gcml.module_edu_child.bean.SheetModel;
import com.gcml.module_edu_child.bean.SongModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface EduChildService {
    @GET("ZZB/rep/sel_music_danforapp")
    Observable<ApiResult<ArrayList<SheetModel>>> getChildEduSheetList(
            @Query("page") int page, @Query("limit") int limit);

    @GET("ZZB/rep/selSomeImitate")
    Observable<ApiResult<List<SongModel>>> getChildEduSongListBySheetId(
            @Query("page") int page,
            @Query("limit") int limit,
            @Query("mid") int mid,
            @Query("type") int type,
            @Query("wr") String wr
    );
}
