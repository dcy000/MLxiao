package com.gcml.common.router;

import android.text.TextUtils;

import com.gcml.common.constant.Global;
import com.gcml.common.data.UserSpHelper;
import com.gcml.common.utils.display.ToastUtils;
import com.sjtu.yifei.annotation.Interceptor;
import com.sjtu.yifei.route.AInterceptor;
import com.sjtu.yifei.route.ActivityCallback;
import com.sjtu.yifei.route.Routerfit;

@Interceptor(priority = 100)
public class SignInInterceptor implements AInterceptor {
    @Override
    public void intercept(Chain chain) throws Exception {
        String path = chain.path();
        if (TextUtils.equals(UserSpHelper.getToken(), Global.TOURIST_TOKEN)) {
            //游客
            if (TextUtils.equals(path, "/auth/hospital/user/info/activity")) {
                Routerfit.register(AppRouter.class).skipUserLogins2Activity(true, new ActivityCallback() {
                    @Override
                    public void onActivityResult(int result, Object data) {
                        if (data) {
                            chain.proceed();
                        } else {
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
