package com.example.han.referralproject.hypertensionmanagement.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.activity.WifiConnectActivity;
import com.example.han.referralproject.hypertensionmanagement.fragment.MultipleChoiceFragment;
import com.example.han.referralproject.hypertensionmanagement.fragment.MultipleChoiceStringFragment;
import com.example.han.referralproject.hypertensionmanagement.fragment.WarmNoticeFragment;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HasDiseaseOrNotActivity extends BaseActivity implements MultipleChoiceStringFragment.OnButtonClickListener {

    @BindView(R.id.fl_container)
    FrameLayout flContainer;
    public static final String CONTENT = " 您是否已经存在以下疾病：肾脏疾病、内分泌疾病、心血管疾病、颅脑病变？";
    String[] itmes = {"是", "否"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_has_disease_or_not);
        ButterKnife.bind(this);
        initTitle();
        initView();
    }

    private void initView() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        MultipleChoiceStringFragment fragment = MultipleChoiceStringFragment
                .getInstance(CONTENT, "",
                        Arrays.asList("是", "否")
                        , true);
        fragment.setListener(this);
        transaction.replace(R.id.fl_container, fragment).commitAllowingStateLoss();
    }

    private void initTitle() {
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("基 础 信 息 列 表");
        mRightText.setVisibility(View.GONE);
        mRightView.setImageResource(R.drawable.white_wifi_3);
        mRightView.setOnClickListener(v -> startActivity(new Intent(HasDiseaseOrNotActivity.this, WifiConnectActivity.class)));
    }

    @Override
    public void onNextStep(int[] checked) {
        if ("是".equals(itmes[checked[0]])) {
            //给方案
            // TODO: 2018/7/26  
        } else {
            //跳转问卷
            startActivity(new Intent(this, HypertensionActivity.class));
        }

    }
}
