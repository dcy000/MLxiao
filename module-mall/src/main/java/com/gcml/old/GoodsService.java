package com.gcml.old;

import com.gcml.common.http.ApiResult;
import com.gcml.common.recommend.bean.get.GoodBean;
import com.gcml.common.recommend.bean.get.GoodsBean;
import com.gcml.mall.bean.OrderBean;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;


/**
 * Created by lenovo on 2018/9/29.
 */

public interface GoodsService {
    @GET("ZZB/api/mall/productType/selAllMallProductType/")
    Observable<ApiResult<List<GoodsTypeBean>>> getGoodsTypes();

    /**
     * 获取商品列表.
     */
    @GET("/ZZB/order/OneType_state/")
    Observable<ApiResult<List<GoodBean>>> goods(
            @Query("state") String state
    );

    /**
     * 生产预支付订单.
     */
    @POST("/ZZB/order/panding_pay/")
    Observable<ApiResult<String>> shopping(
            @Query("userid") String userid,
            @Query("eqid") String eqid,
            @Query("articles") String articles,
            @Query("number") String number,
            @Query("price") String price,
            @Query("photo") String photo,
            @Query("time") String time
    );

    /**
     * 预支付订单取消.
     */
    @POST("/ZZB/order/delivery_del/")
    Observable<ApiResult<Object>> payCancel(
            @Query("pay_state") String pay_state,
            @Query("delivery_state") String delivery_state,
            @Query("display_state") String display_state,
            @Query("orderid") String orderid
    );

    /**
     * 预支付订单取确认.
     */
    @POST("/ZZB/order/pay_pro/")
    Observable<ApiResult<String>> payStatus(
            @Query("userid") String userid,
            @Query("eqid") String eqid,
            @Query("orderid") String orderid
    );

    /**
     * 获取订单列表.
     */
    @GET("/ZZB/order/one_more_orders/")
    Observable<ApiResult<List<Orders>>> order(
            @Query("pay_state") String pay_state,
            @Query("delivery_state") String delivery_state,
            @Query("display_state") String display_state,
            @Query("bname") String bname,
            @Query("page") String page,
            @Query("limit") String limit
    );
}