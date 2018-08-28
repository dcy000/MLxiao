package com.gcml.old.auth.profile;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.application.MyApplication;
import com.gcml.old.auth.entity.UserInfoBean;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.example.han.referralproject.util.LocalShared;
import com.gcml.lib_utils.display.ToastUtils;
import com.gcml.old.auth.register.SelectAdapter;

import java.util.ArrayList;
import java.util.List;


import github.hellocsl.layoutmanager.gallery.GalleryLayoutManager;

public class AlertHeightActivity extends BaseActivity {

    TextView tvSignUpHeight;

    RecyclerView rvSignUpContent;

    TextView tvSignUpUnit;

    TextView tvSignUpGoBack;

    TextView tvSignUpGoForward;
    protected int selectedPosition = 1;
    protected SelectAdapter adapter;
    protected ArrayList<String> mStrings;
    protected UserInfoBean data;
    protected String eat = "", smoke = "", drink = "", exercise = "";
    protected StringBuffer buffer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert_height);
        tvSignUpHeight = (TextView) findViewById(R.id.tv_sign_up_height);
        rvSignUpContent = (RecyclerView) findViewById(R.id.rv_sign_up_content);
        tvSignUpUnit = (TextView) findViewById(R.id.tv_sign_up_unit);
        tvSignUpGoBack = (TextView) findViewById(R.id.tv_sign_up_go_back);
        tvSignUpGoForward = (TextView) findViewById(R.id.tv_sign_up_go_forward);

        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("修改身高");
        tvSignUpGoBack.setText("取消");
        tvSignUpGoForward.setText("确定");
        tvSignUpGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTvGoBackClicked();
            }
        });
        tvSignUpGoForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTvGoForwardClicked();
            }
        });
        data = (UserInfoBean) getIntent().getSerializableExtra("data");
        buffer = new StringBuffer();
        if (data != null) {
            initView();
        }
    }

    protected void initView() {
        if (!TextUtils.isEmpty(data.eatingHabits)) {
            switch (data.eatingHabits) {
                case "荤素搭配":
                    eat = "1";
                    break;
                case "偏好吃荤":
                    eat = "2";
                    break;
                case "偏好吃素":
                    eat = "3";
                    break;
                case "偏好吃咸":
                    break;
                case "偏好油腻":
                    break;
                case "偏好甜食":
                    break;
            }
        }
        if (!TextUtils.isEmpty(data.smoke)) {
            switch (data.smoke) {
                case "经常抽烟":
                    smoke = "1";
                    break;
                case "偶尔抽烟":
                    smoke = "2";
                    break;
                case "从不抽烟":
                    smoke = "3";
                    break;
            }
        }
        if (!TextUtils.isEmpty(data.drink)) {
            switch (data.drink) {
                case "经常喝酒":
                    smoke = "1";
                    break;
                case "偶尔喝酒":
                    smoke = "2";
                    break;
                case "从不喝酒":
                    smoke = "3";
                    break;
            }
        }
        if (!TextUtils.isEmpty(data.exerciseHabits)) {
            switch (data.exerciseHabits) {
                case "每天一次":
                    exercise = "1";
                    break;
                case "每周几次":
                    exercise = "2";
                    break;
                case "偶尔运动":
                    exercise = "3";
                    break;
                case "从不运动":
                    exercise = "4";
                    break;
            }
        }
        if ("尚未填写".equals(data.mh)) {
            buffer = null;
        } else {
            String[] mhs = data.mh.split("\\s+");
            for (int i = 0; i <mhs.length; i++) {
                if (mhs[i].equals("高血压"))
                    buffer.append(1 + ",");
                else if (mhs[i].equals("糖尿病"))
                    buffer.append(2 + ",");
                else if (mhs[i].equals("冠心病"))
                    buffer.append(3 + ",");
                else if (mhs[i].equals("慢阻肺"))
                    buffer.append(4 + ",");
                else if (mhs[i].equals("孕产妇"))
                    buffer.append(5 + ",");
                else if (mhs[i].equals("痛风"))
                    buffer.append(6 + ",");
                else if (mhs[i].equals("甲亢"))
                    buffer.append(7 + ",");
                else if (mhs[i].equals("高血脂"))
                    buffer.append(8 + ",");
                else if (mhs[i].equals("其他"))
                    buffer.append(9 + ",");
            }
        }
        tvSignUpUnit.setText("cm");
        GalleryLayoutManager layoutManager = new GalleryLayoutManager(GalleryLayoutManager.VERTICAL);
        layoutManager.attach(rvSignUpContent, selectedPosition);
        layoutManager.setCallbackInFling(true);
        layoutManager.setOnItemSelectedListener(new GalleryLayoutManager.OnItemSelectedListener() {
            @Override
            public void onItemSelected(RecyclerView recyclerView, View item, int position) {
                selectedPosition = position;
                select(mStrings == null ? String.valueOf(position) : mStrings.get(position));
            }
        });
        adapter = new SelectAdapter();
        adapter.setStrings(getStrings());
        adapter.setOnItemClickListener(new SelectAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                rvSignUpContent.smoothScrollToPosition(position);
            }
        });
        rvSignUpContent.setAdapter(adapter);
    }

    protected List<String> getStrings() {
        mStrings = new ArrayList<>();
        for (int i = 150; i < 200; i++) {
            mStrings.add(String.valueOf(i));
        }
        return mStrings;
    }

    private void select(String text) {
        ToastUtils.showShort(text);
    }

    protected int geTip() {
        return R.string.sign_up_height_tip;
    }

    public void onTvGoBackClicked() {
        finish();
    }

    public void onTvGoForwardClicked() {
        final String height = mStrings.get(selectedPosition);

        NetworkApi.alertBasedata(MyApplication.getInstance().userId, height, data.weight, eat, smoke, drink, exercise,
                buffer == null ? "" : buffer.substring(0, buffer.length() - 1), data.dz, new NetworkManager.SuccessCallback<Object>() {
                    @Override
                    public void onSuccess(Object response) {
                        LocalShared.getInstance(AlertHeightActivity.this).setUserHeight(height);
                        com.gcml.lib_utils.display.ToastUtils.showShort("修改成功");
//                        speak("主人，您的身高已经修改为" + height + "厘米");
                        speak("修改成功");

                    }
                }, new NetworkManager.FailedCallback() {
                    @Override
                    public void onFailed(String message) {
                        com.gcml.lib_utils.display.ToastUtils.showShort(message);
//                speak("主人，出了一些小问题，未修改成功");
                    }
                });
    }

    @Override
    protected void onActivitySpeakFinish() {
        finish();
    }
}
