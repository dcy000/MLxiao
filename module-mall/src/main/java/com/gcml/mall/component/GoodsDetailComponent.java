package com.gcml.mall.component;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.billy.cc.core.component.IComponent;
import com.gcml.mall.bean.GoodsBean;
import com.gcml.mall.ui.GoodsDetailActivity;
import com.gcml.mall.ui.MallActivity;

/**
 * desc: GoodsDetailComponent .
 * author: wecent .
 * date: 2018/8/21 .
 */

public class GoodsDetailComponent implements IComponent {

    @Override
    public String getName() {
        return "com.gcml.mall.mall.goods";
    }

    @Override
    public boolean onCall(CC cc) {
        Context context = cc.getContext();
        Intent intent = new Intent(context, GoodsDetailActivity.class);
        GoodsBean goods = cc.getParamItem("goods");
        intent.putExtra("goods", goods);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
        //发送组件调用的结果（返回信息）
        CC.sendCCResult(cc.getCallId(), CCResult.success());
        return false;
    }
}
