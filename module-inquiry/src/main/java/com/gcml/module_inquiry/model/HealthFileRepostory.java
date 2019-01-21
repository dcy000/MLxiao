package com.gcml.module_inquiry.model;

import com.gcml.common.RetrofitHelper;
import com.gcml.common.utils.RxUtils;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Retrofit;

/**
 * Created by lenovo on 2019/1/21.
 */

public class HealthFileRepostory {
    HealthFileService service = RetrofitHelper.service(HealthFileService.class);

    public Observable<List<Docter>> getDoctors(Integer index, Integer limit) {
        return service.getDoctors(index, limit)
                .compose(RxUtils.apiResultTransformer());
    }
}
