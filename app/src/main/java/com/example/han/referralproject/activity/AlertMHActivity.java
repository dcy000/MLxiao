package com.example.han.referralproject.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.application.MyApplication;
import com.example.han.referralproject.bean.UserInfoBean;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.example.han.referralproject.new_music.ToastUtils;
import com.example.han.referralproject.util.ToastTool;
import com.medlink.danbogh.register.DiseaseHistoryAdapter;
import com.medlink.danbogh.register.DiseaseHistoryModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AlertMHActivity extends BaseActivity {

    @BindView(R.id.rv_sign_up_content)
    RecyclerView rvSignUpContent;
    @BindView(R.id.tv_sign_up_go_back)
    TextView tvSignUpGoBack;
    @BindView(R.id.tv_sign_up_go_forward)
    TextView tvSignUpGoForward;
    private DiseaseHistoryAdapter mAdapter;
    public List<DiseaseHistoryModel> mModels;
    public GridLayoutManager mLayoutManager;
    protected UserInfoBean data;
    protected String eat = "", smoke = "", drink = "", exercise = "";
    private StringBuffer buffer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert_mh);
        ButterKnife.bind(this);
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("修改病史");
        tvSignUpGoBack.setText("取消");
        tvSignUpGoForward.setText("确定");
        data = (UserInfoBean) getIntent().getSerializableExtra("data");
        buffer=new StringBuffer();
        if(data!=null){
            initView();
        }
    }


    protected void initView() {
        if (!TextUtils.isEmpty(data.eating_habits)) {
            switch (data.eating_habits) {
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
        if (!TextUtils.isEmpty(data.exercise_habits)) {
            switch (data.exercise_habits) {
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
        mLayoutManager = new GridLayoutManager(this, 3);
        mLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        rvSignUpContent.setLayoutManager(mLayoutManager);
        mModels = modals();
        mAdapter = new DiseaseHistoryAdapter(mModels);
        rvSignUpContent.setAdapter(mAdapter);
    }


    private List<DiseaseHistoryModel> modals() {
        mModels = new ArrayList<>(9);
        String[] diseaseTypes = getResources().getStringArray(R.array.disease_type);
        for (String diseaseType : diseaseTypes) {
            DiseaseHistoryModel model = new DiseaseHistoryModel(
                    diseaseType,
                    false,
                    R.color.textColorDiseaseSelected,
                    R.color.textColorDiseaseUnselected,
                    R.drawable.bg_tv_disease_selected,
                    R.drawable.bg_tv_disease
            );
            mModels.add(model);
        }
        return mModels;
    }

    @OnClick(R.id.tv_sign_up_go_back)
    public void onTvGoBackClicked() {
        finish();
    }

    @OnClick(R.id.tv_sign_up_go_forward)
    public void onTvGoForwardClicked() {
        String mh = getMh();
        if (TextUtils.isEmpty(mh)) {
            onTvGoBackClicked();
            return;
        }

        NetworkApi.alertBasedata(MyApplication.getInstance().userId, data.height, data.weight, eat, smoke, drink, exercise,
                mh,data.dz,new NetworkManager.SuccessCallback<Object>() {
                    @Override
                    public void onSuccess(Object response) {
                        ToastTool.showShort("修改成功");
                        speak("主人，您的病史已经修改成功");
                    }
                }, new NetworkManager.FailedCallback() {
                    @Override
                    public void onFailed(String message) {

                    }
                });
    }


    private String getMh() {
        StringBuilder mhBuilder = new StringBuilder();
        int size = mModels == null ? 0 : mModels.size();
        for (int i = 0; i < size; i++) {
            if (mModels.get(i).isSelected()) {
                mhBuilder.append(i + 1);
                mhBuilder.append(",");
            }
        }
        int length = mhBuilder.length();
        return length == 0 ? mhBuilder.toString() : mhBuilder.substring(0, length - 1);
    }

}
