package com.gcml.module_auth_hospital.ui.register;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnDismissListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectChangeListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.gcml.common.data.UserEntity;
import com.gcml.common.data.UserSpHelper;
import com.gcml.common.router.AppRouter;
import com.gcml.common.utils.DefaultObserver;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.base.ToolbarBaseActivity;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.common.widget.toolbar.TranslucentToolBar;
import com.gcml.module_auth_hospital.R;
import com.gcml.module_auth_hospital.model.UserRepository;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.sjtu.yifei.route.ActivityCallback;
import com.sjtu.yifei.route.Routerfit;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class ImproveInformationActivity extends ToolbarBaseActivity {

    private TranslucentToolBar tbProfile;
    private TextView height;
    private TextView weight;
    private TextView wc;
    private TextView next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isShowToolbar = false;
        setContentView(R.layout.activity_improve_information);
        initView();
        initEvent();
    }

    private void initEvent() {
        height.setOnClickListener(v -> {
            selectHeight();
        });

        weight.setOnClickListener(v -> {
            selectWeight();
        });

        wc.setOnClickListener(v -> {
            selectWaist();
        });

        next.setOnClickListener(v -> {
            goNext();
        });
    }

    UserRepository repository = new UserRepository();

    public void goNext() {
        String height = this.height.getText().toString().replace("cm", "");
        String weight = this.weight.getText().toString().replace("kg", "");

        String item = this.wc.getText().toString().replace("尺", "");
        String waist = String.valueOf(Math.floor(Float.valueOf(item) * 33.33f + 0.5f));

        UserEntity user = new UserEntity();
        user.height = height;
        user.waist = waist;
        user.weight = weight;

        repository.updateUserInfo(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> showLoading("正在加载..."))
                .doOnTerminate(new Action() {
                    @Override
                    public void run() throws Exception {
                        dismissLoading();
                    }
                })
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new DefaultObserver<UserEntity>() {
                    @Override
                    public void onNext(UserEntity user) {
                        ToastUtils.showShort("更新资料成功");
                        vertifyFace();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        ToastUtils.showShort(throwable.getMessage());
                    }
                });

    }

    private void initView() {
        tbProfile = findViewById(R.id.tb_simple_profile);
        height = findViewById(R.id.sp_height);
        weight = findViewById(R.id.sp_weight);
        wc = findViewById(R.id.sp_wc);
        next = findViewById(R.id.tv_next);

        tbProfile.setData(
                "完 善 信 息",
                0, null,
                R.drawable.common_ic_wifi_state, null,
                null);

        setWifiLevel(tbProfile);

        height.setText(getHeights().get(0));
        weight.setText(getWeights().get(0));
        wc.setText(getWaists().get(0));
    }

    private List<String> heights = new ArrayList<>();
    private List<String> waists = new ArrayList<>();
    private List<String> weights = new ArrayList<>();

    public List<String> getHeights() {
        if (heights.isEmpty()) {
            for (int i = 60; i < 260; i++) {
                heights.add(i + "cm");
            }
        }
        return heights;
    }

    public List<String> getWaists() {
        if (waists.isEmpty()) {
            for (float i = 0.1f; i < 5.0f; i = i + 0.1f) {
                waists.add(String.format(Locale.getDefault(), "%.1f", i) + "尺");
            }
        }
        return waists;
    }

    public List<String> getWeights() {
        if (weights.isEmpty()) {
            for (int i = 30; i < 150; i++) {
                weights.add(i + "kg");
            }
        }
        return weights;
    }

    private void selectHeight() {
        OnOptionsSelectListener listener = new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                Timber.i("options1=%s, options2=%s, options3=%s, view =%s", options1, options2, options3, v);
                UserEntity user = new UserEntity();
                String item = getHeights().get(options1);
                height.setText(item);
                user.height = item.replace("cm", "");

            }

        };
        selectItems(getHeights(), listener, 110);
    }

    private void selectWeight() {
        OnOptionsSelectListener listener = new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                Timber.i("options1=%s, options2=%s, options3=%s, view =%s", options1, options2, options3, v);
                String item = getWeights().get(options1);
                weight.setText(item);
            }

        };
        selectItems(getWeights(), listener, 70);
    }

    private void selectItems(List<String> items, OnOptionsSelectListener listener) {
        OptionsPickerView<String> pickerView = new OptionsPickerBuilder(this, listener)
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
                .setOutSideCancelable(false)
                .build();
        pickerView.setPicker(items);
        pickerView.show();
    }

    private void selectItems(List<String> items, OnOptionsSelectListener listener, int index) {
        OptionsPickerView<String> pickerView = new OptionsPickerBuilder(this, listener)
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
                .setOutSideCancelable(false)
                .build();
        pickerView.setPicker(items);
        pickerView.setSelectOptions(index);
        pickerView.show();
    }


    private OptionsPickerView<String> mWaistPickerView;

    private void selectWaist() {
        OnOptionsSelectListener listener = new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                Timber.i("options1=%s, options2=%s, options3=%s, view =%s", options1, options2, options3, v);
                wc.setText(getWaists().get(options1));
            }

        };
        OnDismissListener onDismissListener = new OnDismissListener() {
            @Override
            public void onDismiss(Object o) {
                mWaistPickerView = null;
            }
        };
        OnOptionsSelectChangeListener onSelectChangelistener = new OnOptionsSelectChangeListener() {
            @Override
            public void onOptionsSelectChanged(int options1, int options2, int options3) {
                if (mWaistPickerView != null) {
                    String item = getWaists().get(options1).replace("尺", "");
                    String waist = String.valueOf(Math.floor(Float.valueOf(item) * 33.33f + 0.5f));
                    mWaistPickerView.setTitleText(String.format("约等于%scm", waist));
                }
            }
        };
        mWaistPickerView = new OptionsPickerBuilder(this, listener)
                .setOptionsSelectChangeListener(onSelectChangelistener)
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
                .setTitleSize(30)
                .setTitleColor(Color.parseColor("#FF333333"))
                .setTitleBgColor(Color.parseColor("#F5F5F5"))
                .setDividerColor(Color.TRANSPARENT)
                .isCenterLabel(false)
                .setOutSideCancelable(false)
                .build();
        mWaistPickerView.setPicker(getWaists());
        mWaistPickerView.setSelectOptions(19);
        mWaistPickerView.setOnDismissListener(onDismissListener);
        mWaistPickerView.show();
    }

    private void vertifyFace() {
        Routerfit.register(AppRouter.class)
                .skipFaceBd3SignUpActivity(UserSpHelper.getUserId(), new ActivityCallback() {
                    @Override
                    public void onActivityResult(int result, Object data) {
                        if (result == Activity.RESULT_OK) {
                            String sResult = data.toString();
                            if (TextUtils.isEmpty(sResult)) return;
                            if (sResult.equals("success")) {
                                //签约或者首页界面
                                toHome();
                            } else if (sResult.equals("failed")) {
                                ToastUtils.showShort("录入人脸失败");
                            }
                        }
                    }
                });
    }

    private void toHome() {
        //签约建档或主页
        Routerfit.register(AppRouter.class).skipMainOrQianyueActivity();
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        dismissLoading();
        MLVoiceSynthetize.stop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MLVoiceSynthetize.startSynthesize(getApplicationContext(),
                "请完善您的个人信息");
    }
}
