package com.gcml.module_auth_hospital.ui.profile;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.gcml.common.data.UserEntity;
import com.gcml.common.router.AppRouter;
import com.gcml.common.utils.base.ToolbarBaseActivity;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.lib_widget.EclipseImageView;
import com.gcml.module_auth_hospital.R;
import com.sjtu.yifei.annotation.Route;
import com.sjtu.yifei.route.Routerfit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DefaultObserver;
import io.reactivex.schedulers.Schedulers;

@Route(path = "/auth/hospital/user/center/activity")
public class UserCenterActivity extends ToolbarBaseActivity implements View.OnClickListener {
    private EclipseImageView mIvPersonCenter;
    private EclipseImageView mIvHealthRecord;
    private EclipseImageView mIvHealthBracelet;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_center);
        initView();
        mTitleText.setText("个 人 中 心");

    }

    private void initView() {
        mIvPersonCenter = (EclipseImageView) findViewById(R.id.iv_person_center);
        mIvPersonCenter.setOnClickListener(this);
        mIvHealthRecord = (EclipseImageView) findViewById(R.id.iv_health_record);
        mIvHealthRecord.setOnClickListener(this);
        mIvHealthBracelet = (EclipseImageView) findViewById(R.id.iv_health_bracelet);
        mIvHealthBracelet.setOnClickListener(v -> {
            toShouHuan();
        });

        mRightText.setVisibility(View.GONE);
        mRightView.setVisibility(View.VISIBLE);
        mRightView.setImageResource(R.drawable.common_ic_settings);
//        setWifiLevel(mRightView);
    }

    private void toShouHuan() {
        Routerfit.register(AppRouter.class).getUserProvider().getUserEntity()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<UserEntity>() {
                    @Override
                    public void onNext(UserEntity user) {
                        if (!TextUtils.isEmpty(user.watchCode)) {
                            Routerfit.register(AppRouter.class).skipBraceletActivtity();
                        } else {
                            Routerfit.register(AppRouter.class).skipHealthManageTipActivity();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.showShort(e.getMessage());

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int i = v.getId();
        if (i == R.id.iv_person_center) {
            Routerfit.register(AppRouter.class).skipUserInfoActivity();
        } else if (i == R.id.iv_health_record) {
            Routerfit.register(AppRouter.class).skipHealthRecordActivity(0);
        }
    }

    @Override
    protected void backMainActivity() {
        Routerfit.register(AppRouter.class).skipSettingActivity();
//        onRightClickWithPermission(new IAction() {
//            @Override
//            public void action() {
//            }
//        });
    }
}
