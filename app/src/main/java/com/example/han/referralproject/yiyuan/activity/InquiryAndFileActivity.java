package com.example.han.referralproject.yiyuan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.application.MyApplication;
import com.example.han.referralproject.bean.UserInfo;
import com.example.han.referralproject.building_record.BuildingRecordActivity;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.example.han.referralproject.require2.login.ChoiceLoginTypeActivity;
import com.example.han.referralproject.util.LocalShared;
import com.example.han.referralproject.yiyuan.bean.WenZhenReultBean;
import com.google.gson.Gson;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.medlink.danbogh.call2.NimAccountHelper;
import com.medlink.danbogh.register.SignUp7HeightActivity;
import com.medlink.danbogh.utils.T;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class InquiryAndFileActivity extends BaseActivity {
    @BindView(R.id.iv_wenzhen)
    ImageView ivWenzhen;
    @BindView(R.id.iv_jiandang)
    ImageView ivJiandang;
    @BindView(R.id.textView3)
    TextView textView3;
    @BindView(R.id.textView6)
    TextView textView6;
    @BindView(R.id.tv_register_done)
    TextView tvRegisterDone;
    @BindView(R.id.iv_inquire_file_exit)
    ImageView ivInquireFileExit;
    @BindView(R.id.tv_file_done)
    TextView tvFileDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inquiry_and_file);
        ButterKnife.bind(this);
        initTitle();
        initView();
    }

    private void initView() {
        boolean isRegister = getIntent().getBooleanExtra("isRegister", false);
        if (isRegister) {
            tvRegisterDone.setVisibility(View.VISIBLE);
            mlSpeak("恭喜您已完成注册，您可选择问诊或建档，完成后即可进入主页");
        } else {
            T.showLong("验证通过，欢迎回来");
            tvRegisterDone.setVisibility(View.INVISIBLE);
        }


        NetworkApi.getFiledIsOrNot(this
                , NetworkApi.FILE_URL
                , LocalShared.getInstance(this).getUserId()
                , new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (response == null) {
                            onGetFileStateFailed();
                            return;
                        }
                        WenZhenReultBean reultBean = new Gson().fromJson(response.body(), WenZhenReultBean.class);
                        if (reultBean.tag) {
                            ivJiandang.setEnabled(false);
                            tvFileDone.setVisibility(View.VISIBLE);

                        } else {
                            ivJiandang.setEnabled(true);
                            tvFileDone.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        T.show("网络繁忙");
                    }
                });
    }


    private void initTitle() {
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("问诊建档");
        mLeftText.setVisibility(View.GONE);
        mLeftView.setVisibility(View.GONE);

    }

    @OnClick({R.id.iv_wenzhen, R.id.iv_jiandang, R.id.iv_inquire_file_exit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_wenzhen:
//                InquiryAndFileEndActivity.startMe(this,"问诊");
                Intent intent = new Intent(this, SignUp7HeightActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            case R.id.iv_jiandang:
                //请求接口 判断时候建档
//                NetworkApi.getFiledIsOrNot(this
//                        , NetworkApi.FILE_URL
//                        , LocalShared.getInstance(this).getUserId()
//                        , new StringCallback() {
//                            @Override
//                            public void onSuccess(Response<String> response) {
//                                if (response == null) {
//                                    T.show("网络繁忙,请稍后重试~");
//                                    return;
//                                }
//                                WenZhenReultBean reultBean = new Gson().fromJson(response.body(), WenZhenReultBean.class);
//                                if (reultBean.tag) {
//                                    T.show("您已建档完毕");
//                                    MLVoiceSynthetize.startSynthesize(InquiryAndFileActivity.this, "您已建档完毕", false);
//                                } else {
//                                    startActivity(new Intent(InquiryAndFileActivity.this, BuildingRecordActivity.class));
//                                }
//                            }
//                        });

                gotoFiled();

                break;
            case R.id.iv_inquire_file_exit:
                tuiChu();
                break;
        }
    }

    private void gotoFiled() {
        NetworkApi.PersonInfo(MyApplication.getInstance().userId, new NetworkManager.SuccessCallback<UserInfo>() {
            @Override
            public void onSuccess(UserInfo response) {
                if (response == null) {
                    return;
                }
                String state = response.getState();
                if ("1".equals(state) || ("0".equals(state) && !TextUtils.isEmpty(response.getDoctername()))) {
                    //请求接口 判断时候建档
                    isNotFile(true);
                } else {
                    isNotFile(false);
                }

            }

        }, new NetworkManager.FailedCallback() {
            @Override
            public void onFailed(String message) {
                T.show(message);
            }
        });
    }

    private void isNotFile(final boolean isBindDoctor) {
        NetworkApi.getFiledIsOrNot(this
                , NetworkApi.FILE_URL
                , LocalShared.getInstance(this).getUserId()
                , new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (response == null) {
                            onGetFileStateFailed();
                            return;
                        }
                        WenZhenReultBean reultBean = new Gson().fromJson(response.body(), WenZhenReultBean.class);
                        if (reultBean.tag) {
                            T.show("您已建档完毕");
                            MLVoiceSynthetize.startSynthesize(InquiryAndFileActivity.this, "您已建档完毕", false);
                        } else {
                            startActivity(new Intent(InquiryAndFileActivity.this, BuildingRecordActivity.class).putExtra("bind", isBindDoctor));
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        T.show("网络繁忙");
                    }
                });
    }

    private void onGetFileStateFailed() {
        T.show("网络繁忙,请稍后重试~");
    }


    public void tuiChu() {

        MobclickAgent.onProfileSignOff();
        NimAccountHelper.getInstance().logout();
        LocalShared.getInstance(this).loginOut();
        startActivity(new Intent(this, ChoiceLoginTypeActivity.class));
        finish();
    }
}
