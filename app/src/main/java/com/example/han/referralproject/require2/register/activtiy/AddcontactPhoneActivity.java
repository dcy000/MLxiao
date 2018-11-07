package com.example.han.referralproject.require2.register.activtiy;

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
import com.example.han.referralproject.require2.login.CodeActivity;
import com.example.han.referralproject.require2.wrap.CanClearEditText;
import com.example.han.referralproject.util.LocalShared;
import com.example.han.referralproject.yiyuan.util.ActivityHelper;
import com.medlink.danbogh.utils.JpushAliasUtils;
import com.medlink.danbogh.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddcontactPhoneActivity extends BaseActivity implements  CanClearEditText.OnTextChangeListener {

    @BindView(R.id.tv_phone_number_notice)
    CanClearEditText ccetIdNumber;
    @BindView(R.id.tv_next)
    TextView tvNext;
    @BindView(R.id.textView17)
    TextView textView17;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        ButterKnife.bind(this);
        intTitle();
        ActivityHelper.addActivity(this);
    }

    private void intTitle() {
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("身 份 证 扫 描 注 册");

        mRightText.setVisibility(View.GONE);
        mRightView.setVisibility(View.VISIBLE);
        mRightView.setImageResource(R.drawable.white_wifi_3);
        mRightView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddcontactPhoneActivity.this, WifiConnectActivity.class));
            }
        });

        mlSpeak("请输入紧急联系人手机号");
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
                    mlSpeak("请输入紧急联系人手机号");
                    return;
                }
//                neworkCheckIdCard(IdCardNumber);
                break;
        }
    }

    /**
     * 提交添加联系人接口
     */
    private void neworkCheckIdCard(final String idCardNumber) {

        NetworkApi.isRegisteredByIdCard(idCardNumber, new NetworkManager.SuccessCallback<UserInfoBean>() {
            @Override
            public void onSuccess(UserInfoBean response) {
                if (isFinishing() || isDestroyed()) {
                    return;
                }
                //保存信息
                LocalShared.getInstance(mContext).setUserInfo(response);
                LocalShared.getInstance(mContext).setSex(response.sex);
                LocalShared.getInstance(mContext).setUserPhoto(response.user_photo);
                LocalShared.getInstance(mContext).setUserAge(response.age);
                LocalShared.getInstance(mContext).setUserHeight(response.height);
                new JpushAliasUtils(AddcontactPhoneActivity.this).setAlias("user_" + response.bid);

                startActivity(new Intent(AddcontactPhoneActivity.this, CodeActivity.class).putExtra("phone", response.tel));
            }
        }, new NetworkManager.FailedCallback() {
            @Override
            public void onFailed(String message) {
                if (isFinishing() || isDestroyed()) {
                    return;
                }
            }
        });
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
