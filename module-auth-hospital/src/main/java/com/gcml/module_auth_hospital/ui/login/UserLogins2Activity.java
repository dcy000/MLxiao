package com.gcml.module_auth_hospital.ui.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gcml.common.data.AppManager;
import com.gcml.common.router.AppRouter;
import com.gcml.common.utils.base.ToolbarBaseActivity;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.common.widget.fdialog.BaseNiceDialog;
import com.gcml.common.widget.fdialog.NiceDialog;
import com.gcml.common.widget.fdialog.ViewConvertListener;
import com.gcml.common.widget.fdialog.ViewHolder;
import com.gcml.common.widget.toolbar.FilterClickListener;
import com.gcml.common.widget.toolbar.ToolBarClickListener;
import com.gcml.common.widget.toolbar.TranslucentToolBar;
import com.gcml.module_auth_hospital.R;
import com.gcml.module_auth_hospital.ui.register.UserRegisters2Activity;
import com.kaer.sdk.IDCardItem;
import com.sjtu.yifei.annotation.Route;
import com.sjtu.yifei.route.ActivityCallback;
import com.sjtu.yifei.route.Routerfit;

import me.jessyan.retrofiturlmanager.RetrofitUrlManager;

/**
 * Created by lenovo on 2019/1/17.
 */
@Route(path = "/auth/hospital/user/logins2/activity")
public class UserLogins2Activity extends ToolbarBaseActivity {

    private TranslucentToolBar tb;
    private LinearLayout lllogins;
    private TextView tvRegister;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isShowToolbar = false;
        setContentView(R.layout.activity_doctor_2_logins);

        lllogins = findViewById(R.id.ll_logins);
        tvRegister = findViewById(R.id.tv_to_register);

        tvRegister.setOnClickListener(
                v -> startActivity(new Intent(UserLogins2Activity.this, UserRegisters2Activity.class)));

        tb = findViewById(R.id.tb_logins);
        tb.setData("登 陆 注 册",
//                R.drawable.common_btn_back, "返回",
                0, null,
                R.drawable.common_ic_wifi_state, null,
                new ToolBarClickListener() {
                    @Override
                    public void onLeftClick() {
                        finish();
                    }

                    @Override
                    public void onRightClick() {
                        Routerfit.register(AppRouter.class).skipSettingActivity();
                    }
                });
        setWifiLevel(tb);
        AppManager.getAppManager().addActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        updatePage();
        updatePage2();
    }

    private void updatePage2() {
        lllogins.getChildAt(0).setVisibility(View.VISIBLE);
        lllogins.getChildAt(0).setOnClickListener(
                v ->
                        Routerfit.register(AppRouter.class).skipConnectActivity(36, (result, data) -> {
                            if (data instanceof IDCardItem) {
                                IDCardItem cardItem = ((IDCardItem) data);
                                if (cardItem != null) {
                                    Bundle bundle = new Bundle();
                                    bundle.putString("name", cardItem.partyName);
                                    bundle.putString("gender", cardItem.gender);
                                    bundle.putString("nation", cardItem.nation);
                                    bundle.putString("address", cardItem.certAddress);
                                    bundle.putParcelable("profile", cardItem.picBitmap);
                                    bundle.putString("idCard", cardItem.certNumber);
                                    startActivity(new Intent(UserLogins2Activity.this, IdCardInfoActivity.class)
                                            .putExtra("flag", "login")
                                            .putExtras(bundle));
                                }
                            }
                        }));

        lllogins.getChildAt(1).setVisibility(View.VISIBLE);
        lllogins.getChildAt(1).setOnClickListener(v ->
                startActivity(new Intent(UserLogins2Activity.this, IDCardNuberLoginActivity.class)));

        lllogins.getChildAt(2).setVisibility(View.VISIBLE);
        lllogins.getChildAt(2).setOnClickListener(new FilterClickListener(v ->
                Routerfit.register(AppRouter.class).skipFaceBd3SignInActivity(false, false, "", false, new ActivityCallback() {
                    @Override
                    public void onActivityResult(int result, Object data) {
                        if (result == Activity.RESULT_OK) {
                            String sResult = data.toString();
                            if (sResult.equals("success") || sResult.equals("skip")) {
                                Routerfit.register(AppRouter.class).skipMainActivity();
                            } else if (sResult.equals("failed")) {

                            }

                        }
                    }
                })));


    }

}
