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
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.require2.wrap.CanClearEditText;
import com.example.han.referralproject.yiyuan.util.ActivityHelper;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.medlink.danbogh.utils.T;
import com.medlink.danbogh.utils.Utils;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddcontactPhoneActivity extends BaseActivity implements CanClearEditText.OnTextChangeListener {
    public static final String FROM_WHERE = "from_where";
    public static final String FROM_REGISTER_BY_IDCARD = "register_by_idCard";
    public static final String FROM_REGISTER_BY_IDCARD_NUMBER = "register_by_idCard_number";
    @BindView(R.id.tv_phone_number_notice)
    CanClearEditText ccetIdNumber;
    @BindView(R.id.tv_next)
    TextView tvNext;
    @BindView(R.id.textView17)
    TextView textView17;
    private String fromWhere;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        ButterKnife.bind(this);
        intTitle();
        ActivityHelper.addActivity(this);
    }

    private void intTitle() {
        Intent intent = getIntent();
        if (intent != null) {
            fromWhere = intent.getStringExtra(FROM_WHERE);
        }
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
    protected void onStart() {
        super.onStart();
        ccetIdNumber.setHintText("请输入手机号码");
        ccetIdNumber.setMaxLength(11);
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
                if (!Utils.isValidPhone(IdCardNumber)) {
                    mlSpeak("请输入正确的手机号码");
                    return;
                }

                addEmergentContact(IdCardNumber);
                break;
        }
    }

    /**
     * 提交添加联系人接口
     */
    private void addEmergentContact(final String idCardNumber) {
        showLoadingDialog("...");
        NetworkApi.putEmergentContact(idCardNumber, new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                try {
                    String body = response.body();
                    JSONObject jsonObject = new JSONObject(body);
                    if (jsonObject.optBoolean("tag")) {
                        jumpPage();

                        T.show("添加成功");
                    } else {
                        T.show("添加失败");
                    }

                } catch (Exception e) {
                    T.show("添加失败");
                }
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                hideLoadingDialog();
                T.show("添加失败");
            }

            @Override
            public void onFinish() {
                super.onFinish();
                hideLoadingDialog();

            }


        });

    }

    /**
     * 添加紧急联系人成功之后  进行页面跳转
     */
    private void jumpPage() {
        if (fromWhere.equals(FROM_REGISTER_BY_IDCARD)) {
            startActivity(new Intent(this, InputFaceActivity.class).putExtras(getIntent()));
        } else if (fromWhere.equals(FROM_REGISTER_BY_IDCARD_NUMBER)) {
            startActivity(new Intent(this, RealNameActivity.class).putExtras(getIntent()));
        }
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
