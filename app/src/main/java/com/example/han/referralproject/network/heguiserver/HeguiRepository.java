package com.example.han.referralproject.network.heguiserver;

import com.gcml.common.RetrofitHelper;
import com.gcml.common.utils.RxUtils;

import io.reactivex.Observable;

public class HeguiRepository {
    private HeGuiServer service = RetrofitHelper.service(HeGuiServer.class);

    public Observable<Object> getProduct(String eqid, String time, String goodType, String sign) {
        return service
                .getProduct(eqid, time, goodType, sign)
                .compose(RxUtils.apiResultTransformer());

    }

    public Observable<Object> getProduct2(PostBean json) {
        return service
                .getProduct2(json)
                .compose(RxUtils.apiResultTransformer());

    }
}
