package com.gcml.module_hypertension_manager.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;

import com.gcml.common.data.AppManager;
import com.gcml.common.data.UserSpHelper;
import com.gcml.common.router.AppRouter;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.UM;
import com.gcml.common.utils.base.ToolbarBaseActivity;
import com.gcml.common.utils.device.DeviceUtils;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.common.widget.dialog.AlertDialog;
import com.gcml.module_hypertension_manager.R;
import com.gcml.module_hypertension_manager.bean.PrimaryHypertensionBean;
import com.gcml.module_hypertension_manager.bean.PrimaryHypertensionQuestionnaireBean;
import com.gcml.module_hypertension_manager.net.HyperRepository;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.sjtu.yifei.annotation.Route;
import com.sjtu.yifei.route.Routerfit;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DefaultObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * 正常高值--->高血压评估
 */
@Route(path = "/app/hypertension/management/normal/hight")
public class NormalHightActivity extends ToolbarBaseActivity implements MultipleChoiceFragment.OnButtonClickListener {

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
                .getNormalHightQuestion()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new DefaultObserver<PrimaryHypertensionQuestionnaireBean.DataBean>() {
                    @Override
                    public void onNext(PrimaryHypertensionQuestionnaireBean.DataBean dataBean) {
                        questionList = dataBean.questionList;
                        if (questionList != null && questionList.size() != 0) {
                            MLVoiceSynthetize.startSynthesize(UM.getApp(), questionList.get(0).questionName);
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
                                    instance.setListener(NormalHightActivity.this);
                                    fragments.add(instance);
                                } else if ("1".equals(questionType)) {
                                    MultipleChoiceFragment instance = MultipleChoiceFragment.getInstance(
                                            questionBean.questionName,
                                            "请认证阅读",
                                            questionBean,
                                            true);
                                    instance.setListener(NormalHightActivity.this);
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
//        mRightView.setOnClickListener(v -> startActivity(new Intent(NormalHightActivity.this, WifiConnectActivity.class)));
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
            //算总分
            for (int i = 0; i < answerList.size(); i++) {
                postBean.score += answerList.get(i).score;
            }
            postData();
            return;
        }
        vp.setCurrentItem(vp.getCurrentItem() + 1);
        MLVoiceSynthetize.startSynthesize(UM.getApp(), questionList.get(vp.getCurrentItem()).questionName);
    }


    private void postData() {
        new HyperRepository()
                .postNormalHightQuestion(UserSpHelper.getUserId(), postBean)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new DefaultObserver<String>() {
                    @Override
                    public void onNext(String s) {
                        showResultDialog(s);
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.showShort(e.getMessage());
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

    /**
     * 选题的得分
     *
     * @param answerBean
     * @param checked
     * @return
     */
    private int getScore(PrimaryHypertensionQuestionnaireBean.DataBean.QuestionListBean answerBean, int[] checked) {
        int score = 0;
        for (int i = 0; i < checked.length; i++) {
            score += answerBean.answerList.get(checked[i]).answerScore;
        }
        return score;
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

    private void showResultDialog(String content) {
//        DialogSure sure = new DialogSure(this);
//        sure.setContent(content);
//        sure.setOnClickSureListener(dialog -> {
//            String fromWhere = getIntent().getStringExtra("fromWhere");
//            if (!TextUtils.isEmpty(fromWhere)) {
//                if (fromWhere.equals("NewMeasureBloodpressureResultActivity")) {
//                    finish();
//                    return;
//                }
//            }
////            startActivity(new Intent(NormalHightActivity.this, WeightMeasureActivity.class));
//            CC.obtainBuilder("health_measure")
//                    .setActionName("To_WeightManagerActivity")
//                    .build().callAsyncCallbackOnMainThread(new IComponentCallback() {
//                @Override
//                public void onResult(CC cc, CCResult result) {
//                    startActivity(new Intent(NormalHightActivity.this, TreatmentPlanActivity.class));
//                }
//            });
//
//            sure.dismiss();
//        });
//        sure.show();

        new AlertDialog(this)
                .builder()
                .setMsg(content)
                .setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String fromWhere = getIntent().getStringExtra("fromWhere");
                        if (!TextUtils.isEmpty(fromWhere)) {
                            if (fromWhere.equals("NewMeasureBloodpressureResultActivity")) {
                                finish();
                                return;
                            }
                        }
//            startActivity(new Intent(NormalHightActivity.this, WeightMeasureActivity.class));
//                        CC.obtainBuilder("health_measure")
//                                .setActionName("To_WeightManagerActivity")
//                                .build().callAsyncCallbackOnMainThread(new IComponentCallback() {
//                            @Override
//                            public void onResult(CC cc, CCResult result) {
//                                startActivity(new Intent(NormalHightActivity.this, TreatmentPlanActivity.class));
//                            }
//                        });
                        Routerfit.register(AppRouter.class).skipDetecteTipActivity("3");
                    }
                }).show();
    }


}
