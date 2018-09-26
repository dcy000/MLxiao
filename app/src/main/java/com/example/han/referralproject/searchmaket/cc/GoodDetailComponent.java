package com.example.han.referralproject.searchmaket.cc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.billy.cc.core.component.IComponent;
import com.example.han.referralproject.shopping.GoodDetailActivity;
import com.example.han.referralproject.shopping.Goods;
import com.gcml.common.recommend.bean.get.GoodBean;

/**
 * Created by lenovo on 2018/9/21.
 */

public class GoodDetailComponent implements IComponent {
    @Override
    public String getName() {
        return "gcml.market.good.detail";
    }

    @Override
    public boolean onCall(CC cc) {
        Context context = cc.getContext();
        Intent intent = new Intent(context, GoodDetailActivity.class);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        GoodBean goods = cc.getParamItem("goods");

        Goods good = new Goods();
        good.goodsid = goods.goodsid.intValue() + "";
        good.goodsname = goods.goodsname + "";
        good.goodsimage = goods.goodsimage;
        good.goodsprice = goods.goodsprice.doubleValue() + "";
        good.goodstate = goods.goodstate.intValue() + "";

        intent.putExtra("goods", good);
        context.startActivity(intent);
        cc.sendCCResult(cc.getCallId(), CCResult.success());
        return false;
    }
}
