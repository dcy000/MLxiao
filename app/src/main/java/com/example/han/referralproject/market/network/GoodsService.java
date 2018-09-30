package com.example.han.referralproject.market.network;

import com.example.han.referralproject.market.network.bean.GoodsTypeBean;
import com.gcml.common.repository.http.ApiResult;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;


/**
 * Created by lenovo on 2018/9/29.
 */

public interface GoodsService {
    @GET("ZZB/api/mall/productType/selAllMallProductType/")
    Observable<ApiResult<List<GoodsTypeBean>>> getGoodsTypes();
}
