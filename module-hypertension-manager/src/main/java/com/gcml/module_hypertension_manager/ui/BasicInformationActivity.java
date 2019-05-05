package com.gcml.module_hypertension_manager.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.gcml.common.data.AppManager;
import com.gcml.common.data.UserEntity;
import com.gcml.common.router.AppRouter;
import com.gcml.common.utils.DefaultObserver;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.UM;
import com.gcml.common.utils.base.ToolbarBaseActivity;
import com.gcml.module_hypertension_manager.R;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.sjtu.yifei.annotation.Route;
import com.sjtu.yifei.route.Routerfit;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@Route(path = "/app/hypertension/basic/information")
public class BasicInformationActivity extends ToolbarBaseActivity {

    TextView tvNextStep;
    TextView tvNameInfo;
    TextView tvSexInfo;
    TextView tvBirthInfo;
    TextView tvHeightInfo;
    TextView tvChangeBirth;
    TextView tvChangeHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_information);
        initView();
        initTitle();
        MLVoiceSynthetize.startSynthesize(UM.getApp(), "请确认您的基本信息");
        AppManager.getAppManager().addActivity(this);
    }

    private void initView() {
        tvNextStep = findViewById(R.id.tv_next_step);
        tvNextStep.setOnClickListener(this);
        tvNameInfo = findViewById(R.id.tv_name_info);
        tvSexInfo = findViewById(R.id.tv_sex_info);
        tvBirthInfo = findViewById(R.id.tv_birth_info);
        tvHeightInfo = findViewById(R.id.tv_height_info);
        tvChangeBirth = findViewById(R.id.tv_change_birth);
        tvChangeBirth.setOnClickListener(this);
        tvChangeHeight = findViewById(R.id.tv_change_height);
        tvChangeHeight.setOnClickListener(this);

        Routerfit.register(AppRouter.class)
                .getUserProvider()
                .getUserEntity()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.observers.DefaultObserver<UserEntity>() {
                    @Override
                    public void onNext(UserEntity userEntity) {
                        if (userEntity != null) {
                            tvNameInfo.setText(userEntity.name);
                            tvSexInfo.setText(userEntity.sex);
                            tvHeightInfo.setText(userEntity.height + "cm");
                            tvBirthInfo.setText(userEntity.birthday);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void initTitle() {
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("基 础 信 息 列 表");
        mRightText.setVisibility(View.GONE);
//        mRightView.setImageResource(R.drawable.white_wifi_3);
//        mRightView.setOnClickListener(v -> startActivity(new Intent(this, WifiConnectActivity.class)));
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int i = v.getId();
        if (i == R.id.tv_next_step) {
            String fromWhere = getIntent().getStringExtra("fromWhere");
            switch (fromWhere) {
                case "pressureNormal":
                    startActivity(new Intent(this, PressureNornalTipActivity.class));
                    break;
                case "pressureFlat":
                    startActivity(new Intent(this, PressureFlatTipActivity.class));
                    break;
                case "pressureNormalHigh":
                    startActivity(new Intent(this, NormalHighTipActivity.class));
                    break;
                case "pressureHigh":
                    startActivity(new Intent(this, HypertensionTipActivity.class));
                    break;
                case "tipHealthManage":
                    startActivity(new Intent(this, OriginHypertensionTipActivity.class));
                    break;

            }


        } else if (i == R.id.tv_change_birth) {
            updateBirth();

        } else if (i == R.id.tv_change_height) {
            updateHeight();

        }
    }

    private void updateHeight() {
        OptionsPickerView pvOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                String height = (String) getHeightItems().get(options1);

                UserEntity user = new UserEntity();
                user.height = height.replace("cm", "");
                updateUserInfo(user);

            }
        })
                .setCancelText("取消")
                .setSubmitText("确认")
                .setLineSpacingMultiplier(1.5f)
                .setSubCalSize(30)
                .setContentTextSize(40)
                .setSubmitColor(Color.parseColor("#FF108EE9"))
                .setCancelColor(Color.parseColor("#FF999999"))
                .setTextColorOut(Color.parseColor("#FF999999"))
                .setTextColorCenter(Color.parseColor("#FF333333"))
                .setBgColor(Color.WHITE)
                .setTitleBgColor(Color.parseColor("#F5F5F5"))
                .setDividerColor(Color.TRANSPARENT)
                .isCenterLabel(false)
                .setOutSideCancelable(true)
                .build();

        pvOptions.setPicker(getHeightItems());
        pvOptions.setSelectOptions(125);
        pvOptions.show();
    }

    private List getHeightItems() {
        List list = new ArrayList();
        for (int i = 0; i < 250; i++) {
            list.add(i + 50 + "cm");
        }
        return list;
    }

    private void updateBirth() {
        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        startDate.set(1900, 0, 1);
        endDate.set(2099, 11, 31);

        TimePickerView pvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                SimpleDateFormat birth = new SimpleDateFormat("yyyy-MM-dd");
                String birthString = birth.format(date);

                UserEntity user = new UserEntity();
                user.birthday = birthString;
                updateUserInfo(user);
            }
        })
                .setType(new boolean[]{true, true, true, false, false, false})// 默认全部显示
                .setCancelText("取消")
                .setSubmitText("确认")
                .setLineSpacingMultiplier(1.5f)
                .setSubCalSize(30)
                .setContentTextSize(40)
                .setTextColorOut(Color.parseColor("#FF999999"))
                .setTextColorCenter(Color.parseColor("#FF333333"))
                .setSubmitText("确认")
                .setOutSideCancelable(false)
                .setDividerColor(Color.WHITE)
                .isCyclic(true)
                .setSubmitColor(Color.parseColor("#FF108EE9"))
                .setCancelColor(Color.parseColor("#FF999999"))
                .setTitleBgColor(Color.WHITE)
                .setBgColor(Color.WHITE)
                .setDate(selectedDate)
                .setRangDate(startDate, endDate)
                .setLabel("年", "月", "日", "时", "分", "秒")//默认设置为年月日时分秒
                .isCenterLabel(false)
                .build();

        pvTime.show();
    }


    private void updateUserInfo(UserEntity user) {

        Routerfit.register(AppRouter.class)
                .getUserProvider()
                .updateUserEntity(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new DefaultObserver<UserEntity>() {
                    @Override
                    public void onNext(UserEntity o) {
                        tvHeightInfo.setText(o.height + "cm");
                        tvBirthInfo.setText(o.birthday);
                        MLVoiceSynthetize.startSynthesize(getApplicationContext(), "修改成功");
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        MLVoiceSynthetize.startSynthesize(getApplicationContext(), "修改失败");
                    }
                });
    }


}
