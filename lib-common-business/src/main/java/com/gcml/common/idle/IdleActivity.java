package com.gcml.common.idle;

import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.gcml.common.business.R;
import com.gcml.common.constant.Global;
import com.gcml.common.data.UserSpHelper;
import com.gcml.common.router.AppRouter;
import com.gcml.common.utils.DefaultObserver;
import com.gcml.common.utils.RxUtils;
import com.sjtu.yifei.annotation.Route;
import com.sjtu.yifei.route.Routerfit;

import io.reactivex.android.schedulers.AndroidSchedulers;

@Route(path = "/idle/activity/idle")
public class IdleActivity extends AppCompatActivity {

    private TextView tvCountDown;
    private TextView tvTips;
    private ConstraintLayout clRoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idle);

        clRoot = (ConstraintLayout) findViewById(R.id.clRoot);
        tvCountDown = (TextView) findViewById(R.id.tvCountDown);
        tvTips = (TextView) findViewById(R.id.tvTips);

        clRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        RxUtils.rxCountDown(1, 15)
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new DefaultObserver<Integer>() {
                    @Override
                    public void onNext(Integer integer) {
                        super.onNext(integer);
                        tvCountDown.setText(String.valueOf(integer));
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        UserSpHelper.setToken(Global.TOURIST_TOKEN);
                        UserSpHelper.setUserId("");
                        Routerfit.register(AppRouter.class).skipUserLogins2Activity();
                        finish();
                    }
                });
        IdleHelper.getInstance().setCallback(new IdleHelper.Callback() {
            @Override
            public void onIdle(boolean idle) {
                if (!idle) {
                    finish();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        IdleHelper.getInstance().setCallback(null);
    }
}
