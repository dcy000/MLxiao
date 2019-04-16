package com.gcml.module_inquiry.inquiry.ui;


import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.gcml.common.base.BaseActivity;
import com.gcml.common.data.UserEntity;
import com.gcml.common.http.ApiResult;
import com.gcml.common.utils.DefaultObserver;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.common.widget.dialog.AlertDialog;
import com.gcml.common.widget.toolbar.ToolBarClickListener;
import com.gcml.common.widget.toolbar.TranslucentToolBar;
import com.gcml.module_blutooth_devices.bloodpressure.BloodPressurePresenter;
import com.gcml.module_inquiry.R;
import com.gcml.module_inquiry.inquiry.ui.base.InquiryBaseActivity;
import com.gcml.module_inquiry.inquiry.ui.fragment.AddressFragment;
import com.gcml.module_inquiry.inquiry.ui.fragment.BloodPressureFragment;
import com.gcml.module_inquiry.inquiry.ui.fragment.DrinkFramgment;
import com.gcml.module_inquiry.inquiry.ui.fragment.GuoMinFragment;
import com.gcml.module_inquiry.inquiry.ui.fragment.HeightFragment;
import com.gcml.module_inquiry.inquiry.ui.fragment.PregnancyFragment;
import com.gcml.module_inquiry.inquiry.ui.fragment.WeightFragment;
import com.gcml.module_inquiry.inquiry.ui.fragment.YueJingTimeFragment;
import com.gcml.module_inquiry.inquiry.ui.fragment.base.ChildActionListenerAdapter;
import com.gcml.module_inquiry.model.HealthFileRepostory;
import com.gcml.module_inquiry.model.WenZhenBean;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by lenovo on 2019/3/21.
 */

public class WenZenEntryAcitivity extends InquiryBaseActivity {

    private TranslucentToolBar tb;
    private FragmentManager fragmentManager;

    private List<Fragment> fragments = new ArrayList<>();

    @Override

    protected int layoutId() {
        return R.layout.activity_wenzen_entry;
    }

    @Override
    protected void initView() {
        initTitle();
        initFragments();
    }

    private void initFragments() {
        CCResult call = CC.obtainBuilder("com.gcml.auth.fetchUser").build().call();
        Observable<UserEntity> user = call.getDataItem("data");
        user.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> showLoading(getString(R.string.prediagnosis_loading_tips)))
                .doOnTerminate(() -> dismissLoading())
                .subscribe(new DefaultObserver<UserEntity>() {
                    @Override
                    public void onError(Throwable throwable) {
                        ToastUtils.showShort(throwable.getMessage());
                    }

                    @Override
                    public void onNext(UserEntity userEntity) {
                        HeightFragment heightFragment = HeightFragment.newInstance(null, null);//1
                        WeightFragment weightFragment = WeightFragment.newInstance(null, null);//2
                        AddressFragment addressFragment = AddressFragment.newInstance(null, null);//3
                        DrinkFramgment drinkFramgment = DrinkFramgment.newInstance(null, null);//4

                        fragments.add(heightFragment);
                        fragments.add(weightFragment);
                        fragments.add(addressFragment);
                        fragments.add(drinkFramgment);
                        heightFragment.setListenerAdapter(listenerAdapter);
                        weightFragment.setListenerAdapter(listenerAdapter);
                        addressFragment.setListenerAdapter(listenerAdapter);
                        drinkFramgment.setListenerAdapter(listenerAdapter);

                        if (!TextUtils.equals(userEntity.sex, "男")) {
                            PregnancyFragment pregnancyFragment = PregnancyFragment.newInstance(null, null);//5
                            YueJingTimeFragment yueJingTimeFragment = YueJingTimeFragment.newInstance(null, null);//6

                            pregnancyFragment.setListenerAdapter(listenerAdapter);
                            yueJingTimeFragment.setListenerAdapter(listenerAdapter);
                        }

                        GuoMinFragment guoMinFragment = GuoMinFragment.newInstance(null, null);//7
                        fragments.add(guoMinFragment);
                        guoMinFragment.setListenerAdapter(listenerAdapter);

                        BloodPressureFragment bloodpressureFragment = BloodPressureFragment.newInstance(null, null);//8
                        fragments.add(bloodpressureFragment);
                        bloodpressureFragment.setListenerAdapter(listenerAdapter);
                        initBody();
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                    }
                });

    }

    private void initBody() {
        fragmentManager = getSupportFragmentManager();
        replaceFragment(fragments.get(currentPosition));
    }

    private int currentPosition = 0;
    private ChildActionListenerAdapter listenerAdapter = new ChildActionListenerAdapter() {
        @Override
        public void onStartBack(String... data) {
            finish();
        }

        @Override
        public void onBack(String... data) {
            updateData(data[0], data);
            currentPosition--;
            replaceFragment(fragments.get(currentPosition));
        }

        @Override
        public void onNext(String... data) {
            updateData(data[0], data);
            currentPosition++;
            replaceFragment(fragments.get(currentPosition));
        }

        public void onFinalNext(String... data) {
            updateData(data[0], data);
            postWenZhenData();
        }

        @Override
        public void onBluetoothConnect(BloodPressurePresenter presenter) {
            setBlueTitle(R.drawable.common_icon_bluetooth_connect, presenter);
        }

        @Override
        public void onBluetoothBreak(BloodPressurePresenter presenter) {
            setBlueTitle(R.drawable.common_icon_bluetooth_break, presenter);
        }

        @Override
        public void onNormalICon() {
            setNormalTitle();
        }
    };

    public void setBlueTitle(int rightIconResourse, BloodPressurePresenter presenter) {
        tb.setData(getString(R.string.prediagnosis_title),
                R.drawable.common_btn_back, getString(R.string.prediagnosis_back_tips),
                rightIconResourse, null, new ToolBarClickListener() {
                    @Override
                    public void onLeftClick() {
                        finish();
                    }

                    @Override
                    public void onRightClick() {
                        reconnection(presenter);
                    }
                });
    }

    private void reconnection(BloodPressurePresenter presenter) {
        new AlertDialog(WenZenEntryAcitivity.this)
                .builder()
                .setMsg("您确定解绑之前的设备，重新连接新设备吗？")
                .setNegativeButton(getString(R.string.prediagnosis_cancel_tips), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                })
                .setPositiveButton(getString(R.string.prediagnosis_cancel_tips), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (presenter != null) {
                            presenter.startDiscovery(null);
                        }
                    }
                }).show();
    }

    public void setNormalTitle() {
        tb.setData(getString(R.string.prediagnosis_title),
                R.drawable.common_btn_back, getString(R.string.prediagnosis_back_tips),
                R.drawable.common_ic_wifi_state, null, new ToolBarClickListener() {
                    @Override
                    public void onLeftClick() {
                        finish();
                    }

                    @Override
                    public void onRightClick() {
                        onRightClickWithPermission(new IAction() {
                            @Override
                            public void action() {
                                CC.obtainBuilder("com.gcml.old.setting").build().call();
                            }
                        });

                    }
                });

        setWifiLevel(tb);
    }

    private void updateData(String tag, String... data) {
        if (TextUtils.equals("1", tag)) {
            bean.height = data[1];
        } else if (TextUtils.equals("2", tag)) {
            bean.weight = data[1];
        } else if (TextUtils.equals("3", tag)) {
            bean.address = data[1];
        } else if (TextUtils.equals("4", tag)) {
            bean.weekDrinkState = data[1];
            bean.wineType = data[2];
        } else if (TextUtils.equals("5", tag)) {
            bean.pregnantState = data[1];
        } else if (TextUtils.equals("6", tag)) {
            bean.lastMensesTime = data[1];
        } else if (TextUtils.equals("7", tag)) {
            bean.allergicHistory = data[1];
            bean.diseasesHistory = data[2];
        } else if (TextUtils.equals("8", tag)) {
            bean.highPressure = data[1];
            bean.lowPressure = data[2];
        }
    }


    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fm_container, fragment);
        transaction.commit();
    }

    private void initTitle() {
        tb = findViewById(R.id.tb_wenzen_entry);
        setNormalTitle();
    }

    WenZhenBean bean = new WenZhenBean();

    private void postWenZhenData() {
        fileRepostory.postWenZen(bean)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> {
                    showLoading("");
                })
                .doOnTerminate(new Action() {
                    @Override
                    public void run() throws Exception {
                        dismissLoading();
                    }
                })
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new DefaultObserver<ApiResult<Object>>() {
                    @Override
                    public void onNext(ApiResult<Object> objectApiResult) {
                        super.onNext(objectApiResult);
                        ToastUtils.showShort(getString(R.string.prediagnosis_submit_success_tips));
//                        CC.obtainBuilder("health.profile.wenzen.output")
//                                .addParam("highPrssure", bean.highPressure)
//                                .addParam("lowPressure", bean.lowPressure)
//                                .build()
//                                .call();
                        finish();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });

    }

    HealthFileRepostory fileRepostory = new HealthFileRepostory();
}
