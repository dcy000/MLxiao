package com.example.han.referralproject.require2.healthservice;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.activity.WifiConnectActivity;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.require2.dialog.AcountInfoDialog;
import com.example.han.referralproject.require2.register.activtiy.ChoiceIDCardRegisterTypeActivity;
import com.example.han.referralproject.require2.wrap.CanClearEditText;
import com.example.han.referralproject.util.Utils;
import com.google.gson.Gson;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.medlink.danbogh.utils.T;

import org.json.JSONObject;
import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BindHealthManageSystemActivity extends BaseActivity implements AcountInfoDialog.OnFragmentInteractionListener {

    @BindView(R.id.ccet_phone)
    CanClearEditText ccetPhone;
    @BindView(R.id.tv_next)
    TextView tvNext;
    @BindView(R.id.et_pass_word)
    EditText etPassWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_health_manage_system);
        ButterKnife.bind(this);
        initTitle();
        initView();
    }

    private void initView() {
        ccetPhone.setIsChinese(true);
        ccetPhone.setHintText("请输入您的公共卫生服务管理账号");

        ccetPhone.setListener(new CanClearEditText.OnTextChangeListener() {
            @Override
            public void onTextChange(Editable s) {
                String passWord = etPassWord.getText().toString().trim();
                if (!TextUtils.isEmpty(passWord) && !TextUtils.isEmpty(s.toString())) {
                    tvNext.setEnabled(true);
                } else {
                    tvNext.setEnabled(false);
                }
            }
        });

        ccetPhone.setTextSize(30);

        etPassWord.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String accout = ccetPhone.getPhone();
                if (!TextUtils.isEmpty(accout) && !TextUtils.isEmpty(s.toString())) {
                    tvNext.setEnabled(true);
                } else {
                    tvNext.setEnabled(false);
                }

            }
        });

    }

    private void initTitle() {
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("V3.0账号绑定");

        mRightText.setVisibility(View.GONE);
        mRightView.setVisibility(View.VISIBLE);
        mRightView.setImageResource(R.drawable.white_wifi_3);
        mRightView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BindHealthManageSystemActivity.this,
                        WifiConnectActivity.class));
            }
        });

        MLVoiceSynthetize.startSynthesize(this, "绑定公共卫生服务管理 V3.0账号",
                false);

    }

    @OnClick(R.id.tv_next)
    public void onViewClicked() {
        // TODO: 2018/8/6 查询
        NetworkApi.Query3Account(ccetPhone.getPhone()
                , etPassWord.getText().toString().trim()
                , new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        String body = response.body();
                        try {
                            AccoutInfoBean accoutInfoBean = new Gson().fromJson(body, AccoutInfoBean.class);
                            if (accoutInfoBean != null) {
                                if (accoutInfoBean.tag) {
                                    AccoutInfoBean.DataBean data = accoutInfoBean.data;
                                    if (data != null) {
                                        if (!TextUtils.isEmpty(data.orgName) && !TextUtils.isEmpty(data.userName)) {
                                            showAccountInfoDialog(data.userName, data.orgName);
                                        }

                                    }
                                }
                            }
                        } catch (Exception e) {

                        }
                    }
                });

    }

    private void showAccountInfoDialog(String operator, String orgnizationName) {
        AcountInfoDialog dialog = AcountInfoDialog.newInstance(operator, orgnizationName);
        dialog.setListener(this);
        dialog.show(getSupportFragmentManager(), "accountInfo");
    }

    /**
     * dialog取消回调
     */
    @Override
    public void onCancle() {

    }

    /**
     * dialog 确定回调
     */
    @Override
    public void onConfirm() {
        TEqSystemBind bindBean = new TEqSystemBind();
        bindBean.equipmentId = Utils.getDeviceId();
        bindBean.password = etPassWord.getText().toString().trim();
        bindBean.userName = ccetPhone.getPhone();

        String jsonData = new Gson().toJson(bindBean);
        NetworkApi.Post3Account(bindBean.equipmentId, jsonData, new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                String body = response.body();
                try {
                    JSONObject object = new JSONObject(body);
                    if (object.getBoolean("tag")) {
                        T.show("绑定成功");
                        finish();
                    } else {
                        T.show("绑定失败");
                    }
                } catch (Exception e) {

                }

            }
        });
    }

    static class TEqSystemBind {
        String equipmentId;

        String password;

        String userName;
    }

}
