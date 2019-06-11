package com.gcml.module_auth_hospital.ui.findPassWord;

import android.app.Activity;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.widget.FrameLayout;

import com.gcml.common.router.AppRouter;
import com.gcml.common.utils.base.ToolbarBaseActivity;
import com.gcml.common.widget.toolbar.ToolBarClickListener;
import com.gcml.common.widget.toolbar.TranslucentToolBar;
import com.gcml.module_auth_hospital.R;
import com.gcml.module_auth_hospital.ui.findPassWord.fragment.FindPassWordFragment;
import com.gcml.module_auth_hospital.ui.findPassWord.fragment.SetPassWord2Fragment;
import com.sjtu.yifei.route.Routerfit;

public class ForgetPassWordActivity extends ToolbarBaseActivity {

    private TranslucentToolBar tbForgetPsw;
    private FrameLayout flContaniner;
    public MutableLiveData<String> liveData = new MutableLiveData<>();

    public static void startMe(Context context) {
        Intent intent = new Intent(context, ForgetPassWordActivity.class);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    private String soures = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pass_word);
        initView();
        liveData.observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String soures) {
                soures = soures;
                if (TextUtils.equals("findPswNext", soures)) {
                    addSetPswFragment();
                } else {
                    finish();
                }
            }
        });
    }

    private void initView() {
        tbForgetPsw = findViewById(R.id.tb_forget_psw);
        flContaniner = findViewById(R.id.fl_container);
        tbForgetPsw.setData("忘 记 密 码",
                R.drawable.common_btn_back, "返回",
                R.drawable.common_ic_wifi_state, null,
                new ToolBarClickListener() {
                    @Override
                    public void onLeftClick() {
                        if (TextUtils.equals("findPswNext", soures)) {
                            addFindPswFragment();
                        } else {
                            finish();
                        }
                    }

                    @Override
                    public void onRightClick() {
//                        Routerfit.register(AppRouter.class).skipSettingActivity();
                        Routerfit.register(AppRouter.class).skipWifiConnectActivity(false);
                    }
                });
        setWifiLevel(tbForgetPsw);

        addFindPswFragment();
    }

    private void addFindPswFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fl_container, findFindPswFragment(), "FindPassWordFragment");
        ft.commitAllowingStateLoss();
    }

    private void addSetPswFragment() {
        Intent data = getIntent();
        if (data == null) {
            return;
        }
        String phoneNumber = data.getStringExtra("phoneNumber");

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();


        ft.replace(R.id.fl_container, findSetPswFragment(phoneNumber), "SetPassWord2Fragment");
        ft.commitAllowingStateLoss();
    }

    private SetPassWord2Fragment findSetPswFragment(String phoneNumber) {
        SetPassWord2Fragment setPassWord2Fragment = (SetPassWord2Fragment) getSupportFragmentManager()
                .findFragmentByTag("SetPassWord2Fragment");
        if (setPassWord2Fragment == null) {
            return SetPassWord2Fragment.newInstance(phoneNumber, null);
        }
        return setPassWord2Fragment;

    }

    private FindPassWordFragment findFindPswFragment() {
        FindPassWordFragment setPassWord2Fragment = (FindPassWordFragment) getSupportFragmentManager()
                .findFragmentByTag("FindPassWordFragment");
        if (setPassWord2Fragment == null) {
            return FindPassWordFragment.newInstance(null, null);
        }
        return setPassWord2Fragment;

    }
}
