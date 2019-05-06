package com.gcml.old;

import com.gcml.common.RetrofitHelper;
import com.gcml.common.recommend.bean.get.GoodBean;
import com.gcml.common.utils.RxUtils;
import com.gcml.mall.bean.OrderBean;

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
}