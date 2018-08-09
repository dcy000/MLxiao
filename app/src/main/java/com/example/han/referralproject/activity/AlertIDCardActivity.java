package com.example.han.referralproject.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.bean.PUTUserBean;
import com.example.han.referralproject.bean.UserInfoBean;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.example.han.referralproject.util.LocalShared;
import com.google.gson.Gson;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.medlink.danbogh.utils.T;
import com.medlink.danbogh.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AlertIDCardActivity extends BaseActivity {

    @BindView(R.id.et_id_card)
    EditText etIdCard;
    @BindView(R.id.tv_sign_up_go_back)
    TextView tvSignUpGoBack;
    @BindView(R.id.tv_sign_up_go_forward)
    TextView tvSignUpGoForward;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert_idcard);
        ButterKnife.bind(this);
        initTitle();

    }

    private void initTitle() {
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("修改身份证号码");
        tvSignUpGoBack.setText("取消");
        tvSignUpGoForward.setText("确定");
    }

    @OnClick({R.id.tv_sign_up_go_back, R.id.tv_sign_up_go_forward})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_sign_up_go_back:
                finish();
                break;
            case R.id.tv_sign_up_go_forward:
                String idCard = etIdCard.getText().toString();
                if (TextUtils.isEmpty(idCard)) {
                    speak("请输入身份证号码");
                    return;
                }
                if (!Utils.checkIdCard1(idCard)) {
                    speak(R.string.sign_up_id_card_tip);
                    return;
                }
                neworkCheckIdCard(idCard);
                break;
        }
    }

    private void putUserInfo(String idCard) {
        PUTUserBean bean = new PUTUserBean();
        bean.bid = Integer.parseInt(LocalShared.getInstance(this).getUserId());
        bean.sfz = idCard;

        NetworkApi.putUserInfo(bean.bid, new Gson().toJson(bean), new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                String body = response.body();
                try {
                    JSONObject json = new JSONObject(body);
                    boolean tag = json.getBoolean("tag");
                    if (tag) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                speak("修改成功");
                            }
                        });
                        finish();
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                speak("修改失败");
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }


    private void neworkCheckIdCard(final String idCardNumber) {
        NetworkApi.isRegisteredByIdCard(idCardNumber, new NetworkManager.SuccessCallback<UserInfoBean>() {
            @Override
            public void onSuccess(UserInfoBean response) {
                T.show("您输入的身份证号码已注册");
            }
        }, new NetworkManager.FailedCallback() {
            @Override
            public void onFailed(String message) {
                putUserInfo(idCardNumber);
            }
        });
    }
}
