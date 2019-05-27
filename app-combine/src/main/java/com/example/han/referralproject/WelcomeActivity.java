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
//        RetrofitUrlManager.getInstance().setGlobalDomain("http://192.168.200.210:5555/");//娄
        RetrofitUrlManager.getInstance().setGlobalDomain("http://192.168.200.222:5555/");//左
        initContentView();
    }

    private void initContentView() {
        setContentView(R.layout.activity_welcome);
        if (!NetUitls.isWifiConnected()) {
            Routerfit.register(AppRouter.class).skipWifiConnectActivity(true);
            finish();
        } else {
//          Routerfit.register(AppRouter.class).skipAuthActivity();//登录
//            Routerfit.register(AppRouter.class).skipUserRegistersActivity();//身份证注册
            touristLogin();
        }

    }

    private void touristLogin() {
        IUserService iUserService = Routerfit.register(AppRouter.class).touristSignInProvider();
        UserPostBody body = new UserPostBody();
        body.password = "123";
        body.username = "superman";
        iUserService.signIn(body)
                .compose(RxUtils.io2Main())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new DefaultObserver<UserToken>() {
                    @Override
                    public void onNext(UserToken userToken) {
                        super.onNext(userToken);
                        Routerfit.register(AppRouter.class).skipUserLogins2Activity();
//                        Routerfit.register(AppRouter.class).skipMainActivity();
                        finish();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        Routerfit.register(AppRouter.class).skipMainActivity();
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
