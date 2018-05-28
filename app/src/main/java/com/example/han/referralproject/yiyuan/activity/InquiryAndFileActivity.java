package com.example.han.referralproject.yiyuan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.building_record.BuildingRecordActivity;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.util.LocalShared;
import com.example.han.referralproject.yiyuan.bean.WenZhenReultBean;
import com.google.gson.Gson;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.medlink.danbogh.register.SignUp7HeightActivity;
import com.medlink.danbogh.utils.T;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inquiry_and_file);
        ButterKnife.bind(this);
        initTitle();
    }

    private void initTitle() {
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("问诊建档");
    }

    @OnClick({R.id.iv_wenzhen, R.id.iv_jiandang})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_wenzhen:
//                InquiryAndFileEndActivity.startMe(this,"问诊");
                Intent intent = new Intent(this, SignUp7HeightActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            case R.id.iv_jiandang:
                //请求接口 判断时候建档
                NetworkApi.getFiledIsOrNot(this
                        , NetworkApi.FILE_URL
                        , LocalShared.getInstance(this).getUserId()
                        , new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                if (response == null) {
                                    T.show("网络繁忙,请稍后重试~");
                                    return;
                                }
                                WenZhenReultBean reultBean = new Gson().fromJson(response.body(), WenZhenReultBean.class);
                                if (reultBean.tag) {
                                    T.show("您已建档完毕");
                                    MLVoiceSynthetize.startSynthesize(InquiryAndFileActivity.this,"您已建档完毕",false);
                                } else {
                                    startActivity(new Intent(InquiryAndFileActivity.this, BuildingRecordActivity.class));
                                }
                            }
                        });

                break;
        }
    }

}
