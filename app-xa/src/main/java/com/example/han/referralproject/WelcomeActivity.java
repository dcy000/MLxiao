package com.example.han.referralproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.gcml.common.constant.Global;
import com.gcml.common.data.UserSpHelper;
import com.gcml.common.router.AppRouter;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.common.utils.network.NetUitls;
import com.gcml.common.widget.fdialog.BaseNiceDialog;
import com.gcml.common.widget.fdialog.NiceDialog;
import com.gcml.common.widget.fdialog.ViewConvertListener;
import com.gcml.common.widget.fdialog.ViewHolder;
import com.sjtu.yifei.annotation.Route;
import com.sjtu.yifei.route.Routerfit;

import me.jessyan.retrofiturlmanager.RetrofitUrlManager;

@Route(path = "/app/welcome/activity")
public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        RetrofitUrlManager.getInstance().setGlobalDomain("http://192.168.200.210:5555/");//娄
//        RetrofitUrlManager.getInstance().setGlobalDomain("http://192.168.200.222:5555/");//左

        //刚启动应用的时候就存一下游客的token
        UserSpHelper.setToken(Global.TOURIST_TOKEN);
        Routerfit.register(AppRouter.class).getWakeControlProvider().enableWakeuperListening(true);
        initContentView();
    }

    private void initContentView() {
        setContentView(R.layout.activity_welcome);
        if (!NetUitls.isWifiConnected()) {
            Routerfit.register(AppRouter.class).skipWifiConnectActivity(true);
            finish();
        } else {
//          Routerfit.register(AppRouter.class).skipAuthActivity();//登录
//            Routerfit.register(AppRouter.class).skipUserRegistersActivity();//身份证注册
//            showIpInputDialog();
            Routerfit.register(AppRouter.class).skipUserLogins2Activity();//
//            touristLogin();
        }

    }

    private void showIpInputDialog() {
        NiceDialog.init()
                .setLayoutId(R.layout.dialog_ip_input)
                .setConvertListener(new ViewConvertListener() {
                    @Override
                    protected void convertView(ViewHolder holder, BaseNiceDialog dialog) {
                        EditText ip = holder.getView(R.id.et_input);
                        EditText port = holder.getView(R.id.et_port);
                        holder.getView(R.id.btn_neg).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String trim = ip.getText().toString().trim();
                                String portTrim = port.getText().toString().trim();
                                if (TextUtils.isEmpty(trim) || TextUtils.isEmpty(portTrim)) {
                                    ToastUtils.showShort("输入的IP不正确和端口");
                                    return;
                                }
                                RetrofitUrlManager.getInstance().setGlobalDomain("http://" + trim + ":" + portTrim + "/");
                                dialog.dismiss();
                                touristLogin();
                            }
                        });
                        holder.getView(R.id.btn_pos).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                touristLogin();
                            }
                        });
                    }
                })
                .setWidth(700)
                .setHeight(300)
                .setOutCancel(false)
                .show(getSupportFragmentManager());
    }

    private void touristLogin() {
        Routerfit.register(AppRouter.class).skipUserLogins2Activity();//
    }
}
