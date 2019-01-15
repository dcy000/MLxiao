package com.gcml.module_auth_hospital.doctor;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.billy.cc.core.component.CC;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.common.widget.toolbar.ToolBarClickListener;
import com.gcml.common.widget.toolbar.TranslucentToolBar;
import com.gcml.module_auth_hospital.R;

/**
 * Created by lenovo on 2019/1/15.
 */

public class SelectUserDefualtRegisterTypeActivity extends AppCompatActivity implements View.OnClickListener {
    private TranslucentToolBar tbSelectUserRegister;
    private RelativeLayout rlSelectUserRegisterIdcrad;
    private RelativeLayout rlSelectUserRegisterCardNumber;
    private RelativeLayout rlSelectUserRegisterCityCard;
    /**
     * 确认
     */
    private TextView tvSelectUserRegisterConfirm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_user_default_register);
        initView();
    }

    private void initView() {
        tbSelectUserRegister = (TranslucentToolBar) findViewById(R.id.tb_select_user_register);
        rlSelectUserRegisterIdcrad = (RelativeLayout) findViewById(R.id.rl_select_user_register_idcrad);
        rlSelectUserRegisterIdcrad.setOnClickListener(this);
        rlSelectUserRegisterCardNumber = (RelativeLayout) findViewById(R.id.rl_select_user_register_card_number);
        rlSelectUserRegisterCardNumber.setOnClickListener(this);
        rlSelectUserRegisterCityCard = (RelativeLayout) findViewById(R.id.rl_select_user_register_city_card);
        rlSelectUserRegisterCityCard.setOnClickListener(this);
        tvSelectUserRegisterConfirm = (TextView) findViewById(R.id.tv_select_user_register_confirm);
        tvSelectUserRegisterConfirm.setOnClickListener(this);

        tbSelectUserRegister.setData("患 者 注 册 方 式 选 择",
                R.drawable.common_btn_back, "返回",
                R.drawable.common_ic_wifi_state, null, new ToolBarClickListener() {
                    @Override
                    public void onLeftClick() {
                        finish();
                    }

                    @Override
                    public void onRightClick() {
                        CC.obtainBuilder("com.gcml.old.wifi").build().callAsync();
                    }
                });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.rl_select_user_register_idcrad:
                rlSelectUserRegisterIdcrad.getChildAt(1).setSelected(!rlSelectUserRegisterIdcrad.getChildAt(1).isSelected());
                break;
            case R.id.rl_select_user_register_card_number:
                rlSelectUserRegisterCardNumber.getChildAt(1).setSelected(!rlSelectUserRegisterCardNumber.getChildAt(1).isSelected());
                break;
            case R.id.rl_select_user_register_city_card:
                rlSelectUserRegisterCityCard.getChildAt(1).setSelected(!rlSelectUserRegisterCityCard.getChildAt(1).isSelected());
                break;
            case R.id.tv_select_user_register_confirm:
                String tag = (String) tvSelectUserRegisterConfirm.getTag();
                ToastUtils.showShort("点击确认" + tag);
                break;
        }
        updateButtonState();
    }

    private void updateButtonState() {
        if (rlSelectUserRegisterIdcrad.getChildAt(1).isSelected()
                && rlSelectUserRegisterCardNumber.getChildAt(1).isSelected()
                && rlSelectUserRegisterCityCard.getChildAt(1).isSelected()) {
            tvSelectUserRegisterConfirm.setEnabled(true);

        } else {
            tvSelectUserRegisterConfirm.setEnabled(false);
        }
    }
}
