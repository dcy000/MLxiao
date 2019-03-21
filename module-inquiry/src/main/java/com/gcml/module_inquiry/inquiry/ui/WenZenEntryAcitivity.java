package com.gcml.module_inquiry.inquiry.ui;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.gcml.common.data.UserEntity;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.common.widget.toolbar.ToolBarClickListener;
import com.gcml.common.widget.toolbar.TranslucentToolBar;
import com.gcml.module_inquiry.R;
import com.gcml.module_inquiry.inquiry.ui.base.InquiryBaseActivity;
import com.gcml.module_inquiry.inquiry.ui.fragment.AddressFragment;
import com.gcml.module_inquiry.inquiry.ui.fragment.DrinkFramgment;
import com.gcml.module_inquiry.inquiry.ui.fragment.HeightFragment;
import com.gcml.module_inquiry.inquiry.ui.fragment.PregnancyFragment;
import com.gcml.module_inquiry.inquiry.ui.fragment.WeightFragment;
import com.gcml.module_inquiry.inquiry.ui.fragment.YueJingTimeFragment;
import com.gcml.module_inquiry.inquiry.ui.fragment.base.ChildActionListenerAdapter;
import com.gcml.module_inquiry.inquiry.ui.fragment.base.InquiryBaseFrament;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by lenovo on 2019/3/21.
 */

public class WenZenEntryAcitivity extends InquiryBaseActivity {

    private TranslucentToolBar tb;
    private FragmentManager fragmentManager;

    private List<InquiryBaseFrament> fragments = new ArrayList<>();

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
                .doOnSubscribe(disposable -> showLoading("页面加载中"))
                .doOnTerminate(() -> dismissLoading())
                .subscribe(userEntity -> {
                    if (TextUtils.equals(userEntity.sex, "男")) {
                        HeightFragment heightFragment = HeightFragment.newInstance(null, null);
                        WeightFragment weightFragment = WeightFragment.newInstance(null, null);
                        AddressFragment addressFragment = AddressFragment.newInstance(null, null);
                        DrinkFramgment drinkFramgment = DrinkFramgment.newInstance(null, null);

                        fragments.add(heightFragment);
                        fragments.add(weightFragment);
                        fragments.add(addressFragment);
                        fragments.add(drinkFramgment);

                        heightFragment.setListenerAdapter(listenerAdapter);
                        weightFragment.setListenerAdapter(listenerAdapter);
                        addressFragment.setListenerAdapter(listenerAdapter);
                        drinkFramgment.setListenerAdapter(listenerAdapter);

                    } else {
                        PregnancyFragment pregnancyFragment = PregnancyFragment.newInstance(null, null);
                        YueJingTimeFragment yueJingTimeFragment = YueJingTimeFragment.newInstance(null, null);

                        pregnancyFragment.setListenerAdapter(listenerAdapter);
                        yueJingTimeFragment.setListenerAdapter(listenerAdapter);
                    }
                    initBody();
                });

    }

    private void initBody() {
        fragmentManager = getSupportFragmentManager();
        replaceFragment(fragments.get(currentPosition));
    }

    private int currentPosition = 0;
    private ChildActionListenerAdapter listenerAdapter = new ChildActionListenerAdapter() {
        @Override
        public void onStartBack() {
            finish();
        }

        @Override
        public void onBack() {
            currentPosition--;
            replaceFragment(fragments.get(currentPosition));
        }


        @Override
        public void onNext(Object... data) {
            currentPosition++;
            replaceFragment(fragments.get(currentPosition));
        }

        public void onFinalNext(Object... data) {
            ToastUtils.showShort("最后一个页面了");
        }
    };

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fm_container, fragment);
        transaction.commit();
    }

    private void initTitle() {
        tb = findViewById(R.id.tb_wenzen_entry);
        tb.setData("问 诊",
                R.drawable.common_btn_back, "返回",
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


}
