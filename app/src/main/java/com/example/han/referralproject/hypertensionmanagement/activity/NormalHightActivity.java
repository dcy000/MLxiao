package com.example.han.referralproject.hypertensionmanagement.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.hypertensionmanagement.bean.PrimaryHypertensionBean;
import com.example.han.referralproject.hypertensionmanagement.bean.PrimaryHypertensionQuestionnaireBean;
import com.example.han.referralproject.hypertensionmanagement.fragment.MultipleChoiceFragment;
import com.gcml.common.data.AppManager;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.util.LocalShared;
import com.example.han.referralproject.util.Utils;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.common.widget.dialog.AlertDialog;
import com.google.gson.Gson;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.sjtu.yifei.annotation.Route;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 正常高值--->高血压评估
 */
@Route(path = "/app/hypertension/management/normal/hight")
public class NormalHightActivity extends BaseActivity implements MultipleChoiceFragment.OnButtonClickListener {

    @BindView(R.id.vp)
    ViewPager vp;
    List<Fragment> fragments = new ArrayList<>();
    PrimaryHypertensionBean postBean = new PrimaryHypertensionBean();
    private List<PrimaryHypertensionQuestionnaireBean.DataBean.QuestionListBean> questionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_primary_hypertension);
        ButterKnife.bind(this);
        initTitle();
        initVP();
        AppManager.getAppManager().addActivity(this);
    }

    private void initVP() {
        showLoadingDialog("正在加载...");
        NetworkApi.getNormalHightQuestion(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                String body = response.body();
                PrimaryHypertensionQuestionnaireBean bean = new Gson().fromJson(body, PrimaryHypertensionQuestionnaireBean.class);
                if (bean != null && bean.tag && bean.data != null) {
                    questionList = bean.data.questionList;
                    if (questionList != null && questionList.size() != 0) {
                        mlSpeak(questionList.get(0).questionName);
                        //给提交的数据赋值===开始
                        postBean.equipmentId = Utils.getDeviceId();
                        postBean.hmQuestionnaireId = bean.data.hmQuestionnaireId;
                        postBean.hmQuestionnaireName = bean.data.questionnaireName;
                        postBean.userId = LocalShared.getInstance(NormalHightActivity.this).getUserId();
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
        mlSpeak(questionList.get(vp.getCurrentItem()).questionName);
    }


    private void postData() {
        NetworkApi.postNormalHightQuestion(new Gson().toJson(postBean), LocalShared.getInstance(this).getUserId() + "", new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                String body = response.body();
                try {
                    JSONObject object = new JSONObject(body);
                    if (object.getBoolean("tag")) {
                        showResultDialog(object.getString("data"));
                    } else {
                        ToastUtils.showShort(object.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
                        startActivity(new Intent(NormalHightActivity.this, DetecteTipActivity.class)
                                .putExtra("fromWhere", "3"));
                    }
                }).show();
    }


}
