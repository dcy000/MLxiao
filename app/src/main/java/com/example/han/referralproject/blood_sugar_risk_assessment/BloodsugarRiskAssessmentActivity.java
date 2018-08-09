package com.example.han.referralproject.blood_sugar_risk_assessment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.application.MyApplication;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.util.LocalShared;
import com.gcml.lib_utils.display.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/5/9.
 */

public class BloodsugarRiskAssessmentActivity extends BaseActivity implements View.OnClickListener {
    private RecyclerView mBloodsugarRiskList;
    private List<BloodSugarRisk> mData;
    private static final String title = "本次风险评估需要您先回答<font color='#FF2D2D'>8</font>道题目，大约<font color='#FF2D2D'>2</font>分钟";
    private BloodsugarRiskAdapter adapter;
    private String hmQuestionnaireId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_sugar_risk_assessment);
        speak("主人，为了您的健康。小依为您准备了八道血糖相关的题目，请您耐心填写");
        initView();
        setAdapter();
        getData();
    }

    private void getData() {
        String sex = LocalShared.getInstance(this).getSex();
        int type = 0;
        if (!TextUtils.isEmpty(sex) && sex.equals("女")) {
            type = 1;
        }
        OkGo.<String>get(NetworkApi.Bloodsugar_Detection)
                .params("type", type)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            JSONObject object = new JSONObject(response.body());
                            if (object.optInt("code") == 200) {
                                JSONObject data = object.optJSONObject("data");
                                hmQuestionnaireId = data.optString("hmQuestionnaireId");
                                JSONArray questionList = data.optJSONArray("questionList");
                                List<BloodSugarRisk> bloodSugarRisks = new Gson().fromJson(questionList.toString(),
                                        new TypeToken<List<BloodSugarRisk>>() {
                                        }.getType());
                                if (bloodSugarRisks != null) {
                                    mData.addAll(bloodSugarRisks);
                                    for (BloodSugarRisk bloodSugarRisk : mData) {
                                        if (bloodSugarRisk.getQuestionName().contains("直系")) {
                                            bloodSugarRisk.setItemType(BloodSugarRisk.THREE_BUTTON_VERTICAL);
                                        }
                                        if (bloodSugarRisk.getAnswerList().size() == 4) {
                                            bloodSugarRisk.setItemType(BloodSugarRisk.FOUR_BUTTON);
                                        }
                                        if (bloodSugarRisk.getAnswerList().size() == 3 && !bloodSugarRisk.getQuestionName().contains("直系")) {
                                            bloodSugarRisk.setItemType(BloodSugarRisk.THREE_BUTTON_HORIZONTAL);
                                        }
                                        if (bloodSugarRisk.getAnswerList().size() == 2) {
                                            bloodSugarRisk.setItemType(BloodSugarRisk.TWO_BUTTON);
                                        }
                                    }
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {

                    }
                });
    }

    private void setAdapter() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mBloodsugarRiskList.setLayoutManager(manager);
        adapter = new BloodsugarRiskAdapter(mData);
        View inflate = LayoutInflater.from(this).inflate(R.layout.blood_sugar_risk_head, null, false);
        ((TextView) inflate.findViewById(R.id.content)).setText(Html.fromHtml(title));
        adapter.addHeaderView(inflate);
        View inflate1 = LayoutInflater.from(this).inflate(R.layout.blood_sugar_risk_foot, null, false);
        inflate1.findViewById(R.id.blood_sugar_submit).setOnClickListener(this);
        adapter.addFooterView(inflate1);
        mBloodsugarRiskList.setAdapter(adapter);
    }

    private void initView() {
        mData = new ArrayList<>();
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("血糖风险评估");
        mBloodsugarRiskList = findViewById(R.id.bloodsugar_risk_list);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.blood_sugar_submit:
                for (BloodSugarRisk bloodSugarRisk : mData) {
                    if (!bloodSugarRisk.isChoosed()) {
                        ToastUtils.showShort("主人，您还有未回答的题目");
                        speak("主人，您还有未回答的题目");
                        return;
                    }
                }

                int score = 0;
                ArrayList<PostBloodSugarRisk.AnswerListBean> answerListBeans = new ArrayList<>();
                for (BloodSugarRisk bloodSugarRisk : adapter.getData()) {
                    BloodSugarRisk.AnswerListBean answerListBean1 = bloodSugarRisk.getAnswerList().get(bloodSugarRisk.getChoosedPosition());
                    score += answerListBean1.getAnswerScore();
                    PostBloodSugarRisk.AnswerListBean answerListBean = new PostBloodSugarRisk.AnswerListBean();
                    answerListBean.setHmAnswerId(answerListBean1.getHmAnswerId());
                    answerListBean.setHmQuestionId(answerListBean1.getHmQuestionId());
                    answerListBeans.add(answerListBean);
                }
                PostBloodSugarRisk postBloodSugarRisk = new PostBloodSugarRisk();
                postBloodSugarRisk.setScore(score);
                postBloodSugarRisk.setEquipmentId(LocalShared.getInstance(MyApplication.getInstance()).getEqID());
                postBloodSugarRisk.setHmQuestionnaireAssessId("");
                postBloodSugarRisk.setHmQuestionnaireId(hmQuestionnaireId);
                String userId = MyApplication.getInstance().userId;
                if (!TextUtils.isEmpty(userId)) {
                    postBloodSugarRisk.setUserId(Integer.parseInt(userId));
                }
                postBloodSugarRisk.setAnswerList(answerListBeans);
                String toJson = new Gson().toJson(postBloodSugarRisk);
                OkGo.<String>post(NetworkApi.Bloodsugar_Detection)
                        .upJson(toJson)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                try {
                                    JSONObject object = new JSONObject(response.body());
                                    if (object.optInt("code") == 200) {
                                        JSONObject data = object.optJSONObject("data");
                                        PostBloodSugarResult postBloodSugarResult = new Gson().fromJson(data.toString(), PostBloodSugarResult.class);
                                        startActivity(new Intent(BloodsugarRiskAssessmentActivity.this, BloodsugarRiskAssessmentResultActivity.class)
                                                .putExtra("data", postBloodSugarResult));
                                        finish();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onError(Response<String> response) {
                                ToastUtils.showShort("上传数据失败");
                                finish();
                            }
                        });
                break;
        }
    }
}
