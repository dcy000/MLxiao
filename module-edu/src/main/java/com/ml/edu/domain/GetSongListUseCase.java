package com.ml.edu.domain;

import com.ml.edu.common.base.UseCase;
import com.ml.edu.data.ApiHelper;
import com.ml.edu.data.entity.SongEntity;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by afirez on 18-2-6.
 */

public class GetSongListUseCase extends UseCase<GetSongListUseCase.Params, List<SongEntity>> {

    private final ApiHelper apiHelper;

    public GetSongListUseCase() {
        super();
        apiHelper = ApiHelper.getInstance();
    }

    @Override
    protected Observable<List<SongEntity>> rxResult(Params params) {
        return apiHelper.songList(params.sheetId, params.page, params.limit);
    }

    public static class Params {
        public int sheetId;
        public int page;
        public int limit;

        public Params(int sheetId, int page, int limit) {
            this.sheetId = sheetId;
            this.page = page;
            this.limit = limit;
        }

        @Override
        public String toString() {
            return "Params{" +
                    "sheetId=" + sheetId +
                    ", page=" + page +
                    ", limit=" + limit +
                    '}';
        }
    }
}
