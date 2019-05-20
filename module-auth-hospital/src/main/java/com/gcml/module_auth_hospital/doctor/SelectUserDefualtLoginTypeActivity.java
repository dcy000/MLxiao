package com.gcml.module_auth_hospital.doctor;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gcml.common.router.AppRouter;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.common.widget.toolbar.ToolBarClickListener;
import com.gcml.common.widget.toolbar.TranslucentToolBar;
import com.gcml.module_auth_hospital.R;
import com.sjtu.yifei.route.Routerfit;

/**
 * Created by lenovo on 2019/1/15.
 */

public class SelectUserDefualtLoginTypeActivity extends AppCompatActivity implements View.OnClickListener {
    private TranslucentToolBar tbSelectUserLogin;
    private RelativeLayout rlSelectUserLoginIdcrad;
    private RelativeLayout rlSelectUserLoginCardNumber;
    private RelativeLayout rlSelectUserLoginFace;
    private RelativeLayout rlSelectUserLoginCityCard;
    private TextView tvSelectUserLoginConfirm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_user_default_login);
        initView();
        bindData();
    }

    private void bindData() {

    }

    private void initView() {
        tbSelectUserLogin = findViewById(R.id.tb_select_user_login);
        rlSelectUserLoginIdcrad = findViewById(R.id.rl_select_user_login_idcrad);
        rlSelectUserLoginIdcrad.setOnClickListener(this);
        rlSelectUserLoginCardNumber = findViewById(R.id.rl_select_user_login_card_number);
        rlSelectUserLoginCardNumber.setOnClickListener(this);
        rlSelectUserLoginFace = findViewById(R.id.rl_select_user_login_face);
        rlSelectUserLoginFace.setOnClickListener(this);
        rlSelectUserLoginCityCard = findViewById(R.id.rl_select_user_login_city_card);
        rlSelectUserLoginCityCard.setOnClickListener(this);
        tvSelectUserLoginConfirm = findViewById(R.id.tv_select_user_login_confirm);
        tvSelectUserLoginConfirm.setOnClickListener(this);

        tbSelectUserLogin.setData("患 者 登 录 方 式 选 择",
                R.drawable.common_btn_back, "返回",
                R.drawable.common_ic_wifi_state, null, new ToolBarClickListener() {
                    @Override
                    public void onLeftClick() {
                        finish();
                    }

                    @Override
                    public void onRightClick() {
                        Routerfit.register(AppRouter.class).skipWifiConnectActivity(false);
                    }
                });

        tvSelectUserLoginConfirm.setEnabled(true);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.rl_select_user_login_idcrad) {
            rlSelectUserLoginIdcrad.getChildAt(1).setSelected(!rlSelectUserLoginIdcrad.getChildAt(1).isSelected());
        } else if (id == R.id.rl_select_user_login_card_number) {
            rlSelectUserLoginCardNumber.getChildAt(1).setSelected(!rlSelectUserLoginCardNumber.getChildAt(1).isSelected());
        } else if (id == R.id.rl_select_user_login_face) {
            rlSelectUserLoginFace.getChildAt(1).setSelected(!rlSelectUserLoginFace.getChildAt(1).isSelected());
        } else if (id == R.id.rl_select_user_login_city_card) {
            rlSelectUserLoginCityCard.getChildAt(1).setSelected(!rlSelectUserLoginCityCard.getChildAt(1).isSelected());
        } else if (id == R.id.tv_select_user_login_confirm) {
            String tag = (String) tvSelectUserLoginConfirm.getTag();
            ToastUtils.showShort("点击确认" + tag);
        }
        updateButtonState();
    }

    private void updateButtonState() {
        if (!rlSelectUserLoginIdcrad.getChildAt(1).isSelected()
                && !rlSelectUserLoginIdcrad.getChildAt(1).isSelected()
                && !rlSelectUserLoginIdcrad.getChildAt(1).isSelected()
                && !rlSelectUserLoginIdcrad.getChildAt(1).isSelected()) {
            tvSelectUserLoginConfirm.setEnabled(false);

        } else {
            tvSelectUserLoginConfirm.setEnabled(true);
        }

    }

}
