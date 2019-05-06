package com.gcml.module_edu_child.net;

import com.gcml.common.RetrofitHelper;
import com.gcml.common.utils.RxUtils;
import com.gcml.module_edu_child.bean.SheetModel;
import com.gcml.module_edu_child.bean.SongModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

public class EduChildRepository {
    private static EduChildService eduChildService = RetrofitHelper.service(EduChildService.class);

    public Observable<ArrayList<SheetModel>> getChildEduSheetList(int page, int limit) {
        return eduChildService.getChildEduSheetList(page, limit).compose(RxUtils.apiResultTransformer());
    }

    public Observable<List<SongModel>> getChildEduSongListBySheetId(int page, int limit, int mid, int type, String wr) {
        return eduChildService.getChildEduSongListBySheetId(page, limit, mid, type, wr).compose(RxUtils.apiResultTransformer());
    }
}
