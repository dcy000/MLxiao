package com.example.han.referralproject.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.application.MyApplication;
import com.example.han.referralproject.bean.UserInfoBean;
import com.example.han.referralproject.music.ToastUtils;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.example.han.referralproject.util.LocalShared;
import com.medlink.danbogh.register.EatAdapter;
import com.medlink.danbogh.register.EatModel;
import com.medlink.danbogh.register.SignUp13SportsActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AlertDrinkingActivity extends BaseActivity {

    @BindView(R.id.tv_sign_up_title)
    TextView tvSignUpTitle;
    @BindView(R.id.rv_sign_up_content)
    RecyclerView rvSignUpContent;
    @BindView(R.id.tv_sign_up_go_back)
    TextView tvSignUpGoBack;
    @BindView(R.id.tv_sign_up_go_forward)
    TextView tvSignUpGoForward;
    private EatAdapter mAdapter;
    private List<EatModel> mModels;
    private String eat = "",smoke="",drink="",exercise="";
    private UserInfoBean data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert_drinking);
        ButterKnife.bind(this);
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("修改饮酒情况");
        tvSignUpGoBack.setText("取消");
        tvSignUpGoForward.setText("确定");
        data= (UserInfoBean) getIntent().getSerializableExtra("data");
        initView();
    }
    private void initView() {
        switch (data.eating_habits){
            case "荤素搭配":
                eat="1";
                break;
            case "偏好吃荤":
                eat="2";
                break;
            case "偏好吃素":
                eat="3";
                break;
            case "偏好吃咸":
                break;
            case "偏好油腻":
                break;
            case "偏好甜食":
                break;
        }
        switch (data.smoke){
            case "经常吸烟":
                smoke="1";
                break;
            case "偶尔吸烟":
                smoke="2";
                break;
            case "从不吸烟":
                smoke="3";
                break;
        }
        switch (data.drink){
            case "经常喝酒":
                smoke="1";
                break;
            case "偶尔喝酒":
                smoke="2";
                break;
            case "从不喝酒":
                smoke="3";
                break;
        }
        switch (data.exercise_habits){
            case "每天一次":
                exercise="1";
                break;
            case "每周几次":
                exercise="2";
                break;
            case "偶尔运动":
                exercise="3";
                break;
            case "从不运动":
                exercise="4";
                break;
        }
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        layoutManager.setOrientation(GridLayoutManager.VERTICAL);
        rvSignUpContent.setLayoutManager(layoutManager);
        mModels = eatModals();
        mAdapter = new EatAdapter(mModels);
        mAdapter.setOnItemClickListener(onItemClickListener);
        rvSignUpContent.setAdapter(mAdapter);

    }
    private int positionSelected = -1;

    private View.OnClickListener onItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = rvSignUpContent.getChildAdapterPosition(v);

            if (positionSelected >= 0
                    && positionSelected < mModels.size()) {
                mModels.get(positionSelected).setSelected(false);
                mAdapter.notifyItemChanged(positionSelected);
            }
            positionSelected = position;
            mModels.get(position).setSelected(true);
            mAdapter.notifyItemChanged(position);
        }
    };

    private List<EatModel> eatModals() {
        mModels = new ArrayList<>(3);
        mModels.add(new EatModel(getString(R.string.always_drink),
                R.drawable.ic_always_drink,
                R.color.colorSaltyPreference,
                R.drawable.bg_tv_salty_preference_selected,
                R.drawable.bg_tv_salty_preference));
        mModels.add(new EatModel(getString(R.string.sometimes_drink),
                R.drawable.ic_sometimes_drink,
                R.color.colorSaltyPreference,
                R.drawable.bg_tv_salty_preference_selected,
                R.drawable.bg_tv_salty_preference));
        mModels.add(new EatModel(getString(R.string.never_drink),
                R.drawable.ic_never_drink,
                R.color.colorSaltyPreference,
                R.drawable.bg_tv_salty_preference_selected,
                R.drawable.bg_tv_salty_preference));
        return mModels;
    }

    @OnClick(R.id.tv_sign_up_go_back)
    public void onTvGoBackClicked() {
        finish();
    }

    @OnClick(R.id.tv_sign_up_go_forward)
    public void onTvGoForwardClicked() {
        NetworkApi.alertBasedata(MyApplication.getInstance().userId, data.height, data.weight, eat, smoke, positionSelected+1+"", exercise, new NetworkManager.SuccessCallback<Object>() {
            @Override
            public void onSuccess(Object response) {
                ToastUtils.show("修改成功");
                switch (positionSelected+1){
                    case 1:
                        speak("主人，您的饮酒情况已经修改为"+"经常喝酒");
                        break;
                    case 2:
                        speak("主人，您的饮酒情况已经修改为"+"偶尔喝酒");
                        break;
                    case 3:
                        speak("主人，您的饮酒情况已经修改为"+"从不喝酒");
                        break;

                }
            }
        }, new NetworkManager.FailedCallback() {
            @Override
            public void onFailed(String message) {

            }
        });
    }
    @Override
    protected void onActivitySpeakFinish() {
        finish();
    }
}
