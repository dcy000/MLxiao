package com.example.han.referralproject.risk_assessment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.application.MyApplication;
import com.example.han.referralproject.bean.HypertensionDetection;
import com.example.han.referralproject.bean.QuestionChoosed;
import com.example.han.referralproject.network.NetworkApi;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Administrator on 2018/5/4.
 */

public class RiskActivity extends BaseActivity implements IFragmentControl {

    private FrameLayout mRiskFrame;
    private RiskFragment1 riskFragment1;
    private RiskFragment2 riskFragment2;
    private RiskFragment3 riskFragment3;
    private int fragmentPosition = 0;
    private PostQuestions postQuestions;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_risk);
        initView();
        getData();
    }

    private void getData() {
        OkGo.<String>get(NetworkApi.Hypertension_Detection)
                .params("key", "va")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            JSONObject object = new JSONObject(response.body());
                            if (object.optInt("code") == 200) {
                                JSONObject data = object.optJSONObject("data");
                                dealData(data.toString());
                            } else if (object.optInt("code") == 0) {
                                dealData(object.toString());
                            }
                        } catch (JSONException e) {
                            Log.e("JSONException", "onSuccess: " + "进入异常");
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        Log.e("获取失败", "onError: " + response.message());
                        dealTest();
                    }
                });
    }

    private void dealTest() {
        riskFragment1 = new RiskFragment1();
        riskFragment2 = new RiskFragment2();
        riskFragment3 = new RiskFragment3();
        riskFragment1.setOnRiskFragment1Listener(this);
        riskFragment2.setOnRiskFragment2Listener(this);
        riskFragment3.setOnRiskFragment3Listener(this);
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.fragment_slide_right_enter,R.anim.fragment_slide_right_exit)
                .add(R.id.risk_frame, riskFragment1).commit();
        fragmentPosition = 1;
    }

    private void dealData(String data) {
        HypertensionDetection hypertensionDetection = new Gson().fromJson(data, HypertensionDetection.class);
        riskFragment1 = new RiskFragment1();
        riskFragment2 = new RiskFragment2();
        riskFragment3 = new RiskFragment3();
        riskFragment1.setOnRiskFragment1Listener(this);
        riskFragment2.setOnRiskFragment2Listener(this);
        riskFragment3.setOnRiskFragment3Listener(this);
        Bundle bundle = new Bundle();
        bundle.putSerializable("list", hypertensionDetection.getPrimary().getList());
        riskFragment1.setArguments(bundle);
        Bundle bundle1 = new Bundle();
        bundle1.putSerializable("list", hypertensionDetection.getSecondary().getList());
        riskFragment2.setArguments(bundle1);
        Bundle bundle2 = new Bundle();
        bundle2.putSerializable("list", hypertensionDetection.getReceptor().getList());
        riskFragment3.setArguments(bundle2);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.risk_frame, riskFragment1).commit();

        fragmentPosition = 1;
    }


    private void initView() {
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("血压风险评估");
        mRiskFrame = (FrameLayout) findViewById(R.id.risk_frame);
        postQuestions = new PostQuestions();
    }

    @Override
    public void stepNext(Fragment fragment, List<QuestionChoosed> lists) {
        if (fragment instanceof RiskFragment1) {
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.fragment_slide_right_enter,R.anim.fragment_slide_right_exit)
                    .replace(R.id.risk_frame, riskFragment2).commit();
            fragmentPosition = 2;
            if (lists.get(0).isChoosed()) {
                postQuestions.setGenetic("1");
            } else {
                postQuestions.setGenetic("0");
            }
            if (lists.get(1).isChoosed()) {
                postQuestions.setDrinkWine("1");
            } else {
                postQuestions.setDrinkWine("0");
            }
            if (lists.get(2).isChoosed()) {
                postQuestions.setMentalStress("1");
            } else {
                postQuestions.setMentalStress("0");
            }
            if (lists.get(3).isChoosed()) {
                postQuestions.setNaSalt("1");
            } else {
                postQuestions.setNaSalt("0");
            }
            if (lists.get(4).isChoosed()) {
                postQuestions.setSport("1");
            } else {
                postQuestions.setSport("0");
            }
        } else if (fragment instanceof RiskFragment2) {
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.fragment_slide_right_enter,R.anim.fragment_slide_right_exit)
                    .replace(R.id.risk_frame, riskFragment3).commit();
            fragmentPosition = 3;
            if (lists.get(0).isChoosed()) {
                postQuestions.setRenalIllness("1");
            } else {
                postQuestions.setRenalIllness("0");
            }
            if (lists.get(1).isChoosed()) {
                postQuestions.setEndocrineIllness("1");
            } else {
                postQuestions.setEndocrineIllness("0");
            }
            if (lists.get(2).isChoosed()) {
                postQuestions.setPrrs("1");
            } else {
                postQuestions.setPrrs("0");
            }
            if (lists.get(3).isChoosed()) {
                postQuestions.setDrugInduced("1");
            } else {
                postQuestions.setDrugInduced("0");
            }
        } else if (fragment instanceof RiskFragment3) {//点击完成按钮
            if (lists.get(0).isChoosed()) {
                postQuestions.setKidney("1");
            } else {
                postQuestions.setKidney("0");
            }
            if (lists.get(1).isChoosed()) {
                postQuestions.setHeart("1");
            } else {
                postQuestions.setHeart("0");
            }
            if (lists.get(2).isChoosed()) {
                postQuestions.setEncephalon("1");
            } else {
                postQuestions.setEncephalon("0");
            }
            if (lists.get(3).isChoosed()) {
                postQuestions.setEye("1");
            } else {
                postQuestions.setEye("0");
            }
            postUserAnswers();
        }
    }

    private void postUserAnswers() {
        String userId = MyApplication.getInstance().userId;
        postQuestions.setUserId(Integer.parseInt(userId));
        final String result = new Gson().toJson(postQuestions);
        Log.e("最后的解析结果", "postUserAnswers: " + result);
        OkGo.<String>post(NetworkApi.Hypertension_Detection)
                .upJson(result)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.e("传递数据成功", "onSuccess: " + response.body());
                        try {
                            JSONObject object=new JSONObject(response.body());
                            if (object.optInt("code") == 200) {
                                JSONObject data = object.optJSONObject("data");
                                if (data.optString("illnessType").equals("0")) {//原发性高血压
                                    EssentialHypertension essentialHypertension = new Gson().fromJson(data.toString(), EssentialHypertension.class);
                                    startActivity(new Intent(RiskActivity.this,PrimaryBloodPressureRiskResultsActivity.class)
                                    .putExtra("data",essentialHypertension));
                                }else if (data.optString("illnessType").equals("1")){//继发性高血压
                                    SecondaryHypertension secondaryHypertension = new Gson().fromJson(data.toString(), SecondaryHypertension.class);
                                    startActivity(new Intent(RiskActivity.this,SecondaryBloodPressureRiskResultsActivity.class)
                                    .putExtra("data",secondaryHypertension));
                                }
                                finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            finish();
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        Log.e("传递数据异常", "onError: " + response.message());
                    }
                });
    }

    @Override
    protected void backLastActivity() {
        switch (fragmentPosition) {
            case 1:
                finish();
                break;
            case 2:
                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.fragment_slide_right_enter,R.anim.fragment_slide_right_exit)
                        .add(R.id.risk_frame, riskFragment1).commit();
                fragmentPosition = 1;
                break;
            case 3:
                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.fragment_slide_right_enter,R.anim.fragment_slide_right_exit)
                        .replace(R.id.risk_frame, riskFragment2).commit();
                fragmentPosition = 2;
                break;
        }
    }
}
