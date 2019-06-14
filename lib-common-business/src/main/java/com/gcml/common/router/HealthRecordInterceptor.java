package com.gcml.common.router;

import android.text.TextUtils;

import com.gcml.common.constant.Global;
import com.gcml.common.data.UserSpHelper;
import com.gcml.common.face2.VertifyFace2ProviderImp;
import com.gcml.common.utils.ChannelUtils;
import com.gcml.common.utils.display.ToastUtils;
import com.sjtu.yifei.annotation.Interceptor;
import com.sjtu.yifei.route.AInterceptor;
import com.sjtu.yifei.route.Routerfit;

@Interceptor(priority = 99)
public class HealthRecordInterceptor implements AInterceptor {
    @Override
    public void intercept(Chain chain) throws Exception {
        //如果不是合版直接跳过检查
        if (!ChannelUtils.isXiongAn()) {
            chain.proceed();
            return;
        }
        String path = chain.path();
        if (!UserSpHelper.isTourist()) {
            //非游客
            if (TextUtils.equals(path, Global.HEALTH_RECORD)) {
                //先跳转到登录页面，登录成功之后，再跳转到目标页
                Routerfit.register(AppRouter.class)
                        .getVertifyFaceProvider3()
                        .onlyVertifyFace(true, true, true, new VertifyFace2ProviderImp.VertifyFaceResult() {
                            @Override
                            public void success() {
                                chain.proceed();
                            }

                            @Override
                            public void failed(String msg) {
                                ToastUtils.showShort(msg);
                            }
                        });
            } else {
                chain.proceed();
            }
        } else {
            //游客
            chain.proceed();
        }
    }
}
