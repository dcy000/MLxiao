package com.gcml.old.auth.profile.otherinfo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.util.LocalShared;
import com.gcml.common.widget.toolbar.ToolBarClickListener;
import com.gcml.common.widget.toolbar.TranslucentToolBar;
import com.gcml.old.auth.profile.otherinfo.bean.PUTUserBean;
import com.google.gson.Gson;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.json.JSONException;
import org.json.JSONObject;

public class AlertNameActivity extends AppCompatActivity implements View.OnClickListener {

    private TranslucentToolBar mTvNameTitle;
    private EditText mEtSignUpName;
    /**
     * 上一步
     */
    private TextView mTvSignUpGoBack;
    /**
     * 下一步
     */
    private TextView mTvSignUpGoForward;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert_name);
        initView();
    }

    private void initView() {
        mTvNameTitle = findViewById(R.id.tb_name_title);
        mEtSignUpName = findViewById(R.id.et_sign_up_name);
        mTvSignUpGoBack = findViewById(R.id.tv_sign_up_go_back);
        mTvSignUpGoBack.setOnClickListener(this);
        mTvSignUpGoForward = findViewById(R.id.tv_sign_up_go_forward);
        mTvSignUpGoForward.setOnClickListener(this);

        mTvSignUpGoBack.setText("取消");
        mTvSignUpGoForward.setText("确定");
        mTvNameTitle.setData("修 改 姓 名", R.drawable.common_icon_back, "返回",
                0, null, new ToolBarClickListener() {
                    @Override
                    public void onLeftClick() {
                        finish();
                    }

                    @Override
                    public void onRightClick() {

                    }
                });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.tv_sign_up_go_back:
                finish();
                break;
            case R.id.tv_sign_up_go_forward:
                nextStep();
                break;
        }
    }

    private void nextStep() {
        String name = mEtSignUpName.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            speak("请输入您的姓名");
            return;
        }
        PUTUserBean bean = new PUTUserBean();
        bean.bid = Integer.parseInt(LocalShared.getInstance(this).getUserId());
        bean.bname = name;

        NetworkApi.putUserInfo(bean.bid, new Gson().toJson(bean), new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                String body = response.body();
                try {
                    JSONObject json = new JSONObject(body);
                    boolean tag = json.getBoolean("tag");
                    if (tag) {
                        runOnUiThread(() -> speak("修改成功"));
                        finish();
                    } else {
                        runOnUiThread(() -> speak("修改失败"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void speak(String text) {
        MLVoiceSynthetize.startSynthesize(this, text, false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MLVoiceSynthetize.stop();
    }
}
