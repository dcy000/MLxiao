package com.example.module_mall.service;

import com.example.module_mall.bean.Goods;
import com.example.module_mall.bean.Orders;
import com.gzq.lib_core.http.model.HttpResult;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface MallAPI {
    /**
     * 生成订单的
     *
     * @param userId
     * @param eqid
     * @param articles
     * @param number
     * @param price
     * @param photo
     * @param time
     * @return
     */
    @POST("ZZB/order/panding_pay")
    Observable<HttpResult<Object>> orderInfo(
            @Query("userid") String userId,
            @Query("eqid") String eqid,
            @Query("articles") String articles,
            @Query("number") String number,
            @Query("price") float price,
            @Query("photo") String photo,
            @Query("time") String time
    );

    /**
     * 取消发起的预付订单
     *
     * @param pay_state
     * @param delivery_state
     * @param display_state
     * @param orderid
     * @return
     */
    @POST("ZZB/order/delivery_del")
    Observable<Object> cancelPayOrder(
            @Query("pay_state") String pay_state,
            @Query("delivery_state") String delivery_state,
            @Query("display_state") String display_state,
            @Query("orderid") String orderid);
    /**
     * 获取订单列表
     *
     * @param pay_state
     * @param delivery_state
     * @param display_state
     * @param bname
     * @param page
     * @param limit
     * @return
     */
    @GET("ZZB/order/one_more_orders")
    Observable<HttpResult<List<Orders>>> getOrderList(
            @Query("pay_state") String pay_state,
            @Query("delivery_state") String delivery_state,
            @Query("display_state") String display_state,
            @Query("bname") String bname,
            @Query("page") String page,
            @Query("limit") String limit
    );
    /**
     * 商品列表
     *
     * @param state
     * @return
     */
    @GET("ZZB/order/OneType_state")
    Observable<HttpResult<List<Goods>>> getGoods(@Query("state") int state);
}
