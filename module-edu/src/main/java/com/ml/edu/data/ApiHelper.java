package com.ml.edu.data;

import com.ml.edu.data.entity.SheetEntity;
import com.ml.edu.data.entity.SongEntity;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Retrofit;

/**
 * Created by afirez on 18-2-6.
 */

public class ApiHelper {

    private static volatile ApiHelper sInstance;

    public static ApiHelper getInstance() {
        if (sInstance == null) {
            synchronized (ApiHelper.class) {
                if (sInstance == null) {
                    sInstance = new ApiHelper();
                }
            }
        }
        return sInstance;
    }

    private Retrofit retrofit;

    public ApiHelper() {
        this.retrofit = RetrofitFactory.getInstance().retrofit();
        musicService = retrofit.create(MusicService.class);
    }

    private MusicService musicService;

//    Observable<ApiResult<List<SheetEntity>>>
    public Observable<List<SheetEntity>> sheetList(
            String name,
            int page,
            int limit) {
        return musicService.sheets(name, page, limit)
                .compose(RxResultUtils.<List<SheetEntity>>apiResultTransformer());
    }

    public Observable<List<SongEntity>> songList(
            int sheetId,
            int page,
            int limit) {
        return musicService.songs(3, "", sheetId, page, limit)
                .compose(RxResultUtils.<List<SongEntity>>apiResultTransformer());
    }
}
