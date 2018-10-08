package com.example.han.referralproject.tcm;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.billy.cc.core.component.IComponentCallback;
import com.example.han.referralproject.R;
import com.example.han.referralproject.bean.DiseaseUser;
import com.example.han.referralproject.homepage.MainActivity;
import com.example.han.referralproject.physicalexamination.activity.ChineseMedicineMonitorActivity;
import com.example.han.referralproject.tcm.activity.OlderHealthManagementSerciveActivity;
import com.example.han.referralproject.util.LocalShared;
import com.gcml.common.widget.toolbar.ToolBarClickListener;
import com.gcml.common.widget.toolbar.TranslucentToolBar;
import com.google.gson.Gson;
import com.iflytek.synthetize.MLVoiceSynthetize;

public class SymptomCheckActivity extends AppCompatActivity implements View.OnClickListener {


    private TranslucentToolBar mTbTitle;
    private ImageView mIvSymptomCheck;
    private ImageView mIvChineConstitution;
    private ImageView mIvRisk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_symptom_check);
        initView();
    }

    private void initView() {
        mTbTitle = (TranslucentToolBar) findViewById(R.id.tb_title);
        mIvSymptomCheck = (ImageView) findViewById(R.id.iv_symptom_check);
        mIvSymptomCheck.setOnClickListener(this);

        mIvChineConstitution = (ImageView) findViewById(R.id.iv_chine_constitution);
        mIvChineConstitution.setOnClickListener(this);

        mIvRisk = (ImageView) findViewById(R.id.iv_risk_assessment);
        mIvRisk.setOnClickListener(this);

        mTbTitle.setData("健 康 自 查", R.drawable.common_icon_back, "返回", R.drawable.icon_home, null, new ToolBarClickListener() {
            @Override
            public void onLeftClick() {
                finish();
            }

            @Override
            public void onRightClick() {
                startActivity(new Intent(SymptomCheckActivity.this, MainActivity.class));
            }
        });

        MLVoiceSynthetize.startSynthesize(getApplicationContext(), "主人,欢迎来到健康自查");
    }

    @Override

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_symptom_check:
                toSymptom();
                break;
            case R.id.iv_chine_constitution:
                toChineseConsititution();
                break;
            case R.id.iv_risk_assessment:
                toRisk();
                break;
        }
    }

    private void toRisk() {
        CC.obtainBuilder("health_measure")
                .setActionName("To_HealthInquiryActivity")
                .build()
                .call();
    }

    private void toChineseConsititution() {
        startActivity(new Intent(this, OlderHealthManagementSerciveActivity.class));
    }

    private void toSymptom() {
        DiseaseUser diseaseUser = new DiseaseUser(
                LocalShared.getInstance(this).getUserName(),
                LocalShared.getInstance(this).getSex().equals("男") ? 1 : 2,
                Integer.parseInt(LocalShared.getInstance(this).getUserAge()) * 12,
                LocalShared.getInstance(this).getUserPhoto()
        );
        String currentUser = new Gson().toJson(diseaseUser);
        Intent intent = new Intent(this, com.witspring.unitbody.ChooseMemberActivity.class);
        intent.putExtra("currentUser", currentUser);
        startActivity(intent);
    }


}
