package com.example.han.referralproject.tcm;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.homepage.MainActivity;
import com.gcml.common.data.UserEntity;
import com.gcml.common.router.AppRouter;
import com.gcml.common.utils.DefaultObserver;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.common.widget.toolbar.ToolBarClickListener;
import com.gcml.common.widget.toolbar.TranslucentToolBar;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.sjtu.yifei.annotation.Route;
import com.sjtu.yifei.route.Routerfit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@Route(path = "/app/tcm/symptom/check")
public class SymptomCheckActivity extends AppCompatActivity implements View.OnClickListener {


    private TranslucentToolBar mTbTitle;
    private ImageView mIvSymptomCheck;
    private ImageView mIvChineConstitution;
    private ImageView mIvRisk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_symptom_check);
        initView();
    }

    private void initView() {
        mTbTitle = (TranslucentToolBar) findViewById(R.id.tb_title);
        mIvSymptomCheck = (ImageView) findViewById(R.id.iv_symptom_check);
        mIvSymptomCheck.setOnClickListener(this);

        mIvChineConstitution = (ImageView) findViewById(R.id.iv_chine_constitution);
        mIvChineConstitution.setOnClickListener(this);

        mIvRisk = (ImageView) findViewById(R.id.iv_risk_assessment);
        mIvRisk.setOnClickListener(this);

        mTbTitle.setData("健 康 管 理", R.drawable.common_icon_back, "返回", R.drawable.icon_home, null, new ToolBarClickListener() {
            @Override
            public void onLeftClick() {
                finish();
            }

            @Override
            public void onRightClick() {
                startActivity(new Intent(SymptomCheckActivity.this, MainActivity.class));
            }
        });

        MLVoiceSynthetize.startSynthesize(getApplicationContext(), "欢迎来到健康管理");
    }

    @Override

    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.iv_symptom_check) {
            Routerfit.register(AppRouter.class)
                    .getUserProvider()
                    .getUserEntity().subscribeOn(Schedulers.io())
                    .as(RxUtils.autoDisposeConverter(this))
                    .subscribe(new DefaultObserver<UserEntity>() {
                        @Override
                        public void onNext(UserEntity user) {
                            if (TextUtils.isEmpty(user.sex) || TextUtils.isEmpty(user.birthday)) {
                                ToastUtils.showShort("请先去个人中心完善性别和年龄信息");
                                MLVoiceSynthetize.startSynthesize(
                                        getApplicationContext(),
                                        "请先去个人中心完善性别和年龄信息");
                            } else {
                                toRisk();
                            }
                        }
                    });

        } else if (i == R.id.iv_chine_constitution) {
            toChineseConsititution();

        } else if (i == R.id.iv_risk_assessment) {
            toSymptom();

        }
    }

    private void toRisk() {
        Routerfit.register(AppRouter.class).skipHealthInquiryActivity();
    }

    private void toChineseConsititution() {
//        startActivity(new Intent(this, OlderHealthManagementSerciveActivity.class));
        Routerfit.register(AppRouter.class)
                .getUserProvider()
                .getUserEntity()
                .subscribeOn(Schedulers.io())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new DefaultObserver<UserEntity>() {
                    @Override
                    public void onNext(UserEntity user) {
                        if (TextUtils.isEmpty(user.height) || TextUtils.isEmpty(user.weight)
                                || TextUtils.isEmpty(user.sex) || TextUtils.isEmpty(user.birthday)) {
                            ToastUtils.showShort("请先去个人中心完善体重,身高,性别,年龄信息");
                            MLVoiceSynthetize.startSynthesize(
                                    SymptomCheckActivity.this.getApplicationContext(),
                                    "请先去个人中心完善体重,身高,性别,年龄信息");
                        } else {
                            Routerfit.register(AppRouter.class).skipSlowDiseaseManagementActivity();
                        }
                    }
                });
    }

    private void toSymptom() {
//        DiseaseUser diseaseUser = new DiseaseUser(
//                LocalShared.getInstance(this).getUserName(),
//                LocalShared.getInstance(this).getSex().equals("男") ? 1 : 2,
//                Integer.parseInt(LocalShared.getInstance(this).getUserAge()) * 12,
//                LocalShared.getInstance(this).getUserPhoto()
//        );
//        String currentUser = new Gson().toJson(diseaseUser);
//        Intent intent = new Intent(this, com.witspring.unitbody.ChooseMemberActivity.class);
//        intent.putExtra("currentUser", currentUser);
//        startActivity(intent);


        Routerfit.register(AppRouter.class)
                .getUserProvider()
                .getUserEntity()
                .subscribeOn(Schedulers.io())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new DefaultObserver<UserEntity>() {
                    @Override
                    public void onNext(UserEntity user) {
                        if (TextUtils.isEmpty(user.height) || TextUtils.isEmpty(user.weight)) {
                            ToastUtils.showShort("请先去个人中心完善体重和身高信息");
                            MLVoiceSynthetize.startSynthesize(SymptomCheckActivity.this.getApplicationContext(),
                                    "请先去个人中心完善体重和身高信息");
                        } else {
                            Routerfit.register(AppRouter.class).getTaskProvider()
                                    .isTaskHealth()
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new io.reactivex.observers.DefaultObserver<Object>() {
                                        @Override
                                        public void onNext(Object o) {
                                            Routerfit.register(AppRouter.class).skipTaskActivity("MLMain");

                                        }

                                        @Override
                                        public void onError(Throwable throwable) {
                                            if (throwable instanceof NullPointerException) {
                                                Routerfit.register(AppRouter.class).skipTaskActivity("MLMain");
                                            } else {
                                                Routerfit.register(AppRouter.class).skipTaskComplyActivity();
                                            }
                                        }

                                        @Override
                                        public void onComplete() {

                                        }
                                    });
                        }
                    }
                });
    }


}
