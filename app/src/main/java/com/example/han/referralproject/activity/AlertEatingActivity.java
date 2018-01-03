package com.example.han.referralproject.activity;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.application.MyApplication;
import com.example.han.referralproject.bean.UserInfoBean;
import com.example.han.referralproject.music.ToastUtils;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.example.han.referralproject.util.ToastUtil;
import com.medlink.danbogh.register.EatAdapter;
import com.medlink.danbogh.register.EatModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AlertEatingActivity extends BaseActivity {

    @BindView(R.id.tv_sign_up_title)
    TextView tvSignUpTitle;
    @BindView(R.id.rv_sign_up_content)
    RecyclerView rvSignUpContent;
    @BindView(R.id.tv_sign_up_go_back)
    TextView tvSignUpGoBack;
    @BindView(R.id.tv_sign_up_go_forward)
    TextView tvSignUpGoForward;
    private UserInfoBean data;
    private GridLayoutManager mLayoutManager;
    public EatAdapter mAdapter;
    public List<EatModel> mModels;
    private String eat = "",smoke="",drink="",exercise="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert_eating);
        ButterKnife.bind(this);
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("修改饮食情况");
        tvSignUpGoBack.setText("取消");
        tvSignUpGoForward.setText("确定");
        data= (UserInfoBean) getIntent().getSerializableExtra("data");
        initView();
    }

    private void initView() {
        if(!TextUtils.isEmpty(data.eating_habits)){
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
        }
        if(!TextUtils.isEmpty(data.smoke)){
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
        }
        if(!TextUtils.isEmpty(data.drink)){
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
        }
        if(!TextUtils.isEmpty(data.exercise_habits)){
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
        }

        mLayoutManager = new GridLayoutManager(this, 3);
        mLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        rvSignUpContent.setLayoutManager(mLayoutManager);
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
        mModels = new ArrayList<>(6);
        mModels.add(new EatModel(getString(R.string.meat_collocation),
                R.drawable.ic_meat_collocation,
                R.color.colorSaltyPreference,
                R.drawable.bg_tv_salty_preference_selected,
                R.drawable.bg_tv_salty_preference));
        mModels.add(new EatModel(getString(R.string.meat_preference),
                R.drawable.ic_meat_preference,
                R.color.colorSaltyPreference,
                R.drawable.bg_tv_salty_preference_selected,
                R.drawable.bg_tv_salty_preference));
        mModels.add(new EatModel(getString(R.string.vegetatian_preference),
                R.drawable.ic_vegetarian_preference,
                R.color.colorSaltyPreference,
                R.drawable.bg_tv_salty_preference_selected,
                R.drawable.bg_tv_salty_preference));
        mModels.add(new EatModel(getString(R.string.salty_preference),
                R.drawable.ic_salty_preference,
                R.color.colorSaltyPreference,
                R.drawable.bg_tv_salty_preference_selected,
                R.drawable.bg_tv_salty_preference));
        mModels.add(new EatModel(getString(R.string.greasy_preferece),
                R.drawable.ic_greasy_preference,
                R.color.colorSaltyPreference,
                R.drawable.bg_tv_salty_preference_selected,
                R.drawable.bg_tv_salty_preference));
        mModels.add(new EatModel(getString(R.string.sweet_preferemce),
                R.drawable.ic_sweet_preference,
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
        if(positionSelected==-1){
            ToastUtil.showShort(this,"请选择其中一个");
            return;
        }
        NetworkApi.alertBasedata(MyApplication.getInstance().userId, data.height, data.weight, positionSelected+1+"", smoke, drink, exercise, new NetworkManager.SuccessCallback<Object>() {
            @Override
            public void onSuccess(Object response) {
                ToastUtils.show("修改成功");
                switch (positionSelected+1){
                    case 1:
                        speak("主人，您的饮食情况已经修改为"+"荤素搭配");
                        break;
                    case 2:
                        speak("主人，您的饮食情况已经修改为"+"偏好吃荤");
                        break;
                    case 3:
                        speak("主人，您的饮食情况已经修改为"+"偏好吃素");
                        break;
                    case 4:
                        speak("主人，您的饮食情况已经修改为"+"偏好吃咸");
                        break;
                    case 5:
                        speak("主人，您的饮食情况已经修改为"+"偏好油腻");
                        break;
                    case 6:
                        speak("主人，您的饮食情况已经修改为"+"偏好甜食");
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
