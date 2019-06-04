package com.gcml.common.router;

import android.text.TextUtils;

import com.gcml.common.constant.Global;
import com.gcml.common.data.AppManager;
import com.gcml.common.data.UserSpHelper;
import com.gcml.common.utils.ChannelUtils;
import com.gcml.common.utils.display.ToastUtils;
import com.sjtu.yifei.annotation.Interceptor;
import com.sjtu.yifei.route.AInterceptor;
import com.sjtu.yifei.route.ActivityCallback;
import com.sjtu.yifei.route.Routerfit;

@Interceptor(priority = 100)
public class SignInInterceptor implements AInterceptor {
    @Override
    public void intercept(Chain chain) throws Exception {
        //如果不是合版直接跳过检查
        if (!ChannelUtils.isAppCombine()) {
            chain.proceed();
            return;
        }
        String path = chain.path();
        if (UserSpHelper.isTourist()) {
            //游客
            if (TextUtils.equals(path, Global.PERSON_DETAIL)) {
                //先跳转到登录页面，登录成功之后，再跳转到目标页
                Routerfit.register(AppRouter.class).skipUserLogins2Activity(true, new ActivityCallback() {
                    @Override
                    public void onActivityResult(int result, Object data) {
                        if (data instanceof Boolean && ((Boolean) data)) {
                            //登录成功
                            chain.proceed();
                            AppManager.getAppManager().finishAllActivity();
                        } else {
                            //登录失败
                            ToastUtils.showShort("请先登录");
                        }
                    }
                });
            } else {
                chain.proceed();
            }
        } else {
            //非游客
            chain.proceed();
        }
    }
}
