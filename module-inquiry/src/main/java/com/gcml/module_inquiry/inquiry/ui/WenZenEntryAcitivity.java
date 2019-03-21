package com.gcml.module_inquiry.inquiry.ui;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.billy.cc.core.component.CC;
import com.gcml.common.widget.toolbar.ToolBarClickListener;
import com.gcml.common.widget.toolbar.TranslucentToolBar;
import com.gcml.module_inquiry.R;
import com.gcml.module_inquiry.inquiry.ui.base.InquiryBaseActivity;
import com.gcml.module_inquiry.inquiry.ui.fragment.base.ChildActionListenerAdapter;
import com.gcml.module_inquiry.inquiry.ui.fragment.HeightFragment;

/**
 * Created by lenovo on 2019/3/21.
 */

public class WenZenEntryAcitivity extends InquiryBaseActivity {

    private TranslucentToolBar tb;
    private FragmentManager fragmentManager;

    @Override
    protected int layoutId() {
        return R.layout.activity_wenzen_entry;
    }

    @Override
    protected void initView() {
        initTitle();
        initBody();
    }

    private void initBody() {
        fragmentManager = getSupportFragmentManager();
        HeightFragment fragment = HeightFragment.newInstance(null, null);
        fragment.setListenerAdapter(listenerAdapter);
        replaceFragment(fragment);
    }

    private ChildActionListenerAdapter listenerAdapter = new ChildActionListenerAdapter() {
        @Override
        public void onBack() {

        }

        @Override
        public void onNext(Object... data) {

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
