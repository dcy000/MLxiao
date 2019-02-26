package com.gcml.mall.network;

import com.gcml.common.recommend.bean.get.GoodBean;
import com.gcml.common.http.ApiResult;
import com.gcml.mall.bean.CategoryBean;
import com.gcml.mall.bean.GoodsBean;
import com.gcml.mall.bean.OrderBean;
import com.gcml.mall.bean.ResultBean;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MallService {

    /**
     * 获取商品分类列表.
     */
    @GET("/ZZB/api/mall/productType/selAllMallProductType/")
    Observable<ApiResult<List<CategoryBean>>> category();

    /**
     * 获取商品列表.
     */
    @GET("/ZZB/order/OneType_state/")
    Observable<ApiResult<List<GoodsBean>>> goods(
            @Query("state") String state
    );

    /**
     * 获取订单列表.
     */
    @GET("/ZZB/order/one_more_orders/")
    Observable<ApiResult<List<OrderBean>>> order(
            @Query("pay_state") String pay_state,
            @Query("delivery_state") String delivery_state,
            @Query("display_state") String display_state,
            @Query("bname") String bname,
            @Query("page") String page,
            @Query("limit") String limit
    );

    @GET("/ZZB/api/mall/recommend/{userId}/")
    Observable<ApiResult<List<GoodsBean>>> recommend(
            @Path("userId") String userId
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
    Observable<ApiResult<String>> payCancel(
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
     * 用户充值.
     */
    @POST("/ZZB/br/chongzhi/")
    Observable<ApiResult<Object>> recharge(
            @Query("eqid") String eqid,
            @Query("bba") String bba,
            @Query("time") String time,
            @Query("bid") String bid
    );

    /**
     * 用户充值.
     */
    @POST("/ai/getResultByOrderID/")
    Observable<ApiResult<ResultBean>> queryResult(
            @Query("transaction_id") String transaction_id
    );
}
