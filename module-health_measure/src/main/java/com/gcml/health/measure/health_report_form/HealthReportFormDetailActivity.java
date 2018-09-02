package com.gcml.health.measure.health_report_form;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.gcml.health.measure.R;
import com.gcml.health.measure.cc.CCAppActions;
import com.gcml.lib_utils.UtilsManager;
import com.gcml.lib_utils.base.ToolbarBaseActivity;
import com.iflytek.synthetize.MLVoiceSynthetize;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/8/29 13:56
 * created by:gzq
 * description:TODO
 */
public class HealthReportFormDetailActivity extends ToolbarBaseActivity {
    public static final String KEY_RESULT = "key_result";
    public static final String KEY_ADVICE = "key_advice";
    /**
     * 根据您提供的相关病史结果，您已经患有糖尿
     */
    private TextView mTvTitle;
    /**
     * 指导建议
     */
    private TextView mTvHeadlineInfluencingFactors;
    private TextView mView1;
    /**
     * 病程较长、控制较差的糖尿病人常伴有各种并发症，如多种感染、酮症酸中毒、肾脏病变、眼底病变、神经病变等。请密切监测血糖，积极治疗，同时注意保持健康的生活方式如均衡饮食、控制每日摄入总热量、适量运动等。
     */
    private TextView mTvAdvice;

    public static void startActivity(Context context, String result, String advice) {
        Intent intent = new Intent(context, HealthReportFormDetailActivity.class);
        intent.putExtra(KEY_RESULT, result);
        intent.putExtra(KEY_ADVICE, advice);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.health_measure_activity_report_form_detail);
        initView();
        setData();
    }

    private void setData() {
        String result= getIntent().getStringExtra(KEY_RESULT);
        String advice = getIntent().getStringExtra(KEY_ADVICE);
        mTvTitle.setText(result);
        mTvAdvice.setText(advice);
        MLVoiceSynthetize.startSynthesize(UtilsManager.getApplication(),result+"。指导建议，"+advice,false);
    }

    @Override
    protected void backMainActivity() {
        CCAppActions.jump2MainActivity();
    }

    private void initView() {
        mTvTitle = (TextView) findViewById(R.id.tv_title);
        mTvHeadlineInfluencingFactors = (TextView) findViewById(R.id.tv_headline_influencing_factors);
        mView1 = (TextView) findViewById(R.id.view_1);
        mTvAdvice = (TextView) findViewById(R.id.tv_advice);
        mTitleText.setText("评 估 结 果");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MLVoiceSynthetize.destory();
    }
}
