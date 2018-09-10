package com.gcml.old.auth.profile.otherinfo;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.homepage.MainActivity;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.example.han.referralproject.util.LocalShared;
import com.gcml.common.widget.toolbar.ToolBarClickListener;
import com.gcml.common.widget.toolbar.TranslucentToolBar;
import com.gcml.lib_utils.display.ToastUtils;
import com.gcml.old.auth.entity.UserInfoBean;
import com.gcml.old.auth.profile.otherinfo.bean.PUTUserBean;
import com.google.gson.Gson;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.medlink.danbogh.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

public class AlertIDCardActivity extends AppCompatActivity implements View.OnClickListener {

    private TranslucentToolBar mTbIdcardTitle;
    /**
     * 您的身份证号码
     */
    private TextView mTvSignUpIdCard;
    /**
     * 请输入身份证号码
     */
    private EditText mEtIdCard;
    /**
     * 上一步
     */
    private TextView mTvSignUpGoBack;
    /**
     * 下一步
     */
    private TextView mTvSignUpGoForward;
    private ConstraintLayout mClSignUpRootIdCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert_idcard);
        initView();
    }

    private void initView() {
        mTbIdcardTitle = (TranslucentToolBar) findViewById(R.id.tb_idcard_title);
        mTvSignUpIdCard = (TextView) findViewById(R.id.tv_sign_up_id_card);
        mEtIdCard = (EditText) findViewById(R.id.et_id_card);
        mTvSignUpGoBack = (TextView) findViewById(R.id.tv_sign_up_go_back);
        mTvSignUpGoBack.setOnClickListener(this);
        mTvSignUpGoForward = (TextView) findViewById(R.id.tv_sign_up_go_forward);
        mTvSignUpGoForward.setOnClickListener(this);
        mClSignUpRootIdCard = (ConstraintLayout) findViewById(R.id.cl_sign_up_root_id_card);

        mTvSignUpGoBack.setText("取消");
        mTvSignUpGoForward.setText("确定");
        mTbIdcardTitle.setData("修改身份证号", R.drawable.common_icon_back, "返回",
                R.drawable.common_icon_home, null, new ToolBarClickListener() {
                    @Override
                    public void onLeftClick() {
                        finish();
                    }

                    @Override
                    public void onRightClick() {
                        startActivity(new Intent(AlertIDCardActivity.this, MainActivity.class));
                        finish();
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
        String idCard = mEtIdCard.getText().toString();
        if (TextUtils.isEmpty(idCard)) {
            speak("请输入身份证号码");
            return;
        }
        if (!Utils.checkIdCard1(idCard)) {
            speak("主人,请输入正确的身份证号码");
            return;
        }
        neworkCheckIdCard(idCard);
    }

    private void neworkCheckIdCard(final String idCardNumber) {
        NetworkApi.isRegisteredByIdCard(idCardNumber,
                response -> ToastUtils.showShort("您输入的身份证号码已注册"),
                message -> putUserInfo(idCardNumber));
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

        //        UserEntity user = new UserEntity();
//        user.age = seletedAge;
//        CCResult result = CC.obtainBuilder("com.gcml.auth.putUser")
//                .addParam("user", user)
//                .build()
//                .call();
//        Observable<Object> data = result.getDataItem("data");
//        data.subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .as(RxUtils.autoDisposeConverter(this))
//                .subscribe(new DefaultObserver<Object>() {
//                    @Override
//                    public void onNext(Object o) {
//                        super.onNext(o);
//                        runOnUiThread(() -> speak("修改成功"));
//                    }
//
//                    @Override
//                    public void onError(Throwable throwable) {
//                        super.onError(throwable);
//                        runOnUiThread(() -> speak("修改失败"));
//                    }
//                });
    }

    private void speak(String text) {
        MLVoiceSynthetize.startSynthesize(this, text, false);
    }

}
