package com.gcml.mall.network;

import com.gcml.common.RetrofitHelper;
import com.gcml.common.utils.RxUtils;
import com.gcml.mall.bean.CategoryBean;
import com.gcml.mall.bean.GoodsBean;
import com.gcml.mall.bean.OrderBean;
import com.gcml.mall.bean.ResultBean;

import java.util.List;

import io.reactivex.Observable;

public class MallRepository {

    private MallService mTaskService = RetrofitHelper.service(MallService.class);

    public Observable<List<CategoryBean>> categoryFromApi() {
        return mTaskService.category().compose(RxUtils.apiResultTransformer());
    }

    public Observable<List<GoodsBean>> goodsFromApi(String state) {
        return mTaskService.goods(state).compose(RxUtils.apiResultTransformer());
    }

    public Observable<List<OrderBean>> orderFromApi(String pay_state, String delivery_state, String display_state, String bname, String page, String limit) {
        return mTaskService.order(pay_state, delivery_state, display_state, bname, page, limit).compose(RxUtils.apiResultTransformer());
    }

    public Observable<List<GoodsBean>> recommendFromApi(String userId) {
        return mTaskService.recommend(userId).compose(RxUtils.apiResultTransformer());
    }

    public Observable<String> shoppingForApi(String userid, String eqid, String articles, String number, String price, String photo, String time) {
        return mTaskService.shopping(userid, eqid, articles, number, price, photo, time).compose(RxUtils.apiResultTransformer());
    }

    public Observable<String> payStatysForApi(String userid, String eqid, String orderid) {
        return mTaskService.payStatus(userid, eqid, orderid).compose(RxUtils.apiResultTransformer());
    }

    public Observable<String> payCancelForApi(String pay_state, String delivery_state, String display_state, String orderid) {
        return mTaskService.payCancel(pay_state, delivery_state, display_state, orderid).compose(RxUtils.apiResultTransformer());
    }

    public Observable<Object> rechargeForApi(String eqid, String bba, String time, String bid) {
        return mTaskService.recharge(eqid, bba, time, bid).compose(RxUtils.apiResultTransformer());
    }

    public Observable<ResultBean> queryResultForApi(String transaction_id) {
        return mTaskService.queryResult(transaction_id).compose(RxUtils.apiResultTransformer());
    }
}
