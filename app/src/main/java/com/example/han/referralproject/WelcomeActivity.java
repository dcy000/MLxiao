package com.example.han.referralproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.gcml.common.router.AppRouter;
import com.gcml.common.utils.network.NetUitls;
import com.sjtu.yifei.annotation.Route;
import com.sjtu.yifei.route.Routerfit;

@Route(path = "/app/welcome/activity")
public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initContentView();
    }

    private void initContentView() {
        setContentView(R.layout.activity_welcome);
        if (!NetUitls.isWifiConnected()) {
            Routerfit.register(AppRouter.class).skipWifiConnectActivity(true);
        } else {
//            Routerfit.register(AppRouter.class).skipAuthActivity();
            Routerfit.register(AppRouter.class).skipUserLogins2Activity();
            IUserService iUserService = Routerfit.register(AppRouter.class).signInProvider();
            Observable<UserEntity> userEntityObservable = iUserService.signIn(new UserPostBody());
            if (userEntityObservable == null)
                Routerfit.register(AppRouter.class).skipAuthActivity();
            userEntityObservable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .as(RxUtils.autoDisposeConverter(this))
                    .subscribe(new DefaultObserver<UserEntity>() {
                        @Override
                        public void onNext(UserEntity userEntity) {
                            super.onNext(userEntity);
                            Routerfit.register(AppRouter.class).skipAuthActivity();
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            super.onError(throwable);
                        }

                        @Override
                        public void onComplete() {
                            super.onComplete();
                        }
                    });

        }
        finish();
    }
}
