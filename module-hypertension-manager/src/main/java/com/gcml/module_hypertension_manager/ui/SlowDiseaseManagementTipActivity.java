package com.gcml.module_hypertension_manager.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.gcml.common.data.AppManager;
import com.gcml.common.router.AppRouter;
import com.gcml.common.utils.UM;
import com.gcml.common.utils.base.ToolbarBaseActivity;
import com.gcml.module_hypertension_manager.R;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.sjtu.yifei.annotation.Route;
import com.sjtu.yifei.route.Routerfit;

@Route(path = "/app/slow/disease/management/tip")
public class SlowDiseaseManagementTipActivity extends ToolbarBaseActivity {
    public static final String CONTENT = "为给您提供更好的健康方案，在方案制定过程中，" +
            "请您根据提示测量血压、血脂、血糖、身高、体重、以及腰围。若咨询的内容非实际情况，" +
            "或缺少设备，会影响到方案的精确性。如需购买设备，请前往商城，如已有设备，请直接点击下一步。";
    TextView tvContent;
    TextView tvNextStep;
    TextView tvToMall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slow_disease_management_tip);
        tvContent=findViewById(R.id.tv_content);
        tvNextStep=findViewById(R.id.tv_next_step);
        tvNextStep.setOnClickListener(this);
        tvToMall=findViewById(R.id.tv_to_mall);
        tvToMall.setOnClickListener(this);
        initTitle();
        MLVoiceSynthetize.startSynthesize(UM.getApp(),"小E来帮您控制血压吧。");
        AppManager.getAppManager().addActivity(this);
    }

    private void initTitle() {
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("健 康 管 理");
        mRightText.setVisibility(View.GONE);
        tvContent.setText(CONTENT);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int i = v.getId();
        if (i == R.id.tv_next_step) {
            Routerfit.register(AppRouter.class).skipBasicInformationActivity("tipHealthManage");

        } else if (i == R.id.tv_to_mall) {
            Routerfit.register(AppRouter.class).skipMarketActivity();

        }
    }
}
