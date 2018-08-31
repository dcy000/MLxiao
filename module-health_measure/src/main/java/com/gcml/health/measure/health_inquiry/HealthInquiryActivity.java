package com.gcml.health.measure.health_inquiry;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.FrameLayout;

import com.gcml.common.utils.RxUtils;
import com.gcml.health.measure.BuildConfig;
import com.gcml.health.measure.R;
import com.gcml.health.measure.cc.CCAppActions;
import com.gcml.health.measure.first_diagnosis.FirstDiagnosisActivity;
import com.gcml.health.measure.first_diagnosis.fragment.HealthFirstTipsFragment;
import com.gcml.health.measure.health_inquiry.bean.HealthInquiryBean;
import com.gcml.health.measure.health_inquiry.bean.HealthInquiryPostBean;
import com.gcml.health.measure.manifest.HealthMeasureSPManifest;
import com.gcml.health.measure.network.HealthMeasureRepository;
import com.gcml.lib_utils.base.ToolbarBaseActivity;
import com.gcml.lib_utils.device.DeviceUtils;
import com.gcml.lib_utils.display.ToastUtils;
import com.gcml.lib_utils.ui.dialog.BaseDialog;
import com.gcml.lib_utils.ui.dialog.DialogClickSureListener;
import com.gcml.lib_utils.ui.dialog.DialogSureCancel;
import com.gcml.module_blutooth_devices.base.FragmentChanged;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.lzy.okgo.OkGo;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DefaultObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.http.PATCH;
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
    private String userId = "100206";

    public void addHealthInquiryBean(HealthInquiryBean.QuestionListBean cache) {
        if (cacheDatas == null) {
            cacheDatas = new ArrayList<>();
        }
        cacheDatas.add(cache);
    }

    @Override
    protected void backLastActivity() {
        // 获取当前回退栈中的Fragment个数
        int backStackEntryCount = getSupportFragmentManager().getBackStackEntryCount();
        // 判断当前回退栈中的fragment个数,
        if (backStackEntryCount > 1) {
            // 立即回退一步,并且把缓存的数据清除
            getSupportFragmentManager().popBackStackImmediate();

            int size = cacheDatas.size();
            if (size > 0) {
                cacheDatas.remove(size - 1);
            }
        } else {
            //回退栈中只剩一个时,退出应用
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
                            addFirstTipFragment();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (BuildConfig.DEBUG) {
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
        bundle.putString("title", "主人，做一个风险评估吧！");
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
                .addToBackStack(null)
                .commit();
        mToolbar.setVisibility(View.VISIBLE);
        //播报语音
        Timber.d(questionListBean.getQuestionName());
        MLVoiceSynthetize.startSynthesize(this,"主人，"+questionListBean.getQuestionName(),false);
    }

    private void initView() {
        mFrame = (FrameLayout) findViewById(R.id.frame);
        mTitleText.setText("健 康 调 查");
        userId = HealthMeasureSPManifest.getUserId();
    }

    @Override
    protected void backMainActivity() {
        showExitDialog();
    }

    private void showExitDialog() {
        DialogSureCancel sureCancel = new DialogSureCancel(this);
        sureCancel.setContent("您已经开始做题，是否要离开当前页面");
        sureCancel.getSureView().setTextColor(Color.parseColor("#BBBBBB"));
        sureCancel.getSureView().setText("确认离开");
        sureCancel.getCancelView().setTextColor(getResources().getColor(R.color.color_3F86FC));
        sureCancel.getCancelView().setText("继续做题");
        sureCancel.show();
        sureCancel.setOnClickCancelListener(null);
        sureCancel.setOnClickSureListener(new DialogClickSureListener() {
            @Override
            public void clickSure(BaseDialog dialog) {
                //TODO:进入MainActivity
                CCAppActions.jump2MainActivity();
            }
        });
    }

    @SuppressLint("CheckResult")
    @Override
    public void onFragmentChanged(Fragment fragment, Bundle bundle) {

        if (healthInquiryBean != null) {
            if (pageIndex < 6) {
                replaceFragment(healthInquiryBean.getQuestionList().get(pageIndex), pageIndex++);
            } else {
                //TODO：六道题全部做完，上传结果
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
                                ToastUtils.showShort("上传数据成功");
                                FirstDiagnosisActivity.startActivity(HealthInquiryActivity.this);
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
    protected void onDestroy() {
        super.onDestroy();
        cacheDatas = null;
    }


}
