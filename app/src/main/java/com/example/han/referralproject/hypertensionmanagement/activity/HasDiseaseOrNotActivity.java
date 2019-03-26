package com.example.han.referralproject.hypertensionmanagement.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.activity.WifiConnectActivity;
import com.example.han.referralproject.health_manager_program.TreatmentPlanActivity;
import com.example.han.referralproject.hypertensionmanagement.fragment.MultipleChoiceStringFragment;
import com.gcml.common.data.AppManager;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.util.LocalShared;
import com.gcml.common.utils.display.ToastUtils;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.json.JSONException;
import org.json.JSONObject;

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
        mlSpeak("您是否存在以下疾病：肾脏疾病，内分泌疾病，心血管疾病，颅脑病变？");
        AppManager.getAppManager().addActivity(this);
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
        mTitleText.setText("健 康 调 查");
        mRightText.setVisibility(View.GONE);
//        mRightView.setImageResource(R.drawable.white_wifi_3);
//        mRightView.setOnClickListener(v -> startActivity(new Intent(HasDiseaseOrNotActivity.this, WifiConnectActivity.class)));
    }

    @Override
    public void onNextStep(int[] checked) {
        if ("是".equals(itmes[checked[0]])) {
            postTargetState("1");
        } else {
            //跳转问卷
            postTargetState("0");
        }
    }

    private void toSolution() {
        startActivity(new Intent(this, TreatmentPlanActivity.class));
    }

    /**
     * 更新靶细胞
     *
     * @param state
     */
    private void postTargetState(String state) {
        NetworkApi.postTargetHypertension(state, LocalShared.getInstance(this).getUserId(), new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                String body = response.body();
                try {
                    JSONObject object = new JSONObject(body);
                    if (object.optBoolean("tag")) {
                        if ("0".equals(state)) {
                            startActivity(new Intent(HasDiseaseOrNotActivity.this, HypertensionActivity.class));
                        } else if ("1".equals(state)) {
                            toSolution();
                        }
                        toSolution();
                    } else {
                        ToastUtils.showShort(object.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

    }
}
