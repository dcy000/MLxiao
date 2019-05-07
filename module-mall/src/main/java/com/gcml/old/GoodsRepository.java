package com.gcml.old;

import com.gcml.common.RetrofitHelper;
import com.gcml.common.data.UserSpHelper;
import com.gcml.common.recommend.bean.get.GoodBean;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.recommend.bean.get.ServicePackageBean;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by lenovo on 2018/9/29.
 */

public class GoodsRepository {

    GoodsService goodsService = RetrofitHelper.service(GoodsService.class);

    public Observable<List<GoodsTypeBean>> getGoodsTypes() {
        return goodsService.getGoodsTypes().compose(RxUtils.apiResultTransformer());
    }

    public Observable<List<GoodBean>> goods(String state) {
        return goodsService.goods(state).compose(RxUtils.apiResultTransformer());
    }

    public Observable<String> shopping(String userid, String eqid, String articles, String number, String price, String photo, String time) {
        return goodsService.shopping(userid, eqid, articles, number, price, photo, time).compose(RxUtils.apiResultTransformer());
    }

    public Observable<Object> payCancel(String pay_state, String delivery_state, String display_state, String orderid) {
        return goodsService.payCancel(pay_state, delivery_state, display_state, orderid).compose(RxUtils.apiResultTransformer());
    }

    public Observable<String> payStatys(String userid, String eqid, String orderid) {
        return goodsService.payStatus(userid, eqid, orderid).compose(RxUtils.apiResultTransformer());
    }

    public Observable<List<Orders>> order(String pay_state, String delivery_state, String display_state, String bname, String page, String limit) {
        return goodsService.order(pay_state, delivery_state, display_state, bname, page, limit).compose(RxUtils.apiResultTransformer());
    }

    public Observable<Object> PayInfo(String eqid, String bba, String time, String userId) {
        return goodsService.PayInfo(eqid, bba, time, userId).compose(RxUtils.apiResultTransformer());
    }

    /**
     * 购买套餐预支付
     *
     * @param price
     * @param des
     * @return
     */
    public Observable<Object> bugServicePackage(String price, String des) {
        return goodsService.bugServicePackage(UserSpHelper.getUserId(), price, des).compose(RxUtils.apiResultTransformer());
    }

    /**
     * 使购买的套餐生效
     *
     * @param type
     * @param orderId
     * @return
     */
    public Observable<String> servicePackageEffective(String type, String orderId) {
        return goodsService.servicePackageEffective(type, orderId, UserSpHelper.getUserId()).compose(RxUtils.apiResultTransformer());
    }


    public Observable<Object> getOrderStarte(String orderId) {
        return goodsService.getOrderStarte(orderId).compose(RxUtils.apiResultTransformer());
    }

    /**
     * 查询套餐是否生效
     *
     * @return
     */
    public Observable<ServicePackageBean> queryServicePackage() {
        return goodsService.queryServicePackage(UserSpHelper.getUserId()).compose(RxUtils.apiResultTransformer());
    }
}
