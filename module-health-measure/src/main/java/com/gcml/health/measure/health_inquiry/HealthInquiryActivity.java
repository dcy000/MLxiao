package com.gcml.health.measure.health_inquiry;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.FrameLayout;

import com.gcml.common.data.UserSpHelper;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.UM;
import com.gcml.common.utils.base.ToolbarBaseActivity;
import com.gcml.common.utils.device.DeviceUtils;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.common.widget.dialog.AlertDialog;
import com.gcml.health.measure.BuildConfig;
import com.gcml.health.measure.R;
import com.gcml.health.measure.first_diagnosis.FirstDiagnosisActivity;
import com.gcml.health.measure.first_diagnosis.fragment.HealthFirstTipsFragment;
import com.gcml.health.measure.health_inquiry.bean.HealthInquiryBean;
import com.gcml.health.measure.health_inquiry.bean.HealthInquiryPostBean;
import com.gcml.health.measure.network.HealthMeasureRepository;
import com.gcml.module_blutooth_devices.base.FragmentChanged;
import com.iflytek.synthetize.MLVoiceSynthetize;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DefaultObserver;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/8/28 16:31
 * created by:gzq
 * description:健康调查问卷
 */
public class HealthInquiryActivity extends ToolbarBaseActivity implements FragmentChanged {
    private FrameLayout mFrame;
    public static final String KEY_BUNDLE = "key_bundle";
    public static final String KEY_PAGEINDEX = "key_page_index";
    private int pageIndex = 0;
    private HealthInquiryBean healthInquiryBean;
    private static List<HealthInquiryBean.QuestionListBean> cacheDatas = new ArrayList<>();
    private String userId = "";
//    private DialogSureCancel mSureCancel;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, HealthInquiryActivity.class);
        if (!(context instanceof Activity)) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    public void addHealthInquiryBean(HealthInquiryBean.QuestionListBean cache) {
        if (cacheDatas == null) {
            cacheDatas = new ArrayList<>();
        }
        cacheDatas.add(cache);
    }

    @Override
    protected void backLastActivity() {
        if (--pageIndex > 0) {
            replaceFragment(healthInquiryBean.getQuestionList().get(pageIndex - 1), pageIndex - 1);
            int size = cacheDatas.size();
            if (size > 0) {
                cacheDatas.remove(size - 1);
            }
        } else {
            finish();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.health_measure_activity_inquiry);
        initView();
        getData();
    }

    @SuppressLint("CheckResult")
    private void getData() {
        HealthMeasureRepository.getHealthInquiryQuestions()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribeWith(new DefaultObserver<HealthInquiryBean>() {
                    @Override
                    public void onNext(HealthInquiryBean healthInquiryBeans) {
                        if (healthInquiryBeans != null) {
                            HealthInquiryActivity.this.healthInquiryBean = healthInquiryBeans;
                            //如果已经做过风险评估则不需要引导页
//                            if (UserSpHelper.getRiskAssessmentState()) {
                            if (true) {
                                List<HealthInquiryBean.QuestionListBean> questionList = healthInquiryBean.getQuestionList();
                                if (questionList != null && questionList.size() > pageIndex) {
                                    replaceFragment(questionList.get(pageIndex), pageIndex++);
                                }
                            } else {
                                addFirstTipFragment();
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (!BuildConfig.DEBUG) {
                            ToastUtils.showShort("获取数据失败");
                        } else {
                            ToastUtils.showShort("获取数据失败:" + e.getMessage());
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    private void addFirstTipFragment() {
        mToolbar.setVisibility(View.GONE);
        HealthFirstTipsFragment firstTipsFragment = new HealthFirstTipsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title", "初次见面，我是小E！为了更好地了解您的身体，先来做一个全套体检吧!");
        firstTipsFragment.setArguments(bundle);
        firstTipsFragment.setOnFragmentChangedListener(this);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame, firstTipsFragment).commit();
    }

    private void replaceFragment(HealthInquiryBean.QuestionListBean questionListBean, int pageIndex) {
        HealthInquiryFragment fragment = new HealthInquiryFragment();
        fragment.setOnFragmentChangedListener(this);
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_BUNDLE, questionListBean);
        bundle.putInt(KEY_PAGEINDEX, pageIndex);
        fragment.setArguments(bundle);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame, fragment)
                .commit();
        mToolbar.setVisibility(View.VISIBLE);
        //播报语音
        Timber.d(questionListBean.getQuestionName());
        MLVoiceSynthetize.startSynthesize(UM.getApp(), "" + questionListBean.getQuestionName(), false);
    }

    private void initView() {
        mFrame = (FrameLayout) findViewById(R.id.frame);
        mTitleText.setText("健 康 调 查");
        userId = UserSpHelper.getUserId();
    }

    @Override
    protected void backMainActivity() {
        showExitDialog();
    }

    private void showExitDialog() {
//        if (mSureCancel != null) {
//            mSureCancel.dismiss();
//        }
//        mSureCancel = new DialogSureCancel(this);
//        mSureCancel.setContent("您已经开始做题，是否要离开当前页面");
//        mSureCancel.getSureView().setTextColor(Color.parseColor("#BBBBBB"));
//        mSureCancel.getSureView().setText("确认离开");
//        mSureCancel.getCancelView().setTextColor(getResources().getColor(R.color.color_3F86FC));
//        mSureCancel.getCancelView().setText("继续做题");
//        mSureCancel.show();
//        mSureCancel.setOnClickCancelListener(null);
//        mSureCancel.setOnClickSureListener(new DialogClickSureListener() {
//            @Override
//            public void clickSure(BaseDialog dialog) {
//                //TODO:进入MainActivity
//                CCAppActions.jump2MainActivity();
//            }
//        });

        new AlertDialog(this)
                .builder()
                .setMsg("您已经开始做题，是否要离开当前页面")
                .setNegativeButton("确认离开", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        CCAppActions.jump2MainActivity();
                        HealthInquiryActivity.super.backMainActivity();
                    }
                }).setPositiveButton("继续做题", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        }).show();
    }

    @SuppressLint("CheckResult")
    @Override
    public void onFragmentChanged(Fragment fragment, Bundle bundle) {

        if (healthInquiryBean != null) {
            if (pageIndex < 6) {
                replaceFragment(healthInquiryBean.getQuestionList().get(pageIndex), pageIndex++);
            } else {
                //六道题全部做完，上传结果
                HealthInquiryPostBean postBean = new HealthInquiryPostBean();
                postBean.setEquipmentId(DeviceUtils.getIMEI());
                postBean.setHmQuestionnaireId(healthInquiryBean.getHmQuestionnaireId());
                postBean.setHmQuestionnaireName(healthInquiryBean.getQuestionnaireName());
                postBean.setUserId(userId);
                List<HealthInquiryPostBean.AnswerListBean> answerListBeans = new ArrayList<>();
                int sum = 0;
                for (HealthInquiryBean.QuestionListBean bean : cacheDatas) {
                    for (HealthInquiryBean.QuestionListBean.AnswerListBean listBean : bean.getAnswerList()) {
                        if (listBean.getChoosed()) {
                            HealthInquiryPostBean.AnswerListBean answerListBean = new HealthInquiryPostBean.AnswerListBean();
                            sum += listBean.getAnswerScore();
                            answerListBean.setAnswerName(listBean.getAnswerInfo());
                            answerListBean.setHmAnswerId(listBean.getHmAnswerId());
                            answerListBean.setHmQuestionId(listBean.getHmQuestionId());
                            answerListBean.setQuestionName(bean.getQuestionName());
                            answerListBean.setQuestionSeq(bean.getSeq());
                            answerListBean.setScore(listBean.getAnswerScore());
                            answerListBeans.add(answerListBean);
                        }
                    }
                }
                postBean.setAnswerList(answerListBeans);
                postBean.setScore(sum);

                HealthMeasureRepository.postHealthInquiryAnswers(userId, postBean)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .as(RxUtils.autoDisposeConverter(this))
                        .subscribeWith(new DefaultObserver<Object>() {
                            @Override
                            public void onNext(Object o) {
                                //问题回答结束就算是做过风险评估了 下次再进入就不需要引导页了 为了保证唯一性，需要和userId进行绑定
                                UserSpHelper.setRiskAssessmentState(true);
                                ToastUtils.showShort("上传数据成功");
                                FirstDiagnosisActivity.startActivity(HealthInquiryActivity.this);
                                finish();
                            }

                            @Override
                            public void onError(Throwable e) {
                                ToastUtils.showShort("上传数据失败:" + e.getMessage());
                            }

                            @Override
                            public void onComplete() {

                            }
                        });
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        MLVoiceSynthetize.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cacheDatas = null;
//        if (mSureCancel != null) {
//            mSureCancel.dismiss();
//        }
    }


}
