package com.example.han.referralproject.hypertensionmanagement.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.activity.WifiConnectActivity;
import com.example.han.referralproject.hypertensionmanagement.bean.PrimaryHypertensionBean;
import com.example.han.referralproject.hypertensionmanagement.bean.PrimaryHypertensionQuestionnaireBean;
import com.example.han.referralproject.hypertensionmanagement.fragment.MultipleChoiceFragment;
import com.example.han.referralproject.network.NetworkApi;
import com.google.gson.Gson;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 原发性高血压问卷
 */
public class PrimaryHypertensionActivity extends BaseActivity {

    @BindView(R.id.vp)
    ViewPager vp;
    List<Fragment> fragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_primary_hypertension);
        ButterKnife.bind(this);
        initTitle();
        initVP();

    }

    private void initVP() {
        showLoadingDialog("正在加载...");
        NetworkApi.getPrimaryHypertensionQuestion(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                String body = response.body();
                PrimaryHypertensionQuestionnaireBean bean = new Gson().fromJson(body, PrimaryHypertensionQuestionnaireBean.class);
                if (bean != null && bean.tag && bean.data != null) {
                    List<PrimaryHypertensionQuestionnaireBean.DataBean.QuestionListBean> questionList = bean.data.questionList;
                    if (questionList != null && questionList.size() != 0) {
                        for (int i = 0; i < questionList.size(); i++) {
                            PrimaryHypertensionQuestionnaireBean.DataBean.QuestionListBean questionBean = questionList.get(i);
                            String questionType = questionBean.questionType;
                            if ("0".equals(questionType)) {
                                fragments.add(MultipleChoiceFragment.getInstance(
                                        questionBean.questionName,
                                        "请认证阅读",
                                        getStrings(questionBean),
                                        false));
                            } else if ("1".equals(questionType)) {
                                fragments.add(MultipleChoiceFragment.getInstance(
                                        questionBean.questionName,
                                        "请认证阅读",
                                        getStrings(questionBean),
                                        true));
                            }

                        }

                        onDataPrepared();
                    }
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                hideLoadingDialog();
            }
        });

    }

    private void onDataPrepared() {
        vp.setAdapter(new MlFragmentAdapter(getSupportFragmentManager(), fragments));
    }

    private List<String> getStrings(PrimaryHypertensionQuestionnaireBean.DataBean.QuestionListBean questionBean) {
        List<String> strings = new ArrayList<>();
        for (int i = 0; i < questionBean.answerList.size(); i++) {
            strings.add(questionBean.answerList.get(i).answerInfo);
        }
        return strings;
    }

    private void initTitle() {
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("基 础 信 息 列 表");
        mRightText.setVisibility(View.GONE);
        mRightView.setImageResource(R.drawable.white_wifi_3);
        mRightView.setOnClickListener(v -> startActivity(new Intent(PrimaryHypertensionActivity.this, WifiConnectActivity.class)));
    }

    class MlFragmentAdapter extends FragmentPagerAdapter {

        List<Fragment> list;

        public MlFragmentAdapter(FragmentManager fm, List<Fragment> list) {
            super(fm);
            this.list = list;
        }

        @Override
        public Fragment getItem(int position) {
            return list.get(position);
        }

        @Override
        public int getCount() {
            return list.size();
        }
    }
}
