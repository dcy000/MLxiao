package com.example.han.referralproject.hypertensionmanagement.activity;

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
import com.billy.cc.core.component.CC;
import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.network.NetworkApi;
import com.gcml.common.data.AppManager;
import com.gcml.common.data.UserEntity;
import com.gcml.common.repository.utils.DefaultObserver;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.display.ToastUtils;
import com.iflytek.synthetize.MLVoiceSynthetize;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class BasicInformationActivity extends BaseActivity {

    @BindView(R.id.tv_next_step)
    TextView tvNextStep;
    @BindView(R.id.tv_name_info)
    TextView tvNameInfo;
    @BindView(R.id.tv_sex_info)
    TextView tvSexInfo;
    @BindView(R.id.tv_birth_info)
    TextView tvBirthInfo;
    @BindView(R.id.tv_height_info)
    TextView tvHeightInfo;
    @BindView(R.id.tv_change_birth)
    TextView tvChangeBirth;
    @BindView(R.id.tv_change_height)
    TextView tvChangeHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_information);
        ButterKnife.bind(this);
        initTitle();
        initView();
        mlSpeak("您好，请确认您的基本信息");
        AppManager.getAppManager().addActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void initView() {
        NetworkApi.getMyBaseData(response -> {
            if (response != null) {
                tvNameInfo.setText(response.bname);
                tvSexInfo.setText(response.sex);
                tvHeightInfo.setText(response.height + "cm");
                tvBirthInfo.setText(response.birthday);
            }
        }, message -> ToastUtils.showShort(message));
    }

    private void initTitle() {
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("基 础 信 息 列 表");
        mRightText.setVisibility(View.GONE);
//        mRightView.setImageResource(R.drawable.white_wifi_3);
//        mRightView.setOnClickListener(v -> startActivity(new Intent(this, WifiConnectActivity.class)));
    }

    @OnClick({R.id.tv_next_step, R.id.tv_birth_info, R.id.tv_height_info, R.id.tv_change_birth, R.id.tv_change_height})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_next_step:
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

                break;
            case R.id.tv_change_birth:
                updateBirth();
                break;
            case R.id.tv_change_height:
                updateHeight();
                break;

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
        Observable<UserEntity> data = CC.obtainBuilder("com.gcml.auth.putUser")
                .addParam("user", user)
                .build()
                .call()
                .getDataItem("data");
        data.subscribeOn(Schedulers.io())
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
