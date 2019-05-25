package com.gcml.module_auth_hospital.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import com.gcml.common.router.AppRouter;
import com.gcml.common.utils.DefaultObserver;
import com.gcml.common.utils.Handlers;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.base.ToolbarBaseActivity;
import com.gcml.common.widget.toolbar.FilterClickListener;
import com.gcml.common.widget.toolbar.ToolBarClickListener;
import com.gcml.common.widget.toolbar.TranslucentToolBar;
import com.gcml.module_auth_hospital.R;
import com.gcml.module_auth_hospital.model2.UserRepository;
import com.gcml.module_auth_hospital.postinputbean.SignUpBean;
import com.sjtu.yifei.route.Routerfit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class BindPhoneActivity extends ToolbarBaseActivity {

    private TextView notice;
    private TextView next;
    private TextView sendCode;
    private EditText phone;
    private EditText code;
    private TranslucentToolBar tb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isShowToolbar = false;
        setContentView(R.layout.activity_bind_phone);
        initView();
    }

    private void initView() {
        notice = findViewById(R.id.tv_notice);
        tb = findViewById(R.id.tb_bind_phone);
        phone = findViewById(R.id.et_phone);
        code = findViewById(R.id.et_code);
        next = findViewById(R.id.tv_next);
        next.setOnClickListener(new FilterClickListener(v -> {
            String phoneNumber = phone.getText().toString().trim();
            signUp(phoneNumber);
        }));
        sendCode = findViewById(R.id.tv_send_code);

        tb.setData("忘 记 密 码",
                R.drawable.common_btn_back, "返回",
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

        phone.addTextChangedListener(watcher);
        sendCode.setOnClickListener(new FilterClickListener(v -> sendCode()));


    }

    UserRepository repository = new UserRepository();

    private void signUp(String phoneNumber) {
        Intent data = getIntent();
        if (data != null) {
            String passWord = data.getStringExtra("passWord");
            String idCardNumber = data.getStringExtra("idCardNumber");

            SignUpBean bean = new SignUpBean();
            bean.setIdNo(idCardNumber);
            bean.setTel(phoneNumber);

            repository.signUp(bean, passWord)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .as(RxUtils.autoDisposeConverter(this))
                    .subscribe(new DefaultObserver<UserBean>() {
                        @Override
                        public void onNext(UserBean userBean) {
                            super.onNext(userBean);
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            super.onError(throwable);
                        }

                        @Override
                        public void onComplete() {
                            super.onComplete();
                        }
                    });
        }

    }

    private void sendCode() {
        updateCountDownUi();
        //请求验证码
       /* showLoadingDialog("");
        NetworkApi.getCode(phoneNumber, new NetworkManager.SuccessCallback<String>() {

            @Override
            public void onSuccess(String codeJson) {
                hideLoadingDialog();
                try {
                    JSONObject codeObj = new JSONObject(codeJson);
                    String code = codeObj.optString("code");
                    CodeActivity.this.code = code;
                    if (code != null) {
                        updateCountDownUi();
                        T.show("获取验证码成功");
                        mlSpeak("获取验证码成功");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    T.show("获取验证码失败");
                    mlSpeak("获取验证码失败");
                }
            }
        }, new NetworkManager.FailedCallback() {
            @Override
            public void onFailed(String message) {
                hideLoadingDialog();
                T.show("获取验证码失败");
                mlSpeak("获取验证码失败");
            }
        });
*/
    }


    TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (TextUtils.isEmpty(phone.getText().toString()) && TextUtils.isEmpty(code.getText().toString())) {
                next.setEnabled(false);
            } else {
                next.setEnabled(true);
            }

        }
    };

    private void updateCountDownUi() {
        sendCode.setSelected(false);
        sendCode.setEnabled(false);
        Handlers.ui().postDelayed(new Runnable() {
            @Override
            public void run() {
                count--;
                if (count <= 0) {
                    sendCode.setSelected(true);
                    sendCode.setEnabled(true);
                    sendCode.setText("发送验证码");
                    count = 60;
                    return;
                }
                sendCode.setText(count + "秒重发");
                Handlers.ui().postDelayed(this, 1000);
            }
        }, 1000);
    }

    private int count = 60;
}
