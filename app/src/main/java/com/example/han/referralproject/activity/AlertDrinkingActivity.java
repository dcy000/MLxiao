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
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.example.han.referralproject.util.ToastTool;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.medlink.danbogh.register.EatAdapter;
import com.medlink.danbogh.register.EatModel;

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
    private StringBuffer buffer;
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
        buffer=new StringBuffer();
        initView();
    }
    private void initView() {
        if(!TextUtils.isEmpty(data.eatingHabits)){
            switch (data.eatingHabits){
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
        if(!TextUtils.isEmpty(data.exerciseHabits)){
            switch (data.exerciseHabits){
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
        if("尚未填写".equals(data.mh)){
            buffer=null;
        }else{
            String[] mhs=data.mh.split("\\s+");
            for (int i=0;i<mhs.length;i++){
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
        if(positionSelected==-1){
            ToastTool.showShort("请选择其中一个");
            return;
        }
        NetworkApi.alertBasedata(MyApplication.getInstance().userId, data.height, data.weight, eat, smoke, positionSelected+1+"",exercise,
                buffer==null?"":buffer.substring(0,buffer.length()-1),data.dz, new NetworkManager.SuccessCallback<Object>() {
            @Override
            public void onSuccess(Object response) {
                ToastTool.showShort("修改成功");
                switch (positionSelected+1){
                    case 1:
                        MLVoiceSynthetize.startSynthesize("主人，您的饮酒情况已经修改为"+"经常喝酒");
                        break;
                    case 2:
                        MLVoiceSynthetize.startSynthesize("主人，您的饮酒情况已经修改为"+"偶尔喝酒");
                        break;
                    case 3:
                        MLVoiceSynthetize.startSynthesize("主人，您的饮酒情况已经修改为"+"从不喝酒");
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
