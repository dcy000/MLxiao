package com.gcml.module_inquiry.model;

import com.gcml.common.RetrofitHelper;
import com.gcml.common.data.DetectionResult;
import com.gcml.common.data.UserSpHelper;
import com.gcml.common.http.ApiResult;
import com.gcml.common.recommend.bean.post.DetectionData;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.UploadHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import afu.org.checkerframework.checker.oigj.qual.O;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import timber.log.Timber;

/**
 * Created by lenovo on 2019/1/21.
 */

public class HealthFileRepostory {
    HealthFileService service = RetrofitHelper.service(HealthFileService.class);
    UploadHelper uploadHelper = new UploadHelper();

    public Observable<List<Docter>> getDoctors(Integer index, Integer limit) {
        return service.getDoctors(index, limit)
                .compose(RxUtils.apiResultTransformer());
    }


    public Observable<String> uploadHeadData(byte[] head, String userId) {
        return service.getQiniuToken()
                .compose(RxUtils.apiResultTransformer())
                .flatMap(new Function<String, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(String token) throws Exception {
                        String time = new SimpleDateFormat("yyyyMMddHHmmss",
                                Locale.getDefault()).format(new Date());
                        String key = String.format("%s_%s.jpg", time, userId);
                        return uploadHelper.upload(head, key, token)
                                .subscribeOn(Schedulers.io());
                    }
                })
                .subscribeOn(Schedulers.io());
    }

    public Observable<Object> bindDoctor(
            String doid,
            String doctorId,
            String headUrl) {
        return service.bindDoctor(doid, doctorId, headUrl)
                .compose(RxUtils.apiResultTransformer());
    }


    /**
     * 上传血压数据的接口
     */
    public Observable<List<DetectionResult>> postMeasureData(ArrayList<DetectionData> datas) {
        String userId = UserSpHelper.getUserId();
        Timber.i("上传测量数据：userID=" + userId);
        return service.postMeasureData(userId, datas).compose(RxUtils.apiResultTransformer());
    }

    /**
     * 问诊
     */
    public Observable<ApiResult<Object>> postWenZen(WenZhenBean data) {
        String userId = UserSpHelper.getUserId();
        data.userId = userId;
        return service.postWenZen(data);
    }


}
