package com.example.han.referralproject.yiyuan.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.application.MyApplication;
import com.example.han.referralproject.bean.UserInfo;
import com.example.han.referralproject.building_record.BuildingRecordActivity;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.example.han.referralproject.require2.login.ChoiceLoginTypeActivity;
import com.example.han.referralproject.require4.bean.InquiryInfoResponseBean;
import com.example.han.referralproject.util.LocalShared;
import com.example.han.referralproject.yiyuan.bean.WenZhenReultBean;
import com.google.gson.Gson;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.medlink.danbogh.call2.NimAccountHelper;
import com.medlink.danbogh.register.SignUp3AddressActivity;
import com.medlink.danbogh.register.SignUp7HeightActivity;
import com.medlink.danbogh.register.SignUp8WeightActivity;
import com.medlink.danbogh.utils.T;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class InquiryAndFile2Activity extends BaseActivity {
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
    @BindView(R.id.tv_wen_jian_skip)
    TextView tvWenJianSkip;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inquiry_and_file2);
        ButterKnife.bind(this);
        initTitle();
//        initView();
        getInquiryInfo();
        getBindInfo();
    }

    private void getBindInfo() {
        showLoadingDialog("");
        NetworkApi.PersonInfo(MyApplication.getInstance().userId, new NetworkManager.SuccessCallback<UserInfo>() {
            @Override
            public void onSuccess(UserInfo response) {
                if (response == null) {
                    return;
                }
                String state = response.getState();
                if ("1".equals(state) || ("0".equals(state) && !TextUtils.isEmpty(response.getDoctername()))) {
                    //请求接口 判断时候建档
                    isBindDoctor = true;
                    getFiledInfo();
                } else {
                    isBindDoctor = false;
                }

            }

        }, new NetworkManager.FailedCallback() {
            @Override
            public void onFailed(String message) {
                T.show(message);
                hideLoadingDialog();
            }
        });
    }

    private boolean isBindDoctor;
    private boolean isFiled;

    private void getFiledInfo() {
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
                        try {
                            if (reultBean.tag) {
                                ivJiandang.setEnabled(false);
                                tvFileDone.setVisibility(View.VISIBLE);
                                if (reultBean.data != null) {
                                    tvFileDone.setText("(建档单位: " + reultBean.data.orgName + ")");
                                }
                            } else {
                                ivJiandang.setEnabled(true);
                                tvFileDone.setVisibility(View.GONE);
                            }
                        } catch (Exception e) {
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        T.show("网络繁忙");
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        hideLoadingDialog();
                    }
                });

    }

    Integer age;
    Integer height;
    Integer weight;
    Integer weightModify;

    private void getInquiryInfo() {
        showLoading("");
        NetworkApi.getInquiryInfo(MyApplication.getInstance().userId, new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                try {
                    String body = response.body();
                    InquiryInfoResponseBean inquiryInfoResponseBean = new Gson().fromJson(body, InquiryInfoResponseBean.class);
                    if (inquiryInfoResponseBean.tag) {
                        InquiryInfoResponseBean.DataBean data = inquiryInfoResponseBean.data;
                        if (data != null) {
                            if (data.age != null) {
                                age = data.age;
                            }

                            if (data.height != null) {
                                height = data.height;
                            }

                            if (data.weightModifyDays != null) {
                                weightModify = data.weightModifyDays;
                            }
                            if (data.weight != null) {
                                weight = data.weight;
                            }
                        }

                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                hideLoading();
            }
        });
    }

    private void initTitle() {
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("问诊建档");
        mRightView.setVisibility(View.GONE);
        mRightText.setVisibility(View.GONE);
    }

    @OnClick({R.id.iv_wenzhen, R.id.iv_jiandang, R.id.iv_inquire_file_exit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_wenzhen:
                wenzen();
                break;
            case R.id.iv_jiandang:
                gotoFiled();
                break;
            case R.id.iv_inquire_file_exit:
                tuiChu();
                break;
        }
    }

    private void wenzen() {
        if (height != null && age >= 25) {
            if (weightModify >= 90 || weight == null || weight <= 0) {
                LocalShared.getInstance(this.getApplicationContext()).setSignUpHeight(Integer.valueOf(height));
                Intent intent = new Intent(this, SignUp8WeightActivity.class);
                intent.putExtra("weight", weight);
                startActivity(intent);
            } else {
                LocalShared.getInstance(getApplicationContext()).setSignUpWeight(Integer.valueOf(weight));
                Intent intent = new Intent(this, SignUp3AddressActivity.class);
                startActivity(intent);
            }
        } else {
            Intent intent = new Intent(this, SignUp7HeightActivity.class)
                    .putExtra("weightModify", weightModify)
                    .putExtra("weight", weight)
                    .putExtra("wheight", height);
            startActivity(intent);
        }
    }

    private void gotoFiled() {
        if (isBindDoctor) {
            isNotFile(true);
        } else {
            isNotFile(false);
        }
    }

    private void isNotFile(final boolean isBindDoctor) {
        startActivity(new Intent(InquiryAndFile2Activity.this, BuildingRecordActivity.class).putExtra("bind", isBindDoctor));
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


    public void showLoading(String message) {
        try {
            dialog = new ProgressDialog(mContext);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setIndeterminate(true);
            dialog.setMessage(message);
            dialog.show();
        } catch (Exception e) {
        }

    }


    public void hideLoading() {
        try {
            if (dialog == null) {
                return;
            }
            dialog.dismiss();
        } catch (Exception e) {
        }
    }
}
