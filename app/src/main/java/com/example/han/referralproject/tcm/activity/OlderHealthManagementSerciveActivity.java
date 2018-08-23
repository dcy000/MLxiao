package com.example.han.referralproject.tcm.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.application.MyApplication;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.physicalexamination.adapter.FragAdapter;
import com.example.han.referralproject.tcm.bean.HealthManagementAnwserBean;
import com.example.han.referralproject.tcm.bean.HealthManagementResultBean;
import com.example.han.referralproject.tcm.bean.OlderHealthManagementBean;
import com.example.han.referralproject.tcm.fragment.HealthItemFragment;
import com.example.han.referralproject.tcm.wrap.MonitorViewPager;
import com.example.han.referralproject.util.LocalShared;
import com.gcml.lib_utils.display.ToastUtils;
import com.google.gson.Gson;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OlderHealthManagementSerciveActivity extends BaseActivity {

    @BindView(R.id.ll_operator)
    LinearLayout llOperator;
    private List<OlderHealthManagementBean.DataBean.QuestionListBean> questionList = new ArrayList<>();
    @BindView(R.id.vp)
    MonitorViewPager vp;
    @BindView(R.id.tv_previous_item)
    TextView tvPreviousItem;
    @BindView(R.id.tv_current_item)
    TextView tvCurrentItem;
    @BindView(R.id.tv_next_item)
    TextView tvNextItem;
    private int count;
    private String hmQuestionnaireId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_older_health_management_sercive);
        ButterKnife.bind(this);
        initTitle();
        initData();
    }

    private void initData() {
        showLoadingDialog("正在加载中...");
        NetworkApi.getHealthManagementForOlder(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                String body = response.body();
                OlderHealthManagementBean bean = new Gson().fromJson(body, OlderHealthManagementBean.class);
                if (bean != null && bean.tag) {
                    OlderHealthManagementBean.DataBean data = bean.data;
                    OlderHealthManagementSerciveActivity.this.hmQuestionnaireId = data.hmQuestionnaireId;
                    if (data != null) {
                        questionList = data.questionList;
                        if (questionList != null && questionList.size() != 0) {
                            count = questionList.size();
                            llOperator.setVisibility(View.VISIBLE);
                            initView();
                        }
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

    private ArrayList<Fragment> fragments;
    private int index;

    private void initView() {
        tvCurrentItem.setText(1 + "/" + count);

        fragments = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            fragments.add(HealthItemFragment.getInstance((i + 1) + "", questionList.get(i)));
        }
        vp.setOffscreenPageLimit(count - 1);
        FragAdapter adapter = new FragAdapter(getSupportFragmentManager(), fragments);
        vp.setAdapter(adapter);

        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                OlderHealthManagementSerciveActivity.this.index = position;
                tvCurrentItem.setText((index + 1) + "/" + count);

                if (isLastPager()) {
                    tvNextItem.setText("提交");
                } else {
                    tvNextItem.setText("下一题");
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initTitle() {
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("中医体质检测");
    }

    @OnClick({R.id.tv_previous_item, R.id.tv_next_item})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_previous_item:
                preCurrentPage();
                break;
            case R.id.tv_next_item:
                if (questionList == null) {
                    return;
                }
                if (!questionList.get(index).isSelected) {
                    ToastUtils.showShort("请选择答案");
                    return;
                }

                if (isLastPager()) {
                    submit();
                    return;
                }
                nextCurrentPage();
                break;
        }
    }

    private void submit() {
        showLoadingDialog("正在提交...");
        HealthManagementAnwserBean anwserBean = new HealthManagementAnwserBean();
        anwserBean.equipmentId = LocalShared.getInstance(this).getEqID();
        anwserBean.userId = MyApplication.getInstance().userId;
        anwserBean.hmQuestionnaireId = this.hmQuestionnaireId;
        anwserBean.answerList = new ArrayList<>();

        if (questionList != null && questionList.size() != 0) {

            for (int i = 0; i < questionList.size(); i++) {
                HealthManagementAnwserBean.AnswerListBean anwser = new HealthManagementAnwserBean.AnswerListBean();
                anwser.answerScore = questionList.get(i).answerScore;
                anwser.hmAnswerId = questionList.get(i).hmAnswerId;
                anwser.hmQuestionId = questionList.get(i).hmQuestionId;
                anwser.questionSeq = questionList.get(i).questionSeq;

                anwserBean.answerList.add(anwser);

            }
        }

        String anwserJson = new Gson().toJson(anwserBean);
        NetworkApi.postHealthManagementAnwser(anwserJson, new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                HealthManagementResultBean resultBean = new Gson().fromJson(response.body(), HealthManagementResultBean.class);
                if (resultBean != null && resultBean.tag) {
                    ToastUtils.showShort("提交成功");
                    gotoResultPage(resultBean.data);
                } else {
                    ToastUtils.showShort(resultBean.message);
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                hideLoadingDialog();
            }
        });


    }

    /**
     * 跳转到结果页面
     *
     * @param data
     */
    private void gotoResultPage(List<HealthManagementResultBean.DataBean> data) {
        startActivity(new Intent(this, HealthManagementResultActivity.class).putExtra("result_data", (Serializable) data));
        finish();
    }

    private boolean isLastPager() {
        return index == count - 1;
    }

    public void nextCurrentPage() {
        vp.setCurrentItem(index + 1, true);
    }

    public void preCurrentPage() {
        vp.setCurrentItem(index - 1, true);
    }


}
