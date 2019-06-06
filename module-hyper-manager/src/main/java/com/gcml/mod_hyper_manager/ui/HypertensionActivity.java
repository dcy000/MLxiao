package com.gcml.mod_hyper_manager.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.gcml.common.data.AppManager;
import com.gcml.common.data.UserSpHelper;
import com.gcml.common.utils.UM;
import com.gcml.common.utils.base.ToolbarBaseActivity;
import com.gcml.common.utils.device.DeviceUtils;
import com.gcml.mod_hyper_manager.R;
import com.gcml.mod_hyper_manager.bean.PrimaryHypertensionBean;
import com.gcml.mod_hyper_manager.bean.PrimaryHypertensionQuestionnaireBean;
import com.gcml.mod_hyper_manager.net.HyperRepository;
import com.iflytek.synthetize.MLVoiceSynthetize;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DefaultObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * 高血压--->心血管风险评估
 */
public class HypertensionActivity extends ToolbarBaseActivity implements MultipleChoiceFragment.OnButtonClickListener {

    ViewPager vp;
    List<Fragment> fragments = new ArrayList<>();
    PrimaryHypertensionBean postBean = new PrimaryHypertensionBean();
    private List<PrimaryHypertensionQuestionnaireBean.DataBean.QuestionListBean> questionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_primary_hypertension);
        vp = findViewById(R.id.vp);
        initTitle();
        initVP();
        AppManager.getAppManager().addActivity(this);
    }

    private void initVP() {
        showLoading("正在加载...");
        new HyperRepository()
                .getHypertensionQuestion()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<PrimaryHypertensionQuestionnaireBean.DataBean>() {
                    @Override
                    public void onNext(PrimaryHypertensionQuestionnaireBean.DataBean dataBean) {
                        questionList = dataBean.questionList;
                        if (questionList != null && questionList.size() != 0) {
                            MLVoiceSynthetize.startSynthesize(UM.getApp(), "您" + questionList.get(0).questionName);
                            //给提交的数据赋值===开始
                            postBean.equipmentId = DeviceUtils.getIMEI();
                            postBean.hmQuestionnaireId = dataBean.hmQuestionnaireId;
                            postBean.hmQuestionnaireName = dataBean.questionnaireName;
                            postBean.userId = UserSpHelper.getUserId();
                            postBean.score = 0;
                            postBean.answerList = new ArrayList<>();
                            //给提交的数据赋值===结束
                            for (int i = 0; i < questionList.size(); i++) {
                                PrimaryHypertensionQuestionnaireBean.DataBean.QuestionListBean questionBean = questionList.get(i);
                                String questionType = questionBean.questionType;
                                if ("0".equals(questionType)) {
                                    MultipleChoiceFragment instance = MultipleChoiceFragment.getInstance(
                                            questionBean.questionName,
                                            "请认证阅读",
                                            questionBean,
                                            false);
                                    instance.setListener(HypertensionActivity.this);
                                    fragments.add(instance);
                                } else if ("1".equals(questionType)) {
                                    MultipleChoiceFragment instance = MultipleChoiceFragment.getInstance(
                                            questionBean.questionName,
                                            "请认证阅读",
                                            questionBean,
                                            true);
                                    instance.setListener(HypertensionActivity.this);
                                    fragments.add(instance);
                                }

                                //给提交的数据赋值==答案集合=开始
                                PrimaryHypertensionBean.AnswerListBean answerBean = new PrimaryHypertensionBean.AnswerListBean();
                                answerBean.questionName = questionBean.questionName;
                                answerBean.hmQuestionId = questionBean.hmQuestionId;
                                postBean.answerList.add(answerBean);
                                //给提交的数据赋值==答案集合=结束
                            }

                            onDataPrepared();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismissLoading();
                    }

                    @Override
                    public void onComplete() {
                        dismissLoading();
                    }
                });
    }

    private void onDataPrepared() {
        vp.setAdapter(new MlFragmentAdapter(getSupportFragmentManager(), fragments));
    }

    private void initTitle() {
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("健 康 调 查");
        mRightText.setVisibility(View.GONE);
//        mRightView.setImageResource(R.drawable.white_wifi_3);
//        mRightView.setOnClickListener(v -> startActivity(new Intent(HypertensionActivity.this, WifiConnectActivity.class)));
        mllBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (vp.getCurrentItem() == 0) {
                    finish();
                }
                vp.setCurrentItem(vp.getCurrentItem() - 1);
            }
        });
    }

    @Override
    public void onNextStep(int[] checked, PrimaryHypertensionQuestionnaireBean.DataBean.QuestionListBean answerBean) {
        //更新提交数据的bean
        List<PrimaryHypertensionBean.AnswerListBean> answerList = postBean.answerList;
        for (int i = 0; i < answerList.size(); i++) {
            if (answerBean.hmQuestionId.equals(answerList.get(i).hmQuestionId)) {
                answerList.get(i).answerName = getAnswerNames(answerBean, checked).toString()
                        .replaceAll("\\[", "")
                        .replaceAll("]", "");
                answerList.get(i).hmAnswerId = answerBean.answerList.get(checked[0]).hmAnswerId;
                answerList.get(i).score = getScore(answerBean, checked);//每个项目 得分之后
            }
        }
        //最后一页
        if (vp.getCurrentItem() + 1 == vp.getAdapter().getCount()) {
            //跳转逻辑
            for (int i = 0; i < answerList.size(); i++) {
                postBean.score += answerList.get(i).score;
            }
            postData();
            return;
        }
        vp.setCurrentItem(vp.getCurrentItem() + 1);
        MLVoiceSynthetize.startSynthesize(UM.getApp(), questionList.get(vp.getCurrentItem()).questionName);
    }

    private int getScore(PrimaryHypertensionQuestionnaireBean.DataBean.QuestionListBean answerBean, int[] checked) {
        int score = 0;
        for (int i = 0; i < checked.length; i++) {
            score += answerBean.answerList.get(checked[i]).answerScore;
        }
        return score;
    }

    private void postData() {
        new HyperRepository()
                .postHypertensionQuestion(UserSpHelper.getUserId(), postBean)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<Object>() {
                    @Override
                    public void onNext(Object o) {
                        startActivity(new Intent(HypertensionActivity.this, IsEmptyStomachOrNotActivity.class));
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private List<String> getAnswerNames(PrimaryHypertensionQuestionnaireBean.DataBean.QuestionListBean answerBean, int[] checked) {
        List<String> strings = new ArrayList<>();
        for (int i = 0; i < checked.length; i++) {
            strings.add(answerBean.answerList.get(checked[i]).answerInfo);
        }
        return strings;
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
