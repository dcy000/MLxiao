package com.example.han.referralproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.gcml.common.http.ApiResult;
import com.gcml.common.router.AppRouter;
import com.gcml.common.user.IUserService;
import com.gcml.common.user.UserPostBody;
import com.gcml.common.user.UserToken;
import com.gcml.common.utils.DefaultObserver;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.common.utils.network.NetUitls;
import com.sjtu.yifei.annotation.Route;
import com.sjtu.yifei.route.Routerfit;

import io.reactivex.functions.Consumer;
import me.jessyan.retrofiturlmanager.RetrofitUrlManager;

@Route(path = "/app/welcome/activity")
public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RetrofitUrlManager.getInstance().setGlobalDomain("http://192.168.200.210:5555/");
        initContentView();
    }

    private void initContentView() {
        setContentView(R.layout.activity_welcome);
        if (!NetUitls.isWifiConnected()) {
            Routerfit.register(AppRouter.class).skipWifiConnectActivity(true);
        } else {
//          Routerfit.register(AppRouter.class).skipAuthActivity();//登录
//            Routerfit.register(AppRouter.class).skipUserRegistersActivity();//身份证注册
            touristLogin();
        }
        finish();
    }

    private void touristLogin() {
        IUserService iUserService = Routerfit.register(AppRouter.class).touristSignInProvider();
        iUserService.signIn(new UserPostBody())
                .compose(RxUtils.io2Main())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new DefaultObserver<Object>() {
                    @Override
                    public void onNext(Object userToken) {
                        super.onNext(userToken);
                        Routerfit.register(AppRouter.class).skipUserRegistersActivity();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        ToastUtils.showShort(throwable.getMessage());
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
      /*  if (NetUitls.isWifiConnected()) {
            touristLogin();
        }*/
    }
}
