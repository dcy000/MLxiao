package com.example.han.referralproject.require2.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.activity.WifiConnectActivity;
import com.example.han.referralproject.bean.UserInfoBean;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.example.han.referralproject.require2.dialog.DialogTypeEnum;
import com.example.han.referralproject.require2.dialog.SomeCommonDialog;
import com.example.han.referralproject.require2.register.activtiy.ChoiceIDCardRegisterTypeActivity;
import com.example.han.referralproject.require2.wrap.CanClearEditText;
import com.example.han.referralproject.yiyuan.util.ActivityHelper;
import com.medlink.danbogh.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginByIDCardNuberActivity extends BaseActivity implements SomeCommonDialog.OnDialogClickListener, CanClearEditText.OnTextChangeListener {

    @BindView(R.id.tv_phone_number_notice)
    CanClearEditText ccetIdNumber;
    @BindView(R.id.tv_next)
    TextView tvNext;
    @BindView(R.id.textView17)
    TextView textView17;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_by_idcard_nuber);
        ButterKnife.bind(this);
        intTitle();
        ActivityHelper.addActivity(this);
    }

    private void intTitle() {
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("身 份 证 号 码 登 录");

        mRightText.setVisibility(View.GONE);
        mRightView.setVisibility(View.VISIBLE);
        mRightView.setImageResource(R.drawable.white_wifi_3);
        mRightView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginByIDCardNuberActivity.this, WifiConnectActivity.class));
            }
        });

        mlSpeak("请输入身份证号码登录");
        ccetIdNumber.setListener(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        setEnableListeningLoop(false);
        setDisableGlobalListen(true);
    }

    @OnClick({R.id.tv_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_next:
                String IdCardNumber = ccetIdNumber.getPhone();
                if (!Utils.checkIdCard1(IdCardNumber)) {
                    mlSpeak("请输入正确的身份证号码");
                    return;
                }
                neworkCheckIdCard(IdCardNumber);
                break;
        }
    }

    /**
     * 网络检测身份证是否注册
     */
    private void neworkCheckIdCard(final String idCardNumber) {

        showLoadingDialog("");
        NetworkApi.isRegisteredByIdCard(idCardNumber, new NetworkManager.SuccessCallback<UserInfoBean>() {
            @Override
            public void onSuccess(UserInfoBean response) {
                hideLoadingDialog();
                /*//保存信息
                LocalShared.getInstance(mContext).setUserInfo(response);
                LocalShared.getInstance(mContext).setSex(response.sex);
                LocalShared.getInstance(mContext).setUserPhoto(response.user_photo);
                LocalShared.getInstance(mContext).setUserAge(response.age);
                LocalShared.getInstance(mContext).setUserHeight(response.height);
                new JpushAliasUtils(LoginByIDCardNuberActivity.this).setAlias("user_" + response.bid);*/

                startActivity(new Intent(LoginByIDCardNuberActivity.this, CodeActivity.class).putExtra("phone", response.tel).putExtra("idCardNumber",idCardNumber));
            }
        }, new NetworkManager.FailedCallback() {
            @Override
            public void onFailed(String message) {
                hideLoadingDialog();
                if (isFinishing() || isDestroyed()) {
                    return;
                }
                registerNoticeDialog();
            }
        });
    }

    private void registerNoticeDialog() {
        SomeCommonDialog dialog = new SomeCommonDialog(DialogTypeEnum.idCardUnregistered);
        dialog.setListener(this);
        dialog.show(getSupportFragmentManager(), "dialog");
    }

    @Override
    public void onClickConfirm(DialogTypeEnum type) {
        startActivity(new Intent(LoginByIDCardNuberActivity.this, ChoiceIDCardRegisterTypeActivity.class));
    }

    @Override
    public void onTextChange(Editable s) {
        if (TextUtils.isEmpty(s.toString())) {
            tvNext.setEnabled(false);
        } else {
            tvNext.setEnabled(true);
        }
    }
}
