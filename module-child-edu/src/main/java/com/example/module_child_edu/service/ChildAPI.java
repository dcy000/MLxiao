package com.example.module_child_edu.service;

import com.example.module_child_edu.bean.RadioEntity;
import com.example.module_child_edu.bean.SheetModel;
import com.example.module_child_edu.bean.SongModel;
import com.gzq.lib_core.http.model.HttpResult;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ChildAPI {
    /**
     * 获取儿童娱乐资源
     *
     * @param page
     * @param limit
     * @return
     */
    @GET("ZZB/rep/sel_music_danforapp")
    Observable<HttpResult<List<SheetModel>>> getChildEduSheetList(
            @Query("page") int page,
            @Query("limit") int limit
    );

    /**
     * 获取儿歌
     *
     * @param page
     * @param limit
     * @param mid
     * @param type
     * @param wr
     * @return
     */
    @GET("ZZB/rep/selSomeImitate")
    Observable<HttpResult<List<SongModel>>> getChildEduSongListBySheetId(
            @Query("page") int page,
            @Query("limit") int limit,
            @Query("mid") int mid,
            @Query("type") int type,
            @Query("wr") String wr
    );

    /**
     * 获取广播列表
     *
     * @param type
     * @param page
     * @param limit
     * @param mid
     * @return
     */
    @GET("ZZB/rep/selSomeImitate")
    Observable<HttpResult<List<RadioEntity>>> getFM(
            @Query("type") int type,
            @Query("page") int page,
            @Query("limit") int limit,
            @Query("mid") int mid
    );

}
