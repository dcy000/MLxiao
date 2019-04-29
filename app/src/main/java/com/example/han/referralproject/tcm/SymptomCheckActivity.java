package com.example.han.referralproject.tcm;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.billy.cc.core.component.IComponentCallback;
import com.example.han.referralproject.R;
import com.example.han.referralproject.bean.DiseaseUser;
import com.example.han.referralproject.homepage.MainActivity;
import com.example.han.referralproject.hypertensionmanagement.activity.SlowDiseaseManagementActivity;
import com.example.han.referralproject.physicalexamination.activity.ChineseMedicineMonitorActivity;
import com.example.han.referralproject.tcm.activity.OlderHealthManagementSerciveActivity;
import com.example.han.referralproject.util.LocalShared;
import com.gcml.common.data.UserEntity;
import com.gcml.common.router.AppRouter;
import com.gcml.common.utils.DefaultObserver;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.widget.toolbar.ToolBarClickListener;
import com.gcml.common.widget.toolbar.TranslucentToolBar;
import com.gcml.common.utils.display.ToastUtils;
import com.google.gson.Gson;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.sjtu.yifei.annotation.Go;
import com.sjtu.yifei.annotation.Route;
import com.sjtu.yifei.route.Routerfit;

import io.reactivex.Observable;
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
        switch (v.getId()) {
            case R.id.iv_symptom_check:
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
                break;
            case R.id.iv_chine_constitution:
                toChineseConsititution();
                break;
            case R.id.iv_risk_assessment:
                toSymptom();
                break;
        }
    }

    private void toRisk() {
        CC.obtainBuilder("health_measure")
                .setActionName("To_HealthInquiryActivity")
                .build()
                .call();
    }

    private void toChineseConsititution() {
//        startActivity(new Intent(this, OlderHealthManagementSerciveActivity.class));

        CCResult result = CC.obtainBuilder("com.gcml.auth.getUser").build().call();
        Observable<UserEntity> rxUser = result.getDataItem("data");
        rxUser.subscribeOn(Schedulers.io())
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
                            Intent intent = new Intent(SymptomCheckActivity.this, SlowDiseaseManagementActivity.class);
                            startActivity(intent);
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
                            CC.obtainBuilder("com.gcml.task.isTask")
                                    .build()
                                    .callAsync(new IComponentCallback() {
                                        @Override
                                        public void onResult(CC cc, CCResult result) {
                                            if (result.isSuccess()) {
                                                CC.obtainBuilder("app.component.task").addParam("startType", "MLMain").build().callAsync();
                                            } else {
                                                CC.obtainBuilder("app.component.task.comply").build().callAsync();
                                            }
                                        }
                                    });
                        }
                    }
                });
    }


}
